package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity{
    // Firebase authentication
    private FirebaseAuth auth; // Firebase authentication for log in
    private static final int RC_SIGN_IN = 123; // Some number needed for authentication

    // Reference to the firebase database
    private FirebaseDatabase db;

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* Firebase Database */
        db = FirebaseDatabase.getInstance(); // Get the firebase database

        /* Firebase Authentication */
        auth = FirebaseAuth.getInstance();

        /* Start log in. Email is available for log in */
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                .build(), RC_SIGN_IN);
        setupNavigationView();

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
                String name = user.getDisplayName();
                String uid = user.getUid();
                db.getReference("Users").child(uid).child("Email").setValue(email);
                db.getReference("Users").child(uid).child("Name").setValue(name);

                //loginUser();
                //Test making an intent
                Intent intent = new Intent(this, StepCounter.class);
                startActivity(intent);
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
        return (auth.getCurrentUser() != null);
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

