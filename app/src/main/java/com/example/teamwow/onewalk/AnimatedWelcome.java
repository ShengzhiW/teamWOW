//package com.example.teamwow.onewalk;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//public class AnimatedWelcome extends AppCompatActivity {
//    Button closeButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        setContentView(R.layout.activity_hat_page);
//        closeButton = (Button)findViewById(R.id.closeHatShop);
//
//
//        closeHatShop(closeButton);
//    }
//
//    private void closeHatShop(Button closeButton) {
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                closeHatShop(closeButton);
//                Intent intent = new Intent(this, ContainerPage.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//}
//
//

package com.example.teamwow.onewalk;
import android.content.Intent;
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
        Button closeButton = (Button)findViewById(R.id.closeHatShop);

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



