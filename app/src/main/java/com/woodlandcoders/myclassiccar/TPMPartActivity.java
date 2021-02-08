package com.woodlandcoders.myclassiccar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

//////////////////////////////////////////////////////////////////////////////////
// TPM = third Party Manufacture
//////////////////////////////////////////////////////////////////////////////////

public class TPMPartActivity extends AppCompatActivity {

    DBManager carDB;
    DBManagerSchema schema;
    Bundle dataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpmpart);

        // Instantiating needed objects.
        // Using query column names as keys to keep things consistent.
        dataBundle = getIntent().getExtras();
        carDB = new DBManager(this);
        Cursor part = carDB.getSingleTpmPart(dataBundle.getInt("partIdFromTpmCell"));

        // Starting the toolbar.
        Toolbar tpmToolbar = findViewById(R.id.tpmPartToolbar);
        setSupportActionBar(tpmToolbar);
        tpmToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(dataBundle.getString(schema.COL_YEAR) + " " + dataBundle.getString(schema.COL_MAKE));

        // Referencing the UI objects.
        TextView tpmSrcTextView = findViewById(R.id.tpmSrcTextView);
        TextView tpmPartNumTextView = findViewById(R.id.tpmPartNumTextView);
        TextView tpmMfgTextView = findViewById(R.id.tpmMfgTextView);
        TextView tpmUpcTextView = findViewById(R.id.tpmUpcTextView);
        TextView tpmNotesTextView = findViewById(R.id.tpmNotesTextView);
        // Custom setting for "Source" label
        TextView tpmSrcLabletextView = findViewById(R.id.tpmSrcLabletextView);
        // Edit Button
        FloatingActionButton fab = findViewById(R.id.tpmFab);

        // Loading the UI objects from a Cursor.
        tpmSrcTextView.setText(part.getString(part.getColumnIndex(schema.COL_SOURCE)));
        tpmPartNumTextView.setText(part.getString(part.getColumnIndex(schema.COL_PART_NUMBER)));
        tpmMfgTextView.setText(part.getString(part.getColumnIndex(schema.COL_MFG)));
        tpmUpcTextView.setText(part.getString(part.getColumnIndex(schema.COL_BARCODE)));
        tpmNotesTextView.setText(part.getString(part.getColumnIndex(schema.COL_NOTES)));
        // Custom setting for "Source" label
        tpmSrcLabletextView.setText("Other Source For Part: " + dataBundle.getString(schema.COL_PART_NAME));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editTPMIntent = new Intent(getApplicationContext(), TPMEditPartActivity.class);
                editTPMIntent.putExtras(dataBundle);
                startActivity(editTPMIntent);
            }
        });

    }

    // Starting the menu for the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater tpmInflater = getMenuInflater();
        tpmInflater.inflate(R.menu.tpm_part_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Provides functionality for the menu items.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_part) {

            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
            deleteDialog.setTitle(R.string.are_you_sure);
            deleteDialog.setMessage(R.string.not_reversible_source);
            deleteDialog.setIcon(android.R.drawable.ic_dialog_info);
            deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    carDB.deleteTPMPart(dataBundle.getInt("partIdFromTpmCell"));
                    Intent returnIntent = new Intent(getApplicationContext(), SinglePartActivity.class);
                    returnIntent.putExtras(dataBundle);
                    startActivity(returnIntent);
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
