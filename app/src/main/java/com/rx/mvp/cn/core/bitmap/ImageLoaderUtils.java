package com.rx.mvp.cn.core.bitmap;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rx.mvp.cn.R;


/**
 * ImageLoaderUtils
 *
 * @author ZhongDaFeng
 */
public class ImageLoaderUtils {

    public static void load(Context context, String url, ImageView imageView) {
        if (context != null) {
            Glide.with(context).load(url).placeholder(R.mipmap.ic_launcher).dontAnimate().error(R.mipmap.ic_launcher).into(imageView);
        }
    }

}
