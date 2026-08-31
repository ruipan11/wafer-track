package com.wafertrack.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "eqp_code", nullable = false, length = 32)
    private String eqpCode;

    @Column(name = "eqp_name", nullable = false, length = 64)
    private String eqpName;

    @Column(name = "workstation_id", nullable = false)
    private Long workstationId;

    @Column(name = "status", nullable = false, length = 16)
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEqpCode() { return eqpCode; }
    public void setEqpCode(String eqpCode) { this.eqpCode = eqpCode; }

    public String getEqpName() { return eqpName; }
    public void setEqpName(String eqpName) { this.eqpName = eqpName; }

    public Long getWorkstationId() { return workstationId; }
    public void setWorkstationId(Long workstationId) { this.workstationId = workstationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}