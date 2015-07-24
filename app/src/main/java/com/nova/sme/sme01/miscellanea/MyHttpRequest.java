package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.CommonClass;
import com.nova.sme.sme01.MainActivity;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.RegularLoginActivity;
import com.nova.sme.sme01.TransactionsViewActivity;
import com.nova.sme.sme01.xml_reader_classes.GetOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;
import com.nova.sme.sme01.xml_reader_classes.Record;
import com.nova.sme.sme01.xml_reader_classes.TransactionXML;
import com.nova.sme.sme01.xml_reader_classes.Transactions;
import com.nova.sme.sme01.xml_reader_classes.XML_Login;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.List;

/*
 *******************************
 *                             *
 *   Universal Http request    *
 *                             *
 *******************************
 */
public class MyHttpRequest {
    private String          url_request;
    private RelativeLayout  base_layout;
    private Activity        activity;
    private MyDialog        my_dialog;
    private Vocabulary      voc;
    private String          className;

    public MyHttpRequest(Activity activity, RelativeLayout base_layout, Vocabulary voc, String url_request, String className) {
        this.activity    = activity;
        this.url_request = url_request;
        this.base_layout = base_layout;
        this.voc         = voc;
        this.className   = className;
        this.my_dialog   = new MyDialog(voc, base_layout);

        new Http_Request().execute();
    }

    private class Http_Request extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            String error;

            Transactions xml_transactions;
            URI uri;//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=23/07/2015&dateTill=23/07/2015
            try {//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=21/06/2015&dateTill=22/07/2015
                URL url = new URL(url_request);
                uri     = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

                RestTemplate               restTemplate = new RestTemplate();
                StringHttpMessageConverter converter    = new StringHttpMessageConverter();

                restTemplate.getMessageConverters().add(converter);

                String xml = restTemplate.getForObject(uri, String.class);

                return xml;
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
        protected void onPostExecute(String xml) {
            String code;

            if (className.equals("BaseXML")) {// request logout
                goStartPage();
                return;
            }

            if (xml != null) {
                Serializer serializer = new Persister();

                if ( xml.indexOf("Session expired") != -1) {
                    goStartPage();
                    return;
                }
                if (className.equals("Transactions")) {
                    implementViewTransactions(xml, serializer);
                } else if (className.equals("TransactionXML")) {
                    implementTransaction(xml, serializer);
                } else if (className.equals("GetOperations")) {
                    implementGetOperations(xml, serializer);
                } else if (className.equals("XML_Login")) {
                    implementXmlLogin(xml, serializer);
                }
            }
        }
    }
    private void implementXmlLogin(String xml, Serializer serializer) {
        XML_Login xml_login;
        try {
            xml_login                  = serializer.read(XML_Login.class, xml);
            MainActivity main_activity = (MainActivity) activity;
            main_activity.passExecute(xml_login);

            return;
        } catch(RestClientException e) {

        } catch(Exception e) {

        }
        my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
    }
    private void implementTransaction(String xml, Serializer serializer) {
        TransactionXML xml_transaction;
        String         code;
        try {
            xml_transaction = serializer.read(TransactionXML.class, xml);
            code            = xml_transaction.getCode();
            if (code.equals("0")) {
                my_dialog.show(voc.getTranslatedString("Success"), R.mipmap.ic_success);
            } else  {
                nonZeroCode(code);
            }
            return;
        } catch(RestClientException e) {

        } catch(Exception e) {

        }
        my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
    }

    private void implementGetOperations(String xml, Serializer serializer) {
        String        code;
        GetOperations xml_operations_list;

        try {
            xml_operations_list = serializer.read(GetOperations.class, xml);
            code                = xml_operations_list.getCode();
            if (code.equals("0")) {
                List<Operation> list = xml_operations_list.getOperationsList();

                if (list == null) {
                    empty_list("Operations List is empty");
                    return;
                } else if (list.size() == 0) {
                    empty_list("Operations List is empty");
                    return;
                }

                RegularLoginActivity rla = (RegularLoginActivity) activity;
                rla.passFunction(xml_operations_list);
            } else  {
                nonZeroCode(code);
            }
            return;
        } catch(RestClientException e) {

        } catch(Exception e) {

        }
        my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
    }

    private void implementViewTransactions(String xml, Serializer serializer) {
        String       code;
        Transactions xml_transactions;

        try {
            xml_transactions = serializer.read(Transactions.class, xml);
            code             = xml_transactions.getCode();
            if (code.equals("0")) {
                List<Record> list = xml_transactions.getRecordsList();

                if (list == null) {
                    empty_list("List of transactions is empty");
                    return;
                } else if (list.size() == 0) {
                    empty_list("List of transactions is empty");
                    return;
                }
                FileManager FM = new FileManager(activity);
                FM.writeToFile("transactions_view.bin", xml_transactions);

                CommonClass c_c = new CommonClass();
                c_c.dateFrom    = xml_transactions.getDateStart();
                c_c.dateTill    = xml_transactions.getDateStop();

                Intent intent = new Intent(activity, TransactionsViewActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                intent.putExtra(MainActivity.MAIN_INFO, c_c);
                activity.startActivity(intent);
            } else {
                nonZeroCode(code);
            }

            return;
        } catch(RestClientException e) {

        } catch(Exception e) {

        }
        my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
    }
    private void nonZeroCode(String code) {
        if (code.equals("3")) {
            goStartPage();
        } else if (code.equals("1")) {
            my_dialog.show(voc.getTranslatedString("Mismatching Company ID"), R.mipmap.ic_failture);
        } else if (code.equals("2")) {
            my_dialog.show(voc.getTranslatedString("Operation Failed"), R.mipmap.ic_failture);
        } else {
            my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
        }
    }
    private void empty_list(String message) {
        my_dialog = new MyDialog(voc, base_layout);
        my_dialog.show(voc.getTranslatedString(message), R.mipmap.ic_zero);
    }

    private void goStartPage() {
        Intent intent = new Intent(activity, MainActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        activity.startActivity(intent);
    }


}
