package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class Http_Request_Logout {
    private String         url_logout;
    private Vocabulary     voc;
    private RelativeLayout base_layout;
    private FileManager    FM;
    private boolean        by_finish;
    private Activity       activity;
    private MyDialog       my_dialog;

    public Http_Request_Logout(Activity activity, String url_logout, FileManager FM, Vocabulary voc, RelativeLayout base_layout, boolean by_finish) {
        this.activity    = activity;
        this.url_logout  = url_logout;
        this.voc         = voc;
        this.base_layout = base_layout;
        this.FM          = FM;
        this.by_finish   = by_finish;

        my_dialog        = new MyDialog(voc, base_layout);

        new HttpRequestLogout().execute();
    }
    private class HttpRequestLogout extends AsyncTask<Void, String, BaseXML> {
        @Override
        protected BaseXML doInBackground(Void... params) {
            String error;

            BaseXML xml_logout;
            URI uri;
            try {
                uri = new URI(url_logout);

                RestTemplate restTemplate = new RestTemplate();
                StringHttpMessageConverter converter = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(converter);

                String xml            = restTemplate.getForObject(uri, String.class);
                Serializer serializer = new Persister();
                SimpleXmlHttpMessageConverter xml_converter = new SimpleXmlHttpMessageConverter(serializer);

                xml_logout = serializer.read(BaseXML.class, xml);

                return xml_logout;
            } catch (java.net.URISyntaxException e) {
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            } catch (RestClientException e){
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(BaseXML xml_logout) {
//            block_button = false;
            if (xml_logout != null) {
                if (xml_logout.getCode().equals("0")) {
                    // erase the file and go to the first page
                    //"parameters.bin";
                    //operations_list.bin
                    FM.deleteFile("parameters.bin");
                    FM.deleteFile("operations_list.bin");
                    if (by_finish) {
                        activity.finish();//goto main view
                    } else {

                    }
                }
            }
            my_dialog.show("Error occured");
        }
    }
}
