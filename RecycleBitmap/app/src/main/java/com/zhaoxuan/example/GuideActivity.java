package com.zhaoxuan.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.cake.recyclebitmap.RLog;
import com.cake.recyclebitmap.RecycleBitmap;

import java.io.InputStream;

public class GuideActivity extends AppCompatActivity {

    RecycleBitmap recycleBitmap;
    RecycleBitmapUtils recycleBitmapUtils;
    int[] resIds = new int[]{R.drawable.order_tutorial_1, R.drawable.order_tutorial_2, R.drawable.order_tutorial_3};
    int index;
    private boolean isRecycleBitmap;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        recycleBitmap = RecycleBitmap.newInstance();
        recycleBitmapUtils = RecycleBitmapUtils.newInstance();
        RLog.setLogLevel(RLog.LEVEL_DEBUG);
        final ImageView imageView = (ImageView) findViewById(R.id.image);

        bitmap = BitmapFactory.decodeStream(getInputStream(resIds[index++]), null, getOptions());

        isRecycleBitmap = getIntent().getBooleanExtra("RecycleBitmap", false);
        if (isRecycleBitmap) {
            setTitle("使用RecycleBitmap引导");
//            recycleBitmap.setImageForViewOnPost(imageView, R.drawable.order_tutorial_1);
            imageView.setImageBitmap(bitmap);
//            recycleBitmapUtils.setImageForViewOnPost(imageView,
//                    new RecycleBitmapUtils.Builder(imageView)
//                            .addSource(this, R.drawable.order_tutorial_1));
        } else {
            setTitle("普通引导");
            imageView.setImageBitmap(BitmapFactory.decodeStream(getInputStream(resIds[index++]), null, getOptions()));
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index >= 3) {
                    index = 0;
                }
                if (isRecycleBitmap) {
//                    imageView.setImageBitmap(recycleBitmap.createBitmap(imageView, resIds[index++]));
//                    imageView.setImageBitmap(recycleBitmapUtils.createBitmap(
//                            new RecycleBitmapUtils.Builder(imageView)
//                                    .addSource(GuideActivity.this, resIds[index++])));

                    BitmapFactory.Options options = getOptions();
                    options.inBitmap = bitmap;
                    bitmap = BitmapFactory.decodeStream(getInputStream(resIds[index++]), null, options);
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageBitmap(BitmapFactory.decodeStream(getInputStream(resIds[index++]), null, getOptions()));
                }
            }
        });
    }


    private BitmapFactory.Options getOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inMutable = true;
        options.inSampleSize = 1;
        return options;
    }

    private InputStream getInputStream(int resId) {
        return this.getResources().openRawResource(resId);
    }
}
