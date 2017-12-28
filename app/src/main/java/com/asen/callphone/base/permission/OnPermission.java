package com.asen.callphone.base.permission;

/**
 * 授权失败与否，多态，可直接与子控件对接
 * Created by asus on 2017/9/26.
 */

public interface OnPermission {

    void permissionSuccess(int requestCode);

    void permissionFail(int requestCode);

}
