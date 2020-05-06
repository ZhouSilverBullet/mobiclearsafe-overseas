package com.mobi.overseas.clearsafe.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mobi.overseas.clearsafe.greendao.gen.DaoMaster;
import com.mobi.overseas.clearsafe.greendao.gen.DaoSession;

/**
 * author:zhaijinlu
 * date: 2019/10/23
 * desc:
 */
public class DbManager {

    public static final String DB_NAME = "mobi.db";
    private static DbManager mDbManager;
    private static MySqliteOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private Context mContext;

    private DbManager(Context context){
        this.mContext=context;
        mDevOpenHelper=new MySqliteOpenHelper(context,DB_NAME);
//        mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
//        mDaoSession = mDaoMaster.newSession();
        getDaoMaster(context);
        getDaoSession(context);

    }

    /**
     * 获取DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (null == mDaoMaster) {
            synchronized (DbManager.class) {
                if (null == mDaoMaster) {
                    mDaoMaster = new DaoMaster(getWritableDatabase(context));
                }
            }
        }
        return mDaoMaster;
    }
    /**
     * 获取DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (null == mDaoSession) {
            synchronized (DbManager.class) {
                mDaoSession = getDaoMaster(context).newSession();
            }
        }
        return mDaoSession;
    }



    public static DbManager getInstance(Context context) {
        if (null == mDbManager) {
            synchronized (DbManager.class) {
                if (null == mDbManager) {
                    mDbManager = new DbManager(context);
                }
            }
        }
        return mDbManager;
    }

    /**
     * 获取可读数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getReadableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance(context);
        }
        return mDevOpenHelper.getReadableDatabase();
    }
    /**
     * 获取可写数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getWritableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance(context);
        }
        return mDevOpenHelper.getWritableDatabase();
    }





}
