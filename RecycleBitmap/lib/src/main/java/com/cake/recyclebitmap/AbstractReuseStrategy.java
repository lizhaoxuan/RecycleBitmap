package com.cake.recyclebitmap;

import android.graphics.Bitmap;
import android.os.Build;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lizhaoxuan on 2017/7/12.
 */

public abstract class AbstractReuseStrategy<T extends CakeBitmap> {

    private Map<Integer, T> cakeMap;

    protected abstract T OnSelector(MetaData metaData);

    protected abstract void put(Bitmap result, T cakeBitmap, int uuid, boolean reuseSuccess);

    protected abstract void recycle(int uuid);

    protected void destroy() {
        for (T cake : getCakeMap().values()) {
            if (cake != null) {
                cake.bitmap = null;
            }
        }
        getCakeMap().clear();
    }

    protected Map<Integer, T> getCakeMap() {
        if (cakeMap == null) {
            cakeMap = new LinkedHashMap<>();
        }
        return cakeMap;
    }


    protected boolean canReuse(CakeBitmap cakeBitmap, MetaData metaData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return cakeBitmap.getHeight() >= metaData.getRealHeight() && cakeBitmap.getWidth() >= metaData.getRealWidth();
        } else {
            return cakeBitmap.getHeight() == metaData.getRealHeight() && cakeBitmap.getWidth() == metaData.getRealWidth();
        }
    }

}
