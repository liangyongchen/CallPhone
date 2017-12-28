package com.asen.callphone.base.util;

import android.app.Activity;

/**
 * 匹配方法数据类型类
 * Created by pc on 2017/3/11.
 */

public class CommonUtil
{
    /**
     * Activity 用 startActivityForResult 方法 跳转的传参数
     */
    public static final int Activity_Login_RequestCode = 80;
    public static final int Activity_ReceiveOrderItem_RequestCode = 90;
    
    // 系统本来就有相应的参数
    public static final int Activity_Result_OK = Activity.RESULT_OK;
    
    public static final int Activity_Result_Canceled = Activity.RESULT_CANCELED;

    /**
     * 验证字母，数字类型
     */
    public enum enumValidatingType
    {
        AllNumber,         // 所有数字
        AllLetter,         // 所有字母
        LetterAndNumber,   // 字母和数字
        PositiveNumber     // 正数
    }
    
    /**
     * 页面切换六大动画（Activity、Fragment）
     */
    public enum enumActionType
    {
        ACTION_LANDING,   // 登陆动态
        ACTION_SIGN_OUT,  // 退出动态
        ACTION_SIGN_OUT2, // 退出动态
        ACTION_FORWARD,   // 前进动态
        ACTION_FORWARD2,  // 前进动态
        ACTION_FADE_IN,   // 淡入
        ACTION_FADE_OUT,  // 淡出
        ACTION_OTHER      // 其他动态
    }
    
    
}
