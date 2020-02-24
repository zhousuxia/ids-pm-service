package com.glaway.ids.dynamicform.vo;

/**
 * 动态表单控件
 * @author likaiyong
 * @version 2018年8月9日10:00:49
 * @see DynamicFormFieldCommonVo
 *
 */
public class DynamicFormFieldCommonVo {

    /**
     * 控件名
     */
    private String fieldName;

    /**
     * 控件标题
     */
    private String fieldTitle;

    /**
     * 控件类型
     */
    private String fieldType;
    
    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private String fieldPlugins;

    /**
     * 流程变量（0：否 1：是）
     */
    private String fieldFlow;
    
    /**
     * 是否必填（true/false）
     */
    private String orgrequired;
    
    /**
     * 是否支持多选（true/false）
     */
    private String multiple;

    /**
     * 编码<br>
     */
    private String fieldCode;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldPlugins() {
        return fieldPlugins;
    }

    public void setFieldPlugins(String fieldPlugins) {
        this.fieldPlugins = fieldPlugins;
    }

    public String getFieldFlow() {
        return fieldFlow;
    }

    public void setFieldFlow(String fieldFlow) {
        this.fieldFlow = fieldFlow;
    }

    public String getOrgrequired() {
        return orgrequired;
    }

    public void setOrgrequired(String orgrequired) {
        this.orgrequired = orgrequired;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

}
