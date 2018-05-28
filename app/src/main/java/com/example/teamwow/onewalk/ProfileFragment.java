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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class ProfileFragment extends Fragment {

    // variable for the default text currently on screen
    private TextView username;
    private TextView mStepDetector;
    private TextView totalCurrency;
    private TextView totalHats;
    private TextView totalBodies;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private String name = "";
    private int count = 0;
    private int hats = 0;
    private int bodies = 0;
    private int currency = 0;

    private DatabaseReference userName;
    private DatabaseReference userStepCount;
    private DatabaseReference userCurrency;
    private DatabaseReference userHats;
    private DatabaseReference userBodies;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        // get the text by id
        username = rootView.findViewById(R.id.name);
        mStepDetector = rootView.findViewById(R.id.total_step_count);
        totalCurrency = rootView.findViewById(R.id.total_currency_count);
        totalHats = rootView.findViewById(R.id.total_hats);
        totalBodies = rootView.findViewById(R.id.total_bodies);

        userName = db.getReference("Users").child(uid).child("Name");
        userStepCount = db.getReference("Users").child(uid).child("Steps");
        userCurrency = db.getReference("Users").child(uid).child("Lifetime Currency");
        userHats = db.getReference("Users").child(uid).child("Inventory").child("Hat");
        userBodies = db.getReference("Users").child(uid).child("Inventory").child("Body");

        //Add listener to get the username
        userName.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()) name = dataSnapshot.getValue(String.class);
                username.setText(String.valueOf(name));
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}

        });

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

        // Add listener to get the total currency
        userCurrency.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) currency = dataSnapshot.getValue(Integer.class);
                totalCurrency.setText(String.valueOf(currency));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Add listener to get the total currency
        userHats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List array = (List) dataSnapshot.getValue();
                if(array != null)
                {
                    for(int i = 0; i < array.size(); i++){
                        if(Integer.valueOf(array.get(i).toString()) >= 1){
                            hats++;
                        }
                    }
                    totalHats.setText(String.valueOf(hats));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Add listener to get the total currency
        userBodies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List array = (List) dataSnapshot.getValue();
                if(array != null)
                {
                    for(int i = 0; i < array.size(); i++){
                        if(Integer.valueOf(array.get(i).toString()) >= 1){
                            bodies++;
                        }
                    }
                    totalBodies.setText(String.valueOf(bodies));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return rootView;
    }
}
