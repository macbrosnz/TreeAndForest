package com.safe.macbros.treeandforest.Activities.incidents;

import android.content.Context;
import android.os.Bundle;
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
import com.safe.macbros.treeandforest.Activities.incidents.adapters.IncidentsRecyclerAdapter;
import com.safe.macbros.treeandforest.Activities.incidents.dialogs.NewIncAcc;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Incident;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;


public class IncidentsMain extends Fragment implements View.OnClickListener {
    private static final String TAG = "IncidentsMain";
    //widgets
    FloatingActionButton fab;
    IncidentsRecyclerAdapter incidentsAdapter;
    RecyclerView recycler;


    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    TailgateHelper tHelper;
    FirebaseFirestore db = MainActivity.getFireDb();
    CollectionReference cr;


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        tHelper = new TailgateHelper(context);
        meth = (Methods) context;




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.incidents_main, container, false);
        Toolbar toolbar = tHelper.newToolbar("Near Miss/Incidents", 0, null, null, getTAG()
                , message, getActivity());
        initUI(view);
        initRecycler();
        return view;
    }


    public void initUI(View view) {

        fab = view.findViewById(R.id.fab_incMain);
        fab.setOnClickListener(this);
        recycler = view.findViewById(R.id.recycler_incMain);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_incMain: {
                Log.d(TAG, "onClick: ");
                message.put("fragment", new IncidentsMain());
                message.put("tag", getTAG());
                meth.sendDialogMessage(new NewIncAcc(), NewIncAcc.getTAG(), message);

            }
            break;

        }
    }

    public void initRecycler() {

        cr = db.collection("incidents");

        Query query = cr.orderBy("created", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Incident>()
                .setQuery(query, Incident.class)
                .build();

        incidentsAdapter = new IncidentsRecyclerAdapter(options);
        recycler.setAdapter(incidentsAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @Override
    public void onStart() {
        super.onStart();
        incidentsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        incidentsAdapter.stopListening();
    }

    public static String getTAG() {
        return TAG;
    }


}
