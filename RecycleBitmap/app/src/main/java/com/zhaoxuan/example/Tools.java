package com.zhaoxuan.example;

import android.content.Context;

import java.io.InputStream;

/**
 * Created by lizhaoxuan on 2017/7/13.
 */

public class Tools {

    private Tools() {
    }

    public static byte[] readFile(Context context, String fileName) {
        try {
            //得到资源中的asset数据流
            InputStream in = context.getResources().getAssets().open(fileName);
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
