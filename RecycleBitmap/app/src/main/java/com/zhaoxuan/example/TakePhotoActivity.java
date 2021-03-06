package com.zhaoxuan.example;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cake.recyclebitmap.RecycleBitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TakePhotoActivity extends AppCompatActivity {
    private ImageView photoImg;
    private Button takeBtn;

    RecycleBitmap recycleBitmap;

    private Uri fileUri;

    private boolean isRecycleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        isRecycleBitmap = getIntent().getBooleanExtra("RecycleBitmap", false);
        if (isRecycleBitmap) {
            setTitle("使用RecycleBitmap拍照");
        } else {
            setTitle("普通拍照");
        }
        recycleBitmap = RecycleBitmap.newInstanceCustomerCache(1);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path, "oldTakePhoto.jpg");
        fileUri = Uri.fromFile(outputImage);
        photoImg = (ImageView) findViewById(R.id.photo_img);
        takeBtn = (Button) findViewById(R.id.take_btn);

        if (isRecycleBitmap) {
            recycleBitmap.setImageForViewOnPost(photoImg, R.drawable.example);
        } else {
            photoImg.setImageResource(R.drawable.example);
        }

        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(captureIntent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        if (isRecycleBitmap) {
            photoImg.setImageBitmap(recycleBitmap.createBitmap(photoImg, fileUri.getPath()));
        } else {
            try {
                photoImg.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(fileUri.getPath())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
