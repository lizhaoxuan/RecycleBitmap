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


    public static class MetaData {

        OnInputStream onInputStream;
        int height;
        int width;
        int uuid;
        boolean overturn;
        boolean needAsync;
        Bitmap.Config bitmapConfig = Bitmap.Config.RGB_565;
        Map<Object, Object> metadata;
        BitmapFactory.Options options;

        int realHeight;
        int realWidth;

        public MetaData(ImageView imageView) {
            this(imageView.getHeight(), imageView.getWidth(), imageView.hashCode());
        }

        public MetaData(int height, int width, int uuid) {
            this.height = height;
            this.width = width;
            this.uuid = uuid;
        }

        public MetaData addSource(final Context context, final int drawableId) {
            onInputStream = new OnInputStream() {
                @Override
                public InputStream getInputStream() {
                    return context.getResources().openRawResource(drawableId);
                }
            };
            return this;
        }

        public MetaData addSource(final String path) {
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

        public MetaData addSource(OnInputStream onInputStream) {
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
}
