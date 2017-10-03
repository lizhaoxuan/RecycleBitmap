package com.cake.recyclebitmap;

import android.graphics.Bitmap;

/**
 * 无缓存策略
 * 只能复用当前正在使用的图片内存
 * Created by lizhaoxuan on 2017/7/12.
 */
public class StrategyNoCache extends AbstractReuseStrategy<CakeBitmap> {

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
