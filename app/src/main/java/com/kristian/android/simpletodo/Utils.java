package com.kristian.android.simpletodo;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kristianss27 on 8/5/16.
 */
public class Utils{
    private String className = "Utils";

    public ArrayList<String> readItem(File fileDir){
        Log.d(className+".readItem()","Dir: "+fileDir.getName());
        ArrayList<String> items = null;

        File todoFile = new File(fileDir,"todo.txt");
        if(todoFile.exists()){
            try{
                items = new ArrayList<String>(FileUtils.readLines(todoFile));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return items;
    }

    public boolean writeItem(File fileDir, ArrayList<String> items){
        Log.d(className+".writeItem()","Dir: "+fileDir.getName());
        File todoFile = new File(fileDir,"todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteFile(File file) throws IOException{
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public ArrayAdapter populateSpinner(Context context, int textArrayResId, int textViewResId){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,textArrayResId,textViewResId);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }

    /*
    Activity define the context
        int multipleShare 0:to use only one sharedPreference or 1: multipleSharePreference


    public SharedPreferences getSharePreferences(Activity activity, int multipleShare){

        if(multipleShare==0){
            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("user_connected",);
        }

    }*/

    /*
    * (With an eye toward)Buffering string best practice - Make sure you're properly buffering streams when reading or writing streams,
    * especially when working with files.
    * Just decorate your FileInputStream with a BufferedInputStream below:
    *
    * InputStream in = new java.io.FileInputStream(myfile);
    try {
    in = new java.io.BufferedInputStream(in);

       in.read(.....
     } finally {
       IOUtils.closeQuietly(in);
     }
    * */
}
