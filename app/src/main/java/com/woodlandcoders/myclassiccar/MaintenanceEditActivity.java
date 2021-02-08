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

public class MaintenanceEditActivity extends AppCompatActivity {

    Bundle dataBundle;
    DBManagerSchema schema;
    DBManager carDB;
    Utilities utilities = new Utilities();

    private TextView date;
    private EditText miles;
    private EditText wp;
    private EditText specifics;

    boolean objectHasChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_add);

        // Creating needed object
        dataBundle = getIntent().getExtras();
        carDB = new DBManager(this);
        utilities = new Utilities();

        date = findViewById(R.id.maintDateTextView);
        miles = findViewById(R.id.maintMilesEditText);
        wp = findViewById(R.id.maintWPEditText);
        specifics = findViewById(R.id.maintSpecificsEditText);

        String currentCarYear = dataBundle.getString(schema.COL_YEAR);
        String currentCarMake = dataBundle.getString(schema.COL_MAKE);


        Toolbar oilToolBar = findViewById(R.id.MaintenanceAddToolbar);
        setSupportActionBar(oilToolBar);
        oilToolBar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentCarYear + " " + currentCarMake);

        Cursor maintenanceInstant = carDB.getMaintenanceInstance(dataBundle.getInt("ID"));
        int z = maintenanceInstant.getCount();

        date.setText(maintenanceInstant.getString(maintenanceInstant.getColumnIndex(schema.COL_DATE)));
        date.addTextChangedListener(new TextWatcher() {
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

        miles.setText(maintenanceInstant.getString(maintenanceInstant.getColumnIndex(schema.COL_MILEAGE)));
        miles.addTextChangedListener(new TextWatcher() {
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

        wp.setText(maintenanceInstant.getString(maintenanceInstant.getColumnIndex(schema.COL_REPAIRS)));
        wp.addTextChangedListener(new TextWatcher() {
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

        specifics.setText(maintenanceInstant.getString(maintenanceInstant.getColumnIndex(schema.COL_NOTES)));
        specifics.addTextChangedListener(new TextWatcher() {
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

        FloatingActionButton fab = findViewById(R.id.maintSaveFab);

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

                    boolean didItSave = carDB.updateMaintenanceInfo(maintInstance, dataBundle.getString("ID"));

                    if(didItSave == true) {
                        Intent oilIntent = new Intent(getApplicationContext(), MaintenanceActivity.class);
                        oilIntent.putExtras(dataBundle);
                        Toast.makeText(MaintenanceEditActivity.this, "This oil change has been saved.", Toast.LENGTH_SHORT).show();
                        startActivity(oilIntent);
                    }
                    else {
                        Toast.makeText(MaintenanceEditActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MaintenanceEditActivity.this, "You must enter the Mileage and Work Performed.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateOil = getMenuInflater();
        inflateOil.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Provides functionality for the menu items.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.homeBoy) {
            Intent homeNavIntent = new Intent(getApplicationContext(), MainActivity.class);
            homeNavIntent.putExtras(dataBundle);
            startActivity(homeNavIntent);
        }


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
            Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();
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
