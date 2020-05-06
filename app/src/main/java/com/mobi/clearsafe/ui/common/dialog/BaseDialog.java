package com.mobi.clearsafe.ui.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mobi.clearsafe.ui.common.Bugs;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/28 17:01
 * @Dec 出现了2个bug，容易奔溃
 */
public abstract class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        if (!isInvalidContext()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing() && !isInvalidContext()) {
            super.dismiss();
        }
    }

    @Bugs(arrValue = {"隐藏掉Dialog的时候：Caused by: java.lang.IllegalArgumentException: View=DecorView@ff712db[PowerCoolActivity] not attached to window manager"
            , "显示Dialog的时候：android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@8ce8ad0 is not valid; is your activity running?"})
    protected final boolean isInvalidContext() {
        //默认getContext()获取到的是ContextThemeWrapper对象，基本不会走这个方法
        if (getContext() instanceof Activity) {
            Activity context = (Activity) getContext();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return (context.isFinishing()
                        || context.isDestroyed());
            }
            return context.isFinishing();
        }

        //判断activity是否还在
        if (getActivityContext() instanceof Activity) {
            Activity activityContext = (Activity) getActivityContext();

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return (activityContext.isFinishing()
                        || activityContext.isDestroyed());
            }
            return activityContext.isFinishing();
        }

        return true;
    }

    /**
     * 用于判断Invalid的，主要直接getContext得到的是ContextThemeWrapper对象
     * 并非是Activity对象，这样判断 #isInvalidContext() 这个方法就有问题了，永远进不去了
     * 就一直有效了
     *
     * @return
     */
    protected abstract Context getActivityContext();
}
