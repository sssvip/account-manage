package com.bjhetang.dto;

import java.io.Serializable;

/**
 * Created by David on 2017/2/26.
 */
public class AccountManagementFilter implements Serializable{
    private String serialNumber;
    private String name;
    private String dateRange;
    private String type;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AccountManagementFilter{" +
                "serialNumber='" + serialNumber + '\'' +
                ", name='" + name + '\'' +
                ", dateRange='" + dateRange + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
