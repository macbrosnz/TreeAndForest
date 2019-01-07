package com.safe.macbros.treeandforest.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by sonu on 23/03/17.
 */

public class ScreenshotUtils {

    /*  Method which will return Bitmap after taking screenshot. We have to pass the view which we want to take screenshot.  */
    public static Bitmap getScreenShot(View view) {
        view = view.getRootView();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }


    /*  Store taken screenshot into above created path  */
    public static File store(Bitmap bm, String fileName,Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, fileName + ".JPEG");
        if(file.exists())
            file.delete();
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.getPath();
        return file;
    }
}
