package com.asen.callphone.base.view.pinyin;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by asus on 2018/2/8.
 */

public class Pinyinmpl implements IPinyin {


    // 显示拼音
    @Override
    public IPinyin showPinyin(List<? extends BasePinyinInfo> datas) {

        if(datas == null || datas.isEmpty()){
            return this;
        }

        for (int i = 0; i <datas.size() ; i++) {

            BasePinyinInfo base =  datas.get(i);

            StringBuilder pySB = new StringBuilder();

            if(base.isNeedToPinyin()){

                String target = base.getTarget();

                for (int j = 0; j <target.length() ; j++) {

                    // 利用TinyPinyin将char转成拼音
                    // 查看源码，方法内 如果char为汉字，则返回大写拼音
                    // 如果c不是汉字，则返回String.valueOf(c)
                    pySB.append(Pinyin.toPinyin(target.charAt(j)));

                }

                // 设置字段的拼音
                base.setPinyin(pySB.toString());

                // 获取第一个首字母
                String firstChar = pySB.toString().substring(0,1);

                if(firstChar.matches("[A-Z]")){

                    base.setFirstPinyin(firstChar);

                }else{

                    base.setFirstPinyin("#");

                }
            }
        }
        return this;
    }

    // shuju 排序
    @Override
    public IPinyin sortPinyinList(List<? extends BasePinyinInfo> datas) {

        if(datas == null || datas.isEmpty()){
            return this;
        }

        showPinyin(datas);

        // 对数据进行排序
        Collections.sort(datas, new Comparator<BasePinyinInfo>() {
            @Override
            public int compare(BasePinyinInfo o1, BasePinyinInfo o2) {

                if(o1.getFirstPinyin().equals("#")){

                    return  1;

                }else if(o2.getFirstPinyin().equals("#")){

                    return -1;

                }else{

                    return o1.getPinyin().compareTo(o2.getPinyin());
                }
            }
        });

        return this;
    }




}
