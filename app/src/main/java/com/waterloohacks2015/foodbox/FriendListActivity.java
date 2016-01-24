package com.waterloohacks2015.foodbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.waterloohacks2015.foodbox.listadapter.FirebaseListAdapter;
import com.waterloohacks2015.foodbox.listadapter.FoodBoxItemListAdapter;
import com.waterloohacks2015.foodbox.listadapter.FriendFoodBoxItemListAdapter;

import java.util.Map;

public class FriendListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseListAdapter listAdapter;
    private Firebase usersRef;
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set user email in nav bar
        TextView drawerEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_email);
        drawerEmail.setText(userEmail);

        // If started by intent
        if (getIntent().getExtras() != null) {
            String itemId = getIntent().getExtras().getString(ListActivity.ITEM_ID);
            Firebase itemRef = new Firebase(ListActivity.FIREBASE_URI).child("items").child(itemId);
            itemRef.child("isPublic").setValue(true);
            getIntent().removeExtra(ListActivity.ITEM_ID);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        final ListView mainList = (ListView) findViewById(R.id.main_list);
        listAdapter = new FriendFoodBoxItemListAdapter(
                usersRef.orderByChild("expirationDate"), this, R.layout.food_friend_view, userName);
        mainList.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        System.out.println("nav clicked");
        int id = item.getItemId();

        if (id == R.id.my_food) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        } else if (id == R.id.add_friend) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onMailButtonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "FoodBox Request!");
        intent.putExtra(Intent.EXTRA_TEXT, "Let's talk about food.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
