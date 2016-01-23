package com.waterloohacks2015.foodbox;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.provider.MediaStore.Images.Media;


/**
 * A simple Activity that performs recognition using the Clarifai API.
 */
public class RecognitionActivity extends FragmentActivity implements
        PickFoodDialogFragment.OnDialogChosenListener, NameOtherDialogFragment.OnDialogChosenListener {
  private static final String TAG = RecognitionActivity.class.getSimpleName();

  // IMPORTANT NOTE: you should replace these keys with your own App ID and secret.
  // These can be obtained at https://developer.clarifai.com/applications
  private static final String APP_ID = "Dzyc6O-UZ6pvZTPGylb-Nao2yvTyO3TG_ZY7UNyF";
  private static final String APP_SECRET = "m1RQPouLYUF8XuQr5oWy6F8ARIQ1AzCsKMK8461X";

  public static final String INPUT_URI = "INPUT_URI";

  public static final String OTHER_TEXT = "Other...";

  private final ClarifaiClient client = new ClarifaiClient(APP_ID, APP_SECRET);
  private ImageView imageView;
  private TextView textView;
  private PickFoodDialogFragment dialogFragment;
  private NameOtherDialogFragment otherFoodDialogFragment;
  private ProgressDialog _progressDialog;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recognition);
    imageView = (ImageView) findViewById(R.id.image_view);
    textView = (TextView) findViewById(R.id.text_view);


    //launch api
    Uri photoUri = getIntent().getExtras().getParcelable(RecognitionActivity.INPUT_URI);
    Log.d(TAG, "User picked image: " + photoUri);
    Bitmap bitmap = loadBitmapFromUri(photoUri);
    if (bitmap != null) {
      imageView.setImageBitmap(bitmap);
      textView.setText("Recognizing...");
      _progressDialog =new ProgressDialog(this);
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
    dialogFragment = new PickFoodDialogFragment();

  }

  /** Loads a Bitmap from a content URI returned by the media picker. */
  private Bitmap loadBitmapFromUri(Uri uri) {
    try {
      // The image may be large. Load an image that is sized for display. This follows best
      // practices from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
      BitmapFactory.Options opts = new BitmapFactory.Options();
      opts.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
      int sampleSize = 1;
      System.out.println(imageView.getWidth());
      System.out.println(imageView.getHeight());

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

  /** Sends the given bitmap to Clarifai for recognition and returns the result. */
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

  /** Updates the UI by displaying tags for the given result. */
  private void updateUIForResult(RecognitionResult result) {
    System.out.println(result.getJsonResponse().toString());
    if (result != null) {
      if (result.getStatusCode() == RecognitionResult.StatusCode.OK) {
        // Display the list of tags in the UI.
        ArrayList<String> foodItems = new ArrayList<String>();
        int count = 0;
        for (Tag tag : result.getTags()) {
          foodItems.add(tag.getName());
          count++;
          if (count > 4) break;
        }
        foodItems.add(RecognitionActivity.OTHER_TEXT);
        dialogFragment.setFoodItems(foodItems.toArray(new CharSequence[foodItems.size()]));
        dialogFragment.show(getFragmentManager(), "dialogFragment");


      } else {
        Log.e(TAG, "Clarifai: " + result.getStatusMessage());
        textView.setText("Sorry, there was an error recognizing your image.");
      }
    } else {
      textView.setText("Sorry, there was an error recognizing your image.");
    }
  }


  @Override
  public void onDialogPositiveClick(DialogFragment dialog) {
    String chosenFood = dialogFragment.getSelectedItem();
    if (chosenFood.equals(RecognitionActivity.OTHER_TEXT))
    {
      otherFoodDialogFragment = new NameOtherDialogFragment();
      otherFoodDialogFragment.show(getFragmentManager(), "otherFoodDialogFragment");
      chosenFood = otherFoodDialogFragment.getOtherFood();
    }
    textView.setText("You have chosen: " + chosenFood);
  }

  @Override
  public void onOtherDialogClick(DialogFragment dialog)
  {
      String chosenFood = otherFoodDialogFragment.getOtherFood();
      System.out.println("HELLO WORLD");
      System.out.println(chosenFood);
      textView.setText("You have chosen: " + chosenFood);
  }
}




