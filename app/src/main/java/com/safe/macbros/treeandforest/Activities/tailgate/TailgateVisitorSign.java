package com.safe.macbros.treeandforest.Activities.tailgate;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.ScreenshotUtils;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.models.Visitor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class TailgateVisitorSign extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "TailgateVisitorSign";

    //widgets
    SignaturePad mSignaturePad;
    TextView title, save, date, block, declaration, agreement, cancel;
    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    Tailgate tailgate;
   Visitor visitor;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        message.clear();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = (HashMap) bundle.getSerializable(getTAG());
            if (message != null) {
                visitor = (Visitor) message.get("visitor");
                tailgate = (Tailgate)message.get("tailgate");
                bundle.clear();
                Log.d(TAG, "onAttach: visitor and tailgate Ids " + visitor.getId() + " tId = " + tailgate.getId());
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
                file = ScreenshotUtils.store(bitmap, tailgate.getId() + "_" + visitor.getId() + "_signature", getContext());
                FirebaseFirestore db = MainActivity.getFireDb();
                DocumentReference dr = db.collection("visitors").document(visitor.getId());
                HashMap<String, Object> update = new HashMap<>();
                update.put("signOfflinePath", file.getPath());
                final File finalFile = file;

                dr.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: " + finalFile.getAbsolutePath());
                        Toast.makeText(getContext(), "Signature Saved + ", Toast.LENGTH_SHORT).show();

                    }
                });

                MainActivity.getToolbar().setVisibility(View.VISIBLE);

                    meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);


            }
            break;
            case R.id.cancel_signature: {
                MainActivity.getToolbar().setVisibility(View.VISIBLE);
                    meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);

            }
            break;
        }
    }


    public void initUI(View view) {
    title = view.findViewById(R.id.signature_title);
        save = view.findViewById(R.id.save_signature);
        cancel = view.findViewById(R.id.cancel_signature);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        date = view.findViewById(R.id.signature_date);

            block = view.findViewById(R.id.signature_block);
            block.setText(tailgate.getBlockName());

        agreement = view.findViewById(R.id.agree_signature);

        declaration = view.findViewById(R.id.declarationText_tailsign);
        com.google.firebase.Timestamp timestamp = com.google.firebase.Timestamp.now();
        dateToTextView(timestamp.toDate(), date);
        title.setText("T&F Visitors Declaration Form");
        declaration(declaration);
        agreement(agreement);
    }

    public void declaration(TextView text) {

        String pre = "I " + visitor.getName() + " have been Inducted onto this site by the following "
                + "\nI understand the emergency procedures if there should be any emergency situation " +
                "\nI have supplied my email only to send me information about todays operation\n and understand it will not be used again" +
                "\nI understand the hazards involved.  \n";

        text.setText(pre);
    }

    public void agreement(TextView text) {
        String pre = visitor.getName();
        text.setText(pre);
    }

    public void dateToTextView(Date date, TextView text) {
        String dateString = null;
        SimpleDateFormat format = new SimpleDateFormat("d " + "MMMM" + " yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
        dateString = format.format(date);
        text.setText(dateString);
    }

    public static String getTAG() {
        return TAG;
    }
}
