package com.larry.cloundusb.cloundusb.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.baseclass.ImageBean;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Larry on 3/16/2016.
 * <p/>
 * <p/>
 * 父类组件适配器
 */
public class PictureGroupAdapter extends RecyclerView.Adapter<PictureGroupAdapter.pictureViewHolder> implements Serializable {
    List<ImageBean> list;
    Point point;//用来设置图片的大小
    Context mContext;     //设备上下文
    clickListener mclickListener;
    updateView mupdateView;   //更新刷新界面的接口
    LayoutInflater layoutInflater;


    public PictureGroupAdapter(Context context, List<ImageBean> list) {

        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        mContext = context;
        point = new Point(0, 0);

    }

    @Override
    public pictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        pictureViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new pictureViewHolder(LayoutInflater.from(mContext).
                    inflate(R.layout.picture_group_item, null));

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final pictureViewHolder holder, final int position) {

        ImageBean imageBean = list.get(position);
        String path = imageBean.getFirstImagePath();
        path = "file:///" + path;
        if (!path.equals(holder.firstImageView.getTag())) {
            holder.firstImageView.setTag(path);
            ImageLoader.getInstance().displayImage(path, holder.firstImageView);
        }
        holder.checkImageButton.setChecked(false);
        holder.countTextView.setText("(" + list.get(position).getCount() + ")");
        holder.nametextview.setText(list.get(position).getParentPath());
        // holder.firstImageView.bringToFront();
        holder.checkImageButton.setVisibility(View.GONE);
        holder.checkImageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                int[] location = new int[2];
                final int[] locationFloat = new int[2];
                holder.firstImageView.getLocationInWindow(location);
                holder.firstImageView.bringToFront();
                holder.itemView.bringToFront();

                View v = LayoutInflater.from(mContext).inflate(R.layout.file_fragment, null);
                final ButtonFloat buttonFloat = (ButtonFloat) v.findViewById(R.id.button_float);
                buttonFloat.post(new Runnable() {
                    @Override
                    public void run() {
                        buttonFloat.getLocationOnScreen(locationFloat);
                    }
                });

                //  holder.pictureRelativeLyout.startAnimation(setAnimationSet);
                if(isChecked)
                {
                    holder.checkImageButton.setChecked(true);
                    holder.checkImageButton.setVisibility(View.VISIBLE);
                    SendFileInform sendFileInform=new SendFileInform();
                    sendFileInform.setName(list.get(holder.getAdapterPosition()).getParentPath());
                    sendFileInform.setPath(list.get(holder.getAdapterPosition()).getParentPath());
                    sendFileInform.setTime(100);
                    File file=new File(list.get(position).getParentPath());
                    try {
                        file.createNewFile();
                    }catch (IOException e)
                    {
                    }
                    sendFileInform.setFilesize(file.getTotalSpace());
                    sendFileInform.setFilesize(Long.valueOf(file.length()));
                    sendFileInform.setPosition(position);
                    FileBox.getInstance().storageSendFileItem(sendFileInform);

                } else{

                    holder.checkImageButton.setChecked(false);
                    FileBox.getInstance().cancelSendFile(list.get(position).getParentPath());
                }
                ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());


            }
        });
        if (mclickListener != null) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getPosition();
                    mclickListener.onItemClick(holder.itemView, position);


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


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();

    }

    public void setOnItemClickListener(clickListener listener) {
        this.mclickListener = listener;


    }

    public void setUpdateView(updateView updateInterface) {

        this.mupdateView = updateInterface;
    }

    public AnimationSet setAnimation(int[] location) {

        AnimationSet animationSet = new AnimationSet(true);
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        Animation animation = new TranslateAnimation(location[0], 45, location[1], 825);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(animation);
        animationSet.setDuration(3000);
        animationSet.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        animation.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
        return animationSet;
    }


    public interface clickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);


    }


    //更新filefragment中的视图
    public interface updateView {

        void updateView(ImageBean imageBean, int size);

    }


    /*
    * 设置item的动画选项
    *
    * */

    class pictureViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout pictureRelativeLyout;  //父类布局
        ImageView firstImageView;   //显示第一个图片的imagview
        TextView nametextview;      //显示姓名的
        CheckBox checkImageButton; //选中按钮
        TextView countTextView;     //显示计数按钮
        clickListener listener;
        public pictureViewHolder(View v) {
            super(v);
            pictureRelativeLyout = (RelativeLayout) v.findViewById(R.id.picture_relativetlayout);
            checkImageButton = (CheckBox) v.findViewById(R.id.picture_fragment_selected_button);
            firstImageView = (ImageView) v.findViewById(R.id.picture_fragment_imageview);
            nametextview = (TextView) v.findViewById(R.id.picutre_fragment_name_textview);
            countTextView = (TextView) v.findViewById(R.id.picutre_fragment_count_textview);
        }


    }


}
