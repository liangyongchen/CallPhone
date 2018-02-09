package com.asen.callphone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.asen.callphone.model.CallPhoneModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/28.
 */

public class DBUtil {

    private static final String TAG = "DBUtil ========== ";
    private DatabaseContext dbContext;
    private DBHelper dbHelper;
    public SQLiteDatabase db;

    private Context mContext;

    private DBUtil(Context context) {

        this.mContext = context;

        // 在SD卡上面创建数据库
        // this.dbContext = new DatabaseContext(context);
        // this.dbHelper = new DBHelper(dbContext);
        // this.db = dbHelper.getReadableDatabase();

        // 在app内部创建
        this.dbHelper = new DBHelper(context);
        this.db = dbHelper.getReadableDatabase();

    }

    private static DBUtil mDBUtil;

    public static DBUtil getInstance(Context context) {
        if (mDBUtil == null) {
            mDBUtil = new DBUtil(context);
        }
        return mDBUtil;
    }


    // 增加数据
    public boolean InsertData(String name, String phone, String mailbox) {
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("phone", phone);
            contentValues.put("mailbox", mailbox);
            contentValues.put("type", "app");
            db.insert("CallPhoneTable", null, contentValues);
            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return InsertSuccess(name, phone, mailbox);
    }


    private boolean InsertSuccess(String name, String phone, String mailbox) {

        String app = "";

        if (StringUtils.isNotEmpty(name)) {
            app += " name = " + name;
        }

        if (StringUtils.isNotEmpty(phone)) {
            app += " and phone = " + phone;
        }

        if (StringUtils.isNotEmpty(mailbox)) {
            app += " and mailbox = " + mailbox;
        }

        String sql = "select count(*) as ResultCount from CallPhoneTable where " + app;
        Cursor c = null;

        try {
            c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                if (c.getInt(c.getColumnIndex("ResultCount")) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return false;
    }

    // 查询数据
    public List<CallPhoneModel> selectAll() {

        List<CallPhoneModel> l = new ArrayList<>();

        try {

            String sql = "select *  from CallPhoneTable";
            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                CallPhoneModel m = new CallPhoneModel();

                m.setName(c.getString(c.getColumnIndex("name")));
                m.setPhone(c.getString(c.getColumnIndex("phone")));
                m.setType(c.getString(c.getColumnIndex("type")));
                m.setGroup(c.getString(c.getColumnIndex("grouping")));
                m.setCollect(c.getString(c.getColumnIndex("collect")));
                m.setPhoto(c.getString(c.getColumnIndex("photo")));

                l.add(m);

            }

            c.close();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        return l;
    }


}
