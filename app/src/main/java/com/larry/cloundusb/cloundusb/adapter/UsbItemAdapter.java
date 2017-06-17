package com.larry.cloundusb.cloundusb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;

import java.util.List;

/**
 * Created by Larry on 6/10/2016.
 * <p/>
 * <p/>
 * <p/>
 * usb文件item的适配器
 */
public class UsbItemAdapter extends RecyclerView.Adapter<UsbItemAdapter.viewHolder> {

    List<SendFileInform> msendFileInfromList;

    recyclerListener mlistener;

    public UsbItemAdapter(int type) {
        switch (type) {
            case 0:
                msendFileInfromList = FileBox.getInstance().getUsbImageList();
                break;
            case 1:
                msendFileInfromList = FileBox.getInstance().getUsbMusicList();
                break;
            case 2:
                msendFileInfromList = FileBox.getInstance().getUsbVideoList();
                break;
            case 3:
                msendFileInfromList = FileBox.getInstance().getUsbDocumentList();
                break;
            case 4:
                msendFileInfromList = FileBox.getInstance().getUsbotherList();
                break;

        }

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new viewHolder(LayoutInflater.from(GetContextUtil.getInstance()).inflate(R.layout.check_usb_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        holder.nameTextView.setText(msendFileInfromList.get(position).getName());
        holder.sizeTextView.setText(String.valueOf(msendFileInfromList.get(position).getFileUnit()));

        holder.imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(holder.imagview.getText().toString().trim()).equals(GetContextUtil.getInstance().getString(R.string.open))) {
                    mlistener.onItemClick(holder.itemView, position, 1);//表示下载文件
                } else {
                    mlistener.onItemClick(holder.itemView, position, 2);  //表示打开文件
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return msendFileInfromList == null ? 0 : msendFileInfromList.size();
    }

    public void setRecyclerListener(recyclerListener listener) {
        mlistener = listener;
    }


    public interface recyclerListener {
        void onItemClick(View view, int position, int type);

        void onItemLongClick(View view, int position);

    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public TextView imagview;
        public TextView nameTextView;
        public TextView sizeTextView;
        public ProgressBar progressbar;

        public viewHolder(View v) {
            super(v);
            imagview = (TextView) v.findViewById(R.id.check_usb_item_download_image);
            nameTextView = (TextView) v.findViewById(R.id.check_usb_item_name_tv);
            sizeTextView = (TextView) v.findViewById(R.id.check_usb_item_size_tv);
            progressbar = (ProgressBar) v.findViewById(R.id.usb_item_progressbar);
        }

    }

    public List<SendFileInform> getMsendFileInfromList() {
        return msendFileInfromList;
    }


}
