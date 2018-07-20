package com.zhaoxuan.example;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class SeeSingleBigActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView bigImg;
    private RecycleBitmapUtils recycleBitmapUtils;

    private boolean isRecycleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_single_big);
        isRecycleBitmap = getIntent().getBooleanExtra("RecycleBitmap", false);


        textView = (TextView) findViewById(R.id.text);
        bigImg = (ImageView) findViewById(R.id.bigImg);
        recycleBitmapUtils = RecycleBitmapUtils.newInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bigImg.setVisibility(View.VISIBLE);
                if (isRecycleBitmap) {
                    bigImg.setImageBitmap(recycleBitmapUtils.createBitmap(new RecycleBitmapUtils.Builder(bigImg)
                            .addSource(new RecycleBitmapUtils.OnInputStream() {
                                @Override
                                public InputStream getInputStream() {
                                    return Tools.readFileInputStream(SeeSingleBigActivity.this, "small/" + 1 + ".png");
                                }
                            })));
                } else {
                    byte[] data = getDatas(1);
                    bigImg.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                }

            }
        });

        bigImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bigImg.setVisibility(View.GONE);
            }
        });
    }

    private byte[] getDatas(final int index) {
        return Tools.readFile(this, "small/" + (index + 1) + ".png");
    }
}
