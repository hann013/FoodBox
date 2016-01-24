package com.waterloohacks2015.foodbox;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.waterloohacks2015.foodbox.expirydatepicker.ExpiryDaysFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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
    private TextView expiryDate;
    private Button saveButton;

    private ProgressDialog _progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);
        foodImage = (ImageView) findViewById(R.id.food_image);
        expiryDate = (TextView) findViewById(R.id.expiry_date);
        saveButton = (Button) findViewById(R.id.save_button);

        //launch api
        Uri photoUri = getIntent().getExtras().getParcelable(RecognitionActivity.INPUT_URI);
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
        Spinner foodNameSpinner = (Spinner) findViewById(R.id.food_name);
        foodNameSpinner.setAdapter(adapter);
        foodNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EditText customFoodName = (EditText) findViewById(R.id.food_name_custom);
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

        // set datepicker dialog listener for expiry date
        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpiryDaysFragment expiryDatePicker = new ExpiryDaysFragment();
                expiryDatePicker.show(getSupportFragmentManager(), "ExpiryDatePicker");
            }
        });

        // add onClickListener for save button
    }
}





