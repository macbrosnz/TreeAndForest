package com.safe.macbros.treeandforest.Activities.equipment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Equipment;

import java.util.HashMap;

public class EquipmentDetail extends Fragment {
    private static final String TAG = "EquipmentDetail";

    //vars
    HashMap<String,Object>message = new HashMap<>();
    Equipment mEquipment = new Equipment();
    Methods meth;

    //fire
    FirebaseFirestore db = MainActivity.getFireDb();



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth=(Methods)context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            meth.sendFragMessage(new EquipmentMain(), EquipmentMain.getTAG(), message);
        }else{
            message = (HashMap<String, Object>)bundle.getSerializable(getTAG());
            mEquipment = (Equipment)message.get("equipment");

        }bundle.clear();
    }

    public static String getTAG() {
        return TAG;
    }
}
