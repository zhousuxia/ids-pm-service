package com.glaway.ids.dynamicform.vo;

import java.util.List;

/**
 * 动态表单生成Vo对象
 * @author likaiyong
 * @version 2018年8月8日
 * @see DynamicFormCommonVo
 */
public class DynamicFormCommonVo {
   
    /**
     * 动态表单编码
     */
    private String formCode;
     
    /**
     * 动态表单名称
     */
    private String formName;
    
    /**
     * 版本
     */
    private String bizVersion;
    
    /**
     * 包含的控件
     */
    private List<DynamicFormFieldCommonVo> fieldList;

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getBizVersion() {
        return bizVersion;
    }

    public void setBizVersion(String bizVersion) {
        this.bizVersion = bizVersion;
    }

    public List<DynamicFormFieldCommonVo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DynamicFormFieldCommonVo> fieldList) {
        this.fieldList = fieldList;
    }
}
