package com.larry.cloundusb.cloundusb.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Larry on 3/17/2016.
 *
 *
 *  选择子类图片文件适配器
 */
public class PictureChildAdapter extends RecyclerView.Adapter<PictureChildAdapter.viewHolder> {

    List<String>  pictureList;        //保存子类音乐图片的路径
    HashMap<Integer,Boolean> checkedMap =new HashMap<Integer,Boolean>();
    LayoutInflater inflater;
    static Context mcontext;

    public PictureChildAdapter(Context context, List<String> list)
    {
        this.mcontext=context;
        pictureList=list;


    }



    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        viewHolder viewHolder=new viewHolder(LayoutInflater.from(mcontext).inflate(R.layout.picture_child_item,parent,false));
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder,final int position) {

        final String path= pictureList.get(position);
        final File file=new File(path);

        if(!path.equals(holder.imagview.getTag()))
        {

            holder.imagview.setTag(path);
            ImageLoader.getInstance().displayImage("file:///"+path,holder.imagview);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedMap.put(position,holder.checkbox.isChecked());
                SendFileInform sendfileinform=new SendFileInform();
                sendfileinform.setPath(pictureList.get(position));
                sendfileinform.setName(file.getName());
                sendfileinform.setTime(10);
                Bitmap bitmap= BitmapFactory.decodeFile(path);
                sendfileinform.setPortrait(GraphicsUtil.getThumbBitmap(pictureList.get(position),90,90));
                if(holder.checkbox.isChecked()==true)
                {
                    FileBox.getInstance().storageSendFileItem(sendfileinform);

                }
                else
                {
                    FileBox.getInstance().cancelSendFile(file.getName());

                }
                ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());
            }
        });


        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SendFileInform sendFileInform=new SendFileInform();
                if(holder.checkbox.isChecked()==true)
                {
                    sendFileInform.setTime(10);
                    sendFileInform.setPath(pictureList.get(position));
                    sendFileInform.setName(FileUtil.getFileName(pictureList.get(position)));
                    FileBox.getInstance().storageSendFileItem(sendFileInform);
                    Log.e("显示文件数量yes", FileBox.getInstance().getSendListSize() +" ");
                    ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());
                }
                else
                {
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
        return pictureList.size();
    }



    static class viewHolder  extends RecyclerView.ViewHolder
   {

       ImageView imagview;
       CheckBox checkbox;
       public viewHolder(View v)
       {
           super(v);
           imagview=(ImageView)v.findViewById(R.id.picture_child_item_image_view);
           checkbox=(CheckBox)v.findViewById(R.id.picture_child_item_check_box);

       }


   }






}
