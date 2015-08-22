package com.nova.sme.sme01.miscellanea;


import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;

public class RequestFromCamera {
    private Uri imageUri;
    private int id;

    public RequestFromCamera(Activity activity, int id) {
//        this.activity = activity;
        this.id = id;

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,       "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

        imageUri      = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, id);
    }

    public int getId() {
        return id;
    }

    public String getRealPathFromURI(Activity activity) {
        String[] proj    = {MediaStore.Images.Media.DATA};
        Cursor cursor    = activity.managedQuery(imageUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private Uri createUri() {
        Calendar cal = Calendar.getInstance();
        File file    = new File(Environment.getExternalStorageDirectory(),(cal.getTimeInMillis()+".jpg"));
        if (file.exists())
            file.delete();

        try {
            file.createNewFile();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

         return Uri.fromFile(file);
    }

/*
    private String get_RealPathFromURI(Activity activity) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(activity, imageUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
*/

}
