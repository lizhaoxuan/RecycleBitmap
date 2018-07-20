package com.zhaoxuan.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.takePhotoBtn).setOnClickListener(this);
        findViewById(R.id.seeBigBtn).setOnClickListener(this);
        findViewById(R.id.viewPagerBtn).setOnClickListener(this);
        findViewById(R.id.takePhotoRecycleBtn).setOnClickListener(this);
        findViewById(R.id.seeBigRecycleBtn).setOnClickListener(this);
        findViewById(R.id.viewPagerRecycleBtn).setOnClickListener(this);
        findViewById(R.id.listBtn).setOnClickListener(this);
        findViewById(R.id.listRecycleBtn).setOnClickListener(this);
        findViewById(R.id.guideBtn).setOnClickListener(this);
        findViewById(R.id.guideRecycleBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takePhotoBtn:
                startActivity(TakePhotoActivity.class, false);
                break;
            case R.id.seeBigBtn:
                startActivity(SeeSingleBigActivity.class, false);
                break;
            case R.id.viewPagerBtn:
                startActivity(ViewPagerActivity.class, false);
                break;
            case R.id.listBtn:
                startActivity(ListActivity.class, false);
                break;
            case R.id.guideBtn:
                startActivity(GuideActivity.class, false);
                break;
            case R.id.takePhotoRecycleBtn:
                startActivity(TakePhotoActivity.class, true);
                break;
            case R.id.seeBigRecycleBtn:
                startActivity(SeeSingleBigActivity.class, true);
                break;
            case R.id.viewPagerRecycleBtn:
                startActivity(ViewPagerActivity.class, true);
                break;
            case R.id.listRecycleBtn:
                startActivity(ListActivity.class, true);
                break;
            case R.id.guideRecycleBtn:
                startActivity(GuideActivity.class, true);
                break;
        }
    }

    private void startActivity(Class clazz, boolean recycle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("RecycleBitmap", recycle);
        startActivity(intent);
    }
}
