package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.models.Visitor;

import java.util.HashMap;

public class VisitorsForm extends DialogFragment {
    private static final String TAG = "VisitorsForm";

    HashMap<String, Object> message = new HashMap<>();
    Tailgate tailgate;
    Methods meth;
    Switch mSwitch;
    FirebaseFirestore db = MainActivity.getFireDb();

//widgets
    EditText name, email, medical;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods)context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {

            meth.sendFragMessage(new TailgateMain(), TailgateMain.getTAG(), message);
            Toast.makeText(context, "Oops something went wrong", Toast.LENGTH_SHORT).show();

        }
        else{

            message = (HashMap<String, Object>)bundle.getSerializable(getTAG());
            tailgate = (Tailgate)message.get("tailgate");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.visitor_form, null);
        initUI(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Visitor Details")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateVisitor();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }

    public void initUI(View view){

         name = view.findViewById(R.id.visitorName);
         email = view.findViewById(R.id.visitorEmail);
         medical = view.findViewById(R.id.visitorMedical);

    }

    public void updateVisitor(){
        DocumentReference dr = db.collection("visitors").document();

        Visitor visitor = new Visitor();
        visitor.setId(dr.getId());
        visitor.setEmail(email.getText().toString());
        visitor.setName(name.getText().toString());
        visitor.setMedicalRequirements(medical.getText().toString());
        visitor.setTailgateId(tailgate.getId());
        dr.set(visitor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: visitor details saved");
            }
        });

    }

    public static String getTAG() {
        return TAG;
    }
}
