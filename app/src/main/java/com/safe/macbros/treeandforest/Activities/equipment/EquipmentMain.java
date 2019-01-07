package com.safe.macbros.treeandforest.Activities.equipment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.safe.macbros.treeandforest.Activities.equipment.adapters.EquipmentAdapter;
import com.safe.macbros.treeandforest.Activities.equipment.dialog.NewEquipment;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.adapters.EquipAdapter;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.RecyclerItemClickListner;
import com.safe.macbros.treeandforest.models.Equipment;

import java.util.ArrayList;
import java.util.HashMap;

public class EquipmentMain extends Fragment implements View.OnClickListener {
    private static final String TAG = "EquipmentMain";

    //widgets
    private FloatingActionButton fab;
    private RecyclerView equipmentRecycler;
    //vars
    Methods meth;
    EquipAdapter equipmentAdapter;
    HashMap<String, Object> message = new HashMap<>();
    //fire
    FirebaseFirestore db = MainActivity.getFireDb();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;

    }

    @Override
    public void onStart() {
        super.onStart();
        equipmentAdapter.startListening();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.equipment_main, container, false);
        initUI(view);
        initRecycler();
        createToolbar(MainActivity.getToolbar(), "Equipment", true, null);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        equipmentAdapter.stopListening();
    }

    public void initUI(View view) {
        fab = view.findViewById(R.id.fab_equipmentmain);
        fab.setOnClickListener(this);
        equipmentRecycler = view.findViewById(R.id.recycler_equipmentmain);
    }


    public void initRecycler(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference cr = db.collection("equipment");

        Query query = cr.whereEqualTo("parentId", null).orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Equipment> options = new FirestoreRecyclerOptions.Builder<Equipment>()
                .setQuery(query, Equipment.class)
                .build();
        equipmentAdapter=new EquipAdapter(options);

        equipmentRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        equipmentRecycler.setAdapter(equipmentAdapter);
        equipmentRecycler.addOnItemTouchListener(new RecyclerItemClickListner(getContext(), equipmentRecycler, new RecyclerItemClickListner.OnTouchActionListener() {
            @Override
            public void onLeftSwipe(View view, int position) {

            }

            @Override
            public void onRightSwipe(View view, int position) {

            }

            @Override
            public void onClick(View view, int position) {

            }
        }));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_equipmentmain: {
                message.put("context", getContext());
                meth.sendDialogMessage(new NewEquipment(), NewEquipment.getTAG(), message);
            }
            break;

        }
    }
    public Toolbar createToolbar(Toolbar toolbar, String title, Boolean main, final Fragment fragment) {
        toolbar.getMenu().clear();
        toolbar.setTitle(title);
        if(main)
        {toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getDrawerLayout().openDrawer(GravityCompat.START);
            }
        });}
        else{toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    meth.sendFragMessage(fragment, fragment.getTag(), message);
                }
            });}
        return toolbar;
    }

    public void createEquipmentList() {
        final ArrayList<Equipment> equipment = new ArrayList<>();
        CollectionReference
                cr = db.collection("equipment");
        Query query = null;
        query = cr.orderBy("name", Query.Direction.DESCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Equipment eq = doc.toObject(Equipment.class);
                    equipment.add(eq);
                }
                message.put("equipment", equipment);
            }
        });

    }

    public static String getTAG() {
        return TAG;
    }
}

