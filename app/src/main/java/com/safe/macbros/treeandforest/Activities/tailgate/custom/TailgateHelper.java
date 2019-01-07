package com.safe.macbros.treeandforest.Activities.tailgate.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateDetails;
import com.safe.macbros.treeandforest.Activities.tailgate.dialog.StaffCheck;
import com.safe.macbros.treeandforest.Activities.tailgate.models.StaffTailgate;
import com.safe.macbros.treeandforest.Activities.tailgate.tabs.DailyMeetings;
import com.safe.macbros.treeandforest.Activities.tailgate.tabs.MonthlyMeetings;
import com.safe.macbros.treeandforest.MainActivity;
import com.safe.macbros.treeandforest.R;
import com.safe.macbros.treeandforest.custom.Methods;
import com.safe.macbros.treeandforest.dialog.AlertsDialog;
import com.safe.macbros.treeandforest.models.Tailgate;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class TailgateHelper implements Serializable {
    private static final String TAG = "TailgateHelper";

    //vars
    private HashMap<String, Object> mMessage = new HashMap<>();
    private Methods mMeth;
    private Context mContext;
    private String mTag;

    //firestore
    FirebaseFirestore db = MainActivity.getFireDb();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference mStorageReference = storage.getReference();

    WifiManager wifi = MainActivity.getWifi();

    public TailgateHelper(Context context) {
        mContext = context;
    }


    public String TimestampToString(Timestamp timestamp) {

        Date date = timestamp.toDate();
        String dateString = null;
        SimpleDateFormat format = new SimpleDateFormat("d " + "MMMM");
        format.setTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
        dateString = format.format(date);
        return dateString;
    }

    public String dateToString(Date date) {

        String dateString = null;
        SimpleDateFormat format = new SimpleDateFormat("d " + "MMM" + " yy");
        format.setTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
        dateString = format.format(date);
        return dateString;

    }

    public String adddateGetString(Date date, int i) {

        String dateString = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        SimpleDateFormat format = new SimpleDateFormat("d " + "MM" + "YY");
        format.setTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
        dateString = format.format(cal.getTime());
        return dateString;

    }


    public String cutSquareBracketsFromString(ArrayList arrayList) {
        String string = arrayList.toString();
        String s = string.substring(1, string.length() - 1);
        return s;
    }

    public void longTimestampToTextView(Timestamp timestamp, TextView text) {
        Date date = timestamp.toDate();
        String dateString = null;
        SimpleDateFormat format = new SimpleDateFormat("d " + "MMMM" + " yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
        dateString = format.format(date);
        text.setText(dateString);
    }

    public void shortTimestampToTextView(Timestamp timestamp, TextView text) {
        Date date = timestamp.toDate();
        String dateString = null;
        SimpleDateFormat format = new SimpleDateFormat("d " + "MMMM");
        format.setTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
        dateString = format.format(date);
        text.setText(dateString);
    }

    public int timestampAlertColor(Timestamp date) {

        Timestamp timestamp = Timestamp.now();

        long diffInMillies = Math.abs(timestamp.toDate().getTime() - date.toDate().getTime());

        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (diff >= 30)
            return Color.parseColor("#d4d4d4");
        else if (diff < 30 && diff >= 8)
            return Color.parseColor("orange");
        else
            return Color.parseColor("red");
    }

    public int dateAlertColor(Date date) {

        Calendar cal = Calendar.getInstance();
        Date current_date = (cal.getTime());
        long diffInMillies = Math.abs(current_date.getTime() - date.getTime());

        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (diff >= 30)
            return Color.parseColor("#d4d4d4");
        else if (diff < 30 && diff >= 8)
            return Color.parseColor("#FFA500");
        else
            return Color.parseColor("#FF4500");
    }

    public Toolbar newToolbar(final String title, int menuId, final Fragment parent, final String parentTag, final String tag,
                              final HashMap<String, Object> message, final Activity context) {
        mContext = context;
        mMeth = (Methods) context;
        mTag = tag;
        mMessage = message;
        Log.d(TAG, "newToolbar: " + tag);
        final Toolbar toolbar = MainActivity.getToolbar();

        toolbar.getMenu().clear();

        if (menuId == 0)

            toolbar.inflateMenu(R.menu.menu_main);

        else

            toolbar.inflateMenu(menuId);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                menuItemSwitch(menuItem);
                return true;
            }
        });


        if (parent == null)
            toolbar.setNavigationIcon(R.drawable.ic_menu);
        else
            toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: toolbarNav, parentTag = " + parentTag + " tag = " + tag);
                if (parentTag == null) {
                        MainActivity.getDrawerLayout().openDrawer(GravityCompat.START);
                } else  {
                    mMeth.sendFragMessage(parent, parentTag, message);
                }
            }

        });

        if (menuId != 0)
            toolbar.inflateMenu(menuId);
        toolbar.setTitle(title);

        return toolbar;

    }

    private void menuItemSwitch(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_ic_alert: {
                mMessage.put("context", mContext);
                alertMessageUpdate(mTag);
                mMeth.sendDialogMessage(new AlertsDialog(), AlertsDialog.getTAG(), mMessage);
            }
            break;
            case R.id.menu_save: {

                mMeth.sendDialogMessage(new StaffCheck(), StaffCheck.getTAG(), mMessage);

                mMeth.sendFragMessage(new TailgateDetails(), TailgateDetails.getTAG(), mMessage);

            }
            break;
            case R.id.menu_ic_note: {

                noteMessageUpdate(mTag);
                mMeth.sendDialogMessage(new AlertsDialog(), AlertsDialog.getTAG(), mMessage);
            }
            break;


        }

    }

    public String datetoDayString(Date date) {
        String s = "";
        Calendar cal = Calendar.getInstance();
        Date current_date = (cal.getTime());
        long diffInMillies = (date.getTime() - current_date.getTime());

        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (diff >= 2)
            s = String.valueOf(diff) + " days";
        else if (diff < 2 && diff > 0)
            s = String.valueOf(diff) + " day";
        else if (diff == 0)
            s = " Due Today";
        else s = " Overdue by " + String.valueOf(Math.abs(diff)) + " days";

        return s;
    }

    public Query queryType(String tag) {
        CollectionReference cr = db.collection("alerts");
        Query query = null;
        switch (tag) {

            case "TailgateMain": {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 30);
                long due = cal.getTimeInMillis();
                Date date = new Date();
                query = cr.whereEqualTo("complete", false);

            }
            break;

            case "TailgateDetails": {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 30);
                long due = cal.getTimeInMillis();
                Date date = cal.getTime();
                query = cr.whereEqualTo("complete", false);


            }
            break;
            case "PdfViewer": {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 30);
                long due = cal.getTimeInMillis();
                Date date = new Date();
                query = cr.whereEqualTo("complete", false);


            }
            break;


        }

        return query;
    }

    public void checkForAlerts(final MenuItem mItem) {
        CollectionReference cr = db.collection("alerts");

        Query query = cr.whereEqualTo("complete", false);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null) {
                    {
                        mItem.setVisible(false);
                    }
                } else if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    mItem.setVisible(true);
                } else {
                    mItem.setVisible(false);
                }

            }
        });


    }

    public void checkForNotes(final MenuItem mItem) {
        CollectionReference cr = db.collection("notes");

        Query query = cr.whereEqualTo("complete", false);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null) {
                    {
                        mItem.setVisible(false);
                    }
                } else if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    mItem.setVisible(true);
                } else {
                    mItem.setVisible(false);
                }

            }
        });


    }

    public void checkStaffAlerts(final MenuItem mItem, String staffId) {
        CollectionReference cr = db.collection("alerts");

        Query query = cr.whereEqualTo("complete", false)
                .whereEqualTo("staffId", staffId);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null) {
                    {
                        mItem.setVisible(false);
                    }
                } else if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    mItem.setVisible(true);
                } else {
                    mItem.setVisible(false);
                }

            }
        });


    }

    public void checkStaffNotes(final MenuItem mItem, String staffId) {
        CollectionReference cr = db.collection("notes");

        Query query = cr.whereEqualTo("complete", false)
                .whereEqualTo("staffId", staffId);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null) {
                    {
                        mItem.setVisible(false);
                    }
                } else if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    mItem.setVisible(true);
                } else {
                    mItem.setVisible(false);
                }

            }
        });

    }


    public void alertMessageUpdate(String tag) {
        Log.d(TAG, "alertMessageUpdate: " + tag);
        switch (tag) {
            case "TailgateEquipment": {

                updateMessageAlertBooleans(true, true, true, true, false);

                StaffTailgate staff = (StaffTailgate) mMessage.get("staff");

                Tailgate tailgate = (Tailgate) mMessage.get("tailgate");

                updateMessageAlertIds(staff.getId(), tailgate.getId());

            }
            break;
            case "TailgateDetails": {

                updateMessageAlertBooleans(true, false, true, false, false);


                Tailgate tailgate = (Tailgate) mMessage.get("tailgate");

                updateMessageAlertIds(null, tailgate.getId());

            }
            break;
            default:

                updateMessageAlertBooleans(true, false, false, false, false);

                break;
        }


    }

    public void noteMessageUpdate(String tag) {

        Log.d(TAG, "noteMessageUpdate: " + tag);
        switch (tag) {
            case "TailgateEquipment": {

                updateMessageAlertBooleans(false, true, true, true, true);

                StaffTailgate staff = (StaffTailgate) mMessage.get("staff");

                Tailgate tailgate = (Tailgate) mMessage.get("tailgate");

                updateMessageAlertIds(staff.getId(), tailgate.getId());

            }
            break;
            case "TailgateDetails": {

                updateMessageAlertBooleans(false, false, true, false, true);


                Tailgate tailgate = (Tailgate) mMessage.get("tailgate");

                updateMessageAlertIds(null, tailgate.getId());

            }
            break;
            default:

                updateMessageAlertBooleans(false, false, false, false, true);

                break;
        }


    }


    public void downloadPdfFile(ProgressBar progressBar, TextView title, String onlinePath, PDFView pdfView) {


    }

    public void pdfDownloadUpdateView(final ProgressBar progressBar, final TextView title, String onlinePath, final PDFView pdfView,
                                      final DocumentReference doc) {
        pdfView.recycle();
        StorageReference pdf = mStorageReference.child(onlinePath);

        File dir = mContext.getFilesDir();

        final File file = new File(dir, pdf.getName());

        if (!file.exists()) {
            progressBar.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);

            pdf.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created

                    Toast.makeText(mContext, "Saved Offline", Toast.LENGTH_SHORT).show();

                    try {
                        pdfView.fromFile(file).load();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Map<String, Object> map = new HashMap<>();

                    map.put("offlinePdfPath", file.getPath());

                    doc.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    title.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    downloadProgress(taskSnapshot, progressBar);
                }
            });
        } else {
            pdfView.fromFile(file).load();
            // Toast.makeText(getContext(), "File exists no Download Needed", Toast.LENGTH_SHORT).show();
        }

    }

    public void downloadProgress(FileDownloadTask.TaskSnapshot taskSnapshot, ProgressBar progressBar) {


        @SuppressWarnings("VisibleForTests")
        long fileSize = taskSnapshot.getTotalByteCount();

        @SuppressWarnings("VisibleForTests")
        long uploadBytes = taskSnapshot.getBytesTransferred();

        long progress = (100 * uploadBytes) / fileSize;

        progressBar.setProgress((int) progress);

    }


    public void downloadGlideImage(ImageView image, String fileName, final String hazardId) {

        StorageReference sRef = mStorageReference.getRoot().child(fileName);

        File dir = mContext.getFilesDir();

        final File file = new File(dir, fileName);

        Log.d(TAG, "downloadGlideImage: offline path = " + file.getPath());

        sRef.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {

                DocumentReference dr = db.collection("hazards").document(hazardId);

                Map<String, Object> map = new HashMap<>();

                map.put("offlinePath", file.getPath());

                Toast.makeText(mContext, "Saved offline", Toast.LENGTH_SHORT).show();

                dr.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "onSuccess: file path saved " + file.getName());
                    }
                });
            }
        });


    }


    public void updateProgress(UploadTask.TaskSnapshot taskSnapshot, ProgressBar progressBar) {

        progressBar.setVisibility(View.VISIBLE);

        @SuppressWarnings("VisibleForTests")
        long fileSize = taskSnapshot.getTotalByteCount();

        @SuppressWarnings("VisibleForTests")
        long uploadBytes = taskSnapshot.getBytesTransferred();

        long progress = (100 * uploadBytes) / fileSize;

        progressBar.setProgress((int) progress);


    }


    public String uploadFile(File file, String folder) {

        StorageReference fileRef = mStorageReference.child(folder + "/" + file.getName());
        UploadTask uploadTask;
        uploadTask = fileRef.putFile(Uri.fromFile(file));

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...


            }
        });

        return fileRef.getPath();
    }


    public void updateMessageAlertBooleans(boolean alert, boolean staff, boolean tailgate, boolean equipment, boolean note
    ) {

        mMessage.put("alertBoolean", alert);

        mMessage.put("staffBoolean", staff);

        mMessage.put("tailgateBoolean", tailgate);

        mMessage.put("equipmentBoolean", equipment);

        mMessage.put("noteBoolean", note);

    }


    public void updateMessageAlertIds(String staffId, String equipmentid) {

        mMessage.put("alertStaffId", staffId);
        mMessage.put("alertEquipId", equipmentid);
        mMessage.put("noteEquipId", equipmentid);
        mMessage.put("noteStaffId", equipmentid);

    }


    public boolean online() {
        boolean online = false;

        if (wifi.isWifiEnabled()) {
//wifi is enabled
            online = true;
        }

        return online;
    }

    public void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {

            view = new View(activity);

        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}























