package com.cake.recyclebitmap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lizhaoxuan on 2017/7/12.
 */

public abstract class AbstractReuseManager<T extends CakeBitmap> {

    private Map<Integer, T> cakeMap;

    abstract T OnSelector(MetaData metaData);

    abstract void put(T cakeBitmap, int uuid, boolean reuseSuccess);

    abstract void recycle(Object uuid);

    void destroy() {
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
