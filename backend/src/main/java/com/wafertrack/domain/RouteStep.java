package com.wafertrack.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_step")
public class RouteStep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "step_seq", nullable = false)
    private Integer stepSeq;

    @Column(name = "step_name", nullable = false, length = 64)
    private String stepName;

    @Column(name = "workstation_id", nullable = false)
    private Long workstationId;

    @Column(name = "layer_no")
    private Integer layerNo;

    @Column(name = "std_cycle_time")
    private Integer stdCycleTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }

    public Integer getStepSeq() { return stepSeq; }
    public void setStepSeq(Integer stepSeq) { this.stepSeq = stepSeq; }

    public String getStepName() { return stepName; }
    public void setStepName(String stepName) { this.stepName = stepName; }

    public Long getWorkstationId() { return workstationId; }
    public void setWorkstationId(Long workstationId) { this.workstationId = workstationId; }

    public Integer getLayerNo() { return layerNo; }
    public void setLayerNo(Integer layerNo) { this.layerNo = layerNo; }

    public Integer getStdCycleTime() { return stdCycleTime; }
    public void setStdCycleTime(Integer stdCycleTime) { this.stdCycleTime = stdCycleTime; }

}