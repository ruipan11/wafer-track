package com.wafertrack.service;

import com.wafertrack.api.dto.LotStatusResponse;
import com.wafertrack.domain.Lot;
import com.wafertrack.domain.RouteStep;
import com.wafertrack.repository.LotRepository;
import com.wafertrack.repository.RouteStepRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wafertrack.service.exception.NotFoundException;

@Service
public class LotService {

    private final LotRepository lotRepository;
    private final RouteStepRepository routeStepRepository;

    public LotService(LotRepository lotRepository,
                      RouteStepRepository routeStepRepository) {
        this.lotRepository = lotRepository;
        this.routeStepRepository = routeStepRepository;
    }

    @Transactional(readOnly = true)
    public LotStatusResponse getLotStatus(String lotNo) {

        Lot lot = lotRepository.findByLotNo(lotNo)
                .orElseThrow(() -> new NotFoundException("Lot not found: " + lotNo));

        LotStatusResponse response = new LotStatusResponse();
        response.setLotNo(lot.getLotNo());
        response.setStatus(lot.getStatus());
        response.setQty(lot.getQty());
        response.setPriority(lot.getPriority());
        response.setCurrentStepSeq(lot.getCurrentStepSeq());

        if (lot.getCurrentStepSeq() != null) {
            routeStepRepository
                    .findByRouteIdAndStepSeq(lot.getRouteId(), lot.getCurrentStepSeq())
                    .ifPresent(step -> {
                        response.setCurrentStepName(step.getStepName());
                        response.setLayerNo(step.getLayerNo());
                    });
        }

        return response;
    }
}