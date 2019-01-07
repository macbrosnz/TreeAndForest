package com.safe.macbros.treeandforest.Activities.tailgate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.tabs.DailyMeetings;
import com.safe.macbros.treeandforest.Activities.tailgate.tabs.MonthlyMeetings;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TailgateMain extends Fragment implements View.OnClickListener {
    private static final String TAG = "TailgateMain";

    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    TailgateHelper tHelper;


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        meth = (Methods) context;
        tHelper = new TailgateHelper(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tailgate_main, container, false);
        tHelper.newToolbar("Safety", 0, null, null, "TailgateMain", message, getActivity());

        return view;
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onClick(View v) {

    }
}
