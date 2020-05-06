package com.mobi.overseas.clearsafe.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.mobi.overseas.clearsafe.R;

import java.util.Map;

/**
 * author : liangning
 * date : 2019-10-17  18:09
 */
public class LoadWebView extends WebView {

    private WebViewListener mListener = null;

    private ProgressBar mProgressBar;

    private Context mContext;
    public LoadWebView(Context context) {
        super(getFixedContext(context));
        this.mContext = context;
        init();
    }

    public LoadWebView(Context context, AttributeSet attrs) {
        super(getFixedContext(context), attrs);
        this.mContext=context;
        init();
    }

    public LoadWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(getFixedContext(context), attrs, defStyleAttr);
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(getFixedContext(context), attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        init();
    }

    private static Context getFixedContext(Context context) {
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) // Android Lollipop 5.0 & 5.1
            return context.createConfigurationContext(new Configuration());
        return context;
    }

    private void init() {
        mProgressBar = new ProgressBar(mContext, null,
                android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2);
        mProgressBar.setLayoutParams(layoutParams);
        Drawable drawable = mContext.getResources().getDrawable(
                R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        WebSettings settings = this.getSettings();

        //开启自动化测试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }
        //自定义UA
        String userAgent = settings.getUserAgentString();
        userAgent = userAgent + "mobistep";
        settings.setUserAgentString(userAgent);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            String ua = settings.getUserAgentString();
            String afterua = ua.replaceAll("Chrome", "Ghrome");
            settings.setUserAgentString(afterua);
        }

        //在5。0之后 开启加载允许http于https混合内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //开启自动播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        settings.setJavaScriptEnabled(true);//允许执行JS脚本
        settings.setSupportZoom(true);//是否支持缩放
        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        settings.setBuiltInZoomControls(false);//是否使用内部缩放机制
        settings.setDisplayZoomControls(false);//是否显示缩放控件
        settings.setUseWideViewPort(true);//支持HTML viewport 标签
        settings.setLoadWithOverviewMode(true);//是否允许WebView度超出以概览的方式载入页面,默认false
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(false);//关闭缓存
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                } catch (Exception e) {

                }

            }
        });
        addJavascriptInterface(new AndroidJavaScript(),"step");
        setWebViewClient(webViewClient);
        setWebChromeClient(chromeClient);
    }

    /**
     * 加载网页 添加头信息
     * @param url
     * @param headers
     */
    public void loadUrlAddHeaders(String url, Map<String,String> headers){
        this.loadUrl(url,headers);
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    /**
     * 设置回调
     * @param listener
     */
    public void setWebViewListener(WebViewListener listener){
        this.mListener = listener;
    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    };

    WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    };
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public class AndroidJavaScript {

        /**
         * 显示领取奖励弹框
         * @param s 获得的金币数量
         */
        @JavascriptInterface
        public void showRewardDialog(String s) {
            if (mListener!=null){
                mListener.showRewardDialog(s);
            }
        }

        /**
         * 打开一个新的webview界面
         * @param url
         */
        @JavascriptInterface
        public void openNewWebview(String url){
            if (mListener!=null){
                mListener.openNewWebview(url);
            }
        }

        /**
         * 设置webview 页面标题
         * @param s
         */
        @JavascriptInterface
        public void setTitle(String s){
            if (mListener!=null){
                mListener.setTitle(s);
            }
        }

        /**
         * 增加步数
         * @param step
         */
        @JavascriptInterface
        public void addStep(int step){
            if (mListener!=null){
                mListener.addStep(step);
            }
        }

        /**
         * 设置步数
         * @param step 当前所有步数
         */
        @JavascriptInterface
        public void setAllStep(int step){
            if (mListener!=null){
                mListener.setAllStep(step);
            }
        }

        /**
         * 增加金币
         * @param gold
         */
        @JavascriptInterface
        public void addGold(int gold){
            if (mListener!=null){
                mListener.addGold(gold);
            }
        }

        /**
         * 设置金币数量
         * @param gold 当前拥有的所有金币数量
         */
        @JavascriptInterface
        public void setAllGold(int gold){
            if (mListener!=null){
                mListener.setAllGold(gold);
            }
        }

        /**
         * 打开其他APP
         * @param intentStr
         */
        @JavascriptInterface
        public void openOtherApp(String intentStr){
            if (mListener!=null){
                mListener.openOtherApp(intentStr);
            }
        }

        /**
         * 关闭当前界面
         */
        @JavascriptInterface
        public void closeThisActivity(int s){
            if (mListener!=null){
                mListener.closeThisActicity(s);
            }
        }

        /**
         * 弹出视频广告
         * @param s
         */
        @JavascriptInterface
        public void showAd(String s){
            if (mListener!=null){
                mListener.showAd(s);
            }
        }

        /**
         * 弹出插屏
         * @param s
         */
        @JavascriptInterface
        public void showPlaqueAdvert(String s){
            if (mListener!=null){
                mListener.showPlaqueAdvert(s);
            }
        }

        /**
         * 初始化广告分组
         */
        @JavascriptInterface
        public void InitializeAD(String s) {
            if (mListener != null) {
                mListener.InitializeAD(s);
            }
        }

        /**
         * 设置页面是否可关闭
         * @param s
         */
        @JavascriptInterface
        public void setCanFinish(String s){
            if (mListener!=null){
                mListener.setCanFinish(s);
            }
        }

        /**
         * 打开新的页面
         * @param s
         */
        @JavascriptInterface
        public void openNewActivity(String s){
            if (mListener!=null){
                mListener.openNewActivity(s);
            }
        }
    }

    public interface WebViewListener{
        void showRewardDialog(String s);
        void showAd(String s);
        void openNewWebview(String url);
        void setTitle(String s);
        void addStep(int step);
        void addGold(int gold);
        void openOtherApp(String intentStr);
        void closeThisActicity(int s);
        void setAllGold(int gold);
        void setAllStep(int step);
        void showPlaqueAdvert(String type);
        void InitializeAD(String s);
        void setCanFinish(String s);
        void openNewActivity(String s);
    }

}
