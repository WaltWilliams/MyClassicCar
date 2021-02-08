package com.woodlandcoders.myclassiccar;

public class MaintenanceContainer {

    private int mID = -1;
    private String mDate;
    private String mRepair;
    private String mSpecifics;
    private String mMiles;

    public MaintenanceContainer(int id, String date, String repair, String miles, String specifics) {
        this.mID = id;
        this.mDate = date;
        this.mRepair = repair;
        this.mMiles = miles;
        this.mSpecifics = specifics;
    }

    public int getID() {
        return mID;
    }

    public String getDate() {
        return mDate;
    }

    public String getRepair() {
        return mRepair;
    }

    public String getMiles() {
        return mMiles;
    }

    public String getSpecifics() {
        return mSpecifics;
    }
}
