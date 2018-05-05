/* Name: ShopFragment
 * Description: The Shop page of the app
 * Source: https://tutorialwing.com/android-bottom-navigation-view-tutorial-with-example/
 * Author: Jungyong Yi
 */

package com.example.alexz.testpedometer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* Shop fragment */
public class ShopFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_main, container, false);
    }
}
