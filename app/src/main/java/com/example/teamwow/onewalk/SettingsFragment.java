package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFragment extends Fragment {

    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();
    final AuthUI auth = AuthUI.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button nicknameButton = rootView.findViewById(R.id.nickname_button);
        Button logOutButton = rootView.findViewById(R.id.logout_button);
        Button deleteButton = rootView.findViewById(R.id.delete_button);
        Button privacyButton = rootView.findViewById(R.id.privacy_button);
        Button profileButton = rootView.findViewById(R.id.profile_button);
        Button aboutButton = rootView.findViewById(R.id.about_walk);

        // Add a listener to the about button that displays the about page
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertAbout();
            }});

        // Add a listener to the change nickname button that prompts the user for input and then updates the database
        nicknameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNickname();
            }
        });

        // Add a listener to the logout button that logs the user out
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        // Add a listener to the delete button that deletes the user's account
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });

        // Adds a listener to the privacy button, provides options
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((ContainerPage)getActivity()).pushFragment(new PrivacyFragment());
            }
        });

        // Adds a listener to the profile button that brings the user to their profile page
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((ContainerPage) getActivity()).pushFragment(new ProfileFragment());
            }
        });
        return rootView;
    }

    /* Starts the login activity and brings the user back to the login page */
    private void logIn(){

        Intent logInIntent = new Intent(getActivity(), Login.class);

        //clears the activity stack
        logInIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logInIntent);
    }

    /* Displays the about page */
    private void alertAbout() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.about_while_walk, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alert dialog builder
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog, show it
        alertDialogBuilder.create().show();
    }

    /* Changes the user's nickname that is displayed in the leaderboard*/
    private void changeNickname() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.change_nickname, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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
                                if(!(input.equals(""))) {
                                    db.getReference("Users").child(uid).child("Name").setValue(input);
                                    db.getReference("Leaderboard").child(uid).child("Name").setValue(input);
                                    displayMessage(getString(R.string.changed_nickname) + " " + input);
                                }
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

    /* Logs the user out of their account */
    private void logoutUser() {
        auth.signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    displayMessage(getString(R.string.signout));

                    // if logged out, service is stopped so steps aren't detected
                    getActivity().stopService(new Intent(getActivity(), StepCounterService.class));

                    logIn();
                } else {
                    displayMessage(getString(R.string.signout_failed));
                }
            }
        });
    }

    /*
     * Creates a confirmation dialogue for account deletion
     * If user confirms then account is deleted and they are returned to sign in page
     */
    private void deleteUser()
    {
        // Alert Builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        alertBuilder.setMessage(R.string.confirm_delete_account);
        alertBuilder.setCancelable(true);

        // Action for positive button
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                auth.delete(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // remove the account information from the database
                            db.getReference("Users").child(uid).removeValue();
                            db.getReference("Leaderboard").child(uid).removeValue();

                            // delete account will stop step counter service
                            getActivity().stopService(new Intent(getActivity(), StepCounterService.class));

                            displayMessage(getString(R.string.delete_account_success));
                            logIn();
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

    public void displayMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
