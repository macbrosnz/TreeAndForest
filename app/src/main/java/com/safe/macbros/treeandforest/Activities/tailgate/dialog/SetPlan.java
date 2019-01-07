package com.safe.macbros.treeandforest.Activities.tailgate.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.Activities.tailgate.models.EquipmentTailgate;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Alert;
import com.safe.macbros.treeandforest.models.Driver;
import com.safe.macbros.treeandforest.models.Equipment;
import com.safe.macbros.treeandforest.models.Note;
import com.safe.macbros.treeandforest.models.Sites;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.models.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class SetPlan extends Fragment implements View.OnClickListener {
    private static final String TAG = "SetPlan";

    Methods meth;
    TextView nextBut, leaveBut;
    EditText plan;

    //vars
    HashMap<String, Object> message = new HashMap<>();
    ArrayList<StaffTailgate> staffList = new ArrayList<>();
    ArrayList<Equipment> equipmentList = new ArrayList<>();
    ArrayList<String> plbStaffIds = new ArrayList<>();
    ArrayList<EquipmentTailgate> equipment = new ArrayList<>();
    Tailgate newTailgate = new Tailgate();
    Driver driver = new Driver();

    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SetPlan() {
        super();

    }

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        db.setFirestoreSettings(settings);
        mContext = context;
        message.clear();
        meth = (Methods) getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            equipmentList = (ArrayList<Equipment>) message.get("equipment");
            driver = (Driver)message.get("driver");
            plbStaffIds = (ArrayList<String>)message.get("plbStaffIds");
            if ((Tailgate) message.get("tailgate") != null)
                newTailgate = (Tailgate) message.get("tailgate");
            bundle.clear();
        }


    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FIRE", "Plan for today is live");
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.set_plan, container, false);
        initUI(view);

        return view;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_check_next: {

                newTailgate();
                message.put("staff", staffList);
                message.put("tailgate", newTailgate);
                message.put("equipment", equipment);
                meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);
                addHitToTaskId(newTailgate.getTaskId(), newTailgate.getBlockId());
                hideKeyboard(getActivity());

            }
            break;
            case R.id.plan_check_dismiss: {
                meth.sendFragMessage(new TailgateMain(), TailgateMain.getTAG(), message);
                hideKeyboard(getActivity());
            }
            break;

        }

    }


    @Override
    public void onDetach() {
        super.onDetach();


    }

    public void initUI(View view) {
        nextBut = view.findViewById(R.id.plan_check_next);
        nextBut.setOnClickListener(this);
        leaveBut = view.findViewById(R.id.plan_check_dismiss);
        leaveBut.setOnClickListener(this);
        plan = view.findViewById(R.id.plan_check_et);

    }

    public void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getTAG() {
        return TAG;
    }

    public void newTailgate() {

        staffList.clear();
        
        DocumentReference newTailgateRef = db.collection("tailgates").document();
        
        DocumentReference driverRef = db.collection("drivers").document(driver.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("tailgateId", newTailgateRef.getId());
        driverRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: driverupdated");     
            }
        });

        newTailgate.setId(newTailgateRef.getId());

        newTailgate.setPlan(plan.getText().toString());

        newTailgate.setCompleted(false);

        newTailgate.setTimestamp(Timestamp.now());

        newTailgateRef.set(newTailgate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


            }
        });

        for (int i = 0; i < newTailgate.getStaffIds().size(); i++) {

            DocumentReference dr = db.collection("tailgates").document(newTailgate.getId())
                    .collection("staffChecks").document(newTailgate.getStaffIds().get(i));

            StaffTailgate staff = new StaffTailgate();
            staff.setId(dr.getId());
            staff.setName(newTailgate.getStaff().get(i));
            staff.setEquipmentPass(false);
            staff.setPlanPass(false);
            staff.setSignPass(false);
            if(plbStaffIds.contains(dr.getId()))
                staff.setPlb(true);
            else
                staff.setPlb(false);
            staffList.add(staff);
            Log.d(TAG, "newTailgate: StaffTailgate plb " + staff.isPlb() + plbStaffIds + " staffId " + staff.getId() );
            dr.set(staff).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }

        for (int i = 0; i < newTailgate.getStaffIds().size(); i++) {
            for (int j = 0; j < equipmentList.size(); j++) {

                DocumentReference dr = db.collection("tailgates").document(newTailgate.getId())
                        .collection("staffChecks").document(newTailgate.getStaffIds().get(i))
                        .collection("equipment").document(equipmentList.get(j).getId());
                EquipmentTailgate equipmentTailgate = new EquipmentTailgate();

                equipmentTailgate.setId(equipmentList.get(j).getId());
                equipmentTailgate.setName(equipmentList.get(j).getName());
                equipmentTailgate.setStandards(equipmentList.get(j).getStandards());
                equipmentTailgate.setPpe(equipmentList.get(j).getPpe());
                equipmentTailgate.setPass(false);
                equipmentTailgate.setTailgateId(newTailgate.getId());
                equipmentTailgate.setStaffId(newTailgate.getStaffIds().get(i));

                equipment.add(equipmentTailgate);

                dr.set(equipmentTailgate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                
                final int finalI = i;
                final int finalJ = j;

                fireEquipmentAlert(newTailgate.getId(), newTailgate.getStaffIds().get(finalI), equipmentList.get(finalJ).getId());

                fireEquipmentNote(newTailgate.getId(), newTailgate.getStaffIds().get(finalI), equipmentList.get(finalJ).getId());

            }

        }

    }

    public void fireEquipmentAlert(String tailId, String staffId, String equipId) {
        final DocumentReference dr = db.collection("tailgates").document(tailId)
                .collection("staffChecks").document(staffId)
                .collection("equipment").document(equipId);

        final Map<String, Object> map = new HashMap<>();

        EquipmentTailgate eq = new EquipmentTailgate();

        CollectionReference cr = db.collection("alerts");

        Query q = cr.whereEqualTo("complete", false)
                .whereEqualTo("staffId", staffId)
                .whereEqualTo("equipmentId", equipId);

        q.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    Alert al = doc.toObject(Alert.class);
                    if (al.getDate() != null)
                        map.put("alertDate", al.getDate());
                    map.put("note", al.getDetails());
                    map.put("alertId", al.getId());
                    dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "equipment updated " + map);
                        }
                    });

                }
            }
        });


    }

    public void fireEquipmentNote(String tailId, String staffId, String equipId) {
        final DocumentReference dr = db.collection("tailgates").document(tailId)
                .collection("staffChecks").document(staffId)
                .collection("equipment").document(equipId);

        final Map<String, Object> map = new HashMap<>();

        EquipmentTailgate eq = new EquipmentTailgate();

        CollectionReference cr = db.collection("notes");

        Query q = cr.whereEqualTo("complete", false)
                .whereEqualTo("staffId", staffId)
                .whereEqualTo("equipmentId", equipId);

        q.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    Note al = doc.toObject(Note.class);

                    map.put("note", al.getDetails());
                    map.put("noteId", al.getId());
                    dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "equipment updated " + map);
                        }
                    });

                }
            }
        });


    }

    public void addHitToTaskId(String taskId, String siteId) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference dr = db.collection("tasks").document(taskId);
        
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                Tasks t = doc.toObject(Tasks.class);
                int hits = t.getHits() + 1;
                HashMap<String, Object> update = new HashMap<>();
                update.put("hits", hits);
                dr.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: hits updated ");
                    }
                });
            }
        });


        /*final DocumentReference dr1 = db.collection("sites").document(siteId);
        dr1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                Sites s = doc.toObject(Sites.class);

                Map<String, Object> map = new HashMap<>();

                if (s.getHit() == null) {
                    map.put("hit", "1");
                } else {
                    Log.d(TAG, "onEvent: " +s.getHit());
                    int i = Integer.parseInt(s.getHit()) + 1;
                    map.put("hit", String.valueOf(i));
                }

                dr1.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: site count updated");
                    }
                });

            }
        });*/


    }
    
    @Override
    public void onStop() {
        super.onStop();
    }
}









































