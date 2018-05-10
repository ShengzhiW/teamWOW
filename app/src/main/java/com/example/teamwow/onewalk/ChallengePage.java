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

public class ChallengePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_page);

        setupNavigationView();
    }

    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));

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
}
