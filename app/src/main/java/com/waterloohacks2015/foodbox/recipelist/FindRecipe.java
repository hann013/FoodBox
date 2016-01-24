package com.example.lagarwal.foodbox;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FindRecipe extends AsyncTask<String, Void, List<Recipe>> {

    String responseString;
    SimpleAdapter adpt;

    public FindRecipe(SimpleAdapter adpt){
        this.adpt=adpt;
    }

    @Override
    protected void onPostExecute(List<Recipe> result) {
        super.onPostExecute(result);
        adpt.setItemList(result);
        adpt.notifyDataSetChanged();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Recipe> doInBackground(String... params) {
        URL url;
        HttpURLConnection urlConnection = null;
        JSONArray response= new JSONArray();
        List<Recipe> result = new ArrayList<Recipe>();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpStatus.SC_OK){
                responseString = readStream(urlConnection.getInputStream());
                //Log.e("CatalogClient", responseString);
                response = new JSONArray(responseString);
            }else{
               // Log.e("CatalogClient", "Response code:"+ responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("hits");
            for (int i = 0; i < (jsonArray.length()); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                JSONObject jsonObject3 = jsonObject2.getJSONObject("recipe");
                String label= (String) jsonObject3.get("label");
                String uri=(String) jsonObject3.get("shareAs");
                String imageURI= (String) jsonObject3.get("image");
                List<String> healthLabels= new ArrayList<String>();
                JSONArray jsonArray2 = jsonObject3.getJSONArray("healthLabels");
                for (int j = 0; j < (jsonArray2.length()); j++) {
                    healthLabels.add(((String) jsonArray2.get(j)));
                }
                //getImageBitmap(imageURI);
                Recipe newRecipe= new Recipe(label, healthLabels, uri);
                result.add(newRecipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (Exception e) {
            Log.e("TAG", "Error getting bitmap", e);
        }
        saveIamgeToLocalStore(bm);
    }

    private void saveIamgeToLocalStore(Bitmap finalBitmap) {
        File directory = Environment.getExternalStorageDirectory();
        File file = new File(directory + "/" + "profile_image.png");
        /*String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/temp");
        myDir.mkdirs();
        String fname = "Profile_Image.png";
        File file = new File(myDir, fname);*/
        if (file.exists()) file.delete();
        Log.e("File", file.toString());
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.e("File", "SAVED");
        } catch (Exception e) {
            Log.e("File", "NOT SAVED");
            e.printStackTrace();
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public void readJson() throws Exception{
        JSONObject jsonObject = new JSONObject(responseString);
        System.out.println("Parsing the tweets");
        JSONArray jsonArray = jsonObject.getJSONArray("hits");
        Log.e("sd",String.valueOf(jsonArray.length()));
        for (int i = 0; i < (jsonArray.length()); i++) {
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            System.out.println(jsonObject2.toString());
            JSONObject jsonObject3 = jsonObject2.getJSONObject("recipe");
            System.out.println();
            System.out.println(jsonObject3.get("uri"));
            System.out.println(jsonObject3.get("label"));
        }
    }
}
