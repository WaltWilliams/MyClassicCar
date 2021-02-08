package com.woodlandcoders.myclassiccar;


public class OilChangeContainer {

   private int mID = 0;
    private String mDate = "";
    private String mMileage = "";
    private String mDue = "";
    private String mViscosity = "";
    private String mOBrand = "";
    private String mFBrand = "";


    public OilChangeContainer(int id, String Date, String Mileage, String due, String Viscosity, String oBrand, String fBrand) {
        this.mID = id;
        this.mDate = Date;
        this.mMileage = Mileage;
        this.mDue = due;
        this.mViscosity = Viscosity;
        this.mOBrand = oBrand;
        this.mFBrand = fBrand;
    }

    public int getId() {
        return mID;
    }

    public String getDate() {
        return mDate;
    }

    public String getMileage() {
        return mMileage;
    }

    public String getDue() {
        return mDue;
    }

    public String getViscosity() {
        return mViscosity;
    }

    public String getOBrand() {
        return mOBrand;
    }

    public String getFBrand() {
        return mFBrand;
    }

}
