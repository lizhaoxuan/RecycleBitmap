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

import java.io.IOException;
import java.io.InputStream;

public class SeeBigActivity extends AppCompatActivity {

    private ImageView imageView;

    //    private RecycleBitmap recycleBitmap;
    private RecycleBitmapUtils recycleBitmapUtils;

    private boolean isRecycleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_big);
        isRecycleBitmap = getIntent().getBooleanExtra("RecycleBitmap", false);
        if (isRecycleBitmap) {
            setTitle("使用RecycleBitmap查看大图");
        } else {
            setTitle("普通查看大图");
        }
//        recycleBitmap = RecycleBitmap.newInstance(RecycleBitmap.STRATEGY_NO_CACHE);
        recycleBitmapUtils = RecycleBitmapUtils.newInstance();

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
                setImageView(position);
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setImageView(final int position) {
        if (isRecycleBitmap) {
            imageView.setImageBitmap(recycleBitmapUtils.createBitmap(new RecycleBitmapUtils.Builder(imageView)
                    .addSource(new RecycleBitmapUtils.OnInputStream() {
                        @Override
                        public InputStream getInputStream() {
                            return Tools.readFileInputStream(SeeBigActivity.this, "small/" + (position + 1) + ".png");
                        }
                    })));
        } else {
            byte[] data = getDatas(position);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
        }
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
            return 1;
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
