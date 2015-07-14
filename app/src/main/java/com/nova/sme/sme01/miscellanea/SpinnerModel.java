package com.nova.sme.sme01.miscellanea;


public class SpinnerModel {

    private  String operationName;
    private  int    imageId; // checked or unchecked

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    public String getOperationName() {
        return this.operationName;
    }


    public void setimageId(int id) {
        this.imageId = id;
    }
    public int getImageId() {
        return this.imageId;
    }
}
