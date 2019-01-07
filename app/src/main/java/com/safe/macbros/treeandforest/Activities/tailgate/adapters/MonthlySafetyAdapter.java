package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMonthly;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.models.MonthlySafety;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;

import java.util.HashMap;

public class MonthlySafetyAdapter extends FirestoreRecyclerAdapter<MonthlySafety, MonthlySafetyAdapter.ViewHolder> {
    private static final String TAG = "MonthlySafetyAdapter";

    //vars
    Methods meth;
    TailgateHelper tHelper;
    HashMap<String,Object>message;


    public MonthlySafetyAdapter(@NonNull FirestoreRecyclerOptions<MonthlySafety> options, HashMap<String,Object> message) {
        super(options);
        this.message = message;

    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final MonthlySafety model) {

        holder.date.setText(tHelper.dateToString(model.getTimestamp().toDate()));
    holder.staff.setText(tHelper.cutSquareBracketsFromString(model.getMeetingAttendance()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.put("monthlySafety", model);
                meth.sendFragMessage(new TailgateMonthly(), TailgateMonthly.getTAG(), message);

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        meth = (Methods)viewGroup.getContext();

        tHelper = new TailgateHelper(viewGroup.getContext());

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_tailgateadapter, viewGroup, false);

        return new ViewHolder(v);

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, staff;
        ImageView image;


        CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.block_tailgateAdapter);

            staff = itemView.findViewById(R.id.staff_tailgateAdapter);

            parent = itemView.findViewById(R.id.parent_tailgateAdapter);

            image = itemView.findViewById(R.id.image_tailgateAdapter);

            image.setVisibility(View.GONE);
        }
    }

    public static String getTAG() {
        return TAG;
    }
}
