package com.example.teamwow.onewalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

public class ChallengePage extends AppCompatActivity {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private TextView questText;
    private TextView rewardText;
    private int questNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_page);

        setupNavigationView();

        questText = findViewById(R.id.quest_text);
        rewardText = findViewById(R.id.reward_text);

        buildChallenges();
    }

    /* Class to retrieve the challenges once a day
    *  Author: Connie
    */
    private void buildChallenges() {
        // Get the current date and time
        Calendar now = Calendar.getInstance();
        long time = now.getTimeInMillis();

        // Create object of SharedPreferences.
        SharedPreferences sharedPref= getSharedPreferences("updateTime", 0);
        //now get Editor
        SharedPreferences.Editor editor= sharedPref.edit();

        SharedPreferences sharedQuestNum = getSharedPreferences("dailyQuest", 0);
        SharedPreferences.Editor questNumEditor = sharedQuestNum.edit();

        // Retrieved stored time.
        long storedTime = sharedPref.getLong("updateTime", 0);
        long diff = time - storedTime;


        // If stored time doesn't exist yet then create it
        if(storedTime == 0 || diff > 3600000  * 24)
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
        }

        int dailyQuestNum = sharedQuestNum.getInt("dailyQuest", 0);

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

    public void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /* Creates the bottom navigation bar */
    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectPage(menu.getItem(0));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectPage(item);
                            return false;
                        }
                    });
        }
    }

    /* Starts the activity corresponding to the selected nav-bar button */
    protected void selectPage(MenuItem item) {
        item.setChecked(true);
        Intent intent;

        switch (item.getItemId()) {
            case R.id.navigation_challenge:
                // Action to perform when challenge Menu item is selected.

                break;
            case R.id.navigation_shop:
                // Action to perform when shop Menu item is selected.
                intent = new Intent(this, ShopPage.class);
                startActivity(intent);
                break;
            case R.id.navigation_home:
                // Action to perform when home Menu item is selected.
                intent = new Intent(this, StepCounter.class);
                startActivity(intent);
                break;
            case R.id.navigation_leader_board:
                // Action to perform when leaderboard Menu item is selected.
                intent = new Intent(this, LeaderboardPage.class);
                startActivity(intent);
                break;
            case R.id.navigation_settings:
                // Action to perform when settings Menu item is selected.
                intent = new Intent(this, SettingsPage.class);
                startActivity(intent);
                break;
        }
    }

    /** have the back button redirect to the home page*/
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, StepCounter.class);
        startActivity(intent);
    }

}
