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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takePhotoBtn:
                startActivity(TakePhotoActivity.class);
                break;
            case R.id.seeBigBtn:
                startActivity(SeeBigActivity.class);
                break;
            case R.id.viewPagerBtn:
                startActivity(ViewPagerActivity.class);
                break;
        }
    }

    private void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
