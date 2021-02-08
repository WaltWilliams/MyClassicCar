package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MaintenanceAddActivity extends AppCompatActivity {

    Bundle dataBundle;
    DBManagerSchema schema;
    DBManager carDB;
    Utilities utilities;

    private TextView date;
    private EditText miles;
    private EditText wp;
    private EditText specifics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_add);

        // Creating needed object
        dataBundle = getIntent().getExtras();
        carDB = new DBManager(this);
        utilities = new Utilities();

        // Setting the current days date in the date field.
        date = findViewById(R.id.maintDateTextView);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.date_format));
        String dateString = dateFormatter.format(today);
        date.setText(dateString);

        miles = findViewById(R.id.maintMilesEditText);
        wp = findViewById(R.id.maintWPEditText);
        specifics = findViewById(R.id.maintSpecificsEditText);


        String currentCarYear = dataBundle.getString(schema.COL_YEAR);
        String currentCarMake = dataBundle.getString(schema.COL_MAKE);

        FloatingActionButton fab = findViewById(R.id.maintSaveFab);

        Toolbar oilToolBar = findViewById(R.id.MaintenanceAddToolbar);
        setSupportActionBar(oilToolBar);
        oilToolBar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentCarYear + " " + currentCarMake);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues maintInstance = new ContentValues();

                if(!miles.getText().toString().isEmpty() && !wp.getText().toString().isEmpty()) {
                    maintInstance.put(schema.COL_CAR_ID, dataBundle.getInt(schema.COL_CAR_ID));
                    maintInstance.put(schema.COL_DATE, date.getText().toString());
                    maintInstance.put(schema.COL_MILEAGE, miles.getText().toString());
                    maintInstance.put(schema.COL_REPAIRS, wp.getText().toString());
                    maintInstance.put(schema.COL_NOTES, specifics.getText().toString());

                    boolean didItSave = carDB.addMaintenanceInfo(maintInstance);

                    if(didItSave == true) {
                        Intent oilIntent = new Intent(getApplicationContext(), MaintenanceActivity.class);
                        oilIntent.putExtras(dataBundle);
                        Toast.makeText(MaintenanceAddActivity.this, "This oil change has been saved.", Toast.LENGTH_SHORT).show();
                        startActivity(oilIntent);
                    }
                    else {
                        Toast.makeText(MaintenanceAddActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MaintenanceAddActivity.this, "You must enter the Mileage and Work Performed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateOil = getMenuInflater();
        inflateOil.inflate(R.menu.no_drop_down_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Provides functionality for the menu items.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent backNavIntent = new Intent(getApplicationContext(), MaintenanceActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    // Takes control of the hardware back button.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }

}
