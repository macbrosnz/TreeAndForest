package com.safe.macbros.treeandforest.Activities.equipment.dialog;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.models.Equipment;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.ArrayList;
import java.util.List;

public class NewEquipment extends DialogFragment {
    private static final String TAG = "NewEquipment";
    private Equipment equipment;
    private Tasks task;
    private FirebaseFirestore db = MainActivity.getFireDb();
    ArrayList<String> eqIds = new ArrayList<>();
    ArrayList<String> taskIds = new ArrayList<>();

    Context context;
    //widgets
    private EditText title, maintenance;
    Spinner spinner, spinner2;
    ArrayAdapter<String> mArrayAdapter;
    Switch ppeBoolean, vBool, mBool, cBool;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_equipment, null);
        initUI(view);
        builder.setTitle("New Item")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference dr = db.collection("equipment").document();
                        equipment = new Equipment();
                        equipment.setId(dr.getId());
                        equipment.setName(title.getText().toString());
                        equipment.setMaintenanceHint(maintenance.getText().toString());
                        equipment.setChildlist(null);

                        if (ppeBoolean.isChecked())
                            equipment.setPpe(true);
                        if (mBool.isChecked())
                            equipment.setFirstaid(true);
                        if (cBool.isChecked())
                            equipment.setChemical(true);
                        if (vBool.isChecked())
                            equipment.setVehicle(true);
                        if (spinner.getSelectedItemPosition() != 0)
                            equipment.setParentId(eqIds.get(spinner.getSelectedItemPosition() - 1));
                        dr.set(equipment).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "New Item created", Toast.LENGTH_SHORT).show();
                                if (spinner.getSelectedItemPosition() != 0) {
                                    updateParent(eqIds.get(spinner.getSelectedItemPosition() - 1), equipment.getId());
                                }
                                Log.d(TAG, "onComplete: success");

                            }
                        });
                    }
                });

        return builder.create();
    }

    public void initUI(View view) {
        context = view.getContext();
        title = view.findViewById(R.id.title_addequipment);
        maintenance = view.findViewById(R.id.maintenance_addequipment);
        spinner = view.findViewById(R.id.spinner_addequipment);
        spinner2 = view.findViewById(R.id.spinner2_addequipment);
        ppeBoolean = view.findViewById(R.id.ppeBoolean);
        cBool = view.findViewById(R.id.chemicalBoolean);
        mBool = view.findViewById(R.id.medicalBoolean);
        vBool = view.findViewById(R.id.vehicleBoolean);
        initAdapters();
        ppeBoolean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ppeBoolean.isChecked()) {

                    cBool.setChecked(false);
                    mBool.setChecked(false);
                    vBool.setChecked(false);

                }
            }
        });
        cBool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cBool.isChecked()) {

                    ppeBoolean.setChecked(false);
                    mBool.setChecked(false);
                    vBool.setChecked(false);

                }
            }
        });
        mBool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mBool.isChecked()) {

                    cBool.setChecked(false);
                    ppeBoolean.setChecked(false);
                    vBool.setChecked(false);

                }
            }
        });
        vBool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (vBool.isChecked()) {

                    cBool.setChecked(false);
                    ppeBoolean.setChecked(false);
                    mBool.setChecked(false);

                }
            }
        });
    }

    public void initAdapters() {
        final List<String> list = new ArrayList<>();
        list.add("none");


        CollectionReference cr = db.collection("equipment");
        Query query = cr.whereEqualTo("parentId", null).orderBy("name", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (docs != null) {
                    for (int i = 0; i < docs.size(); i++) {
                        equipment = new Equipment();

                        equipment = docs.getDocuments().get(i).toObject(Equipment.class);
                        eqIds.add(equipment.getId());
                        list.add(equipment.getName());

                    }
                    mArrayAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, list);

                    mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(mArrayAdapter);
                }
            }
        });
        final List<String> list2 = new ArrayList<>();
        list2.add("none");
        cr = db.collection("tasks");
        Query queryt = cr.orderBy("name", Query.Direction.ASCENDING);
        queryt.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (docs != null) {
                    for (int i = 0; i < docs.size(); i++) {
                        task = new Tasks();

                        task = docs.getDocuments().get(i).toObject(Tasks.class);
                        taskIds.add(task.getId());
                        list2.add(task.getName());

                    }
                    mArrayAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, list2);

                    mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner2.setAdapter(mArrayAdapter);
                }
            }
        });


    }

    public void initBooleans() {

        if (ppeBoolean.isChecked()) {

            cBool.setChecked(false);
            mBool.setChecked(false);
            vBool.setChecked(false);

        }
        else if (cBool.isChecked()) {

            ppeBoolean.setChecked(false);
            mBool.setChecked(false);
            vBool.setChecked(false);

        }
        else if (mBool.isChecked()) {

            cBool.setChecked(false);
            ppeBoolean.setChecked(false);
            vBool.setChecked(false);

        }
        else if (vBool.isChecked()) {

            cBool.setChecked(false);
            ppeBoolean.setChecked(false);
            mBool.setChecked(false);

        }


    }

    public void updateParent(final String parentId, String childId) {
        DocumentReference dr = db.collection("equipment").document(parentId);

        dr.update("childlist", FieldValue.arrayUnion(childId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: child added to " + parentId);
            }
        });

    }

    public static String getTAG() {
        return TAG;
    }
}
