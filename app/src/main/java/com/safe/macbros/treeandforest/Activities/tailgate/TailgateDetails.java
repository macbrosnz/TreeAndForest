package com.safe.macbros.treeandforest.Activities.tailgate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.HazardsAdaptor;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.StaffAdapter;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.TailgateVisitorAdapter;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.UpdateDriver;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.VisitorsForm;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.PdfViewer;
import com.safe.macbros.treeandforest.dialog.CreateNewHazard;
import com.safe.macbros.treeandforest.models.Driver;
import com.safe.macbros.treeandforest.models.Hazards;
import com.safe.macbros.treeandforest.models.Sites;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.models.Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

@IgnoreExtraProperties
public class TailgateDetails extends Fragment implements View.OnClickListener {
    private static final String TAG = "TailgateDetails";
    //vars
    HashMap<String, Object> message = new HashMap<>();
    Methods meth;
    ArrayList<StaffTailgate> staffList = new ArrayList<>();
    TailgateHelper mTailgateHelper;
    InputMethodManager imm;
    Map<String, Object> map = new HashMap<>();
    //models
    Tailgate tailgate;
    Driver driver;

    //adapters
    HazardsAdaptor hazardsAdapter;
    StaffAdapter staffAdapter;
    TailgateVisitorAdapter visitorAdapter;
    //widget
    CheckBox workPass, driverPass, planPass, staffPass, hazPass, emergencyPass, issuesPass, superPass, visitorPass;
    ImageView updateHazards, updateVisitor, updateDriver, taskImage, mapImage, safetree;
    RecyclerView hazardsRecycler, staffRecycler, visitorRecycler;
    TextView blockName, tailDate, planfortoday, forest, gps, radio, hoursText, driverText, noVisitors, target,
            updatePlan, updateEmergency, updateIssues, updateSuperNote, siteArea;
    FloatingActionButton keyboard;
    EditText emergency, issues, supervisorNote, startTime, finishTime;

    //firebase
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    FirebaseFirestore db = MainActivity.getFireDb();
    CollectionReference cr;

    private DocumentSnapshot mLastQueriedDocument;


    public TailgateDetails() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTailgateHelper = new TailgateHelper(context);
        Log.d(TAG, "onAttach: network");
        db.setFirestoreSettings(settings);

        meth = (Methods) getActivity();
        Bundle bundle = this.getArguments();
        bundle.setClassLoader(getClass().getClassLoader());
        if (bundle != null) {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            tailgate = ((Tailgate) message.get("tailgate"));
            bundle.clear();

        } else
            meth.sendFragMessage(new TailgateMain(), TailgateMain.getTAG(), message);

        Log.d(TAG, "onAttach: ");
        imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

    }

    @Override
    public void onStart() {
        super.onStart();
        staffAdapter.startListening();
        hazardsAdapter.startListening();
        visitorAdapter.startListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: network");

        Toolbar toolbar = mTailgateHelper.newToolbar("Details", 0, new TailgateMain(), TailgateMain.getTAG(), getTAG(), message, getActivity());
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_ic_alert);
        mTailgateHelper.checkForAlerts(menuItem);
        View view = inflater.inflate(R.layout.tailgate_details, container, false);

        initUI(view);
        getSiteDetails();
        initStaff();
        staffRVSetup();
        hazardsRVSetup();
        visitorRVSetup();

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.taskimage_taildetails: {
            }
            break;
            case R.id.map_button_taildetails: {
                DocumentReference dr = db.collection("sites").document(tailgate.getBlockId());

                dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        Sites s = doc.toObject(Sites.class);
                        Log.d(TAG, "onComplete: pdfPath" + s.getOnlinePdfPath());
                        message.put("onlinePath", s.getOnlinePdfPath());
                        meth.sendFragMessage(new PdfViewer(), PdfViewer.getTAG(), message);

                    }
                });

            }
            break;
            case R.id.updateVisitor: {
                meth.sendDialogMessage(new VisitorsForm(), VisitorsForm.getTAG(), message);
                Toast.makeText(getContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();

            }
            break;
            case R.id.updatePlan: {

                updateClicked("plan", planfortoday.getText().toString());
                setCheckbox(planPass, planfortoday.getText().toString());
                Toast.makeText(getContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();


            }
            break;
            case R.id.updateEmergency: {

                updateClicked("emergencyProcedure", emergency.getText().toString());
                setCheckbox(emergencyPass, emergency.getText().toString());
                Toast.makeText(getContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();


            }
            break;
            case R.id.updateDriver: {
                message.put("driver", driver);
                meth.sendDialogMessage(new UpdateDriver(), UpdateDriver.getTAG(), message);
                Toast.makeText(getContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();

            }
            case R.id.updateIssues: {

                updateClicked("issues", issues.getText().toString());
                setCheckbox(issuesPass, issues.getText().toString());
                Toast.makeText(getContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();


            }
            break;
            case R.id.updateSuperNote: {

                updateClicked("supervisorNote", supervisorNote.getText().toString());
                setCheckbox(superPass, supervisorNote.getText().toString());
                Toast.makeText(getContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.updateHazards: {


                message.put("siteId", tailgate.getBlockId());
                meth.sendDialogMessage(new CreateNewHazard(), CreateNewHazard.getTAG(), message);
            }
            break;
            case R.id.safeTree: {

                message.put("onlinePath", "/pdf/five_steps.pdf");
                meth.sendFragMessage(new PdfViewer(), PdfViewer.getTAG(), message);
            }

        }
    }

    private void initUI(View view) {
        //checkboxes
        workPass = view.findViewById(R.id.workPass);
        driverPass = view.findViewById(R.id.driverPass);
        planPass = view.findViewById(R.id.planPass);
        staffPass = view.findViewById(R.id.staffPass);
        hazPass = view.findViewById(R.id.hazPass);
        emergencyPass = view.findViewById(R.id.emergencyPass);
        issuesPass = view.findViewById(R.id.issuesPass);
        superPass = view.findViewById(R.id.supervisorPass);
        visitorPass = view.findViewById(R.id.visitorPass);

        //textviews
        siteArea = view.findViewById(R.id.area_taildetails);
        startTime = view.findViewById(R.id.startTime);
        finishTime = view.findViewById(R.id.finishTime);
        hoursText = view.findViewById(R.id.hoursText);
        driverText = view.findViewById(R.id.driverText);
        radio = view.findViewById(R.id.radio_taildetails);
        forest = view.findViewById(R.id.forest_taildetails);
        gps = view.findViewById(R.id.gps_taildetails);
        planfortoday = view.findViewById(R.id.plan_taildetails);
        tailDate = view.findViewById(R.id.date_taildetails);
        blockName = view.findViewById(R.id.title_taildetails);
        emergency = view.findViewById(R.id.emergency);
        issues = view.findViewById(R.id.issues);
        supervisorNote = view.findViewById(R.id.supervisorNote);
        noVisitors = view.findViewById(R.id.noVisitors);
        updateVisitor = view.findViewById(R.id.updateVisitor);
        updatePlan = view.findViewById(R.id.updatePlan);
        updateEmergency = view.findViewById(R.id.updateEmergency);
        updateIssues = view.findViewById(R.id.updateIssues);
        updateSuperNote = view.findViewById(R.id.updateSuperNote);
        target = view.findViewById(R.id.target_taildetails);
        //images
        mapImage = view.findViewById(R.id.map_button_taildetails);
        taskImage = view.findViewById(R.id.taskimage_taildetails);
        updateDriver = view.findViewById(R.id.updateDriver);
        updateHazards = view.findViewById(R.id.updateHazards);
        safetree = view.findViewById(R.id.safeTree);
        //recyclers
        hazardsRecycler = view.findViewById(R.id.hazardsAdapter_tailgate);
        staffRecycler = view.findViewById(R.id.staffAdapter_tailgate);
        visitorRecycler = view.findViewById(R.id.visitorRecycler);

        setCheckbox(workPass, tailgate.getStartTime());
        setCheckbox(driverPass, "pass");
        setCheckbox(planPass, tailgate.getPlan());
        setCheckbox(hazPass, "pass");
        setCheckbox(emergencyPass, tailgate.getEmergencyProcedure());
        setCheckbox(issuesPass, tailgate.getIssues());
        setCheckbox(superPass, tailgate.getSupervisorNote());
        setCheckbox(visitorPass, "pass");
        setCheckbox(staffPass, "");

        mapImage.setOnClickListener(this);
        blockName.setText(tailgate.getBlockName());
        //images
        taskImage.setOnClickListener(this);
        safetree.setOnClickListener(this);
        updateVisitor.setOnClickListener(this);
        updateDriver.setOnClickListener(this);
        updateHazards.setOnClickListener(this);
        keyboard = view.findViewById(R.id.keyboard);
        keyboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: ");

                imm.hideSoftInputFromWindow(planfortoday.getWindowToken(), 0);

            }
        });
        loadViews();
    }


    private void loadViews() {
        areThereVisitors();
        planfortoday.setText(tailgate.getPlan());
        emergency.setText(tailgate.getEmergencyProcedure());
        tailDate.setText(mTailgateHelper.TimestampToString(tailgate.getTimestamp()));
        issues.setText(tailgate.getIssues());
        supervisorNote.setText(tailgate.getSupervisorNote());
        if (tailgate.getTaskId().equalsIgnoreCase("CDMIlnFink5WRK4WeBLb")) {
            Log.d(TAG, "loadViews: taskId = " + tailgate.getTaskId());
            Glide.with(getActivity()).load(R.drawable.spray_icon).into(taskImage);
        } else
            Glide.with(getActivity()).load(R.drawable.chainsaw1_task).into(taskImage);
        ifNull(startTime, tailgate.getStartTime());
        ifNull(finishTime, tailgate.getFinishTime());
        ifNull(emergency, tailgate.getEmergencyProcedure());
        ifNull(issues, tailgate.getIssues());
        ifNull(supervisorNote, tailgate.getSupervisorNote());
        driver(driverText, hoursText);
        //updates
        updatePlan.setOnClickListener(this);
        updateEmergency.setOnClickListener(this);
        updateIssues.setOnClickListener(this);
        updateSuperNote.setOnClickListener(this);

    }

    private void setCheckbox(CheckBox checkbox, String s) {

        checkbox.setEnabled(false);
        if (s != null)
            if (s.length() > 1)
                checkbox.setChecked(true);
            else
                checkbox.setChecked(false);
    }

    public void updateClicked(final String type, final String string) {
        HashMap<String, Object> map = new HashMap<>();
        DocumentReference dr = db.collection("tailgates").document(tailgate.getId());
        map.put(type, string);
        dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onSuccess: " + type + " = " + string);
            }
        });

    }

    public void driver(final TextView name, final TextView hours) {
        CollectionReference cr = db.collection("drivers");
        Query query = cr.whereEqualTo("tailgateId", tailgate.getId());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot docs, @Nullable FirebaseFirestoreException e) {
                for (int i = 0; i < docs.size(); i++) {
                    Log.d(TAG, "onEvent: inside driverTailgate");
                    driver = docs.getDocuments().get(i).toObject(Driver.class);
                    name.setText(driver.getName());
                    hours.setText(String.valueOf(driver.getHours()));
                }
            }
        });
    }

    private void getSiteDetails() {

        DocumentReference dr = db.collection("sites").document(tailgate.getBlockId());
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {

                Sites sites = doc.toObject(Sites.class);
                if (sites.getGps() != null)
                    gps.setText(sites.getGps());
                if (sites.getForest() != null)
                    forest.setText(sites.getForest());
                if (sites.getTarget() != null)
                    target.setText(sites.getTarget());
                if (sites.getRadio() != null)
                    radio.setText(sites.getRadio());
                siteArea.setText(sites.getArea());
            }
        });
    }

    private void initStaff() {
        CollectionReference cr = db.collection("tailgates").document(tailgate.getId()).collection("staffChecks");
        Query tailgateQuery = null;
        if (mLastQueriedDocument != null) {
            tailgateQuery = cr.orderBy("name", Query.Direction.ASCENDING).startAfter(mLastQueriedDocument);
        } else {
            tailgateQuery = cr.orderBy("name", Query.Direction.ASCENDING);
        }
        tailgateQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    StaffTailgate staff = doc.toObject(StaffTailgate.class);
                    staffList.add(staff);
                }
            }
        });

    }

    public void staffRVSetup() {
        Log.d(TAG, "initRecycler: started");
        cr = db.collection("tailgates").document(tailgate.getId()).collection("staffChecks");
        Query query = cr.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<StaffTailgate> options = new FirestoreRecyclerOptions.Builder<StaffTailgate>()
                .setQuery(query, StaffTailgate.class)
                .build();


        staffAdapter = new StaffAdapter(options, message, tailgate, getActivity());
        staffRecycler.setAdapter(staffAdapter);
        staffRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void hazardsRVSetup() {
        cr = db.collection("sites").document(tailgate.getBlockId()).collection("hazards");

        Query query = cr.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Hazards> options = new FirestoreRecyclerOptions.Builder<Hazards>()
                .setQuery(query, Hazards.class)
                .build();

        hazardsAdapter = new HazardsAdaptor(options);
        hazardsRecycler.setAdapter(hazardsAdapter);
        hazardsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void visitorRVSetup() {
        cr = db.collection("visitors");

        Query query = cr.whereEqualTo("tailgateId", tailgate.getId());

        FirestoreRecyclerOptions<Visitor> options = new FirestoreRecyclerOptions.Builder<Visitor>()
                .setQuery(query, Visitor.class)
                .build();

        visitorAdapter = new TailgateVisitorAdapter(options, tailgate);
        visitorRecycler.setAdapter(visitorAdapter);
        visitorRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    public void areThereVisitors() {
        CollectionReference cr = db.collection("visitors");

        Query query = cr.whereEqualTo("tailgateId", tailgate.getId());

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0)
                    noVisitors.setVisibility(View.GONE);
                else
                    noVisitors.setText("No visitors today");
            }
        });

    }

    public void ifNull(TextView text, String string) {
        if (string != null) {
            text.setText(string);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hazardsAdapter.stopListening();
        staffAdapter.stopListening();
        visitorAdapter.stopListening();
        if (imm.isActive())
            imm.hideSoftInputFromWindow(planfortoday.getWindowToken(), 0);
    }

    public static String getTAG() {
        return TAG;
    }

}


