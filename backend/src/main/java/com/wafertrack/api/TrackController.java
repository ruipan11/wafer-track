package com.wafertrack.api;

import com.wafertrack.api.dto.TrackInRequest;
import com.wafertrack.api.dto.TrackOutRequest;
import com.wafertrack.api.dto.TrackResponse;
import com.wafertrack.domain.Lot;
import com.wafertrack.domain.LotHistory;
import com.wafertrack.repository.LotRepository;
import com.wafertrack.service.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/track")
@Tag(name = "Track", description = "過站作業")
public class TrackController {

    private final TrackService trackService;
    private final LotRepository lotRepository;

    public TrackController(TrackService trackService, LotRepository lotRepository) {
        this.trackService = trackService;
        this.lotRepository = lotRepository;
    }

    @PostMapping("/in")
    @Operation(summary = "進站", description = "將批號上機，需通過四項過站檢核")
    public TrackResponse trackIn(@RequestBody TrackInRequest request) {
        LotHistory history = trackService.trackIn(
                request.getLotNo(), request.getEqpCode(), request.getOperator());
        return toResponse(history, request.getLotNo(), request.getEqpCode());
    }

    @PostMapping("/out")
    @Operation(summary = "出站", description = "批號下機並推進至下一步驟")
    public TrackResponse trackOut(@RequestBody TrackOutRequest request) {
        LotHistory history = trackService.trackOut(
                request.getLotNo(), request.getEqpCode(), request.getOperator(),
                request.getOutQty(), request.getDefectCode());
        return toResponse(history, request.getLotNo(), request.getEqpCode());
    }

    private TrackResponse toResponse(LotHistory history, String lotNo, String eqpCode) {
        Lot lot = lotRepository.findByLotNo(lotNo).orElseThrow();

        TrackResponse response = new TrackResponse();
        response.setLotNo(lotNo);
        response.setStepSeq(history.getStepSeq());
        response.setStepName(history.getStepName());
        response.setEqpCode(eqpCode);
        response.setTrackInTime(history.getTrackInTime());
        response.setTrackOutTime(history.getTrackOutTime());
        response.setScrapQty(history.getScrapQty());
        response.setQty(lot.getQty());
        response.setLotStatus(lot.getStatus());
        response.setNextStepSeq(lot.getCurrentStepSeq());
        return response;
    }
}