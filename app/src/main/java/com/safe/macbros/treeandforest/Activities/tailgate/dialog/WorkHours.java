package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.util.HashMap;

public class WorkHours extends DialogFragment {
    private static final String TAG = "WorkHours";

    HashMap<String, Object> message = new HashMap<>();
    Tailgate tailgate;
    Methods meth;

    //widgets
    TimePicker startTime, finishTime;
    TextView startText, finishText;
    CardView start, finish;
    Button saveFinish, saveStart;

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
        View view = inflater.inflate(R.layout.working_hours, null);
        initUI(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Hours of Work")
                .setView(view)
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tailgate.setStartTime(startText.getText().toString());
                        tailgate.setFinishTime(finishText.getText().toString());
                        tailgate.setWorkPass(true);
                        meth.sendDialogMessage(new StaffPLB(), StaffPLB.getTAG(), message);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
    public void initUI(View view){

        start = view.findViewById(R.id.cardStartTime);
        finish = view.findViewById(R.id.cardFinishTime);
        startText = view.findViewById(R.id.startText);
        finishText = view.findViewById(R.id.finishText);
        startTime = view.findViewById(R.id.startTime);
        finishTime = view.findViewById(R.id.finishTime);
        saveStart = view.findViewById(R.id.saveStart);
        saveFinish = view.findViewById(R.id.saveFinish);
        setTime(startTime, 6, 30);
        setTime(finishTime, 13, 30);
        loadViews();
    }

    public void loadViews(){

        startText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.VISIBLE);
            }
        });
        finishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish.setVisibility(View.VISIBLE);
            }
        });
        saveStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(startTime, startText);
                start.setVisibility(View.GONE);

            }
        });

        saveFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(finishTime, finishText);
                finish.setVisibility(View.GONE);
                tailgate.setStartTime(startText.getText().toString());
                tailgate.setFinishTime(finishText.getText().toString());
                tailgate.setWorkPass(true);
                meth.sendDialogMessage(new StaffPLB(), StaffPLB.getTAG(), message);

            }
        });

    }

    public void setTime(TimePicker timePicker, int hour, int minute) {
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
    }

    public void getTime(TimePicker timePicker, TextView text) {
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();
        showStartTime(hour, min, text);
    }
    public void showStartTime(int hour, int min, TextView text) {
        String timeString = "";
        if (hour == 0) {
            hour += 12;
            timeString = "AM";
        } else if (hour == 12) {
            timeString = "PM";
        } else if (hour > 12) {
            hour -= 12;
            timeString = "PM";
        } else {
            timeString = "AM";
        }

        text.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(timeString));
    }

    public static String getTAG() {
        return TAG;
    }
}
