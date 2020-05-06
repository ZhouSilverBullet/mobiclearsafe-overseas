package com.mobi.clearsafe.ui.clear.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.clearsafe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/9 14:54
 * @Dec 略
 */
public class HomeClearItemView extends FrameLayout {

    private final Drawable mDrawable;
    private final String hcivTitle;
    private final String hcivContent;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvContent)
    TextView tvContent;

    public HomeClearItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HomeClearItemView, 0, 0);
        try {
            mDrawable = a.getDrawable(R.styleable.HomeClearItemView_hcivSrc);
            hcivTitle = a.getString(R.styleable.HomeClearItemView_hcivTitle);
            hcivContent = a.getString(R.styleable.HomeClearItemView_hcivContent);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_clear_item, this, true);
        ButterKnife.bind(this);

        ivIcon.setImageDrawable(mDrawable);
        tvTitle.setText(hcivTitle);
        tvContent.setText(hcivContent);
    }


}
