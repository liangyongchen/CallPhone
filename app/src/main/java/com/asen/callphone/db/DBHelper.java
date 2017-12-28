package com.asen.callphone.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 * Created by asus on 2017/11/28.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static final String DBNAME = "MailList.db3";
    private static final int VERSION = 1;

    // 创建表
    private static final String CallPhoneTable = "create table if not exists CallPhoneTable(" +
            " id integer primary key autoincrement," +  // id
            " name text," +                             // 名称
            " phone integer(20)," +                     // 电话号码
            " mailbox char(50)," +                      // 邮箱
            " photo text," +                            // 图片（Base64）
            " collect integer," +                       // 收藏
            " grouping char(30)," +                     // 分组
            " type char(10))";                          // 电话号码类型（设备的、app数据库的、sd卡文件上面的）

    public DBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    // 当数据库第一次创建时会回调该方法：
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CallPhoneTable);
    }

    // 当数据库发现有版本更新时(newVersion > oldVersion),调用该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
