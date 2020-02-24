package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.entity.GLObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 计划 输入
 * 
 * @generated
 */
@Getter
@Setter
@Entity(name = "Inputs")
@Table(name = "PM_INPUTS")
public class Inputs extends GLObject {

    /**
     * 输入项名称
     */
    @Basic()
    private String name = null;

    /**
     * 输入关联对象类型，如："PLAN",表示该输入数据属于某条计划
     */
    @Basic()
    private String useObjectType = null;

    /**
     * 输入关联对象ID
     */
    @Basic()
    private String useObjectId = null;

    /**
     * 所属
     */
    @Basic()
    private String fileId = null;

    /**
     * 来源
     */
    @Basic()
    private String origin = null;

    /**
     * 是否必要 
     */
    @Basic()
    private String required = null;

    /**
     * 项目库文档的bizId、本地上传文档对应的jackrabbit路径
     */
    @Basic()
    private String docId = null;

    /**
     * 文档名称 ：本地上传的是文件的全名包括后缀；来源计划输出和项目库文档的都是项目库文档名称
     */
    @Basic()
    private String docName = null;

    /**
     * 来源计划对象ID 
     */
    @Basic()
    private String originObjectId = null;

    /**
     * 来源计划对象名称
     */
    @Transient()
    private String originObjectName = null;
    
    /**
     * 来源计划对象名称显示
     */
    @Transient()
    private String originObjectNameShow = null;

    /**
     * 来源输出对象ID 
     */
    @Basic()
    private String originDeliverablesInfoId = null;

    /**
     * 文件类型
     */
    @Basic
    private String fileType;

    /**
     * 版本号
     */
    @Basic
    private String versionCode;

  /**
     * 来源输出对象名称
     */
    @Transient()
    private String originDeliverablesInfoName = null;

    /**
     * 是否选中
     */
    @Transient()
    private String checked = null;

    /**
     * 文件
     */
    @Transient()
    private Document document = null;

    /**
     * 表单ID
     */
    @Basic()
    private String formId = null;
    
    /**
     * 来源类型(LOCAL:本地上传；PLAN:计划(实际为计划的输出)；PROJECTLIBDOC:项目库文档)
     */
    @Basic()
    private String originType = null;
    
    /**
     * 补充类型,记录流程分解时：研发流程模版带来的内部和外部输入的区别(INNERTASK,DELIEVER)
     */
    @Basic()
    private String originTypeExt = null;
    
    /**
     * 临时Id
     */
    @Transient()
    private String tempId = null;
    
    
    /**
     * 是否有权限
     */
    @Transient()
    private Boolean havePower = null;
    
    /**
     * 是否有下载权限
     */
    @Transient()
    private Boolean download = null;
    
    /**
     * 是否有查看权限
     */
    @Transient()
    private Boolean detail = null;
    
    /**
     * 密级
     */
    @Transient()
    private Short securityLeve = null;
    
    /**
     * 文档名称显示
     */
    @Transient()
    private String docNameShow = null;
    
    /**
     * 来源显示
     */
    @Transient()
    private String originPath = null;
    
    /**
     * 文档id显示
     */
    @Transient()
    private String docIdShow = null;
    
    /**
     * 匹配标志 
     */
    @Transient()
    private String matchFlag = null;

}
