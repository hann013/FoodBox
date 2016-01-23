package com.waterloohacks2015.foodbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import db.ListDBHelper;

public class MainActivity extends Activity {

    ListDBHelper db;
    SimpleCursorAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();

/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.d("Main Activity", "Menu Inflate");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_food:
                Intent i = new Intent(getApplicationContext(), NewFood.class);
                this.startActivity(i);
                return true;

            case R.id.send_notification:
                Intent j = new Intent(getApplicationContext(), NotificationActivity.class);
                this.startActivity(j);
                return true;

            default:
                return false;
        }
    }


    private void updateUI() {
        db = new ListDBHelper(this);
        Cursor cursor = db.displayData();

        String[] columns = new String[]{
                db.COL_2,
                db.COL_3,
                db.KEY_ID
        };

        int[] to = new int[] {
                R.id.foodName,
                R.id.expirationDate,
        };

        MyCursorAdapter dataAdapter = new MyCursorAdapter(this, R.layout.food_view, cursor,
            columns, to, 0 );

    ListView listView = (ListView) findViewById(R.id.list);
    // Assign adapter to ListView
    listView.setAdapter(dataAdapter);

    }

    private class MyCursorAdapter extends SimpleCursorAdapter {

        public MyCursorAdapter(Context context, int layout, Cursor c,
                               String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //get reference to the row
            View view = super.getView(position, convertView, parent);
            //check for odd or even to set alternate colors to the row background
            if (position % 2 == 0) {
                view.setBackgroundColor(Color.rgb(238, 233, 233));
            } else {
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            return view;
        }
    }

    public void onEatenButtonClick(View view) {
        View v = (View) view.getParent();
        TextView foodItemV = (TextView) v.findViewById(R.id.foodName);
        String food = foodItemV.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                db.TABLE_NAME,
                db.COL_2,
                food);


        db = new ListDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
}

