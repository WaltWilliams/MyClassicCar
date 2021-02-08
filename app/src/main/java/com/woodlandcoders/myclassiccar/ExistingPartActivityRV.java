package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;

public class ExistingPartActivityRV extends AppCompatActivity implements ExistingPartsActivityAdapterRV.OnRVexpItemClickListener{

    ExistingPartsActivityAdapterRV epa;
    DBManager carsDB;
    DBManagerSchema schema;
    Utilities utilities; // Where the Cursor to ArrayList conversions are located.
    Bundle dataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_part_rv);

        // Starting toolbar
        Toolbar existingPartToolbar = findViewById(R.id.existingPartToolbar);
        setSupportActionBar(existingPartToolbar);
        existingPartToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Available Parts");

        carsDB = new DBManager(this);
        dataBundle = getIntent().getExtras();
        utilities = new Utilities();

        RecyclerView existingRV = findViewById(R.id.existingPartRV);
        existingRV.setLayoutManager(new LinearLayoutManager(this));

        Cursor allParts = carsDB.getAdjustedOemParts(dataBundle.getInt(schema.COL_CAR_ID));
        ArrayList exPtList = utilities.CreateExistingPartsListFromCursor(allParts);

        epa = new ExistingPartsActivityAdapterRV(this, exPtList, this);
        existingRV.setAdapter(epa);
    }


    // Starting the menu used in the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater existingInflater = getMenuInflater();
        existingInflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.homeBoy) {
            Intent homeNavIntent = new Intent(getApplicationContext(), MainActivity.class);
            homeNavIntent.putExtras(dataBundle);
            startActivity(homeNavIntent);
        }

        if (item.getItemId() == android.R.id.home) {
            Intent backNavIntent = new Intent(getApplicationContext(), RecordSelectActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    // Takes control of the hardware back button.
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onCellClick(int partID) {       // Save once the cell is clicked.

        final int mPartID = partID;

        // Using an Alert box to ask if mapping an existing part to this car is what they want.
        AlertDialog.Builder existingPartsDialog = new AlertDialog.Builder(ExistingPartActivityRV.this);
        existingPartsDialog.setTitle("Are you sure . . ");
        existingPartsDialog.setMessage("you want to include this part with this car?");
        existingPartsDialog.setIcon(android.R.drawable.ic_dialog_info);
        existingPartsDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int carID = dataBundle.getInt(schema.COL_CAR_ID);
                Intent existingPartIntent = new Intent(getApplicationContext(), ViewPartsActivityRV.class);

                // Gathering car and part IDs so the call
                // to the db query has what it needs.
                ContentValues cv = new ContentValues();
                cv.put("carID", carID);
                cv.put("partID", mPartID);

                // There is no need for a returned cursor as it is merely
                // making an entry into the "CarsToParts" table before it
                // returns to ViewPartsActivityRV and displays all the parts
                // mapped to this car including the newly mapped one.
                carsDB.selectExistingPart(cv);

                existingPartIntent.putExtras(dataBundle);
                startActivity(existingPartIntent);
            }
        });
        existingPartsDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); // Do nothing.
            }
        });
        AlertDialog addWarning = existingPartsDialog.create();
        addWarning.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        carsDB.close();
    }

}
