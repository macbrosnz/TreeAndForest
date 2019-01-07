package com.safe.macbros.treeandforest.Activities.tasks.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Equipment;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateTask extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "CreateTask";

    //widgets
    ListView listView;
    Button addQual;
    EditText name, quals, details;


    //vars
    List<String> list = new ArrayList<>();
    Methods meth;
    ArrayList<Equipment> eList = new ArrayList<>();
    HashMap<String,Object> message = new HashMap<>();
    Tasks task = new Tasks();
    FirebaseFirestore db = MainActivity.getFireDb();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createEquipmentList();
        meth = (Methods)context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {

            dismiss();
        }
        else{

            message=(HashMap<String, Object>) bundle.getSerializable(getTAG());


        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.create_task, null);
        initUI(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Create a new Task")
                .setView(view)
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createTasks();
                        meth.sendDialogMessage(new AddTaskEquipment(), AddTaskEquipment.getTAG(), message);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        return builder.create();

    }

    public void initUI(View view){

        listView = view.findViewById(R.id.qualList_createTask);

        addQual = view.findViewById(R.id.addQual_createTask);

        addQual.setOnClickListener(this);

        name=view.findViewById(R.id.name_createTask);

        quals = view.findViewById(R.id.qual_createTask);

        details = view.findViewById(R.id.details_createTask);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.addQual_createTask:{

                adapter(quals.getText().toString());

            }break;

        }
    }

    public void adapter(String qual){

        list.add(qual);

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);

        listView.setAdapter(arrayAdapter);

    }

    public void createTasks(){
        Tasks task = new Tasks();

        task.setName(name.getText().toString());
        task.setDescription(details.getText().toString());
        task.setQualsList(list);
        message.put("newTask", task);
    }

    public static String getTAG() {
        return TAG;
    }

    public void createEquipmentList() {
        final ArrayList<Equipment> equipment = new ArrayList<>();
        CollectionReference
                cr = db.collection("equipment");
        Query query = null;
        query = cr.orderBy("name", Query.Direction.ASCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Equipment eq = doc.toObject(Equipment.class);
                    equipment.add(eq);

                }
                Log.d(TAG, "onEvent: " + equipment.size() );
                message.put("equipmentList", equipment);
            }

        });

    }
}
