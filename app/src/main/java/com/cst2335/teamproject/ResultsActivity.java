package com.cst2335.teamproject;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Result Activity.
 * Display results from search, and provide option to save to database/
 * @author Vincent Zheng, Kevin Ong
 * @version 1.0
 */
public class ResultsActivity extends AppCompatActivity{

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

}