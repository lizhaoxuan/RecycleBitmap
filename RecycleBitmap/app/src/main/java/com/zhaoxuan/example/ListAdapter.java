package com.zhaoxuan.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cake.recyclebitmap.RecycleBitmap;

import java.util.List;

/**
 * Created by lizhaoxuan on 2017/11/9.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private Context context;
    private List<ListActivity.ItemValue> myDatas;
    private RecycleBitmap recycleBitmap;
    private boolean isRecycleBitmap;
    private SparseArray<Bitmap> bitmapHashMap = new SparseArray<>();

    public ListAdapter(Context context, List<ListActivity.ItemValue> myDatas, boolean isRecycleBitmap) {
        this.context = context;
        this.myDatas = myDatas;
        this.isRecycleBitmap = isRecycleBitmap;
        recycleBitmap = RecycleBitmap.newInstance(10);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_recycle_view, parent,
                false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListActivity.ItemValue item = myDatas.get(position);
        Bitmap bitmap;
        long start = System.currentTimeMillis();
        if (isRecycleBitmap) {
            bitmap = recycleBitmap.createBitmap(holder.iconImg, Tools.readFile(context, item.getPath()));
        } else {
            bitmap = bitmapHashMap.get(item.getId());
            if (bitmap == null) {
                byte[] data = Tools.readFile(context, item.getPath());
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                bitmapHashMap.put(item.getId(), bitmap);
            }
        }
        long end = System.currentTimeMillis();
        holder.iconImg.setImageBitmap(bitmap);
        holder.timeText.setText(item.getPath() + "  time:" + (end - start) + "ms");
    }

    @Override
    public int getItemCount() {
        return myDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        protected ImageView iconImg;
        protected TextView timeText;

        public MyViewHolder(View view) {
            super(view);
            iconImg = (ImageView) view.findViewById(R.id.icon);
            timeText = (TextView) view.findViewById(R.id.time);
        }

    }
}
