package com.mobi.overseas.clearsafe.main.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mobi.overseas.clearsafe.MainActivity;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.main.adapter.HomeAdapter;
import com.mobi.overseas.clearsafe.main.adapter.ToolBoxFileAdapter;
import com.mobi.overseas.clearsafe.main.adapter.ToolBoxFileAdapter2;
import com.mobi.overseas.clearsafe.main.adapter.data.ClearBean;
import com.mobi.overseas.clearsafe.ui.common.base.BaseFragment;
import com.mobi.overseas.clearsafe.ui.common.util.StatusBarPaddingUtil;

import java.util.List;

import butterknife.BindView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/9 15:21
 * @Dec 略
 */
public class ToolBoxFragment extends BaseFragment {
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.rvTool)
    RecyclerView rvTool;
    @BindView(R.id.rvFile)
    RecyclerView rvFile;
    private ToolBoxFileAdapter mBoxFileAdapter;
    private ToolBoxFileAdapter2 mBoxFileAdapter2;
    private int mMemoryValue;


    @Override
    protected int getFragmentView() {
        return R.layout.fragment_tool_box;
    }

    @Override
    protected void initView() {
        StatusBarPaddingUtil.statusBar(getActivity(),false);
        StatusBarPaddingUtil.topViewPadding(toolBar);

        rvTool.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvTool.setNestedScrollingEnabled(false);

        mBoxFileAdapter2 = new ToolBoxFileAdapter2();
        rvTool.setAdapter(mBoxFileAdapter2);

        List<ClearBean> list = ClearBean.getRvGridBoxData2();
        mBoxFileAdapter2.addData(list);


        rvFile.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        rv.addItemDecoration(new GridDividerItemDecoration(0));
        rvFile.setNestedScrollingEnabled(false);
        mBoxFileAdapter = new ToolBoxFileAdapter();
        rvFile.setAdapter(mBoxFileAdapter);

        List<ClearBean> boxList = ClearBean.getRvGridBoxData();
        mBoxFileAdapter.addData(boxList);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            StatusBarPaddingUtil.statusBar(getActivity(),false);
        }

        if (getActivity() instanceof MainActivity) {
            mMemoryValue = ((MainActivity) getActivity()).getMemoryValue();
            mBoxFileAdapter2.setMemoryValue(mMemoryValue);
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
