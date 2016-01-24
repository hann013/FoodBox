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
    protected void populateView(View v, FoodBoxItem item, String key) {
        String userName = item.getUserName();
        String itemName = item.getFoodName();
        Date expiryDate = new Date(item.getExpirationDate());

        v.setVisibility(View.VISIBLE);
        String expiryDateFormatted = ListActivity.expiryDateDisplay.format(expiryDate);
        long daysAway = (expiryDate.getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000);

        TextView friendNameView = (TextView) v.findViewById(R.id.friend_friendName);
        friendNameView.setText(userName);

        TextView itemNameView = (TextView) v.findViewById(R.id.friend_foodName);
        itemNameView.setText(itemName);

        TextView expiryDateView = (TextView) v.findViewById(R.id.friend_expirationDate);

        if (daysAway == 0) {
            expiryDateView.setText(String.format("%s (today!)", expiryDateFormatted));
        } else if (daysAway == 1) {
            expiryDateView.setText(String.format("%s (%d day)", expiryDateFormatted, daysAway));
        } else {
            expiryDateView.setText(String.format("%s (%d days)", expiryDateFormatted, daysAway));
        }

        TextView itemKey = (TextView) v.findViewById(R.id.friend_itemKey);
        itemKey.setText(key);

        v.setVisibility(View.VISIBLE);
        v.setLayoutParams(new AbsListView.LayoutParams(-1, -2));

        if (!item.getIsPublic())
        {
            v.setVisibility(View.GONE);
            v.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
        }
    }
}
