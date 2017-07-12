package com.cake.recyclebitmap;

/**
 * Created by lizhaoxuan on 2017/7/12.
 */

public class DefaultReuseManager extends AbstractReuseManager<CakeBitmap> {
    private static final int RECYCLE_BITMAP_KEY = -1;

    @Override
    CakeBitmap OnSelector(MetaData metaData) {
        CakeBitmap cakeBitmap = getCakeMap().get(metaData.getUuid());
        if (cakeBitmap == null) {
            //尝试利用最近废弃的一个CakeBitmap
            cakeBitmap = getCakeMap().get(RECYCLE_BITMAP_KEY);
            if (cakeBitmap == null) {
                cakeBitmap = new CakeBitmap(metaData.getUuid());
            }
        }
        return cakeBitmap;
    }

    @Override
    void put(CakeBitmap cakeBitmap, int uuid, boolean reuseSuccess) {
        //如果复用失败，将直接替换原有uuid的缓存
        getCakeMap().put(uuid, cakeBitmap);
        if (reuseSuccess) {
            if (cakeBitmap.getKey() != uuid) {
                //uuid不同，说明利用了以废弃的一个cakeBitmap
                getCakeMap().put(RECYCLE_BITMAP_KEY, null);
            }
        }
    }


    @Override
    public void recycle(Object uuid) {
        CakeBitmap cakeBitmap = getCakeMap().get(uuid);
        cakeBitmap.setKey(RECYCLE_BITMAP_KEY);

        getCakeMap().put(RECYCLE_BITMAP_KEY, cakeBitmap);
        getCakeMap().put((int) uuid, null);
    }

}
