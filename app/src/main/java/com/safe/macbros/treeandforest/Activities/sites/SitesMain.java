package com.safe.macbros.treeandforest.Activities.sites;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.sites.adapters.SitesAdapter;
import com.safe.macbros.treeandforest.Activities.sites.dialog.CreateSite;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Hazards;
import com.safe.macbros.treeandforest.models.Sites;

import java.util.ArrayList;
import java.util.HashMap;


public class SitesMain extends Fragment implements View.OnClickListener {

    private static final String TAG = "SitesMain";

    FloatingActionButton fab;
    RecyclerView recycler;
    SitesAdapter sitesAdapter;

    //vars
    Methods meth;
    ArrayList<Hazards> hazardsList = new ArrayList<>();
    HashMap<String, Object> message = new HashMap<>();
    TailgateHelper tHelper;
    FirebaseFirestore db = MainActivity.getFireDb();
    CollectionReference cr;


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        tHelper = new TailgateHelper(context);
        meth = (Methods) context;
        createHazardsList();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = tHelper.newToolbar("Sites", 0, null, null, getTAG()
                , message, getActivity());
        View view = inflater.inflate(R.layout.blocks_main, container, false);
        initUI(view);
        return view;


    }


    public void initUI(View view) {

        fab = view.findViewById(R.id.fab_sitesMain);
        fab.setOnClickListener(this);
        recycler = view.findViewById(R.id.recycler_sitesMain);
    initAdapter();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab_sitesMain: {

                meth.sendDialogMessage(new CreateSite(), CreateSite.getTAG(), message);

            }
            break;

        }

    }

    public void initAdapter(){
        cr = db.collection("sites");
        Query query = cr.orderBy("name");

        FirestoreRecyclerOptions<Sites> options = new FirestoreRecyclerOptions.Builder<Sites>()
                .setQuery(query, Sites.class)
                .build();
        sitesAdapter = new SitesAdapter(options);
        recycler.setAdapter(sitesAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    public void createHazardsList() {
        cr = db.collection("hazards");
        cr.orderBy("title", Query.Direction.ASCENDING);
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (int i = 0; i < docs.size(); i++) {
                    hazardsList.add(docs.getDocuments().get(i).toObject(Hazards.class));
                }
                message.put("hazardsList", hazardsList);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        sitesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        sitesAdapter.stopListening();
    }

    public static String getTAG() {
        return TAG;
    }
}
