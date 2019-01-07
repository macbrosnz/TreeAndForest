package com.safe.macbros.treeandforest.custom;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.models.Alert;

import java.util.Calendar;
import java.util.Date;

public class SafetyHelper {
    private static final String TAG = "SafetyHelper";
    FirebaseFirestore db = MainActivity.getFireDb();


    public void makeVisible(View view) {

        view.setVisibility(View.VISIBLE);

    }

    public void coloredHazardsText(TextView textView, int i) {
        String risk = String.valueOf(i);
        if (i < 4) {
            textView.setTextColor(Color.parseColor("#00B300"));
            textView.setText(risk);
        } else if (i >= 4 && i <= 6) {
            textView.setText(risk);
            textView.setTextColor(Color.parseColor("#fdff00"));
        } else if (i > 6 && i <= 12) {
            textView.setText(risk);
            textView.setTextColor(Color.parseColor("#ff8d00"));
        } else if (i > 12) {
            textView.setText(risk);
            textView.setTextColor(Color.parseColor("#ff0000"));
        }

    }

    public void createAlert(String staffId, String tailgateId, String sMeetingId, String equipId,
                            String vehicleId, String authorId, String trainingId, String title,
                            String details, int days) {
        DocumentReference dr = db.collection("alerts").document();

        Alert alert = new Alert();
        alert.setId(dr.getId());
        if (staffId != null) {
            alert.setStaffId(staffId);
        }
        if (tailgateId != null) {
            alert.setTailgateId(tailgateId);
        }
        if (sMeetingId != null) {
            alert.setSafetyMeetingId(sMeetingId);
        }
        if (equipId != null) {
            alert.setEquipmentId(equipId);
        }
        if (vehicleId != null) {
            alert.setVehicleId(vehicleId);
        }
        if (authorId != null) {
            alert.setAuthorId(authorId);
        }
        if (trainingId != null) {
            alert.setTrainingId(trainingId);
        }
        alert.setDetails(details);
        alert.setDate(addDaysDate(days));
        alert.setTitle(title);

        dr.set(alert).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: ");
            }
        });

    }

    public Date addDaysDate(int i) {
        Timestamp timestamp = Timestamp.now();
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp.toDate());
        cal.add(Calendar.DATE, i);
        return cal.getTime();

    }

    public void completeFromAlert() {

    }

    public void deleteAlert() {

    }



    public static String getTAG() {
        return TAG;
    }
}
