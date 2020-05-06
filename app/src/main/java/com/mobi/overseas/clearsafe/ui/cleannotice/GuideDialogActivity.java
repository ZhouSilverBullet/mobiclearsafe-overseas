package com.mobi.overseas.clearsafe.ui.cleannotice;

import android.view.View;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.base.BaseActivity;

import butterknife.BindView;

/**
 * 引导用户开权限
 */
public class GuideDialogActivity extends BaseActivity {

    @BindView(R.id.vClick)
    View vClick;
    @BindView(R.id.ivExit)
    View ivExit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide_dialog;
    }

    @Override
    protected boolean isFitWindow() {
        return true;
    }

    @Override
    public void initEvent() {
        vClick.setOnClickListener(v -> {
            finish();
        });

        ivExit.setOnClickListener(v -> {
            finish();
        });
    }
}
