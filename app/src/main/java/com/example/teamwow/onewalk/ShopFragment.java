package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();

    private DatabaseReference hatDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Hat");
    ArrayList<Integer> hatArray = new ArrayList<Integer>();

    private DatabaseReference bodyDB = db.getReference("Users").child(uid).child("Inventory")
            .child("Body");
    ArrayList<Integer> bodyArray = new ArrayList<>();

    private int bodyIndex = 9;
    private int hatIndex = 4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        Button hatButton = (Button)view.findViewById(R.id.hatOpen);
        Button bodyButton = (Button)view.findViewById(R.id.bodyOpen);

        int [] bodiesDrawables = {
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

        int [] hatDrawables = {
                R.drawable.baseball,
                R.drawable.magician,
                R.drawable.pirate,
                R.drawable.tree,
                R.drawable.poop,
                R.drawable.cowboyhat,
                R.drawable.chef,
                R.drawable.konoha,
                R.drawable.viking,
                R.drawable.leprechaun_hat,
                R.drawable.sun_hat,
                R.drawable.jojo_hat,
                R.drawable.duck_hat
        };

        hatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), HatPage.class);
             //   intent.putExtra("some", "hats");
                startActivity(intent);
            }
        });

        bodyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), BodyPage.class);
              //  intent.putExtra("some", "hats");
                startActivity(intent);
            }
        });

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

        ImageView imgView = (ImageView) view.findViewById(R.id.imgView) ;
        Drawable drawable = getResources().getDrawable(bodiesDrawables[bodyIndex]);
        imgView.setImageDrawable(drawable);

        ImageView imgViewHat = (ImageView) view.findViewById(R.id.imgViewHat) ;
        Drawable drawableHat = getResources().getDrawable(hatDrawables[hatIndex]);
        imgViewHat.setImageDrawable(drawableHat);

        return view;
    }
}
