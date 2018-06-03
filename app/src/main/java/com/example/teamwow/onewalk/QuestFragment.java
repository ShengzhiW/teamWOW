package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QuestFragment extends Fragment {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private TextView questText;
    private TextView rewardText;
    private TextView todaysSteps;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private int dailyCount = 0;
    private DatabaseReference todayStepCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_quest, container, false);
        buildQuests(rootView);
        return rootView;
    }

    private void buildQuests(final View v) {

        questText = v.findViewById(R.id.quest_text);
        rewardText = v.findViewById(R.id.reward_text);
        todaysSteps = v.findViewById(R.id.stepCount);

        // Get the current time
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        final String todayDate = df.format(today);

        todayStepCount = db.getReference("Users").child(uid).child("Archive").child(todayDate);

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        int dailyQuestNum = (day+month) % 3;

        DatabaseReference questTextRef = db.getReference("Quests").child("" + dailyQuestNum);
        questTextRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String quest = dataSnapshot.child("text").getValue(String.class);
                int reward = dataSnapshot.child("reward").getValue(int.class);

                questText.setText(quest);
                rewardText.setText("" + reward);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        todayStepCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) dailyCount = dataSnapshot.getValue(Integer.class);
                todaysSteps.setText(String.valueOf(dailyCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
