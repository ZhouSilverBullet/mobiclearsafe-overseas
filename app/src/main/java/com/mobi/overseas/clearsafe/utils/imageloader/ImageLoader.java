package com.mobi.overseas.clearsafe.utils.imageloader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.utils.imageloader.transformation.BlurTransformation;
import com.mobi.overseas.clearsafe.utils.imageloader.transformation.CircleWithBorderTransformation;

/**
 * author : liangning
 * date : 2019-10-17  15:03
 */
public class ImageLoader {


    private static int placeHolderImage = R.color.c_eeeff0;
    private static int errorImage = R.color.c_eeeff0;

    /**
     * 普通加载图片形式
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void loadImage(Context context, ImageView iv, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(placeHolderImage)
                .error(errorImage)
                .into(iv);
    }

    public static void loadImage(Context context, ImageView iv, Drawable drawable){
        Glide.with(context)
                .load(drawable)
                .placeholder(placeHolderImage)
                .error(errorImage)
                .into(iv);
    }

    /**
     * 普通加载图片形式
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void loadCenterCropImage(Context context, ImageView iv, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(placeHolderImage)
                .centerCrop()
                .error(errorImage)
                .into(iv);
    }

    /**
     * 加载默认圆角图片 圆角 8
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void loadDefultRoundImage(Context context, ImageView iv, String url) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(8))
                .placeholder(placeHolderImage)
                .error(errorImage);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }


    /**
     * 加载圆角图片
     *
     * @param context
     * @param iv
     * @param url
     * @param round
     */
    public static void loadRoundImage(Context context, ImageView iv, String url, int round) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(round))
                .placeholder(placeHolderImage)
                .error(errorImage);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void loadCircleImage(Context context, ImageView iv, String url) {
        RequestOptions options = RequestOptions
                .circleCropTransform()
                .placeholder(placeHolderImage)
                .error(errorImage);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    /**
     * 加载圆形带边框图片
     *
     * @param context
     * @param iv
     * @param url
     * @param borderWidth
     * @param borderColor
     */
    public static void loadCircleWithFrameImage(Context context, ImageView iv,
                                                String url, int borderWidth, int borderColor) {

        RequestOptions options = RequestOptions
                .bitmapTransform(new CircleWithBorderTransformation(borderWidth, borderColor))
                .placeholder(placeHolderImage)
                .error(errorImage);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);

    }

    /**
     * 加载图片 模糊处理
     *
     * @param context
     * @param iv
     * @param url
     * @param radius  模糊半径0～25
     */
    public static void loadBlurImage(Context context, ImageView iv, String url, int radius) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new BlurTransformation(context, radius))
                .placeholder(placeHolderImage)
                .error(errorImage);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

}
