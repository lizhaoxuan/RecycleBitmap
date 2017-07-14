package com.cake.recyclebitmap;

import android.graphics.Bitmap;

/**
 * Created by lizhaoxuan on 2017/7/12.
 */

public class ReuseNoCacheStrategy extends AbstractReuseStrategy<CakeBitmap> {

    @Override
    protected CakeBitmap OnSelector(MetaData metaData) {
        CakeBitmap cakeBitmap = getCakeMap().get(metaData.getUuid());
        if (cakeBitmap == null) {
            cakeBitmap = new CakeBitmap(metaData);
        }
        return cakeBitmap;
    }

    @Override
    protected void put(Bitmap result, CakeBitmap cakeBitmap, int uuid, boolean reuseSuccess) {
        cakeBitmap.setBitmap(result);
        getCakeMap().put(uuid, cakeBitmap);
    }

    @Override
    protected void recycle(int uuid) {
        getCakeMap().put(uuid, null);
    }
}
