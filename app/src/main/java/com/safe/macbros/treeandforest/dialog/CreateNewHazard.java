package com.safe.macbros.treeandforest.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Hazards;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateNewHazard extends DialogFragment {
    private static final String TAG = "CreateNewHazard";


    //vars
    Methods meth;
    String siteId, taskId;
    HashMap<String, Object> message = new HashMap<>();
    AlertDialog.Builder builder;
    Hazards hazard = new Hazards();
    TailgateHelper tHelper;
    Tailgate mTailgate = new Tailgate();
    int riskscore = 0;
    FirebaseFirestore db = MainActivity.getFireDb();

    //widgets
    TextInputLayout titleTIL;
    TextView riskNumber;
    EditText title, details, control;
    SeekBar riskScore;
    Spinner type;
    Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        mContext = context;
        tHelper = new TailgateHelper(context);
        Bundle bundle = this.getArguments();
        if (bundle == null) {


        } else {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            siteId = (String) message.get("siteId");
            taskId = (String) message.get("taskId");

        }
        bundle.clear();

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_hazard_dialog, null);
        initUI(view);
        builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("Create a New Hazard")
                .setView(view)
                .setPositiveButton("Next", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog ad = builder.create();
        ad.show();

        Button b = ad.getButton(DialogInterface.BUTTON_POSITIVE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean p = checkForErrors(title.getText().toString(), titleTIL);
                if (p) {
                    Log.d(TAG, "onClick: " + details.getText().toString());
                    hazard.setDetails(details.getText().toString());
                    hazard.setTitle(title.getText().toString());
                    hazard.setControl(control.getText().toString());
                    hazard.setEim(type.getSelectedItem().toString());
                    hazard.setRiskScore(riskscore);
                    message.put("hazard", hazard);
                    meth.sendDialogMessage(new AddImageHazard(), AddImageHazard.getTAG(), message);
                    dismiss();
                }
            }
        });

        return ad;

    }


    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle savedInstanceState) {
        return super.onGetLayoutInflater(savedInstanceState);

    }


    public void initUI(View view) {
        type = view.findViewById(R.id.spinner_newHazard);
        control = view.findViewById(R.id.control_newHazard);
        titleTIL = view.findViewById(R.id.til_newHazard);
        title = view.findViewById(R.id.title_newHazard);
        details = view.findViewById(R.id.details_newHazard);
        riskScore = view.findViewById(R.id.riskSlider_newHazard);
        riskNumber = view.findViewById(R.id.riskNumber_newHazard);
        /*keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(control.getWindowToken(), 0);
            }
        });*/

        List<String> list = new ArrayList<>();
        list.add("Minimise");
        list.add("Eliminate");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(dataAdapter);

        riskScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                riskNumber.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                riskscore = seekBar.getProgress();
            }
        });

    }


    public boolean checkForErrors(String text, TextInputLayout floatingUsernameLabel) {
        boolean pass = false;
        Log.d(TAG, "checkForErrors: text length = " + text.length());

        if (text.length() == 0) {

            floatingUsernameLabel.setError("You must enter a title");
            floatingUsernameLabel.setErrorEnabled(true);

        } else {
            floatingUsernameLabel.setErrorEnabled(false);
            pass = true;

        }

        return pass;
    }



    public static String getTAG() {
        return TAG;
    }
}
