package com.example.chenjunmei.myscrollview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    private Banner banner;

    //准备的本地的标题集合
    String[] titles = new String[]{"《神奇动物》中文终极预告", "《罗曼蒂克消亡史》预告", "《承诺》首款预告", "《娃娃老板》预告片"};

    //准备图片url的集合
    String[] imgUrlStrs = new String[]{
            "http://img5.mtime.cn/mg/2016/09/29/101927.61748190.jpg",
            "http://img5.mtime.cn/mg/2016/10/17/172246.85687810.jpg",
            "http://img31.mtime.cn/mg/2016/09/10/140500.21315060.jpg",
            "http://img5.mtime.cn/mg/2016/10/18/102203.49333729.jpg"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> imageUrls = new ArrayList<String>();
        //设置图片url集合
        for (int i = 0; i < imgUrlStrs.length; i++) {
            imageUrls.add(imgUrlStrs[i]);
        }

        Banner banner = (Banner) findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageUrls);
        //设置banner动画效果
        banner.setBannerAnimation(com.youth.banner.Transformer.CubeOut);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.LEFT);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picacco加载图片简单用法
            Picasso.with(MainActivity.this).load((String) path).into(imageView);
        }
    }
}
