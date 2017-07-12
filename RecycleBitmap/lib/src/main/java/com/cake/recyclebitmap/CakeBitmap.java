package com.cake.recyclebitmap;

import android.graphics.Bitmap;

/**
 * Created by lizhaoxuan on 2017/7/11.
 */

public class CakeBitmap {

    protected Bitmap bitmap;

    private int key;

    public CakeBitmap(int key) {
        this.key = key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
