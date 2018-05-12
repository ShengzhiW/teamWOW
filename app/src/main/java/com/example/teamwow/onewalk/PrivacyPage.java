package com.example.teamwow.onewalk;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

/**
 * New Activity for privacy options
 */
public class PrivacyPage extends AppCompatActivity{

    // Set up buttons
    Button retBtn;
    Switch primarySwitch, emailSwitch;
    private static final String TAG = "Privacy Page";

    // Set up database references
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();
    private DatabaseReference privacyDB = db.getReference("Users").child(uid).child("Privacy");
    private DatabaseReference privacy_leaderboard = privacyDB.child("Appear on Leaderboard");
    private DatabaseReference privacy_email = privacyDB.child("Show Email");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_prompts);

        // Set up buttons on page
        retBtn = findViewById(R.id.returnBtn);
        primarySwitch = findViewById(R.id.priLead);
        emailSwitch = findViewById(R.id.eSwitch);


        Log.d(TAG,"Logging stuff");

        // Access current data, and update switches as necessary
        ValueEventListener primaryListener = new ValueEventListener() {

            boolean value;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) value = dataSnapshot.getValue(Boolean.class);
                Log.d(TAG,"The Reference: " + dataSnapshot.getRef() + " Value: " + value );
                primarySwitch.setChecked(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database error");
            }
        };

        privacy_leaderboard.addValueEventListener(primaryListener);

        ValueEventListener emailListener = new ValueEventListener() {

            boolean value;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) value = dataSnapshot.getValue(Boolean.class);
                Log.d(TAG,"The Reference: " + dataSnapshot.getRef() + " Value: " + value );
                emailSwitch.setChecked(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database error");
            }
        };

        privacy_email.addValueEventListener(emailListener);

        // Set up primary switch functionality
        primarySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                privacyDB.child("Appear on Leaderboard").setValue(isChecked);

                // Turn off all other switches if the primary button is turned off
                if(!primarySwitch.isChecked()) {
                    emailSwitch.setChecked(false);
                    privacyDB.child("Show Email").setValue(false);
                }
                emailSwitch.setEnabled(isChecked);
            }
        });


        // Set up email switch functionality
        emailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                privacyDB.child("Show Email").setValue(isChecked);
            }
        });



        // Finish activity, should return to Settings Page activity
        retBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}