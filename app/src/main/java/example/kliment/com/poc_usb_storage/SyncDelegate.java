package example.kliment.com.poc_usb_storage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Kliment on 9/21/2017.
 */

public class SyncDelegate {

  private static final String TAG = "SyncDelegate";

  private Context context;

  SyncDelegate(Context context){
    this.context = context;
  }

  public void syncFile(String filename){
    Log.d(TAG, "syncFile: called");
    File source = new File(context.getFilesDir()+"/"+filename);
    if(source.exists() && !source.isDirectory()) {
      Log.d(TAG, "syncFile: file exits on internal");
    }else{
      Log.d(TAG, "syncFile: file does not exist on internal");
    }
    File[] externalStorageFiles= ContextCompat.getExternalFilesDirs(context,null);
    for(int i =0;i<externalStorageFiles.length;i++){
      Log.d(TAG, "syncFile: external storage no. "+(i+1)+" "+externalStorageFiles[i].getAbsolutePath());
    }
    Log.d(TAG, "syncFile: kek -> "+context.getExternalFilesDir(null));
    File destination = new File(context.getExternalFilesDir(null) + "/"+filename);
    if(destination.exists() && !destination.isDirectory()) {
      Log.d(TAG, "syncFile: file exits on external");

    }else{
      Log.d(TAG, "syncFile: file does not exists on external");
      try{
        FileUtils.copyFile(source,destination);
      }catch (IOException e){
        e.printStackTrace();
      }
    }

  }

}
