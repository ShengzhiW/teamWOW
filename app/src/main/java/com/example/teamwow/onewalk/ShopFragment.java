package com.example.teamwow.onewalk;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        return view;
    }
}
