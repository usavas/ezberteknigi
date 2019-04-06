package com.example.savas.ezberteknigi.BLL.Helper;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.FileOutputStream;
import java.io.File;


public class SaveFileHelper {

    public static void saveImageToLocalFile(Context context, String fileName, byte[] bitmapBytes) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(bitmapBytes);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getBitmapFromLocalFile(Context context, String fileName){
        return null;
    }

}
