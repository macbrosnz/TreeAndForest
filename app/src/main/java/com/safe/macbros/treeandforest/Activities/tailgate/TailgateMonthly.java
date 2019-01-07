package com.safe.macbros.treeandforest.Activities.tailgate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.incidents.dialogs.NewIncAcc;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.HazardsAdaptor;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.staffMonthlyAdaptor;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.models.MonthlySafety;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.adapters.IncAdapter;
import com.safe.macbros.treeandforest.adapters.TrainingAdapter;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.dialog.CreateNewHazard;
import com.safe.macbros.treeandforest.dialog.NewTrainingDialog;
import com.safe.macbros.treeandforest.models.Hazards;
import com.safe.macbros.treeandforest.models.Incident;
import com.safe.macbros.treeandforest.models.Staff;
import com.safe.macbros.treeandforest.models.Training;

import java.util.ArrayList;
import java.util.HashMap;

public class TailgateMonthly extends Fragment implements View.OnClickListener {
    private static final String TAG = "TailgateMonthly";

    //widgets
    RecyclerView staffRecycler, hazardsRecycler, incidentRecycler, trainingRecycler, actionsRecycler;
    Toolbar mToolbar;
    TextView newTraining, newHazard, newAudit, newIncident;

    //var
    IncAdapter incidentAdapter;
    staffMonthlyAdaptor attendees;
    HazardsAdaptor hazardsAdapter;
    TrainingAdapter tAdapter;

    Methods meth;
    HashMap<String, Object> message;
    ArrayList<Staff> staffList = new ArrayList<>();
    MonthlySafety mMonthlySafety = new MonthlySafety();
    TailgateHelper tHelper;
    String siteId;
    //Firebase
    FirebaseFirestore db = MainActivity.getFireDb();

    @Override
    public void onStart() {
        super.onStart();
        attendees.startListening();
        hazardsAdapter.startListening();
        incidentAdapter.startListening();
        tAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        attendees.stopListening();
        hazardsAdapter.stopListening();
        incidentAdapter.stopListening();
        tAdapter.stopListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        tHelper = new TailgateHelper(context);

        Bundle bundle = this.getArguments();
        if (bundle == null) {

        } else {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            mMonthlySafety = (MonthlySafety) message.get("monthlySafety");
        }bundle.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mToolbar = tHelper.newToolbar("Health and Safety Meeting", 0, new TailgateMain(), TailgateMain.getTAG(), getTAG(), message, getActivity());
        MenuItem menuItem = mToolbar.getMenu().findItem(R.id.menu_ic_alert);
        tHelper.checkForAlerts(menuItem);
        View view = inflater.inflate(R.layout.monthly_tailgate, container, false);

        initUI(view);

        initAdapters();

        return view;

    }


    public void initUI(View view) {
        newIncident = view.findViewById(R.id.newIncident_mTailgate);
        newIncident.setOnClickListener(this);
        newTraining = view.findViewById(R.id.newTraining_mTailgate);
        newTraining.setOnClickListener(this);
        newHazard = view.findViewById(R.id.newHazard_mTailgate);
        newHazard.setOnClickListener(this);
        staffRecycler = view.findViewById(R.id.recycler_monthlyTailgate);
        hazardsRecycler = view.findViewById(R.id.hazRecycler_monthlyTailgate);
        incidentRecycler = view.findViewById(R.id.incidentRecycler_montlyTailgate);
        trainingRecycler = view.findViewById(R.id.trainRecycler_mTailgate);
        actionsRecycler = view.findViewById(R.id.completedRecycler_monthlyTailgate);
    }


    public void initAdapters() {

        CollectionReference cr = db.collection("safetyMonthly")
                .document(mMonthlySafety.getId()).collection("attendees");

        Query query = cr.orderBy("name");

        FirestoreRecyclerOptions<StaffTailgate> options = new FirestoreRecyclerOptions.Builder<StaffTailgate>()
                .setQuery(query, StaffTailgate.class)
                .build();

        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent: " + docs.size() + " monthlyId " + mMonthlySafety.getId());
                for (int i = 0; i < docs.size(); i++) {

                    Staff staff = docs.getDocuments().get(i).toObject(Staff.class);
                    staffList.add(staff);

                }
                message.put("staffList", staffList);
            }
        });

        attendees = new staffMonthlyAdaptor(options, mMonthlySafety);
        staffRecycler.setAdapter(attendees);
        staffRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //hazards
        cr = db.collection("sites").document(mMonthlySafety.getSiteId()).collection("hazards");
        Query queryHaz = cr.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Hazards> optionsHaz = new FirestoreRecyclerOptions.Builder<Hazards>()
                .setQuery(queryHaz, Hazards.class)
                .build();

        hazardsAdapter = new HazardsAdaptor(optionsHaz);
        hazardsRecycler.setAdapter(hazardsAdapter);
        hazardsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));


        //incidents
        cr = db.collection("incidents");
        Query queryInc = cr.orderBy("created", Query.Direction.DESCENDING).limit(5);

        FirestoreRecyclerOptions<Incident> optionsInc = new FirestoreRecyclerOptions.Builder<Incident>()
                .setQuery(queryInc, Incident.class)
                .build();

        incidentAdapter = new IncAdapter(optionsInc, message);
        incidentRecycler.setAdapter(incidentAdapter);
        incidentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        //training
        cr = db.collection("training");
        Query queryT = cr.whereEqualTo("completed", false);

        FirestoreRecyclerOptions<Training> optionsT = new FirestoreRecyclerOptions.Builder<Training>()
                .setQuery(queryT, Training.class)
                .build();

        tAdapter = new TrainingAdapter(optionsT);
        trainingRecycler.setAdapter(tAdapter);
        trainingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    public static String getTAG() {
        return TAG;
    }



    public void setTailgate(){

        DocumentReference dr = db.collection("tailgates").document(mMonthlySafety.getTailgateId());
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Tailgate t = documentSnapshot.toObject(Tailgate.class);
                Log.d(TAG, "onSuccess: siteId = " + t.getBlockId());
                siteId = t.getBlockId();
            }
        });}


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newTraining_mTailgate: {

                meth.sendDialogMessage(new NewTrainingDialog(), NewTrainingDialog.getTAG(), message);

            }
            break;
            case R.id.newHazard_mTailgate: {

                meth.sendDialogMessage(new CreateNewHazard(), CreateNewHazard.getTAG(), message);

            }
            break;
            case R.id.newIncident_mTailgate:{
                message.put("fragment", new TailgateMonthly());
                message.put("tag", getTAG());
                meth.sendDialogMessage(new NewIncAcc(), NewIncAcc.getTAG(), message);

            }
        }
    }
}


