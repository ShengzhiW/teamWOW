package com.example.teamwow.onewalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BodyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_page);
        Bundle bundle = getIntent().getExtras();
    }
}
