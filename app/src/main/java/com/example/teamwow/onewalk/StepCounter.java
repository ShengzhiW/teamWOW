package com.example.teamwow.onewalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class StepCounter extends AppCompatActivity {
    // variable to listen when a step a taken
    private StepCounterReceiver mStepCounterReceiver;

    // variable for the default text currently on screen
    private TextView mStepDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);

        // Set up navigation bar
        setupNavigationView();

        // get the text by id
        mStepDetector = findViewById(R.id.stepCount);

        // begin step counting service
        startService(new Intent(this, StepCounterService.class));
    }

    @Override
    public void onResume() {
        super.onResume();

        // create a BroadcastReceiver to listen for steps from step counting service
        if(mStepCounterReceiver == null) mStepCounterReceiver = new StepCounterReceiver();

        // receiver listens for intens with the action "update_count"
        IntentFilter intentFilter = new IntentFilter("update_count");
        registerReceiver(mStepCounterReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister BroadcastReceiver, no longer need to listen for steps
        if(mStepCounterReceiver != null) unregisterReceiver(mStepCounterReceiver);
    }

    /* Creates the bottom navigation bar */
    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectPage(menu.getItem(2));

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
                intent = new Intent(this, ChallengePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.navigation_shop:
                // Action to perform when shop Menu item is selected.
                intent = new Intent(this, ShopPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.navigation_home:
                // Action to perform when home Menu item is selected.

                break;
            case R.id.navigation_leader_board:
                // Action to perform when leaderboard Menu item is selected.
                intent = new Intent(this, LeaderboardPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.navigation_settings:
                // Action to perform when settings Menu item is selected.
                intent = new Intent(this, SettingsPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // if app is killed, stop service here
        stopService(new Intent(this, StepCounterService.class));
    }

    /*
     * Class that runs the function onReceive every time a step is detected in StepCounterService
     * Receives information from service via the sendBroadcast function (see StepCounterService)
     */
    private class StepCounterReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // get the current count from the intent's extras defined in StepCounterService
            int currentCount = intent.getExtras().getInt("count");

            // update the text on screen
            mStepDetector.setText(String.valueOf(currentCount));
        }
    }
}
