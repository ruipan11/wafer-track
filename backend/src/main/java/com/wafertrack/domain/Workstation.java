package com.wafertrack.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "workstation")
public class Workstation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ws_code", nullable = false, length = 32)
    private String wsCode;

    @Column(name = "ws_name", nullable = false, length = 64)
    private String wsName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWsCode() { return wsCode; }
    public void setWsCode(String wsCode) { this.wsCode = wsCode; }

    public String getWsName() { return wsName; }
    public void setWsName(String wsName) { this.wsName = wsName; }

}
