package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateEquipment;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.StaffNote;
import com.safe.macbros.treeandforest.Activities.tailgate.models.EquipmentTailgate;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PPEAdapter extends FirestoreRecyclerAdapter<EquipmentTailgate, PPEAdapter.ViewHolder> {
    private static final String TAG = "PPEAdapter";



    TailgateEquipment mTailgateEquipment = new TailgateEquipment();

    public StaffTailgate staff;

    private Tailgate tailgate;

    private HashMap<String, Object> message;

    private Context context;

    TailgateHelper mTailgateHelper = new TailgateHelper(context);

    Methods meth;

    private FirebaseFirestore db = MainActivity.getFireDb();


    public PPEAdapter(@NonNull FirestoreRecyclerOptions<EquipmentTailgate> options,
                      Context context, HashMap<String, Object> message) {
        super(options);
        this.context = context;

        this.message = message;

        this.tailgate = (Tailgate) message.get("tailgate");

        this.staff = (StaffTailgate) message.get("staff");
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(TAG, "onAttachedToRecyclerView: the ppe adaptor is set.");
    }

    @Override
    protected void onBindViewHolder(@NonNull final PPEAdapter.ViewHolder holder, int position, @NonNull final EquipmentTailgate model) {

        holder.alert.setVisibility(View.GONE);
        holder.alertDate.setVisibility(View.GONE);

        if (model.getNoteId() != null) {

            holder.note.setVisibility(View.VISIBLE);

            holder.noteDetails.setText(model.getNote());

        } else {
            holder.note.setVisibility(View.GONE);
        }

        if (model.getAlertDate() != null){
            holder.alert.setVisibility(View.VISIBLE);
            holder.alert.setColorFilter(mTailgateHelper.dateAlertColor(model.getAlertDate()));
            holder.alertDate.setVisibility(View.VISIBLE);
            String date = mTailgateHelper.dateToString(model.getAlertDate());
            holder.alertDate.setText(date);

        } else {

            holder.alert.setVisibility(View.GONE);

        }

        holder.title.setText(model.getName());


        if (mTailgateEquipment.getEquipment().isChecked()) {

            holder.checkBox.setEnabled(false);

            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setChecked(model.getPass());



        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    Log.d(TAG, "AlertId " + model.getAlertId()+ " alertdate " + model.getAlertDate());
                    updateFirestore(model.getTailgateId(),
                            model.getStaffId(),
                            model.getId(), true);


                } else

                    updateFirestore(model.getTailgateId(),
                            model.getStaffId(),
                            model.getId(), false);


            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (model.getNote() != null || model.getAlertDate() != null) {

                    if (holder.alertBox.getVisibility() == View.GONE)
                        holder.alertBox.setVisibility(View.VISIBLE);
                    else
                        holder.alertBox.setVisibility(View.GONE);
                }
            }

        });

        holder.title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                HashMap<String, Object> newMessage = new HashMap<>();

                newMessage.put("context", context);

                newMessage.put("equipmentDetails", model);

                meth.sendDialogMessage(new StaffNote(), StaffNote.getTAG(), newMessage);

                return true;
            }
        });

        holder.standards.setText(cutUpTheCommas(model.getStandards()));

    }

    @NonNull
    @Override
    public PPEAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        meth = (Methods) viewGroup.getContext();

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_equipadapter, viewGroup, false);

        return new PPEAdapter.ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        TextView title, standards, noteDetails, alertDate;

        ImageView note, info, alert;

        FrameLayout requirementsBox, alertBox;

        public ViewHolder(@NonNull View v) {

            super(v);

            alertDate = v.findViewById(R.id.alertDate_equipadapter);

            noteDetails = v.findViewById(R.id.noteDetails_equipadapter);

            checkBox = v.findViewById(R.id.check_equipadapter);

            title = v.findViewById(R.id.title_equipadapter);

            standards = v.findViewById(R.id.standards_equipadapter);

            note = v.findViewById(R.id.note_equipadapter);

            info = v.findViewById(R.id.info_equipadapter);

            alert = v.findViewById(R.id.alert_equipadapter);

            requirementsBox = v.findViewById(R.id.infoParent_equipadapter);

            alertBox = v.findViewById(R.id.alertParent_equipadapter);

        }

    }

    public String cutUpTheCommas(String commas) {

        StringBuilder stringBuilder = new StringBuilder();

        if(commas!=null){
            List<String> noCommaList = Arrays.asList(commas.split(","));

            for (String noCommas : noCommaList) {

                stringBuilder.append(noCommas);
                stringBuilder.append("\n");

            }}
        stringBuilder.append("");
        return stringBuilder.toString();
    }

    public void updateFirestore(String tailgateId, String staffId, String equipmentId, Boolean pass) {


        DocumentReference dr = db.collection("tailgates").document(tailgateId)
                .collection("staffChecks").document(staffId).collection("equipment")
                .document(equipmentId);

        Map<String, Object> map = new HashMap<>();
        map.clear();
        map.put("pass", pass);

        dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "equipment updated: ");
            }
        });

    }


}
