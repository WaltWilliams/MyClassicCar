package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class OemPartAddActivity extends AppCompatActivity {

    // References to Objects used in the class.
    private DBManagerSchema schema;
    private Utilities utilities;
    private DBManager carDB;
    private Bundle dataBundle;

    // References to UI objects.
    private EditText addPartNameEditText;
    private EditText addPartNumEditText;
    private EditText addPartMfgEditText;
    private EditText addPartBCEditText;
    private EditText addPartNotesEditText;
    private ImageButton addPartImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part);

        // Instantiating needed objects
        dataBundle = getIntent().getExtras();
        utilities = new Utilities();
        carDB = new DBManager(this);

        // Starting the toolbar
        Toolbar viewPartToolbar = findViewById(R.id.addPartToolbar);
        setSupportActionBar(viewPartToolbar);
        viewPartToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the text  at the top of the activity to identify the
        // car you're currently displaying data for.
        getSupportActionBar().setTitle(dataBundle.getString(schema.COL_YEAR) + " " + dataBundle.getString(schema.COL_MAKE));


        // Instantiating UI objects  ==============================================================
        addPartNameEditText = findViewById(R.id.addPartNameEditText);
        addPartNumEditText = findViewById(R.id.addPartNumEditText);
        addPartMfgEditText = findViewById(R.id.addPartMfgEditText);
        addPartBCEditText = findViewById(R.id.addPartBCEditText);
        addPartNotesEditText = findViewById(R.id.addPartNotesEditText);
        addPartImageButton = findViewById(R.id.addPartImageButton);


        addPartMfgEditText.setText(dataBundle.getString(schema.COL_MAKE));
        addPartMfgEditText.setEnabled(true);
        addPartMfgEditText.setTextColor(Color.parseColor("#000000"));

        FloatingActionButton fab = findViewById(R.id.addPartFab);
        // ========================================================================================


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            addPartImageButton.setEnabled(false);
        }

        // Part of the code to store a photo for when the device is rotated.
        if (savedInstanceState != null) {
            Bitmap photo = savedInstanceState.getParcelable("image");
            addPartImageButton.setImageBitmap(photo);
        }

        // Code to activate the device camera.
        addPartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, utilities.getREQUEST_IMAGE_CAPTURE());
            }
        });

        // An idea for informing new, first time, users of the app
        //utilities.alertDialogPopup(this, dataBundle.getBoolean("trainingTorF"), getString(R.string.trainingDialogTitle), getString(R.string.trainingAddPartActivity));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues addPartValues = new ContentValues();

                // Checking to see if the name has an entry. If not don't store.
                if (!addPartNameEditText.getText().toString().isEmpty()) {
                    Bitmap bitmap = ((BitmapDrawable) addPartImageButton.getDrawable()).getBitmap();
                    addPartValues.put(schema.COL_PHOTO, utilities.convertImageForStorage(bitmap));
                    addPartValues.put(schema.COL_PART_NAME, addPartNameEditText.getText().toString());
                    addPartValues.put(schema.COL_PART_NUMBER, addPartNumEditText.getText().toString());
                    addPartValues.put(schema.COL_MFG, addPartMfgEditText.getText().toString());
                    addPartValues.put(schema.COL_BARCODE, addPartBCEditText.getText().toString());
                    addPartValues.put(schema.COL_NOTES, addPartNotesEditText.getText().toString());

                    boolean didItSave = carDB.addPart(addPartValues, dataBundle.getInt(schema.COL_CAR_ID));

                    if (didItSave == true) {
                        Intent returnIntent = new Intent(getApplicationContext(), ViewPartsActivityRV.class);
                        returnIntent.putExtras(dataBundle);
                        startActivity(returnIntent);
                        Toast.makeText(OemPartAddActivity.this, "OEM Part Has Been Saved.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(OemPartAddActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(OemPartAddActivity.this, "A Part must at least have a name.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Even though there is no menu these next two functions are
    // need to be here to insure the correct back-arrow operation.
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
            Intent backNavIntent = new Intent(getApplicationContext(), ViewPartsActivityRV.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    // Retrieving the photo from the camera.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            addPartImageButton.setImageBitmap(bitmap);
        }
    }

    // The other part of the code to store a photo for when the device is rotated.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) addPartImageButton.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        outState.putParcelable("image", bitmap);
        super.onSaveInstanceState(outState);
    }


    // Takes control of the hardware back button.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();
            Intent x = new Intent(getApplicationContext(), ViewPartsActivityRV.class);
            x.putExtras(dataBundle);
            startActivity(x);
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }

}
