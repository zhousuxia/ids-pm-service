/*
 * 文件名：projVo.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：jishuai
 * 修改时间：2015年12月09日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.vo;

public class ProjVo {
    
    /** 项目id*/
    private String id;
    /** 项目编号*/
    private String projectNumber;
    /**项目名称*/
    private String name;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProjectNumber() {
        return projectNumber;
    }
    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
}
