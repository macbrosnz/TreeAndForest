package com.safe.macbros.treeandforest.Activities.policies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safe.macbros.treeandforest.R;

public class PoliciesMain extends Fragment {
    private static final String TAG = "PoliciesMain";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.policies_main, container, false);
        return view;
    }
}
