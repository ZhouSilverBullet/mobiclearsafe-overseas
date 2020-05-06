package com.mobi.clearsafe.greendao;

import android.content.Context;
import android.util.Log;

import com.mobi.clearsafe.greendao.gen.DaoSession;
import com.mobi.clearsafe.greendao.gen.TodayStepDataDao;
import com.mobi.clearsafe.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/10/23
 * desc:
 */
public class TodayStepSessionUtils {
    static DaoSession daoSession;

    public static DaoSession getDaoInstance(Context context) {
        if (daoSession == null) {
            daoSession = DbManager.getDaoSession(context);
        }
        //清空所有数据表的缓存数据
        // daoSession.clear();
        return daoSession;
    }


    /**
     * 同一天只保存最新的一条数据
     * @param bean
     */
    public static void insertDbBean(Context context,TodayStepData bean){
        List<TodayStepData> data = query(context, bean.getDate());
        if(data!=null&&data.size()>0){
            Log.e("xxx","更新数据");
            getDaoInstance(context).getTodayStepDataDao().queryBuilder().where(TodayStepDataDao.Properties.Date.eq(bean.getDate())).buildDelete().executeDeleteWithoutDetachingEntities();
            insertOrReplaceDbBean(context,bean);
        }else {//没有则插入
            Log.e("xxx","插入数据");
            getDaoInstance(context).getTodayStepDataDao().insert(bean);
        }
    }

    /**
     * insertOrReplace()数据存在则替换，数据不存在则插入
     * @param context
     * @param bean
     */
    public static void insertOrReplaceDbBean(Context context,TodayStepData bean){
        getDaoInstance(context).getTodayStepDataDao().insertOrReplace(bean);
    }

    /**
     * delete()删除单个数据
     * @param context
     * @param bean
     */
    public static void deleteDbBean(Context context,TodayStepData bean) {
        getDaoInstance(context).getTodayStepDataDao().delete(bean);
    }

    /**
     * delete()删除所有数据
     * @param context
     */
    public static void deleteAllDbBean(Context context) {
        getDaoInstance(context).getTodayStepDataDao().deleteAll();
    }

    /**
     * update()修改本地数据
     * @param context
     * @param bean
     */
    public static void updateDbBean(Context context,TodayStepData bean) {
        getDaoInstance(context).getTodayStepDataDao().update(bean);
    }

    /**
     * 查询所有数据
     * @param context
     */
    public static List<TodayStepData> queryAll(Context context) {
        QueryBuilder<TodayStepData> builder = getDaoInstance(context).getTodayStepDataDao().queryBuilder();
        return builder.build().list();
    }


    /**
     * 根据日期查询判断是否存在
     * @param context
     * @param time
     * @return
     */
    public static  List<TodayStepData> query(Context context,String time){
        List<TodayStepData> list = getDaoInstance(context).getTodayStepDataDao().queryBuilder().where(TodayStepDataDao.Properties.Date.eq(time)).list();
        LogUtils.e(list.toString());
        return  list;
    }








}
