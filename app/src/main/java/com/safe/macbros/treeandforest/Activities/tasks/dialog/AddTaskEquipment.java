package com.safe.macbros.treeandforest.Activities.tasks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Equipment;

import java.util.ArrayList;
import java.util.HashMap;

public class AddTaskEquipment extends DialogFragment {
    private static final String TAG = "AddTaskEquipment";


    //vars
    Methods meth;
    ArrayList<Equipment> eList = new ArrayList<>();
    ArrayList<Equipment> equipmentList = new ArrayList<>();
    ArrayList<Equipment> newEList = new ArrayList<>();
    HashMap<String, Object> message = new HashMap<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {

            dismiss();
            Toast.makeText(context, "Network Issues", Toast.LENGTH_SHORT).show();

        } else {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());

            if (message.get("equipmentList") != null) {

                equipmentList = (ArrayList<Equipment>) message.get("equipmentList");

            } else {

                dismiss();
                Toast.makeText(context, "Network Issues", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("What equipment would you be using")
                .setMultiChoiceItems(listtoStringArray(), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {

                            newEList.add(eList.get(which));

                        } else {

                            newEList.remove(newEList.indexOf(eList.get(which)));

                        }
                    }

                })
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        message.put("eList", newEList);
                        message.put("equipmentList", equipmentList);
                        meth.sendDialogMessage(new AddTaskPPE(), AddTaskPPE.getTAG(), message);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();

    }

    public String[] listtoStringArray() {

        for (int i = 0; i < equipmentList.size(); i++) {
            if (equipmentList.get(i).getPpe() == null) {
                eList.add(equipmentList.get(i));
            } else {
                if (!equipmentList.get(i).getPpe()) {
                    eList.add(equipmentList.get(i));
                }
            }
        }


        String[] s;
        if (eList.size() > 0) ;
        {
            s = new String[eList.size()];

            for (int i = 0; i < eList.size(); i++) {

                s[i] = eList.get(i).getName();

            }
        }

        return s;
    }

    public static String getTAG() {
        return TAG;
    }
}
