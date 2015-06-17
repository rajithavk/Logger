package com.romba.logger;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class echoes a string called from JavaScript.
 */
public class Logger extends CordovaPlugin {
    private static BufferedWriter bufferedWriter;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("initialize")) {
            this.initialize(args.getString(0),callbackContext);
            return true;
        }
        else if(action.equals("write")){
            // this.write(args.getString(0),callbackContext);
            class threaded implements Runnable{
                BufferedWriter budWriter;
                CallbackContext cbx;
                String data;

                threaded(String d,BufferedWriter bw, CallbackContext c){
                    this.budWriter = bw;
                    this.cbx = c;
                    this.data = d;
                }
                public void run(){
                    try{
                        this.budWriter.write(data);
                        cbx.success("Written successfully");
                    } catch(Exception e){
                        cbx.error(e.toString());
                    }
                }
            }
            cordova.getThreadPool().execute(new threaded(args.getString(0),this.bufferedWriter,callbackContext));
            return true;
        }
        else if(action.equals("close")){
            this.close(callbackContext);
            return true;
        }
        return false;
    }

    public void initialize(String filename, CallbackContext callbackContext){
        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter("/sdcard/"+filename,false));
            // this.bufferedWriter.write("x,y");
            // this.bufferedWriter.write(content);
            // this.bufferedWriter.close();
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
            this.bufferedWriter.close();
            callbackContext.success("Closed");
        } catch(IOException e){
            callbackContext.error(e.getMessage());
        }
    }
}