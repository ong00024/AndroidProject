package com.cst2335.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Search Activity.
 *
 * @author Kevin Ong, Vincent Zheng, Minh Trung Do
 * @version 1.0
 */
public class SearchActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sharedPrefs = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        String savedCountry = sharedPrefs.getString("Country", "");
        String savedDate = sharedPrefs.getString("Date", "");

        EditText inputCountry = findViewById(R.id.inputCountry);
        EditText inputDate = findViewById(R.id.inputDate);
        inputCountry.setText(savedCountry);
        inputDate.setText(savedDate);

        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(click -> {
            Intent goToResult = new Intent(SearchActivity.this, ResultsActivity.class);
            goToResult.putExtra("Searched Country", inputCountry.getText().toString());
            goToResult.putExtra("Searched Date", inputDate.getText().toString());
            saveSharedPrefs(inputCountry.getText().toString(), inputDate.getText().toString());
            Toast.makeText(SearchActivity.this, getString(R.string.loadResults), Toast.LENGTH_LONG).show();
            startActivity(goToResult);
        });

    }

    private void saveSharedPrefs(String savedCountry, String savedDate){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("Country", savedCountry);
        editor.putString("Date", savedDate);
        editor.apply();
    }
}