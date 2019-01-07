package com.safe.macbros.treeandforest.Activities.sites.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.sites.SitesMain;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Hazards;
import com.safe.macbros.treeandforest.models.Sites;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateSiteHazards extends DialogFragment {
    private static final String TAG = "CreateSiteHazards";

    //vars
    Methods meth;
    ArrayList<Hazards> hazardsList = new ArrayList<>();
    ArrayList<Hazards> newHList = new ArrayList<>();
    HashMap<String, Object> message = new HashMap<>();
    TailgateHelper tHelper;
    FirebaseFirestore db = MainActivity.getFireDb();
    CollectionReference cr;
    Sites site = new Sites();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods)context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {

            meth.sendFragMessage(new SitesMain(), SitesMain.getTAG(), message);

        } else {

            message = (HashMap<String,Object>)bundle.getSerializable(getTAG());
            hazardsList = (ArrayList<Hazards>)message.get("hazardsList");
            site = (Sites)message.get("newSite");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Site Hazards")
                .setMultiChoiceItems(listtoStringArray(), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {

                            newHList.add(hazardsList.get(which));

                        } else {

                            newHList.remove(newHList.indexOf(hazardsList.get(which)));

                        }
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveToFire();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();



    }

    public String[] listtoStringArray() {



        String[] s;
        if (hazardsList.size() > 0) ;
        {
            s = new String[hazardsList.size()];

            for (int i = 0; i < hazardsList.size(); i++) {

                s[i] = hazardsList.get(i).getTitle();

            }
        }

        return s;
    }

    public static String getTAG() {
        return TAG;

    }

    public void saveToFire(){

      final DocumentReference dr =  db.collection("sites").document();

      site.setId(dr.getId());
      site.setCreated(Timestamp.now());

dr.set(site).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void aVoid) {

        for (int i = 0; i < newHList.size(); i++) {
            DocumentReference dr2 = db.collection("sites").document(dr.getId())
                    .collection("hazards").document(newHList.get(i).getId());

            dr2.set(newHList.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
    }
});



    }
}
