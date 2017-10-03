package com.cake.recyclebitmap;

import android.graphics.Bitmap;

import java.util.LinkedList;

/**
 * 可以自定义缓存个数的的策略，默认两个
 * 回收时，如果当前缓存未满，加入缓存队列，如果缓存已满，踢出队尾缓存，加入缓存队列
 * 创建时，会遍历所有缓存队列寻找可符合复用条件的缓存进行复用
 * Created by lizhaoxuan on 2017/7/13.
 */
public class StrategyCustomerCache extends AbstractReuseStrategy<CakeBitmap> {

    private int RECYCLE_BITMAP_KEY = this.hashCode();
    private int cacheNum = 2;
    private LinkedList<CakeBitmap> cacheQueue;

    public StrategyCustomerCache(int cacheNum) {
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
            if (cakeBitmap.getUuid() != uuid) {
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
        cakeBitmap.setUuid(RECYCLE_BITMAP_KEY);
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
