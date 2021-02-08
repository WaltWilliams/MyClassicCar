package com.woodlandcoders.myclassiccar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by walt on 3/10/18.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The transition from the layoutless splash screen to the MainActivity.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
