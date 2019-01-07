package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateDetails;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class TailgateAdapter extends FirestoreRecyclerAdapter<Tailgate, TailgateAdapter.TailgateHolder> {
    private static final String TAG = "TailgateAdapter";

    Tailgate tailgate = new Tailgate();
    Methods meth;
    Context mContext;
    HashMap<String, Object> message = new HashMap<>();


    public TailgateAdapter(@NonNull FirestoreRecyclerOptions<Tailgate> options, Context context) {

        super(options);
        this.mContext = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull TailgateHolder holder, int position, @NonNull final Tailgate model) {

        this.tailgate = model;
        if (model.getTimestamp() != null)
            holder.date.setText(dateToString(model.getTimestamp().toDate()));

        if (position != 0)
            holder.parent.setBackgroundColor(Color.parseColor("#ededed"));

        holder.blockTitle.setText(model.getBlockName());

        holder.staff.setText(cutString(model.getStaff()));

        holder.image.setImageResource(taskResource(model.getTaskName()));

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message.put("tailgate", model);
                meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);

            }
        });
    }

    @NonNull
    @Override
    public TailgateHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tailgateadapter,
                parent, false);
        return new TailgateHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        meth = (Methods) mContext;
    }

    class TailgateHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView blockTitle, staff, date;
        CardView parent;

        public TailgateHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_tailgateAdapter);
            blockTitle = itemView.findViewById(R.id.block_tailgateAdapter);
            staff = itemView.findViewById(R.id.staff_tailgateAdapter);
            date = itemView.findViewById(R.id.date_tailgateAdapter);
            parent = itemView.findViewById(R.id.parent_tailgateAdapter);

        }
    }

    public String cutString(ArrayList arrayList) {
        String string = arrayList.toString();
        String s = string.substring(1, string.length() - 1);
        return s;
    }

    public String dateToString(Date date) {

        String dateString = null;
        SimpleDateFormat format = new SimpleDateFormat("d " + "MMMM");
        format.setTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
        dateString = format.format(date);
        return dateString;

    }

    public int taskResource(String task) {
        int i;
        if (task == null) {
            task = "Thinning";
        }
        String urlPath = null;
        switch (task) {
            case "Planting":
                i = R.drawable.spade_task;
                break;
            case "Thinning to waste":
                i = R.drawable.chainsaw1_task;
                break;
            case "Spot Spraying":
                i = R.drawable.spray_icon;
                break;
            default:
                i = R.drawable.icon;

        }

        return i;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    public Tailgate getTailgate() {
        return tailgate;
    }
}