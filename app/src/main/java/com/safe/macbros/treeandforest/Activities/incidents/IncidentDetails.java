package com.safe.macbros.treeandforest.Activities.incidents;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.SafetyHelper;
import com.safe.macbros.treeandforest.models.Incident;

import java.util.HashMap;

public class IncidentDetails extends DialogFragment {
    private static final String TAG = "IncidentDetails";

    //vars
    SafetyHelper sHelper = new SafetyHelper();
    TailgateHelper tHelper;
    Methods meth;
    HashMap<String,Object> message = new HashMap<>();
    Incident mIncident = new Incident();
    String staffName;
    //widgets
    TextView title, subTitle, happened, happenedTitle, causeTitle,  cause, preventionTitle, prevention;
    CardView card1, card2, card3;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = this.getArguments();
        meth = (Methods)context;
        if (bundle == null) {

            meth.sendFragMessage(new IncidentsMain(), IncidentsMain.getTAG(), message);

        }else{
            message = (HashMap<String, Object>)bundle.getSerializable(getTAG());
            mIncident = (Incident)message.get("incident");

        }bundle.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tHelper = new TailgateHelper(container.getContext());
        Toolbar toolbar = tHelper.newToolbar("Details", 0, new IncidentsMain(), IncidentsMain.getTAG(), getTAG(), message, getActivity());
        View view = inflater.inflate(R.layout.universal_details, container, false);
initUI(view);
        return view;
    }

    public void initUI(View view){

        title = view.findViewById(R.id.title_ud);
        subTitle = view.findViewById(R.id.subtitle_ud);

        card1 = view.findViewById(R.id.card1_ud);
        happened = view.findViewById(R.id.detailsCard1_ud);
        happenedTitle = view.findViewById(R.id.titleCard1_ud);

        card2 = view.findViewById(R.id.card2_ud);
        causeTitle = view.findViewById(R.id.titleCard2_ud);
        cause = view.findViewById(R.id.detailsCard2_ud);

        card3 = view.findViewById(R.id.card3_ud);
        preventionTitle = view.findViewById(R.id.titleCard3_ud);
        prevention = view.findViewById(R.id.detailsCard3_ud);

        updateView();

    }

    public void updateView(){

        title.setText(mIncident.getTitle());
        sHelper.makeVisible(subTitle);
        if(mIncident.getStaffName()!=null)
        staffName = mIncident.getStaffName();

        subTitle.setText(mIncident.getSiteName()+", "+ staffName);

        happenedTitle.setText("What Happened");
        sHelper.makeVisible(happened);
        happened.setText(mIncident.getWhatHappened());

        sHelper.makeVisible(card2);
        sHelper.makeVisible(cause);
        causeTitle.setText("What was the main cause?");
        cause.setText(mIncident.getMainCause());

        sHelper.makeVisible(card3);
        sHelper.makeVisible(prevention);
        preventionTitle.setText("What can be done to prevent this from happening again?");
        prevention.setText(mIncident.getPrevention());

    }


    public static String getTAG() {
        return TAG;
    }
}
