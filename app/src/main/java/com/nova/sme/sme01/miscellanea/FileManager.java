package com.nova.sme.sme01.miscellanea;

/*
 **************************************************
 *                                                *
 *   Read/Write from/to file                      *
 *                                                *
 **************************************************
 */

import android.content.Context;
import android.util.Log;

import com.nova.sme.sme01.transactions.GetOperations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class FileManager {
    private Context context;
//    private String  file_name = "sme_data.txt";

    public FileManager(Context context) {
        this.context = context;
    }

    public boolean writeToFile(String file_name, Object object){
        File file;
        try {
            file = this.context.getFileStreamPath(file_name);

            if (!file.exists())
                file.createNewFile();

            FileOutputStream writer = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos  = new ObjectOutputStream(writer);
            oos.writeObject(object);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();//java.io.NotSerializableException: com.nova.sme.sme01.miscellanea.Parameters$AllowedOperations
        }
        return false;
    }
    public Object readFromFile(String file_name){
        try {
            FileInputStream fis  = context.openFileInput(file_name);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object readObject    = is.readObject();
            is.close();

            return readObject;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteFile(String file_name) {
        File file;
        try {
            file = this.context.getFileStreamPath(file_name);

            if (!file.exists())
                return false;

            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
/*
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
*/

}
