package com.wafertrack.api.dto;

public class TrackOutRequest {

    private String lotNo;
    private String eqpCode;
    private String operator;
    private Integer outQty;
    private String defectCode;

    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }

    public String getEqpCode() { return eqpCode; }
    public void setEqpCode(String eqpCode) { this.eqpCode = eqpCode; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Integer getOutQty() { return outQty; }
    public void setOutQty(Integer outQty) { this.outQty = outQty; }

    public String getDefectCode() { return defectCode; }
    public void setDefectCode(String defectCode) { this.defectCode = defectCode; }
}