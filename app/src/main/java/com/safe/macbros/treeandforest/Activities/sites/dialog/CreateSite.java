package com.safe.macbros.treeandforest.Activities.sites.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.safe.macbros.treeandforest.Activities.sites.SitesMain;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Sites;

import java.util.HashMap;

public class CreateSite extends DialogFragment {
    private static final String TAG = "CreateSite";

    //widgets
    EditText title, gps, radio, description;

    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    Sites mSites = new Sites();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            meth.sendFragMessage(new SitesMain(), SitesMain.getTAG(), message);
        } else {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());

        }bundle.clear();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        View view = inflater.inflate(R.layout.create_site, null);
        initUI(view);
        builder.setTitle("Create a Site")
                .setView(view)
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newSite();
                        meth.sendDialogMessage(new CreateSiteHazards(), CreateSiteHazards.getTAG(), message);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        return builder.create();
    }

    public void initUI(View view) {

        gps = view.findViewById(R.id.gps_createSite);
        title = view.findViewById(R.id.title_createSite);
        radio = view.findViewById(R.id.radio_createSite);
        description = view.findViewById(R.id.desc_createSite);

    }

    public void newSite() {

        mSites.setName(title.getText().toString());
        mSites.setRadio(radio.getText().toString());
        mSites.setGps(gps.getText().toString());
        mSites.setDetails(description.getText().toString());
        message.put("newSite", mSites);

    }

    public static String getTAG() {
        return TAG;
    }
}
