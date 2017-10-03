package com.cake.recyclebitmap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * 异步创建BitmapTask
 * Activity刚启动时createBitmap，弱View未创建完成，无法获取长宽，无法根据View大小对图片进行压缩
 * 此时需要借助异步Task创建
 * Created by lizhaoxuan on 2017/7/12.
 */
class CreateBitmapTask extends AsyncTask<MetaData, Integer, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;
    private WeakReference<RecycleBitmap> recycleBitmapWeakReference;

    public CreateBitmapTask(ImageView view, RecycleBitmap recycleBitmap) {
        this.imageViewWeakReference = new WeakReference<>(view);
        this.recycleBitmapWeakReference = new WeakReference<>(recycleBitmap);
    }

    @Override
    protected Bitmap doInBackground(MetaData... params) {
        RecycleBitmap recycleBitmap = recycleBitmapWeakReference.get();
        if (recycleBitmap != null && params.length > 0 && params[0] != null) {
            return recycleBitmap.createBitmap(params[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewWeakReference.get() != null && bitmap != null) {
            imageViewWeakReference.get().setImageBitmap(bitmap);
        }
    }
}