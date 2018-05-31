package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardFragment extends Fragment {

    View rootView;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    TextView username;
    TextView steps;

    // for leaderboard
    private RecyclerView leaderboardView;
    private RecyclerView.Adapter leaderboardAdapter;
    private RecyclerView.LayoutManager leaderboardLayoutManager;
    private int[] nameIds = {R.id.rank1username, R.id.rank2username, R.id.rank3username,
            R.id.rank4username, R.id.rank5username, R.id.rank6username, R.id.rank7username,
            R.id.rank8username, R.id.rank9username, R.id.rank10username};
    private int[] stepIds = {R.id.rank1steps, R.id.rank2steps, R.id.rank3steps,
            R.id.rank4steps, R.id.rank5steps, R.id.rank6steps, R.id.rank7steps,
            R.id.rank8steps, R.id.rank9steps, R.id.rank10steps};
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Integer> stepCounts = new ArrayList<>();
    private ArrayList<Boolean> showSteps = new ArrayList<>();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private String name = "";
    private int stepCount = 0;

    private DatabaseReference userName;
    private DatabaseReference userStepCount;


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
//        buildLeaderboard();
//        return rootView;
//    }
//
//    /* Pulls and displays the top 10 users on the leaderboard from the database */
//    public void buildLeaderboard() {
//        Query leaderQuery = db.getReference("Leaderboard").orderByChild("Steps");
//
//        // attaches a listener to check when a user's step count is updated
//        leaderQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                list.clear();
//                int totalMembers = 0;
//                for (DataSnapshot leaderSnapshot: dataSnapshot.getChildren()) {
//                    if(leaderSnapshot.child("Steps").exists()) {
//                        if((leaderSnapshot.child("Private").getValue()).equals(false)) {
//                                    list.add(leaderSnapshot.child("Name").getValue(String.class)
//                                            + ": " + leaderSnapshot.child("Steps").getValue(Integer.class));
//                                    totalMembers++;
//                                    if (totalMembers >= 10) break;
//                                }
//                    }
//                }
//                Collections.reverse(list);
//
//                leaderboardAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//
//        // sets up the leaderboard using a recycler view and adapter
//        leaderboardView = (RecyclerView) rootView.findViewById(R.id.leaderboardView);
//        leaderboardView.setHasFixedSize(true);
//        leaderboardLayoutManager = new LinearLayoutManager(getActivity());
//        leaderboardView.setLayoutManager(leaderboardLayoutManager);
//        leaderboardAdapter = new LeaderboardAdapter(list);
//        leaderboardView.setAdapter(leaderboardAdapter);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        return rootView;
    }

    @Nullable
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        buildLeaderboard(v);
    }

    public void buildLeaderboard(final View v) {

        userName = db.getReference("Users").child(uid).child("Name");
        userStepCount = db.getReference("Users").child(uid).child("Steps");

        username = rootView.findViewById(R.id.userLB);
        steps = rootView.findViewById(R.id.userLBSteps);

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
                if(dataSnapshot.exists()) stepCount = dataSnapshot.getValue(Integer.class);
                steps.setText(String.valueOf(stepCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Query leaderQuery = db.getReference("Leaderboard").orderByChild("Step Sort");

        // attaches a listener to check when a user's step count is updated
        leaderQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                names.clear();
                stepCounts.clear();
                int totalMembers = 0;
                for (DataSnapshot leaderSnapshot: dataSnapshot.getChildren()) {
                    if(leaderSnapshot.child("Steps").exists()) {
                        if((leaderSnapshot.child("Private").getValue()).equals(false)) {
                            names.add(leaderSnapshot.child("Name").getValue(String.class));

                            if((leaderSnapshot.child("Private Steps").getValue()).equals(false)){
                               showSteps.add(true);
                            }
                            else{
                                showSteps.add(false);
                            }
                            stepCounts.add(leaderSnapshot.child("Steps").getValue(Integer.class));

                            totalMembers++;
                            if (totalMembers >= 10) break;
                        }
                    }
                }

                TextView name;
                TextView stepCount;

                for(int i = 0; i < names.size(); i++) {
                    name = (TextView) v.findViewById(nameIds[i]);
                    name.setText(names.get(i));
                    name.setTextColor(Color.parseColor("#747474"));

                    stepCount = (TextView) v.findViewById(stepIds[i]);
                    if((showSteps.get(i)).equals(true)) {
                        stepCount.setText(String.valueOf(stepCounts.get(i)));
                    }
                    else{
                        stepCount.setText(" ");
                    }
                    stepCount.setTextColor(Color.parseColor("#B7B7B7"));
                }


                ((TextView)v.findViewById(nameIds[0])).setTextColor(Color.parseColor("#FFD700"));
                ((TextView)v.findViewById(nameIds[1])).setTextColor(Color.parseColor("#A8A8A8"));
                ((TextView)v.findViewById(nameIds[2])).setTextColor(Color.parseColor("#CD7F32"));
                ((TextView)v.findViewById(stepIds[0])).setTextColor(Color.parseColor("#FFD700"));
                ((TextView)v.findViewById(stepIds[1])).setTextColor(Color.parseColor("#A8A8A8"));
                ((TextView)v.findViewById(stepIds[2])).setTextColor(Color.parseColor("#CD7F32"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
}
