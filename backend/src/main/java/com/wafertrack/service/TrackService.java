package com.wafertrack.service;

import com.wafertrack.domain.Equipment;
import com.wafertrack.domain.Lot;
import com.wafertrack.domain.LotHistory;
import com.wafertrack.domain.RouteStep;
import com.wafertrack.repository.EquipmentRepository;
import com.wafertrack.repository.LotHistoryRepository;
import com.wafertrack.repository.LotRepository;
import com.wafertrack.repository.RouteStepRepository;
import com.wafertrack.service.exception.NotFoundException;
import com.wafertrack.service.exception.TrackException;
import com.wafertrack.repository.LotHoldRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TrackService {
    private static final String LOT_WAITING    = "WAITING";
    private static final String LOT_PROCESSING = "PROCESSING";
    private static final String LOT_FINISHED   = "FINISHED";
    private static final String EQP_IDLE       = "IDLE";
    private static final String EQP_RUN        = "RUN";

    private final LotRepository lotRepository;
    private final RouteStepRepository routeStepRepository;
    private final EquipmentRepository equipmentRepository;
    private final LotHistoryRepository lotHistoryRepository;
    private final LotHoldRepository lotHoldRepository;

    public TrackService(LotRepository lotRepository,
                        RouteStepRepository routeStepRepository,
                        EquipmentRepository equipmentRepository,
                        LotHistoryRepository lotHistoryRepository,
                        LotHoldRepository lotHoldRepository) {
        this.lotRepository = lotRepository;
        this.routeStepRepository = routeStepRepository;
        this.equipmentRepository = equipmentRepository;
        this.lotHistoryRepository = lotHistoryRepository;
        this.lotHoldRepository = lotHoldRepository;
    }
    @Transactional
    public LotHistory trackIn(String lotNo, String eqpCode, String operator) {

        Lot lot = lotRepository.findByLotNo(lotNo)
                .orElseThrow(() -> new NotFoundException("Lot not found: " + lotNo));

        if (lotHoldRepository.existsByLotIdAndReleaseTimeIsNull(lot.getId())) {
            throw new TrackException("Lot " + lotNo + " is on hold and cannot be tracked in");
        }

        Equipment eqp = equipmentRepository.findByEqpCode(eqpCode)
                .orElseThrow(() -> new NotFoundException("Equipment not found: " + eqpCode));

        // 檢核 1：lot 狀態必須是 WAITING
        if (!LOT_WAITING.equals(lot.getStatus())) {
            throw new TrackException(
                    "Lot " + lotNo + " is not ready for track-in, current status: " + lot.getStatus());
        }

        // lot 必須已經在某個步驟上
        if (lot.getCurrentStepSeq() == null) {
            throw new TrackException("Lot " + lotNo + " has no current step assigned");
        }

        RouteStep step = routeStepRepository
                .findByRouteIdAndStepSeq(lot.getRouteId(), lot.getCurrentStepSeq())
                .orElseThrow(() -> new NotFoundException(
                        "Route step not found: step " + lot.getCurrentStepSeq()));

        // 檢核 2：機台所屬工站必須符合此步驟
        if (!step.getWorkstationId().equals(eqp.getWorkstationId())) {
            throw new TrackException(
                    "Equipment " + eqpCode + " does not belong to the workstation required by step "
                            + step.getStepSeq() + " (" + step.getStepName() + ")");
        }

        // 檢核 3：機台狀態必須是 IDLE
        if (!EQP_IDLE.equals(eqp.getStatus())) {
            throw new TrackException(
                    "Equipment " + eqpCode + " is not available, current status: " + eqp.getStatus());
        }

        // 檢核 4：不能有尚未 track-out 的紀錄
        lotHistoryRepository.findByLotIdAndTrackOutTimeIsNull(lot.getId())
                .ifPresent(open -> {
                    throw new TrackException(
                            "Lot " + lotNo + " already tracked in at step " + open.getStepSeq());
                });

        LotHistory history = new LotHistory();
        history.setLotId(lot.getId());
        history.setStepSeq(step.getStepSeq());
        history.setStepName(step.getStepName());
        history.setWorkstationId(step.getWorkstationId());
        history.setEquipmentId(eqp.getId());
        history.setTrackInTime(LocalDateTime.now());
        history.setTrackInQty(lot.getQty());
        history.setTrackInOperator(operator);
        history.setScrapQty(0);
        lotHistoryRepository.save(history);

        lot.setStatus(LOT_PROCESSING);
        lotRepository.save(lot);

        eqp.setStatus(EQP_RUN);
        equipmentRepository.save(eqp);

        return history;
    }
    @Transactional
    public LotHistory trackOut(String lotNo, String eqpCode, String operator,
                               Integer outQty, String defectCode) {

        Lot lot = lotRepository.findByLotNo(lotNo)
                .orElseThrow(() -> new NotFoundException("Lot not found: " + lotNo));

        Equipment eqp = equipmentRepository.findByEqpCode(eqpCode)
                .orElseThrow(() -> new NotFoundException("Equipment not found: " + eqpCode));

        if (!LOT_PROCESSING.equals(lot.getStatus())) {
            throw new TrackException(
                    "Lot " + lotNo + " is not processing, current status: " + lot.getStatus());
        }

        LotHistory history = lotHistoryRepository
                .findByLotIdAndTrackOutTimeIsNull(lot.getId())
                .orElseThrow(() -> new TrackException(
                        "No open track-in record found for lot " + lotNo));

        // 必須由同一台機台 track-out
        if (!history.getEquipmentId().equals(eqp.getId())) {
            throw new TrackException(
                    "Lot " + lotNo + " was tracked in on a different equipment");
        }

        // 出站數量不得大於進站數量
        if (outQty > history.getTrackInQty()) {
            throw new TrackException(
                    "Track-out quantity " + outQty + " exceeds track-in quantity "
                            + history.getTrackInQty());
        }

        int scrap = history.getTrackInQty() - outQty;

        // 有報廢就必須填原因碼
        if (scrap > 0 && (defectCode == null || defectCode.isBlank())) {
            throw new TrackException(
                    "Defect code is required when scrapping " + scrap + " wafer(s)");
        }

        history.setTrackOutTime(LocalDateTime.now());
        history.setTrackOutQty(outQty);
        history.setTrackOutOperator(operator);
        history.setScrapQty(scrap);
        history.setDefectCode(scrap > 0 ? defectCode : null);
        lotHistoryRepository.save(history);

        Integer nextStepSeq = routeStepRepository
                .findFirstByRouteIdAndStepSeqGreaterThanOrderByStepSeqAsc(
                        lot.getRouteId(), lot.getCurrentStepSeq())
                .map(RouteStep::getStepSeq)
                .orElse(null);

        lot.setQty(outQty);
        if (nextStepSeq == null) {
            lot.setStatus(LOT_FINISHED);
        } else {
            lot.setCurrentStepSeq(nextStepSeq);
            lot.setStatus(LOT_WAITING);
        }
        lotRepository.save(lot);

        eqp.setStatus(EQP_IDLE);
        equipmentRepository.save(eqp);

        return history;
    }
}