package com.asen.callphone.base.view.pinyin;

import java.util.List;

/**
 * Created by asus on 2018/2/8.
 */

public interface IPinyin {

    /**
     * 此接口实现数据的升降序和拼音的
     */

    // 显示拼音
    IPinyin showPinyin(List<? extends BasePinyinInfo> data);

    // 对拼音排序
    IPinyin sortPinyinList(List<? extends BasePinyinInfo> data);



}
