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

public class OilChangeAddActivity extends AppCompatActivity {

    Bundle dataBundle;
    DBManagerSchema schema;
    DBManager carDB;
    Utilities utilities = new Utilities();

    private TextView date;
    private EditText currentOdo;
    private EditText oilBrand;
    private EditText oilVis;
    private EditText filter;
    private TextView shouldHave;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_change_add);

        // Creating needed object
        dataBundle = getIntent().getExtras();
        carDB = new DBManager(this);

        // Setting the current days date in the date field.
        date = findViewById(R.id.aocDateTextView);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.date_format));
        String dateString = dateFormatter.format(today);
        date.setText(dateString);


        currentOdo = findViewById(R.id.aocOdoEditText);
        oilBrand = findViewById(R.id.aocOilBrandEditText);
        oilVis = findViewById(R.id.aocOilVisEditText);
        filter = findViewById(R.id.aocFilterEditText);
        shouldHave = findViewById(R.id.aocShouldHaveTextView);

        Cursor previousOilChangeData = carDB.getLastOilChangeData(dataBundle.getInt(schema.COL_CAR_ID));
        int cursorSize = previousOilChangeData.getCount();
        if(cursorSize != 0) {
            currentOdo.requestFocus();
            oilBrand.setText(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_OIL_BRAND)));
            oilVis.setText(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_VISCOSITY)));
            filter.setText(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_FILTER_BRAND)));
            String mileage = previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_MILEAGE));
            String interval = carDB.getCarOilInterval(dataBundle.getInt(schema.COL_CAR_ID));
            String due = utilities.calculateOilDue(mileage, interval);
            shouldHave.setText(due);
        }
        else {
            shouldHave.setText(R.string.no_data);
        }


        String currentCarYear = dataBundle.getString(schema.COL_YEAR);
        String currentCarMake = dataBundle.getString(schema.COL_MAKE);

        FloatingActionButton fab = findViewById(R.id.oilSaveFab);

        Toolbar oilToolBar = findViewById(R.id.oilChangeToolbar);
        setSupportActionBar(oilToolBar);
        oilToolBar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentCarYear + " " + currentCarMake);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues oilChangeValues = new ContentValues();

                if(!currentOdo.getText().toString().isEmpty()) {
                    oilChangeValues.put(schema.COL_DATE, date.getText().toString());
                    oilChangeValues.put(schema.COL_CAR_ID, dataBundle.getInt(schema.COL_CAR_ID));
                    oilChangeValues.put(schema.COL_MILEAGE, currentOdo.getText().toString());
                    oilChangeValues.put(schema.COL_OIL_BRAND, oilBrand.getText().toString());
                    oilChangeValues.put(schema.COL_VISCOSITY, oilVis.getText().toString());
                    oilChangeValues.put(schema.COL_FILTER_BRAND, filter.getText().toString());

                    boolean didItSave = carDB.addOilChange(oilChangeValues);

                    if(didItSave == true) {
                        Intent oilIntent = new Intent(getApplicationContext(), OilChangeActivity.class);
                        oilIntent.putExtras(dataBundle);
                        Toast.makeText(OilChangeAddActivity.this, "This oil change has been saved.", Toast.LENGTH_SHORT).show();
                        startActivity(oilIntent);
                    }
                    else {
                        Toast.makeText(OilChangeAddActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(OilChangeAddActivity.this, "You must enter the current\n       odometer reading.", Toast.LENGTH_LONG).show();
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
            Intent backNavIntent = new Intent(getApplicationContext(), OilChangeActivity.class);
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
