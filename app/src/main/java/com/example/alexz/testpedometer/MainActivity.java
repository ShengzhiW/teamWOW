package com.example.alexz.testpedometer;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView mStepDetector;
    private int count = 0;

    // Firebase authentication
    private FirebaseAuth auth; // Firebase authentication for log in
    private static final int RC_SIGN_IN = 123; // Some number needed for authentication

    // Reference to the firebase database
    private FirebaseDatabase db;
    private DatabaseReference myDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* Firebase Database */
        db = FirebaseDatabase.getInstance(); // Get the firebase database
        myDBRef = db.getReference("Steps"); // Get a database reference for the "Steps" db

        /* Firebase Authentication */
        auth = FirebaseAuth.getInstance();

        /* Start log in. Email is available for log in */
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                .build(), RC_SIGN_IN);


        mStepDetector = (TextView)findViewById(R.id.test);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        count++;
        myDBRef.setValue(count); // Write the count value to the database

        mStepDetector.setText(String.valueOf(count));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

            // If sucessfully signed in
            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
}

