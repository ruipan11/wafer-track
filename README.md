# Wafer Track

晶圓生產製造系統核心模組，實作 Lot 過站追蹤、可回流流程檢核與 HOLD 管理。

## 技術架構

Java 17 / Spring Boot 3.3 / MySQL 8 / Docker Compose

## 快速啟動

```bash
# 複製並填入資料庫密碼
cp .env.example .env

# 啟動容器服務
docker compose up -d --build
```

啟動後：

- 健康檢查：http://localhost:8080/api/health
- API 文件：http://localhost:8080/swagger-ui.html

## 設計說明

- Schema 由 SQL migration 管理，避免資料表結構非預期變更。
- 資料庫連線採用一般帳號而非 root 帳號，以符合最小權限原則。
- MySQL 對外連線埠（Port）使用 3007。
