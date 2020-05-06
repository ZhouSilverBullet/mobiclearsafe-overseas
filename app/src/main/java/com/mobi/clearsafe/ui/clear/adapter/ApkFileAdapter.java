package com.mobi.clearsafe.ui.clear.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.clearsafe.ui.clear.data.GarbageHeaderBean;
import com.mobi.clearsafe.ui.clear.data.GarbageRemoveBean;
import com.mobi.clearsafe.ui.clear.data.ScanFileBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.utils.imageloader.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 10:51
 * @Dec 略
 */
public class ApkFileAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private AtomicLong selectedSize;
    private long size;

    public ApkFileAdapter(List<MultiItemEntity> data) {
        super(data);
        selectedSize = new AtomicLong(0L);

        addItemType(0, R.layout.apk_file_recycler_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case 0: {
                ScanFileBean garbageBean = (ScanFileBean) item;
                int icon = garbageBean.icon;
                ImageView ivIcon = helper.getView(R.id.ivIcon);
                ImageView ivMovieIcon = helper.getView(R.id.ivMovieIcon);

                TextView tvClear = helper.getView(R.id.tvClear);
                tvClear.setText(garbageBean.name);

                ivMovieIcon.setVisibility(View.GONE);
                if (garbageBean.garbageType > 4) {
                    ImageLoader.loadCenterCropImage(ivIcon.getContext(), ivIcon, garbageBean.fileList.get(0).getPath());
                    if (garbageBean.garbageType == ScanFileBean.TYPE_MOVIE) {
                        ivMovieIcon.setVisibility(View.VISIBLE);
                    }
                } else {
                    ivIcon.setImageDrawable(garbageBean.imageDrawable);
                }


                CheckBox cbMemory = helper.getView(R.id.cbMemory);
//                cbMemory.setText(garbageBean.getFileStrSize());

                cbMemory.setOnCheckedChangeListener(null);
                cbMemory.setChecked(garbageBean.isCheck);
                cbMemory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    garbageBean.isCheck = isChecked;

                    notifyDataSetChanged();

                    if (isChecked) {
                        selectedSize.set(selectedSize.get() + garbageBean.fileSize);
                    } else {
                        selectedSize.set(selectedSize.get() - garbageBean.fileSize);
                    }

                    if (checkedListener != null) {
                        String[] fileSize01 = FileUtil.getFileSize0(selectedSize.get());
                        checkedListener.onAllChecked(selectedSize.get(), fileSize01);
                    }
                });

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cbMemory.setChecked(!cbMemory.isChecked());
                    }
                });

                TextView tvDec = helper.getView(R.id.tvDec);
                tvDec.setText(garbageBean.getFileStrSize());

                TextView tvInstalled = helper.getView(R.id.tvInstalled);
                if (garbageBean.isInstalled) {
                    tvInstalled.setText("已经安装");
                    tvInstalled.setTextColor(mContext.getResources().getColor(R.color.black_99));
                } else {
                    tvInstalled.setText("未安装");
                    tvInstalled.setTextColor(mContext.getResources().getColor(R.color.c_328AFF));
                }

            }

            break;
        }

    }

    private IGAdapterCheckedListener checkedListener;

    public void setCheckedListener(IGAdapterCheckedListener checkedListener) {
        this.checkedListener = checkedListener;
    }

    /**
     * 点击选中全部
     */
    public long checkAll() {
        if (size == selectedSize.get()) {
            for (MultiItemEntity datum : getData()) {
                if (datum instanceof ScanFileBean) {
                    ((ScanFileBean) datum).isCheck = false;
                }
            }
            selectedSize.set(0);
        } else {
            for (MultiItemEntity datum : getData()) {
                if (datum instanceof ScanFileBean) {
                    ((ScanFileBean) datum).isCheck = true;
                }
            }
            selectedSize.set(size);
        }

        notifyDataSetChanged();
        return selectedSize.get();
    }

    public List<File> findClearFile() {
        List<File> pathList = new ArrayList<>();
        for (MultiItemEntity datum : getData()) {
            if (datum instanceof ScanFileBean) {
                ScanFileBean bean = (ScanFileBean) datum;
                if (bean.isCheck) {
                    pathList.addAll(bean.fileList);
                }
            }
        }
        //替换成这个
        return pathList;
    }

    public interface IGAdapterCheckedListener {

        void onAllChecked(long allSize, String[] data);

//        void onSingleChecked(long allSize, String[] data);

    }

    public void setSelectedSize(long allSize) {
        this.selectedSize.set(allSize);
    }

    public AtomicLong getSelectedSize() {
        return selectedSize;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
