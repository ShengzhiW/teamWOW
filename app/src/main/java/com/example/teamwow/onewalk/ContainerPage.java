package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ContainerPage extends AppCompatActivity {

    // variable to listen when a step a taken
    private StepCounterReceiver mStepCounterReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_page);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        setupNavigationView();

        startService(new Intent(this, StepCounterService.class));
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // create a BroadcastReceiver to listen for steps from step counting service
        if(mStepCounterReceiver == null) mStepCounterReceiver = new StepCounterReceiver();

        // receiver listens for intent with the action "update_count"
        IntentFilter intentFilter = new IntentFilter("update_count");
        registerReceiver(mStepCounterReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister BroadcastReceiver, no longer need to listen for steps
        if(mStepCounterReceiver != null) unregisterReceiver(mStepCounterReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // if app is killed, stop service here
        stopService(new Intent(this, StepCounterService.class));
    }

    /* Creates the bottom navigation bar */
    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(2));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });
        }
    }

    protected void selectFragment(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.navigation_quest:
                pushFragment(new QuestFragment());
                break;
            case R.id.navigation_shop:
                pushFragment(new ShopFragment());
                break;
            case R.id.navigation_today:
                pushFragment(new TodayFragment());
                break;
            case R.id.navigation_leader_board:
                pushFragment(new LeaderboardFragment());
                break;
            case R.id.navigation_settings:
                pushFragment(new SettingsFragment());
                break;
        }
    }

    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        }
    }


    /* have the back button redirect to the home page*/
    @Override
    public void onBackPressed(){
        selectFragment(((BottomNavigationView)findViewById(R.id.bottom_navigation)).getMenu().getItem(2));
    }
}
