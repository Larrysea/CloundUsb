package com.larry.cloundusb.cloundusb.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.baseclass.MusicBean;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;

import java.util.List;

/**
 * Created by Larry on 3/20/2016.
 */
public class MusicGroupAdapter extends  RecyclerView.Adapter<MusicGroupAdapter.musicViewHolder> {



    List<MusicBean> list;
    Point point=new Point(0,0);//用来设置图片的大小
    Context mContext;//
    clickListener mclickListener;
    @Override
    public musicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        musicViewHolder viewHolder=new musicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.music_fragment_group_item,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final musicViewHolder holder, final int position) {

        MusicBean imageBean=list.get(position);



        holder.checkImageButton.setSelected(false);
        holder.checkImageButton.setVisibility(View.GONE);
        holder.countTextView.setText("("+list.get(position).getCount()+")");
        holder.nametextview.setText(list.get(position).getParentPath());
        holder.firstImageView.setImageResource(R.mipmap.music_folder);
        if(mclickListener!=null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position= holder.getPosition();
                    mclickListener.onItemClick(holder.itemView,position);


                    holder.checkImageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(!holder.checkImageButton.isChecked())
                            {
                                holder.checkImageButton.setChecked(true);
                                holder.checkImageButton.setVisibility(View.VISIBLE);
/*
                                SendFileInform sendFileInform=new SendFileInform();
                                sendFileInform.setName(list.get(position).getParentPath());

                                sendFileInform.setPath(list.get(holder.getAdapterPosition()).getAbsPath());
                                String path=list.get(holder.getAdapterPosition()).getAbsPath();

                                sendFileInform.setTime(100);
                                sendFileInform.setFilesize(Long.valueOf(list.get(position).getFileSize()));
                                sendFileInform.setPosition(position);
                                FileBox.getInstance().storageSendFileItem(sendFileInform);*/
                            }
                            else{
/*
                                holder.checkImageButton.setChecked(false);
                                holder.checkImageButton.setVisibility(View.INVISIBLE);
                                FileBox.getInstance().cancelSendFile(list.get(position).getFileName());*/
                            }
                        }
                    });


                    ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());


                }
            });


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int postion=holder.getPosition();
                    mclickListener.onItemLongClick(holder.itemView,position);


                    return false;
                }
            });
        }
        holder.checkImageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParentFragment.updateCountTextView(list.get(holder.getPosition()).getCount());

            }
        });



    }
    @Override
    public int getItemCount() {
        return list.size();

    }

    LayoutInflater layoutInflater;



    public interface  clickListener
    {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }


    public void setOnItemClickListener( clickListener listener)
    {
        this.mclickListener=listener;


    }






    public MusicGroupAdapter(Context context, List<MusicBean> list)
    {

        layoutInflater=LayoutInflater.from(context);

        this.list=list;
        mContext=context;


    }


    static class musicViewHolder extends RecyclerView.ViewHolder
    {


        ImageView firstImageView;   //显示第一个图片的imagview
        TextView nametextview;      //显示姓名的
        CheckBox checkImageButton; //选中按钮
        TextView countTextView;     //显示计数按钮
        clickListener listener;

        public musicViewHolder(View v)
        {
            super(v);
            checkImageButton=(CheckBox) v.findViewById(R.id.music_fragment_selected_button);
            firstImageView=(ImageView)v.findViewById(R.id.music_fragment_imageview);
            nametextview=(TextView)v.findViewById(R.id.music_fragment_name_textview);
            countTextView=(TextView)v.findViewById(R.id.music_fragment_count_textview);
        }







    }



}
