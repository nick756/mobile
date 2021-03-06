package com.nova.sme.sme01.miscellanea;



import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nova.sme.sme01.R;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static java.sql.DriverManager.println;

/*
 **************************************************
 *                                                *
 *   Support Malay & English User Interface       *
 *                                                *
 **************************************************
 */

public class Vocabulary implements Serializable {
    // The list is quiet short so that we use hardcoded info

    private Map<String, String> EngToMalay = new HashMap<String, String>();
    private Map<String, String> MalayToEng = new HashMap<String, String>();
    private String      current_language   = "EN"; //"MY"
/*
    public  String      months[] = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "Augustus",
            "September",
            "October",
            "November",
            "December"
    };
*/
    private String arr[][] = {
        {"Synchronize Operations List",                         "Selaraskan Senarai Operasi"},
        {"Lock Company",                                        "Kekalkan Syarikat"},
        {"Log Out",                                             "Log Keluar"},
        {"Logout",                                              "Log Keluar"},
        {"Perform Transaction",                                 "Lakukan Transaksi"},      // correct
        {"View Transactions",                                   "Senaraikan Transaksi"},   // correct
        {"Synchronize Operations List",                         "Selaraskan Transaksi"},   // correct
        {"User Name",                                           "ID Pengguna"},
        {"Password",                                            "Kata laluan"},
        {"Login",                                               "Log Masuk"},
        {"User Login",                                          "Log masuk pengguna"},
        {"User Role is not supported for Mobile Interface",     "Peranan Pengguna tidak disokong untuk perantara muka mudah alih"},
        {"Select Language",                                     "Pilih Bahasa"},
        {"Authentication Failed",                               "Pengesahan Gagal"},
        {"Timeout is over",                                     "Had masa tamat"},
        {"Unknown error",                                       "Ralat tidak diketahui"},
        {"English",                                             "Bahasa Inggeris"},
        {"Malay",                                               "Melayu"},
        {"Ok",                                                  "OK"}, // correct
        {"OK",                                                  "OK"}, // correct
        {"Settings",                                            "Tetapan"},
        {"Url Address",                                         "Alamat URL"}, // correct
        {"Color's Themes",                                      "Tema warna ini"},
        {"First Time Login",                                    "Login Kali Pertama"},
        {"Regular Login",                                       "Log Masuk Biasa"},
        {"Mismatching Company ID",                              "ID Syarikat yang tidak sepadan"},
        {"Session expired",                                     "Sesi tamat"},
        {"Operations List has been saved successfully",         "Senarai Operasi telah berjaya disimpan"},
        {"Error of saving Operations List",                     "Ralat penjimatan Senarai Operasi"},
        {"Error occured",                                       "Ralat berlaku"},
        {"Operations List is empty",                            "Senarai Operasi kosong"},
        {"Reset Operations List",                               "Set semula Senarai Operasi"},// correct
        {"Please, select operation",                            "Sila pilih operasi"},
        {"Submit",                                              "Hantar"},
        {"Success",                                             "Kejayaan"},
        {"Description",                                         "Penerangan"},
        {"Amount",                                              "Jumlah"},
        {"Cancel",                                              "Batal"},
        {"Make Transaction",                                    "Buat Transaksi"},
        {"Operation failed",                                    "Operasi gagal"},
        {"Description is empty",                                "Penerangan kosong"},
        {"Amount is empty",                                     "Jumlah kosong"},
        {"From",                                                "Dari"},
        {"Till",                                                "Hingga"},
        {"List of transactions",                                "Senarai transaksi"},
        {"List of transactions is empty",                       "Senarai transaksi kosong"},
        {"Please, wait for Server response",                    "Sila tunggu respon Server" },
        {"Perform",                                             "laksanakan"},
        {"Accept",                                              "Terima"},
        {"Buttons Themes",                                      "Butang Tema"},
        {"Colors Themes",                                       "Warna Tema"},
        {"Reset",                                               "Set semula"},
        {"Select/Unselect All",                                 "Pilih/Nyahpilih Semua"},
        {"Date is wrong",                                       "Tarikh salah"},
        {"Base Functionality",                                  "Fungsi asas"},
        {"General",                                             "Umum"},
        {"Login Procedure",                                     "Prosedur Log Masuk"},
        {"Operations List",                                     "Senarai operasi"},
        {"Perform Transactions",                                "Lakukan Urusniaga"},
//        {"View Transactions",                                   "Lihat Urusniaga"},
        {"Setting",                                             "Tetapan"},
        {"Select Language",                                     "Pilih Bahasa"},
        {"URL Address",                                         "Alamat URL"},
        {"Buttons Themes",                                      "Butang Tema"},
        {"Colors Themes",                                       "Warna Tema"},
        {"Miscellaneous",                                       "Pelbagai"},
        {"Troubleshooting",                                     "Penyelesaian masalah"},
        {"Help",                                                "Bantuan"},
        {"About",                                               "Mengenai"},
        {"Application Settings",                                "Tetapan Aplikasi"},
        {"General Information",                                 "Informasi Am"},
        {"Re-synchronization is needed",                        "Penyelarasan semula diperlukan"},
        {"Upload Photo",                                        "Muatnaik Gambar"},
        {"Date",                                                "Tarikh"},
        {"Operation type",                                      "Jenis operasi"},
        {"Highest amount first",                                "Jumlah tertinggi dahulu"},
        {"Lowest amount first",                                 "Jumlah terendah dahulu"},
        {"Date descending",                                     "Tarikh menurun"},
        {"Date ascending",                                      "Tarikh menaik"},
        {"Sort by:",                                            "Susun mengikut:"},
        {"Help Topics",                                         "Topik Bantuan"},
        {"Cash",                                                "Tunai"},
        {"Exit",                                                "Keluar"},

//TranslateAll
    };

    public Vocabulary() {
        String key, value;
        for (int i = 0; i < arr.length; i ++) {
            key   = arr[i][0];
            value = arr[i][1];
            if (!EngToMalay.containsKey(key))
                EngToMalay.put(key, value);

            key   = key.toUpperCase();
            value = value.toUpperCase();
            if (!EngToMalay.containsKey(key))
                EngToMalay.put(key, value);


            key   = arr[i][1];
            value = arr[i][0];
            if (!MalayToEng.containsKey(key))
                MalayToEng.put(key, value);

            key   = key.toUpperCase();
            value = value.toUpperCase();
            if (!EngToMalay.containsKey(key))
                EngToMalay.put(key, value);

        }
    }

    public String getEnglish(String malay) {
        String ret = MalayToEng.get(malay);
        if (ret == null)
            return malay;
        if (ret.length() == 0)
            return malay;

        return ret;
    }

    public String getMalay(String eng) {
        String ret = EngToMalay.get(eng);
        if (ret == null)
            return eng;
        if (ret.length() == 0)
            return eng;

        return ret;
    }

    public void setLanguage(String lang) {
        if (lang == null) lang = "EN";
        current_language = lang;
    }
    public String getLanguage() {
        return current_language;
    }

    public String getTranslatedString(String str) {
        if (current_language.equals("EN"))
            return getEnglish(str);
        else
            return getMalay(str);
    }

    public void change_views_captions(Vector<View> views) {

        for (int i = 0; i < views.size(); i ++)
            change_caption(views.get(i));
    }

    public boolean change_caption(View view) {
        String class_name = view.getClass().getName().toString();
        if (class_name.indexOf("Button") != -1) {
            change_caption((Button) view);
            return true;
        } else if(class_name.indexOf("TextView") != -1) {
            change_caption((TextView) view);
            return true;
        } else if(class_name.indexOf("MenuItem") != -1) {
            change_caption((MenuItem) view);
            return true;
        } else if(class_name.indexOf("EditText") != -1) {
            change_caption((EditText) view);
            return true;
        } else if(class_name.indexOf("CheckBox") != -1) {
            change_caption((CheckBox) view);
            return true;
        }
        return false;
    }
    public void change_caption(CheckBox cb) {
        if (getLanguage().equals("MY"))
            cb.setText(getMalay(cb.getText().toString()));
        else
            cb.setText(getEnglish(cb.getText().toString()));
    }

    public void change_caption(TextView view) {
        if (getLanguage().equals("MY"))
            view.setText(getMalay(view.getText().toString()));
        else
            view.setText(getEnglish(view.getText().toString()));
    }
    public void change_caption(MenuItem item) {
        if (getLanguage().equals("MY"))
            item.setTitle(getMalay(item.getTitle().toString()));
        else
            item.setTitle(getEnglish(item.getTitle().toString()));
    }

    public void change_caption(EditText edit) {
        if (getLanguage().equals("MY"))
            edit.setHint(getMalay(edit.getHint().toString()));
        else
            edit.setHint(getEnglish(edit.getHint().toString()));
    }

    public void change_caption(Button bt) {
        if (getLanguage().equals("MY"))
            bt.setText(getMalay(bt.getText().toString()));
        else
            bt.setText(getEnglish(bt.getText().toString()));
    }

    public void change_captions(Vector<Button> btns) {
        Button bt;
        for (int i = 0; i < btns.size(); i ++) {
            bt = btns.get(i);
            if (getLanguage().equals("MY"))
                bt.setText(getMalay(bt.getText().toString()));
            else
                bt.setText(getEnglish(bt.getText().toString()));
        }
    }

    public String  change_caption(String str) {
        if (getLanguage().equals("MY"))
            return getMalay(str);
        else
            return str;
    }

    public void TranslateAll(View view) {
        String class_name = view.getClass().getName().toString();

        if (class_name.indexOf("RelativeLayout") != -1) {
            RelativeLayout layout =  (RelativeLayout) view;
            for (int i = 0; i < layout.getChildCount(); i ++) {
                view = layout.getChildAt(i);
                try {
                    if (!change_caption(view))
                        TranslateAll(view);
                } catch(Exception err) {

                }
            }

        } else if (class_name.indexOf("LinearLayout") != -1) {
            LinearLayout layout = (LinearLayout) view;
            for (int i = 0; i < layout.getChildCount(); i ++) {
                view = layout.getChildAt(i);
                try {
                    if (!change_caption(view))
                        TranslateAll(view);
                } catch(Exception err) {

                }
            }
        } else if (class_name.indexOf("Spinner") >= 0) {
            String tag = (String) view.getTag();
            if (tag == null) return;

            if (tag.equals("translate me")) {
                ArrayList<String> temp = new ArrayList<String>();
                String  item_name;
                Spinner spinner              = (Spinner) view;
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
                for (int j = 0; j < adapter.getCount(); j ++) {
                    item_name = adapter.getItem(j);
                    temp.add(getTranslatedString(item_name));
                }
                try {
                    adapter.clear();
                    for (int j = 0; j < temp.size(); j++)
                        adapter.add(temp.get(j));
                } catch(Exception e) {
                    println(e.getMessage().toString());
                }
                temp.clear();
                temp = null;
            }

        }
    }

}
