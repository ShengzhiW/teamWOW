package com.example.teamwow.onewalk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AnimatedWelcome extends AppCompatActivity {
    //add click function
    Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat_page);

        closeButton = (Button)findViewById(R.id.closeHatShop);

        /* Begin process */
        closeHatShop(closeButton);
    }

    private void closeHatShop(Button closeButton) {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}



