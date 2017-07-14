package com.cake.recyclebitmap;

import android.graphics.Bitmap;

/**
 * Created by lizhaoxuan on 2017/7/11.
 */

public class CakeBitmap {

    protected Bitmap bitmap;
    protected int width;
    protected int height;

    private int key;

    public CakeBitmap(MetaData metaData) {
        this.key = metaData.getUuid();
        this.width = metaData.getRealWidth();
        this.height = metaData.getRealHeight();
    }

    public CakeBitmap(Bitmap bitmap, int uuid) {
        this.key = uuid;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.bitmap = bitmap;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
