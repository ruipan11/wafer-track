package com.wafertrack.service;

import com.wafertrack.domain.Lot;
import com.wafertrack.domain.LotHold;
import com.wafertrack.repository.LotHoldRepository;
import com.wafertrack.repository.LotRepository;
import com.wafertrack.service.exception.NotFoundException;
import com.wafertrack.service.exception.TrackException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class HoldService {

    private static final Set<String> VALID_REASON_CODES =
            Set.of("QUALITY", "ENGINEERING", "EQUIPMENT", "MATERIAL");
    
    private static final String LOT_HOLD    = "HOLD";
    private static final String LOT_WAITING = "WAITING";

    private final LotRepository lotRepository;
    private final LotHoldRepository lotHoldRepository;

    public HoldService(LotRepository lotRepository,
                       LotHoldRepository lotHoldRepository) {
        this.lotRepository = lotRepository;
        this.lotHoldRepository = lotHoldRepository;
    }

    @Transactional
    public LotHold hold(String lotNo, String reasonCode, String comment, String operator) {
        Lot lot = lotRepository.findByLotNo(lotNo)
            .orElseThrow(() -> new NotFoundException("Lot not found: " + lotNo));

        if (!VALID_REASON_CODES.contains(reasonCode)){
            throw new TrackException(
                        "Invalid hold reason code: " + reasonCode
                                + ", must be one of " + VALID_REASON_CODES);
        }

        // 建立新的 hold 紀錄
        LotHold hold = new LotHold();
        hold.setLotId(lot.getId());
        hold.setHoldReasonCode(reasonCode);
        hold.setHoldComment(comment);
        hold.setHoldStepSeq(lot.getCurrentStepSeq()); // 凍結所在步驟
        hold.setHoldBy(operator);
        hold.setHoldTime(LocalDateTime.now());
        lotHoldRepository.save(hold);

        lot.setStatus(LOT_HOLD);
        lotRepository.save(lot);

        return hold;
    }
    @Transactional
    public int release(String lotNo, String comment, String operator) {
        Lot lot = lotRepository.findByLotNo(lotNo)
            .orElseThrow(() -> new NotFoundException("Lot not found: " + lotNo));

        List<LotHold> activeHolds =
                lotHoldRepository.findByLotIdAndReleaseTimeIsNull(lot.getId());

        if (activeHolds.isEmpty()) {
            throw new TrackException("Lot " + lotNo + " is not on hold");
        }

        LocalDateTime now = LocalDateTime.now();
        for (LotHold hold : activeHolds) {
            hold.setReleaseBy(operator);
            hold.setReleaseTime(now);
            hold.setReleaseComment(comment);
        }
        lotHoldRepository.saveAll(activeHolds);

        lot.setStatus(LOT_WAITING);
        lotRepository.save(lot);

        return activeHolds.size();
    }


}