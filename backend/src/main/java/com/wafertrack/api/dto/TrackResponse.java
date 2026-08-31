package com.wafertrack.api.dto;

import java.time.LocalDateTime;

public class TrackResponse {

    private String lotNo;
    private Integer stepSeq;
    private String stepName;
    private String eqpCode;
    private LocalDateTime trackInTime;
    private LocalDateTime trackOutTime;
    private Integer qty;
    private Integer scrapQty;
    private String lotStatus;
    private Integer nextStepSeq;

    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }

    public Integer getStepSeq() { return stepSeq; }
    public void setStepSeq(Integer stepSeq) { this.stepSeq = stepSeq; }

    public String getStepName() { return stepName; }
    public void setStepName(String stepName) { this.stepName = stepName; }

    public String getEqpCode() { return eqpCode; }
    public void setEqpCode(String eqpCode) { this.eqpCode = eqpCode; }

    public LocalDateTime getTrackInTime() { return trackInTime; }
    public void setTrackInTime(LocalDateTime trackInTime) { this.trackInTime = trackInTime; }

    public LocalDateTime getTrackOutTime() { return trackOutTime; }
    public void setTrackOutTime(LocalDateTime trackOutTime) { this.trackOutTime = trackOutTime; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Integer getScrapQty() { return scrapQty; }
    public void setScrapQty(Integer scrapQty) { this.scrapQty = scrapQty; }

    public String getLotStatus() { return lotStatus; }
    public void setLotStatus(String lotStatus) { this.lotStatus = lotStatus; }

    public Integer getNextStepSeq() { return nextStepSeq; }
    public void setNextStepSeq(Integer nextStepSeq) { this.nextStepSeq = nextStepSeq; }
}