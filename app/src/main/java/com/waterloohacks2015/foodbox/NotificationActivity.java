package com.waterloohacks2015.foodbox;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by jennadeng on 2016-01-23.
 */
public class NotificationActivity extends Activity {
    Button btnShow, btnClear;
    NotificationManager manager;
    Notification myNotication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        initialise();

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        btnShow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //API level 11
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 1, intent, 0);

                Notification.Builder builder = new Notification.Builder(NotificationActivity.this);

                builder.setAutoCancel(false);
                builder.setTicker("this is ticker text");
                builder.setContentTitle("WhatsApp Notification");
                builder.setContentText("You have a new message");
                //builder.setSmallIcon(R.drawable.ic_launcher);
                builder.setContentIntent(pendingIntent);
                builder.setOngoing(true);
                builder.setSubText("This is subtext...");   //API level 16
                builder.setNumber(100);
                builder.build();

                myNotication = builder.getNotification();
                manager.notify(11, myNotication);

            /*
            //API level 8
            Notification myNotification8 = new Notification(R.drawable.ic_launcher, "this is ticker text 8", System.currentTimeMillis());

            Intent intent2 = new Intent(MainActivity.this, SecActivity.class);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(getApplicationContext(), 2, intent2, 0);
            myNotification8.setLatestEventInfo(getApplicationContext(), "API level 8", "this is api 8 msg", pendingIntent2);
            manager.notify(11, myNotification8);
            */

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                manager.cancel(11);
            }
        });
    }

    private void initialise() {
        btnShow = (Button) findViewById(R.id.btnShowNotification);
        btnClear = (Button) findViewById(R.id.btnClearNotification);
    }
}
