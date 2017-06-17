package com.larry.cloundusb.cloundusb.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.activity.CheckPictureActivity;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Larry on 3/17/2016.
 * <p>
 * <p>
 * 选择子类图片文件适配器
 */
public class PictureChildAdapter extends RecyclerView.Adapter<PictureChildAdapter.viewHolder> {

    List<String> pictureList;        //保存子类音乐图片的路径
    HashMap<Integer, Boolean> checkedMap = new HashMap<Integer, Boolean>();
    LayoutInflater inflater;
    static Context mcontext;

    IopenPictureActivity mIopenPicture;

    public PictureChildAdapter(Context context, List<String> list) {
        this.mcontext = context;
        pictureList = list;


    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewHolder viewHolder = new viewHolder(LayoutInflater.from(mcontext).inflate(R.layout.picture_child_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        final String path = pictureList.get(position);
        final File file = new File(path);

        if (!path.equals(holder.imagview.getTag())) {

            holder.imagview.setTag(path);
            ImageLoader.getInstance().displayImage("file:///" + path, holder.imagview);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIopenPicture != null) {
                    mIopenPicture.openPicture(path);
                }

            }
        });


        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SendFileInform sendFileInform = new SendFileInform();
                if (holder.checkbox.isChecked() == true) {
                    sendFileInform.setTime(10);
                    sendFileInform.setPath(pictureList.get(position));
                    sendFileInform.setName(FileUtil.getFileName(pictureList.get(position)));
                    FileBox.getInstance().storageSendFileItem(sendFileInform);
                    Log.e("显示文件数量yes", FileBox.getInstance().getSendListSize() + " ");
                    ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());
                } else {
                    FileBox.getInstance().cancelSendFile(FileUtil.getFileName(pictureList.get(position)));
                    ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());
                }
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return pictureList == null ? 0 : pictureList.size();

    }


    static class viewHolder extends RecyclerView.ViewHolder {

        ImageView imagview;
        CheckBox checkbox;

        public viewHolder(View v) {
            super(v);
            imagview = (ImageView) v.findViewById(R.id.picture_child_item_image_view);
            checkbox = (CheckBox) v.findViewById(R.id.picture_child_item_check_box);

        }
    }


    public interface IopenPictureActivity {
        //通知打开图片大图activity
        void openPicture(String filePath);
    }


    public void setOpenPicture(IopenPictureActivity iopenPictureActivity) {
        mIopenPicture = iopenPictureActivity;
    }

}
