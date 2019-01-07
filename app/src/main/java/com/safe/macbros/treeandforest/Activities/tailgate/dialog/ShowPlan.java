package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.custom.Methods;


import java.util.HashMap;

public class ShowPlan extends DialogFragment {
    private static final String TAG = "ShowPlan";
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;
    Tailgate tailgate = new Tailgate();
    StaffTailgate staff = new StaffTailgate();
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        meth = (Methods)getActivity();
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            meth.sendFragMessage(new TailgateMain(), TailgateMain.getTAG(), message);
        }
        message = (HashMap)bundle.getSerializable(getTAG());
        tailgate = (Tailgate)message.get("tailgate");
        staff = (StaffTailgate)message.get("staff");
        bundle.clear();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Plan for Today")
                .setMessage(tailgate.getPlan())
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        staff.setPlanPass(true);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        HashMap<String, Object> updatePlan = new HashMap<>();
                        updatePlan.put("planPass", true);
                        DocumentReference dr = db.collection("tailgates").document(tailgate.getId())
                                .collection("staffChecks")
                                .document(staff.getId());
                        dr.update(updatePlan).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Plan has been saved", Toast.LENGTH_SHORT).show();
                            }
                        });
                        staff.setPlanPass(true);
                        message.put("staff", staff);
                        meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);
                        dismiss();
                    }
                });
        return builder.create();
    }

    public static String getTAG() {
        return TAG;
    }
}