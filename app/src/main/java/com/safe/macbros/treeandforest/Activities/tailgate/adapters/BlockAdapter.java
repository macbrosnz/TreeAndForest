package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Sites;

import java.util.HashMap;

public class BlockAdapter extends FirestoreRecyclerAdapter<Sites, BlockAdapter.BlockHolder> {
    private static final String TAG = "BlockAdapter";
    public Sites mSites;
    private Methods meth;
    private HashMap<String, Object> message= new HashMap<>();
    private Context context;

    public BlockAdapter(@NonNull FirestoreRecyclerOptions<Sites> options, HashMap<String, Object> message, Context context ) {
        super(options);
        this.message = message;
        this.context = context;

    }
    /**/
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        meth = (Methods)context;

    }

    @Override
    protected void onBindViewHolder(@NonNull BlockAdapter.BlockHolder holder, int position, @NonNull final Sites model) {

        holder.title.setText(model.getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @NonNull
    @Override
    public BlockAdapter.BlockHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_textadapter,
                viewGroup, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new BlockAdapter.BlockHolder(v);

    }

    class BlockHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView parent;
        public BlockHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "BlockHolder: ");
            title=itemView.findViewById(R.id.title_textadapter);
            parent=itemView.findViewById(R.id.parent_textadapter);
        }
    }


}
