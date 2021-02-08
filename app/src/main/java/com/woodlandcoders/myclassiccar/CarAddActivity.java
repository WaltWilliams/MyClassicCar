package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;




public class CarAddActivity extends AppCompatActivity implements OnItemSelectedListener{

    // References to a couple of objects that need to be global.
    private DBManagerSchema schema;
    private DBManager carDB;
    private Utilities utilities;
    private Bundle dataBundle;

    // Variables for getting data from spinners.
    // Method near the bottom.
    private String yearData;
    private String unitData;
    private String configData;
    private String odoData;

    // References to UI objects and a couple variables
    // so they are globally available.
    private EditText addCarMakeEditText;
    private EditText addCarModelEditText;
    private EditText addCarNameEditText;
    private EditText addCarVINeditText;
    private EditText addCarEngSizeEditText;
    private EditText addCarEngNumberEditText;
    private EditText addCarIntervalEditText;
    private EditText addCarNotesEditText;
    private ImageButton addCarImageButton;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        // Starting the Toolbar
        Toolbar addCarToolbar = findViewById(R.id.addCarToolbar);
        setSupportActionBar(addCarToolbar);
        addCarToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Car");

        // Instantiating need objects.
        carDB = new DBManager(this);
        utilities = new Utilities();
        dataBundle = getIntent().getExtras();

        // Creating references to UI objects
        addCarMakeEditText = findViewById(R.id.addCarMakeEditText);
        addCarModelEditText = findViewById(R.id.addCarModelEditText);
        addCarNameEditText = findViewById(R.id.addCarNameEditText);
        addCarVINeditText = findViewById(R.id.addCarVINeditText);
        addCarEngSizeEditText = findViewById(R.id.addCarEngSizeEditText);
        addCarEngNumberEditText = findViewById(R.id.addCarEngNumberEditText);
        addCarIntervalEditText = findViewById(R.id.addCarIntervalEditText);
        addCarNotesEditText = findViewById(R.id.addCarNotesEditText);
        addCarImageButton = findViewById(R.id.addCarImageButton);

        FloatingActionButton fab = findViewById(R.id.addCarFab);


        // Photo stuff  =====================================================================
        // // This is part of the code to store a photo during rotation of the device
        if (savedInstanceState != null) {
            Bitmap photo = savedInstanceState.getParcelable("image");
            addCarImageButton.setImageBitmap(photo);
        }
        // Checking to see if the device has a camera.
        // Most likely not needed but its here just in case.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            addCarImageButton.setEnabled(false);
        }
        // =========================================================================================

        // Setting up the three spinners in this activity.
        Spinner yearSpinner = findViewById(R.id.addCarYearSpinner);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(this);

        Spinner unitSpinner = findViewById(R.id.addCarUnitSpinner);
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this, R.array.engUnits, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);
        unitSpinner.setOnItemSelectedListener(this);

        Spinner configSpinner = findViewById(R.id.addCarConfigSpinner);
        ArrayAdapter<CharSequence> configAdapter = ArrayAdapter.createFromResource(this, R.array.engConfiguration, android.R.layout.simple_spinner_item);
        configAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        configSpinner.setAdapter(configAdapter);
        configSpinner.setOnItemSelectedListener(this);

        Spinner odometerSpinner = findViewById(R.id.addCarOdometerSpinner);
        ArrayAdapter<CharSequence> odoAdapter = ArrayAdapter.createFromResource(this, R.array.odo, android.R.layout.simple_spinner_item);
        odoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        odometerSpinner.setAdapter(odoAdapter);
        odometerSpinner.setOnItemSelectedListener(this);

        // Clicking the camera.
        addCarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, utilities.getREQUEST_IMAGE_CAPTURE());
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues addCarValues = new ContentValues();

                // Guarding against empty entries. If the car make is empty don't save
                if ((!addCarMakeEditText.getText().toString().isEmpty()) & (yearData.compareTo("Year") != 0) & !addCarIntervalEditText.getText().toString().isEmpty()) {
                    bitmap = ((BitmapDrawable) addCarImageButton.getDrawable()).getBitmap();
                    addCarValues.put(schema.COL_PHOTO, utilities.convertImageForStorage(bitmap));
                    addCarValues.put(schema.COL_MAKE, addCarMakeEditText.getText().toString());
                    addCarValues.put(schema.COL_YEAR, yearData);
                    addCarValues.put(schema.COL_MODEL, addCarModelEditText.getText().toString());
                    addCarValues.put(schema.COL_NAME, addCarNameEditText.getText().toString());
                    addCarValues.put(schema.COL_VIN, addCarVINeditText.getText().toString());
                    addCarValues.put(schema.COL_ENG_SIZE, addCarEngSizeEditText.getText().toString());
                    addCarValues.put(schema.COL_ENG_UNIT, unitData);
                    addCarValues.put(schema.COL_ENG_CONFIG, configData);
                    addCarValues.put(schema.COL_ENG_NUMBER, addCarEngNumberEditText.getText().toString());
                    addCarValues.put(schema.COL_ODOMETER_TYPE, odoData);
                    addCarValues.put(schema.COL_OIL_INTERVAL, addCarIntervalEditText.getText().toString());
                    addCarValues.put(schema.COL_NOTES, addCarNotesEditText.getText().toString());


                    boolean didItSave = carDB.addCar(addCarValues);

                    // If it saved the data return to the MainActivity.
                    if (didItSave == true) {
                        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(returnIntent);
                        Toast.makeText(CarAddActivity.this, "Car has been saved.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(CarAddActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(CarAddActivity.this, "You must enter a \"Make\", \"Year\" and \"Change Interval\".", Toast.LENGTH_LONG).show();
                }
            }
        });
     }


    // Retrieving the photo from the camera.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            addCarImageButton.setImageBitmap(bitmap);
        }
     }



    // ----------------------------------------------------------------
    // Starting the menu that is at the top of the view.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateAddCar = getMenuInflater();
        inflateAddCar.inflate(R.menu.plain_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Code for the different menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Display help
        if (item.getItemId() == R.id.help) {
            Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
            dataBundle.putString("help", getString(R.string.AddCarActivity_help));
            dataBundle.putString("pageName", "Add Car - Help");
            dataBundle.putString("FromActivity", "AddCar");
            helpIntent.putExtras(dataBundle);
            startActivity(helpIntent);
        }

        if(item.getItemId() == R.id.homeView) {
            Intent backNavIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(backNavIntent);
        }

        if (item.getItemId() == android.R.id.home){
            Intent x = new Intent(getApplicationContext(), MainActivity.class);
            x.putExtras(dataBundle);
            startActivity(x);
        }
        return super.onOptionsItemSelected(item);
    }


    // Getting the data from the spinners . The "if" statements is to
    // differentiating which spinner to grab the data from.
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (parent.getId() == R.id.addCarYearSpinner) {
            yearData = parent.getSelectedItem().toString();
        }
        if (parent.getId() == R.id.addCarUnitSpinner) {
            unitData = parent.getSelectedItem().toString();
        }
        if (parent.getId() == R.id.addCarConfigSpinner) {
            configData = parent.getSelectedItem().toString();
        }
        if(parent.getId() == R.id.addCarOdometerSpinner) {
            odoData = parent.getSelectedItem().toString();
        }
    }

    // I have not see this message displayed even once.
    // This is here just in case.
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "Invalid Selection.", Toast.LENGTH_LONG).show();
    }

    // This is part of the code to store the photo for when the device is rotated.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) addCarImageButton.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        outState.putParcelable("image", bitmap);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Toast.makeText(this, "Use the Up Arrow at the top left to navigate back. ", Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }
}
