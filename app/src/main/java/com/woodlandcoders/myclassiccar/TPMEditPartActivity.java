package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//////////////////////////////////////////////////////////////////////////////////
// TPM = third Party Manufacture
//////////////////////////////////////////////////////////////////////////////////


public class TPMEditPartActivity extends AppCompatActivity {

    // References to needed objects.
    DBManager carDB;
    DBManagerSchema schema;
    Bundle dataBundle;

    // References to UI objects that are needed globally.
    EditText TPMPartNumEditText;
    EditText TPMPartSourceEditText;
    EditText TPMmfgEditText;
    EditText TPMbarcodeEditText;
    EditText TPMPartNoteEditText;

    boolean objectHasChanges = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpmpart_add_edit);

        // Using query column names as keys to keep things consistent.
        dataBundle = getIntent().getExtras();
        // Instantiating needed objects.
        carDB = new DBManager(this);
        Cursor part = carDB.getSingleTpmPart(dataBundle.getInt("partIdFromTpmCell"));

        // Starting the toolbar.
        Toolbar tpmEditToolbar = findViewById(R.id.addTpmPartToolbar);
        setSupportActionBar(tpmEditToolbar);
        tpmEditToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(dataBundle.getString(schema.COL_YEAR) + " " + dataBundle.getString(schema.COL_MAKE));

        // Referencing UI objects.
        TPMPartSourceEditText = findViewById(R.id.TPMPartSourceEditText);
        TPMPartNumEditText = findViewById(R.id.TPMPartNumEditText);
        TPMmfgEditText = findViewById(R.id.TPMmfgEditText);
        TPMbarcodeEditText = findViewById(R.id.TPMbarcodeEditText);
        TPMPartNoteEditText = findViewById(R.id.TPMPartNoteEditText);
        FloatingActionButton fab = findViewById(R.id.tpmAddFab);

        // Loading UI objects from Cursor received after a db query.
        TPMPartSourceEditText.setText(part.getString(part.getColumnIndex(schema.COL_SOURCE)));
        TPMPartSourceEditText.addTextChangedListener(new TextWatcher() {
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


        TPMPartNumEditText.setText(part.getString(part.getColumnIndex(schema.COL_PART_NUMBER)));
        TPMPartNumEditText.addTextChangedListener(new TextWatcher() {
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

        TPMmfgEditText.setText(part.getString(part.getColumnIndex(schema.COL_MFG)));
        TPMmfgEditText.addTextChangedListener(new TextWatcher() {
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

        TPMbarcodeEditText.setText(part.getString(part.getColumnIndex(schema.COL_BARCODE)));
        TPMbarcodeEditText.addTextChangedListener(new TextWatcher() {
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

        TPMPartNoteEditText.setText(part.getString(part.getColumnIndex(schema.COL_NOTES)));
        TPMPartNoteEditText.addTextChangedListener(new TextWatcher() {
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

        // Setting custom "Source" label.
        TextView TPMPartSourceLabelTextView = findViewById(R.id.TPMPartSourceLabelTextView);
        TPMPartSourceLabelTextView.setText("Other Source For Part: " + dataBundle.getString(schema.COL_PART_NAME));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues tpmValues = new ContentValues();

                // Getting altered values from UI objects for storage.
                ContentValues updatePartValues = new ContentValues();
                updatePartValues.put(schema.COL_SOURCE, TPMPartSourceEditText.getText().toString());
                updatePartValues.put(schema.COL_PART_NUMBER, TPMPartNumEditText.getText().toString());
                updatePartValues.put(schema.COL_MFG, TPMmfgEditText.getText().toString());
                updatePartValues.put(schema.COL_BARCODE, TPMbarcodeEditText.getText().toString());
                updatePartValues.put(schema.COL_NOTES, TPMPartNoteEditText.getText().toString());

                // Attempting to store in db collected values.
                boolean didItSave = carDB.updateTpmPart(updatePartValues, dataBundle.getInt("partIdFromTpmCell"));

                // If storage was successful display a Toast and return to calling activity.
                if (didItSave == true) {
                    Toast.makeText(TPMEditPartActivity.this, "This part source listing has been changed.", Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent(getApplicationContext(), TPMPartActivity.class);
                    returnIntent.putExtras(dataBundle);
                    startActivity(returnIntent);
                } else {
                    Toast.makeText(TPMEditPartActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Code to make menu item function.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (objectHasChanges == true) {
                AlertDialog.Builder notSavedDialog = new AlertDialog.Builder(this);
                notSavedDialog.setTitle(R.string.wait);
                notSavedDialog.setMessage(R.string.didYouSave);
                notSavedDialog.setIcon(android.R.drawable.ic_dialog_info);

                notSavedDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), R.string.tapSaveNoArrow, Toast.LENGTH_LONG).show();
                    }
                });

                notSavedDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent x = new Intent(getApplicationContext(), TPMPartActivity.class);
                        x.putExtras(dataBundle);
                        startActivity(x);
                    }
                });
                AlertDialog saved = notSavedDialog.create();
                saved.show();
            }
            else {
                Intent x = new Intent(getApplicationContext(), TPMPartActivity.class);
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
                        finish();
                    }
                });
                AlertDialog saved = notSavedDialog.create();
                saved.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }
}


