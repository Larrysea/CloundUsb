package com.larry.cloundusb.cloundusb.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileSizeUtil;

/**
 * Created by Larry on 6/17/2016.
 */
public class PropertyDialog extends DialogFragment {

    TextView nameTv;
    TextView typeTv;
    TextView filesizeTv;
    TextView pathTv;
    SendFileInform sendFileInform;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.propertydialog,container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        sendFileInform=(SendFileInform) getArguments().getParcelable("copyfileinform");
        initComonent(view);
        return view;
    }


    public void initComonent(View view)
    {

        nameTv=(TextView)view.findViewById(R.id.property_name_tv);
        typeTv=(TextView)view.findViewById(R.id.property_type_tv);
        filesizeTv=(TextView)view.findViewById(R.id.property_filesize_tv);
        pathTv=(TextView)view.findViewById(R.id.property_filepath_tv);
        nameTv.setText(sendFileInform.getName());
        typeTv.setText(sendFileInform.getType());
        filesizeTv.setText(FileSizeUtil.convertFileSize(sendFileInform.getFilesize()));
        pathTv.setText(sendFileInform.getPath());
    }



}
