package com.safe.macbros.treeandforest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Incident;

import java.util.HashMap;

public class IncAdapter extends FirestoreRecyclerAdapter<Incident, IncAdapter.ViewHolder> {
    //vars
    HashMap<String, Object> message;
    Methods meth;
    TailgateHelper tHelper;
    Context context;

    public IncAdapter(@NonNull FirestoreRecyclerOptions<Incident> options, HashMap<String, Object> message) {
        super(options);
        this.message = message;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Incident model) {
        if(model.getImageOfflineUrl()!=null){
            holder.incImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.getImageOfflineUrl()).into(holder.incImage);
        }
        holder.title.setText(model.getTitle());
        holder.date.setText(tHelper.dateToString(model.getCreated().toDate()));
        if(model.isFirstAid())
            holder.firstAid.setVisibility(View.VISIBLE);
        if(model.getStaffId()!=null)
            holder.staff.setVisibility(View.VISIBLE);
        if(model.getVehicleId()!=null)
            holder.vehicle.setVisibility(View.VISIBLE);
        if(model.isMedical())
            holder.medical.setVisibility(View.VISIBLE);
        if(!model.isCompleted())
            holder.complete.setColorFilter(ContextCompat.getColor(context, R.color.common_google_signin_btn_text_light_focused));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        tHelper = new TailgateHelper(context);
        meth = (Methods)context;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.universal_row, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView incImage, firstAid, medical, complete, staff, vehicle;
        TextView title, date;
        LinearLayout imgHolder;
        LinearLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHolder = itemView.findViewById(R.id.iconLayout);
            imgHolder.setVisibility(View.VISIBLE);
            incImage = itemView.findViewById(R.id.image_ur);
            firstAid = itemView.findViewById(R.id.firstImg_ur);
            medical = itemView.findViewById(R.id.medicalImg_ur);
            complete = itemView.findViewById(R.id.complete_ur);
            staff = itemView.findViewById(R.id.staffImg_ur);
            vehicle = itemView.findViewById(R.id.vehicleImg_ur);
            parent = itemView.findViewById(R.id.parent_ur);
            title = itemView.findViewById(R.id.title_ur);
            date = itemView.findViewById(R.id.subtitle_ur);

        }
    }
}
