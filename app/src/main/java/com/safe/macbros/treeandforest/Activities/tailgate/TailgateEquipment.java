package com.safe.macbros.treeandforest.Activities.tailgate;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptC;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.TEquipAdapter;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.PPEAdapter;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.StaffCheck;
import com.safe.macbros.treeandforest.Activities.tailgate.models.EquipmentTailgate;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.Activities.tasks.TasksDetail;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.models.Tailgate;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TailgateEquipment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TailgateEquipment";

    //vars
    Tailgate tailgate = new Tailgate();
    StaffTailgate staff = new StaffTailgate();
    TailgateHelper tHelper;
    static PPEAdapter ppeAdapter;
    static TEquipAdapter eqAdapter;
    Methods meth;

    static HashMap<String, Object> message = new HashMap<>();
    ArrayList<EquipmentTailgate> equipmentList = new ArrayList<>();

    //widgets
    TextView staffName, jobTask, equipmentTitle;
    ImageView staffImage;
    static CheckBox ppe, equipment;
    RecyclerView equipRecycler, ppeRecycler;
    Toolbar toolbar;

    //firestore
    FirebaseFirestore db = MainActivity.getFireDb();

    CollectionReference cr;
    DocumentReference dr;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //settings loaded for firestore
        tHelper = new TailgateHelper(context);
        meth = (Methods) getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = (HashMap) bundle.getSerializable(getTAG());
            this.tailgate = (Tailgate) message.get("tailgate");
            this.staff = (StaffTailgate) message.get("staff");
            bundle.clear();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        eqAdapter.startListening();
        ppeAdapter.startListening();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.equipment_tailgate, container, false);

        initUI(view);
        eqAdapter();
        ppeAdapter();
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.allequipCheck_staffeq_checklist: {
                if (equipment.isChecked()) {

                    updateEquip(true);

                    staff.setEqCheckPass(true);

                } else

                    updateEquip(false);

                eqAdapter.notifyDataSetChanged();

            }
            break;
            case R.id.allPPE_checklist: {

                if (ppe.isChecked()) {

                    updatePpe(true);

                    staff.setPpeCheckPass(true);

                } else {

                    updatePpe(false);
                }
                ppeAdapter.notifyDataSetChanged();
            }
            break;
        }
    }

    void initUI(View view) {
        //instantiate widgets
        ppe = view.findViewById(R.id.allPPE_checklist);
        equipment = view.findViewById(R.id.allequipCheck_staffeq_checklist);
        staffName = view.findViewById(R.id.name_staffEq_checklist);
        jobTask = view.findViewById(R.id.status_staffEqChecklist);
        equipmentTitle = view.findViewById(R.id.equipTitle_checklist);
        staffImage = view.findViewById(R.id.staffImage_staffeq_checklist);

        //onClickListeners
        ppe.setOnClickListener(this);
        equipment.setOnClickListener(this);
        ppeRecycler = view.findViewById(R.id.ppeAdapter_checklist);
        equipRecycler = view.findViewById(R.id.eqAdapter_checklist);
        setWidgets();
    }


    public void setWidgets() {
        staffName.setText(staff.getName());

        jobTask.setText(tailgate.getTaskName());

        equipmentTitle.setText(tailgate.getTaskName() + " Equipment");

        if (staff.isPpeCheckPass()) {

            ppe.setEnabled(false);

            ppe.setChecked(true);

        }
        if (staff.isEqCheckPass()) {
            equipment.setEnabled(false);
            equipment.setChecked(true);
        }



    }

    public void initToolbar(){ toolbar = tHelper.newToolbar("Staff Equipment",R.menu.menu_main, new TailgateDetails(), TailgateDetails.getTAG()
            ,getTAG(), message, getActivity());

        MenuItem sItem = toolbar.getMenu().findItem(R.id.menu_save);

        sItem.setVisible(true);

        sItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                updateMessage();

                updateFirebase(ppe.isChecked(), equipment.isChecked());

                Toast.makeText(getContext(), "Equipment Check Saved", Toast.LENGTH_SHORT).show();

                meth.sendDialogMessage(new StaffCheck(), StaffCheck.getTAG(), message);
                meth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), message);

                return true;
            }
        });

        MenuItem aItem = toolbar.getMenu().findItem(R.id.menu_ic_alert);
        tHelper.checkStaffAlerts(aItem, staff.getId());
        MenuItem nItem = toolbar.getMenu().findItem(R.id.menu_ic_note);
        tHelper.checkStaffNotes(nItem, staff.getId());}

    public void eqAdapter() {
        Log.d(TAG, "eqAdapter: ");
        cr = db.collection("tailgates").document(tailgate.getId())
                .collection("staffChecks").document(staff.getId()).collection("equipment");
        Query query = cr.whereEqualTo("ppe", false);

        FirestoreRecyclerOptions<EquipmentTailgate> options = new FirestoreRecyclerOptions.Builder<EquipmentTailgate>()
                .setQuery(query, EquipmentTailgate.class)
                .build();

        eqAdapter = new TEquipAdapter(options, message);
        equipRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        equipRecycler.setAdapter(eqAdapter);


    }

    public void ppeAdapter() {

        cr = db.collection("tailgates").document(tailgate.getId())
                .collection("staffChecks").document(staff.getId()).collection("equipment");
        Query query = cr.whereEqualTo("ppe", true);

        FirestoreRecyclerOptions<EquipmentTailgate> options = new FirestoreRecyclerOptions.Builder<EquipmentTailgate>()
                .setQuery(query, EquipmentTailgate.class)
                .build();

        ppeAdapter = new PPEAdapter(options,getActivity(),message);
        ppeRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ppeRecycler.setAdapter(ppeAdapter);

    }

    public void updatePpe(final Boolean pass) {

            cr = db.collection("tailgates").document(tailgate.getId()).collection("staffChecks")
                    .document(staff.getId()).collection("equipment");

            cr.whereEqualTo("ppe", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot doc:task.getResult()) {
                        firestorePpe(pass, doc.getId());
                    }

                }
            });

    }

    public void updateEquip(final Boolean pass) {

        cr = db.collection("tailgates").document(tailgate.getId()).collection("staffChecks")
                .document(staff.getId()).collection("equipment");

        cr.whereEqualTo("ppe", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc:task.getResult()) {
                    firestoreEq(pass, doc.getId());
                }


            }
        });

    }

    public void firestorePpe(Boolean pass, String docId){
        Map<String,Object> map = new HashMap<>();
        map.put("pass", pass);
        DocumentReference dr = db.collection("tailgates").document(tailgate.getId()).collection("staffChecks")
                .document(staff.getId()).collection("equipment").document(docId);
        dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    public void firestoreEq(Boolean pass, String docId){
        Map<String,Object> map = new HashMap<>();
        map.put("pass", pass);
        DocumentReference dr = db.collection("tailgates").document(tailgate.getId()).collection("staffChecks")
                .document(staff.getId()).collection("equipment").document(docId);
        dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }


    public void updateMessage() {

        if (staff.isPpeCheckPass() && staff.isEqCheckPass()) {
            staff.setEquipmentPass(true);
        }
        message.put("staff", staff);
    }

    public void updateFirebase(Boolean ppe, Boolean eq) {
        dr = db.collection("tailgates").document(tailgate.getId())
                .collection("staffChecks").document(staff.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("ppeCheckPass", ppe);
        map.put("eqCheckPass", eq);
        if (ppe && eq)
            map.put("equipmentPass", true);
        dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Checklist Updated", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static String getTAG() {
        return TAG;
    }

    public static HashMap<String, Object> getMessage() {
        return message;
    }


    public  CheckBox getPpe() {
        return ppe;
    }

    public  CheckBox getEquipment() {
        return equipment;
    }

    @Override
    public void onStop() {
        super.onStop();
        eqAdapter.stopListening();
        ppeAdapter.stopListening();

    }
}




