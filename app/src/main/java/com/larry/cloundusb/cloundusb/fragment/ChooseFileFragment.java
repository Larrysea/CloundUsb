package com.larry.cloundusb.cloundusb.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileSizeUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fileutil.MultiMediaUtil;
import com.larry.cloundusb.cloundusb.service.CheckNetWorkStateServices;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Larry on 4/29/2016.
 * <p/>
 * 选择文件类的fragment
 * <p/>
 * 第五栏选择文件栏
 */
public class ChooseFileFragment extends BackHandledFragment {

    final static int SCAN_OK = 1;
    final static int UPDATE_UI = 1;
    final static String TAG = "Choose  file  fragment";
    public static int fragmentCounts = 0;// fileChoosePathfragment  是否有choosefilepathfragment   没有则显示linearLayout
    /* View view;*/
    LinearLayout phoneStorage;  //展示手机的存储信息的item
    LinearLayout sdCardStorage; //展示内存存储信息的item
    LinearLayout documentLinearLayout; //下面的三个子item 分别是文档类型，压缩文件，电子书
    LinearLayout zipLinearLayout;
    LinearLayout ebookLieatLayout;
    TextView documentAmountTextview;
    TextView zipAmountTextview;
    TextView ebookAmountTextView;
    TextView phoneAmountTextView;  //显示手机文件比例的textview
    TextView sdCardAmountTextView; //显示内存卡存储比例的textview
    ProgressBar phoneProgressBar;
    ProgressBar sdCardProgrssBar; //文件比例进度条
    FileChooserPathFragment mfragment;//文件选择fragment
    Bundle mbundle;//存储文件信息的bundle
    FragmentTransaction mfragmentTransaction;
    LinearLayout mlinearLayout;
    public static showLinearLayout mshowLinearLayout;



    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int amount[] = new int[3];
            amount = (int[]) msg.obj;
            switch (msg.what) {
                case SCAN_OK:
                    documentAmountTextview.setText(amount[0] + getResources().getString(R.string.file_unit));
                    zipAmountTextview.setText(amount[1] + getResources().getString(R.string.file_unit));
                    ebookAmountTextView.setText(amount[2] + getResources().getString(R.string.file_unit));
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_file_fragment, container, false);
        initComponent(view);
        return view;
    }

    @Override
    public boolean interceptBackPressed() {
        return true;
    }

    /*
    *
    * 初始化组件的相关信息
    *
    * */
    public void initComponent(View view) {
        mbundle = new Bundle();
        mfragment = new FileChooserPathFragment();
        phoneStorage = (LinearLayout) view.findViewById(R.id.choose_file_fragment_phone_linearlayout);
        sdCardStorage = (LinearLayout) view.findViewById(R.id.choose_file_fragment_card_linearlayout);
        mlinearLayout = (LinearLayout) view.findViewById(R.id.choose_file_fragment_linearlayout);
        // if (FileUtil.hasSDCard()) {
        sdCardStorage.setVisibility(View.VISIBLE);
        // }
        documentLinearLayout = (LinearLayout) view.findViewById(R.id.choose_file_fragment_docunment_lineatlayout);
        zipLinearLayout = (LinearLayout) view.findViewById(R.id.choose_file_fragment_zip_linealayout);
        ebookLieatLayout = (LinearLayout) view.findViewById(R.id.choose_file_fragment_ebook_linearlayout);
        documentAmountTextview = (TextView) view.findViewById(R.id.choose_file_fragment_doc_amount_textview);
        zipAmountTextview = (TextView) view.findViewById(R.id.choose_file_fragment_zip_amount_textview);
        ebookAmountTextView = (TextView) view.findViewById(R.id.choose_file_fragment_ebook_amount_textview);
        phoneAmountTextView = (TextView) view.findViewById(R.id.choose_file_fragment_phone_memory_rate);
        sdCardAmountTextView = (TextView) view.findViewById(R.id.choose_file_fragment_card_memory_rate);
        phoneProgressBar = (ProgressBar) view.findViewById(R.id.choose_file_fragment_phone_progressbar);
        sdCardProgrssBar = (ProgressBar) view.findViewById(R.id.choose_file_fragment_card_progressbar);

        phoneStorage.setOnClickListener(new clickListenr());
        sdCardStorage.setOnClickListener(new clickListenr());
        documentLinearLayout.setOnClickListener(new clickListenr());
        zipLinearLayout.setOnClickListener(new clickListenr());
        ebookLieatLayout.setOnClickListener(new clickListenr());
        CheckNetWorkStateServices.getHandler().sendEmptyMessage(UPDATE_UI);
        CheckNetWorkStateServices.setUpdateUi(new CheckNetWorkStateServices.updateUi() {
            @Override
            public void updateUi(int[] amount) {
                documentAmountTextview.setText(String.valueOf(amount[0]) + getResources().getString(R.string.file_unit));
                ebookAmountTextView.setText(String.valueOf(amount[1]) + getResources().getString(R.string.file_unit));
                zipAmountTextview.setText(String.valueOf(amount[2]) + getResources().getString(R.string.file_unit));
                Log.e(FileSizeUtil.getTotalMemorySize(GetContextUtil.getInstance()) + "", "  ");
                double phoneResult[] = FileSizeUtil.getPhoneSDaCardStafs();
                double sdcardResult[] = FileSizeUtil.getExternalSDCardStafs(GetContextUtil.getInstance(), true);
                if (FileUtil.checkSDCard()) {
                    sdCardAmountTextView.setText(FileSizeUtil.convertFileSize((long) sdcardResult[1]) + "/" + FileSizeUtil.convertFileSize((long) sdcardResult[0]));
                }
                phoneAmountTextView.setText(FileSizeUtil.convertFileSize((long) phoneResult[1]) + "/" + FileSizeUtil.convertFileSize((long) phoneResult[0]));
                phoneProgressBar.setProgress((int) (100 * (phoneResult[0] - phoneResult[1]) / phoneResult[0]));
                sdCardProgrssBar.setProgress((int) (100 * (sdcardResult[0] - sdcardResult[1]) / sdcardResult[0]));


            }
        });

    }

    /*
    * 获取handler
    *
    * */
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "choose file fragment被销毁");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "choose file fragment stop");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "choose file fragmen on resume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "choose file fragment pause");

    }



    static public void setShowLinearLayout(showLinearLayout showLinearLayout)
    {
        mshowLinearLayout=showLinearLayout;
    }


    public interface showLinearLayout {
        void showLinearLayout();

    }

    class clickListenr implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.choose_file_fragment_phone_linearlayout:
                    mbundle.putString("initPath", Environment.getExternalStorageDirectory().getAbsolutePath());
                    mbundle.putInt("type", 0);
                    break;
                case R.id.choose_file_fragment_card_linearlayout:
                    mbundle.putInt("type", 1);
                    String path;

                    String sdpath = FileUtil.getStoragePath(GetContextUtil.getInstance(), true);
                    File file = new File(sdpath);
                    Log.e(sdpath + "    ", "显示外置sd卡文件路径" + file.isDirectory());
                    mbundle.putString("initPath", sdpath);
                    break;
                case R.id.choose_file_fragment_docunment_lineatlayout:
                    mbundle.putInt("type", 2);
                    mbundle.putSerializable("docmument", (Serializable) MultiMediaUtil.getDocInformList());
                    break;
                case R.id.choose_file_fragment_zip_linealayout:
                    mbundle.putInt("type", 3);
                    mbundle.putSerializable("zip", (Serializable) MultiMediaUtil.getZipInformList());
                    break;
                case R.id.choose_file_fragment_ebook_linearlayout:
                    mbundle.putInt("type", 4);
                    mbundle.putSerializable("ebook", (Serializable) MultiMediaUtil.getEbookInformLis());
            }
            mfragment.setArguments(mbundle);
            FragmentTransaction mfragmentTransaction = getFragmentManager().beginTransaction();
            mfragmentTransaction.replace(R.id.choose_file_fragment_container, mfragment);
            mfragmentTransaction.addToBackStack(null);
            mfragmentTransaction.commit();
          //  mlinearLayout.setVisibility(View.GONE);

        }
    }


}
