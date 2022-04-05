package com.cst2335.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * Main activity user will see when first opening app.
 *
 * @author Kevin Ong, Vincent Zheng
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startApp);

        startButton.setOnClickListener(click -> {
            Intent goToSearch = new Intent(MainActivity.this, SearchActivity.class);

            startActivity(goToSearch);

        });
    }
}