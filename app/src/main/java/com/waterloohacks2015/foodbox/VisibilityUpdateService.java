package com.waterloohacks2015.foodbox;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.firebase.client.Firebase;

/**
 * Created by hanna on 2016-01-24.
 */
public class VisibilityUpdateService extends IntentService {
    public VisibilityUpdateService() {
        super("VisibilityUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getExtras() != null) {
            String itemId = intent.getExtras().getString(ListActivity.ITEM_ID);
            String itemName = intent.getExtras().getString(ListActivity.INGREDIENT_NAME);

            Firebase itemRef = new Firebase(ListActivity.FIREBASE_URI).child("items").child(itemId);
            itemRef.child("isPublic").setValue(true);
            Toast.makeText(getApplicationContext(), itemName + "is now visible.", Toast.LENGTH_SHORT).show();
        }
    }
}
