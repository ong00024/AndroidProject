package com.cst2335.teamproject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    String qResults = "";
    int id = 0;
    String msgDetails = "";
    TextView resultsDetails;
    TextView messageDetails;
    TextView msgId;
    Button hide;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            if (getArguments() != null) {
                id = getArguments().getInt("id", 0);
                msgDetails = getArguments().getString("message", "message details");
                qResults = getArguments().getString("results", "query results");
            }
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated = inflater.inflate(R.layout.activity_details_fragment, container, false);

        return inflated;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        //set values
        messageDetails = (TextView) view.findViewById(R.id.message_title);
        msgId = view.findViewById(R.id.message_id);
        resultsDetails = view.findViewById(R.id.query_results);
        resultsDetails.setMovementMethod(new ScrollingMovementMethod());
        hide = (Button) view.findViewById(R.id.hide);

        messageDetails.setText(msgDetails);
        String displayId = "ID = " + id;
        msgId.setText(displayId);
        resultsDetails.setText(qResults);

        hide.setOnClickListener( click -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.setReorderingAllowed(true);
            ft.remove(this);
            ft.commit();

            getActivity().finish();
        });

    }
}