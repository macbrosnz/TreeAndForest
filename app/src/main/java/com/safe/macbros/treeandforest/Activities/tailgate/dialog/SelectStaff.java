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

import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectStaff extends DialogFragment {
    private static final String TAG = "SelectStaff";

    //vars
    ArrayList<String> staff = new ArrayList();
    ArrayList<String> staffIds = new ArrayList();
    ArrayList<String> names = new ArrayList();
    ArrayList<String> nameIds = new ArrayList();
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;
    Tailgate newTailgate = new Tailgate();

    public SelectStaff() {
        super();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meth = (Methods) getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            message = (HashMap) bundle.getSerializable(getTAG());

            for (int i = 0; i < ((ArrayList<Staff>) message.get("staff")).size(); i++) {

                Staff staff = ((ArrayList<Staff>) message.get("staff")).get(i);
                names.add(staff.getName());
                nameIds.add(staff.getId());
                bundle.clear();
            }

        } else {
            meth.sendFragMessage(new TailgateMain(), TailgateMain.getTAG(), message);
            Toast.makeText(getContext(), "Something went Wrong: Please try again", Toast.LENGTH_SHORT).show();
        }

        newTailgate = (Tailgate) message.get("tailgate");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Who's on Staff today?")
                .setMultiChoiceItems(setNames(names), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {

                            staff.add(names.get(i));
                            staffIds.add(nameIds.get(i));

                        } else {

                            staff.remove(staff.indexOf(names.get(i)));
                            staffIds.remove(staffIds.indexOf(nameIds.get(i)));

                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        newTailgate.setStaff(staff);
                        newTailgate.setStaffIds(staffIds);
                        message.put("tailgate", newTailgate);
                        meth.sendDialogMessage(new DriverHours(), DriverHours.getTAG(), message);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {

        super.onDetach();
        names.clear();
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
