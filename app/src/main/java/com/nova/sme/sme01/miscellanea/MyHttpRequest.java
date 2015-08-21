package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nova.sme.sme01.CommonClass;
import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.MainActivity;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.RegularLoginActivity;
import com.nova.sme.sme01.TransactionsViewActivity;
import com.nova.sme.sme01.miscellanea.Dialogs.GifDialog;
import com.nova.sme.sme01.miscellanea.Dialogs.MyDialog;
import com.nova.sme.sme01.xml_reader_classes.ListOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;
import com.nova.sme.sme01.xml_reader_classes.Record;
import com.nova.sme.sme01.xml_reader_classes.AddTransaction;
import com.nova.sme.sme01.xml_reader_classes.ListTransactions;
import com.nova.sme.sme01.xml_reader_classes.XML_Login;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.support.Base64;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import static java.sql.DriverManager.println;

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
    private FormResizing    FR;
    private String          className;
    private FileManager     FM;
    private GifDialog       gif_doalog = null;

    private String          photoPath = "";
//    private Bitmap          bitmap;
    private String          data = "";

    public MyHttpRequest(FormResizing  FR, Activity activity, RelativeLayout base_layout, Vocabulary voc, String url_request, String className) {
        this.activity    = activity;
        this.url_request = url_request;//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=27/01/2015&dateTill=27/07/2015
        this.base_layout = base_layout;
        this.voc         = voc;
        this.className   = className;
        this.FR          = FR;
        this.my_dialog   = new MyDialog(FR, voc, base_layout);

        FM = new FileManager(activity);
        new Http_Request().execute();
    }

    public MyHttpRequest(FormResizing  FR, Activity activity, RelativeLayout base_layout, Vocabulary voc, String url_request, String className, GifDialog gif_doalog) {
        this.activity    = activity;
        this.url_request = url_request;//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=27/01/2015&dateTill=27/07/2015
        this.base_layout = base_layout;
        this.voc         = voc;
        this.className   = className;
        this.FR          = FR;
        this.my_dialog   = new MyDialog(FR, voc, base_layout);
        this.gif_doalog  = gif_doalog;


        FM = new FileManager(activity);
        new Http_Request().execute();
    }

    public MyHttpRequest(FormResizing  FR, Activity activity, RelativeLayout base_layout, Vocabulary voc, String url_request, String className, GifDialog gif_doalog, String photoPath) {
        this.activity    = activity;
        this.url_request = url_request;//http://103.6.239.242/sme/mobile/listtransactions/?id=4&dateFrom=27/01/2015&dateTill=27/07/2015
        this.base_layout = base_layout;
        this.voc         = voc;
        this.className   = className;
        this.FR          = FR;
        this.my_dialog   = new MyDialog(FR, voc, base_layout);
        this.gif_doalog  = gif_doalog;
        this.photoPath   = photoPath;


        Bitmap bitmap;

        try {
            bitmap = BitmapFactory.decodeFile(photoPath);

        } catch (OutOfMemoryError e) {
            bitmap = null;
        } catch (Exception e) {
            bitmap = null;
        }

        if (bitmap != null){
            getStringByte(bitmap, 90);
            if (data.length() == 0) {
                Toast.makeText(gif_doalog.getContext(), "OUT OF MEMORY", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(gif_doalog.getContext(), "DATA IS OK", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(gif_doalog.getContext(), "OUT OF MEMORY", Toast.LENGTH_LONG).show();
        }

        FM = new FileManager(activity);
        new Http_Request().execute();
    }

    private void getStringByte(Bitmap bitmap, int n) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, n, bao);
            bitmap.recycle();
            byte[] ba = bao.toByteArray();
            data = Base64.encodeBytes(ba, Base64.ENCODE);
        } catch (OutOfMemoryError e) {
            data = "";
        } catch (java.io.IOException e) {
            data = "";
        } catch (Exception e) {
            data = "";
        }
    }


    private class Http_Request extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            String error;

//            ListTransactions xml_List_transactions;
            URI uri;
            try {
                URL url = new URL(url_request);
                uri     = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

                RestTemplate               restTemplate = new RestTemplate();
                StringHttpMessageConverter converter    = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(converter);

                String xml = "";
                if (className.equals("AddTransaction") && photoPath.length() > 0 && data.length() > 0) {
                    xml = restTemplate.postForObject(uri, data, String.class);//restTemplate.getForObject(uri, String.class);
 //                   Toast.makeText(gif_doalog.getContext(), "OKOKOKOK", Toast.LENGTH_LONG).show();
                } else {
                    xml = restTemplate.getForObject(uri, String.class);
                }

                return xml;
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

    private void temp(String fileToUpload, String data, String url_request) {
//        String fileToUpload = "";// = dir.getPath() + File.separator + fileName;
/*
        URL url = new URL(url_request);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", new FileSystemResource(fileToUpload));

        RestTemplate               restTemplate = new RestTemplate();
        StringHttpMessageConverter converter    = new StringHttpMessageConverter();
        restTemplate.getMessageConverters().add(converter);

        restTemplate.postForObject(uri, data, String.class);

*/
//        String response = rest.postForObject(uploadUri, parts, String.class, authToken, folderId);
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
 //       MyDialog my_dialog = new MyDialog(null, voc, base_layout);
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

        if (gif_doalog != null) {
            gif_doalog.dismiss();
            Toast.makeText(gif_doalog.getContext(), "Image has been sent", Toast.LENGTH_LONG).show();
        }

        try {
            xml_transaction = serializer.read(AddTransaction.class, xml);
            code            = xml_transaction.getCode();
            if (code.equals("0")) {
                my_dialog.show(voc.getTranslatedString("Success"), R.mipmap.ic_success, "RegularActivity");
            } else if (code.equals("1")) {

                my_dialog.show(voc.getTranslatedString("Operation Failed"), R.mipmap.ic_failture);
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
                xml_operations_list.sort();
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
    }




    /*
        LISTTRANSACTIONS
        --------------------------------
        Code = 0
        Code = 3 Expired Session
    */
    private void implementViewTransactions(String xml, Serializer serializer) {
        String       code;
        boolean      empty = true;
        ListTransactions xml_List_transactions;
        try {
            xml_List_transactions = serializer.read(ListTransactions.class, xml);
            code             = xml_List_transactions.getCode();
            if (code.equals("0")) {
                List<Record> list = xml_List_transactions.getRecordsList();

                if (list == null) {
                    if (gif_doalog != null)
                        gif_doalog.dismiss();

                    empty_list("List of transactions is empty");
                    return;
                } else if (list.size() == 0) {
                    if (gif_doalog != null)
                        gif_doalog.dismiss();

                    empty_list("List of transactions is empty");
                    return;
                }

                // additional  stuff
                OperationsSelector operationSelector = (OperationsSelector) FM.readFromFile("OperationsSelector.bin");
                if (operationSelector != null) {
                    Record record;
                    String operationType;
                    for (int j = 0; j < list.size(); j ++) {
                        record        = list.get(j);
                        operationType = record.getType();

                        if (operationType.indexOf("Bank Charges") != -1 || operationType.indexOf("Capital Injection") != -1)
                            println("");

                        if (operationSelector.isCheckedFullName(operationType)) {
                            empty = false;
                            break;
                        }
                    }
                    //OUT:Bank Charges
                    //IN:Capital Injection
                    if (empty) {
                        if (gif_doalog != null)
                            gif_doalog.dismiss();

                        empty_list("List of transactions is empty");
                        return;
                    }

                }

                FileManager FM = new FileManager(activity);
                FM.writeToFile("transactions_view.bin", xml_List_transactions);

                CommonClass c_c = new CommonClass();
                c_c.dateFrom    = xml_List_transactions.getDateStart();
                c_c.dateTill    = xml_List_transactions.getDateStop();

                Intent intent = new Intent(activity, TransactionsViewActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MainActivity.MAIN_INFO, c_c);

                if (gif_doalog != null)
                    gif_doalog.dismiss();

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
    }

    private void empty_list(String message) {
        my_dialog.show(voc.getTranslatedString(message), R.mipmap.ic_zero);
    }

    private void goStartPage() {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        activity.startActivity(intent);
    }

}
