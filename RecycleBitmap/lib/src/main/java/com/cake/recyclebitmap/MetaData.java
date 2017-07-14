package com.cake.recyclebitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lizhaoxuan on 2017/7/14.
 */

public class MetaData {

    OnInputStream onInputStream;
    int height;
    int width;
    int uuid;
    boolean overturn;
    boolean needAsync;
    Bitmap.Config bitmapConfig = Bitmap.Config.RGB_565;
    Map<Object, Object> metadata;
    BitmapFactory.Options options;
    private byte[] picDatas;

    int realHeight;
    int realWidth;

    public MetaData(View imageView) {
        this(imageView.getHeight(), imageView.getWidth(), imageView.hashCode());
    }

    public MetaData(int height, int width, int uuid) {
        this.height = height;
        this.width = width;
        this.uuid = uuid;
    }

    public MetaData setSource(final Context context, final int drawableId) {
        onInputStream = new OnInputStream() {
            @Override
            public InputStream getInputStream() {
                return context.getResources().openRawResource(drawableId);
            }
        };
        return this;
    }

    public MetaData setSource(final String path) {
        onInputStream = new OnInputStream() {
            @Override
            public InputStream getInputStream() {
                try {
                    return new FileInputStream(new File(path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        return this;
    }


    public MetaData setSource(final byte[] datas) {
        onInputStream = new OnInputStream() {
            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(datas);
            }
        };
        return this;
    }

    public MetaData setSource(OnInputStream onInputStream) {
        this.onInputStream = onInputStream;
        return this;
    }

    public MetaData setUuid(int uuid) {
        this.uuid = uuid;
        return this;
    }

    public MetaData setBitmapConfig(Bitmap.Config bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
        return this;
    }

    public MetaData needOverturn(boolean overturn) {
        this.overturn = overturn;
        return this;
    }

    public MetaData needAsync(boolean needAsync) {
        this.needAsync = needAsync;
        return this;
    }

    public MetaData putMetadata(Object key, Object value) {
        if (metadata == null) {
            metadata = new LinkedHashMap<>();
        }
        metadata.put(key, value);
        return this;
    }

    public OnInputStream getOnInputStream() {
        return onInputStream;
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

    public int getRealHeight() {
        return realHeight;
    }

    public int getRealWidth() {
        return realWidth;
    }

    protected void initOptions() {
        options = new BitmapFactory.Options();
        options.inPreferredConfig = bitmapConfig;
        options.inMutable = true;
        if (width == 0) {
            return;
        }
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(onInputStream.getInputStream(), null, options);
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
        this.realHeight = options.outHeight /= scale;
        this.realWidth = options.outWidth /= scale;
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
    }
}
