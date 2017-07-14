package com.cake.recyclebitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lizhaoxuan on 2017/7/11.
 */

public class RecycleBitmap {
    public static final int REUSE_NO_CACHE_STRATEGY = -10;
    public static final int REUSE_ONCE_CACHE_STRATEGY = -11;

    private static final String TAG = RecycleBitmap.class.getSimpleName();

    private AbstractReuseStrategy reuseStrategy;

    public static RecycleBitmap newInstanceCustomerCache(int cacheNum) {
        if (cacheNum <= 0) {
            return new RecycleBitmap(new ReuseNoCacheStrategy());
        }
        switch (cacheNum) {
            case 1:
                return new RecycleBitmap(new ReuseOnceCacheStrategy());
            default:
                return new RecycleBitmap(new ReuseCustomerCacheStrategy(cacheNum));
        }
    }

    public static RecycleBitmap newInstance() {
        return newInstance(REUSE_ONCE_CACHE_STRATEGY);
    }

    public static RecycleBitmap newInstance(int strategyType) {
        switch (strategyType) {
            case REUSE_NO_CACHE_STRATEGY:
                return new RecycleBitmap(new ReuseNoCacheStrategy());
            case REUSE_ONCE_CACHE_STRATEGY:
            default:
                return new RecycleBitmap(new ReuseOnceCacheStrategy());
        }
    }

    public static RecycleBitmap newInstance(AbstractReuseStrategy reuseManager) {
        return new RecycleBitmap(reuseManager);
    }

    private RecycleBitmap(AbstractReuseStrategy reuseManager) {
        this.reuseStrategy = reuseManager;
    }

    public synchronized void recycle(int uuid) {
        reuseStrategy.recycle(uuid);
    }

    public synchronized void destroy() {
        reuseStrategy.destroy();
    }


    public synchronized void setImageForViewOnPost(final ImageView view, final MetaData metaData) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (metaData.needAsync) {
                    new CreateBitmapTask(view, RecycleBitmap.this).execute(metaData);
                } else {
                    view.setImageBitmap(createBitmap(metaData));
                }
            }
        });
    }

    public synchronized Bitmap createBitmap(MetaData metaData) {
        if (checkAndInitOptions(metaData)) {
            Log.d(TAG, "onInputStream == null or onInputStream.getInputStream() == null");
            return null;
        }

        CakeBitmap cakeBitmap = reuseStrategy.OnSelector(metaData);
        if (cakeBitmap == null) {
            throw new NullPointerException("method Strategy.OnSelector() Can't return null !!!");
        }
        return createBitmap(cakeBitmap,
                metaData.options,
                metaData.onInputStream,
                metaData.uuid);
    }

    private boolean checkAndInitOptions(MetaData builder) {
        if (builder.onInputStream == null || builder.onInputStream.getInputStream() == null) {
            return true;
        }
        builder.initOptions();
        return false;
    }

    private Bitmap createBitmap(CakeBitmap cakeBitmap, BitmapFactory.Options options, OnInputStream onInputStream, int uuid) {
        boolean reuseSuccess = true;
        options.inBitmap = cakeBitmap.bitmap;
        Bitmap result;

        try {
            result = BitmapFactory.decodeStream(onInputStream.getInputStream(), null, options);
            if (result == null) {
                result = createBitmapForByte(onInputStream, options);
            }
            Log.d(TAG, "复用成功");
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "复用失败：" + e.getMessage());
            reuseSuccess = false;
            //复用失败重新创建
            result = newCakeAndRecycleOld(onInputStream, options);
        }

        if (result == null) {
            Log.d(TAG, "解码失败，return null");
            return null;
        }

        reuseStrategy.put(result, cakeBitmap, uuid, reuseSuccess);

        return result;
    }

    /**
     * 复用失败，创建新的Cake,废弃旧的Cake
     */
    private Bitmap newCakeAndRecycleOld(OnInputStream onInputStream, BitmapFactory.Options options) {
        options.inBitmap = null;
        Log.d(TAG, "复用失败，newCakeBitmap");
        Bitmap result;
        result = BitmapFactory.decodeStream(onInputStream.getInputStream(), null, options);
        if (result == null) {
            return createBitmapForByte(onInputStream, options);
        }
        return result;
    }

    private Bitmap createBitmapForByte(OnInputStream onInputStream, BitmapFactory.Options options) throws IllegalArgumentException {
        byte[] data = Tools.inputStreamToByte(onInputStream);
        if (data == null || data.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }
}
