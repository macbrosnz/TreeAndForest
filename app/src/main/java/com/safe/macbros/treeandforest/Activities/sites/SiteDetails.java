package com.safe.macbros.treeandforest.Activities.sites;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.HazardsAdaptor;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.SafetyHelper;
import com.safe.macbros.treeandforest.models.Hazards;
import com.safe.macbros.treeandforest.models.Sites;

import java.util.HashMap;

public class SiteDetails extends Fragment {
    private static final String TAG = "SiteDetails";
    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    SafetyHelper sHelper = new SafetyHelper();
    TailgateHelper tHelper;
    Sites mSites = new Sites();
    int staff = 0, days =0;
    HazardsAdaptor hAdapter;



    //widgets
    TextView title, details, gps, forest, mandays, card1Title;
    ImageView image;
    CardView card1;
    RecyclerView mRecyclerView;

    //fire
    FirebaseFirestore db = MainActivity.getFireDb();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        tHelper = new TailgateHelper(context);
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            meth.sendFragMessage(new SitesMain(), SitesMain.getTAG(), message);
        } else {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            mSites = (Sites) message.get("site");
            Log.d(TAG, "onAttach: Sites offlinepdf = " + mSites.getOnlinePdfPath());
        }
        bundle.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = tHelper.newToolbar("Site Details", 0, new SitesMain(), SitesMain.getTAG(), getTAG()
                , message, getActivity());

        View view = inflater.inflate(R.layout.universal_details, container, false);
        initUI(view);
        return view;

    }

    public void initUI(View view) {

        image = view.findViewById(R.id.image_ud);
        title = view.findViewById(R.id.title_ud);
        gps = view.findViewById(R.id.subtitle_ud);
        mandays = view.findViewById(R.id.sideText_ud);
        card1 = view.findViewById(R.id.card1_ud);
        card1Title = view.findViewById(R.id.titleCard1_ud);
        details = view.findViewById(R.id.detailsCard1_ud);
        mRecyclerView = view.findViewById(R.id.recyclerCard1_ud);
        updateViews();
        calculateMandays();
        initRecycler();
    }

    public void updateViews() {

        title.setText(mSites.getName());
        sHelper.makeVisible(card1Title);
        card1Title.setText("Hazards");
        sHelper.makeVisible(mRecyclerView);

        if (mSites.getOfflinePdfPath() != null) {
            image.setImageResource(R.drawable.map_small);
            sHelper.makeVisible(image);
        }

        if (mSites.getGps() != null) {
            sHelper.makeVisible(gps);
            gps.setText(mSites.getGps());
        }

        if (mSites.getMandays() > 0)
            sHelper.makeVisible(mandays);

        if (mSites.getDetails() != null)
            sHelper.makeVisible(details);
    }

    public void initRecycler(){

        CollectionReference cr = db.collection("sites").document(mSites.getId()).collection("hazards");
        Query query = cr.orderBy("title");

        FirestoreRecyclerOptions<Hazards> options = new FirestoreRecyclerOptions.Builder<Hazards>()
                .setQuery(query, Hazards.class)
                .build();

        hAdapter = new HazardsAdaptor(options);

        mRecyclerView.setAdapter(hAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onStart() {
        super.onStart();
        hAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        hAdapter.stopListening();
    }

    public void calculateMandays() {

        CollectionReference cr = db.collection("tailgates");

        Query query = cr.whereEqualTo("blockId", mSites.getId());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                days = docs.size();

                for (int i = 0; i < docs.size(); i++) {
                    staff += docs.getDocuments().get(i).toObject(Tailgate.class).getStaff().size();
                }
                if(staff>0&&days>0)
                mandays.setText(String.valueOf(staff)+"/"+String.valueOf(days));

            }
        });


    }

    public static String getTAG() {
        return TAG;
    }
}
