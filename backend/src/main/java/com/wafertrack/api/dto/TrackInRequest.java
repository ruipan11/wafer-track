package com.wafertrack.api.dto;

public class TrackInRequest {

    private String lotNo;
    private String eqpCode;
    private String operator;

    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }

    public String getEqpCode() { return eqpCode; }
    public void setEqpCode(String eqpCode) { this.eqpCode = eqpCode; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
}