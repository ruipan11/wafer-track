package com.wafertrack.api.dto;

public class ReleaseRequest {
    private String lotNo;
    private String comment;
    private String operator;

    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) {this.lotNo = lotNo; }

    public String getComment() { return comment; }
    public void setComment(String comment) {this.comment = comment; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) {this.operator = operator; }
}