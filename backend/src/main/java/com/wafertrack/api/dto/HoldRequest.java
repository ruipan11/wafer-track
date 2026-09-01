package com.wafertrack.api.dto;

public class HoldRequest {
    private String lotNo;
    private String reasonCode;
    private String comment;
    private String operator;

    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) {this.lotNo = lotNo; }

    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) {this.reasonCode = reasonCode; }

    public String getComment() { return comment; }
    public void setComment(String comment) {this.comment = comment; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) {this.operator = operator; }
}