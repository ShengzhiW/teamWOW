package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrivacyFragment extends Fragment {

    Button closeButton;
    Switch leaderboardSwitch, stepsSwitch;

    // Set up database references
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();
    private DatabaseReference privacyDB = db.getReference("Users").child(uid).child("Privacy");
    private DatabaseReference privacylbdb = db.getReference("Leaderboard").child(uid).child("Private");
    private DatabaseReference privacystepslbdb = db.getReference("Leaderboard").child(uid).child("Private Steps");
    private DatabaseReference privacy_leaderboard = privacyDB.child("Appear on Leaderboard");
    private DatabaseReference privacy_email = privacyDB.child("Display Steps on Leaderboard");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_privacy, container, false);

        closeButton = rootView.findViewById(R.id.closePrivacy);
        leaderboardSwitch = rootView.findViewById(R.id.leaderboardSwitch);
        stepsSwitch = rootView.findViewById(R.id.stepsSwitch);

        // Access current data, and update switches as necessary
        ValueEventListener leaderboardListener = new ValueEventListener() {
            boolean value;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) value = dataSnapshot.getValue(Boolean.class);
                leaderboardSwitch.setChecked(value);
                if(!leaderboardSwitch.isChecked()) {
                    stepsSwitch.setChecked(false);
                    privacyDB.child("Display Steps on Leaderboard").setValue(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        privacy_leaderboard.addValueEventListener(leaderboardListener);

        ValueEventListener stepsListener = new ValueEventListener() {
            boolean value;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) value = dataSnapshot.getValue(Boolean.class);
                stepsSwitch.setChecked(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        privacy_email.addValueEventListener(stepsListener);

        // Set up leaderboard switch functionality
        leaderboardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                privacyDB.child("Appear on Leaderboard").setValue(isChecked);
                privacylbdb.setValue(!isChecked);
                // Turn off all other switches if the leaderboard switch is turned off
                if(!leaderboardSwitch.isChecked()) {
                    stepsSwitch.setChecked(false);
                    privacyDB.child("Display Steps on Leaderboard").setValue(false);
                    privacystepslbdb.setValue(true);
                }
                stepsSwitch.setEnabled(isChecked);
            }
        });


        // Set up email switch functionality
        stepsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                privacyDB.child("Display Steps on Leaderboard").setValue(isChecked);
                privacystepslbdb.setValue(!isChecked);
            }
        });

        // Finish activity, should return to Settings Page activity
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ContainerPage)getActivity()).pushFragment(new SettingsFragment());
            }
        });

        return rootView;
    }
}
