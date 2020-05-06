package com.mobi.overseas.clearsafe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;

/**
 * author : liangning
 * date : 2019-10-23  15:24
 */
public class UiUtils {

    /**
     * 设置标题栏 无右侧设置
     *
     * @param activity
     * @param leftStr
     * @param leftClick
     * @param title
     * @param titleClick
     */
    public static void setTitleBar(Activity activity, String leftStr,
                                   View.OnClickListener leftClick,
                                   String title, View.OnClickListener titleClick) {
        if (activity == null) return;
        try {

            LinearLayout ll_back = activity.findViewById(R.id.ll_back);

            if (!TextUtils.isEmpty(leftStr)) {
                TextView tv_back = activity.findViewById(R.id.tv_back);
                tv_back.setText(leftStr);
                tv_back.setVisibility(View.VISIBLE);
            }
            if (leftClick != null) {
                ll_back.setOnClickListener(leftClick);
            }
            if (!TextUtils.isEmpty(title)) {
                TextView tv_title = activity.findViewById(R.id.tv_title);
                tv_title.setText(title);
                if (titleClick != null) {
                    tv_title.setOnClickListener(titleClick);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题栏 包括右侧
     *
     * @param activity
     * @param leftStr
     * @param leftClick
     * @param title
     * @param titleClick
     * @param rightStr
     * @param rightClick
     */
    public static void setTitleBar(Activity activity, String leftStr,
                                   View.OnClickListener leftClick,
                                   String title, View.OnClickListener titleClick,
                                   String rightStr, View.OnClickListener rightClick) {
        if (activity == null) return;
        try {
            if (!TextUtils.isEmpty(rightStr)) {
                TextView tv_right = activity.findViewById(R.id.tv_right);
                tv_right.setVisibility(View.VISIBLE);
                tv_right.setText(rightStr);
            }
            if (rightClick != null) {
                LinearLayout ll_right = activity.findViewById(R.id.ll_right);
                ll_right.setOnClickListener(rightClick);
            }
            setTitleBar(activity, leftStr, leftClick, title, titleClick);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************
     *
     * 发起添加群流程。群号：全民走路(954106426) 的 key 为： Thqz2kzdWqwhBThk8Zq54iUaqdBl2UMp
     * 调用 joinQQGroup(Thqz2kzdWqwhBThk8Zq54iUaqdBl2UMp) 即可发起手Q客户端申请加群 全民走路(954106426)
     *
     * @param QQGroupKey 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Activity activity,String QQGroupKey) {
//        String key = "Thqz2kzdWqwhBThk8Zq54iUaqdBl2UMp";
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + QQGroupKey));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
           activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    /**
     * 获取屏内容
     */
    public static DisplayMetrics getScreen(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics;
    }

    /**
     * dip转换px
     */
    public static int dp2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public static int px2dp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
