package com.wafertrack.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "lot_hold")
public class LotHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_id", nullable = false)
    private Long lotId;

    @Column(name = "hold_reason_code", nullable = false, length = 32)
    private String holdReasonCode;

    @Column(name = "hold_comment", length = 255)
    private String holdComment;

    @Column(name = "hold_step_seq")
    private Integer holdStepSeq;

    @Column(name = "hold_by", nullable = false, length = 32)
    private String holdBy;

    @Column(name = "hold_time", nullable = false)
    private LocalDateTime holdTime;

    @Column(name = "release_by", length = 32)
    private String releaseBy;

    @Column(name = "release_time")
    private LocalDateTime releaseTime;

    @Column(name = "release_comment", length = 255)
    private String releaseComment;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) {this.id = id; }

    public Long getLotId() { return lotId; }
    public void setLotId(Long lotId) { this.lotId = lotId;}

    public String getHoldReasonCode() { return holdReasonCode; }
    public void setHoldReasonCode(String holdReasonCode) { this.holdReasonCode = holdReasonCode; }

    public String getHoldComment() { return holdComment; }
    public void setHoldComment(String holdComment) { this.holdComment = holdComment; }

    public Integer getHoldStepSeq() { return holdStepSeq; }
    public void setHoldStepSeq(Integer holdStepSeq) { this.holdStepSeq = holdStepSeq; }

    public String getHoldBy() { return holdBy; }
    public void setHoldBy(String holdBy) { this.holdBy = holdBy; }

    public LocalDateTime getHoldTime() { return holdTime; }
    public void setHoldTime(LocalDateTime holdTime) { this.holdTime = holdTime; }

    public String getReleaseBy() { return releaseBy; }
    public void setReleaseBy(String releaseBy) { this.releaseBy = releaseBy; }

    public LocalDateTime getReleaseTime() { return releaseTime; }
    public void setReleaseTime(LocalDateTime releaseTime) { this.releaseTime = releaseTime; }

    public String getReleaseComment() { return releaseComment; }
    public void setReleaseComment(String releaseComment) { this.releaseComment = releaseComment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}