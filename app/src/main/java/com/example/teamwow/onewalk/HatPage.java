/*
 * Sources:
 * https://www.youtube.com/watch?v=VUPM387qyrw&t=1s
 * https://www.youtube.com/watch?v=K2V6Y7zQ8NU
 * https://www.youtube.com/watch?v=go9q4O44b4E
 */

package com.example.teamwow.onewalk;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog.Builder;
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

public class HatPage extends AppCompatActivity {
    //add click function
    Button closeButton;
    GridLayout mainGrid;

    /* Initialize firebase db */
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();
    private DatabaseReference hatDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Hat");
    private DatabaseReference currencyDB = db.getReference("Users").child(uid).child("Currency");

    // hat shop variables
    private ArrayList<Integer> hatArray = new ArrayList<Integer>();
    private int garyBucks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat_page);
        Bundle bundle = getIntent().getExtras();
        closeButton = (Button)findViewById(R.id.closeHatShop);
        mainGrid = (GridLayout)findViewById(R.id.mainGrid);

        /* Set up inventory array */
        hatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // the snapshot provides a generic list holding Objects
                List array = (List) snapshot.getValue();

                if(array != null)
                {
                    // we then transform those objects into integers for our hat array
                    for(int i = 0; i < array.size(); i++){
                        hatArray.add(Integer.valueOf(array.get(i).toString()));
                    }
                }


            }

            // onCancelled...

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // DO nothing
            }
        });
        ValueEventListener currencyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) garyBucks = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        currencyDB.addListenerForSingleValueEvent(currencyListener);

        /* Begin process */
        setSingleEvent(mainGrid);
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

    public void setSingleEvent(GridLayout singleEvent) {
        for(int i = 0; i < mainGrid.getChildCount();i++){
            final CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            final int price = 100 * i;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ImageView image = new ImageView(HatPage.this);
                    ImageView getter = (ImageView)((LinearLayout)cardView.getChildAt(0)).getChildAt(0);
                    image.setImageDrawable(getter.getDrawable());
                    TextView getterName = (TextView)((LinearLayout)cardView.getChildAt(0)).getChildAt(1);

                    if(hatArray.get(finalI) == 0){
                        // Dialog Box to buy hat
                        Builder builder = new Builder(HatPage.this);
                        builder
                                .setTitle("Buy " + getterName.getText().toString() + "?")
                                .setMessage("Price:" + price + "\n" + "Currency: " + garyBucks)
                                .setView(image)
                                // Line below creates icon for dialog box in upper left corner
                                .setIcon(image.getDrawable())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Yes button clicked, do something
                                        if (garyBucks >= price) {
                                            Toast.makeText(HatPage.this, "Looking good!", Toast.LENGTH_SHORT).show();
                                            // Unequips currently equipped item
                                            for (int i = 0; i < hatArray.size(); i++) {
                                                if (hatArray.get(i) == 2) {
                                                    hatDB.child(Integer.valueOf(i).toString()).setValue(1);
                                                    hatArray.set(i, 1);
                                                    break;
                                                }
                                            }
                                            currencyDB.setValue(garyBucks - price);
                                            garyBucks -= price;
                                            hatDB.child(Integer.valueOf(finalI).toString()).setValue(2);
                                            hatArray.set(finalI, 2);
                                        }else{
                                            Toast.makeText(HatPage.this, "Go walk more you potato", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else if(hatArray.get(finalI) == 1){
                        Builder builder = new Builder(HatPage.this);
                        builder
                                .setTitle("Equip " + getterName.getText().toString() + "?")
                                .setView(image)
                                // Line below creates icon for dialog box in upper left corner
                                .setIcon(image.getDrawable())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Yes button clicked, do something
                                        Toast.makeText(HatPage.this, "Looking good!", Toast.LENGTH_SHORT).show();
                                        // Unequips currently equipped item
                                        for(int i = 0; i < hatArray.size(); i++){
                                            if(hatArray.get(i) == 2){
                                                hatDB.child(Integer.valueOf(i).toString()).setValue(1);
                                                hatArray.set(i,1);
                                                break;
                                            }
                                        }
                                        hatDB.child(Integer.valueOf(finalI).toString()).setValue(2);
                                        hatArray.set(finalI,2);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else{
                        Toast.makeText(HatPage.this, "Already equipped", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
