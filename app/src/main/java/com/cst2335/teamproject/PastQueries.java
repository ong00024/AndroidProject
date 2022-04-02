package com.cst2335.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Listview showing past queries saved in db
 * Click on one item will bring up details of the queries.
 */
public class PastQueries extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pastqueries);
    }
}
