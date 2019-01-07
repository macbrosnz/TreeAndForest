package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.util.ArrayList;
import java.util.HashMap;

public class StaffPLB extends DialogFragment {
    private static final String TAG = "StaffPLB";

    HashMap<String, Object> message = new HashMap<>();
    Tailgate tailgate;
    Methods meth;

    ArrayList<String> plbStaffIds = new ArrayList();

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        meth = (Methods) context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            dismiss();
            Log.d(TAG, "onAttach: got the boot");
            Toast.makeText(context, "Oops something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            tailgate = (Tailgate) message.get("tailgate");
            bundle.clear();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Who is carrying a PLB")
                .setMultiChoiceItems(setNames(tailgate.getStaff()), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean b) {
                        if (b) {
                            plbStaffIds.add((tailgate.getStaffIds().get(which)));
                            Log.d(TAG, "onClick: staffIds = " + plbStaffIds);
                        } else {
                            plbStaffIds.remove(plbStaffIds.indexOf(tailgate.getStaffIds().get(which)));

                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        message.put("plbStaffIds", plbStaffIds);
                        meth.sendFragMessage(new SetPlan(), SetPlan.getTAG(), message);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        return builder.create();
}


    public String[] setNames(ArrayList<String> list) {
        String[] names = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            names[i] = list.get(i);
        }
        return names;

    }

    public static String getTAG() {
        return TAG;
    }
}



























