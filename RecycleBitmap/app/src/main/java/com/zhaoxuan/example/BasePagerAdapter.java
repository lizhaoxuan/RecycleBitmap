package com.zhaoxuan.example;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * 提供一个可以复用Item的ViewPagerAdapter
 * 只有当Count大于 whenLargeThisNeedCache 是才会启动复用机制。
 * Created by lizhaoxuan on 2017/6/15.
 */
public abstract class BasePagerAdapter<T extends View> extends PagerAdapter {

    private LinkedList<T> cacheView;

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    protected abstract T getView(T cacheView, int position);

    protected int whenLargeThisNeedCache() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T convertView;
        if (getCount() > whenLargeThisNeedCache()) {
            if (getCacheView().size() == 0) {
                convertView = getView(null, position);
            } else {
                convertView = getCacheView().removeFirst();
                convertView = getView(convertView, position);
            }
        } else {
            convertView = getView(null, position);
        }
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        if (getCount() > whenLargeThisNeedCache()) {
            getCacheView().add((T) object);
        }
    }

    private LinkedList<T> getCacheView() {
        if (cacheView == null) {
            cacheView = new LinkedList<>();
        }
        return cacheView;
    }
}
