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
import com.nova.sme.sme01.xml_reader_classes.ListOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;
import com.nova.sme.sme01.xml_reader_classes.Record;
import com.nova.sme.sme01.xml_reader_classes.AddTransaction;
import com.nova.sme.sme01.xml_reader_classes.ListTransactions;
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

            ListTransactions xml_List_transactions;
            URI uri;//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=23/07/2015&dateTill=23/07/2015
            try {//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=21/06/2015&dateTill=22/07/2015
                URL url = new URL(url_request);//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=25/01/2015&dateTill=25/07/2015
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
                if (className.equals("ListTransactions")) {
                    implementViewTransactions(xml, serializer);
                } else if (className.equals("AddTransaction")) {
                    implementTransaction(xml, serializer);
                } else if (className.equals("ListOperations")) {//GetOperations")) {
                    implementGetOperations(xml, serializer);
                } else if (className.equals("XML_Login")) {
                    implementXmlLogin(xml, serializer);
                }
            } else {
                if (className.equals("XML_Login")) {
                    implementXmlLogin(null, null);
                } else {
                    goStartPage();
                }
            }
        }
    }

    private void implementXmlLogin(String xml, Serializer serializer) {
        XML_Login xml_login;
        MainActivity main_activity = (MainActivity) activity;

        if (xml != null && serializer != null) {
            try {
                xml_login = serializer.read(XML_Login.class, xml);
                main_activity.passExecute(xml_login);

                return;
            } catch (RestClientException e) {

            } catch (Exception e) {

            }
        }
        my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
        main_activity.reset_block_login_button();
    }


    /*
        Николай писал:
        ADDTRANSACTION
        ------------------------------
        Code = 0 Successful Operation
        Code = 1 Operation Failed
        Code = 3 Expired Session

        Код 1 возвращен в случае, если добавление транзакции вызвало ошибку на сервере, по любым причинам. ID транзакции в этом случае
        равно 0.
     */
    private void implementTransaction(String xml, Serializer serializer) {
        AddTransaction xml_transaction;
        String         code;
        try {
            xml_transaction = serializer.read(AddTransaction.class, xml);
            code            = xml_transaction.getCode();
            if (code.equals("0")) {
                my_dialog.show(voc.getTranslatedString("Success"), R.mipmap.ic_success);
            } else if (code.equals("1")) {
                my_dialog.show(voc.getTranslatedString("Operation Failed"), R.mipmap.ic_success);
            } else  { // Session expired or other error
/*
            Николай писал:
            Скорее всего, на мобильном приложении должна работать такая логика: если для какого-либо метода возврашен код не
            указанный во втором списке выше, то всегда делать принудительный лог-аут.
*/
                goStartPage();
            }
            return;
        } catch(RestClientException e) {

        } catch(Exception e) {

        }
        goStartPage();
//        my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
    }





/*
        Николай писал:

        GETOPERATIONS
        -----------------------------
        Code = 0 Successful Operation
        Code = 3 Expired Session

        Другие коды при промышленном использовании не возникают. Код 0 возвращен и в случае, если список пуст - просто админы забыли
        назначить профайл для компании.
*/
    private void implementGetOperations(String xml, Serializer serializer) {
        String        code;
        ListOperations xml_operations_list;

        try {
            xml_operations_list = serializer.read(ListOperations.class, xml);
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
                return;
            } else  {// Expired Session or any error
/*
            Николай писал:
            Скорее всего, на мобильном приложении должна работать такая логика: если для какого-либо метода возврашен код не
            указанный во втором списке выше, то всегда делать принудительный лог-аут.
*/

                goStartPage();
            }
        } catch(RestClientException e) {

        } catch(Exception e) {

        }
        goStartPage();
//        my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
    }




    /*
        LISTTRANSACTIONS
        --------------------------------
        Code = 0
        Code = 3 Expired Session
    */

    private void implementViewTransactions(String xml, Serializer serializer) {
        String       code;
        ListTransactions xml_List_transactions;

        try {
            xml_List_transactions = serializer.read(ListTransactions.class, xml);
            code             = xml_List_transactions.getCode();
            if (code.equals("0")) {
                List<Record> list = xml_List_transactions.getRecordsList();

                if (list == null) {
                    empty_list("List of transactions is empty");
                    return;
                } else if (list.size() == 0) {
                    empty_list("List of transactions is empty");
                    return;
                }
                FileManager FM = new FileManager(activity);
                FM.writeToFile("transactions_view.bin", xml_List_transactions);

                CommonClass c_c = new CommonClass();
                c_c.dateFrom    = xml_List_transactions.getDateStart();
                c_c.dateTill    = xml_List_transactions.getDateStop();

                Intent intent = new Intent(activity, TransactionsViewActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                intent.putExtra(MainActivity.MAIN_INFO, c_c);
                activity.startActivity(intent);
            } else {// Expired Session or any error
/*
            Николай писал:
            Скорее всего, на мобильном приложении должна работать такая логика: если для какого-либо метода возврашен код не
            указанный во втором списке выше, то всегда делать принудительный лог-аут.
*/
                goStartPage();
            }

            return;
        } catch(RestClientException e) {

        } catch(Exception e) {

        }
        goStartPage();
//        my_dialog.show(voc.getTranslatedString("Unknown error"), R.mipmap.ic_failture);
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
