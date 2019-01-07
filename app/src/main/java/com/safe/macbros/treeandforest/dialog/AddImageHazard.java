package com.safe.macbros.treeandforest.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.models.Hazards;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class AddImageHazard extends DialogFragment {

    private static final String TAG = "AddImageHazard";
    public static final int GALLERY = 1;
    public static final int CAMERA = 0;
    FirebaseFirestore db = MainActivity.getFireDb();
    DocumentReference dr = db.collection("hazards").document();

    //widgets
    ImageView image;


    //vars
    Methods meth;
    HashMap<String, Object> message = new HashMap<>();
    Hazards hazard = new Hazards();
    TailgateHelper tHelper;
    Tailgate tailgate;
    String siteId, taskId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tHelper = new TailgateHelper(context);
        meth = (Methods) context;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            bundle.clear();
        } else {
            message = (HashMap<String, Object>) bundle.getSerializable(getTAG());
            bundle.clear();
            tailgate = (Tailgate) message.get("tailgate");
            hazard = (Hazards) message.get("hazard");
            siteId = (String)message.get("siteId");
            taskId = (String)message.get("taskId");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_image_hazards, null);
        image = view.findViewById(R.id.hiddenImage_hazards);

        String[] items = {"Choose from Gallery", "Take a Picture"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add an Image")
                .setView(view)
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            choosePhotoFromGallery();

                        } else if (which == 1) {

                            useCamera();
                        }
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        message.put("hazard", hazard);
                        dr.set(hazard).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: Updated Hazard");
                            }
                        });
                        updateSiteHazards(hazard);

                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        AlertDialog a = builder.create();
        a.getListView();

        return builder.create();
    }


    public void choosePhotoFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            Fragment d = getChildFragmentManager().findFragmentByTag("AddImageHazard");

            startActivityForResult(galleryIntent, GALLERY);
        }

    }


    public void useCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            startActivityForResult(takePictureIntent, CAMERA);
        }

    }


    public static String getTAG() {
        return TAG;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode" + requestCode + ", resultCode = " + resultCode);

        if (resultCode == getActivity().RESULT_CANCELED) {
            Log.d(TAG, "onActivityResult: resultCode is false");
            return;
        }

        if (requestCode == CAMERA) {
            Log.d(TAG, "onActivityResult: inside Camera " + data.getType());
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
            image.setVisibility(View.VISIBLE);
            try {
                createImageFile(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == GALLERY) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
            image.setVisibility(View.VISIBLE);
            try {
                createImageFile(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void createImageFile(Intent data) throws IOException {

        hazard.setId(dr.getId());
        // Create an image file name
        String imageFileName = "image_" + dr.getId() + "_";

        File storageDir = getContext().getFilesDir();

        File file = File.createTempFile(

                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */

        );

        if (file.exists())

            file.delete();

        try {

            FileOutputStream fOut = new FileOutputStream(file);
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        hazard.setOfflinePath(file.getAbsolutePath());

        if (tHelper.online())
            hazard.setOnlinePath(tHelper.uploadFile(file, "images"));


    }

    public void updateHazards(Hazards hazard) {
        {
            DocumentReference dr = db.collection("hazards").document();
            hazard.setId(dr.getId());
            dr.set(hazard).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "onComplete: updated site hazards");
                }
            });
        }
    }

    private void updateSiteHazards(Hazards newHazard) {
        if (siteId != null) {
                DocumentReference drH = db.collection("sites").document(tailgate.getBlockId()).collection("hazards").document();
                newHazard.setId(drH.getId());
                drH.set(newHazard).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                });
            }
    }
}



































