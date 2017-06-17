package com.larry.cloundusb.cloundusb.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.activity.CopyFileActivity;
import com.larry.cloundusb.cloundusb.adapter.FileChoosePathAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.view.ContextMenuDialog;
import com.larry.cloundusb.cloundusb.view.RenameDialog;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 5/23/2016.
 * <p/>
 * <p/>
 * <p/>
 * 显示文件的详细路径信息的fragment
 * <p/>
 * <p/>
 * 子框架展示子文件
 */
public class FileChooserPathFragment extends BackHandledFragment implements CopyFileActivity.updateFragment, RenameDialog.updaRenameDialog,ChooseFileFragment.showLinearLayout{

    final static int SCAN_OK = 1;
    static public String nowPath;//现在fragment的路径
    static List<SendFileInform> msendFileInformList;//展示当前文件的细信息
    RecyclerView mrecyclerview;//文件item的recyclerview
    FileChoosePathAdapter madapter;
    View view;
    String minitPath;//初始化路径
    ProgressDialog mprogressdialog;//私有进度条
    List<FileChooserPathFragment> fileChooserFragmentStack;
    Bundle mbundle;
    FragmentManager mfragmantager;
    File file;
    static String oldPath;   //上一次的文件位置历史记录
    final static String TAG="file choose  path ";



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    mprogressdialog.dismiss();
                    break;
            }
        }
    };

    public FileChooserPathFragment() {
        addFragmentTostack(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.file_chooser_path_fragment,container, false);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        initComponent();
        return view;

    }

    /*
    *
    *  初始化组件相关信息
    * */
    public void initComponent() {
        mbundle = new Bundle();
        mfragmantager = getFragmentManager();
        mprogressdialog = ProgressDialog.show(getActivity(), null, "一会就好!");
        msendFileInformList = getData(getArguments().getInt("type"));
        madapter = new FileChoosePathAdapter(GetContextUtil.getInstance(), msendFileInformList);
        mrecyclerview = (RecyclerView) view.findViewById(R.id.file_chooser_path_fragment_recyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(GetContextUtil.getInstance()));
        mrecyclerview.setAdapter(madapter);
        initListener();


    }

    /*
    * 获取handler
    * */
    public Handler getHanlder() {

        return handler;


    }




    /*
    *
    *初始化监听器
    *
    * */
    public void initListener()
    {
        RenameDialog.setRenameInterface(this);
        madapter.setOnClickListener(new FileChoosePathAdapter.clickListener() {
            @Override
            public void itemOnClick(View view, int position, String path) {
                file=new File(path);
                if(file.isDirectory())
                {
                    openFragment(position);
                }else
                {
                    FileUtil.openFile(file);
                }
            }

            @Override
            public void itemLongClick(View view, int position, String path) {
                // position=position-1;
                ContextMenuDialog contextMenuDialog=new ContextMenuDialog();
                contextMenuDialog.setDeleteInterface(new ContextMenuDialog.deleteInterface() {
                    @Override
                    public void deleteFileUpdate(int position) {
                        madapter.removeData(position);
                    }
                });
                SendFileInform sendFileInform = new SendFileInform();
                sendFileInform.setPath(msendFileInformList.get(position).getPath());
                sendFileInform.setName(msendFileInformList.get(position).getName());
                sendFileInform.setFilesize(msendFileInformList.get(position).getFilesize());
                sendFileInform.setType(msendFileInformList.get(position).getType());
                sendFileInform.setSpecialInfo(msendFileInformList.get(position).getSpecialInfo());
                sendFileInform.setPosition(position);
                sendFileInform.setPortrait(msendFileInformList.get(position).getPortrait());
                mbundle.putParcelable("copyfileinform", sendFileInform);
                contextMenuDialog.setArguments(mbundle);
                contextMenuDialog.show(mfragmantager, "tag");

            }
        });


        try {
            CopyFileActivity copyFileActivity = (CopyFileActivity) getActivity();
            copyFileActivity.setUpdateFragment(this);

        } catch (ClassCastException e) {

        }
    }

    /*
    * 获取跳转数据
    *
    * */
    public List<SendFileInform> getData(int type) {
        List<SendFileInform> result;
        switch (type) {
            case 0:
               oldPath =nowPath = getArguments().getString("initPath");
                return FileUtil.listFileInform(new File(getArguments().getString("initPath")));
            case 1:
                oldPath=nowPath = getArguments().getString("initPath");
                return FileUtil.listFileInform(new File(getArguments().getString("initPath")));
            case 2:
                return (List<SendFileInform>) getArguments().getSerializable("docmument");
            case 3:
                return (List<SendFileInform>) getArguments().getSerializable("zip");
            case 4:
                return (List<SendFileInform>) getArguments().getSerializable("ebook");
            case 5:
                return (List<SendFileInform>) getArguments().getSerializable("initList");
            default:
                return null;

        }


    }

    @Override
    public boolean interceptBackPressed() {
        Log.e("内部消耗", "消耗了");
        return true;
    }

    /*
    * 返回两种不同的fragment如果fragment为空返回为空的fragment
    *
    * 中间文字提示
    *
    *如果不为空显示有内容的fragment
    *
    * */
    public void openFragment(int position) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        oldPath=nowPath;
        if(msendFileInformList.size()>position)
        {
            nowPath = msendFileInformList.get(position).getPath();

        }
        List<SendFileInform> childList = FileUtil.listFileInform(new File(nowPath));
        if (childList != null && childList.size() != 0) {
            bundle.putInt("type", 5);
            bundle.putSerializable("initList", (Serializable) childList);
            FileChooserPathFragment fileChooserPathFragment = new FileChooserPathFragment();
            fileChooserPathFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.choose_file_fragment_container, fileChooserPathFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } else {
            File file = new File(nowPath);
            if (file.isFile()) {
                //  FileUtil.openFile(file);
            } else if (file.isDirectory()) {
                NoContentFragment noContentFragment = new NoContentFragment();
                bundle.putString("contentInfor", getString(R.string.no_file_infor));
                noContentFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.choose_file_fragment_path_container, noContentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }


        }

    }

    /*
    * 将fragment添加到fragment  stack中
    *
    * */
    public void addFragmentTostack(FileChooserPathFragment Filechoosefragment) {
        if (fileChooserFragmentStack != null) {
            fileChooserFragmentStack.add(Filechoosefragment);
        } else {
            fileChooserFragmentStack = new ArrayList<FileChooserPathFragment>();
            fileChooserFragmentStack.add(Filechoosefragment);
        }

    }

    public List<FileChooserPathFragment> getFileChooserFragmentStack() {
        return fileChooserFragmentStack;
    }


    @Override
    public void copyupdateFragment(int position,SendFileInform sendFileInform) {
       /* msendFileInformList = FileUtil.listFileInform(new File(nowPath));
        madapter.notifyDataSetChanged();*/
        madapter.addData(madapter.getItemCount()-1,sendFileInform);
    }

    @Override
    public void renameUPdate(int position,SendFileInform sendFileInform) {
      /*  msendFileInformList = FileUtil.listFileInform(new File(nowPath));
        madapter.notifyDataSetChanged();*/
        madapter.removeData(position);
        madapter.addData(position, sendFileInform);
    }

    @Override
    public void onResume() {
//        msendFileInformList=FileUtil.listFileInform(new File(oldPath));
        madapter.notifyDataSetChanged();
        super.onResume();
        Log.e(TAG,"choose file fragmen on resume");

    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e(TAG,"choose file 被销毁");
    }


    @Override
    public void onStop()
    {
        super.onStop();
        Log.e(TAG,"choose file fragment stop");
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler.sendEmptyMessage(SCAN_OK);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.e(TAG,"choose file fragment pause");

    }

    @Override
    public void showLinearLayout() {

    }
}
