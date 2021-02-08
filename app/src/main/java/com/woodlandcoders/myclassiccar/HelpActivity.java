package com.woodlandcoders.myclassiccar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

//import androidx.core.text.HtmlCompat;

public class HelpActivity extends AppCompatActivity {

    Bundle dataBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Getting the passed in values.
        dataBundle = getIntent().getExtras();

        // Starting the toolbar.
        Toolbar helpToolbar = findViewById(R.id.helpToolbar);
        setSupportActionBar(helpToolbar);
        helpToolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(dataBundle.getString("pageName"));

        // Loading the TextView with HTML passed in content.
        // This makes this activity reusable for all help pages.

        // DEPRECATED *************************************************************************
        // https://stackoverflow.com/questions/37904739/html-fromhtml-deprecated-in-android-n
        TextView htmlHelpTextView = findViewById(R.id.helpTextView);
        htmlHelpTextView.setText(fromHtml(dataBundle.getString("help")));

    }

    /////////////////////////////////////////////////////////////////////////////////////
    // This WILL require modification in the future when the old "fromHtml()" is removed.
    /////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent x = null;
            String string = dataBundle.getString("FromActivity");
            if (string.compareTo("Main") == 0) {
                x = new Intent(getApplicationContext(), MainActivity.class);
            }

            if (string.compareTo("RS") == 0) {
                x = new Intent(getApplicationContext(), RecordSelectActivity.class);
            }

            if (string.compareTo("ViewOEM") == 0) {
                x = new Intent(getApplicationContext(), ViewPartsActivityRV.class);
            }

            if (string.compareTo("AddCar") == 0) {
                x = new Intent(getApplicationContext(), CarAddActivity.class);
            }

            if (string.compareTo("SinglePart") == 0) {
                x = new Intent(getApplicationContext(), SinglePartActivity.class);
            }

            if (string.compareTo("Backup") == 0) {
                x = new Intent(getApplicationContext(), BackupActivity.class);
            }
            dataBundle.remove("FromActivity");
            dataBundle.remove("pageName");
            dataBundle.remove("help");
            x.putExtras(dataBundle);
            startActivity(x);
        }
        return super.onOptionsItemSelected(item);
    }

    // Takes control of the hardware back button.
    // This selectively returns use to the same part of the app from whence they came.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }


}
