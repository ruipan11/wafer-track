#!/bin/bash
# Wafer Track 功能展示腳本
# 用法：先啟動服務（docker compose up -d 與 mvn spring-boot:run），再執行 ./demo.sh

BASE="http://localhost:8080/api"
LOT="LOT-20260801-001"

step() {
    echo ""
    echo "=============================================================="
    echo "$1"
    echo "=============================================================="
}

post() {
    curl -s -X POST "$BASE$1" -H "Content-Type: application/json" -d "$2"
    echo ""
}

get() {
    curl -s "$BASE$1"
    echo ""
}

step "0. 系統健康檢查"
get "/health"

step "1. 查詢批號現況（應在 step 10，狀態 WAITING）"
get "/lots/$LOT"

step "2. 跳站測試：批號在 step 10（CLEAN 站），卻想上 LITHO 機台"
echo "預期：400，機台不屬於此步驟所需工站"
post "/track/in" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-LITHO-01\",\"operator\":\"OP001\"}"

step "3. 正常進站：step 10 用 CLEAN 站機台"
post "/track/in" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-CLEAN-01\",\"operator\":\"OP001\"}"

step "4. 重複進站測試"
echo "預期：400，批號已在加工中"
post "/track/in" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-CLEAN-01\",\"operator\":\"OP001\"}"

step "5. 出站：推進至 step 20"
post "/track/out" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-CLEAN-01\",\"operator\":\"OP001\",\"outQty\":25}"

step "6. 連續跑完 step 20 至 step 40"
post "/track/in"  "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-FURN-01\",\"operator\":\"OP001\"}"
post "/track/out" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-FURN-01\",\"operator\":\"OP001\",\"outQty\":25}"
post "/track/in"  "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-LITHO-01\",\"operator\":\"OP001\"}"
post "/track/out" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-LITHO-01\",\"operator\":\"OP001\",\"outQty\":25}"
post "/track/in"  "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-ETCH-01\",\"operator\":\"OP001\"}"

step "7. 報廢測試：出站 23 片但未填不良代碼"
echo "預期：400，報廢必須填寫不良代碼"
post "/track/out" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-ETCH-01\",\"operator\":\"OP001\",\"outQty\":23}"

step "8. 補上不良代碼後出站（推進至 step 50）"
post "/track/out" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-ETCH-01\",\"operator\":\"OP001\",\"outQty\":23,\"defectCode\":\"PARTICLE\"}"

step "9. 回流驗證：step 50 是 CLEAN 站的第二次"
echo "系統以 step_seq 判斷，不因該工站曾經走過而阻擋"
get "/lots/$LOT"

step "10. 同工站多機台：CLEAN 站有兩台，改用 EQP-CLEAN-02"
echo "檢核比對的是工站而非特定機台，現場可依機台空閒狀況調度"
post "/track/in" "{\"lotNo\":\"$LOT\",\"eqpCode\":\"EQP-CLEAN-02\",\"operator\":\"OP001\"}"

step "11. 凍結：品質異常"
post "/hold" "{\"lotNo\":\"LOT-20260801-002\",\"reasonCode\":\"QUALITY\",\"comment\":\"膜厚超出管制界限\",\"operator\":\"QC001\"}"

step "12. 多重凍結：設備部門另行凍結同一批號"
post "/hold" "{\"lotNo\":\"LOT-20260801-002\",\"reasonCode\":\"EQUIPMENT\",\"comment\":\"機台參數異常需回溯\",\"operator\":\"EE002\"}"

step "13. 凍結中嘗試進站"
echo "預期：400，批號處於凍結狀態"
post "/track/in" "{\"lotNo\":\"LOT-20260801-002\",\"eqpCode\":\"EQP-CLEAN-01\",\"operator\":\"OP001\"}"

step "14. 無效原因碼測試"
echo "預期：400，原因碼須為四種合法值之一"
post "/hold" "{\"lotNo\":\"LOT-20260801-002\",\"reasonCode\":\"UNKNOWN\",\"comment\":\"test\",\"operator\":\"OP001\"}"

step "15. 放行：兩筆凍結一次解除"
echo "預期：releasedCount 為 2"
post "/hold/release" "{\"lotNo\":\"LOT-20260801-002\",\"comment\":\"工程確認允收\",\"operator\":\"PE003\"}"

step "16. 查無此批號"
echo "預期：404"
get "/lots/LOT-NOT-EXIST"

step "17. 完整過站履歷（直接查資料庫）"
docker exec wafertrack-mysql mysql -uwt -pwt123456 wafertrack -N -e "
SELECT h.step_seq, h.step_name, e.eqp_code, h.track_in_qty, h.track_out_qty, h.scrap_qty, IFNULL(h.defect_code,'-')
FROM lot_history h
JOIN lot l ON l.id = h.lot_id
JOIN equipment e ON e.id = h.equipment_id
WHERE l.lot_no = '$LOT'
ORDER BY h.track_in_time;" 2>/dev/null

echo ""
echo "=============================================================="
echo "展示結束"
echo "=============================================================="