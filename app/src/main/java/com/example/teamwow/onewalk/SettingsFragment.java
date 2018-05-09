/* Name: SettingsFragment
 * Description: The Settings page of the app
 * Source: https://tutorialwing.com/android-bottom-navigation-view-tutorial-with-example/
 * Author: Jungyong Yi
 */

package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Intent;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFragment extends Fragment {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.settings_main, container, false);

        Button logOutButton = rootView.findViewById(R.id.Logout); // get the log out button
        Button deleteButton = rootView.findViewById(R.id.deleteBtn); //get the delete account btn

        // Add a listener to the logout button that logs the user out
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
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

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .delete(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
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

        return rootView;

    }

    /* Name: LogIn
     * Returns user to log in screen
     *
     * Author: Connie
     */

    private void LogIn(){

        Intent logInIntent = new Intent(getActivity(), Login.class);

        startActivity(logInIntent);

    }

    /*
     * Name: displayMessage
     * Displays a message
     * Author: Connie
     */
    private void displayMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
