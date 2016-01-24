package com.waterloohacks2015.foodbox.listadapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Query;
import com.waterloohacks2015.foodbox.FoodBoxItem;
import com.waterloohacks2015.foodbox.ListActivity;
import com.waterloohacks2015.foodbox.R;
import com.waterloohacks2015.foodbox.recipelist.RecipeList;

import java.util.Date;

/**
 * Created by hanna on 2016-01-23.
 */
public class FoodBoxItemListAdapter extends FirebaseListAdapter<FoodBoxItem> {
    String myUserName;
    Activity currentActivity;

    public FoodBoxItemListAdapter(Query mRef, Activity activity, int layout, String username) {
        super(mRef, FoodBoxItem.class, layout, activity);
        myUserName = username;
        currentActivity = activity;
    }

    @Override
    protected void populateView(View v, final FoodBoxItem item, String key) {
        String userName = item.getUserName();
        final String itemName = item.getFoodName();
        Date expiryDate = new Date(item.getExpirationDate());

        v.setVisibility(View.VISIBLE);
        String expiryDateFormatted = ListActivity.expiryDateDisplay.format(expiryDate);
        long daysAway = (expiryDate.getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000);

        TextView itemNameView = (TextView) v.findViewById(R.id.foodName);
        itemNameView.setText(itemName);

        TextView expiryDateView = (TextView) v.findViewById(R.id.expirationDate);
        if (daysAway < 0) {
            expiryDateView.setText(String.format("%s (EXPIRED)", expiryDateFormatted));
        } else if (daysAway == 0) {
            expiryDateView.setText(String.format("%s (today!)", expiryDateFormatted));
        } else if (daysAway == 1) {
            expiryDateView.setText(String.format("%s (%d day)", expiryDateFormatted, daysAway));
        } else {
            expiryDateView.setText(String.format("%s (%d days)", expiryDateFormatted, daysAway));
        }

        TextView itemKey = (TextView) v.findViewById(R.id.itemKey);
        itemKey.setText(key);

        // set on-click listener
        RelativeLayout rootLayout = (RelativeLayout) v.findViewById(R.id.root_layout);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getRecipes = new Intent(currentActivity, RecipeList.class);
                getRecipes.putExtra(ListActivity.INGREDIENT_NAME, itemName);
                currentActivity.startActivity(getRecipes);
            }
        });

        v.setVisibility(View.VISIBLE);
        v.setLayoutParams(new AbsListView.LayoutParams(-1, -2));

        if (!userName.equals(myUserName))
        {
            v.setVisibility(View.GONE);
            v.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
        }
    }
}
