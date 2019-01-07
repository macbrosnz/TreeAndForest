package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.SafetyHelper;
import com.safe.macbros.treeandforest.dialog.HazardDialog;
import com.safe.macbros.treeandforest.models.Hazards;

import java.io.File;
import java.util.HashMap;

public class HazardsAdaptor extends FirestoreRecyclerAdapter<Hazards, HazardsAdaptor.ViewHolder> {

    private static final String TAG = "HazardsAdaptor";

    //vars
    Methods meth;
    HashMap<String,Object> message = new HashMap<>();
    SafetyHelper sHelper;
    Context mContext;

    public HazardsAdaptor(@NonNull FirestoreRecyclerOptions<Hazards> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Hazards model) {

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.put("title", model.getTitle());
                message.put("description", model.getControl());
                meth.sendDialogMessage(new HazardDialog(), HazardDialog.getTAG(), message);
            }
        });

        if (model.getOfflinePath() != null)
            if (new File(model.getOfflinePath()).length() > 2)
                Glide.with(mContext).load(new File(model.getOfflinePath())).into(holder.image);

        holder.title.setText(model.getTitle());
        sHelper.coloredHazardsText(holder.rating, model.getRiskScore());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_hazardsadaptor, viewGroup, false);
        sHelper = new SafetyHelper();
        mContext = viewGroup.getContext();
        meth = (Methods)viewGroup.getContext();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, rating;
        ImageView image;
        ConstraintLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.hazardParent_taildetails);
            title = itemView.findViewById(R.id.hazardName_taildetails);
            rating = itemView.findViewById(R.id.hazardRating_taildetails);
            image = itemView.findViewById(R.id.image_ur);
        }
    }
}
