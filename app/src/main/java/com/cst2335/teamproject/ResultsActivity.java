package com.cst2335.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;



import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * Result Activity.
 * Display results from search, and provide option to save to database/
 * @author Kevin Ong
 * @version 1.0
 */
public class ResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CovidOpener myOpener;
    SQLiteDatabase theDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        myOpener = new CovidOpener(this);
        StringBuilder sBuild = new StringBuilder();
        theDB = myOpener.getWritableDatabase();
        TextView viewResults = findViewById(R.id.tv_result);
        viewResults.setMovementMethod(new ScrollingMovementMethod());
        ArrayList<CovidData> results;
        results = (ArrayList<CovidData>) getIntent().getSerializableExtra("Result");

        /*Toolbar and Navigation Drawer*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Sets title to white
        toolbar.setTitleTextColor(Color.WHITE);
        //Sets overflow icon colour to white
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE,  PorterDuff.Mode.SRC_ATOP);

        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColorFilter(Color.WHITE,  PorterDuff.Mode.SRC_ATOP);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        for (int i = 0; i < results.size() ; i++) {
            sBuild.append(results.get(i).toString());
            sBuild.append("\n");
        }
        int size = results.size();
        String country = results.get(0).getCountry();
        String from = results.get(0).getDate();
        String to = results.get(size-1).getDate();
        viewResults.setText(sBuild.toString());

        Button saveResult = findViewById(R.id.saveButton);


        String message = String.format("Cases in %s from %s to %s", country, from, to);


        /* Vincent
         * AlertDialog comes up when user clicks save results button. Option to save the result or cancel.
         */
        saveResult.setOnClickListener(save -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(ResultsActivity.this);
            alert.setTitle(R.string.wantToSave);
            alert.setPositiveButton(R.string.yes,(click, yes)-> {
                Intent saveToPastQueries = new Intent(ResultsActivity.this, PastQueries.class);
                saveToPastQueries.putExtra("days", size);
                saveToPastQueries.putExtra("country", country);
                saveToPastQueries.putExtra("message", message);
                saveToPastQueries.putExtra("fromDate", from);
                saveToPastQueries.putExtra("toDate", to);
                saveToPastQueries.putExtra("results", sBuild.toString());
                startActivity(saveToPastQueries);

            });

            alert.setNegativeButton(R.string.cancel, (click, no) ->{ })
                    .create().show();

        });
    }

    /**
     * Menu items placed onto inflated menu
     * @author Vincent
     * @param menu items to be used in menu
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
                    .setMessage(getResources().getString(R.string.resultHelp))
                    .setNeutralButton(R.string.ok, (click, b) -> {
                    })
                    .create().show();
        }
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * Called whenever user selects a menu icon from the navigation drawer.
     * @author Vincent
     * @param item the item that the user selected
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*Navigates to chosen activity*/
        Intent nextActivity;
        switch(item.getItemId())
        {
            case R.id.itemStart:
                nextActivity = new Intent (ResultsActivity.this, MainActivity.class);
                startActivity(nextActivity);
                break;

            case R.id.itemSearch:
                nextActivity = new Intent (ResultsActivity.this, SearchActivity.class);
                startActivity(nextActivity);
                break;


            case R.id.itemSaved:
                nextActivity = new Intent (ResultsActivity.this, PastQueries.class);
                startActivity(nextActivity);
                break;
        }

        return true;
    }
}