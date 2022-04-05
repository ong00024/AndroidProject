package com.cst2335.teamproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

/**
 * Empty Activity to be used for fragment showing listview details
 * @author Kevin Ong
 * @version 1.0
 * @see DetailsFragment
 * @see PastQueries
 */
public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Intent fromPastQ = getIntent();
        String qResults = fromPastQ.getStringExtra("results");
        int id = fromPastQ.getIntExtra("id",0);
        String msgDetails = fromPastQ.getStringExtra("message");

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("message", msgDetails);
        args.putString("results", qResults);

        detailsFragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setReorderingAllowed(true);
        ft.add(R.id.detail_frag, detailsFragment);
        ft.commit();

    }
}