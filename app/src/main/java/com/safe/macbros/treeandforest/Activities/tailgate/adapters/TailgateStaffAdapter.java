package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.models.Staff;

import javax.annotation.Nullable;

public class TailgateStaffAdapter extends FirestoreRecyclerAdapter<StaffTailgate, TailgateStaffAdapter.ViewHolder> {


    public TailgateStaffAdapter(@NonNull FirestoreRecyclerOptions<StaffTailgate> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull StaffTailgate model) {
        holder.staffName.setText(model.getName());
        staffQualifications(model.getId(), holder.fire, holder.firstAid);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.universal_row, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView fire, firstAid, staffImage;
        TextView staffName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            staffImage = itemView.findViewById(R.id.image_ur);
            fire = itemView.findViewById(R.id.fire_ur);
            firstAid = itemView.findViewById(R.id.firstImg_ur);
            staffName = itemView.findViewById(R.id.title_ur);

        }
    }

    public void staffQualifications(String staffId, final View view1, final View view2) {

        FirebaseFirestore db = MainActivity.getFireDb();
        DocumentReference dr = db.collection("staff").document(staffId);

        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {

                if (doc.toObject(Staff.class) != null) {
                    Staff s = doc.toObject(Staff.class);
                    if (s.isFireCert())
                        view1.setVisibility(View.VISIBLE);
                    if (s.isFirstaid())
                        view2.setVisibility(View.VISIBLE);
                }

            }
        });


    }


}
