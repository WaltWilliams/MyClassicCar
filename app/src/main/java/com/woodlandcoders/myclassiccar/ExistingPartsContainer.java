package com.woodlandcoders.myclassiccar;

public class ExistingPartsContainer {
    int id = -1;
    byte[] mImage;
    String mPartName;
    String mPartNumber;
    String mMfg;


    public ExistingPartsContainer(int id, byte[] image, String partName, String partNumber, String mfg) {
        this.id = id;
        this.mImage = image;
        this.mPartName = partName;
        this.mPartNumber = partNumber;
        this.mMfg = mfg;
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

    public String getMfg() {
        return mMfg;
    }
}
