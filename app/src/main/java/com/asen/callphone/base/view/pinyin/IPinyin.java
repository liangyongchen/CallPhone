package com.asen.callphone.base.view.pinyin;

import java.util.List;

/**
 * Created by asus on 2018/2/8.
 */

public interface IPinyin {

    /**
     * 此接口实现数据的升降序和拼音的
     */

    // 第一步：需要转化拼音
    IPinyin needToPinyin(List<? extends BasePinyinInfo> datas);

    // 第二步：对拼音排序
    IPinyin sortPinyinList(List<? extends BasePinyinInfo> datas);

    // 第三步：显示拼音（筛选）
    IPinyin showPinyin(List<? extends BasePinyinInfo> datas);







}
