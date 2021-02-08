package com.woodlandcoders.myclassiccar;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class OilChangeActivity extends AppCompatActivity implements OilChangeAdapter.OilOnRVItemClickListener {

    DBManager carDB;
    DBManagerSchema schema;
    OilChangeAdapter adapter;
    Utilities utilities;
    Bundle dataBundle;
    boolean ee = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_change);

        carDB = new DBManager(this);
        utilities = new Utilities();
        dataBundle = getIntent().getExtras();

        FloatingActionButton fab = findViewById(R.id.oilFab);

        String currentCarYear = dataBundle.getString(schema.COL_YEAR);
        String currentCarMake = dataBundle.getString(schema.COL_MAKE);


        Toolbar oilToolBar = findViewById(R.id.oilToolbar);
        setSupportActionBar(oilToolBar);
        oilToolBar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentCarYear + " " + currentCarMake);


        // Firing up the RecyclerView
        RecyclerView orv = findViewById(R.id.oilRecyclerView);
        orv.setLayoutManager(new LinearLayoutManager(this));

        // Getting data from database and converting to an ArrayList.
        Cursor oilCursor = carDB.getOilInfo(dataBundle.getInt(schema.COL_CAR_ID));
        ArrayList<OilChangeContainer> oilList = utilities.CreateOilListFromCursor(this, oilCursor, dataBundle.getInt(schema.COL_CAR_ID));

        //Establishing the adapter
        adapter = new OilChangeAdapter(this, oilList, this);
        orv.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addChangeIntent = new Intent(getApplicationContext(), OilChangeAddActivity.class);
                addChangeIntent.putExtras(dataBundle);
                startActivity(addChangeIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateOil = getMenuInflater();
        inflateOil.inflate(R.menu.record_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.homeHome) {
            Intent backNavIntent = new Intent(getApplicationContext(), MainActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }

        if(item.getItemId() == R.id.enableEditing) {
            ee = true;
        }

        if (item.getItemId() == android.R.id.home) {
            Intent backNavIntent = new Intent(getApplicationContext(), RecordSelectActivity.class);
            backNavIntent.putExtras(dataBundle);
            startActivity(backNavIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCellClick(String cellContentID) {
        if(ee == true) {
            Intent oilInstanceIntent = new Intent(getApplicationContext(), OilChangeEditActivity.class);
            oilInstanceIntent.putExtras(dataBundle);
            oilInstanceIntent.putExtra("ID", cellContentID);
            startActivity(oilInstanceIntent);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.to_edit, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }
}
