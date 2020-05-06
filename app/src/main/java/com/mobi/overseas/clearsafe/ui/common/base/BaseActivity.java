package com.mobi.overseas.clearsafe.ui.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.mobi.overseas.clearsafe.MainActivity;
import com.mobi.overseas.clearsafe.WelcomeActivity;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.ui.common.dialog.LoadingDialog;
import com.mobi.overseas.clearsafe.ui.common.mvp.IMvpView;
import com.mobi.overseas.clearsafe.ui.common.util.StatusBarPaddingUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/31 17:28
 * @Dec 略
 */
public abstract class BaseActivity extends BaseAppCompatActivity implements IInitView, IViewLayout, IMvpView {

    private Unbinder bind;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFitWindow()) {
            fitWindow();
        }

        setContentView(getLayoutId());

        StatusBarPaddingUtil.statusBar(getActivity(), true);
        bind = ButterKnife.bind(this);

        initVariables();
        initView();
        initData();
        initEvent();
    }

    public void initVariables() {

    }

    public void initView() {

    }

    public void initData() {

    }

    public void initEvent() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    private void fitWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 默认不打开
     *
     * @return
     */
    protected boolean isFitWindow() {
        return false;
    }

    //要抽取
    public static void setDarkStatusIcon(Window window, boolean bDark) {
        StatusBarPaddingUtil.setDarkStatusIcon(window, bDark);
    }

    /**
     * 展示对话框 不可取消
     */
    public void showProgressDialog() {
        showProgressDialog(true);
    }

    /**
     * 展示对话框 不可取消
     */
    public void showProgressDialog(boolean isCancel) {
        showProgressDialog(isCancel, null);
    }

    /**
     * 展示对话框 不可取消
     */
    public void showProgressDialog(boolean isCancel, String message) {
        if (isFinishing()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(this);
            }
            mLoadingDialog.setCancelable(isCancel);
            mLoadingDialog.setMessage(message);
            mLoadingDialog.show();
        }
    }

    protected void closeProgressDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public boolean isViewFinishing() {
        return isFinishing();
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 如果栈里面没有其他activity，证明是直接打开的activity需要重新打开app
     *
     * @param context
     */
    public void openSplashActivity(Context context) {
        /*if (MyApplication.getContext() != null) {
            int indexOf = MyApplication.getApplication().getActivityList().indexOf(this);
            if (indexOf == 0) {
                Intent intent = new Intent(context, WelcomeActivity.class);
                context.startActivity(intent);
            }
        } else */
        if (!MyApplication.getApplication().isRun()) {
            Intent intent = new Intent(context, WelcomeActivity.class);
            context.startActivity(intent);
        }
    }
}
