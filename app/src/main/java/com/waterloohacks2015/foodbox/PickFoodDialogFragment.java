package com.waterloohacks2015.foodbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class PickFoodDialogFragment extends DialogFragment {

    CharSequence[] _foodItems;
    int _chosen;

    public void setFoodItems(CharSequence[] foodItems) {
        _foodItems = foodItems;
    }

    public String getSelectedItem()
    {
        if (_foodItems == null) return "";
        return _foodItems[_chosen].toString();
    }

    public interface OnDialogChosenListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    OnDialogChosenListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("What do you want to add?")
                .setItems(_foodItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        _chosen = which;
                        mListener.onDialogPositiveClick(PickFoodDialogFragment.this);
                    }
                });

        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the OnDialogChosenListener so we can send events to the host
            mListener = (OnDialogChosenListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnDialogChosenListener");
        }
    }

}

