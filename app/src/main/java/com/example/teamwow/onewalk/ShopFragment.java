package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopFragment extends Fragment {

    private TextView currencyAmount;

    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();

    private DatabaseReference dbBodyIdx = db.getReference("Users").child(uid).child("BodyIdx");
    private DatabaseReference dbHatIdx = db.getReference("Users").child(uid).child("HatIdx");
    private DatabaseReference currencyDB = db.getReference("Users").child(uid).child("Currency");


    private DatabaseReference hatDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Hat");

    private DatabaseReference bodyDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Body");

    private int bodyIndex;
    private int hatIndex;
    private int currency = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shop, container, false);
        Button hatButton = (Button)view.findViewById(R.id.hatOpen);
        Button bodyButton = (Button)view.findViewById(R.id.bodyOpen);

        currencyAmount = view.findViewById(R.id.currencyShop);
        //Add listener to get the username
        currencyDB.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()) currency = dataSnapshot.getValue(Integer.class);
                currencyAmount.setText(String.valueOf(currency));
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}

        });


        final int [] bodiesDrawables = {
                R.drawable.cowboy_body,
                R.drawable.magician_body,
                R.drawable.jojo_body,
                R.drawable.earth_body,
                R.drawable.chef_body,
                R.drawable.viking_body,
                R.drawable.striped_body,
                R.drawable.yellow_body,
                R.drawable.lee,
                R.drawable.gary_body
        };

        final int [] hatDrawables = {
                R.drawable.baseball_scale,
                R.drawable.magician_scale,
                R.drawable.pirate_scale,
                R.drawable.tree_scale,
                R.drawable.poop_scale,
                R.drawable.cowboyhat_scale,
                R.drawable.chef_scale,
                R.drawable.konoha_scale,
                R.drawable.viking_scale,
                R.drawable.leprechaun_hat_scale,
                R.drawable.sun_hat_scale,
                R.drawable.jojo_hat_scale,
                R.drawable.duck_hat_scale
        };

        hatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), HatPage.class);
                startActivity(intent);
            }
        });

        bodyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), BodyPage.class);
                startActivity(intent);
            }
        });

        /* Set up inventory array */
        bodyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    if( item.getValue(Integer.class)  == 2){
                        bodyIndex = Integer.parseInt(item.getKey());
                        dbBodyIdx.setValue(item.getKey());

                        if(isAdded())
                        {
                            DisplayMetrics dimension = new DisplayMetrics();
                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dimension);
                            int height = dimension.heightPixels;
                            ImageView imgView = (ImageView) view.findViewById(R.id.imgView) ;
                            imgView.getLayoutParams().height = height / 3;
                            imgView.getLayoutParams().width = height / 3;


                            Drawable drawable = getResources().getDrawable(bodiesDrawables[bodyIndex]);
                            imgView.setImageDrawable(drawable);
                        }


                    }
                }
            }
            // onCancelled...
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // DO nothing
            }
        });

        hatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    if( item.getValue(Integer.class)  == 2){
                        hatIndex = Integer.parseInt(item.getKey());
                        dbHatIdx.setValue(item.getKey());

                        if(isAdded())
                        {
                            DisplayMetrics dimension = new DisplayMetrics();
                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dimension);
                            int height = dimension.heightPixels;
                            ImageView imgViewHat = (ImageView) view.findViewById(R.id.imgViewHat) ;

                            imgViewHat.getLayoutParams().height = height / 3;
                            imgViewHat.getLayoutParams().width = height / 3;

                            Drawable drawableHat = getResources().getDrawable(hatDrawables[hatIndex]);
                            imgViewHat.setImageDrawable(drawableHat);
                        }



                    }
                }
            }
            // onCancelled...
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // DO nothing
            }
        });

        return view;
    }
}
