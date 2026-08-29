package com.wafertrack.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "System", description = "系統狀態")
public class HealthController {
    //依賴注入
    private final DataSource dataSource;

    public HealthController(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @GetMapping("/health")
    @Operation(summary = "健康檢查", description = "回傳服務與資料庫連線狀態")
    public Map<String, Object> health() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("application", "wafer-track");
        result.put("version", "0.1.0");
        result.put("timestamp", LocalDateTime.now().toString());
        result.put("database", checkDatabase());
        return result;
    } 

    private String checkDatabase() {
        try (Connection conn = dataSource.getConnection()){
            return conn.isValid(2) ? "UP" : "DOWN";
        } catch (Exception e){
            return "DOWN: " + e.getMessage();
        }
    }
}