package com.safe.macbros.treeandforest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Alert;
import com.safe.macbros.treeandforest.models.Note;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AlertsDialog extends DialogFragment {
    private static final String TAG = "AlertsDialog";


    //widgets
    RecyclerView mRecyclerView;
    AlertAdapter mAlertAdapter;
    noteAdapter mNoteAdapter;

    //vars
    Context mContext;
    boolean alert, staff, tailgate, equipment, note;

    String mStaffId, mEquipId;

    HashMap<String, Object> message = new HashMap<>();

    @Override
    public void onStart() {
        super.onStart();
        if(alert)
        mAlertAdapter.startListening();
        else
        mNoteAdapter.startListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());

            mStaffId = (String) message.get("alertStaffId");

            mEquipId = (String)message.get("alertEquipId");

            alert = (boolean)message.get("alertBoolean");

            staff = (boolean)message.get("staffBoolean");

            tailgate = (boolean)message.get("tailgateBoolean");

            equipment = (boolean)message.get("equipmentBoolean");

            note = (boolean)message.get("noteBoolean");

            mContext = (Context) message.get("context");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recyclerview, null);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        initRecycler();
        String title = "";
        builder.setTitle(title)
                .setView(view)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();

                    }

                });
        return builder.create();

    }

    @Override
    public void onStop() {
        super.onStop();
        if(alert)
        mAlertAdapter.stopListening();
        else
        mNoteAdapter.stopListening();
    }

    public void initRecycler() {






        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d(TAG, "initRecycler: " + alert + " staff:" + staff);

        if (alert&&staff) {
            Log.d(TAG, "initRecycler: querying alert and staff" + mStaffId );

            CollectionReference cr = db.collection("alerts");

            Query query = cr.whereEqualTo("complete", false)
                    .whereEqualTo("staffId", mStaffId)
                    .orderBy("timestamp", Query.Direction.ASCENDING);


            FirestoreRecyclerOptions<Alert> options = new FirestoreRecyclerOptions.Builder<Alert>()
                    .setQuery(query, Alert.class)
                    .build();


            mAlertAdapter = new AlertAdapter(options);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

            mRecyclerView.setAdapter(mAlertAdapter);
        }
        else if(note&&staff)
        {
            Log.d(TAG, "initRecycler: note query and staff");
            CollectionReference cr = db.collection("notes");

            Query query = cr.whereEqualTo("complete", false)
                    .whereEqualTo("staffId", mStaffId)
                    .orderBy("timestamp", Query.Direction.ASCENDING);


            FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                    .setQuery(query, Note.class)
                    .build();


            mNoteAdapter = new noteAdapter(options);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

            mRecyclerView.setAdapter(mNoteAdapter);

        }
        else{
            Log.d(TAG, "initRecycler: allAlerts");
            CollectionReference cr = db.collection("alerts");

            Query query = cr.whereEqualTo("complete", false)
                    .orderBy("date", Query.Direction.ASCENDING);


            FirestoreRecyclerOptions<Alert> options = new FirestoreRecyclerOptions.Builder<Alert>()
                    .setQuery(query, Alert.class)
                    .build();


            mAlertAdapter = new AlertAdapter(options);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

            mRecyclerView.setAdapter(mAlertAdapter);
        }


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
if(alert)
                mAlertAdapter.itemCompleted(viewHolder.getAdapterPosition());
else
    mNoteAdapter.itemCompleted(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(mRecyclerView);

    }

    class noteAdapter extends FirestoreRecyclerAdapter<Note, noteAdapter.ViewHolder> {
        private static final String TAG = "AlertAdapter";

        //firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true).build();

        //vars
        TailgateHelper mTailgateHelper;
        Methods meth;

        public noteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Note model) {

            holder.image.setVisibility(View.GONE);

            if (model.getTimestamp() != null) {
                holder.date.setText(mTailgateHelper.datetoDayString(model.getTimestamp().toDate()));
            }
            holder.details.setText(model.getTitle());
            getDetails(model.getStaffId(), model.getEquipmentId(), holder.title);

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            meth = (Methods) viewGroup.getContext();
            mTailgateHelper  = new TailgateHelper(viewGroup.getContext());
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_tailgateadapter, viewGroup, false);
            return new ViewHolder(view);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, date, details;
            ImageView image;

            public ViewHolder(@NonNull View view) {
                super(view);
                image = view.findViewById(R.id.image_tailgateAdapter);
                title = view.findViewById(R.id.block_tailgateAdapter);
                details = view.findViewById(R.id.staff_tailgateAdapter);
                date = view.findViewById(R.id.date_tailgateAdapter);

            }
        }

        public void getDetails(String staffId, final String equipmentId,
                               final TextView titleText) {

            db.setFirestoreSettings(settings);

            if (staffId != null) {
                Query query = db.collection("staff").whereEqualTo("id", staffId);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                titleText.setText(doc.getString("name"));

                            }

                        } else {


                        }
                    }
                });
            }


        }

        public void equipDetails(String id, final String name, final TextView textView) {
            db.setFirestoreSettings(settings);

            Query query = db.collection("equipment").whereEqualTo("id", id);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {

                            textView.setText(name + ": " + doc.getString("name"));

                        }

                    } else {


                    }
                }
            });


        }

        public void itemCompleted(int position) {
            Map<String, Object> map = new HashMap<>();
            map.put("complete", true);
            String tailgateId = (String) getSnapshots().getSnapshot(position).get("tailgateId");
            String staffId = (String) getSnapshots().getSnapshot(position).get("staffId");
            String equipmentId = (String) getSnapshots().getSnapshot(position).get("equipmentId");
            Log.d(TAG, "itemCompleted: " + tailgateId + ", " + staffId + ", " + equipmentId);
            updateEquip(tailgateId, staffId, equipmentId);
            dismiss();
            getSnapshots().getSnapshot(position).getReference().update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Toast.makeText(mContext, "Alert Completed", Toast.LENGTH_SHORT).show();

                }
            });

        }

        public void updateEquip(String tailgateId, String staffId, String equipmentId) {
            if (tailgateId == null) {

            } else {
                DocumentReference dr = db.collection("tailgates").document(tailgateId)
                        .collection("staffChecks").document(staffId).collection("equipment")
                        .document(equipmentId);
                Map<String, Object> map = new HashMap<>();
                map.put("note", null);
                map.put("noteId", null);
                dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "equipment updated: ");
                    }
                });
            }

        }

    }

    class AlertAdapter extends FirestoreRecyclerAdapter<Alert, AlertAdapter.ViewHolder> {
        private static final String TAG = "AlertAdapter";

        //firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true).build();

        //vars
        TailgateHelper mTailgateHelper;
        Methods meth;

        public AlertAdapter(@NonNull FirestoreRecyclerOptions<Alert> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Alert model) {
            holder.image.setVisibility(View.GONE);
            if (model.getDate() != null) {
                holder.date.setText(mTailgateHelper.datetoDayString(model.getDate()));
                holder.date.setTextColor(mTailgateHelper.dateAlertColor(model.getDate()));
            }
            holder.details.setText(model.getTitle());
            getDetails(model.getStaffId(), model.getEquipmentId(), holder.title);

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            meth = (Methods) viewGroup.getContext();
            mTailgateHelper = new TailgateHelper(viewGroup.getContext());
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_tailgateadapter, viewGroup, false);
            return new ViewHolder(view);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title, date, details;
            ImageView image;

            public ViewHolder(@NonNull View view) {
                super(view);

                image = view.findViewById(R.id.image_tailgateAdapter);
                title = view.findViewById(R.id.block_tailgateAdapter);
                details = view.findViewById(R.id.staff_tailgateAdapter);
                date = view.findViewById(R.id.date_tailgateAdapter);

            }
        }

        public void getDetails(String staffId, final String equipmentId,
                               final TextView titleText) {


            db.setFirestoreSettings(settings);

            if (staffId != null) {
                Query query = db.collection("staff").whereEqualTo("id", staffId);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                titleText.setText(doc.getString("name"));

                            }

                        } else {


                        }
                    }
                });
            }


        }

        public void equipDetails(String id, final String name, final TextView textView) {
            db.setFirestoreSettings(settings);

            Query query = db.collection("equipment").whereEqualTo("id", id);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {

                            textView.setText(name + ": " + doc.getString("name"));

                        }

                    } else {


                    }
                }
            });


        }

        public void itemCompleted(int position) {
            Map<String, Object> map = new HashMap<>();
            map.put("complete", true);
            String tailgateId = (String) getSnapshots().getSnapshot(position).get("tailgateId");
            String staffId = (String) getSnapshots().getSnapshot(position).get("staffId");
            String equipmentId = (String) getSnapshots().getSnapshot(position).get("equipmentId");
            Log.d(TAG, "itemCompleted: " + tailgateId + ", " + staffId + ", " + equipmentId);
            updateEquip(tailgateId, staffId, equipmentId);
            dismiss();
            getSnapshots().getSnapshot(position).getReference().update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Toast.makeText(mContext, "Alert Completed", Toast.LENGTH_SHORT).show();

                }
            });

        }

        public void updateEquip(String tailgateId, String staffId, String equipmentId) {
            if (tailgateId == null) {

            } else {
                DocumentReference dr = db.collection("tailgates").document(tailgateId)
                        .collection("staffChecks").document(staffId).collection("equipment")
                        .document(equipmentId);
                Map<String, Object> map = new HashMap<>();
                map.put("note", null);
                map.put("alertDate", null);
                map.put("alertId",null);
                dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "equipment updated: ");
                    }
                });
            }

        }

    }

    public static String getTAG() {
        return TAG;
    }
}


















































