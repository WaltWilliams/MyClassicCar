package com.woodlandcoders.myclassiccar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class BackupActivity extends AppCompatActivity {

    // References to needed objects
    // The db is instantiated so we can programmatically get the db name.
    // Used in the onRequestPermissionsResult below.
    private DBManager db = new DBManager(this);
    private int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    Bundle dataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        // Getting the passed in values.
        dataBundle = getIntent().getExtras();

        // Starting the toolbar that is at the top of the view.
        Toolbar backupToolbar = findViewById(R.id.backupToolbar);
        setSupportActionBar(backupToolbar);
        backupToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Backup - Restore");

        // References and instantiation of UI objects.
        // They didn't need to be globally available in this activity.
        Button backupButton = findViewById(R.id.backupButton);
        Button restoreButton = findViewById(R.id.restoreButton);

        // This section is for judging what permissions are available. ============================
        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hasPermission = ContextCompat.checkSelfPermission(BackupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int h = PackageManager.PERMISSION_GRANTED;
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(BackupActivity.this, "Storage permission is denied", Toast.LENGTH_SHORT).show();
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(BackupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(BackupActivity.this, "Re-requesting permissions", Toast.LENGTH_SHORT).show();
                    if (Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).canWrite() == false) {
                        ActivityCompat.requestPermissions(BackupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                }
                else {// Request the permission
                    ActivityCompat.requestPermissions(BackupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    Toast.makeText(BackupActivity.this, "Permissions Requested.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // ========================================================================================
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BackupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(BackupActivity.this, "Storage permission is denied", Toast.LENGTH_SHORT).show();

                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(BackupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(BackupActivity.this, "Re-requesting permissions", Toast.LENGTH_SHORT).show();
                    if (Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).canWrite() == false) {
                        ActivityCompat.requestPermissions(BackupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }
                else {// Request the permission
                    ActivityCompat.requestPermissions(BackupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    Toast.makeText(BackupActivity.this, "Permissions Requested.", Toast.LENGTH_SHORT).show();
                }
            }
        });

         // ========================================================================================

      }



    // This is the place to code the functionality for when
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // A couple of initial test to see if the permissions are. These are used below.
        boolean canWrite = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).canWrite();
        boolean canRead = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).canRead();

        // Write file to "external" storage. Which is really part of the device storage.
        // This code should work whether the user device has an SD card or not.
        // I have tested for this on my own 2 devices.
        if (requestCode == 0) {
            if (canWrite) {
                // Getting the database file location.
                File fileToExternal = new File(BackupActivity.this.getDatabasePath(db.getDatabaseName()).toString());
                // Getting the Documents directory location programmatically, for ease of access.
                File external = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).toString(), db.getDatabaseName());
                // Testing to see if the db file is present before continuing.
                if (fileToExternal.exists()) {
                    try {
                        FileChannel src = new FileInputStream(fileToExternal).getChannel();
                        FileChannel dst = new FileOutputStream(external).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        Toast.makeText(this, "Write was successful. Check the Documents directory in your device's FileManager.", Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException f) {
                        System.out.println("File Not Found Exception" + f);
                        Toast.makeText(this, "File Not Found Exception: " + f, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(this, "An exception was thrown: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        // Read file from "external" storage.
        if (requestCode == 1) {
            if (canRead) {
                // Getting the database file location. This time in the Backup directory.
                // I found most app that enable one to download a file put them all in the same place.
                File external = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toString(), db.getDatabaseName());
                // Getting the directory location for app location for the db programmatically,
                // so it can be put in the right place for use by the app.
                File fileReturn = new File(BackupActivity.this.getDatabasePath(db.getDatabaseName()).toString());
                // Checking if the file is present in the Download directory.
                if (external.exists()){
                    Toast.makeText(this, "The file is present in the Download directory.", Toast.LENGTH_SHORT).show();
                    try {
                        FileChannel src = new FileInputStream(external).getChannel();
                        FileChannel dst = new FileOutputStream(fileReturn).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        Toast.makeText(this, "Database has been restored", Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException f) {
                        System.out.println("File Not Found Exception" + f);
                        Toast.makeText(this, "File Not Found Exception: " + f, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(this, "An exception was thrown: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    // Starting the menu for use in the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateBackupMenu = getMenuInflater();
        inflateBackupMenu.inflate(R.menu.backup_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Code to provide functionality for the menu selections.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {
            Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
            dataBundle.putString("help", getString(R.string.BackupActivity_help));
            dataBundle.putString("pageName", "Backup/Restore - Help");
            dataBundle.putString("FromActivity", "Backup");
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
            Intent x = new Intent(getApplicationContext(), MainActivity.class);
            //x.putExtras(dataBundle);
            startActivity(x);
        }
        return super.onKeyDown(keyCode, event);
    }



    // To close the database when the app is terminated
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
