package com.waterloohacks2015.foodbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.waterloohacks2015.foodbox.listadapter.FirebaseListAdapter;
import com.waterloohacks2015.foodbox.listadapter.FoodBoxItemListAdapter;
import com.waterloohacks2015.foodbox.listadapter.FriendFoodBoxItemListAdapter;

import java.util.Map;

public class FriendListActivity extends AppCompatActivity {

    private FirebaseListAdapter listAdapter;
    private Firebase usersRef;
    private ValueEventListener originalListener;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE);
        String userEmail = prefs.getString(ListActivity.USER_EMAIL, "");
        userName = userEmail.split("@")[0];
        usersRef = new  Firebase(ListActivity.FIREBASE_URI);
        usersRef = usersRef.child("items");

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        // Attach an listener to read the data at our posts reference
//        usersRef.addValueEventListener(originalListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                System.out.println(snapshot.getValue());
//                Map<String, Object> val = (Map) snapshot.getValue();
//                for (String name : val.keySet()){
//                    if (!name.equals(userName))
//                    {
//                        val.get(key);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
//        usersRef.removeEventListener(originalListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ListView mainList = (ListView) findViewById(R.id.friend_list);
        listAdapter = new FriendFoodBoxItemListAdapter(
                usersRef.orderByChild("expirationDate"), this, R.layout.food_friend_view, userName);
        mainList.setAdapter(listAdapter);
    }

}
