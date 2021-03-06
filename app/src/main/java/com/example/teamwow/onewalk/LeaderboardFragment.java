package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.graphics.Color;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    View rootView;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    TextView username;
    TextView steps;

    // IDs for xml text elements to update
    final private int[] nameIds = {
            R.id.rank1username,
            R.id.rank2username,
            R.id.rank3username,
            R.id.rank4username,
            R.id.rank5username,
            R.id.rank6username,
            R.id.rank7username,
            R.id.rank8username,
            R.id.rank9username,
            R.id.rank10username
    };

    final private int[] stepIds = {
            R.id.rank1steps,
            R.id.rank2steps,
            R.id.rank3steps,
            R.id.rank4steps,
            R.id.rank5steps,
            R.id.rank6steps,
            R.id.rank7steps,
            R.id.rank8steps,
            R.id.rank9steps,
            R.id.rank10steps
    };

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Integer> stepCounts = new ArrayList<>();
    private ArrayList<Boolean> showSteps = new ArrayList<>();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private String name = "";
    private int stepCount = 0;

    private DatabaseReference userName;
    private DatabaseReference userStepCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        return rootView;
    }

    /* Ensure that view has been created first */
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

        // sort in descending order of step count ("step sort" is negative)
        Query leaderQuery = db.getReference("Leaderboard").orderByChild("Step Sort");

        // attaches a listener to check when a user's step count is updated
        leaderQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                names.clear();
                stepCounts.clear();
                int totalMembers = 0;
                for (DataSnapshot leaderSnapshot: dataSnapshot.getChildren()) {
                    // check that user has steps and is not set to private
                    if(leaderSnapshot.child("Steps").exists()) {
                        if((leaderSnapshot.child("Private").getValue()).equals(false)) {
                            names.add(leaderSnapshot.child("Name").getValue(String.class));

                            // determine whether or not to display step count on leaderboard
                            if((leaderSnapshot.child("Private Steps").getValue()).equals(false)) {
                                showSteps.add(true);
                            } else {
                                showSteps.add(false);
                            }
                            stepCounts.add(leaderSnapshot.child("Steps").getValue(Integer.class));

                            totalMembers++;
                            if (totalMembers >= 10) break; // count top 10 users that are not private
                        }
                    }
                }
                updateLeaderboardText(v);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    /* Update the names and step counts of each user on the leaderboard */
    public void updateLeaderboardText(final View v) {
        TextView name;
        TextView stepCount;

        for(int i = 0; i < names.size(); i++) {
            name = (TextView) v.findViewById(nameIds[i]);
            name.setText(names.get(i));
            name.setTextColor(Color.parseColor("#747474"));

            stepCount = (TextView) v.findViewById(stepIds[i]);
            if((showSteps.get(i)).equals(true)) {
                stepCount.setText(String.valueOf(stepCounts.get(i)));
            } else {
                stepCount.setText(" ");
            }
            stepCount.setTextColor(Color.parseColor("#B7B7B7"));
        }

        // Gold for 1st, Silver for 2nd, Bronze for 3rd
        ((TextView)v.findViewById(nameIds[0])).setTextColor(Color.parseColor("#FFD700"));
        ((TextView)v.findViewById(nameIds[1])).setTextColor(Color.parseColor("#A8A8A8"));
        ((TextView)v.findViewById(nameIds[2])).setTextColor(Color.parseColor("#CD7F32"));
        ((TextView)v.findViewById(stepIds[0])).setTextColor(Color.parseColor("#FFD700"));
        ((TextView)v.findViewById(stepIds[1])).setTextColor(Color.parseColor("#A8A8A8"));
        ((TextView)v.findViewById(stepIds[2])).setTextColor(Color.parseColor("#CD7F32"));
    }
}
