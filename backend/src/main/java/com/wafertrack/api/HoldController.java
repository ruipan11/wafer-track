package com.wafertrack.api;

import com.wafertrack.api.dto.HoldRequest;
import com.wafertrack.api.dto.HoldResponse;
import com.wafertrack.api.dto.ReleaseRequest;
import com.wafertrack.domain.Lot;
import com.wafertrack.domain.LotHold;
import com.wafertrack.repository.LotRepository;
import com.wafertrack.service.HoldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/hold")
@Tag(name = "Hold", description = "批號凍結與放行")
public class HoldController {

    private final HoldService holdService;
    private final LotRepository lotRepository;

    public HoldController(HoldService holdService, LotRepository lotRepository) {
        this.holdService = holdService;
        this.lotRepository = lotRepository;
    }

    @PostMapping
    @Operation(summary = "凍結", description = "將批號設為 HOLD，禁止過站")
    public HoldResponse hold(@RequestBody HoldRequest request) {
        LotHold hold = holdService.hold(
                request.getLotNo(), request.getReasonCode(),
                request.getComment(), request.getOperator());

        HoldResponse response = new HoldResponse();
        response.setLotNo(request.getLotNo());
        response.setLotStatus(currentStatus(request.getLotNo()));
        response.setReasonCode(hold.getHoldReasonCode());
        response.setHoldStepSeq(hold.getHoldStepSeq());
        response.setHoldTime(hold.getHoldTime());
        return response;
    }
    
    @PostMapping("/release")
    @Operation(summary = "放行", description = "解除批號的所有凍結，恢復可過站狀態")
    public HoldResponse release(@RequestBody ReleaseRequest request) {
        int released = holdService.release(
                request.getLotNo(), request.getComment(), request.getOperator());

        HoldResponse response = new HoldResponse();
        response.setLotNo(request.getLotNo());
        response.setLotStatus(currentStatus(request.getLotNo()));
        response.setReleaseTime(LocalDateTime.now());
        response.setReleasedCount(released);
        return response;
    }

    private String currentStatus(String lotNo) {
        return lotRepository.findByLotNo(lotNo).map(Lot::getStatus).orElse(null);
    }
}