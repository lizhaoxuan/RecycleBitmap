package com.cake.recyclebitmap;

import android.graphics.Bitmap;

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

}
