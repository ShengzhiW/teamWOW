package com.example.alexz.testpedometer;

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // variables for sensor manager and sensor, used to implement step detector
    private SensorManager mSensorManager;
    private Sensor mSensor;

    // variable for the default text currently on screen ("Hello World")
    private TextView mStepDetector;

    // step count
    private int count = 0;

    // Firebase authentication
    private FirebaseAuth auth; // Firebase authentication for log in
    private static final int RC_SIGN_IN = 123; // Some number needed for authentication

    // Reference to the firebase database
    private FirebaseDatabase db;
    private DatabaseReference myDBRef;
    private DatabaseReference userStepCount; // Step count for the current user

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* Firebase Database */
        db = FirebaseDatabase.getInstance(); // Get the firebase database
        myDBRef = db.getReference("Steps"); // Get a database reference for the "Steps" d

        /* Firebase Authentication */
        auth = FirebaseAuth.getInstance();

        /* Start log in. Email is available for log in */
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                .build(), RC_SIGN_IN);

        // gets the text with id "test", centered on screen
        mStepDetector = (TextView)findViewById(R.id.test);

        // set up step detector using a manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // begin listening for steps, set the delay time to fastest speed
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

        setupNavigationView();
    }

    /**
     * @author Alex Zhu
     *
     * Event listener that fires when the step detector is triggered, i.e. a step
     * is taken.
     *
     * @param event - holds information about the fired event, irrelevant
     * for this function for now
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // increment count and store value in database
        count++;
        myDBRef.setValue(count); // Write the count value to the database
        if(userStepCount != null) userStepCount.setValue(count); // Write the count value to the database under the user
        // set text to current step count
        mStepDetector.setText(String.valueOf(count));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
     * Name: onActivityResult()
     * Parameters: int requestCode - Number that identifies the request
     *             int resultCode - The result of the method that called this
     *                              RESULT_OK for success RESULT_CANCELED for fail
     *             Intent data - data returned from the method that called this
     * Return: void
     * Description:  If user signed in correctly then, user logs in
     *  Otherwise displays an error message
     *
     * Author: Connie Guan
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // If successfully signed in
            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Look up / add the specific user in/to the database and keep a reference while app is open
                String email = user.getEmail();
                String uid = user.getUid();
                db.getReference("Users").child(uid).child("Email").setValue(email);
                userStepCount = db.getReference("Users").child(uid).child("Steps");

                //loginUser();
            }
            if(resultCode == RESULT_CANCELED){
                displayMessage(getString(R.string.signin_failed));
            }
            return;
        }
        displayMessage(getString(R.string.unknown_response));
    }

    /* Name: isUserLogin()
     * Parameters: None
     * Return: boolean - whether or  not the user is logged in
     * Description: Checks whether or not the user is signed in or not
     * .getCurrentUser will return null is the user is not signed in
     *
     * Author: Connie Guan
     */
    private boolean isUserLogin(){
        // If the current user is signed in
        if(auth.getCurrentUser() != null){
            return true;
        }
        return false;
    }

    /* Name: loginUser()
     * Parameters: None
     * Return: void
     * Description: Starts logging in the user if they are already "signed" in
     *
     * Author: Connie Guan
     */
    /*private void loginUser(){
        Intent loginIntent = new Intent(MainActivity.this, SigninActivity.class);
        startActivity(loginIntent);
        finish();
    }*/

    /* Name: displayMessage()
     * Parameters: String message - message to be displayed
     * Return: void
     * Description: Displays a pop up message to user
     *
     * Author: Connie Guan
     */
    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

