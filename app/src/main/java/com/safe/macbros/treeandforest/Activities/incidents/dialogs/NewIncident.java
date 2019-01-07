package com.safe.macbros.treeandforest.Activities.incidents.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.incidents.IncidentsMain;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Incident;
import com.safe.macbros.treeandforest.models.Staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewIncident extends Fragment {
    private static final String TAG = "NewIncident";


    //widgets
    Spinner spinner, locationSpinner;
    FloatingActionButton keyboard;
    EditText description, cause, prevention, location, title;
    MenuItem menuItem;
    CheckBox medical, firstAid;
    ArrayAdapter<String> dataAdapter, locationAdapter;


    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    String type;
    ArrayList<Staff> staffList = new ArrayList<>();
    TailgateHelper tHelper;
    Context mContext;
    Fragment fragment;
    String tag;


    //firebase
    FirebaseFirestore db = MainActivity.getFireDb();


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        tHelper = new TailgateHelper(context);
        meth = (Methods) context;
        mContext = context;
        Bundle bundle = this.getArguments();

        if (bundle == null) {

            meth.sendFragMessage(new IncidentsMain(), IncidentsMain.getTAG(), message);

        } else {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            type = (String) message.get("type");
            fragment = (Fragment)message.get("fragment");
            tag = (String)message.get("tag");
            staffList = (ArrayList<Staff>) message.get("staffList");
        }

    }

    @Override
    public void onStop() {

        super.onStop();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(description.getWindowToken(), 0);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.newincident, container, false);

        initUI(view);

        return view;

    }


    public void initUI(View view) {

        spinner = view.findViewById(R.id.spinner_newIncident);

        keyboard = view.findViewById(R.id.fab_newincident);

        description = view.findViewById(R.id.description_newIncident);

        firstAid = view.findViewById(R.id.firstAid_newincident);

        medical = view.findViewById(R.id.medical_newincident);

        cause = view.findViewById(R.id.cause_newIncident);

        prevention = view.findViewById(R.id.prevention_newIncident);

        location = view.findViewById(R.id.locationEt_newincident);

        locationSpinner = view.findViewById(R.id.location_newIncident);

        title = view.findViewById(R.id.title_newincident);

        keyboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: ");

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(description.getWindowToken(), 0);

            }
        });

        Toolbar toolbar = tHelper.newToolbar(type + " Form", R.menu.menu_main, fragment, tag, getTAG()
                , message, getActivity());

        setMenuItem(toolbar);

        spinAdaptor();
        locationAdapter();

    }

    public void setMenuItem(final Toolbar toolbar) {

        menuItem = toolbar.getMenu().findItem(R.id.menu_save);
        menuItem.setVisible(true);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                saveToFire();

                Toast.makeText(mContext, "Incident has been Saved", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    }

    public void saveToFire() {

        DocumentReference dr = db.collection("incidents").document();

        Incident incident = new Incident();
        incident.setId(dr.getId());
        incident.setFirstAid(firstAid.isChecked());
        incident.setMedical(medical.isChecked());
        incident.setWhatHappened(description.getText().toString());
        incident.setMainCause(cause.getText().toString());
        incident.setPrevention(cause.getText().toString());
        incident.setType(type);
        incident.setCreated(Timestamp.now());
        incident.setTitle(title.getText().toString());
        int i = spinner.getSelectedItemPosition();
        if (i > 0) {
            incident.setStaffId(staffList.get(i - 1).getId());
            incident.setStaffName(staffList.get(i - 1).getName());
        }
        if (locationSpinner.getSelectedItem().toString() != "none")
            incident.setSiteName(locationSpinner.getSelectedItem().toString());
        else {
            incident.setSiteName(location.getText().toString());
        }
        dr.set(incident).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: Saved in Firebase");
            }
        });

    }

    public void spinAdaptor() {

        List<String> list = new ArrayList<>();
        list.add("none");

        for (int i = 0; i < staffList.size(); i++) {
            list.add(staffList.get(i).getName());
        }

        dataAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    public void locationAdapter() {
        final List<String> list = new ArrayList<>();
        list.add("none");
        CollectionReference cr = db.collection("tailgates");
        Query query = cr.orderBy("timestamp", Query.Direction.DESCENDING).limit(5);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot docs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (docs != null) {
                    for (int i = 0; i < docs.size(); i++) {
                        Tailgate t = new Tailgate();

                        t = docs.getDocuments().get(i).toObject(Tailgate.class);
                        if(!list.contains(t.getBlockName()))
                        list.add(t.getBlockName());

                    }
                    locationAdapter = new ArrayAdapter<>(mContext,
                            android.R.layout.simple_spinner_item, list);

                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    locationSpinner.setAdapter(locationAdapter);
                }
            }
        });


    }

    public static String getTAG() {
        return TAG;
    }
}

































