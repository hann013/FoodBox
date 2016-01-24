package com.waterloohacks2015.foodbox;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;
import com.firebase.client.Firebase;
import com.waterloohacks2015.foodbox.expirydatepicker.ExpiryDaysFragment;
import com.waterloohacks2015.foodbox.recipelist.RecipeList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * A simple Activity that performs recognition using the Clarifai API.
 */
public class RecognitionActivity extends FragmentActivity {
    private static final String TAG = RecognitionActivity.class.getSimpleName();

    // IMPORTANT NOTE: you should replace these keys with your own App ID and secret.
    // These can be obtained at https://developer.clarifai.com/applications
    private static final String APP_ID = "Dzyc6O-UZ6pvZTPGylb-Nao2yvTyO3TG_ZY7UNyF";
    private static final String APP_SECRET = "m1RQPouLYUF8XuQr5oWy6F8ARIQ1AzCsKMK8461X";

    public static final String INPUT_URI = "INPUT_URI";

    private final ClarifaiClient client = new ClarifaiClient(APP_ID, APP_SECRET);
    private ImageView foodImage;
    private Spinner foodNameSpinner;
    private EditText customFoodName;
    private TextView expiryDate;
    private Button saveButton;

    private String userName;

    private ProgressDialog _progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);
        foodImage = (ImageView) findViewById(R.id.food_image);
        foodNameSpinner = (Spinner) findViewById(R.id.food_name);
        customFoodName = (EditText) findViewById(R.id.food_name_custom);
        expiryDate = (TextView) findViewById(R.id.expiry_date);
        saveButton = (Button) findViewById(R.id.save_button);

        // get user name
        SharedPreferences prefs = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE);
        userName = prefs.getString(ListActivity.USER_EMAIL, "").split("@")[0];

        //launch api
        if (getIntent().getExtras() != null) {
            Uri photoUri = getIntent().getExtras().getParcelable(RecognitionActivity.INPUT_URI);
            // load user image
            Log.d(TAG, "User picked image: " + photoUri);
            Bitmap bitmap = loadBitmapFromUri(photoUri);
            if (bitmap != null) {
                foodImage.setImageBitmap(bitmap);
                _progressDialog = new ProgressDialog(this);
                _progressDialog.setMessage("Interpreting...");
                _progressDialog.setCancelable(false);
                _progressDialog.setInverseBackgroundForced(false);
                _progressDialog.show();

                // Run recognition on a background thread since it makes a network call.
                new AsyncTask<Bitmap, Void, RecognitionResult>() {
                    @Override
                    protected RecognitionResult doInBackground(Bitmap... bitmaps) {
                        return recognizeBitmap(bitmaps[0]);
                    }

                    @Override
                    protected void onPostExecute(RecognitionResult result) {
                        updateUIForResult(result);
                        _progressDialog.hide();
                    }
                }.execute(bitmap);
            }
        } else {
            foodImage.setVisibility(View.GONE);
            foodNameSpinner.setVisibility(View.GONE);
            customFoodName.setVisibility(View.VISIBLE);
        }

        // set datepicker dialog listener for expiry date
        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExpiryDaysFragment().show(getSupportFragmentManager(), "ExpiryDatePicker");
            }
        });

        // add onClickListener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItemName = (String) foodNameSpinner.getSelectedItem();
                if (newItemName == null || newItemName.equals("Custom")) {
                    newItemName = ((EditText) findViewById(R.id.food_name_custom)).getText().toString();
                }
                HealthScale.setColor(newItemName);

                try {
                    long newItemExpiryDate = ListActivity.expiryDateDisplay.parse(expiryDate.getText().toString()).getTime();
                    FoodBoxItem newItem = new FoodBoxItem(newItemName, newItemExpiryDate, false, userName);
                    Firebase newItemRef = new Firebase(ListActivity.FIREBASE_URI).child("items").push();
                    newItemRef.setValue(newItem);

                    long millisDelay = newItemExpiryDate - System.currentTimeMillis() - 2*(24 * 60 * 60 * 1000);
                    //testing 30 seconds
                    millisDelay = 10000;
                    scheduleNotification(getNewNotification(newItemRef.getKey(), newItemName), millisDelay);

                } catch (ParseException e) {
                    Toast.makeText(RecognitionActivity.this, "Error saving item.", Toast.LENGTH_SHORT).show();
                } finally {
                    // return to ListActivity
                    startActivity(new Intent(RecognitionActivity.this, ListActivity.class));
                }
            }
        });
    }

    /**
     * Loads a Bitmap from a content URI returned by the media picker.
     */
    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            // The image may be large. Load an image that is sized for display. This follows best
            // practices from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
            int sampleSize = 1;
            System.out.println(foodImage.getWidth());
            System.out.println(foodImage.getHeight());

            //downsample image
            while (opts.outWidth / (2 * sampleSize) >= 1000 &&
                    opts.outHeight / (2 * sampleSize) >= 1000) {
                sampleSize *= 2;
                System.out.println(opts.outWidth / (2 * sampleSize));
                System.out.println(opts.outHeight / (2 * sampleSize));
            }


            opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + uri, e);
        }
        return null;
    }

    /**
     * Sends the given bitmap to Clarifai for recognition and returns the result.
     */
    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
        try {
            // Scale down the image. This step is optional. However, sending large images over the
            // network is slow and  does not significantly improve recognition performance.
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                    320 * bitmap.getHeight() / bitmap.getWidth(), true);

            // Compress the image as a JPEG.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
            byte[] jpeg = out.toByteArray();

            // Send the JPEG to Clarifai and return the result.
            return client.recognize(new RecognitionRequest(jpeg)).get(0);
        } catch (ClarifaiException e) {
            Log.e(TAG, "Clarifai error", e);
            return null;
        }
    }

    /**
     * Updates the UI by displaying tags for the given result.
     */
    private void updateUIForResult(RecognitionResult result) {
        System.out.println(result.getJsonResponse().toString());
        if (result.getStatusCode() == RecognitionResult.StatusCode.OK) {
            // Display the list of tags in the UI.
            final ArrayList<String> foodItems = new ArrayList<String>();
            int count = 0;
            for (Tag tag : result.getTags()) {
                foodItems.add(tag.getName());
                count++;
                if (count > 4) break;
            }
            initializeDetailsFields(foodItems);
        } else {
            Log.e(TAG, "Clarifai: " + result.getStatusMessage());
            Toast.makeText(this, "Error recognizing image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeDetailsFields(ArrayList<String> foodItems) {
        // add "Custom" choice to spinner and initialize spinner
        foodItems.add("Custom");
        final int listSize = foodItems.size();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, foodItems);
        foodNameSpinner.setAdapter(adapter);
        foodNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // selected "Custom"
                if (position == listSize - 1) {
                    customFoodName.setVisibility(View.VISIBLE);
                } else {
                    customFoodName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    private void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNewNotification(String itemKey, String itemName) {
        Notification.Builder builder = new Notification.Builder(this)
        .setContentTitle("Food Expiring Soon")
        .setContentText(itemName + " expires in two days.")
        .setSmallIcon(R.drawable.ic_menu_friends_food)
        .setAutoCancel(true);

        // make visible action
        Intent visibilityIntent = new Intent(this, FriendListActivity.class);
        visibilityIntent.putExtra(ListActivity.ITEM_ID, itemKey);

        PendingIntent visibilityPendingIntent = PendingIntent.getActivity(this, 0, visibilityIntent, 0);
        builder.addAction(R.drawable.ic_visible_button, "Make Public", visibilityPendingIntent);

        // view recipes action
        TaskStackBuilder rStackBuilder = TaskStackBuilder.create(this);
        rStackBuilder.addParentStack(RecipeList.class);

        Intent recipesIntent = new Intent(this, RecipeList.class);
        recipesIntent.putExtra(ListActivity.INGREDIENT_NAME, itemName);

        // Adds the Intent that starts the Activity to the top of the stack
        rStackBuilder.addNextIntent(recipesIntent);
        PendingIntent recipesPendingIntent = rStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addAction(R.drawable.ic_recipe_button, "View Recipes", recipesPendingIntent);

        Intent showListIntent = new Intent(this, ListActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, showListIntent, 0));

        return builder.build();
    }
}





