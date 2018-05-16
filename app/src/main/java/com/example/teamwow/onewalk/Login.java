package com.example.teamwow.onewalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class Login extends AppCompatActivity{
    // Firebase authentication
    private FirebaseAuth auth; // Firebase authentication for log in
    private static final int RC_SIGN_IN = 123; // Some number needed for authentication

    // Reference to the firebase database
    private FirebaseDatabase db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    /* Name: updateDatabase()
     * Description: When user is logged in, load their uid/name/email into the database
     *
     * Author: Alex Lo
     */
    private void updateDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Look up / add the specific user in/to the database and keep a reference while app is open
        final String email = user.getEmail();
        final String name = user.getDisplayName();
        final String uid = user.getUid();
        final String default_privacy = "Friends only";
        db.getReference("Users").child(uid).child("Email").setValue(email);
        db.getReference("Users").child(uid).child("Name").setValue(name);

        db.getReference("Leaderboard").child(uid).child("Name").setValue(name);

    }

    /* Name: displayMessage()
     * Description: Displays a pop up message to user
     *
     * Author: Connie Guan
     */
    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}
