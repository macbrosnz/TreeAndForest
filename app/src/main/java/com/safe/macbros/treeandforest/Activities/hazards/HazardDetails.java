package com.safe.macbros.treeandforest.Activities.hazards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.custom.SafetyHelper;
import com.safe.macbros.treeandforest.models.Hazards;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HazardDetails extends Fragment implements View.OnClickListener {
    private static final String TAG = "HazardDetails";


    //widgets
    ImageView image;
    TextView title, details, type, controls, downloading, titleCard1, titleCard2;
    ImageButton refresh;
    CardView card1, card2, progressCard;

    //vars
    FirebaseFirestore db = MainActivity.getFireDb();
    Hazards mHazards = new Hazards();
    Methods meth;
    TailgateHelper tHelper;
    HashMap<String, Object> message = new HashMap<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressBar progBar;
    SafetyHelper sHelper = new SafetyHelper();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meth = (Methods) context;
        tHelper = new TailgateHelper(context);


        Bundle bundle = this.getArguments();
        if (bundle == null) {

        } else {
            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            mHazards = (Hazards) message.get("hazard");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.universal_details, container, false);
        Toolbar toolbar = tHelper.newToolbar("Details", 0, new HazardsMain(),
                HazardsMain.getTAG(), getTAG(), message, getActivity());
        MenuItem mItem = toolbar.getMenu().findItem(R.id.menu_ic_alert);
        tHelper.checkForAlerts(mItem);
        initUI(view);
        return view;

    }

    public void initUI(View view) {

        image = view.findViewById(R.id.image_ud);

        refresh = view.findViewById(R.id.imageButton_ud);

        progBar = view.findViewById(R.id.progressBar_ud);

        downloading = view.findViewById(R.id.downloading_ud);

        refresh.setOnClickListener(this);

        details = view.findViewById(R.id.detailsCard1_ud);

        titleCard1 = view.findViewById(R.id.titleCard1_ud);
        titleCard2 = view.findViewById(R.id.titleCard2_ud);
        card1 = view.findViewById(R.id.card1_ud);
        card2 = view.findViewById(R.id.card2_ud);
        progressCard = view.findViewById(R.id.progressCard_ud);
        title = view.findViewById(R.id.title_ud);
        controls = view.findViewById(R.id.detailsCard2_ud);
        type = view.findViewById(R.id.sideText_ud);

        updateViews();
    }

    public void updateViews(){

        sHelper.makeVisible(image);
        sHelper.makeVisible(type);
        title.setText(mHazards.getTitle());

        if (mHazards.getEim() != null) {

            type.setText(mHazards.getEim()+"/"+String.valueOf(mHazards.getRiskScore()));

        }
        if (mHazards.getOfflinePath() != null) {
            if (new File(mHazards.getOfflinePath()).length() > 0)
                Glide.with(getActivity()).load(new File(mHazards.getOfflinePath())).into(image);
            else if (tHelper.online()) {
                if (mHazards.getOnlinePath() != null) {
                    refresh.setVisibility(View.VISIBLE);

                }
            }
        }


        sHelper.makeVisible(card1);
        sHelper.makeVisible(details);
        titleCard1.setText("Details");
        if (mHazards.getDetails() != null) {
            details.setText(mHazards.getDetails());
        }

        sHelper.makeVisible(card2);
        sHelper.makeVisible(controls);
        titleCard2.setText("Controls");
        if (mHazards.getControl() != null) {

            controls.setText(mHazards.getControl());
        }


    }
    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageButton_ud: {
                if (mHazards.getOnlinePath()!= null) {
                    progressCard.setVisibility(View.VISIBLE);
                    StorageReference imageRef = storage.getReference(mHazards.getOnlinePath());
                    final File file = new File(getContext().getFilesDir(), imageRef.getName());
                    if(file.exists())
                        file.delete();

                    imageRef.getFile(file).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            tHelper.downloadProgress(taskSnapshot, progBar);

                        }
                    }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            mHazards.setOfflinePath(file.getPath());
                            Glide.with(getActivity()).load(file).into(image);
                            progressCard.setVisibility(View.GONE);
                            updateHazard(mHazards.getOfflinePath());

                        }
                    });

                }

            }
            break;
        }

    }


    public void updateHazard(String path){

        Map<String,Object> map = new HashMap<>();
        map.put("offlinePath", path);
        DocumentReference dRef = db.collection("hazards").document(mHazards.getId());
        dRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "File Saved Offline", Toast.LENGTH_SHORT).show();
            }
        });

    }
}


