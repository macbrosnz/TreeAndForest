package com.safe.macbros.treeandforest.Activities.incidents.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.Activities.incidents.IncidentDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Incident;

import java.net.IDN;
import java.util.HashMap;

public class IncidentsRecyclerAdapter extends FirestoreRecyclerAdapter<Incident, IncidentsRecyclerAdapter.ViewHolder> {

//vars
    TailgateHelper tHelper;
    Methods meth;
    HashMap<String,Object> message = new HashMap<>();

    public IncidentsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Incident> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Incident model) {
        if(model.getStaffName()!=null)
            holder.title.setText(model.getStaffName() + ", " + model.getTitle());
        else
            holder.title.setText(model.getTitle());


        if(model.getCreated()!=null)
        holder.date.setText(tHelper.dateToString(model.getCreated().toDate()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.put("incident", model);
                meth.sendFragMessage(new IncidentDetails(), IncidentDetails.getTAG(), message);
            }
        });
        holder.location.setText(model.getSiteName());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        meth = (Methods)viewGroup.getContext();
        tHelper = new TailgateHelper(viewGroup.getContext());
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_tailgateadapter, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, location;
        ImageView image;
        CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent_tailgateAdapter);
            title = itemView.findViewById(R.id.block_tailgateAdapter);
            date = itemView.findViewById(R.id.date_tailgateAdapter);
            location = itemView.findViewById(R.id.staff_tailgateAdapter);
            image = itemView.findViewById(R.id.image_tailgateAdapter);
            image.setVisibility(View.GONE);

        }
    }
}
