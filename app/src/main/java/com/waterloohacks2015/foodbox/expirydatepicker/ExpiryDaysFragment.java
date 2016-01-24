package com.waterloohacks2015.foodbox.expirydatepicker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.waterloohacks2015.foodbox.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lagarwal on 1/23/2016.
 */
public class ExpiryDaysFragment extends DialogFragment {
    public static SimpleDateFormat expiryDateDisplay = new SimpleDateFormat("yyyy-MM-dd");
    NumberPicker numberPicker;
    String expiryDate;
    TextView expiryDateView;
    long expiryDateMillis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        expiryDateView = (TextView) getActivity().findViewById(R.id.expiry_date);

        View view = inflater.inflate(R.layout.activity_expiry_days, container);
        numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker1);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setWrapSelectorWheel(true);

        Button btn = (Button) view.findViewById(R.id.confirmButton);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                Log.e("Expiration Days", String.valueOf(numberPicker.getValue()));
                try {
                    getExpirationDate(numberPicker.getValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getDialog().dismiss();
            }
        });

        Button expiryDateBtn = (Button) view.findViewById(R.id.expiryDateButton);
        expiryDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
                showDatePicker();
            }
        });

        return view;
    }

    private void getExpirationDate(int days) throws ParseException {
        Calendar c = Calendar.getInstance();
        String dateInString = (expiryDateDisplay.format(c.getTime()));
        c.setTime(expiryDateDisplay.parse(dateInString));
        c.add(Calendar.DATE, days);

        expiryDateMillis = c.getTimeInMillis();
        expiryDate = expiryDateDisplay.format(new Date(expiryDateMillis));
        expiryDateView.setText(expiryDate);
    }

    public long getExpiryDateMillis() {
        return expiryDateMillis;
    }

    private void showDatePicker() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog date = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);

                expiryDateMillis = c.getTimeInMillis();
                expiryDate = expiryDateDisplay.format(new Date(expiryDateMillis));
                expiryDateView.setText(expiryDate);
            }
        }, year, month, day);
        date.show();
    }
}