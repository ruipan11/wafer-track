package com.wafertrack.api.dto;

import java.time.LocalDateTime;

public class HoldResponse {

    private String lotNo;
    private String lotStatus;
    private String reasonCode;
    private Integer holdStepSeq;
    private LocalDateTime holdTime;
    private LocalDateTime releaseTime;
    private Integer releasedCount;

    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }

    public String getLotStatus() { return lotStatus; }
    public void setLotStatus(String lotStatus) { this.lotStatus = lotStatus; }

    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }

    public Integer getHoldStepSeq() { return holdStepSeq; }
    public void setHoldStepSeq(Integer holdStepSeq) { this.holdStepSeq = holdStepSeq; }

    public LocalDateTime getHoldTime() { return holdTime; }
    public void setHoldTime(LocalDateTime holdTime) { this.holdTime = holdTime; }

    public LocalDateTime getReleaseTime() { return releaseTime; }
    public void setReleaseTime(LocalDateTime releaseTime) { this.releaseTime = releaseTime; }

    public Integer getReleasedCount() { return releasedCount; }
    public void setReleasedCount(Integer releasedCount) { this.releasedCount = releasedCount; }


}