package com.barcodescaner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsProvider;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TXTMechanic {



    static   String filePath = "/storage/emulated/0/Documents/QRCodes.txt";
    static File file;

    Activity activity= new Activity();

    public static String stringFilter(String str){
        String resultString=new String();
        String tmp=new String();
        if (str.trim().length()==0) resultString="_";
        else {
            tmp = str.replaceAll(" ", "_");
            resultString=tmp.replaceAll("\t","_");
        }
        return resultString;
    }


    public static   String spaceFilter(String str){
        String resultString=new String();
        resultString=str.replaceAll("_"," ");
        return resultString;
    }

    public static void saveRecord(Context context, String str, String filepath) throws IOException {


        FileWriter fileWriter = new FileWriter(filepath, true);
        try {
            fileWriter.append(str);
            fileWriter.flush();
            fileWriter.close();
            Toast toast;
            toast = Toast.makeText(context, R.string.record_added,Toast.LENGTH_LONG);
            toast.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            Toast toast= Toast.makeText(context,R.string.adding_data_error,Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static void deleteRecord(Context context, ArrayList<String> txt, String filePath, int stringNumber){

        File file;

        for(int i =0;i<4;i++)
            txt.remove(stringNumber);

        file=new File(filePath);


        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter fileWriter= null;
        try {
            fileWriter = new FileWriter(filePath,true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            for ( int i=0;i<txt.size();i++) {

                if(i % 4 == 0) fileWriter.append("\n");
                fileWriter.write(txt.get(i));
                fileWriter.flush();
                fileWriter.write(" ");
                fileWriter.flush();


            }
            fileWriter.close();
        } catch (IOException e) { System.out.println(e.getMessage());
            Toast toast= Toast.makeText(context,R.string.delite_error,Toast.LENGTH_LONG);
            toast.show();

        }

        Toast toast= Toast.makeText(context,R.string.record_dellited,Toast.LENGTH_LONG);
        toast.show();
    }

    public static void editRecord(Context context,ArrayList<String> txt,String filePath){
        FileWriter fileWriter= null;
        try {
            fileWriter = new FileWriter(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for ( int i=0;i<txt.size();i++) {

                if(i % 4 == 0) fileWriter.append("\n");
                fileWriter.write(txt.get(i));
                fileWriter.flush();
                fileWriter.write(" ");
                fileWriter.flush();


            }
            fileWriter.close();
        } catch (IOException e) { System.out.println(e.getMessage());
            Toast toast= Toast.makeText(context,R.string.Edit_error,Toast.LENGTH_LONG);
            toast.show();

        }

        Toast toast= Toast.makeText(context,R.string.record_editted,Toast.LENGTH_LONG);
        toast.show();

    }

    public static void purgeFile(){
        file=new File(filePath);
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int WRITE_REQUEST_CODE = 43;





}
