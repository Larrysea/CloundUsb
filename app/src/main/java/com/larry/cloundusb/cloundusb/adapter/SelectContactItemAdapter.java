package com.larry.cloundusb.cloundusb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendContactInfo;

import java.util.List;

/**
 * Created by Larry on 5/14/2016.
 *
 *
 * 显示将要发送的联系人的适配器
 *
 *
 */
public class SelectContactItemAdapter  extends RecyclerView.Adapter<SelectContactItemAdapter.SelectContactViewHolder>{

    List<SendContactInfo> mcontactInforList;
    ClickListener mlistener;

    public SelectContactItemAdapter(List<SendContactInfo> list)
    {
      mcontactInforList=list;
    }

    @Override
    public SelectContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SelectContactViewHolder viewHolder=new SelectContactViewHolder(LayoutInflater.from
                (GetContextUtil.getInstance()).inflate(R.layout.select_contact_item,null,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SelectContactViewHolder holder, final int position) {
        holder.portraitView.setImageResource(mcontactInforList.get(position).getResourceId());
        holder.contactnameTextView.setText(mcontactInforList.get(position).getName());
         if(mlistener!=null)
         {
             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                  mlistener.onItemClick(holder.itemView,holder.getAdapterPosition());

                 }
             });

         }

    }

    @Override
    public int getItemCount() {
        return mcontactInforList==null?0:mcontactInforList.size();
    }

    class SelectContactViewHolder extends RecyclerView.ViewHolder
   {

        ImageView portraitView;
        TextView  contactnameTextView;
       public SelectContactViewHolder(View itemView) {
           super(itemView);
           portraitView=(ImageView) itemView.findViewById(R.id.select_contact_child_item_imageview);
           contactnameTextView=(TextView)itemView.findViewById(R.id.select_contact_fragment_name_textview);
       }
   }

  static public interface  ClickListener
    {
        void onItemClick(View view,int position);
        void onLongItemClick(View view,int position);
    }

    public void setOnClickListener(ClickListener listener)
    {
     mlistener=listener;
    }



}

