package com.cake.recyclebitmap;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by lizhaoxuan on 2017/7/12.
 */

public class MetaData {

    private OnInputStream onInputStream;
    private int height;
    private int width;
    private int uuid;
    private boolean overturn;
    private boolean needAsync;
    private Bitmap.Config bitmapConfig = Bitmap.Config.RGB_565;
    private Map<Object, Object> metadata;

    MetaData(RecycleBitmap.Builder builder) {
        this.onInputStream = builder.getOnInputStream();
        this.height = builder.getHeight();
        this.width = builder.getWidth();
        this.uuid = builder.getUuid();
        this.overturn = builder.isOverturn();
        this.needAsync = builder.isNeedAsync();
        this.bitmapConfig = builder.getBitmapConfig();
        this.metadata = builder.getMetadata();
    }

    public OnInputStream getOnInputStream() {
        return onInputStream;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getUuid() {
        return uuid;
    }

    public boolean isOverturn() {
        return overturn;
    }

    public boolean isNeedAsync() {
        return needAsync;
    }

    public Bitmap.Config getBitmapConfig() {
        return bitmapConfig;
    }

    public Object getMetadata(Object key) {
        if (metadata == null) {
            return null;
        }
        return metadata.get(key);
    }
}
