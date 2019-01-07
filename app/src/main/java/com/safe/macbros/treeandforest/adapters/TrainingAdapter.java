package com.safe.macbros.treeandforest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.dialog.TrainingDetailsD;
import com.safe.macbros.treeandforest.models.Staff;
import com.safe.macbros.treeandforest.models.Training;

import java.util.HashMap;

import javax.annotation.Nullable;

public class TrainingAdapter extends FirestoreRecyclerAdapter<Training, TrainingAdapter.ViewHolder> {

    FirebaseFirestore db = MainActivity.getFireDb();
    Context context;
    Staff staff = new Staff();
    TailgateHelper tHelper;
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();

    public TrainingAdapter(@NonNull FirestoreRecyclerOptions<Training> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Training model) {
        if (model.getQualId() != null) {
            holder.subtitle.setVisibility(View.VISIBLE);
            holder.subtitle.setText(model.getQualId());
        }
        if(!model.isCompleted())
            holder.complete.setColorFilter(ContextCompat.getColor(context, R.color.common_google_signin_btn_text_light_focused));
        subtitle(holder.title, model.getStaffId(), holder.image);
        if (model.getCreated() != null)
            holder.date.setText(tHelper.dateToString(model.getCreated().toDate()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meth.sendDialogMessage(new TrainingDetailsD(), TrainingDetailsD.getTAG(), message);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        tHelper = new TailgateHelper(context);
        meth = (Methods) context;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.universal_row, viewGroup, false);

        return new ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, subtitle, date;
        LinearLayout parent;
        LinearLayout iconLayout;
        ImageView complete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            complete = itemView.findViewById(R.id.complete_ur);
            image = itemView.findViewById(R.id.image_ur);
            parent = itemView.findViewById(R.id.parent_ur);
            title = itemView.findViewById(R.id.title_ur);
            subtitle = itemView.findViewById(R.id.subtitle_ur);
            date = itemView.findViewById(R.id.sideTitle_ur);
            iconLayout = itemView.findViewById(R.id.iconLayout);
            iconLayout.setVisibility(View.VISIBLE);
        }
    }

    public void subtitle(final TextView view, String id, final ImageView image) {


        view.setVisibility(View.VISIBLE);
        DocumentReference dr = db.collection("staff").document(id);

        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                staff = doc.toObject(Staff.class);
                view.setText(staff.getName());
                if (staff.getOfflineImageUrl() != null)
                    Glide.with(context).load(staff.getOfflineImageUrl()).into(image);

            }
        });

    }


}





















