package com.safe.macbros.treeandforest.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.safe.macbros.treeandforest.custom.Methods;

import java.util.HashMap;

public class HazardDialog extends DialogFragment {
    private static final String TAG = "HazardDialog";

    //vars
    HashMap<String, Object>message = new HashMap<>();
    String title, description;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = this.getArguments();
        if (bundle == null) {

            dismiss();

        }
        else{

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            title = (String)message.get("title");
            description = (String)message.get("description");

        }
        bundle.clear();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(title)
                .setMessage(description);

        return builder.create();
    }

    public static String getTAG() {
        return TAG;
    }
}
