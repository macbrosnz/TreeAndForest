package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.models.Tailgate;


import java.util.HashMap;

public class ShowImage extends Fragment {
    private static final String TAG = "ShowScalableImage";
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    StaffTailgate staff = new StaffTailgate();
    Tailgate tailgate = new Tailgate();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods)context;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = (HashMap)bundle.getSerializable(getTAG());
            tailgate = (Tailgate)message.get("tailgate");
            staff=(StaffTailgate)message.get("staff");
            bundle.clear();


        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.show_imageview, container, false);
        ImageView imageView = v.findViewById(R.id.fullscreen_image);
        Log.d(TAG, "onCreateView: signUrl" + staff.getSignUrl());
        Glide.with(getActivity()).load(staff.getSignUrl()).into(imageView);
        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setTitle(staff.getName() + " Signature" );
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);
            }
        });

        return v;
    }




    public static String getTAG() {
        return TAG;
    }
}
