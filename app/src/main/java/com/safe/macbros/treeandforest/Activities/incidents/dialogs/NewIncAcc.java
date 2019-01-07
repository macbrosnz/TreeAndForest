package com.safe.macbros.treeandforest.Activities.incidents.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.ArrayList;
import java.util.HashMap;

public class NewIncAcc extends DialogFragment {

    private static final String TAG = "NewIncAcc";

    //vars
    FirebaseFirestore db = MainActivity.getFireDb();
    CollectionReference cr;
    Methods meth;
    HashMap<String,Object> message= new HashMap<>();

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        meth = (Methods)context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            Log.d(TAG, "onAttach: Failed");
        }
        else {
            message = (HashMap<String, Object>)bundle.getSerializable(getTAG());
        }
        cr = db.collection("staff");
        cr.orderBy("name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                ArrayList<Staff> staffList = new ArrayList<>();
                for (DocumentSnapshot doc :
                        queryDocumentSnapshots) {
                    Staff staff = doc.toObject(Staff.class);
                    staffList.add(staff);

                }
                message.put("staffList", staffList);
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items =new  String[2];
        items[0] = "Near Miss";
        items[1] = "Incident";

        builder.setTitle("Please Choose")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            message.put("type", "Incident");
                            meth.sendFragMessage(new NewIncident(), NewIncident.getTAG(), message);
                        }
                        else{
                            message.put("type", "Accident");
                            meth.sendFragMessage(new NewIncident(), NewIncident.getTAG(), message);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

       return  builder.create();
    }

    public static String getTAG() {
        return TAG;
    }
}
