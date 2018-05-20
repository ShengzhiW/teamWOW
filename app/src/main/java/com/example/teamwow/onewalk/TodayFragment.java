package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TodayFragment extends Fragment {
    // variable for the default text currently on screen
    private TextView mStepDetector;
    private TextView todaysSteps;
    private TextView currencyText;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private int count = 0;
    private int dailyCount = 0;
    private int currencyCount = 0;

    private DatabaseReference userStepCount;
    private DatabaseReference todayStepCount;
    private DatabaseReference currencyCountDb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        // get the text by id
        mStepDetector = rootView.findViewById(R.id.total_steps);
        todaysSteps = rootView.findViewById(R.id.stepCount);
        currencyText = rootView.findViewById(R.id.currency_count);

        // Get the current time
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String todayDate = df.format(today);

        userStepCount = db.getReference("Users").child(uid).child("Steps");
        todayStepCount = db.getReference("Users").child(uid).child("Archive").child(todayDate);
        currencyCountDb = db.getReference("Users").child(uid).child("Currency");

        // Add listener to get the total step count
        userStepCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) count = dataSnapshot.getValue(Integer.class);
                mStepDetector.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Add listener to get today's step count
        todayStepCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) dailyCount = dataSnapshot.getValue(Integer.class);
                todaysSteps.setText(String.valueOf(dailyCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Add listener to get currency count
        currencyCountDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) currencyCount = dataSnapshot.getValue(Integer.class);
                currencyText.setText(String.valueOf(currencyCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }


        public void displayMessage(String message){
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }

    }
