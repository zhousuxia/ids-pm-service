package com.glaway.ids.planGeneral.plantabtemplate.entity;

import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据源属性信息
 * @Date: 2019/8/27
 */
@Entity(name = "ObjectPropertyInfo")
@Table(name = "pm_object_property_info")
public class ObjectPropertyInfo extends GLObject {

    //数据源对象Id
    @Basic
    private String dataSourceId;

    //对象路径
    @Basic
    private String objectPath;

    //属性名称
    @Basic
    private String propertyName;

    //对象属性
    @Basic
    private String propertyValue;

    //控件
    @Basic
    private String control;

    //format
    @Basic
    private String format;

    //显示(0-编制，1-编制&启动，2-启动，3-/)
    @Basic
    private String display;

    //读写权限(0-编制，1-编制&启动，2-启动，3-/)
    @Basic
    private String readWriteAccess;

    //必填项(默认为“false”，显示为不必填，true为必填)
    @Basic
    private boolean required = false;

    //操作事件
    @Basic
    private String operationEvent;

    //排序序号
    @Basic
    private String orderNumber;

    @Basic
    private String avaliable = "1";

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getObjectPath() {
        return objectPath;
    }

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getReadWriteAccess() {
        return readWriteAccess;
    }

    public void setReadWriteAccess(String readWriteAccess) {
        this.readWriteAccess = readWriteAccess;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getOperationEvent() {
        return operationEvent;
    }

    public void setOperationEvent(String operationEvent) {
        this.operationEvent = operationEvent;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(String avaliable) {
        this.avaliable = avaliable;
    }

    @Override
    public String toString() {
        return "ObjectPropertyInfo{" + "dataSourceId='" + dataSourceId + '\'' + ", objectPath='"
               + objectPath + '\'' + ", propertyName='" + propertyName + '\'' + ", propertyValue='"
               + propertyValue + '\'' + ", control='" + control + '\'' + ", format='" + format
               + '\'' + ", display='" + display + '\'' + ", readWriteAccess='" + readWriteAccess
               + '\'' + ", required=" + required + ", operationEvent='" + operationEvent + '\''
               + ", orderNumber='" + orderNumber + '\'' + ", avaliable='" + avaliable + '\'' + '}';
    }
}
