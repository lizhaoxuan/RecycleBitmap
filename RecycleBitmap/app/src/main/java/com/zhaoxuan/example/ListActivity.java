package com.zhaoxuan.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView myRecycleView;
    private boolean isRecycleBitmap;
    private ListAdapter listAdapter;
    private List<ItemValue> myDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        myRecycleView = (RecyclerView) findViewById(R.id.recycle_view);

        isRecycleBitmap = getIntent().getBooleanExtra("RecycleBitmap", false);
        if (isRecycleBitmap) {
            setTitle("使用RecycleBitmap ListView");
        } else {
            setTitle("普通ListView");
        }

        myDatas = initData();
        listAdapter = new ListAdapter(this, myDatas, isRecycleBitmap);
        myRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myRecycleView.setAdapter(listAdapter);
    }

    private List<ItemValue> initData() {
        List<ItemValue> datas = new LinkedList<>();
        for (int i = 1; i < 32 * 2; i++) {
            if (i > 32) {
                datas.add(new ItemValue(i - 32));
            } else {
                datas.add(new ItemValue(i));
            }
        }

        return datas;
    }


    public static class ItemValue {
        private int id;

        public ItemValue(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getPath() {
            return "small/" + id + ".png";
        }
    }
}
