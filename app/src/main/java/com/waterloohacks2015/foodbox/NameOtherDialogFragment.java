package com.waterloohacks2015.foodbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import java.util.jar.Attributes;

/**
 * Created by dxu on 16-01-23.
 */
public class NameOtherDialogFragment extends DialogFragment {

    String _otherFoodName = "";

    public String getOtherFood(){
        return _otherFoodName;
    }

    public interface OnDialogChosenListener {
        public void onOtherDialogClick(DialogFragment dialog);
    }

    OnDialogChosenListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_other, null))
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        Dialog diag = (Dialog) dialog;
                        EditText editText = (EditText) diag.findViewById(R.id.other_food);
                        _otherFoodName = editText.getText().toString();
                        mListener.onOtherDialogClick(NameOtherDialogFragment.this);
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
