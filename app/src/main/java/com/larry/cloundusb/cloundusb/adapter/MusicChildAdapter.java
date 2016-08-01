package com.larry.cloundusb.cloundusb.adapter;

import android.content.Context;
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
import com.larry.cloundusb.cloundusb.baseclass.MusicInform;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Larry on 3/20/2016.
 *
 * 用于设置选择音乐的适配器
 */
public class MusicChildAdapter extends RecyclerView.Adapter<MusicChildAdapter.viewHolder> {


    List<MusicInform> musiceList;
    HashMap<Integer,Boolean> checkedMap =new HashMap<Integer,Boolean>();
    LayoutInflater inflater;
    static Context mcontext;

    MusicChildClickListener musicChildClickListener;

    public MusicChildAdapter(Context context, List<MusicInform> list)
    {
        this.mcontext=context;
        musiceList=list;



    }



    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        viewHolder viewHolder=new viewHolder(LayoutInflater.from(mcontext).inflate(R.layout.music_child_item,parent,false));
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder,final int position) {

        String path= musiceList.get(position).getAbsPath();
        path="file:///"+path;
        holder.imagview.setImageResource(R.mipmap.music_ic);
        holder.authorNameTextView.setText(musiceList.get(position).getAuthor());
        holder.nameTextView.setText(musiceList.get(position).getAlbumName());

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SendFileInform sendFileInform=new SendFileInform();
              if(holder.checkbox.isChecked()==true)
              {

                  sendFileInform.setTime(10);
                  sendFileInform.setPath(musiceList.get(position).getAbsPath());
                  sendFileInform.setName(musiceList.get(position).getFileName());
                  FileBox.getInstance().storageSendFileItem(sendFileInform);
                  Log.e("显示文件数量yes", FileBox.getInstance().getSendListSize() +" ");
                  ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());



              }
              else
                {
                    FileBox.getInstance().cancelSendFile(musiceList.get(position).getFileName());
                    ParentFragment.updateCountTextView(FileBox.getInstance().getSendListSize());


                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                musicChildClickListener.onClick(view,position);
            }
        });




    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return musiceList.size();
    }



    static class viewHolder  extends RecyclerView.ViewHolder
    {

        ImageView imagview;
        CheckBox checkbox;
        TextView nameTextView;
        TextView authorNameTextView;
        public viewHolder(View v)
        {
            super(v);
            imagview=(ImageView)v.findViewById(R.id.music_child_item_imageview);
            checkbox=(CheckBox)v.findViewById(R.id.music_fragment_selected_button);
            nameTextView=(TextView)v.findViewById(R.id.music_fragment_name_textview);
            authorNameTextView=(TextView)v.findViewById(R.id.music_fragment_count_textview);


        }


    }

   public  interface  MusicChildClickListener
    {
        void onClick(View view,int posoition);
       void onLongClick(View view,int position);
    }
    public void setMusicListener(MusicChildClickListener musicListener)
    {
     musicChildClickListener=musicListener;

    }







}
