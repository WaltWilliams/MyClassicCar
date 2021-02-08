package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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


//////////////////////////////////////////////////////////////////////////////////
// TPM = third Party Manufacture
//////////////////////////////////////////////////////////////////////////////////

public class TPMAddPartActivity extends AppCompatActivity {

    // References to objects used in thi class
    DBManagerSchema schema;
    DBManager carDB;
    Bundle dataBundle;
    Utilities utilities;

    // References to UI objects to make them globally available.
    EditText TPMPartSourceEditText;
    EditText TPMPartNumEditText;
    EditText TPMmfgEditText;
    EditText TPMbarcodeEditText;
    EditText TPMPartNoteEditText;

    boolean objectHasChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpmpart_add_edit);

        // Instantiating needed objects.
        carDB = new DBManager(this);
        utilities = new Utilities();
        // This uses schema column names as keys to keep things consistent.
        dataBundle = getIntent().getExtras();

        // Starting the toolbar
        Toolbar tpmToolbar = findViewById(R.id.addTpmPartToolbar);
        setSupportActionBar(tpmToolbar);
        tpmToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(dataBundle.getString(schema.COL_YEAR) + " " + dataBundle.getString(schema.COL_MAKE));

        // References to UI objects.
        TPMPartSourceEditText = findViewById(R.id.TPMPartSourceEditText);
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

        TPMPartNumEditText = findViewById(R.id.TPMPartNumEditText);
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

        TPMmfgEditText = findViewById(R.id.TPMmfgEditText);
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

        TPMbarcodeEditText = findViewById(R.id.TPMbarcodeEditText);
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

        TPMPartNoteEditText = findViewById(R.id.TPMPartNoteEditText);
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

        FloatingActionButton fab = findViewById(R.id.tpmAddFab);

        // Setting custom "Source" label.
        TextView TPMPartSourceLabelTextView = findViewById(R.id.TPMPartSourceLabelTextView);
        TPMPartSourceLabelTextView.setText("Other Source For Part: " + dataBundle.getString(schema.COL_PART_NAME));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues tpmValues = new ContentValues();

                // Loading the ContentValues object and passing it to a method that puts in the db.
                if (!(TPMPartSourceEditText.getText().toString().isEmpty() && TPMPartNumEditText.getText().toString().isEmpty())) {
                    tpmValues.put(schema.COL_PART_ID, dataBundle.getInt("partIdFromCell"));
                    tpmValues.put(schema.COL_SOURCE, TPMPartSourceEditText.getText().toString());
                    tpmValues.put(schema.COL_PART_NUMBER, TPMPartNumEditText.getText().toString());
                    tpmValues.put(schema.COL_MFG, TPMmfgEditText.getText().toString());
                    tpmValues.put(schema.COL_BARCODE, TPMbarcodeEditText.getText().toString());
                    tpmValues.put(schema.COL_NOTES, TPMPartNoteEditText.getText().toString());

                    boolean didItSave = carDB.addTpmPart(tpmValues);

                    // If it worked go back to where we were.
                    if (didItSave == true) {
                        Intent returnSinglePart = new Intent(getApplicationContext(), SinglePartActivity.class);
                        returnSinglePart.putExtras(dataBundle);
                        startActivity(returnSinglePart);
                        Toast.makeText(getApplicationContext(), "Part saved", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(TPMAddPartActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(TPMAddPartActivity.this, "A Third Party party must at least have a\nManufacture name and Part number.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    // Starting the menu for use in the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateTPMmenu = getMenuInflater();
        inflateTPMmenu.inflate(R.menu.no_drop_down_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Code to make the menu items function.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (objectHasChanges == true) {
                AlertDialog.Builder notSavedDialog = new AlertDialog.Builder(this);
                notSavedDialog.setTitle("Wait!");
                notSavedDialog.setMessage(R.string.didYouSaveNew);
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
                        Intent x = new Intent(getApplicationContext(), SinglePartActivity.class);
                        x.putExtras(dataBundle);
                        startActivity(x);
                    }
                });
                AlertDialog saved = notSavedDialog.create();
                saved.show();
            }
            else {
                Intent x = new Intent(getApplicationContext(), SinglePartActivity.class);
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
                notSavedDialog.setTitle(R.string.wait);
                notSavedDialog.setMessage(R.string.didYouSaveNew);
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
                        Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();
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
