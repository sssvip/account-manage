package com.bjhetang.domain;

import java.sql.Timestamp;

/**
 * 账户管理
 * Created by David on 2017/2/24.
 */
public class AccountManagement {
    //编码
    private int serialNumber;
    //名称
    private String name;
    //备注
    private String remark;
    //状态
    private String status;
    //账户状态，这里简单用常亮定义，复杂的可以枚举、map等类型做常量定义
    public static final String STATUS_LOCKED = "封存";
    public static final String STATUS_UNLOCKED = "启封";
    //创建时间
    private Timestamp createOn;
    //最后登录时间
    private Timestamp lastLoginTime;
    //账号类型
    private String type;
    //账户类型，这里简单用常亮定义，复杂的可以枚举、map等类型做常量定义
    public static final String TYPE_USER = "用户";
    public static final String TYPE_ADMIN = "管理员";
    public static final String TYPE_SALES = "销售";

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Timestamp createOn) {
        this.createOn = createOn;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AccountManagement{" +
                "serialNumber=" + serialNumber +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", createOn=" + createOn +
                ", lastLoginTime=" + lastLoginTime +
                ", type='" + type + '\'' +
                '}';
    }
}
