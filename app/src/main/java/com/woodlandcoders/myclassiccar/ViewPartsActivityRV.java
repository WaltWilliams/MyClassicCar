package com.woodlandcoders.myclassiccar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
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
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewPartsActivityRV extends AppCompatActivity implements ViewPartsActivityAdapterRV.ViewPartsOnRVItemClickListener{

    DBManager carDB;
    DBManagerSchema schema;
    ViewPartsActivityAdapterRV adapter;
    Utilities utilities;
    Bundle dataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parts_rv);

        // Instantiating need objects.
        // Using query column names as keys to keep things consistent.
        dataBundle = getIntent().getExtras();
        utilities = new Utilities();
        carDB = new DBManager(this);

        FloatingActionButton fab = findViewById(R.id.vpFab);

        // Getting info for displaying in the toolbar.
        String currentCarMake = dataBundle.getString(schema.COL_MAKE);
        String currentCarYear  = dataBundle.getString(schema.COL_YEAR);

        // Starting the toolbar
        Toolbar viewPartToolbar = findViewById(R.id.viewPartsToolbar);
        setSupportActionBar(viewPartToolbar);
        viewPartToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentCarYear + " " + currentCarMake);

        // Starting the RecyclerView
        RecyclerView vprv = findViewById(R.id.viewPartsRecyclerView);
        vprv.setLayoutManager(new LinearLayoutManager(this));

        Cursor partsCursor = carDB.getPartsForCars(dataBundle.getInt(schema.COL_CAR_ID));
        ArrayList<ViewPartsContainer> partsList = utilities.CreateViewPartsListFromCursor(partsCursor);

        adapter = new ViewPartsActivityAdapterRV(this, partsList, this);
        vprv.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor allParts = carDB.getAdjustedOemParts(dataBundle.getInt(schema.COL_CAR_ID));
                int dd = allParts.getCount();
                if (allParts.getCount() > 0) {
                    if (allParts.getCount() > 0) {

                        final AlertDialog.Builder addPartDialog = new AlertDialog.Builder(ViewPartsActivityRV.this);
                        addPartDialog.setTitle("Add OEM Part");
                        addPartDialog.setMessage("You can choose from an existing part already assigned to another car, or add a new part.");
                        addPartDialog.setIcon(android.R.drawable.ic_dialog_info);
                        addPartDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent addPartIntent = new Intent(getApplicationContext(), OemPartAddActivity.class);
                                addPartIntent.putExtras(dataBundle);
                                startActivity(addPartIntent);
                            }
                        });
                        addPartDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        addPartDialog.setNeutralButton("Existing", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent existingPartIntent = new Intent(getApplicationContext(), ExistingPartActivityRV.class);
                                existingPartIntent.putExtras(dataBundle);
                                startActivity(existingPartIntent);
                            }
                        });

                        AlertDialog deleteWarning = addPartDialog.create();
                        deleteWarning.show();
                    }
                }
                else {
                    Intent addPartIntent = new Intent(getApplicationContext(), OemPartAddActivity.class);
                    addPartIntent.putExtras(dataBundle);
                    startActivity(addPartIntent);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater viewPartInflater = getMenuInflater();
        viewPartInflater.inflate(R.menu.plain_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.help) {
            Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
            dataBundle.putString("help", getString(R.string.ViewPartActivity_help));
            dataBundle.putString("pageName", "View Parts - Help");
            dataBundle.putString("FromActivity", "ViewOEM");
            helpIntent.putExtras(dataBundle);
            startActivity(helpIntent);
        }

        if (item.getItemId() == R.id.homeView) {
            Intent backNavIntent = new Intent(getApplicationContext(), MainActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }

        if (item.getItemId() == android.R.id.home) {
            Intent backNavIntent = new Intent(getApplicationContext(), RecordSelectActivity.class);
            backNavIntent.putExtras(dataBundle);
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

    @Override
    public void onCellClick(int cellContentID) {
        Intent vrIntent = new Intent(getApplicationContext(), SinglePartActivity.class);
        vrIntent.putExtras(dataBundle);
        vrIntent.putExtra("partIdFromCell", cellContentID);
        startActivity(vrIntent);
    }
}
