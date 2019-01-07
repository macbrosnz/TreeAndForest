package com.safe.macbros.treeandforest.Activities.hazards.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.Activities.hazards.HazardDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.FileHelper;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Hazards;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HazardAdapter extends FirestoreRecyclerAdapter<Hazards, HazardAdapter.Viewholder> {
    private static final String TAG = "HazardAdapter";

    //vars
    Context mContext;
    TailgateHelper tHelper;
    FileHelper fHelper;
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;

    public HazardAdapter(@NonNull FirestoreRecyclerOptions<Hazards> options) {
        super(options);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull final Hazards model) {

        holder.parent.setBackgroundColor(Color.parseColor("#ededed"));

        coloredTextView(holder.date, model.getRiskScore());
        holder.date.setText(String.valueOf(model.getRiskScore()));


        holder.blockTitle.setText(model.getTitle());

        if (model.getOfflinePath() != null)
            if (new File(model.getOfflinePath()).length() > 2)
                Glide.with(mContext).load(new File(model.getOfflinePath())).into(holder.image);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.put("hazard", model);
                meth.sendFragMessage(new HazardDetails(), HazardDetails.getTAG(), message);
            }
        });

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        meth = (Methods) mContext;
        tHelper = new TailgateHelper(viewGroup.getContext());
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_tailgateadapter, viewGroup, false);
        return new Viewholder(view);

    }


    public class Viewholder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView blockTitle, staff, date;
        CardView parent;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_tailgateAdapter);
            blockTitle = itemView.findViewById(R.id.block_tailgateAdapter);
            staff = itemView.findViewById(R.id.staff_tailgateAdapter);
            date = itemView.findViewById(R.id.date_tailgateAdapter);
            date.setTextSize(20);
            parent = itemView.findViewById(R.id.parent_tailgateAdapter);


        }
    }

    public void coloredTextView(TextView textView, int i){
        if(i< 4)
            textView.setTextColor(Color.parseColor("#00B300"));
        else if(i>=4 && i<=6)
            textView.setTextColor(Color.parseColor("#fdff00"));
        else if(i>6 && i<=12)
        textView.setTextColor(Color.parseColor("#ff8d00"));
        else if(i >12)
            textView.setTextColor(Color.parseColor("#ff0000"));

    }

}

