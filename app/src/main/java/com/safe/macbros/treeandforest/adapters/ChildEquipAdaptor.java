package com.safe.macbros.treeandforest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class ChildEquipAdaptor extends FirestoreRecyclerAdapter<Equipment, ChildEquipAdaptor.ViewHolder> {

    FirebaseFirestore db = MainActivity.getFireDb();
    Context mContext;

    public ChildEquipAdaptor(@NonNull FirestoreRecyclerOptions<Equipment> options) {
        super(options);
    }




    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Equipment model) {

        if (model.getOfflineImageUrl() != null)
        Glide.with(mContext).load(model.getOfflineImageUrl()).into(holder.image);
        else
        Glide.with(mContext).load(R.drawable.emptyimg_icon).into(holder.image);

        if (model.getName() != null)
            holder.title.setText(model.getName());

        checkAlerts(holder.alert, model.getId());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.universal_column, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        ImageView image, subMenu, alert, standards, maintenance;
        TextView title, subTitle;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            parent = itemView.findViewById(R.id.parent_ur);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            alert = itemView.findViewById(R.id.alert);


        }
    }

    public void checkAlerts(final ImageView alert, String id) {
        CollectionReference cr = db.collection("alerts");
        Query query = cr.whereEqualTo("equipmentId", id);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot docs, @Nullable FirebaseFirestoreException e) {
                if (docs.size() > 0) {
                    alert.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
