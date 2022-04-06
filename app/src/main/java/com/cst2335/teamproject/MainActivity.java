package com.cst2335.teamproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

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

        /* Vincent
         * Toolbar
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Sets title to white
        toolbar.setTitleTextColor(Color.WHITE);
        //Sets overflow icon colour to white
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE,  PorterDuff.Mode.SRC_ATOP);



        Button startButton = findViewById(R.id.startApp);

        startButton.setOnClickListener(click -> {
            Intent goToSearch = new Intent(MainActivity.this, SearchActivity.class);

            startActivity(goToSearch);

        });
    }

    /**
     * Menu items placed onto inflated menu
     * @author Vincent
     * @param menu options for toolbar
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Called whenever user selects a menu icon from the toolbar
     * @author Vincent
     * @param item item that user selected
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toast = null;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        /*When help is clicked an AlertDialog opens to give instructions on how to use the activity*/
        switch(item.getItemId())
        {
            case R.id.info:
                toast = getString(R.string.clickInfo);
                alertDialogBuilder.setTitle(R.string.covidTitle)
                        .setMessage(R.string.covidDescription)
                        .setNeutralButton(R.string.ok, (click, b) -> {
                        }).create().show();
                break;

            case R.id.help:
                toast = getString(R.string.clickHelp);
                alertDialogBuilder.setTitle(R.string.howTo)
                        .setMessage(getResources().getString(R.string.startHelp))
                        .setNeutralButton(R.string.ok, (click, b) -> {
                        })
                        .create().show();
        }
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
        return true;
    }
}