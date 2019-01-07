package com.safe.macbros.treeandforest.Activities.equipment.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.models.Equipment;

public class EquipmentAdapter extends FirestoreRecyclerAdapter<Equipment, EquipmentAdapter.ViewHolder > {
    private static final String TAG = "EquipmentAdapter";

    public EquipmentAdapter(@NonNull FirestoreRecyclerOptions<Equipment> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull EquipmentAdapter.ViewHolder holder, int position, @NonNull Equipment model) {
        holder.title.setText(model.getName());
    }

    @NonNull
    @Override
    public EquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_textadapter, viewGroup,false);
        return new EquipmentAdapter.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_textadapter);
        }
    }
}
