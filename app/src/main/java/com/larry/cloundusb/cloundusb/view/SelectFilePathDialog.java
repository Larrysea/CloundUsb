package com.larry.cloundusb.cloundusb.view;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileSizeUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Larry on 6/8/2016.
 */
public class SelectFilePathDialog extends DialogFragment
{
    TextView internalTv;
    TextView externalTv;
    TextView cancelTextView;
    com.gc.materialdesign.views.CheckBox internalCB;
    com.gc.materialdesign.views.CheckBox externalCB;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.choosefiledialog,container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initComponent(view);
        return view;
    }

    /*
    * 初始化组件
    *
    * */
    public void initComponent(View view)
    {
        internalTv=(TextView)view.findViewById(R.id.choose_dialog_text1);
        internalCB=(com.gc.materialdesign.views.CheckBox)view.findViewById(R.id.choose_dialog_checkbox1);
        externalTv=(TextView)view.findViewById(R.id.choose_dialog_text2);
        externalCB=(com.gc.materialdesign.views.CheckBox)view.findViewById(R.id.choose_dialog_checkbox2);
        String internal=getResources().getString(R.string.external_path)+FileSizeUtil.sdCardstorageInfor();
        internalTv.setText(internal);
        final String external=getResources().getString(R.string.internal_path)+FileSizeUtil.phoneStorageInfor();
        externalTv.setText(external);
        cancelTextView=(TextView)view.findViewById(R.id.cancle_tv);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        internalCB.setOncheckListener(new com.gc.materialdesign.views.CheckBox.OnCheckListener() {
            @Override
            public void onCheck(com.gc.materialdesign.views.CheckBox checkBox, boolean b) {
                FileUtil.initDefaultpath(Environment.getExternalStorageDirectory().getAbsolutePath());
                if(b==true)
                {
                    externalCB.setChecked(false);
                }
            }
        });
        externalCB.setOncheckListener(new com.gc.materialdesign.views.CheckBox.OnCheckListener()
        {
            @Override
            public void onCheck(com.gc.materialdesign.views.CheckBox checkBox, boolean b) {
                FileUtil.initDefaultpath(FileUtil.getStoragePath(GetContextUtil.getInstance(),true));
                if(b==true)
                {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    startActivityForResult(intent, 42);
                    internalCB.setChecked(false);
                }
            }

        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == Activity.RESULT_OK) {
            Uri treeUri = resultData.getData();
            DocumentFile pickedDir = DocumentFile.fromTreeUri(GetContextUtil.getInstance(), treeUri);

            // List all existing files inside picked directory
            for (DocumentFile file : pickedDir.listFiles()) {
                Log.e("selecetfilepathdialog", "Found file " + file.getName() + " with size " + file.length());
            }

            // Create a new file and write into it
            DocumentFile newFile = pickedDir.createFile("text/plain", "My Novel");
            try{

                OutputStream out = GetContextUtil.getInstance().getContentResolver().openOutputStream(newFile.getUri());
                out.write("A long time ago...".getBytes());
                out.close();
            }catch (IOException e)
            {

            }

        }
    }



}
