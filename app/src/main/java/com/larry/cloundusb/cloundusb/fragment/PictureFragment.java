package com.larry.cloundusb.cloundusb.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.adapter.PictureGroupAdapter;
import com.larry.cloundusb.cloundusb.baseclass.ImageBean;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.MultiMediaUtil;
import com.larry.cloundusb.cloundusb.util.DividerItemDecoration;
import com.larry.cloundusb.cloundusb.view.ContextMenuDialog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Larry on 3/15/2016.
 */
public class PictureFragment extends BackHandledFragment {

    ProgressDialog mprogressDialog;
    HashMap<String, List<String>> pictureHashMap;//保存图片的文件信息的hashmap
    RecyclerView pictureRecyclerView;   //展示图片的recycerView
    final static int SCAN_oK = 1;//扫描结束的判定符号..
    PictureGroupAdapter madapter;
    static Context mcontext;
    List<ImageBean> pictureList;
    static final int REPLACEFLAG = 2;
    static List<String> childList = null;     //子类图片信息
    static final int BACK_PRESSED = 2;         //返回标志
    FragmentManager fragmentManager;


    @Override
    public boolean interceptBackPressed() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {

        pictureHashMap = new HashMap<String, List<String>>();
        pictureList = new ArrayList<ImageBean>();
        View convertView = inflater.inflate(R.layout.picture_fragment, container, false);
        mcontext = getContext();
        pictureRecyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerviewPicture);
        pictureRecyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        pictureRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        init();
        MultiMediaUtil.scanImage(3,null);                    //测试语句
        return convertView;



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    public interface updateFragment {

        void updateFragment();
    }


    public interface ItemClickListener {

        void ItemClick(List<String> list);
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_oK:
                    mprogressDialog.dismiss();
                    pictureList = subgroupImage(pictureHashMap);
                    if (pictureList != null) {
                        madapter = new PictureGroupAdapter
                                (mcontext, pictureList);
                        madapter.setOnItemClickListener(new PictureGroupAdapter.clickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Bundle bundle = new Bundle();
                                childList = new ArrayList<String>();
                                childList = pictureHashMap.get(pictureList.get(position).getParentPath());
                                bundle.putStringArrayList("childList", (ArrayList<String>) childList);
                                PictureChildFragment pictureChildFragment = null;
                                pictureChildFragment = new PictureChildFragment();
                                pictureChildFragment.setArguments(bundle);
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.picture_group_linearLayout, pictureChildFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();


                            }

                            @Override
                            public void onItemLongClick(View v, int position) {

                                ContextMenuDialog contextMenuDialog = new ContextMenuDialog();
                                SendFileInform sendFileInform = new SendFileInform();
                                sendFileInform.setPath(pictureList.get(position).getFirstImagePath());
                                sendFileInform.setName(pictureList.get(position).getParentPath());
                                File file = new File(pictureList.get(position).getParentPath());
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                }
                                sendFileInform.setFilesize(file.length());
                                sendFileInform.setType(getResources().getString(R.string.folder));
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("copyfileinform", sendFileInform);
                                contextMenuDialog.setArguments(bundle);
                                contextMenuDialog.show(fragmentManager, "tag");


                            }
                        });
                        pictureRecyclerView.setAdapter(madapter);
                        sendAdapterMessage();
                    }
                    break;
                case BACK_PRESSED:
                    pictureRecyclerView.setVisibility(View.VISIBLE);
                    break;
            }

        }


    };


    public void init() {


        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mcontext, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageLoaderConfiguration.Builder config = new
                ImageLoaderConfiguration.Builder(getContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
        mprogressDialog = ProgressDialog.show(getContext(), null, "稍等");
        pictureHashMap = MultiMediaUtil.scanImage(0,null);
        if (pictureHashMap != null)
            handler.sendEmptyMessage(SCAN_oK);
        fragmentManager = getFragmentManager();

    }


    private List<ImageBean> subgroupImage(HashMap<String, List<String>> pictureHashMap) {


        List<ImageBean> subImageList = new ArrayList<ImageBean>();
        Iterator<Map.Entry<String, List<String>>> iterator = pictureHashMap.entrySet().iterator();
        if (pictureHashMap.size() == 0) {
            return null;
        }
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            ImageBean bean = new ImageBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();
            bean.setCount(value.size());
            bean.setFirstImagePath(value.get(0));
            bean.setParentPath(key);
            subImageList.add(bean);

         /*   subImageList.get(i);
            i++;*/
        }

        return subImageList;

    }

    //返回fragment的适配器
    public PictureGroupAdapter getAdapter() {

        return madapter;

    }


    //发送适配器消息
    public void sendAdapterMessage() {
        Message messageinfor = new Message();
        Bundle bundle = new Bundle();
        bundle.putSerializable("adapter", madapter);
        bundle.putString("haha", "larryString");
        messageinfor.setData(bundle);
        messageinfor.what = SCAN_oK;
        ParentFragment.handler.sendMessage(messageinfor);

    }


    /*
    * 获得子图片的信息
    *
    * */

    static public List<String> getChildList() {
        if (childList != null)
            return childList;
        else
            return null;
    }


}
