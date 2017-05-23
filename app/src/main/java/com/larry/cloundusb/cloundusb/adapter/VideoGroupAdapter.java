package com.larry.cloundusb.cloundusb.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.baseclass.VideoInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fileutil.FileSizeUtil;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;
import com.larry.cloundusb.cloundusb.util.CommonUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Larry on 3/20/2016.
 */
public class VideoGroupAdapter extends RecyclerView.Adapter<VideoGroupAdapter.videoViewHolder> {

    List<VideoInform> list;
    Point point = new Point(0, 0);//用来设置图片的大小
    Context mContext;//
    clickListener mclickListener;
    LayoutInflater layoutInflater;

    public VideoGroupAdapter(Context context, List<VideoInform> list) {

        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        mContext = context;
    }

    @Override
    public videoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        videoViewHolder viewHolder = new videoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.video_item, null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final videoViewHolder holder, final int position) {

        VideoInform videoInform = list.get(position);
        holder.checkImageButton.setSelected(false);
        DateFormat formatparm = new SimpleDateFormat("mm:ss");
        holder.timeTextView.setText(CommonUtil.getTotalTime(list.get(position).getTotaltime()));
        holder.sizeTextView.setText(FileSizeUtil.convertFileSize((long) Integer.valueOf(videoInform.getFileSize())));
        holder.nametextview.setText(list.get(position).getFileName());
        holder.videoThumb.setImageBitmap(list.get(position).getThumb());
        if (mclickListener != null) {

            holder.checkImageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.e("点解了video fragment", "点击点解");
                    int position = holder.getPosition();
                    int deletePosition = FileBox.getInstance().getSendListSize();
                    if (b) {
                        holder.checkImageButton.setChecked(true);
                        holder.checkImageButton.setVisibility(View.VISIBLE);
                        SendFileInform sendFileInform = new SendFileInform();
                        sendFileInform.setName(list.get(holder.getAdapterPosition()).getFileName() + "." + list.get(holder.getAdapterPosition()).getType());
                        sendFileInform.setPath(list.get(holder.getAdapterPosition()).getAbsPath());
                        sendFileInform.setPortrait(list.get(holder.getAdapterPosition()).getThumb());
                        String path = list.get(holder.getAdapterPosition()).getAbsPath();
                        sendFileInform.setTime(100);
                        sendFileInform.setFilesize(Long.valueOf(list.get(position).getFileSize()));
                        sendFileInform.setPosition(deletePosition);
                        FileBox.getInstance().storageSendFileItem(sendFileInform);
                    } else {

                        holder.checkImageButton.setChecked(false);
                        FileBox.getInstance().cancelSendFile(list.get(position).getFileName());
                    }
                    ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int postion = holder.getPosition();
                    mclickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mclickListener != null)
                    mclickListener.onItemClick(holder.itemView, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();

    }

    public void setOnItemClickListener(clickListener listener) {
        this.mclickListener = listener;


    }


    public interface clickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }

    static class videoViewHolder extends RecyclerView.ViewHolder {
        TextView nametextview;      //显示姓名的
        CheckBox checkImageButton; //选中按钮
        TextView sizeTextView;     //显示计数按钮
        ImageView videoThumb;      //电影缩略图
        TextView timeTextView;   //显示时间textview


        clickListener listener;

        public videoViewHolder(View v) {
            super(v);
            checkImageButton = (CheckBox) v.findViewById(R.id.video_fragment_selected_button);
            videoThumb = (ImageView) v.findViewById(R.id.video_thumb_background);
            nametextview = (TextView) v.findViewById(R.id.video_fragment_name_textview);
            sizeTextView = (TextView) v.findViewById(R.id.video_fragment_size_textview);
            timeTextView = (TextView) v.findViewById(R.id.timeTextView);


        }


    }


}
