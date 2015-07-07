package com.nova.sme.sme01.miscellanea;

/*
 **************************************************
 *                                                *
 *   Support Malay & English User Interface       *
 *                                                *
 **************************************************
 */

import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Vocabulary {
    // The list is quiet short so that we use hardcoded info

    private Map<String, String> EngToMalay = new HashMap<String, String>();
    private Map<String, String> MalayToEng = new HashMap<String, String>();
    private String      current_language   = "EN"; //"MY"

    private String arr[][] = {
            {"Synchronize Operations List",                    "Menyegerak Senarai Operasi"},   // correct
            {"Lock Company",                                   "Syarikat Lock"},                // correct
            {"Log Out",                                        "Log Keluar"},                   // correct
            {"Perform Transaction",                            "Melaksanakan Transaks"},        // correct
            {"View Transactions",                              "Lihat Transaksi"},              // correct
            {"Synchronize Operations List",                    "Menyegerak Senarai Operasi"},   // correct
            {"User Name",                                      "Gelaran"},
            {"Password",                                       "Kata laluan"},
            {"Login",                                          "Log Masuk"},
            {"User Login",                                     "Log masuk pengguna"},
            {"User's role is not supported for mobile device", "Peranan Pengguna tidak disokong untuk peranti mudah alih"},
            {"Select Language",                                "Pilih Bahasa"},
            {"Authentication failed",                          "Pengesahan gagal"},
            {"Timeout is over",                                "Had masa tamat"},
            {"Unknown error",                                  "Ralat tidak diketahui"},
            {"English",                                        "Bahasa Inggeris"},
            {"Malay",                                          "Melayu"},
            {"Ok",                                             "Okey"},
            {"OK",                                             "OKey"},
            {"Settings",                                       "Tetapan"},
            {"Url Address",                                    "Url Alamat"},
            {"Color's Themes",                                 "Tema warna ini"},
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

}
