package com.example.lagarwal.foodbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

public class SimpleAdapter extends ArrayAdapter<Recipe> {

    private List<Recipe> itemList;
    private Context context;

    public SimpleAdapter(List<Recipe> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Recipe getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listitem, null);
        }

        Recipe c = itemList.get(position);
        TextView text = (TextView) v.findViewById(R.id.tvLabel);
        text.setText(c.getLabel());

        TextView text1 = (TextView) v.findViewById(R.id.tvHealthLabel);
        List<String> hLabels=c.getHealthLabels();
        Iterator<String> iterator= hLabels.iterator();
        String healthLabels="";
        while(iterator.hasNext()){
            healthLabels += "#" + iterator.next() + " ";
        }
        //healthLabels.substring(0, healthLabels.length()-2);
        text1.setText(healthLabels);

        //ImageView imgView1 = (ImageView) v.findViewById(R.id.recipeImage);
        //imgView1.setImageBitmap(loadImageFromLocalStore("Profile_Image.png"));

        /*String URL1 = "https://www.edamam.com/web-img/5f5/5f51c89f832d50da84e3c05bef3f66f9.jpg";
        imgView1.setTag(URL1);
        new DownloadImageTask.execute(imgView1);*/

        //imgView1.setImageBitmap(getImageBitmap(("https://www.edamam.com/web-img/5f5/5f51c89f832d50da84e3c05bef3f66f9.jpg")));

        return v;

    }

    private Bitmap loadImageFromLocalStore(String imageURI) {
        Bitmap bitmap= null;
        try {
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/" + imageURI);
            Log.e("URL", uri.toString());
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap getImageBitmap(String url) {
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
        return bm;
    }

    public List<Recipe> getItemList() {
        return itemList;
    }

    public void setItemList(List<Recipe> itemList) {
        this.itemList = itemList;
    }


}