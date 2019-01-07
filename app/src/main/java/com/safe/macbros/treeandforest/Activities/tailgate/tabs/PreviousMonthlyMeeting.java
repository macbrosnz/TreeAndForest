package com.safe.macbros.treeandforest.Activities.tailgate.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safe.macbros.treeandforest.R;

import java.util.HashMap;

public class PreviousMonthlyMeeting extends Fragment implements View.OnClickListener {
    private static final String TAG = "PreviousMonthlyMeeting";

    //vars
HashMap<String, Object> message = new HashMap<>();


    //widgets


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = this.getArguments();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monthly_tailgate, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
