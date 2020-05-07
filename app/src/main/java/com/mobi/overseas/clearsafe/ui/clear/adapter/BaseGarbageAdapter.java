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
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.clear.ad.ADRelativeLayout;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageRemoveBean;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;

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
public class BaseGarbageAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public AtomicLong dataAllSize;
    /**
     * 加载结束
     */
    public boolean isLoadFinish;

    private Set<Integer> expendPositionSet = new HashSet<>();

    public BaseGarbageAdapter(List<MultiItemEntity> data) {
        super(data);
        dataAllSize = new AtomicLong();

        addItemType(3, R.layout.garbage_app_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case 0: {
                GarbageBean garbageBean = (GarbageBean) item;
                int icon = garbageBean.icon;
                ImageView ivIcon = helper.getView(R.id.ivIcon);
                TextView tvClear = helper.getView(R.id.tvClear);
                tvClear.setText(garbageBean.name);
                ivIcon.setImageDrawable(garbageBean.imageDrawable);

                CheckBox cbMemory = helper.getView(R.id.cbMemory);
                cbMemory.setText(garbageBean.getFileStrSize());

                cbMemory.setOnCheckedChangeListener(null);
                cbMemory.setChecked(garbageBean.isCheck);
                cbMemory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    garbageBean.isCheck = isChecked;

                    notifyDataSetChanged();

                    if (isChecked) {
                        dataAllSize.set(dataAllSize.get() + garbageBean.fileSize);
                    } else {
                        dataAllSize.set(dataAllSize.get() - garbageBean.fileSize);
                    }

                    if (checkedListener != null) {
                        String[] fileSize01 = FileUtil.getFileSize0(dataAllSize.get());
                        checkedListener.onAllChecked(dataAllSize.get(), fileSize01);
                    }
                });

                TextView tvDec = helper.getView(R.id.tvDec);
                tvDec.setText(garbageBean.dec);
            }

            break;
            case 1: {
//                ImageView ivIcon = helper.getView(R.id.ivIcon);
//                TextView tvClear = helper.getView(R.id.tvClear);
                TextView tvText = helper.getView(R.id.tvText);
                ImageView ivImage = helper.getView(R.id.ivImage);
                ProgressBar pbLoad = helper.getView(R.id.pbLoad);
                CheckBox cbText = helper.getView(R.id.cbText);
                TextView tvEmptyText = helper.getView(R.id.tvEmptyText);

                final GarbageHeaderBean garbageHeaderBean = (GarbageHeaderBean) item;

                if (garbageHeaderBean.getSubItems().isEmpty() && !garbageHeaderBean.isLoading) {
                    tvEmptyText.setVisibility(View.VISIBLE);
                    cbText.setVisibility(View.INVISIBLE);
                    pbLoad.setVisibility(View.INVISIBLE);
                } else {
                    tvEmptyText.setVisibility(View.INVISIBLE);
//                    cbText.setVisibility(View.VISIBLE);
//                    pbLoad.setVisibility(View.VISIBLE);
                    //设置状态
                    pbLoad.setVisibility(garbageHeaderBean.isLoading ? View.VISIBLE : View.GONE);
                    cbText.setVisibility(garbageHeaderBean.isLoading ? View.INVISIBLE : View.VISIBLE);
                }


                ivImage.setRotation(garbageHeaderBean.isExpanded() ? 0 : 180);

                tvText.setText(garbageHeaderBean.name);

                String[] fileSize0 = FileUtil.getFileSize0(garbageHeaderBean.allSize);
                cbText.setText(fileSize0[0] + fileSize0[1]);
                cbText.setOnCheckedChangeListener(null);
                cbText.setChecked(garbageHeaderBean.isCheck);

                cbText.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isLoadFinish) {
                        return;
                    }

                    if (isChecked) {
                        for (GarbageBean subItem : garbageHeaderBean.getSubItems()) {
                            subItem.isCheck = true;
                            dataAllSize.set(dataAllSize.get() + subItem.fileSize);
                        }
                    } else {
                        for (GarbageBean subItem : garbageHeaderBean.getSubItems()) {
                            subItem.isCheck = false;
                            dataAllSize.set(dataAllSize.get() - subItem.fileSize);
                        }
                    }
                    garbageHeaderBean.isCheck = isChecked;
                    notifyDataSetChanged();
                    if (checkedListener != null) {
                        String[] fileSize01 = FileUtil.getFileSize0(dataAllSize.get());
                        checkedListener.onAllChecked(dataAllSize.get(), fileSize01);
                    }
                });

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isLoadFinish) {
                            return;
                        }
                        int pos = helper.getAdapterPosition();
                        if (garbageHeaderBean.isExpanded()) {
                            expendPositionSet.remove(pos);
                            collapse(pos + getHeaderLayoutCount(), false);
                        } else {
                            expendPositionSet.add(pos);
                            expand(pos + getHeaderLayoutCount(), true);
                        }
                    }
                });
            }

            break;
            case 2: {
                GarbageBean garbageBean = (GarbageBean) item;
                ADRelativeLayout adrLayout = helper.getView(R.id.adRLayout);
                adrLayout.setAdrScenario(garbageBean.scenarioEnum);
                adrLayout.showAdLayout();
            }
            break;
            case 3: {
                GarbageRemoveBean garbageBean = (GarbageRemoveBean) item;
                int icon = garbageBean.icon;
                ImageView ivIcon = helper.getView(R.id.ivIcon);
                TextView tvClear = helper.getView(R.id.tvClear);
                tvClear.setText(garbageBean.name);
                ivIcon.setImageDrawable(garbageBean.imageDrawable);

                CheckBox cbMemory = helper.getView(R.id.cbMemory);
                cbMemory.setCompoundDrawables(null, null, null, null);

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
                        garbageRemoveBean.dec = MyApplication.getResString(R.string.recommendedCleaning);
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

    public interface IGAdapterCheckedListener {

        void onAllChecked(long allSize, String[] data);

//        void onSingleChecked(long allSize, String[] data);

    }

}
