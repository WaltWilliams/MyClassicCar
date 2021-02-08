package com.woodlandcoders.myclassiccar;

public class ViewPartsContainer {

    int id = -1;
    byte[] mImage;
    String mPartName;
    String mPartNumber;


    public ViewPartsContainer(int id, byte[] mImage, String mPartName, String mPartNumber) {
        this.id = id;
        this.mImage = mImage;
        this.mPartName = mPartName;
        this.mPartNumber = mPartNumber;
    }


    public int getId() {
        return id;
    }

    public byte[] getImage() {
        return mImage;
    }

    public String getPartName() {
        return mPartName;
    }

    public String getPartNumber() {
        return mPartNumber;
    }
}
