package com.larry.cloundusb.cloundusb.fileutil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.activity.MainActivity;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.MusicInform;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.baseclass.VideoInform;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

import java.io.File;
import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Larry on 5/2/2016.
 * 获取手机的多媒体信息
 * 例如获得手机安装的apk信息
 * 还有手机的音乐信息
 * 以及各种多媒体信息
 * <p/>
 * <p/>
 * 调用本类之中的gettypefile函数之前切记调用initSnedinformlist();
 */
public class MultiMediaUtil {
    static final int SCAN_VIDEO = 3;//扫描视频文件的标记
    /*
   * 扫描本地文件中的图片
   *
   * */

    public static HashMap<String, VideoInform> videoHashMap;
    static public List<String> recentPictureList;  //最近的图片
    static List<SendFileInform> zipInformList;  //压缩文件
    static List<SendFileInform> docInformList; //文档类型文件
    static List<SendFileInform> ebookInformList;//电子书类型文件
    static int Amount[];//保存三个数据的数量数组
    static File tempFile;//临时性文件

    /*
    * parm  最近几个小时以内的图片
    *
    * */
    static public HashMap<String, List<String>> scanImage(final int rencentHours, final Handler handler) {
        final HashMap<String, List<String>> pictureHashMap = new HashMap<String, List<String>>();
        recentPictureList = new ArrayList<String>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                ContentResolver contentResolver = null;
                ContentResolver sdContentResolver = null;
                Uri uri = null;
                for (int count = 0; count < 2; count++) {
                    if (count == 0) {
                        uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

                        contentResolver = GetContextUtil.getInstance().getApplicationContext().getContentResolver();
                        cursor = contentResolver.query(uri, null,
                                MediaStore.Images.Media.MIME_TYPE + "=? or "
                                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                    } else if (count == 1) {

                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        sdContentResolver = GetContextUtil.getInstance().getApplicationContext().getContentResolver();
                        cursor = sdContentResolver.query(uri, null,
                                MediaStore.Images.Media.MIME_TYPE + "=? or "
                                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

                    }

                    while (cursor.moveToNext()) {
                        String path = cursor.getString(cursor.getColumnIndex(
                                MediaStore.Images.Media.DATA));
                        String parentPath = new File(path).getParentFile().getName();  //尝试一下使用getparent()

                        if (!pictureHashMap.containsKey(parentPath)) {
                            List<String> filePath = new ArrayList<String>();
                            filePath.add(path);
                            pictureHashMap.put(parentPath, filePath);
                        } else {
                            tempFile = new File(path);

                            if (rencentHours > ((System.currentTimeMillis() - tempFile.lastModified()) / 1000) / 3600) {
                                recentPictureList.add(path);
                                Log.e("MUltiMedia Util ", path + (rencentHours - ((System.currentTimeMillis() - tempFile.lastModified()) / 1000) / 3600));
                            }
                            pictureHashMap.get(parentPath).add(path);

                        }
                    }

                }
                if (handler != null) {
                    handler.sendEmptyMessage(2);
                }
                cursor.close();
            }
        }).start();

        return pictureHashMap;

    }






    /*
    *  获得手机的音乐信息
    *
    * */

     /*
    * 通过contentporvider
    * 来获得手机的本地的音乐信息
    *
    * musicinform是自己定义的音乐信息
    *
    *
    * */

    static public HashMap<String, List<MusicInform>> scanMusic(Context context) {

        HashMap<String, List<MusicInform>> musicHashMap = new HashMap<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null,
                null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String parentPath = new File(path).getParentFile().getName();//尝试一下使用getparent()
            File tempFile = new File(path);
            long modifyDate = tempFile.lastModified();
            String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String type = context.getString(R.string.music);
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String author = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String totlaTime = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            MusicInform infor = new MusicInform();
            infor.setModifydateDate(modifyDate);
            infor.setType(type);
            infor.setFileName(name);
            infor.setAuthor(author);
            infor.setTotalTime(totlaTime);
            infor.setAlbumName(albumName);
            infor.setFileSize(size);
            infor.setAbsPath(path);
            if (!musicHashMap.containsKey(parentPath)) {
                List<MusicInform> filePath = new ArrayList<MusicInform>();
                filePath.add(infor);
                musicHashMap.put(parentPath, filePath);
            } else {
                musicHashMap.get(parentPath).add(infor);
            }

        }
        cursor.close();
        return musicHashMap;
    }

    /*
    * 获取手机视频信息
    *
    *
    * */
    static public void scanVideo(final Handler handler) {

        videoHashMap = new HashMap<String, VideoInform>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri uri = null;
                Cursor cursor = null;
                for (int Stroagetype = 0; Stroagetype < 2; Stroagetype++) {
                    if (Stroagetype == 0) {
                        uri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
                    } else if (Stroagetype == 1) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }
                    ContentResolver contentResolver = GetContextUtil.getInstance().getContentResolver();
                    cursor = contentResolver.query(uri, null,
                            null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
                    uri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
                    while (cursor.moveToNext()) {

                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        String parentPath = new File(path).getParentFile().getName();//尝试一下使用getparent()
                        File tempFile = new File(path);
                        long modifyDate = tempFile.lastModified();
                        String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        if (Integer.valueOf(size) != 0) {
                            String type = FileUtil.getFileType(path);
                            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                            String totlaTime = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                            Bitmap map = Bitmap.createScaledBitmap(VideoUtil.getThumb(path), 100, 100, true);
                            VideoInform infor = new VideoInform();
                            infor.setFileName(name);
                            infor.setType(type);
                            infor.setFileSize(size);
                            infor.setAbsPath(path);
                            infor.setModifydateDate(modifyDate);
                            infor.setTotaltime(totlaTime);
                            infor.setThumb(map);
                            if (!videoHashMap.containsKey(parentPath)) {
                                videoHashMap.put(path, infor);
                            }
                        }


                    }
                }
                cursor.close();
                Message message = new Message();
                message.obj = videoHashMap;
                message.what = SCAN_VIDEO;
                if (handler != null)
                    handler.sendMessage(message);

            }
        }).start();


    }

    /*
    * 获取手机所有的文档类型文件  包括 ppt  doc xls pdf xls  xlsx docx
     *
    *
    * */


    static public HashMap<String, SendFileInform> scanDocument(Context context) {

        final HashMap<String, SendFileInform> pictureHashMap = new HashMap<String, SendFileInform>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                Environment.getExternalStorageDirectory();
                if (Environment.getExternalStorageDirectory().isDirectory()) {
                }
            }
        }).start();
        return pictureHashMap;


    }

    /*
    *
    * 获取文档类型文件
    * */
    static public List<SendFileInform> getDocInformList() {
        return docInformList;
    }


    /*
   * 获取压缩类型文件
   *
   * */
    static public List<SendFileInform> getZipInformList() {
        return zipInformList;
    }


    /*
    * 获取电子书类型文件
    *
    * */
    static public List<SendFileInform> getEbookInformLis() {
        return ebookInformList;
    }

    /*
    * 获取各种类型文件
    *
    *
    * */
    static public void getTypeFile(File file) {

        // 文档类型文件 ppt docx pdf  doc xls
        //压缩文件类型 zip iso
        //电子书类型文件 txt
        if (docInformList == null || zipInformList == null || ebookInformList == null) {
            docInformList = new ArrayList<SendFileInform>();
            zipInformList = new ArrayList<SendFileInform>();
            ebookInformList = new ArrayList<SendFileInform>();

        }

        if (file != null) {
            for (File temp : file.listFiles()) {
                if (temp.isDirectory()) {
                    getTypeFile(temp);
                } else {
                    String postFix = FileUtil.getFileType(temp.getAbsolutePath());
                    if (temp.getName().endsWith("doc") || temp.getName().endsWith("ppt") || temp.getName().endsWith("pdf") || temp.getName().equals("xls") || temp.getName().equals("docx")) {
                        SendFileInform sendFileInform = new SendFileInform();
                        sendFileInform.setPath(temp.getAbsolutePath());
                        sendFileInform.setName(temp.getName());
                        sendFileInform.setFilesize(temp.length());
                        sendFileInform.setFile(true);
                        sendFileInform.setType(GetContextUtil.getInstance().getString(R.string.document));
                        sendFileInform.setPortrait(GraphicsUtil.drawableToBitmap(FileUtil.initDrawable(false)));
                        docInformList.add(sendFileInform);
                    } else if (temp.getName().endsWith("zip") || temp.getName().endsWith("iso")) {
                        SendFileInform sendFileInform = new SendFileInform();
                        sendFileInform.setPath(temp.getAbsolutePath());
                        sendFileInform.setName(temp.getName());
                        sendFileInform.setFilesize(temp.length());
                        sendFileInform.setType(GetContextUtil.getInstance().getString(R.string.zipfile));
                        sendFileInform.setFile(true);
                        sendFileInform.setPortrait(GraphicsUtil.drawableToBitmap(FileUtil.initDrawable(false)));
                        zipInformList.add(sendFileInform);
                    } else if (temp.getName().endsWith("txt")) {
                        SendFileInform sendFileInform = new SendFileInform();
                        sendFileInform.setPath(temp.getAbsolutePath());
                        sendFileInform.setName(temp.getName());
                        sendFileInform.setFilesize(temp.length());
                        sendFileInform.setFile(true);
                        sendFileInform.setType(GetContextUtil.getInstance().getString(R.string.ebook));
                        sendFileInform.setPortrait(GraphicsUtil.drawableToBitmap(FileUtil.initDrawable(false)));
                        ebookInformList.add(sendFileInform);
                    }
                }
            }


        }
    }


    /*
    * 获取三种文件的数量 zip ebook  doc
    *
    * */
    static public int[] getAmount() {
        if (docInformList != null && zipInformList != null && ebookInformList != null) {
            Amount = new int[3];
            Amount[0] = docInformList.size();
            Amount[1] = zipInformList.size();
            Amount[2] = ebookInformList.size();
        }
        return Amount;

    }

    /*
    * 初始化使用gettypefile之前一定要先调用该函数
    *
    * */
    static public void initSendInformList() {
        docInformList = new ArrayList<SendFileInform>();
        zipInformList = new ArrayList<SendFileInform>();
        ebookInformList = new ArrayList<SendFileInform>();
    }


    /*
    * 获取本地相册图片
    *
    *
    * */
    static public void scanAlbum(final Context context, final int recentHours, final Handler handler) {
        MainActivity.sexecutorService.submit(new Runnable() {
            @Override
            public void run() {
                String externaDcimlPath = "/storage/extSdCard/DCIM/Camera/";
                File[] contentListFile = new File(externaDcimlPath).listFiles();
                addPicture(contentListFile, recentHours);
            }
        });
        if (handler != null) {
            handler.sendEmptyMessage(2);
        }


    }

    /*
    * 添加图片信息
    *
    * */
    static public void addPicture(File[] contentListFile, int recentHours) {

        for (File file : contentListFile) {
            if (file.isDirectory()) {
                addPicture(file.listFiles(), recentHours);
            } else {

                if (recentHours > ((System.currentTimeMillis() - file.lastModified()) / 1000) / 3600) {
                    recentPictureList.add(file.getAbsolutePath());
                    Log.e("MUltiMedia Util ", file.getAbsolutePath());
                }
            }

        }


    }


}
