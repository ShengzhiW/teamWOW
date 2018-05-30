package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private int bodyIndex;
    private int hatIndex;
    private DatabaseReference dbRef = db.getReference("Users").child(uid);
    private DatabaseReference dbBodyIdx = db.getReference("Users").child(uid).child("BodyIdx");
    private DatabaseReference dbHatIdx = db.getReference("Users").child(uid).child("HatIdx");
    private DatabaseReference hatDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Hat");
    ArrayList<Integer> hatArray = new ArrayList<Integer>();

    private DatabaseReference bodyDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Body");
    ArrayList<Integer> bodyArray = new ArrayList<>();


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

        userName = db.getReference("Users").child(uid).child("Name");
        userStepCount = db.getReference("Users").child(uid).child("Steps");
        userCurrency = db.getReference("Users").child(uid).child("Lifetime Currency");
        userHats = db.getReference("Users").child(uid).child("Inventory").child("Hat");
        userBodies = db.getReference("Users").child(uid).child("Inventory").child("Body");
        questsCompleted = db.getReference("Users").child(uid).child("Quests Completed");
        daysWalked = db.getReference("Users").child(uid).child("Archive");


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

                //    Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();
                for (DataSnapshot item : snapshot.getChildren()) {
                    //       Toast.makeText(getActivity(),item.toString(),Toast.LENGTH_SHORT).show();
                    if( item.getValue(Integer.class)  == 2){
                        // Toast.makeText(getActivity(),"this value is a 2 " + item.getKey(),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(),"set body index to " + item.getKey().toString(),Toast.LENGTH_SHORT).show();
                        bodyIndex = Integer.parseInt(item.getKey());
                        dbBodyIdx.setValue(item.getKey());

                        if(isAdded())
                        {
                            DisplayMetrics dimension = new DisplayMetrics();
                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dimension);
                            ImageView imgView = (ImageView) rootView.findViewById(R.id.imgView) ;


                            //Toast.makeText(getActivity(), "the bodyIdx is" + bodyIndex, Toast.LENGTH_SHORT).show();
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

                //    Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();
                for (DataSnapshot item : snapshot.getChildren()) {
                    //       Toast.makeText(getActivity(),item.toString(),Toast.LENGTH_SHORT).show();
                    if( item.getValue(Integer.class)  == 2){
                        // Toast.makeText(getActivity(),"this value is a 2 " + item.getKey(),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(),"set hat index to " + item.getKey().toString(),Toast.LENGTH_SHORT).show();
                        hatIndex = Integer.parseInt(item.getKey());
                        dbHatIdx.setValue(item.getKey());

                        if(isAdded())
                        {
                            //Toast.makeText(getActivity(), "the bodyIdx is" + bodyIndex, Toast.LENGTH_SHORT).show();
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


        // Add listener to get the total number of days walked
        daysWalked.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap array = (HashMap) dataSnapshot.getValue();
                if(array != null)
                {
                    totalDays.setText(String.valueOf(array.size()));
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
