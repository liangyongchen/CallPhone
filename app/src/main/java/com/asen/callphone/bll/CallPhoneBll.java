package com.asen.callphone.bll;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.asen.callphone.base.app.DebugLog;
import com.asen.callphone.model.CallPhoneModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/27.
 */

public class CallPhoneBll {

    private static final String TAG = "CallPhoneBll ///////////// ";

    // 通讯录获取
    public static List<CallPhoneModel> getContact(Context context) {
        List<CallPhoneModel> l0 = new ArrayList<>();

        Cursor c = null;

        try {

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

            c = context.getContentResolver().query(uri, new String[]{"display_name", "sort_key", "contact_id", "data1"}, null, null, "sort_key");

            if (c.moveToFirst()) {
                do {

                    CallPhoneModel m = new CallPhoneModel();
                    m.setPhone(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    m.setName(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    m.setType("手机");
                    l0.add(m);

                } while (c.moveToNext());


            }
        } catch (Exception e) {

            DebugLog.d(TAG, e.getMessage());

        } finally {
            if (c != null) {

                c.close();
            }
        }
        return l0;
    }

}
