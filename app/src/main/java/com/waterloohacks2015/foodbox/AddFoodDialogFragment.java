package com.waterloohacks2015.foodbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.net.URI;

public class AddFoodDialogFragment extends DialogFragment {
    private static final String URI_KEY = "photoUri";

    private CharSequence[] choices = new CharSequence[] {
            "From photo",
            "Custom"
    };

    static AddFoodDialogFragment newInstance(Uri photoUri) {
        AddFoodDialogFragment dialog = new AddFoodDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(URI_KEY, photoUri);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new food item");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // launch camera
                        takePicture();
                        break;
                    case 1:
                        addFoodItem();
                        break;
                    default:
                        // launch activity for adding new items
                        break;
                }
            }
        });
        return builder.create();
    }

    private void takePicture() {
        Uri photoUri = getArguments().getParcelable(URI_KEY);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        getActivity().startActivityForResult(cameraIntent, ListActivity.IMAGE_CAPTURE_REQUEST_CODE);
    }

    private void addFoodItem()
    {
        Intent newFoodIntent = new Intent(getActivity(), NewFood.class);
        startActivity(newFoodIntent);

    }
}
