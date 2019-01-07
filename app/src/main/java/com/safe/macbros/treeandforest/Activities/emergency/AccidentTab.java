package com.safe.macbros.treeandforest.Activities.emergency;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Sites;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.util.HashMap;

public class AccidentTab extends Fragment {
    private static final String TAG = "AccidentTab";

    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    TailgateHelper tHelper;
    FirebaseFirestore db = MainActivity.getFireDb();
    //widgets
    TextView radio, title3, title4, title5, site, road, gps, type;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        tHelper = new TailgateHelper(context);
        Bundle bundle = this.getArguments();
        if (bundle == null) {

        } else {

        }


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emergency_procedure, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view) {

        radio = view.findViewById(R.id.radiochannel);
        title3 = view.findViewById(R.id.title3);
        title4 = view.findViewById(R.id.title4);
        title5 = view.findViewById(R.id.title5);
        site = view.findViewById(R.id.sitename);
        road = view.findViewById(R.id.road);
        gps = view.findViewById(R.id.gps);
        type = view.findViewById(R.id.type);

        //widgetsloadedwithdata
        loadViews();
    }

    private void loadViews() {

        CollectionReference cr = db.collection("tailgates");

        Query query = cr.orderBy("timestamp", Query.Direction.DESCENDING).limit(1);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (docs != null&&docs.size()>0) {
                    Tailgate t = docs.getDocuments().get(0).toObject(Tailgate.class);
                    if (t.getBlockId() != null)
                        fireBlock(t.getBlockId());
                }

            }
        });

        //type
        type.setText("Accident");
    }

    private void fireBlock(String id) {

        DocumentReference dr = db.collection("sites").document(id);

        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot doc, @javax.annotation.Nullable FirebaseFirestoreException e) {

                Sites sites = doc.toObject(Sites.class);
                if (sites.getRadio() != null) {
                    radio.setText(sites.getRadio());
                }
                if (sites.getAccess() != null) {
                    road.setText(sites.getAccess());
                }
                if (sites.getGps() != null) {
                    gps.setText(sites.getGps());
                }
                if (sites.getName() != null) {
                    site.setText(sites.getName());
                }
            }
        });

    }

    public static String getTAG() {
        return TAG;
    }
}

























