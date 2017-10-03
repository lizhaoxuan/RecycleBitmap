package com.zhaoxuan.example;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cake.recyclebitmap.MetaData;
import com.cake.recyclebitmap.OnInputStream;
import com.cake.recyclebitmap.RecycleBitmap;

import java.io.IOException;
import java.io.InputStream;

public class ViewPagerActivity extends AppCompatActivity {
    private static final String TAG = ViewPagerActivity.class.getSimpleName();
    private RecycleBitmap recycleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyAdapter());
        recycleBitmap = RecycleBitmap.newInstance(RecycleBitmap.STRATEGY_ONCE_CACHE);
    }

    class MyAdapter extends PagerAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            recycleBitmap.recycle(position % 3);
            Log.d(TAG, "destroyItem:" + position + "   " + (position % 3));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem:" + position + "   " + (position % 3));
            ImageView imageView = new ImageView(ViewPagerActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            if (getIntent().getBooleanExtra("RecycleBitmap", false)) {
                imageView.setImageBitmap(recycleBitmap.createBitmap(
                        new MetaData(imageView)
                                .setSource(getInputStream(position))
                                .setUuid(position % 3)));
            } else {
                imageView.setImageBitmap(BitmapFactory.decodeStream(getInputStream(position).getInputStream()));
            }

            imageView.setOnClickListener(this);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(ViewPagerActivity.this, "点啥？？左右滑啊！", Toast.LENGTH_SHORT).show();
        }

        private OnInputStream getInputStream(final int index) {
            return new OnInputStream() {
                @Override
                public InputStream getInputStream() {
                    try {
                        return getResources().getAssets().open("small/" + (index + 1) + ".png");
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }
    }
}
