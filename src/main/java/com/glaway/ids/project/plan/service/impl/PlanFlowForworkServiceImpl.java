package com.glaway.ids.project.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.dto.outwardextension.OutwardExtensionDto;
import com.glaway.foundation.fdk.dev.dto.outwardextension.OutwardExtensionUrlDto;
import com.glaway.foundation.fdk.dev.service.FeignOutwardExtensionService;
import com.glaway.foundation.fdk.dev.service.calendar.FeignCalendarService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.common.constant.CommonConfigConstants;
import com.glaway.ids.common.service.ResourceEverydayuseServiceI;
import com.glaway.ids.common.thread.ResourceEverydayuseInfosThread;
import com.glaway.ids.config.constant.TaskProcConstants;
import com.glaway.ids.constant.*;
import com.glaway.ids.message.notice.service.PlanChangeBackMsgNoticeI;
import com.glaway.ids.message.notice.service.PlanChangeMsgNoticeI;
import com.glaway.ids.models.HttpClientUtil;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.*;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.ProcessUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;


/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: PlanFlowForworkServiceImpl
 * @Date: 2019/7/24-9:25
 * @since
 */
@Service("planFlowForworkService")
@Transactional
public class PlanFlowForworkServiceImpl extends BusinessObjectServiceImpl<Plan> implements PlanFlowForworkServiceI {

    /**
     * Logger for this class
     */
    private static final SystemLog log = BaseLogFactory.getSystemLog(PlanFlowForworkServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    /**
     * 输入信息
     */
    @Autowired
    private InputsServiceI inputsService;

    @Autowired
    private WorkFlowFacade workFlowFacade;

    @Autowired
    private PlanServiceI planService;

    @Autowired
    private FeignUserService userService;

    /**
     * 资源每日使用信息处理
     */
    @Autowired
    private ResourceEverydayuseServiceI resourceEverydayuseService;

    @Autowired
    private Environment env;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private PlanChangeServiceI planChangeService;

    @Autowired
    private DeliverablesChangeServiceI deliverablesChangeService;

    @Autowired
    private ResourceChangeServiceI resourceChangeService;

    @Autowired
    private FeignOutwardExtensionService outwardExtensionService;


    @Autowired
    private PreposePlanServiceI preposePlanService;

    @Autowired
    private ResourceLinkInfoRemoteFeignServiceI resourceLinkInfoService;

    @Autowired
    private TaskFlowResolveServiceI taskFlowResolveService;


    @Autowired
    private BasicLineServiceI basicLineServiceI;

    /**
     *
     */
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 交付物信息
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;

    @Autowired
    private FeignCalendarService calendarService;

    /**
     * 接口
     */
    @Value(value="${spring.application.name}")
    private String appKey;

    /**
     *
     */
    private String message;

    /**
     * 注入消息Service
     */
    @Autowired
    private PlanChangeBackMsgNoticeI planChangeBackMsgNotice;

    /**
     * 获取消息接收人接口
     */
    @Autowired
    private PlanChangeMsgNoticeI planChangeMsgNotice;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void doAddDelDeliverForWork(String names, DeliverablesInfo deliverablesInfo) {
        for (String name : names.split(",")) {
            DeliverablesInfo deliverablesInfoTemp = new DeliverablesInfo();
            deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
            deliverablesInfoTemp.setId(null);
            deliverablesInfoTemp.setName(name);
            deliverablesInfoTemp.setUseObjectId(deliverablesInfo.getUseObjectId());
            deliverablesInfoTemp.setUseObjectType(deliverablesInfo.getUseObjectType());
            sessionFacade.save(deliverablesInfoTemp);
        }

    }

    @Override
    public void doBatchDelDeliverForWork(String ids) {
        for (String id : ids.split(",")) {
            // 删除其相关的输入
            inputsService.deleteInputsByOriginDeliverables(id, PlanConstants.USEOBJECT_TYPE_PLAN);
            // 删除输出
            deliverablesInfoService.deleteDeliverablesById(id);
        }
    }

    @Override
    public FeignJson doBatchDel(String ids) {
        FeignJson j = new FeignJson();
        message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.deleteResourceSuccess");
        try {

            doBatchDelResourceForWork(ids);

            log.info(message, ids, "");
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.deleteResourceFail");
            log.error(message, e, ids, "");
            Object[] params = new Object[] {message,
                    ResourceLinkInfo.class.getClass() + " oids:" + ids};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            // systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public void doBatchDelResourceForWork(String ids) {
        List<ResourceLinkInfo> list = new ArrayList<ResourceLinkInfo>();
        String useObjectId = "";
        for (String id : ids.split(",")) {
            ResourceLinkInfo resourceLinkInfo = (ResourceLinkInfo) sessionFacade.getEntity(
                    ResourceLinkInfo.class, id);
            list.add(resourceLinkInfo);
            if (StringUtils.isEmpty(useObjectId)
                    && StringUtils.isNotEmpty(resourceLinkInfo.getUseObjectId())) {
                useObjectId = resourceLinkInfo.getUseObjectId();
            }
            sessionFacade.delete(resourceLinkInfo);
        }

        // 在线程中更新资源每日使用信息
        ResourceEverydayuseInfosThread resourceEverydayuseInfosThread = new ResourceEverydayuseInfosThread();
        resourceEverydayuseInfosThread.setType(CommonConfigConstants.OBJECT_TYPE_RESOURCE);
        resourceEverydayuseInfosThread.setOperationType(CommonConfigConstants.RESOURCEEVERYDAYUSE_OPERATIONTYPE_DELETE);
        Plan plan = planService.getEntity(useObjectId);
        if (plan != null) {
            resourceEverydayuseInfosThread.setProjectId(plan.getProjectId());
        }
        resourceEverydayuseInfosThread.setLinkInfoList(list);
        resourceEverydayuseInfosThread.setResourceEverydayuseService(resourceEverydayuseService);
        new Thread(resourceEverydayuseInfosThread).start();

    }

    @Override
    public void deleteChildPlan(Plan plan, String userId) {
        TSUserDto user = userService.getUserByUserId(userId);
        Plan parentPlan = new Plan();
        String parentPlanId = plan.getId();
        parentPlan.setParentPlanId(plan.getId());
        List<Plan> planList = planService.queryPlanList(parentPlan, 1, 10, false);
        boolean needHttp = false;
        for (Plan p : planList) {
            if (!CommonUtil.isEmpty(p.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(p.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(p.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(p.getTaskNameType())) {
                needHttp = true;
                break;
            }
        }

        planService.deletePlansPhysics(planList);
        Plan planDb = planService.getEntity(plan.getId());
        planDb.setOpContent(PlanConstants.PLAN_DELETE_SPLIT);
        planDb.setFlowResolveXml("");
        sessionFacade.updateEntitie(planDb);

        PlanLog planLog = new PlanLog();
        planLog.setPlanId(plan.getId());
        planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_DELETE_SPLIT);

        planLog.setCreateBy(user.getId());
        planLog.setCreateName(user.getUserName());
        planLog.setCreateFullName(user.getRealName());
        planLog.setCreateTime(new Date());
        sessionFacade.save(planLog);

        if (needHttp) {
            List<String> httpUrls = new ArrayList<String>();
            List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"flowResolveCategoryHttpServer");
            if (!CommonUtil.isEmpty(outwardExtensionList)) {
                for (OutwardExtensionDto ext : outwardExtensionList) {
                    for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                        if ("repealFlowResolve".equals(out.getOperateCode())) {
                            httpUrls.add(out.getOperateUrl());
                        }
                    }
                }
            }
            if (!CommonUtil.isEmpty(httpUrls)) {
                for (String url : httpUrls) {
                    try {
                        HttpClientUtil.httpClientPostByTest(url + "&parentPlanId=" + parentPlanId,
                                null);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void startAssignForWork(String ids, String leader, String deptLeader,
                                   String assignType, TSUserDto actor, String assignId) {
        ApprovePlanForm approvePlanForm = new ApprovePlanForm();
        approvePlanForm.setApproveType("发布");
        sessionFacade.saveOrUpdate(approvePlanForm);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", actor.getUserName());
        FeignJson pi = new FeignJson();
        if (assignType != null && "single".equals(assignType)) {
            pi = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                    BpmnConstants.BPMN_START_PLAN, approvePlanForm.getId(), variables);
        }
        else {
            pi = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                    BpmnConstants.BPMN_START_MASS_PLAN, approvePlanForm.getId(), variables);
        }
        String procInstId = ""; // 流程实例ID
        if (pi.isSuccess()) {
            procInstId = pi.getObj() == null ? "" : pi.getObj().toString();
        }

        // 将procInstId存放到approvePlanForm中
        FeignJson taskIdFeignJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                procInstId);
        String taskId = "";

        if (taskIdFeignJson.isSuccess()) {
            taskId = taskIdFeignJson.getObj() == null ? "" : taskIdFeignJson.getObj().toString();
        }

        variables.put("cancelEventListener",
                "com.glaway.foundation.activiti.task.common.CommonCancelListener");
        variables.put("leader", leader);
        variables.put("deptLeader", deptLeader);
        variables.put("assigner", actor.getUserName());
        variables.put("editUrl", PlanConstants.URL_ASSIGNPROCESS_EDIT + approvePlanForm.getId());
        variables.put("viewUrl", PlanConstants.URL_ASSIGNPROCESS_VIEW + approvePlanForm.getId());
        variables.put("oid", BpmnConstants.OID_APPROVEPLANFORM + approvePlanForm.getId());
        variables.put("userId", actor.getId());

        //流程变量添加监听
        String planAssignUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/planAssignExcutionRestController/notify.do";
        variables.put("planAssign", planAssignUrl);
        variables.put("beginPlan", "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/beginPlanAssignExcutionRestController/notify.do");
        variables.put("planAssignBack", "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/planAssignBackExcutionRestController/notify.do");
        String stopPlanAssignUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/stopPlanAssignExcutionRestController/notify.do";
        variables.put("stopPlanAssignUrl", stopPlanAssignUrl);

        // 执行流程
        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

        FeignJson statusFeign = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = "";
        if (statusFeign.isSuccess()) {
            status = statusFeign.getObj() == null ? "" : statusFeign.getObj().toString();
        }

        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
        myStartedTask.setTaskNumber(approvePlanForm.getId());
        if (assignType != null && "single".equals(assignType)) {
            myStartedTask.setType(BpmnConstants.BPMN_START_PLAN);
        } else {
            myStartedTask.setType(BpmnConstants.BPMN_START_MASS_PLAN);
        }
        // 流程已发起列表中、增加对象名称的记录,流程任务名称规范化
        // 多个对象的批量审批任务，对象名称为null
        myStartedTask.setObjectName(null);
        myStartedTask.setTitle(ProcessUtil.getProcessTitle("",
                BpmnConstants.BPMN_START_PLAN_DISPLAYNAME, procInstId));
        // myStartedTask.setTitle(ProcessUtil.getProcessTitle("计划下达", "启动审批", procInstId));

        myStartedTask.setCreater(actor.getUserName());
        myStartedTask.setCreaterFullname(actor.getRealName());
        myStartedTask.setStartTime(new Date());

        if (assignType != null && "single".equals(assignType)) {
            myStartedTask.setProcType(BpmnConstants.BPMN_START_PLAN);
        } else {
            myStartedTask.setProcType(BpmnConstants.BPMN_START_MASS_PLAN);
        }

        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

        // 将procInstId保存到approvePlanForm信息中
        approvePlanForm.setProcInstId(procInstId);
        sessionFacade.saveOrUpdate(approvePlanForm);

        if (assignType != null && "single".equals(assignType)) {
            Plan plan = planService.getEntity(assignId);
            ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
            approvePlanInfo.setPlanId(plan.getId());
            approvePlanInfo.setFormId(approvePlanForm.getId());
            sessionFacade.saveOrUpdate(approvePlanInfo);
            plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_ORDERED);
            plan.setFormId(approvePlanForm.getId());
            plan.setLauncher(actor.getId());
            plan.setLaunchTime(new Date());
            sessionFacade.saveOrUpdate(plan);
        } else if (assignType != null && "mass".equals(assignType)) {
            for (String id : ids.split(",")) {
                Plan plan = planService.getEntity(id);
                if (plan != null) {
                    // 计划审批相关信息表中存放计划与approvePlanForm关系
                    ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                    approvePlanInfo.setPlanId(plan.getId());
                    approvePlanInfo.setFormId(approvePlanForm.getId());
                    sessionFacade.saveOrUpdate(approvePlanInfo);
                    plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_ORDERED);
                    plan.setFormId(approvePlanForm.getId());
                    plan.setLauncher(actor.getId());
                    plan.setLaunchTime(new Date());
                    sessionFacade.saveOrUpdate(plan);
                }
            }
        }
    }

    @Override
    public void startAssignForWorkFlow(String formId, String taskId, String leader,
                                       String deptLeader, String userId) {
        TSUserDto curUser = userService.getUserByUserId(userId);
        ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
        approvePlanInfo.setFormId(formId);

        List<ApprovePlanInfo> approve = planService.queryAssignList(approvePlanInfo, 1, 10, false);
        for (int i = 0; i < approve.size(); i++) {
            Plan p = planService.getEntity(approve.get(i).getPlanId());
            p.setIsAssignBack("false");
            sessionFacade.saveOrUpdate(p);
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", curUser.getUserName());
        if (!CommonUtil.isEmpty(leader)) {
            variables.put("leader", leader);
        }
        if (!CommonUtil.isEmpty(deptLeader)) {
            variables.put("deptLeader", deptLeader);
        }
        variables.put("userId", userId);
        // 执行流程
        TaskDto task = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
        String procInstId = task.getProcessInstanceId();
        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

        FeignJson nextTask = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = nextTask.getObj().toString();

        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                formId);
        myStartedTask.setEndTime(new Date());
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
    }

    @Override
    public void startAssignForWorkFlowOnTree(String formId, String taskId, String leader,
                                             String deptLeader, String userId) {
        ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
        approvePlanInfo.setFormId(formId);
        TSUserDto curUser = userService.getUserByUserId(userId);

        List<ApprovePlanInfo> approve = planService.queryAssignList(approvePlanInfo, 1, 10, false);
        for (int i = 0; i < approve.size(); i++) {
            Plan p = planService.getEntity(approve.get(i).getPlanId());
            p.setIsAssignSingleBack("false");
            sessionFacade.saveOrUpdate(p);
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("cancelEventListener",
                "com.glaway.foundation.activiti.task.common.CommonCancelListener");
        variables.put("user", curUser.getUserName());
        variables.put("leader", leader);
        variables.put("deptLeader", deptLeader);
        variables.put("userId", userId);

        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);
        // 执行流程
        TaskDto task = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
        String procInstId = task.getProcessInstanceId();

        FeignJson statusFj = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = "";
        if (statusFj.isSuccess()) {
            status = statusFj.getObj() == null ? "" : statusFj.getObj().toString();
        }

        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                formId);
        myStartedTask.setEndTime(new Date());
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
    }


    @Override
    public void stopPlanChangeAndSaveData(String oid, String userId) {
        if (oid == null || oid.equals("")) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        TSUserDto user = userService.getUserByUserId(userId);
        String id = arrays[1];
        String projectId = "";
        // 获取计划id
        String msg = "";
        ApprovePlanForm form = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, id);

        if (!PlanConstants.PLAN_APPROVE_TYPE_FLOWTASKCHANGE.equals(form.getApproveType())) {
            ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
            approvePlanInfo.setFormId(id);
            List<ApprovePlanInfo> approve = planService.queryAssignList(approvePlanInfo, 1, 10,
                    false);
            Map<String, String> planIdMap = new HashMap<String, String>();
            List<TemporaryPlan> temporaryPlanList = new ArrayList<TemporaryPlan>();
            TemporaryPlan temporaryPlan = new TemporaryPlan();
            for (ApprovePlanInfo a : approve) {
//                    TemporaryPlan plan = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class, a.getPlanId());
                temporaryPlan.setId(a.getPlanId());
                temporaryPlan.setFormId(id);
                a.setPlanId(a.getPlanId());
                sessionFacade.saveOrUpdate(a);
                if (StringUtils.isEmpty(planIdMap.get(a.getPlanId()))) {
                    msg = a.getPlanId();
                    try {
//                        planChangeMsgNotice.getAllMessage(msg, user);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    planIdMap.put(a.getPlanId(), a.getPlanId());
                }
                List<TemporaryPlan> temporaryPlanListTemp = planChangeService.queryTemporaryPlanList(
                        temporaryPlan, 1, 10, false);
                temporaryPlanList.add(temporaryPlanListTemp.get(0));

            }


            for (TemporaryPlan temporaryPlans : temporaryPlanList) {
                Plan plan = (Plan) sessionFacade.getEntity(Plan.class, temporaryPlans.getPlanId());
                if (StringUtils.isEmpty(projectId) && plan != null) {
                    projectId = plan.getProjectId();
                }

                PlanHistory saveplan = new PlanHistory();
                saveplan.setPlanId(plan.getId());
                saveplan.setCreateTime(new Date());
                saveplan.setChangeType(temporaryPlans.getChangeType());
                saveplan.setFormId(id);
                saveplan.setBizId(plan.getBizId());
                saveplan.setPlanType(plan.getPlanType());
                saveplan.setBizVersion(plan.getBizVersion());
                saveplan.setBizCurrent(plan.getBizCurrent());
                if(!CommonUtil.isEmpty(plan.getSecurityLevel())){
                    saveplan.setSecurityLevel(plan.getSecurityLevel());
                }else{
                    saveplan.setSecurityLevel(Short.valueOf("1"));
                }

                saveplan.setPlanCreateBy(plan.getCreateBy());
                saveplan.setPlanCreateTime(plan.getCreateTime());
                saveplan.setPlanUpdateBy(plan.getUpdateBy());
                saveplan.setPlanUpdateTime(plan.getUpdateTime());
                saveplan.setAssignTime(plan.getAssignTime());
                saveplan.setAvaliable(plan.getAvaliable());
                saveplan.setActualStartTime(plan.getActualStartTime());
                saveplan.setActualEndTime(plan.getActualEndTime());
                saveplan.setAssigner(plan.getAssigner());
                saveplan.setBeforePlanId(plan.getBeforePlanId());
                saveplan.setFlowStatus(plan.getFlowStatus());
                saveplan.setImplementation(plan.getImplementation());
                saveplan.setMilestone(plan.getMilestone());
                saveplan.setOwner(plan.getOwner());
                saveplan.setParentPlanId(plan.getParentPlanId());
                saveplan.setPlanEndTime(plan.getPlanEndTime());
                saveplan.setPlanLevel(plan.getPlanLevel());
                saveplan.setPlanName(plan.getPlanName());
                saveplan.setPlanNumber(plan.getPlanNumber());
                saveplan.setPlanOrder(plan.getPlanOrder());
                saveplan.setPlanStartTime(plan.getPlanStartTime());
                saveplan.setPreposeIds(plan.getPreposeIds());
                saveplan.setPreposePlans(plan.getPreposePlans());
                saveplan.setProgressRate(plan.getProgressRate());
                saveplan.setProjectId(plan.getProjectId());
                saveplan.setProjectStatus(plan.getProjectStatus());
                saveplan.setRemark(plan.getRemark());
                saveplan.setStoreyNo(plan.getStoreyNo());
                saveplan.setWorkTime(plan.getWorkTime());

                plan.setAvaliable(temporaryPlans.getAvaliable());
                plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                plan.setImplementation(temporaryPlans.getImplementation());
                plan.setMilestone(temporaryPlans.getMilestone());
                plan.setOwner(temporaryPlans.getOwner());
                plan.setPlanEndTime(temporaryPlans.getPlanEndTime());
                plan.setPlanLevel(temporaryPlans.getPlanLevel());
                plan.setPlanStartTime(temporaryPlans.getPlanStartTime());
                plan.setRemark(temporaryPlans.getRemark());
                plan.setWorkTime(temporaryPlans.getWorkTime());

                planService.updateBizCurrent(plan);
                //调用计划反馈引擎计算进度
                planService.updateProgressRate(plan);

                planService.saveOrUpdate(plan);
                sessionFacade.saveOrUpdate(saveplan);

                plan.setPreposeIds(temporaryPlans.getPreposeIds());

                if (StringUtils.isNotEmpty(plan.getPreposeIds())) {
                    // 删除其已有的前置
                    preposePlanService.removePreposePlansByPlanId(plan);
                    // 存放其新的前置计划
                    if (StringUtils.isNotEmpty(plan.getPreposeIds())) {
                        for (String preposeId : plan.getPreposeIds().split(",")) {
                                /*Plan preposePlan = (Plan) sessionFacade.getEntity(Plan.class, preposeId);
                                if (preposePlan != null) {*/
                            PreposePlan prepose = new PreposePlan();
                            prepose.setPlanId(plan.getId());
                            prepose.setPreposePlanId(preposeId);
                            sessionFacade.save(prepose);
                            /* }*/
                        }
                    }
                }

                if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(plan.getBizCurrent()) || PlanConstants.PLAN_FLOWSTATUS_ORDERED.equals(plan.getBizCurrent()) || PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                    planService.updateBizCurrent(plan);
                }

                if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(plan.getBizCurrent())){
                    String taskId = null;
                    if (StringUtils.isNotEmpty(plan.getPlanReceivedProcInstId())) {
                        FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                                plan.getPlanReceivedProcInstId());
                        if (feignJson.isSuccess()) {
                            taskId = feignJson.getObj() == null ? "" : feignJson.getObj().toString();
                        }
                        if (!CommonUtil.isEmpty(taskId)) {
                            workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                                    plan.getId(), plan.getPlanReceivedProcInstId(), "");

                            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                                    plan.getId());
                            workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask));
                            plan.setPlanReceivedProcInstId("");
                        }
                    }
                }

                sessionFacade.updateEntitie(plan);

                if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                    if(CommonUtil.isEmpty(plan.getPlanReceivedProcInstId())){
                        planService.startPlanReceivedProcess(plan,userId);
                    }else if(!CommonUtil.isEmpty(plan.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(plan.getPlanReceivedCompleteTime())){
                        planService.startPlanReceivedProcess(plan,userId);
                    }

                }


            }
        }
    }

    @Override
    public void startPlanChangeFlowForWork(String oid, String userId, String flowTaskIdStr, String flowTaskInputsIdAndRDTaskInputsIdStr, String flowTaskDeliversIdAndRDTaskDeliversIdStr) {
        {
            if (oid == null || oid.equals("")) {
                return;
            }
            String[] arrays = oid.split(":");
            if (arrays.length < 2) {
                return;
            }
            TSUserDto user = userService.getUserByUserId(userId);
            
            String id = arrays[1];
            Map<String, String> flowTaskIdMap = new HashMap<String, String>();
            Map<String, String> flowTaskIdStrMap = new HashMap<String, String>();
            if (!CommonUtil.isEmpty(flowTaskIdStr)) {
                String[] entity = flowTaskIdStr.split(";");
                for (int i = 0; i < entity.length; i++) {
                    String[] value = entity[i].split("=");
                    if (!CommonUtil.isEmpty(value)) {
                        flowTaskIdStrMap.put(value[0], value[1]);
                    }
                }
            }
            Map<String, String> flowTaskInputsIdAndRDTaskInputsIdStrMap = new HashMap<String, String>();
            if (!CommonUtil.isEmpty(flowTaskInputsIdAndRDTaskInputsIdStr)) {
                String[] entity = flowTaskInputsIdAndRDTaskInputsIdStr.split(";");
                for (int i = 0; i < entity.length; i++) {
                    String[] value = entity[i].split("=");
                    if (!CommonUtil.isEmpty(value)) {
                        flowTaskInputsIdAndRDTaskInputsIdStrMap.put(value[0], value[1]);
                    }
                }
            }
            Map<String, String> flowTaskDeliversIdAndRDTaskDeliversIdStrMap = new HashMap<String, String>();
            if (!CommonUtil.isEmpty(flowTaskDeliversIdAndRDTaskDeliversIdStr)) {
                String[] entity = flowTaskDeliversIdAndRDTaskDeliversIdStr.split(";");
                for (int i = 0; i < entity.length; i++) {
                    String[] value = entity[i].split("=");
                    if (!CommonUtil.isEmpty(value)) {
                        flowTaskDeliversIdAndRDTaskDeliversIdStrMap.put(value[0], value[1]);
                    }
                }
            }
            List<ResourceLinkInfo> resourcelinks = new ArrayList<ResourceLinkInfo>();
            String projectId = "";
            // 获取计划id
            String msg = "";
            ApprovePlanForm form = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, id);
            boolean reviewPluginValid = false;
            List<ServiceInstance> instances = discoveryClient.getInstances("ids-review-service");
            if (!CommonUtil.isEmpty(instances)) {
                reviewPluginValid = true;
            }
            if (!PlanConstants.PLAN_APPROVE_TYPE_FLOWTASKCHANGE.equals(form.getApproveType())) {
                ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                approvePlanInfo.setFormId(id);
                List<ApprovePlanInfo> approve = planService.queryAssignList(approvePlanInfo, 1, 10,
                        false);
                Map<String, String> planIdMap = new HashMap<String, String>();
                List<TemporaryPlan> temporaryPlanList = new ArrayList<TemporaryPlan>();
                TemporaryPlan temporaryPlan = new TemporaryPlan();
                for (ApprovePlanInfo a : approve) {
//                    TemporaryPlan plan = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class, a.getPlanId());
                    temporaryPlan.setId(a.getPlanId());
                    temporaryPlan.setFormId(id);
                    a.setPlanId(a.getPlanId());
                    sessionFacade.saveOrUpdate(a);
                    List<TemporaryPlan> temporaryPlanListTemp = planChangeService.queryTemporaryPlanList(
                            temporaryPlan, 1, 10, false);

                    temporaryPlanList.add(temporaryPlanListTemp.get(0));

                }

                TempPlanDeliverablesInfo deliverablesInfo = new TempPlanDeliverablesInfo();
                deliverablesInfo.setUseObjectId(id);
                deliverablesInfo.setFormId(id);
                List<TempPlanDeliverablesInfo> deliverablesInfoList = deliverablesChangeService.queryDeliverableChangeList(
                        deliverablesInfo, 1, 10, false);

                TempPlanInputs tempPlanInputs = new TempPlanInputs();
                tempPlanInputs.setUseObjectId(id);
                tempPlanInputs.setFormId(id);
                List<TempPlanInputs> inputList = deliverablesChangeService.queryInputChangeList(
                        tempPlanInputs, 1, 10, false);

                TempPlanResourceLinkInfo resourceLinkInfo = new TempPlanResourceLinkInfo();
                resourceLinkInfo.setUseObjectId(id);
                resourceLinkInfo.setFormId(id);
                List<TempPlanResourceLinkInfo> resourceLinkInfoList = resourceChangeService.queryResourceChangeList(
                        resourceLinkInfo, 1, 10, false);

                for (TemporaryPlan temporaryPlans : temporaryPlanList) {
                    Plan plan = (Plan) sessionFacade.getEntity(Plan.class, temporaryPlans.getPlanId());
                    if (StringUtils.isEmpty(projectId) && plan != null) {
                        projectId = plan.getProjectId();
                    }

                    PlanHistory saveplan = new PlanHistory();
                    saveplan.setPlanId(plan.getId());
                    saveplan.setCreateTime(new Date());
                    saveplan.setChangeType(temporaryPlans.getChangeType());
                    saveplan.setFormId(id);
                    saveplan.setBizId(plan.getBizId());
                    saveplan.setPlanType(plan.getPlanType());
                    saveplan.setBizVersion(plan.getBizVersion());
                    saveplan.setBizCurrent(plan.getBizCurrent());
                    if(!CommonUtil.isEmpty(plan.getSecurityLevel())){
                        saveplan.setSecurityLevel(plan.getSecurityLevel());
                    }else{
                        saveplan.setSecurityLevel(Short.valueOf("1"));
                    }

                    saveplan.setPlanCreateBy(plan.getCreateBy());
                    saveplan.setPlanCreateTime(plan.getCreateTime());
                    saveplan.setPlanUpdateBy(plan.getUpdateBy());
                    saveplan.setPlanUpdateTime(plan.getUpdateTime());
                    saveplan.setAssignTime(plan.getAssignTime());
                    saveplan.setAvaliable(plan.getAvaliable());
                    saveplan.setActualStartTime(plan.getActualStartTime());
                    saveplan.setActualEndTime(plan.getActualEndTime());
                    saveplan.setAssigner(plan.getAssigner());
                    saveplan.setBeforePlanId(plan.getBeforePlanId());
                    saveplan.setFlowStatus(plan.getFlowStatus());
                    saveplan.setImplementation(plan.getImplementation());
                    saveplan.setMilestone(plan.getMilestone());
                    saveplan.setOwner(plan.getOwner());
                    saveplan.setParentPlanId(plan.getParentPlanId());
                    saveplan.setPlanEndTime(plan.getPlanEndTime());
                    saveplan.setPlanLevel(plan.getPlanLevel());
                    saveplan.setPlanName(plan.getPlanName());
                    saveplan.setPlanNumber(plan.getPlanNumber());
                    saveplan.setPlanOrder(plan.getPlanOrder());
                    saveplan.setPlanStartTime(plan.getPlanStartTime());
                    saveplan.setPreposeIds(plan.getPreposeIds());
                    saveplan.setPreposePlans(plan.getPreposePlans());
                    saveplan.setProgressRate(plan.getProgressRate());
                    saveplan.setProjectId(plan.getProjectId());
                    saveplan.setProjectStatus(plan.getProjectStatus());
                    saveplan.setRemark(plan.getRemark());
                    saveplan.setStoreyNo(plan.getStoreyNo());
                    saveplan.setWorkTime(plan.getWorkTime());

                    plan.setAvaliable(temporaryPlans.getAvaliable());
                    plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                    plan.setImplementation(temporaryPlans.getImplementation());
                    plan.setMilestone(temporaryPlans.getMilestone());
                    plan.setOwner(temporaryPlans.getOwner());
                    plan.setPlanEndTime(temporaryPlans.getPlanEndTime());
                    plan.setPlanLevel(temporaryPlans.getPlanLevel());
                    plan.setPlanStartTime(temporaryPlans.getPlanStartTime());
                    plan.setRemark(temporaryPlans.getRemark());
                    plan.setWorkTime(temporaryPlans.getWorkTime());

                    planService.updateBizCurrent(plan);
                    //调用计划反馈引擎计算进度
                    planService.updateProgressRate(plan);



                    planService.deleteResourceByPlanId(plan.getId());
                    resourceEverydayuseService.delResourceUseByPlan(plan.getId());
                    planService.deleteDeliverablesByPlanId(plan.getId());
                    planService.saveOrUpdate(plan);
                    sessionFacade.saveOrUpdate(saveplan);

                    plan.setPreposeIds(temporaryPlans.getPreposeIds());

                    if (StringUtils.isNotEmpty(plan.getPreposeIds())) {
                        // 删除其已有的前置
                        preposePlanService.removePreposePlansByPlanId(plan);
                        // 存放其新的前置计划
                        if (StringUtils.isNotEmpty(plan.getPreposeIds())) {
                            for (String preposeId : plan.getPreposeIds().split(",")) {
                                /*Plan preposePlan = (Plan) sessionFacade.getEntity(Plan.class, preposeId);
                                if (preposePlan != null) {*/
                                PreposePlan prepose = new PreposePlan();
                                prepose.setPlanId(plan.getId());
                                prepose.setPreposePlanId(preposeId);
                                sessionFacade.save(prepose);
                               /* }*/
                            }
                        }
                    }

                    for (TempPlanDeliverablesInfo tempDeliverables : deliverablesInfoList) {
                        if (temporaryPlans.getPlanId().equals(tempDeliverables.getDeliverLinkId())) {
                            DeliverablesInfo deliverables = new DeliverablesInfo();
                            initBusinessObject(deliverables);
                            deliverables.setAvaliable(tempDeliverables.getAvaliable());
                            deliverables.setBizCurrent(tempDeliverables.getBizCurrent());
                            deliverables.setBizId(tempDeliverables.getBizId());
                            deliverables.setBizVersion(tempDeliverables.getBizVersion());
                            deliverables.setDocument(tempDeliverables.getDocument());
                            deliverables.setName(tempDeliverables.getName());
                            deliverables.setPolicy(tempDeliverables.getPolicy());
                            if(!CommonUtil.isEmpty(tempDeliverables.getSecurityLevel())){
                                deliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                            }else{
                                deliverables.setSecurityLevel(Short.valueOf("1"));
                            }
                            deliverables.setOrigin(tempDeliverables.getOrigin());
                            deliverables.setUseObjectId(temporaryPlans.getPlanId());
                            deliverables.setUseObjectType("PLAN");
                            sessionFacade.save(deliverables);

                            DeliverablesInfoHistory saveDeliverables = new DeliverablesInfoHistory();
                            saveDeliverables.setAvaliable(tempDeliverables.getAvaliable());
                            saveDeliverables.setBizCurrent(tempDeliverables.getBizCurrent());
                            saveDeliverables.setBizId(tempDeliverables.getBizId());
                            saveDeliverables.setBizVersion(tempDeliverables.getBizVersion());
                            saveDeliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                            saveDeliverables.setPlanCreateBy(tempDeliverables.getCreateBy());
                            saveDeliverables.setPlanCreateTime(tempDeliverables.getCreateTime());
                            saveDeliverables.setPlanUpdateBy(tempDeliverables.getUpdateBy());
                            saveDeliverables.setPlanUpdateName(tempDeliverables.getUpdateTime());
                            saveDeliverables.setBizVersion(tempDeliverables.getBizVersion());
                            saveDeliverables.setDocument(tempDeliverables.getDocument());
                            saveDeliverables.setName(tempDeliverables.getName());
                            if(!CommonUtil.isEmpty(tempDeliverables.getSecurityLevel())){
                                saveDeliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                            }else{
                                saveDeliverables.setSecurityLevel(Short.valueOf("1"));
                            }
                            saveDeliverables.setUseObjectId(temporaryPlans.getPlanId());
                            saveDeliverables.setUseObjectType("PLAN");
                            sessionFacade.save(saveDeliverables);
                        }
                    }
                    for (TempPlanResourceLinkInfo tempResourceLink : resourceLinkInfoList) {
                        if (temporaryPlans.getPlanId().equals(tempResourceLink.getResourceLinkId())) {
                            ResourceLinkInfo resourceLink = new ResourceLinkInfo();
                            initBusinessObject(resourceLink);
                            resourceLink.setAvaliable(tempResourceLink.getAvaliable());
                            resourceLink.setBizCurrent(tempResourceLink.getBizCurrent());
                            resourceLink.setBizId(tempResourceLink.getBizId());
                            resourceLink.setBizVersion(tempResourceLink.getBizVersion());
                            resourceLink.setPolicy(tempResourceLink.getPolicy());
                            resourceLink.setResourceId(tempResourceLink.getResourceId());
                            resourceLink.setResourceInfo(tempResourceLink.getResourceInfo());
                            resourceLink.setResourceName(tempResourceLink.getResourceName());
                            resourceLink.setResourceType(tempResourceLink.getResourceType());
                            resourceLink.setStartTime(tempResourceLink.getStartTime());
                            resourceLink.setEndTime(tempResourceLink.getEndTime());
                            if(!CommonUtil.isEmpty(tempResourceLink.getSecurityLevel())){
                                resourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                            }else{
                                resourceLink.setSecurityLevel(Short.valueOf("1"));
                            }

                            resourceLink.setUseObjectId(temporaryPlans.getPlanId());
                            resourceLink.setUseObjectType("PLAN");
                            resourceLink.setUseRate(tempResourceLink.getUseRate());
                            sessionFacade.save(resourceLink);
                            resourcelinks.add(resourceLink);

                            ResourceLinkInfoHistory saveResourceLink = new ResourceLinkInfoHistory();

                            saveResourceLink.setAvaliable(tempResourceLink.getAvaliable());
                            saveResourceLink.setBizCurrent(tempResourceLink.getBizCurrent());
                            saveResourceLink.setBizId(tempResourceLink.getBizId());
                            saveResourceLink.setBizVersion(tempResourceLink.getBizVersion());
                            saveResourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                            saveResourceLink.setPlanCreateBy(tempResourceLink.getCreateBy());
                            saveResourceLink.setPlanCreateTime(tempResourceLink.getCreateTime());
                            saveResourceLink.setPlanUpdateBy(tempResourceLink.getUpdateBy());
                            saveResourceLink.setPlanUpdateTime(tempResourceLink.getUpdateTime());
                            saveResourceLink.setResourceId(tempResourceLink.getResourceId());
                            saveResourceLink.setResourceInfo(tempResourceLink.getResourceInfo());
                            if(!CommonUtil.isEmpty(tempResourceLink.getSecurityLevel())){
                                saveResourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                            }else{
                                saveResourceLink.setSecurityLevel(Short.valueOf("1"));
                            }

                            saveResourceLink.setResourceName(tempResourceLink.getResourceName());
                            saveResourceLink.setResourceType(tempResourceLink.getResourceType());
                            saveResourceLink.setUseObjectId(temporaryPlans.getPlanId());
                            saveResourceLink.setUseObjectType("PLAN");
                            saveResourceLink.setUseRate(tempResourceLink.getUseRate());
                            sessionFacade.save(saveResourceLink);
                        }
                    }

                    for (TempPlanInputs tempResourceLink : inputList) {
                        if (temporaryPlans.getPlanId().equals(tempResourceLink.getInputId())) {
                            Inputs resourceLink = new Inputs();
                            // resourceLink.setInputId(tempResourceLink.getId());
                            resourceLink.setName(tempResourceLink.getName());
                            // resourceLink.setUseObjectId(tempResourceLink.getUseObjectId());
                            resourceLink.setUseObjectId(temporaryPlans.getPlanId());
                            resourceLink.setUseObjectType("PLAN");
                            resourceLink.setFileId(tempResourceLink.getFileId());
                            resourceLink.setOrigin(tempResourceLink.getOrigin());
                            resourceLink.setRequired(tempResourceLink.getRequired());
                            resourceLink.setDocId(tempResourceLink.getDocId());
                            resourceLink.setDocName(tempResourceLink.getDocName());
                            resourceLink.setOriginObjectId(tempResourceLink.getOriginObjectId());
                            resourceLink.setOriginDeliverablesInfoId(tempResourceLink.getOriginDeliverablesInfoId());
                            resourceLink.setChecked(tempResourceLink.getChecked());
                            resourceLink.setDocument(tempResourceLink.getDocument());
                            resourceLink.setOriginType(tempResourceLink.getOriginType());
                            resourceLink.setExt1(tempResourceLink.getExt1());
                            resourceLink.setExt2(tempResourceLink.getExt2());
                            resourceLink.setExt3(tempResourceLink.getExt3());
                            inputsService.saveOrUpdate(resourceLink);

                            InputsHistory inputsHistory = new InputsHistory();
                            inputsHistory.setName(tempResourceLink.getName());
                            inputsHistory.setDocument(tempResourceLink.getDocument());
                            inputsHistory.setFileId(tempResourceLink.getFileId());
                            inputsHistory.setInputsId(tempResourceLink.getId());
                            inputsHistory.setOrigin(tempResourceLink.getOrigin());
                            inputsHistory.setRequired(tempResourceLink.getRequired());
                            inputsHistory.setUseObjectId(tempResourceLink.getUseObjectId());
                            inputsHistory.setUseObjectType(tempResourceLink.getUseObjectType());
                            inputsService.saveOrUpdate(inputsHistory);
                        }
                    }

                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(plan.getBizCurrent()) || PlanConstants.PLAN_FLOWSTATUS_ORDERED.equals(plan.getBizCurrent()) || PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                        planService.updateBizCurrent(plan);
                    }

                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(plan.getBizCurrent())){
                        String taskId = null;
                        if (StringUtils.isNotEmpty(plan.getPlanReceivedProcInstId())) {
                            FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                                    plan.getPlanReceivedProcInstId());
                            if (feignJson.isSuccess()) {
                                taskId = feignJson.getObj() == null ? "" : feignJson.getObj().toString();
                            }
                            if (!CommonUtil.isEmpty(taskId)) {
                                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                                        plan.getId(), plan.getPlanReceivedProcInstId(), "");

                                MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                                        plan.getId());
                                workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask));
                                plan.setPlanReceivedProcInstId("");
                            }
                        }
                    }

                    sessionFacade.updateEntitie(plan);

                    if (StringUtils.isEmpty(planIdMap.get(plan.getId()))) {
                        msg = plan.getId();
                        try {
                            planChangeMsgNotice.getAllMessage(msg, user);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        planIdMap.put(plan.getId(), plan.getId());
                    }

                    if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                        if(CommonUtil.isEmpty(plan.getPlanReceivedProcInstId())){
                            planService.startPlanReceivedProcess(plan,userId);
                        }else if(!CommonUtil.isEmpty(plan.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(plan.getPlanReceivedCompleteTime())){
                            planService.startPlanReceivedProcess(plan,userId);
                        }

                    }


                }
            } else {

                Map<String, String> cellWorkTimeMap = new HashMap<String, String>();
                ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                approvePlanInfo.setFormId(id);
                String parentPlanId = "";
                List<ApprovePlanInfo> approve = planService.queryAssignList(approvePlanInfo, 1, 10,
                        false);

                for (int i = 0; i < approve.size(); i++) {

                    // FlowTask p = planService.getEntity(FlowTask.class, approve.get(i).getPlanId());
                    // changeFlowTaskList =
                    // taskFlowResolveService.getChangeFlowTaskList(flowTaskParent);
                    FlowTaskVo p = planService.getFlowTaskVo(approve.get(i).getPlanId());
                    if (p != null && StringUtils.isNotEmpty(p.getParentPlanId())) {
                        Plan curParent = (Plan) sessionFacade.getEntity(Plan.class, p.getParentPlanId());
                        if (!CommonUtil.isEmpty(curParent)) {
                            msg = p.getPlanId();
                            if (StringUtils.isNotEmpty(msg)) {
                                try {
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }
                        }
                    }

                }
                for (int i = 0; i < approve.size(); i++) {
                    // FlowTask p = planService.getEntity(FlowTask.class, approve.get(i).getPlanId());
                    // approve.get(i).setPlanId(p.getPlanId());
                    // saveOrUpdate(approve.get(i));
                    FlowTaskVo p = planService.getFlowTaskVo(approve.get(i).getPlanId());
                    if (p != null && StringUtils.isNotEmpty(p.getParentPlanId())) {
                        Plan curParent = (Plan) sessionFacade.getEntity(Plan.class, p.getParentPlanId());
                        if (!CommonUtil.isEmpty(curParent)) {
                            parentPlanId = p.getId();
                            break;
                        }
                        
                        
                    }
                }

                // FlowTaskParentVo flowTaskParent = planService.getEntity(FlowTaskParent.class,
                // parentPlanId);

                FlowTaskParentVo flowTaskParent = planService.getFlowTaskParentVoInfo(parentPlanId);

                Plan temPlan = new Plan();
                temPlan.setParentPlanId(parentPlanId);
                temPlan.setFormId(id);

                Map<String, String> oldIdCellIdMap = new HashMap<String, String>();
                List<FlowTaskVo> changeFlowTaskList = taskFlowResolveService.getChangeFlowTaskList(temPlan, userId);
                for (FlowTaskVo flowtask : changeFlowTaskList) {
                    oldIdCellIdMap.put(flowtask.getCellId(), flowtask.getId());
                }

                List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList = taskFlowResolveService.getChangeFlowTaskConnectList(temPlan);
                List<FlowTaskPreposeVo> flowTaskPreposeList = taskFlowResolveService.getChangeFlowTaskPreposeList(temPlan);

                // 更新父计划的flowResolveXml
                Plan parent = (Plan) sessionFacade.getEntity(Plan.class, flowTaskParent.getParentId());
                // 不删除临时表
                // 删除原有的FlowTaskCellConnect关系
                taskFlowResolveService.deleteFlowTaskCellConn(parent.getId());

                // 删除原有的前后置关系（主要针对内部前置）暂时不删除,可能会引起其他问题
                /*
                 * List<PreposePlan> preposePlanList =
                 * preposePlanService.getPreposePlansByParent(parent);
                 * for(PreposePlan prepose : preposePlanList){
                 * planService.deleteEntityById(PreposePlan.class, prepose.getId());
                 * }
                 */

                Plan condition = new Plan();
                condition.setParentPlanId(parent.getId());
                List<Plan> oldPlanList = planService.queryPlanList(condition, 1, 10, false);
                String changeType = "";
                for (FlowTaskVo flowTask : changeFlowTaskList) {
                    if (StringUtils.isNotEmpty(flowTask.getChangeType())) {
                        changeType = flowTask.getChangeType();
                        break;
                    }
                }

                // 备份历史记录
                for (Plan plan : oldPlanList) {
                    if (StringUtils.isEmpty(projectId) && plan != null) {
                        projectId = plan.getProjectId();
                    }
                    // 计划任务历史
                    PlanHistory saveplan = new PlanHistory();
                    saveplan.setPlanId(plan.getId());
                    saveplan.setCreateTime(new Date());
                    saveplan.setChangeType(changeType);
                    saveplan.setFormId(id);
                    saveplan.setBizId(plan.getBizId());
                    saveplan.setPlanType(plan.getPlanType());
                    saveplan.setBizVersion(plan.getBizVersion());
                    saveplan.setBizCurrent(plan.getBizCurrent());
                    if(!CommonUtil.isEmpty(plan.getSecurityLevel())){
                        saveplan.setSecurityLevel(plan.getSecurityLevel());
                    }else{
                        saveplan.setSecurityLevel(Short.valueOf("1"));
                    }

                    saveplan.setPlanCreateBy(plan.getCreateBy());
                    saveplan.setPlanCreateTime(plan.getCreateTime());
                    saveplan.setPlanUpdateBy(plan.getUpdateBy());
                    saveplan.setPlanUpdateTime(plan.getUpdateTime());
                    saveplan.setAssignTime(plan.getAssignTime());
                    saveplan.setAvaliable(plan.getAvaliable());
                    saveplan.setActualStartTime(plan.getActualStartTime());
                    saveplan.setActualEndTime(plan.getActualEndTime());
                    saveplan.setAssigner(plan.getAssigner());
                    saveplan.setBeforePlanId(plan.getBeforePlanId());
                    saveplan.setFlowStatus(plan.getFlowStatus());
                    saveplan.setImplementation(plan.getImplementation());
                    saveplan.setMilestone(plan.getMilestone());
                    saveplan.setOwner(plan.getOwner());
                    saveplan.setParentPlanId(plan.getParentPlanId());
                    saveplan.setPlanEndTime(plan.getPlanEndTime());
                    saveplan.setPlanLevel(plan.getPlanLevel());
                    saveplan.setPlanName(plan.getPlanName());
                    saveplan.setPlanNumber(plan.getPlanNumber());
                    saveplan.setPlanOrder(plan.getPlanOrder());
                    saveplan.setPlanStartTime(plan.getPlanStartTime());
                    saveplan.setPreposeIds(plan.getPreposeIds());
                    saveplan.setPreposePlans(plan.getPreposePlans());
                    saveplan.setProgressRate(plan.getProgressRate());
                    saveplan.setProjectId(plan.getProjectId());
                    saveplan.setProjectStatus(plan.getProjectStatus());
                    saveplan.setRemark(plan.getRemark());
                    saveplan.setStoreyNo(plan.getStoreyNo());
                    saveplan.setWorkTime(plan.getWorkTime());
                    saveplan.setTaskType(plan.getTaskType());
                    saveplan.setTaskNameType(!CommonUtil.isEmpty(plan.getTaskNameType()) ? plan.getTaskNameType() : CommonConstants.NAMESTANDARD_TYPE_DEV);

                    sessionFacade.save(saveplan);


                    // 输入历史
                    Inputs inputs = new Inputs();
                    inputs.setUseObjectId(plan.getId());
                    inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                    List<Inputs> inputList = inputsService.queryInputList(inputs, 1, 10, false);
                    for (Inputs input : inputList) {
                        InputsHistory inputsHistory = new InputsHistory();
                        inputsHistory.setName(input.getName());
                        inputsHistory.setDocument(input.getDocument());
                        inputsHistory.setFileId(input.getFileId());
                        inputsHistory.setInputsId(input.getId());
                        inputsHistory.setOrigin(input.getOrigin());
                        inputsHistory.setRequired(input.getRequired());
                        inputsHistory.setUseObjectId(input.getUseObjectId());
                        inputsHistory.setUseObjectType(input.getUseObjectType());
                        inputsHistory.setOriginDeliverablesInfoId(input.getOriginDeliverablesInfoId());
                        inputsHistory.setOriginObjectId(input.getOriginObjectId());
                        inputsHistory.setOriginType(input.getOriginType());
                        inputsHistory.setOriginTypeExt(input.getOriginTypeExt());
                        inputsService.saveOrUpdate(inputsHistory);
                    }

                    // 输出历史
                    List<DeliverablesInfo> deliverablesInfoList = deliverablesInfoService.getDeliverablesByUseObeject(
                            PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());
                    for (DeliverablesInfo tempDeliverables : deliverablesInfoList) {
                        DeliverablesInfoHistory saveDeliverables = new DeliverablesInfoHistory();
                        saveDeliverables.setAvaliable(tempDeliverables.getAvaliable());
                        saveDeliverables.setBizCurrent(tempDeliverables.getBizCurrent());
                        saveDeliverables.setBizId(tempDeliverables.getBizId());
                        saveDeliverables.setBizVersion(tempDeliverables.getBizVersion());
                        if(!CommonUtil.isEmpty(tempDeliverables.getSecurityLevel())){
                            saveDeliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                        }else{
                            saveDeliverables.setSecurityLevel(Short.valueOf("1"));
                        }

                        saveDeliverables.setPlanCreateBy(tempDeliverables.getCreateBy());
                        saveDeliverables.setPlanCreateTime(tempDeliverables.getCreateTime());
                        saveDeliverables.setPlanUpdateBy(tempDeliverables.getUpdateBy());
                        saveDeliverables.setPlanUpdateName(tempDeliverables.getUpdateTime());
                        saveDeliverables.setBizVersion(tempDeliverables.getBizVersion());
                        saveDeliverables.setDocument(tempDeliverables.getDocument());
                        saveDeliverables.setName(tempDeliverables.getName());
                        saveDeliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                        saveDeliverables.setUseObjectId(tempDeliverables.getUseObjectId());
                        saveDeliverables.setUseObjectType(tempDeliverables.getUseObjectType());
                        sessionFacade.saveOrUpdate(saveDeliverables);

                    }

                    // 资源历史
                    ResourceLinkInfoDto resourceLinkInfo = new ResourceLinkInfoDto();
                    resourceLinkInfo.setUseObjectId(plan.getId());
                    resourceLinkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                    List<ResourceLinkInfoDto> resourceLinkInfoList = resourceLinkInfoService.getList(resourceLinkInfo);
                    for (ResourceLinkInfoDto tempResourceLink : resourceLinkInfoList) {
                        ResourceLinkInfoHistory saveResourceLink = new ResourceLinkInfoHistory();
                        ;
                        saveResourceLink.setAvaliable(tempResourceLink.getAvaliable());
                        saveResourceLink.setBizCurrent(tempResourceLink.getBizCurrent());
                        saveResourceLink.setBizId(tempResourceLink.getBizId());
                        saveResourceLink.setBizVersion(tempResourceLink.getBizVersion());
                        if(!CommonUtil.isEmpty(tempResourceLink.getSecurityLevel())){
                            saveResourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                        }else{
                            saveResourceLink.setSecurityLevel(Short.valueOf("1"));
                        }
                        saveResourceLink.setPlanCreateBy(tempResourceLink.getCreateBy());
                        saveResourceLink.setPlanCreateTime(tempResourceLink.getCreateTime());
                        saveResourceLink.setPlanUpdateBy(tempResourceLink.getUpdateBy());
                        saveResourceLink.setPlanUpdateTime(tempResourceLink.getUpdateTime());
                        saveResourceLink.setResourceId(tempResourceLink.getResourceId());
                        Resource resource = new Resource();
                        try {
                            BeanUtil.copyBean2Bean(resource,tempResourceLink.getResourceInfo());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        saveResourceLink.setResourceInfo(resource);
                        if(!CommonUtil.isEmpty(tempResourceLink.getSecurityLevel())){
                            saveResourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                        }else{
                            saveResourceLink.setSecurityLevel(Short.valueOf("1"));
                        }
                        saveResourceLink.setResourceName(tempResourceLink.getResourceName());
                        saveResourceLink.setResourceType(tempResourceLink.getResourceType());
                        saveResourceLink.setUseObjectId(tempResourceLink.getUseObjectId());
                        saveResourceLink.setUseObjectType(tempResourceLink.getUseObjectType());
                        saveResourceLink.setUseRate(tempResourceLink.getUseRate());
                        sessionFacade.saveOrUpdate(saveResourceLink);
                    }
                }

                // 输入来源计划id
                Map<String, String> originObjectMap = new HashMap<String, String>();
                // 输入来源输出id
                Map<String, String> originOutputMap = new HashMap<String, String>();
                Map<String, String> changePlanMap = new HashMap<String, String>();

                List<FlowTaskPreposeVo> allflowTaskPreposeList = new ArrayList<FlowTaskPreposeVo>();

                for (FlowTaskVo flowTask : changeFlowTaskList) {
                    if (StringUtils.isNotEmpty(flowTask.getPlanId())) {
                        changePlanMap.put(flowTask.getPlanId(), flowTask.getPlanId());
                    }
                    // List<FlowTaskPrepose> flowTaskinnerPreposeList =
                    // taskFlowResolveService.getChangeFlowTaskPreposeList(TemPlan);
                    List<FlowTaskPreposeVo> curList = taskFlowResolveService.getChangeFlowTaskOutPreposeList(flowTask);
                    if (!CommonUtil.isEmpty(curList)) {
                        allflowTaskPreposeList.addAll(curList);
                    }

                }

                List<Plan> delList = new ArrayList<Plan>();
                for (Plan plan : oldPlanList) {
                    if (StringUtils.isEmpty(changePlanMap.get(plan.getId()))) {
                        delList.add(plan);
                    }
                }

//            planService.deletePlans(delList);
                for (Plan p : delList) {
                    planService.discardPlansForFlow(p.getId(), user);
                }

                List<Plan> saveInputsBatchPlans = new ArrayList<Plan>();
                List<Inputs> saveInputsBatchInputs = new ArrayList<Inputs>();
                List<DeliverablesInfo> saveInputsBatchDelivers = new ArrayList<DeliverablesInfo>();

                List<Inputs> newInputList = new ArrayList<Inputs>();
                for (FlowTaskVo flowTask : changeFlowTaskList) {
                    if (StringUtils.isNotEmpty(flowTask.getPlanId())) {
                        Plan plan = (Plan) sessionFacade.getEntity(Plan.class, flowTask.getPlanId());
                        if (plan != null) {
                            // 更新计划
                            plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                            plan.setOwner(flowTask.getOwner());
                            plan.setCellId(flowTask.getCellId());
                            plan.setPlanName(flowTask.getPlanName());
                            plan.setPlanEndTime(flowTask.getPlanEndTime());
                            plan.setPlanLevel(flowTask.getPlanLevel());
                            plan.setPlanStartTime(flowTask.getPlanStartTime());
                            plan.setRemark(flowTask.getRemark());
                            plan.setWorkTime(flowTask.getWorkTime());
                            plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                            plan.setTaskNameType(!CommonUtil.isEmpty(flowTask.getTaskNameType()) ? flowTask.getTaskNameType() : CommonConstants.NAMESTANDARD_TYPE_DEV);
                            planService.updateBizCurrent(plan);
                            //调用计划反馈引擎计算进度
                            planService.updateProgressRate(plan);
                            sessionFacade.saveOrUpdate(plan);
                            originObjectMap.put(flowTask.getId(), plan.getId());
                            String label = plan.getPlanName() + "," + plan.getWorkTime() + "天";
                            cellWorkTimeMap.put(plan.getCellId(), label);
                            // 删除该计划的所有前置关系（主要针对外部前置）
                            List<PreposePlan> currentPlanPreposes = preposePlanService.getPreposePlansByPlanId(plan);
                            if (!CommonUtil.isEmpty(currentPlanPreposes)) {
                                sessionFacade.deleteAllEntitie(currentPlanPreposes);
                            }

                            flowTaskIdMap.put(flowTask.getId(), plan.getId());
                            // 更新计划的输入
                            List<Inputs> delInList = new ArrayList<Inputs>();
                            Map<String, String> inMap = new HashMap<String, String>();
                            for (FlowTaskInputsVo inputs : flowTask.getInputList()) {
                                if (StringUtils.isNotEmpty(inputs.getInputId())) {
                                    inMap.put(inputs.getInputId(), inputs.getInputId());
                                }
                            }

                            Inputs in = new Inputs();
                            in.setUseObjectId(plan.getId());
                            in.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                            List<Inputs> inputList = inputsService.queryInputList(in, 1, 10, false);
                            for (Inputs input : inputList) {
                                if (StringUtils.isEmpty(inMap.get(input.getId()))) {
                                    delInList.add(input);
                                }
                            }
                            // 不删除临时表
                            for (Inputs input : delInList) {
                                sessionFacade.delete(input);
                            }

                            for (FlowTaskInputsVo inputs : flowTask.getInputList()) {
                                if (StringUtils.isNotEmpty(inputs.getInputId())) {
                                    Inputs input = (Inputs) sessionFacade.getEntity(Inputs.class,
                                            inputs.getInputId());
                                    boolean flag = false;
                                    if (input == null) {
                                        input = new Inputs();
                                        input.setCreateBy(inputs.getCreateBy());
                                        input.setCreateFullName(inputs.getCreateFullName());
                                        input.setCreateName(inputs.getCreateName());
                                        input.setCreateTime(inputs.getCreateTime());
                                        flag = true;
                                    }
                                    input.setName(inputs.getName());
                                    input.setUseObjectId(plan.getId());
                                    input.setDocId(inputs.getDocId());
                                    input.setDocName(inputs.getDocName());
                                    if (StringUtils.isNotEmpty(inputs.getFileId())) {

                                        input.setFileId(inputs.getFileId());
                                    }
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setOrigin(inputs.getOrigin());
                                    input.setOriginObjectId(inputs.getOriginObjectId());
                                    input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                    input.setOriginType(inputs.getOriginType());
                                    input.setOriginTypeExt(inputs.getOriginTypeExt());
//                                    if (flag && !CommonUtil.isEmpty(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()))) {
//                                        input.setId(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()));
//                                        saveInputsBatchInputs.add(input);
//                                    } else {
//                                        inputsService.saveOrUpdate(input);
//                                    }
                                    inputsService.saveOrUpdate(input);
                                    newInputList.add(input);
                                } else {
                                    Inputs input = new Inputs();
                                    input.setName(inputs.getName());
                                    input.setUseObjectId(plan.getId());
                                    input.setCreateBy(inputs.getCreateBy());
                                    input.setCreateFullName(inputs.getCreateFullName());
                                    input.setCreateName(inputs.getCreateName());
                                    input.setCreateTime(inputs.getCreateTime());
                                    input.setDocId(inputs.getDocId());
                                    input.setDocName(inputs.getDocName());
                                    input.setOriginObjectId(inputs.getOriginObjectId());
                                    input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                    input.setOriginType(inputs.getOriginType());
                                    input.setOriginTypeExt(inputs.getOriginTypeExt());
                                    if (StringUtils.isNotEmpty(inputs.getFileId())) {
                                        input.setFileId(inputs.getFileId());
                                    }
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setOrigin(inputs.getOrigin());
//                                    if (!CommonUtil.isEmpty(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()))) {
//                                        input.setId(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()));
//                                        saveInputsBatchInputs.add(input);
//                                    }
                                    inputsService.save(input);
                                    newInputList.add(input);
                                }
                            }

                            // 更新计划的输出
                            List<DeliverablesInfo> delOutList = new ArrayList<DeliverablesInfo>();
                            Map<String, String> outMap = new HashMap<String, String>();
                            for (FlowTaskDeliverablesInfoVo outputs : flowTask.getOutputList()) {
                                if (StringUtils.isNotEmpty(outputs.getOutputId())) {
                                    outMap.put(outputs.getOutputId(), outputs.getOutputId());
                                }
                            }

                            List<DeliverablesInfo> outputList = deliverablesInfoService.getDeliverablesByUseObeject(
                                    PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());
                            for (DeliverablesInfo output : outputList) {
                                if (StringUtils.isEmpty(outMap.get(output.getId()))) {
                                    delOutList.add(output);
                                }
                            }
                            // 不删除临时表
                            for (DeliverablesInfo output : delOutList) {
                                sessionFacade.delete(output);
                            }

                            for (FlowTaskDeliverablesInfoVo outputs : flowTask.getOutputList()) {
                                if (StringUtils.isNotEmpty(outputs.getOutputId())) {
                                    DeliverablesInfo output = (DeliverablesInfo) sessionFacade.getEntity(
                                            DeliverablesInfo.class, outputs.getOutputId());
                                    if (output != null) {
                                    output.setName(outputs.getName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputs.getRequired());
                                    sessionFacade.saveOrUpdate(output);
                                    originOutputMap.put(outputs.getId(), output.getId());
                                    }
                                } else {
                                    DeliverablesInfo output = new DeliverablesInfo();
                                    planService.initBusinessObject(output);
                                    output.setCreateBy(outputs.getCreateBy());
                                    output.setCreateFullName(outputs.getCreateFullName());
                                    output.setCreateName(outputs.getCreateName());
                                    output.setCreateTime(outputs.getCreateTime());
                                    output.setName(outputs.getName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputs.getRequired());
//                                    if (!CommonUtil.isEmpty(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()))) {
//                                        output.setId(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()));
//                                        saveInputsBatchDelivers.add(output);
//                                    }
                                    sessionFacade.save(output);
                                    originOutputMap.put(outputs.getId(), output.getId());
                                }
                            }

                            // 更新计划的资源
                            List<ResourceLinkInfoDto> delResourceList = new ArrayList<>();
                            Map<String, String> resourceMap = new HashMap<String, String>();
                            for (FlowTaskResourceLinkInfoVo resource : flowTask.getResourceLinkList()) {
                                if (StringUtils.isNotEmpty(resource.getLinkInfoId())) {
                                    resourceMap.put(resource.getLinkInfoId(), resource.getLinkInfoId());
                                }
                            }

                            ResourceLinkInfoDto resourceLinkInfo = new ResourceLinkInfoDto();
                            resourceLinkInfo.setUseObjectId(plan.getId());
                            resourceLinkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                            List<ResourceLinkInfoDto> resourceList = resourceLinkInfoService.getList(resourceLinkInfo);
                            for (ResourceLinkInfoDto resource : resourceList) {
                                if (StringUtils.isEmpty(resourceMap.get(resource.getId()))) {
                                    delResourceList.add(resource);
                                }
                            }
                            // 不删除临时表
                            for (ResourceLinkInfoDto resource : delResourceList) {
                                sessionFacade.delete(resource);
                                ResourceEverydayuseInfo info = new ResourceEverydayuseInfo();
                                info.setUseObjectId(resource.getUseObjectId());
                                info.setUseObjectType(resource.getUseObjectType());
//                            resourceEverydayuseService.deleteResourceEverydayuseInfos(info);
                            }

                            for (FlowTaskResourceLinkInfoVo resourceLink : flowTask.getResourceLinkList()) {
                                if (StringUtils.isNotEmpty(resourceLink.getLinkInfoId())) {
                                    ResourceLinkInfo resource = (ResourceLinkInfo) sessionFacade.getEntity(
                                            ResourceLinkInfo.class, resourceLink.getLinkInfoId());
                                    resource.setResourceId(resourceLink.getResourceId());
                                    resource.setUseObjectId(plan.getId());
                                    resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    resource.setUseRate(resourceLink.getUseRate());
                                    resource.setStartTime(resourceLink.getStartTime());
                                    resource.setEndTime(resourceLink.getEndTime());
                                    sessionFacade.saveOrUpdate(resource);
                                    resourcelinks.add(resource);
                                } else {
                                    ResourceLinkInfo resource = new ResourceLinkInfo();
                                    initBusinessObject(resource);
                                    resource.setResourceId(resourceLink.getResourceId());
                                    resource.setUseObjectId(plan.getId());
                                    resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    resource.setUseRate(resourceLink.getUseRate());
                                    resource.setStartTime(resourceLink.getStartTime());
                                    resource.setEndTime(resourceLink.getEndTime());
                                    sessionFacade.save(resource);
                                    resourcelinks.add(resource);

                                }
                            }

                        } else {
                            // 新增
                            plan = new Plan();                            
                            plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                            plan.setCellId(flowTask.getCellId());
                            plan.setOwner(flowTask.getOwner());
                            plan.setPlanName(flowTask.getPlanName());
                            plan.setPlanEndTime(flowTask.getPlanEndTime());
                            plan.setPlanLevel(flowTask.getPlanLevel());
                            plan.setPlanStartTime(flowTask.getPlanStartTime());
                            plan.setRemark(flowTask.getRemark());
                            plan.setWorkTime(flowTask.getWorkTime());
                            plan.setPlanType(PlanConstants.PLAN_TYPE_FLOW);
                            plan.setProjectId(parent.getProjectId());
                            plan.setParentPlanId(parent.getId());
                            plan.setCreateBy(flowTask.getCreateBy());
                            plan.setCreateTime(flowTask.getCreateTime());
                            plan.setCreateFullName(flowTask.getCreateFullName());
                            plan.setCreateName(flowTask.getCreateName());
                            plan.setFormId(id);
                            if (StringUtils.isNotEmpty(flowTask.getLauncher())) {
                                plan.setAssigner(flowTask.getLauncher());
                            } else {
                                plan.setAssigner(parent.getOwner());
                            }

                            plan.setAssignTime(new Date());
                            plan.setLauncher(plan.getCreateBy());
                            plan.setLaunchTime(new Date());

                            // if(planService.isReviewTask(flowTask.getPlanName())){
                            // plan.setTaskNameType("评审任务");
                            // }
                            plan.setPlanNumber(planService.getMaxPlanNumber(plan) + 1);
                            plan.setStoreyNo(planService.getMaxStoreyNo(plan) + 1);
                            plan.setBizCurrent(PlanConstants.PLAN_FLOWSTATUS_ORDERED);
                            planService.updateBizCurrent(plan);
                            //调用计划反馈引擎计算进度
                            planService.updateProgressRate(plan);
//                            if (!CommonUtil.isEmpty(flowTaskIdStrMap.get(flowTask.getId()))) {
//                                plan.setId(flowTaskIdStrMap.get(flowTask.getId()));
//                                saveInputsBatchPlans.add(plan);
//                            }
                            sessionFacade.saveOrUpdate(plan);
//                        forwardBusinessObject(plan);
                            planService.saveFlowTaskAsCreate(plan, user);
                            originObjectMap.put(flowTask.getId(), plan.getId());
                            flowTaskIdMap.put(flowTask.getId(), plan.getId());
                            String label = plan.getPlanName() + "," + plan.getWorkTime() + "天";
                            cellWorkTimeMap.put(plan.getCellId(), label);

                            // 新增计划的输入
                            for (FlowTaskInputsVo inputs : flowTask.getInputList()) {
                                if (StringUtils.isNotEmpty(inputs.getInputId())) {
                                    Inputs input = (Inputs) sessionFacade.getEntity(Inputs.class, inputs.getId());
                                    input.setName(inputs.getName());
                                    input.setUseObjectId(plan.getId());
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setOrigin(inputs.getOrigin());
                                    input.setOriginObjectId(inputs.getOriginObjectId());
                                    input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                    input.setDocId(inputs.getDocId());
                                    input.setDocName(inputs.getDocName());
                                    input.setOriginType(inputs.getOriginType());
                                    input.setOriginTypeExt(inputs.getOriginTypeExt());
                                    inputsService.saveOrUpdate(input);
                                    newInputList.add(input);
                                } else {
                                    Inputs input = new Inputs();
                                    input.setName(inputs.getName());
                                    input.setUseObjectId(plan.getId());
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setOrigin(inputs.getOrigin());
                                    input.setOriginObjectId(inputs.getOriginObjectId());
                                    input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                    input.setCreateBy(inputs.getCreateBy());
                                    input.setCreateFullName(inputs.getCreateFullName());
                                    input.setCreateName(inputs.getCreateName());
                                    input.setCreateTime(inputs.getCreateTime());
                                    input.setDocId(inputs.getDocId());
                                    input.setDocName(inputs.getDocName());
                                    input.setOriginType(inputs.getOriginType());
                                    input.setOriginTypeExt(inputs.getOriginTypeExt());
                                    inputsService.save(input);
//                                    if (!CommonUtil.isEmpty(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()))) {
//                                        input.setId(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()));
//                                        saveInputsBatchInputs.add(input);
//                                    }
                                    newInputList.add(input);
                                }
                            }

                            // 新增计划的输出
                            for (FlowTaskDeliverablesInfoVo outputs : flowTask.getOutputList()) {
                                if (StringUtils.isNotEmpty(outputs.getOutputId())) {
                                    DeliverablesInfo output = (DeliverablesInfo) sessionFacade.getEntity(
                                            DeliverablesInfo.class, outputs.getId());
                                    output.setName(outputs.getName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputs.getRequired());
                                    sessionFacade.saveOrUpdate(output);
                                    originOutputMap.put(outputs.getId(), output.getId());
                                } else {
                                    DeliverablesInfo output = new DeliverablesInfo();
                                    planService.initBusinessObject(output);
                                    output.setCreateBy(outputs.getCreateBy());
                                    output.setCreateFullName(outputs.getCreateFullName());
                                    output.setCreateName(outputs.getCreateName());
                                    output.setCreateTime(outputs.getCreateTime());
                                    output.setName(outputs.getName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputs.getRequired());
//                                    if (!CommonUtil.isEmpty(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()))) {
//                                        output.setId(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()));
//                                        saveInputsBatchDelivers.add(output);
//                                    }
                                    sessionFacade.save(output);
                                    originOutputMap.put(outputs.getId(), output.getId());

                                }
                            }

                            // 新增计划的资源
                            for (FlowTaskResourceLinkInfoVo resourceLink : flowTask.getResourceLinkList()) {
                                if (StringUtils.isNotEmpty(resourceLink.getLinkInfoId())) {
                                    ResourceLinkInfo resource = (ResourceLinkInfo) sessionFacade.getEntity(
                                            ResourceLinkInfo.class, resourceLink.getLinkInfoId());
                                    resource.setResourceId(resourceLink.getResourceId());
                                    resource.setUseObjectId(plan.getId());
                                    resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    resource.setUseRate(resourceLink.getUseRate());
                                    resource.setStartTime(resourceLink.getStartTime());
                                    resource.setEndTime(resourceLink.getEndTime());
                                    sessionFacade.saveOrUpdate(resource);
                                    resourcelinks.add(resource);
                                } else {
                                    ResourceLinkInfo resource = new ResourceLinkInfo();
                                    initBusinessObject(resource);
                                    resource.setResourceId(resourceLink.getResourceId());
                                    resource.setUseObjectId(plan.getId());
                                    resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    resource.setUseRate(resourceLink.getUseRate());
                                    resource.setStartTime(resourceLink.getStartTime());
                                    resource.setEndTime(resourceLink.getEndTime());
                                    sessionFacade.save(resource);
                                    resourcelinks.add(resource);
                                }
                            }
                        }
                    } else {
                        // 新增
                        Plan plan = new Plan();
                        plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                        plan.setCellId(flowTask.getCellId());
                        plan.setOwner(flowTask.getOwner());
                        plan.setPlanName(flowTask.getPlanName());
                        plan.setPlanEndTime(flowTask.getPlanEndTime());
                        plan.setPlanLevel(flowTask.getPlanLevel());
                        plan.setPlanStartTime(flowTask.getPlanStartTime());
                        plan.setRemark(flowTask.getRemark());
                        plan.setWorkTime(flowTask.getWorkTime());
                        plan.setPlanType(PlanConstants.PLAN_TYPE_FLOW);
                        plan.setProjectId(parent.getProjectId());
                        plan.setParentPlanId(parent.getId());
                        plan.setCreateBy(flowTask.getCreateBy());
                        plan.setCreateTime(flowTask.getCreateTime());
                        plan.setCreateFullName(flowTask.getCreateFullName());
                        plan.setCreateName(flowTask.getCreateName());
                        plan.setFormId(id);
                        plan.setFirstBy(flowTask.getCreateBy());
                        plan.setFirstTime(flowTask.getCreateTime());
                        plan.setFirstFullName(flowTask.getCreateFullName());
                        plan.setFirstName(flowTask.getCreateName());
                        if (StringUtils.isNotEmpty(flowTask.getLauncher())) {
                            plan.setAssigner(flowTask.getLauncher());
                            plan.setLauncher(flowTask.getLauncher());
                        } else {
                            plan.setAssigner(parent.getOwner());
                            plan.setLauncher(parent.getOwner());
                        }
                        plan.setAssignTime(new Date());
                        plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                        plan.setTaskNameType(!CommonUtil.isEmpty(flowTask.getTaskNameType()) ? flowTask.getTaskNameType() : CommonConstants.NAMESTANDARD_TYPE_DEV);
                        plan.setLaunchTime(new Date());

                        if (planService.isReviewTask(flowTask.getPlanName())) {
                            plan.setTaskNameType("评审任务");
                        }
                        plan.setPlanNumber(planService.getMaxPlanNumber(plan) + 1);
                        plan.setStoreyNo(planService.getMaxStoreyNo(plan) + 1);
                        plan.setBizCurrent(PlanConstants.PLAN_FLOWSTATUS_ORDERED);
                        planService.updateBizCurrent(plan);
                        //调用计划反馈引擎计算进度
                        planService.updateProgressRate(plan);
                        sessionFacade.saveOrUpdate(plan);
//                        if (!CommonUtil.isEmpty(flowTaskIdStrMap.get(flowTask.getId()))) {
//                            plan.setId(flowTaskIdStrMap.get(flowTask.getId()));
//                            saveInputsBatchPlans.add(plan);
//                        }
                        planService.saveFlowTaskAsCreate(plan, user);
                        originObjectMap.put(flowTask.getId(), plan.getId());

                        flowTaskIdMap.put(flowTask.getId(), plan.getId());
                        String label = plan.getPlanName() + "," + plan.getWorkTime() + "天";
                        cellWorkTimeMap.put(plan.getCellId(), label);

                        // 新增计划的输入
                        for (FlowTaskInputsVo inputs : flowTask.getInputList()) {
                            if (StringUtils.isNotEmpty(inputs.getInputId())) {
                                Inputs input = (Inputs) sessionFacade.getEntity(Inputs.class, inputs.getId());
                                input.setName(inputs.getName());
                                input.setUseObjectId(plan.getId());
                                input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                input.setOrigin(inputs.getOrigin());
                                input.setOriginObjectId(inputs.getOriginObjectId());
                                input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                input.setDocId(inputs.getDocId());
                                input.setDocName(inputs.getDocName());
                                input.setOriginType(inputs.getOriginType());
                                input.setOriginTypeExt(inputs.getOriginTypeExt());
                                inputsService.saveOrUpdate(input);
                                newInputList.add(input);
                            } else {
                                Inputs input = new Inputs();
                                input.setName(inputs.getName());
                                input.setUseObjectId(plan.getId());
                                input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                input.setOrigin(inputs.getOrigin());
                                input.setOriginObjectId(inputs.getOriginObjectId());
                                input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                input.setCreateBy(inputs.getCreateBy());
                                input.setCreateFullName(inputs.getCreateFullName());
                                input.setCreateName(inputs.getCreateName());
                                input.setCreateTime(inputs.getCreateTime());
                                input.setDocId(inputs.getDocId());
                                input.setDocName(inputs.getDocName());
                                input.setOriginType(inputs.getOriginType());
                                input.setOriginTypeExt(inputs.getOriginTypeExt());
//                                if (!CommonUtil.isEmpty(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()))) {
//                                    input.setId(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()));
//                                    saveInputsBatchInputs.add(input);
//                                }
                                inputsService.save(input);
                                newInputList.add(input);
                            }
                        }

                        // 新增计划的输出
                        for (FlowTaskDeliverablesInfoVo outputs : flowTask.getOutputList()) {
                            if (StringUtils.isNotEmpty(outputs.getOutputId())) {
                                DeliverablesInfo output = (DeliverablesInfo) sessionFacade.getEntity(
                                        DeliverablesInfo.class, outputs.getId());
                                output.setName(outputs.getName());
                                output.setUseObjectId(plan.getId());
                                output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                output.setRequired(outputs.getRequired());
                                sessionFacade.saveOrUpdate(output);
                                originOutputMap.put(outputs.getId(), output.getId());
                            } else {
                                DeliverablesInfo output = new DeliverablesInfo();
                                planService.initBusinessObject(output);
                                output.setCreateBy(outputs.getCreateBy());
                                output.setCreateFullName(outputs.getCreateFullName());
                                output.setCreateName(outputs.getCreateName());
                                output.setCreateTime(outputs.getCreateTime());
                                output.setName(outputs.getName());
                                output.setUseObjectId(plan.getId());
                                output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                output.setRequired(outputs.getRequired());
                                sessionFacade.save(output);
//                                if (!CommonUtil.isEmpty(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()))) {
//                                    output.setId(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()));
//                                    saveInputsBatchDelivers.add(output);
//                                }
                                originOutputMap.put(outputs.getId(), output.getId());

                            }
                        }

                        // 新增计划的资源
                        for (FlowTaskResourceLinkInfoVo resourceLink : flowTask.getResourceLinkList()) {
                            if (StringUtils.isNotEmpty(resourceLink.getLinkInfoId())) {
                                ResourceLinkInfo resource = (ResourceLinkInfo) sessionFacade.getEntity(
                                        ResourceLinkInfo.class, resourceLink.getLinkInfoId());
                                resource.setResourceId(resourceLink.getResourceId());
                                resource.setUseObjectId(plan.getId());
                                resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                resource.setUseRate(resourceLink.getUseRate());
                                resource.setStartTime(resourceLink.getStartTime());
                                resource.setEndTime(resourceLink.getEndTime());
                                sessionFacade.saveOrUpdate(resource);
                                resourcelinks.add(resource);
                            } else {
                                ResourceLinkInfo resource = new ResourceLinkInfo();
                                initBusinessObject(resource);
                                resource.setResourceId(resourceLink.getResourceId());
                                resource.setUseObjectId(plan.getId());
                                resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                resource.setUseRate(resourceLink.getUseRate());
                                resource.setStartTime(resourceLink.getStartTime());
                                resource.setEndTime(resourceLink.getEndTime());
                                sessionFacade.save(resource);
                                resourcelinks.add(resource);
                            }
                        }

                        ApprovePlanInfo approveInfo = new ApprovePlanInfo();
                        approveInfo.setFormId(id);
                        approveInfo.setPlanId(plan.getId());
                        sessionFacade.saveOrUpdate(approveInfo);
                    }
                }
                // 更新FlowTaskCellConnect
                Plan parentCondition = new Plan();
                parentCondition.setParentPlanId(parent.getId());
                List<Plan> newPlanList = planService.queryPlanList(parentCondition, 1, 10, false);
                List<FlowTaskCellConnectVo> flowTaskCellConnectVoList = new ArrayList<FlowTaskCellConnectVo>();
                for (ChangeFlowTaskCellConnectVo cellConnect : changeFlowTaskConnectList) {
                    FlowTaskCellConnectVo connect = new FlowTaskCellConnectVo();
                    connect.setId(UUIDGenerator.generate().toString());
                    connect.setParentPlanId(parent.getId());
                    connect.setCellId(cellConnect.getCellId());
                    connect.setInfoId(cellConnect.getInfoId());
                    connect.setTargetId(cellConnect.getTargetId());
                    connect.setTargetInfoId(cellConnect.getTargetInfoId());
                    // 通过节点与模板ID获得基本信息ID
                    if (StringUtils.isNotEmpty(connect.getCellId())) {
                        for (Plan f : newPlanList) {
                            if (connect.getCellId().equals(f.getCellId())) {
                                connect.setInfoId(f.getId());
                                break;
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(connect.getTargetId())) {
                        for (Plan f : newPlanList) {
                            if (connect.getTargetId().equals(f.getCellId())) {
                                connect.setTargetInfoId(f.getId());
                                break;
                            }
                        }
                    }
                    flowTaskCellConnectVoList.add(connect);
                }
                planService.saveFlowTaskCellConnect(flowTaskCellConnectVoList);

                // 更新内部前置
                List<FlowTaskCellConnectVo> flowTaskConnectList = taskFlowResolveService.getFlowTaskConnectList(parent.getId());
                for (FlowTaskCellConnectVo cellConnect : flowTaskConnectList) {
                    if (!TaskProcConstants.TASK_CELL_START.equals(cellConnect.getCellId())
                            && !TaskProcConstants.TASK_CELL_END.equals(cellConnect.getTargetId())) {
                        PreposePlan prepose = new PreposePlan();
                        prepose.setPlanId(cellConnect.getTargetInfoId());
                        prepose.setPreposePlanId(cellConnect.getInfoId());
                        sessionFacade.saveOrUpdate(prepose);
                    }
                }

                // 更新外部前置
                for (FlowTaskPreposeVo preposeTask : allflowTaskPreposeList) {
                    if (!flowTaskPreposeList.contains(preposeTask)
                            && !preposeTask.getPreposeId().startsWith(PlanConstants.PLAN_CREATE_UUID)) {
                        PreposePlan prepose = new PreposePlan();
                        prepose.setPlanId(flowTaskIdMap.get(preposeTask.getFlowTaskId()));
                        prepose.setPreposePlanId(preposeTask.getPreposeId());
                        sessionFacade.saveOrUpdate(prepose);
                    }
                }

                // 更新输入的来源信息
                for (Inputs in : newInputList) {
                    if (StringUtils.isNotEmpty(originObjectMap.get(in.getOriginObjectId()))
                            && StringUtils.isNotEmpty(in.getOriginDeliverablesInfoId())) {
                        Inputs curInput = (Inputs) sessionFacade.getEntity(Inputs.class, in.getId());
                        curInput.setOriginObjectId(originObjectMap.get(in.getOriginObjectId()));
                        curInput.setOriginDeliverablesInfoId(originOutputMap.get(in.getOriginDeliverablesInfoId()));
                        sessionFacade.saveOrUpdate(curInput);
                    }

                }
                // 变更之后将节点对应的工期在XML中更新
                String flowResolveXml = flowTaskParent.getFlowResolveXml();
                flowResolveXml = taskFlowResolveService.refreshFlowResolveXml(flowResolveXml,
                        cellWorkTimeMap);
                parent.setFlowResolveXml(flowResolveXml);
                sessionFacade.saveOrUpdate(parent);

                List<String> httpUrls = new ArrayList<String>();
//                List<OutwardExtension> outwardExtensionList = outwardExtensionService.getOutwardExtensionList("flowResolveCategoryHttpServer");
//                if (!CommonUtil.isEmpty(outwardExtensionList)) {
//                    for (OutwardExtension ext : outwardExtensionList) {
//                        if (!CommonUtil.isEmpty(ext.getUrlList())) {
//                            for (OutwardExtensionUrl out : ext.getUrlList()) {
//                                if ("changeApprove".equals(out.getOperateCode())) {
//                                    httpUrls.add(out.getOperateUrl());
//                                }
//                            }
//                        }
//                    }
                if (!CommonUtil.isEmpty(httpUrls)) {
                    FlowTaskOutChangeVO flowTaskOutChangeVO = new FlowTaskOutChangeVO();
                    flowTaskOutChangeVO.setParentPlanId(parent.getId());
                    List<FlowTaskNodeVO> nodes = new ArrayList<FlowTaskNodeVO>();
                    FlowTaskNodeVO nodeVO = new FlowTaskNodeVO();
                    List<FlowTaskDeliverVO> delivers = new ArrayList<FlowTaskDeliverVO>();
                    FlowTaskDeliverVO deliverVO = new FlowTaskDeliverVO();
                    List<Plan> newPlans = sessionFacade.findHql(
                            "from Plan where parentPlanId = ? and avaliable = '1' ", parent.getId());
                    List<NameStandard> nameStandards = sessionFacade.findHql(
                            "from NameStandard where name in (select planName from Plan where parentPlanId = ? and avaliable = '1' ) ",
                            parent.getId());
                    Map<String, String> nameStandardMap = new HashMap<String, String>();
                    for (NameStandard ns : nameStandards) {
                        nameStandardMap.put(ns.getName(), ns.getId());
                    }
                    List<Inputs> newInputs = sessionFacade.findHql(
                            " from Inputs where useObjectId in ( select id from Plan where parentPlanId = ? and avaliable = '1' ) ",
                            parent.getId());
                    List<DeliverablesInfo> newOutputs = sessionFacade.findHql(
                            " from DeliverablesInfo where useObjectId in ( select id from Plan where parentPlanId = ? and avaliable = '1' ) ",
                            parent.getId());
                    List<DeliveryStandard> deliveryStandards = sessionFacade.findByQueryString("from DeliveryStandard");
                    Map<String, String> deliveryStandardMap = new HashMap<String, String>();
                    for (DeliveryStandard d : deliveryStandards) {
                        deliveryStandardMap.put(d.getName(), d.getId());
                    }

                    for (Plan pl : newPlans) {
                        nodeVO = new FlowTaskNodeVO();
                        nodeVO.setNewTaskId(pl.getId());
                        if (!CommonUtil.isEmpty(oldIdCellIdMap)
                                && !CommonUtil.isEmpty(oldIdCellIdMap.get(pl.getCellId()))) {
                            nodeVO.setOldTaskId(oldIdCellIdMap.get(pl.getCellId()));
                        }
                        if (!CommonUtil.isEmpty(nameStandardMap)
                                && !CommonUtil.isEmpty(nameStandardMap.get(pl.getPlanName()))) {
                            nodeVO.setNameStandardId(nameStandardMap.get(pl.getPlanName()));
                        }
                        delivers = new ArrayList<FlowTaskDeliverVO>();
                        for (Inputs in : newInputs) {
                            if (pl.getId().equals(in.getUseObjectId())) {
                                deliverVO = new FlowTaskDeliverVO();
                                deliverVO.setNewId(in.getId());
                                if (!CommonUtil.isEmpty(deliveryStandardMap)
                                        && !CommonUtil.isEmpty(deliveryStandardMap.get(in.getName()))) {
                                    deliverVO.setDeliverId(deliveryStandardMap.get(in.getName()));
                                }
                                deliverVO.setInOrOut("INPUT");
                                deliverVO.setSourceId(in.getOriginObjectId());
                                deliverVO.setSourceDeliverSeq(in.getOriginDeliverablesInfoId());
                                delivers.add(deliverVO);
                            }
                        }

                        for (DeliverablesInfo out : newOutputs) {
                            if (pl.getId().equals(out.getUseObjectId())) {
                                deliverVO = new FlowTaskDeliverVO();
                                deliverVO.setNewId(out.getId());
                                if (!CommonUtil.isEmpty(deliveryStandardMap)
                                        && !CommonUtil.isEmpty(deliveryStandardMap.get(out.getName()))) {
                                    deliverVO.setDeliverId(deliveryStandardMap.get(out.getName()));
                                }
                                deliverVO.setInOrOut("OUTPUT");
                                delivers.add(deliverVO);
                            }
                        }
                        nodeVO.setDelivers(delivers);
                        nodes.add(nodeVO);
                    }
                    flowTaskOutChangeVO.setNodes(nodes);
                    for (String url : httpUrls) {
                        try {
                            HttpClientUtil.httpClientPostByTest(url, flowTaskOutChangeVO);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

//                Connection conn = null;
//                // 插入计划及相关信息
//                PreparedStatement psForPlan = null;
//                PreparedStatement psForInputs = null;
//                PreparedStatement psForDeliverablesInfo = null;
//                if (!CommonUtil.isEmpty(changeFlowTaskList)) {
//                    try {
//                        conn = jdbcTemplate.getDataSource().getConnection();
//                        conn.setAutoCommit(false);
//                        // 批量插入计划
//                        if (!CommonUtil.isEmpty(saveInputsBatchPlans)) {
//                            String sqlForPlan = " insert into PM_PLAN ("
//                                    + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
//                                    + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
//                                    + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
//                                    + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
//                                    + " SECURITYLEVEL, AVALIABLE,"
//                                    + " PLANNAME, PLANNUMBER, PROJECTID, PARENTPLANID,"
//                                    + " PLANLEVEL, PLANSTARTTIME, PLANENDTIME, "
//                                    + " PROGRESSRATE, STOREYNO, MILESTONE, PLANSOURCE,"
//                                    + " WORKTIME, WORKTIMEREFERENCE, TASKNAMETYPE, TASKTYPE, "
//                                    + " FLOWSTATUS, CELLID, FROMTEMPLATE, REQUIRED, OWNER,launcher,launchtime,assigner,assigntime) "
//                                    + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'),"
//                                    + " to_date(?, 'yyyy-mm-dd'), ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?,'NORMAL'," + " ?, 'true', ?, ?, ?, ?, ?,?)";
//                            psForPlan = conn.prepareStatement(sqlForPlan);
//                            for (Plan plan : saveInputsBatchPlans) {
//                                psForPlan.setObject(1, plan.getId());
//                                psForPlan.setObject(2, plan.getCreateTime());
//                                psForPlan.setObject(3, plan.getCreateBy());
//                                psForPlan.setObject(4, plan.getCreateFullName());
//                                psForPlan.setObject(5, plan.getCreateName());
//                                psForPlan.setObject(6, plan.getUpdateTime());
//                                psForPlan.setObject(7, plan.getUpdateBy());
//                                psForPlan.setObject(8, plan.getUpdateFullName());
//                                psForPlan.setObject(9, plan.getUpdateName());
//                                psForPlan.setObject(10, plan.getFirstTime());
//                                psForPlan.setObject(11, plan.getFirstBy());
//                                psForPlan.setObject(12, plan.getFirstFullName());
//                                psForPlan.setObject(13, plan.getFirstName());
//                                psForPlan.setObject(14, plan.getPolicy().getId());
//                                psForPlan.setObject(15, plan.getBizCurrent());
//                                psForPlan.setObject(16, plan.getBizId());
//                                psForPlan.setObject(17, plan.getBizVersion());
//                                if(CommonUtil.isEmpty(plan.getSecurityLevel())) {
//                                    psForPlan.setObject(18, 1);  
//                                }else {
//                                    psForPlan.setObject(18, plan.getSecurityLevel());
//                                }
//                                psForPlan.setObject(19, plan.getAvaliable());
//                                psForPlan.setObject(20, plan.getPlanName());
//                                psForPlan.setObject(21, plan.getPlanNumber());
//                                psForPlan.setObject(22, plan.getProjectId());
//                                psForPlan.setObject(23, plan.getParentPlanId());
//                                psForPlan.setObject(24, plan.getPlanLevel());
//                                psForPlan.setObject(25,
//                                        DateUtil.dateToString(plan.getPlanStartTime(), DateUtil.YYYY_MM_DD));
//                                psForPlan.setObject(26,
//                                        DateUtil.dateToString(plan.getPlanEndTime(), DateUtil.YYYY_MM_DD));
//                                psForPlan.setObject(27, plan.getProgressRate());
//                                psForPlan.setObject(28, plan.getStoreyNo());
//                                psForPlan.setObject(29, plan.getMilestone());
//                                psForPlan.setObject(30, plan.getPlanSource());
//                                psForPlan.setObject(31, plan.getWorkTime());
//                                psForPlan.setObject(32, plan.getWorkTimeReference());
//                                psForPlan.setObject(33, plan.getTaskNameType());
//                                psForPlan.setObject(34, plan.getTaskType());
//                                psForPlan.setObject(35, plan.getCellId());
//                                psForPlan.setObject(36,
//                                        StringUtils.isEmpty(plan.getRequired()) ? "" : plan.getRequired());
//                                psForPlan.setObject(37,
//                                        StringUtils.isEmpty(plan.getOwner()) ? "" : plan.getOwner());
//                                psForPlan.setObject(38, plan.getLauncher());
//                                if (!CommonUtil.isEmpty(plan.getLaunchTime())) {
//                                    psForPlan.setObject(39, new Timestamp(plan.getLaunchTime().getTime()));
//                                } else {
//                                    psForPlan.setObject(39, null);
//                                }
//                                psForPlan.setObject(40, plan.getAssigner());
//                                if (!CommonUtil.isEmpty(plan.getAssignTime())) {
//                                    psForPlan.setObject(41, new Timestamp(plan.getAssignTime().getTime()));
//                                } else {
//                                    psForPlan.setObject(41, null);
//                                }
//                                psForPlan.addBatch();
//                            }
//                            psForPlan.executeBatch();
//                        }
//
//                        // 批量插入输入
//                        if (!CommonUtil.isEmpty(saveInputsBatchInputs)) {
//                            String sqlForInputs = " insert into PM_INPUTS ("
//                                    + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
//                                    + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, USEOBJECTID,"
//                                    + " USEOBJECTTYPE, NAME, ORIGINOBJECTID, ORIGINDELIVERABLESINFOID,DOCID,DOCNAME,OriginType,OriginTypeExt) "
//                                    + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?,?,?,?,?)";
//                            psForInputs = conn.prepareStatement(sqlForInputs);
//                            for (Inputs inputs : saveInputsBatchInputs) {
//                                psForInputs.setObject(1, inputs.getId());
//                                psForInputs.setObject(2, inputs.getCreateTime());
//                                psForInputs.setObject(3, inputs.getCreateBy());
//                                psForInputs.setObject(4, inputs.getCreateFullName());
//                                psForInputs.setObject(5, inputs.getCreateName());
//                                psForInputs.setObject(6, inputs.getUpdateTime());
//                                psForInputs.setObject(7, inputs.getUpdateBy());
//                                psForInputs.setObject(8, inputs.getUpdateFullName());
//                                psForInputs.setObject(9, inputs.getUpdateName());
//                                psForInputs.setObject(10, inputs.getUseObjectId());
//                                psForInputs.setObject(11, inputs.getUseObjectType());
//                                psForInputs.setObject(12, inputs.getName());
//                                psForInputs.setObject(13, inputs.getOriginObjectId());
//                                psForInputs.setObject(14, inputs.getOriginDeliverablesInfoId());
//                                psForInputs.setObject(15, inputs.getDocId());
//                                psForInputs.setObject(16, inputs.getDocName());
//                                psForInputs.setObject(17, inputs.getOriginType());
//                                psForInputs.setObject(18, inputs.getOriginTypeExt());
//                                psForInputs.addBatch();
//                            }
//                            psForInputs.executeBatch();
//                        }
//
//                        // 批量插入计划交付项
//                        if (!CommonUtil.isEmpty(saveInputsBatchDelivers)) {
//                            String sqlForDeliverablesInfo = " insert into PM_DELIVERABLES_INFO ("
//                                    + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
//                                    + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
//                                    + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
//                                    + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
//                                    + " SECURITYLEVEL, AVALIABLE,"
//                                    + " USEOBJECTID, USEOBJECTTYPE, NAME, ORIGIN, REQUIRED) "
//                                    + " values (" + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?, ?," + " ?, ?, ?,?)";
//                            psForDeliverablesInfo = conn.prepareStatement(sqlForDeliverablesInfo);
//                            for (DeliverablesInfo info : saveInputsBatchDelivers) {
//                                psForDeliverablesInfo.setObject(1, info.getId());
//                                psForDeliverablesInfo.setObject(2, info.getCreateTime());
//                                psForDeliverablesInfo.setObject(3, info.getCreateBy());
//                                psForDeliverablesInfo.setObject(4, info.getCreateFullName());
//                                psForDeliverablesInfo.setObject(5, info.getCreateName());
//                                psForDeliverablesInfo.setObject(6, info.getUpdateTime());
//                                psForDeliverablesInfo.setObject(7, info.getUpdateBy());
//                                psForDeliverablesInfo.setObject(8, info.getUpdateFullName());
//                                psForDeliverablesInfo.setObject(9, info.getUpdateName());
//                                psForDeliverablesInfo.setObject(10, info.getFirstTime());
//                                psForDeliverablesInfo.setObject(11, info.getFirstBy());
//                                psForDeliverablesInfo.setObject(12, info.getFirstFullName());
//                                psForDeliverablesInfo.setObject(13, info.getFirstName());
//                                psForDeliverablesInfo.setObject(14, info.getPolicy().getId());
//                                psForDeliverablesInfo.setObject(15, info.getBizCurrent());
//                                psForDeliverablesInfo.setObject(16, info.getBizId());
//                                psForDeliverablesInfo.setObject(17, info.getBizVersion());
//                                psForDeliverablesInfo.setObject(18, info.getSecurityLevel());
//                                psForDeliverablesInfo.setObject(19, info.getAvaliable());
//                                psForDeliverablesInfo.setObject(20, info.getUseObjectId());
//                                psForDeliverablesInfo.setObject(21, info.getUseObjectType());
//                                psForDeliverablesInfo.setObject(22, info.getName());
//                                psForDeliverablesInfo.setObject(23, info.getOrigin());
//                                psForDeliverablesInfo.setObject(24, info.getRequired());
//                                psForDeliverablesInfo.addBatch();
//                            }
//                            psForDeliverablesInfo.executeBatch();
//                        }
//
//                        conn.commit();
//                    } catch (Exception ex) {
//                        msg = "false";
//                        try {
//                            conn.rollback();
//                        } catch (SQLException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            conn.setAutoCommit(true);
//                            DBUtil.closeConnection(null, psForPlan, conn);
//                            DBUtil.closeConnection(null, psForInputs, conn);
//                            DBUtil.closeConnection(null, psForDeliverablesInfo, conn);
//
//                        } catch (SQLException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//
//                }

            }
//TODO..
//            // 在线程中更新资源每日使用信息
//            ResourceEverydayuseInfosThread resourceEverydayuseInfosThread = new ResourceEverydayuseInfosThread();
//            resourceEverydayuseInfosThread.setType(CommonConfigConstants.OBJECT_TYPE_RESOURCE);
//            resourceEverydayuseInfosThread.setOperationType(CommonConfigConstants.RESOURCEEVERYDAYUSE_OPERATIONTYPE_INSERT);
//            resourceEverydayuseInfosThread.setProjectId(projectId);
//            resourceEverydayuseInfosThread.setLinkInfoList(resourcelinks);
//            resourceEverydayuseInfosThread.setResourceEverydayuseService(resourceEverydayuseService);
//            new Thread(resourceEverydayuseInfosThread).start();
        }
    }
    
    @Override
    public void startPlanChangeFlowForPlan(String oid, String userId) {
        {
            if (oid == null || oid.equals("")) {
                return;
            }
            String[] arrays = oid.split(":");
            if (arrays.length < 2) {
                return;
            }
            TSUserDto user = userService.getUserByUserId(userId);
            
            String id = arrays[1];
            List<ResourceLinkInfo> resourcelinks = new ArrayList<ResourceLinkInfo>();
            String projectId = "";
            // 获取计划id
            String msg = "";
            ApprovePlanForm form = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, id);
            boolean reviewPluginValid = false;
            List<ServiceInstance> instances = discoveryClient.getInstances("ids-review-service");
            if (!CommonUtil.isEmpty(instances)) {
                reviewPluginValid = true;
            }
            if (!PlanConstants.PLAN_APPROVE_TYPE_FLOWTASKCHANGE.equals(form.getApproveType())) {
                ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                approvePlanInfo.setFormId(id);
                List<ApprovePlanInfo> approve = planService.queryAssignList(approvePlanInfo, 1, 10,
                        false);
                Map<String, String> planIdMap = new HashMap<String, String>();
                List<TemporaryPlan> temporaryPlanList = new ArrayList<TemporaryPlan>();
                TemporaryPlan temporaryPlan = new TemporaryPlan();
                
                
                
                
                for (ApprovePlanInfo a : approve) {
//                    TemporaryPlan plan = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class, a.getPlanId());
                    temporaryPlan.setId(a.getPlanId());
                    temporaryPlan.setFormId(id);
                    a.setPlanId(a.getPlanId());
                    sessionFacade.saveOrUpdate(a);
                    if (StringUtils.isEmpty(planIdMap.get(a.getPlanId()))) {
                        msg = a.getPlanId();
                        try {
                            planChangeMsgNotice.getAllMessage(msg, user);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        planIdMap.put(a.getPlanId(), a.getPlanId());
                    }
                    List<TemporaryPlan> temporaryPlanListTemp = planChangeService.queryTemporaryPlanList(
                            temporaryPlan, 1, 10, false);
                    temporaryPlanList.add(temporaryPlanListTemp.get(0));

                }

                TempPlanDeliverablesInfo deliverablesInfo = new TempPlanDeliverablesInfo();
                deliverablesInfo.setUseObjectId(id);
                deliverablesInfo.setFormId(id);
                List<TempPlanDeliverablesInfo> deliverablesInfoList = deliverablesChangeService.queryDeliverableChangeList(
                        deliverablesInfo, 1, 10, false);

                TempPlanInputs tempPlanInputs = new TempPlanInputs();
                tempPlanInputs.setUseObjectId(id);
                tempPlanInputs.setFormId(id);
                List<TempPlanInputs> inputList = deliverablesChangeService.queryInputChangeList(
                        tempPlanInputs, 1, 10, false);

                TempPlanResourceLinkInfo resourceLinkInfo = new TempPlanResourceLinkInfo();
                resourceLinkInfo.setUseObjectId(id);
                resourceLinkInfo.setFormId(id);
                List<TempPlanResourceLinkInfo> resourceLinkInfoList = resourceChangeService.queryResourceChangeList(
                        resourceLinkInfo, 1, 10, false);

                for (TemporaryPlan temporaryPlans : temporaryPlanList) {
                    Plan plan = (Plan) sessionFacade.getEntity(Plan.class, temporaryPlans.getPlanId());
                    if (StringUtils.isEmpty(projectId) && plan != null) {
                        projectId = plan.getProjectId();
                    }

                    PlanHistory saveplan = new PlanHistory();
                    saveplan.setPlanId(plan.getId());
                    saveplan.setCreateTime(new Date());
                    saveplan.setChangeType(temporaryPlans.getChangeType());
                    saveplan.setFormId(id);
                    saveplan.setBizId(plan.getBizId());
                    saveplan.setPlanType(plan.getPlanType());
                    saveplan.setBizVersion(plan.getBizVersion());
                    saveplan.setBizCurrent(plan.getBizCurrent());
                    if(!CommonUtil.isEmpty(plan.getSecurityLevel())){
                        saveplan.setSecurityLevel(plan.getSecurityLevel());
                    }else{
                        saveplan.setSecurityLevel(Short.valueOf("1"));
                    }

                    saveplan.setPlanCreateBy(plan.getCreateBy());
                    saveplan.setPlanCreateTime(plan.getCreateTime());
                    saveplan.setPlanUpdateBy(plan.getUpdateBy());
                    saveplan.setPlanUpdateTime(plan.getUpdateTime());
                    saveplan.setAssignTime(plan.getAssignTime());
                    saveplan.setAvaliable(plan.getAvaliable());
                    saveplan.setActualStartTime(plan.getActualStartTime());
                    saveplan.setActualEndTime(plan.getActualEndTime());
                    saveplan.setAssigner(plan.getAssigner());
                    saveplan.setBeforePlanId(plan.getBeforePlanId());
                    saveplan.setFlowStatus(plan.getFlowStatus());
                    saveplan.setImplementation(plan.getImplementation());
                    saveplan.setMilestone(plan.getMilestone());
                    saveplan.setOwner(plan.getOwner());
                    saveplan.setParentPlanId(plan.getParentPlanId());
                    saveplan.setPlanEndTime(plan.getPlanEndTime());
                    saveplan.setPlanLevel(plan.getPlanLevel());
                    saveplan.setPlanName(plan.getPlanName());
                    saveplan.setPlanNumber(plan.getPlanNumber());
                    saveplan.setPlanOrder(plan.getPlanOrder());
                    saveplan.setPlanStartTime(plan.getPlanStartTime());
                    saveplan.setPreposeIds(plan.getPreposeIds());
                    saveplan.setPreposePlans(plan.getPreposePlans());
                    saveplan.setProgressRate(plan.getProgressRate());
                    saveplan.setProjectId(plan.getProjectId());
                    saveplan.setProjectStatus(plan.getProjectStatus());
                    saveplan.setRemark(plan.getRemark());
                    saveplan.setStoreyNo(plan.getStoreyNo());
                    saveplan.setWorkTime(plan.getWorkTime());

                    plan.setAvaliable(temporaryPlans.getAvaliable());
                    plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                    plan.setImplementation(temporaryPlans.getImplementation());
                    plan.setMilestone(temporaryPlans.getMilestone());
                    plan.setOwner(temporaryPlans.getOwner());
                    plan.setPlanEndTime(temporaryPlans.getPlanEndTime());
                    plan.setPlanLevel(temporaryPlans.getPlanLevel());
                    plan.setPlanStartTime(temporaryPlans.getPlanStartTime());
                    plan.setRemark(temporaryPlans.getRemark());
                    plan.setWorkTime(temporaryPlans.getWorkTime());




                    planService.deleteResourceByPlanId(plan.getId());
                    resourceEverydayuseService.delResourceUseByPlan(plan.getId());
                    planService.deleteDeliverablesByPlanId(plan.getId());
                    planService.saveOrUpdate(plan);
                    sessionFacade.saveOrUpdate(saveplan);

                    plan.setPreposeIds(temporaryPlans.getPreposeIds());

                    if (!CommonUtil.isEmpty(plan.getPreposeIds())) {
                        // 删除其已有的前置
                        preposePlanService.removePreposePlansByPlanId(plan);
                        // 存放其新的前置计划
                        if (!CommonUtil.isEmpty(plan.getPreposeIds())) {
                            for (String preposeId : plan.getPreposeIds().split(",")) {
                                /*Plan preposePlan = (Plan) sessionFacade.getEntity(Plan.class, preposeId);
                                if (preposePlan != null) {*/
                                PreposePlan prepose = new PreposePlan();
                                prepose.setPlanId(plan.getId());
                                prepose.setPreposePlanId(preposeId);
                                sessionFacade.save(prepose);
                               /* }*/
                            }
                        }
                    }

                    for (TempPlanDeliverablesInfo tempDeliverables : deliverablesInfoList) {
                        if (temporaryPlans.getPlanId().equals(tempDeliverables.getDeliverLinkId())) {
                            DeliverablesInfo deliverables = new DeliverablesInfo();
                            initBusinessObject(deliverables);
                            deliverables.setAvaliable(tempDeliverables.getAvaliable());
                            deliverables.setBizCurrent(tempDeliverables.getBizCurrent());
                            deliverables.setBizId(tempDeliverables.getBizId());
                            deliverables.setBizVersion(tempDeliverables.getBizVersion());
                            deliverables.setDocument(tempDeliverables.getDocument());
                            deliverables.setName(tempDeliverables.getName());
                            deliverables.setPolicy(tempDeliverables.getPolicy());
                            if(!CommonUtil.isEmpty(tempDeliverables.getSecurityLevel())){
                                deliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                            }else{
                                deliverables.setSecurityLevel(Short.valueOf("1"));
                            }

                            deliverables.setUseObjectId(temporaryPlans.getPlanId());
                            deliverables.setUseObjectType("PLAN");
                            sessionFacade.save(deliverables);

                            DeliverablesInfoHistory saveDeliverables = new DeliverablesInfoHistory();
                            saveDeliverables.setAvaliable(tempDeliverables.getAvaliable());
                            saveDeliverables.setBizCurrent(tempDeliverables.getBizCurrent());
                            saveDeliverables.setBizId(tempDeliverables.getBizId());
                            saveDeliverables.setBizVersion(tempDeliverables.getBizVersion());
                            saveDeliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                            saveDeliverables.setPlanCreateBy(tempDeliverables.getCreateBy());
                            saveDeliverables.setPlanCreateTime(tempDeliverables.getCreateTime());
                            saveDeliverables.setPlanUpdateBy(tempDeliverables.getUpdateBy());
                            saveDeliverables.setPlanUpdateName(tempDeliverables.getUpdateTime());
                            saveDeliverables.setBizVersion(tempDeliverables.getBizVersion());
                            saveDeliverables.setDocument(tempDeliverables.getDocument());
                            saveDeliverables.setName(tempDeliverables.getName());
                            if(!CommonUtil.isEmpty(tempDeliverables.getSecurityLevel())){
                                saveDeliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                            }else{
                                saveDeliverables.setSecurityLevel(Short.valueOf("1"));
                            }
                            saveDeliverables.setUseObjectId(temporaryPlans.getPlanId());
                            saveDeliverables.setUseObjectType("PLAN");
                            sessionFacade.save(saveDeliverables);
                        }
                    }
                    for (TempPlanResourceLinkInfo tempResourceLink : resourceLinkInfoList) {
                        if (temporaryPlans.getPlanId().equals(tempResourceLink.getResourceLinkId())) {
                            ResourceLinkInfo resourceLink = new ResourceLinkInfo();
                            initBusinessObject(resourceLink);
                            resourceLink.setAvaliable(tempResourceLink.getAvaliable());
                            resourceLink.setBizCurrent(tempResourceLink.getBizCurrent());
                            resourceLink.setBizId(tempResourceLink.getBizId());
                            resourceLink.setBizVersion(tempResourceLink.getBizVersion());
                            resourceLink.setPolicy(tempResourceLink.getPolicy());
                            resourceLink.setResourceId(tempResourceLink.getResourceId());
                            resourceLink.setResourceInfo(tempResourceLink.getResourceInfo());
                            resourceLink.setResourceName(tempResourceLink.getResourceName());
                            resourceLink.setResourceType(tempResourceLink.getResourceType());
                            resourceLink.setStartTime(tempResourceLink.getStartTime());
                            resourceLink.setEndTime(tempResourceLink.getEndTime());
                            if(!CommonUtil.isEmpty(tempResourceLink.getSecurityLevel())){
                                resourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                            }else{
                                resourceLink.setSecurityLevel(Short.valueOf("1"));
                            }

                            resourceLink.setUseObjectId(temporaryPlans.getPlanId());
                            resourceLink.setUseObjectType("PLAN");
                            resourceLink.setUseRate(tempResourceLink.getUseRate());
                            sessionFacade.save(resourceLink);
                            resourcelinks.add(resourceLink);

                            ResourceLinkInfoHistory saveResourceLink = new ResourceLinkInfoHistory();
                            ;
                            saveResourceLink.setAvaliable(tempResourceLink.getAvaliable());
                            saveResourceLink.setBizCurrent(tempResourceLink.getBizCurrent());
                            saveResourceLink.setBizId(tempResourceLink.getBizId());
                            saveResourceLink.setBizVersion(tempResourceLink.getBizVersion());
                            saveResourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                            saveResourceLink.setPlanCreateBy(tempResourceLink.getCreateBy());
                            saveResourceLink.setPlanCreateTime(tempResourceLink.getCreateTime());
                            saveResourceLink.setPlanUpdateBy(tempResourceLink.getUpdateBy());
                            saveResourceLink.setPlanUpdateTime(tempResourceLink.getUpdateTime());
                            saveResourceLink.setResourceId(tempResourceLink.getResourceId());
                            saveResourceLink.setResourceInfo(tempResourceLink.getResourceInfo());
                            if(!CommonUtil.isEmpty(tempResourceLink.getSecurityLevel())){
                                saveResourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                            }else{
                                saveResourceLink.setSecurityLevel(Short.valueOf("1"));
                            }

                            saveResourceLink.setResourceName(tempResourceLink.getResourceName());
                            saveResourceLink.setResourceType(tempResourceLink.getResourceType());
                            saveResourceLink.setUseObjectId(temporaryPlans.getPlanId());
                            saveResourceLink.setUseObjectType("PLAN");
                            saveResourceLink.setUseRate(tempResourceLink.getUseRate());
                            sessionFacade.save(saveResourceLink);
                        }
                    }

                    for (TempPlanInputs tempResourceLink : inputList) {
                        if (temporaryPlans.getPlanId().equals(tempResourceLink.getInputId())) {
                            Inputs resourceLink = new Inputs();
                            // resourceLink.setInputId(tempResourceLink.getId());
                            resourceLink.setName(tempResourceLink.getName());
                            // resourceLink.setUseObjectId(tempResourceLink.getUseObjectId());
                            resourceLink.setUseObjectId(temporaryPlans.getPlanId());
                            resourceLink.setUseObjectType("PLAN");
                            resourceLink.setFileId(tempResourceLink.getFileId());
                            resourceLink.setOrigin(tempResourceLink.getOrigin());
                            resourceLink.setRequired(tempResourceLink.getRequired());
                            resourceLink.setDocId(tempResourceLink.getDocId());
                            resourceLink.setDocName(tempResourceLink.getDocName());
                            resourceLink.setOriginObjectId(tempResourceLink.getOriginObjectId());
                            resourceLink.setOriginDeliverablesInfoId(tempResourceLink.getOriginDeliverablesInfoId());
                            resourceLink.setChecked(tempResourceLink.getChecked());
                            resourceLink.setDocument(tempResourceLink.getDocument());
                            resourceLink.setOriginType(tempResourceLink.getOriginType());
                            resourceLink.setExt1(tempResourceLink.getExt1());
                            resourceLink.setExt2(tempResourceLink.getExt2());
                            resourceLink.setExt3(tempResourceLink.getExt3());
                            inputsService.saveOrUpdate(resourceLink);

                            InputsHistory inputsHistory = new InputsHistory();
                            inputsHistory.setName(tempResourceLink.getName());
                            inputsHistory.setDocument(tempResourceLink.getDocument());
                            inputsHistory.setFileId(tempResourceLink.getFileId());
                            inputsHistory.setInputsId(tempResourceLink.getId());
                            inputsHistory.setOrigin(tempResourceLink.getOrigin());
                            inputsHistory.setRequired(tempResourceLink.getRequired());
                            inputsHistory.setUseObjectId(tempResourceLink.getUseObjectId());
                            inputsHistory.setUseObjectType(tempResourceLink.getUseObjectType());
                            inputsService.saveOrUpdate(inputsHistory);
                        }
                    }

                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(plan.getBizCurrent()) || PlanConstants.PLAN_FLOWSTATUS_ORDERED.equals(plan.getBizCurrent()) || PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                        planService.updateBizCurrent(plan);
                    }
                    sessionFacade.updateEntitie(plan);
                    if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                        if(CommonUtil.isEmpty(plan.getPlanReceivedProcInstId())){
                            planService.startPlanReceivedProcess(plan,userId);
                        }else if(!CommonUtil.isEmpty(plan.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(plan.getPlanReceivedCompleteTime())){
                            planService.startPlanReceivedProcess(plan,userId);
                        }

                    }


                }
            } else {
                Map<String, String> cellWorkTimeMap = new HashMap<String, String>();
                String parentPlanId = "";                
                List<FlowTaskVo>  FlowTaskVoList = planService.getFlowTaskVoList(id);
                Map<String, String> idAndParentMap = new  HashMap<String, String>();
                for(FlowTaskVo curVo : FlowTaskVoList) {
                    idAndParentMap.put(curVo.getId(), curVo.getParentPlanId());
                }
                //获取变更父节点Id：
                for(String key : idAndParentMap.keySet()) {
                    if(CommonUtil.isEmpty(idAndParentMap.get(idAndParentMap.get(key)))) {
                        parentPlanId = idAndParentMap.get(key);
                        break;
                    }
                }

                FlowTaskParentVo flowTaskParent = planService.getFlowTaskParentVoInfo(parentPlanId);

                Plan temPlan = new Plan();
                temPlan.setParentPlanId(parentPlanId);
                temPlan.setFormId(id);

                Map<String, String> oldIdCellIdMap = new HashMap<String, String>();
                List<FlowTaskVo> changeFlowTaskList = taskFlowResolveService.getChangeFlowTaskList(temPlan, userId);
                for (FlowTaskVo flowtask : changeFlowTaskList) {
                    oldIdCellIdMap.put(flowtask.getCellId(), flowtask.getId());
                }

                List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList = taskFlowResolveService.getChangeFlowTaskConnectList(temPlan);
                List<FlowTaskPreposeVo> flowTaskPreposeList = taskFlowResolveService.getChangeFlowTaskPreposeList(temPlan);

                // 更新父计划的flowResolveXml
                Plan parent = (Plan) sessionFacade.getEntity(Plan.class, flowTaskParent.getParentId());
                // 不删除临时表
                // 删除原有的FlowTaskCellConnect关系
                //taskFlowResolveService.deleteFlowTaskCellConn(parent.getId());

                // 删除原有的前后置关系（主要针对内部前置）暂时不删除,可能会引起其他问题
                /*
                 * List<PreposePlan> preposePlanList =
                 * preposePlanService.getPreposePlansByParent(parent);
                 * for(PreposePlan prepose : preposePlanList){
                 * planService.deleteEntityById(PreposePlan.class, prepose.getId());
                 * }
                 */

                Plan condition = new Plan();
                condition.setParentPlanId(parent.getId());
                List<Plan> oldPlanList = planService.queryPlanList(condition, 1, 10, false);
                String changeType = "";
                for (FlowTaskVo flowTask : changeFlowTaskList) {
                    if (StringUtils.isNotEmpty(flowTask.getChangeType())) {
                        changeType = flowTask.getChangeType();
                        break;
                    }
                }

                // 备份历史记录
                for (Plan plan : oldPlanList) {
                    if (StringUtils.isEmpty(projectId) && plan != null) {
                        projectId = plan.getProjectId();
                    }
                    // 计划任务历史
                    PlanHistory saveplan = new PlanHistory();
                    saveplan.setPlanId(plan.getId());
                    saveplan.setCreateTime(new Date());
                    saveplan.setChangeType(changeType);
                    saveplan.setFormId(id);
                    saveplan.setBizId(plan.getBizId());
                    saveplan.setPlanType(plan.getPlanType());
                    saveplan.setBizVersion(plan.getBizVersion());
                    saveplan.setBizCurrent(plan.getBizCurrent());
                    if(!CommonUtil.isEmpty(plan.getSecurityLevel())){
                        saveplan.setSecurityLevel(plan.getSecurityLevel());
                    }else{
                        saveplan.setSecurityLevel(Short.valueOf("1"));
                    }

                    saveplan.setPlanCreateBy(plan.getCreateBy());
                    saveplan.setPlanCreateTime(plan.getCreateTime());
                    saveplan.setPlanUpdateBy(plan.getUpdateBy());
                    saveplan.setPlanUpdateTime(plan.getUpdateTime());
                    saveplan.setAssignTime(plan.getAssignTime());
                    saveplan.setAvaliable(plan.getAvaliable());
                    saveplan.setActualStartTime(plan.getActualStartTime());
                    saveplan.setActualEndTime(plan.getActualEndTime());
                    saveplan.setAssigner(plan.getAssigner());
                    saveplan.setBeforePlanId(plan.getBeforePlanId());
                    saveplan.setFlowStatus(plan.getFlowStatus());
                    saveplan.setImplementation(plan.getImplementation());
                    saveplan.setMilestone(plan.getMilestone());
                    saveplan.setOwner(plan.getOwner());
                    saveplan.setParentPlanId(plan.getParentPlanId());
                    saveplan.setPlanEndTime(plan.getPlanEndTime());
                    saveplan.setPlanLevel(plan.getPlanLevel());
                    saveplan.setPlanName(plan.getPlanName());
                    saveplan.setPlanNumber(plan.getPlanNumber());
                    saveplan.setPlanOrder(plan.getPlanOrder());
                    saveplan.setPlanStartTime(plan.getPlanStartTime());
                    saveplan.setPreposeIds(plan.getPreposeIds());
                    saveplan.setPreposePlans(plan.getPreposePlans());
                    saveplan.setProgressRate(plan.getProgressRate());
                    saveplan.setProjectId(plan.getProjectId());
                    saveplan.setProjectStatus(plan.getProjectStatus());
                    saveplan.setRemark(plan.getRemark());
                    saveplan.setStoreyNo(plan.getStoreyNo());
                    saveplan.setWorkTime(plan.getWorkTime());
                    saveplan.setTaskType(plan.getTaskType());
                    saveplan.setTaskNameType(!CommonUtil.isEmpty(plan.getTaskNameType()) ? plan.getTaskNameType() : CommonConstants.NAMESTANDARD_TYPE_DEV);
                    sessionFacade.save(saveplan);


                    // 输入历史
                    Inputs inputs = new Inputs();
                    inputs.setUseObjectId(plan.getId());
                    inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                    List<Inputs> inputList = inputsService.queryInputList(inputs, 1, 10, false);
                    for (Inputs input : inputList) {
                        InputsHistory inputsHistory = new InputsHistory();
                        inputsHistory.setName(input.getName());
                        inputsHistory.setDocument(input.getDocument());
                        inputsHistory.setFileId(input.getFileId());
                        inputsHistory.setInputsId(input.getId());
                        inputsHistory.setOrigin(input.getOrigin());
                        inputsHistory.setRequired(input.getRequired());
                        inputsHistory.setUseObjectId(input.getUseObjectId());
                        inputsHistory.setUseObjectType(input.getUseObjectType());
                        inputsHistory.setOriginDeliverablesInfoId(input.getOriginDeliverablesInfoId());
                        inputsHistory.setOriginObjectId(input.getOriginObjectId());
                        inputsHistory.setOriginType(input.getOriginType());
                        inputsHistory.setOriginTypeExt(input.getOriginTypeExt());
                        inputsService.saveOrUpdate(inputsHistory);
                    }

                    // 输出历史
                    List<DeliverablesInfo> deliverablesInfoList = deliverablesInfoService.getDeliverablesByUseObeject(
                            PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());
                    for (DeliverablesInfo tempDeliverables : deliverablesInfoList) {
                        DeliverablesInfoHistory saveDeliverables = new DeliverablesInfoHistory();
                        saveDeliverables.setAvaliable(tempDeliverables.getAvaliable());
                        saveDeliverables.setBizCurrent(tempDeliverables.getBizCurrent());
                        saveDeliverables.setBizId(tempDeliverables.getBizId());
                        saveDeliverables.setBizVersion(tempDeliverables.getBizVersion());
                        if(!CommonUtil.isEmpty(tempDeliverables.getSecurityLevel())){
                            saveDeliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                        }else{
                            saveDeliverables.setSecurityLevel(Short.valueOf("1"));
                        }

                        saveDeliverables.setPlanCreateBy(tempDeliverables.getCreateBy());
                        saveDeliverables.setPlanCreateTime(tempDeliverables.getCreateTime());
                        saveDeliverables.setPlanUpdateBy(tempDeliverables.getUpdateBy());
                        saveDeliverables.setPlanUpdateName(tempDeliverables.getUpdateTime());
                        saveDeliverables.setBizVersion(tempDeliverables.getBizVersion());
                        saveDeliverables.setDocument(tempDeliverables.getDocument());
                        saveDeliverables.setName(tempDeliverables.getName());
                        saveDeliverables.setSecurityLevel(tempDeliverables.getSecurityLevel());
                        saveDeliverables.setUseObjectId(tempDeliverables.getUseObjectId());
                        saveDeliverables.setUseObjectType(tempDeliverables.getUseObjectType());
                        sessionFacade.saveOrUpdate(saveDeliverables);

                    }

                    // 资源历史
                    ResourceLinkInfoDto resourceLinkInfo = new ResourceLinkInfoDto();
                    resourceLinkInfo.setUseObjectId(plan.getId());
                    resourceLinkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                    List<ResourceLinkInfoDto> resourceLinkInfoList = resourceLinkInfoService.getList(resourceLinkInfo);
                    for (ResourceLinkInfoDto tempResourceLink : resourceLinkInfoList) {
                        ResourceLinkInfoHistory saveResourceLink = new ResourceLinkInfoHistory();
                        ;
                        saveResourceLink.setAvaliable(tempResourceLink.getAvaliable());
                        saveResourceLink.setBizCurrent(tempResourceLink.getBizCurrent());
                        saveResourceLink.setBizId(tempResourceLink.getBizId());
                        saveResourceLink.setBizVersion(tempResourceLink.getBizVersion());
                        if(!CommonUtil.isEmpty(tempResourceLink.getSecurityLevel())){
                            saveResourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                        }else{
                            saveResourceLink.setSecurityLevel(Short.valueOf("1"));
                        }
                        saveResourceLink.setPlanCreateBy(tempResourceLink.getCreateBy());
                        saveResourceLink.setPlanCreateTime(tempResourceLink.getCreateTime());
                        saveResourceLink.setPlanUpdateBy(tempResourceLink.getUpdateBy());
                        saveResourceLink.setPlanUpdateTime(tempResourceLink.getUpdateTime());
                        saveResourceLink.setResourceId(tempResourceLink.getResourceId());
                        Resource resource = new Resource();
                        try {
                            BeanUtil.copyBean2Bean(resource,tempResourceLink.getResourceInfo());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        saveResourceLink.setResourceInfo(resource);
                        if(!CommonUtil.isEmpty(tempResourceLink.getSecurityLevel())){
                            saveResourceLink.setSecurityLevel(tempResourceLink.getSecurityLevel());
                        }else{
                            saveResourceLink.setSecurityLevel(Short.valueOf("1"));
                        }
                        saveResourceLink.setResourceName(tempResourceLink.getResourceName());
                        saveResourceLink.setResourceType(tempResourceLink.getResourceType());
                        saveResourceLink.setUseObjectId(tempResourceLink.getUseObjectId());
                        saveResourceLink.setUseObjectType(tempResourceLink.getUseObjectType());
                        saveResourceLink.setUseRate(tempResourceLink.getUseRate());
                        sessionFacade.saveOrUpdate(saveResourceLink);
                    }
                }

                // 输入来源计划id
                Map<String, String> originObjectMap = new HashMap<String, String>();

                // 输入来源输入id
                Map<String, String> originInputMap = new HashMap<String, String>();

                // 输入来源输出id
                Map<String, String> originOutputMap = new HashMap<String, String>();
                Map<String, String> changePlanMap = new HashMap<String, String>();

                List<FlowTaskPreposeVo> allflowTaskPreposeList = new ArrayList<FlowTaskPreposeVo>();

                for (FlowTaskVo flowTask : changeFlowTaskList) {
                    if (StringUtils.isNotEmpty(flowTask.getPlanId())) {
                        changePlanMap.put(flowTask.getPlanId(), flowTask.getPlanId());
                    }
                    // List<FlowTaskPrepose> flowTaskinnerPreposeList =
                    // taskFlowResolveService.getChangeFlowTaskPreposeList(TemPlan);
                    List<FlowTaskPreposeVo> curList = taskFlowResolveService.getChangeFlowTaskOutPreposeList(flowTask);
                    if (!CommonUtil.isEmpty(curList)) {
                        allflowTaskPreposeList.addAll(curList);
                    }

                }

                List<Plan> delList = new ArrayList<Plan>();
                for (Plan plan : oldPlanList) {
                    if (StringUtils.isEmpty(changePlanMap.get(plan.getId()))) {
                        delList.add(plan);
                    }
                }

                for (Plan p : delList) {
                    planService.discardPlansForFlow(p.getId(), user);
                }

//                List<Plan> saveInputsBatchPlans = new ArrayList<Plan>();
//                List<Inputs> saveInputsBatchInputs = new ArrayList<Inputs>();
//                List<DeliverablesInfo> saveInputsBatchDelivers = new ArrayList<DeliverablesInfo>();

                List<Inputs> newInputList = new ArrayList<Inputs>();
                for (FlowTaskVo flowTask : changeFlowTaskList) {
                    if (StringUtils.isNotEmpty(flowTask.getPlanId())) {
                        Plan plan = (Plan) sessionFacade.getEntity(Plan.class, flowTask.getPlanId());
                        if (plan != null) {
                            // 更新计划
                            plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                            plan.setOwner(flowTask.getOwner());
                            plan.setCellId(flowTask.getCellId());
                            plan.setPlanName(flowTask.getPlanName());
                            plan.setPlanEndTime(flowTask.getPlanEndTime());
                            plan.setPlanLevel(flowTask.getPlanLevel());
                            plan.setPlanStartTime(flowTask.getPlanStartTime());
                            plan.setRemark(flowTask.getRemark());
                            plan.setWorkTime(flowTask.getWorkTime());
                            plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                            plan.setTaskNameType(flowTask.getTaskNameType());
                            plan.setTabCbTemplateId(flowTask.getTabCbTemplateId());
//                            //删除待办流程：
//                            delPlanReceivedProcInst(plan,plan.getPlanReceivedProcInstId());
//                            //设置生命周期：
//                            planService.updateBizCurrent(plan);
//                            //调用计划反馈引擎计算进度
//                            planService.updateProgressRate(plan);
                            sessionFacade.saveOrUpdate(plan);
//                            if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
//                                if(CommonUtil.isEmpty(plan.getPlanReceivedProcInstId())){
//                                    planService.startPlanReceivedProcess(plan,userId);
//                                }else if(!CommonUtil.isEmpty(plan.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(plan.getPlanReceivedCompleteTime())){
//                                    planService.startPlanReceivedProcess(plan,userId);
//                                }
//
//                            }
                            originObjectMap.put(flowTask.getId(), plan.getId());
                            String label = plan.getPlanName() + "," + plan.getWorkTime() + "天";
                            cellWorkTimeMap.put(plan.getCellId(), label);
                            // 删除该计划的所有前置关系（主要针对外部前置）
                            List<PreposePlan> currentPlanPreposes = preposePlanService.getPreposePlansByPlanId(plan);
                            if (!CommonUtil.isEmpty(currentPlanPreposes)) {
                                sessionFacade.deleteAllEntitie(currentPlanPreposes);
                            }

//                            flowTaskIdMap.put(flowTask.getId(), plan.getId());
                            // 更新计划的输入
                            List<Inputs> delInList = new ArrayList<Inputs>();
                            Map<String, String> inMap = new HashMap<String, String>();
                            for (FlowTaskInputsVo inputs : flowTask.getInputList()) {
                                if (StringUtils.isNotEmpty(inputs.getInputId())) {
                                    inMap.put(inputs.getInputId(), inputs.getInputId());
                                }
                            }

                            Inputs in = new Inputs();
                            in.setUseObjectId(plan.getId());
                            in.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                            List<Inputs> inputList = inputsService.queryInputList(in, 1, 10, false);
                            for (Inputs input : inputList) {
                                if (StringUtils.isEmpty(inMap.get(input.getId()))) {
                                    delInList.add(input);
                                }
                            }
                            // 不删除临时表
                            for (Inputs input : delInList) {
                                sessionFacade.delete(input);
                            }

                            for (FlowTaskInputsVo inputs : flowTask.getInputList()) {
                                if (StringUtils.isNotEmpty(inputs.getInputId())) {
                                    Inputs input = (Inputs) sessionFacade.getEntity(Inputs.class,
                                            inputs.getInputId());
                                    boolean flag = false;
                                    if (input == null) {
                                        input = new Inputs();
                                        input.setCreateBy(inputs.getCreateBy());
                                        input.setCreateFullName(inputs.getCreateFullName());
                                        input.setCreateName(inputs.getCreateName());
                                        input.setCreateTime(inputs.getCreateTime());
                                        flag = true;
                                    }
                                    input.setName(inputs.getName());
                                    input.setUseObjectId(plan.getId());
                                    input.setDocId(inputs.getDocId());
                                    input.setDocName(inputs.getDocName());
                                    if (StringUtils.isNotEmpty(inputs.getFileId())) {

                                        input.setFileId(inputs.getFileId());
                                    }
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setOrigin(inputs.getOrigin());
                                    input.setOriginObjectId(inputs.getOriginObjectId());
                                    input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                    input.setOriginType(inputs.getOriginType());
                                    input.setOriginTypeExt(inputs.getOriginTypeExt());
//                                    if (flag) {
//                                        input.setId(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()));
//                                        saveInputsBatchInputs.add(input);
//                                    } else {
//                                        inputsService.saveOrUpdate(input);
//                                    }
                                    inputsService.saveOrUpdate(input);
                                    newInputList.add(input);
                                    originInputMap.put(input.getId(),inputs.getInputId());
                                } else {
                                    Inputs input = new Inputs();
                                    input.setName(inputs.getName());
                                    input.setUseObjectId(plan.getId());
                                    input.setCreateBy(inputs.getCreateBy());
                                    input.setCreateFullName(inputs.getCreateFullName());
                                    input.setCreateName(inputs.getCreateName());
                                    input.setCreateTime(inputs.getCreateTime());
                                    input.setDocId(inputs.getDocId());
                                    input.setDocName(inputs.getDocName());
                                    input.setOriginObjectId(inputs.getOriginObjectId());
                                    input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                    input.setOriginType(inputs.getOriginType());
                                    input.setOriginTypeExt(inputs.getOriginTypeExt());
                                    if (StringUtils.isNotEmpty(inputs.getFileId())) {
                                        input.setFileId(inputs.getFileId());
                                    }
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setOrigin(inputs.getOrigin());
                                    inputsService.save(input);
                                    newInputList.add(input);
                                    originInputMap.put(input.getId(),inputs.getInputId());
                                }
                            }

                            // 更新计划的输出
                            List<DeliverablesInfo> delOutList = new ArrayList<DeliverablesInfo>();
                            Map<String, String> outMap = new HashMap<String, String>();
                            for (FlowTaskDeliverablesInfoVo outputs : flowTask.getOutputList()) {
                                if (StringUtils.isNotEmpty(outputs.getOutputId())) {
                                    outMap.put(outputs.getOutputId(), outputs.getOutputId());
                                }
                            }

                            List<DeliverablesInfo> outputList = deliverablesInfoService.getDeliverablesByUseObeject(
                                    PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());
                            for (DeliverablesInfo output : outputList) {
                                if (StringUtils.isEmpty(outMap.get(output.getId()))) {
                                    delOutList.add(output);
                                }
                            }
                            // 不删除临时表
                            for (DeliverablesInfo output : delOutList) {
                                sessionFacade.delete(output);
                            }

                            for (FlowTaskDeliverablesInfoVo outputs : flowTask.getOutputList()) {
                                if (StringUtils.isNotEmpty(outputs.getOutputId())) {
                                    DeliverablesInfo output = (DeliverablesInfo) sessionFacade.getEntity(
                                            DeliverablesInfo.class, outputs.getOutputId());
                                    if (output != null) {
                                    output.setName(outputs.getName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputs.getRequired());
                                    sessionFacade.saveOrUpdate(output);
                                    originOutputMap.put(outputs.getId(), output.getId());
                                    }
                                } else {
                                    DeliverablesInfo output = new DeliverablesInfo();
                                    planService.initBusinessObject(output);
                                    output.setCreateBy(outputs.getCreateBy());
                                    output.setCreateFullName(outputs.getCreateFullName());
                                    output.setCreateName(outputs.getCreateName());
                                    output.setCreateTime(outputs.getCreateTime());
                                    output.setName(outputs.getName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputs.getRequired());
//                                    if (!CommonUtil.isEmpty(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()))) {
//                                        output.setId(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()));
//                                        saveInputsBatchDelivers.add(output);
//                                    }
                                    originOutputMap.put(outputs.getId(), output.getId());
                                    sessionFacade.save(output);
                                }
                            }

                            // 更新计划的资源
                            List<ResourceLinkInfoDto> delResourceList = new ArrayList<>();
                            Map<String, String> resourceMap = new HashMap<String, String>();
                            for (FlowTaskResourceLinkInfoVo resource : flowTask.getResourceLinkList()) {
                                if (StringUtils.isNotEmpty(resource.getLinkInfoId())) {
                                    resourceMap.put(resource.getLinkInfoId(), resource.getLinkInfoId());
                                }
                            }

                            ResourceLinkInfoDto resourceLinkInfo = new ResourceLinkInfoDto();
                            resourceLinkInfo.setUseObjectId(plan.getId());
                            resourceLinkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                            List<ResourceLinkInfoDto> resourceList = resourceLinkInfoService.getList(resourceLinkInfo);
                            for (ResourceLinkInfoDto resource : resourceList) {
                                if (StringUtils.isEmpty(resourceMap.get(resource.getId()))) {
                                    delResourceList.add(resource);
                                }
                            }
                            // 不删除临时表
                            for (ResourceLinkInfoDto resource : delResourceList) {
                                sessionFacade.delete(resource);
                                ResourceEverydayuseInfo info = new ResourceEverydayuseInfo();
                                info.setUseObjectId(resource.getUseObjectId());
                                info.setUseObjectType(resource.getUseObjectType());
//                            resourceEverydayuseService.deleteResourceEverydayuseInfos(info);
                            }

                            for (FlowTaskResourceLinkInfoVo resourceLink : flowTask.getResourceLinkList()) {
                                if (StringUtils.isNotEmpty(resourceLink.getLinkInfoId())) {
                                    ResourceLinkInfo resource = (ResourceLinkInfo) sessionFacade.getEntity(
                                            ResourceLinkInfo.class, resourceLink.getLinkInfoId());
                                    resource.setResourceId(resourceLink.getResourceId());
                                    resource.setUseObjectId(plan.getId());
                                    resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    resource.setUseRate(resourceLink.getUseRate());
                                    resource.setStartTime(resourceLink.getStartTime());
                                    resource.setEndTime(resourceLink.getEndTime());
                                    sessionFacade.saveOrUpdate(resource);
                                    resourcelinks.add(resource);
                                } else {
                                    ResourceLinkInfo resource = new ResourceLinkInfo();
                                    initBusinessObject(resource);
                                    resource.setResourceId(resourceLink.getResourceId());
                                    resource.setUseObjectId(plan.getId());
                                    resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    resource.setUseRate(resourceLink.getUseRate());
                                    resource.setStartTime(resourceLink.getStartTime());
                                    resource.setEndTime(resourceLink.getEndTime());
                                    sessionFacade.save(resource);
                                    resourcelinks.add(resource);

                                }
                            }

                        } else {
                            // 新增
                            plan = new Plan();
//                            planService.initBusinessObject(plan);
                            plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                            plan.setCellId(flowTask.getCellId());
                            plan.setOwner(flowTask.getOwner());
                            plan.setPlanName(flowTask.getPlanName());
                            plan.setPlanEndTime(flowTask.getPlanEndTime());
                            plan.setPlanLevel(flowTask.getPlanLevel());
                            plan.setPlanStartTime(flowTask.getPlanStartTime());
                            plan.setRemark(flowTask.getRemark());
                            plan.setWorkTime(flowTask.getWorkTime());
                            plan.setPlanType(PlanConstants.PLAN_TYPE_FLOW);
                            plan.setProjectId(parent.getProjectId());
                            plan.setParentPlanId(parent.getId());
                            plan.setCreateBy(flowTask.getCreateBy());
                            plan.setCreateTime(flowTask.getCreateTime());
                            plan.setCreateFullName(flowTask.getCreateFullName());
                            plan.setCreateName(flowTask.getCreateName());
                            plan.setFormId(id);
                            if (StringUtils.isNotEmpty(flowTask.getLauncher())) {
                                plan.setAssigner(flowTask.getLauncher());
                            } else {
                                plan.setAssigner(parent.getOwner());
                            }

                            plan.setAssignTime(new Date());
                            plan.setLauncher(flowTask.getCreateBy());
                            plan.setLaunchTime(new Date());
                            plan.setTaskNameType(flowTask.getTaskType());
                            // if(planService.isReviewTask(flowTask.getPlanName())){
                            // plan.setTaskNameType("评审任务");
                            // }
                            plan.setPlanNumber(planService.getMaxPlanNumber(plan) + 1);
                            plan.setStoreyNo(planService.getMaxStoreyNo(plan) + 1);
//                            plan.setBizCurrent(PlanConstants.PLAN_FLOWSTATUS_ORDERED);
//                            //删除待办流程：
//                            delPlanReceivedProcInst(plan,plan.getPlanReceivedProcInstId());
//                            //设置生命周期：
//                            planService.updateBizCurrent(plan);
//                            //调用计划反馈引擎计算进度
//                            planService.updateProgressRate(plan);
//                            if (!CommonUtil.isEmpty(flowTaskIdStrMap.get(flowTask.getId()))) {
//                                plan.setId(flowTaskIdStrMap.get(flowTask.getId()));
//                                saveInputsBatchPlans.add(plan);
//                            }
                            plan.setTabCbTemplateId(flowTask.getTabCbTemplateId());
                            sessionFacade.saveOrUpdate(plan);
//                        forwardBusinessObject(plan);
//                            planService.saveFlowTaskAsCreate(plan, user);
//                            if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
//                                if(CommonUtil.isEmpty(plan.getPlanReceivedProcInstId())){
//                                    planService.startPlanReceivedProcess(plan,userId);
//                                }else if(!CommonUtil.isEmpty(plan.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(plan.getPlanReceivedCompleteTime())){
//                                    planService.startPlanReceivedProcess(plan,userId);
//                                }
//
//                            }
                            originObjectMap.put(flowTask.getId(), plan.getId());
//                            flowTaskIdMap.put(flowTask.getId(), plan.getId());
                            String label = plan.getPlanName() + "," + plan.getWorkTime() + "天";
                            cellWorkTimeMap.put(plan.getCellId(), label);

                            // 新增计划的输入
                            for (FlowTaskInputsVo inputs : flowTask.getInputList()) {
                                if (StringUtils.isNotEmpty(inputs.getInputId())) {
                                    Inputs input = (Inputs) sessionFacade.getEntity(Inputs.class, inputs.getId());
                                    input.setName(inputs.getName());
                                    input.setUseObjectId(plan.getId());
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setOrigin(inputs.getOrigin());
                                    input.setOriginObjectId(inputs.getOriginObjectId());
                                    input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                    input.setDocId(inputs.getDocId());
                                    input.setDocName(inputs.getDocName());
                                    input.setOriginType(inputs.getOriginType());
                                    input.setOriginTypeExt(inputs.getOriginTypeExt());
                                    inputsService.saveOrUpdate(input);
                                    newInputList.add(input);
                                    originInputMap.put(input.getId(),inputs.getInputId());
                                } else {
                                    Inputs input = new Inputs();
                                    input.setName(inputs.getName());
                                    input.setUseObjectId(plan.getId());
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setOrigin(inputs.getOrigin());
                                    input.setOriginObjectId(inputs.getOriginObjectId());
                                    input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                    input.setCreateBy(inputs.getCreateBy());
                                    input.setCreateFullName(inputs.getCreateFullName());
                                    input.setCreateName(inputs.getCreateName());
                                    input.setCreateTime(inputs.getCreateTime());
                                    input.setDocId(inputs.getDocId());
                                    input.setDocName(inputs.getDocName());
                                    input.setOriginType(inputs.getOriginType());
                                    input.setOriginTypeExt(inputs.getOriginTypeExt());
                                    inputsService.save(input);
//                                    if (!CommonUtil.isEmpty(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()))) {
//                                        input.setId(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()));
//                                        saveInputsBatchInputs.add(input);
//                                    }
                                    newInputList.add(input);
                                    originInputMap.put(input.getId(),inputs.getInputId());
                                }
                            }

                            // 新增计划的输出
                            for (FlowTaskDeliverablesInfoVo outputs : flowTask.getOutputList()) {
                                if (StringUtils.isNotEmpty(outputs.getOutputId())) {
                                    DeliverablesInfo output = (DeliverablesInfo) sessionFacade.getEntity(
                                            DeliverablesInfo.class, outputs.getId());
                                    output.setName(outputs.getName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputs.getRequired());
                                    sessionFacade.saveOrUpdate(output);
                                    originOutputMap.put(outputs.getId(), output.getId());
                                } else {
                                    DeliverablesInfo output = new DeliverablesInfo();
                                    planService.initBusinessObject(output);
                                    output.setCreateBy(outputs.getCreateBy());
                                    output.setCreateFullName(outputs.getCreateFullName());
                                    output.setCreateName(outputs.getCreateName());
                                    output.setCreateTime(outputs.getCreateTime());
                                    output.setName(outputs.getName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputs.getRequired());
                                    sessionFacade.save(output);
//                                    if (!CommonUtil.isEmpty(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()))) {
//                                        output.setId(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()));
//                                        saveInputsBatchDelivers.add(output);
//                                    }                             
                                    originOutputMap.put(outputs.getId(), output.getId());
                                }
                            }

                            // 新增计划的资源
                            for (FlowTaskResourceLinkInfoVo resourceLink : flowTask.getResourceLinkList()) {
                                if (StringUtils.isNotEmpty(resourceLink.getLinkInfoId())) {
                                    ResourceLinkInfo resource = (ResourceLinkInfo) sessionFacade.getEntity(
                                            ResourceLinkInfo.class, resourceLink.getLinkInfoId());
                                    resource.setResourceId(resourceLink.getResourceId());
                                    resource.setUseObjectId(plan.getId());
                                    resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    resource.setUseRate(resourceLink.getUseRate());
                                    resource.setStartTime(resourceLink.getStartTime());
                                    resource.setEndTime(resourceLink.getEndTime());
                                    sessionFacade.saveOrUpdate(resource);
                                    resourcelinks.add(resource);
                                } else {
                                    ResourceLinkInfo resource = new ResourceLinkInfo();
                                    initBusinessObject(resource);
                                    resource.setResourceId(resourceLink.getResourceId());
                                    resource.setUseObjectId(plan.getId());
                                    resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    resource.setUseRate(resourceLink.getUseRate());
                                    resource.setStartTime(resourceLink.getStartTime());
                                    resource.setEndTime(resourceLink.getEndTime());
                                    sessionFacade.save(resource);
                                    resourcelinks.add(resource);
                                }
                            }
                        }
                    } else {
                        // 新增
                        Plan plan = new Plan();
                        planService.initBusinessObject(plan);
                        plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                        plan.setCellId(flowTask.getCellId());
                        plan.setOwner(flowTask.getOwner());
                        plan.setPlanName(flowTask.getPlanName());
                        plan.setPlanEndTime(flowTask.getPlanEndTime());
                        plan.setPlanLevel(flowTask.getPlanLevel());
                        plan.setPlanStartTime(flowTask.getPlanStartTime());
                        plan.setRemark(flowTask.getRemark());
                        plan.setWorkTime(flowTask.getWorkTime());
                        plan.setPlanType(PlanConstants.PLAN_TYPE_FLOW);
                        plan.setProjectId(parent.getProjectId());
                        plan.setParentPlanId(parent.getId());
                        plan.setCreateBy(flowTask.getCreateBy());
                        plan.setCreateTime(flowTask.getCreateTime());
                        plan.setCreateFullName(flowTask.getCreateFullName());
                        plan.setCreateName(flowTask.getCreateName());
                        plan.setFormId(id);
                        plan.setFirstBy(flowTask.getCreateBy());
                        plan.setFirstTime(flowTask.getCreateTime());
                        plan.setFirstFullName(flowTask.getCreateFullName());
                        plan.setFirstName(flowTask.getCreateName());
                        if (StringUtils.isNotEmpty(flowTask.getLauncher())) {
                            plan.setAssigner(flowTask.getLauncher());
                            plan.setLauncher(flowTask.getLauncher());
                        } else {
                            plan.setAssigner(parent.getOwner());
                            plan.setLauncher(parent.getOwner());
                        }
                        plan.setAssignTime(new Date());
                        plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                        plan.setTaskNameType(!CommonUtil.isEmpty(flowTask.getTaskNameType()) ? flowTask.getTaskNameType() : CommonConstants.NAMESTANDARD_TYPE_DEV);
                        plan.setTabCbTemplateId(flowTask.getTabCbTemplateId());
                        plan.setLaunchTime(new Date());

                        if (planService.isReviewTask(flowTask.getPlanName())) {
                            plan.setTaskNameType("评审任务");
                        }
                        plan.setPlanNumber(planService.getMaxPlanNumber(plan) + 1);
                        plan.setStoreyNo(planService.getMaxStoreyNo(plan) + 1);
//                        plan.setBizCurrent(PlanConstants.PLAN_FLOWSTATUS_ORDERED);
//                        if (!CommonUtil.isEmpty(flowTaskIdStrMap.get(flowTask.getId()))) {
//                            plan.setId(flowTaskIdStrMap.get(flowTask.getId()));
//                            saveInputsBatchPlans.add(plan);
//                        }
//                        //删除待办流程：
//                        delPlanReceivedProcInst(plan,plan.getPlanReceivedProcInstId());
//                        //设置生命周期：
//                        planService.updateBizCurrent(plan);
//                        //调用计划反馈引擎计算进度
//                        planService.updateProgressRate(plan);
                        sessionFacade.saveOrUpdate(plan);
//                        planService.saveFlowTaskAsCreate(plan, user);
//                        if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
//                            if(CommonUtil.isEmpty(plan.getPlanReceivedProcInstId())){
//                                planService.startPlanReceivedProcess(plan,userId);
//                            }else if(!CommonUtil.isEmpty(plan.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(plan.getPlanReceivedCompleteTime())){
//                                planService.startPlanReceivedProcess(plan,userId);
//                            }
//
//                        }
                        originObjectMap.put(flowTask.getId(), plan.getId());

//                        flowTaskIdMap.put(flowTask.getId(), plan.getId());
                        String label = plan.getPlanName() + "," + plan.getWorkTime() + "天";
                        cellWorkTimeMap.put(plan.getCellId(), label);

                        // 新增计划的输入
                        for (FlowTaskInputsVo inputs : flowTask.getInputList()) {
                            if (StringUtils.isNotEmpty(inputs.getInputId())) {
                                Inputs input = (Inputs) sessionFacade.getEntity(Inputs.class, inputs.getId());
                                input.setName(inputs.getName());
                                input.setUseObjectId(plan.getId());
                                input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                input.setOrigin(inputs.getOrigin());
                                input.setOriginObjectId(inputs.getOriginObjectId());
                                input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                input.setDocId(inputs.getDocId());
                                input.setDocName(inputs.getDocName());
                                input.setOriginType(inputs.getOriginType());
                                input.setOriginTypeExt(inputs.getOriginTypeExt());
                                inputsService.saveOrUpdate(input);
                                newInputList.add(input);
                                originInputMap.put(input.getId(),inputs.getInputId());
                            } else {
                                Inputs input = new Inputs();
                                input.setName(inputs.getName());
                                input.setUseObjectId(plan.getId());
                                input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                input.setOrigin(inputs.getOrigin());
                                input.setOriginObjectId(inputs.getOriginObjectId());
                                input.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                                input.setCreateBy(inputs.getCreateBy());
                                input.setCreateFullName(inputs.getCreateFullName());
                                input.setCreateName(inputs.getCreateName());
                                input.setCreateTime(inputs.getCreateTime());
                                input.setDocId(inputs.getDocId());
                                input.setDocName(inputs.getDocName());
                                input.setOriginType(inputs.getOriginType());
                                input.setOriginTypeExt(inputs.getOriginTypeExt());
//                                if (!CommonUtil.isEmpty(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()))) {
//                                    input.setId(flowTaskInputsIdAndRDTaskInputsIdStrMap.get(inputs.getId()));
//                                    saveInputsBatchInputs.add(input);
//                                }
                                inputsService.save(input);
                                newInputList.add(input);
                                originInputMap.put(input.getId(),inputs.getInputId());
                            }
                        }

                        // 新增计划的输出
                        for (FlowTaskDeliverablesInfoVo outputs : flowTask.getOutputList()) {
                            if (StringUtils.isNotEmpty(outputs.getOutputId())) {
                                DeliverablesInfo output = (DeliverablesInfo) sessionFacade.getEntity(
                                        DeliverablesInfo.class, outputs.getId());
                                output.setName(outputs.getName());
                                output.setUseObjectId(plan.getId());
                                output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                output.setRequired(outputs.getRequired());
                                sessionFacade.saveOrUpdate(output);
                                originOutputMap.put(outputs.getId(), output.getId());
                            } else {
                                DeliverablesInfo output = new DeliverablesInfo();
                                planService.initBusinessObject(output);
                                output.setCreateBy(outputs.getCreateBy());
                                output.setCreateFullName(outputs.getCreateFullName());
                                output.setCreateName(outputs.getCreateName());
                                output.setCreateTime(outputs.getCreateTime());
                                output.setName(outputs.getName());
                                output.setUseObjectId(plan.getId());
                                output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                output.setRequired(outputs.getRequired());
                                sessionFacade.save(output);
//                                if (!CommonUtil.isEmpty(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()))) {
//                                    output.setId(flowTaskDeliversIdAndRDTaskDeliversIdStrMap.get(outputs.getId()));
//                                    saveInputsBatchDelivers.add(output);
//                                }
                                originOutputMap.put(outputs.getId(), output.getId());

                            }
                        }

                        // 新增计划的资源
                        for (FlowTaskResourceLinkInfoVo resourceLink : flowTask.getResourceLinkList()) {
                            if (StringUtils.isNotEmpty(resourceLink.getLinkInfoId())) {
                                ResourceLinkInfo resource = (ResourceLinkInfo) sessionFacade.getEntity(
                                        ResourceLinkInfo.class, resourceLink.getLinkInfoId());
                                resource.setResourceId(resourceLink.getResourceId());
                                resource.setUseObjectId(plan.getId());
                                resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                resource.setUseRate(resourceLink.getUseRate());
                                resource.setStartTime(resourceLink.getStartTime());
                                resource.setEndTime(resourceLink.getEndTime());
                                sessionFacade.saveOrUpdate(resource);
                                resourcelinks.add(resource);
                            } else {
                                ResourceLinkInfo resource = new ResourceLinkInfo();
                                initBusinessObject(resource);
                                resource.setResourceId(resourceLink.getResourceId());
                                resource.setUseObjectId(plan.getId());
                                resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                resource.setUseRate(resourceLink.getUseRate());
                                resource.setStartTime(resourceLink.getStartTime());
                                resource.setEndTime(resourceLink.getEndTime());
                                sessionFacade.save(resource);
                                resourcelinks.add(resource);
                            }
                        }

                        ApprovePlanInfo approveInfo = new ApprovePlanInfo();
                        approveInfo.setFormId(id);
                        approveInfo.setPlanId(plan.getId());
                        sessionFacade.saveOrUpdate(approveInfo);
                    }
                }
                // 更新FlowTaskCellConnect
                Plan parentCondition = new Plan();
                parentCondition.setParentPlanId(parent.getId());
                List<Plan> newPlanList = planService.queryPlanList(parentCondition, 1, 10, false);
                List<FlowTaskCellConnectVo> flowTaskCellConnectVoList = new ArrayList<FlowTaskCellConnectVo>();
                for (ChangeFlowTaskCellConnectVo cellConnect : changeFlowTaskConnectList) {
                    FlowTaskCellConnectVo connect = new FlowTaskCellConnectVo();
                    connect.setId(UUIDGenerator.generate().toString());
                    connect.setParentPlanId(parent.getId());
                    connect.setCellId(cellConnect.getCellId());
                    connect.setInfoId(cellConnect.getInfoId());
                    connect.setTargetId(cellConnect.getTargetId());
                    connect.setTargetInfoId(cellConnect.getTargetInfoId());
                    // 通过节点与模板ID获得基本信息ID
                    if (StringUtils.isNotEmpty(connect.getCellId())) {
                        for (Plan f : newPlanList) {
                            if (connect.getCellId().equals(f.getCellId())) {
                                connect.setInfoId(f.getId());
                                break;
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(connect.getTargetId())) {
                        for (Plan f : newPlanList) {
                            if (connect.getTargetId().equals(f.getCellId())) {
                                connect.setTargetInfoId(f.getId());
                                break;
                            }
                        }
                    }
                    flowTaskCellConnectVoList.add(connect);
                }
                planService.saveFlowTaskCellConnect(flowTaskCellConnectVoList);

                // 更新内部前置
                List<FlowTaskCellConnectVo> flowTaskConnectList = taskFlowResolveService.getFlowTaskConnectList(parent.getId());
                for (FlowTaskCellConnectVo cellConnect : flowTaskConnectList) {
                    if (!TaskProcConstants.TASK_CELL_START.equals(cellConnect.getCellId())
                            && !TaskProcConstants.TASK_CELL_END.equals(cellConnect.getTargetId())) {
                        PreposePlan prepose = new PreposePlan();
                        prepose.setPlanId(cellConnect.getTargetInfoId());
                        prepose.setPreposePlanId(cellConnect.getInfoId());
                        sessionFacade.saveOrUpdate(prepose);
                    }
                }

                // 更新外部前置
                for (FlowTaskPreposeVo preposeTask : allflowTaskPreposeList) {
                    if (!flowTaskPreposeList.contains(preposeTask)
                            && !preposeTask.getPreposeId().startsWith(PlanConstants.PLAN_CREATE_UUID)) {
                        PreposePlan prepose = new PreposePlan();
                        if(StringUtils.isNotEmpty(originObjectMap.get(preposeTask.getFlowTaskId()))) {
                            prepose.setPlanId(originObjectMap.get(preposeTask.getFlowTaskId()));
                        }
                        prepose.setPreposePlanId(preposeTask.getPreposeId());
                        sessionFacade.saveOrUpdate(prepose);
                    }
                }

                // 更新输入的来源信息
                for (Inputs in : newInputList) {
                    if (StringUtils.isNotEmpty(originObjectMap.get(in.getOriginObjectId()))
                            && StringUtils.isNotEmpty(in.getOriginDeliverablesInfoId())) {
                        Inputs curInput = (Inputs) sessionFacade.getEntity(Inputs.class, in.getId());
                        curInput.setOriginObjectId(originObjectMap.get(in.getOriginObjectId()));
                        curInput.setOriginDeliverablesInfoId(originOutputMap.get(in.getOriginDeliverablesInfoId()));
                        sessionFacade.saveOrUpdate(curInput);
                    }

                }
                // 变更之后将节点对应的工期在XML中更新
                String flowResolveXml = flowTaskParent.getFlowResolveXml();
                flowResolveXml = taskFlowResolveService.refreshFlowResolveXml(flowResolveXml,
                        cellWorkTimeMap);
                parent.setFlowResolveXml(flowResolveXml);
                sessionFacade.saveOrUpdate(parent);

                for(String curKey:originObjectMap.keySet()){
                    String curId = originObjectMap.get(curKey);
                    Plan curPlan =  (Plan)sessionFacade.getEntity(Plan.class,curId);
                        //删除待办流程：
                        delPlanReceivedProcInst(curPlan,curPlan.getPlanReceivedProcInstId());
                        //设置生命周期：
                        planService.updateBizCurrent(curPlan);
                        //调用计划反馈引擎计算进度
                        planService.updateProgressRate(curPlan);
                        sessionFacade.saveOrUpdate(curPlan);
                        planService.saveFlowTaskAsCreate(curPlan, user);
                        if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(curPlan.getBizCurrent())){
                            planService.startPlanReceivedProcess(curPlan,userId);
                        }
                }

//                List<String> httpUrls = new ArrayList<String>();
//                if (!CommonUtil.isEmpty(httpUrls)) {
//                    FlowTaskOutChangeVO flowTaskOutChangeVO = new FlowTaskOutChangeVO();
//                    flowTaskOutChangeVO.setParentPlanId(parent.getId());
//                    List<FlowTaskNodeVO> nodes = new ArrayList<FlowTaskNodeVO>();
//                    FlowTaskNodeVO nodeVO = new FlowTaskNodeVO();
//                    List<FlowTaskDeliverVO> delivers = new ArrayList<FlowTaskDeliverVO>();
//                    FlowTaskDeliverVO deliverVO = new FlowTaskDeliverVO();
//                    List<Plan> newPlans = sessionFacade.findHql(
//                            "from Plan where parentPlanId = ? and avaliable = '1' ", parent.getId());
//                    List<NameStandard> nameStandards = sessionFacade.findHql(
//                            "from NameStandard where name in (select planName from Plan where parentPlanId = ? and avaliable = '1' ) ",
//                            parent.getId());
//                    Map<String, String> nameStandardMap = new HashMap<String, String>();
//                    for (NameStandard ns : nameStandards) {
//                        nameStandardMap.put(ns.getName(), ns.getId());
//                    }
//                    List<Inputs> newInputs = sessionFacade.findHql(
//                            " from Inputs where useObjectId in ( select id from Plan where parentPlanId = ? and avaliable = '1' ) ",
//                            parent.getId());
//                    List<DeliverablesInfo> newOutputs = sessionFacade.findHql(
//                            " from DeliverablesInfo where useObjectId in ( select id from Plan where parentPlanId = ? and avaliable = '1' ) ",
//                            parent.getId());
//                    List<DeliveryStandard> deliveryStandards = sessionFacade.findByQueryString("from DeliveryStandard");
//                    Map<String, String> deliveryStandardMap = new HashMap<String, String>();
//                    for (DeliveryStandard d : deliveryStandards) {
//                        deliveryStandardMap.put(d.getName(), d.getId());
//                    }
//
//                    for (Plan pl : newPlans) {
//                        nodeVO = new FlowTaskNodeVO();
//                        nodeVO.setNewTaskId(pl.getId());
//                        if (!CommonUtil.isEmpty(oldIdCellIdMap)
//                                && !CommonUtil.isEmpty(oldIdCellIdMap.get(pl.getCellId()))) {
//                            nodeVO.setOldTaskId(oldIdCellIdMap.get(pl.getCellId()));
//                        }
//                        if (!CommonUtil.isEmpty(nameStandardMap)
//                                && !CommonUtil.isEmpty(nameStandardMap.get(pl.getPlanName()))) {
//                            nodeVO.setNameStandardId(nameStandardMap.get(pl.getPlanName()));
//                        }
//                        delivers = new ArrayList<FlowTaskDeliverVO>();
//                        for (Inputs in : newInputs) {
//                            if (pl.getId().equals(in.getUseObjectId())) {
//                                deliverVO = new FlowTaskDeliverVO();
//                                deliverVO.setNewId(in.getId());
//                                if (!CommonUtil.isEmpty(deliveryStandardMap)
//                                        && !CommonUtil.isEmpty(deliveryStandardMap.get(in.getName()))) {
//                                    deliverVO.setDeliverId(deliveryStandardMap.get(in.getName()));
//                                }
//                                deliverVO.setInOrOut("INPUT");
//                                deliverVO.setSourceId(in.getOriginObjectId());
//                                deliverVO.setSourceDeliverSeq(in.getOriginDeliverablesInfoId());
//                                delivers.add(deliverVO);
//                            }
//                        }
//
//                        for (DeliverablesInfo out : newOutputs) {
//                            if (pl.getId().equals(out.getUseObjectId())) {
//                                deliverVO = new FlowTaskDeliverVO();
//                                deliverVO.setNewId(out.getId());
//                                if (!CommonUtil.isEmpty(deliveryStandardMap)
//                                        && !CommonUtil.isEmpty(deliveryStandardMap.get(out.getName()))) {
//                                    deliverVO.setDeliverId(deliveryStandardMap.get(out.getName()));
//                                }
//                                deliverVO.setInOrOut("OUTPUT");
//                                delivers.add(deliverVO);
//                            }
//                        }
//                        nodeVO.setDelivers(delivers);
//                        nodes.add(nodeVO);
//                    }
//                    flowTaskOutChangeVO.setNodes(nodes);
//                    for (String url : httpUrls) {
//                        try {
//                            HttpClientUtil.httpClientPostByTest(url, flowTaskOutChangeVO);
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                Connection conn = null;
//                // 插入计划及相关信息
//                PreparedStatement psForPlan = null;
//                PreparedStatement psForInputs = null;
//                PreparedStatement psForDeliverablesInfo = null;
//                if (!CommonUtil.isEmpty(changeFlowTaskList)) {
//                    try {
//                        conn = jdbcTemplate.getDataSource().getConnection();
//                        conn.setAutoCommit(false);
//                        // 批量插入计划
//                        if (!CommonUtil.isEmpty(saveInputsBatchPlans)) {
//                            String sqlForPlan = " insert into PM_PLAN ("
//                                    + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
//                                    + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
//                                    + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
//                                    + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
//                                    + " SECURITYLEVEL, AVALIABLE,"
//                                    + " PLANNAME, PLANNUMBER, PROJECTID, PARENTPLANID,"
//                                    + " PLANLEVEL, PLANSTARTTIME, PLANENDTIME, "
//                                    + " PROGRESSRATE, STOREYNO, MILESTONE, PLANSOURCE,"
//                                    + " WORKTIME, WORKTIMEREFERENCE, TASKNAMETYPE, TASKTYPE, "
//                                    + " FLOWSTATUS, CELLID, FROMTEMPLATE, REQUIRED, OWNER,launcher,launchtime,assigner,assigntime) "
//                                    + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'),"
//                                    + " to_date(?, 'yyyy-mm-dd'), ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?,'NORMAL'," + " ?, 'true', ?, ?, ?, ?, ?,?)";
//                            psForPlan = conn.prepareStatement(sqlForPlan);
//                            for (Plan plan : saveInputsBatchPlans) {
//                                psForPlan.setObject(1, plan.getId());
//                                psForPlan.setObject(2, plan.getCreateTime());
//                                psForPlan.setObject(3, plan.getCreateBy());
//                                psForPlan.setObject(4, plan.getCreateFullName());
//                                psForPlan.setObject(5, plan.getCreateName());
//                                psForPlan.setObject(6, plan.getUpdateTime());
//                                psForPlan.setObject(7, plan.getUpdateBy());
//                                psForPlan.setObject(8, plan.getUpdateFullName());
//                                psForPlan.setObject(9, plan.getUpdateName());
//                                psForPlan.setObject(10, plan.getFirstTime());
//                                psForPlan.setObject(11, plan.getFirstBy());
//                                psForPlan.setObject(12, plan.getFirstFullName());
//                                psForPlan.setObject(13, plan.getFirstName());
//                                psForPlan.setObject(14, plan.getPolicy().getId());
//                                psForPlan.setObject(15, plan.getBizCurrent());
//                                psForPlan.setObject(16, plan.getBizId());
//                                psForPlan.setObject(17, plan.getBizVersion());
//                                if(CommonUtil.isEmpty(plan.getSecurityLevel())) {
//                                    psForPlan.setObject(18, 1);  
//                                }else {
//                                    psForPlan.setObject(18, plan.getSecurityLevel());
//                                }
//                                psForPlan.setObject(19, plan.getAvaliable());
//                                psForPlan.setObject(20, plan.getPlanName());
//                                psForPlan.setObject(21, plan.getPlanNumber());
//                                psForPlan.setObject(22, plan.getProjectId());
//                                psForPlan.setObject(23, plan.getParentPlanId());
//                                psForPlan.setObject(24, plan.getPlanLevel());
//                                psForPlan.setObject(25,
//                                        DateUtil.dateToString(plan.getPlanStartTime(), DateUtil.YYYY_MM_DD));
//                                psForPlan.setObject(26,
//                                        DateUtil.dateToString(plan.getPlanEndTime(), DateUtil.YYYY_MM_DD));
//                                psForPlan.setObject(27, plan.getProgressRate());
//                                psForPlan.setObject(28, plan.getStoreyNo());
//                                psForPlan.setObject(29, plan.getMilestone());
//                                psForPlan.setObject(30, plan.getPlanSource());
//                                psForPlan.setObject(31, plan.getWorkTime());
//                                psForPlan.setObject(32, plan.getWorkTimeReference());
//                                psForPlan.setObject(33, plan.getTaskNameType());
//                                psForPlan.setObject(34, plan.getTaskType());
//                                psForPlan.setObject(35, plan.getCellId());
//                                psForPlan.setObject(36,
//                                        StringUtils.isEmpty(plan.getRequired()) ? "" : plan.getRequired());
//                                psForPlan.setObject(37,
//                                        StringUtils.isEmpty(plan.getOwner()) ? "" : plan.getOwner());
//                                psForPlan.setObject(38, plan.getLauncher());
//                                if (!CommonUtil.isEmpty(plan.getLaunchTime())) {
//                                    psForPlan.setObject(39, new Timestamp(plan.getLaunchTime().getTime()));
//                                } else {
//                                    psForPlan.setObject(39, null);
//                                }
//                                psForPlan.setObject(40, plan.getAssigner());
//                                if (!CommonUtil.isEmpty(plan.getAssignTime())) {
//                                    psForPlan.setObject(41, new Timestamp(plan.getAssignTime().getTime()));
//                                } else {
//                                    psForPlan.setObject(41, null);
//                                }
//                                psForPlan.addBatch();
//                            }
//                            psForPlan.executeBatch();
//                        }
//
//                        // 批量插入输入
//                        if (!CommonUtil.isEmpty(saveInputsBatchInputs)) {
//                            String sqlForInputs = " insert into PM_INPUTS ("
//                                    + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
//                                    + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, USEOBJECTID,"
//                                    + " USEOBJECTTYPE, NAME, ORIGINOBJECTID, ORIGINDELIVERABLESINFOID,DOCID,DOCNAME,OriginType,OriginTypeExt) "
//                                    + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?,?,?,?,?)";
//                            psForInputs = conn.prepareStatement(sqlForInputs);
//                            for (Inputs inputs : saveInputsBatchInputs) {
//                                psForInputs.setObject(1, inputs.getId());
//                                psForInputs.setObject(2, inputs.getCreateTime());
//                                psForInputs.setObject(3, inputs.getCreateBy());
//                                psForInputs.setObject(4, inputs.getCreateFullName());
//                                psForInputs.setObject(5, inputs.getCreateName());
//                                psForInputs.setObject(6, inputs.getUpdateTime());
//                                psForInputs.setObject(7, inputs.getUpdateBy());
//                                psForInputs.setObject(8, inputs.getUpdateFullName());
//                                psForInputs.setObject(9, inputs.getUpdateName());
//                                psForInputs.setObject(10, inputs.getUseObjectId());
//                                psForInputs.setObject(11, inputs.getUseObjectType());
//                                psForInputs.setObject(12, inputs.getName());
//                                psForInputs.setObject(13, inputs.getOriginObjectId());
//                                psForInputs.setObject(14, inputs.getOriginDeliverablesInfoId());
//                                psForInputs.setObject(15, inputs.getDocId());
//                                psForInputs.setObject(16, inputs.getDocName());
//                                psForInputs.setObject(17, inputs.getOriginType());
//                                psForInputs.setObject(18, inputs.getOriginTypeExt());
//                                psForInputs.addBatch();
//                            }
//                            psForInputs.executeBatch();
//                        }
//
//                        // 批量插入计划交付项
//                        if (!CommonUtil.isEmpty(saveInputsBatchDelivers)) {
//                            String sqlForDeliverablesInfo = " insert into PM_DELIVERABLES_INFO ("
//                                    + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
//                                    + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
//                                    + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
//                                    + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
//                                    + " SECURITYLEVEL, AVALIABLE,"
//                                    + " USEOBJECTID, USEOBJECTTYPE, NAME, ORIGIN, REQUIRED) "
//                                    + " values (" + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
//                                    + " ?, ?, ?, ?, ?," + " ?, ?, ?,?)";
//                            psForDeliverablesInfo = conn.prepareStatement(sqlForDeliverablesInfo);
//                            for (DeliverablesInfo info : saveInputsBatchDelivers) {
//                                psForDeliverablesInfo.setObject(1, info.getId());
//                                psForDeliverablesInfo.setObject(2, info.getCreateTime());
//                                psForDeliverablesInfo.setObject(3, info.getCreateBy());
//                                psForDeliverablesInfo.setObject(4, info.getCreateFullName());
//                                psForDeliverablesInfo.setObject(5, info.getCreateName());
//                                psForDeliverablesInfo.setObject(6, info.getUpdateTime());
//                                psForDeliverablesInfo.setObject(7, info.getUpdateBy());
//                                psForDeliverablesInfo.setObject(8, info.getUpdateFullName());
//                                psForDeliverablesInfo.setObject(9, info.getUpdateName());
//                                psForDeliverablesInfo.setObject(10, info.getFirstTime());
//                                psForDeliverablesInfo.setObject(11, info.getFirstBy());
//                                psForDeliverablesInfo.setObject(12, info.getFirstFullName());
//                                psForDeliverablesInfo.setObject(13, info.getFirstName());
//                                psForDeliverablesInfo.setObject(14, info.getPolicy().getId());
//                                psForDeliverablesInfo.setObject(15, info.getBizCurrent());
//                                psForDeliverablesInfo.setObject(16, info.getBizId());
//                                psForDeliverablesInfo.setObject(17, info.getBizVersion());
//                                psForDeliverablesInfo.setObject(18, info.getSecurityLevel());
//                                psForDeliverablesInfo.setObject(19, info.getAvaliable());
//                                psForDeliverablesInfo.setObject(20, info.getUseObjectId());
//                                psForDeliverablesInfo.setObject(21, info.getUseObjectType());
//                                psForDeliverablesInfo.setObject(22, info.getName());
//                                psForDeliverablesInfo.setObject(23, info.getOrigin());
//                                psForDeliverablesInfo.setObject(24, info.getRequired());
//                                psForDeliverablesInfo.addBatch();
//                            }
//                            psForDeliverablesInfo.executeBatch();
//                        }
//
//                        conn.commit();
//                    } catch (Exception ex) {
//                        msg = "false";
//                        try {
//                            conn.rollback();
//                        } catch (SQLException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            conn.setAutoCommit(true);
//                            DBUtil.closeConnection(null, psForPlan, conn);
//                            DBUtil.closeConnection(null, psForInputs, conn);
//                            DBUtil.closeConnection(null, psForDeliverablesInfo, conn);
//
//                        } catch (SQLException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//
//                }

            }
        }
    }
    
    @Override
    public void delPlanReceivedProcInst(Plan plan,String processInstanceId) {
        if ("1".equals(plan.getAvaliable()) && !CommonUtil.isEmpty(plan.getPlanReceivedProcInstId())) {
            try {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(plan.getId(),
                        plan.getPlanReceivedProcInstId(), "delete");
            } catch (Exception e) {
                // TODO Auto-generated catch block

            }

        }
    }

    @Override
    public void modifyResourceMassForWork(List<ResourceLinkInfo> resourceLst, String[] useRate,
                                          String[] endTime, String[] startTime)
            throws ParseException {
        String useObjectId = "";
        for (int i = 0; i < resourceLst.size(); i++ ) {
            ResourceLinkInfo p = resourceLst.get(i);
            if (StringUtils.isNotBlank(p.getId())) {
                ResourceLinkInfo resourceLinkInfo = (ResourceLinkInfo) sessionFacade.getEntity(ResourceLinkInfo.class,p.getId());

                if (useRate != null && useRate.length > 0) {
                    resourceLinkInfo.setUseRate(useRate[i]);
                }

                Date startTime2 = DateUtil.getDateFromString(startTime[i], DateUtil.YYYY_MM_DD);
                resourceLinkInfo.setStartTime(startTime2);

                Date endTime2 = DateUtil.getDateFromString(endTime[i], DateUtil.YYYY_MM_DD);
                resourceLinkInfo.setEndTime(endTime2);

                sessionFacade.saveOrUpdate(resourceLinkInfo);
            }
            else {
                if (useRate != null && useRate.length > 0) {
                    p.setUseRate(useRate[i]);
                }

                Date startTime2 = DateUtil.getDateFromString(startTime[i], DateUtil.YYYY_MM_DD);
                p.setStartTime(startTime2);

                Date endTime2 = DateUtil.getDateFromString(endTime[i], DateUtil.YYYY_MM_DD);
                p.setEndTime(endTime2);

                sessionFacade.saveOrUpdate(p);
            }
            if (StringUtils.isEmpty(useObjectId)) {
                useObjectId = p.getUseObjectId();
            }

        }

        // 在线程中更新资源每日使用信息
        ResourceEverydayuseInfosThread resourceEverydayuseInfosThread = new ResourceEverydayuseInfosThread();
        resourceEverydayuseInfosThread.setType(CommonConfigConstants.OBJECT_TYPE_RESOURCE);
        resourceEverydayuseInfosThread.setOperationType(CommonConfigConstants.RESOURCEEVERYDAYUSE_OPERATIONTYPE_INSERT);
        Plan plan = planService.getEntity(useObjectId);
        if (plan != null) {
            resourceEverydayuseInfosThread.setProjectId(plan.getProjectId());
        }
        resourceEverydayuseInfosThread.setLinkInfoList(resourceLst);
        resourceEverydayuseInfosThread.setResourceEverydayuseService(resourceEverydayuseService);
        new Thread(resourceEverydayuseInfosThread).start();
    }

    @Override
    public void saveModifyListForWork(List<Object> planList) throws ParseException {
        for (int i = 0; i < planList.size(); i++ ) {
            Map<String,String> planMap = (Map<String,String>) JSON.parse(String.valueOf(planList.get(i)));
            Plan p = (Plan)sessionFacade.getEntity(Plan.class, planMap.get("id"));
            Project projectParent = null;
            if(CommonUtil.isEmpty(p.getProjectId())){
                projectParent = (Project)sessionFacade.getEntity(Project.class, p.getProjectId());
            }

            if (planMap.get("ownerId") != null) {
                if (StringUtils.isNotEmpty(planMap.get("ownerId").trim())) {
                    p.setOwner(planMap.get("ownerId").trim());
                }
            }

            if (planMap.get("planLevel") != null) {
                p.setPlanLevel(planMap.get("planLevel").trim());

            }
            Date planStartTime2 = DateUtil.getDateFromString(planMap.get("planStartTime"), DateUtil.YYYY_MM_DD);
            p.setPlanStartTime(planStartTime2);

            Date planEndTime2 = DateUtil.getDateFromString(planMap.get("planEndTime"), DateUtil.YYYY_MM_DD);
            p.setPlanEndTime(planEndTime2);

            long workTime1 = 0;
            Date start = (Date)planStartTime2.clone();
            Date end = (Date)planEndTime2.clone();
            if (projectParent !=null && ProjectConstants.WORKDAY.equals(projectParent.getProjectTimeType())) {
                workTime1 = TimeUtil.getWorkDayNumber(start, end);
            }
            else if (projectParent !=null && ProjectConstants.COMPANYDAY.equals(projectParent.getProjectTimeType())) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("startDate",start);
                params.put("endDate",end);
                workTime1 = calendarService.getAllWorkingDay(appKey,params).size();
            }
            else {
                workTime1 = DateUtil.dayDiff(start, end) + 1;
            }
            p.setWorkTime(String.valueOf(workTime1));

            if (!CommonUtil.isEmpty(planMap.get("milestone"))) {
                if ("是".equals(planMap.get("milestone").trim())) {
                    p.setMilestone("true");
                }
                else if ("否".equals(planMap.get("milestone").trim())) {
                    p.setMilestone("false");
                }
                else if (StringUtils.isNotEmpty(planMap.get("milestone").trim())) {
                    p.setMilestone(planMap.get("milestone").trim());
                }
                else {
                    p.setMilestone("false");
                }
            }
            planService.saveOrUpdate(p);
        }
    }

    @Override
    public void planChangeBack(String oid,String userId) {
        if (oid == null || oid.equals("")) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String id = arrays[1];
        ApprovePlanForm form = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, id);
        if (!PlanConstants.PLAN_APPROVE_TYPE_FLOWTASKCHANGE.equals(form.getApproveType())) {
            ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
            approvePlanInfo.setFormId(id);
            List<ApprovePlanInfo> approve = planService.queryAssignList(approvePlanInfo, 1, 10,
                    false);
            TemporaryPlan temporaryPlan = new TemporaryPlan();
            for (ApprovePlanInfo a : approve) {
                temporaryPlan.setId(a.getPlanId());
                temporaryPlan.setFormId(id);
                List<TemporaryPlan> temporaryPlanListTemp = planChangeService.queryTemporaryPlanList(
                        temporaryPlan, 1, 10, false);
                String PlanId = temporaryPlanListTemp.get(0).getPlanId();
                Plan p = (Plan) sessionFacade.getEntity(Plan.class, PlanId);
                p.setIsChangeSingleBack("true");
                sessionFacade.saveOrUpdate(p);
                try {
                    TSUserDto userDto = userService.getUserByUserId(userId);
                    planChangeBackMsgNotice.getAllMessage(p.getId(), userDto);
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }

    @Override
    public List<TemporaryPlan> getTempPlanListForWork(TemporaryPlan temporaryPlan, List<Plan> planChangeList,String userId) {
        List<TemporaryPlan> tempPlanList = new ArrayList<TemporaryPlan>();
        for (Plan p : planChangeList) {
            TemporaryPlan t = new TemporaryPlan();
            initBusinessObject(t);
            Plan plan = (Plan) sessionFacade.getEntity(Plan.class, p.getId());
            t.setPlanId(p.getId());
            t.setRemark(temporaryPlan.getRemark());
            t.setChangeRemark(temporaryPlan.getChangeRemark());
            t.setChangeType(temporaryPlan.getChangeType());
            t.setActualStartTime(p.getActualStartTime());
            t.setActualEndTime(p.getActualEndTime());
            t.setAssigner(p.getAssigner());
            t.setAssignerInfo(p.getAssignerInfo());
            t.setAssignTime(p.getAssignTime());
            t.setAvaliable(p.getAvaliable());
            t.setBeforePlanId(p.getBeforePlanId());
            t.setBizCurrent(p.getBizCurrent());
            t.setBizId(p.getBizId());
            t.setBizVersion(p.getBizVersion());
            t.setCreator(p.getCreator());
            t.setFlowStatus(p.getFlowStatus());
            t.setImplementation(p.getImplementation());
            t.setMilestone(p.getMilestone());
            t.setOwner(p.getOwner());
            t.setOwnerDept(p.getOwnerDept());
            t.setOwnerInfo(p.getOwnerInfo());
            t.setParentPlanId(p.getParentPlanId());
            t.setPlanEndTime(p.getPlanEndTime());
            t.setPlanLevel(p.getPlanLevel());
            t.setPlanLevelInfo(p.getPlanLevelInfo());
            t.setPlanName(p.getPlanName());
            t.setPlanNumber(p.getPlanNumber());
            t.setPlanOrder(p.getPlanOrder());
            t.setPlanStartTime(p.getPlanStartTime());
//            t.setPolicy(plan.getPolicy());
            t.setPreposeIds(p.getPreposeIds());
            t.setPreposePlans(p.getPreposePlans());
            t.setProgressRate(p.getProgressRate());
            t.setProject(p.getProject());
            t.setProjectId(p.getProjectId());
            t.setProjectStatus(p.getProjectStatus());
            t.setRemark(p.getRemark());
            t.setSecurityLevel(p.getSecurityLevel());
            t.setStoreyNo(p.getStoreyNo());
            t.setWorkTime(p.getWorkTime());
//            t.setPolicy(plan.getPolicy());
            plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_CHANGE);
            plan.setLauncher(userId);
            sessionFacade.saveOrUpdate(plan);
            sessionFacade.saveOrUpdate(t);
            t.setId(t.getId());
            tempPlanList.add(t);
        }
        return tempPlanList;
    }

    @Override
    public void startPlanChangeMassForWork(List<TemporaryPlan> tempPlanList,List<TempPlanResourceLinkInfo> resourceLinkInfoList,
                                           TSUserDto actor, String leader, String deptLeader,
                                           String changeType, String userId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        // variables.put("approve", "third");

        ApprovePlanForm approvePlanForm = new ApprovePlanForm();
        approvePlanForm.setApproveType("变更");
        sessionFacade.saveOrUpdate(approvePlanForm);

        variables.put("user", actor.getUserName());
        FeignJson pi = new FeignJson();
        pi = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                BpmnConstants.BPMN_CHANGE_MASS_PLAN, approvePlanForm.getId(), variables);
        String procInstId = ""; // 流程实例ID
        if (pi.isSuccess()) {
            procInstId = pi.getObj() == null ? "" : pi.getObj().toString();
        }
        // 将procInstId存放到approvePlanForm中
        FeignJson taskIdFeignJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(procInstId);
        String taskId = "";

        if (taskIdFeignJson.isSuccess()) {
            taskId = taskIdFeignJson.getObj() == null ? "" : taskIdFeignJson.getObj().toString();
        }

        variables.put("cancelEventListener",
                "com.glaway.ids.project.plan.listener.CancelPlanChangeListener");
        List<String> lstAssignPeople = new ArrayList<String>();
        variables.put("assignPeoples", lstAssignPeople);
        variables.put("leader", leader);
        variables.put("deptLeader", deptLeader);
        variables.put("assigner", userId);
        variables.put("editUrl",
                PlanConstants.URL_CHANGEMASSPROCESS_EDIT + approvePlanForm.getId());
        variables.put("viewUrl",
                PlanConstants.URL_CHANGEMASSPROCESS_VIEW + approvePlanForm.getId());
        variables.put("oid", BpmnConstants.OID_APPROVEPLANFORM + approvePlanForm.getId());
        variables.put("userId",userId);
        String planChangeUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/planChangeExcutionRestController/notify.do";
        variables.put("planChange",planChangeUrl);
        String stopPlanChangeUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/stopPlanChangeExcutionRestController/notify.do";
        variables.put("stopPlanChange",stopPlanChangeUrl);
        // 执行流程
        workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, variables);

        FeignJson statusFeign = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = "";
        if (statusFeign.isSuccess()) {
            status = statusFeign.getObj() == null ? "" : statusFeign.getObj().toString();
        }
        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
        myStartedTask.setTaskNumber(approvePlanForm.getId());
        myStartedTask.setType(BpmnConstants.BPMN_CHANGE_PLAN);

        // 流程已发起列表中、增加对象名称的记录,流程任务名称规范化
        // 多个对象的批量审批任务，对象名称为null
        myStartedTask.setObjectName(null);
        myStartedTask.setTitle(ProcessUtil.getProcessTitle(null,
                BpmnConstants.BPMN_CHANGE_PLAN_DISPLAYNAME, procInstId));
        // myStartedTask.setTitle(ProcessUtil.getProcessTitle("计划变更", "启动审批", procInstId));

        myStartedTask.setCreater(actor.getUserName());
        myStartedTask.setCreaterFullname(actor.getRealName());
        myStartedTask.setStartTime(new Date());
        myStartedTask.setProcType(BpmnConstants.BPMN_CHANGE_PLAN);
        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

        // 将procInstId保存到approvePlanForm信息中
        approvePlanForm.setProcInstId(procInstId);
        sessionFacade.saveOrUpdate(approvePlanForm);

        if (changeType != null && "mass".equals(changeType)) {
            for (TemporaryPlan plan : tempPlanList) {
                // 计划审批相关信息表中存放计划与approvePlanForm关系
                ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                approvePlanInfo.setPlanId(plan.getId());
                approvePlanInfo.setFormId(approvePlanForm.getId());
                sessionFacade.saveOrUpdate(approvePlanInfo);
                plan.setFormId(approvePlanForm.getId());
                TemporaryPlan entity = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class, plan.getId());
                plan.setPolicy(entity.getPolicy());
                BeanUtils.copyProperties(plan,entity);
                sessionFacade.updateEntitie(entity);
            }
        }

        for (TemporaryPlan plan : tempPlanList) {
            DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
            deliverablesInfo.setUseObjectId(plan.getPlanId());
            List<DeliverablesInfo> deliverablesList = deliverablesInfoService.queryDeliverableList(
                    deliverablesInfo, 1, 10, false);
            for (DeliverablesInfo deliverables : deliverablesList) {
                TempPlanDeliverablesInfo tempDeliverables = new TempPlanDeliverablesInfo();
                tempDeliverables.setAvaliable(deliverables.getAvaliable());
                tempDeliverables.setBizCurrent(deliverables.getBizCurrent());
                tempDeliverables.setBizId(deliverables.getBizId());
                tempDeliverables.setDeliverLinkId(plan.getPlanId());
                tempDeliverables.setBizVersion(deliverables.getBizVersion());
                tempDeliverables.setDeliverablesId(deliverables.getId());
                tempDeliverables.setDocument(deliverables.getDocument());
                tempDeliverables.setName(deliverables.getName());
                tempDeliverables.setPolicy(deliverables.getPolicy());
                tempDeliverables.setSecurityLevel(deliverables.getSecurityLevel());
                tempDeliverables.setUseObjectId(approvePlanForm.getId());
                tempDeliverables.setFormId(approvePlanForm.getId());
                tempDeliverables.setUseObjectType(deliverables.getUseObjectType());
                sessionFacade.saveOrUpdate(tempDeliverables);
            }
        }

        for (TempPlanResourceLinkInfo r : resourceLinkInfoList) {
            if (CommonUtil.isEmpty(r.getBizCurrent())) {
                resourceChangeService.initBusinessObject(r);
            }
            r.setId(null);
            r.setUseObjectId(approvePlanForm.getId());
            r.setFormId(approvePlanForm.getId());
            sessionFacade.saveOrUpdate(r);
        }
    }


    @Override
    public void saveBasicLineForWork(String ids, BasicLine basicLine, String projectId) {
        List<BasicLinePlan> basicLinePlanListTemp = new ArrayList<BasicLinePlan>();
        PlanDto condition = new PlanDto();
        condition.setProjectId(projectId);
        List<PlanDto> list1 = planService.queryPlanListForTreegrid(condition);
        List<Plan> planList = CodeUtils.JsonListToList(list1, Plan.class);
        Map<String, Plan> plansMap = new HashMap<String, Plan>();
        for (Plan plan : planList) {
            plansMap.put(plan.getId(), plan);
        }
        // 将计划的输出按照key为计划ID,value为计划所对应的输出list 组成map
        List<DeliverablesInfo> deliverablesAllList = deliverablesInfoService.getDeliverablesByProject(projectId);
        Map<String, List<DeliverablesInfo>> deliverablesMap = new HashMap<String, List<DeliverablesInfo>>();
        for (DeliverablesInfo deliverablesInfo : deliverablesAllList) {
            List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
            if (!CommonUtil.isEmpty(deliverablesMap.get(deliverablesInfo.getUseObjectId()))) {
                list = deliverablesMap.get(deliverablesInfo.getUseObjectId());
            }
            list.add(deliverablesInfo);
            deliverablesMap.put(deliverablesInfo.getUseObjectId(), list);
        }
        for (String id : ids.split(",")) {
            BasicLinePlan basicLinePlan = new BasicLinePlan();
            Plan plan = plansMap.get(id);
            basicLinePlan.setPlanId(plan.getId());
            basicLinePlan.setBizCurrent(plan.getBizCurrent());
            basicLinePlan.setAssigner(plan.getAssigner());
            basicLinePlan.setAssignerInfo(plan.getAssignerInfo());
            basicLinePlan.setAssignTime(plan.getAssignTime());
            basicLinePlan.setBasicLine(basicLine);
            basicLinePlan.setBasicLineId(basicLine.getId());
            basicLinePlan.setPlanType(plan.getPlanType());
            basicLinePlan.setCreateBy(plan.getCreateBy());
            basicLinePlan.setCreateName(plan.getCreateName());
            basicLinePlan.setCreateTime(plan.getCreateTime());
            basicLinePlan.setCreator(plan.getCreateBy());
            basicLinePlan.setCreatorInfo(plan.getCreator());
            List<DeliverablesInfo> deliverablesList = deliverablesMap.get(plan.getId());
            if (!CommonUtil.isEmpty(deliverablesList)) {
                String deliverables = "";
                for (DeliverablesInfo deliverablesInfo : deliverablesList) {
                    if (StringUtils.isNotEmpty(deliverables)) {
                        deliverables = deliverables + "," + deliverablesInfo.getName();
                    }
                    else {
                        deliverables = deliverablesInfo.getName();
                    }
                }
                basicLinePlan.setDeliverables(deliverables);
            }

            basicLinePlan.setImplementation(plan.getImplementation());
            basicLinePlan.setMilestone(plan.getMilestone());
            basicLinePlan.setMilestoneName(plan.getMilestoneName());
            basicLinePlan.setOwner(plan.getOwner());
            basicLinePlan.setOwnerDept(plan.getOwnerDept());
            basicLinePlan.setOwnerInfo(plan.getOwnerInfo());
            basicLinePlan.setParentPlanId(plan.getParentPlanId());
            basicLinePlan.setParentStorey(plan.getParentStorey());
            basicLinePlan.setPlanEndTime(plan.getPlanEndTime());
            basicLinePlan.setPlanLevel(plan.getPlanLevel());
            basicLinePlan.setPlanLevelInfo(plan.getPlanLevelInfo());
            basicLinePlan.setPlanName(plan.getPlanName());
            basicLinePlan.setPlanStartTime(plan.getPlanStartTime());
            basicLinePlan.setPreposeIds(plan.getPreposeIds());
            basicLinePlan.setProgressRate(plan.getProgressRate());
            basicLinePlan.setProject(plan.getProject());
            basicLinePlan.setProjectId(plan.getProjectId());
            basicLinePlan.setRemark(plan.getRemark());
            basicLinePlan.setStatus(plan.getStatus());
            basicLinePlan.setStoreyNo(plan.getStoreyNo());
            basicLinePlan.setUpdateBy(plan.getUpdateBy());
            basicLinePlan.setUpdateName(plan.getUpdateName());
            basicLinePlan.setUpdateTime(plan.getUpdateTime());
            basicLinePlan.setWorkTime(plan.getWorkTime());
            sessionFacade.saveOrUpdate(basicLinePlan);
            basicLinePlanListTemp.add(basicLinePlan);
        }

        String planIds = "";
        for (BasicLinePlan s : basicLinePlanListTemp) {
            planIds = planIds + s.getPlanId() + ",";
        }
        if (planIds.endsWith(",")) {
            planIds = planIds.substring(0, planIds.length() - 1);
        }

        for (BasicLinePlan b : basicLinePlanListTemp) {
            if (StringUtils.isNotEmpty(b.getParentPlanId())) {
                Plan p = plansMap.get(b.getPlanId());
                if (StringUtils.isNotEmpty(p.getParentPlanId())) {
                    if (planIds.contains(p.getParentPlanId())) {
                        for (BasicLinePlan basicLinePlan : basicLinePlanListTemp) {
                            if (p.getParentPlanId().equals(basicLinePlan.getPlanId())) {
                                b.setParentPlanId(basicLinePlan.getId());
                            }
                        }
                    }
                    else {
                        b.setParentPlanId(null);
                    }
                }
                sessionFacade.saveOrUpdate(b);
            }
        }
    }

    @Override
    public void copyBasicLineForWork(BasicLine basicLine, String ids, String projectId, String basicLineIdForCopy) {
        initBusinessObject(basicLine);
        sessionFacade.saveOrUpdate(basicLine);
        List<BasicLinePlan> basicLinePlanListTemp = new ArrayList<BasicLinePlan>();
        List<BasicLinePlan> basicLinePlanListTemp2 = new ArrayList<BasicLinePlan>();
        BasicLinePlan condition = new BasicLinePlan();
        condition.setBasicLineId(basicLineIdForCopy);
        List<BasicLinePlan> basicLineCopyList = basicLineServiceI.queryBasicLinePlanList(condition,
                1, 10, false);
        Map<String, BasicLinePlan> basicLinePlansMap = new HashMap<String, BasicLinePlan>();
        for (BasicLinePlan plan : basicLineCopyList) {
            basicLinePlansMap.put(plan.getId(), plan);
        }
        for (String id : ids.split(",")) {
            BasicLinePlan basicLinePlan = basicLinePlansMap.get(id);
            basicLinePlanListTemp.add(basicLinePlan);
        }

        PlanDto planCondition = new PlanDto();
        planCondition.setProjectId(projectId);
        List<PlanDto> list1 = planService.queryPlanListForTreegrid(planCondition);
        List<Plan> planList = CodeUtils.JsonListToList(list1, Plan.class);
        Map<String, Plan> plansMap = new HashMap<String, Plan>();
        for (Plan plan : planList) {
            plansMap.put(plan.getId(), plan);
        }
        // 将计划的输出按照key为计划ID,value为计划所对应的输出list 组成map
        List<DeliverablesInfo> deliverablesAllList = deliverablesInfoService.getDeliverablesByProject(projectId);
        Map<String, List<DeliverablesInfo>> deliverablesMap = new HashMap<String, List<DeliverablesInfo>>();
        for (DeliverablesInfo deliverablesInfo : deliverablesAllList) {
            List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
            if (!CommonUtil.isEmpty(deliverablesMap.get(deliverablesInfo.getUseObjectId()))) {
                list = deliverablesMap.get(deliverablesInfo.getUseObjectId());
            }
            list.add(deliverablesInfo);
            deliverablesMap.put(deliverablesInfo.getUseObjectId(), list);
        }

        for (BasicLinePlan basicLineTemp : basicLinePlanListTemp) {

            BasicLinePlan basicLinePlan = new BasicLinePlan();

            Plan b = plansMap.get(basicLineTemp.getPlanId());
            basicLinePlan.setPlanId(b.getId());

            basicLinePlan.setBizCurrent(b.getBizCurrent());
            basicLinePlan.setAssigner(b.getAssigner());
            basicLinePlan.setAssignerInfo(b.getAssignerInfo());
            basicLinePlan.setAssignTime(b.getAssignTime());
            basicLinePlan.setBasicLineId(basicLine.getId());
            basicLinePlan.setCreateBy(b.getCreateBy());
            basicLinePlan.setPlanType(b.getPlanType());
            basicLinePlan.setCreateName(b.getCreateName());
            basicLinePlan.setCreateTime(b.getCreateTime());
            basicLinePlan.setCreator(b.getCreateBy());

            List<DeliverablesInfo> deliverablesList = deliverablesMap.get(b.getId());
            if (!CommonUtil.isEmpty(deliverablesList)) {
                String deliverables = "";
                for (DeliverablesInfo deliverablesInfo : deliverablesList) {
                    if (StringUtils.isNotEmpty(deliverables)) {
                        deliverables = deliverables + "," + deliverablesInfo.getName();
                    }
                    else {
                        deliverables = deliverablesInfo.getName();
                    }
                }
                basicLinePlan.setDeliverables(deliverables);
            }

            basicLinePlan.setImplementation(b.getImplementation());
            basicLinePlan.setMilestone(b.getMilestone());
            basicLinePlan.setMilestoneName(b.getMilestoneName());
            basicLinePlan.setOwner(b.getOwner());
            basicLinePlan.setOwnerDept(b.getOwnerDept());
            basicLinePlan.setOwnerInfo(b.getOwnerInfo());
            basicLinePlan.setPlanEndTime(b.getPlanEndTime());
            basicLinePlan.setPlanLevel(b.getPlanLevel());
            basicLinePlan.setPlanLevelInfo(b.getPlanLevelInfo());
            basicLinePlan.setPlanName(b.getPlanName());
            basicLinePlan.setPlanStartTime(b.getPlanStartTime());
            basicLinePlan.setPreposeIds(b.getPreposeIds());
            basicLinePlan.setProgressRate(b.getProgressRate());
            basicLinePlan.setProject(b.getProject());
            basicLinePlan.setProjectId(b.getProjectId());
            basicLinePlan.setRemark(b.getRemark());
            basicLinePlan.setStatus(b.getStatus());
            basicLinePlan.setParentPlanId(b.getParentPlanId());
            basicLinePlan.setParentStorey(b.getParentStorey());
            basicLinePlan.setStoreyNo(b.getStoreyNo());
            basicLinePlan.setUpdateBy(b.getUpdateBy());
            basicLinePlan.setUpdateName(b.getUpdateName());
            basicLinePlan.setUpdateTime(b.getUpdateTime());
            basicLinePlan.setWorkTime(b.getWorkTime());
            sessionFacade.saveOrUpdate(basicLinePlan);
            basicLinePlanListTemp2.add(basicLinePlan);
        }

        String planIds = "";
        for (BasicLinePlan s : basicLinePlanListTemp2) {
            planIds = planIds + s.getPlanId() + ",";
        }
        if (planIds.endsWith(",")) {
            planIds = planIds.substring(0, planIds.length() - 1);
        }

        for (BasicLinePlan b : basicLinePlanListTemp2) {
            if (StringUtils.isNotEmpty(b.getParentPlanId())) {
                Plan p = (Plan) sessionFacade.getEntity(Plan.class, b.getPlanId());
                if (StringUtils.isNotEmpty(p.getParentPlanId())) {
                    if (planIds.contains(p.getParentPlanId())) {
                        for (BasicLinePlan basicLinePlan : basicLinePlanListTemp2) {
                            if (p.getParentPlanId().equals(basicLinePlan.getPlanId())) {
                                b.setParentPlanId(basicLinePlan.getId());
                            }
                        }
                    }
                    else {
                        b.setParentPlanId(null);
                    }
                }
                sessionFacade.saveOrUpdate(b);
            }
        }
    }

    @Override
    public void startBasicLineForWork(TSUserDto actor, String leader, String deptLeader, String basicLineIdForSubmit,String userId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        BasicLine basicLine = (BasicLine) sessionFacade.getEntity(BasicLine.class, basicLineIdForSubmit);
        if (StringUtils.isNotEmpty(basicLine.getProcInstId())) {

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("user", actor.getUserName());
            variables.put("cancelEventListener",
                    "com.glaway.ids.project.plan.listener.CancelBasicLineListener");
            variables.put("leader", leader);
            variables.put("deptLeader", deptLeader);
            variables.put("assigner", userDto.getUserName());
            variables.put("editUrl", PlanConstants.URL_BASICLINE_EDIT + basicLineIdForSubmit);
            variables.put("viewUrl", PlanConstants.URL_BASICLINE_VIEW + basicLineIdForSubmit);
            variables.put("oid", BpmnConstants.OID_BASICLINE + basicLineIdForSubmit);
            variables.put("userId",userId);
            String basicLineUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/basicLineExcutionRestController/notify.do";
            variables.put("basicLineUrl",basicLineUrl);
            String basicLineBackUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/basicLineBackExcutionLRestController/notify.do";
            variables.put("basicLineBackUrl",basicLineBackUrl);

            String procInstId = basicLine.getProcInstId();
            String taskId = "";
            if (procInstId != null && !"".equals(procInstId)) {
                FeignJson taskIdFeignJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                        procInstId);

                if (taskIdFeignJson.isSuccess()) {
                    taskId = taskIdFeignJson.getObj() == null ? "" : taskIdFeignJson.getObj().toString();
                }
            }
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                    basicLineIdForSubmit);
            myStartedTask.setEndTime(new Date());
            myStartedTask.setStatus("室领导审批");
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);
            workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
            basicLine.setLauncher(actor.getId());
            basicLine.setLaunchTime(new Date());
            basicLine.setBizCurrent(PlanConstants.BASICLINE_FLOWSTATUS_VERFYING);
            basicLine.setProcInstId(procInstId);
            sessionFacade.saveOrUpdate(basicLine);
        }
        else {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("user", actor.getUserName());
            FeignJson pi = new FeignJson();
            pi = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                    BpmnConstants.BPMN_START_BASICLINE, basicLineIdForSubmit, variables);
            String procInstId = ""; // 流程实例ID
            if (pi.isSuccess()) {
                procInstId = pi.getObj() == null ? "" : pi.getObj().toString();
            }
            FeignJson taskIdFeignJson  = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                    procInstId);
            String taskId = "";

            if (taskIdFeignJson.isSuccess()) {
                taskId = taskIdFeignJson.getObj() == null ? "" : taskIdFeignJson.getObj().toString();
            }

            variables.put("cancelEventListener",
                    "com.glaway.ids.project.plan.listener.CancelBasicLineListener");
            variables.put("leader", leader);
            variables.put("deptLeader", deptLeader);
            variables.put("assigner", userDto.getUserName());
            variables.put("editUrl", PlanConstants.URL_BASICLINE_EDIT + basicLineIdForSubmit);
            variables.put("viewUrl", PlanConstants.URL_BASICLINE_VIEW + basicLineIdForSubmit);
            variables.put("oid", BpmnConstants.OID_BASICLINE + basicLineIdForSubmit);
            variables.put("userId",userId);
            String basicLineUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/basicLineExcutionRestController/notify.do";
            variables.put("basicLineUrl",basicLineUrl);
            String basicLineBackUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/basicLineBackExcutionLRestController/notify.do";
            variables.put("basicLineBackUrl",basicLineBackUrl);
            // 执行流程
            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            FeignJson statusFeign = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    procInstId);
//        String status = ProcessUtil.getProcessStatus(nextTasks);
            String status = "";
            if (statusFeign.isSuccess()) {
                status = statusFeign.getObj() == null ? "" : statusFeign.getObj().toString();
            }

            MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
            myStartedTask.setTaskNumber(basicLineIdForSubmit);
            myStartedTask.setType(BpmnConstants.BPMN_START_BASICLINE);

            // 流程已发起列表中、增加对象名称的记录,流程任务名称规范化
            myStartedTask.setObjectName(basicLine.getBasicLineName());
            myStartedTask.setTitle(ProcessUtil.getProcessTitle(basicLine.getBasicLineName(),
                    BpmnConstants.BPMN_START_BASICLINE_DISPLAYNAME, procInstId));
            // myStartedTask.setTitle(ProcessUtil.getProcessTitle("基线提交", "启动审批", procInstId));

            myStartedTask.setCreater(actor.getUserName());
            myStartedTask.setCreaterFullname(actor.getRealName());
            myStartedTask.setStartTime(new Date());
            myStartedTask.setProcType(BpmnConstants.BPMN_START_BASICLINE);
            myStartedTask.setProcInstId(procInstId);
            myStartedTask.setStatus(status);
            workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

            basicLine.setLauncher(actor.getId());
            basicLine.setLaunchTime(new Date());
            basicLine.setBizCurrent(PlanConstants.BASICLINE_FLOWSTATUS_VERFYING);
            basicLine.setProcInstId(procInstId);
            sessionFacade.saveOrUpdate(basicLine);
        }
    }

    @Override
    public void startBasicLineFlowForWork(String taskId, String basicLineIdForFlow) {
        Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(taskId);
      //  variables.put("user", UserUtil.getInstance().getUser().getUserName());
        // 执行流程
        TaskDto task = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
        String procInstId = task.getProcessInstanceId();
        workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, variables);

        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                basicLineIdForFlow);
        myStartedTask.setEndTime(new Date());
        myStartedTask.setStatus("室领导审批");
        workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
    }

    @Override
    public void doAddInheritlistForTemplate(String names, String planIdForInherit, String useObjectType,TSUserDto userDto,String orgId) {
        for (String name : names.split(",")) {
            DeliverablesInfo deliverablesInfoTemp = new DeliverablesInfo();
            deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
            CommonInitUtil.initGLObjectForCreate(deliverablesInfoTemp,userDto,orgId);
            deliverablesInfoTemp.setId(null);
            deliverablesInfoTemp.setName(name);
            deliverablesInfoTemp.setUseObjectId(planIdForInherit);
            deliverablesInfoTemp.setUseObjectType(useObjectType);
            sessionFacade.save(deliverablesInfoTemp);
        }
    }

    @Override
    public String getChangeTaskIdByFormId(String planTemplateId) {
        String sql = "select r.id_ as taskId from ACT_RU_TASK r where r.proc_inst_id_ in ( select s.procinstid from act_fd_started_task s  "
                + "where s.tasknumber=\'"
                + planTemplateId
                + "\' and s.type='planChangeProcess')";
        String taskId = "";
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(sql);
        for (Map<String, Object> map : objArrayList) {
            if (map != null) {
                taskId = (String)map.get("taskId");
            }
        }
        return taskId;
    }

    @Override
    public List<ResourceLinkInfo> getResourceLinkInfosByProject(String projectId) {
        StringBuilder hqlBuffer = new StringBuilder("");
        hqlBuffer.append(" SELECT D.ID ID, ");
        hqlBuffer.append(" D.RESOURCEID RESOURCEID, ");
        hqlBuffer.append(" R.NO RESOURCENO, ");
        hqlBuffer.append(" R.NAME RESOURCENAME, ");
        hqlBuffer.append(" D.STARTTIME STARTTIME, ");
        hqlBuffer.append(" D.ENDTIME ENDTIME, ");
        hqlBuffer.append(" D.USERATE USERATE, ");
        hqlBuffer.append(" D.USEOBJECTID USEOBJECTID, ");
        hqlBuffer.append(" D.USEOBJECTTYPE USEOBJECTTYPE ");
        hqlBuffer.append(" FROM CM_RESOURCE_LINK_INFO D ");
        hqlBuffer.append(" JOIN PM_PLAN P ON P.ID = D.USEOBJECTID ");
        hqlBuffer.append(" JOIN CM_RESOURCE R ON R.ID = D.RESOURCEID ");
        hqlBuffer.append(" WHERE D.USEOBJECTTYPE = 'PLAN' ");
        hqlBuffer.append(" AND D.AVALIABLE = '1' ");
        hqlBuffer.append(" AND P.PROJECTID = '" + projectId + "' ");
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hqlBuffer.toString());
        List<ResourceLinkInfo> list = new ArrayList<ResourceLinkInfo>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                ResourceLinkInfo r = new ResourceLinkInfo();
                r.setId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                r.setResourceId(StringUtils.isNotEmpty((String)map.get("RESOURCEID")) ? (String)map.get("RESOURCEID") : "");
                r.setUseObjectId(StringUtils.isNotEmpty((String)map.get("USEOBJECTID")) ? (String)map.get("USEOBJECTID") : "");
                r.setUseObjectType(StringUtils.isNotEmpty((String)map.get("USEOBJECTTYPE")) ? (String)map.get("USEOBJECTTYPE") : "");
                r.setStartTime((Date)map.get("STARTTIME"));
                r.setEndTime((Date)map.get("ENDTIME"));
                r.setUseRate(StringUtils.isNotEmpty((String)map.get("USERATE")) ? (String)map.get("USERATE") : "");
                Resource resource = new Resource();
                resource.setId(StringUtils.isNotEmpty((String)map.get("RESOURCEID")) ? (String)map.get("RESOURCEID") : "");
                resource.setNo(StringUtils.isNotEmpty((String)map.get("RESOURCENO")) ? (String)map.get("RESOURCENO") : "");
                resource.setName(StringUtils.isNotEmpty((String)map.get("RESOURCENAME")) ? (String)map.get("RESOURCENAME") : "");
                if (StringUtils.isNotEmpty(resource.getNo())
                        && StringUtils.isNotEmpty(resource.getName())) {
                    r.setResourceInfo(resource);
                }
                if (StringUtils.isNotEmpty(r.getId()) && StringUtils.isNotEmpty(r.getResourceId())
                        && StringUtils.isNotEmpty(r.getUseObjectId())
                        && StringUtils.isNotEmpty(r.getResourceInfo().getName())) {
                    list.add(r);
                }
            }
        }
        return list;
    }

    @Override
    public void startPlanChangeFlowForWork(String formId, String taskId, String temporaryId, List<TempPlanResourceLinkInfo> resourceLinkInfoList, List<TempPlanDeliverablesInfo> deliverablesInfoList, List<TempPlanInputs> inputList, String leader, String deptLeader, String userId){
        Map<String, Object> variables = new HashMap<String, Object>();
        TSUserDto userDto = userService.getUserByUserId(userId);
//        variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(taskId);
        variables.put("user", userDto.getUserName());

        // variables.put("approve", "third");

        // 执行流程
        TaskDto task = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
        variables.put("leader", leader);
        variables.put("deptLeader", deptLeader);
        variables.put("userId", userId);
        String procInstId = task.getProcessInstanceId();
        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

        FeignJson statusFeign = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = "";
        if (statusFeign.isSuccess()) {
            status = statusFeign.getObj() == null ? "" : statusFeign.getObj().toString();
        }
        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                formId);
        myStartedTask.setEndTime(new Date());
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);

        // String temporaryId = (String)req.getSession().getAttribute("temporaryId");
        TemporaryPlan temporaryPlan = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class, temporaryId);
        /*
         * ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
         * approvePlanInfo.setPlanId(temporaryPlan.getId());
         * approvePlanInfo.setFormId(formId);
         * planService.saveOrUpdate(approvePlanInfo);
         */
        temporaryPlan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_CHANGE);
        sessionFacade.saveOrUpdate(temporaryPlan);
        Plan plan = (Plan) sessionFacade.getEntity(Plan.class, temporaryPlan.getPlanId());
        plan.setIsChangeSingleBack("false");
        sessionFacade.saveOrUpdate(plan);

        planService.deleteDeliverablesAndResourceByTempForm(formId);

        for (TempPlanResourceLinkInfo r : resourceLinkInfoList) {
            initBusinessObject(r);
            r.setId(null);
            r.setUseObjectId(formId);
            sessionFacade.save(r);
        }
        for (TempPlanDeliverablesInfo d : deliverablesInfoList) {
            deliverablesChangeService.initBusinessObject(d);
            d.setId(null);
            d.setUseObjectId(formId);
            d.setFormId(formId);
            d.setUseObjectType("PLAN");
            d.setDeliverLinkId(temporaryPlan.getPlanId());
            sessionFacade.save(d);
        }

        for(TempPlanInputs in : inputList){
            in.setId(null);
            in.setUseObjectId(formId);
            in.setInputId(temporaryPlan.getPlanId());
            in.setFormId(formId);
            sessionFacade.save(in);
        }
    }

    @Override
    public void doAddInheritlist(String names, String planIdForInherit, TSUserDto curUserDto, String orgId) {
        for (String name : names.split(",")) {
            DeliverablesInfo deliverablesInfoTemp = new DeliverablesInfo();
            deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
            CommonInitUtil.initGLObjectForCreate(deliverablesInfoTemp,curUserDto,orgId);
            deliverablesInfoTemp.setId(null);
            deliverablesInfoTemp.setName(name);
            deliverablesInfoTemp.setUseObjectId(planIdForInherit);
            deliverablesInfoTemp.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            sessionFacade.save(deliverablesInfoTemp);
        }
    }

    @Override
    public Date dateChange(Date date, int day, String beforOrAfter) {
        long dateTime = date.getTime();
        long dayTime = day * 24 * 60 * 60 * 1000;
        long newDateTime;
        if ("after".equals(beforOrAfter)) {
            newDateTime = dateTime + dayTime;
        }
        else {
            newDateTime = dateTime - dayTime;
        }
        return new Date(newDateTime);
    }

    @Override
    public Plan getPlanWarnAndOver(Plan plan) {
        Project p = new Project();
        if (plan.getProject() == null) {
            p = (Project)sessionFacade.getEntity(Project.class, plan.getProjectId());
        }
        else {
            p = plan.getProject();
        }
        plan.setProjectName(p.getName() + "-" + p.getProjectNumber());
        return plan;
    }
}