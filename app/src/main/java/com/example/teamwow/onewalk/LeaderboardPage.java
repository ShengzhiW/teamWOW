package com.example.teamwow.onewalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardPage extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    // for leaderboard
    private RecyclerView leaderboardView;
    private RecyclerView.Adapter leaderboardAdapter;
    private RecyclerView.LayoutManager leaderboardLayoutManager;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_page);

        setupNavigationView();
        buildLeaderboard();
    }

    /* Pulls and displays the top 10 users on the leaderboard from the database */
    public void buildLeaderboard() {
        Query leaderQuery = db.getReference("Leaderboard").orderByChild("Steps").limitToLast(10);
        // attaches a listener to check when a user's step count is updated
        leaderQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot leaderSnapshot: dataSnapshot.getChildren()) {
                    if(leaderSnapshot.child("Steps").exists())
                        list.add(leaderSnapshot.child("Name").getValue(String.class)
                                + ": " + leaderSnapshot.child("Steps").getValue(Integer.class));
                }
                Collections.reverse(list);
                leaderboardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // sets up the leaderboard using a recycler view and adapter
        leaderboardView = (RecyclerView) findViewById(R.id.leaderboardView);
        leaderboardView.setHasFixedSize(true);
        leaderboardLayoutManager = new LinearLayoutManager(this);
        leaderboardView.setLayoutManager(leaderboardLayoutManager);
        leaderboardAdapter = new LeaderboardAdapter(list);
        leaderboardView.setAdapter(leaderboardAdapter);
    }

    /* Creates the bottom navigation bar */
    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectPage(menu.getItem(3));

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
                startActivity(intent);
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
