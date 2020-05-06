package com.mobi.overseas.clearsafe.ui.clear.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.clear.ad.ADRelativeLayout;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageRemoveBean;
import com.mobi.overseas.clearsafe.ui.clear.data.ScanFileBean;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
import com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 10:51
 * @Dec 略
 */
public class BigFileAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private AtomicLong selectedSize;
    private long size;

    private Set<Integer> expendPositionSet = new HashSet<>();

    public BigFileAdapter(List<MultiItemEntity> data) {
        super(data);
        selectedSize = new AtomicLong(0L);

        addItemType(0, R.layout.garbage_app_scan_item);
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
                cbMemory.setText(garbageBean.getFileStrSize());

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
                tvDec.setText(garbageBean.dec);
            }

            break;
        }

    }

    /**
     * 收缩全部item
     */
    public void collapseAll() {
        for (Integer integer : expendPositionSet) {
            collapse(integer + getHeaderLayoutCount(), false);
        }

        notifyDataSetChanged();
    }

    private IGAdapterCheckedListener checkedListener;

    public void setCheckedListener(IGAdapterCheckedListener checkedListener) {
        this.checkedListener = checkedListener;
    }

    /**
     * 迭代删除 没有数据的
     *
     * @param garbageHeaders
     */
    public boolean removeEmptyAndReplaceData(List<MultiItemEntity> garbageHeaders) {
        for (ListIterator<MultiItemEntity> iterator = garbageHeaders.listIterator();
             iterator.hasNext(); ) {
            MultiItemEntity entity = iterator.next();
            if (entity instanceof GarbageHeaderBean) {
                if (((GarbageHeaderBean) entity).getSubItems().isEmpty()) {
                    iterator.remove();
                }
            }
        }
        replaceData(garbageHeaders);
        return garbageHeaders.isEmpty();
    }

    /**
     * 点击删除按钮的时候，然后显示删除的界面逻辑
     *
     * @return
     */
    public List<File> changeToRemoveDataIsEmpty() {
        List<GarbageRemoveBean> beanList = new ArrayList<>();
        List<File> pathList = new ArrayList<>();
        for (MultiItemEntity datum : getData()) {
            if (datum instanceof GarbageHeaderBean) {
                List<GarbageBean> subItems = ((GarbageHeaderBean) datum).getSubItems();
                for (GarbageBean subItem : subItems) {
                    if (subItem.isCheck) {
                        GarbageRemoveBean garbageRemoveBean = new GarbageRemoveBean();
                        garbageRemoveBean.itemType = 3;
                        garbageRemoveBean.name = subItem.name;
                        garbageRemoveBean.imageDrawable = subItem.imageDrawable;
                        garbageRemoveBean.dec = "建议清理";
                        beanList.add(garbageRemoveBean);

                        pathList.addAll(subItem.fileList);
                    }
                }
            }
        }
        //替换成这个
        replaceData(beanList);
        return pathList;
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

    public interface IGAdapterCheckedListener {

        void onAllChecked(long allSize, String[] data);

//        void onSingleChecked(long allSize, String[] data);

    }

    public AtomicLong getDataAllSize() {
        return selectedSize;
    }
}
