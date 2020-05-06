package com.mobi.overseas.clearsafe.ui.clear.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialogContainer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/14 16:06
 * @Dec 略
 */
public class CleanDialogWarp extends BaseDialogContainer {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.btnQuit)
    Button btnQuit;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    private String title;
    private String content;
    private String leftButton;
    private String rightButton;

    private View.OnClickListener leftListener;
    private View.OnClickListener rightListener;
    private Unbinder mBind;

    public CleanDialogWarp(Builder builder) {
        super(builder.mActivity, true, true);
        title = builder.title;
        content = builder.content;
        leftButton = builder.leftButton;
        rightButton = builder.rightButton;

        leftListener = builder.leftListener;
        rightListener = builder.rightListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.clean_dialog_view;
    }

    @Override
    protected void onBindView(@NonNull View view) {
        mBind = ButterKnife.bind(this, view);
    }

    @Override
    protected void onDismiss() {
        if (mBind != null) {
            mBind.unbind();
        }
    }

    @Override
    protected void initView(@NonNull View view) {
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftListener != null) {
                    leftListener.onClick(v);
                }
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightListener != null) {
                    rightListener.onClick(v);
                }
                dismiss();
            }
        });

        tvTitle.setText(title);
        tvContent.setText(content);
        btnQuit.setText(leftButton);
        btnConfirm.setText(rightButton);

    }

    public static class Builder {
        private Activity mActivity;

        private String title = "停止扫描";
        private String content = "正在扫描中，确认要停止吗？";
        private String leftButton = "停止扫描";
        private String rightButton = "继续扫描";

        boolean isOutSide = true;
        boolean isCancelable = true;

        private View.OnClickListener leftListener;
        private View.OnClickListener rightListener;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public Builder setLeftButton(String leftButton) {
            this.leftButton = leftButton;
            return this;
        }

        public Builder setRightButton(String rightButton) {
            this.rightButton = rightButton;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            isCancelable = cancelable;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setOutSide(boolean outSide) {
            isOutSide = outSide;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setLeftListener(View.OnClickListener leftListener) {
            this.leftListener = leftListener;
            return this;
        }

        public Builder setRightListener(View.OnClickListener rightListener) {
            this.rightListener = rightListener;
            return this;
        }

        public CleanDialogWarp build() {
            return new CleanDialogWarp(this);
        }
    }
}
