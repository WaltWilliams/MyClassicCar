package com.woodlandcoders.myclassiccar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by walt on 2/2/18.
 */

public class MainActivityAdapter extends CursorAdapter {

    DBManagerSchema schema;
    DBManager carDB;
    Utilities utilities = new Utilities();

    public MainActivityAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    // setting up a single listView cell.

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.activity_main_detail, viewGroup, false);
    }

    @Override
    public void bindView(View singleCarView, Context context, Cursor cursor) {

        // Alternating the ListView cell color so each entry stands out.
        if(cursor.getPosition() % 2 == 1) {
             singleCarView.setBackgroundResource(R.color.StandardFieldAlternate);
        }

        TextView id = singleCarView.findViewById(R.id.idTextView);
        ImageView carImage = singleCarView.findViewById(R.id.carInfoImageView);
        TextView year = singleCarView.findViewById(R.id.carInfoYearTextView);
        TextView make = singleCarView.findViewById(R.id.carInfoMakeTextView);
        TextView model = singleCarView.findViewById(R.id.carInfoModelTextView);
        TextView name = singleCarView.findViewById(R.id.carNameTextView);
        TextView oil = singleCarView.findViewById(R.id.carInfoOilTextView);

        // Converting the image from the db for use in the ImageView.
        byte[] byteData = cursor.getBlob(cursor.getColumnIndex(schema.COL_PHOTO));
        Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        carImage.setImageBitmap(bm);

        year.setText(cursor.getString(cursor.getColumnIndex(schema.COL_YEAR)));
        make.setText(cursor.getString(cursor.getColumnIndex(schema.COL_MAKE)));
        model.setText(cursor.getString(cursor.getColumnIndex(schema.COL_MODEL)));
        name.setText(cursor.getString(cursor.getColumnIndex(schema.COL_NAME)));
        id.setText(cursor.getString(cursor.getColumnIndex(schema.COL_ID)));

        // Grabbing latest mileage for each car from oil table and the change interval from the cars table..
        carDB = new DBManager(context);
        int currentCar = cursor.getInt(cursor.getColumnIndex(schema.COL_ID));
        Cursor oilInfo = carDB.getCarInfoHighestMiles(currentCar);
        String interval = carDB.getCarOilInterval(currentCar);
        if(oilInfo.getString(oilInfo.getColumnIndex(schema.COL_MILEAGE)) == null) {
            oil.setText("No data");
        }
        else {
            String mileage = oilInfo.getString(oilInfo.getColumnIndex(schema.COL_MILEAGE));
            String due = utilities.calculateOilDue(mileage, interval);
            oil.setText(due);
        }
        carDB.close();
    }
}
