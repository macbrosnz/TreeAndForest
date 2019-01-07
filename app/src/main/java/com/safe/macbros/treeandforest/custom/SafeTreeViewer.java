package com.safe.macbros.treeandforest.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.models.Sites;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.util.HashMap;

public class SafeTreeViewer extends Fragment {
    private static final String TAG = "SafeTreeViewer";

    //vars
    Tailgate mTailgate;
    Sites mSites;
    HashMap<String, Object> message = new HashMap<>();
    TailgateHelper mThelper;

    //widgets
    PDFView mPDFView;
    ProgressBar mProgressBar;
    TextView mTextView;

    //firestore
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = MainActivity.getFireDb();
    DocumentReference dr;



    @Override
    public void onAttach(Context context) {



        super.onAttach(context);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());

            mTailgate = (Tailgate) message.get("tailgate");

            bundle.clear();
        }
        mThelper = new TailgateHelper(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pdf_viewer, container, false);

        mThelper.newToolbar( "Site Details",0,new TailgateDetails(), TailgateDetails.getTAG(),
                getTAG(), message, getActivity());


        initUI(view);

        initBlock(mTailgate.getBlockId());

        Log.d(TAG, "onCreateView: " + mTailgate.getBlockOfflineUrl());


        return view;
    }

    public void initUI(View view) {

        mPDFView = view.findViewById(R.id.pdf_view);
        mProgressBar = view.findViewById(R.id.progressBar_pdf);
        mTextView = view.findViewById(R.id.downloading_pdf);

    }


    public void initBlock(String blockId) {

        dr = db.collection("sites").document(blockId);

        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot doc, @javax.annotation.Nullable FirebaseFirestoreException e) {

                Sites sites;

                sites = doc.toObject(Sites.class);

                pdfSetBlockUpdate(sites.getOnlinePdfPath());

            }
        });

    }

    public void pdfSetBlockUpdate(String onlinePath){

        mThelper.pdfDownloadUpdateView(mProgressBar, mTextView, onlinePath, mPDFView, dr);

    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(getActivity(), new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    public static String getTAG() {
        return TAG;
    }
}
