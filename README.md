# Wafer Track

半導體晶圓廠情境的生產執行系統（MES）核心模組，實作 lot 過站追蹤、可回流途程檢核與 Hold 管理。

**Java 17 / Spring Boot 3.3 / MySQL 8 / Flyway / Docker Compose**

---

## 快速啟動

```bash
cp .env.example .env
docker compose up -d
cd backend && mvn spring-boot:run
```

Flyway 在啟動時自動建表並載入測試資料。

- Swagger：http://localhost:8080/swagger-ui.html
- 功能展示：`./demo.sh`

---

## 核心概念：可回流途程

晶片是多層結構，每層都要重跑一輪微影與蝕刻，同一個工站在一條途程裡會被走過多次。

| step_seq | 步驟            | 工站            | layer |
| -------- | --------------- | --------------- | ----- |
| 10       | Initial clean   | CLEAN           |       |
| 30       | Photo layer 1   | LITHO           | 1     |
| 40       | Etch layer 1    | ETCH            | 1     |
| 50       | Post-etch clean | **CLEAN** | 1     |
| 70       | Photo layer 2   | **LITHO** | 2     |
| 80       | Etch layer 2    | **ETCH**  | 2     |
| 90       | Post-etch clean | **CLEAN** | 2     |

因此**判斷批號能否進站必須比對步驟序號，不能問「這個工站來過沒有」**。
這是本系統與離散製造 MES 的根本差異。

資料層面的實作：唯一鍵為 `(route_id, step_seq)`，刻意不含 `workstation_id`。
若納入，每個工站在一條途程中只能出現一次，模型就退化成線性流程。

---

## 資料模型

| 表                                       | 職責             |
| ---------------------------------------- | ---------------- |
| `product` / `route` / `route_step` | 產品與途程定義   |
| `workstation` / `equipment`          | 工站與其下的機台 |
| `lot` / `wafer`                      | 批號與晶圓       |
| `lot_history`                          | 過站紀錄         |
| `lot_hold`                             | 凍結紀錄         |

批號與機台各有獨立狀態，過站時同步變更：

```
track-in :  lot WAITING → PROCESSING     equipment IDLE → RUN
track-out:  lot PROCESSING → WAITING     equipment RUN → IDLE
            current_step_seq 推進至下一步
```

---

## 架構

```
        HTTP 請求
            ↓
    ┌───────────────┐
    │  Controller   │  只處理 HTTP，不含業務判斷
    └───────┬───────┘
            ↓
    ┌───────────────┐
    │    Service    │  業務規則、狀態轉換、交易邊界
    └───────┬───────┘
            ↓
    ┌───────────────┐
    │  Repository   │  資料存取
    └───────┬───────┘
            ↓
    ┌───────────────┐
    │    Entity     │  資料表映射
    └───────┬───────┘
            ↓
          MySQL
```

過站檢核集中在 `TrackService`，可脫離 HTTP 層獨立測試；
未來若新增訊息佇列等入口，直接重用同一套邏輯。

Service 拋出語意明確的例外，`@RestControllerAdvice` 統一轉成 HTTP 狀態碼
（`NotFoundException` → 404、`TrackException` → 400），Controller 不需 try-catch。

---

## 設計要點

### Track-in 檢核鏈

五道檢核依序執行，任一項不通過即回傳 400 與可讀原因。

| # | 規則                             | 攔截情境                 |
| - | -------------------------------- | ------------------------ |
| 0 | 不得有未放行的 hold              | 凍結中的批號被誤操作     |
| 1 | 批號狀態為`WAITING`            | 重複進站                 |
| 2 | 目標步驟等於`current_step_seq` | **跳站**           |
| 3 | 機台所屬工站符合該步驟           | 上錯機台                 |
| 4 | 機台狀態為`IDLE`               | 在 DOWN 或 PM 的機台上貨 |

順序由「批號本身的問題」排到「機台的問題」，讓作業員先看到最根本的原因。
回流在第 2 項成立：系統只問「當前這一步該在哪個工站」，不問「這個工站是否走過」。

### 步驟推進

下一步以「大於當前序號的第一筆」查詢取得，而非固定加 10。
`step_seq` 採 10 遞增保留插入空間，日後在既有步驟間插入量測時無須修改程式碼。

### 過站紀錄的單筆設計

一次完整過站對應 `lot_history` 一筆：track-in 新增，track-out 補完。
因此 `track_out_time IS NULL` 即表示批號目前在機台上，
這是判斷 WIP 位置與 track-out 定位紀錄的依據。

### 多重 Hold

Release 不刪除紀錄，而是填入 `release_time`，判斷條件為 `release_time IS NULL`。
這使得多個單位可各自凍結同一批號，必須全部放行後批號才能繼續流動，
對應 fab 中品質與設備部門各有各的凍結理由的實際流程。

### 交易邊界

`trackIn`、`trackOut`、`hold`、`release` 皆標註 `@Transactional`。
以 track-in 為例，一次操作寫入 `lot_history`、`lot`、`equipment` 三張表，
必須同生共死。若僅寫入歷史而未更新批號狀態，
現場將出現「查得到過站紀錄但批號仍可再次進站」的不一致。

### Schema 管理

`ddl-auto: none`，結構完全由 Flyway 的 SQL 檔案管理，
一經執行即不可修改，變更一律新增版本檔，確保各環境 schema 一致且可回溯。

### 歷史凍結當時的事實

`lot_history.step_name` 是反正規化欄位，雖可由 `route_step` 查得仍複製一份。
若日後修改途程步驟名稱，查詢半年前的履歷應顯示當時的名稱。
主檔可變，歷史不可變。同樣的原則體現在 hold 以標記而非刪除實作 release。

---

## API

| 方法 | 路徑                  | 說明     |
| ---- | --------------------- | -------- |
| GET  | `/api/lots/{lotNo}` | 批號現況 |
| POST | `/api/track/in`     | 進站     |
| POST | `/api/track/out`    | 出站     |
| POST | `/api/hold`         | 凍結     |
| POST | `/api/hold/release` | 解除凍結 |

錯誤回應直接面向現場作業員：

```json
{
  "error": "TRACK_REJECTED",
  "message": "Equipment EQP-LITHO-01 does not belong to the workstation required by step 10 (Initial clean)"
}
```

---

## 測試資料

| 批號                 | 起始步驟 | 說明            |
| -------------------- | -------- | --------------- |
| `LOT-20260801-001` | 10       | 尚未開始        |
| `LOT-20260801-002` | 50       | CLEAN 站第 2 次 |
| `LOT-20260801-003` | 70       | LITHO 站第 2 次 |

機台狀態刻意包含非可用案例：`EQP-LITHO-02` 為 `PM`、`EQP-ETCH-02` 為 `DOWN`。

---

## 授權

MIT
