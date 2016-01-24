package com.waterloohacks2015.foodbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.text.format.Time;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.waterloohacks2015.foodbox.listadapter.FirebaseListAdapter;
import com.waterloohacks2015.foodbox.listadapter.FoodBoxItemListAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String FIREBASE_URI = "https://foodbox.firebaseio.com";
    public static final int IMAGE_CAPTURE_REQUEST_CODE = 100;

    public static SimpleDateFormat expiryDateDisplay = new SimpleDateFormat("yyyy-MM-dd");

    private Uri photoUri;
    private String userEmail;

    private Firebase ref;
    private Firebase userRef;
    private FirebaseListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoUri = getOutputFileUri();
                AddFoodDialogFragment dialog = AddFoodDialogFragment.newInstance(photoUri);
                dialog.show(getFragmentManager(), "AddFoodDialog");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // initialize Firebase
        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(FIREBASE_URI);

        // Set user email in nav bar
        SharedPreferences prefs = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE);
        userEmail = prefs.getString(USER_EMAIL, "");
        TextView drawerEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_email);
        drawerEmail.setText(userEmail);

        // username no email
        String userName = userEmail.split("@")[0];
        System.out.println(userName);

        // get firebase user ref
        userRef = ref.child("users").child(userName);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ListView mainList = (ListView) findViewById(R.id.main_list);
        listAdapter = new FoodBoxItemListAdapter(userRef.orderByChild("expirationDate"), this, R.layout.food_view);
        mainList.setAdapter(listAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        int id = item.getItemId();

        if (id == R.id.my_food) {

        } else if (id == R.id.friends_food) {

        } else if (id == R.id.add_friend) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                //Toast.makeText(this, "Image saved to:\n" + photoUri, Toast.LENGTH_LONG).show();

                Intent launchRecognitionIntent = new Intent(this, RecognitionActivity.class);
                launchRecognitionIntent.putExtra(RecognitionActivity.INPUT_URI, photoUri);
                startActivity(launchRecognitionIntent);
            } else if (resultCode == RESULT_CANCELED) {
                // Do nothing
            } else {
                // Image capture failed
                Toast.makeText(this, "Image capture failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Uri getOutputFileUri() {
        File photoDir = new File(
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                getResources().getString(R.string.app_name));

        // Create the storage directory if it does not exist
        if (!photoDir.exists()){
            if (!photoDir.mkdirs()){
                Toast.makeText(this, "Failed to get photo directory", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        // Create a photo file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        File photoFile = new File(photoDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        return Uri.fromFile(photoFile);
    }
}
