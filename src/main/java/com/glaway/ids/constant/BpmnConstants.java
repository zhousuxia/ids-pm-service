/*
 * 文件名：pbmnConstants.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：duanpengfei
 * 修改时间：2015年4月1日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.constant;

/**
 * 流程属性
 * 描述：记录流程相关的属性常量
 *  
 * @author duanpengfei
 * @version 2015年4月1日
 * @see BpmnConstants
 * @since
 */
public class BpmnConstants {
    /** 工作流属性文件*/
    public static final String IDS_PBMN_ADDRESS = "/properties/pbmn.properties";
    
    /*******************processDefinitionKey**************************/
    /** IDS通用流程*/
    public static final String BPMN_IDS = "idsProcess";
    
    /** 启动项目流程*/
    public static final String BPMN_START_PROJECT = "startProjectProcess";
    
    /** 暂停项目流程*/
    public static final String BPMN_PAUSE_PROJECT = "pauseProjectProcess";
    
    /** 恢复项目流程*/
    public static final String BPMN_RESUME_PROJECT = "resumeProjectProcess";
    
    /** 关闭项目流程*/
    public static final String BPMN_CLOSE_PROJECT = "closeProjectProcess";
    
    /** 文档审批流程*/
    public static final String BPMN_SUBMIT_DOCUMENT = "submitDocProcess";
    
    /** 计划模板流程*/
    public static final String BPMN_START_PLANTEMPLATE = "startPlanTemplateProcess";

    /** 项目模板流程*/
    public static final String BPMN_START_PROJTEMPLATE = "startProjTemplateProcess";
    
    /** 研发流程模板流程*/
    public static final String BPMN_START_TASKPROCTEMPLATE = "taskProcTemplateProcess";
    
    /** 计划下达流程*/
    public static final String BPMN_START_PLAN = "planAssignProcess";
    
    /** 计划批量下达流程*/
    public static final String BPMN_START_MASS_PLAN = "planAssignMassProcess";
    
    /** 流程任务批量下达流程*/
    public static final String BPMN_START_MASS_FLOWTASK = "flowtaskAssignMassProcess";
    
    /** 计划变更流程*/
    public static final String BPMN_CHANGE_PLAN = "planChangeProcess";
    
    /** 计划批量变更流程*/
    public static final String BPMN_CHANGE_MASS_PLAN = "planChangeMassProcess";
    
    /** 流程任务批量变更流程*/
    public static final String BPMN_CHANGE_MASS_FLOWTASK = "flowtaskChangeMassProcess";
    
    /** 流程任务批量变更流程*/
    public static final String BPMN_CHANGE_MASS_RDTASKFLOWTASK = "rdTaskFlowtaskChangeMassProcess";
    
    /** 任务完成反馈流程*/
    public static final String BPMN_START_TASKFEEDBACK = "taskFeedbackProcess";
    
    /** 基线流程*/
    public static final String BPMN_START_BASICLINE = "basicLineProcess";
    
    /** 计划变更申请流程*/
    public static final String BPMN_CHANGE_APLY = "changeApplyProcess";
    
    /** 风险问题上报流程*/
    public static final String BPMN_RISK_PROBLEM = "riskProblemProcess";

    /** 计划模板提交审批流程*/
    public static final String BPMN_STRAT_PLAN_TEMPLATE = "startPlanTemplateProcess";

    /** 计划模板流程*/
    public static final String BPMN_PLAN_TEMPLATE_ENTITYNAME = "PlanTemplate";
    
    /** 项目模板流程*/
    public static final String BPMN_PROJ_TEMPLATE_ENTITYNAME = "ProjTemplate";

    /** 页签组合模板流程*/
    public static final String BPMN_START_TABCBTEMPLATE = "startTabCbTemplateProcess";

    /**  页签模板流程*/
    public static final String BPMN_START_TABTEMPLATE = "startTabTemplateProcess";

    
    public static final String FLOW_PLAN_TEMPLATE_DEFAULT_BUSINESSTYPE = "default";
    
    /*******************processDefinitionKey**************************/
    
    
    /*******************processDefinitionDisplayname**************************/
    /** 启动项目流程*/
    public static final String BPMN_START_PROJECT_DISPLAYNAME = "启动项目流程";
    
    /** 暂停项目流程*/
    public static final String BPMN_PAUSE_PROJECT_DISPLAYNAME = "暂停项目审批";
    
    /** 恢复项目流程*/
    public static final String BPMN_RESUME_PROJECT_DISPLAYNAME = "恢复项目审批";
    
    /** 关闭项目流程*/
    public static final String BPMN_CLOSE_PROJECT_DISPLAYNAME = "关闭项目流程";
    
    /** 文档审批流程*/
    public static final String BPMN_SUBMIT_DOCUMENT_DISPLAYNAME = "文档审批流程";
    
    /** 计划下达流程*/
    public static final String BPMN_START_PLAN_DISPLAYNAME = "计划发布流程";
    
    /** 计划变更流程*/
    public static final String BPMN_CHANGE_PLAN_DISPLAYNAME = "计划变更流程"; 
    
    /** 任务完成反馈流程*/
    public static final String BPMN_FEEDBACK = "任务完工确认流程";
    
    /** 基线流程*/
    public static final String BPMN_START_BASICLINE_DISPLAYNAME = "基线提交";
    
    /** 计划变更申请流程*/
    public static final String BPMN_START_APPLY_CHANGENAME = "变更申请流程";
    
    /** 风险问题上报流程*/
    public static final String BPMN_RISK_PROBLEM_DISPLAYNAME = "问题处理流程";

    /** 风险问题上报流程*/
    public static final String BPMN_PLANTEMPLATE_APPLY_DISPLAYNAME = "计划模板提交审批流程";

    /** 风险问题上报流程*/
    public static final String BPMN_PROJECTTEMPLATE_APPLY_DISPLAYNAME = "项目模板提交审批流程";

    /** 页签模板提交审批流程*/
    public static final String BPMN_TABTEMPLATE_APPLY_DISPLAYNAME = "页签模板提交审批流程";

    /** 页签组合模板提交审批流程*/
    public static final String BPMN_TABCBTEMPLATE_APPLY_DISPLAYNAME = "页签组合模板提交审批流程";

    /*******************processDefinitionDisplayname**************************/
    
    
    /********************OID****************************/
    /** 项目对象的OID*/
    public static final String OID_PROJECT = "com.glaway.ids.project.projectmanager.entity.Project:";
    
    /** 计划对象的OID*/
    public static final String OID_PLAN = "com.glaway.ids.project.plan.entity.Plan:";
    
    /** 批量申请单对象的OID*/
    public static final String OID_APPROVEPLANFORM = "com.glaway.ids.project.plan.entity.ApprovePlanForm:";
    
    /** 申请单对象的OID*/
    public static final String OID_APPROVERDTASKFORM = "com.glaway.ids.rdflow.plan.entity.RDTaskApproveForm:";
    
    /** TaskActityInfo对象的OID*/
    public static final String OID_TASKACTITYINFO = "com.glaway.ids.rdtask.task.pbmn.activity.entity.TaskActityInfo:";
    
    /** 计划模板的OID*/
    public static final String OID_PLANTEMPLATE = "com.glaway.ids.project.plantemplate.entity.PlanTemplate:";
   
    /** 项目模板的OID*/
    public static final String OID_PROJECTTEMPLATE = "com.glaway.ids.project.projecttemplate.entity.ProjTemplate:";
    
    /** 研发流程模板对象的OID*/
    public static final String OID_TASKPROCTEMPLATE = "com.glaway.ids.rdtask.proctemplate.entity.TaskProcTemplate:";
    
    /** 计划基线对象的OID*/
    public static final String OID_BASICLINE = "com.glaway.ids.project.plan.entity.BasicLine:";
    
    /** 变更申请对象的OID*/
    public static final String OID_CHANGEAPPLY = "com.glaway.ids.project.plan.entity.PlanownerApplychangeInfo:";

    /** 页签组合模板的OID*/
    public static final String OID_TABCBTEMPLATE = "com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate:";

    /** 页签模板的OID*/
    public static final String OID_TABTEMPLATE = "com.glaway.ids.planGeneral.plantabtemplate.entity.TabTemplate:";

    /********************OID****************************/

    /** object_business_bpmn_link文档审批默认业务类型 */
    public static final String BUSINESSTYPE_DEFAULT= "default";

    /** 项目对象的OID*/
    public static final String TASKVO_REPFILETYPE = "REPFILETYPE";

    /** 向上移动节点*/
    public static final String DIRECTION_UP = "up";

    /** 向下移动节点*/
    public static final String DIRECTION_DOWN = "down";

    /********************BpmoTaskVo存放在Redis的名称****************************/
    public static final String FLOW_NAME_SUFFIX = "_文档类型流程";

    /** objectBusinessBpmnLink中实体名称*/
    public static final String BPMN_ENTITIY_NAME_PROJECT_FILE = "ProjectFile";

    /** 象业务类型流程对应信息业务类型前缀*/
    public static final String OBJECT_BUSINESS_BPMN_LINK_BUSINESSTYPE = "fileType:";

    /** 节点表单前缀*/
    public static final String EDIT_FORM_ID = "editForm";

    /** 编辑节点Id*/
    public static final String EDIT_TASK_ID = "editTask";

    /** bpmn模型转换默认编码*/
    public static final String BPMN_PROCESS_ID = "projFileProcess_";

    /** 编辑节点表单标题*/
    public static final String EDIT_FORM_NAME = "选人";

    /** 当前任务办理人变量前缀*/
    public static final String TASK_ROLE_VARIBALES_PREFIX = "leader";

    /** 编辑节点标题*/
    public static final String EDIT_TASK_TITLE = "编辑信息";

    /** 流程元素类型-任务节点*/
    public static final String FLOWTYPE_USERTASK = "usertask";

    /** 流程节点属性-办理人 */
    public static final String FLOWPROPERTY_ASSIGNEE = "assignee";

    /** 流程节点属性-表单 */
    public static final String FLOWPROPERTY_FORMKEY = "formKey";

    /** 审核节点前缀*/
    public static final String USER_TASK_PREFIX = "userTask";

    /** 排他网关前缀*/
    public static final String EXCLUSIVE_GATEWAY_PREFIX = "exclusivegateway";


    /** 编辑节点出去的连线名称（有监听）*/
    public static final String BPMN_LISTENER_SEQUENCEFLOW= "flowlsr";

    /** 同意连线标题*/
    public static final String FLOW_AGREE_NAME = "同意";

    /** 驳回连线标题*/
    public static final String FLOW_DISAGREE_NAME = "驳回";

    /** bpmn模型转换默认编码*/
    public static final String BPMN_CONVERT_ENCODING = "utf-8";


    /** 单人模式*/
    public static final String ASSIGN_MODE_SIGNLE = "singleSign";

    /** 竞争模式*/
    public static final String ASSIGN_MODE_VIE = "vieSign";

    /** 会签模式*/
    public static final String ASSIGN_MODE_COUNTER = "counterSign";

    /** 流程节点属性-连线出处 */
    public static final String FLOWPROPERTY_TARGETREF = "targetRef";

    /** 流程节点属性-连线出处 */
    public static final String FLOWPROPERTY_SUBELEMENT_LOOPCHARACTERISTICS = "loopCharacteristics";

    /** 项目模板拟制中状态 */
    public static final String PROJTEMPLATE_NIZHL = "nizhi";

    /** 项目模板审核中状态 */
    public static final String PROJTEMPLATE_SHENHE = "shenhe";

    /** 项目模板禁用中状态 */
    public static final String PROJTEMPLATE_JINYONG = "jinyong";

    /** 项目模板启用状态 */
    public static final String PROJTEMPLATE_QIYONG= "qiyong";

    /** 项目模板修订中状态 */
    public static final String PROJTEMPLATE_XIUDING= "xiuding";

    /** 项目模板修订中状态 */
    public static final String PROJTMP_USED= "1";

    
}
