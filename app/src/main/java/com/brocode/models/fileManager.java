package com.brocode.models;



import android.os.Environment;
import android.util.Log;

import com.brocode.startups.ConManager;
import com.brocode.utils.ChunkedFileUpload;
import com.brocode.utils.Constants;
import com.brocode.utils.ImageToBase64;
import com.brocode.utils.ZipFolder;

import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
                temp.put("numberofItems",getFolderItems(f));
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
    private static int getFolderItems(File f){
        try{
            return  f.list().length;
        }
        catch (Exception e){

        }
        return  0;
    }
    public static  void getListOfFile(Object [] args){
        JSONObject result = new JSONObject();
        String filePath = "";
        for(int i =0;i< args.length;i++) filePath = (String) args[i];
        try{
            String path = filePath;
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            for(File f : directory.listFiles()){
                JSONObject temp = new JSONObject();
                temp.put("Apath", f.getAbsolutePath());
                temp.put("path",f.getPath());
                temp.put("isFile" , f.isFile());
                temp.put("parent",f.getParent());
                temp.put("lastModified", f.getUsableSpace());
                temp.put("numberofItems",getFolderItems(f));
//                temp.put("size",)
                result.put(f.getName(), temp);

                Log.d("storage", "APth :: " + f.getAbsolutePath() + " Name :: " + f.getName());
            }
            Log.d("Storage", result.toString());
        }catch (Exception e){
                Log.d("storage",e.toString());
        }
        finally {
                socket.emit("sendByPath", result);


        }
    }
    public static void DownloadFoler(Object [] args ){
        String path = "";

        for(int i = 0;i< args.length;i++) path = (String) args[i];

        try {
            String rootPath = Environment.getExternalStorageDirectory().toString() + "/" + path;
            ZipFolder.zipDirectory(rootPath,Environment.getExternalStorageDirectory().toString()  + "/file"+path+".zip" );
        }
        catch (Exception e){
            Log.d("zip", e.toString());
        }
        finally {
            Log.d("uploading","uploadin to the server");
            socket.emit("fileZipDone", "file Zip Done");
            uploadToTheServer(path);
        }

    }
    public  static  void uploadToTheServer(String path){
        ExecutorService executorService  =  Executors.newSingleThreadExecutor();
        Future<?> uploadTask = executorService.submit(()->{

            try {
                String filePath = Environment.getExternalStorageDirectory().toString() +  "/file"+path+".zip";
                ChunkedFileUpload.uploadFileInChunks(Constants.SOCKET_URL  + "/downloadZip" , filePath);
                socket.emit("zipUplaodDone", "done");
                File file = new File(filePath);
                file.delete();

                

            } catch (IOException e) {
                Log.d("serverError" , e.toString());
            }
            finally {
                executorService.shutdown();
            }
        });

    }


    public static void DownloadImage(Object[] args) {
        String path = "";
        for(int i=0;i< args.length;i++){
            path = (String) args[i];
        }
        try {
            File f = new File(path);
            String image = ImageToBase64.getbase64Image(path);

            JSONObject obj = new JSONObject();
            obj.put("src", image);
            obj.put("name",f.getName());
            socket.emit("uplaodImageformPhone", obj);
        }
        catch (Exception e){
            Log.d("image", "DownloadImage: " + e.toString());

        }


    }
}
