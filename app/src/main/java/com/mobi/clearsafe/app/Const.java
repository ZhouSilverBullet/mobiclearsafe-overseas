package com.mobi.clearsafe.app;

import com.example.adtest.manager.Constants;
import com.mobi.clearsafe.BuildConfig;
import com.mobi.clearsafe.main.PleasantlyBean;
import com.mobi.clearsafe.main.bean.ExitDialogBean;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

public class Const {

    public static String deviceID = "";
    
    /**
     * DEBUG :是用来控制，第三方插件等是否debug状态
     */
    public static boolean DEBUG = Constants.DEBUG;//true 测试服地址 false 正式服地址

    /**
     * umeng使用这个
     * 因为测试的时候，访问接口有问题
     */
    public static boolean UMENG_DEBUG = BuildConfig.DEBUG;//true 测试服地址 false 正式服地址


    public static final String DEBUGUEL="http://devclean.quanminzoulu.com";//测试服配置表域名
    public static final String RELEASEURL="http://clean.quanminzoulu.com";//正式服配置表域名



    public static String getBaseUrl() {
        return Const.DEBUG ? UserEntity.getInstance().getConfigEntity().getDev_domain() : UserEntity.getInstance().getConfigEntity().getPrd_domain();
    }

    public static int SYSTEM_ID = 1;
    public static int SIGNIG_ACTIVITY_ID = 1;//签到活动ID
    public static int INTEGRAL_ACTIVITY_ID = 1;


//    活动列表->活动ID记录
    public static final int ACTIVITY_LOOK_AD_ID = 5;//观看视频广告ID
    public static final int ACTIVITY_ROTARY_AD = 1;//跳转到大转盘ID
    public static final int ACTIVITY_BIND_PHONE_ID = 3;//绑定手机号活动ID
    public static final int ACTIVITY_BING_WX_ID = 4;//绑定微信活动ID
    public static final int ACTIVITY_PLEASANTLY_id = 14;//
    public static final int ACTIVITY_FIRSTWITHDRAWAL_id = 15;//提现活动活动id


    //热云appkey  5dc0df783fc19536ef0005e6
    public static final String RYAPPKEKY="1262048a86f75bbfb1456ba7ea08ecc8";
    public static String getRYAPPkey() {
        return  RYAPPKEKY;
    }

    public static PleasantlyBean pBean = null;//惊喜奖励显示在哪个tab
    public static ExitDialogBean exitBean = null;//退出app时弹框信息


    /**
     * 5分钟
     */
    public static final long CURRENT_MINUTE = 5 * 60 * 1000;

    /**
     * sp 是否下次需要清理的时长
     */
    public static final String GARBAGE_CAN_CLEAR  = "garbage_can_clear";
    public static final String WECHAT_CAN_CLEAR  = "wechat_can_clear";
    public static final String QQ_CAN_CLEAR  = "qq_can_clear";
    public static final String POWER_CAN_CLEAR  = "power_can_clear";
    public static final String SPEED_CAN_CLEAR  = "speed_can_clear";
    /**
     * 内存临时存的值，方便后面直接跳转进来的时候获取最近的值
     */
    public static final String SPEED_TEMP_VALUE  = "speed_temp_value";

    /**
     * 临时存储分数
     */
    public static final String HOME_POINT_TEMP_VALUE  = "home_point_temp_value";
    public static final String HOME_POINT_TEMP_STATUS  = "home_point_temp_status";

    /**
     * app widget 被添加到了桌面上
     */
    public static final String APP_WIDGET_ENABLE  = "app_widget_enable";
}
