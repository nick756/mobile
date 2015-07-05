package com.nova.sme.sme01.miscellanea;

/*
 **************************************************
 *                                                *
 *   Read/Write from/to file                      *
 *                                                *
 **************************************************
 */

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
    private Context context;
    private String  file_name = "sme_data.txt";

    public FileManager(Context context) {
        this.context = context;
    }
    
    public void writeData(Parameters params) {
        try {
            File   file = this.context.getFileStreamPath(file_name);

            if (!file.exists())
                file.createNewFile();

            FileOutputStream writer = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            writer.write(params.getSerializedString().getBytes());
            writer.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } catch (Exception e) {

        }
    }
    public boolean readData(Parameters params) {
        boolean result = true;
        int     len;
        try {
            File file = this.context.getFileStreamPath(file_name);

            if (!file.exists()) return false;

            FileInputStream reader = context.openFileInput(file.getName());
            int             length = (int) file.length();
            byte[]          bytes  = new byte[length];
            FileInputStream in     = context.openFileInput(file.getName());

            try {
                in.read(bytes);
            } finally {
                in.close();
                reader.close();
            }

            String content = new String(bytes);
            if (content.equals(""))
                return false;
            params.takeSerializedString(content);
        } catch (FileNotFoundException e) {
            result = false;
        } catch (IOException e) {
            result = false;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


}
