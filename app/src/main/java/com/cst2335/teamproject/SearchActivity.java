package com.cst2335.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
public class SearchActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    TextView result;
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

        result = (TextView) findViewById(R.id.tv_result);
        pb = findViewById(R.id.pb_search);

        EditText inputCountry = findViewById(R.id.inputCountry);
        EditText inputStartDate = findViewById(R.id.inputStartDate);
        EditText inputEndDate = findViewById(R.id.inputEndDate);
        inputCountry.setText(savedCountry);
        inputStartDate.setText(savedStart);
        inputEndDate.setText(savedEnd);

        Button search = findViewById(R.id.searchButton);

        search.setOnClickListener((View click) -> {
            if (inputCountry.getText().toString().isEmpty() || inputStartDate.getText().toString().isEmpty() || inputEndDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "You have to enter country and date!", Toast.LENGTH_LONG).show();
            } else {

                Intent goToResult = new Intent(SearchActivity.this, ResultsActivity.class);
                goToResult.putExtra("Searched Country", inputCountry.getText().toString());
                goToResult.putExtra("Searched Date", inputStartDate.getText().toString());
                goToResult.putExtra("Searched Date", inputStartDate.getText().toString());

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
                Log.i(TAG, "url is <" + makeUrl.toString()+ ">");
                req.execute(makeUrl.toString());

            }

            //startActivity(goToResult); will utilize fragments



        });

    }

    private void saveSharedPrefs(String savedCountry, String savedStart, String savedEnd){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("Country", savedCountry);
        editor.putString("from", savedStart);
        editor.putString("to", savedEnd);
        editor.apply();
    }

    private class MyHTTPReq extends AsyncTask<String, Integer, String> {
        static private final String TAG1 = "MyHTTPRequest";


        // returns type3, input is type 1
        @Override
        protected String doInBackground(String... args) {
            StringBuilder sBuild = new StringBuilder();
            Log.i(TAG1, "start of doInBg");

            try {
                ArrayList<CovidData> covidList = new ArrayList();
                publishProgress(25);
                URL url = new URL(args[0]); //build the server connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null) {
                    sb.append(line+ "\n");
                }
                String text = sb.toString();
                JSONArray covidArray = new JSONArray(text);

                /* JSONObject theDocument = new JSONObject(text);
                JSONArray covidArray = theDocument.getJSONArray(""); */
                publishProgress( 50);
                for (int j = 0; j < covidArray.length(); j++) {

                    JSONObject objPos = covidArray.getJSONObject(j);
                    String country = objPos.getString("Country");
                    String countryCode = objPos.getString("CountryCode");
                    double lat = objPos.getDouble("Lat");
                    double lon = objPos.getDouble("Lon");
                    int cases = objPos.getInt("Cases");
                    String status = objPos.getString("Status");
                    String date = objPos.getString("Date");


                    CovidData covidIn = new CovidData(country, countryCode, lat, lon, cases, status, date);

                    covidList.add(covidIn);


                }
                for (int k = 0; k < covidList.size(); k++) {
                    sBuild.append(covidList.get(k).toString() + " \n");
                }
                Log.i(TAG1, "end of try in doInBg");

                publishProgress(75);
            }   catch (IOException | JSONException e) {
                Log.i(TAG1, e.getMessage() + " exception in doInBg");

                e.printStackTrace();
            }
            publishProgress(100);
            return sBuild.toString();
        }

        //type2
        protected void onProgressUpdate(Integer ... values) {
            Log.i(TAG1, "onProgressUpdate");
            pb.setProgress(values[0]);
        }

        // input is from doing type3
        public void onPostExecute(String fromDoInBg) {
            //update GUI
            Log.i(TAG1, "onPostExecute");

            result.setText(fromDoInBg);
            pb.setVisibility(View.GONE);

        }
    }
}