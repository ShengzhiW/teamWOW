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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    ArrayList<Integer> bodyArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_page);
        Bundle bundle = getIntent().getExtras();
        closeButton = findViewById(R.id.closeBodyShop);
        mainGrid = (GridLayout)findViewById(R.id.mainGrid);

        /* Set up inventory array */
        bodyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Toast.makeText(HatPage.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                // the snapshot provides a generic list holding Objects
                List array = (List) snapshot.getValue();

                // we then transform those objects into integers for our hat array
                for(int i = 0; i < array.size(); i++){
                    //Toast.makeText(HatPage.this, "Value is" + Integer.valueOf(array.get(i).toString()), Toast.LENGTH_SHORT).show();
                    bodyArray.add(Integer.valueOf(array.get(i).toString()));
                }

            }

            // onCancelled...

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // DO nothing
            }
        });
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

                    if(bodyArray.get(finalI) == 0){
                        // Dialog Box to buy hat
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
                                        Toast.makeText(BodyPage.this, "Looking good!", Toast.LENGTH_SHORT).show();
                                        bodyDB.child(Integer.valueOf(finalI).toString()).setValue(1);
                                        bodyArray.set(finalI,1);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else{
                        Toast.makeText(BodyPage.this, "Already purchased this item", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }
}