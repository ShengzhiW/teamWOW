package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class TodayFragment extends Fragment {
    // variable for the default text currently on screen
    private TextView username;
    private TextView mStepDetector;
    private TextView todaysSteps;
    private TextView currencyText;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private String name = "";
    private int count = 0;
    private int dailyCount = 0;
    private int currencyCount = 0;

    private String challengerUid = "";

    private DatabaseReference userName;
    private DatabaseReference userStepCount;
    private DatabaseReference todayStepCount;
    private DatabaseReference currencyCountDb;

    private DatabaseReference challengerDb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        // get the text by id
        username = rootView.findViewById(R.id.name);
        mStepDetector = rootView.findViewById(R.id.total_steps);
        todaysSteps = rootView.findViewById(R.id.stepCount);
        currencyText = rootView.findViewById(R.id.currency_count);

        // Get the current time
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        final String todayDate = df.format(today);

        userName = db.getReference("Users").child(uid).child("Name");
        userStepCount = db.getReference("Users").child(uid).child("Steps");
        todayStepCount = db.getReference("Users").child(uid).child("Archive").child(todayDate);
        currencyCountDb = db.getReference("Users").child(uid).child("Currency");

        challengerDb = db.getReference("Users").child(uid).child("Archive").child(todayDate + " Challenger");

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

        // Add singled value event listener, only need to set challenger uid once upon entering fragment
        challengerDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if no challenger is found, then add a challenger for the day
                if(!dataSnapshot.exists()) {
                    getRandomUid(db.getReference("Users"));
                }
                else {
                    challengerUid = dataSnapshot.getValue(String.class);

                    // TODO with access to challenger uid, it would be best to call a function here because i think these are async calls
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    /*
     * Gets a random uid from users list and sets it to challenger uid
     */
    private void getRandomUid(DatabaseReference users) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    int userCount = (int) dataSnapshot.getChildrenCount();
                    int newChallenger = new Random().nextInt(userCount);

                    Iterator randomUser = dataSnapshot.getChildren().iterator();

                    for(int i = 0; i < newChallenger; i++) {
                        randomUser.next();
                    }

                    DataSnapshot childSnapshot = (DataSnapshot)randomUser.next();
                    challengerUid = childSnapshot.getKey();

                    challengerDb.setValue(challengerUid);

                    //TODO like the above TODO, probably make a function call from here if you use challenger uid
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
