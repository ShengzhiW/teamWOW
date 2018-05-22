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

public class ShopFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        Button hatButton = (Button)view.findViewById(R.id.hatOpen);
        Button bodyButton = (Button)view.findViewById(R.id.bodyOpen);

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

        ImageView imgView = (ImageView) view.findViewById(R.id.imgView) ;
        Drawable drawable = getResources().getDrawable(bodiesDrawables[9]);
        imgView.setImageDrawable(drawable);

        ImageView imgViewHat = (ImageView) view.findViewById(R.id.imgViewHat) ;
        Drawable drawableHat = getResources().getDrawable(hatDrawables[4]);
        imgViewHat.setImageDrawable(drawableHat);

        return view;
    }
}
