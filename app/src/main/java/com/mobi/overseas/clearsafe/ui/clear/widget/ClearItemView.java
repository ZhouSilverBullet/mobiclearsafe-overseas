package com.mobi.overseas.clearsafe.ui.clear.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.clear.data.WechatBean;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;

import java.io.File;
import java.util.List;

public class ClearItemView extends FrameLayout {
    private TextView tvClear;
    private TextView tvDec;
    private CheckBox cbMemory;
    private ProgressBar pbLoad;
    private WechatBean mBean;
    private ImageView ivIcon;

    public ClearItemView(@NonNull Context context) {
        super(context);
        init();
    }

    public ClearItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        View.inflate(getContext(), R.layout.view_clear_item, this);
        tvClear = findViewById(R.id.tvClear);
        tvDec = findViewById(R.id.tvDec);
        cbMemory = findViewById(R.id.cbMemory);
        pbLoad = findViewById(R.id.pbLoad);
        ivIcon = findViewById(R.id.ivIcon);

    }

    /**
     * 先把title dec icon这三个元素设置好
     *
     * @param bean
     */
    public void initData(WechatBean bean) {
        mBean = bean;
        ivIcon.setImageResource(bean.icon);
        tvClear.setText(mBean.name);
        tvDec.setText(mBean.dec);
    }

    public synchronized void setData(WechatBean bean) {
        if (mBean == null) {
            throw new IllegalArgumentException("please init mBean call initData method !!");
        }
        //mBean不为空的时候就复制
        if (bean != null) {
            //文件添加一下，可能是多个文件
            mBean.fileList.addAll(bean.fileList);
            mBean.fileSize = mBean.fileSize + bean.fileSize;
            mBean.sizeAndUnit = FileUtil.getFileSize0(mBean.fileSize);
        }
        tvClear.setText(mBean.name);
        tvDec.setText(mBean.dec);

        //处理右边的状态
        pbLoad.setVisibility(GONE);
        cbMemory.setVisibility(VISIBLE);
        if (mBean.fileSize <= 0) {
            cbMemory.setButtonDrawable(null);
            cbMemory.setCompoundDrawables(null, null, null, null);
            cbMemory.setText("无需清理");
            cbMemory.setChecked(false);
            mBean.isCheck = false;
        } else {
            mBean.isCheck = true;
            Drawable drawable = getResources().getDrawable(R.drawable.clear_cb_selector);
            cbMemory.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//            cbMemory.setCompoundDrawables(null, null, drawable, null);
            cbMemory.setText(mBean.getFileStrSize());
            cbMemory.setChecked(true);
        }
    }

    public boolean isCheck() {
        return cbMemory.isChecked();
    }

    /**
     * 让外面去获取设置点击事件
     *
     * @return
     */
    public CheckBox getCbMemory() {
        return cbMemory;
    }

    public List<File> getFile() {
        return mBean.fileList;
    }

    public long getSize() {
        return mBean.fileSize;
    }

    public void release() {
        mBean.fileSize = 0;
    }
}
