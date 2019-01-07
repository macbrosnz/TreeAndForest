package com.safe.macbros.treeandforest.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.safe.macbros.treeandforest.Activities.tailgate.dialog.StaffNote;
import com.safe.macbros.treeandforest.Activities.tailgate.models.EquipmentTailgate;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AlarmDialog extends DialogFragment  implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "AlarmDialog";

    private HashMap<String,Object> message = new HashMap<>();

    private TextView mTextView;

    private Context mContext;

    private EquipmentTailgate equipmentDetails;

    StaffNote mStaffNote = new StaffNote();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            message = (HashMap<String,Object>)bundle.getSerializable(getTAG());

            equipmentDetails = (EquipmentTailgate)message.get("equipmentDetails");

            mContext = (Context)message.get("context");

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        mTextView = mStaffNote.getDate();

        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)this, year, month, day);

    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
        mTextView.setText(dayOfMonth +"/"+ month + "/" +year + "\n\n");
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        Date d = cal.getTime();
        mStaffNote.setStaticDate(d);
    }
}
































