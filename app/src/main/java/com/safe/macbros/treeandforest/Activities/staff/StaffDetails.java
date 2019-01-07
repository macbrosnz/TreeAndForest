package com.safe.macbros.treeandforest.Activities.staff;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.incidents.adapters.IncidentsRecyclerAdapter;
import com.safe.macbros.treeandforest.Activities.staff.adapters.StaffIncidentAdapter;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.SafetyHelper;
import com.safe.macbros.treeandforest.models.Incident;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.HashMap;

public class StaffDetails extends Fragment {
    private static final String TAG = "StaffDetails";

    //vars
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;
    Staff staff;
    SafetyHelper sHelper = new SafetyHelper();
    TailgateHelper tHelper;
    StaffIncidentAdapter incAdapter;
    int s =0;

    //fire
    FirebaseFirestore db = MainActivity.getFireDb();

    //widgets
    TextView title, subtitle, days, card1Title, card1Details, card2Title, card3Title;
    RecyclerView recycler_card2, recycler_card3;
    CardView card1, card2, card3;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tHelper = new TailgateHelper(context);
        meth = (Methods) context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            meth.sendFragMessage(new StaffMain(), StaffMain.getTAG(), message);
        } else {
            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            staff = (Staff) message.get("staff");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.universal_details, container, false);
        Toolbar toolbar = tHelper.newToolbar("Details", 0, new StaffMain(), StaffMain.getTAG(), getTAG(), message, getActivity());
        initUI(view);
        return view;
    }

    public void initUI(View view) {
        title = view.findViewById(R.id.title_ud);
        subtitle = view.findViewById(R.id.subtitle_ud);
        days = view.findViewById(R.id.sideText_ud);
        card1Title = view.findViewById(R.id.titleCard1_ud);
        card2Title = view.findViewById(R.id.titleCard2_ud);
        card3Title = view.findViewById(R.id.titleCard3_ud);
        card1 = view.findViewById(R.id.card1_ud);
        card2 = view.findViewById(R.id.card2_ud);
        card3 = view.findViewById(R.id.card3_ud);
        card1Details = view.findViewById(R.id.detailsCard1_ud);
        recycler_card2 = view.findViewById(R.id.recyclerCard2_ud);
        recycler_card3 = view.findViewById(R.id.recyclerCard3_ud);

        updateViews();
        initRecyclers();

    }

    public void updateViews(){

        sHelper.makeVisible(card2);
        sHelper.makeVisible(days);
        sHelper.makeVisible(recycler_card2);
        sHelper.makeVisible(card3);
        sHelper.makeVisible(recycler_card3);
        sHelper.makeVisible(subtitle);
        sHelper.makeVisible(card1Details);

        title.setText(staff.getName());
        subtitle.setText(staff.getMobile());

        card1Title.setText("Qualifications");

        card2Title.setText("Tallies");

        card3Title.setText("Incidents");

        howManyDays();
    }


    public void initRecyclers(){
        CollectionReference cr = db.collection("incidents");

        Query query = cr.whereEqualTo("staffId", staff.getId()).orderBy("created", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Incident> options = new FirestoreRecyclerOptions.Builder<Incident>()
                .setQuery(query, Incident.class)
                .build();

        incAdapter = new StaffIncidentAdapter(options);
        recycler_card3.setAdapter(incAdapter);
        recycler_card3.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public void howManyDays(){

        CollectionReference cr = db.collection("tailgates");
        Query query =cr.whereArrayContains("staffIds", staff.getId());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                s = docs.size();
                days.setText(String.valueOf(s));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        incAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        incAdapter.stopListening();
    }

    public static String getTAG() {
        return TAG;
    }
}























