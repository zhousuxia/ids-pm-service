package com.glaway.ids.project.plan.dto;


import com.glaway.foundation.common.vdata.GLVData;
import lombok.Getter;
import lombok.Setter;

/**
 * 计划 输入
 * 
 * @generated
 */
@Getter
@Setter
public class InputsDto extends GLVData {

    /**
     * 输入项名称
     */
    private String name = null;

    /**
     * 输入关联对象类型，如："PLAN",表示该输入数据属于某条计划
     */
    private String useObjectType = null;

    /**
     * 输入关联对象ID
     */
    private String useObjectId = null;

    /**
     * 所属
     */
    private String fileId = null;

    /**
     * 来源
     */
    private String origin = null;

    /**
     * 是否必要 
     */
    private String required = null;

    /**
     * 项目库文档的bizId、本地上传文档对应的jackrabbit路径
     */
    private String docId = null;

    /**
     * 文档名称 ：本地上传的是文件的全名包括后缀；来源计划输出和项目库文档的都是项目库文档名称
     */
    private String docName = null;

    /**
     * 来源计划对象ID 
     */
    private String originObjectId = null;

    /**
     * 来源计划对象名称
     */
    private String originObjectName = null;
    
    /**
     * 来源计划对象名称显示
     */
    private String originObjectNameShow = null;

    /**
     * 来源输出对象ID 
     */
    private String originDeliverablesInfoId = null;

    /**
     * 来源输出对象名称
     */
    private String originDeliverablesInfoName = null;

    /**
     * 是否选中
     */
    private String checked = null;

    /**
     * 文件
     */
    private DocumentDto document = null;

    /**
     * 表单ID
     */
    private String formId = null;
    
    /**
     * 来源类型(LOCAL:本地上传；PLAN:计划(实际为计划的输出)；PROJECTLIBDOC:项目库文档)
     */
    private String originType = null;
    
    /**
     * 补充类型,记录流程分解时：研发流程模版带来的内部和外部输入的区别(INNERTASK,DELIEVER)
     */
    private String originTypeExt = null;
    
    /**
     * 临时Id
     */
    private String tempId = null;
    
    
    /**
     * 是否有权限
     */
    private Boolean havePower = null;
    
    /**
     * 是否有下载权限
     */
    private Boolean download = null;
    
    /**
     * 是否有查看权限
     */
    private Boolean detail = null;
    
    /**
     * 密级
     */
    private Short securityLeve = null;
    
    /**
     * 文档名称显示
     */
    private String docNameShow = null;
    
    /**
     * 来源显示
     */
    private String originPath = null;
    
    /**
     * 文档id显示
     */
    private String docIdShow = null;
    
    /**
     * 匹配标志 
     */
    private String matchFlag = null;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 版本号
     */
    private String versionCode;

}
