package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


import com.safe.macbros.treeandforest.Activities.tailgate.TailgateEquipment;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateSign;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.util.HashMap;

public class StaffCheck extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "TailgateStaffCheck";
    //widgets
    ConstraintLayout parent;
    TextView passBanner, equipment, sign;
    CheckBox eqCheck, planCheck, signCheck;

    //vars
    Boolean ppePass = false, eqPass = false, planPass = false, signPass = false;
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;

    //class
    Tailgate tailgate = new Tailgate();
    StaffTailgate staff = new StaffTailgate();


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        message.clear();
        meth = (Methods) getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = (HashMap) bundle.getSerializable(getTAG());
            if (message != null) {

                tailgate = (Tailgate) message.get("tailgate");
                staff = (StaffTailgate) message.get("staff");
                Log.d(TAG, "onAttach: task = " + tailgate.getTaskName() + "eqPass = " + staff.isEquipmentPass());
                bundle.clear();

            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.staffcheck_tailgate, null);
        initUI(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(staff.getName() + "'s " + "Pre-Work Checklist")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    private void initUI(View view) {
        Log.d(TAG, "initUI: planPass" + String.valueOf(planPass));
        equipment = view.findViewById(R.id.equipText_staffchecklist);
        equipment.setOnClickListener(this);

        sign = view.findViewById(R.id.signatureText_staffchecklist);
        sign.setOnClickListener(this);

        eqCheck = view.findViewById(R.id.equipment_tailstaffcheck);
        eqCheck.setEnabled(false);
        eqCheck.setChecked(staff.isEquipmentPass());

        signCheck = view.findViewById(R.id.signature_tailstaffcheck);
        signCheck.setEnabled(false);
        signCheck.setChecked(staff.isSignPass());

        passBanner=view.findViewById(R.id.passtext_staffcheckDialog);
        parent=view.findViewById(R.id.parent_staffcheckDialog);

        if(staff.isCompleted()) {
            passBanner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signatureText_staffchecklist: {
                Log.d(TAG, "onClick: signature");
                if (staff.getSignUrl() != null) {
                    Log.d(TAG, "onClick: staffUrl" + staff.getSignUrl());
                    meth.sendFragMessage(new ShowImage(), ShowImage.getTAG(), message);
                } else if (staff.isEquipmentPass()){
                    message.put("type", "daily");
                    meth.sendFragMessage(new TailgateSign(), TailgateSign.getTAG(), message);}
                else {
                    meth.sendDialogMessage(new StaffCheck(), StaffCheck.getTAG(), message);
                    meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);
                }
                dismiss();
                break;
            }
            case R.id.equipText_staffchecklist: {
                meth.sendFragMessage(new TailgateEquipment(), TailgateEquipment.getTAG(), message);
                dismiss();
                break;
            }


        }

    }

    public static String getTAG() {
        return TAG;
    }


}

