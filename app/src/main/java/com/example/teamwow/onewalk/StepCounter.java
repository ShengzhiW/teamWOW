package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
    // variables for sensor manager and sensor, used to implement step detector
    private SensorManager mSensorManager;
    private Sensor mSensor;
    // variable for the default text currently on screen ("Hello World")
    private TextView mStepDetector;
    // step count
    private int count = 0;
    private FirebaseDatabase db;
    private DatabaseReference userStepCount;
    private String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setupNavigationView();

        Intent intent = getIntent();

        // Pull current step count from database
        initialStepCount();

        // set up step detector using a manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        // begin listening for steps, set the delay time to fastest speed
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * @author Alex Zhu
     *
     * Event listener that fires when the step detector is triggered, i.e. a step
     * is taken. Updates database.
     *
     * @param event - holds information about the fired event, irrelevant
     * for this function for now
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // increment count and store value in database
        count++;
        // Write the count value to the database under the user
        userStepCount.setValue(count);
        db.getReference("Leaderboard").child(uid).child("Steps").setValue(count);
        // set text to current step count
        mStepDetector.setText(String.valueOf(count));
    }

    /**
     * @author Alex Zhu
     *
     * Placeholder function to satisfy interface, fires when accuracy of
     * the sensor changes.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
     * @author Alex Lo
     *
     * Looks up the user in the database and keeps a reference
     * Grabs the existing step count if there is one
     */
    public void initialStepCount() {
        // Get the database instance
        db = FirebaseDatabase.getInstance();

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Look up specific user in database and keep a reference while app is open
        final String email = user.getEmail();
        uid = user.getUid();
        db.getReference("Users").child(uid).child("Email").setValue(email);

        // Get the text with id "test"
        mStepDetector = findViewById(R.id.stepCount);
        // Get a reference to the step count, and read the data and attach it to the screen
        userStepCount = db.getReference("Users").child(uid).child("Steps");

        ValueEventListener stepListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) count = dataSnapshot.getValue(Integer.class);
                mStepDetector.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userStepCount.addListenerForSingleValueEvent(stepListener);
    }

    /* Name: setupNavigationView
     * Description: Creates the bottom navigation bar
     * Source: https://tutorialwing.com/android-bottom-navigation-view-tutorial-with-example/
     * Author: Jungyong Yi
     */
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

    /* Name: selectFragment
     * Description: Calls pushFragment to bring up appropriate screen
     * Source: https://tutorialwing.com/android-bottom-navigation-view-tutorial-with-example/
     * Author: Jungyong Yi
     */
    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.navigation_challenge:
                // Action to perform when challenge Menu item is selected.
                pushFragment(new ChallengeFragment());
                break;
            case R.id.navigation_shop:
                // Action to perform when shop Menu item is selected.
                pushFragment(new ShopFragment());
                break;
            case R.id.navigation_home:
                // Action to perform when home Menu item is selected.
                pushFragment(new HomeFragment());
                break;
            case R.id.navigation_leader_board:
                // Action to perform when leaderboard Menu item is selected.
                pushFragment(new LeaderboardFragment());
                break;
            case R.id.navigation_settings:
                // Action to perform when settings Menu item is selected.
                pushFragment(new SettingsFragment());
                break;
        }
    }

    /* Name: pushFragment
     * Description: Changes the screen to be the selected screen from navigation bar
     * Source: https://tutorialwing.com/android-bottom-navigation-view-tutorial-with-example/
     * Author: Jungyong Yi
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }

}
