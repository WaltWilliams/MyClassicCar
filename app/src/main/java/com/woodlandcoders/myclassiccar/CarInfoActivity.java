package com.woodlandcoders.myclassiccar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CarInfoActivity extends AppCompatActivity {

    // Creating references to needed objects.
    DBManager carDB;
    DBManagerSchema schema;
    Bundle dataBundle;
    Utilities utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_car);

        // Instantiating needed objects.
        dataBundle = getIntent().getExtras();
        utilities = new Utilities();

        // The starting of the toolbar is near the bottom of this method, as
        // it is getting its title from the db.

        // db stuff fio loading the UI objects.
        carDB = new DBManager(this);
        Cursor selectedCar = carDB.getSelectedCarInfo(dataBundle.getInt(schema.COL_CAR_ID));

        // References to UI objects.
        ImageView carInfoImageView = findViewById(R.id.carInfoImageView);
        TextView carInfoModelTextView = findViewById(R.id.carInfoModelTextView);
        TextView carInfoNameTextView = findViewById(R.id.carInfoNameTextView);
        TextView carInfoVINtextView = findViewById(R.id.carInfoVINtextView);
        TextView carInfoEngSizeTextView = findViewById(R.id.carInfoEngSizeTextView);
        TextView carInfoEngUnitsTextView = findViewById(R.id.carInfoEngUnitsTextView);
        TextView carInfoEngConfigTextView = findViewById(R.id.carInfoEngConfigTextView);
        TextView carInfoEngNumTextView = findViewById(R.id.carInfoEngNumTextView);
        TextView carInfoIntervalTextView = findViewById(R.id.carInfoIntervalTextView);
        TextView carInfoOdoTypeTextView = findViewById(R.id.carInfoOdoTypeTextView);
        TextView carInfoNotesTextView = findViewById(R.id.carInfoNotesTextView);
        // Wiring up the FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.carInfoFab);

        // Preparing an image for displaying.
        Bitmap bm = utilities.convertImageForDisplay(selectedCar.getBlob(selectedCar.getColumnIndex(schema.COL_PHOTO)));
        carInfoImageView.setImageBitmap(bm);

        // Loading UI objects.
        String make = selectedCar.getString(selectedCar.getColumnIndex(schema.COL_MAKE));
        carInfoModelTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_MODEL)));
        carInfoNameTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_NAME)));
        String year = selectedCar.getString(selectedCar.getColumnIndex(schema.COL_YEAR));
        carInfoVINtextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_VIN)));
        carInfoEngSizeTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ENG_SIZE)));
        carInfoEngUnitsTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ENG_UNIT)));
        carInfoEngConfigTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ENG_CONFIG)));
        carInfoEngNumTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ENG_NUMBER)));
        carInfoIntervalTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_OIL_INTERVAL)));
        carInfoOdoTypeTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ODOMETER_TYPE)));
        carInfoNotesTextView.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_NOTES)));


        // Starting the toolbar.
        Toolbar carInfoToolbar = findViewById(R.id.carInfoToolbar);
        setSupportActionBar(carInfoToolbar);
        carInfoToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(year + " " + make);


        // ========================================================================================

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(getApplicationContext(), CarEditActivity.class);
                editIntent.putExtras(dataBundle);
                startActivity(editIntent);
            }
        });
    }

    // Starting the menu for use in the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater carInfoInflater = getMenuInflater();
        carInfoInflater.inflate(R.menu.car_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Code to make menu selections work.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.deleteCar) {

            // Alert box asking if you really want to delete this car.
            // It would be kind if rude to silently delete things.
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
            deleteDialog.setTitle(R.string.are_you_sure);
            deleteDialog.setMessage(R.string.not_reversible_car);
            deleteDialog.setIcon(android.R.drawable.ic_dialog_info);
            deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    // A function to delete the oil change history for this car.
                    Cursor oilInfo = carDB.getOilInfo(dataBundle.getInt(schema.COL_CAR_ID));
                    if(oilInfo.getCount() > 0) {
                        do {
                            int oilID = oilInfo.getInt(oilInfo.getColumnIndex(schema.COL_ID));
                            carDB.deleteOilChange(oilID);
                        } while (oilInfo.moveToNext());
                    }

                    // A function to delete the maintenance history for this car.
                    Cursor maintenanceInfo = carDB.getMaintenanceInfo(dataBundle.getInt(schema.COL_CAR_ID));
                    if(oilInfo.getCount() > 0) {
                        do {
                            int maintID = maintenanceInfo.getInt(oilInfo.getColumnIndex(schema.COL_ID));
                            carDB.deleteMaintenance(maintID);
                        } while (oilInfo.moveToNext());
                    }

                    // The function launched here iterates through the cursor deleting all OEM parts and
                    // their associated TPM parts. (TPM = Third Part Manufacture).
                    // The method tests to see if there are other cars mapped to an
                    // OEM part and will not delete it from the parts table if there are.
                    Cursor partsList = carDB.getCarsToParts(dataBundle.getInt(schema.COL_CAR_ID));
                    if (partsList.getCount() > 0) {
                        do {
                            int partID = partsList.getInt(partsList.getColumnIndex("PartID"));
                            carDB.deleteOemPart(partID, dataBundle.getInt(schema.COL_CAR_ID));
                        } while (partsList.moveToNext());
                    }


                    // Once the histories and parts are deleted from the db for this car, delete the car.
                    carDB.deleteCar(dataBundle.getInt(schema.COL_CAR_ID));

                    // Return to the MainActivity when finished.
                    Intent returnToCars = new Intent(getApplicationContext(), MainActivity.class);
                    returnToCars.putExtras(dataBundle);
                    startActivity(returnToCars);
                }
            });
            deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel(); // Do nothing.
                }
            });
            AlertDialog deleteWarning = deleteDialog.create();
            deleteWarning.show();
        }

        // Up button
        if (item.getItemId() == android.R.id.home) {
            Intent backNavIntent = new Intent(getApplicationContext(), RecordSelectActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }

        // Not to be confused with the Up button
        if (item.getItemId() == R.id.homeView) {
            Intent homeIntext = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeIntext);
        }

        return super.onOptionsItemSelected(item);
    }


    // Takes control of the hardware back button.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }
}
