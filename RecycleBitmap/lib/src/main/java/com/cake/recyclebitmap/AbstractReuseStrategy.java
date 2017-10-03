package com.cake.recyclebitmap;

import android.graphics.Bitmap;
import android.os.Build;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 缓存复用策略抽象类
 * Created by lizhaoxuan on 2017/7/12.
 */
public abstract class AbstractReuseStrategy<T extends CakeBitmap> {

    /**
     * 缓存池
     * 包含正在使用的和已经废弃但缓存起来等待被复用的
     */
    private Map<Integer, T> cakeMap;

    /**
     * 根据将要使用的图片属性筛选出可用的缓存内存
     *
     * @param metaData 将要使用的图片属性
     * @return
     */
    protected abstract T OnSelector(MetaData metaData);

    /**
     * Bitmap创建成功后放回缓存池中，等待被回收或下次复用
     *
     * @param result       createBitmap 结果，可能为null
     * @param cakeBitmap
     * @param uuid
     * @param reuseSuccess 是否复用成功
     */
    protected abstract void put(Bitmap result, T cakeBitmap, int uuid, boolean reuseSuccess);

    /**
     * 将此uuid的对象标记为可复用对象
     *
     * @param uuid 缓存池的唯一标示id
     */
    protected abstract void recycle(int uuid);

    /**
     * 清空所有缓存，在你确定该场景结束时应该调用该方法
     */
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

    /**
     * 是否可以进行复用
     * SDK 19以前需要两张图片长宽一致才可以复用
     * SDK 19以后只需要被复用的图片长宽大于将要使用的图片即可
     *
     * @param cakeBitmap 将被复用的图片
     * @param metaData   将要使用的图片属性
     * @return true 可以复用 false 不可复用
     */
    protected boolean canReuse(CakeBitmap cakeBitmap, MetaData metaData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return cakeBitmap.getHeight() >= metaData.getRealHeight() && cakeBitmap.getWidth() >= metaData.getRealWidth();
        } else {
            return cakeBitmap.getHeight() == metaData.getRealHeight() && cakeBitmap.getWidth() == metaData.getRealWidth();
        }
    }

}
