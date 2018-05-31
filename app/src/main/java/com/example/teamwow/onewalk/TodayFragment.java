package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class TodayFragment extends Fragment {
    // variable for the default text currently on screen
    private TextView username;
    private TextView mStepDetector;
    private TextView todaysSteps;
    private TextView currencyText;
    private TextView greetings;
    private TextView totalHats;
    private TextView totalBodies;
    private TextView totalQuests;

    private TextView challengerNickname;
    private TextView challengerSteps;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private String name = "";
    private int count = 0;
    private int dailyCount = 0;
    private int currencyCount = 0;
    private int hats = 0;
    private int bodies = 0;

    private String opponentNickname = "";
    private int opponentSteps = 0;

    private int bodyIndex;
    private int hatIndex;
    private DatabaseReference dbBodyIdx = db.getReference("Users").child(uid).child("BodyIdx");
    private DatabaseReference dbHatIdx = db.getReference("Users").child(uid).child("HatIdx");
    private DatabaseReference hatDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Hat");

    private DatabaseReference bodyDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Body");

    private String challengerUid = "";

    private DatabaseReference userName;
    private DatabaseReference todayStepCount;

    private String greeting;

    private DatabaseReference challengerDb;
    private DatabaseReference opponentRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        // get the text by id
        username = rootView.findViewById(R.id.name);
        //mStepDetector = rootView.findViewById(R.id.total_steps);
        todaysSteps = rootView.findViewById(R.id.stepCount);
        //currencyText = rootView.findViewById(R.id.currency_count);
        greetings = rootView.findViewById(R.id.textView);

        // set textviews to challenger
        challengerNickname = rootView.findViewById(R.id.challengerToday);
        challengerSteps = rootView.findViewById(R.id.opponentStepCount);


        // Get the current time
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        final String todayDate = df.format(today);

        userName = db.getReference("Users").child(uid).child("Name");
        todayStepCount = db.getReference("Users").child(uid).child("Archive").child(todayDate);


        final int [] bodiesDrawables = {
                R.drawable.cowboy_body,
                R.drawable.magician_body,
                R.drawable.jojo_body,
                R.drawable.earth_body,
                R.drawable.chef_body,
                R.drawable.viking_body,
                R.drawable.striped_body,
                R.drawable.yellow_body,
                R.drawable.lee,
                R.drawable.gary_body
        };

        final int [] hatDrawables = {
                R.drawable.baseball_scale,
                R.drawable.magician_scale,
                R.drawable.pirate_scale,
                R.drawable.tree_scale,
                R.drawable.poop_scale,
                R.drawable.cowboyhat_scale,
                R.drawable.chef_scale,
                R.drawable.konoha_scale,
                R.drawable.viking_scale,
                R.drawable.leprechaun_hat_scale,
                R.drawable.sun_hat_scale,
                R.drawable.jojo_hat_scale,
                R.drawable.duck_hat_scale
        };


        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if(hour>= 12 && hour < 17){
            greeting = "Good Afternoon, ";
        } else if(hour >= 17 && hour < 21){
            greeting = "Good Evening, ";
        } else if(hour >= 21 && hour < 24){
            greeting = "Good Night, ";
        } else if (hour < 12){
            greeting = "Good Morning, ";
        } else{
            greeting = "Hello, ";
        }

        challengerDb = db.getReference("Users").child(uid).child("Archive").child(todayDate + " Challenger");

        //Add listener to get the username
        userName.addValueEventListener(new ValueEventListener(){
           @Override
           public void onDataChange(DataSnapshot dataSnapshot){
               if(dataSnapshot.exists()) name = dataSnapshot.getValue(String.class);
               String greetUsername = greeting + String.valueOf(name) + ".";
               username.setText(greetUsername);
           }

           @Override
            public void onCancelled(DatabaseError databaseError){}

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


        /* Set up inventory array */
        bodyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    if( item.getValue(Integer.class)  == 2){
                        bodyIndex = Integer.parseInt(item.getKey());
                        dbBodyIdx.setValue(item.getKey());

                        if(isAdded())
                        {
                            DisplayMetrics dimension = new DisplayMetrics();
                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dimension);
                            ImageView imgView = (ImageView) rootView.findViewById(R.id.imgView) ;

                            Drawable drawable = getResources().getDrawable(bodiesDrawables[bodyIndex]);
                            imgView.setImageDrawable(drawable);
                        }


                    }
                }
            }
            // onCancelled...
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // DO nothing
            }
        });

        hatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    if( item.getValue(Integer.class)  == 2){
                        hatIndex = Integer.parseInt(item.getKey());
                        dbHatIdx.setValue(item.getKey());

                        if(isAdded())
                        {
                            ImageView imgViewHat = (ImageView) rootView.findViewById(R.id.imgViewHat) ;
                            Drawable drawableHat = getResources().getDrawable(hatDrawables[hatIndex]);
                            imgViewHat.setImageDrawable(drawableHat);
                        }



                    }
                }
            }
            // onCancelled...
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // DO nothing
            }
        });

        // Add singled value event listener, only need to set challenger uid once upon entering fragment
        challengerDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if no challenger is found, then add a challenger for the day
                if(!dataSnapshot.exists()) {
                    getRandomUid(db.getReference("Users"), todayDate);
                }
                else {
                    challengerUid = dataSnapshot.getValue(String.class);
                    connectChallengerToLayout(todayDate);
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
    private void getRandomUid(DatabaseReference users, final String todayDate) {
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

                    if(!childSnapshot.getKey().equals(uid)) {
                        challengerDb.setValue(challengerUid);
                        connectChallengerToLayout(todayDate);
                    }
                    else {
                        challengerNickname.setText("Come back later!");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void connectChallengerToLayout(final String todayDate) {
        opponentRef = db.getReference("Users").child(challengerUid);
        opponentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    opponentNickname = dataSnapshot.child("Name").getValue(String.class);

                    DataSnapshot checkOpponentSteps = dataSnapshot.child("Archive").child(todayDate);
                    if(checkOpponentSteps.exists()) {
                        opponentSteps = checkOpponentSteps.getValue(Integer.class);
                    }
                    else {
                        opponentSteps = 0;
                    }

                    challengerNickname.setText(opponentNickname);
                    challengerSteps.setText(String.valueOf(opponentSteps));
                }
                else {
                    challengerNickname.setText("Please reload.");
                    challengerDb.removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
