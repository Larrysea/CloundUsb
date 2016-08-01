package com.larry.cloundusb.cloundusb.view;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.adapter.SelectContactItemAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.larry.cloundusb.cloundusb.util.StringUtil;

import java.io.File;

/**
 * Created by LARRYSEA on 2016/6/18.
 *
 *
 *
 * 重命名文件的dialog
 */
public class RenameDialog  extends DialogFragment {

    EditText nameEd;
    closeInterface minterface;
    String filepath;//原有文件路径
    String filename;//文件名
    int filePosition;  //文件位置
    String specialInfo;   //文件详细信息
    long fileSize;         //文件大小
    TextView okTv;  //确认textview
    TextView cancelTv;  //取消textview
    String fileType;    //文件类型
    clickListener mclicklistener;
    static updaRenameDialog mupdateInter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rename_dialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        filename=this.getArguments().getString("fileName");
        fileType=FileUtil.getFileType(filename);
        filename=FileUtil.getFileName(filename);
        filepath=this.getArguments().getString("filePath");
        filePosition=this.getArguments().getInt("filePosition");

        fileSize=this.getArguments().getLong("fileSize");
        nameEd=(EditText)view.findViewById(R.id.rename_dialog_ed);

        nameEd.setText(filename);
        okTv=(TextView)view.findViewById(R.id.renameDialog_okbtn);
        cancelTv=(TextView)view.findViewById(R.id.renamedialog_canbtn);
        mclicklistener=new clickListener();
        okTv.setOnClickListener(mclicklistener);
        cancelTv.setOnClickListener(mclicklistener);
        return view;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


    interface closeInterface
    {
        void closeListener(String newname);//监听dialog关闭事件

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class clickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.renameDialog_okbtn:
                    FileUtil.renameFile(filepath,filename,nameEd.getText().toString().trim());
                    getDialog().dismiss();
                    if(mupdateInter!=null)
                    {
                        SendFileInform sendFileInform=new SendFileInform();
                        sendFileInform.setType(fileType);
                        sendFileInform.setName(nameEd.getText().toString().trim()+"."+fileType);
                        filepath=filepath.substring(0,filepath.lastIndexOf("/"));
                        filepath+="/"+nameEd.getText().toString().trim();
                        sendFileInform.setPath(filepath+"."+fileType);
                        sendFileInform.setPortrait(GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.history_files_file)));
                        sendFileInform.setFilesize(fileSize);
                        mupdateInter.renameUPdate(filePosition,sendFileInform);
                    }

                break;
                case R.id.renamedialog_canbtn:
                    getDialog().dismiss();
                    break;

            }
        }
    }



    public interface updaRenameDialog{
        void   renameUPdate(int position, SendFileInform sendFileInform);
    }

    static public void setRenameInterface(updaRenameDialog updaRenameDialog)
    {
        mupdateInter=updaRenameDialog;

    }





}
