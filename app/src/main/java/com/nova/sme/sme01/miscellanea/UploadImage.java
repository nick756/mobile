package com.nova.sme.sme01.miscellanea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.support.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class UploadImage {
    private String url;
    private String ba1;
    private String imageName;

    public UploadImage(String imagePath, String imageName, String url) {
        this.url       = url;
        this.imageName = imageName;

        Bitmap                bm  = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1       = Base64.encodeBytes(ba);

        new uploadToServer().execute();
    }

    public class uploadToServer extends AsyncTask<Void, Void, String> {

 //       private ProgressDialog pd = new ProgressDialog(MainActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();
 //           pd.setMessage("Wait image uploading!");
 //           pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair(imageName, System.currentTimeMillis() + ".jpg"));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost   httppost   = new HttpPost(url);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String       st       = EntityUtils.toString(response.getEntity());

//                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
//                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
 //           pd.hide();
 //           pd.dismiss();
        }
    }
}
