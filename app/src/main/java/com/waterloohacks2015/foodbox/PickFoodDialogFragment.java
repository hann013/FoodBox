package com.waterloohacks2015.foodbox;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("What do you want to add?")
                .setItems(_foodItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        _chosen = which;
                    }
                });
        return builder.create();
    }
}

