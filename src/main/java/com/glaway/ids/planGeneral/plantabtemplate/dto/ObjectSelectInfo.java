package com.glaway.ids.planGeneral.plantabtemplate.dto;

/**
 * 选择实体对象DTO
 * @Date: 2019/8/28
 */
public class ObjectSelectInfo {

    //对象类路径
    private String objectPath;

    //表名字
    private String tableName;

    //对象字段集合
    private String objectModelProperty;

    public String getObjectPath() {
        return objectPath;
    }

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getObjectModelProperty() {
        return objectModelProperty;
    }

    public void setObjectModelProperty(String objectModelProperty) {
        this.objectModelProperty = objectModelProperty;
    }

    @Override
    public String toString() {
        return "ObjectSelectInfo{" + "objectPath='" + objectPath + '\'' + ", tableName='"
               + tableName + '\'' + ", objectModelProperty='" + objectModelProperty + '\'' + '}';
    }
}
