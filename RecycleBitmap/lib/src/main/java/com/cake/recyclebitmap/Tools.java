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
