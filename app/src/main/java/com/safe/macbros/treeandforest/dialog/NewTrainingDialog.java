package com.safe.macbros.treeandforest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.SafetyHelper;
import com.safe.macbros.treeandforest.models.Staff;
import com.safe.macbros.treeandforest.models.Training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewTrainingDialog extends DialogFragment {
    private static final String TAG = "NewTrainingDialog";

    //vars
    Methods meth;
    HashMap<String, Object> message;
    ArrayList<Staff> staffList = new ArrayList<>();
    StaffTailgate staff = new StaffTailgate();
    SafetyHelper sHelper = new SafetyHelper();
    TailgateHelper tHelper;
    Tailgate mTailgate = new Tailgate();
    Training training = new Training();
    Context mContext;
    //widgets
    Spinner spin1, spin2;
    TextView title1, title2;
    //fire
    FirebaseFirestore db = MainActivity.getFireDb();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            dismiss();
        } else {
            message = (HashMap<String, Object>)bundle.getSerializable(getTAG());
            staffList = (ArrayList<Staff>)message.get("staffList");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.spinner_dialog, null);
        initUI(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("New Staff Training ")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateFire();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();

    }

    public void initUI(View view) {

        spin1 = view.findViewById(R.id.spinner_sd);
        spin2 = view.findViewById(R.id.spinner2_sd);
        title1 = view.findViewById(R.id.title2_sd);
        title2 = view.findViewById(R.id.title3_sd);

            setSpinner();
    }

    public void setSpinner() {

        List<String> list = new ArrayList<>();

        list.add("Staff");

        for (int i = 0; i < staffList.size(); i++) {
            list.add(staffList.get(i).getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin1.setAdapter(dataAdapter);

        List<String> list2 = new ArrayList<>();
        list2.add("Module");
        list2.add("Planting");
        list2.add("Thinning");
        list2.add("Spot Spraying");
        list2.add("Pruning");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, list2);

        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(dataAdapter2);

    }

    public void updateFire(){
        final DocumentReference dr = db.collection("training").document();
        training.setId(dr.getId());
        if(spin1.getSelectedItemPosition()>0)
            training.setStaffId(staffList.get(spin1.getSelectedItemPosition()-1).getId());
        if(spin2.getSelectedItemPosition()>0)
            training.setQualId(spin2.getSelectedItem().toString());
        training.setCreated(Timestamp.now());
        if(spin1.getSelectedItemPosition()!=0 && spin2.getSelectedItemPosition()!=0)
        dr.set(training).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(mContext, "Training Saved", Toast.LENGTH_SHORT).show();
                sHelper.createAlert(staffList.get(spin1.getSelectedItemPosition()-1).getId(),null, null, null, null, null,
                        dr.getId(), spin2.getSelectedItem().toString()+" module", null, 30);

            }
        });
        else{

            Toast.makeText(mContext, "Nothing Saved", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getTAG() {
        return TAG;
    }
}
