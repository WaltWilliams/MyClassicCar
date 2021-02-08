package com.woodlandcoders.myclassiccar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by walt on 2/23/18.
 */

public class Utilities extends AppCompatActivity {

    DBManagerSchema schema = new DBManagerSchema();
    private final int REQUEST_IMAGE_CAPTURE = 1;

    public Utilities() {
        // Empty
    }

    public int getREQUEST_IMAGE_CAPTURE() {
        return REQUEST_IMAGE_CAPTURE;
    }


    // A function to convert a byte array - "blob" - from the db for display in an ImageView.
    public Bitmap convertImageForDisplay(byte[] byteArray) {
        final Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bm;
    }


    // A function that converts a bitmap image from a ImageView to a byte array for storage as "blob" in a sqlite db.
    public byte[] convertImageForStorage(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] byteArray = out.toByteArray();
        return byteArray;
    }


    // A function to convert a Cursor to an ArrayList for use in the oil change history RecyclerView.
    public ArrayList CreateOilListFromCursor(Context context, Cursor cursor, int carID) {
        ArrayList<OilChangeContainer> oilList = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(schema.COL_ID));
                String mDate = cursor.getString(cursor.getColumnIndex(schema.COL_DATE));
                String mMileage = cursor.getString(cursor.getColumnIndex(schema.COL_MILEAGE));
                String mViscosity = cursor.getString(cursor.getColumnIndex(schema.COL_VISCOSITY));
                String mOBrand = cursor.getString(cursor.getColumnIndex(schema.COL_OIL_BRAND));
                String mFBrand = cursor.getString(cursor.getColumnIndex(schema.COL_FILTER_BRAND));

                DBManager db = new DBManager(context);
                String interval = db.getCarOilInterval(carID);
                String mInterval = calculateOilDue(mMileage, interval);

                OilChangeContainer item = new OilChangeContainer(id, mDate, mMileage, mInterval, mViscosity, mOBrand, mFBrand);
                oilList.add(item);
            }
            while (cursor.moveToNext());
        }
        return oilList;
    }


    // A function to convert a Cursor to an ArrayList for use in the maintenance history RecyclerView.
    public ArrayList CreateMaintenanceListFromCursor(Cursor cursor) {
        ArrayList<MaintenanceContainer> maintenanceList = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(schema.COL_ID));
                String date = cursor.getString(cursor.getColumnIndex(schema.COL_DATE));
                String repair = cursor.getString(cursor.getColumnIndex(schema.COL_REPAIRS));
                String miles = cursor.getString(cursor.getColumnIndex(schema.COL_MILEAGE));
                String specifics = cursor.getString(cursor.getColumnIndex(schema.COL_NOTES));

                MaintenanceContainer item = new MaintenanceContainer(id, date, repair, miles, specifics);
                maintenanceList.add(item);
            }
            while (cursor.moveToNext());
        }
        return maintenanceList;
    }


    // A function to convert a Cursor to an ArrayList for use in the View Parts RecyclerView.
    public ArrayList CreateViewPartsListFromCursor(Cursor cursor) {
        ArrayList<ViewPartsContainer> viewPartsList = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(schema.COL_ID));
                byte[] byteData = cursor.getBlob(cursor.getColumnIndex(schema.COL_PHOTO));
                String partName = cursor.getString(cursor.getColumnIndex(schema.COL_PART_NAME));
                String partNum = cursor.getString(cursor.getColumnIndex(schema.COL_PART_NUMBER));

                ViewPartsContainer item = new ViewPartsContainer(id, byteData, partName, partNum);
                viewPartsList.add(item);
            }
            while (cursor.moveToNext());
        }
        return viewPartsList;
    }

    // A function to convert a Cursor to an ArrayList for use in the View Parts RecyclerView.
    public ArrayList CreateExistingPartsListFromCursor(Cursor cursor) {
        ArrayList<ExistingPartsContainer> existingPartsList = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(schema.COL_ID));
                byte[] byteData = cursor.getBlob(cursor.getColumnIndex(schema.COL_PHOTO));
                String partName = cursor.getString(cursor.getColumnIndex(schema.COL_PART_NAME));
                String partNum = cursor.getString(cursor.getColumnIndex(schema.COL_PART_NUMBER));
                String mfg = cursor.getString(cursor.getColumnIndex(schema.COL_MFG));

                ExistingPartsContainer item = new ExistingPartsContainer(id, byteData, partName, partNum, mfg);
                existingPartsList.add(item);
            }
            while (cursor.moveToNext());
        }
        return existingPartsList;
    }

    // The central function to calculate the oil change due mileage.
    public String calculateOilDue(String mMileage, String mInterval) {
        int m = Integer.parseInt(mMileage);
        int i = Integer.parseInt(mInterval);
        int d = m + i;

        return Integer.toString(d);
    }
}
