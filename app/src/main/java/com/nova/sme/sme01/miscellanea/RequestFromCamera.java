package com.nova.sme.sme01.miscellanea;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class RequestFromCamera {
    private Activity activity;
    private Uri      imageUri;
    private int      id;

    public RequestFromCamera( Activity activity, int id) {
        this.activity = activity;
        this.id       = id;

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

        imageUri  = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, id);
    }

    public Uri getUri(){return imageUri;}
 //   public void setUri(String imageUri) {this.imageUri = imageUri;}

    public int getId(){return id;}

    public String getRealPathFromURI() {
        String[] proj    = { MediaStore.Images.Media.DATA };
        Cursor cursor    = activity.managedQuery(imageUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
