package com.mobi.overseas.clearsafe.ui.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobi.overseas.clearsafe.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Email: zhousaito@163.com
 * Created by zhousaito 2019-11-15 11:28
 * Version: 1.2
 * Description: 对话框容器的 基类
 */
public abstract class BaseDialogContainer {

    public enum DialogStyle {
        //中间显示
        CENTER,
        //底下显示
        BOTTOM
        //... 后续的可以进行开发根据style
    }

    public Dialog dialog;
    protected Activity activity;
    private boolean outSide;
    private boolean cancelable;

    public BaseDialogContainer(Activity activity) {
        this(activity, false, false);
    }

    public BaseDialogContainer(Activity activity, boolean outSide, boolean cancelable) {
        this.activity = activity;
        this.outSide = outSide;
        this.cancelable = cancelable;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    public void show(DialogStyle style) {
        switch (style) {
            case BOTTOM:
                dialog = new BaseDialog(activity, R.style.BaseDialogStyleBottom) {
                    @Override
                    protected Context getActivityContext() {
                        return activity;
                    }
                };
                Window dialogWindow = dialog.getWindow();
                dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
                dialogWindow.setGravity(Gravity.BOTTOM);
                WindowManager windowManager = dialogWindow.getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = (int) (display.getWidth());
                dialogWindow.setAttributes(lp);
                break;
            default: //都是中间显示
                dialog = new Dialog(activity, R.style.BaseDialogStyleCenter);
                break;
        }

        dialog.setCanceledOnTouchOutside(outSide);
        dialog.setCancelable(cancelable);
        dialog.show();

        View view = LayoutInflater.from(activity).inflate(getLayoutId(), null);
        dialog.setContentView(view);
        onBindView(view);

        initView(view);
    }

    public void show() {
        show(DialogStyle.CENTER);
    }

    /**
     * 获取到对话框 layout
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化 view 操作
     *
     * @param view
     */
    protected abstract void initView(@NonNull View view);

    public Dialog getDialog() {
        return dialog;
    }

    /**
     * 隐藏的时候取消unBinder
     */
    protected void onDismiss() {

    }

    protected void onBindView(@NonNull View view) {

    }

}
