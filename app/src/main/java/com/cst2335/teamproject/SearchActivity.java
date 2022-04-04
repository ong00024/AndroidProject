package com.cst2335.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Search Activity.
 *
 * @author Kevin Ong, Vincent Zheng
 * @version 1.0
 */
public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences sharedPrefs;
    private static final String TAG = "SEARCH_ACTIVITY";
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sharedPrefs = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        String savedCountry = sharedPrefs.getString("Country", "");

        String savedStart = sharedPrefs.getString("from", "");
        String savedEnd = sharedPrefs.getString("to", "");

        pb = findViewById(R.id.pb_search);

        EditText inputCountry = findViewById(R.id.inputCountry);
        EditText inputStartDate = findViewById(R.id.inputStartDate);
        EditText inputEndDate = findViewById(R.id.inputEndDate);
        inputCountry.setText(savedCountry);
        inputStartDate.setText(savedStart);
        inputEndDate.setText(savedEnd);

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

        Button search = findViewById(R.id.searchButton);


        search.setOnClickListener((View click) -> {
            if (inputCountry.getText().toString().isEmpty() || inputStartDate.getText().toString().isEmpty() || inputEndDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "You have to enter country and date!", Toast.LENGTH_LONG).show();

            }   else {

                Intent goToResult = new Intent(SearchActivity.this, ResultsActivity.class);
                goToResult.putExtra("Searched Country", inputCountry.getText().toString());
                goToResult.putExtra("Searched Start Date", inputStartDate.getText().toString());
                goToResult.putExtra("Searched End Date", inputEndDate.getText().toString());

                saveSharedPrefs(inputCountry.getText().toString(), inputStartDate.getText().toString(), inputEndDate.getText().toString());
                Toast.makeText(SearchActivity.this, getString(R.string.loadResults), Toast.LENGTH_LONG).show();

                pb.setVisibility(View.VISIBLE);
                pb.setProgress(0);
                MyHTTPReq req = new MyHTTPReq();
                String urlString = "https://api.covid19api.com/country/";
                String country = inputCountry.getText().toString();
                String startDate = inputStartDate.getText().toString();
                String endDate = inputEndDate.getText().toString();
                StringBuilder makeUrl = new StringBuilder(urlString);
                makeUrl.append(country);
                makeUrl.append("/status/confirmed/live?from=");
                makeUrl.append(startDate);
                makeUrl.append("T00:00:00Z&to=");
                makeUrl.append(endDate);
                makeUrl.append("T00:00:00Z");
                Log.i(TAG, "url is <" + makeUrl.toString() + ">");
                req.execute(makeUrl.toString());

            }

        });

        /* view previous queries */
        Button saved = findViewById(R.id.savedDates);

        saved.setOnClickListener(goToSaved -> {
            Intent toSaved = new Intent(SearchActivity.this, PastQueries.class);
            startActivity(toSaved);
        });


    }

    private void saveSharedPrefs(String savedCountry, String savedStart, String savedEnd) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("Country", savedCountry);
        editor.putString("from", savedStart);
        editor.putString("to", savedEnd);
        editor.apply();
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
                    .setMessage(getResources().getString(R.string.searchHelp))
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
                finish();
                break;

            case R.id.itemSearch:
                Toast.makeText(this, R.string.onSearchPage, Toast.LENGTH_LONG).show();
                break;

            case R.id.itemSaved:
                nextActivity = new Intent(SearchActivity.this, PastQueries.class);
                startActivity(nextActivity);
                break;
        }

        return true;
    }

    private class MyHTTPReq extends AsyncTask<String, Integer, ArrayList<CovidData>> {
        private static final String TAG1 = "MyHTTPRequest";


        // returns type3, input is type 1
        @Override
        protected ArrayList<CovidData> doInBackground(String... args) {
            StringBuilder sBuild = new StringBuilder();
            Log.i(TAG1, "start of doInBg");
            ArrayList<CovidData> covidList = new ArrayList();

            try {
                publishProgress(25);
                URL url = new URL(args[0]); //build the server connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String text = sb.toString();
                JSONArray covidArray = new JSONArray(text);

                /* JSONObject theDocument = new JSONObject(text);
                JSONArray covidArray = theDocument.getJSONArray(""); */
                publishProgress(50);
                for (int j = 0; j < covidArray.length(); j++) {

                    JSONObject objPos = covidArray.getJSONObject(j);
                    String country = objPos.getString("Country");
                    String countryCode = objPos.getString("CountryCode");
                    String province = objPos.getString("Province");
                    double lat = objPos.getDouble("Lat");
                    double lon = objPos.getDouble("Lon");
                    int cases = objPos.getInt("Cases");
                    String status = objPos.getString("Status");
                    String date = objPos.getString("Date");


                    CovidData covidIn = new CovidData(country, countryCode, lat, lon, cases, status, date);
                    if(!province.isEmpty()){
                        covidIn.setProvince(province);
                    }

                    covidList.add(covidIn);


                }

                Log.i(TAG1, "end of try in doInBg");

                publishProgress(75);
            } catch (IOException | JSONException e) {
                Log.i(TAG1, e.getMessage() + " exception in doInBg");

                e.printStackTrace();
            }
            publishProgress(100);
            return covidList;
            //return sBuild.toString();
        }

        //type2
        protected void onProgressUpdate(Integer... values) {
            Log.i(TAG1, "onProgressUpdate");
            pb.setProgress(values[0]);
        }

        // input is from doing type3
        public void onPostExecute(ArrayList<CovidData> fromDoInBg) {
            //update GUI
            Log.i(TAG1, "onPostExecute");
            pb.setVisibility(View.GONE);

            Intent goToResult = new Intent(SearchActivity.this, ResultsActivity.class);
            goToResult.putExtra("Result", fromDoInBg);

            startActivity(goToResult);

        }
    }

}