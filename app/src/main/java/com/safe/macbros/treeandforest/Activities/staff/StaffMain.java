package com.safe.macbros.treeandforest.Activities.staff;


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
import android.widget.ImageView;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.safe.macbros.treeandforest.Activities.staff.adapters.StaffAdapterS;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.HashMap;


public class StaffMain extends Fragment {
    private static final String TAG = "StaffMain";

    //widgets
    RecyclerView recycler;
    ImageView image;
    TextView title;
    FloatingActionButton fab;

    //fire
    FirebaseFirestore db = MainActivity.getFireDb();

    //vars
    TailgateHelper tHelper;
    HashMap<String,Object> message = new HashMap<>();
    StaffAdapterS sAdaptor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tHelper = new TailgateHelper(container.getContext());
        Toolbar toolbar = tHelper.newToolbar("Staff", 0, null, null, getTAG(), message, getActivity());
        View view = inflater.inflate(R.layout.universal_main, container, false);
        initUI(view);
        return view;
    }

    public void initUI(View view){

        fab=view.findViewById(R.id.fab_um);
        recycler=view.findViewById(R.id.recycler_um);
        title=view.findViewById(R.id.title_um);
        image=view.findViewById(R.id.image_ud);
    updateViews();
    initRecycler();
    }

    public void updateViews(){
        title.setText("Staff");

    }

    public void initRecycler(){
        CollectionReference cr = db.collection("staff");
        Query query = cr.orderBy("name");

        FirestoreRecyclerOptions<Staff> options = new FirestoreRecyclerOptions.Builder<Staff>()
                .setQuery(query, Staff.class)
                .build();

        sAdaptor = new StaffAdapterS(options);
        recycler.setAdapter(sAdaptor);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    @Override
    public void onStart() {
        super.onStart();
        sAdaptor.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        sAdaptor.stopListening();
    }

    public static String getTAG() {
        return TAG;
    }
}
