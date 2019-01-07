package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateEquipment;
import com.safe.macbros.treeandforest.Activities.tailgate.models.EquipmentTailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.dialog.AlarmDialog;
import com.safe.macbros.treeandforest.models.Alert;
import com.safe.macbros.treeandforest.models.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StaffNote extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private static final String TAG = "StaffNotes";
    //WIDGETS
    EditText mNote;
    ImageView mAlert;
    static TextView mDate;

    //firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();

    //vars
    Methods meth;
    Context context;
    String mDateString;
    static Date staticDate = null;
    EquipmentTailgate equipmentDetails = new EquipmentTailgate();
    HashMap<String,Object> message = new HashMap<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
        Bundle bundle = this.getArguments();
        meth = (Methods)context;
        db.setFirestoreSettings(settings);

        if (bundle != null) {
            message = (HashMap<String, Object>)bundle.getSerializable(getTAG());
            equipmentDetails = (EquipmentTailgate)message.get("equipmentDetails");
            bundle.clear();
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        staticDate = null;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.staffnotes_tailgate, null);
        initUI(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(equipmentDetails.getName() + " Note")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mNote.getText().toString().length()>0||staticDate!=null)
                        fireCreateAlert();
                        else
                            Toast.makeText(context, "Nothing Saved note was Empty", Toast.LENGTH_SHORT).show();
                        dismiss();

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();

    }
    public void initUI(View view){
        mNote= view.findViewById(R.id.note_staffnotes);
        mAlert = view.findViewById(R.id.alertIcon_staffnotes);
        mDate = view.findViewById(R.id.date_staffnotes);
        mDate.setText(mDateString);

        if(mDate.getText().toString().length()>0)
            mAlert.setColorFilter(R.color.colorAccent);

        mAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.put("equipmentDetails", equipmentDetails);
                meth.sendDialogMessage(new AlarmDialog(),AlarmDialog.getTAG(),message);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mDateString = "Reminder added for: " + dayOfMonth +"/"+ month + "/" +year + "\n\n";

        Log.d(TAG, "onDateSet: " + mDateString);
       // mDate.setText("timmy");
    }

    public static String getTAG() {
        return TAG;
    }

    public TextView getDate() {
        return mDate;
    }

    public void fireCreateAlert(){
        final DocumentReference dr = db.collection("alerts").document();
        Log.d(TAG, "fireCreateAlert: " + dr.getId());
        final DocumentReference noteRef = db.collection("notes").document();

        if(staticDate==null) {
            Note note = new Note();

            note.setId(noteRef.getId());
            note.setTitle(equipmentDetails.getName());
            note.setStaffId(equipmentDetails.getStaffId());
            note.setTailgateId(equipmentDetails.getTailgateId());
            note.setEquipmentId(equipmentDetails.getId());
            note.setComplete(false);
            note.setTimestamp(Timestamp.now());
            fireUpdateNote(noteRef.getId());

           noteRef.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(context, "Note Set for " + staticDate + "\n\n", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: Note added");

                }
            });


        }
        else{

        Alert newAlert = new Alert();

        newAlert.setId(dr.getId());

        newAlert.setTitle(equipmentDetails.getName());

        newAlert.setTailgateId(equipmentDetails.getTailgateId());

        newAlert.setDetails(mNote.getText().toString());

        newAlert.setStaffId(equipmentDetails.getStaffId());

        newAlert.setEquipmentId(equipmentDetails.getId());
        newAlert.setDate(staticDate);
        newAlert.setComplete(false);
        newAlert.setTimestamp(Timestamp.now());
        fireUpdateAlert(dr.getId());

        dr.set(newAlert).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(context, "Alert Set for " + staticDate + "\n\n", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onComplete: alert added");

            }
        });}



    }

    public void fireUpdateAlert(final String alertId){



        DocumentReference dr = db.collection("tailgates").document(equipmentDetails.getTailgateId())
                .collection("staffChecks").document(equipmentDetails.getStaffId())
                .collection("equipment").document(equipmentDetails.getId());

        Map<String,Object> map = new HashMap<>();

        map.put("note", mNote.getText().toString());

        map.put("alertId", alertId);

        map.put("alertDate", getStaticDate());

        dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Log.d(TAG, "onComplete: equipment Updated" + alertId);
            }
        });



    }

    public void fireUpdateNote(final String noteId){


        DocumentReference dr = db.collection("tailgates").document(equipmentDetails.getTailgateId())
                .collection("staffChecks").document(equipmentDetails.getStaffId())
                .collection("equipment").document(equipmentDetails.getId());

        Map<String,Object> map = new HashMap<>();

        map.put("note", mNote.getText().toString());

        map.put("noteId", noteId);


        dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Log.d(TAG, "onComplete: equipment Updated" + noteId);

            }
        });



    }

    public void setDate(TextView date) {
        mDate = date;
    }

    public static Date getStaticDate() {
        return staticDate;
    }

    public void setStaticDate(Date staticDate) {
        StaffNote.staticDate = staticDate;
    }
}












































