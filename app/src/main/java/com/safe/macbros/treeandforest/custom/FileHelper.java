package com.safe.macbros.treeandforest.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.Serializable;

public class FileHelper implements Serializable {
    private static final String TAG = "FileHelper";

    private File mFile;

    private String mFileType;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private String mOnlinePath;

    private String mOfflinePath;

    private static Context mContext;

    StorageReference mSRef = storage.getReference();


    public FileHelper(String fileType, String onlinePath, String offlinePath, Context context) {

        mFileType = fileType;
        mOnlinePath = onlinePath;
        mOfflinePath = offlinePath;
        mContext = context;


    }

    public void makeFileAndShow(final ImageView image) {


        if (mOfflinePath == null && mOnlinePath != null) {
            StorageReference im = mSRef.child(mOnlinePath);
            Glide.with(mContext).load(im).into(image);

            final File dir = mContext.getFilesDir();

            im.getFile(new File(dir, mOnlinePath)).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {

                    Log.d(TAG, "onComplete: FileDownloaded");
                    Glide.with(mContext).load(new File(dir, mOnlinePath)).into(image);

                }


            });


        } else if (mOfflinePath != null) {
            if (!new File(mOfflinePath).exists() && mOnlinePath != null) {
                StorageReference im = mSRef.child(mOnlinePath);
                File dir = mContext.getFilesDir();

                im.getFile(new File(dir, mOnlinePath)).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        Log.d(TAG, "onComplete: FileDownloaded");
                        Glide.with(mContext).load(mOfflinePath).into(image);

                    }


                });

            }else
                Glide.with(mContext).load(mOfflinePath).into(image);
        }


    }


    public static Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        FileHelper.mContext = mContext;
    }
}
