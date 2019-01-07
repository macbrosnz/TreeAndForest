package com.safe.macbros.treeandforest.Activities.tailgate;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.StaffCheck;
import com.safe.macbros.treeandforest.Activities.tailgate.models.MonthlySafety;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.ScreenshotUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class TailgateSign extends Fragment implements View.OnClickListener {
    public static String getTAG() {
        return TAG;
    }

    private static final String TAG = "TailgateSign";
    //widgets
    SignaturePad mSignaturePad;
    TextView save, date, block, declaration, agreement, cancel;
    ScreenshotUtils screenshot = new ScreenshotUtils();
    //vars
    Methods meth;
    String Declaration;
    HashMap<String, Object> message = new HashMap<>();
    HashMap<String, String> strings = new HashMap<>();
    Date currentDate = new Date();
    Tailgate tailgate = new Tailgate();
    StaffTailgate staff = new StaffTailgate();
    String type;
    MonthlySafety monthly = new MonthlySafety();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        message.clear();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = (HashMap) bundle.getSerializable(getTAG());
            if (message != null) {
                tailgate = (Tailgate) message.get("tailgate");
                monthly = (MonthlySafety) message.get("monthly");
                staff = (StaffTailgate) message.get("staff");
                type = (String) message.get("type");
                bundle.clear();
            }
        }
        meth = (Methods) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.signatures, container, false);
        initUI(view);
        MainActivity.getToolbar().setVisibility(View.GONE);
        mSignaturePad = view.findViewById(R.id.signature_pad);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
        MainActivity.getToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_signature: {
                save.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);

                Bitmap bitmap = ScreenshotUtils.getScreenShot(view);

                 File file = null;
if(type == "daily") {
    file = ScreenshotUtils.store(bitmap, tailgate.getId() + "_" + staff.getId() + "_signature", getContext());
}
else if (type=="monthly"){

    file = ScreenshotUtils.store(bitmap, monthly.getId() + "_" + staff.getId() + "_signature", getContext());

}
                staff.setSignUrl(file.getPath());
                staff.setSignPass(true);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference dr = null;
                if (type == "daily") {
                    dr = db.collection("tailgates").document(tailgate.getId())
                            .collection("staffChecks").document(staff.getId());
                }
                else if (type == "monthly") {

                    dr = db.collection("monthlyMeetings").document(monthly.getId()).collection("attendees")
                            .document(staff.getId());

                }
                HashMap<String, Object> update = new HashMap<>();
                update.put("signPass", staff.isSignPass());
                update.put("signUrl", staff.getSignUrl());
                update.put("completed", true);
                final File finalFile = file;
                dr.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: " + finalFile.getAbsolutePath());
                        Toast.makeText(getContext(), "Signature Saved + ", Toast.LENGTH_SHORT).show();

                    }
                });

                message.put("staff", staff);

                if (type == "daily")
                    meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);
                else if (type == "monthly"){
                    message.put("attendees", monthly);
                    meth.sendFragMessage(new TailgateMonthly(), TailgateMonthly.getTAG(), message);}

                MainActivity.getToolbar().setVisibility(View.VISIBLE);

            }
            break;
            case R.id.cancel_signature: {
                if (type == "daily") {
                    meth.sendDialogMessage(new StaffCheck(), StaffCheck.getTAG(), message);
                    meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);
                } else if (type == "monthly") {
                    message.put("attendees", monthly);
                    meth.sendFragMessage(new TailgateMonthly(), TailgateMonthly.getTAG(), message);
                }
                MainActivity.getToolbar().setVisibility(View.VISIBLE);

            }
            break;
        }
    }


    public void initUI(View view) {

        save = view.findViewById(R.id.save_signature);
        cancel = view.findViewById(R.id.cancel_signature);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        date = view.findViewById(R.id.signature_date);
        if (type == "daily") {
            block = view.findViewById(R.id.signature_block);
            block.setText(tailgate.getBlockName());
        }
        agreement = view.findViewById(R.id.agree_signature);

        declaration = view.findViewById(R.id.declarationText_tailsign);
        com.google.firebase.Timestamp timestamp = com.google.firebase.Timestamp.now();
        dateToTextView(timestamp.toDate(), date);

        declaration(declaration);
        agreement(agreement);
    }

    public void declaration(TextView text) {

        String pre = "I " + staff.getName() + " have read and understood the requirements needed from me today \n" +
                " I will work to the best of my ability on this day " + date.getText()
                + " \nI have performed an equipment check and verify that that my equipment is ready for today " +
                "I have checked my Personal Protective EquipmentTailgate to be safe and ready for the job prescribed\n" +
                "I understand the hazards involved and will use the recommended safe operating procedures.  \n" +
                "I am fit and ready for work today ";

        Declaration = pre;
        text.setText(Declaration);
    }

    public void agreement(TextView text) {
        String pre = staff.getName();
        text.setText(pre);
    }

    public void dateToTextView(Date date, TextView text) {
        String dateString = null;
        SimpleDateFormat format = new SimpleDateFormat("d " + "MMMM" + " yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
        dateString = format.format(date);
        text.setText(dateString);
    }
}