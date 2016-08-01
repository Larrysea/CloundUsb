package com.larry.cloundusb.cloundusb.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.adapter.MusicGroupAdapter;
import com.larry.cloundusb.cloundusb.adapter.VideoGroupAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.baseclass.VideoInform;
import com.larry.cloundusb.cloundusb.fileutil.MultiMediaUtil;
import com.larry.cloundusb.cloundusb.util.DividerItemDecoration;
import com.larry.cloundusb.cloundusb.fileutil.VideoUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.larry.cloundusb.cloundusb.view.ContextMenuDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 用于展示并且获取视频数据
 * Created by Larry on 3/21/2016.
 * <p/>
 * //待解决问题视频格式多种判定防止因为格式不符而崩溃
 */
public class VideoFragment extends Fragment {


    final static int SCAN_oK = 1;//扫描结束的判定符号
    static Context mcontext;
    ProgressDialog mprogressDialog;
    HashMap<String, VideoInform> videoHashMap;//保存所有视频文件信息的hashmap
    RecyclerView videoRecyclerView;   //展示视频的recycerView

    List<VideoInform> videoList;
    static final int SCAN_VIDEO=3;//扫描视频文件的标记
    Bundle mbundle;
    FragmentManager fragmentManager;
     public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_oK:
                    mprogressDialog.dismiss();
                    videoList = subgroupVideo(videoHashMap);
                    if (videoList!= null) {
                        VideoGroupAdapter madapeter = new VideoGroupAdapter
                                (mcontext, videoList);
                        Log.e("显示数据的大小",videoList.size()+" ");
                        madapeter.setOnItemClickListener(new VideoGroupAdapter.clickListener() {
                            @Override
                            public void onItemClick(View v, int position) {

                                VideoInform inform = subgroupVideo(videoHashMap).get(position);
                                Uri uri = Uri.parse(inform.getAbsPath());
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "video/mp4");
                                startActivity(intent);
                            }
                            @Override
                            public void onItemLongClick(View v, int position) {
                                ContextMenuDialog contextMenuDialog= new ContextMenuDialog();
                                SendFileInform sendFileInform=new SendFileInform();
                                sendFileInform.setPath(videoList.get(position).getAbsPath());
                                sendFileInform.setName(videoList.get(position).getFileName());
                                sendFileInform.setFilesize(Long.parseLong(videoList.get(position).getFileSize()));
                                sendFileInform.setType(videoList.get(position).getType());
                                mbundle.putParcelable("copyfileinform",sendFileInform);
                                contextMenuDialog.setArguments(mbundle);
                                contextMenuDialog.show(fragmentManager,"tag");

                            }
                        });
                        videoRecyclerView.setAdapter(madapeter);
                    }
                    else{
                        if(videoList==null)
                        {
                            FragmentTransaction transaction=getFragmentManager().beginTransaction();
                            NoContentFragment noContentFragment=new NoContentFragment();
                            Bundle bundle=new Bundle();
                            bundle.putString("contentInfor",getContext().getString(R.string.noVideoContentInfor));
                            noContentFragment.setArguments(bundle);
                            transaction.replace(R.id.video_group_linearlayout,noContentFragment);
                            transaction.commit();
                        }
                    }

                break;

                case SCAN_VIDEO:
                    videoHashMap=(HashMap<String, VideoInform>) msg.obj;

                    handler.sendEmptyMessage(SCAN_oK);
                     break;
            }

        }


    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {

        View convertView = inflater.inflate(R.layout.video_fragment, container, false);
        mcontext = getContext();
        videoRecyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerviewVideo);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        videoRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        init();
        return convertView;

    }

    public void init() {


        videoHashMap = new HashMap<String, VideoInform>();
        videoList = new ArrayList<VideoInform>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mcontext, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        mprogressDialog = ProgressDialog.show(getContext(), null, "稍等");

       if(MultiMediaUtil.videoHashMap!=null&&MultiMediaUtil.videoHashMap.size()>0)
       {
           videoHashMap= MultiMediaUtil.videoHashMap;
           handler.sendEmptyMessage(SCAN_oK);
           
       }
        else
       {
           MultiMediaUtil.scanVideo(handler);
       }
        fragmentManager=getFragmentManager();
        mbundle = new Bundle();



    }

    private List<VideoInform> subgroupVideo(HashMap<String, VideoInform> videoHashMap) {

        int start = 0;  //用于记录链表数据
        List<VideoInform> subVideoList = new ArrayList<VideoInform>();
        Iterator<Map.Entry<String, VideoInform>> iterator = videoHashMap.entrySet().iterator();
        if (videoHashMap.size() == 0) {
            return null;

        }
        while (iterator.hasNext()) {
            Map.Entry<String, VideoInform> entry = iterator.next();
            VideoInform value = entry.getValue();
            subVideoList.add(start, value);
            ++start;
        }
        return subVideoList;

    }


    public interface ItemClickListener {
        void ItemClick(List<String> list);
    }







}
