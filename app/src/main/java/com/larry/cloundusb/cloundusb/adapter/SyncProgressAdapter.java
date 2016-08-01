package com.larry.cloundusb.cloundusb.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.List;

import ch.halcyon.squareprogressbar.SquareProgressBar;
import ch.halcyon.squareprogressbar.utils.PercentStyle;

/**
 * Created by LARRYSEA on 2016/7/19.
 *
 * 同步照片的adapter
 *
 *
 */
public class SyncProgressAdapter extends RecyclerView.Adapter<SyncProgressAdapter.viewHolder> {

    List<String> syncPictureList;                      //待同步照片
    Context mcontext;
    Bitmap contentBitmap;                              //内容btimap
    List<Bitmap> tempbitmapList;                     //暂时性的bitmaplist

    public SyncProgressAdapter(List<String> syncPictureList,List<Bitmap> listBitmap,Context context) {
        this.syncPictureList = syncPictureList;
        this.tempbitmapList=listBitmap;
        mcontext = context;
    }

    @Override
    public SyncProgressAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(mcontext).inflate(R.layout.sync_picture_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SyncProgressAdapter.viewHolder holder, int position) {

        holder.squareProgressBar.showProgress(true);
        holder.squareProgressBar.setClearOnHundred(true);

        holder.squareProgressBar.drawOutline(true);
        PercentStyle percentStyle = new PercentStyle(Paint.Align.CENTER, 150, true);
        percentStyle.setTextColor(Color.parseColor("#000000"));
        percentStyle.setTextSize(40);
        holder.squareProgressBar.setPercentStyle(percentStyle);
        holder.squareProgressBar.setImageGrayscale(true);
        holder.squareProgressBar.setWidth(5);

        if (!syncPictureList.get(position).equals(holder.squareProgressBar.getTag())) {
            holder.squareProgressBar.setTag(syncPictureList.get(position));
            holder.squareProgressBar.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            if(tempbitmapList.size()>0)
            holder.squareProgressBar.setImageBitmap(tempbitmapList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return syncPictureList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }



   static public class viewHolder extends RecyclerView.ViewHolder {

        public   SquareProgressBar squareProgressBar;
        public viewHolder(View itemView) {
            super(itemView);
            squareProgressBar = (SquareProgressBar) itemView.findViewById(R.id.sync_picture_squre__item_pb);
        }
    }



}
