package com.waterloohacks2015.foodbox;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import db.ListDBHelper;

/**
 * Created by jennadeng on 2016-01-23.
 */
public class NewFood extends Activity implements View.OnClickListener{

    private ListDBHelper helper;
    Button mButton;
    String foodName;
    String expirationDate;
    EditText mEdit;
    EditText storeDate;
    private EditText date;

    private DatePickerDialog fromDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_food_entry);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        findViewsById();

        setDateTimeField();
        mButton = (Button)findViewById(R.id.button1);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEdit = (EditText) findViewById(R.id.editText1);

                foodName = mEdit.getText().toString();
                expirationDate = date.getText().toString();
                Log.d("ADebugTag", "foodName: " + foodName);
                Log.d("ADebugTag", "expirationDate: " + expirationDate);

                helper = new ListDBHelper(NewFood.this);
                boolean isInserted = helper.insertData(foodName, expirationDate);
                if (isInserted = true) {
                    Toast.makeText(NewFood.this, "Food Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewFood.this, "Try Again", Toast.LENGTH_SHORT).show();
                }
                scheduleNotification(getNotification("5 s delay"), 300000);
                finish();
            }
        });
    }

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.putExtra(NotificationActivity.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationActivity.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }


    private void findViewsById() {
        date = (EditText) findViewById(R.id.date);
        date.setInputType(InputType.TYPE_NULL);
        date.requestFocus();
    }

    private void setDateTimeField() {
        date.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        fromDatePickerDialog.show();

    }

}
