package com.larry.cloundusb.cloundusb.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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
 * Created by LARRYSEA on 2016/6/24.
 */
public class ReceiveHistoryAdapter extends RecyclerView.Adapter<ReceiveHistoryAdapter.viewHolder> {


    List<FileHistoryInfo> fileHistoryInfos;//接收文件清单
    clickListener mclickListener;
    Bitmap portraitBitmap;//头像bitmap

    @Override
    public ReceiveHistoryAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(GetContextUtil.getInstance()).inflate(R.layout.receive_history_item, null, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        FileHistoryInfo fileHistoryInfo = fileHistoryInfos.get(position);
        portraitBitmap=User.getPortraitBitmap(fileHistoryInfos.get(position).getTFUserPicId());
        if(portraitBitmap!=null)
            portraitBitmap=GraphicsUtil.toRoundBitmap(portraitBitmap);
        holder.portraitImageView.setImageBitmap(portraitBitmap);
        holder.fileNameTextView.setText(fileHistoryInfo.getTFFileName());
        holder.fileThumb.setImageBitmap(FileUtil.getFilethumb(fileHistoryInfos.get(position).getTFFilePath(),GetContextUtil.getInstance()));
        holder.nameTextView.setText(fileHistoryInfo.getTFUserName());
        holder.sizeTextView.setText(fileHistoryInfo.getTFFileSize());
        holder.timeTextView.setText(fileHistoryInfo.getTFTime());
        holder.openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mclickListener.onClick(v,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileHistoryInfos==null?0:fileHistoryInfos.size();
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
            portraitImageView = (ImageView) v.findViewById(R.id.receive_history_portrait_iv);
            nameTextView = (TextView) v.findViewById(R.id.receive_history_sender_name_tv);
            sizeTextView = (TextView) v.findViewById(R.id.receive_history_file_size_tv);
            timeTextView = (TextView) v.findViewById(R.id.receive_history_time_tv);
            openButton = (Button) v.findViewById(R.id.receive_history_open_button);
            fileThumb = (ImageView) v.findViewById(R.id.receive_history_file_thumb);
            fileNameTextView = (TextView) v.findViewById(R.id.receive_history_file_name_tv);


        }

    }

    /*
    * 构造函数
    * */
    public ReceiveHistoryAdapter(List<FileHistoryInfo> fileHistoryInfos) {
        this.fileHistoryInfos = fileHistoryInfos;
    }

    public   interface clickListener
    {
        void onClick(View view,int position);

    }


    public void setClickListener(clickListener clickListener)
    {
     mclickListener=clickListener;
    }


}
