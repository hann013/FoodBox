package com.waterloohacks2015.foodbox.recipelist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lagarwal on 1/23/2016.
 */
public class Recipe implements Serializable {

    private String label;
    private List<String> healthLabels;
    private String uri;
    private String imageURI;

    public Recipe(String label, List<String> healthLabels, String uri) {
        super();
        this.label=label;
        this.healthLabels=healthLabels;
        this.uri= uri;
        this.imageURI="https://www.edamam.com/web-img/5f5/5f51c89f832d50da84e3c05bef3f66f9.jpg";
    }

    public String getLabel(){
        return label;
    }

    public List<String> getHealthLabels(){
        return healthLabels;
    }

    public String uri(){
        return  uri;
    }
    // get and set methods
}
