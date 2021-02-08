package com.woodlandcoders.myclassiccar;

import android.provider.BaseColumns;

/**
 * Created by walt on 1/31/18.
 */

public class DBManagerSchema implements BaseColumns {



    DBManagerSchema() {
        // Nothing
    }

    // ===========  Tables ===============
    static final String TABLE_CARS = "Cars";
    static final String TABLE_CARS2PARTS = "CarsToParts";
    static final String TABLE_PARTS = "Parts";
    static final String TABLE_TPM_PARTS = "TPM_Parts";
    static final String TABLE_OIL = "Oil";
    static final String TABLE_REPAIRS = "Repairs";


    // ===========  Columns ===============
    static final String COL_ID = "_id";
    static final String COL_CAR_ID = "CarID";
    static final String COL_PART_ID = "PartID";
    static final String COL_MAKE = "Make";
    static final String COL_YEAR = "Year";
    static final String COL_MODEL = "Model";
    static final String COL_NAME = "Name";
    static final String COL_VIN = "engVIN";
    static final String COL_ENG_SIZE = "engSize";
    static final String COL_ENG_UNIT = "engUnit";
    static final String COL_ENG_CONFIG = "engConfig";
    static final String COL_ENG_NUMBER = "engNumber";
    static final String COL_PHOTO = "Photo";
    static final String COL_PART_NAME = "PartName";
    static final String COL_PART_NUMBER = "PartNumber";
    static final String COL_NOTES = "Notes";
    static final String COL_BARCODE = "Barcode";
    static final String COL_SOURCE = "Source";
    static final String COL_MFG = "MFG";
    static final String COL_OIL_INTERVAL = "OilInterval";
    static final String COL_ODOMETER_TYPE = "OdometerType";
    static final String COL_MILEAGE = "Mileage";
    static final String COL_OIL_BRAND = "OilBrand";
    static final String COL_VISCOSITY = "Viscosity";
    static final String COL_FILTER_BRAND = "FilterBrand";
    static final String COL_DATE = "Date";
    static final String COL_REPAIRS = "Repairs";


    // =========== Queries ===============
    // Cars table query
    static final String CREATE_CAR_TABLE = "CREATE TABLE " + TABLE_CARS + " (" +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_YEAR + " TEXT, " +
            COL_MAKE + " TEXT, " +
            COL_MODEL + " TEXT, " +
            COL_NAME + " TEXT, " +
            COL_VIN  + " TEXT, " +
            COL_ENG_SIZE + " TEXT, " +
            COL_ENG_UNIT + " TEXT, " +
            COL_ENG_CONFIG + " TEXT, " +
            COL_ENG_NUMBER + " TEXT, " +
            COL_OIL_INTERVAL + " TEXT, " +
            COL_ODOMETER_TYPE + " TEXT, " +
            COL_PHOTO + " BLOB, " +
            COL_NOTES + " TEXT)";


    // Cars to Parts table query
    // No autoincrement is to placed on the primary key as this will receive a proper id
    // number from a part that exists in the Parts table. This is to map parts to cars
    // and make it so a part can be mapped to multiple cars.
    static final String CREATE_PARTS2CARS_TABLE = "CREATE TABLE " + TABLE_CARS2PARTS + " (" +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_CAR_ID + " INTEGER, " +
            COL_PART_ID + " INTEGER)";

    // Parts table query
    static final String CREATE_PARTS_TABLE = "CREATE TABLE " + TABLE_PARTS + " (" +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_PART_NAME + " TEXT, " +
            COL_PART_NUMBER + " TEXT, " +
            COL_MFG + " TEXT, " +
            COL_BARCODE + " TEXT, " +
            COL_PHOTO + " BLOB, " +
            COL_NOTES + " TEXT)";

    // TPM_Parts table query
    static final String CREATE_TPM_PARTS_TABLE = "CREATE TABLE " + TABLE_TPM_PARTS + " (" +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_PART_ID + " INTEGER, " +
            COL_SOURCE + " TEXT, " +
            COL_MFG + " TEXT, " +
            COL_PART_NUMBER + " TEXT, " +
            COL_BARCODE + " TEXT, " +
            COL_NOTES + " TEXT)";

    // Oil change table.
    static final String CREATE_TABLE_OIL = "CREATE TABLE " + TABLE_OIL + " (" +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_CAR_ID + " INTEGER, " +
            COL_MILEAGE + " TEXT, " +
            COL_OIL_BRAND + " TEXT, " +
            COL_VISCOSITY + " TEXT, " +
            COL_FILTER_BRAND + " TEXT, " +
            COL_DATE + " TEXT)";

    // Maintenance table.
    static final String CREATE_TABLE_REPAIRS = "CREATE TABLE " + TABLE_REPAIRS + " (" +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_CAR_ID + " INTEGER, " +
            COL_DATE + " TEXT, " +
            COL_REPAIRS + " TEXT, " +
            COL_MILEAGE + " TEXT, " +
            COL_NOTES + " TEXT)";

}

