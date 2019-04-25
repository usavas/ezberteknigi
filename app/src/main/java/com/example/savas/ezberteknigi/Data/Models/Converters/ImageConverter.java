package com.example.savas.ezberteknigi.Data.Models.Converters;

import android.arch.persistence.room.TypeConverter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageConverter {

    @TypeConverter
    public static byte[] toByteArray(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    @TypeConverter
    public static Bitmap toBitmap(byte[] bytes) {
        return (bytes == null) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
