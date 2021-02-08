package com.woodlandcoders.myclassiccar;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MaintenanceActivity extends AppCompatActivity implements MaintenanceAdapter.MaintenanceOnRVItemClickListener {

    Bundle dataBundle;
    DBManagerSchema schema;
    MaintenanceAdapter adapter;
    DBManager carDB;
    Utilities utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        // Creating needed object
        dataBundle = getIntent().getExtras();
        carDB = new DBManager(this);
        utilities = new Utilities();

        FloatingActionButton fab = findViewById(R.id.maintenanceFab);

        String currentCarYear = dataBundle.getString(schema.COL_YEAR);
        String currentCarMake = dataBundle.getString(schema.COL_MAKE);

        Toolbar oilToolBar = findViewById(R.id.maintenanceToolbar);
        setSupportActionBar(oilToolBar);
        oilToolBar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentCarYear + " " + currentCarMake);

        // Firing up the RecyclerView
        RecyclerView mrv = findViewById(R.id.maintenanceRecyclerView);
        mrv.setLayoutManager(new LinearLayoutManager(this));

        // Getting data from database and converting to an ArrayList.
        Cursor MaintenanceCursor = carDB.getMaintenanceInfo(dataBundle.getInt(schema.COL_CAR_ID));
        ArrayList<MaintenanceContainer> maintenanceList = utilities.CreateMaintenanceListFromCursor(MaintenanceCursor);

        //Establishing the adapter
        adapter = new MaintenanceAdapter(this, maintenanceList, this);
        mrv.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addChangeIntent = new Intent(getApplicationContext(), MaintenanceAddActivity.class);
                addChangeIntent.putExtras(dataBundle);
                startActivity(addChangeIntent);
            }
        });
    }


    // Code to provide functionality for menu selections.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateOil = getMenuInflater();
        inflateOil.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Provides functionality for the menu items.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.homeBoy) {
            Intent backNavIntent = new Intent(getApplicationContext(), MainActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }

        // Back-arrow functionality.
        if (item.getItemId() == android.R.id.home) {
            Intent backNavIntent = new Intent(getApplicationContext(), RecordSelectActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), R.string.useUpNav, Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onCellClick(int cellContentID) {
            Intent oilInstanceIntent = new Intent(getApplicationContext(), MaintenanceEditActivity.class);
            oilInstanceIntent.putExtras(dataBundle);
            oilInstanceIntent.putExtra("ID", cellContentID);
            startActivity(oilInstanceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }
}
