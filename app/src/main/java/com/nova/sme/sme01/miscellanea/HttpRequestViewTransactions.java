package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.xml_reader_classes.Record;
import com.nova.sme.sme01.xml_reader_classes.TransactionXML;
import com.nova.sme.sme01.xml_reader_classes.Transactions;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.List;


/*
 *************************************
 *                                   *
 *   Http request for implemented    *
 *   transactions (to view)          *
 *                                   *
 *************************************
*/

public class HttpRequestViewTransactions {
    private String         url_request;
//    private RelativeLayout base_layout;
    private Activity       activity;
//    private MyDialog       my_dialog;
    private Vocabulary     voc;

    public HttpRequestViewTransactions(Activity  activity, /*RelativeLayout base_layout, */Vocabulary voc, String url_request) {
        this.activity    = activity;
        this.url_request = url_request;
//        this.base_layout = base_layout;
        this.voc         = voc;

//        my_dialog        = new MyDialog(voc, base_layout);

        new Http_Request_View_Transactions().execute();
    }
    private class Http_Request_View_Transactions extends AsyncTask<Void, String, Transactions> {
        @Override
        protected Transactions doInBackground(Void... params) {
            String error;

            Transactions xml_transactions;
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

                xml_transactions = serializer.read(Transactions.class, xml);

                return xml_transactions;
            } catch (java.net.URISyntaxException e) {
                error = e.getMessage();
            } catch (RestClientException e){
                error = e.getMessage();
            } catch (Exception e) {
                error = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Transactions xml_transactions) {
            boolean ok = false;
            if (xml_transactions != null) {
                if (xml_transactions.getCode().equals("0")) {
                    ok = true;
                    List<Record> list = xml_transactions.getRecordsList();

//                    my_dialog.show(voc.getTranslatedString("Success"), R.mipmap.ic_success);
/*
                    if (by_finish) {
                        activity.finish();//goto login view
                    } else {
                        Intent intent = new Intent(activity, MainActivity.class );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        activity.startActivity(intent);
                    }
*/
                } else {
//                   my_dialog.show(voc.getTranslatedString(xml_transactions.getResDescription()), R.mipmap.ic_failture);
                    return;
                }
            }
            if (!ok) {
//                my_dialog.show(voc.getTranslatedString("Error occured"), R.mipmap.ic_failture);
            }
        }
    }

}