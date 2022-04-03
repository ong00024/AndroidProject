package com.cst2335.teamproject;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Result Activity.
 *
 * @author Vincent Zheng
 * @version 1.0
 */
public class ResultsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView viewResults = findViewById(R.id.tv_result);
        String result = getIntent().getStringExtra("Result");
        viewResults.setText(result);

        Button saveResult = findViewById(R.id.saveButton);



    }

}