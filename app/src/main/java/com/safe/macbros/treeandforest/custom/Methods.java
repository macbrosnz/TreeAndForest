package com.safe.macbros.treeandforest.custom;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.List;

public interface Methods {


    void sendFragMessage(Fragment frag, String tag, HashMap<String, Object> message);

    void sendDialogMessage(DialogFragment dialog, String tag, HashMap<String, Object> message);

    void runTabLayout(List<Fragment> fragList, List<String> titles);

}
