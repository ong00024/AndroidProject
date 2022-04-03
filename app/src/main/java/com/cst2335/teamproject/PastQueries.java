package com.cst2335.teamproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Listview showing past queries saved in db
 * Click on one item will bring up details of the queries.
 * Long click on item to prompt delete.
 *
 * @author Kevin Ong
 */

public class PastQueries extends AppCompatActivity {
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
        SavedQuery sq = new SavedQuery();

        myOpener = new CovidOpener(this);
        theDB = myOpener.getWritableDatabase();

        myList = findViewById(R.id.theListView);
        theAdaptor = new MyListAdapter();
        myList.setAdapter(theAdaptor);

        //load previously saved db entries
        savedQs = theDB.rawQuery("SELECT * from " + CovidOpener.TABLE_NAME + ";", null);
        int idIndex = savedQs.getColumnIndex(CovidOpener.COL_ID);
        int messageIndex = savedQs.getColumnIndex(CovidOpener.COL_MESSAGE);
        int fromIndex = savedQs.getColumnIndex(CovidOpener.COL_FROM_DATE);
        int toIndex = savedQs.getColumnIndex(CovidOpener.COL_TO_DATE);

        while (savedQs.moveToNext()) {
            long idQ = (long) savedQs.getInt(idIndex);
            String countryQ = savedQs.getString(messageIndex);
            String fromQ = savedQs.getString(fromIndex);
            String toQ = savedQs.getString(toIndex);
            queries.add(new SavedQuery(countryQ, fromQ, toQ, idQ));
        }
        //save entry from results activity
        if (fromResults.getStringExtra("country") != null) {
            Log.i(TAG, "inserting to db...");
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
            Log.i(TAG, "end of insert, adapter  notified");
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

            goToFragment.putExtra("id", (int) id);
            goToFragment.putExtra("message", queries.get(position).toString());
            goToFragment.putExtra("results", displayQ);
            startActivity(goToFragment);

        });



    }

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

            tView.setText(((SavedQuery) getItem(position)).toString());

            return newV;
        }

    }


}
