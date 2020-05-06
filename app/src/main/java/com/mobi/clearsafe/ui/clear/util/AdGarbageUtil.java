package com.mobi.clearsafe.ui.clear.util;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.adtest.manager.ScenarioEnum;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.ad.ADRelativeLayout;
import com.mobi.clearsafe.ui.clear.data.GarbageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/30 15:31
 * @Dec 略
 */
public class AdGarbageUtil {

    public static List<MultiItemEntity> getAdGarbageList() {
        ArrayList<MultiItemEntity> data = new ArrayList<>();
        data.add(getAdGarbage(ScenarioEnum.garbage_result1_native));
        data.add(getAdGarbage(ScenarioEnum.garbage_result2_native));
        data.add(getAdGarbage(ScenarioEnum.garbage_result3_native));
        data.add(getAdGarbage(ScenarioEnum.garbage_result4_native));
        return data;
    }

    private static GarbageBean getAdGarbage(ScenarioEnum scenarioEnum) {
        GarbageBean garbageBean = new GarbageBean();
        garbageBean.itemType = 2;
        garbageBean.scenarioEnum = scenarioEnum;
        return garbageBean;
    }

    public static void addAdd(LinearLayout adLayout) {
        adLayout.setVisibility(View.VISIBLE);
//        adLayout.addView(getView(adLayout.getContext(), ScenarioEnum.garbage_result1_native));
//        adLayout.addView(getView(adLayout.getContext(), ScenarioEnum.garbage_result2_native));
//        adLayout.addView(getView(adLayout.getContext(), ScenarioEnum.garbage_result3_native));
//        adLayout.addView(getView(adLayout.getContext(), ScenarioEnum.garbage_result4_native));
    }

    public static View getView(Context context, ScenarioEnum scenarioEnum) {
        ADRelativeLayout adrLayout = new ADRelativeLayout(context);
        adrLayout.setAdrScenario(scenarioEnum);
        adrLayout.showAdLayout();
        return adrLayout;
    }
}
