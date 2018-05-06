package com.example.teamwow.onewalk;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
    // variables for sensor manager and sensor, used to implement step detector
    private SensorManager mSensorManager;
    private Sensor mSensor;
    // variable for the default text currently on screen ("Hello World")
    private TextView mStepDetector;
    // step count
    private int count = 0;
    private FirebaseDatabase db;
    private DatabaseReference userStepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();

        // Pull current step count from database
        initialStepCount();

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
     * is taken. Updates database.
     *
     * @param event - holds information about the fired event, irrelevant
     * for this function for now
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // increment count and store value in database
        count++;
        // Write the count value to the database under the user
        if(userStepCount != null) userStepCount.setValue(count);
        // set text to current step count
        mStepDetector.setText(String.valueOf(count));
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
     * @author Alex Lo
     *
     * Looks up the user in the database and keeps a reference
     * Grabs the existing step count if there is one
     */
    public void initialStepCount() {
        // Get the database instance
        db = FirebaseDatabase.getInstance();

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Look up specific user in database and keep a reference while app is open
        String email = user.getEmail();
        String uid = user.getUid();
        db.getReference("Users").child(uid).child("Email").setValue(email);

        // Get the text with id "test"
        mStepDetector = findViewById(R.id.stepCount);

        // Get a reference to the step count, and read the data and attach it to the screen
        userStepCount = db.getReference("Users").child(uid).child("Steps");
        ValueEventListener stepListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = dataSnapshot.getValue(Integer.class);
                mStepDetector.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        db.getReference("Users").child(uid).child("Steps").addListenerForSingleValueEvent(stepListener);
    }

}
