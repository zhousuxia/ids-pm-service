package com.glaway.ids.project.plan.service.impl;


import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.calendar.FeignCalendarService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.ids.config.constant.ConfigStateConstants;
import com.glaway.ids.config.constant.NameStandardSwitchConstants;
import com.glaway.ids.config.constant.SwitchConstants;
import com.glaway.ids.config.constant.TaskProcConstants;
import com.glaway.ids.config.dto.RDTaskVO;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.models.JsonResult;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.ActivityTypeManageEntityServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.*;
import com.glaway.ids.project.plan.vo.KnowledgeInfoReq;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;


/**
 * 应用研发流程模板实现类
 * 
 * @author wqb
 * @version 2019年10月20日 09:34:17
 * @see ApplyProcTemplateServiceImpl
 */
@Service("applyProcTemplateService")
@Transactional(propagation = Propagation.REQUIRED)
public class ApplyProcTemplateServiceImpl extends CommonServiceImpl implements ApplyProcTemplateServiceI {

    /**
     * 
     */
    private static final SystemLog log = BaseLogFactory.getSystemLog(ApplyProcTemplateServiceImpl.class);

    /**
     * sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;

  
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    
    /**
     * planService
     */
    @Autowired
    private PlanServiceI planService;
    
    /**
     * 名称库<br>
     */
    @Autowired
    private NameStandardRemoteFeignServiceI nameStandardService;
    

    
    /**
     * 配置业务接口
     */
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
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;
    
    
    /**
     * 项目计划参数接口
     */
    @Autowired
    private ParamSwitchServiceI paramSwitchService;
      
    /**
     * 流程模板接口
     */
    @Autowired
    private RdFlowTaskFlowResolveRemoteFeignServiceI rdFlowTaskFlowResolveService;
       

    /**
     *
     */
    @Autowired
    private FeignUserService userService;

    /**
     * 项目管理
     */
    @Autowired
    private ProjectServiceI projectService;
   
    @Autowired
    private Environment env;
    
    @Autowired
    private ActivityTypeManageEntityServiceI activityTypeManageEntityService;

    @Autowired
    private ReignKnowledgeReferenceServiceI  planKnowledgeReferenceFeignService;

    /**
     * 调用研发流程模板分解任务/计划
     */
    @Override
    public boolean templateResolveForPlan(String parentId, String templateId,String currentId) {
        // if (getRdFlowPluginValid()) {

        try {
            Plan parentPlan = (Plan)sessionFacade.getEntity(Plan.class, parentId);
            if (parentPlan == null) {
                return false;
            }
            Project projectParent = (Project)sessionFacade.getEntity(Project.class, parentPlan.getProjectId());
            String projectTimeType = projectParent.getProjectTimeType();
            String parentWorkTime = parentPlan.getWorkTime();
            TaskProcTemplateDto tptmpl = new TaskProcTemplateDto();
            tptmpl = rdFlowTaskFlowResolveService.getProcTemplateEntity(templateId);
            if (!CommonUtil.isEmpty(tptmpl) && !CommonUtil.isEmpty(tptmpl.getId())) {

                FeignJson maxWorkTimeWithTemplateInfoJson = rdFlowTaskFlowResolveService.getMaxWorkTimeByTemp(templateId);
                String maxWorkTimeWithTemplate = maxWorkTimeWithTemplateInfoJson.getObj().toString();
                boolean isStandard = false;
                Map<String, String> standardNameMap = new HashMap<String, String>();
                String defaultNameType = "";
//                String dictCode = "activeCategory";
//                List<TSType> types = TSTypegroup.allTypes.get(dictCode.toLowerCase());
//                  String curTaskNameType = parentPlan.getTaskNameType();
//                  if (!CommonUtil.isEmpty(curTaskNameType)) {
//                      defaultNameType = curTaskNameType;
//                  }else {
                    //获取计划类型 ：
                      List<ActivityTypeManage> activityTypeManageList = activityTypeManageEntityService.getAllActivityTypeManage(false);
                      if(activityTypeManageList.size()>0) {
                          for(ActivityTypeManage activityTypeManage : activityTypeManageList) {
                              if(!CommonUtil.isEmpty(activityTypeManage.getName()) && "研发类".equals(activityTypeManage.getName())) {
                                  defaultNameType = activityTypeManage.getId();
                                  break;
                              }
                          }
                      }
//                  }
//                Map<String,List<TSTypeDto>> tsMap = feignSystemService.getAllTypesOfTypeGroup(ResourceUtil.getApplicationInformation().getAppKey());
//                List<TSTypeDto> types = tsMap.get(dictCode);
//                if (!CommonUtil.isEmpty(types)) {
//                    defaultNameType = types.get(0).getTypecode();
//                }

                String switchStr = "";
                try {
                    switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                    || NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                    || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                    isStandard = true;
                }

                if (isStandard) {
                    NameStandardDto ns = new NameStandardDto();
                    ns.setStopFlag(ConfigStateConstants.START);
                    List<NameStandardDto> list = nameStandardService.searchNameStandardsAccurate(ns);
                    for (NameStandardDto n : list) {
                        standardNameMap.put(n.getName(), n.getActiveCategory());
                    }
                }

                List<TaskProcTemCellConnectVO> taskProcTemCellConnList = new ArrayList<TaskProcTemCellConnectVO>();
                taskProcTemCellConnList = rdFlowTaskFlowResolveService.findTaskProcTemCellConnList(templateId);
                Map<String, List<TaskProcTemCellConnectVO>> connMap = new HashMap<String, List<TaskProcTemCellConnectVO>>();
                for (TaskProcTemCellConnectVO conn : taskProcTemCellConnList) {
                    List<TaskProcTemCellConnectVO> connList = new ArrayList<TaskProcTemCellConnectVO>();
                    if (!CommonUtil.isEmpty(connMap.get(conn.getCellId()))) {
                        connList = connMap.get(conn.getCellId());
                    }
                    connList.add(conn);
                    connMap.put(conn.getCellId(), connList);
                }

                List<TaskCellDeliverItemVO> itemsInputList = new ArrayList<TaskCellDeliverItemVO>();
                itemsInputList = rdFlowTaskFlowResolveService.getTemplateInputs(templateId);

                Map<String, List<TaskCellDeliverItemVO>> itemsInputMap = new HashMap<String, List<TaskCellDeliverItemVO>>();
                for (TaskCellDeliverItemVO in : itemsInputList) {
                    List<TaskCellDeliverItemVO> inList = new ArrayList<TaskCellDeliverItemVO>();
                    if (!CommonUtil.isEmpty(itemsInputMap.get(in.getCellId()))) {
                        inList = itemsInputMap.get(in.getCellId());
                    }
                    inList.add(in);
                    itemsInputMap.put(in.getCellId(), inList);
                }

                List<TaskCellDeliverItemVO> itemsOutputList = new ArrayList<TaskCellDeliverItemVO>();
                itemsOutputList = rdFlowTaskFlowResolveService.getTemplateOutputs(templateId);

                Map<String, List<TaskCellDeliverItemVO>> itemsOutputMap = new HashMap<String, List<TaskCellDeliverItemVO>>();
                for (TaskCellDeliverItemVO out : itemsOutputList) {
                    List<TaskCellDeliverItemVO> outList = new ArrayList<TaskCellDeliverItemVO>();
                    if (!CommonUtil.isEmpty(itemsOutputMap.get(out.getCellId()))) {
                        outList = itemsOutputMap.get(out.getCellId());
                    }
                    outList.add(out);
                    itemsOutputMap.put(out.getCellId(), outList);
                }

                List<TaskCellBasicInfoVO> infoList = new ArrayList<TaskCellBasicInfoVO>();
                infoList = rdFlowTaskFlowResolveService.getTasKCellBaseInfoByTemplateId(templateId);

                Map<String, TaskCellBasicInfoVO> cellInfoMap = new HashMap<String, TaskCellBasicInfoVO>();
                for (TaskCellBasicInfoVO info : infoList) {
                    cellInfoMap.put(info.getCellIndex(), info);
                }

                List<TaskProcTemCellConnectVO> taskProcTemCellConns = connMap.get(TaskProcConstants.TASK_CELL_START);
                Map<String, Object> paramMap = new HashMap<String, Object>();
                List<Plan> insertPlans = new ArrayList<Plan>();
                List<PlanLog> insertPlanLogs = new ArrayList<PlanLog>();
                List<Inputs> insertInputs = new ArrayList<Inputs>();
                List<DeliverablesInfo> insertOutputs = new ArrayList<DeliverablesInfo>();
                List<PreposePlan> insertPreposePlans = new ArrayList<PreposePlan>();
                paramMap.put("insertPlanLogs", insertPlanLogs);
                paramMap.put("insertInputs", insertInputs);
                paramMap.put("insertOutputs", insertOutputs);
                paramMap.put("insertPreposePlans", insertPreposePlans);
                Plan pc = new Plan();
                pc.setProjectId(parentPlan.getProjectId());
                pc.setParentPlanId(parentId);
                int maxPlanNumber = planService.getMaxPlanNumber(pc);
                int maxStoreyNo = 0;
                Plan p = new Plan();
                planService.initBusinessObject(p);
                String planBizCurrent = p.getBizCurrent();
                String planBizVersion = p.getBizVersion();
                LifeCyclePolicy planPolicy = p.getPolicy();

                DeliverablesInfo d = new DeliverablesInfo();
                deliverablesInfoService.initBusinessObject(d);
                String dBizCurrent = d.getBizCurrent();
                String dBizVersion = d.getBizVersion();
                LifeCyclePolicy dPolicy = p.getPolicy();

                Map<String, String> templateInputIdAndInputIdMap = new HashMap<String, String>();
                Map<String, String> templateOutputIdAndOutputIdMap = new HashMap<String, String>();
                Map<String, String> templateBasicIdAndPlanIdMap = new HashMap<String, String>();

                Map<String, String> deliverItemIdPlanIdMap = new HashMap<String, String>();
                Map<String, String> inputNameAndFromIdOutputIdMap = new HashMap<String, String>();
                Map<String, String> infoIdPlanIdMap = new HashMap<String, String>();
                Map<String, String> deliverItemIdOutputIdMap = new HashMap<String, String>();
                Map<String, Plan> namePlanMap = new HashMap<String, Plan>();
                for (TaskProcTemCellConnectVO conn : taskProcTemCellConns) {
                    if (TaskProcConstants.TASK_CELL_START.equals(conn.getCellId())) {
                        TaskCellBasicInfoVO info = cellInfoMap.get(conn.getTargetId());
                        if (CommonUtil.isEmpty(namePlanMap.get(info.getCellName()))) {
                            Plan plan = new Plan();
                            plan.setTabCbTemplateId(info.getTabCbTemplateId());
                            plan.setProjectId(parentPlan.getProjectId());
                            plan.setParentPlanId(parentId);
                            plan.setCellId(conn.getTargetId());
                            plan.setPlanName(info.getCellName()); // cellName 节点名称
                            plan.setRemark(info.getCellRemark()); // cellRemark 备注说明
                            plan.setRequired(info.getIsCellRequired()); // isCellRequired 是否必要活动节点
                            plan.setPlanType(PlanConstants.PLAN_TYPE_FLOW);
                            plan.setPlanStartTime(parentPlan.getPlanStartTime());
                            plan.setFromTemplate("true");
                            String workTime = String.valueOf((int)Math.floor(((Long.valueOf(parentPlan.getWorkTime()) * info.getRefDuration()) / (Long.valueOf(maxWorkTimeWithTemplate)))));
                            if (StringUtils.isEmpty(workTime) || "0".equals(workTime)) {
                                workTime = "1";
                            }
                            if (info.getRefDuration() != null) {
                                plan.setWorkTimeReference(String.valueOf(info.getRefDuration()));
                            }
                            plan.setWorkTime(workTime);
                            Date date = (Date)plan.getPlanStartTime().clone();
                            if (ProjectConstants.WORKDAY.equals(projectTimeType)) {
                                plan.setPlanEndTime(DateUtil.nextWorkDay(date,
                                    Integer.valueOf(plan.getWorkTime()) - 1));
                            }
                            else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                                Map<String, Object> params1 = new HashMap<String, Object>();
                                params1.put("startDate",date);
                                params1.put("days",Integer.valueOf(plan.getWorkTime()) - 1);
                                plan.setPlanEndTime(calendarService.getNextWorkingDay(appKey,params1));
                            }
                            else {
                                plan.setPlanEndTime(TimeUtil.getExtraDate(date,
                                    Integer.valueOf(plan.getWorkTime()) - 1));
                            }
                            // 增加计划类别和计划类型的设值
                            plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                            plan.setTaskNameType(info.getTaskNameType());
                            if (isStandard) {
                                if (StringUtils.isNotEmpty(standardNameMap.get(plan.getPlanName()))) {
                                    plan.setTaskNameType(standardNameMap.get(plan.getPlanName()));
                                    plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                                }
                            }
                            plan.setPlanNumber(maxPlanNumber + 1);
                            maxPlanNumber++ ;
                            plan.setStoreyNo(maxStoreyNo + 1);
                            maxStoreyNo++ ;
                            plan.setId(UUIDGenerator.generate().toString());
                            templateBasicIdAndPlanIdMap.put(info.getId(), plan.getId());
                            plan.setBizCurrent(planBizCurrent);
                            plan.setBizVersion(planBizVersion);
                            plan.setPolicy(planPolicy);
                            plan.setBizId(UUID.randomUUID().toString());
                            infoIdPlanIdMap.put(info.getId(), plan.getId());
                            namePlanMap.put(plan.getPlanName(), plan);
                            namePlanMap.put(plan.getPlanName(), plan);
                            plan.setSecurityLevel((short)1);
                            insertPlans.add(plan);
                            // 计划日志记录
                            PlanLog planLog = new PlanLog();
                            planLog.setId(UUIDGenerator.generate().toString());
                            planLog.setPlanId(plan.getId());
                            planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);
                            insertPlanLogs.add(planLog);

                            // 输出：
                            List<TaskCellDeliverItemVO> outputItems = itemsOutputMap.get(conn.getTargetId());
                            if (!CommonUtil.isEmpty(outputItems)) {
                                for (TaskCellDeliverItemVO outputItem : outputItems) {
                                    DeliverablesInfo output = new DeliverablesInfo();
                                    output.setId(UUIDGenerator.generate().toString());
                                    templateOutputIdAndOutputIdMap.put(outputItem.getId(),
                                        output.getId());
                                    output.setPolicy(dPolicy);
                                    output.setBizCurrent(dBizCurrent);
                                    output.setBizVersion(dBizVersion);
                                    output.setBizId(UUID.randomUUID().toString());
                                    output.setName(outputItem.getDeliverName());
                                    output.setUseObjectId(plan.getId());
                                    output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    output.setRequired(outputItem.getIsOutputDeliverRequired());
                                    insertOutputs.add(output);
                                    deliverItemIdOutputIdMap.put(outputItem.getId(),
                                        output.getId());
                                    deliverItemIdPlanIdMap.put(outputItem.getUserObjectId(),
                                        output.getUseObjectId());
                                    inputNameAndFromIdOutputIdMap.put(output.getName() + ","
                                        + output.getUseObjectId(),
                                        output.getId());
                                }
                            }

                            // 输入：
                            List<TaskCellDeliverItemVO> inputItems = itemsInputMap.get(conn.getTargetId());
                            if (!CommonUtil.isEmpty(inputItems)) {
                                for (TaskCellDeliverItemVO inputItem : inputItems) {
                                    Inputs input = new Inputs();
                                    input.setId(UUIDGenerator.generate().toString());
                                    templateInputIdAndInputIdMap.put(inputItem.getId(),
                                        input.getId());
                                    input.setName(inputItem.getDeliverName());
                                    input.setUseObjectId(plan.getId());
                                    input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    input.setRequired(inputItem.getIsOutputDeliverRequired());
                                    // 内部输入：
                                    if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                        && PlanConstants.INNERTASK_EN.equals(inputItem.getOriginType())) {
                                        input.setOriginType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                        input.setOriginTypeExt(PlanConstants.INNERTASK_EN);
                                    }
                                    // 外部输入：
                                    if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                        && PlanConstants.DELIEVER_EN.equals(inputItem.getOriginType())) {
                                        input.setOriginTypeExt(PlanConstants.DELIEVER_EN);
                                    }
                                    // 本地上传输入：
                                    if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                        && PlanConstants.LOCAL_EN.equals(inputItem.getOriginType())) {
                                        input.setDocId(inputItem.getFilePath());
                                        input.setDocName(inputItem.getFileName());
                                        input.setOriginType(PlanConstants.LOCAL_EN);
                                        input.setDocId(inputItem.getFilePath());
                                    }
                                    input.setOriginObjectId(inputItem.getOriginObjectId());
                                    insertInputs.add(input);

                                }
                            }

                            List<TaskProcTemCellConnectVO> backConns = connMap.get(conn.getTargetId());
                            paramMap.put("prepose", plan);
                            paramMap.put("parentWorkTime", parentWorkTime);
                            paramMap.put("maxWorkTimeWithTemplate", maxWorkTimeWithTemplate);
                            paramMap.put("isStandard", isStandard);
                            paramMap.put("defaultNameType", defaultNameType);
                            paramMap.put("projectTimeType", projectTimeType);
                            paramMap.put("maxPlanNumber", maxPlanNumber);
                            paramMap.put("maxStoreyNo", maxStoreyNo);

                            paramMap.put("planBizCurrent", planBizCurrent);
                            paramMap.put("planBizVersion", planBizVersion);
                            paramMap.put("planPolicy", planPolicy);

                            paramMap.put("dBizCurrent", dBizCurrent);
                            paramMap.put("dBizVersion", dBizVersion);
                            paramMap.put("dPolicy", dPolicy);

                            paramMap.put("insertPlans", insertPlans);
                            paramMap.put("insertPlanLogs", insertPlanLogs);
                            paramMap.put("insertInputs", insertInputs);
                            paramMap.put("insertOutputs", insertOutputs);
                            paramMap.put("insertPreposePlans", insertPreposePlans);
                            paramMap.put("backPlans", backConns);
                            templateResolveWithBackPlan(standardNameMap, paramMap, connMap,
                                cellInfoMap, namePlanMap, infoIdPlanIdMap,
                                deliverItemIdOutputIdMap, itemsInputMap, itemsOutputMap,
                                inputNameAndFromIdOutputIdMap, deliverItemIdPlanIdMap,
                                templateInputIdAndInputIdMap, templateOutputIdAndOutputIdMap,
                                templateBasicIdAndPlanIdMap);
                        }
                    }
                }

                insertPlans = (List<Plan>)paramMap.get("insertPlans");
                for(Plan curPl : insertPlans) {
                    curPl.setSecurityLevel((short)1);
                }
                insertPlanLogs = (List<PlanLog>)paramMap.get("insertPlanLogs");
                insertInputs = (List<Inputs>)paramMap.get("insertInputs");

                // 获取来源信息：
                for (Inputs curInputs : insertInputs) {
                    if (!CommonUtil.isEmpty(curInputs.getOriginObjectId())) {
                        curInputs.setOriginObjectId(deliverItemIdPlanIdMap.get(curInputs.getOriginObjectId()));
                        curInputs.setOriginDeliverablesInfoId(inputNameAndFromIdOutputIdMap.get(curInputs.getName()
                            + ","
                            + curInputs.getOriginObjectId()));
                    }
                }

                insertOutputs = (List<DeliverablesInfo>)paramMap.get("insertOutputs");
                insertPreposePlans = (List<PreposePlan>)paramMap.get("insertPreposePlans");

                TSUserDto currentUser = userService.getUserByUserId(currentId);
                Map<String, Object> flgMap = new HashMap<>();
                flgMap.put("user", currentUser);
                flgMap.put("needDelete", false);
                // 判断是否反推计划：
                String backChangePlanSwitchStr = paramSwitchService.getSwitch(SwitchConstants.BACKCHANGEPLAN);
                if (!CommonUtil.isEmpty(backChangePlanSwitchStr)) {
                    String msg = planService.batchSaveDatas(flgMap, insertPlans, insertPlanLogs,
                        insertInputs, insertOutputs, insertPreposePlans, new ArrayList<Inputs>());
                    if (!"false".equals(msg)) {
                        Map<String, String> planMap = new HashMap<String, String>();
                        Map<String, String> cellWorkTimeMap = new HashMap<String, String>();
                        for (Plan pl : insertPlans) {
                            planMap.put(pl.getPlanName(), pl.getId());
                            String label = pl.getPlanName() + "," + pl.getWorkTime() + "天";
                            cellWorkTimeMap.put(pl.getCellId(), label);
                        }

                        String flowResolveXml = tptmpl.getTemlXml();
                        flowResolveXml = refreshFlowResolveXml(tptmpl.getTemlXml(),
                            cellWorkTimeMap);
                        parentPlan.setOpContent(PlanConstants.PLAN_LOGINFO_FLOW_SPLIT);
                        parentPlan.setFlowResolveXml(flowResolveXml);
                        sessionFacade.saveOrUpdate(parentPlan);
                    }
                }
                else {
                    templateInputIdAndInputIdMap = null;
                    templateOutputIdAndOutputIdMap = null;
                    templateBasicIdAndPlanIdMap = null;
                    Map<String, String> planMap = new HashMap<String, String>();
                    Map<String, String> cellWorkTimeMap = new HashMap<String, String>();
                    for (Plan pl : insertPlans) {
                        planMap.put(pl.getPlanName(), pl.getId());
                        String label = pl.getPlanName() + "," + pl.getWorkTime() + "天";
                        cellWorkTimeMap.put(pl.getCellId(), label);
                    }

                    String flowResolveXml = tptmpl.getTemlXml();
                    flowResolveXml = refreshFlowResolveXml(tptmpl.getTemlXml(), cellWorkTimeMap);
                    parentPlan.setOpContent(PlanConstants.PLAN_LOGINFO_FLOW_SPLIT);
                    parentPlan.setFlowResolveXml(flowResolveXml);
                    sessionFacade.saveOrUpdate(parentPlan);
                }

                Plan condition = new Plan();
                condition.setParentPlanId(parentPlan.getId());
                List<Plan> planlist = planService.queryPlanList(condition, 1, 10, false);

                String taskIds = "";
                String workIds = "";

                //获取研发流程模板节点：
                List<TaskCellBasicInfoVO> infoList2 = new ArrayList<TaskCellBasicInfoVO>();
                infoList2 = rdFlowTaskFlowResolveService.getTasKCellBaseInfoByTemplateId(templateId);
                for(TaskCellBasicInfoVO curVo : infoList2){
                    if (CommonUtil.isEmpty(workIds)) {
                        workIds = curVo.getId();
                    }
                    else {
                        workIds = workIds + "," + curVo.getId();
                    }
                }

                for (Plan t : planlist) {
                    if (CommonUtil.isEmpty(taskIds)) {
                        taskIds = t.getId();
                    }
                    else {
                        taskIds = taskIds + "," + t.getId();
                    }
                }

                //保存流程分解的参考信息
                KnowledgeInfoReq req = new KnowledgeInfoReq();
                req.setTaskId(taskIds);
                req.setWorkId(workIds);
                req.setUserId(currentId);
                JsonResult jsonResult = new JsonResult();
                try {
                    Map<String, Object> paramsMap = new HashMap<String, Object>();
                    paramsMap.put("jsonResult", jsonResult);
                    paramsMap.put("knowledgeInfoReq", req);
                    jsonResult = planKnowledgeReferenceFeignService.savePlanTaskKnowledgeReference(paramsMap);
                } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }


//                if (getKnowledgePluginValid()) {
//                    Gson gsonOld = new Gson();
//                    JsonRequery jsonReq = new JsonRequery();
//                    jsonReq.setReqObj(gsonOld.toJson(req));
//                    String jdelete = gsonOld.toJson(jsonReq);
//                    JsonResult jsonResult = new JsonResult();
//                    KnowledgeSupportImplService supportService = new KnowledgeSupportImplService();
//                    KnowledgeSupport supportOld = supportService.getKnowledgeSupportImplPort();
//                    String listStr = null;
//                    try {
//                        listStr = supportOld.operationKnowledge(jdelete, "resolve");
//                    }
//                    catch (GWException_Exception e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    jsonResult = gsonOld.fromJson(listStr, JsonResult.class);
//                }
//                // 调用外部系统处理
//                TaskFlowResolveVO taskFlowResolveVO = new TaskFlowResolveVO();
//                taskFlowResolveVO.setTemplateId(templateId);
//                taskFlowResolveVO.setParentPlanId(parentId);
//                taskFlowResolveVO.setUserId(UserUtil.getCurrentUser().getId());
//                List<PlanForResolveVO> resolvePlans = new ArrayList<PlanForResolveVO>();
//                PlanForResolveVO planForResolveVO = new PlanForResolveVO();
//                List<InputsForResolveVO> inputs = new ArrayList<InputsForResolveVO>();
//                InputsForResolveVO inputsForResolveVO = new InputsForResolveVO();
//                List<DeliverablesInfoForResolveVO> deliverables = new ArrayList<DeliverablesInfoForResolveVO>();
//                DeliverablesInfoForResolveVO deliverablesInfoForResolveVO = new DeliverablesInfoForResolveVO();
//                TaskCellBasicInfoVO cellBasicInfo = new TaskCellBasicInfoVO();
//                List<TaskCellDeliverItemVO> inList = new ArrayList<TaskCellDeliverItemVO>();
//                Map<String, TaskCellDeliverItemVO> inItemMap = new HashMap<String, TaskCellDeliverItemVO>();
//
//                List<TaskCellDeliverItemVO> outList = new ArrayList<TaskCellDeliverItemVO>();
//                Map<String, TaskCellDeliverItemVO> outItemMap = new HashMap<String, TaskCellDeliverItemVO>();
//
//                TaskCellDeliverItemVO deliverItem = new TaskCellDeliverItemVO();
//
//                boolean needHttp = false;
//
//                List<String> outCctiveCategorys = new ArrayList<String>();
//
//                for (Plan pl : insertPlans) {
//                    planForResolveVO = new PlanForResolveVO();
//                    planForResolveVO.setPlanId(pl.getId());
//                    planForResolveVO.setPlanName(pl.getPlanName());
//                    if (!CommonUtil.isEmpty(cellInfoMap)) {
//                        cellBasicInfo = cellInfoMap.get(pl.getCellId());
//                        if (!CommonUtil.isEmpty(cellBasicInfo)) {
//                            planForResolveVO.setTemplateNodeId(cellBasicInfo.getId());
//                            planForResolveVO.setNamestandardId(cellBasicInfo.getNameStandardId());
//                            inList = itemsInputMap.get(cellBasicInfo.getCellIndex());
//                            if (!CommonUtil.isEmpty(inList)) {
//                                for (TaskCellDeliverItemVO inItem : inList) {
//                                    inItemMap.put(inItem.getDeliverName(), inItem);
//                                }
//                            }
//                            outList = itemsOutputMap.get(cellBasicInfo.getCellIndex());
//                            if (!CommonUtil.isEmpty(outList)) {
//                                for (TaskCellDeliverItemVO outItem : outList) {
//                                    outItemMap.put(outItem.getDeliverName(), outItem);
//                                }
//                            }
//                        }
//                    }
//                    if (!CommonUtil.isEmpty(standardNameMap)) {
//                        planForResolveVO.setActiveCategory(standardNameMap.get(pl.getPlanName()));
//                        if (!needHttp
//                            && !CommonUtil.isEmpty(planForResolveVO.getActiveCategory())
//                            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(planForResolveVO.getActiveCategory())
//                            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(planForResolveVO.getActiveCategory())
//                            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(planForResolveVO.getActiveCategory())) {
//                            if (!outCctiveCategorys.contains(planForResolveVO.getActiveCategory())) {
//                                outCctiveCategorys.add(planForResolveVO.getActiveCategory());
//                            }
//                            needHttp = true;
//                        }
//                    }
//                    if (!CommonUtil.isEmpty(insertInputs)) {
//                        inputs = new ArrayList<InputsForResolveVO>();
//                        for (Inputs in : insertInputs) {
//                            if (pl.getId().equals(in.getUseObjectId())) {
//                                inputsForResolveVO = new InputsForResolveVO();
//                                inputsForResolveVO.setInputId(in.getId());
//                                inputsForResolveVO.setSourcePlanId(in.getOriginObjectId());
//                                inputsForResolveVO.setSourceDeliverSeq(in.getOriginDeliverablesInfoId());
//                                if (!CommonUtil.isEmpty(inItemMap)) {
//                                    deliverItem = inItemMap.get(in.getName());
//                                    if (!CommonUtil.isEmpty(deliverItem)) {
//                                        inputsForResolveVO.setTemplateDeliverSeq(deliverItem.getId());
//                                        inputsForResolveVO.setDeliverId(deliverItem.getDeliverId());
//                                    }
//                                }
//                                inputs.add(inputsForResolveVO);
//                            }
//                        }
//                        planForResolveVO.setInputs(inputs);
//                    }
//                    if (!CommonUtil.isEmpty(insertOutputs)) {
//                        deliverables = new ArrayList<DeliverablesInfoForDto>();
//                        for (DeliverablesInfo out : insertOutputs) {
//                            if (pl.getId().equals(out.getUseObjectId())) {
//                                deliverablesInfoForResolveVO = new DeliverablesInfoForResolveVO();
//                                deliverablesInfoForResolveVO.setDeliverablesInfoId(out.getId());
//                                if (!CommonUtil.isEmpty(outItemMap)) {
//                                    deliverItem = outItemMap.get(out.getName());
//                                    if (!CommonUtil.isEmpty(deliverItem)) {
//                                        deliverablesInfoForResolveVO.setTemplateDeliverSeq(deliverItem.getId());
//                                        deliverablesInfoForResolveVO.setDeliverId(deliverItem.getDeliverId());
//                                    }
//                                }
//                                deliverables.add(deliverablesInfoForResolveVO);
//                            }
//                        }
//                        planForResolveVO.setDeliverables(deliverables);
//                    }
//                    resolvePlans.add(planForResolveVO);
//                }
//                taskFlowResolveVO.setResolvePlans(resolvePlans);
                
//                if (needHttp && !CommonUtil.isEmpty(outCctiveCategorys)) {
//                    List<String> httpUrls = new ArrayList<String>();
//                    List<OutwardExtension> outwardExtensionList = outwardExtensionService.getOutwardExtensionList("flowResolveCategoryHttpServer");
//                    if (!CommonUtil.isEmpty(outwardExtensionList)) {
//                        for (OutwardExtension ext : outwardExtensionList) {
//                            if (outCctiveCategorys.contains(ext.getOptionValue())
//                                && !CommonUtil.isEmpty(ext.getUrlList())) {
//                                for (OutwardExtensionUrl out : ext.getUrlList()) {
//                                    if ("chooseFlowTemplate".equals(out.getOperateCode())) {
//                                        httpUrls.add(out.getOperateUrl());
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (!CommonUtil.isEmpty(httpUrls)) {
//                        for (String url : httpUrls) {
//                            try {
//                                HttpClientUtil.httpClientPostByTest(url, taskFlowResolveVO);
//                            }
//                            catch (Exception e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
                
                String planMapStr = "";
                String inputMapStr = "";
                String outputMapStr = "";
                // 计划：
                if (!CommonUtil.isEmpty(templateBasicIdAndPlanIdMap)) {
                    for (String planKey : templateBasicIdAndPlanIdMap.keySet()) {
                        if (CommonUtil.isEmpty(planMapStr)) {
                            planMapStr = planKey + "=" + templateBasicIdAndPlanIdMap.get(planKey);
                        }
                        else {
                            planMapStr = planMapStr + "," + planKey + "="
                                + templateBasicIdAndPlanIdMap.get(planKey);
                        }
                    }
                }
                // 输入：
                if (!CommonUtil.isEmpty(templateInputIdAndInputIdMap)) {
                    for (String inputKey : templateInputIdAndInputIdMap.keySet()) {
                        if (CommonUtil.isEmpty(inputMapStr)) {
                            inputMapStr = inputKey + "="
                                + templateInputIdAndInputIdMap.get(inputKey);
                        }
                        else {
                            inputMapStr = inputMapStr + "," + inputKey + "="
                                + templateInputIdAndInputIdMap.get(inputKey);
                        }
                    }
                }
                // 输出：
                if (!CommonUtil.isEmpty(templateOutputIdAndOutputIdMap)) {
                    for (String outputKey : templateOutputIdAndOutputIdMap.keySet()) {
                        if (CommonUtil.isEmpty(outputMapStr)) {
                            outputMapStr = outputKey + "="
                                + templateOutputIdAndOutputIdMap.get(outputKey);
                        }
                        else {
                            outputMapStr = outputMapStr + "," + outputKey + "="
                                + templateOutputIdAndOutputIdMap.get(outputKey);
                        }
                    }
                }
                // 分解成功后，数据保存到研发任务里面：
                // 添加到任务流程中去：
                PlanDto parent = planService.getPlanEntity(parentId);
                RDTaskVO rdTaskVO = new RDTaskVO();
                rdTaskVO.setParentPlanId(parent.getId());
                rdTaskVO.setPlanName(parent.getPlanName());
                Project curProject = projectService.getProjectEntity(parent.getProjectId());
                if(!CommonUtil.isEmpty(curProject)) {
                    rdTaskVO.setWorkTimeType(curProject.getProjectTimeType());
                }
                rdTaskVO.setOwner(parent.getOwner());
                if(!CommonUtil.isEmpty(parent.getPlanStartTime())) {
                    rdTaskVO.setPlanStartTimeStr(DateUtil.formatDate(parent.getPlanStartTime(),
                        DateUtil.FORMAT_ONE));
                }
                rdTaskVO.setPlanStartTime(parent.getPlanStartTime());
                if(!CommonUtil.isEmpty(parent.getPlanEndTime())) {
                    rdTaskVO.setPlanEndTimeStr(DateUtil.formatDate(parent.getPlanEndTime(),
                        DateUtil.FORMAT_ONE));
                }
                rdTaskVO.setPlanEndTime(parent.getPlanEndTime());
                rdTaskVO.setWorkTime(parent.getWorkTime());
                rdTaskVO.setRemark(parent.getRemark());
                rdTaskVO.setAssigner(parent.getAssigner());
                if(!CommonUtil.isEmpty(parent.getAssignTime())) {
                    rdTaskVO.setAssignTimeStr(DateUtil.formatDate(parent.getAssignTime(),
                        DateUtil.FORMAT_ONE));
                }
                rdTaskVO.setAssignTime(parent.getAssignTime());
                // 相关的下达信息也要添加：
                String approveType = "";
                String procInstId = "";
                if (!CommonUtil.isEmpty(parent.getFormId())) {
                    ApprovePlanFormDto approvePlanForm = planService.getApprovePlanFormEntity(parent.getFormId());
                    if (!CommonUtil.isEmpty(approvePlanForm)) {
                        approveType = approvePlanForm.getApproveType();
                        procInstId = approvePlanForm.getProcInstId();
                    }
                }
                
                String outUserId = currentId;
                String curFormId ="";
                if(!CommonUtil.isEmpty(parent.getFormId())) {
                    curFormId = parent.getFormId();
                }
                rdFlowTaskFlowResolveService.getSaveRdTaskInfo(rdTaskVO, templateId, outUserId, approveType, procInstId,
                    curFormId, planMapStr, inputMapStr, outputMapStr);
                return true;
            }else {
                //手动分解同步父节点信息：
                parentPlan.setOpContent(PlanConstants.PLAN_LOGINFO_FLOW_SPLIT);
                String flowResolveXml = tptmpl.getTemlXml();
                parentPlan.setFlowResolveXml(flowResolveXml);
                sessionFacade.saveOrUpdate(parentPlan);
                //同步任务信息：
                PlanDto parent = planService.getPlanEntity(parentId);
                RDTaskVO rdTaskVO = new RDTaskVO();
                rdTaskVO.setParentPlanId(parent.getId());
                rdTaskVO.setPlanName(parent.getPlanName());
                Project curProject = projectService.getProjectEntity(parent.getProjectId());
                if(!CommonUtil.isEmpty(curProject)) {
                    rdTaskVO.setWorkTimeType(curProject.getProjectTimeType());
                }
                rdTaskVO.setOwner(parent.getOwner());
                if(!CommonUtil.isEmpty(parent.getPlanStartTime())) {
                    rdTaskVO.setPlanStartTimeStr(DateUtil.formatDate(parent.getPlanStartTime(),
                        DateUtil.FORMAT_ONE));
                }
                rdTaskVO.setPlanStartTime(parent.getPlanStartTime());
                if(!CommonUtil.isEmpty(parent.getPlanEndTime())) {
                    rdTaskVO.setPlanEndTimeStr(DateUtil.formatDate(parent.getPlanEndTime(),
                        DateUtil.FORMAT_ONE));
                }
                rdTaskVO.setPlanEndTime(parent.getPlanEndTime());
                rdTaskVO.setWorkTime(parent.getWorkTime());
                rdTaskVO.setRemark(parent.getRemark());
                rdTaskVO.setAssigner(parent.getAssigner());
                if(!CommonUtil.isEmpty(parent.getAssignTime())) {
                    rdTaskVO.setAssignTimeStr(DateUtil.formatDate(parent.getAssignTime(),
                        DateUtil.FORMAT_ONE));
                }
                rdTaskVO.setAssignTime(parent.getAssignTime());
                // 相关的下达信息也要添加：
                String approveType = "";
                String procInstId = "";
                if (!CommonUtil.isEmpty(parent.getFormId())) {
                    ApprovePlanFormDto approvePlanForm = planService.getApprovePlanFormEntity(parent.getFormId());
                    if (!CommonUtil.isEmpty(approvePlanForm)) {
                        approveType = approvePlanForm.getApproveType();
                        procInstId = approvePlanForm.getProcInstId();
                    }
                }
                
                String outUserId = currentId;
                String curFormId ="";
                if(!CommonUtil.isEmpty(parent.getFormId())) {
                    curFormId = parent.getFormId();
                }
                rdFlowTaskFlowResolveService.getSaveRdTaskInfo(rdTaskVO, templateId, outUserId, approveType, procInstId,
                    curFormId, "", "", "");
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // }

        return false;

    }
    
    
    /**
     * 根据流程模板节点保存期后置计划及其相关信息
     * @param standardNameMap
     * @param paramMap
     * @param connMap
     * @param cellInfoMap
     * @param namePlanMap
     * @param infoIdPlanIdMap
     * @param deliverItemIdOutputIdMap
     * @param itemsInputMap
     * @param itemsOutputMap
     * @param inputNameAndFromIdOutputIdMap
     * @param deliverItemIdPlanIdMap
     * @param templateInputIdAndInputIdMap
     * @param templateOutputIdAndOutputIdMap
     * @param templateBasicIdAndPlanIdMap
     */
    private void templateResolveWithBackPlan(Map<String, String> standardNameMap,
                                             Map<String, Object> paramMap,
                                             Map<String, List<TaskProcTemCellConnectVO>> connMap,
                                             Map<String, TaskCellBasicInfoVO> cellInfoMap,
                                             Map<String, Plan> namePlanMap,
                                             Map<String, String> infoIdPlanIdMap,
                                             Map<String, String> deliverItemIdOutputIdMap,
                                             Map<String, List<TaskCellDeliverItemVO>> itemsInputMap,
                                             Map<String, List<TaskCellDeliverItemVO>> itemsOutputMap,
                                             Map<String, String> inputNameAndFromIdOutputIdMap,
                                             Map<String, String> deliverItemIdPlanIdMap,
                                             Map<String, String> templateInputIdAndInputIdMap,
                                             Map<String, String> templateOutputIdAndOutputIdMap,
                                             Map<String, String> templateBasicIdAndPlanIdMap) {
        List<TaskProcTemCellConnectVO> backPlans = (List<TaskProcTemCellConnectVO>)paramMap.get("backPlans");
        Plan prepose = (Plan)paramMap.get("prepose");
        String parentWorkTime = (String)paramMap.get("parentWorkTime");
        String maxWorkTimeWithTemplate = (String)paramMap.get("maxWorkTimeWithTemplate");
        boolean isStandard = (boolean)paramMap.get("isStandard");
        String defaultNameType = (String)paramMap.get("defaultNameType");
        String projectTimeType = (String)paramMap.get("projectTimeType");
        int maxPlanNumber = (int)paramMap.get("maxPlanNumber");
        int maxStoreyNo = (int)paramMap.get("maxStoreyNo");

        String planBizCurrent = (String)paramMap.get("planBizCurrent");
        String planBizVersion = (String)paramMap.get("planBizVersion");
        LifeCyclePolicy planPolicy = (LifeCyclePolicy)paramMap.get("planPolicy");

        String dBizCurrent = (String)paramMap.get("dBizCurrent");
        String dBizVersion = (String)paramMap.get("dBizVersion");
        LifeCyclePolicy dPolicy = (LifeCyclePolicy)paramMap.get("dPolicy");

        List<Plan> insertPlans = (List<Plan>)paramMap.get("insertPlans");
        List<PlanLog> insertPlanLogs = (List<PlanLog>)paramMap.get("insertPlanLogs");
        List<Inputs> insertInputs = (List<Inputs>)paramMap.get("insertInputs");
        List<DeliverablesInfo> insertOutputs = (List<DeliverablesInfo>)paramMap.get("insertOutputs");
        List<PreposePlan> insertPreposePlans = (List<PreposePlan>)paramMap.get("insertPreposePlans");

        for (TaskProcTemCellConnectVO conn : backPlans) {
            if (!TaskProcConstants.TASK_CELL_END.equals(conn.getTargetId())) {
                TaskCellBasicInfoVO info = cellInfoMap.get(conn.getTargetId());
                Plan plan = new Plan();
                if (CommonUtil.isEmpty(namePlanMap.get(info.getCellName()))) {
                    plan.setTabCbTemplateId(info.getTabCbTemplateId());
                    plan.setProjectId(prepose.getProjectId());
                    plan.setParentPlanId(prepose.getParentPlanId());
                    plan.setCellId(conn.getTargetId());
                    plan.setPlanName(info.getCellName()); // cellName 节点名称
                    plan.setRemark(info.getCellRemark()); // cellRemark 备注说明
                    plan.setRequired(info.getIsCellRequired()); // isCellRequired 是否必要活动节点
                    plan.setPlanType(PlanConstants.PLAN_TYPE_FLOW);
                    plan.setFromTemplate("true");
                    if (info.getRefDuration() != null) {
                        plan.setWorkTimeReference(String.valueOf(info.getRefDuration()));
                    }
                    String workTime = String.valueOf((int)Math.floor(((Long.valueOf(parentWorkTime) * info.getRefDuration()) / (Long.valueOf(maxWorkTimeWithTemplate)))));
                    if (StringUtils.isEmpty(workTime) || "0".equals(workTime)) {
                        workTime = "1";
                    }
                    plan.setWorkTime(workTime);

                    Date proposeEndTime = (Date)prepose.getPlanEndTime().clone();
                    if (StringUtils.isNotEmpty(projectTimeType)) {
                        if (ProjectConstants.WORKDAY.equals(projectTimeType)) {
                            plan.setPlanStartTime(DateUtil.nextWorkDay(proposeEndTime, 1));
                            Date date = (Date)plan.getPlanStartTime().clone();
                            plan.setPlanEndTime(DateUtil.nextWorkDay(date,
                                Integer.valueOf(plan.getWorkTime()) - 1));
                        }
                        else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("startDate",proposeEndTime);
                            params.put("days",1);   
                            plan.setPlanStartTime(calendarService.getNextWorkingDay(
                                appKey, params));
                            Date date = (Date)plan.getPlanStartTime().clone();
                            Map<String, Object> params1 = new HashMap<String, Object>();
                            params1.put("startDate",date);
                            params1.put("days",Integer.valueOf(plan.getWorkTime()) - 1);   
                            plan.setPlanEndTime(calendarService.getNextWorkingDay(appKey,params1));
                        }
                        else {
                            plan.setPlanStartTime(TimeUtil.getExtraDate(proposeEndTime, 1));
                            Date date = (Date)plan.getPlanStartTime().clone();
                            plan.setPlanEndTime(TimeUtil.getExtraDate(date,
                                Integer.valueOf(plan.getWorkTime()) - 1));
                        }
                    }
                    else {
                        plan.setPlanStartTime(TimeUtil.getExtraDate(proposeEndTime, 1));
                        Date date = (Date)plan.getPlanStartTime().clone();
                        plan.setPlanEndTime(TimeUtil.getExtraDate(date,
                            Integer.valueOf(plan.getWorkTime()) - 1));
                    }

                    // 增加计划类别和计划类型的设值
                    plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                    plan.setTaskNameType(info.getTaskNameType());
                    if (isStandard) {
                        if (StringUtils.isNotEmpty(standardNameMap.get(plan.getPlanName()))) {
                            plan.setTaskNameType(standardNameMap.get(plan.getPlanName()));
                            plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                        }
                    }
                    plan.setPlanNumber(maxPlanNumber + 1);
                    maxPlanNumber++ ;
                    plan.setStoreyNo(maxStoreyNo + 1);
                    maxStoreyNo++ ;
                    plan.setId(UUIDGenerator.generate().toString());
                    templateBasicIdAndPlanIdMap.put(info.getId(), plan.getId());
                    plan.setBizCurrent(planBizCurrent);
                    plan.setBizVersion(planBizVersion);
                    plan.setPolicy(planPolicy);
                    plan.setBizId(UUID.randomUUID().toString());
                    plan.setTabCbTemplateId(info.getTabCbTemplateId());
                    infoIdPlanIdMap.put(info.getId(), plan.getId());
                    insertPlans.add(plan);
                    namePlanMap.put(plan.getPlanName(), plan);

                    // 计划日志记录
                    PlanLog planLog = new PlanLog();
                    planLog.setId(UUIDGenerator.generate().toString());
                    planLog.setPlanId(plan.getId());
                    planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);
                    insertPlanLogs.add(planLog);

                    PreposePlan preposePlan = new PreposePlan();
                    preposePlan.setId(UUIDGenerator.generate().toString());
                    preposePlan.setPlanId(plan.getId());
                    preposePlan.setPreposePlanId(prepose.getId());
                    insertPreposePlans.add(preposePlan);

                    List<TaskCellDeliverItemVO> inputItems = itemsInputMap.get(conn.getTargetId());
                    if (!CommonUtil.isEmpty(inputItems)) {
                        for (TaskCellDeliverItemVO inputItem : inputItems) {
                            Inputs input = new Inputs();
                            input.setId(UUIDGenerator.generate().toString());
                            templateInputIdAndInputIdMap.put(inputItem.getId(), input.getId());
                            input.setName(inputItem.getDeliverName());
                            input.setUseObjectId(plan.getId());
                            input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                            input.setRequired(inputItem.getIsOutputDeliverRequired());
                            // 内部输入：
                            if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                && PlanConstants.INNERTASK_EN.equals(inputItem.getOriginType())) {
                                input.setOriginType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                input.setOriginTypeExt(PlanConstants.INNERTASK_EN);
                            }
                            // 外部输入：
                            if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                && PlanConstants.DELIEVER_EN.equals(inputItem.getOriginType())) {
                                input.setOriginTypeExt(PlanConstants.DELIEVER_EN);
                            }
                            // 本地上传输入：
                            if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                && PlanConstants.LOCAL_EN.equals(inputItem.getOriginType())) {
                                input.setOriginType(PlanConstants.LOCAL_EN);
                                input.setDocId(inputItem.getFilePath());
                                input.setDocName(inputItem.getFileName());
                            }
                            input.setOriginObjectId(inputItem.getOriginObjectId());
                            insertInputs.add(input);
                        }
                    }

                    List<TaskCellDeliverItemVO> outputItems = itemsOutputMap.get(conn.getTargetId());
                    if (!CommonUtil.isEmpty(outputItems)) {
                        for (TaskCellDeliverItemVO outputItem : outputItems) {
                            DeliverablesInfo output = new DeliverablesInfo();
                            output.setId(UUIDGenerator.generate().toString());
                            templateOutputIdAndOutputIdMap.put(outputItem.getId(), output.getId());
                            output.setPolicy(dPolicy);
                            output.setBizCurrent(dBizCurrent);
                            output.setBizVersion(dBizVersion);
                            output.setBizId(UUID.randomUUID().toString());
                            output.setName(outputItem.getDeliverName());
                            output.setUseObjectId(plan.getId());
                            output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                            output.setRequired(outputItem.getIsOutputDeliverRequired());
                            insertOutputs.add(output);
                            deliverItemIdPlanIdMap.put(outputItem.getUserObjectId(),
                                output.getUseObjectId());
                            deliverItemIdOutputIdMap.put(outputItem.getId(), output.getId());
                            inputNameAndFromIdOutputIdMap.put(
                                output.getName() + "," + output.getUseObjectId(), output.getId());
                        }
                    }

                    List<TaskProcTemCellConnectVO> backConns = connMap.get(conn.getTargetId());

                    paramMap.put("prepose", plan);
                    paramMap.put("parentWorkTime", parentWorkTime);
                    paramMap.put("maxPlanNumber", maxPlanNumber);
                    paramMap.put("maxStoreyNo", maxStoreyNo);
                    paramMap.put("insertPlans", insertPlans);
                    paramMap.put("insertPlanLogs", insertPlanLogs);
                    paramMap.put("insertInputs", insertInputs);
                    paramMap.put("insertOutputs", insertOutputs);
                    paramMap.put("insertPreposePlans", insertPreposePlans);
                    paramMap.put("backPlans", backConns);
                    templateResolveWithBackPlan(standardNameMap, paramMap, connMap, cellInfoMap,
                        namePlanMap, infoIdPlanIdMap, deliverItemIdOutputIdMap, itemsInputMap,
                        itemsOutputMap, inputNameAndFromIdOutputIdMap, deliverItemIdPlanIdMap,
                        templateInputIdAndInputIdMap, templateOutputIdAndOutputIdMap,
                        templateBasicIdAndPlanIdMap);
                }
                else {
                    plan = namePlanMap.get(info.getCellName());
                    Date startTime = new Date();
                    Date endTime = new Date();
                    Date proposeEndTime = (Date)prepose.getPlanEndTime().clone();
                    if (StringUtils.isNotEmpty(projectTimeType)) {
                        if (ProjectConstants.WORKDAY.equals(projectTimeType)) {
                            startTime = DateUtil.nextWorkDay(proposeEndTime, 1);
                            Date date = (Date)startTime.clone();
                            endTime = DateUtil.nextWorkDay(date,
                                Integer.valueOf(plan.getWorkTime()) - 1);
                        }
                        else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("startDate",proposeEndTime);
                            params.put("days",1);   
                            startTime = calendarService.getNextWorkingDay(appKey, params);
                            
                            Map<String, Object> params1 = new HashMap<String, Object>();
                            params1.put("startDate",proposeEndTime);
                            params1.put("days",Integer.valueOf(plan.getWorkTime()) - 1);   
                            endTime = calendarService.getNextWorkingDay(appKey, params1);
                        }
                        else {
                            startTime = TimeUtil.getExtraDate(proposeEndTime, 1);
                            Date date = (Date)startTime.clone();
                            endTime = TimeUtil.getExtraDate(date,
                                Integer.valueOf(plan.getWorkTime()) - 1);
                        }
                    }
                    else {
                        startTime = TimeUtil.getExtraDate(proposeEndTime, 1);
                        Date date = (Date)startTime.clone();
                        endTime = TimeUtil.getExtraDate(date,
                            Integer.valueOf(plan.getWorkTime()) - 1);
                    }

                    if ((plan.getPlanStartTime().getTime() < startTime.getTime())) {
                        plan.setPlanStartTime(startTime);
                        plan.setPlanEndTime(endTime);
                    }
                    namePlanMap.put(info.getCellName(), plan);
                    for (Plan p : insertPlans) {
                        if (plan.getId().equals(p.getId())) {
                            p = plan;
                            break;
                        }
                    }

                    boolean isPreposePlanExist = false;
                    for (PreposePlan preposePlan : insertPreposePlans) {
                        if (preposePlan.getPlanId().equals(plan.getId())
                            && preposePlan.getPreposePlanId().equals(prepose.getId())) {
                            isPreposePlanExist = true;
                            break;
                        }
                    }
                    if (!isPreposePlanExist) {
                        PreposePlan preposePlan = new PreposePlan();
                        preposePlan.setId(UUIDGenerator.generate().toString());
                        preposePlan.setPlanId(plan.getId());
                        preposePlan.setPreposePlanId(prepose.getId());
                        insertPreposePlans.add(preposePlan);
                    }

                    // 保存其输入
                    List<TaskCellDeliverItemVO> inputItems = itemsInputMap.get(conn.getTargetId());
                    if (!CommonUtil.isEmpty(inputItems)) {
                        for (TaskCellDeliverItemVO inputItem : inputItems) {
                            Inputs input = new Inputs();
                            input.setId(UUIDGenerator.generate().toString());
                            templateInputIdAndInputIdMap.put(inputItem.getId(), input.getId());
                            input.setName(inputItem.getDeliverName());
                            input.setUseObjectId(plan.getId());
                            input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                            input.setRequired(inputItem.getIsOutputDeliverRequired());
                            // 内部输入：
                            if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                && PlanConstants.INNERTASK_EN.equals(inputItem.getOriginType())) {
                                input.setOriginType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                input.setOriginTypeExt(PlanConstants.INNERTASK_EN);
                            }
                            // 外部输入：
                            if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                && PlanConstants.DELIEVER_EN.equals(inputItem.getOriginType())) {
                                input.setOriginTypeExt(PlanConstants.DELIEVER_EN);
                            }
                            // 本地上传输入：
                            if (!CommonUtil.isEmpty(inputItem.getOriginType())
                                && PlanConstants.LOCAL_EN.equals(inputItem.getOriginType())) {
                                input.setOriginType(PlanConstants.LOCAL_EN);
                                input.setDocId(inputItem.getFilePath());
                                input.setDocName(inputItem.getFileName());
                            }
                            // if (prepose.getCellId().equals(inputItem.getOriginObjectId())) {
                            // input.setOriginDeliverablesInfoId(deliverItemIdOutputIdMap.get(inputItem.getFromDeliverId()));
                            // input.setOriginObjectId(prepose.getId());
                            // }
                            input.setOriginObjectId(inputItem.getOriginObjectId());
                            // boolean isExist = false;
                            // for (Inputs in : insertInputs) {
                            // if (!CommonUtil.isEmpty(in) && !CommonUtil.isEmpty(input)
                            // && !CommonUtil.isEmpty(input.getUseObjectId())
                            // && !CommonUtil.isEmpty(input.getName())) {
                            // if (input.getUseObjectId().equals(in.getUseObjectId())
                            // && input.getName().equals(in.getName())) {
                            // isExist = true;
                            // if (CommonUtil.isEmpty(in.getOriginDeliverablesInfoId())) {
                            // in.setOriginDeliverablesInfoId(input.getOriginDeliverablesInfoId());
                            // in.setOriginObjectId(input.getOriginObjectId());
                            // }
                            // break;
                            // }
                            // }
                            // }
                            // if (!isExist) {
                            // insertInputs.add(input);
                            // }
                        }
                    }
                    List<TaskProcTemCellConnectVO> backConns = connMap.get(conn.getTargetId());

                    paramMap.put("prepose", plan);
                    paramMap.put("parentWorkTime", parentWorkTime);
                    paramMap.put("maxPlanNumber", maxPlanNumber);
                    paramMap.put("maxStoreyNo", maxStoreyNo);
                    paramMap.put("insertPlans", insertPlans);
                    paramMap.put("insertPlanLogs", insertPlanLogs);
                    paramMap.put("insertInputs", insertInputs);
                    paramMap.put("insertOutputs", insertOutputs);
                    paramMap.put("insertPreposePlans", insertPreposePlans);
                    paramMap.put("backPlans", backConns);
                    templateResolveWithBackPlan(standardNameMap, paramMap, connMap, cellInfoMap,
                        namePlanMap, infoIdPlanIdMap, deliverItemIdOutputIdMap, itemsInputMap,
                        itemsOutputMap, inputNameAndFromIdOutputIdMap, deliverItemIdPlanIdMap,
                        templateInputIdAndInputIdMap, templateOutputIdAndOutputIdMap,
                        templateBasicIdAndPlanIdMap);

                }
            }
        }
    }
    

    /**
     * 将flowResolveXml中的参考工期改为实际工期
     * 
     * @param flowResolveXml
     * @param cellWorkTimeMap
     * @return
     */
    @Override
    public String refreshFlowResolveXml(String flowResolveXml, Map<String, String> cellWorkTimeMap) {
        try {
            SAXReader reader = new SAXReader();
            InputStream in = new ByteArrayInputStream(flowResolveXml.getBytes("UTF-8"));
            Document document = reader.read(in);
            Element root = document.getRootElement();
            List<Element> nodeRoots = root.elements("root");
            if (nodeRoots != null && nodeRoots.size() >= 1) {
                Element nodeRoot = nodeRoots.get(0);
                List<Element> nodes = nodeRoot.elements("Task");
                for (Iterator it = nodes.iterator(); it.hasNext();) {
                    org.dom4j.Element elm = (org.dom4j.Element)it.next();
                    String cellId = elm.attributeValue("id");
                    if (StringUtil.isNotEmpty(cellWorkTimeMap.get(cellId))) {
                        String cellNameInfo = cellWorkTimeMap.get(cellId);
                        elm.setAttributeValue("label", cellNameInfo);
                    }
                }

            }
            flowResolveXml = document.asXML();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flowResolveXml;
    }
}
