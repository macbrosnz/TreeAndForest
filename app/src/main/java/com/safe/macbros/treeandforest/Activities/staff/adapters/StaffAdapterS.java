package com.safe.macbros.treeandforest.Activities.staff.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.Activities.staff.StaffDetails;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.HashMap;

public class StaffAdapterS extends FirestoreRecyclerAdapter<Staff, StaffAdapterS.ViewHolder> {

    //vars
    Methods meth;
    HashMap<String,Object> message = new HashMap<>();



    public StaffAdapterS(@NonNull FirestoreRecyclerOptions<Staff> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Staff model) {

        holder.name.setText(model.getName());
        if(model.getMobile()!=null)
        {holder.cellnumber.setText(model.getMobile());}

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message.put("staff", model);
                meth.sendFragMessage(new StaffDetails(), StaffDetails.getTAG(), message);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_tailgateadapter, viewGroup, false);
    meth = (Methods)viewGroup.getContext();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, cellnumber, days;
        CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent_tailgateAdapter);
            name = itemView.findViewById(R.id.block_tailgateAdapter);
            cellnumber = itemView.findViewById(R.id.staff_tailgateAdapter);
            days = itemView.findViewById(R.id.days_tailgateAdapter);
            image = itemView.findViewById(R.id.image_tailgateAdapter);
            image.setVisibility(View.GONE);
        }
    }
}
