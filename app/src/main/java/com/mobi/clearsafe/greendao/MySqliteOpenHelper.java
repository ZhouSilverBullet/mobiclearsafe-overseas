package com.mobi.clearsafe.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mobi.clearsafe.greendao.gen.DaoMaster;

/**
 * author:zhaijinlu
 * date: 2019/10/23
 * desc: 重写MySqliteOpenHelper，防止数据库升级清空本地数据
 */
public class MySqliteOpenHelper extends DaoMaster.OpenHelper {
    public MySqliteOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
     //   MigrationHelper.migrate(db, StudentDao.class);
    }



}
