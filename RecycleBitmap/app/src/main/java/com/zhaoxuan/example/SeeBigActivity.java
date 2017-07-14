package com.zhaoxuan.example;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cake.recyclebitmap.RecycleBitmap;

import java.io.IOException;

public class SeeBigActivity extends AppCompatActivity {

    private ImageView imageView;

    private RecycleBitmap recycleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_big);
        recycleBitmap = RecycleBitmap.newInstance(RecycleBitmap.REUSE_NO_CACHE_STRATEGY);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.GONE);
            }
        });

        GridView gv = (GridView) findViewById(R.id.gridView);
        //为GridView设置适配器
        gv.setAdapter(new MyAdapter(this));
        //注册监听事件
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                imageView.setImageBitmap(recycleBitmap.createBitmap(imageView, getDatas(position)));
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private byte[] getDatas(final int index) {
        return Tools.readFile(this, "small/" + (index + 1) + ".png");
    }

    class MyAdapter extends BaseAdapter {
        private Context context;

        MyAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return 9;
        }

        public Object getItem(int item) {
            return item;
        }

        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView;
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 300));
            imageView.setAdjustViewBounds(false);
            imageView.setPadding(8, 8, 8, 8);//设置间距
            try {
                imageView.setImageBitmap(BitmapFactory.decodeStream(
                        getResources().getAssets().open("small/" + (i + 1) + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return imageView;
        }
    }
}
