package com.larry.cloundusb.cloundusb.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.appinterface.recyclerviewClickListener;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Larry on 5/1/2016.
 * 发送文件进度的adapter
 */
public class SendProgressAdapter extends RecyclerView.Adapter<SendProgressAdapter.viewHolder> {

    Context mcontext;

    List<SendFileInform> sendfilelist;
    recyclerviewClickListener mrecyclerviewClickListener;

    public SendProgressAdapter(Context context, List<SendFileInform> sendList) {
        mcontext = context;
        sendfilelist = sendList;

    }

    @Override
    public SendProgressAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder viewHolder;
        if (parent.getTag() == null) {
            viewHolder = new viewHolder(LayoutInflater.from(mcontext).inflate(R.layout.send_item, null, false));
            parent.setTag(viewHolder);
        } else {
            viewHolder = (SendProgressAdapter.viewHolder) parent.getTag();
        }
        return new viewHolder(LayoutInflater.from(mcontext).inflate(R.layout.send_item, null, false));

    }

    @Override
    public void onBindViewHolder(SendProgressAdapter.viewHolder holder, final int position) {


        if (sendfilelist.get(position).getPortrait() != null) {
            holder.portraitView.setImageDrawable(new BitmapDrawable(sendfilelist.get(position).getPortrait()));
        } else {
           ;
            holder.portraitView.setImageBitmap(initFilePortrait( FileUtil.getFileType(sendfilelist.get(position).getName())));

        }

        Resources resources = GetContextUtil.getInstance().getResources();
        holder.nameTextView.setText(sendfilelist.get(position).getName());
        holder.openTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mrecyclerviewClickListener.onItemClick(view, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return sendfilelist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public ImageView portraitView;   //显示头像
        // 显示文件名
        public TextView totaltimeTextView;  //显示预计发送时间
        public ProgressBar progressBar;
        public TextView nameTextView;
        public TextView openTv;     //打开文件textview

        public viewHolder(View itemView) {
            super(itemView);
            openTv = (TextView) itemView.findViewById(R.id.send_progress_open_file);
            portraitView = (ImageView) itemView.findViewById(R.id.send_child_item_imageview);
            nameTextView = (TextView) itemView.findViewById(R.id.send_fragment_name_textview);
            totaltimeTextView = (TextView) itemView.findViewById(R.id.send_item_total_time);
            progressBar = (ProgressBar) itemView.findViewById(R.id.send_item_progressBar);


        }


    }


    /*
    * 设置recyclerview的监听接口
    *
    * */
    public void setRecyclerListener(recyclerviewClickListener listener) {
        mrecyclerviewClickListener = listener;

    }

    /*
    * 初始化文件的缩略图
    *
    * parm为文件类型 里输入MP3
    *
    * */
    public Bitmap initFilePortrait(String type) {
        Bitmap bitmap=null;
        if (type.equals("doc") || type.equals("docx") || type.equals("ppt") || type.equals("xls") || type.equals("pdf")) {

            bitmap = GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.ic_document_type));
        } else if (type.equals("iso") || type.equals("rar") || type.equals("zip"))/*   放进压缩文件里*/ {
            bitmap = GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.ic_zip_type));

        } else if (type.equals("txt")) {

            bitmap = GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.ic_document_type));
        } else if (type.equals("apk") || type.equals("exe")) {

            bitmap = GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.ic_android_type));
        } else if (type.equals("avi") || type.equals("mp4") || type.equals("wmv") || type.equals("mov") || type.equals("3gp") || type.equals("rm")) {
            bitmap = GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.ic_video_type));

        } else if (type.equals("mp3") || type.equals("ape") || type.equals("wma") || type.equals("mkv")) {
            bitmap = GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.ic_music_type));
        }
        else
        {
            bitmap = GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.ic_file_type));
        }
        return  bitmap;


    }


}
