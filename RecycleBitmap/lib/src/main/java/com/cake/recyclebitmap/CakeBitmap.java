package com.cake.recyclebitmap;

import android.graphics.Bitmap;

/**
 * Created by lizhaoxuan on 2017/7/11.
 */

public class CakeBitmap {

    protected Bitmap bitmap;
    protected int width;
    protected int height;

    private int uuid;

    public CakeBitmap(MetaData metaData) {
        this.uuid = metaData.getUuid();
        this.width = metaData.getRealWidth();
        this.height = metaData.getRealHeight();
    }

    public CakeBitmap(Bitmap bitmap, int uuid) {
        this.uuid = uuid;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.bitmap = bitmap;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public int getUuid() {
        return uuid;
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
