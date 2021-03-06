package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/* Holds all of the main fragments, is essentially an empty page with a navigation bar */
public class ContainerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_page);

        // prevents the expanding of the menu icons
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        setupNavigationView();

        startService(new Intent(this, StepCounterService.class));
    }

    /* If app is killed, stop service here */
    @Override
    public void onDestroy() {
        super.onDestroy();
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

    /*
     * Chooses the fragment to display on the screen
     * Calls pushFragment
     */
    public void selectFragment(MenuItem item) {
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

    /* Replaces the currently displayed fragment with the specified fragment */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        }
    }


    /* Back button redirects to the home page */
    @Override
    public void onBackPressed(){
        selectFragment(((BottomNavigationView)findViewById(R.id.bottom_navigation)).getMenu().getItem(2));
    }
}
