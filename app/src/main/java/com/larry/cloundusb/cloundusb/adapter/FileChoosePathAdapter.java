package com.larry.cloundusb.cloundusb.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;

import java.util.List;

/**
 * Created by Larry on 5/23/2016.
 * <p>
 * <p>
 * 显示详细文件的路径的适配器
 */
public class FileChoosePathAdapter extends RecyclerView.Adapter<FileChoosePathAdapter.viewHodler> {

    List<SendFileInform> mfileInformList;
    Context mcontext;
    FileChoosePathAdapter.clickListener mclickListener;//监听器

    public FileChoosePathAdapter(Context context, List<SendFileInform> fileInformsList) {
        mcontext = context;
        mfileInformList = fileInformsList;


    }

    @Override
    public viewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHodler(LayoutInflater.from(mcontext).inflate(R.layout.chooser_file_fragment_item, null, false));
    }

    @Override
    public void onBindViewHolder(final viewHodler holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mclickListener != null) {
                    mclickListener.itemOnClick(v, position, mfileInformList.get(position).getPath());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mclickListener != null) {
                    mclickListener.itemLongClick(view, position, mfileInformList.get(position).getPath());
                }

                return false;
            }
        });
        holder.typeImageView.setImageDrawable(new BitmapDrawable(mfileInformList.get(position).getPortrait()));
        holder.nameTextVeiw.setText(mfileInformList.get(position).getName());
        long filesize = mfileInformList.get(position).getFilesize();
        if (mfileInformList.get(position).isFile()) {
            // holder.sizeTextView.setText(FileSizeUtil.convertFileSize(filesize));
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mfileInformList == null ? 0 : mfileInformList.size();
    }

    class viewHodler extends RecyclerView.ViewHolder {
        TextView nameTextVeiw;  //显示文件名
        TextView sizeTextView;  //显示文件大小
        CheckBox checkBox;      //文件chekckbox
        ImageView typeImageView;  //文件类别展示图片

        public viewHodler(View itemView) {
            super(itemView);
            nameTextVeiw = (TextView) itemView.findViewById(R.id.choose_file_fragment_item_name_textview);
            sizeTextView = (TextView) itemView.findViewById(R.id.choose_file_fragment_item_size_textview);
            checkBox = (CheckBox) itemView.findViewById(R.id.choose_file_fragment_item_checkbox);
            typeImageView = (ImageView) itemView.findViewById(R.id.choose_file_fragment_item_imageview);

        }
    }



    /*
    * 点击事件接口
    *
    * */

    /*
    * 初始化接口
    *
    * */
    public void setOnClickListener(clickListener clickListener) {
        mclickListener = clickListener;
    }

    public interface clickListener {
        void itemOnClick(View view, int position, String path);

        void itemLongClick(View view, int position, String path);
    }


    public void addData(int position, SendFileInform sendFileInform) {
        mfileInformList.add(position, sendFileInform);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mfileInformList.remove(position);
        notifyItemRemoved(position);
    }


}
