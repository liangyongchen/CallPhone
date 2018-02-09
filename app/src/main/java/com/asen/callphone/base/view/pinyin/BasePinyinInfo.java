package com.asen.callphone.base.view.pinyin;

/**
 * Created by asus on 2018/2/8.
 */

public abstract class BasePinyinInfo {

    /**
     * 拼音的转父类
     * 逻辑
     * 先获取目标字段，根据是否需要转化拼音才转化
     */

    // 字段的全部拼音
    private String pinyin;

    // 字段的第一个字的首字母
    private String firstPinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstPinyin() {
        return firstPinyin;
    }

    public void setFirstPinyin(String firstPinyin) {
        this.firstPinyin = firstPinyin;
    }



    // 是否需要转化成拼音
    public boolean isNeedToPinyin() {
        return true;
    }


    // 目标字段
    protected abstract String getTarget();


}
