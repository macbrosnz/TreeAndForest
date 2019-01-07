package com.safe.macbros.treeandforest.Activities.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tasks.adapters.EqAdapterTasks;
import com.safe.macbros.treeandforest.Activities.tasks.adapters.PPEAdaptorTasks;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Equipment;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.HashMap;

public class TasksDetail extends Fragment {
    private static final String TAG = "TasksDetail";

    //widgets
    RecyclerView ppe, equipment;
    TextView description, quals;
TextView title, days;

    //vars
    Methods meth;
    TailgateHelper tHelper;
    HashMap<String,Object> message = new HashMap<>();
    Tasks mTasks = new Tasks();
    PPEAdaptorTasks mPPEAdapter;
    EqAdapterTasks mEquipAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        tHelper = new TailgateHelper(context);
        meth = (Methods)context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {

            meth.sendFragMessage(new TasksMain(), TasksMain.getTAG(), message);

        }
        else{

            message = (HashMap<String, Object>)bundle.getSerializable(getTAG());

            mTasks = (Tasks)message.get("task");

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = tHelper.newToolbar("Task Details", 0, new TasksMain(), TasksMain.getTAG(), getTAG()
        , message, getActivity());
        View view = inflater.inflate(R.layout.tasks_detail, container, false);

    initUI(view);

        return view;
    }

    public void initUI(View view){
        title = view.findViewById(R.id.title_taskDetail);
        title.setText(mTasks.getName());
        days = view.findViewById(R.id.days_tasksDetail);
        days.setText(String.valueOf(mTasks.getHits()));
        description = view.findViewById(R.id.desc_tasksDetail);
        description.setText(mTasks.getDescription());
        quals = view.findViewById(R.id.quals_tasksDetail);
        quals.setText(String.valueOf(mTasks.getQualsList()));
        ppe = view.findViewById(R.id.ppeRec_tasksDetail);
        equipment = view.findViewById(R.id.eqRec_tasksDetail);

        initAdapters();
    }

    public void initAdapters(){

        FirebaseFirestore db = MainActivity.getFireDb();
        CollectionReference cr1 = db.collection("tasks").document(mTasks.getId()).collection("equipment");

        Query query = cr1.orderBy("name");

        FirestoreRecyclerOptions<Equipment> options = new FirestoreRecyclerOptions.Builder<Equipment>()
                .setQuery(query, Equipment.class)
                .build();

        mEquipAdapter = new EqAdapterTasks(options);

        equipment.setAdapter(mEquipAdapter);

        equipment.setLayoutManager(new LinearLayoutManager(this.getContext()));

        CollectionReference cr2 = db.collection("tasks").document(mTasks.getId()).collection("ppe");

        Query q2 = cr2.orderBy("name");


        FirestoreRecyclerOptions<Equipment> options2 = new FirestoreRecyclerOptions.Builder<Equipment>()
                .setQuery(q2, Equipment.class)
                .build();

        mPPEAdapter = new PPEAdaptorTasks(options2);

        ppe.setAdapter(mPPEAdapter);

        ppe.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }


    @Override
    public void onStart() {
        super.onStart();

        mPPEAdapter.startListening();
        mEquipAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();

        mPPEAdapter.stopListening();
        mEquipAdapter.stopListening();
    }

    public static String getTAG() {
        return TAG;
    }
}






























