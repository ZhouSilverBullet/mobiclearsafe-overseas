package com.mobi.overseas.clearsafe.wxapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mobi.overseas.clearsafe.app.MyApplication;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * author:zhaijinlu
 * date: 2019/10/24
 * desc:
 */
public class WeixinHandler {
    public static final String APPID = WXEntryActivity.APPID;

    private static WeixinHandler sInstance;
    private IWXAPI api;
    //  private Listener mListener;


    public static WeixinHandler getInstance() {
        if (sInstance == null) {
            sInstance = new WeixinHandler();
        }
        return sInstance;
    }

    public boolean isWeixinInstalled() {
        try {
            IWXAPI api = createWXApiInstance();
            return api.isWXAppInstalled();//&& api.isWXAppSupportAPI()
        } catch (Exception e) {
        }
        return false;
    }

    public IWXAPI createWXApiInstance() {
        api = WXAPIFactory.createWXAPI(MyApplication.getContext(), APPID, true);
        api.registerApp(APPID);
        return api;
    }

    /**
     * 登录微信
     */
    public void loginWeChat() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
    }


    /**
     * 微信分享
     *
     * @param title
     * @param text
     * @param link
     * @param thumbIconResid
     * @param toTimeline
     */
    public void shareToWeixin(String title, String text, String link, int thumbIconResid, boolean toTimeline) {
        try {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = link;

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = webpage;
            msg.title = title;
            msg.description = text;
            msg.thumbData = getThumbnailByte(createThumbnail(thumbIconResid, 100, 100), 32);


            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = "webpage" + System.currentTimeMillis();
            req.message = msg;
            req.scene = toTimeline ? SendMessageToWX.Req.WXSceneTimeline
                    : SendMessageToWX.Req.WXSceneSession;
            IWXAPI api = createWXApiInstance();
            if (toTimeline) {
                api.openWXApp();
            }
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Bitmap createThumbnail(int resId, int maxWidth, int maxHeight) {
        if (maxWidth <= 0 || maxHeight <= 0) {
            return null;
        }
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(MyApplication.getContext().getResources(), resId, opt);
            opt.inSampleSize = 1;
            int targetWidth = opt.outWidth;
            int targetHeight = opt.outHeight;
            while (targetWidth > maxWidth || targetHeight > maxHeight) {
                opt.inSampleSize++;
                targetWidth = opt.outWidth / opt.inSampleSize;
                targetHeight = opt.outHeight / opt.inSampleSize;
            }
            opt.inJustDecodeBounds = false;
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeResource(MyApplication.getContext().getResources(), resId, opt);
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] getThumbnailByte(Bitmap bitmap, int size) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int maxSize = size * 1024; // 32K, max size of thumbnail shared to Weixin
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        int aa = output.toByteArray().length;
        int options = 100;
        while (output.toByteArray().length > maxSize && options != 10) {
            output.reset(); //清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;
        }
        bitmap.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
