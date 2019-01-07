package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;

import java.util.HashMap;

public class SelectWeather extends DialogFragment {
    private static final String TAG = "TailgateWeather";


    //vars
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;
    Tailgate newTailgate = new Tailgate();

    static SelectWeather newInstance() {
        return new SelectWeather();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            newTailgate = (Tailgate) message.get("tailgate");
            bundle.clear();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Whats the weather Today ?")
                .setItems(R.array.weather, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String[] list = getResources().getStringArray(R.array.weather);
                        newTailgate.setWeather(list[which]);
                        message.put("tailgate", newTailgate);
                        meth.sendDialogMessage(new SelectStaff(), SelectStaff.getTAG(), message);

                    }
                });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public static String getTAG() {
        return TAG;
    }
}
