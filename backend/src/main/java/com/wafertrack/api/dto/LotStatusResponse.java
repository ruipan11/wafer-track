package com.wafertrack.api.dto;

public class LotStatusResponse {

    private String lotNo;
    private String productCode;
    private String routeCode;
    private Integer currentStepSeq;
    private String currentStepName;
    private String currentWorkstation;
    private Integer layerNo;
    private String status;
    private Integer qty;
    private Integer priority;

    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getRouteCode() { return routeCode; }
    public void setRouteCode(String routeCode) { this.routeCode = routeCode; }

    public Integer getCurrentStepSeq() { return currentStepSeq; }
    public void setCurrentStepSeq(Integer currentStepSeq) { this.currentStepSeq = currentStepSeq; }

    public String getCurrentStepName() { return currentStepName; }
    public void setCurrentStepName(String currentStepName) { this.currentStepName = currentStepName; }

    public String getCurrentWorkstation() { return currentWorkstation; }
    public void setCurrentWorkstation(String currentWorkstation) { this.currentWorkstation = currentWorkstation; }

    public Integer getLayerNo() { return layerNo; }
    public void setLayerNo(Integer layerNo) { this.layerNo = layerNo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}