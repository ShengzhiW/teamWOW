package com.example.teamwow.onewalk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;

public class Login extends AppCompatActivity{
    // Firebase authentication
    private FirebaseAuth auth; // Firebase authentication for log in
    private static final int RC_SIGN_IN = 123; // Some number needed for authentication

    // Reference to the firebase database
    private FirebaseDatabase db;
    private DatabaseReference userdb;
    private DatabaseReference lbdb;

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
                Intent intent = new Intent(this, ContainerPage.class);
                startActivity(intent);
            }

            //means you cancelled sign in, just return to home page
            if(resultCode == RESULT_CANCELED){

                //exits the current activity
                finish();
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

        /* Initialize empty list for inventory */
        List<Integer> t = new ArrayList<Integer>(Collections.nCopies(20, 0));
        t.set(0, 2);

        // Look up / add the specific user in/to the database and keep a reference while app is open
        final String email = user.getEmail();
        final String name = user.getDisplayName();
        final String uid = user.getUid();

        // Get a reference to the user's database
        userdb = db.getReference("Users").child(uid);
        lbdb = db.getReference("Leaderboard").child(uid);
        initializeReference(userdb.child("Name"), name);
        initializeReference(userdb.child("BodyIdx"), "0");
        initializeReference(userdb.child("HatIdx"), "0");
        initializeReference(userdb.child("Email"), email);
        initializeReference(userdb.child("Steps"), 0);
        initializeReference(userdb.child("Privacy").child("Appear on Leaderboard"), true);
        initializeReference(lbdb.child("Private"), false);
        initializeReference(lbdb.child("Name"), name);

        // Update shop database if not already
        initializeInventory(userdb.child("Inventory").child("Hat"),t);
        initializeInventory(userdb.child("Inventory").child("Body"),t);

    }

    public void initializeReference(final DatabaseReference dr, final String value) {
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) dr.setValue(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void initializeReference(final DatabaseReference dr, final int value) {
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) dr.setValue(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void initializeReference(final DatabaseReference dr, final Boolean value) {
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) dr.setValue(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /* Had to be a different function due to it not being a String */
    public void initializeInventory(final  DatabaseReference dr, final List<Integer> list){
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) dr.setValue(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /* Name: displayMessage()
     * Description: Displays a pop up message to user
     *
     * Author: Connie Guan
     */
    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /** have the back button redirect to the phone's home screen */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
