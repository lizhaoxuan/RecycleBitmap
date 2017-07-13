package com.cake.recyclebitmap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by lizhaoxuan on 2017/7/12.
 */

class CreateBitmapTask extends AsyncTask<RecycleBitmap.MetaData, Integer, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;
    private WeakReference<RecycleBitmap> recycleBitmapWeakReference;

    public CreateBitmapTask(ImageView view, RecycleBitmap recycleBitmap) {
        this.imageViewWeakReference = new WeakReference<>(view);
        this.recycleBitmapWeakReference = new WeakReference<>(recycleBitmap);
    }

    @Override
    protected Bitmap doInBackground(RecycleBitmap.MetaData... params) {
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