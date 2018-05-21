/*
 * Sources:
 * https://www.youtube.com/watch?v=VUPM387qyrw&t=1s
 * https://www.youtube.com/watch?v=K2V6Y7zQ8NU
 * https://www.youtube.com/watch?v=go9q4O44b4E
 */

package com.example.teamwow.onewalk;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BodyPage extends AppCompatActivity {
    //add click function
    Button closeButton;
    GridLayout mainGrid;

    /* Initialize firebase db */
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();
    private DatabaseReference bodyDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Body");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_page);
        Bundle bundle = getIntent().getExtras();
        closeButton = findViewById(R.id.closeBodyShop);
        mainGrid = (GridLayout)findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
        closeBodyShop(closeButton);
    }

    private void closeBodyShop(Button closeButton) {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setSingleEvent(GridLayout singleEvent) {
        for(int i = 0; i < mainGrid.getChildCount();i++){
            final CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: replace toast with start new Activity
                    Toast.makeText(BodyPage.this, "Clicked at index" + finalI, Toast.LENGTH_SHORT).show();

                    ImageView image = new ImageView(BodyPage.this);
                    ImageView getter = (ImageView)((LinearLayout)cardView.getChildAt(0)).getChildAt(0);
                    image.setImageDrawable(getter.getDrawable());
                    TextView getterName = (TextView)((LinearLayout)cardView.getChildAt(0)).getChildAt(1);

                    // Dialog Box to buy body
                    AlertDialog.Builder builder = new AlertDialog.Builder(BodyPage.this);
                    builder
                            .setTitle("Buy " + getterName.getText().toString() + "?")
                            //.setMessage("Are you sure?")
                            .setView(image)
                            // Line below creates icon for dialog box in upper left corner
                            .setIcon(image.getDrawable())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Yes button clicked, do something
                                    Toast.makeText(BodyPage.this, "Bought body at index" + finalI, Toast.LENGTH_SHORT).show();
                                    bodyDB.child(new Integer(finalI).toString()).setValue(1);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

        }
    }
}