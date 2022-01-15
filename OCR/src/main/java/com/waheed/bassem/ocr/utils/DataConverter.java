package com.waheed.bassem.ocr.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import static com.waheed.bassem.ocr.utils.Constants.PREFIX;

public class DataConverter {


    public static String imageToBase64 (Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); //bm is the bitmap object
        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
        String base64String = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        String base64Fixed = base64String.replace("\n", "");

        return PREFIX + base64Fixed;
    }

}
