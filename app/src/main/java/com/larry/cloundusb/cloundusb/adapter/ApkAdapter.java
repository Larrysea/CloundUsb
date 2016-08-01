package com.larry.cloundusb.cloundusb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.baseclass.ApkInform;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

import java.util.List;


/**
 * Created by Larry on 3/22/2016.
 */
public class ApkAdapter  extends RecyclerView.Adapter<ApkAdapter.viewHolder>  {

    List<ApkInform> apkList;
    LayoutInflater inflater;
    static Context mcontext;
    final int SHOW_BUTTONFLOAT=2;//
    public ApkAdapter(Context context, List<ApkInform> list)
    {
        this.mcontext=context;
        apkList =list;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return  new viewHolder(LayoutInflater.from(mcontext).inflate(R.layout.apk_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        String path= apkList.get(position).getAbsPath();
       if(!path.equals(holder.imageview.getTag()))
       {
           holder.imageview.setTag(path);
           holder.imageview.setImageDrawable(apkList.get(position).getThumbPath());
       }
        holder.imageview.setMaxHeight(170);
        holder.imageview.setMaxWidth(170);
        holder.imageview.setMinimumWidth(170);
        holder.imageview.setMinimumHeight(170);
        holder.imageview.setAdjustViewBounds(true);
        holder.checkbox.setChecked(false);
        holder.nameTextView.setText(apkList.get(position).getFileName().substring(0,apkList.get(holder.getAdapterPosition()).getFileName().lastIndexOf(".")));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletePosition= FileBox.getInstance().getSendListSize();
                if(!holder.checkbox.isChecked())
                {
                    holder.checkbox.setChecked(true);
                    holder.checkbox.setVisibility(View.VISIBLE);
                    SendFileInform sendFileInform=new SendFileInform();
                    sendFileInform.setName(apkList.get(holder.getAdapterPosition()).getFileName());
                    sendFileInform.setPath(apkList.get(holder.getAdapterPosition()).getAbsPath());
                    String path=apkList.get(holder.getAdapterPosition()).getAbsPath();
                    sendFileInform.setPortrait(GraphicsUtil.drawableToBitmap(apkList.get(holder.getAdapterPosition()).getThumbPath()));
                    sendFileInform.setTime(100);
                    sendFileInform.setFilesize(Long.valueOf(apkList.get(position).getFileSize()));
                    sendFileInform.setPosition(deletePosition);
                    FileBox.getInstance().storageSendFileItem(sendFileInform);
                }
                else{

                    holder.checkbox.setChecked(false);
                    holder.checkbox.setVisibility(View.INVISIBLE);
                    FileBox.getInstance().cancelSendFile(apkList.get(position).getFileName());
                }
                ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
             mclickListener.onItemLongClick(v,position);
                return false;
            }
        });








    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return apkList.size();
    }

    //子項目的viewholder

    static class viewHolder  extends RecyclerView.ViewHolder
    {

        ImageView imageview;
        CheckBox checkbox;
        public  TextView nameTextView;



        public viewHolder(View v)
        {
            super(v);
            imageview=(ImageView)v.findViewById(R.id.apk_item_image_view);
            checkbox=(CheckBox)v.findViewById(R.id.apk_item_check_box);
            nameTextView=(TextView)v.findViewById(R.id.apk_item_name_TextView);



        }

        /*
        * 获取nametextview
        *
        * */
        public View getNameTextView()
        {
            return nameTextView;
        }


    }

    clickListener mclickListener;
    public interface  clickListener
    {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }


    public void setOnItemClickListener( clickListener listener)
    {
        this.mclickListener=listener;
    }







}
