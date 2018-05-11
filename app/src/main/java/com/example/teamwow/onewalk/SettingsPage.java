package com.example.teamwow.onewalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsPage extends AppCompatActivity {

    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();
    final AuthUI auth = AuthUI.getInstance();
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        setupNavigationView();

        Button nicknameButton = findViewById(R.id.Nickname);
        Button logOutButton = findViewById(R.id.Logout); // get the log out button
        Button deleteButton = findViewById(R.id.deleteBtn); //get the delete account btn
        Button privacyButton = findViewById(R.id.priBtn); // get the privacy buttoon

        // Add a listener to the change nickname button that prompts the user for input and then updates the database
        nicknameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.change_nickname, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alert dialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.newNickname);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and update database
                                        String input = userInput.getText().toString();
                                        db.getReference("Users").child(uid).child("Name").setValue(input);
                                        db.getReference("Leaderboard").child(uid).child("Name").setValue(input);
                                        displayMessage(getString(R.string.changed_nickname)+" "+input);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog, show it
                alertDialogBuilder.create().show();
            }
        });

        // Add a listener to the logout button that logs the user out
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    displayMessage(getString(R.string.signout));
                                    LogIn();
                                } else {
                                    displayMessage(getString(R.string.signout_failed));
                                }
                            }
                        });

            }
        });

        // Add a listener to the delete button that deletes the user's account
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createAlertDialogue();
            }
        });

        // Adds a listener to the privacy button, provides options
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(SettingsPage.this,PrivacyPage.class));
            }
        });

    }

    /* Starts the login activity and brings the user back to the login page */
    private void LogIn(){
        Intent logInIntent = new Intent(this, Login.class);
        startActivity(logInIntent);
    }

    /*
     * Name: displayMessage
     * Displays a message
     * Author: Connie
     */
    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /*
     * Creates a confirmation dialogue for account deletion
     * If user confirms then account is deleted and they are returned to sign in page
     *
     * Author: Connie
     */
    private void createAlertDialogue()
    {
        // Alert Builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Are you sure you want to delete your account? This will erase all data" +
                "linked to your account.");
        alertBuilder.setCancelable(true);

        // Action for positive button
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                auth.delete(context).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    // remove the account information from the database
                                    db.getReference("Users").child(uid).removeValue();
                                    db.getReference("Leaderboard").child(uid).removeValue();

                                    displayMessage(getString(R.string.delete_account_success));
                                    LogIn();
                                }
                                else {
                                    displayMessage(getString(R.string.delete_account_failed));
                                }
                            }
                        });
            }
        });

        // Action for negative option
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        // Create the dialog and show it
        alertBuilder.create().show();
    }

    /* Creates the bottom navigation bar */
    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectPage(menu.getItem(4));

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
                intent = new Intent(this, LeaderboardPage.class);
                startActivity(intent);
                break;
            case R.id.navigation_settings:
                // Action to perform when settings Menu item is selected.

                break;
        }
    }
}
