package com.example.teamwow.onewalk;
<<<<<<< HEAD
import android.content.Intent;
=======

>>>>>>> 918a8a0ef6e02637d10755ad18d0858cb5094b22
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AnimatedWelcome extends AppCompatActivity {
    //add click function

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat_page);
<<<<<<< HEAD
        Button closeButton = (Button)findViewById(R.id.closeHatShop);
=======

        closeButton = (Button)findViewById(R.id.closeHatShop);
>>>>>>> 918a8a0ef6e02637d10755ad18d0858cb5094b22

        /* Begin process */
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnimatedWelcome.this, ContainerPage.class);
                startActivity(i);
            }
        });
    }
}



