package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.xml_reader_classes.BaseXML;
import com.nova.sme.sme01.xml_reader_classes.TransactionXML;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;


public class HttpRequestTransaction {
    private String         url_request;
    private RelativeLayout base_layout;
    private Activity       activity;
    private MyDialog       my_dialog;
    private Vocabulary     voc;

    public HttpRequestTransaction(Activity  activity, RelativeLayout base_layout, Vocabulary voc, String url_request) {
        this.activity    = activity;
        this.url_request = url_request;
        this.base_layout = base_layout;
        this.voc         = voc;

        my_dialog        = new MyDialog(voc, base_layout);

        new Http_Request_Transaction().execute();
    }
    private class Http_Request_Transaction extends AsyncTask<Void, String, TransactionXML> {
        @Override
        protected TransactionXML doInBackground(Void... params) {
            String error;

            TransactionXML xml_transaction;
            URI uri;
            try {

                URL url = new URL(url_request);
                uri     = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

                RestTemplate restTemplate = new RestTemplate();
                StringHttpMessageConverter converter = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(converter);

                String xml            = restTemplate.getForObject(uri, String.class);
                Serializer serializer = new Persister();
                SimpleXmlHttpMessageConverter xml_converter = new SimpleXmlHttpMessageConverter(serializer);

                xml_transaction = serializer.read(TransactionXML.class, xml);

                return xml_transaction;
            } catch (java.net.URISyntaxException e) {
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            } catch (RestClientException e){
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            }//http://103.6.239.242/sme/mobile/addtransaction/?id=4&companyID=2&date=15/07/2015&operationCode=1&operationAmount=5000.45&operationDescription='This%20is%20a%20test'
            //Illegal character in query at index 145: http://103.6.239.242/sme/mobile/addtransaction/?id=4&companyID=2&date=15/7/2015&operationCode=3&operationAmount=1500.60&operationDescription=This is a test
            return null;
        }

        @Override
        protected void onPostExecute(TransactionXML xml_transaction) {
            boolean ok = false;
            if (xml_transaction != null) {
                if (xml_transaction.getCode().equals("0")) {
                    ok = true;
/*
                    if (by_finish) {
                        activity.finish();//goto login view
                    } else {
                        Intent intent = new Intent(activity, MainActivity.class );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        activity.startActivity(intent);
                    }
*/
                }
            }
            if (!ok)
                my_dialog.show("Error occured");
        }
    }

}
