package com.glaway.ids.project.menu.entity;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.glaway.foundation.common.entity.GLObject;
import com.glaway.foundation.system.event.EventSource;


/**
 * 项目模板树
 * 
 * @author wangshen
 */
@SuppressWarnings("serial")
@Entity(name = "ProjTemplateMenu")
@Table(name = "PM_TEMPLATE_MENU")
public class ProjTemplateMenu extends GLObject implements EventSource{

    /**
     * 显示名称
     */
    @Basic()
    private String text;

    /**
     * 图标
     */
    @Basic()
    private String iconCls;

    /**
     * 文件夹开关
     */
    @Transient
    private String state;

    /**
     * 父节点
     */
    @Basic()
    private String parentId;

    /**
     * 链接地址
     */
    @Basic()
    private String url;

    /**
     * 是否开启
     */
    @Basic()
    private Integer status;

    /**
     * 排序
     */
    @Basic()
    private Integer sort;

    /**
     * 子目录结构
     */
    @Transient
    private List<ProjTemplateMenu> children = new ArrayList<ProjTemplateMenu>();

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getState() {
        return state;
    }

    public List<ProjTemplateMenu> getChildren() {
        return children;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParentId() {
        return parentId;
    }

    public void setChildren(List<ProjTemplateMenu> children) {
        this.children = children;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
