package com.safe.macbros.treeandforest.Activities.sites.adapters;

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
import com.safe.macbros.treeandforest.Activities.sites.SiteDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Sites;

import java.util.HashMap;

public class SitesAdapter extends FirestoreRecyclerAdapter<Sites, SitesAdapter.ViewHolder> {
    private static final String TAG = "SitesAdapter";

    //var
    TailgateHelper tHelper;
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();


    public SitesAdapter(@NonNull FirestoreRecyclerOptions<Sites> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Sites model) {
        holder.title.setText(model.getName());
        holder.gps.setText(model.getGps());
        if (model.getCreated() != null)
            holder.date.setText(tHelper.dateToString(model.getCreated().toDate()));

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.put("site", model);
                meth.sendFragMessage(new SiteDetails(), SiteDetails.getTAG(), message);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        tHelper = new TailgateHelper(viewGroup.getContext());
        meth = (Methods)viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_tailgateadapter, viewGroup, false);

        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, gps, date;
        CardView parent;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent_tailgateAdapter);
            title = itemView.findViewById(R.id.block_tailgateAdapter);
            gps = itemView.findViewById(R.id.staff_tailgateAdapter);
            date = itemView.findViewById(R.id.date_tailgateAdapter);
            image = itemView.findViewById(R.id.image_tailgateAdapter);
            image.setVisibility(View.GONE);
        }
    }


    public static String getTAG() {
        return TAG;
    }
}
