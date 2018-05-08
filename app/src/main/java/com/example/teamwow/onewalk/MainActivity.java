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
                updateDatabase();

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

    private void updateDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Look up / add the specific user in/to the database and keep a reference while app is open
        final String email = user.getEmail();
        final String name = user.getDisplayName();
        final String uid = user.getUid();
        db.getReference("Users").child(uid).child("Email").setValue(email);
        db.getReference("Users").child(uid).child("Name").setValue(name);
        db.getReference("Leaderboard").child(uid).child("Name").setValue(name);
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
}

