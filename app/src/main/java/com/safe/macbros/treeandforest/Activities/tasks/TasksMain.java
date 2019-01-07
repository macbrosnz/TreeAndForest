package com.safe.macbros.treeandforest.Activities.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tasks.adapters.TasksAdaptor;
import com.safe.macbros.treeandforest.Activities.tasks.dialog.CreateTask;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Equipment;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksMain extends Fragment implements View.OnClickListener {
    private static final String TAG = "TasksMain";

    //fire
    FirebaseFirestore db = MainActivity.getFireDb();

    //vars
    Methods meth;
    TailgateHelper tHelper;
    TasksAdaptor tAdaptor;
    HashMap<String, Object> message = new HashMap<>();

    //widgets
    RecyclerView taskRecycler;
    FloatingActionButton fab;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        meth = (Methods)container.getContext();
        tHelper = new TailgateHelper(container.getContext());
        View view = inflater.inflate(R.layout.tasks_main, container, false);

        initUI(view);

        initRecycler();

        return view;
    }


    public void initUI(View view) {

        fab = view.findViewById(R.id.fab_taskMain);
        fab.setOnClickListener(this);

        taskRecycler = view.findViewById(R.id.recycler_tasksMain);
        Toolbar toolbar = tHelper.newToolbar("Tasks", 0,null, null, getTAG(), message, getActivity());
    }


    public void initRecycler() {

        CollectionReference cr = db.collection("tasks");

        Query query = null;
        query = cr.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Tasks> options = new FirestoreRecyclerOptions.Builder<Tasks>()
                .setQuery(query, Tasks.class)
                .build();
        tAdaptor = new TasksAdaptor(options);
        taskRecycler.setAdapter(tAdaptor);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_taskMain: {

                meth.sendDialogMessage(new CreateTask(), CreateTask.getTAG(), message);


            }
            break;


        }
    }




    @Override
    public void onStart() {
        super.onStart();
        tAdaptor.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        tAdaptor.stopListening();
    }

    public static String getTAG() {
        return TAG;
    }
}


