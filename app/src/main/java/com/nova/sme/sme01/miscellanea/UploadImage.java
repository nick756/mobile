package com.nova.sme.sme01.miscellanea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.Dialogs.GifDialog;
import com.nova.sme.sme01.miscellanea.Dialogs.MyDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.support.Base64;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class UploadImage {
    private String    url_request;
    private String    imageName;
    private String    data;
    private GifDialog gif_doalog;
    private String    error;
    private MyDialog  my_dialog;

    public UploadImage(Bitmap bitmap, String url, GifDialog  gif_doalog) {
        this.url_request = url;
        this.gif_doalog  = gif_doalog;

        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        data       = Base64.encodeBytes(ba);


//        this.my_dialog   = new MyDialog(FR, voc, base_layout);

        new Http_Request().execute();

 //       new uploadToServer().execute();
    }


    private class Http_Request extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {


            URI uri;//http://103.6.239.242:80/sme/mobile/addInvoice/?id=4&companyID=2
            try {
                URL url = new URL(url_request);
                uri     = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

                RestTemplate               restTemplate = new RestTemplate();
                StringHttpMessageConverter converter    = new StringHttpMessageConverter();

                restTemplate.getMessageConverters().add(converter);

                String xml = restTemplate.postForObject(uri, data, String.class);//restTemplate.getForObject(uri, String.class);

                return xml;
            } catch (java.net.URISyntaxException e) {
                error = e.getMessage();
            } catch (RestClientException e){
                error = e.getMessage();
            } catch (Exception e) {//404 Not Found
                error = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String xml) {
            String code;

            if (gif_doalog != null)
                gif_doalog.dismiss();

            if (xml == null) {
                if (gif_doalog != null)
                    Toast.makeText(gif_doalog.getContext(), error, Toast.LENGTH_LONG).show();
            } else {
                if (gif_doalog != null)
                    Toast.makeText(gif_doalog.getContext(), "Ok", Toast.LENGTH_LONG).show();
            }


        }
    }

/*
    public class uploadToServer extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", data));
            nameValuePairs.add(new BasicNameValuePair(imageName, System.currentTimeMillis() + ".jpg"));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost   httppost   = new HttpPost(url_request);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String       st       = EntityUtils.toString(response.getEntity());

//                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
//                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

     }
*/
}
