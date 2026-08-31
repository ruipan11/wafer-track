package com.wafertrack.api;

import com.wafertrack.api.dto.LotStatusResponse;
import com.wafertrack.service.LotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lots")
@Tag(name = "Lot", description = "批號查詢")
public class LotController {

    private final LotService lotService;

    public LotController(LotService lotService) {
        this.lotService = lotService;
    }

    @GetMapping("/{lotNo}")
    @Operation(summary = "查詢批號現況", description = "回傳批號目前所在步驟、狀態與片數")
    public LotStatusResponse getLotStatus(@PathVariable String lotNo) {
        return lotService.getLotStatus(lotNo);
    }
}