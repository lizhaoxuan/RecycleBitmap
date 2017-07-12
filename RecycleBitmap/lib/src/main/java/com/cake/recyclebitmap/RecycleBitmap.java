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
    private static final String TAG = RecycleBitmap.class.getSimpleName();

    private AbstractReuseManager reuseManager;

    public static RecycleBitmap newInstance() {
        return new RecycleBitmap(new DefaultReuseManager());
    }

    public static RecycleBitmap newInstance(AbstractReuseManager reuseManager) {
        return new RecycleBitmap(reuseManager);
    }

    public RecycleBitmap(AbstractReuseManager reuseManager) {
        this.reuseManager = reuseManager;
    }

    public void recycle(int uuid) {
        reuseManager.recycle(uuid);
    }

    public void destroy() {
        reuseManager.destroy();
    }


    public void setImageForViewOnPost(final ImageView view, final Builder builder) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (builder.needAsync) {
                    new CreateBitmapTask(view, RecycleBitmap.this).execute(builder);
                } else {
                    view.setImageBitmap(builder.createBitmap(RecycleBitmap.this));
                }
            }
        });
    }

    public Bitmap createBitmap(Builder builder) {
        CakeBitmap cakeBitmap = reuseManager.OnSelector(new MetaData(builder));
        return createBitmap(cakeBitmap,
                builder.getWidth(),
                builder.getHeight(),
                builder.getUuid(),
                builder.isOverturn(),
                builder.getOnInputStream(),
                builder.getBitmapConfig());
    }

    private Bitmap createBitmap(CakeBitmap cakeBitmap, int width, int height, int uuid, boolean overturn, OnInputStream onInputStream, Bitmap.Config bitmapConfig) {
        boolean reuseSuccess = true;

        BitmapFactory.Options options = Tools.getOptions(width, height, overturn, onInputStream.getInputStream(), bitmapConfig);
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

        if (reuseSuccess) {
            cakeBitmap.bitmap = result;
            reuseManager.put(cakeBitmap, uuid, true);
        } else {
            CakeBitmap newCake = new CakeBitmap(uuid);
            newCake.bitmap = result;
            reuseManager.put(newCake, uuid, false);
        }

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


    public static class Builder {

        private OnInputStream onInputStream;
        private int height;
        private int width;
        private int uuid;
        private boolean overturn;
        private boolean needAsync;
        private Bitmap.Config bitmapConfig = Bitmap.Config.RGB_565;
        private Map<Object, Object> metadata;

        public Builder(ImageView imageView) {
            uuid = imageView.hashCode();
            height = imageView.getHeight();
            width = imageView.getWidth();
        }

        public Builder(int height, int width, int uuid) {
            this.height = height;
            this.width = width;
            this.uuid = uuid;
        }

        public Builder addSource(final Context context, final int drawableId) {
            onInputStream = new OnInputStream() {
                @Override
                public InputStream getInputStream() {
                    return context.getResources().openRawResource(drawableId);
                }
            };
            return this;
        }

        public Builder addSource(final String path) {
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

        public Builder setUuid(int uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            this.bitmapConfig = bitmapConfig;
            return this;
        }

        public Builder needOverturn(boolean overturn) {
            this.overturn = overturn;
            return this;
        }

        public Builder needAsync(boolean needAsync) {
            this.needAsync = needAsync;
            return this;
        }

        public Builder putMetadata(Object key, Object value) {
            if (metadata == null) {
                metadata = new LinkedHashMap<>();
            }
            metadata.put(key, value);
            return this;
        }

        Map<Object, Object> getMetadata() {
            return metadata;
        }

        OnInputStream getOnInputStream() {
            return onInputStream;
        }

        int getHeight() {
            return height;
        }

        int getWidth() {
            return width;
        }

        int getUuid() {
            return uuid;
        }

        boolean isOverturn() {
            return overturn;
        }

        boolean isNeedAsync() {
            return needAsync;
        }

        Bitmap.Config getBitmapConfig() {
            return bitmapConfig;
        }

        protected Bitmap createBitmap(RecycleBitmap recycleBitmap) {
            if (onInputStream == null || onInputStream.getInputStream() == null) {
                Log.d(TAG, "no source or invalid source");
                return null;
            }
            return recycleBitmap.createBitmap(this);
        }
    }
}
