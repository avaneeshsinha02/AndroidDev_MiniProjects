package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This is a dialog from a fragment.")
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                });
        return builder.create();
    }
}
