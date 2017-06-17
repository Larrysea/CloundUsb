package com.larry.cloundusb.cloundusb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.larry.cloundusb.R;
import com.nostra13.universalimageloader.core.ImageLoader;



/*
*    
* 项目名称：present-android      
* 类描述：查看头像大图的activity
* 创建人：Larry-sea   
* 创建时间：2017/5/29 20:32   
* 修改人：Larry-sea  
* 修改时间：2017/5/29 20:32   
* 修改备注：   
* @version    
*    
*/
public class CheckPictureActivity extends AppCompatActivity {

    PhotoView photoView;

//    String  =null;

    String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_check_portrait);
        photoView = (PhotoView) findViewById(R.id.pv_picture);
        imagePath = getIntent().getStringExtra("imgPath");
        initConfig();
        ImageLoader.getInstance().displayImage("file:///" + imagePath, photoView);

    }


    public void initConfig() {
        // 启用图片缩放功能
//        photoView.enable();
        // 禁用图片缩放功能 (默认为禁用，会跟普通的ImageView一样，缩放功能需手动调用enable()启用)
        photoView.disenable();
        // 获取图片信息
        Info info = photoView.getInfo();
        // 从普通的ImageView中获取Info
        // 从一张图片信息变化到现在的图片，用于图片点击后放大浏览，具体使用可以参照demo的使用
        photoView.animaFrom(info);
        // 从现在的图片变化到所给定的图片信息，用于图片放大后点击缩小到原来的位置，具体使用可以参照demo的使用

        // 获取/设置 动画持续时间
        photoView.setAnimaDuring(200);
        int d = photoView.getAnimaDuring();
        // 获取/设置 最大缩放倍数
        photoView.setMaxScale(2);
        float maxScale = photoView.getMaxScale();
        // 设置动画的插入器
        // photoView.setInterpolator(Interpolator interpolator);
    }
}
