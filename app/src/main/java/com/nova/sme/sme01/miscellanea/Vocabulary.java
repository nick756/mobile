package com.nova.sme.sme01.miscellanea;

/*
 **************************************************
 *                                                *
 *   Support Malay & English User Interface       *
 *                                                *
 **************************************************
 */

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
      };

    public Vocabulary() {
        String key, value;
        for (int i = 0; i < arr.length; i ++) {
            key   = arr[i][0];
            value = arr[i][1];
            if (!EngToMalay.containsKey(key))
                EngToMalay.put(key, value);

            key   = arr[i][1];
            value = arr[i][0];
            if (!MalayToEng.containsKey(key))
                MalayToEng.put(key, value);
        }
    }

    public String getEnglish(String malay) {
        return MalayToEng.get(malay);
    }

    public String getMalay(String malay) {
        return EngToMalay.get(malay);
    }

    public void setLanguage(String lang) {
        current_language = lang;
    }
    public String getLanguage() {
        return current_language;
    }
}
