package com.waterloohacks2015.foodbox;

import android.util.Log;

/**
 * Created by jennadeng on 2016-01-24.
 */
public class HealthScale {
    public HealthScale(){

    }
    private static String color = "white";
    private static int fv = 0; ; private static int cD = 0; private static int jf = 0;private static int o = 0;

    public static String getColor() {
        return color;
    }

    public static void setColor(String foodName) {

        String [] fruitVeg = { "apple", "banana", "asparagus", "pepper", "eggplant", "tomato", "celery", "spinach", "mango", "watermelon" } ;
        String [] crabDia = {"rice", "sliced bread", "milk", "eggs", "cheese", "chicken" , "beef", "pork", "lamb", "veal"  };
        String [] junkFood = {"chocolate", "cream", "mayo", "chocolate cake", "fries", "kfc", "chip", "brownies", "candy", "pudding"};
        int c = 0;
        for( int i = 3; i >= 0; i-- ){
            if( foodName.contains(fruitVeg[i])){
                fv += 1;
                break;
            }
            if( foodName.contains(crabDia[i])){
                cD += 1;
                break;
            }
            if( foodName.contains(junkFood[i])){
                jf += 1;
                break;
            }
            else {
                c += 1;
            }
            Log.d("This row is :", Integer.toString(fv) + fruitVeg[i] + Integer.toString(cD) + crabDia[i]+ Integer.toString(jf) + junkFood[i]+ Integer.toString(c));
        }
        if( c == 4 ){
            o+=1;
        }
        HealthScale.color = determineRank();
        Log.d("This color is :", HealthScale.color);

    }

    public static String determineRank(){
        double t = fv + cD + jf + o;
        Log.d("Number of fv", Integer.toString(fv));
        Log.d("Number of jf", Integer.toString(jf));
        Log.d("Number of t", Double.toString(t));

        if(jf/t > 0.20){
            Log.d("Current", "red");
            return "red";
        }
        if(jf/t <= 0.20)
        {
            if(fv > cD ){
                Log.d("Current", "green");
                return "green";
            }

            else if(fv <= cD){
                return "yellow";
            }
        }

        return "white";
    }
}
