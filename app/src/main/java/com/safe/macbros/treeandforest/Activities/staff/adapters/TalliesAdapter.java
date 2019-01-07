package com.safe.macbros.treeandforest.Activities.staff.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Tallies;

import java.util.HashMap;

public class TalliesAdapter extends FirestoreRecyclerAdapter<Tallies, TalliesAdapter.ViewHolder> {

    //vars
    Methods meth;
    HashMap<String,Object> message = new HashMap<>();
    Tallies tally = new Tallies();



    public TalliesAdapter(@NonNull FirestoreRecyclerOptions<Tallies> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Tallies model) {
        holder.site.setText(model.getTailgateName()+"/"+model.getTaskName());
        holder.tally.setText(model.getTally());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        meth = (Methods)viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_hazardsadaptor, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView site, tally;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            site = itemView.findViewById(R.id.hazardName_taildetails);
            tally = itemView.findViewById(R.id.hazardRating_taildetails);
            image = itemView.findViewById(R.id.image_ur);

        }
    }
}
