package com.safe.macbros.treeandforest.Activities.emergency;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EmergencyMain extends Fragment {
    private static final String TAG = "EmergencyMain";

    //vars
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;
    TailgateHelper tHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods)context;
        tHelper = new TailgateHelper(context);

        List<Fragment> fragList = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        fragList.add(new AccidentTab());
        fragList.add(new FireTab());
        fragList.add(new HazardousSubstance());

        titles.add("ACCIDENT");
        titles.add("FIRE");
        titles.add("CHEMICAL");

    meth.runTabLayout(fragList, titles);

    }


    public static String getTAG() {
        return TAG;
    }
}
