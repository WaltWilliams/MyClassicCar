package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OilChangeEditActivity extends AppCompatActivity {

    Bundle dataBundle;
    DBManagerSchema schema;
    DBManager carDB;
    Utilities utilities = new Utilities();

    private TextView date;
    private EditText currentOdo;
    private TextView shouldHave;
    private EditText oilBrand;
    private EditText oilVis;
    private EditText filter;

    boolean objectHasChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_change_add);

        // Creating needed object
        dataBundle = getIntent().getExtras();
        carDB = new DBManager(this);

        date = findViewById(R.id.aocDateTextView);
        currentOdo = findViewById(R.id.aocOdoEditText);
        currentOdo.requestFocus();
        oilBrand = findViewById(R.id.aocOilBrandEditText);
        oilVis = findViewById(R.id.aocOilVisEditText);
        filter = findViewById(R.id.aocFilterEditText);
        shouldHave = findViewById(R.id.aocShouldHaveTextView);


        String currentCarYear = dataBundle.getString(schema.COL_YEAR);
        String currentCarMake = dataBundle.getString(schema.COL_MAKE);

        Toolbar oilToolBar = findViewById(R.id.oilChangeToolbar);
        setSupportActionBar(oilToolBar);
        oilToolBar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentCarYear + " " + currentCarMake);


        Cursor previousOilChangeData = carDB.getOilChangeDataByID(Integer.parseInt(dataBundle.getString("ID")));
        int x = previousOilChangeData.getCount();
        int z = previousOilChangeData.getInt(previousOilChangeData.getColumnIndex(schema.COL_ID));

        String interval = carDB.getCarOilInterval(dataBundle.getInt(schema.COL_CAR_ID));
        if(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_MILEAGE)) == null) {
            shouldHave.setText(R.string.no_data);
        }
        else {
            String mileage = previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_MILEAGE));
            String due = utilities.calculateOilDue(mileage, interval);
            shouldHave.setText(due);
        }



        date.setText(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_DATE)));
        currentOdo.setText(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_MILEAGE)));
        currentOdo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });
        oilBrand.setText(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_OIL_BRAND)));
        oilBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });
        oilVis.setText(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_VISCOSITY)));
        oilVis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });
        filter.setText(previousOilChangeData.getString(previousOilChangeData.getColumnIndex(schema.COL_FILTER_BRAND)));
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.oilSaveFab);

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

                    boolean didItSave = carDB.updateOilChange(oilChangeValues, dataBundle.getString("ID"));            // This may not work.. ////////////////////////////////////

                    if(didItSave == true) {
                        Intent oilIntent = new Intent(getApplicationContext(), OilChangeActivity.class);
                        oilIntent.removeExtra("ID");
                        oilIntent.putExtras(dataBundle);
                        Toast.makeText(OilChangeEditActivity.this, "This oil change has been saved.", Toast.LENGTH_SHORT).show();
                        startActivity(oilIntent);
                    }
                    else {
                        Toast.makeText(OilChangeEditActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(OilChangeEditActivity.this, "You must enter the current\n       odometer reading.", Toast.LENGTH_LONG).show();
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

        if(item.getItemId() == android.R.id.home) {
            if(objectHasChanges == true) {
                youDidntSave();
            } else {
                Intent backNavIntent = new Intent(getApplicationContext(), MaintenanceActivity.class);
                backNavIntent.putExtras(dataBundle);
                startActivity(backNavIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    // Takes control of the hardware back button.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (objectHasChanges == true) {
                Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();
                youDidntSave();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void youDidntSave() {
        AlertDialog.Builder notSavedDialog = new AlertDialog.Builder(this);
        notSavedDialog.setTitle("Wait!");
        notSavedDialog.setMessage("An alteration has been made. Do you want to save the change? If you tap No your changes will be lost");
        notSavedDialog.setIcon(android.R.drawable.ic_dialog_info);

        notSavedDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), R.string.tapSave, Toast.LENGTH_LONG).show();
            }
        });

        notSavedDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();
                finish();
            }
        });
        AlertDialog saved = notSavedDialog.create();
        saved.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }
}
