package com.wafertrack.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "lot_history")
public class LotHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_id", nullable = false)
    private Long lotId;

    @Column(name = "step_seq", nullable = false)
    private Integer stepSeq;

    @Column(name = "step_name", nullable = false, length = 64)
    private String stepName;

    @Column(name = "workstation_id", nullable = false)
    private Long workstationId;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "track_in_time", nullable = false)
    private LocalDateTime trackInTime;

    @Column(name = "track_in_qty", nullable = false)
    private Integer trackInQty;

    @Column(name = "track_in_operator", nullable = false, length = 32)
    private String trackInOperator;

    @Column(name = "track_out_time")
    private LocalDateTime trackOutTime;

    @Column(name = "track_out_qty")
    private Integer trackOutQty;

    @Column(name = "track_out_operator", length = 32)
    private String trackOutOperator;

    @Column(name = "scrap_qty", nullable = false)
    private Integer scrapQty = 0;

    @Column(name = "defect_code", length = 32)
    private String defectCode;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLotId() { return lotId; }
    public void setLotId(Long lotId) { this.lotId = lotId; }

    public Integer getStepSeq() { return stepSeq; }
    public void setStepSeq(Integer stepSeq) { this.stepSeq = stepSeq; }

    public String getStepName() { return stepName; }
    public void setStepName(String stepName) { this.stepName = stepName; }

    public Long getWorkstationId() { return workstationId; }
    public void setWorkstationId(Long workstationId) { this.workstationId = workstationId; }

    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }

    public LocalDateTime getTrackInTime() { return trackInTime; }
    public void setTrackInTime(LocalDateTime trackInTime) { this.trackInTime = trackInTime; }

    public Integer getTrackInQty() { return trackInQty; }
    public void setTrackInQty(Integer trackInQty) { this.trackInQty = trackInQty; }

    public String getTrackInOperator() { return trackInOperator; }
    public void setTrackInOperator(String trackInOperator) { this.trackInOperator = trackInOperator; }

    public LocalDateTime getTrackOutTime() { return trackOutTime; }
    public void setTrackOutTime(LocalDateTime trackOutTime) { this.trackOutTime = trackOutTime; }

    public Integer getTrackOutQty() { return trackOutQty; }
    public void setTrackOutQty(Integer trackOutQty) { this.trackOutQty = trackOutQty; }

    public String getTrackOutOperator() { return trackOutOperator; }
    public void setTrackOutOperator(String trackOutOperator) { this.trackOutOperator = trackOutOperator; }

    public Integer getScrapQty() { return scrapQty; }
    public void setScrapQty(Integer scrapQty) { this.scrapQty = scrapQty; }

    public String getDefectCode() { return defectCode; }
    public void setDefectCode(String defectCode) { this.defectCode = defectCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}