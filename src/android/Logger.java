package com.romba.logger;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.*;
import java.util.zip.*;
import java.io.IOException;

/**
 * This class echoes a string called from JavaScript.
 */
public class Logger extends CordovaPlugin {
    // private static BufferedWriter bufferedWriter;
    private static FileOutputStream fos;
    private BufferedOutputStream bos;
    private ZipOutputStream zos;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("initialize")) {
            this.initialize(args.getString(0),callbackContext);
            return true;
        }
        else if(action.equals("write")){
            // this.write(args.getString(0),callbackContext);
            class threaded implements Runnable{
                // BufferedWriter budWriter;
                ZipOutputStream zipWriter;
                CallbackContext cbx;
                String data;

                // threaded(String d,BufferedWriter bw, CallbackContext c){
                threaded(String d,ZipOutputStream zw, CallbackContext c){
                    // this.budWriter = bw;
                    this.zipWriter = zw;
                    this.cbx = c;
                    this.data = d;
                }
                public void run(){
                    try{
                        this.zipWriter.write(data.getBytes());
                        cbx.success("Written successfully");
                    } catch(Exception e){
                        cbx.error(e.toString());
                    }
                }
            }
            cordova.getThreadPool().execute(new threaded(args.getString(0),this.zos,callbackContext));
            return true;
        }
        else if(action.equals("close")){
            this.close(callbackContext);
            return true;
        }
        return false;
    }

    public void initialize(String fileName, CallbackContext callbackContext){
        try {
            // this.bufferedWriter = new BufferedWriter(new FileWriter("/sdcard/"+filename,false));
            // this.bufferedWriter.write("x,y");
            // this.bufferedWriter.write(content);
            // this.bufferedWriter.close();
            this.fos = new FileOutputStream("/sdcard/" + fileName + ".zip");
            this.bos = new BufferedOutputStream(fos);
            this.zos = new ZipOutputStream(bos);
            zos.putNextEntry( new ZipEntry(fileName + ".txt"));

            callbackContext.success("File Opened");

        } catch (IOException e) {
            callbackContext.error(e.getMessage());
        }
    }

    // public void write(String data, CallbackContext callbackContext){
    //     try{
    //         this.bufferedWriter.write(data);
    //         callbackContext.success("Written successfully");
    //     } catch(IOException e){
    //         callbackContext.error(e.toString());
    //     }
    // }

    private void close(CallbackContext callbackContext) {
        try{
            // this.bufferedWriter.close();
            zos.closeEntry();
            zos.close();
            callbackContext.success("Closed");
        } catch(IOException e){
            callbackContext.error(e.getMessage());
        }
    }
}