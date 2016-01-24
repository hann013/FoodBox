package com.waterloohacks2015.foodbox.listadapter;

import android.app.Activity;
import android.view.View;
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
    public FoodBoxItemListAdapter(Query mRef, Activity activity, int layout) {
        super(mRef, FoodBoxItem.class, layout, activity);
    }

    @Override
    protected void populateView(View v, FoodBoxItem item, String key) {
        String itemName = item.getFoodName();
        Date expiryDate = new Date(item.getExpirationDate());

        String expiryDateFormatted = ListActivity.expiryDateDisplay.format(expiryDate);
        long daysAway = (expiryDate.getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000);

        TextView itemNameView = (TextView) v.findViewById(R.id.foodName);
        itemNameView.setText(itemName);

        TextView expiryDateView = (TextView) v.findViewById(R.id.expirationDate);
        if (daysAway == 0) {
            expiryDateView.setText(String.format("%s (today!)", expiryDateFormatted));
        } else {
            expiryDateView.setText(String.format("%s (%d days)", expiryDateFormatted, daysAway));
        }

        TextView itemKey = (TextView) v.findViewById(R.id.itemKey);
        itemKey.setText(key);
    }
}
