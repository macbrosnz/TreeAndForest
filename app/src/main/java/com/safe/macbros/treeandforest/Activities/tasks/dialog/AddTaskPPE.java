package com.safe.macbros.treeandforest.Activities.tasks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Equipment;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddTaskPPE extends DialogFragment {
    private static final String TAG = "AddTaskPPE";

    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    ArrayList<Equipment> eList = new ArrayList<>();
    ArrayList<Equipment> newPpeList = new ArrayList<>();
    ArrayList<Equipment> ppeList = new ArrayList<>();
    ArrayList<Equipment> equipmentList = new ArrayList<>();
    Tasks task = new Tasks();

    FirebaseFirestore db = MainActivity.getFireDb();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            dismiss();
        } else {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            equipmentList = (ArrayList<Equipment>) message.get("equipmentList");
            task = (Tasks) message.get("newTask");
            eList = (ArrayList<Equipment>)message.get("eList");
        }

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("What PPE is needed?")
                .setMultiChoiceItems(listtoStringArray(), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){

                        newPpeList.add(ppeList.get(which));

                        }
                        else{

                            newPpeList.remove(newPpeList.indexOf(ppeList.get(which)));

                        }
                    }
                })
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNewTask();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    public String[] listtoStringArray() {


        for (int i = 0; i < equipmentList.size(); i++) {
            if (equipmentList.get(i).getPpe() != null) {
                if (equipmentList.get(i).getPpe()) {
                    ppeList.add(equipmentList.get(i));
                }
            }
        }


        String[] s;
        if (ppeList.size() > 0) ;

        {
            s = new String[ppeList.size()];
            for (int i = 0; i < ppeList.size(); i++) {
                s[i] = ppeList.get(i).getName();
            }
        }

        return s;
    }


    public void saveNewTask(){

        final DocumentReference dr = db.collection("tasks").document();
        task.setId(dr.getId());

        dr.set(task).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                newEquipCollection(dr);
                newPPECollection(dr);

            }
        });

    }

    public void newEquipCollection(DocumentReference dr){

        for (int i = 0; i < eList.size(); i++) {

            DocumentReference dr2 = dr.collection("equipment").document(eList.get(i).getId());
            dr2.set(eList.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                }
            });
            newHazardCollection(eList.get(i).getId(), dr);
        }

    }

    public void newHazardCollection(final String id, final DocumentReference dr){

        CollectionReference cr = db.collection("hazards");

        Query query = cr.whereEqualTo("equipmentId", id);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (int i = 0; i < docs.size(); i++) {

                    String hId = docs.getDocuments().get(i).getId();

                    DocumentReference dr2 = dr.collection("hazards").document(hId);

                    dr2.set(docs.getDocuments().get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.d(TAG, "onSuccess: ");

                        }
                    });
                }


            }
        });


    }

    public void newPPECollection(DocumentReference dr){

        for (int i = 0; i < newPpeList.size(); i++) {


            DocumentReference dr2 = dr.collection("ppe").document(newPpeList.get(i).getId());

            dr2.set(newPpeList.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Log.d(TAG, "onSuccess: ");

                }
            });
        }

    }

    public static String getTAG() {
        return TAG;
    }
}
