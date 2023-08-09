package com.brocode.models;



import android.os.Environment;
import android.util.Log;

import com.brocode.startups.ConManager;

import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;

import java.io.File;

public class fileManager {
    private  static  Socket socket = ConManager.getSocket();
    public static  void  getBaseFolder() throws JSONException {
        JSONObject result = new JSONObject();
        try{
            File x = Environment.getExternalStorageDirectory();
            for(File f : x.getCanonicalFile().listFiles()){
                JSONObject temp = new JSONObject();
                temp.put("Apath", f.getAbsolutePath());
                temp.put("path",f.getPath());
                temp.put("isFile" , f.isFile());
                temp.put("parent",f.getParent());
                result.put(f.getName(), temp);
            }
        }
        catch (Exception e){
                Log.d("StorageException : " , e.toString() );
                result.put("err",e.toString());
        }
        finally {
            socket.emit("e#postRootFiles", result);
        }

    }
    public static  void getListOfFile(String pathh){
        JSONObject result = new JSONObject();
        String path = Environment.getExternalStorageDirectory().toString()+"/" + pathh;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        try{
            for(File f : directory.listFiles()){
                JSONObject temp = new JSONObject();
                temp.put("Apath", f.getAbsolutePath());
                temp.put("path",f.getPath());
                temp.put("isFile" , f.isFile());
                temp.put("parent",f.getParent());
                result.put(f.getName(), temp);
                Log.d("storage", "APth :: " + f.getAbsolutePath() + " Name :: " + f.getName());
            }
            Log.d("Storage", result.toString());
        }catch (Exception e){
                Log.d("storage",e.toString());
        }
    }
}
