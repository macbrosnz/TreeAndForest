package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Hazards;
import com.safe.macbros.treeandforest.models.Staff;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.util.ArrayList;
import java.util.HashMap;

public class NewSiteHazard extends DialogFragment {
    private static final String TAG = "NewSiteHazard";

    ArrayList<String> hazards = new ArrayList();
    ArrayList<String> hazardIds = new ArrayList();
    ArrayList<Hazards> hazardList = new ArrayList();
    Hazards hazard = new Hazards();
    Tailgate newTailgate = new Tailgate();
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            message = (HashMap) bundle.getSerializable(getTAG());

            bundle.clear();

        } else {

            meth.sendFragMessage(new TailgateMain(), TailgateMain.getTAG(), message);
            Toast.makeText(getContext(), "Something went Wrong: Please try again", Toast.LENGTH_SHORT).show();

        }

        newTailgate = (Tailgate) message.get("tailgate");

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);


    }



    public static String getTAG() {
        return TAG;
    }


}
