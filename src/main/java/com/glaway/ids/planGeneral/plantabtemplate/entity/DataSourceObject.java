package com.glaway.ids.planGeneral.plantabtemplate.entity;

import com.glaway.foundation.common.entity.GLObject;
import org.hibernate.annotations.Type;

import javax.persistence.*;


/**
 * 数据源对象信息
 * @Date: 2019/8/27
 */
@Entity(name = "DataSourceObject")
@Table(name = "pm_data_source_Object")
public class DataSourceObject extends GLObject {

    //对象选择
    @Basic
    private String objectPath;

    //数据转换接口
    @Basic
    private String dataToInterface;

    //页签模版Id
    @Basic
    private String tabId;

    //对象所属模块（Review,PM）
    @Basic
    private String projectModel;

    //对象数据结果
    @Lob
    @Type(type = "text")
    @Column(columnDefinition = "CLOB")
    private String objectModelProperty;

    //SQL条件
    @Basic
    private String resultSql;

    //表名
    @Basic
    private String tableName;

    @Basic
    private String avaliable = "1";

    public String getObjectPath() {
        return objectPath;
    }

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getDataToInterface() {
        return dataToInterface;
    }

    public void setDataToInterface(String dataToInterface) {
        this.dataToInterface = dataToInterface;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(String projectModel) {
        this.projectModel = projectModel;
    }

    public String getObjectModelProperty() {
        return objectModelProperty;
    }

    public void setObjectModelProperty(String objectModelProperty) {
        this.objectModelProperty = objectModelProperty;
    }

    public String getResultSql() {
        return resultSql;
    }

    public void setResultSql(String resultSql) {
        this.resultSql = resultSql;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(String avaliable) {
        this.avaliable = avaliable;
    }

    @Override
    public String toString() {
        return "DataSourceObject{" + "objectPath='" + objectPath + '\'' + ", dataToInterface='"
               + dataToInterface + '\'' + ", tabId='" + tabId + '\'' + ", projectModel='"
               + projectModel + '\'' + ", objectModelProperty='" + objectModelProperty + '\''
               + ", resultSql='" + resultSql + '\'' + ", tableName='" + tableName + '\''
               + ", avaliable='" + avaliable + '\'' + '}';
    }
}
