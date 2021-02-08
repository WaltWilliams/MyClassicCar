package com.woodlandcoders.myclassiccar;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SinglePartActivity extends AppCompatActivity {

    // References for needed objects.
    DBManager carDB;
    DBManagerSchema schema;
    Utilities utilities;
    Bundle dataBundle = new Bundle();

    // Global references for required Strings.
    String selectedPartName = "";
    String selectedMFG = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_part);

        // Instantiating needed objects.
        // Using query column names as keys to keep things consistent.
        dataBundle = getIntent().getExtras();
        utilities = new Utilities();
        carDB = new DBManager(this);

        // References for UI objects.
        // Getting a list of part from the TPM_Parts table.
        Cursor selectPart = carDB.getPart(dataBundle.getInt("partIdFromCell"));

        // Starting the toolbar
        Toolbar viewPartToolbar = findViewById(R.id.singleCarToolbar);
        setSupportActionBar(viewPartToolbar);
        viewPartToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the text  at the top of the activity to identify the
        // car you're currently displaying data for.
        getSupportActionBar().setTitle(dataBundle.getString(schema.COL_YEAR) + " " + dataBundle.getString(schema.COL_MAKE));

        // Referencing UI objects.
        ListView tpmList = findViewById(R.id.singleCarPartListview);
        final FloatingActionButton fab = findViewById(R.id.scpvFab);

        ImageView singlePartImageView = findViewById(R.id.singlePartImageView);
        TextView singlePartNameTextView = findViewById(R.id.existingPartsNameTextView);
        TextView singlePartNumberTextView = findViewById(R.id.existingPartsNumberTextView);
        TextView singlePartMfgTextView = findViewById(R.id.singlePartSrcTextView);
        TextView singlePartBarcodeTextView = findViewById(R.id.singlePartBarcodeTextView);

        // Converting  the image from the db so it can be displayed in a ImageView.
        byte[] byteData = selectPart.getBlob(selectPart.getColumnIndex(schema.COL_PHOTO));
        final Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        singlePartImageView.setImageBitmap(bm);

        selectedPartName = selectPart.getString(selectPart.getColumnIndex(schema.COL_PART_NAME));
        singlePartNameTextView.setText(selectPart.getString(selectPart.getColumnIndex(schema.COL_PART_NAME)));
        singlePartNumberTextView.setText(selectPart.getString(selectPart.getColumnIndex(schema.COL_PART_NUMBER)));
        selectedMFG = selectPart.getString(selectPart.getColumnIndex(schema.COL_MFG));
        singlePartMfgTextView.setText(selectPart.getString(selectPart.getColumnIndex(schema.COL_MFG)));
        singlePartBarcodeTextView.setText(selectPart.getString(selectPart.getColumnIndex(schema.COL_PART_NUMBER)));

        // Setting values for use in the next activity.
        dataBundle.putString(schema.COL_PART_NAME, selectedPartName);
        dataBundle.getString(schema.COL_MFG, selectedMFG);


        // Tap on Name or Part Number field of the OEM part at the top ===========================
        // to see its details.
        singlePartNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tpmIntent = new Intent(getApplicationContext(), OemPartActivity.class);
                tpmIntent.putExtras(dataBundle);
                startActivity(tpmIntent);
            }
        });

        singlePartNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tpmIntent = new Intent(getApplicationContext(), OemPartActivity.class);
                tpmIntent.putExtras(dataBundle);
                startActivity(tpmIntent);
            }
        });
        // =======================================================================================


        // ---------- Loading the ListView -------------
        final Cursor tpmPartList = carDB.getTpmParts(dataBundle.getInt("partIdFromCell"));

        if (tpmPartList.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Currently there are no \"Additional Source\" Parts available for display,", Toast.LENGTH_LONG).show();
        }
        else {
            SinglePartActivityAdapter x = new SinglePartActivityAdapter(this, tpmPartList);
            tpmList.setAdapter(x);
        }


        //----------- Tap on ListView item --------------
        tpmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cellCursor = (Cursor) adapterView.getItemAtPosition(i);
                //DatabaseUtils.dumpCursor(cellCursor);

                // Getting selected car by exact _id number and passing that to the next activity.
                int partIdFromTpmCell = cellCursor.getInt(cellCursor.getColumnIndex("_id"));
                String partMFGFromTpmCell = cellCursor.getString(cellCursor.getColumnIndex("MFG"));
                dataBundle.putInt("partIdFromTpmCell", partIdFromTpmCell);
                dataBundle.putString("partMFGFromTpmCell", partMFGFromTpmCell);

                Intent tpmIntent = new Intent(getApplicationContext(), TPMPartActivity.class);
                tpmIntent.putExtras(dataBundle);
                startActivity(tpmIntent);
            }
        });


        // This is the green button at the bottom of the view for adding a new TPM part.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tpmIntent = new Intent(getApplicationContext(), TPMAddPartActivity.class);
                tpmIntent.putExtras(dataBundle);
                startActivity(tpmIntent);
            }
        });

    }

    // This for capturing the photo taken by the device camera.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater singlePartInflater = getMenuInflater();
        singlePartInflater.inflate(R.menu.plain_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Code to make the menu item work.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.help) {
            Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
            dataBundle.putString("help", getString(R.string.SinglePartActivity_help));
            dataBundle.putString("FromActivity", "SinglePart");
            dataBundle.putString("pageName", "OEM Part - Help");
            helpIntent.putExtras(dataBundle);
            startActivity(helpIntent);
        }

        if (item.getItemId() == android.R.id.home) {
            Intent backNavIntent = new Intent(getApplicationContext(), ViewPartsActivityRV.class);
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
