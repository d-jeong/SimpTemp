package com.davidjeong.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setPositiveButton(getArguments().getString("buttonText"), null);

        return builder.create();
    }

    public static AlertDialogFragment newInstance(String title, String message, String buttonText) {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("buttonText", buttonText);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }
}
