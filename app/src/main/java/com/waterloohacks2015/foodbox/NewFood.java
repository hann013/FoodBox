package com.waterloohacks2015.foodbox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

    String [] fruitVeg = { "apple", "banana", "asparagus", "pepper", "eggplant", "tomato", "celery", "spinach", "mango", "watermelon" } ;
    String [] crabDia = {"rice", "sliced bread", "milk", "eggs", "cheese", "chicken" , "beef", "pork", "lamb", "veal"  };
    String [] junkFood = {"chocolate", "cream", "mayo", "chocolate cake", "fries", "kfc", "chip", "brownies", "candy", "pudding"};

    private static int fv = 0; ; private static int cD = 0; private static int jf = 0;private static int o = 0;

    private EditText date;

    private DatePickerDialog fromDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    private static String color = "white";

    public String getColor() {
        return color;
    }


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
                foodName.toLowerCase();
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

                color = determineRank(fv,cD, jf, o);
                Log.d("This color is :", color);

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
                finish();
            }
        });
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


    //Rewards system

    public String determineRank( int fv, int cD, int jf, int o ){
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
