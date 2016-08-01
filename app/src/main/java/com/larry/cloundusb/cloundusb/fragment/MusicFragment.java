package com.larry.cloundusb.cloundusb.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.adapter.MusicGroupAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.MusicBean;
import com.larry.cloundusb.cloundusb.baseclass.MusicInform;
import com.larry.cloundusb.cloundusb.fileutil.MultiMediaUtil;
import com.larry.cloundusb.cloundusb.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Larry on 3/20/2016.
 */
public class MusicFragment  extends BackHandledFragment{

    ProgressDialog mprogressDialog;
    public static  HashMap<String ,List<MusicInform>> musicHashMap;//保存所有音乐的文件信息的hashmap
    RecyclerView musicRecyclerView;   //展示音乐的recycerView
    final static int SCAN_oK=1;        //扫描结束的判定符号
    MusicGroupAdapter groupAapter;
    static Context mcontext;
    List<MusicBean> musicList;   //保存某个子类的文件


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate)
    {

        View convertView=inflater.inflate(R.layout.music_fragment,container,false);
        mcontext=getContext();
        musicRecyclerView =(RecyclerView)convertView.findViewById(R.id.recyclerviewMusic);
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        musicRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        init();
        return convertView;

    }




    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean interceptBackPressed() {
        return true;
    }

    public interface  ItemClickListener
    {
        void ItemClick(List<String> list);
    }

    private Handler handler=new Handler()
    {
        @Override
        public  void handleMessage(Message msg)
        {

            super.handleMessage(msg);
            switch(msg.what)
            {
                case SCAN_oK:
                    mprogressDialog.dismiss();
                    musicList=subgroupMusic(musicHashMap);
                    if(musicList!=null)
                    {
                    MusicGroupAdapter madapeter=new MusicGroupAdapter
                            (mcontext,musicList);
                        /*
                        *
                        *
                        * music recyclerview的监听事件
                        *
                        * */
                    madapeter.setOnItemClickListener(new MusicGroupAdapter.clickListener() {
                        @Override
                        public void onItemClick(View v, int position) {

                            Bundle bundle=new Bundle();
                            List<MusicInform> list=new ArrayList<MusicInform>();
                            list= musicHashMap.get(musicList.get(position).getParentPath());
                            bundle.putSerializable("musicChildList",(ArrayList<MusicInform>) list);
                            MusicChildFragment musicChildFragment=new MusicChildFragment();
                            musicChildFragment.setArguments(bundle);
                            FragmentTransaction transaction=getFragmentManager().beginTransaction();
                            transaction.replace(R.id.music_group_linearLayout,musicChildFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }

                        @Override
                        public void onItemLongClick(View v, int position) {

                        }
                    });
                    musicRecyclerView.setAdapter(madapeter);
                    }
                    else
                    if(musicList==null)
                    {
                        FragmentTransaction transaction=getFragmentManager().beginTransaction();
                        NoContentFragment noContentFragment=new NoContentFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("contentInfor",getContext().getString(R.string.noMusicContentInfor));
                        noContentFragment.setArguments(bundle);
                        transaction.replace(R.id.music_group_linearLayout,noContentFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                    break;
            }

        }


    };


    public void init()
    {

        musicHashMap =new HashMap<String,List<MusicInform>>();
        musicList =new ArrayList<MusicBean>();
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(mcontext, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        mprogressDialog=ProgressDialog.show(getContext(),null,"稍等");
        new Thread(new Runnable() {
            @Override
            public void run() {
                musicHashMap= MultiMediaUtil.scanMusic(GetContextUtil.getInstance());
                handler.sendEmptyMessage(SCAN_oK);

            }
        }).start();



    }










    private List<MusicBean> subgroupMusic(HashMap<String,List<MusicInform>> msuicHashMap) {




        List<MusicBean> subMusicList =new ArrayList<MusicBean>();
        Iterator<Map.Entry<String,List<MusicInform> >> iterator=msuicHashMap.entrySet().iterator();
        if(msuicHashMap.size()==0)
        {
            return null;
        }
        while (iterator.hasNext())
        {
            Map.Entry<String,List<MusicInform>> entry=iterator.next();
            MusicBean bean=new MusicBean();
            String key=entry.getKey();
            List<MusicInform> value=entry.getValue();
            bean.setCount(value.size());
            bean.setParentPath(key);
            subMusicList.add(bean);
        }

        return  subMusicList;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }





}
