package com.larry.cloundusb.cloundusb.fileutil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.TcpServer;
import com.larry.cloundusb.cloundusb.appinterface.updateProgressbarInterface;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * Created by Larry on 4/5/2016.
 */

public class FileUtil {

    final static String TAG = "file util";
    static String defaultPath;                              //默认文件保存路径
    public FileOutputStream fileOutputStream;               //文件操作
    public long file_length;
    public String FileName;
    public static long FileReceiveTime;                     //文件正式接收开始的时间
    static Bitmap bitmap;                                   //临时bitmap
    static String fileType;                                        //文件类型


    /*
    * return 返回获得文件
    * parm 字节流
    *
    * */
    public FileUtil(String file_name, long file_length) {
        this.file_length = file_length;
        try {
            this.FileName = file_name;
            this.fileOutputStream = new FileOutputStream(FileUtil.createFile(file_name));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File getFile(byte[] array) {

        File file;
        int endJudge;
        File picturePath = null; //图片路径
        File sdCardPath = null;  //sdcard路径
        FileOutputStream fileOutputStream = null;
        try {
            sdCardPath = Environment.getExternalStorageDirectory();
            if (!FileUtil.checkSDCard()) {
                picturePath = new File(Environment.getDataDirectory(), "tupina.jpg");
            } else
                picturePath = new File(sdCardPath, "tupian,jpg");

            fileOutputStream = new FileOutputStream(picturePath);
            while (true) {
                fileOutputStream.write(array, 0, array.length);

            }

        } catch (IOException e) {


            e.printStackTrace();
        }


        return picturePath;


    }

    /*
    * parm 文件
    * return 返回字节流
    *
    * */
    public static byte[] readFile(File file) {
        byte buffer[] = new byte[1024 * 60];
        int start = 0;
        try {

            int endJudge = 0;
            FileInputStream inputStream = new FileInputStream(file);
            while ((endJudge = inputStream.read(buffer)) != -1) {

                Log.e("读取文件", "杜文文件");
                buffer = new byte[1024 * 60];
                inputStream.read(buffer);
            }

            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }


    /*
    * 检查是否有sd卡
    *
    * */
    public static boolean checkSDCard() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && Environment.isExternalStorageRemovable();
    }


    /*
    *parm 传进来的文件路径
    *
    * return 返回文件信息链表
    * */
    static public List<SendFileInform> listFileInform(File dir) {
        ArrayList<SendFileInform> sendFileInformList = new ArrayList<SendFileInform>();
        if (dir.isDirectory()) {
            File file[] = dir.listFiles();
            if (file != null)
                if (file.length > 0) {
                    for (File tempFile : file) {
                        SendFileInform sendFileInform = new SendFileInform();
                        sendFileInform.setPath(tempFile.getAbsolutePath());
                        sendFileInform.setName(tempFile.getName());
                        sendFileInform.setFilesize(tempFile.length());
                        sendFileInform.setFile(tempFile.isFile());
                        if (!tempFile.isDirectory()) {
                            sendFileInform.setType(GetContextUtil.getInstance().getString(R.string.file));
                        } else {
                            sendFileInform.setType(GetContextUtil.getInstance().getString(R.string.diretory));
                        }

                        sendFileInform.setPortrait(GraphicsUtil.drawableToBitmap(initDrawable(tempFile.isDirectory())));
                        sendFileInformList.add(sendFileInform);
                    }

                }

        }
        return sendFileInformList;
    }

    /*
    *
    *   parm 输入流
    *
    *   return 返回字节数组
    *
    *
    *
    * */

    /*
    * parm 文件名
    *
    * return 返回文件类型的后缀
    *
    * 示例 ： parm 新建文本文档.txt
    *
    * 返回结果是txt
    *
    *
    * */
    static public String getFileType(String filename) {

        String postFix = filename.substring(filename.lastIndexOf(".") + 1);
        return postFix;

    }

    static public Drawable initDrawable(boolean type) {
        //如果type 为true 则表示为目录  为 false 则表示为文件
        if (type != true)
            return ContextCompat.getDrawable(GetContextUtil.getInstance(), R.mipmap.history_files_file);
        else
            return ContextCompat.getDrawable(GetContextUtil.getInstance(), R.mipmap.common_folder_default_icon);

    }


     /*
    * 获取手机的内存卡存储位值
    *
    * */


    public static String getStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
       *
       * 获取外部存储路径
       *
       * */

    /*
    * 复制文件
    *
    * */
    static public File copyFile(File sourceFile) {
        File file = null;
        if (sourceFile.exists()) {
            try {
                sourceFile.setWritable(true);
                sourceFile.setReadable(true);
                file = new File(sourceFile.getAbsolutePath(), sourceFile.getName() + "1");
                file.mkdir();
                int endJudge;
                byte buffer[] = new byte[1024 * 60];
                OutputStream outputStream = new FileOutputStream(sourceFile);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(sourceFile));
                while ((endJudge = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, endJudge);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                e.getCause();
                e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return file;

    }

    /*
    *
    * 判断是否存在sd卡然后选择保存路径
    *
    *
    *

    *
    *
    * */
    static public File createFile(String filename) throws IOException {


        Log.e(TAG, filename);
        String type = getFileType(filename);
        String path = null;
        String externalpath = null;
        boolean isscard = checkSDCard();//存在sdcard
        path = Environment.getExternalStorageDirectory().getAbsolutePath();//手机内置存储卡的位置
        externalpath = getStoragePath(GetContextUtil.getInstance(), true);
        File file;
            /*   放进文档类型文件夹里*/
        if (type.equals("doc") || type.equals("docx") || type.equals("ppt") || type.equals("xls") || type.equals("pdf")) {
            if (isscard) {
                externalpath += "/xiechuan/文档/" + filename;
            } else {
                path += "/xiechuan/文档/" + filename;
            }

        } else if (type.equals("iso") || type.equals("rar") || type.equals("zip"))/*   放进压缩文件里*/ {
            if (isscard) {
                externalpath += "/xiechuan/压缩文件/" + filename;
            } else {
                path += "/xiechuan/压缩文件/" + filename;

            }
        } else if (type.equals("txt")) {
            if (isscard) {
                externalpath += "/xiechuan/电子书/";
            } else {
                path += "/xiechuan/电子书/";
            }

        } else if (type.equals("apk") || type.equals("exe")) {
            if (isscard) {
                externalpath += "/xiechuan/安装包/";
            } else {
                path += "/xiechuan/安装包/";
            }

        } else if (type.equals("avi") || type.equals("mp4") || type.equals("wmv") || type.equals("mov") || type.equals("3gp") || type.equals("rm")) {
            if (isscard) {
                externalpath += "/xiechuan/视频/";

            } else {
                path += "/xiechuan/视频/";
            }
        } else if (type.equals("mp3") || type.equals("ape") || type.equals("wma") || type.equals("mkv")) {
            if (isscard) {
                externalpath += "/xiechuan/音乐/";
            } else {
                path += "/xiechuan/音乐/";
            }
        } else if (type.equals("bmp") || type.equals("gif") || type.equals("jpg") || type.equals("jpeg") || type.equals("png")) {
            if (isscard) {
                externalpath += "/xiechuan/图片/";
            } else {
                path += "/xiechuan/图片/";
            }

        } else {
            if (isscard) {
                externalpath += "/xiechuan/文件/";
            } else {
                path += "/xiechuan/文件/";
            }
        }

        if (isscard) {
            file = new File(externalpath);
            file.mkdirs();
            file = new File(externalpath + filename);
            file.createNewFile();
            FileUtil.defaultPath = externalpath + filename;
            return file;
        } else {
            file = new File(path);
            file.mkdirs();
            file = new File(path + filename);
            FileUtil.defaultPath = path + filename;
            file.createNewFile();

            return file;
        }


    }



    public byte[] readInputStream(InputStream inputStream) {
        byte buffer[] = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = null;
        int len = -1;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("filetool ioexception", e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }


     /*
      *  parm 输入流，文件输出流
      *
      *  return 返回文件是否复制成功
      *
      *
      * */

    static public boolean copyFile(/*InputStream inputStream,*/byte[] buffer, FileUtil tempfille, int length, long now_length) {
       /* int len;
        byte content[]=new byte[1024*60];
        try{
            while((len=inputStream.read(content))!=-1)
            {*/
        try {
            tempfille.fileOutputStream.write(buffer, 0, length);

        } catch (IOException e) {
            return false;
        }
        // }
        if (now_length == tempfille.file_length) {
            try {
                tempfille.fileOutputStream.close();
                //在这里进行保存传输信息
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }


    /*
    *
    * 一次性写文件的方式
    *
    *parm 1输入流
    *
    *
    * parm2 保存文件
    * */
    static public void copyFile(final InputStream inputStream, final long fileTotalLength, final File tempFile, final int fileposition, final updateProgressbarInterface updateProgressbarIn) {
        byte buffer[] = new byte[1460 * 8];
        long fileSendLength = 0;
        FileOutputStream fileOutputStream;
        int length;
        FileReceiveTime = System.currentTimeMillis();
        try {
            fileOutputStream = new FileOutputStream(tempFile);
            TcpServer.fileLength = fileTotalLength;
            while ((length = inputStream.read(buffer)) != -1) {
                TcpServer.fileReceiveLength = fileSendLength += length;
                fileOutputStream.write(buffer, 0, length);
                if (TcpServer.fileLength == TcpServer.fileReceiveLength) {
                    break;
                }
                if (updateProgressbarIn != null) {
                    updateProgressbarIn.updateProgressbar(fileposition, fileSendLength, TcpServer.fileLength);
                }

            }
            Log.e(TAG + "TAGATG", length + "");
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage() + "");

        }

    }


    /*
    *
    * 高速文件复制
    *
    * 返回-1表示复制文件失败
    *
    * 返回  1代表复制文件成功
    *
    * */
    static public int copyFile(File sourceFile, File resultFile) {

        FileInputStream fileinputStream;
        FileOutputStream fileOutputStream;
        FileChannel in;
        FileChannel out;

        try {
            fileinputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(resultFile);
            in = fileinputStream.getChannel();
            out = fileOutputStream.getChannel();
            in.transferTo(0, in.size(), out);
            in.close();
            out.close();
            fileOutputStream.close();
            fileinputStream.close();

        } catch (FileNotFoundException e) {
            return -1;
        } catch (IOException e) {
            return -1;
        }
        return 1;


    }

    /*
    * 初始化文件默认保存路径功能
    *
    * */
    static public void initDefaultpath(String Path) {
        defaultPath = Path;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    /*
    *
    * 获取文件名
    *
    * */
    static public String getFileName(String filename) {
        int postion = filename.lastIndexOf(".");
        return filename.substring(0, postion);

    }


    static public String getFileNameFromPath(String path) {
        int postion = path.lastIndexOf("/");
        return path.substring(postion);
    }


    /*
    *
    * 更改文件名
    * */

    static public boolean renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path);
            String filepath = path.substring(0, path.lastIndexOf("/"));
            String fileType = FileUtil.getFileType(path.substring(path.lastIndexOf("/")));
            File newfile = new File(filepath + "/" + newname + "." + fileType);

            if (!oldfile.exists()) {
                return false;//重命名文件不存在
            }
            if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
            {
                Toast.makeText(GetContextUtil.getInstance(), "已存在相同文件名文件", Toast.LENGTH_SHORT).show();
            } else {
                oldfile.renameTo(newfile);
                newfile.exists();
                Toast.makeText(GetContextUtil.getInstance(), "文件名已修改", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            Toast.makeText(GetContextUtil.getInstance(), "新文件名与源文件名相同", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    static public void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        GetContextUtil.getInstance().startActivity(intent);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    static public String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
                   /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }



    /*
    * Mime maptable 类型对应文件名
    *
    *
    * */

    public final static String[][] MIME_MapTable = {
            //{后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    /*
    *
    *
    *
    * 获取图片类型文件或者是apk类型文件的缩略图
    *
    *
    * */
    static public Bitmap getFilethumb(String filePath, Context context) {
        fileType = FileUtil.getFileType(filePath);
        if (fileType.equals("apk")) {
            return ApkSearchUtils.getApkThumb(filePath, context);

        }
        if (fileType.equals("bmp") || fileType.equals("gif") || fileType.equals("jpg") || fileType.equals("jpeg") || fileType.equals("png")) {
            return ImageLoader.getInstance().loadImageSync("file://" + filePath);
        } else
            return GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.mipmap.common_folder_default_icon));
    }


    /**
     * 检查外部存储设备是否可用
     *
     * @return   如果外部设备可用则返回true，如果不可用则返回false
     */
    public static  boolean isExternalStorageAvailable() {

        String state = Environment.getExternalStorageState();
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (mExternalStorageAvailable == true
                && mExternalStorageWriteable == true) {
            return true;
        } else {
            return false;
        }
    }



}





