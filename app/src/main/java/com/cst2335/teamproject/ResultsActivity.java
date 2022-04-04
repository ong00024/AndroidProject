package com.cst2335.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
 * @author Vincent Zheng, Kevin Ong
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
        ArrayList<CovidData> results;
        results = (ArrayList<CovidData>) getIntent().getSerializableExtra("Result");

        //Gets toolbar from the layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE,  PorterDuff.Mode.SRC_ATOP);

        //For NavigationDrawer
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

        saveResult.setOnClickListener(save -> {
            Intent saveToPastQueries = new Intent(ResultsActivity.this, PastQueries.class);
            saveToPastQueries.putExtra("days", size);
            saveToPastQueries.putExtra("country", country);
            saveToPastQueries.putExtra("message", message);
            saveToPastQueries.putExtra("fromDate", from);
            saveToPastQueries.putExtra("toDate", to);
            saveToPastQueries.putExtra("results", sBuild.toString());
            startActivity(saveToPastQueries);




        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toast = null;
        //What actions occur when the menu item is selected
        if (item.getItemId() == R.id.help) {
            toast = getString(R.string.clickHelp);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.howTo)
                    .setMessage(getResources().getString(R.string.resultHelp))
                    .setNeutralButton("OK", (click, b) -> {
                    })
                    .create().show();
        }
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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