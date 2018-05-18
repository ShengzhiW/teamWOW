/*
 * Sources:
 * https://www.youtube.com/watch?v=VUPM387qyrw&t=1s
 * https://www.youtube.com/watch?v=K2V6Y7zQ8NU
 * https://www.youtube.com/watch?v=go9q4O44b4E
 */

package com.example.teamwow.onewalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class BodyPage extends AppCompatActivity {
    //add click function
    GridLayout mainGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_page);
        Bundle bundle = getIntent().getExtras();
        mainGrid = (GridLayout)findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
    }

    public void setSingleEvent(GridLayout singleEvent) {
        for(int i = 0; i < mainGrid.getChildCount();i++){
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: replace toast with start new Activity
                    Toast.makeText(BodyPage.this, "Clicked at index" + finalI, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}