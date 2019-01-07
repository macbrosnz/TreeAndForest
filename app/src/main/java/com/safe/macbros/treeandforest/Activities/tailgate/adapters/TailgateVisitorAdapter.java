package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

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
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateVisitorSign;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.models.Visitor;

import java.util.HashMap;

public class TailgateVisitorAdapter extends FirestoreRecyclerAdapter<Visitor, TailgateVisitorAdapter.ViewHolder> {
    private static final String TAG = "TailgateVisitorAdapter";
    Context mContext;
    Tailgate tailgate;
    Methods meth;
    HashMap<String, Object>message = new HashMap<>();

    public TailgateVisitorAdapter(@NonNull FirestoreRecyclerOptions<Visitor> options, Tailgate tailgate) {
        super(options);
        this.tailgate = tailgate;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Visitor model) {
        holder.mainImage.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(R.drawable.staff_icon).into(holder.mainImage);
        holder.title.setText(model.getName());
        if(model.getEmail()!=null) {
            holder.subtitle.setText(model.getEmail());
            holder.subtitle.setVisibility(View.VISIBLE);
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.put("visitor", model);
                message.put("tailgate", tailgate);
                meth.sendFragMessage(new TailgateVisitorSign(), TailgateVisitorSign.getTAG(), message);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        meth = (Methods)mContext;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.universal_row, viewGroup, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle;
        ImageView signed, mainImage;
    LinearLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
    parent = itemView.findViewById(R.id.parent_ur);
            title = itemView.findViewById(R.id.title_ur);
            subtitle = itemView.findViewById(R.id.subtitle_ur);
            signed = itemView.findViewById(R.id.signed);
            signed.setVisibility(View.VISIBLE);
            mainImage = itemView.findViewById(R.id.image_ur);

        }

    }

    public static String getTAG() {
        return TAG;
    }
}
