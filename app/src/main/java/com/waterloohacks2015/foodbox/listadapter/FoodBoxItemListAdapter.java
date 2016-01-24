package com.waterloohacks2015.foodbox.listadapter;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.waterloohacks2015.foodbox.FoodBoxItem;
import com.waterloohacks2015.foodbox.ListActivity;
import com.waterloohacks2015.foodbox.R;

import java.util.Date;

/**
 * Created by hanna on 2016-01-23.
 */
public class FoodBoxItemListAdapter extends FirebaseListAdapter<FoodBoxItem> {
    String myUserName;
    public FoodBoxItemListAdapter(Query mRef, Activity activity, int layout, String username) {
        super(mRef, FoodBoxItem.class, layout, activity);
        myUserName = username;
    }

    @Override
    protected void populateView(View v, FoodBoxItem item) {
        String userName = item.getUserName();
        if (userName.equals(myUserName))
        {
            v.setVisibility(View.VISIBLE);
            //v.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
            String itemName = item.getFoodName();
            String expiryDate = ListActivity.expiryDateDisplay.format(new Date(item.getExpirationDate()));

            TextView itemNameView = (TextView) v.findViewById(R.id.foodName);
            itemNameView.setText(itemName);

            TextView expiryDateView = (TextView) v.findViewById(R.id.expirationDate);
            expiryDateView.setText("Expires: " + expiryDate);
        }
        else
        {
            v.setVisibility(View.GONE);
            //v.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
        }

    }
}
