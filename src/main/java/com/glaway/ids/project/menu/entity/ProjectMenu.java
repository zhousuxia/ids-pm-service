package com.glaway.ids.project.menu.entity;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.glaway.foundation.common.entity.GLObject;


/**
 * 项目树
 * 
 * @author wangshen
 */
@SuppressWarnings("serial")
@Entity(name = "ProjectMenu")
@Table(name = "PM_MENU")
public class ProjectMenu extends GLObject {

    /**
     * 显示名称
     */
    private String text;

    /**
     * 图标
     */
    private String iconCls;

    /**
     * 文件夹开关
     */
    @Transient
    private String state; 

    /**
     * 父节点ID
     */
    private String parentId;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 是否是项目下菜单
     */
    private Integer underProject;

    /**
     * 是否开启
     */
    private Integer status;

    /**
     * 点击是否更新最近N条
     */
    private Integer insertRecently;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 子目录结构
     */
    @Transient
    private List<ProjectMenu> children = new ArrayList<ProjectMenu>();

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

    public List<ProjectMenu> getChildren() {
        return children;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParentId() {
        return parentId;
    }

    public void setChildren(List<ProjectMenu> children) {
        this.children = children;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getUnderProject() {
        return underProject;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUnderProject(Integer underProject) {
        this.underProject = underProject;
    }

    public Integer getInsertRecently() {
        return insertRecently;
    }

    public void setInsertRecently(Integer insertRecently) {
        this.insertRecently = insertRecently;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ProjectMenu [text=" + text + ", iconCls=" + iconCls + ", state=" + state
               + ", parentId=" + parentId + ", url=" + url + ", underProject=" + underProject
               + ", status=" + status + ", insertRecently=" + insertRecently + ", children="
               + children + "]";
    }

}
