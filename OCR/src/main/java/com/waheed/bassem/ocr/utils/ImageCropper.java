package com.waheed.bassem.ocr.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

public class ImageCropper {

    public static Bitmap cropImage(Context context,
                                   FrameLayout frameLayout,
                                   Bitmap bitmap,
                                   int rectWidthPX,
                                   int rectHeightDIP) {

        int frameWidth = frameLayout.getWidth();
        int frameHeight = frameLayout.getHeight();

        int rectHeightPX = (int) convertDpToPixel(context, rectHeightDIP);

        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();


        int rectWidthNew = (rectWidthPX * imageWidth) / frameWidth;
        int rectHeightNew = (rectHeightPX * imageHeight) / frameHeight;


        int x = (imageWidth / 2) - (rectWidthNew / 2);
        int y = (imageHeight / 2) - (rectHeightNew / 2);

        return Bitmap.createBitmap(bitmap, x, y, rectWidthNew, rectHeightNew);
    }

    private static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
