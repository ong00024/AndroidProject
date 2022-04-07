package com.cst2335.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * Listview showing past queries saved in db
 * Click on one item will bring up details of the queries.
 * Long click on item to prompt delete.
 * Tool bar provides help instructions
 * Navigation drawer can allow for navigating between activities
 * @author Kevin Ong, Vincent Zheng
 * @version 1.0
 */

public class PastQueries extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "PastQueries";

    CovidOpener myOpener;
    SQLiteDatabase theDB;

    ListView myList;
    MyListAdapter theAdaptor;
    ArrayList<SavedQuery> queries = new ArrayList<>();
    Cursor savedQs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pastqueries);
        Intent fromResults = getIntent();
        int size;
        String country;
        String message;
        String fromDate;
        String toDate;
        String results;

        myOpener = new CovidOpener(this);
        theDB = myOpener.getWritableDatabase();

        myList = findViewById(R.id.theListView);
        theAdaptor = new MyListAdapter();
        myList.setAdapter(theAdaptor);

        /* Vincent
         * Toolbar and DrawerLayout
         */
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

        //load previously saved db entries
        savedQs = theDB.rawQuery("SELECT * from " + CovidOpener.TABLE_NAME + ";", null);
        int idIndex = savedQs.getColumnIndex(CovidOpener.COL_ID);
        int countryIndex = savedQs.getColumnIndex(CovidOpener.COL_COUNTRY);
        int fromIndex = savedQs.getColumnIndex(CovidOpener.COL_FROM_DATE);
        int toIndex = savedQs.getColumnIndex(CovidOpener.COL_TO_DATE);

        while (savedQs.moveToNext()) {
            Log.i(TAG, "moveToNext");
            long idQ = (long) savedQs.getInt(idIndex);
            String countryQ = savedQs.getString(countryIndex);
            String fromQ = savedQs.getString(fromIndex);
            String toQ = savedQs.getString(toIndex);
            queries.add(new SavedQuery(countryQ, fromQ, toQ, idQ));
        }

        /* save entry from results activity */
        if (fromResults.getStringExtra("country") != null) {
            SavedQuery sq = new SavedQuery();

            country = fromResults.getStringExtra("country");
            size = fromResults.getIntExtra("days", 0);
            message = fromResults.getStringExtra("message");
            fromDate = fromResults.getStringExtra("fromDate");
            toDate = fromResults.getStringExtra("toDate");
            results = fromResults.getStringExtra("results");

            ContentValues newRow = new ContentValues();
            newRow.put(CovidOpener.COL_COUNTRY, country);
            newRow.put(CovidOpener.COL_FROM_DATE, fromDate);
            newRow.put(CovidOpener.COL_TO_DATE, toDate);
            newRow.put(CovidOpener.COL_DAYS, size);
            newRow.put(CovidOpener.COL_MESSAGE, message);
            newRow.put(CovidOpener.COL_RESULTS, results);

            long id = theDB.insert(CovidOpener.TABLE_NAME, null, newRow);
            sq.setCountry(country);
            sq.setFrom(fromDate);
            sq.setId(id);
            sq.setTo(toDate);
            queries.add(sq);
            theAdaptor.notifyDataSetChanged();
        }

        /* prompt user with delete option if item is clicked and held */
        myList.setOnItemLongClickListener((parent, view, position, id) -> {

            SavedQuery whatWhatClicked = queries.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(PastQueries.this);

            builder.setTitle("Do you want to delete this?  \'" + whatWhatClicked.toString() + "\'")
                    .setMessage("The selected row is: " + position +
                            "\nThe database id is: " + myList.getItemIdAtPosition(position))
                    .setNegativeButton("No", (dialog, click1) -> {
                    })
                    .setPositiveButton("Yes", (dialog, click2) -> {
                        queries.remove(position);
                        theAdaptor.notifyDataSetChanged();
                        Toast.makeText(this, getResources().getString(R.string.you_removed_toast) + position, Toast.LENGTH_LONG).show();
                        theDB.delete(CovidOpener.TABLE_NAME,
                                CovidOpener.COL_ID +" = ?", new String[]{Long.toString(whatWhatClicked.getId())});
                    }).create().show();
            return true;
        });
        /*view details of query */
        myList.setOnItemClickListener((list, view, position, id) -> {
            Intent goToFragment = new Intent(PastQueries.this, EmptyActivity.class);
            Cursor cursor = theDB.rawQuery("SELECT " +CovidOpener.COL_RESULTS +" from " + CovidOpener.TABLE_NAME + " WHERE _id=" + id + ";", null);
            cursor.moveToFirst();
            int resultsIndex = cursor.getColumnIndex(CovidOpener.COL_RESULTS);

            String displayQ = cursor.getString(resultsIndex);
            cursor.close();

            goToFragment.putExtra("id", (int) id);
            goToFragment.putExtra("message", queries.get(position).toString());
            goToFragment.putExtra("results", displayQ);
            startActivity(goToFragment);

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
                        .setMessage(getResources().getString(R.string.savedHelp))
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
                nextActivity = new Intent (PastQueries.this, MainActivity.class);
                startActivity(nextActivity);
                break;

            case R.id.itemSearch:
                nextActivity = new Intent (PastQueries.this, SearchActivity.class);
                startActivity(nextActivity);
                break;

            case R.id.itemSaved:
                Toast.makeText(this, R.string.onSavedPage, Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    /**
     * Each item in the arraylist of saved queries is represented in the list view,
     * which contains individual text views inflated through this
     * @author Kevin Ong
     */
    public class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return queries.size();
        }

        @Override
        public Object getItem(int position) {
            return queries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return queries.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newV;
            TextView tView;

            newV = inflater.inflate(R.layout.activity_pastquery, parent, false);
            tView = newV.findViewById(R.id.query);

            tView.setText(getItem(position).toString());

            return newV;
        }

    }

}
