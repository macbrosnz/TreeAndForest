package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateSign;
import com.safe.macbros.treeandforest.Activities.tailgate.models.MonthlySafety;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;

import java.util.HashMap;

public class staffMonthlyAdaptor extends FirestoreRecyclerAdapter<StaffTailgate, staffMonthlyAdaptor.Viewholder> {
    private static final String TAG = "staffMonthlyAdaptor";



    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    MonthlySafety monthly = new MonthlySafety();



    public staffMonthlyAdaptor(@NonNull FirestoreRecyclerOptions<StaffTailgate> options, MonthlySafety monthly) {
        super(options);
        this.monthly = monthly;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull final StaffTailgate model) {

        holder.name.setText(model.getName());
        if(model.isSignPass())
        holder.signed.setChecked(true);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message.put("staff", model);

                message.put("monthly", monthly);

                message.put("type", "monthly");

                meth.sendFragMessage(new TailgateSign(), TailgateSign.getTAG(), message);
            }
        });

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        meth = (Methods)viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_signatures, viewGroup, false);
        return new Viewholder(v);

    }


    class Viewholder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox signed;
        ConstraintLayout parent;
        public Viewholder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.staffName_rowSignatures);
            signed = view.findViewById(R.id.signed_rowSignatures);
            parent = view.findViewById(R.id.parent_rowSignatures);
            signed.setEnabled(false);

        }
    }


}
