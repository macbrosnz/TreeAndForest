package com.safe.macbros.treeandforest.Activities.tailgate.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.safe.macbros.treeandforest.R;

import java.util.HashMap;

public class UpdateVisitor extends DialogFragment {
    private static final String TAG = "VisitorsForm";

    private String tailgateId;
    private String text;
    private HashMap<String, Object> message;

    @NonNull
    @Override
    public Dialog onCreateDialog(@android.support.annotation.Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.visitor_form, null);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Change Driver details")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();

    }
}