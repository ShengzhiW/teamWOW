/*
 * Sources:
 * https://www.youtube.com/watch?v=VUPM387qyrw&t=1s
 * https://www.youtube.com/watch?v=K2V6Y7zQ8NU
 * https://www.youtube.com/watch?v=go9q4O44b4E
 */

package com.example.teamwow.onewalk;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HatPage extends AppCompatActivity {
    //add click function
    Button closeButton;
    GridLayout mainGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat_page);
        Bundle bundle = getIntent().getExtras();
        closeButton = (Button)findViewById(R.id.closeHatShop);
        mainGrid = (GridLayout)findViewById(R.id.mainGrid);
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
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: replace toast with start new Activity
                    Toast.makeText(HatPage.this, "Clicked at index" + finalI, Toast.LENGTH_SHORT).show();

                    ImageView image = new ImageView(HatPage.this);
                    ImageView getter = (ImageView)((LinearLayout)cardView.getChildAt(0)).getChildAt(0);
                    image.setImageDrawable(getter.getDrawable());

                    // Dialog Box to buy hat
                    AlertDialog.Builder builder = new AlertDialog.Builder(HatPage.this);
                    builder
                            .setTitle("Buy Hat?")
                            //.setMessage("Are you sure?")
                            .setView(image)
                            // Line below creates icon for dialog box in upper left corner
                            .setIcon(image.getDrawable())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Yes button clicked, do something
                                    Toast.makeText(HatPage.this, "Bought hat at index" + finalI, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });
        }
    }
}
