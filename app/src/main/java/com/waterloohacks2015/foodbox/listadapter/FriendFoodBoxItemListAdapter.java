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
public class FriendFoodBoxItemListAdapter extends FirebaseListAdapter<FoodBoxItem> {
    String userNameToExclude;
    public FriendFoodBoxItemListAdapter(Query mRef, Activity activity, int layout, String aInUserNameToExclude) {
        super(mRef, FoodBoxItem.class, layout, activity);
        userNameToExclude = aInUserNameToExclude;
    }

    @Override
    protected void populateView(View v, FoodBoxItem item) {
        String itemName = item.getFoodName();
        String expiryDate = ListActivity.expiryDateDisplay.format(new Date(item.getExpirationDate()));
        String friendName = item.getUserName();

        if (!friendName.equals(userNameToExclude)) {
            v.setVisibility(View.VISIBLE);
            TextView userNameView = (TextView) v.findViewById(R.id.friend_friendName);
            userNameView.setText(friendName);

            TextView itemNameView = (TextView) v.findViewById(R.id.friend_foodName);
            itemNameView.setText(itemName);

            TextView expiryDateView = (TextView) v.findViewById(R.id.friend_expirationDate);
            expiryDateView.setText("Expires: " + expiryDate);
        }
        else
        {
            v.setVisibility(View.GONE);
            //v.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
        }
    }
}
