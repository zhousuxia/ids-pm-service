package com.glaway.ids.project.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.dto.TemporaryPlanDto;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.DeliverablesChangeServiceI;
import com.glaway.ids.project.plan.service.PlanChangeServiceI;
import com.glaway.ids.project.plan.service.ResourceChangeServiceI;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.ProcessUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by LHR on 2019/8/5.
 */
@Service("planChangeService")
@Transactional(propagation = Propagation.REQUIRED)
public class PlanChangeServiceImpl extends BusinessObjectServiceImpl<TemporaryPlan> implements PlanChangeServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private WorkFlowFacade workFlowFacade;

    @Autowired
    private ResourceChangeServiceI resourceChangeService;

    @Autowired
    private DeliverablesChangeServiceI deliverablesChangeService;

    @Autowired
    private FeignUserService userService;

    @Autowired
    private Environment env;

    @Override
    public List<TemporaryPlan> queryTemporaryPlanList(TemporaryPlan temporaryPlan, int page, int rows, boolean isPage) {
        String hql = createHql(temporaryPlan);
        if (isPage) {
            return pageList(hql, (page - 1) * rows, rows);
        }
        return findByQueryString(hql);
    }

    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param
     * @return
     * @see
     */
    private String createHql(TemporaryPlan temporaryPlan) {
        String hql = "from TemporaryPlan l where 1 = 1";
        // 计划ID
        if (StringUtils.isNotEmpty(temporaryPlan.getId())) {
            hql = hql + " and l.id = '" + temporaryPlan.getId() + "'";
        }
        if (!StringUtils.isEmpty(temporaryPlan.getFormId())) {
            hql = hql + " and l.formId = '" + temporaryPlan.getFormId() + "'";
        }
        // 所属项目
        if (StringUtils.isNotEmpty(temporaryPlan.getProjectId())) {
            hql = hql + " and l.projectId = '" + temporaryPlan.getProjectId() + "'";
        }
        // 父计划
        if (StringUtils.isNotEmpty(temporaryPlan.getPlanId())) {
            hql = hql + " and l.planId = '" + temporaryPlan.getPlanId() + "'";
        }
        hql = hql + " order by createTime desc";
        return hql;
    }

    @Override
    public <T> List<T> pageList(String hql, int firstResult, int maxResult) {
        return sessionFacade.pageList(hql, firstResult, maxResult);
    }

    @Override
    public <T> List<T> findByQueryString(String query) {
        return sessionFacade.findByQueryString(query);
    }

    @Override
    public String initPlanChange(TemporaryPlanDto temporaryPlan) {
        TemporaryPlan plan = JSON.parseObject(JSON.toJSONString(temporaryPlan),TemporaryPlan.class);
        TemporaryPlan p = initBusinessObject(plan);
        String json = JSON.toJSONString(p);
        return json;
    }

    @Override
    public String saveOrUpdateTemporaryPlan(TemporaryPlan temporaryPlan,String curUserId,String orgId) {
        TemporaryPlan plan = JSON.parseObject(JSON.toJSONString(temporaryPlan),TemporaryPlan.class);
        TSUserDto userDto = userService.getUserByUserId(curUserId);
        if (temporaryPlan.getId().startsWith(PlanConstants.PLAN_CREATE_UUID)) {

            // plan.setId(null);
            initBusinessObject(plan);
            CommonInitUtil.initGLObjectForCreate(plan,userDto,orgId);
            sessionFacade.saveOrUpdate(plan);
        }
        else {
            TemporaryPlan t = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class,
                    temporaryPlan.getId());
            temporaryPlan.setPlanNumber(t.getPlanNumber());
            try {
                BeanUtil.copyBeanNotNull2Bean(temporaryPlan, t);
            } catch (Exception e) {
                e.printStackTrace();
            }
            CommonInitUtil.initGLObjectForUpdate(temporaryPlan,userDto,orgId);
            sessionFacade.saveOrUpdate(t);

        }
        return temporaryPlan.getId();
    }

    @Override
    public String getTemporaryPlanId(TemporaryPlan temporaryPlan, String uploadSuccessPath, String uploadSuccessFileName,String userId,String orgId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        initBusinessObject(temporaryPlan);
        CommonInitUtil.initGLObjectForCreate(temporaryPlan,userDto,orgId);
        temporaryPlan.setChangeInfoDocPath(uploadSuccessPath);
        temporaryPlan.setChangeInfoDocName(uploadSuccessFileName);
        Plan p = (Plan) sessionFacade.getEntity(Plan.class, temporaryPlan.getPlanId());
        p.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_CHANGE);
        p.setLauncher(userId);
        sessionFacade.saveOrUpdate(temporaryPlan);
        sessionFacade.saveOrUpdate(p);
        return temporaryPlan.getId();
    }

    @Override
    public void startPlanChangeForWork(List<Plan> planList, List<TempPlanResourceLinkInfo> resourceLinkInfoList, List<TempPlanInputs> inputList, List<TempPlanDeliverablesInfo> deliverablesInfoList,
                                       TSUserDto actor, String leader, String deptLeader, String changeType, String temporaryId,String userId,String typeIds) {
        Map<String, Object> variables = new HashMap<String, Object>();

        // variables.put("approve", "third");

        ApprovePlanForm approvePlanForm = new ApprovePlanForm();
        approvePlanForm.setApproveType("变更");
        sessionFacade.saveOrUpdate(approvePlanForm);

        variables.put("user", actor.getUserName());
        FeignJson pi = new FeignJson();
        pi = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                BpmnConstants.BPMN_CHANGE_PLAN, approvePlanForm.getId(), variables);
        String procInstId = ""; // 流程实例ID
        if (pi.isSuccess()) {
            procInstId = pi.getObj() == null ? "" : pi.getObj().toString();
        }
//        String procInstId = pi.getProcessInstanceId(); // 流程实例ID
        // 将procInstId存放到approvePlanForm中
        FeignJson taskIdFeignJson  = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                procInstId);
        String taskId = "";

        if (taskIdFeignJson.isSuccess()) {
            taskId = taskIdFeignJson.getObj() == null ? "" : taskIdFeignJson.getObj().toString();
        }
//        String taskId = tasks.get(0).getId();

        variables.put("cancelEventListener",
                "com.glaway.ids.project.plan.listener.CancelPlanChangeListener");
        List<String> lstAssignPeople = new ArrayList<String>();
        variables.put("assignPeoples", lstAssignPeople);
        variables.put("leader", leader);
        variables.put("deptLeader", deptLeader);
        variables.put("assigner", userId);
        variables.put("editUrl", PlanConstants.URL_CHANGEPROCESS_EDIT + approvePlanForm.getId()+"&typeIds="+typeIds);
        variables.put("viewUrl", PlanConstants.URL_CHANGEROCESS_VIEW + approvePlanForm.getId());
        variables.put("oid", BpmnConstants.OID_APPROVEPLANFORM + approvePlanForm.getId());
        variables.put("userId",userId);
        String planChangeUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/planChangeOneExcutionRestController/notify.do";
        variables.put("planChange",planChangeUrl);
        String planChangeBackUrl = "http://" + env.getProperty("server.address") + ":" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/feign/planChangeBackExcutionRestController/notify.do";
        variables.put("planChangeBackUrl",planChangeBackUrl);
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
        myStartedTask.setTaskNumber(approvePlanForm.getId());
        myStartedTask.setType(BpmnConstants.BPMN_CHANGE_PLAN);

        // 流程已发起列表中、增加对象名称的记录,流程任务名称规范化
        // 多个对象的批量审批任务，对象名称为null
        myStartedTask.setObjectName(null);
        myStartedTask.setTitle(ProcessUtil.getProcessTitle("",
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

        if (changeType != null && "single".equals(changeType)) {
            TemporaryPlan temporaryPlan = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class, temporaryId);
            ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
            approvePlanInfo.setPlanId(temporaryPlan.getId());
            approvePlanInfo.setFormId(approvePlanForm.getId());
            sessionFacade.saveOrUpdate(approvePlanInfo);
            temporaryPlan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_CHANGE);
            temporaryPlan.setLauncher(actor.getId());
            temporaryPlan.setLaunchTime(new Date());
            temporaryPlan.setFormId(approvePlanForm.getId());
            sessionFacade.saveOrUpdate(temporaryPlan);
            Plan plan = (Plan) sessionFacade.getEntity(Plan.class, temporaryPlan.getPlanId());
            plan.setFormId(approvePlanForm.getId());
            sessionFacade.saveOrUpdate(plan);
            for (TempPlanResourceLinkInfo r : resourceLinkInfoList) {
                initBusinessObject(r);
                r.setId(null);
                r.setResourceLinkId(temporaryPlan.getPlanId());
                r.setUseObjectId(approvePlanForm.getId());
                r.setFormId(approvePlanForm.getId());
                sessionFacade.saveOrUpdate(r);
            }
            for (TempPlanDeliverablesInfo d : deliverablesInfoList) {
                initBusinessObject(d);
                d.setId(null);
                d.setUseObjectId(approvePlanForm.getId());
                d.setDeliverLinkId(temporaryPlan.getPlanId());
                d.setUseObjectType("PLAN");
                d.setFormId(approvePlanForm.getId());
                sessionFacade.saveOrUpdate(d);
            }

            for (TempPlanInputs d : inputList) {
                d.setId(null);
                d.setUseObjectId(approvePlanForm.getId());
                d.setInputId(temporaryPlan.getPlanId());
                d.setFormId(approvePlanForm.getId());
                sessionFacade.saveOrUpdate(d);
            }
        }
        else if (changeType != null && "mass".equals(changeType)) {
            for (int i = 0; i < planList.size(); i++ ) {
                TemporaryPlan plan = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class,
                        planList.get(i).getId());
                if (plan != null) {
                    // 计划审批相关信息表中存放计划与approvePlanForm关系
                    ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                    approvePlanInfo.setPlanId(plan.getId());
                    approvePlanInfo.setFormId(approvePlanForm.getId());
                    sessionFacade.saveOrUpdate(approvePlanInfo);
                    plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_CHANGE);
                    sessionFacade.saveOrUpdate(plan);
                }
            }
        }
    }

    @Override
    public TemporaryPlanDto getTemporaryPlan(String id) {
        TemporaryPlan p = (TemporaryPlan)sessionFacade.getEntity(TemporaryPlan.class,id);
        TemporaryPlanDto plan = new TemporaryPlanDto();
        if (!CommonUtil.isEmpty(p)) {
            try {
                plan = (TemporaryPlanDto) VDataUtil.getVDataByEntity(p,TemporaryPlanDto.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return plan;
    }

    @Override
    public List<PlanHistory> getPlanHistoryListByPlanId(String planId) {
        List<PlanHistory> planHistoryList = sessionFacade.findHql("from PlanHistory where planid=? order by createTime desc", planId);
        return planHistoryList;
    }

    @Override
    public List<TemporaryPlan> getTemporaryPlanListByPlanId(String planId) {
        List<TemporaryPlan> temporaryPlanList = sessionFacade.findHql("from TemporaryPlan where planid=? order by createTime desc", planId);
        return temporaryPlanList;
    }

    @Override
    public List<ApprovePlanInfo> queryApprovePlanInfoList(ApprovePlanInfo approvePlanInfo, int page, int rows, boolean isPage) {
        String hql = createApprovePlanInfoHql(approvePlanInfo);
        if (isPage) {
            return pageList(hql, (page - 1) * rows, rows);
        }
        return findByQueryString(hql);
    }

    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param approvePlanInfo
     * @return
     * @see
     */
    private String createApprovePlanInfoHql(ApprovePlanInfo approvePlanInfo) {
        String hql = "from ApprovePlanInfo l where 1 = 1";
        if (StringUtils.isNotEmpty(approvePlanInfo.getId())) {
            hql = hql + " and l.id = '" + approvePlanInfo.getId() + "'";
        }
        if (StringUtils.isNotEmpty(approvePlanInfo.getPlanId())) {
            hql = hql + " and l.planId = '" + approvePlanInfo.getPlanId() + "'";
        }
        return hql;
    }

    @Override
    public List<TempPlanResourceLinkInfo> queryTempPlanResourceLinkList(TempPlanResourceLinkInfo tempPlanResourceLinkInfo, int page, int rows, boolean isPage) {
        String hql = createHqlTemp(tempPlanResourceLinkInfo);
        if(isPage){
            return pageList(hql, (page - 1) * rows , rows);
        }
        return findByQueryString(hql);
    }


    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param tempPlanResourceLinkInfo
     * @return
     * @see
     */
    private String createHqlTemp(TempPlanResourceLinkInfo tempPlanResourceLinkInfo){
        String hql = "from TempPlanResourceLinkInfo l where 1 = 1";
        //  资源名称
        if(!StringUtils.isEmpty(tempPlanResourceLinkInfo.getResourceName())){
            hql = hql + " and l.resourceInfo.name like '%" + tempPlanResourceLinkInfo.getResourceName() + "%'";
        }
        //  资源ID
        if(!StringUtils.isEmpty(tempPlanResourceLinkInfo.getResourceId())){
            hql = hql + " and l.resourceId = '" + tempPlanResourceLinkInfo.getResourceId() + "'";
        }
        if (!StringUtils.isEmpty(tempPlanResourceLinkInfo.getUseObjectId())) {
            hql = hql + " and l.useObjectId = '" + tempPlanResourceLinkInfo.getUseObjectId() + "'";
        }

        // 生命周期状态
        if (!StringUtils.isEmpty(tempPlanResourceLinkInfo.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + tempPlanResourceLinkInfo.getBizCurrent() + "'";
        }
        //是否可用
        if (!StringUtils.isEmpty(tempPlanResourceLinkInfo.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + tempPlanResourceLinkInfo.getAvaliable() + "'";
        }
        return hql;
    }
}
