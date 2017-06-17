package com.larry.cloundusb.cloundusb.fileutil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.ApkInform;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 3/22/2016.
 * <p>
 * 获取手机的apk信息
 */

public class ApkSearchUtils {


    Context mContext;
    public static List<ApkInform> apkList = new ArrayList<ApkInform>();

    /**
     * @param context
     */

    public ApkSearchUtils(Context context) {
        mContext = context;
    }

    public List<ApkInform> getApkFIle() {
        return apkList;
    }

    //该参数可以设置是否是手机自己的文件路径或者是sk
    public void FindAllAPKFile(File file) {

        // 手机上的文件,目前只判断SD卡上的APK文件sd卡的路径
        // file = Environment.getDataDirectory();
        // SD卡上的文件目录
        if (file.isFile()) {
            String name_s = file.getName();
            ApkInform myFile;
            String apk_path;
            //   MimeTypeMap.getSingleton();
            if (name_s.toLowerCase().endsWith(".apk")) {
                apk_path = file.getAbsolutePath();// apk文件的绝对路劲
                File temp = new File(apk_path);


                PackageManager pm = mContext.getPackageManager();
                PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, pm.GET_ACTIVITIES);
                ApplicationInfo appInfo = packageInfo.applicationInfo;


                /*获取apk的图标 */
                appInfo.sourceDir = apk_path;
                appInfo.publicSourceDir = apk_path;
                Drawable apk_icon = appInfo.loadIcon(pm);

                /** 得到包名 */
                String packageName = packageInfo.packageName;

                /** apk的绝对路劲 */

                /** apk的版本名称 String */
                String versionName = packageInfo.versionName;

                /** apk的版本号码 int */
                int versionCode = packageInfo.versionCode;

                /**安装处理类型*/
                String type = "应用";
                // myFile=new ApkInform(name_s,type,file.length()+"",file.getAbsolutePath(),file.lastModified(),versionName,versionCode+"",packageName,apk_icon);
                myFile = new ApkInform();
                apkList.add(myFile);
                Log.e("lodaing ------", myFile.toString());
            }
            // String apk_app = name_s.substring(name_s.lastIndexOf("."));
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    FindAllAPKFile(file_str);
                }
            }
        }
    }

    /*public List<> getApkInfor()
    {

    }
*/

    /*
    *用来获取安卓手机的已经安装的安装包
    *
   ****/


    public List<ApkInform> getApkInfor(Context context) {
        List<ApkInform> apkInformList = new ArrayList<ApkInform>();

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfosList = pm.getInstalledPackages(0);

        for (int i = 0; i < packageInfosList.size(); i++) {
            ApkInform apkInform = new ApkInform();
            PackageInfo infor = (PackageInfo) packageInfosList.get(i);
            if ((infor.applicationInfo.flags & infor.applicationInfo.FLAG_SYSTEM) <= 0) {

                //  Bitmap bitmap=BitmapFactory.decodeResource(Resources.getSystem(),pm.getApplicationIcon(infor.applicationInfo));
                Drawable drawable = pm.getApplicationIcon(infor.applicationInfo);
                drawable.setBounds(0, 0, 100, 100);
                apkInform.setThumbPath(drawable);
                //apkInform.setPackageName(pm.getApplicationLabel(infor.applicationInfo).toString());
                apkInform.setPackageName(pm.getInstallerPackageName(infor.packageName));
                apkInform.setVersionNumber(infor.versionCode);
                apkInform.setTypeNumber(infor.versionName);
                apkInform.setType(context.getResources().getString(R.string.application));
                apkInform.setSpecialInfor("package:" + infor.packageName);
                apkInform.setFileName(pm.getApplicationLabel(infor.applicationInfo).toString() + GetContextUtil.getInstance().getResources().getString(R.string.apk));
                apkInform.setAbsPath(infor.applicationInfo.sourceDir);
                apkInform.setFileSize(String.valueOf(new File(infor.applicationInfo.sourceDir).length()));
                apkInformList.add(apkInform);
            }

        }

        return apkInformList;
    }


    static public Bitmap getApkThumb(String apk_path, Context context) {

        //apk_path = file.getAbsolutePath();// apk文件的绝对路劲


        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, pm.GET_ACTIVITIES);
        if (packageInfo != null) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
             /*获取apk的图标 */
            appInfo.sourceDir = apk_path;
            appInfo.publicSourceDir = apk_path;
            Drawable apk_icon = appInfo.loadIcon(pm);
            return GraphicsUtil.drawableToBitmap(apk_icon);
        } else
            return GraphicsUtil.drawableToBitmap(context.getResources().getDrawable(R.mipmap.ic_android_type));


    }

}





