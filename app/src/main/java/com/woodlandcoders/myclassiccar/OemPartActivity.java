package com.woodlandcoders.myclassiccar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class OemPartActivity extends AppCompatActivity {

    DBManager carDB;
    DBManagerSchema schema;
    Utilities utilities = new Utilities();
    Bundle dataBundle;
    Bitmap bitmap;
    Bitmap bm;

    private ImageView mainPartImageView;
    private TextView mainPartNameTextView;
    private TextView mainPartNumTextView;
    private TextView mainPartMfgTextView;
    private TextView mainPartBCTextView;
    private TextView mainPartNotesTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oem_part);

        // Instantiating needed objects
        // Using query column names as keys to keep things consistent.
        dataBundle = getIntent().getExtras();
        carDB = new DBManager(this);
        Cursor part = carDB.getPart(dataBundle.getInt("partIdFromCell"));

        // Starting the toolbar.
        Toolbar tpmToolbar = findViewById(R.id.oemPartToolbar);
        setSupportActionBar(tpmToolbar);
        tpmToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(dataBundle.getString(schema.COL_YEAR) + " " + dataBundle.getString(schema.COL_MAKE));

        // Referencing UI objects.
        mainPartImageView = findViewById(R.id.mainPartImageView);
        mainPartNameTextView = findViewById(R.id.mainPartNameTextView);
        mainPartNumTextView = findViewById(R.id.mainPartNumTextView);
        mainPartMfgTextView = findViewById(R.id.mainPartMfgTextView);
        mainPartBCTextView = findViewById(R.id.mainPartBCTextView);
        mainPartNotesTextView = findViewById(R.id.mainPartNotesTextView);

        FloatingActionButton fab = findViewById(R.id.mainPartInfoFab);

        // Converting image for displaying in the ImageView.
        bm = utilities.convertImageForDisplay(part.getBlob(part.getColumnIndex(schema.COL_PHOTO)));
        mainPartImageView.setImageBitmap(bm);

        mainPartNameTextView.setText(part.getString(part.getColumnIndex(schema.COL_PART_NAME)));
        mainPartNumTextView.setText(part.getString(part.getColumnIndex(schema.COL_PART_NUMBER)));
        mainPartMfgTextView.setText(part.getString(part.getColumnIndex(schema.COL_MFG)));
        mainPartBCTextView.setText(part.getString(part.getColumnIndex(schema.COL_BARCODE)));
        mainPartNotesTextView.setText(part.getString(part.getColumnIndex(schema.COL_NOTES)));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editOEM = new Intent(getApplicationContext(), OemPartEditActivity.class);
                editOEM.putExtras(dataBundle);
                startActivity(editOEM);

            }
        });

    }


    // Starting the menu for the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater tpmInflater = getMenuInflater();
        tpmInflater.inflate(R.menu.oem_part_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Code to make menu item function.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.delete_part) {
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
            deleteDialog.setTitle(R.string.are_you_sure);
            deleteDialog.setMessage(R.string.not_reversible_part);
            deleteDialog.setIcon(android.R.drawable.ic_dialog_info);
            deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Query to delete OEM parts for this car.
                    boolean TorF = carDB.deleteOemPart(dataBundle.getInt("partIdFromCell"), dataBundle.getInt(schema.COL_CAR_ID));

                    if (TorF == true) {
                        Intent returnToViewParts = new Intent(getApplicationContext(), ViewPartsActivityRV.class);
                        returnToViewParts.putExtras(dataBundle);
                        startActivity(returnToViewParts);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Something went wrong. You may now have unreachable entries in the storage files.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog deleteWarning = deleteDialog.create();
            deleteWarning.show();
        }

        if (item.getItemId() == android.R.id.home) {
            Intent backNavIntent = new Intent(getApplicationContext(), SinglePartActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }

        // Go to the MainActivity
        if (item.getItemId() == R.id.homeView) {
            Intent backNavIntent = new Intent(getApplicationContext(), MainActivity.class);
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
