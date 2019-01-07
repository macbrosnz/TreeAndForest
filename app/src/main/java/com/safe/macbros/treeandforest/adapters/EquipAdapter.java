package com.safe.macbros.treeandforest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.models.Equipment;

import javax.annotation.Nullable;

public class EquipAdapter extends FirestoreRecyclerAdapter<Equipment, EquipAdapter.ViewHolder> {
    FirebaseFirestore db = MainActivity.getFireDb();
    Context mContext;
    ChildEquipAdaptor cAdaptor;

    public EquipAdapter(@NonNull FirestoreRecyclerOptions<Equipment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Equipment model) {
        if (model.getOfflineImageUrl() != null) {
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(model.getOfflineImageUrl()).into(holder.image);
        } else {
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.emptyimg_icon).into(holder.image);
        }
        if (model.getName() != null)
            holder.title.setText(model.getName());
        checkAlerts(holder.alert, model.getId());
        if (model.getMaintenanceUrl() != null)
            holder.maintenance.setVisibility(View.VISIBLE);
        if (model.getOfflineStandardsUrl() != null)
            holder.standards.setVisibility(View.VISIBLE);
        if (model.getChildlist() != null) {
            holder.subMenu.setVisibility(View.GONE );
            holder.subMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.recycler.getVisibility() == View.GONE) {
                        holder.recycler.setVisibility(View.VISIBLE);
                    } else {
                        holder.recycler.setVisibility(View.GONE);
                    }
                }
            });
            initAdapter(model.getId(), mContext, holder.recycler);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.universal_row, viewGroup, false);

        return new ViewHolder(view);

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        ImageView image, subMenu, alert, standards, maintenance;
        TextView title, subTitle;
        RecyclerView recycler;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent_ur);
            image = itemView.findViewById(R.id.image_ur);
            subMenu = itemView.findViewById(R.id.submenu_ur);
            title = itemView.findViewById(R.id.title_ur);
            subTitle = itemView.findViewById(R.id.subtitle_ur);
            alert = itemView.findViewById(R.id.alert_ur);
            standards = itemView.findViewById(R.id.standards_ur);
            maintenance = itemView.findViewById(R.id.maintenance_ur);
            recycler = itemView.findViewById(R.id.childRecycler_ur);

        }


    }

    public void checkAlerts(final ImageView image, String id) {
        CollectionReference cr = db.collection("alerts");
        Query query = cr.whereEqualTo("complete", false).whereEqualTo("equipmentId", id);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot docs, @Nullable FirebaseFirestoreException e) {
                if (docs.size() > 0) {
                    image.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void initAdapter(String id, Context context, RecyclerView recycler) {

        CollectionReference cr = db.collection("equipment");
        Query query = cr.whereEqualTo("parentId", id);

        FirestoreRecyclerOptions<Equipment> options = new FirestoreRecyclerOptions.Builder<Equipment>()
                .setQuery(query, Equipment.class)
                .build();

        cAdaptor = new ChildEquipAdaptor(options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler.setAdapter(cAdaptor);
        recycler.setLayoutManager(layoutManager);

        cAdaptor.startListening();
    }


}
