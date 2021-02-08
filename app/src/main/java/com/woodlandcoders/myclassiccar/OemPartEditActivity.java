package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class OemPartEditActivity extends AppCompatActivity {

    // References for needed objects.
    DBManager carDB;
    DBManagerSchema schema;
    Bundle dataBundle;
    Bitmap bitmap;
    Utilities utilities = new Utilities();

    // References for UI objects.
    private ImageButton addPartImageButton;
    private EditText addPartNameEditText;
    private EditText addPartNumEditText;
    private EditText addPartMfgEditText;
    private EditText addPartBCEditText;
    private EditText addPartNotesEditText;
    boolean objectHasChanges = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part);

        // Instantiating needed objects.
        // Using query column names as keys to keep things consistent.
        dataBundle = getIntent().getExtras();
        utilities = new Utilities();
        carDB = new DBManager(this);
        Cursor part = carDB.getPart(dataBundle.getInt("partIdFromCell"));


        // Starting the toolbar.
        Toolbar tpmToolbar = findViewById(R.id.addPartToolbar);
        setSupportActionBar(tpmToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tpmToolbar.setContentInsetStartWithNavigation(0);
        tpmToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objectHasChanges == true) {
                    youDidntSave();
                }
                else {
                    Intent x = new Intent(getApplicationContext(), OemPartActivity.class);
                    x.putExtras(dataBundle);
                    startActivity(x);
                }
            }
        });

        getSupportActionBar().setTitle(dataBundle.getString(schema.COL_YEAR) + " " + dataBundle.getString(schema.COL_MAKE));

        // Referencing UI objects.
        addPartImageButton = findViewById(R.id.addPartImageButton);
        addPartNameEditText = findViewById(R.id.addPartNameEditText);
        addPartNumEditText = findViewById(R.id.addPartNumEditText);
        addPartMfgEditText = findViewById(R.id.addPartMfgEditText);
        addPartBCEditText = findViewById(R.id.addPartBCEditText);
        addPartNotesEditText = findViewById(R.id.addPartNotesEditText);

        FloatingActionButton fab = findViewById(R.id.addPartFab);

        // Loading UI objects
        // Converting image from db for display in ImageView
        byte[] byteData = part.getBlob(part.getColumnIndex(schema.COL_PHOTO));
        Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        addPartImageButton.setImageBitmap(bm);

        addPartNameEditText.setText(part.getString(part.getColumnIndex(schema.COL_PART_NAME)));
        addPartNameEditText.addTextChangedListener(new TextWatcher() {
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


        addPartNumEditText.setText(part.getString(part.getColumnIndex(schema.COL_PART_NUMBER)));
        addPartNumEditText.addTextChangedListener(new TextWatcher() {
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

        addPartMfgEditText.setText(part.getString(part.getColumnIndex(schema.COL_MFG)));
        addPartMfgEditText.setEnabled(true);
        addPartMfgEditText.setTextColor(Color.parseColor("#000000"));
        addPartMfgEditText.addTextChangedListener(new TextWatcher() {
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

        addPartBCEditText.setText(part.getString(part.getColumnIndex(schema.COL_BARCODE)));
        addPartBCEditText.addTextChangedListener(new TextWatcher() {
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

        addPartNotesEditText.setText(part.getString(part.getColumnIndex(schema.COL_NOTES)));
        addPartNotesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // no nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                objectHasChanges = true;
            }
        });


        // Photo stuff  =====================================================================
        if (savedInstanceState != null) {
            Bitmap photo = savedInstanceState.getParcelable("image");
            addPartImageButton.setImageBitmap(photo);
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            addPartImageButton.setEnabled(false);
        }

        // This is for activating the device camers.
        addPartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, utilities.getREQUEST_IMAGE_CAPTURE());
            }
        });
        // ==================================================================================

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues mainPartValues = new ContentValues();

                // Checking to see if the name has an entry. If not don't store, do nothing.
                if (!addPartNameEditText.getText().toString().isEmpty()) {

                    bitmap = ((BitmapDrawable) addPartImageButton.getDrawable()).getBitmap();
                    mainPartValues.put(schema.COL_PHOTO, utilities.convertImageForStorage(bitmap));
                    mainPartValues.put(schema.COL_PART_NAME, addPartNameEditText.getText().toString());
                    mainPartValues.put(schema.COL_PART_NUMBER, addPartNumEditText.getText().toString());
                    mainPartValues.put(schema.COL_MFG, addPartMfgEditText.getText().toString());
                    mainPartValues.put(schema.COL_BARCODE, addPartBCEditText.getText().toString());
                    mainPartValues.put(schema.COL_NOTES, addPartNotesEditText.getText().toString());

                    boolean didItSave = carDB.updatePart(mainPartValues, dataBundle.getInt("partIdFromCell"));

                    // If the data was stored return to the previous view.
                    if (didItSave == true) {
                        Intent returnIntent = new Intent(getApplicationContext(), OemPartActivity.class);
                        returnIntent.putExtras(dataBundle);
                        startActivity(returnIntent);
                        Toast.makeText(OemPartEditActivity.this, "Changes To This OEM Part Has Been Saved.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OemPartEditActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(OemPartEditActivity.this, "A Part must at least have a name.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    // This is for retrieving a photo once the device camera has taken a photo.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            addPartImageButton.setImageBitmap(bitmap);
            objectHasChanges = true;
        }
    }


    // Other part of code for storing a photo for when the device is rotated.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) addPartImageButton.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        outState.putParcelable("image", bitmap);
        super.onSaveInstanceState(outState);
    }


    // Code for the functionality of the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.help) {
            Intent goHomeIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goHomeIntent);
        }

        if (item.getItemId() == android.R.id.home){
            if (objectHasChanges == true) {
                youDidntSave();
            }
            else {
                Intent x = new Intent(getApplicationContext(), OemPartActivity.class);
                x.putExtras(dataBundle);
                startActivity(x);
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
