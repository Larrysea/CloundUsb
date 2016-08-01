package com.larry.cloundusb.cloundusb.fileutil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

/**
 * Created by Larry on 3/21/2016.
 * <p/>
 * 关于视频类的工具
 */


public class VideoUtil {

    public static Bitmap getThumb(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        retriever.getFrameAtTime();
        if (checkIsvideo(FileUtil.getFileType(path))) {
            retriever.setDataSource(path);
        }

        if (retriever.getFrameAtTime() != null) {
            return retriever.getFrameAtTime();
        } else {
            return GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.drawable.video_ic));
        }


    }

    public static int setBitampSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int scale = 1;
        if (height > reqHeight || width > reqWidth) {
            int widthRate = (int) height / reqHeight;
            int heightRate = (int) width / reqWidth;
            scale = widthRate > heightRate ? heightRate : widthRate;
        }

        return scale;

    }

    public static Bitmap getCompressBitmap(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = setBitampSize(options, 80, 80);
        BitmapFactory.decodeFile(path, options);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);


    }


    /*
    * 检查是否是视频类型文件
    *
    *
    * */
    static public boolean checkIsvideo(String type) {

        if ((type.equals("avi") || type.equals("mp4") || type.equals("wmv") || type.equals("mov") || type.equals("3gp") || type.equals("rm"))) {
            return true;
        } else {
            return false;
        }


    }


}
