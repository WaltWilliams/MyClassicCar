package com.woodlandcoders.myclassiccar;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class RecordSelectActivity extends AppCompatActivity {

    Bundle dataBundle;
    DBManagerSchema schema;
    DBManager carDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_select);

        // Creating needed object
        dataBundle = getIntent().getExtras();

        // Grabbing infor to get car make and year.
        carDB = new DBManager(this);
        Cursor selectedCar = carDB.getSelectedCarInfo(dataBundle.getInt(schema.COL_CAR_ID));

        // Getting info for displaying in the toolbar and passing on in Intents.
        String currentCarMake = selectedCar.getString(selectedCar.getColumnIndex(schema.COL_MAKE));
        String currentCarYear  = selectedCar.getString(selectedCar.getColumnIndex(schema.COL_YEAR));

        // Inserting more data to be passed to the next activity.
        // These two pieces of data are for display in the Toolbar title.
        dataBundle.putString(schema.COL_MAKE, currentCarMake);
        dataBundle.putString(schema.COL_YEAR, currentCarYear);


        // ToolBar
        Toolbar myToolBar = findViewById(R.id.recSelToolbar);
        setSupportActionBar(myToolBar);
        myToolBar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the text  at the top of the activity to identify the
        // car you're currently displaying data for.
        getSupportActionBar().setTitle(currentCarYear + " " + currentCarMake);


        Button oilBT = findViewById(R.id.BTengineOil);
        Button mainRecBT = findViewById(R.id.BTmaintRec);
        Button partsBT = findViewById(R.id.BTpartNum);

        oilBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oilIntent = new Intent(getApplicationContext(), OilChangeActivity.class);
                oilIntent.putExtras(dataBundle);
                startActivity(oilIntent);
            }
        });
        mainRecBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintIntent = new Intent(getApplicationContext(), MaintenanceActivity.class);
                maintIntent.putExtras(dataBundle);
                startActivity(maintIntent);
            }
        });
        partsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent partIntent = new Intent(getApplicationContext(), ViewPartsActivityRV.class);
                partIntent.putExtras(dataBundle);
                startActivity(partIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.rec_sel_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.car_info) {
            Intent carInfoIntent = new Intent(getApplicationContext(), CarInfoActivity.class);
            carInfoIntent.putExtras(dataBundle);
            startActivity(carInfoIntent);
        }

        if (item.getItemId() == R.id.help) {
            Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
            dataBundle.putString("help", getString(R.string.RecordSelectActivity_help));
            dataBundle.putString("pageName", "Record Select - Help");
            dataBundle.putString("FromActivity", "RS");
            helpIntent.putExtras(dataBundle);
            startActivity(helpIntent);
        }

        if (item.getItemId() == android.R.id.home) {
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
