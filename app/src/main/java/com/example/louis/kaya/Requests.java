package com.example.louis.kaya;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Requests extends Fragment {

    TextView serviceText;
    public Requests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_services, container, false);

        serviceText =(TextView)view.findViewById(R.id.serviceName);

        if (serviceText.getText().equals("SERVICES"))
        {
            serviceText.setText("No delivery requests have been made yet");
        }
        return view;

    }
}
