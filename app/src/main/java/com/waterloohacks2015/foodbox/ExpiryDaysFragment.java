package com.waterloohacks2015.foodbox;

import android.app.DatePickerDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lagarwal on 1/23/2016.
 */
public class ExpiryDaysFragment extends DialogFragment {
    NumberPicker np;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_expiry_days, container);
        np = (NumberPicker) view.findViewById(R.id.numberPicker1);

        np.setMinValue(0);
        np.setMaxValue(10);
        np.setWrapSelectorWheel(true);
        getDialog().setTitle("Hello");

        Button btn = (Button)  view.findViewById(R.id.confirmButton);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                Log.e("Expiration Days", String.valueOf(np.getValue()));
                try {
                    getExpirationDate(np.getValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getDialog().dismiss();
            }
        });

        Button expiryDateBtn = (Button)  view.findViewById(R.id.expiryDateButton);
        expiryDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
                showDatePicker();
            }
        });

        return view;
    }

    public String getExpirationDate(int days) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String dateInString=(sdf.format(c.getTime()));
        c.setTime(sdf.parse(dateInString));
        c.add(Calendar.DATE, days);
        sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        Log.e("String date:",dateInString);
        return dateInString;
    }

    private void showDatePicker() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ExpiryDateFragment date = new ExpiryDateFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(fm, "fragment_edit_name");
        //date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Log.e("Year", String.valueOf(year));
            Log.e("monthOfYear", String.valueOf(monthOfYear));
            Log.e("dayofMonth", String.valueOf(dayOfMonth));
        }
    };
}
    /*DatePickerDialog.OnDateSetListener ondateSet;
    NumberPicker np;

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    private int year, month, day;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }*/