package com.safe.macbros.treeandforest.Activities.tasks.adapters;

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
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.TasksAdapter;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tasks.TasksDetail;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.HashMap;

public class TasksAdaptor extends FirestoreRecyclerAdapter<Tasks, TasksAdaptor.ViewHolder> {
    private static final String TAG = "TasksAdaptor";

    //vars
    Methods meth;
    TailgateHelper tHelper;
    HashMap<String,Object> message = new HashMap<>();


    public TasksAdaptor(@NonNull FirestoreRecyclerOptions<Tasks> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Tasks model) {

        holder.title.setText(model.getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message.put("task", model);
                meth.sendFragMessage(new TasksDetail(), TasksDetail.getTAG(), message);

            }
        });

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

        TextView title, days;
        ImageView image;
        CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent_tailgateAdapter);
            title = itemView.findViewById(R.id.block_tailgateAdapter);
            days = itemView.findViewById(R.id.staff_tailgateAdapter);
            image = itemView.findViewById(R.id.image_tailgateAdapter);
            image.setVisibility(View.GONE);
        }
    }

}
