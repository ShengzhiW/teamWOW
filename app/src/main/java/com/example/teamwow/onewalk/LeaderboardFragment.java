package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
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

    // for leaderboard
    private RecyclerView leaderboardView;
    private RecyclerView.Adapter leaderboardAdapter;
    private RecyclerView.LayoutManager leaderboardLayoutManager;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Integer> stepCounts = new ArrayList<>();

    private TableLayout leaderboardTable;

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
        rootView = inflater.inflate(R.layout.secretstuffsimtesting, container, false);
        buildLeaderboard();
        return rootView;
    }
    public void buildLeaderboard() {
        Query leaderQuery = db.getReference("Leaderboard").orderByChild("Steps");

        leaderboardTable = (TableLayout) rootView.findViewById(R.id.leaderboard_table);

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
                            stepCounts.add(leaderSnapshot.child("Steps").getValue(Integer.class));
                            totalMembers++;
                            if (totalMembers >= 10) break;
                        }
                    }
                }
                Collections.reverse(names);
                Collections.reverse(stepCounts);

                TextView name;
                TextView stepCount;
                for(int i = 0; i < names.size(); i++) {
                    String nameID = "rank" + (i+1) + "username";
                    int resNameID = getResources().getIdentifier(nameID, "id", getActivity().getPackageName());
                    name = (TextView) rootView.findViewById(resNameID);
                    name.setText(names.get(i));

                    String stepsID = "rank" + (i+1) + "steps";
                    int resStepsID = getResources().getIdentifier(stepsID, "id", getActivity().getPackageName());
                    stepCount = (TextView) rootView.findViewById(resStepsID);
                    stepCount.setText(String.valueOf(stepCounts.get(i)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
}
