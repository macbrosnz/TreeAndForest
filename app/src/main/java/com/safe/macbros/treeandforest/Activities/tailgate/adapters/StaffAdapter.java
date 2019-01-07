package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.StaffCheck;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.ShowImage;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.models.Staff;
import com.safe.macbros.treeandforest.models.Tailgate;


import java.util.HashMap;

public class StaffAdapter extends FirestoreRecyclerAdapter<StaffTailgate, StaffAdapter.ViewHolder> {
    private static final String TAG = "StaffAdapter";
    HashMap<String, Object> message;
    Tailgate tailgate;
    Methods meth;
    Context context;
    FirebaseFirestore db = MainActivity.getFireDb();

    public StaffAdapter(@NonNull FirestoreRecyclerOptions<StaffTailgate> options,
                        HashMap<String, Object> message, Tailgate tailgate, Context context) {
        super(options);
        this.message = message;
        this.tailgate = tailgate;
        this.context = context;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        meth = (Methods) context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final StaffTailgate model) {

        holder.complete.setChecked(model.isCompleted());
        holder.sign.setChecked(model.isSignPass());
        holder.equipment.setChecked(model.isEquipmentPass());
        holder.plb.setChecked(model.isPlb());
        staffDetails(model.getId(), holder.fire, holder.firstAid);
        if(model.isCompleted())
            holder.staffName.setTextColor(Color.parseColor("#00CC00"));
        holder.staffName.setText(model.getName());
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (model.getSignUrl() != null) {
                    message.put("staff", model);
                    message.put("tailgate", tailgate);
                    meth.sendFragMessage(new ShowImage(), ShowImage.getTAG(), message);
                }
                return false;

            }
        });
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, model.getName(), Toast.LENGTH_SHORT).show();
                message.put("staff", model);
                message.put("tailgate", tailgate);
                meth.sendDialogMessage(new StaffCheck(), StaffCheck.getTAG(), message);
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_staffadapter, viewGroup, false);

        return new ViewHolder(v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView staffImage;
        TextView staffName;
        CheckBox complete, equipment, sign, plb, fire, firstAid;
        RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            staffImage = itemView.findViewById(R.id.staffImg_taildetails);
            staffName = itemView.findViewById(R.id.staffName_taildetails);
            complete = itemView.findViewById(R.id.staffCheck_taildetails);
            equipment = itemView.findViewById(R.id.staffPpeCheck_taildetails);
            sign = itemView.findViewById(R.id.staffSignCheck_taildetails);
            plb = itemView.findViewById(R.id.plbCheck);
            fire = itemView.findViewById(R.id.fireCheck);
            firstAid = itemView.findViewById(R.id.firstAid_check);
            parent = itemView.findViewById(R.id.staffParent_taildetails);
        }
    }

    public void staffDetails(String id, final CheckBox fire, final CheckBox firstAid){

        DocumentReference dr = db.collection("staff").document(id);

        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if(doc.toObject(Staff.class)!=null)
                {Staff staff = doc.toObject(Staff.class);
               firstAid.setChecked(staff.isFirstaid());
               fire.setChecked(staff.isFireCert());}
            }
        });
    }
}
