package com.asen.callphone.base.permission;

import android.Manifest;
import android.content.pm.PackageManager;


/**
 * 设备授权
 * ---- android6.0 以前的获取
 * ---- android6.0 之后的版本
 * Created by asus on 2017/7/26.
 */

public class PermissionApply {

    // 定义的 KEY 值不能大于 65536


    // region  // 连接WiFi所需权限

    protected static final int PERMISSION_REQ_CONNECT_WIFI = 3020; // WiFi热点连接和创建权限请求码

    public static final String[] PERMISSION_CONNECT_WIFI = new String[] // 连接WiFi所需权限
            {
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION, /* android.permission-group.LOCATION 组 */
                    Manifest.permission.ACCESS_COARSE_LOCATION,/* android.permission-group.LOCATION 组 */
                    Manifest.permission.WRITE_EXTERNAL_STORAGE /* android.permission-group.STORAGE 组 */
            };

    // endregion


    // region // 自定义授权


    // region // 拍照授权

    public static final int PERMISSION_READ_CAMERA_KEY = 111;

    public static final String[] PERMISSION_READ_CAMERA = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    // endregion

    // region  // 创建便携热点所需权限

    // 创建便携热点权限请求码
    protected static final int PERMISSION_REQ_CREATE_HOTSPOT_KEY = 3021;
    // 创建便携热点所需权限
    public static final String[] PERMISSION_CREATE_HOTSPOT = new String[]
            {
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

    // endregion

    // region // 通讯录、SD卡授权、手机型号权限请求码

    public static final int PERMISSION_READ_CONTACTS_and_STORAGE_KEY = 999;

    public static final String[] PERMISSION_READ_CONTACTS_and_STORAGE = new String[]{
            Manifest.permission.READ_CONTACTS,
            // Manifest.permission.ACCESS_FINE_LOCATION, /*定位授权申请*/
            Manifest.permission.READ_EXTERNAL_STORAGE,  /*SD卡授权申请*/
            Manifest.permission.READ_PHONE_STATE,  /*型号授权申请*/
    };

    // endregion


    // endregion


    // region  // 权限申请管理组 : 每一组只要有一个授权，其他会自动默认授权


    // region   android.permission-group.STORAGE 组 读写 SD卡

    public static final int ANDROID_PERMISSION_GROUP_STORAGE_KEY = 1000;

    public static final String[] ANDROID_PERMISSION_GROUP_STORAGE = new String[]
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE, /* 读取 SD卡 */
                    Manifest.permission.WRITE_EXTERNAL_STORAGE /* 创建文件 */
            };

    // endregion

    // region android.permission-group.SMS 组

    public static final int ANDROID_PERMISSION_GROUP_SMS_KEY = 1001;

    public static final String[] ANDROID_PERMISSION_GROUP_SMS = new String[]
            {
                    Manifest.permission.SEND_SMS,/*  */
                    Manifest.permission.RECEIVE_SMS,/*  */
                    Manifest.permission.READ_SMS,/*  */
                    Manifest.permission.RECEIVE_WAP_PUSH,/*  */
                    Manifest.permission.RECEIVE_MMS/*  */
            };

    // endregion

    // region  android.permission-group.SENSORS 组

    public static final int ANDROID_PERMISSION_GROUP_SENSORS_KEY = 1002;

    public static final String[] ANDROID_PERMISSION_GROUP_SENSORS = new String[]
            {
                    Manifest.permission.BODY_SENSORS/*  */
            };

    // endregion

    // region android.permission-group.PHONE 组

    public static final int ANDROID_PERMISSION_GROUP_PHONE_KEY = 1003;

    public static final String[] ANDROID_PERMISSION_GROUP_PHONE = new String[]
            {
                    Manifest.permission.READ_PHONE_STATE, /* 获取设备型号 */
                    Manifest.permission.CALL_PHONE,/* 打电话 */
                    Manifest.permission.READ_CALL_LOG,/* 读写通话记录 */
                    Manifest.permission.WRITE_CALL_LOG,/* 读写通话记录 */
                    Manifest.permission.USE_SIP,/*  */
                    Manifest.permission.PROCESS_OUTGOING_CALLS/*  */
//                    Manifest.permission.ADD_VOICEMAIL,/*  */
            };

    // endregion

    // region android.permission-group.MICROPHONE 组

    public static final int ANDROID_PERMISSION_GROUP_MICROPHONE_KEY = 1004;

    public static final String[] ANDROID_PERMISSION_GROUP_MICROPHONE = new String[]
            {
                    Manifest.permission.RECORD_AUDIO/*  */
            };

    // endregion

    // region android.permission-group.LOCATION 组

    public static final int ANDROID_PERMISSION_GROUP_LOCATION_KEY = 1005;

    public static final String[] ANDROID_PERMISSION_GROUP_LOCATION = new String[]
            {
                    Manifest.permission.ACCESS_FINE_LOCATION,/* 这个权限用于访问GPS定位 */
                    Manifest.permission.ACCESS_COARSE_LOCATION/* 这个权限用于进行网络定位 */
            };

    // endregion

    // region android.permission-group.CONTACTS 组

    public static final int ANDROID_PERMISSION_GROUP_CONTACTS_KEY = 1006;

    public static final String[] ANDROID_PERMISSION_GROUP_CONTACTS = new String[]
            {
                    Manifest.permission.READ_CONTACTS,/* 手机通讯录6.0后需要动态申请 */
                    Manifest.permission.GET_ACCOUNTS,/*  */
                    Manifest.permission.WRITE_CONTACTS/*  */
            };

    // endregion

    // region android.permission-group.CAMERA 组

    public static final int ANDROID_PERMISSION_GROUP_CAMERA_KEY = 1007;

    public static final String[] ANDROID_PERMISSION_GROUP_CAMERA = new String[]
            {
                    Manifest.permission.CAMERA/*  */
            };

    // endregion

    // region android.permission-group.CALENDAR 组

    public static final int ANDROID_PERMISSION_GROUP_CALENDAR_KEY = 1008;

    public static final String[] ANDROID_PERMISSION_GROUP_CALENDAR = new String[]
            {
                    Manifest.permission.READ_CALENDAR,/*  */
                    Manifest.permission.WRITE_CALENDAR/*  */
            };

    // endregion


    // endregion


    // 确认所需权限是否都已授权, 在 onRequestPermissionsResult 方法中判断
    public static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


}
