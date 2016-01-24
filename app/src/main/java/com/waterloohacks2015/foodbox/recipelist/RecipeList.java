package com.example.lagarwal.foodbox;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class RecipeList extends ListActivity {
    SimpleAdapter adpt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        String ingredient="chicken";
        String url="https://api.edamam.com/search?q="+ingredient+"&app_id=d532a6af&app_key=2d2558593b5a96eb974d14d7a6432a2d";
        adpt  = new SimpleAdapter(new ArrayList<Recipe>(), this);
        ListView lView = (ListView) findViewById(android.R.id.list);

        lView.setAdapter(adpt);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe clickedRecipe = adpt.getItem(position);
                String uri= clickedRecipe.uri();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(browserIntent);
            }
        });

        // Exec async load task
        (new FindRecipe(adpt)).execute(url);
    }

}
