package com.woodlandcoders.myclassiccar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    DBManager carDB;
    DBManagerSchema schema;
    Bundle dataBundle;
    Utilities utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Starting the database
        carDB = new DBManager(this);
        Cursor carData = carDB.getCarData();

        // Defining Toolbar that is at the top of the page
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        // Starting UI items
        ListView carList = findViewById(R.id.carListview);
        final FloatingActionButton fab = findViewById(R.id.mainFab);
        utilities = new Utilities();

        // I found it simplest to create a "package" of items to
        // be passed around while navigating through the app.
        dataBundle = new Bundle();

        // ---------- Loading the list -------------
        if (carData.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Please add a car.", Toast.LENGTH_LONG).show();
        }
        else {
            MainActivityAdapter carViewAdapter = new MainActivityAdapter(this, carData);
            carList.setAdapter(carViewAdapter);
        }

        //----------- Tap on list item --------------
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cellCursor = (Cursor) adapterView.getItemAtPosition(i);

                // Getting selected car by exact _id number and passing that to the next activity.
                int x = cellCursor.getInt(cellCursor.getColumnIndex("_id"));

                // Using queries naming to keep things consistent.
                dataBundle.putInt(schema.COL_CAR_ID, x);

                // Navigation code
                Intent recordSel = new Intent(getApplicationContext(), RecordSelectActivity.class);
                recordSel.putExtras(dataBundle);
                startActivity(recordSel);
            }
        });


        // This provides the small, round, Add button found in the lower right of the screen.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCarIntent = new Intent(getApplicationContext(), CarAddActivity.class);

                // Passing in tidbits of data just to pass
                // them back to insure a smooth back transition.
                // Related to the Help Activity.
                // Completely unnecessary otherwise.
                addCarIntent.putExtras(dataBundle);

                startActivity(addCarIntent);
            }
        });
    } // End of main onCreate method.

    // ============================================================================================

    // Code for setting up menu in the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateMain = getMenuInflater();
        inflateMain.inflate(R.menu.backup_menu, menu);  // Temporally disabling the backup routine.
        return super.onCreateOptionsMenu(menu);         // It was not reliable. This menu only contains "help"
    }


    // The place to code drop-down menu selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.backup) {
            Intent backupIntent = new Intent(getApplicationContext(), BackupActivity.class);
            backupIntent.putExtras(dataBundle);
            startActivity(backupIntent);
        }

        if (item.getItemId() == R.id.help) {
            Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
            dataBundle.putString("help", getString(R.string.MainActivity_help));
            dataBundle.putString("pageName", "Home - Help");
            dataBundle.putString("FromActivity", "Main");
            helpIntent.putExtras(dataBundle);
            startActivity(helpIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    // To close the database when the app is terminated
    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDB.close();
    }
}
