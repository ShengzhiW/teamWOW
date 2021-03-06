package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ProfileFragment extends Fragment {
    Button closeButton;

    // variable for the default text currently on screen
    private TextView username;
    private TextView mStepDetector;
    private TextView totalCurrency;
    private TextView totalHats;
    private TextView totalBodies;
    private TextView totalQuests;
    private TextView totalDays;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private String name = "";
    private int count = 0;
    private int hats = 0;
    private int bodies = 0;
    private int numquests = 0;
    private int currency = 0;

    private DatabaseReference userName;
    private DatabaseReference userStepCount;
    private DatabaseReference userCurrency;
    private DatabaseReference userHats;
    private DatabaseReference userBodies;
    private DatabaseReference questsCompleted;
    private DatabaseReference daysWalked;

    private DatabaseReference userDB = db.getReference("Users").child(uid);

    private int bodyIndex;
    private int hatIndex;
    private DatabaseReference dbBodyIdx = userDB.child("BodyIdx");
    private DatabaseReference dbHatIdx = userDB.child("HatIdx");
    private DatabaseReference hatDB = userDB.child("Inventory").child("Hat");
    private DatabaseReference bodyDB = userDB.child("Inventory").child("Body");

    final private int [] bodiesDrawables = {
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

    final private int [] hatDrawables = {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        closeButton = rootView.findViewById(R.id.closeProfile);

        // get the text by id
        username = rootView.findViewById(R.id.name);
        mStepDetector = rootView.findViewById(R.id.total_step_count);
        totalCurrency = rootView.findViewById(R.id.total_currency_count);
        totalHats = rootView.findViewById(R.id.total_hats);
        totalBodies = rootView.findViewById(R.id.total_bodies);
        totalQuests = rootView.findViewById(R.id.total_quests);
        totalDays = rootView.findViewById(R.id.total_days);

        userName = userDB.child("Name");
        userStepCount = userDB.child("Steps");
        userCurrency = userDB.child("Lifetime Currency");
        userHats = userDB.child("Inventory").child("Hat");
        userBodies = userDB.child("Inventory").child("Body");
        questsCompleted = userDB.child("Quests Completed");
        daysWalked = userDB.child("Archive");

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

        // Add listener to get the total hats
        userHats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List array = (List) dataSnapshot.getValue();
                if(array != null)
                {
                    for(int i = 0; i < array.size(); i++){
                        if(Integer.valueOf(array.get(i).toString()) > 0){
                            hats++;
                        }
                    }
                    totalHats.setText(String.valueOf(hats));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Add listener to get the total bodies
        userBodies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List array = (List) dataSnapshot.getValue();
                if(array != null)
                {
                    for(int i = 0; i < array.size(); i++){
                        if(Integer.valueOf(array.get(i).toString()) > 0){
                            bodies++;
                        }
                    }
                    totalBodies.setText(String.valueOf(bodies));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //Add listener to get the total quests completed
        questsCompleted.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()) numquests = dataSnapshot.getValue(Integer.class);
                    totalQuests.setText(String.valueOf(numquests));

            }

            @Override
            public void onCancelled(DatabaseError databaseError){}

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

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        // Add listener to get the total number of days walked
        daysWalked.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap array = (HashMap) dataSnapshot.getValue();

                if(array != null)
                {

                    Set<String> keys = array.keySet();
                    String[] keyList = keys.toArray(new String[keys.size()]);

                    ArrayList<String> days = new ArrayList<String>();

                    // only add days walked, not challengers
                    for(String key: keyList) {
                        if(key.length() <= 10) {
                            days.add(key);
                        }
                    }
                    totalDays.setText(String.valueOf(days.size()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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
