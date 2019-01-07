package com.safe.macbros.treeandforest.Activities.hazards;

import android.content.Context;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.safe.macbros.treeandforest.Activities.hazards.adapters.HazardAdapter;
import com.safe.macbros.treeandforest.dialog.CreateNewHazard;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Hazards;

import java.util.HashMap;

public class HazardsMain extends Fragment implements View.OnClickListener {

    private static final String TAG = "HazardsMain";

    //widgets
    FloatingActionButton fab;
    RecyclerView hazardRecycler;

    //firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //vars
    HazardAdapter mHazardsAdapter;
    TailgateHelper tHelper;
    HashMap<String,Object> message = new HashMap<>();
    Methods meth;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods)context;
        tHelper= new TailgateHelper(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = tHelper.newToolbar("Hazards", 0, null, null, getTAG(), message, getActivity());

        MenuItem mItem = toolbar.getMenu().findItem(R.id.menu_ic_alert);

        tHelper.checkForAlerts(mItem);

        View view = inflater.inflate(R.layout.hazards_main, container, false);

        initUI(view);

        initAdapter();

        return view;
    }


    public void initUI(View view) {


        fab = view.findViewById(R.id.fab_hazardMain);
        hazardRecycler = view.findViewById(R.id.recycler_hazMain);

        fab.setOnClickListener(this);
    }


    public void initAdapter() {

        CollectionReference cr = db.collection("hazards");
        Query query = null;

        query = cr.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Hazards> options = new FirestoreRecyclerOptions.Builder<Hazards>()
                .setQuery(query, Hazards.class)
                .build();


        mHazardsAdapter = new HazardAdapter(options);

        hazardRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        hazardRecycler.setAdapter(mHazardsAdapter);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_hazardMain:

            meth.sendDialogMessage(new CreateNewHazard(), CreateNewHazard.getTAG(), message);
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        mHazardsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mHazardsAdapter.stopListening();
    }

    public static String getTAG() {
        return TAG;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode);

    }
}
