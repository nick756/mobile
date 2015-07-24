package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.CommonClass;
import com.nova.sme.sme01.MainActivity;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.TransactionsViewActivity;
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
    private RelativeLayout base_layout;
    private Activity       activity;
    private MyDialog       my_dialog;
    private Vocabulary     voc;

    private String         dateFrom;
    private String         dateTill;

    public HttpRequestViewTransactions(Activity  activity, RelativeLayout base_layout, Vocabulary voc, String url_request, String dateFrom, String dateTill) {
        this.activity    = activity;
        this.url_request = url_request;
        this.base_layout = base_layout;
        this.voc         = voc;

        this.dateFrom    = dateFrom;
        this.dateTill    = dateTill;

//        my_dialog        = new MyDialog(voc, base_layout);

        new Http_Request_View_Transactions().execute();
    }
    private class Http_Request_View_Transactions extends AsyncTask<Void, String, Transactions> {
        @Override
        protected Transactions doInBackground(Void... params) {
            String error;

            Transactions xml_transactions;
            URI uri;//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=23/07/2015&dateTill=23/07/2015
            try {//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=21/06/2015&dateTill=22/07/2015
                URL url = new URL(url_request);
                uri     = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

                RestTemplate restTemplate = new RestTemplate();
                StringHttpMessageConverter converter = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(converter);

                String xml            = restTemplate.getForObject(uri, String.class);//<result code='3' id='4'><originator>194.219.45.10</originator><resDescription>Session expired</resDescription></result>
                Serializer serializer = new Persister();
                SimpleXmlHttpMessageConverter xml_converter = new SimpleXmlHttpMessageConverter(serializer);

                xml_transactions = serializer.read(Transactions.class, xml);

                return xml_transactions;
            } catch (java.net.URISyntaxException e) {
                error = e.getMessage();
            } catch (RestClientException e){
                error = e.getMessage();
            } catch (Exception e) {//Unable to satisfy @org.simpleframework.xml.Element(data=false, name=, required=true, type=void) on field 'records' private com.nova.sme.sme01.xml_reader_classes.Records com.nova.sme.sme01.xml_reader_classes.Transactions.records for class com.nova.sme.sme01.xml_reader_classes.Transactions at line 1
                error = e.getMessage();
            }
            return null;
        }
        //http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=21/07/2015&dateTill=22/07/2015
        //http://103.6.239.242/sme/mobile/listtransactions?id=4&dateFrom=21/06/2014&dateTill=22/07/2015
        /*
        todo
        1. check if operator is the same
        2. size as a recordCount

         */
        @Override
        protected void onPostExecute(Transactions xml_transactions) {
            boolean ok = false;
            int cnt, cnt1;
            if (xml_transactions != null) {
                String code = xml_transactions.getCode();
                if (code.equals("0")) {
                    List<Record> list = xml_transactions.getRecordsList();

                    if (list == null) {
                        empty_list();
                        return;
                    } else if (list.size() == 0) {
                        empty_list();
                        return;
                    }
                    FileManager FM = new FileManager(activity);
                    FM.writeToFile("transactions_view.bin", xml_transactions);

                    CommonClass c_c   = new CommonClass();
                    c_c.dateFrom      = xml_transactions.getDateStart(); //. dateFrom;
                    c_c.dateTill      = xml_transactions.getDateStop();  // dateTill;

                    // other idea is using real date from the list
//                    c_c.dateFrom = list.get(0).getDate();
//                    c_c.dateTill = list.get(list.size() - 1).getDate();


                    Intent intent = new Intent(activity, TransactionsViewActivity.class );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    intent.putExtra(MainActivity.MAIN_INFO, c_c);
                    activity.startActivity(intent);
                } else if (code.equals("3")) { // Session expired
                    Intent intent = new Intent(activity, MainActivity.class );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    activity.startActivity(intent);
                } else {
//                   my_dialog.show(voc.getTranslatedString(xml_transactions.getResDescription()), R.mipmap.ic_failture);
                    return;
                }
            }
            if (!ok) {
//                my_dialog.show(voc.getTranslatedString("Error occured"), R.mipmap.ic_failture);
            }
        }
        private void empty_list() {
            my_dialog = new MyDialog(voc, base_layout);
            my_dialog.show("List of transactions is empty", R.mipmap.ic_zero);
        }
    }

}
