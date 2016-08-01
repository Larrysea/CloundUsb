package com.larry.cloundusb.cloundusb.testactivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 5/29/2016.
 */
public class testrecyclercview extends Activity {

    RecyclerView recyclerview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testrecyclervew);
        recyclerview=(RecyclerView)findViewById(R.id.testrecyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(GetContextUtil.getInstance()));
        recyclerview.addItemDecoration(new DividerItemDecoration(GetContextUtil.getInstance(), DividerItemDecoration.VERTICAL_LIST));
        testviewadapter.viewholder viewholder;
        testviewadapter testviewadapter=new testviewadapter(init());
        recyclerview.setAdapter(testviewadapter);

        testviewadapter.setMclickListener(new clickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(testrecyclercview.this, "position"+position, Toast.LENGTH_SHORT).show();
                LinearLayoutManager lin=new LinearLayoutManager(GetContextUtil.getInstance());
                int last=lin.findLastVisibleItemPosition();
                int fast=lin.findFirstVisibleItemPosition();
               // if(position>=fast&&position<=last)
                {
                   View itemview=recyclerview.getChildAt(position);
                  testviewadapter.viewholder viewholder=(testviewadapter.viewholder) recyclerview.getChildViewHolder(view);
                  Log.e(viewholder+"","");
                  viewholder.nameTextView.setText("cdsjcnsdkjnc");
                }

           }
        });


        //viewholder=(testviewadapter.viewholder)recyclerview.findViewHolderForLayoutPosition(1);
        // viewholder=(testviewadapter.viewholder)recyclerview.getChildViewHolder(recyclerview.getChildAt(1));
        //viewholder.nameTextView.setText("测试成功");
    }

    class testviewadapter extends RecyclerView.Adapter<testviewadapter.viewholder>
    {

        public void setMclickListener(clickListener parm)
        {
            mclickListener=parm;
        }



        List<String> content;
        public testviewadapter(List<String> parm)
        {
            content=parm;

        }
        @Override
        public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            return  new viewholder(LayoutInflater.from(GetContextUtil.getInstance()).inflate(R.layout.simple_item_test,parent,false));
        }

        @Override
        public void onBindViewHolder(viewholder holder, final int position) {

            holder.nameTextView.setText(content.get(position)+" dasdasd");
            if(mclickListener!=null)
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mclickListener.onItemClick(v,position);
                    Log.e("显示position",position+"");
                }
            });
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        class viewholder extends  RecyclerView.ViewHolder
        {

            public TextView nameTextView;
            public viewholder(View itemView) {
                super(itemView);
                nameTextView=(TextView)itemView.findViewById(R.id.simple_item_textview);
            }
        }
    }

    public List<String> init() {
        List<String> content=new ArrayList<String>();
        for (int i=0;i<10;i++)
        {
            content.add("测试使用"+i);

        }
        return content;
    }
    clickListener mclickListener;
    interface  clickListener
    {
        void onItemClick(View view,int position);
    }



}