package com.safe.macbros.treeandforest.Activities.tailgate.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Driver;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateDriver extends DialogFragment {
    private static final String TAG = "UpdateDriving";

    HashMap<String, Object> message = new HashMap<>();
    Tailgate tailgate;
    Methods meth;
    ArrayAdapter<String> dataAdapter;
    Driver driver;
    //widgets
    NumberPicker hours;
    Spinner driverSpinner;

    //firebase
    FirebaseFirestore db = MainActivity.getFireDb();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {

            meth.sendFragMessage(new TailgateMain(), TailgateMain.getTAG(), message);
            Toast.makeText(context, "Oops something went wrong", Toast.LENGTH_SHORT).show();

        } else {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            tailgate = (Tailgate) message.get("tailgate");
            driver = (Driver)message.get("driver");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.driving_hours, null);
        initUI(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Driving Hours")
                .setView(view)
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateDriver();
                        message.put("driver", driver);


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

        hours = view.findViewById(R.id.numberPicker);
        driverSpinner = view.findViewById(R.id.driver);
        hours.setMinValue(1);
        hours.setMaxValue(10);
        hours.setWrapSelectorWheel(true);
        hours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });
        spinAdaptor(tailgate.getStaff());
    }

    public void spinAdaptor(ArrayList<String> staffList) {
        dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, staffList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverSpinner.setAdapter(dataAdapter);

    }

    public void updateDriver() {

        DocumentReference dr = db.collection("drivers").document(driver.getId());
        driver.setHours(hours.getValue());
        driver.setName(tailgate.getStaff().get(driverSpinner.getSelectedItemPosition()));
        driver.setStaffId(tailgate.getStaffIds().get(driverSpinner.getSelectedItemPosition()));
        Log.d(TAG, "onSuccess:before firestore Driver id= " +driver.getId() +" hours = "+ driver.getHours());

        dr.set(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Driver id " +driver.getId() +" hours = "+ driver.getHours());
            }
        });

    }


    public static String getTAG() {
        return TAG;
    }
}