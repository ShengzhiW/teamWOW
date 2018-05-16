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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TodayFragment extends Fragment {
    // variable for the default text currently on screen
    private TextView mStepDetector;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        // get the text by id
        mStepDetector = rootView.findViewById(R.id.stepCount);

        DatabaseReference userStepCount = db.getReference("Users").child(uid).child("Steps");

        // allow the step counter text to be updated
        ValueEventListener stepListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) count = dataSnapshot.getValue(Integer.class);
                mStepDetector.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userStepCount.addValueEventListener(stepListener);

        return rootView;
    }
}
