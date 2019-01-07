package com.safe.macbros.treeandforest.Activities.staff.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Incident;

import java.util.HashMap;

public class StaffIncidentAdapter extends FirestoreRecyclerAdapter<Incident, StaffIncidentAdapter.ViewHolder> {

    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    TailgateHelper tHelper;
    //widgets


    public StaffIncidentAdapter(@NonNull FirestoreRecyclerOptions<Incident> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Incident model) {
        if (model.getSiteName() != null) {
            holder.subtitle.setVisibility(View.VISIBLE);
            holder.subtitle.setText(model.getSiteName());
        }
        holder.title.setText(model.getTitle());
        holder.date.setText(tHelper.dateToString(model.getCreated().toDate()));


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        tHelper = new TailgateHelper(viewGroup.getContext());
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.universal_row, viewGroup, false);
        meth = (Methods) viewGroup.getContext();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle, date;
        ImageView image;
        LinearLayout parent;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            parent = itemView.findViewById(R.id.parent_ur);
            title = itemView.findViewById(R.id.title_ur);
            subtitle = itemView.findViewById(R.id.subtitle_ur);
            date = itemView.findViewById(R.id.sideTitle_ur);
            image = itemView.findViewById(R.id.image_ur);
            image.setVisibility(View.GONE);
        }
    }
}
