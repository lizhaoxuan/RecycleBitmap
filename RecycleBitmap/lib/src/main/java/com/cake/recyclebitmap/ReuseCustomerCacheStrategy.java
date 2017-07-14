package com.cake.recyclebitmap;

import android.graphics.Bitmap;

import java.util.LinkedList;

/**
 * Created by lizhaoxuan on 2017/7/13.
 */

public class ReuseCustomerCacheStrategy extends AbstractReuseStrategy<CakeBitmap> {

    private int RECYCLE_BITMAP_KEY = this.hashCode();
    private int cacheNum = 2;
    private LinkedList<CakeBitmap> cacheQueue;

    public ReuseCustomerCacheStrategy(int cacheNum) {
        this.cacheNum = cacheNum;
    }

    @Override
    protected CakeBitmap OnSelector(MetaData metaData) {
        CakeBitmap cakeBitmap = getCakeMap().get(metaData.getUuid());
        if (cakeBitmap == null) {
            //尝试利用最近废弃的一个CakeBitmap
            cakeBitmap = checkCacheQueue(metaData);
            if (cakeBitmap == null) {
                cakeBitmap = new CakeBitmap(metaData);
            }
        }
        return cakeBitmap;
    }

    private CakeBitmap checkCacheQueue(MetaData metaData) {
        if (getQueue().size() == 0) {
            return null;
        }
        for (CakeBitmap cakeBitmap : getQueue()) {
            if (canReuse(cakeBitmap, metaData)) {
                return cakeBitmap;
            }
        }
        return null;
    }

    @Override
    protected void put(Bitmap result, CakeBitmap cakeBitmap, int uuid, boolean reuseSuccess) {
        if (reuseSuccess) {
            cakeBitmap.setBitmap(result);
            if (cakeBitmap.getKey() != uuid) {
                //uuid不同，说明利用了以废弃的一个cakeBitmap,此时将其踢出Map
                getQueue().remove(cakeBitmap);
            }
            getCakeMap().put(uuid, cakeBitmap);
        } else {
            //如果复用失败，将直接新建且替换
            CakeBitmap newCake = new CakeBitmap(result, uuid);
            getCakeMap().put(uuid, newCake);
        }
    }

    @Override
    protected void recycle(int uuid) {
        CakeBitmap cakeBitmap = getCakeMap().get(uuid);
        if (cakeBitmap == null) {
            return;
        }

        if (getQueue().size() >= cacheNum) {
            getQueue().poll();
        }
        cakeBitmap.setKey(RECYCLE_BITMAP_KEY);
        getQueue().offer(cakeBitmap);
        getCakeMap().put(uuid, null);
    }

    private LinkedList<CakeBitmap> getQueue() {
        if (cacheQueue == null) {
            cacheQueue = new LinkedList<>();
        }
        return cacheQueue;
    }
}
