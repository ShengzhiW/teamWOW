package com.example.teamwow.onewalk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrivacyPage extends AppCompatActivity{

    Button retBtn;
    Switch primarySwitch, emailSwitch;
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_prompts);

        retBtn = findViewById(R.id.returnBtn);
        primarySwitch = findViewById(R.id.priLead);
        emailSwitch = findViewById(R.id.eSwitch);
        final String uid = user.getUid();

        primarySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                db.getReference("Users").child(uid).child("Privacy")
                        .child("Appear on Leaderboard").setValue(isChecked);

                // Turn off all other switches if the primary button is turned off
                if(!primarySwitch.isChecked()) {
                    emailSwitch.setChecked(false);
                    db.getReference("Users").child(uid).child("Privacy")
                            .child("Show Email").setValue(false);
                }
            }
        });

        emailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String uid = user.getUid();
                db.getReference("Users").child(uid).child("Privacy")
                        .child("Show Email").setValue(isChecked);

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