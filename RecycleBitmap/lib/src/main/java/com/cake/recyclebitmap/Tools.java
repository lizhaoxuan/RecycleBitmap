package com.cake.recyclebitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by lizhaoxuan on 2017/7/12.
 */

class Tools {

    private Tools() {
    }

    static BitmapFactory.Options getOptions(int width, int height, boolean overturn, InputStream inputStream, Bitmap.Config bitmapConfig) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = bitmapConfig;
        options.inMutable = true;
        if (width == 0) {
            return options;
        }
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        int outHeight, outWidth;
        if (overturn) {
            outWidth = options.outWidth;
            outHeight = options.outHeight;
        } else {
            outHeight = options.outWidth;
            outWidth = options.outHeight;
        }

        int scale = 1;
        while (true) {
            if (outWidth / 2 < width || outHeight / 2 < height) {
                break;
            }
            outWidth /= 2;
            outHeight /= 2;
            scale *= 2;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        return options;
    }

    static byte[] inputStreamToByte(OnInputStream onInputStream) {
        try {
            InputStream in = onInputStream.getInputStream();
            if (in == null) {
                return null;
            }
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
