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
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
    // variables for sensor manager and sensor, used to implement step detector
    private SensorManager mSensorManager;
    private Sensor mSensor;
    // variable for the default text currently on screen ("Hello World")
    private TextView mStepDetector;
    // step count
    private int count = 0;
    private FirebaseDatabase db;
    private DatabaseReference myDBRef;
    private DatabaseReference userStepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
        /* Firebase Database */
        db = FirebaseDatabase.getInstance(); // Get the firebase database
        myDBRef = db.getReference("Steps"); // Get a database reference for the "Steps" d
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Look up / add the specific user in/to the database and keep a reference while app is open
        String email = user.getEmail();
        String uid = user.getUid();
        db.getReference("Users").child(uid).child("Email").setValue(email);
        userStepCount = db.getReference("Users").child(uid).child("Steps");

        // gets the text with id "test", centered on screen
        mStepDetector = (TextView)findViewById(R.id.test);
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

}
