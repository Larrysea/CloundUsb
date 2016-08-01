package com.larry.cloundusb.cloundusb.view;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.loginUtil;
import com.larry.cloundusb.cloundusb.activity.CopyFileActivity;
import com.larry.cloundusb.cloundusb.activity.MainActivity;
import com.larry.cloundusb.cloundusb.activity.RegisterActivity;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.CopyFileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by Larry on 6/17/2016.
 */
public class ContextMenuDialog extends DialogFragment {


    static deleteInterface mdeleteInterface;
    TextView copyTv;
    TextView deleteTv;
    TextView renameTv;
    TextView propertyTv;
    TextView uploadFileTv;    //上传文件按钮视图
    String path;//文件路径
    String type; //文件类型   为1表示普通文件  为2表示为apk文件
    String name;//文件名
    Bundle bundle;
    int filePosition;            //文件位置
    SendFileInform sendFileInform;
    FragmentManager fragmentManager;
    ProgressDialog mprogressDialog;  //显示上传进度的dialog
    boolean progressdialogFlag;           //progressdialog是否刷新的属性


    static public void setCopyInterface(deleteInterface deleteInterface) {
        mdeleteInterface = deleteInterface;
    }

    static public void setDeleteInterface(deleteInterface deleteInterface) {
        mdeleteInterface = deleteInterface;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contextmenudialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        sendFileInform = getArguments().getParcelable("copyfileinform");
        type = sendFileInform.getType();
        path = sendFileInform.getPath();
        name = sendFileInform.getName();
        filePosition = sendFileInform.getPosition();
        initComponent(view);
        return view;

    }

    /*
    * 初始化组件信息
    *
    * */
    public void initComponent(View view) {
        copyTv = (TextView) view.findViewById(R.id.context_menu_dialog_copy);

        deleteTv = (TextView) view.findViewById(R.id.context_menu_dialog_delete);
        renameTv = (TextView) view.findViewById(R.id.context_menu_dialog_rename);
        propertyTv = (TextView) view.findViewById(R.id.context_menu_dialog_property);
        uploadFileTv = (TextView) view.findViewById(R.id.context_menu_dialog_upload_file);
        copyTv.setOnClickListener(new itemClickListener());
        uploadFileTv.setOnClickListener(new itemClickListener());
        deleteTv.setOnClickListener(new itemClickListener());
        renameTv.setOnClickListener(new itemClickListener());
        propertyTv.setOnClickListener(new itemClickListener());
        fragmentManager = getFragmentManager();


    }

    public void updatePorgressbar() {

        MainActivity.sexecutorService.submit(new Runnable() {
            @Override
            public void run() {
                while (progressdialogFlag) {
                    mprogressDialog.setProgress(loginUtil.Percent);
                    if (loginUtil.Percent == 100) {
                        mprogressDialog.dismiss();
                        progressdialogFlag=false;
                        break;
                    }
                }
            }
        });



    }

    public interface deleteInterface {
        void deleteFileUpdate(int position);

    }

    class itemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.context_menu_dialog_copy:
                    CopyFileUtil.initList();
                    CopyFileUtil.storageCopyFile(sendFileInform);
                    startActivity(new Intent(GetContextUtil.getInstance(), CopyFileActivity.class));
                    getDialog().dismiss();
                    break;

                case R.id.context_menu_dialog_delete:
                    if (type.equals("应用") && type != null) {
                        Uri uri = Uri.parse(sendFileInform.getSpecialInfo());
                        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                        startActivity(intent);

                    } else {
                        try {
                            File file = new File(path);
                            file.createNewFile();
                            file.delete();
                            Toast.makeText(GetContextUtil.getInstance(), "删除成功", Toast.LENGTH_SHORT).show();
                            mdeleteInterface.deleteFileUpdate(filePosition);
                        } catch (IOException e) {

                        }
                    }
                    getDialog().dismiss();
                    break;
                case R.id.context_menu_dialog_rename:
                    if (type.equals("应用") && type != null) {
                        Toast.makeText(GetContextUtil.getInstance(), "应用不能重新命名！", Toast.LENGTH_SHORT).show();

                    } else {
                        RenameDialog renameDialog = new RenameDialog();
                        renameDialog.show(fragmentManager, "rename");
                        Bundle bundle = new Bundle();
                        bundle.putString("fileName", sendFileInform.getName());
                        bundle.putString("filePath", sendFileInform.getPath());
                        bundle.putInt("filePosition", sendFileInform.getPosition());
                        bundle.putLong("fileSize", sendFileInform.getFilesize());
                        renameDialog.setArguments(bundle);

                    }
                    getDialog().dismiss();
                    break;
                case R.id.context_menu_dialog_property:
                    getDialog().dismiss();
                    PropertyDialog propertyDialog = new PropertyDialog();
                    bundle = new Bundle();
                    bundle.putParcelable("copyfileinform", sendFileInform);
                    propertyDialog.setArguments(bundle);
                    propertyDialog.show(fragmentManager, "contextmenu");
                    getDialog().dismiss();
                    break;

                case R.id.context_menu_dialog_upload_file:
                    if (MainActivity.user.isLogin()) {

                        mprogressDialog = new ProgressDialog(getActivity());
                        mprogressDialog.setMessage("正在上传" +name);
                        mprogressDialog.setTitle("上传进度");
                        mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mprogressDialog.setIndeterminate(false);
                        mprogressDialog.setMax(100);
                        getDialog().dismiss();
                        mprogressDialog.show();
                        progressdialogFlag = true;
                        updatePorgressbar();
                        loginUtil.sendFile(path,name);


                    } else {
                        Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                        getActivity().startActivity(new Intent(getActivity(), RegisterActivity.class));

                    }
                    break;

            }
        }
    }
}
