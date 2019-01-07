package com.safe.macbros.treeandforest.Activities.tailgate.tabs;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.MonthlySafetyAdapter;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.MonthlySafetyStaff;
import com.safe.macbros.treeandforest.Activities.tailgate.models.MonthlySafety;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;


public class MonthlyMeetings extends Fragment implements View.OnClickListener {
    private static final String TAG = "MonthlyMeetings";

    //Class
    Methods meth;
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cr = db.collection("safetyMonthly");
    MonthlySafetyAdapter mAdapter;
    /* widgets */
    RecyclerView recyclerView;
    FloatingActionButton fab;

    //vars
    TailgateHelper tHelper;
    HashMap<String, Object> message = new HashMap<>();


    public MonthlyMeetings() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) getActivity();
        db.setFirestoreSettings(settings);
        createStaffList();
        createTailgate();



    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.tailgate_main, container, false);
        initUI(view);

        return view;
    }

    public void initUI(View view) {
        recyclerView = view.findViewById(R.id.tailgate_recyclerView);
        fab = view.findViewById(R.id.newTailgateFab_tailMain);
        fab.setOnClickListener(this);
        initRecycler();
    }

    private void initRecycler() {
        Log.d(TAG, "initRecView: started");
        Query query = null;
        query = cr.orderBy("mTimestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MonthlySafety> options = new FirestoreRecyclerOptions.Builder<MonthlySafety>()
                .setQuery(query, MonthlySafety.class)
                .build();


        mAdapter = new MonthlySafetyAdapter(options, message);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newTailgateFab_tailMain: {

                meth.sendDialogMessage(new MonthlySafetyStaff(), MonthlySafetyStaff.getTAG(), message);

            }break;
        }

    }

    public void createStaffList() {

        CollectionReference
                cr = db.collection("staff");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Staff> staffList = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Staff staff = doc.toObject(Staff.class);
                    staffList.add(staff);
                }
                message.put("staff", staffList);
            }
        });
    }

    public void createTailgate(){
        CollectionReference cr = db.collection("tailgates");
        Query query = cr.orderBy("timestamp", Query.Direction.DESCENDING).limit(1);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {

                    Tailgate t;
                    t = (Tailgate) queryDocumentSnapshots.getDocuments().get(i).toObject(Tailgate.class);
                    message.put("tailgate", t);

                }
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();


    }


    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
        Log.d(TAG, "onStop: " + String.valueOf(mAdapter.getItemCount()));
    }


    public static String getTAG() {
        return TAG;
    }
}
