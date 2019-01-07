package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.TasksAdapter;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.RecyclerItemClickListner;
import com.safe.macbros.treeandforest.models.Equipment;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.ArrayList;
import java.util.HashMap;


public class SelectTask extends DialogFragment {
    private static final String TAG = "SelectTask";
    //Firestore vars
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cr = db.collection("tasks");

    //classVars
    Methods meth;
    //vars
    TasksAdapter taskAdapter;
    RecyclerView taskRecycler;
    ArrayList<Equipment> ppeEquipment = new ArrayList<>();
    ArrayList<Equipment> equipment = new ArrayList<>();
    HashMap<String, Object> message = new HashMap<>();
    Tailgate mNewTailgate = new Tailgate();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);

        meth = (Methods) getActivity();

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            mNewTailgate = (Tailgate) message.get("tailgate");
            bundle.clear();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recyclerview, null);
        initUI(view);
        initRecycler();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("What Type of work are you doing Today ?")
                .setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dismiss();
            }
        });

        return builder.create();
    }


    public void initUI(View v){

        taskRecycler = v.findViewById(R.id.recyclerview);

    }




    private void initRecycler() {
        Log.d(TAG, "initRecycler: started");
        Query query = cr.orderBy("hits", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Tasks> options = new FirestoreRecyclerOptions.Builder<Tasks>()
                .setQuery(query, Tasks.class)
                .build();

        taskAdapter = new TasksAdapter(options, message, getContext());
        Log.d(TAG, "initRecycler: postAdapter");

        taskRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        taskRecycler.setAdapter(taskAdapter);
        Log.d(TAG, "initRecycler: end");
        taskRecycler.addOnItemTouchListener(new RecyclerItemClickListner(getContext(), taskRecycler,
                new RecyclerItemClickListner.OnTouchActionListener() {
                    @Override
                    public void onLeftSwipe(View view, int position) {

                    }

                    @Override
                    public void onRightSwipe(View view, int position) {

                    }

                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(getActivity(), taskAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
                        ppeList(taskAdapter.getItem(position).getId());
                        eqList(taskAdapter.getItem(position).getId());


                        sendTailgate(taskAdapter.getItem(position).getName(), taskAdapter.getItem(position).getId());

                        dismiss();
                    }
                }));
    }

    public void sendTailgate(String taskName, String taskId){

        mNewTailgate.setTaskName(taskName);
        mNewTailgate.setTaskId(taskId);

        message.put("tailgate", mNewTailgate);

        meth.sendDialogMessage(new SelectWeather(), SelectWeather.getTAG(), message);
    }


    public void ppeList( String id){

        CollectionReference cr = db.collection("tasks").document(id).collection("ppe");

        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {

                for (int i = 0; i < docs.size(); i++) {

                    Equipment eq;
                    eq = docs.getDocuments().get(i).toObject(Equipment.class);
                    ppeEquipment.add(eq);

                }
            }
        });

    }

    public void eqList(String id){

        CollectionReference cr = db.collection("tasks").document(id).collection("equipment");

        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {

                for (int i = 0; i < docs.size(); i++) {

                    Equipment eq;
                    eq = docs.getDocuments().get(i).toObject(Equipment.class);
                    equipment.add(eq);

                }
                equipment.addAll(ppeEquipment);
                message.put("equipment", equipment);

            }
        });

    }



    @Override
    public void onStop() {
        super.onStop();
        taskAdapter.stopListening();
    }

    public static String getTAG() {
        return TAG;
    }
    @Override
    public void onDetach() {

        super.onDetach();

    }
}
