package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by walt on 2/6/18.
 */

public class CarEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // References to needed objects
    DBManager carDB;
    DBManagerSchema schema;
    Bundle dataBundle;
    Utilities utilities;

    // References to Strings needed globally.
    String yearData;
    String unitData;
    String configData;
    String odometerData;
    String currentCarUnit;
    String currentCarYear;
    String currentEngConfig;
    String currentOdometer;

    // References to UI objects needed globally.
    ImageButton editCarImageButton;
    EditText editCarMakeEditText;
    EditText editCarModelEditText;
    EditText editCarNameEditText;
    EditText editCarVINeditText;
    Spinner editCarYearSpinner;
    EditText editCarEngSizeEditText;
    Spinner editCarUnitSpinner;
    Spinner editCarConfigSpinner;
    EditText editCarEngNumberEditText;
    EditText editCarIntervalEditText;
    Spinner editCarOdometerSpinner;
    EditText editCarNotesEditText;

    boolean objectHasChanges = false;

    // Other variables.
    Bitmap bitmap;
    boolean didItSave;
    final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        // Instantiating needed objects.
        dataBundle = getIntent().getExtras();
        utilities = new Utilities();

        // Starting the toolbar.
        Toolbar editCarToolbar = findViewById(R.id.addCarToolbar);
        setSupportActionBar(editCarToolbar);
        editCarToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Car");

        // db stuff so the view can be loaded.
        carDB = new DBManager(this);
        Cursor selectedCar = carDB.getSelectedCarInfo(dataBundle.getInt(schema.COL_CAR_ID));
        selectedCar.moveToFirst();

        // References to UI objects.
        editCarImageButton = findViewById(R.id.addCarImageButton);
        editCarMakeEditText = findViewById(R.id.addCarMakeEditText);
        editCarModelEditText = findViewById(R.id.addCarModelEditText);
        editCarNameEditText = findViewById(R.id.addCarNameEditText);
        editCarVINeditText = findViewById(R.id.addCarVINeditText);
        editCarYearSpinner = findViewById(R.id.addCarYearSpinner);
        editCarEngSizeEditText = findViewById(R.id.addCarEngSizeEditText);
        editCarUnitSpinner = findViewById(R.id.addCarUnitSpinner);
        editCarConfigSpinner = findViewById(R.id.addCarConfigSpinner);
        editCarEngNumberEditText = findViewById(R.id.addCarEngNumberEditText);
        editCarIntervalEditText = findViewById(R.id.addCarIntervalEditText);
        editCarOdometerSpinner = findViewById(R.id.addCarOdometerSpinner);
        editCarNotesEditText = findViewById(R.id.addCarNotesEditText);

        FloatingActionButton fab = findViewById(R.id.addCarFab);

        // Photo stuff  =====================================================================
        if (savedInstanceState != null) {
            Bitmap photo = savedInstanceState.getParcelable("image");
            editCarImageButton.setImageBitmap(photo);
        }

        // Code to prepare a photo for display.
        byte[] byteData = selectedCar.getBlob(selectedCar.getColumnIndex(schema.COL_PHOTO));
        Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        editCarImageButton.setImageBitmap(bm);

        // For each EditText field we are watching for the field contents to change.
        // This is for the purpose of warning the user that a change has not been saved
        // if they hit the hardware back button.
        // If I could make this a separate method in the Utilities class. Reuse of code.
        editCarMakeEditText.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_MAKE)));
        editCarMakeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });


        editCarModelEditText.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_MODEL)));
        editCarModelEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });

        editCarNameEditText.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_NAME)));
        editCarNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });


        // Setting the selected Year on the spinner  ==============================================
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCarYearSpinner.setAdapter(yearAdapter);
        editCarYearSpinner.setOnItemSelectedListener(this);
        // It gets the index and sets the currently stored Year.
        currentCarYear = selectedCar.getString(selectedCar.getColumnIndex(schema.COL_YEAR));
        int yearSetting = getYearSpinnerIndex(currentCarYear);
        editCarYearSpinner.setSelection(yearSetting);

        editCarVINeditText.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_VIN)));
        editCarVINeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });

        editCarEngSizeEditText.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ENG_SIZE)));
        editCarEngSizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });


        // Setting the selected Unit on the spinner ===============================================
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this, R.array.engUnits, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCarUnitSpinner.setAdapter(unitAdapter);
        editCarUnitSpinner.setOnItemSelectedListener(this);
        // It gets the index and sets the currently stored Unit.
        currentCarUnit = selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ENG_UNIT));
        int unitSetting = getUnitSpinnerIndex(currentCarUnit);
        editCarUnitSpinner.setSelection(unitSetting);


        // Setting the selected Engine Configuration on the spinner ===============================
        ArrayAdapter<CharSequence> configAdapter = ArrayAdapter.createFromResource(this, R.array.engConfiguration, android.R.layout.simple_spinner_item);
        configAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCarConfigSpinner.setAdapter(configAdapter);
        editCarConfigSpinner.setOnItemSelectedListener(this);
        // It gets the index and sets the currently stored Engine Configuration.
        currentEngConfig = selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ENG_CONFIG));
        int configSetting = getConfigSpinnerIndex(currentEngConfig);
        editCarConfigSpinner.setSelection(configSetting);

        editCarEngNumberEditText.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ENG_NUMBER)));
        editCarEngNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });

        editCarIntervalEditText.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_OIL_INTERVAL)));
        editCarIntervalEditText.addTextChangedListener(new TextWatcher() {
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

        // Setting the selected Engine Configuration on the spinner ===============================
        ArrayAdapter<CharSequence> intervalAdapter = ArrayAdapter.createFromResource(this, R.array.odo, android.R.layout.simple_spinner_item);
        configAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCarOdometerSpinner.setAdapter(intervalAdapter);
        editCarOdometerSpinner.setOnItemSelectedListener(this);
        // It gets the index and sets the currently stored Engine Configuration.
        currentOdometer = selectedCar.getString(selectedCar.getColumnIndex(schema.COL_ODOMETER_TYPE));
        int intervalSetting = getConfigSpinnerIndex(currentOdometer);
        editCarOdometerSpinner.setSelection(intervalSetting);



        editCarNotesEditText.setText(selectedCar.getString(selectedCar.getColumnIndex(schema.COL_NOTES)));
        editCarNotesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });

        selectedCar.moveToFirst();

        // Photo stuff  =====================================================================
        // Part of the code to preserve a photo when the device gets rotated.
        if (savedInstanceState != null) {
            Bitmap photo = savedInstanceState.getParcelable("image");
            editCarImageButton.setImageBitmap(photo);
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            editCarImageButton.setEnabled(false);
        }

        // Activating the device camera.
        editCarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, REQUEST_IMAGE_CAPTURE);
            }
        });
        // =========================================================================================

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues editCarValues = new ContentValues();

                // Collecting data from UI objects for storage in the db.
                editCarValues.put(schema._ID, dataBundle.getInt(schema.COL_CAR_ID));
                bitmap = ((BitmapDrawable) editCarImageButton.getDrawable()).getBitmap();
                editCarValues.put(schema.COL_PHOTO, utilities.convertImageForStorage(bitmap));
                editCarValues.put(schema.COL_MAKE, editCarMakeEditText.getText().toString());
                editCarValues.put(schema.COL_YEAR, yearData);
                editCarValues.put(schema.COL_MODEL, editCarModelEditText.getText().toString());
                editCarValues.put(schema.COL_NAME, editCarNameEditText.getText().toString());
                editCarValues.put(schema.COL_VIN, editCarVINeditText.getText().toString());
                editCarValues.put(schema.COL_ENG_SIZE, editCarEngSizeEditText.getText().toString());
                editCarValues.put(schema.COL_ENG_UNIT, unitData);
                editCarValues.put(schema.COL_ENG_CONFIG, configData);
                editCarValues.put(schema.COL_ENG_NUMBER, editCarEngNumberEditText.getText().toString());
                editCarValues.put(schema.COL_OIL_INTERVAL, editCarIntervalEditText.getText().toString());
                editCarValues.put(schema.COL_ODOMETER_TYPE, odometerData);
                editCarValues.put(schema.COL_NOTES, editCarNotesEditText.getText().toString());

                // Storing data in db.
                didItSave = carDB.saveCarChanges(editCarValues, dataBundle.getInt(schema.COL_CAR_ID));

                // If it worked display a positive message, then return to the calling view.
                if (didItSave == true) {
                    Toast.makeText(CarEditActivity.this, "Car changes have been saved.", Toast.LENGTH_SHORT).show();

                    // Return Intent.
                    Intent returnIntent = new Intent(getApplicationContext(), CarInfoActivity.class);
                    returnIntent.putExtras(dataBundle);
                    startActivity(returnIntent);
                } // This provides an indication something didn't work correctly.
                else {
                    Toast.makeText(CarEditActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    // Other part of code to store for when the device is rotated.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            editCarImageButton.setImageBitmap(bitmap);
            // To detect if there has been a change.
            objectHasChanges = true;
        }
    }

    // Other part of code for storing a photo for when the device is rotated.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) editCarImageButton.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        outState.putParcelable("image", bitmap);
        super.onSaveInstanceState(outState);
    }



    // Code to provide functionality for menu selections.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Help specific to editing a car.
        if (item.getItemId() == R.id.help) {
            Toast.makeText(getApplicationContext(), "Launcher for the Help page still needs to be written", Toast.LENGTH_LONG).show();
        }

        if (item.getItemId() == android.R.id.home){
            if (objectHasChanges == true) {
                youDidntSave();
            }
            else {
                Intent x = new Intent(getApplicationContext(), CarInfoActivity.class);
                x.putExtras(dataBundle);
                startActivity(x);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Get index number for stored spinner selections. ============================================
    public int getYearSpinnerIndex (String value) {
        int yearIndex = -1;
        String[] yearsArray = getResources().getStringArray(R.array.years);
        for (int i = 0; i < yearsArray.length; i++) {
            if (value.equalsIgnoreCase(yearsArray[i])) {
                yearIndex = i;
                return i;
            }
        }
        return yearIndex;
    }

    public int getUnitSpinnerIndex (String value) {
        int unitIndex = -1;
        String[] unitArray = getResources().getStringArray(R.array.engUnits);
        for (int i = 0; i < unitArray.length; i++) {
            if (value.equalsIgnoreCase(unitArray[i])) {
                unitIndex = i;
                return i;
            }
        }
        return unitIndex;
    }

    public int getConfigSpinnerIndex (String value) {
        int configIndex = -1;
        String[] configArray = getResources().getStringArray(R.array.engConfiguration);
        for (int i = 0; i < configArray.length; i++) {
            if (value.equalsIgnoreCase(configArray[i])) {
                configIndex = i;
                return i;
            }
        }
        return configIndex;
    }

    public int getIntervalSpinnerIndex (String value) {
        int intervalIndex = -1;
        String[] configArray = getResources().getStringArray(R.array.odo);
        for (int i = 0; i < configArray.length; i++) {
            if (value.equalsIgnoreCase(configArray[i])) {
                intervalIndex = i;
                return i;
            }
        }
        return intervalIndex;
    }

    // ============================================================================================

    // Grabbing values from spinners for storage in db.
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (parent.getId() == R.id.addCarYearSpinner) {
            yearData = parent.getSelectedItem().toString();
            if (currentCarYear.compareTo(yearData) != 0) {
                objectHasChanges = true;
            }
        }
        if (parent.getId() == R.id.addCarUnitSpinner) {
            unitData = parent.getSelectedItem().toString();
            // Testing to see if anything has changed in case the user taps the hardware back button.
            if (currentCarUnit.compareTo(unitData) != 0) {
                objectHasChanges = true;
            }
        }
        if (parent.getId() == R.id.addCarConfigSpinner) {
            configData = parent.getSelectedItem().toString();
            if (currentEngConfig.compareTo(configData) != 0) {
                objectHasChanges = true;
            }
        }

        if (parent.getId() == R.id.addCarOdometerSpinner) {
            odometerData = parent.getSelectedItem().toString();
            if (currentOdometer.compareTo(odometerData) != 0) {
                objectHasChanges = true;
            }
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "Invalid Selection.", Toast.LENGTH_LONG).show();
    }


    // Takes control of the hardware back button.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (objectHasChanges == true) {
                AlertDialog.Builder notSavedDialog = new AlertDialog.Builder(this);
                notSavedDialog.setTitle(R.string.wait);
                notSavedDialog.setMessage(R.string.didYouSave);
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
                        finish();
                    }
                });
                AlertDialog saved = notSavedDialog.create();
                saved.show();
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
