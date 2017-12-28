package com.asen.callphone.model;

/**
 * Created by asus on 2017/11/24.
 */

public class CallPhoneModel {


    // 数据库建表
    private String id;      // id
    private String name;    // 名称
    private String phone;   // 电话号码
    private String mailbox; // 邮箱
    private String collect; // 收藏
    private String group;   // 分组
    private String type;    // 电话号码类型（设备的、app数据库的、sd卡文件上面的）
    private String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
