package com.larry.cloundusb.cloundusb.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.FileHistoryInfo;
import com.larry.cloundusb.cloundusb.baseclass.User;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

import java.util.List;

/**
 * Created by LARRYSEA on 2016/6/23.
 * 发送历史适配器信息
 */
public class SendHistoryAdapter extends RecyclerView.Adapter<SendHistoryAdapter.viewHolder> {


    List<FileHistoryInfo> sendHistoryList;    //发送历史记录链表

    clickListener mclickListener;             //监听器
    Bitmap portraitBitmap;                    //头像bitmap


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewHolder viewHolder = new viewHolder(LayoutInflater.from(GetContextUtil.getInstance()).inflate(R.layout.send_history_item, null, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        holder.fileNameTextView.setText(sendHistoryList.get(position).getTFFileName());
        holder.nameTextView.setText(sendHistoryList.get(position).getTFUserName());
        holder.fileNameTextView.setText(sendHistoryList.get(position).getTFFileName());
        holder.sizeTextView.setText(sendHistoryList.get(position).getTFFileSize());
        sendHistoryList.get(position).getTFFilePath();
        holder.fileThumb.setImageBitmap(FileUtil.getFilethumb(sendHistoryList.get(position).getTFFilePath(), GetContextUtil.getInstance()));
        portraitBitmap = User.getPortraitBitmap(sendHistoryList.get(position).getTFUserPicId());
        if (portraitBitmap != null) {
            portraitBitmap = GraphicsUtil.toRoundBitmap(portraitBitmap);
            holder.portraitImageView.setImageBitmap(portraitBitmap);
        }
        holder.timeTextView.setText(sendHistoryList.get(position).getTFTime());
        holder.openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mclickListener.onClicK(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sendHistoryList == null ? 0 : sendHistoryList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView portraitImageView;
        TextView nameTextView;
        TextView sizeTextView;
        TextView timeTextView;
        Button openButton;
        ImageView fileThumb;
        TextView fileNameTextView;

        public viewHolder(View v) {
            super(v);
            portraitImageView = (ImageView) v.findViewById(R.id.send_history_sender_portrait_iv);
            nameTextView = (TextView) v.findViewById(R.id.send_history_sender_name_tv);
            sizeTextView = (TextView) v.findViewById(R.id.send_history_file_size_tv);
            timeTextView = (TextView) v.findViewById(R.id.send_history_time_tv);
            openButton = (Button) v.findViewById(R.id.send_history_open_button);
            fileThumb = (ImageView) v.findViewById(R.id.send_history_file_portrait_imageview);
            fileNameTextView = (TextView) v.findViewById(R.id.send_history_file_name_tv);


        }

    }

    /*
    * 构造函数
    * */
    public SendHistoryAdapter(List<FileHistoryInfo> fileHistoryInfos) {
        sendHistoryList = fileHistoryInfos;
    }


    /*
    *
    *
    *
    * 实现监听接口
    * */
    public void setOnClickListener(clickListener clickListener) {
        mclickListener = clickListener;


    }


    public interface clickListener {
        void onClicK(View view, int position);    //点击方法
    }


}
