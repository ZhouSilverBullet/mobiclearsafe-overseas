//package com.mobi.overseas.clearsafe.fragment;
//
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.baidu.mobads.CpuInfoManager;
//import com.mobi.overseas.clearsafe.R;
//import com.mobi.overseas.clearsafe.app.Const;
//import com.mobi.overseas.clearsafe.eventbean.PleasanEvent;
//import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
//import com.mobi.overseas.clearsafe.utils.AppUtil;
//import com.mobi.overseas.clearsafe.utils.BaiDuRSA;
//import com.mobi.overseas.clearsafe.utils.StatusBarUtil;
//import com.mobi.overseas.clearsafe.widget.LazyLoadFragment;
//import com.mobi.overseas.clearsafe.widget.LoadWebView;
//import com.mobi.overseas.clearsafe.widget.LoadWebViewActivity;
//import com.umeng.analytics.MobclickAgent;
//
//import org.greenrobot.eventbus.EventBus;
//
///**
// * author:zhaijinlu
// * date: 2019/12/12
// * desc:
// */
//public class BdFragment extends LazyLoadFragment {
//
//    private static String DEFAULT_APPID = "e94da656";
//    private LoadWebView mWebView;
//
//    @Override
//    protected int setContentView() {
//        return R.layout.bd_fragment_layout;
//    }
//
//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart("BdFragment");
//        if (mWebView != null) {
//            mWebView.onResume();
//            getView().setFocusableInTouchMode(true);
//            getView().requestFocus();
//            getView().setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
//                        // 监听到返回按钮点击事件
//                        if ( mWebView.canGoBack()) {
//                            mWebView.goBack();
//                            return true;
//                        }
//                    }
//                    return false;
//                }
//            });
//        }
//
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("BdFragment");
//        if (mWebView != null) {
//            mWebView.onPause();
//        }
//
//    }
//    @Override
//    protected void lazyLoad() {
//        ButtonStatistical.bdCount();
//        StatusBarUtil.setStatusBarMode(getActivity(), true, R.color.white);
//        if (Const.pBean != null) {
//            if (Const.pBean != null && Const.pBean.show_page == 2) {
//                EventBus.getDefault().post(new PleasanEvent(Const.pBean.show_page));
//            }
//        }
//
//    }
//
//    @Override
//    protected void firstIn() {
//        initWebView();
//        String url="https://cpu.baidu.com/1032/e94da656?scid=50390&im="
//                + BaiDuRSA.encrypt(AppUtil.getIMEI())
//                +"&imMd5="+BaiDuRSA.encrypt(AppUtil.getIMEI())
//                +"&aid="+BaiDuRSA.encrypt(AppUtil.getAndroidID());
//        mWebView.loadUrl("https://cpu.baidu.com/1032/e94da656?scid=50390&im="
//                + BaiDuRSA.encrypt(AppUtil.getIMEI())
//                +"&imMd5="+BaiDuRSA.encrypt(AppUtil.getIMEI())
//                +"&aid="+BaiDuRSA.encrypt(AppUtil.getAndroidID()));
////        Log.e("bdurl","https://cpu.baidu.com/1032/e94da656?scid=50390&im="
////                + BaiDuRSA.encrypt(AppUtil.getIMEI())
////                +"&imMd5="+BaiDuRSA.encrypt(AppUtil.getIMEI()))
////                +"&aid="+BaiDuRSA.encrypt("Android"));
//        // showSelectedCpuWebPage();
//
//
//    }
//
//
//    /**
//     * 调用SDK接口，获取内容联盟页面URL
//     */
//    private void showSelectedCpuWebPage() {
//        // 内容联盟url获取后只能展示一次，多次展示需要每次通过以下接口重新获取
//        // 媒体伙伴必须在MSSP业务端选择接入内容联盟的应用与频道类型，以便在接入内容页中生成广告，从而获得广告收益。
//        // 不进行相关操作，将无法获得内容联盟页面的广告收益。
//        CpuInfoManager.getCpuInfoUrl(getActivity(), getAppid(), 1032, new CpuInfoManager.UrlListener() {
//
//            @Override
//            public void onUrl(String url) {
//                Log.e("bdurl",url+"?scid=50390");
//                mWebView.loadUrl(url+"?scid=50390");
//            }
//        });
//
//    }
//
//    /**
//     * 获取appsid
//     *
//     * @return
//     */
//    private String getAppid() {
//        return DEFAULT_APPID;
//    }
//
//
//
//
//    /**
//     * 初始化展示内容联盟页面的webview
//     */
//    private void initWebView() {
//        mWebView =findViewById(R.id.bd_wv);
//        mWebView.setVerticalScrollBarEnabled(false);
//        mWebView.setHorizontalScrollBarEnabled(false);
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        // 如果是图片频道，则必须设置该接口为true，否则页面无法展现
//        webSettings.setDomStorageEnabled(true);
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                 if(url.contains("detail")||url.contains("cpro")){
//                    LoadWebViewActivity.intoBddetails(getContext(), url);
//                }else {
//                    view.loadUrl(url);
//                }
//                return true;
//            }
//        });
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mWebView != null) {
//            mWebView.stopLoading();
//            mWebView.setWebChromeClient(null);
//            mWebView.setWebViewClient(null);
//            mWebView.destroy();
//            mWebView = null;
//        }
//    }
//}
