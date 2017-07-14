package com.cake.recyclebitmap;

import android.graphics.Bitmap;

/**
 * Created by lizhaoxuan on 2017/7/12.
 */
public class ReuseOnceCacheStrategy extends AbstractReuseStrategy<CakeBitmap> {
    private final int RECYCLE_BITMAP_KEY = this.hashCode();

    @Override
    public CakeBitmap OnSelector(MetaData metaData) {
        CakeBitmap cakeBitmap = getCakeMap().get(metaData.getUuid());
        if (cakeBitmap == null) {
            //尝试利用最近废弃的一个CakeBitmap
            cakeBitmap = getCakeMap().get(RECYCLE_BITMAP_KEY);
            if (cakeBitmap == null) {
                cakeBitmap = new CakeBitmap(metaData);
            }
        }
        return cakeBitmap;
    }

    @Override
    public void put(Bitmap result, CakeBitmap cakeBitmap, int uuid, boolean reuseSuccess) {
        if (reuseSuccess) {
            cakeBitmap.setBitmap(result);
            getCakeMap().put(uuid, cakeBitmap);
            if (cakeBitmap.getKey() != uuid) {
                //uuid不同，说明利用了以废弃的一个cakeBitmap,此时将其踢出Map
                getCakeMap().put(RECYCLE_BITMAP_KEY, null);
            }
        } else {
            //如果复用失败，将直接更新uuid的缓存
            CakeBitmap newCake = new CakeBitmap(result, uuid);
            getCakeMap().put(uuid, newCake);
        }
    }


    @Override
    public void recycle(int uuid) {
        CakeBitmap cakeBitmap = getCakeMap().get(uuid);

        if (cakeBitmap == null) {
            return;
        }
        cakeBitmap.setKey(RECYCLE_BITMAP_KEY);

        getCakeMap().put(RECYCLE_BITMAP_KEY, cakeBitmap);
        getCakeMap().put(uuid, null);
    }

}
