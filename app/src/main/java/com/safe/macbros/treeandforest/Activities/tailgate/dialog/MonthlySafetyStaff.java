package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMonthly;
import com.safe.macbros.treeandforest.Activities.tailgate.models.MonthlySafety;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.ArrayList;
import java.util.HashMap;

public class MonthlySafetyStaff extends DialogFragment {
    private static final String TAG = "MonthlySafetyStaff";


    FirebaseFirestore db = MainActivity.getFireDb();

    //vars
    ArrayList<String> staff = new ArrayList();
    ArrayList<String> staffIds = new ArrayList();
    ArrayList<String> names = new ArrayList();
    ArrayList<String> nameIds = new ArrayList();
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;
    StaffTailgate mStaffTailgate = new StaffTailgate();
    Tailgate newTailgate;
    MonthlySafety mSafety = new MonthlySafety();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meth = (Methods)getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            message = (HashMap)bundle.getSerializable(getTAG());
            for (int i = 0; i < ((ArrayList<Staff>)message.get("staff")).size(); i++) {
                Staff staff;
                staff = ((ArrayList<Staff>) message.get("staff")).get(i);
                names.add(staff.getName());
                nameIds.add(staff.getId());
                bundle.clear();
            }

        }else
        { meth.sendFragMessage(new TailgateMain(), TailgateMain.getTAG(), message);
            Toast.makeText(getContext(), "Something went Wrong: Please try again", Toast.LENGTH_SHORT).show();}
        newTailgate = (Tailgate)message.get("tailgate");
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Meeting Attendees")
        .setMultiChoiceItems(setNames(names), null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i, boolean b) {
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
                    public void onClick(DialogInterface dialog, int which) {
                        createMonthlyMeeting();
                        message.put("monthlySafety", mSafety);
                        meth.sendFragMessage(new TailgateMonthly(), TailgateMonthly.getTAG(), message);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
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


    public void createMonthlyMeeting(){

        DocumentReference dr1 = db.collection("safetyMonthly").document();
        mSafety.setId(dr1.getId());
        mSafety.setMeetingAttendance(staff);
        mSafety.setTimestamp(Timestamp.now());
        mSafety.setTailgateId(newTailgate.getId());
        mSafety.setSiteId(newTailgate.getBlockId());

        dr1.set(mSafety).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: attendees updated");
            }
        });

        for (int i = 0; i < staff.size(); i++) {

            DocumentReference dr = db.collection("safetyMonthly").document(dr1.getId())
                    .collection("attendees").document(staffIds.get(i));
            mStaffTailgate.setId(staffIds.get(i));
            mStaffTailgate.setName(staff.get(i));
            mStaffTailgate.setSignPass(false);
            dr.set(mStaffTailgate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });


        }




    }

    public static String getTAG() {
        return TAG;
    }
}
