package com.example.teamwow.onewalk;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

public class QuestFragment extends Fragment {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private TextView questText;
    private TextView rewardText;
    private int questNum;

    //private static final String ACTION_EDIT =
            //"com.example.android.updateQuest.ACTION_EDIT";

    //private static final int UPDATE_QUEST_ID = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_quest, container, false);
        questText = rootView.findViewById(R.id.quest_text);
        rewardText = rootView.findViewById(R.id.reward_text);

        //buildChallenges();

        /* WHAT AM I DOING???????? */

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dailyQuestNum = day%3;

        Context context = this.getActivity();

        /* ALL OF THIS IS POINTLES????? */
        // Shared preferences
        /* SharedPreferences sharedQuestNum = context.getSharedPreferences("dailyQuest", 0);
        SharedPreferences.Editor questNumEditor = sharedQuestNum.edit();
        int dailyQuestNum = sharedQuestNum.getInt("dailyQuest", 0);

        long afterOneDay = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        // Making an intent??????
        Intent updateQuestIntent = new Intent();

        // Create a pending intent
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, UPDATE_QUEST_ID
                , updateQuestIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm???
        alarmManager.setRepeating(AlarmManager.RTC, afterOneDay, AlarmManager.INTERVAL_DAY,
                alarmPendingIntent);*/


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
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });


        return rootView;
    }

    /* Class to retrieve the challenges once a day
     *  Author: Connie
     */
    private void buildChallenges() {

        //int hours = calendar.get(Calendar.HOUR_OF_DAY);
        //long time = calendar.getTimeInMillis();

        // Create object of SharedPreferences.
        //SharedPreferences sharedPref = getActivity().getSharedPreferences("updateTime", 0);
        //now get Editor
        //SharedPreferences.Editor editor = sharedPref.edit();

        SharedPreferences sharedQuestNum = getActivity().getSharedPreferences("dailyQuest", 0);
        SharedPreferences.Editor questNumEditor = sharedQuestNum.edit();

        int dailyQuestNum = sharedQuestNum.getInt("dailyQuest", 1);

        // Retrieved stored time.
        //long storedTime = sharedPref.getLong("updateTime", 0);
        //long diff = time - storedTime;




        /* SEND HELPPPPP */

        // If stored time doesn't exist yet then create it
        /*if(storedTime == 0 || hours == 7)
        {
            // Random number generator
            Random rand = new Random();
            int questNum = rand.nextInt(3);

            // Update quest number
            questNumEditor.putInt("dailyQuest", questNum);

            //Update last time of updating
            editor.putLong("updateTime", time);
            editor.apply();
        }
        else
        {
            // Do nothing
        }*/


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
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });

        return;
    }
}
