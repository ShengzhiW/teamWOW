package com.example.teamwow.onewalk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StepCounterService extends Service implements SensorEventListener {
    // variables for sensor manager and sensor, used to implement step detector
    private SensorManager mSensorManager;
    private Sensor mSensor;

    // database elements
    private int count = 0;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference userStepCount;
    private DatabaseReference lbStepCount;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // when service starts, get most current user information
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        count = 0;

        // declare step detector using sensor and sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // begin listening for steps, set the delay time to fastest speed
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Get a reference to the step count, and read the data and attach it to the screen
        userStepCount = db.getReference("Users").child(uid).child("Steps");
        lbStepCount = db.getReference("Leaderboard").child(uid).child("Steps");

        // set a listener every time a user's steps change
        ValueEventListener stepListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) count = dataSnapshot.getValue(Integer.class);

                // create an Intent with the action "update_count"
                Intent setCount = new Intent("update_count");

                // add an extra key-value pair relating the step count
                setCount.putExtra("count", count);

                // send a broadcast back to home page, received in BroadcastReceiver
                sendBroadcast(setCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userStepCount.addListenerForSingleValueEvent(stepListener);

        // if service killed, recreate service (may change to different value found on android service guide)
        return START_STICKY;
    }

    /*
     * Method must be overridden, but since service is a started service, onBind is never used.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * Method called every time step detector is triggered.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // increment count and set database's Step count to variable
        count++;
        userStepCount.setValue(count);
        lbStepCount.setValue(count);

        // create an Intent with the action "update_count"
        Intent updateCount = new Intent("update_count");

        // add an extra key-value pair relating current step count
        updateCount.putExtra("count", count);

        // send a message back to home page
        sendBroadcast(updateCount);
    }

    /*
     * Method must be overridden to implement SensorEventListener, but has no use currently.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /*
     * Method called when service is stopped; must unreigster step detector so steps no longer
     * counted.
     */
    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
    }
}
