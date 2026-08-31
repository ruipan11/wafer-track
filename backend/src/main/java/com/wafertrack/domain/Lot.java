package com.wafertrack.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "lot")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_no", nullable = false, length = 32)
    private String lotNo;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "current_step_seq")
    private Integer currentStepSeq;

    @Column(name = "status", nullable = false, length = 16)
    private String status;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }

    public Integer getCurrentStepSeq() { return currentStepSeq; }
    public void setCurrentStepSeq(Integer currentStepSeq) { this.currentStepSeq = currentStepSeq; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; } 
}
