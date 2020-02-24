package com.glaway.ids.planGeneral.plantabtemplate.entity;

import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 页签模版信息
 * @Date: 2019/8/27
 */
@Entity(name = "TabTemplate")
@Table(name = "pm_tab_template")
public class TabTemplate extends BusinessObject {

    //页签名称
    @Basic
    private String name;

    //活动编号
    @Basic
    private String code;

    //活动状态（默认为“1”表示启用，0表示禁用）
    @Basic
    private String stopFlag = "1";

    //页签类型
    @Basic
    private String tabType;

    //页面显示方式(sql编辑--0，URL接口--1)
    @Basic
    private String displayUsage;

    //备注
    @Basic
    private String remake;

    //外部URL
    @Basic
    private String externalURL;

    //来源（默认为“1”表示系统，0表示SQL增加）
    @Basic
    private String source = "1";

    @Basic
    private String avaliable = "1";

    //流程实例Id
    @Basic()
    private String processInstanceId = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(String stopFlag) {
        this.stopFlag = stopFlag;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public String getDisplayUsage() {
        return displayUsage;
    }

    public void setDisplayUsage(String displayUsage) {
        this.displayUsage = displayUsage;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getExternalURL() {
        return externalURL;
    }

    public void setExternalURL(String externalURL) {
        this.externalURL = externalURL;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(String avaliable) {
        this.avaliable = avaliable;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String toString() {
        return "TabTemplate{" + "name='" + name + '\'' + ", code='" + code + '\'' + ", stopFlag='"
               + stopFlag + '\'' + ", tabType='" + tabType + '\'' + ", displayUsage='"
               + displayUsage + '\'' + ", remake='" + remake + '\'' + ", externalURL='"
               + externalURL + '\'' + ", source='" + source + '\'' + ", avaliable='" + avaliable
               + '\'' + ", processInstanceId='" + processInstanceId + '\'' + '}';
    }
}
