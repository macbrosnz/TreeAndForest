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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.TailgateAdapter;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.SelectBlock;
import com.safe.macbros.treeandforest.models.Sites;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Equipment;
import com.safe.macbros.treeandforest.models.Staff;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class DailyMeetings extends Fragment implements View.OnClickListener {
    private static final String TAG = "DailyMeetings";

    //Class
    Methods meth;
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cr = db.collection("tailgates");
    private TailgateAdapter tailgateAdapter;
    /* widgets */
    RecyclerView recyclerView;
    FloatingActionButton fab;

    //vars
    TailgateHelper tHelper;
    HashMap<String, Object> message = new HashMap<>();


    public DailyMeetings() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tHelper = new TailgateHelper(context);
        meth = (Methods) getActivity();
        db.setFirestoreSettings(settings);
        // createTasksList();
        createStaffList();
        createEquipmentList();

    }

    @Override
    public void onStart() {
        super.onStart();


        tailgateAdapter.startListening();

        Log.d(TAG, "onStart: " + String.valueOf(tailgateAdapter.getItemCount()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tailgate_main, container, false);
        initUI(view);
        initRecycler();

        return view;
    }

    public void initUI(View view) {
        recyclerView = view.findViewById(R.id.tailgate_recyclerView);
        fab = view.findViewById(R.id.newTailgateFab_tailMain);
        fab.setOnClickListener(this);


    }

    private void initRecycler() {
        Log.d(TAG, "initRecView: started");
        Query tailgateQuery = null;
        tailgateQuery = cr.orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Tailgate> options = new FirestoreRecyclerOptions.Builder<Tailgate>()
                .setQuery(tailgateQuery, Tailgate.class)
                .build();

        tailgateAdapter = new TailgateAdapter(options, getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(tailgateAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                tailgateAdapter.deleteItem(viewHolder.getAdapterPosition());
               removeHit(tailgateAdapter.getTailgate().getBlockId(), tailgateAdapter.getTailgate().getTaskId());

            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newTailgateFab_tailMain: {
                meth.sendDialogMessage(new SelectBlock(), SelectBlock.getTAG(), message);
            }break;
        }

    }

    public void removeHit(String siteId, String taskId){


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference dr1 = db.collection("tasks").document(taskId);


        dr1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                Tasks t = doc.toObject(Tasks.class);
                int hits = t.getHits() -1;
                HashMap<String, Object> update = new HashMap<>();
                update.put("hits", hits);
                dr1.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: hits updated ");
                    }
                });
            }
        });

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


    public void createEquipmentList() {
        final ArrayList<Equipment> equipment = new ArrayList<>();
        CollectionReference
                cr = db.collection("equipment");
        Query query = null;
        query = cr.orderBy("name", Query.Direction.DESCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Equipment eq = doc.toObject(Equipment.class);
                    equipment.add(eq);
                }
                message.put("equipment", equipment);
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
        tailgateAdapter.stopListening();

        Log.d(TAG, "onStop: " + String.valueOf(tailgateAdapter.getItemCount()));
    }


    public static String getTAG() {
        return TAG;
    }
}
