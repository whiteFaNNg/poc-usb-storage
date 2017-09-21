package example.kliment.com.poc_usb_storage;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kliment on 9/20/2017.
 */

public class FileUtils {

  public static void createRandomFile(Context context){
    String filename = String.valueOf ((new Date()).getTime());

    FileOutputStream outputStream;

    try {
      outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
      outputStream.write(filename.getBytes());
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static List<String> getFiles(Context context){
    List<String> items = new ArrayList<>();
    File directory = new File(context.getFilesDir(), "");
    File[] files = directory.listFiles();
    for (int i = 0; i < files.length; i++)
    {
      items.add(files[i].getName());
    }
    return items;
  }

  public static void copyFileOrDirectory(String srcDir, String dstDir) {

    try {
      File src = new File(srcDir);
      File dst = new File(dstDir, src.getName());

      if (src.isDirectory()) {

        String files[] = src.list();
        int filesLength = files.length;
        for (int i = 0; i < filesLength; i++) {
          String src1 = (new File(src, files[i]).getPath());
          String dst1 = dst.getPath();
          copyFileOrDirectory(src1, dst1);

        }
      } else {
        copyFile(src, dst);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void copyFile(File sourceFile, File destFile) throws IOException {
    if (!destFile.getParentFile().exists())
      destFile.getParentFile().mkdirs();

    if (!destFile.exists()) {
      destFile.createNewFile();
    }

    FileChannel source = null;
    FileChannel destination = null;

    try {
      source = new FileInputStream(sourceFile).getChannel();
      destination = new FileOutputStream(destFile).getChannel();
      destination.transferFrom(source, 0, source.size());
    } finally {
      if (source != null) {
        source.close();
      }
      if (destination != null) {
        destination.close();
      }
    }
  }

  public static byte[] readFile(String encryptedFileName) {
    byte[] contents = null;

    File file = new File(Environment.getExternalStorageDirectory()
        + File.separator, encryptedFileName);
    int size = (int) file.length();
    contents = new byte[size];
    try {
      BufferedInputStream buf = new BufferedInputStream(
          new FileInputStream(file));
      try {
        buf.read(contents);
        buf.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return contents;
  }

}
