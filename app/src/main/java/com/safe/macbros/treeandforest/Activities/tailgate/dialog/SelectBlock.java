package com.safe.macbros.treeandforest.Activities.tailgate.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.BlockAdapter;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.RecyclerItemClickListner;
import com.safe.macbros.treeandforest.models.Sites;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectBlock extends DialogFragment {
    private static final String TAG = "SelectBlock";
    //firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cr = db.collection("sites");
    private BlockAdapter blockAdapter;
    //widgets
    RecyclerView blockRecycler;
    RecyclerItemClickListner clickListner;
    //classvar
    Tailgate newTailgate = new Tailgate();
    Methods meth;
    //vars
    HashMap<String, Object> message = new HashMap<>();

    @Override
    public void onStart() {
        super.onStart();
        blockAdapter.startListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            bundle.clear();
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View recyclerView = inflater.inflate(R.layout.recyclerview, null);
        initUI(recyclerView);
        initRecycler();
        builder.setTitle("Where are you working today?")
                .setView(recyclerView)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    public void initUI(View v) {
        blockRecycler = v.findViewById(R.id.recyclerview);
    }

    private void initRecycler() {
        Log.d(TAG, "initRecycler: started");
        Query query = cr.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Sites> options = new FirestoreRecyclerOptions.Builder<Sites>()
                .setQuery(query, Sites.class)
                .build();

        blockAdapter = new BlockAdapter(options, message, getContext());
        Log.d(TAG, "initRecycler: postAdapter");

        blockRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        blockRecycler.setAdapter(blockAdapter);
        Log.d(TAG, "initRecycler: end");
        blockRecycler.addOnItemTouchListener(new RecyclerItemClickListner(getContext(), blockRecycler,
                new RecyclerItemClickListner.OnTouchActionListener() {
                    @Override
                    public void onLeftSwipe(View view, int position) {

                    }

                    @Override
                    public void onRightSwipe(View view, int position) {

                    }

                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(getActivity(), blockAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
                        sendTailgate(blockAdapter.getItem(position).getName(), blockAdapter.getItem(position).getId()
                                , blockAdapter.getItem(position).getHazards(), blockAdapter.getItem(position).getOnlinePdfPath()
                                , blockAdapter.getItem(position).getOfflinePdfPath());
                        dismiss();
                    }
                }));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //this will Delete info
    }

    @Override
    public void onStop() {
        super.onStop();
        blockAdapter.stopListening();

    }

    public void sendTailgate(String blockName, String blockId, ArrayList<String> hazards, String blockOnline, String blockOffline) {
        Tailgate newTailgate = new Tailgate();
        newTailgate.setBlockName(blockName);
        newTailgate.setBlockId(blockId);
        newTailgate.setBlockOnlineUrl(blockOnline);
        newTailgate.setBlockOfflineUrl(blockOffline);

        message.put("tailgate", newTailgate);

        meth.sendDialogMessage(new SelectTask(), SelectTask.getTAG(), message);
    }

    public static String getTAG() {
        return TAG;
    }

}

