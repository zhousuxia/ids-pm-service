package com.glaway.ids.project.plan.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.service.FeignSystemService;
import com.glaway.foundation.fdk.dev.service.calendar.FeignCalendarService;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.ids.config.constant.*;
import com.glaway.ids.config.dto.RDTaskVO;
import com.glaway.ids.config.entity.ParamSwitch;
import com.glaway.ids.config.entity.RepFile;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.ActivityTypeManageEntityServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.*;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjDocVo;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import com.glaway.ids.util.ProcessUtil;
import org.apache.commons.beanutils.BeanUtils;
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
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


/**
 * 交付物信息
 *
 * @author blcao
 * @version 2015年4月1日
 * @see taskFlowResolveServiceImpl
 * @since
 */
@Service("taskFlowResolveService")
@Transactional(propagation = Propagation.REQUIRED)
public class taskFlowResolveServiceImpl extends CommonServiceImpl implements TaskFlowResolveServiceI {

    /**
     *
     */
    private static final SystemLog log = BaseLogFactory.getSystemLog(taskFlowResolveServiceImpl.class);

    /**
     * sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private WorkFlowFacade workFlowFacade;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * planService
     */
    @Autowired
    private PlanServiceI planService;

    /**
     * inputsService
     */
    @Autowired
    private InputsServiceI inputsService;

    /**
     * 项目角色人员服务实现接口<br>
     */
    @Autowired
    private ProjRoleServiceI projRoleService;

    /**
     * 名称库<br>
     */
    @Autowired
    private NameStandardRemoteFeignServiceI nameStandardService;

    /**
     *
     */
    @Autowired
    private PreposePlanServiceI preposePlanService;

    /**
     *
     */
    @Autowired
    private FeignRepService repFileService;

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
     * 任务资源接口
     */
    @Autowired
    private ResourceLinkInfoRemoteFeignServiceI resourceLinkInfoService;

    /**
     * 项目计划参数接口
     */
    @Autowired
    private ParamSwitchServiceI paramSwitchService;

    /**
     *
     */
    @Autowired
    private NameStandardRemoteFeignServiceI nameStandardDeliveryRelationService;

    @Autowired
    private FeignSystemService feignSystemService;

    /**
     * 流程模板接口
     */
    @Autowired
    private RdFlowTaskFlowResolveRemoteFeignServiceI rdFlowTaskFlowResolveService;

    /**
     * 资源接口
     */
    @Autowired
    private ResourceRemoteFeignServiceI resourceService;

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

    /**
     *交付项
     */
    @Autowired
    private  DeliveryStandardRemoteFeignServiceI deliveryStandardService;

    @Autowired
    private Environment env;

    @Autowired
    private ActivityTypeManageEntityServiceI activityTypeManageEntityService;

    @Override
    public List<FlowTaskVo> getChangeFlowTaskList(FlowTaskParentVo parent ,String userId){
        List<FlowTaskVo> list = new ArrayList<FlowTaskVo>();
        Plan p = new Plan();
        p.setParentPlanId(parent.getParentId());
        List<Plan> childList = planService.queryPlanList(p, 1, 10, false);
//        List<NameStandard> nameStandards = sessionFacade.findHql(
//            "from NameStandard where name in ( select planName from Plan where parentPlanId = ? and avaliable = '1' ) ",
//            parent.getParentId());
        List<NameStandardDto> nameStandards = nameStandardService.searchNameStandardsExceptDesign(new NameStandardDto());
        Map<String, String> nameStandardMap = new HashMap<String, String>();
        for (NameStandardDto n : nameStandards) {
            nameStandardMap.put(n.getName(), n.getId());
        }
        List<DeliveryStandardDto> deliveryStandards = nameStandardService.searchDeliveryStandards(new DeliveryStandardDto());
        Map<String, String> deliveryStandardMap = new HashMap<String, String>();
        for (DeliveryStandardDto d : deliveryStandards) {
            deliveryStandardMap.put(d.getName(), d.getId());
        }
        Map<String, String> originObjectMap = new HashMap<String, String>();
        Map<String, String> originDeliverablesInfoMap = new HashMap<String, String>();
        Map<String, String> inputOriginMap = new HashMap<String, String>();
        Map<String, String> inputOriginObjectMap = new HashMap<String, String>();
        if (childList.size()>0) {
            p = childList.get(0);
        }
        FeignJson libIdJson ;
        String libId = "";
        if (!CommonUtil.isEmpty(p)) {
            String projectId = p.getProjectId();
            if (!CommonUtil.isEmpty(projectId)) {
                libId =projRoleService.getLibIdByProjectId(projectId);
            }
        }

        Map<String, String> fileNameMap = new HashMap<String, String>();

        Map<String, String> filePathMap = new HashMap<String, String>();

        Map<String, String> fileIdMap = new HashMap<String, String>();

        if (!CommonUtil.isEmpty(libId)) {
            fileNameMap = inputsService.getRepFileNameAndBizIdMap(libId);

            filePathMap = inputsService.getRepFilePathAndBizIdMap(libId);

            fileIdMap = inputsService.getRepFileIdAndBizIdMap(libId);
        }

        for (Plan child : childList) {
            if (!PlanConstants.PLAN_INVALID.equals(child.getBizCurrent())) {
                FlowTaskVo flowTask = new FlowTaskVo();
                String uuid = PlanConstants.PLAN_CREATE_UUID + UUID.randomUUID().toString();
                flowTask.setId(uuid);
                flowTask.setPlanId(child.getId());
                originObjectMap.put(child.getId(), uuid);
                flowTask.setPlanName(child.getPlanName());
                flowTask.setPlanNumber(child.getPlanNumber());
                flowTask.setAssigner(child.getAssigner());
                flowTask.setAssignerInfo(child.getAssignerInfo());
                flowTask.setAssignTime(child.getAssignTime());
                flowTask.setBeforePlanId(child.getBeforePlanId());
                flowTask.setBizCurrent(child.getBizCurrent());
                flowTask.setCellId(child.getCellId());
                flowTask.setCreateBy(child.getCreateBy());
                flowTask.setCreator(child.getCreator());
                flowTask.setDocuments(child.getDocuments());
                flowTask.setFeedbackProcInstId(child.getFeedbackProcInstId());
                flowTask.setFeedbackRateBefore(child.getFeedbackRateBefore());
                flowTask.setFlowResolveXml(child.getFlowResolveXml());
                flowTask.setFlowStatus(child.getFlowStatus());
                flowTask.setFromTemplate(child.getFromTemplate());
                flowTask.setLauncher(child.getLauncher());
                flowTask.setLauncherInfo(child.getLauncherInfo());
                flowTask.setLaunchTime(child.getLaunchTime());
                flowTask.setMilestone(child.getMilestone());
                flowTask.setOpContent(child.getOpContent());
                flowTask.setOwner(child.getOwner());
                flowTask.setOwnerInfo(child.getOwnerInfo());
                flowTask.setCreateOrgId(child.getCreateOrgId());
                //TODO..
//                flowTask.setParentPlan(child.getParentPlan());
                flowTask.setParentPlanId(parent.getId());
                flowTask.setPlanLevel(child.getPlanLevel());
                //TODO..
//                flowTask.setPlanLevelInfo(child.getPlanLevelInfo());
                flowTask.setPlanOrder(child.getPlanOrder());
                flowTask.setPlanSource(child.getPlanSource());
                flowTask.setPlanStartTime(child.getPlanStartTime());
                flowTask.setPlanEndTime(child.getPlanEndTime());
                flowTask.setPlanType(child.getPlanType());
                flowTask.setProgressRate(child.getProgressRate());
                //TODO..
//                flowTask.setProject(child.getProject());
                flowTask.setProjectId(child.getProjectId());
                flowTask.setProjectStatus(child.getProjectStatus());
                flowTask.setRemark(child.getRemark());
                flowTask.setRequired(child.getRequired());
                flowTask.setStoreyNo(child.getStoreyNo());
                flowTask.setWorkTime(child.getWorkTime());
                flowTask.setWorkTimeReference(child.getWorkTimeReference());
                flowTask.setTaskNameType(child.getTaskNameType());
                flowTask.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                flowTask.setTabCbTemplateId(child.getTabCbTemplateId());
                if (!CommonUtil.isEmpty(nameStandardMap)
                    && !CommonUtil.isEmpty(nameStandardMap.get(child.getPlanName()))) {
                    flowTask.setNameStandardId(nameStandardMap.get(child.getPlanName()));
                }
                // flowTask.setTaskType(child.getTaskType());

                List<PreposePlan> preposelist = preposePlanService.getPreposePlansByPlanId(child);
                getChangeFlowTaskPreposeList(child);
                String preposeIds = "";
                String preposePlans = "";
                for (PreposePlan prepose : preposelist) {
                    FlowTaskPreposeVo flowTaskPreposeVo = new FlowTaskPreposeVo();
                    flowTaskPreposeVo.setId(PlanConstants.PLAN_CREATE_UUID
                                         + UUID.randomUUID().toString());
                    Plan preposePlan = null;
                    if (prepose.getPreposePlanInfo() != null) {
                        preposePlan = prepose.getPreposePlanInfo();
                    }
                    else {
                        preposePlan = (Plan)sessionFacade.getEntity(Plan.class, prepose.getPreposePlanId());
                    }
                    if (preposePlan != null) {
                        flowTaskPreposeVo.setPreposeId(preposePlan.getId());
                        flowTaskPreposeVo.setFlowTaskId(flowTask.getId());
                        if (!child.getParentPlanId().equals(preposePlan.getParentPlanId())) {
                            String id = prepose.getPreposePlanId();
                            String name = preposePlan.getPlanName();
                            if (preposeIds.equals("")) {
                                preposeIds = id;
                                preposePlans = name;
                            }
                            else {
                                preposeIds = preposeIds + "," + id;
                                preposePlans = preposePlans + "," + name;
                            }
                        }
                        flowTask.getFlowTaskPreposeList().add(flowTaskPreposeVo);
                    }
                }
                flowTask.setPreposeIds(preposeIds);
                flowTask.setPreposePlans(preposePlans);

                Inputs inputs = new Inputs();
                inputs.setUseObjectId(child.getId());
                inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                List<Inputs> inputList = inputsService.queryInputsDetailList(inputs);
                for (Inputs in : inputList) {
                    FlowTaskInputsVo flowTaskInputs = new FlowTaskInputsVo();
                    flowTaskInputs.setId(PlanConstants.PLAN_CREATE_UUID
                                         + UUID.randomUUID().toString());
                    flowTaskInputs.setName(in.getName());
                    //TODO..
//                    flowTaskInputs.setDocument(in.getDocument());
                    flowTaskInputs.setOriginType(in.getOriginType());
                    flowTaskInputs.setOriginTypeExt(in.getOriginTypeExt());
                    flowTaskInputs.setFileId(in.getFileId());
                    flowTaskInputs.setInputId(in.getId());
                    flowTaskInputs.setOrigin(in.getOrigin());
                    flowTaskInputs.setRequired(in.getRequired());
                    flowTaskInputs.setUseObjectId(uuid);
                    flowTaskInputs.setUseObjectType(in.getUseObjectType());
                    flowTaskInputs.setDocId(in.getDocId());
                    flowTaskInputs.setDocName(in.getDocName());
                    flowTaskInputs.setOriginDeliverablesInfoId(in.getOriginDeliverablesInfoId());
                    flowTaskInputs.setOriginDeliverablesInfoName(in.getOriginDeliverablesInfoName());
                    flowTaskInputs.setOriginObjectId(in.getOriginObjectId());
                    flowTaskInputs.setOriginObjectName(in.getOriginObjectName());
                    flowTaskInputs.setCreateBy(in.getCreateBy());
                    flowTaskInputs.setCreateFullName(in.getCreateFullName());
                    flowTaskInputs.setCreateName(in.getCreateName());
                    flowTaskInputs.setCreateTime(in.getCreateTime());
                    if (!CommonUtil.isEmpty(deliveryStandardMap)
                        && !CommonUtil.isEmpty(deliveryStandardMap.get(flowTaskInputs.getName()))) {
                        flowTaskInputs.setDeliverId(deliveryStandardMap.get(flowTaskInputs.getName()));
                    }

                    if (StringUtils.isNotEmpty(in.getDocId())) {
                        RepFileDto r = repFileService.getRepFileByRepFileId(appKey, in.getDocId());
                        if (r != null) {
                            flowTaskInputs.setSecurityLevel(r.getSecurityLevel());
                            String havePower = planService.getOutPower(in.getDocId(),
                                in.getUseObjectId(), userId);
                            if ("downloadDetail".equals(havePower)) {
                                flowTaskInputs.setDownload(true);
                                flowTaskInputs.setDetail(true);
                                flowTaskInputs.setHavePower(true);
                            }
                            else if ("detail".equals(havePower)) {
                                flowTaskInputs.setDownload(false);
                                flowTaskInputs.setDetail(true);
                                flowTaskInputs.setHavePower(true);
                            }
                            else {
                                flowTaskInputs.setDownload(false);
                                flowTaskInputs.setDetail(false);
                                flowTaskInputs.setHavePower(false);
                            }
                        }
                    }

                    // 外部输入挂接项目库的数据获取：
                    if (!CommonUtil.isEmpty(flowTaskInputs.getOriginType())
                        && PlanConstants.PROJECTLIBDOC.equals(flowTaskInputs.getOriginType())) {
                        if (!CommonUtil.isEmpty(fileNameMap.get(flowTaskInputs.getDocId()))) {
                            flowTaskInputs.setDocName(fileNameMap.get(flowTaskInputs.getDocId()));
                        }
                        if (!CommonUtil.isEmpty(filePathMap.get(flowTaskInputs.getDocId()))) {
                            flowTaskInputs.setOriginObjectName(filePathMap.get(flowTaskInputs.getDocId()));
                        }
                        if (!CommonUtil.isEmpty(fileIdMap.get(flowTaskInputs.getDocId()))) {
                            flowTaskInputs.setDocIdShow(fileIdMap.get(flowTaskInputs.getDocId()));
                        }
                    }
                    else if ((!CommonUtil.isEmpty(flowTaskInputs.getOriginType()) && flowTaskInputs.getOriginType().equals(
                        "PLAN"))
                             && (!CommonUtil.isEmpty(flowTaskInputs.getOriginTypeExt()) && flowTaskInputs.getOriginTypeExt().equals(
                                 PlanConstants.DELIEVER_EN))) {
                        Plan curPlan = (Plan)sessionFacade.getEntity(Plan.class,
                            flowTaskInputs.getOriginObjectId());
                        // 外部输入挂接计划的数据获取：
                        List<ProjDocVo> projDocRelationList = new ArrayList<ProjDocVo>();
                        if (!CommonUtil.isEmpty(curPlan)) {
                            projDocRelationList = inputsService.getDocRelationList(curPlan,
                                userId);
                        }
                        ProjDocVo projDoc = new ProjDocVo();
                        if (!CommonUtil.isEmpty(projDocRelationList)) {
                            for (ProjDocVo vo : projDocRelationList) {
                                if (vo.getDeliverableId().equals(
                                    flowTaskInputs.getOriginDeliverablesInfoId())) {
                                    projDoc = vo;
                                    break;
                                }
                            }
                        }
                        if (!CommonUtil.isEmpty(curPlan)) {
                        flowTaskInputs.setOriginObjectName(curPlan.getPlanNumber() + "."
                                                           + curPlan.getPlanName());
                        }
                        flowTaskInputs.setDocId(projDoc.getDocId());
                        flowTaskInputs.setDocName(projDoc.getDocName());
                        flowTaskInputs.setExt1(String.valueOf(projDoc.isDownload()));
                        flowTaskInputs.setExt2(String.valueOf(projDoc.isHavePower()));
                        flowTaskInputs.setExt3(String.valueOf(projDoc.isDetail()));
                    }

                    inputOriginMap.put(in.getId(), in.getOriginDeliverablesInfoId());
                    inputOriginObjectMap.put(in.getId(), in.getOriginObjectId());
                    flowTask.getInputList().add(flowTaskInputs);
                }

                DeliverablesInfo outputs = new DeliverablesInfo();
                outputs.setUseObjectId(child.getId());
                outputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                List<DeliverablesInfo> outputList = deliverablesInfoService.queryDeliverableList(
                    outputs, 1, 10, false);
                for (DeliverablesInfo out : outputList) {
                    FlowTaskDeliverablesInfoVo flowTaskOutputs = new FlowTaskDeliverablesInfoVo();
                    flowTaskOutputs.setId(PlanConstants.PLAN_CREATE_UUID
                                          + UUID.randomUUID().toString());
                    flowTaskOutputs.setName(out.getName());
                    //TODO..
//                    flowTaskOutputs.setDocument(out.getDocument());
                    flowTaskOutputs.setFileId(out.getFileId());
                    flowTaskOutputs.setOutputId(out.getId());
                    flowTaskOutputs.setOrigin(out.getOrigin());
                    flowTaskOutputs.setRequired(out.getRequired());
                    flowTaskOutputs.setUseObjectId(uuid);
                    flowTaskOutputs.setUseObjectType(out.getUseObjectType());
                    if (!CommonUtil.isEmpty(deliveryStandardMap)
                        && !CommonUtil.isEmpty(deliveryStandardMap.get(flowTaskOutputs.getName()))) {
                        flowTaskOutputs.setDeliverId(deliveryStandardMap.get(flowTaskOutputs.getName()));
                    }
                    originDeliverablesInfoMap.put(flowTaskOutputs.getOutputId(),
                        flowTaskOutputs.getId());
                    flowTask.getOutputList().add(flowTaskOutputs);
                }

                ResourceLinkInfoDto resourceLinkInfo = new ResourceLinkInfoDto();
                resourceLinkInfo.setUseObjectId(child.getId());
                resourceLinkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                List<ResourceLinkInfoDto> resourceLinkList = resourceLinkInfoService.queryResourceList(
                    resourceLinkInfo, 1, 10, false);
                for (ResourceLinkInfoDto resource : resourceLinkList) {
                    FlowTaskResourceLinkInfoVo flowTaskResourceInfo = new FlowTaskResourceLinkInfoVo();
                    flowTaskResourceInfo.setId(PlanConstants.PLAN_CREATE_UUID
                                               + UUID.randomUUID().toString());
                    flowTaskResourceInfo.setLinkInfoId(resource.getId());
                    flowTaskResourceInfo.setResourceId(resource.getResourceId());
                    ResourceDto o = (ResourceDto) CodeUtils.JsonBeanToBean(resource.getResourceInfo(), ResourceDto.class);
                    flowTaskResourceInfo.setResourceInfo(o);
                    if (resource.getResourceInfo() != null) {
                        flowTaskResourceInfo.setResourceName(resource.getResourceInfo().getName());
                    }
                    flowTaskResourceInfo.setResourceType(resource.getResourceType());
                    flowTaskResourceInfo.setUseObjectId(uuid);
                    flowTaskResourceInfo.setUseObjectType(resource.getUseObjectType());
                    flowTaskResourceInfo.setStartTime(resource.getStartTime());
                    flowTaskResourceInfo.setEndTime(resource.getEndTime());
                    flowTaskResourceInfo.setUseRate(resource.getUseRate());
                    flowTask.getResourceLinkList().add(flowTaskResourceInfo);
                }
                list.add(flowTask);
            }
        }
        for (FlowTaskVo task : list) {
            if (!CommonUtil.isEmpty(task.getInputList())) {
                for (FlowTaskInputsVo in : task.getInputList()) {
                    if (StringUtils.isNotEmpty(inputOriginObjectMap.get(in.getInputId()))) {
                        if (StringUtils.isNotEmpty(originObjectMap.get(in.getOriginObjectId()))) {
                            in.setOriginObjectId(originObjectMap.get(in.getOriginObjectId()));
                        }
                    }
                    if (StringUtils.isNotEmpty(inputOriginMap.get(in.getInputId()))) {
                        if (StringUtils.isNotEmpty(originDeliverablesInfoMap.get(in.getOriginDeliverablesInfoId()))) {
                            in.setOriginDeliverablesInfoId(originDeliverablesInfoMap.get(in.getOriginDeliverablesInfoId()));
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public List<FlowTaskPreposeVo> getChangeFlowTaskPreposeList(Plan plan)
        throws GWException {
        List<FlowTaskPreposeVo> list = new ArrayList<FlowTaskPreposeVo>();
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            StringBuffer hqlBuffer = new StringBuffer();
            hqlBuffer.append("select a.id,a.flowtaskid,a.preposeid,a.formid , ");
            hqlBuffer.append(" a.parentPlanId from (select t.id,t.flowtaskid,t.preposeid,t.formid, p.parentPlanId from PM_FLOWTASK_PREPOSE t,");
            hqlBuffer.append(" PM_FLOWTASK p where t.flowTaskid = p.id) a, (select t.id, p.parentPlanId from PM_FLOWTASK_PREPOSE t, PM_FLOWTASK p where t.preposeId = p.id) b ");
            hqlBuffer.append(" where a.id = b.id and a.parentPlanId = '" + plan.getParentPlanId()
                             + "' and b.parentPlanId = '" + plan.getParentPlanId() + "'");
            List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
            if (!CommonUtil.isEmpty(objArrayList)) {
                for (Map<String, Object> map : objArrayList) {
                    String id = (String)map.get("id");
                    if (StringUtils.isNotEmpty(id)) {
                        FlowTaskPreposeVo iitem = new FlowTaskPreposeVo();
                        iitem.setId(id);
                        iitem.setFlowTaskId((String)map.get("flowtaskid"));
                        iitem.setFormId((String)map.get("formid"));
                        iitem.setPreposeId((String)map.get("preposeid"));
                        list.add(iitem);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<FlowTaskVo> getChangeFlowTaskList(Plan plan, String userId) throws GWException {
        // String hql = "from FlowTask f  where  1 = 1 ";
        // if (!StringUtils.isEmpty(plan.getFormId())) {
        // hql = hql + " and f.formId = '" + plan.getFormId() + "'";
        // }
        // if (!StringUtils.isEmpty(plan.getParentPlanId())) {
        // hql = hql + " and f.parentPlanId = '" + plan.getParentPlanId() + "'";
        // }
        // Plan p = new Plan();
        // p.setParentPlanId(plan.getParentPlanId());
        // p.setProjectId(plan.getProjectId());
        // List<Plan> planList = planService.queryPlanList(p, 1, 10, false);

        Map<String, String> deliveryStandardMap = new HashMap<String, String>();
        String planParentIdOld = planService.getPlanIdByFlowTaskParentId(plan.getParentPlanId());
        String libId = "";
        if (!CommonUtil.isEmpty(planParentIdOld)) {
            Plan parentOld = (Plan) sessionFacade.getEntity(Plan.class, planParentIdOld);
            String projectId = parentOld.getProjectId();
            libId = projRoleService.getLibIdByProjectId(projectId);
            List<DeliveryStandard> deliveryStandards = sessionFacade.findHql("from DeliveryStandard ");
            for (DeliveryStandard d : deliveryStandards) {
                deliveryStandardMap.put(d.getName(), d.getId());
            }
        }

        Map<String, String> fileNameMap = new HashMap<String, String>();

        Map<String, String> filePathMap = new HashMap<String, String>();

        Map<String, String> fileIdMap = new HashMap<String, String>();

        if (!CommonUtil.isEmpty(libId)) {
            fileNameMap = inputsService.getRepFileNameAndBizIdMap(libId);

            filePathMap = inputsService.getRepFilePathAndBizIdMap(libId);

            fileIdMap = inputsService.getRepFileIdAndBizIdMap(libId);
        }

        Map<String, String> namesMap = new HashMap<String, String>();
        Map<String, String> originObejectNameMap = new HashMap<String, String>();
        // for (Plan pl : planList) {
        // namesMap.put(pl.getId(), pl.getPlanName());
        // originObejectNameMap.put(pl.getId(), pl.getPlanName());// 外部前置
        // }
        List<FlowTaskVo> list = getFlowTaskVoList(plan);
        for (FlowTaskVo f : list) {
            originObejectNameMap.put(f.getId(), f.getPlanName());
        }
        for (FlowTaskVo f : list) {
            FlowTaskInputsVo in = new FlowTaskInputsVo();
            in.setUseObjectId(f.getId());
            // in.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            List<FlowTaskInputsVo> inputList = getFlowTaskInputsVo(in);
            if (!CommonUtil.isEmpty(inputList)) {
                for (FlowTaskInputsVo input : inputList) {
                    input.setOriginObjectName(originObejectNameMap.get(input.getOriginObjectId()));
                    if (StringUtils.isNotEmpty(input.getDocId())) {
                        RepFile r = (RepFile) sessionFacade.getEntity(RepFile.class, input.getDocId());
                        if (r != null) {
                            input.setSecurityLevel(r.getSecurityLevel());
                            String old = "";
                            if (!CommonUtil.isEmpty(input.getUseObjectId())) {
                                old = planService.getPlanIdByUseObjectId(input.getUseObjectId());
                            }
                            if (!CommonUtil.isEmpty(old)) {
                                String havePower = planService.getOutPower(input.getDocId(), old,
                                        userId);
                                if ("downloadDetail".equals(havePower)) {
                                    input.setDownload(true);
                                    input.setDetail(true);
                                    input.setHavePower(true);
                                }
                                else if ("detail".equals(havePower)) {
                                    input.setDownload(false);
                                    input.setDetail(true);
                                    input.setHavePower(true);
                                }
                                else {
                                    input.setDownload(false);
                                    input.setDetail(false);
                                    input.setHavePower(false);
                                }
                            }
                        }
                    }

                    // 外部输入挂接项目库的数据获取：
                    if (!CommonUtil.isEmpty(input.getOriginType())
                            && PlanConstants.PROJECTLIBDOC.equals(input.getOriginType())) {
                        if (!CommonUtil.isEmpty(fileNameMap.get(input.getDocId()))) {
                            input.setDocName(fileNameMap.get(input.getDocId()));
                        }
                        if (!CommonUtil.isEmpty(filePathMap.get(input.getDocId()))) {
                            input.setOriginObjectName(filePathMap.get(input.getDocId()));
                        }
                        if (!CommonUtil.isEmpty(fileIdMap.get(input.getDocId()))) {
                            input.setDocIdShow(fileIdMap.get(input.getDocId()));
                        }
                    }
                    else if ((!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals(
                            "PLAN"))
                            && (!CommonUtil.isEmpty(input.getOriginTypeExt()) && input.getOriginTypeExt().equals(
                            PlanConstants.DELIEVER_EN))) {
                        Plan curPlan = (Plan) sessionFacade.getEntity(Plan.class, input.getOriginObjectId());
                        // 外部输入挂接计划的数据获取：
                        List<ProjDocVo> projDocRelationList = new ArrayList<ProjDocVo>();
                        if (!CommonUtil.isEmpty(curPlan)) {
                            projDocRelationList = inputsService.getDocRelationList(curPlan, userId);
                        }
                        ProjDocVo projDoc = new ProjDocVo();
                        if (!CommonUtil.isEmpty(projDocRelationList)) {
                            for (ProjDocVo vo : projDocRelationList) {
                                if (vo.getDeliverableId().equals(
                                        input.getOriginDeliverablesInfoId())) {
                                    projDoc = vo;
                                    break;
                                }
                            }
                        }
                        if(!CommonUtil.isEmpty(curPlan)){
                            input.setOriginObjectName(curPlan.getPlanNumber() + "."
                                    + curPlan.getPlanName());
                        }
                        input.setDocId(projDoc.getDocId());
                        input.setDocName(projDoc.getDocName());
                        input.setExt1(String.valueOf(projDoc.isDownload()));
                        input.setExt2(String.valueOf(projDoc.isHavePower()));
                        input.setExt3(String.valueOf(projDoc.isDetail()));
                    }
                    else if ((!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals(
                            PlanConstants.LOCAL_EN))) {
                        input.setOriginObjectName(PlanConstants.LOCAL);
                    }
                }
                f.getInputList().addAll(inputList);

            }
            FlowTaskDeliverablesInfoVo out = new FlowTaskDeliverablesInfoVo();
            out.setUseObjectId(f.getId());
            // out.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            List<FlowTaskDeliverablesInfoVo> outputList = getFlowTaskDeliverablesInfoVo(out);
            if (!CommonUtil.isEmpty(outputList)) {
                for (FlowTaskDeliverablesInfoVo flowTaskOutputs : outputList) {
                    if (!CommonUtil.isEmpty(deliveryStandardMap)
                            && !CommonUtil.isEmpty(deliveryStandardMap.get(flowTaskOutputs.getName()))) {
                        flowTaskOutputs.setDeliverId(deliveryStandardMap.get(flowTaskOutputs.getName()));
                    }
                }
                f.getOutputList().addAll(outputList);
            }
            FlowTaskResourceLinkInfoVo resource = new FlowTaskResourceLinkInfoVo();
            resource.setUseObjectId(f.getId());
            // resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            List<FlowTaskResourceLinkInfoVo> resourceList = queryResourceListForChange(resource, 1, 10,
                    false);
            if (!CommonUtil.isEmpty(resourceList)) {
                f.getResourceLinkList().addAll(resourceList);
            }
            String preposeIds = "";
            String preposePlans = "";
            List<FlowTaskPreposeVo> flowTaskPreposes = getFlowTaskPreposeVo(f);
            for (FlowTaskPreposeVo prepose : flowTaskPreposes) {
                if (!prepose.getPreposeId().startsWith(PlanConstants.PLAN_CREATE_UUID)) {
                    if (StringUtils.isNotEmpty(preposeIds)) {
                        if (StringUtils.isNotEmpty(prepose.getPreposeId())) {
                            Plan curPrepose = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPreposeId());
                            if(!CommonUtil.isEmpty(curPrepose)) {
                                preposeIds = preposeIds + "," + prepose.getPreposeId();
                                preposePlans = preposePlans + ","
                                        + curPrepose.getPlanName();
                            }
                        }
                    }
                    else {
                        if (StringUtils.isNotEmpty(prepose.getPreposeId())) {
                            Plan curPrepose = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPreposeId());
                            if(!CommonUtil.isEmpty(curPrepose)) {
                                preposeIds = prepose.getPreposeId();
                                preposePlans = curPrepose.getPlanName();
                            }
                        }
                    }
                }
            }
            f.setPreposeIds(preposeIds);
            f.setPreposePlans(preposePlans);
        }
        return list;
    }


    private List<FlowTaskResourceLinkInfoVo> queryResourceListForChange(FlowTaskResourceLinkInfoVo resource, int page, int rows, boolean isPage) {
        List<FlowTaskResourceLinkInfoVo> list = new ArrayList<>();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select t.id, t.startTime, t.endTime,t.linkInfoId,(select a.name  from cm_resource a where a.id = t.resourceid ) resourceName, t.formId,t.useRate ,t.useObjectId,t.useObjectType,t.resourceId  from PM_FLOWTASK_RESOURCE_LINK_INFO t");
        if(!CommonUtil.isEmpty(resource) && !CommonUtil.isEmpty(resource.getUseObjectId())) {
            hqlBuffer.append(" where  t.useObjectId = '" + resource.getUseObjectId() + "'");
        }
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    FlowTaskResourceLinkInfoVo iitem = new FlowTaskResourceLinkInfoVo();
                    iitem.setId(id);
                    try
                    {
                        iitem.setStartTime((Date)map.get("startTime"));
                    }
                    catch (Exception e)
                    {
                        continue;
                    }
                    try
                    {
                        iitem.setEndTime((Date)map.get("endTime"));
                    }
                    catch (Exception e)
                    {
                        continue;
                    }

                    iitem.setLinkInfoId(StringUtils.isNotEmpty((String)map.get("linkInfoId")) ? (String)map.get("linkInfoId") : "");
                    iitem.setResourceName(StringUtils.isNotEmpty((String)map.get("resourceName")) ? (String)map.get("resourceName") : "");
                    iitem.setUseRate(StringUtils.isNotEmpty((String)map.get("useRate")) ? (String)map.get("useRate") : "");
                    iitem.setUseObjectId(StringUtils.isNotEmpty((String)map.get("useObjectId")) ? (String)map.get("useObjectId") : "");
                    iitem.setUseObjectType(StringUtils.isNotEmpty((String)map.get("useObjectType")) ? (String)map.get("useObjectType") : "");
                    iitem.setResourceId(StringUtils.isNotEmpty((String)map.get("resourceId")) ? (String)map.get("resourceId") : "");
                    list.add(iitem);
                }
            }
        }
        return list;
    }

    /**
     * 获取满足条件FlowTaskPreposeVo的输入信息
     */

    private List<FlowTaskPreposeVo> getFlowTaskPreposeVo(FlowTaskVo task) {
        List<FlowTaskPreposeVo> list = new ArrayList<FlowTaskPreposeVo>();
        if (StringUtils.isNotEmpty(task.getId())) {
            String hql = "select * from PM_FLOWTASK_PREPOSE p where p.flowTaskId = '"
                    + task.getId() + "'";
            if (!StringUtils.isEmpty(task.getFormId())) {
                hql = hql + " and p.formId = '" + task.getFormId() + "'";
            }
            List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
            if (!CommonUtil.isEmpty(objArrayList)) {
                for (Map<String, Object> map : objArrayList) {
                    FlowTaskPreposeVo d = new FlowTaskPreposeVo();
                    d.setId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                    d.setFlowTaskId(StringUtils.isNotEmpty((String)map.get("FLOWTASKID")) ? (String)map.get("FLOWTASKID") : "");
                    d.setFormId(StringUtils.isNotEmpty((String)map.get("FORMID")) ? (String)map.get("FORMID") : "");
                    d.setPreposeId(StringUtils.isNotEmpty((String)map.get("PREPOSEID")) ? (String)map.get("PREPOSEID") : "");
                    if (StringUtils.isNotEmpty(d.getId())) {
                        list.add(d);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取满足条件deliverablesInfoVo的输入信息
     */

    private List<FlowTaskDeliverablesInfoVo> getFlowTaskDeliverablesInfoVo(FlowTaskDeliverablesInfoVo deliverablesInfoVo) {
        String hql = "select * from PM_FLOWTASK_DELIVERABLES_INFO i  where i.useObjectId = '"
                + deliverablesInfoVo.getUseObjectId() + "'";
        if (!StringUtils.isEmpty(deliverablesInfoVo.getUseObjectType())) {
            hql = hql + " and i.useobjecttype = '" + deliverablesInfoVo.getUseObjectType() + "'";
        }
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<FlowTaskDeliverablesInfoVo> list = new ArrayList<FlowTaskDeliverablesInfoVo>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                FlowTaskDeliverablesInfoVo d = new FlowTaskDeliverablesInfoVo();
                d.setId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                d.setName(StringUtils.isNotEmpty((String)map.get("NAME")) ? (String)map.get("NAME") : "");
                d.setUseObjectId(StringUtils.isNotEmpty((String)map.get("USEOBJECTID")) ? (String)map.get("USEOBJECTID") : "");
                d.setUseObjectType(StringUtils.isNotEmpty((String)map.get("USEOBJECTTYPE")) ? (String)map.get("USEOBJECTTYPE") : "");
                d.setRequired(StringUtils.isNotEmpty((String)map.get("REQUIRED")) ? (String)map.get("REQUIRED") : "");
                d.setOutputId(StringUtils.isNotEmpty((String)map.get("OUTPUTID")) ? (String)map.get("OUTPUTID") : "");
                if (StringUtils.isNotEmpty(d.getId()) && StringUtils.isNotEmpty(d.getName())
                        && StringUtils.isNotEmpty(d.getUseObjectId())) {
                    list.add(d);
                }
            }
        }
        return list;
    }

    /**
     * 获取满足条件inputs的输入信息
     */
    private List<FlowTaskInputsVo> getFlowTaskInputsVo(FlowTaskInputsVo inputs) {
        String hql = "select i.createTime, i.createBy, i.createName, i.createFullName,i.inputId,i.id,i.name,i.fileid,i.origin,i.required,i.docid,i.docname,i.useobjectid,i.useobjecttype,i.originobjectid,i.origindeliverablesinfoid,i.origintype,i.origintypeext from PM_FLOWTASK_INPUTS i  where i.useObjectId = '"
                + inputs.getUseObjectId() + "' order by i.createTime asc";
        if (!StringUtils.isEmpty(inputs.getUseObjectType())) {
            hql = hql + " and i.useobjecttype = '" + inputs.getUseObjectType() + "'";
        }
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<FlowTaskInputsVo> list = changeResultsToInputsList(objArrayList);
        return list;
    }

    /**
     * 获取FlowTaskVo的值
     *
     * @param plan
     * @return
     * @see
     */
    private List<FlowTaskVo> getFlowTaskVoList(Plan plan) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select * from PM_FLOWTASK f  where  1 = 1");
        if (!StringUtils.isEmpty(plan.getFormId())) {
            hqlBuffer.append(" and f.formId = '" + plan.getFormId() + "'");
        }
        if (!StringUtils.isEmpty(plan.getParentPlanId())) {
            hqlBuffer.append(" and f.parentPlanId = '" + plan.getParentPlanId() + "'");
        }
        List<FlowTaskVo> flowTaskVoList = new ArrayList<FlowTaskVo>();
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("ID");
                if (StringUtils.isNotEmpty(id)) {
                    FlowTaskVo iitem = new FlowTaskVo();
                    iitem.setId(id);
                    try {
                        iitem.setAssignTime((Date)map.get("ASSIGNTIME"));
                    }
                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    iitem.setAssigner(StringUtils.isNotEmpty((String)map.get("ASSIGNER")) ? (String)map.get("ASSIGNER") : "");
                    iitem.setBizCurrent(StringUtils.isNotEmpty((String)map.get("BIZCURRENT")) ? (String)map.get("BIZCURRENT") : "");
                    iitem.setCellId(StringUtils.isNotEmpty((String)map.get("CELLID")) ? (String)map.get("CELLID") : "");
                    iitem.setChangeRemark(StringUtils.isNotEmpty((String)map.get("CHANGEREMARK")) ? (String)map.get("CHANGEREMARK") : "");
                    iitem.setChangeType(StringUtils.isNotEmpty((String)map.get("CHANGETYPE")) ? (String)map.get("CHANGETYPE") : "");
                    iitem.setFlowResolveXml(StringUtils.isNotEmpty((String)map.get("FLOWRESOLVEXML")) ? (String)map.get("FLOWRESOLVEXML") : "");
                    iitem.setFlowStatus(StringUtils.isNotEmpty((String)map.get("FLOWSTATUS")) ? (String)map.get("FLOWSTATUS") : "");
                    iitem.setFormId(StringUtils.isNotEmpty((String)map.get("FORMID")) ? (String)map.get("FORMID") : "");
                    iitem.setFromTemplate(StringUtils.isNotEmpty((String)map.get("FROMTEMPLATE")) ? (String)map.get("FROMTEMPLATE") : "");
                    try {
                        iitem.setLaunchTime((Date)map.get("LAUNCHTIME"));
                    }
                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    iitem.setLauncher(StringUtils.isNotEmpty((String)map.get("LAUNCHER")) ? (String)map.get("LAUNCHER") : "");
                    iitem.setMilestone(StringUtils.isNotEmpty((String)map.get("MILESTONE")) ? (String)map.get("MILESTONE") : "");
                    iitem.setOpContent(StringUtils.isNotEmpty((String)map.get("OPCONTENT")) ? (String)map.get("OPCONTENT") : "");
                    iitem.setOwner(StringUtils.isNotEmpty((String)map.get("OWNER")) ? (String)map.get("OWNER") : "");
                    iitem.setParentPlanId(StringUtils.isNotEmpty((String)map.get("PARENTPLANID")) ? (String)map.get("PARENTPLANID") : "");
                    try {
                        iitem.setPlanEndTime((Date)map.get("PLANENDTIME"));
                    }
                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    iitem.setPlanId(StringUtils.isNotEmpty((String)map.get("PLANID")) ? (String)map.get("PLANID") : "");
                    iitem.setPlanLevel(StringUtils.isNotEmpty((String)map.get("PLANLEVEL")) ? (String)map.get("PLANLEVEL") : "");
                    iitem.setPlanName(StringUtils.isNotEmpty((String)map.get("PLANNAME")) ? (String)map.get("PLANNAME") : "");
                    String number = map.get("PLANNUMBER").toString();
                    if (!CommonUtil.isEmpty(number)) {
                        iitem.setPlanNumber(Integer.parseInt(number));
                    }
                    try {
                        iitem.setPlanStartTime((Date)map.get("PLANSTARTTIME"));
                    }
                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    iitem.setPlanType(StringUtils.isNotEmpty((String)map.get("PLANTYPE")) ? (String)map.get("PLANTYPE") : "");
                    iitem.setProgressRate(StringUtils.isNotEmpty((String)map.get("PROGRESSRATE")) ? (String)map.get("PROGRESSRATE") : "");
                    iitem.setProjectId(StringUtils.isNotEmpty((String)map.get("PROJECTID")) ? (String)map.get("PROJECTID") : "");
                    iitem.setRemark(StringUtils.isNotEmpty((String)map.get("REMARK")) ? (String)map.get("REMARK") : "");
                    iitem.setRequired(StringUtils.isNotEmpty((String)map.get("REQUIRED")) ? (String)map.get("REQUIRED") : "");
                    String storeyNo = map.get("STOREYNO").toString();
                    if (!CommonUtil.isEmpty(storeyNo)) {
                        iitem.setStoreyNo(Integer.parseInt(storeyNo));
                    }
                    iitem.setTaskNameType(StringUtils.isNotEmpty((String)map.get("TASKNAMETYPE")) ? (String)map.get("TASKNAMETYPE") : "");
                    iitem.setTaskType(StringUtils.isNotEmpty((String)map.get("TASKTYPE")) ? (String)map.get("TASKTYPE") : "");
                    iitem.setWorkTime(StringUtils.isNotEmpty((String)map.get("WORKTIME")) ? (String)map.get("WORKTIME") : "");
                    iitem.setWorkTimeReference(StringUtils.isNotEmpty((String)map.get("WORKTIMEREFERENCE")) ? (String)map.get("WORKTIMEREFERENCE") : "");
                    iitem.setWorkTimeType(StringUtils.isNotEmpty((String)map.get("WORKTIMETYPE")) ? (String)map.get("WORKTIMETYPE") : "");
                    if (!CommonUtil.isEmpty(StringUtils.isNotEmpty((String)map.get("CREATEBY")) ? (String)map.get("CREATEBY") : "")) {
                        iitem.setCreateTime((Date)map.get("CREATETIME"));
                        iitem.setCreateBy(StringUtils.isNotEmpty((String)map.get("CREATEBY")) ? (String)map.get("CREATEBY") : "");
                        iitem.setCreateFullName(StringUtils.isNotEmpty((String)map.get("CREATEFULLNAME")) ? (String)map.get("CREATEFULLNAME") : "");
                        iitem.setCreateName(StringUtils.isNotEmpty((String)map.get("CREATENAME")) ? (String)map.get("CREATENAME") : "");
                    }
                    iitem.setIsChangeSingleBack(StringUtils.isNotEmpty((String)map.get("ISCHANGESINGLEBACK")) ? (String)map.get("ISCHANGESINGLEBACK") : "");
                    iitem.setTabCbTemplateId(StringUtils.isNotEmpty((String)map.get("TABCBTEMPLATEID")) ? (String)map.get("TABCBTEMPLATEID") : "");
                    flowTaskVoList.add(iitem);
                }
            }
        }

        return flowTaskVoList;
    }

    /**
     * 将查询的结果转换为List<Inputs>
     *
     * @param objArrayList
     * @return
     */
    private List<FlowTaskInputsVo> changeResultsToInputsList(List<Map<String, Object>> objArrayList) {
        List<FlowTaskInputsVo> list = new ArrayList<FlowTaskInputsVo>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    FlowTaskInputsVo input = new FlowTaskInputsVo();
                    input.setId(id);
                    input.setName(StringUtils.isNotEmpty((String)map.get("name")) ? (String)map.get("name") : "");
                    input.setFileId(StringUtils.isNotEmpty((String)map.get("fileid")) ? (String)map.get("fileid") : "");
                    input.setOrigin(StringUtils.isNotEmpty((String)map.get("origin")) ? (String)map.get("origin") : "");
                    input.setRequired(StringUtils.isNotEmpty((String)map.get("required")) ? (String)map.get("required") : "");
                    input.setDocId(StringUtils.isNotEmpty((String)map.get("docId")) ? (String)map.get("docId") : "");
                    input.setDocName(StringUtils.isNotEmpty((String)map.get("docname")) ? (String)map.get("docname") : "");
                    input.setUseObjectId(StringUtils.isNotEmpty((String)map.get("useobjectid")) ? (String)map.get("useobjectid") : "");
                    input.setUseObjectType(StringUtils.isNotEmpty((String)map.get("useobjecttype")) ? (String)map.get("useobjecttype") : "");
                    input.setOriginObjectId(StringUtils.isNotEmpty((String)map.get("originObjectId")) ? (String)map.get("originObjectId") : "");
                    input.setOriginObjectName(StringUtils.isNotEmpty((String)map.get("originObjectName")) ? (String)map.get("originObjectName") : "");
                    input.setOriginDeliverablesInfoId(StringUtils.isNotEmpty((String)map.get("originDeliverablesInfoId")) ? (String)map.get("originDeliverablesInfoId") : "");
                    input.setOriginDeliverablesInfoName(StringUtils.isNotEmpty((String)map.get("originDeliverablesInfoName")) ? (String)map.get("originDeliverablesInfoName") : "");
                    input.setOriginType(StringUtils.isNotEmpty((String)map.get("origintype")) ? (String)map.get("origintype") : "");
                    input.setOriginTypeExt(StringUtils.isNotEmpty((String)map.get("origintypeext")) ? (String)map.get("origintypeext") : "");
                    input.setInputId(StringUtils.isNotEmpty((String)map.get("inputId")) ? (String)map.get("inputId") : "");
                    list.add(input);
                }
            }
        }
        return list;
    }

    @Override
    public List<ChangeFlowTaskCellConnectVo> getChangeFlowTaskConnectList(FlowTaskParentVo parent)
        throws GWException {
        List<ChangeFlowTaskCellConnectVo> list = new ArrayList<ChangeFlowTaskCellConnectVo>();
        FlowTaskCellConnectVo FlowTaskCellConnectVo = new FlowTaskCellConnectVo();
        FlowTaskCellConnectVo.setParentPlanId(parent.getParentId());
        List<FlowTaskCellConnectVo> connectList = planService.queryFlowTaskCellConnectList(FlowTaskCellConnectVo);
        for (FlowTaskCellConnectVo con : connectList) {
            ChangeFlowTaskCellConnectVo connect = new ChangeFlowTaskCellConnectVo();
            connect.setId(PlanConstants.PLAN_CREATE_UUID + UUID.randomUUID().toString());
            connect.setCellId(con.getCellId());
            connect.setInfoId(con.getInfoId());
            connect.setParentPlanId(con.getParentPlanId());
            connect.setTargetId(con.getTargetId());
            connect.setTargetInfoId(con.getTargetInfoId());
            list.add(connect);
        }
        return list;
    }

    @Override
    public void updateFlowTasks(Plan temp, String cellIds, String parentPlanId, String cellContact,String userId)
        throws GWException {
        String[] cells = cellIds.split(",");
        Plan plan = new Plan();
        List<Plan> childList = new ArrayList<Plan>();
        plan.setParentPlanId(parentPlanId);
        childList = planService.queryPlanList(plan, 1, 10, false);
        boolean flag = true;
        if (childList.size() > 0) {
            for (Plan child : childList) {
                if (cells.length > 0) {
                    for (int i = 0; i < cells.length; i++ ) {
                        if (child.getCellId().equals(cells[i])) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        // 删除该节点的输入
                        Inputs inputs = new Inputs();
                        inputs.setUseObjectId(child.getId());
                        inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        List<Inputs> inputList = inputsService.queryInputList(inputs, 1, 10, false);
                        for (Inputs input : inputList) {
                            inputsService.delete(input);
                        }
                        // 删除该节点的输出
                        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
                        deliverablesInfo.setUseObjectId(child.getId());
                        deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        List<DeliverablesInfo> outputList = deliverablesInfoService.queryDeliverableList(
                            deliverablesInfo, 1, 10, false);
                        for (DeliverablesInfo output : outputList) {
                            sessionFacade.delete(output);
                        }
                        // 删除该节点输出的传递
                        Plan p = new Plan();
                        p.setPreposeIds(child.getId());
                        List<PreposePlan> postposeList = preposePlanService.getPostposesByPreposeId(p);
                        for (PreposePlan preposePlan : postposeList) {
                            if (StringUtils.isNotEmpty(preposePlan.getPlanId())) {
                                Plan prepose = (Plan)sessionFacade.getEntity(Plan.class,preposePlan.getPlanId());
                                if (prepose != null) {
                                    Inputs postposeIn = new Inputs();
                                    postposeIn.setUseObjectId(prepose.getId());
                                    postposeIn.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                    postposeIn.setOrigin(child.getPlanName());
                                    List<Inputs> postposeInputList = inputsService.queryInputList(
                                        postposeIn, 1, 10, false);
                                    for (Inputs postposeInput : postposeInputList) {
                                        inputsService.delete(postposeInput);
                                    }
                                }
                            }
                            preposePlanService.delete(preposePlan);
                        }
                        // 删除与该节点相关的前后置关系
                        List<PreposePlan> preposeList = preposePlanService.getPreposePlansByPlanId(child);
                        for (PreposePlan prepose : preposeList) {
                            preposePlanService.delete(prepose);
                        }

                        // 删除资源
                        ResourceLinkInfoDto resource = new ResourceLinkInfoDto();
                        resource.setUseObjectId(child.getId());
                        resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        List<ResourceLinkInfoDto> resourceLinkInfoList = resourceLinkInfoService.getList(resource);
                        for (ResourceLinkInfoDto resourceLinkInfo : resourceLinkInfoList) {
                            sessionFacade.delete(resourceLinkInfo);
                        }

                        // 删除不存在的节点
                        sessionFacade.delete(child);
                    }
                    else {

                        // 删除与该节点相关的前后置关系
                        List<PreposePlan> preposeList = preposePlanService.getPreposePlansByPlanId(child);
                        for (PreposePlan prepose : preposeList) {
                            Plan preposePlan = getEntity(Plan.class, prepose.getPreposePlanId());
                            if (child.getParentPlanId().equals(preposePlan.getParentPlanId())) {
                                preposePlanService.delete(prepose);
                            }
                        }

                        child.setPlanStartTime(null);
                        child.setPlanEndTime(null);
                        sessionFacade.save(child);
                    }
                }
                flag = true;
            }
        }

        Plan parent = (Plan)sessionFacade.getEntity(Plan.class, parentPlanId);
        parent.setFlowResolveXml(temp.getFlowResolveXml());
        sessionFacade.saveOrUpdate(parent);

        // 保存前后置关联关系：
        rdFlowTaskFlowResolveService.saveFlowTaskCellConns(cellContact, parentPlanId);

//        // 调用Webservice
//        RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//        RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//        rdfConfigSupport.saveFlowTaskCellConns(cellContact, parentPlanId);    
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm") // 时间转化为特定格式
//        .setPrettyPrinting() // 对json结果格式化
//        .create();
        // List<FlowTaskCellConnectVo> connectList =
        // (List<FlowTaskCellConnectVo>)gson.fromJson(jsonList, FlowTaskCellConnectVo.class);
        List<FlowTaskCellConnectVo> connectList = new ArrayList<FlowTaskCellConnectVo>();
        connectList = rdFlowTaskFlowResolveService.queryFlowTaskCellConnectVoList(parentPlanId, "");

//        try {
//            String jsonList = rdfConfigSupport.queryFlowTaskCellConnectVoList(parentPlanId, "");
//            AjaxJson ajaxJson = gson.fromJson(jsonList, AjaxJson.class);
//            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//            mapList = (List<Map<String, Object>>)ajaxJson.getObj();
//            if (!CommonUtil.isEmpty(mapList)) {
//                for (Map<String, Object> map : mapList) {
//                    FlowTaskCellConnectVo vo = new FlowTaskCellConnectVo();
//                    vo.setCellId(StringUtils.isEmpty((String)map.get("cellId")) ? "" : map.get(
//                        "cellId").toString());
//                    vo.setFormId(StringUtils.isEmpty((String)map.get("formId")) ? "" : map.get(
//                        "formId").toString());
//                    vo.setInfoId(StringUtils.isEmpty((String)map.get("infoId")) ? "" : map.get(
//                        "infoId").toString());
//                    vo.setParentPlanId(StringUtils.isEmpty((String)map.get("parentPlanId")) ? "" : map.get(
//                        "parentPlanId").toString());
//                    vo.setTargetId(StringUtils.isEmpty((String)map.get("targetId")) ? "" : map.get(
//                        "targetId").toString());
//                    vo.setTargetInfoId(StringUtils.isEmpty((String)map.get("targetInfoId")) ? "" : map.get(
//                        "targetInfoId").toString());
//                    connectList.add(vo);
//                }
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        //获取cellConnectMap集合：
        Map<String,String> cellConnectMap = new HashMap<String,String>();
        Map<String,String> cellConnectTargetIdMap = new HashMap<String,String>();
        Map<String,FlowTaskCellConnectVo> cellConnectVoMap = new HashMap<String,FlowTaskCellConnectVo>();
        for (FlowTaskCellConnectVo cellConnect : connectList) {
            if (!TaskProcConstants.TASK_CELL_START.equals(cellConnect.getCellId())
                && !TaskProcConstants.TASK_CELL_END.equals(cellConnect.getTargetId())) {
                Plan p1 = new Plan();
                p1.setParentPlanId(cellConnect.getParentPlanId());
                p1.setCellId(cellConnect.getCellId());
                List<Plan> list1 = planService.queryPlanList(p1, 1, 10, false);
                Plan p2 = new Plan();
                p2.setParentPlanId(cellConnect.getParentPlanId());
                p2.setCellId(cellConnect.getTargetId());
                List<Plan> list2 = planService.queryPlanList(p2, 1, 10, false);
                if (!CommonUtil.isEmpty(list1) && !CommonUtil.isEmpty(list2)) {
                    PreposePlan prepose = new PreposePlan();
                    prepose.setPreposePlanId(list1.get(0).getId());
                    prepose.setPlanId(list2.get(0).getId());
                    if (CommonUtil.isEmpty(preposePlanService.searchPrepose(prepose))) {
                        preposePlanService.save(prepose);
                    }
                }
            }
            cellConnectMap.put(cellConnect.getCellId(),cellConnect.getTargetId());
            cellConnectTargetIdMap.put(cellConnect.getTargetId(),cellConnect.getTargetId());
            cellConnectVoMap.put(cellConnect.getCellId()+"-"+cellConnect.getTargetId(),cellConnect);
        }

        // connect.setCellId(TaskProcConstants.TASK_CELL_START);
        // List<FlowTaskCellConnectVo> cellConnectList =
        // planService.queryFlowTaskCellConnectVoList(connect);
        // String jsonList1=
        // rdfConfigSupport.queryFlowTaskCellConnectVoList(parentPlanId,TaskProcConstants.TASK_CELL_START);
        // List<FlowTaskCellConnectVo> cellConnectList =
        // (List<FlowTaskCellConnectVo>)gson.fromJson(jsonList1, FlowTaskCellConnectVo.class);
//        List<FlowTaskCellConnectVo> cellConnectList = new ArrayList<FlowTaskCellConnectVo>();
//        rdFlowTaskFlowResolveService.queryFlowTaskCellConnectVoList(parentPlanId,TaskProcConstants.TASK_CELL_START);
//        try {
//
//            String jsonList1 = rdfConfigSupport.queryFlowTaskCellConnectVoList(parentPlanId,
//                TaskProcConstants.TASK_CELL_START);
//            AjaxJson ajaxJson1 = gson.fromJson(jsonList1, AjaxJson.class);
//            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//            mapList = (List<Map<String, Object>>)ajaxJson1.getObj();
//            if (!CommonUtil.isEmpty(mapList)) {
//                for (Map<String, Object> map : mapList) {
//                    FlowTaskCellConnectVo vo = new FlowTaskCellConnectVo();
//                    vo.setCellId(StringUtils.isEmpty((String)map.get("cellId")) ? "" : map.get(
//                        "cellId").toString());
//                    vo.setFormId(StringUtils.isEmpty((String)map.get("formId")) ? "" : map.get(
//                        "formId").toString());
//                    vo.setInfoId(StringUtils.isEmpty((String)map.get("infoId")) ? "" : map.get(
//                        "infoId").toString());
//                    vo.setParentPlanId(StringUtils.isEmpty((String)map.get("parentPlanId")) ? "" : map.get(
//                        "parentPlanId").toString());
//                    vo.setTargetId(StringUtils.isEmpty((String)map.get("targetId")) ? "" : map.get(
//                        "targetId").toString());
//                    vo.setTargetInfoId(StringUtils.isEmpty((String)map.get("targetInfoId")) ? "" : map.get(
//                        "targetInfoId").toString());
//                    cellConnectList.add(vo);
//                }
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        List<FlowTaskCellConnectVo> cellConnectList = new ArrayList<FlowTaskCellConnectVo>();
        //找出画布中流程图节点的关联最开始节点：
        for (String cur : cellConnectMap.keySet()) {
            boolean curFlag = false;
            String targetId = cellConnectMap.get(cur);
            if(CommonUtil.isEmpty(cellConnectTargetIdMap.get(cur))){
                curFlag = true;
            }
            if(curFlag){
                cellConnectList.add(cellConnectVoMap.get(cur+"-"+targetId));
            }
        }

        //获取项目日期类型：
        Project projectEntity = projectService.getProjectEntity(parent.getProjectId());
        String projectTimeType = "";
        if(!CommonUtil.isEmpty(projectEntity)){
            projectTimeType = projectEntity.getProjectTimeType();
        }

        for (FlowTaskCellConnectVo cellConnect : cellConnectList) {
            Plan p = new Plan();
            p.setParentPlanId(cellConnect.getParentPlanId());
            p.setCellId(cellConnect.getTargetId());
            List<Plan> infoList = planService.queryPlanList(p, 1, 10, false);
            if (!CommonUtil.isEmpty(infoList)) {
                Plan info = infoList.get(0);
                info.setPlanStartTime(parent.getPlanStartTime());
                Date date = (Date) info.getPlanStartTime().clone();
                if (StringUtils.isNotEmpty(projectTimeType)) {
                    if (ProjectConstants.WORKDAY.equals(projectTimeType)) {
                        info.setPlanEndTime(com.glaway.foundation.common.util.DateUtil.nextWorkDay(
                                date, Integer.valueOf(info.getWorkTime()) - 1));
                    } else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("startDate", date);
                        params.put("days", Integer.valueOf(info.getWorkTime()) - 1);
                        info.setPlanEndTime(calendarService.getNextWorkingDay(appKey, params));
                    } else {
                        info.setPlanEndTime(TimeUtil.getExtraDate(date,
                                Integer.valueOf(info.getWorkTime()) - 1));
                    }
                } else {
                    info.setPlanEndTime(TimeUtil.getExtraDate(date,
                            Integer.valueOf(info.getWorkTime()) - 1));
                }

                info = planService.saveFlowResolvePlan(info, userId);
                // connect = new FlowTaskCellConnectVo();
                // connect.setCellId(cellConnect.getTargetId());
                // connect.setParentPlanId(parentPlanId);
                // List<FlowTaskCellConnectVo> backConns =
                // planService.queryFlowTaskCellConnectVoList(connect);
                // String jsonList2=
                // rdfConfigSupport.queryFlowTaskCellConnectVoList(parentPlanId,cellConnect.getTargetId());
                // List<FlowTaskCellConnectVo> backConns =
                // (List<FlowTaskCellConnectVo>)gson.fromJson(jsonList2,
                // FlowTaskCellConnectVo.class);
                List<FlowTaskCellConnectVo> backConns = new ArrayList<FlowTaskCellConnectVo>();
                backConns = rdFlowTaskFlowResolveService.queryFlowTaskCellConnectVoList(parentPlanId, cellConnect.getTargetId());
//                try {
//
//                    String jsonList2 = rdfConfigSupport.queryFlowTaskCellConnectVoList(
//                        parentPlanId, cellConnect.getTargetId());
//                    AjaxJson ajaxJson2 = gson.fromJson(jsonList2, AjaxJson.class);
//                    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//                    mapList = (List<Map<String, Object>>)ajaxJson2.getObj();
//                    if (!CommonUtil.isEmpty(mapList)) {
//                        for (Map<String, Object> map : mapList) {
//                            FlowTaskCellConnectVo vo = new FlowTaskCellConnectVo();
//                            vo.setCellId(StringUtils.isEmpty((String)map.get("cellId")) ? "" : map.get(
//                                "cellId").toString());
//                            vo.setFormId(StringUtils.isEmpty((String)map.get("formId")) ? "" : map.get(
//                                "formId").toString());
//                            vo.setInfoId(StringUtils.isEmpty((String)map.get("infoId")) ? "" : map.get(
//                                "infoId").toString());
//                            vo.setParentPlanId(StringUtils.isEmpty((String)map.get("parentPlanId")) ? "" : map.get(
//                                "parentPlanId").toString());
//                            vo.setTargetId(StringUtils.isEmpty((String)map.get("targetId")) ? "" : map.get(
//                                "targetId").toString());
//                            vo.setTargetInfoId(StringUtils.isEmpty((String)map.get("targetInfoId")) ? "" : map.get(
//                                "targetInfoId").toString());
//                            backConns.add(vo);
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
                flowResolveWithBackPlan(info.getId(), backConns, userId,projectTimeType);
            }
        }

        childList = new ArrayList<Plan>();
        plan.setParentPlanId(parentPlanId);
        childList = planService.queryPlanList(plan, 1, 10, false);
        for (Plan info : childList) {
            if (info.getPlanStartTime() == null || info.getPlanEndTime() == null) {
                info.setPlanStartTime(parent.getPlanStartTime());
                Date date = (Date)info.getPlanStartTime().clone();
                if (StringUtils.isNotEmpty(projectTimeType)) {
                    if (ProjectConstants.WORKDAY.equals(projectTimeType)) {
                        info.setPlanEndTime(com.glaway.foundation.common.util.DateUtil.nextWorkDay(
                            date, Integer.valueOf(info.getWorkTime()) - 1));
                    }
                    else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("startDate",date);
                        params.put("days",Integer.valueOf(info.getWorkTime()) - 1);
                        info.setPlanEndTime(calendarService.getNextWorkingDay(appKey,params));
                    }
                    else {
                        info.setPlanEndTime(TimeUtil.getExtraDate(date,
                            Integer.valueOf(info.getWorkTime()) - 1));
                    }
                }
                else {
                    info.setPlanEndTime(TimeUtil.getExtraDate(date,
                        Integer.valueOf(info.getWorkTime()) - 1));
                }
                info = planService.saveFlowResolvePlan(info,userId);
            }
        }
    }

    @Override
    public void doAddInputsNew(String ids, Inputs currentinputs,String currentUserId) {
        if (CommonUtil.isEmpty(currentinputs.getId())) {
            // 新增：
            // if(PlanConstants.LOCAL_EN.equals(currentinputs.getOriginType())){
            CommonUtil.glObjectSet(currentinputs);
            inputsService.saveOrUpdate(currentinputs);
            // 同步到任务中：
            // saveRdfInputs(currentinputs);：
            InputsDto inputsDto = new  InputsDto();
            try {
                BeanUtils.copyProperties(inputsDto, currentinputs);
            }
            catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rdFlowTaskFlowResolveService.setInputsAddInnerTask(inputsDto,currentUserId);
//            try {
//                RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//                rdfConfigSupport.setInputsAddInnerTask(currentUserId, currentinputs.getDocId(),
//                    currentinputs.getDocName(), currentinputs.getName(),
//                    currentinputs.getOriginType(), currentinputs.getOriginTypeExt(),
//                    currentinputs.getUseObjectId(), currentinputs.getOriginObjectId(),
//                    currentinputs.getOriginDeliverablesInfoId(), currentinputs.getId());
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }

            // }
        }
        else {
            // 修改：
            Inputs curInputs = getEntity(Inputs.class, currentinputs.getId());
            if ((!CommonUtil.isEmpty(curInputs.getOriginTypeExt()) && curInputs.getOriginTypeExt().equals(
                PlanConstants.DELIEVER_EN))) {
                curInputs.setDocId(currentinputs.getDocId());
                curInputs.setDocName(currentinputs.getDocName());
                curInputs.setOriginType(currentinputs.getOriginType());
                curInputs.setOriginTypeExt(currentinputs.getOriginTypeExt());
                curInputs.setOriginObjectId(currentinputs.getOriginObjectId());
                curInputs.setOriginDeliverablesInfoId(currentinputs.getOriginDeliverablesInfoId());
                curInputs.setExt1(currentinputs.getExt1());
                curInputs.setExt2(currentinputs.getExt2());
                curInputs.setExt3(currentinputs.getExt3());
                CommonUtil.glObjectSet(curInputs);
                inputsService.saveOrUpdate(curInputs);
                // 同步到任务中：
                InputsDto inputsDto = new  InputsDto();
                try {
                    BeanUtils.copyProperties(inputsDto, currentinputs);
                    rdFlowTaskFlowResolveService.setInputsAddInnerTask(inputsDto,currentUserId);
                }
                catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void saveFlowTask1(Plan plan,
                              Plan parent,TSUserDto user) {
        String preposeIds = plan.getPreposeIds();
        Plan t = (Plan)sessionFacade.getEntity(Plan.class, plan.getId());

        // 20160928 如果计划名称变更，则先删除该计划相关的输入及输入对应的输入
        if (!t.getPlanName().equals(plan.getPlanName())) {
            deliverablesInfoService.deleteDeliverablesByPlanId(t.getId());
        }

        // 活动名称类型决定taskNameType
        if ("true".equals(plan.getFromTemplate())) {
            t.setOwner(plan.getOwner());
            t.setPlanLevel(plan.getPlanLevel());
            t.setRemark(plan.getRemark());
            if (!CommonUtil.isEmpty(plan.getTaskNameType())) {
                t.setTaskNameType(plan.getTaskNameType());
            }
//            else {
//                t.setTaskNameType(CommonConstants.NAMESTANDARD_TYPE_DEV);
//            }
            t.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
        }
        else {
            t.setPlanName(plan.getPlanName());
            t.setOwner(plan.getOwner());
            t.setCreateOrgId(plan.getCreateOrgId());
            t.setPlanLevel(plan.getPlanLevel());
            t.setRemark(plan.getRemark());
            t.setWorkTime(plan.getWorkTime());
            if (!CommonUtil.isEmpty(t.getTaskNameType())) {
                t.setTaskNameType(t.getTaskNameType());
            }
//            else {
//                t.setTaskNameType(CommonConstants.NAMESTANDARD_TYPE_DEV);
//            }
            t.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
        }

        Date date = (Date)t.getPlanStartTime().clone();
        Project parentProject = projectService.getProjectEntity(parent.getProjectId());
        if (parentProject != null
            && StringUtils.isNotEmpty(parentProject.getProjectTimeType())) {
            if (ProjectConstants.WORKDAY.equals(parentProject.getProjectTimeType())) {
                t.setPlanEndTime(com.glaway.foundation.common.util.DateUtil.nextWorkDay(date,
                    Integer.valueOf(t.getWorkTime()) - 1));
            }
            else if (parentProject != null
                    && ProjectConstants.COMPANYDAY.equals(parentProject.getProjectTimeType())) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("startDate",date);
                params.put("days",Integer.valueOf(t.getWorkTime()) - 1);
                t.setPlanEndTime(calendarService.getNextWorkingDay(appKey,params));
            }
            else {
                t.setPlanEndTime(TimeUtil.getExtraDate(date, Integer.valueOf(t.getWorkTime()) - 1));
            }
        }
        else {
            t.setPlanEndTime(TimeUtil.getExtraDate(date, Integer.valueOf(t.getWorkTime()) - 1));
        }

        List<DeliverablesInfo> list2 = deliverablesInfoService.getDeliverablesByUseObeject("PLAN",
            t.getId());

        String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
//        String switchStr = "";
//        try {
//            RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//            RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//            String switchStrJson = rdfConfigSupport.getSwitchfromRdFlow(SwitchConstants.NAMESTANDARDSWITCH);
//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().create();
//            AjaxJson ajaxJson = gson.fromJson(switchStrJson, AjaxJson.class);
//            switchStr = (String)ajaxJson.getObj();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        NameStandardDto condition2 = new NameStandardDto();
        condition2.setStopFlag("启用");
        condition2.setName(t.getPlanName());
        List<NameStandardDto> nameStandardList2 = nameStandardService.searchNameStandards(condition2);
        if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
            || NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
            || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
            if (nameStandardList2.size() > 0) {
                for (NameStandardDto n : nameStandardList2) {
                    if ("启用".equals(n.getStopFlag())) {
                        NameStandardDeliveryRelationDto relation = new NameStandardDeliveryRelationDto();
                        relation.setNameStandardId(n.getId());
                        List<NameStandardDeliveryRelationDto> list = nameStandardDeliveryRelationService.searchForPage(
                            relation, 1, 10);
                        // List<NameStandardDeliveryRelation> list =
                        // nameStandardDeliveryRelationService.getInfoByNameStandardId(n.getId());
                        if (list.size() > 0) {
                            for (NameStandardDeliveryRelationDto r : list) {
                                if (r.getDeliveryStandardId() != null) {
                                    r.setDeliveryStandard(deliveryStandardService.getDeliveryStandardEntity(r.getDeliveryStandardId()));
                                    int a = 0;
                                    for (DeliverablesInfo d : list2) {
                                        if (r.getDeliveryStandard().getName().equals(d.getName())) {
                                            a = 1;
                                            break;
                                        }
                                    }
                                    if (a == 0 && r.getDeliveryStandardId() != null) {
                                        DeliverablesInfo deliverablesInfoTemp = new DeliverablesInfo();
                                        deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
                                        deliverablesInfoTemp.setId(null);
                                        deliverablesInfoTemp.setName(r.getDeliveryStandard().getName());
                                        deliverablesInfoTemp.setUseObjectId(t.getId());
                                        deliverablesInfoTemp.setUseObjectType("PLAN");
                                        // 查看系统参数，决定初始化带过来的交付项师傅必要
                                        List<ParamSwitch> paramSwitchList = paramSwitchService.searchProconfigParamSwitch();
                                        // String datagridStr = JSON.toJSONString(list);
                                        // 国际化加载：活动名称库、启用活动名称库、关联交付项可以修改；
                                        // String activityNameBase =
                                        // I18nUtil.getValue("com.glaway.ids.pm.config.paramSwitch.activityNameBase");
                                        // String startActivityNameBase =
                                        // I18nUtil.getValue("com.glaway.ids.pm.config.paramSwitch.startActivityNameBase");
                                        // String deliverablesLinkCanUpdate =
                                        // I18nUtil.getValue("com.glaway.ids.pm.config.paramSwitch.deliverablesLinkCanUpdate");
                                        String activityNameBase = SwitchConstants.NAMESTANDARDSWITCH;
                                        String startActivityNameBase = NameStandardSwitchConstants.USENAMESTANDARDLIB;
                                        String deliverablesLinkCanUpdate = NameStandardSwitchConstants.DELIVERABLEUPATEABLE;
                                        String flagUpdate = "true";
                                        if (!CommonUtil.isEmpty(paramSwitchList)) {
                                            for (ParamSwitch curStr : paramSwitchList) {
                                                if (curStr.getName().equals(activityNameBase)
                                                    && curStr.getStatus().contains(
                                                        startActivityNameBase)
                                                    && curStr.getStatus().contains(
                                                        deliverablesLinkCanUpdate)) {
                                                    flagUpdate = "false";
                                                }
                                            }
                                        }
                                        deliverablesInfoTemp.setRequired(flagUpdate);
                                        deliverablesInfoTemp.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                                        deliverablesInfoTemp.setCreateBy(user.getId());
                                        deliverablesInfoTemp.setCreateFullName(user.getRealName());
                                        deliverablesInfoTemp.setCreateName(user.getUserName());
                                        deliverablesInfoTemp.setCreateTime(new Date());
                                        sessionFacade.save(deliverablesInfoTemp);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(CommonUtil.isEmpty(t.getId())) {
            t.setCreateBy(user.getId());
            t.setCreateName(user.getUserName());
            t.setCreateFullName(user.getRealName());
            t.setCreateTime(new Date());


        }else {
            t.setUpdateBy(user.getId());
            t.setUpdateName(user.getUserName());
            t.setUpdateFullName(user.getRealName());
            t.setUpdateTime(new Date());
        }
        sessionFacade.saveOrUpdate(t);

        if (StringUtils.isNotEmpty(preposeIds.trim())) {
            String[] a = preposeIds.split(",");
            if (a.length > 0) {
                for (int i = 0; i < a.length; i++ ) {
                    String id = a[i];
                    PreposePlan outplan = new PreposePlan();
                    outplan.setPreposePlanId(id);
                    outplan.setPlanId(t.getId());
                    saveOrUpdate(outplan);
                }
            }
        }
    }

    @Override
    public String saveFlowTask2(Plan plan,
                              Plan parent,String userId) {
        String preposeIds = plan.getPreposeIds();
        parent.setOpContent(PlanConstants.PLAN_LOGINFO_FLOW_SPLIT);
        planService.initBusinessObject(plan);
        plan.setTabCbTemplateId(plan.getTabCbTemplateId());
        plan.setProgressRate(planService.getProgress(plan));
        plan.setProjectId(parent.getProjectId());
        plan.setParentPlanId(parent.getId());
        plan.setCreateOrgId(plan.getCreateOrgId());
        plan.setOwner(plan.getOwner());
        plan.setPlanLevel(plan.getPlanLevel());
        plan.setRemark(plan.getRemark());
        plan.setPlanStartTime(parent.getPlanStartTime());
        if (!CommonUtil.isEmpty(plan.getTaskNameType())) {
            plan.setTaskNameType(plan.getTaskNameType());
        }
//        else {
//            plan.setTaskNameType(CommonConstants.NAMESTANDARD_TYPE_DEV);
//        }
        plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
        plan.setWorkTime(plan.getWorkTime());
        plan.setWorkTimeReference(plan.getWorkTimeReference());
        Date date = (Date)plan.getPlanStartTime().clone();
        Project parentProject = projectService.getProjectEntity(parent.getProjectId());
        if (parentProject != null
            && StringUtils.isNotEmpty(parentProject.getProjectTimeType())) {
            if (ProjectConstants.WORKDAY.equals(parentProject.getProjectTimeType())) {
                plan.setPlanEndTime(com.glaway.foundation.common.util.DateUtil.nextWorkDay(date,
                    Integer.valueOf(plan.getWorkTime()) - 1));
            }
            else if (ProjectConstants.COMPANYDAY.equals(parentProject.getProjectTimeType())) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("startDate",date);
                params.put("days",Integer.valueOf(plan.getWorkTime()) - 1);
                plan.setPlanEndTime(calendarService.getNextWorkingDay(appKey,params));
            }
            else {
                plan.setPlanEndTime(TimeUtil.getExtraDate(date,
                    Integer.valueOf(plan.getWorkTime()) - 1));
            }
        }
        else {
            plan.setPlanEndTime(TimeUtil.getExtraDate(date,
                Integer.valueOf(plan.getWorkTime()) - 1));
        }

        plan.setPlanType(PlanConstants.PLAN_TYPE_FLOW);
        TSUserDto user = userService.getUserByUserId(userId);
        plan.setCreateBy(user.getId());
        plan.setCreateName(user.getUserName());
        plan.setCreateFullName(user.getRealName());
        plan.setCreateTime(new Date());
        Plan curParent = (Plan)sessionFacade.get(Plan.class, parent.getId());
        curParent.setUpdateBy(user.getId());
        curParent.setUpdateName(user.getUserName());
        curParent.setUpdateFullName(user.getRealName());
        curParent.setUpdateTime(new Date());
        sessionFacade.saveOrUpdate(curParent);
        planService.saveFlowResolvePlan(plan,userId);

//        boolean needHttp = false;
//        if (!CommonUtil.isEmpty(plan.getTaskNameType())
//            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(plan.getTaskNameType())
//            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(plan.getTaskNameType())
//            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(plan.getTaskNameType())) {
//            needHttp = true;
//        }
//        if (needHttp) {
//            String initStr = "";
//            //TODO..
//            List<OutwardExtension> outwardExtensionList = outwardExtensionService.getOutwardExtensionList("flowActiveCategoryHttpServer");
//            if (!CommonUtil.isEmpty(outwardExtensionList)) {
//                for (OutwardExtension ext : outwardExtensionList) {
//                    if (!CommonUtil.isEmpty(ext.getUrlList())) {
//                        for (OutwardExtensionUrl out : ext.getUrlList()) {
//                            if ("init".equals(out.getOperateCode())) {
//                                NameStandard ns = new NameStandard();
//                                ns.setName(plan.getPlanName());
//                                ns.setStopFlag("启用");
//                                List<NameStandard> list = nameStandardService.searchNameStandards(ns);
//
//                                initStr = out.getOperateUrl() + "&flowTemplateId="
//                                          + parent.getId() + "&namestandardId="
//                                          + list.get(0).getId() + "&flowTempNodeId="
//                                          + plan.getId() + "&userId="
//                                          + UserUtil.getCurrentUser().getId();
//                            }
//                        }
//                    }
//
//                }
//                try {
//                    HttpClientUtil.httpClientPostByTest(initStr, null);
//                }
//                catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        else {
            String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
//            String switchStr = "";
//            try {
//                RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//                String switchStrJson = rdfConfigSupport.getSwitchfromRdFlow(SwitchConstants.NAMESTANDARDSWITCH);
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().create();
//                AjaxJson ajaxJson = gson.fromJson(switchStrJson, AjaxJson.class);
//                switchStr = (String)ajaxJson.getObj();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
            NameStandardDto condition2 = new NameStandardDto();
            condition2.setStopFlag("启用");
            condition2.setName(plan.getPlanName());
            List<NameStandardDto> nameStandardList2 = nameStandardService.searchNameStandards(condition2);
            if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                || NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                if (nameStandardList2.size() > 0) {
                    NameStandardDto n = nameStandardList2.get(0);
                    if ("启用".equals(n.getStopFlag())) {
                        NameStandardDeliveryRelationDto relation = new NameStandardDeliveryRelationDto();
                        relation.setNameStandardId(n.getId());
                        List<NameStandardDeliveryRelationDto> list = nameStandardDeliveryRelationService.searchForPage(
                            relation, 1, 10);
                        if (list.size() > 0) {
                            for (NameStandardDeliveryRelationDto r : list) {
                                if(!CommonUtil.isEmpty(r.getDeliveryStandardId())) {
                                    r.setDeliveryStandard(deliveryStandardService.getDeliveryStandardEntity(r.getDeliveryStandardId()));
                                }
                                if (r.getDeliveryStandardId() != null
                                    && r.getDeliveryStandard() != null
                                    && "启用".equals(r.getDeliveryStandard().getStopFlag())) {
                                    DeliverablesInfo deliverablesInfoTemp = new DeliverablesInfo();
                                    deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
                                    deliverablesInfoTemp.setId(null);
                                    deliverablesInfoTemp.setName(r.getDeliveryStandard().getName());
                                    deliverablesInfoTemp.setUseObjectId(plan.getId());
                                    deliverablesInfoTemp.setUseObjectType("PLAN");
                                    // 查看系统参数，决定初始化带过来的交付项师傅必要
                                    List<ParamSwitch> paramSwitchList = paramSwitchService.searchProconfigParamSwitch();
                                    // String datagridStr = JSON.toJSONString(list);
                                    // 国际化加载：活动名称库、启用活动名称库、关联交付项可以修改；
                                    // String activityNameBase =
                                    // I18nUtil.getValue("com.glaway.ids.pm.config.paramSwitch.activityNameBase");
                                    // String startActivityNameBase =
                                    // I18nUtil.getValue("com.glaway.ids.pm.config.paramSwitch.startActivityNameBase");
                                    // String deliverablesLinkCanUpdate =
                                    // I18nUtil.getValue("com.glaway.ids.pm.config.paramSwitch.deliverablesLinkCanUpdate");
                                    String activityNameBase = SwitchConstants.NAMESTANDARDSWITCH;
                                    String startActivityNameBase = NameStandardSwitchConstants.USENAMESTANDARDLIB;
                                    String deliverablesLinkCanUpdate = NameStandardSwitchConstants.DELIVERABLEUPATEABLE;
                                    String flagUpdate = "true";
                                    if (!CommonUtil.isEmpty(paramSwitchList)) {
                                        for (ParamSwitch curStr : paramSwitchList) {
                                            if (curStr.getName().equals(activityNameBase)
                                                && curStr.getStatus().contains(
                                                    startActivityNameBase)
                                                && curStr.getStatus().contains(
                                                    deliverablesLinkCanUpdate)) {
                                                flagUpdate = "false";
                                            }
                                        }
                                    }
                                    deliverablesInfoTemp.setRequired(flagUpdate);
                                    deliverablesInfoTemp.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                                    deliverablesInfoTemp.setCreateBy(user.getId());
                                    deliverablesInfoTemp.setCreateName(user.getUserName());
                                    deliverablesInfoTemp.setCreateFullName(user.getRealName());
                                    deliverablesInfoTemp.setCreateTime(new Date());
                                    sessionFacade.save(deliverablesInfoTemp);
                                }
                            }
                        }
                    }
                }
            }
//        }

        if (!CommonUtil.isEmpty((preposeIds))) {
            String[] a = preposeIds.split(",");
            if (a.length > 0) {
                for (int i = 0; i < a.length; i++ ) {
                    String id = a[i];
                    PreposePlan outplan = new PreposePlan();
                    outplan.setPreposePlanId(id);
                    outplan.setPlanId(plan.getId());
                    saveOrUpdate(outplan);
                }
            }
        }

        return plan.getId();
    }

    /**
     * 根据流程模板节点保存期后置计划及其相关信息
     *
     * @param preposeId
     * @param userId
     * @param backPlans
     * @see
     */

    private void flowResolveWithBackPlan(String preposeId, List<FlowTaskCellConnectVo> backPlans,String userId,String projectTimeType) {
        PlanDto prepose = planService.getPlanEntity(preposeId);
        for (FlowTaskCellConnectVo conn : backPlans) {
            if (!TaskProcConstants.TASK_CELL_END.equals(conn.getTargetId())) {
                Plan p = new Plan();
                p.setParentPlanId(conn.getParentPlanId());
                p.setCellId(conn.getTargetId());
                List<Plan> infoList = planService.queryPlanList(p, 1, 10, false);
                if (!CommonUtil.isEmpty(infoList)) {
                    Plan info = infoList.get(0);
                    Date proposeEndTime = (Date)prepose.getPlanEndTime().clone();
                    Date startTime;
                    if (StringUtils.isNotEmpty(projectTimeType)) {
                        if (ProjectConstants.WORKDAY.equals(projectTimeType)) {
                            startTime = DateUtil.nextWorkDay(proposeEndTime, 1);
                            if (info.getPlanStartTime() == null
                                || startTime.getTime() > info.getPlanStartTime().getTime()) {
                                info.setPlanStartTime(startTime);
                                Date date = (Date)info.getPlanStartTime().clone();
                                info.setPlanEndTime(com.glaway.foundation.common.util.DateUtil.nextWorkDay(
                                    date, Integer.valueOf(info.getWorkTime()) - 1));
                            }
                        }
                        else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("startDate",proposeEndTime);
                            params.put("days",1);
                            startTime = calendarService.getNextWorkingDay(appKey,params);
                            if (info.getPlanStartTime() == null
                                || startTime.getTime() > info.getPlanStartTime().getTime()) {
                                info.setPlanStartTime(startTime);
                                Date date = (Date)info.getPlanStartTime().clone();
                                Map<String, Object> params1 = new HashMap<String, Object>();
                                params1.put("startDate",date);
                                params1.put("days",Integer.valueOf(info.getWorkTime()) - 1);
                                info.setPlanEndTime(calendarService.getNextWorkingDay(appKey,params1));
                            }
                        }
                        else {
                            startTime = TimeUtil.getExtraDate(proposeEndTime, 1);
                            if (info.getPlanStartTime() == null
                                || startTime.getTime() > info.getPlanStartTime().getTime()) {
                                info.setPlanStartTime(startTime);
                                Date date = (Date)info.getPlanStartTime().clone();
                                info.setPlanEndTime(TimeUtil.getExtraDate(date,
                                    Integer.valueOf(info.getWorkTime()) - 1));
                            }
                        }
                    }
                    else {
                        startTime = TimeUtil.getExtraDate(proposeEndTime, 1);
                        if (info.getPlanStartTime() == null
                            || startTime.getTime() > info.getPlanStartTime().getTime()) {
                            info.setPlanStartTime(startTime);
                            Date date = (Date)info.getPlanStartTime().clone();
                            info.setPlanEndTime(TimeUtil.getExtraDate(date,
                                Integer.valueOf(info.getWorkTime()) - 1));
                        }
                    }
                    info = planService.saveFlowResolvePlan(info,userId);

//                    RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                    RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm") // 时间转化为特定格式
//                    .setPrettyPrinting() // 对json结果格式化
//                    .create();                
                    List<FlowTaskCellConnectVo> backConns = new ArrayList<FlowTaskCellConnectVo>();
                    backConns = rdFlowTaskFlowResolveService.queryFlowTaskCellConnectVoList(conn.getParentPlanId(), conn.getTargetId());
//                    try {
//
//                        String jsonList2 = rdfConfigSupport.queryFlowTaskCellConnectVoList(
//                            conn.getParentPlanId(), conn.getTargetId());
//                        AjaxJson ajaxJson2 = gson.fromJson(jsonList2, AjaxJson.class);
//                        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//                        mapList = (List<Map<String, Object>>)ajaxJson2.getObj();
//                        if (!CommonUtil.isEmpty(mapList)) {
//                            for (Map<String, Object> map : mapList) {
//                                FlowTaskCellConnectVo vo = new FlowTaskCellConnectVo();
//                                vo.setCellId(StringUtils.isEmpty((String)map.get("cellId")) ? "" : map.get(
//                                    "cellId").toString());
//                                vo.setFormId(StringUtils.isEmpty((String)map.get("formId")) ? "" : map.get(
//                                    "formId").toString());
//                                vo.setInfoId(StringUtils.isEmpty((String)map.get("infoId")) ? "" : map.get(
//                                    "infoId").toString());
//                                vo.setParentPlanId(StringUtils.isEmpty((String)map.get("parentPlanId")) ? "" : map.get(
//                                    "parentPlanId").toString());
//                                vo.setTargetId(StringUtils.isEmpty((String)map.get("targetId")) ? "" : map.get(
//                                    "targetId").toString());
//                                vo.setTargetInfoId(StringUtils.isEmpty((String)map.get("targetInfoId")) ? "" : map.get(
//                                    "targetInfoId").toString());
//                                backConns.add(vo);
//                            }
//                        }
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    flowResolveWithBackPlan(info.getId(), backConns,userId,projectTimeType);
                }
            }
        }
    }

    @Override
    public void doBatchDelInputsForWork(String ids) {
        for (String id : ids.split(",")) {
            inputsService.deleteInputsById(id);
            // 同步任务：
            sessionFacade.executeSql2("delete from RDF_INPUTS t where t.linkobjectinputsid='" + id
                                      + "'");
        }
    }

    @Override
    public void doAddInheritDocument(String names, Plan plan) {
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (String name : names.split(",")) {
            DeliverablesInfo deliverablesInfoTemp = new DeliverablesInfo();
            deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
            deliverablesInfoTemp.setId(null);
            deliverablesInfoTemp.setName(name);
            deliverablesInfoTemp.setUseObjectId(plan.getId());
            deliverablesInfoTemp.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            sessionFacade.save(deliverablesInfoTemp);
            JSONObject obj = new JSONObject();
            obj.put("id", deliverablesInfoTemp.getId());
            obj.put("name", name);
            jsonList.add(obj);
        }
        boolean needHttp = false;
        if (!CommonUtil.isEmpty(plan.getTaskNameType())
            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(plan.getTaskNameType())
            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(plan.getTaskNameType())
            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(plan.getTaskNameType())) {
            needHttp = true;
        }
        if (needHttp) {
            List<String> httpUrls = new ArrayList<String>();
          //TODO..
//            List<OutwardExtension> outwardExtensionList = outwardExtensionService.getOutwardExtensionList("flowResolveCategoryHttpServer");
//            if (!CommonUtil.isEmpty(outwardExtensionList)) {
//                for (OutwardExtension ext : outwardExtensionList) {
//                    if (!CommonUtil.isEmpty(ext.getUrlList())) {
//                        for (OutwardExtensionUrl out : ext.getUrlList()) {
//                            if ("inheritParent".equals(out.getOperateCode())) {
//                                httpUrls.add(out.getOperateUrl());
//                            }
//                        }
//                    }
//                }
//            }
//            if (!CommonUtil.isEmpty(httpUrls)) {
//                List<Map<String, String>> deliveryList = new ArrayList<Map<String, String>>();
//                Map<String, String> standardNameIdMap = new HashMap<String, String>();
//                Map<String, String> deliveryMap = new HashMap<String, String>();
//                List<DeliveryStandard> standards = sessionFacade.findHql("from DeliveryStandard");
//                for (DeliveryStandard d : standards) {
//                    standardNameIdMap.put(d.getName(), d.getId());
//                }
//                for (String name : names.split(",")) {
//                    deliveryMap = new HashMap<String, String>();
//                    if (!CommonUtil.isEmpty(standardNameIdMap)
//                        && !CommonUtil.isEmpty(standardNameIdMap.get(name))) {
//                        deliveryMap.put("deliveryId", standardNameIdMap.get(name));
//                        deliveryMap.put("deliveryName", name);
//                        deliveryList.add(deliveryMap);
//                    }
//                }
//              
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("parentPlanId", plan.getParentPlanId());
//                map.put("planId", plan.getId());
//                map.put("useId", UserUtil.getCurrentUser().getId());
//                map.put("deliveryIds", deliveryList);
//                for (String url : httpUrls) {
//                    try {
//                        HttpClientUtil.httpClientPostByTest(url + "&deleteType=node", map);
//                    }
//                    catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
        }

    }

    @Override
    public void doAdd(String type, String names, String userId, List<Plan> childList) {
        TSUserDto curUser = userService.getUserByUserId(userId);
        if ("INPUT".equals(type)) {
            for (String name : names.split(",")) {
                Inputs input = new Inputs();
                input.setName(name);
                input.setUseObjectId(childList.get(0).getId());
                input.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                CommonUtil.glObjectSet(input);
                input.setOriginTypeExt(PlanConstants.DELIEVER_EN);
                input.setCreateBy(curUser.getId());
                input.setCreateFullName(curUser.getRealName());
                input.setCreateName(curUser.getUserName());
                input.setCreateTime(new Date());
                inputsService.save(input);

                // 同步研发流程任务输出
                InputsDto inputsDto = new  InputsDto();
                try {
                    BeanUtils.copyProperties(inputsDto, input);
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                rdFlowTaskFlowResolveService.setInputsAddOutInputTask(inputsDto, userId);

//                RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//                rdfConfigSupport.setInputsAddOutInputTask(userId, input.getName(),
//                    input.getUseObjectId(), input.getOriginTypeExt(), input.getId());
            }
        }
        else {
            for (String name : names.split(",")) {
                DeliverablesInfo output = new DeliverablesInfo();
                deliverablesInfoService.initBusinessObject(output);
                output.setName(name);
                output.setUseObjectId(childList.get(0).getId());
                output.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                output.setCreateBy(curUser.getId());
                output.setCreateFullName(curUser.getRealName());
                output.setCreateName(curUser.getUserName());
                output.setCreateTime(new Date());
                deliverablesInfoService.saveDeliverablesInfo(output);

                // 同步研发流程任务输出
                rdFlowTaskFlowResolveService.saveRdfDeliverByPlan(childList.get(0).getParentPlanId(),
                  childList.get(0).getCellId(), name, userId, output.getId());

//                RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//                rdfConfigSupport.saveRdfDeliverByPlan(childList.get(0).getParentPlanId(),
//                    childList.get(0).getCellId(), name, userId, output.getId());
            }
        }

    }

    @Override
    public void doAddInputs(String ids, Inputs inputs) {
        List<DeliverablesInfo> selectedList = deliverablesInfoService.getSelectedPreposePlanDeliverables(ids);
        for (DeliverablesInfo selected : selectedList) {
            Inputs temp = new Inputs();
            temp.setName(selected.getName());
            temp.setUseObjectId(inputs.getUseObjectId());
            temp.setUseObjectType(inputs.getUseObjectType());
            temp.setOriginObjectId(selected.getUseObjectId());
            temp.setOriginDeliverablesInfoId(selected.getId());
            inputsService.save(temp);
        }
    }

    @Override
    public void doBatchSaveBasicInfo(String fromTemplate, Plan task, String planStartTime,
                                     String planEndTime, String workTime, boolean nameChange) {
        if (!"true".equals(fromTemplate) && nameChange) {
            List<DeliverablesInfo> list2 = deliverablesInfoService.getDeliverablesByUseObeject(
                "PLAN", task.getId());

            // String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
//            String switchStr = "";
//            try {
//                RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//                String switchStrJson = rdfConfigSupport.getSwitchfromRdFlow(SwitchConstants.NAMESTANDARDSWITCH);
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().create();
//                AjaxJson ajaxJson = gson.fromJson(switchStrJson, AjaxJson.class);
//                switchStr = (String)ajaxJson.getObj();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
            FeignJson switchStrJson = rdFlowTaskFlowResolveService.getSwitchfromRdFlow(SwitchConstants.NAMESTANDARDSWITCH);
            String switchStr = switchStrJson.getObj().toString();
            NameStandardDto condition2 = new NameStandardDto();
            condition2.setStopFlag("启用");
            condition2.setName(task.getPlanName());
            List<NameStandardDto> nameStandardList2 = nameStandardService.searchNameStandards(condition2);
            if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                || NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                if (nameStandardList2.size() > 0) {
                    for (NameStandardDto n : nameStandardList2) {
                        if ("启用".equals(n.getStopFlag())) {
                            NameStandardDeliveryRelationDto relation = new NameStandardDeliveryRelationDto();
                            relation.setNameStandardId(n.getId());
                            List<NameStandardDeliveryRelationDto> list = nameStandardDeliveryRelationService.searchForPage(
                                relation, 1, 10);
                            deliverablesInfoService.deleteDeliverablesByPlanId(task.getId());

                            if (list.size() > 0) {
                                for (NameStandardDeliveryRelationDto r : list) {
                                    if(!CommonUtil.isEmpty(r.getDeliveryStandardId())) {
                                        r.setDeliveryStandard(deliveryStandardService.getDeliveryStandardEntity(r.getDeliveryStandardId()));
                                    }
                                    if (r.getDeliveryStandard() != null) {
                                        int a = 0;
                                        for (DeliverablesInfo d : list2) {
                                            if (r.getDeliveryStandard().getName().equals(
                                                d.getName())) {
                                                a = 1;
                                                break;
                                            }
                                        }
                                        if (a == 0 && r.getDeliveryStandardId() != null) {
                                            DeliverablesInfo deliverablesInfoTemp = new DeliverablesInfo();
                                            deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
                                            deliverablesInfoTemp.setId(null);
                                            deliverablesInfoTemp.setName(r.getDeliveryStandard().getName());
                                            deliverablesInfoTemp.setUseObjectId(task.getId());
                                            deliverablesInfoTemp.setUseObjectType("PLAN");
                                            deliverablesInfoTemp.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                                            deliverablesInfoService.saveDeliverablesInfo(deliverablesInfoTemp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Date startTime = DateUtil.stringtoDate(planStartTime, DateUtil.LONG_DATE_FORMAT);
        Date endTime = DateUtil.stringtoDate(planEndTime, DateUtil.LONG_DATE_FORMAT);

        if ((startTime.getTime() != task.getPlanEndTime().getTime())
            || (endTime.getTime() != task.getPlanStartTime().getTime())) {
            ResourceLinkInfoDto resourceLinkInfo = new ResourceLinkInfoDto();
            resourceLinkInfo.setUseObjectId(task.getId());// 关连的外键id
            resourceLinkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);// 关连的外键type
            List<ResourceLinkInfoDto> res = resourceLinkInfoService.queryResourceList(
                resourceLinkInfo, 1, 10, false);// 一个任务对应的资源列表

            if (!CommonUtil.isEmpty(res)) {
                for (ResourceLinkInfoDto linkInfo : res) {
                    linkInfo.setStartTime(startTime);
                    linkInfo.setEndTime(endTime);
                }

//                resourceLinkInfoService.updateResourceLinkInfoTimeByDto(res);
            }
        }

        task.setPlanStartTime(DateUtil.stringtoDate(planStartTime, DateUtil.LONG_DATE_FORMAT));
        task.setPlanEndTime(DateUtil.stringtoDate(planEndTime, DateUtil.LONG_DATE_FORMAT));
        task.setWorkTime(workTime);
        planService.initBusinessObject(task);
        sessionFacade.saveOrUpdate(task);

    }

    @Override
    public void deleteLineConnect(Plan to, Plan from) {
        Inputs inputs = new Inputs();
        inputs.setUseObjectId(to.getId());
        inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
        inputs.setOriginObjectId(from.getId());
        List<Inputs> list = inputsService.queryInputList(inputs, 1, 10, false);
        boolean needHttp = false;
        if (!CommonUtil.isEmpty(to.getTaskNameType())
            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(to.getTaskNameType())
            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(to.getTaskNameType())
            && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(to.getTaskNameType())) {
            needHttp = true;
        }
        if (needHttp) {
            inputsService.batchUpdateInputsAttribute("fromDeliverId", list);
            //TODO..
//            List<String> httpUrls = new ArrayList<String>();
//            List<OutwardExtension> outwardExtensionList = outwardExtensionService.getOutwardExtensionList("flowResolveCategoryHttpServer");
//            if (!CommonUtil.isEmpty(outwardExtensionList)) {
//                for (OutwardExtension ext : outwardExtensionList) {
//                    if (to.getTaskNameType().contains(ext.getOptionValue())
//                        && !CommonUtil.isEmpty(ext.getUrlList())) {
//                        for (OutwardExtensionUrl out : ext.getUrlList()) {
//                            if ("deleteLine".equals(out.getOperateCode())) {
//                                httpUrls.add(out.getOperateUrl());
//                            }
//                        }
//                    }
//                }
//            }
//            if (!CommonUtil.isEmpty(httpUrls)) {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("parentPlanId", from.getParentPlanId());
//                map.put("fromPlanId", from.getId());
//                map.put("toPlanId", to.getId());
//                for (String url : httpUrls) {
//                    try {
//                        HttpClientUtil.httpClientPostByTest(url, map);
//                    }
//                    catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
        }
        else {
            inputsService.deleteAllEntitie(list);
        }
    }

    @Override
    public void deleteLinkInput(Plan to, List<Plan> toList, Plan from) {
        to = toList.get(0);
        Inputs inputs = new Inputs();
        inputs.setUseObjectId(to.getId());
        inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
        inputs.setOrigin(from.getPlanName());

        List<Inputs> list = inputsService.queryInputList(inputs, 1, 10, false);
        for (Inputs in : list) {
            inputsService.delete(in);
        }
    }

    @Override
    public void deleteSelectedCell(String parentPlanId, String cellId) {
        List<Plan> childList = new ArrayList<Plan>();
        Plan plan = new Plan();
        plan.setParentPlanId(parentPlanId);
        plan.setCellId(cellId);
        childList = planService.queryPlanList(plan, 1, 10, false);
        if (!CommonUtil.isEmpty(childList)) {
            Plan child = childList.get(0);
            Inputs inputs = new Inputs();
            inputs.setUseObjectId(child.getId());
            inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            List<Inputs> inputList = inputsService.queryInputList(inputs, 1, 10, false);
            for (Inputs input : inputList) {
                inputsService.delete(input);
            }
            // 删除该节点的输出
            DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
            deliverablesInfo.setUseObjectId(child.getId());
            deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            List<DeliverablesInfo> outputList = deliverablesInfoService.queryDeliverableList(
                deliverablesInfo, 1, 10, false);
            for (DeliverablesInfo output : outputList) {
                sessionFacade.delete(output);
            }
            // 删除该节点输出的传递
            Plan p = new Plan();
            p.setPreposeIds(child.getId());
            List<PreposePlan> postposeList = preposePlanService.getPostposesByPreposeId(p);
            for (PreposePlan preposePlan : postposeList) {
                if (StringUtils.isNotEmpty(preposePlan.getPlanId())) {
                    Plan prepose = (Plan)sessionFacade.getEntity(Plan.class, preposePlan.getPlanId());
                    if (prepose != null) {
                        Inputs postposeIn = new Inputs();
                        postposeIn.setUseObjectId(prepose.getId());
                        postposeIn.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        postposeIn.setOrigin(child.getPlanName());
                        List<Inputs> postposeInputList = inputsService.queryInputList(postposeIn,
                            1, 10, false);
                        for (Inputs postposeInput : postposeInputList) {
                            inputsService.delete(postposeInput);
                        }
                    }
                }
                preposePlanService.delete(preposePlan);
            }
            // 删除与该节点相关的前置关系
            List<PreposePlan> preposeList = preposePlanService.getPreposePlansByPlanId(child);
            for (PreposePlan prepose : preposeList) {
                preposePlanService.delete(prepose);
            }

            // 删除资源
            ResourceLinkInfoDto resource = new ResourceLinkInfoDto();
            resource.setUseObjectId(child.getId());
            resource.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            List<ResourceLinkInfoDto> resourceLinkInfoList = resourceLinkInfoService.getList(resource);
            for (ResourceLinkInfoDto resourceLinkInfo : resourceLinkInfoList) {
                sessionFacade.delete(resourceLinkInfo);
            }
            resourceService.delResourceUseByPlan(child.getId());

            // 删除其子孙计划
            List<Plan> childs = planService.getPlanAllChildren(child);
            for (Plan c : childs) {
                // 删除其相关的交付物
                deliverablesInfoService.deleteDeliverablesPhysicsByPlan(c);
                // 删除其相关的资源
                ResourceLinkInfoDto linkInfo = new ResourceLinkInfoDto();
                linkInfo.setUseObjectId(c.getId());
                linkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                resourceLinkInfoService.deleteResourceByCondition(linkInfo);
                resourceService.delResourceUseByPlan(c.getId());
                // 删除其相关输入
                inputsService.deleteInputsByPlan(c);
                // 删除其相关前后置
                List<PreposePlan> preposeList1 = preposePlanService.getPreposePlansByPlanId(c);
                for (PreposePlan prepose : preposeList1) {
                    deleteEntityById(PreposePlan.class, prepose.getId());
                }
                Plan childp = new Plan();
                childp.setPreposeIds(c.getId());
                List<PreposePlan> preposeList2 = preposePlanService.getPostposesByPreposeId(childp);
                for (PreposePlan prepose : preposeList2) {
                    deleteEntityById(PreposePlan.class, prepose.getId());
                }
                delete(c);
            }

            // 删除不存在的节点
            sessionFacade.delete(child);
        }
    }

    public void saveOutPreposePlan(String preposeIds, String useObjectId) {
        String[] a = preposeIds.split(",");
        if (a.length > 0) {
            for (int i = 0; i < a.length; i++ ) {
                String id = a[i];
                PreposePlan outplan = new PreposePlan();
                outplan.setPreposePlanId(id);
                outplan.setPlanId(useObjectId);
                int num = preposePlanService.searchPrepose(outplan).size();
                if (num == 0) {
                    saveOrUpdate(outplan);
                }
            }
        }

    }

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

            String projectTimeType = parentPlan.getProject().getProjectTimeType();
            String parentWorkTime = parentPlan.getWorkTime();


            // List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//            RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//            RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//            String maxWorkTimeWithTemplateInfo = rdfConfigSupport.getMaxWorkTimeByTemp(templateId);

            // String tptmplStr = rdfConfigSupport.getTaskProcTemplateEntity(templateId);
//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().create();
//            AjaxJson ajaxJson = gson.fromJson(maxWorkTimeWithTemplateInfo, AjaxJson.class);
//            String maxWorkTimeWithTemplate = (String)ajaxJson.getObj();
            // AjaxJson ajaxJson = gson.fromJson(tptmplStr, AjaxJson.class);
            // mapList = (List<Map<String, Object>>)ajaxJson.getObj();
            TaskProcTemplateDto tptmpl = new TaskProcTemplateDto();
            // TaskProcTemplate tptmpl = planService.getEntity(TaskProcTemplate.class, templateId);

            tptmpl = rdFlowTaskFlowResolveService.getProcTemplateEntity(templateId);
//            tptmpl = gson.fromJson(procTemplatelist, TaskProcTemplateVO.class);

            if (!CommonUtil.isEmpty(tptmpl) && !CommonUtil.isEmpty(tptmpl.getId())) {

                FeignJson maxWorkTimeWithTemplateInfoJson = rdFlowTaskFlowResolveService.getMaxWorkTimeByTemp(templateId);
                String maxWorkTimeWithTemplate = maxWorkTimeWithTemplateInfoJson.getObj().toString();
                // for (Map<String, Object> map : mapList) {
                // tptmpl.setId(StringUtils.isEmpty((String)map.get("id")) ? "" :
                // map.get("id").toString());
                // tptmpl.setTemlXml(StringUtils.isEmpty((String)map.get("temlXml")) ? "" :
                // map.get(
                // "temlXml").toString());
                // tptmpl.setBizId(StringUtils.isEmpty((String)map.get("bizId")) ? "" : map.get(
                // "bizId").toString());
                // }
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
                              }
                          }
                      }
//                  }
//                Map<String,List<TSTypeDto>> tsMap = feignSystemService.getAllTypesOfTypeGroup(ResourceUtil.getApplicationInformation().getAppKey());
//                List<TSTypeDto> types = tsMap.get(dictCode);
//                if (!CommonUtil.isEmpty(types)) {
//                    defaultNameType = types.get(0).getTypecode();
//                }

                // 获取当前系统是否启用标准名称库
                // String switchStr =
                // paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
                String switchStr = "";
                try {

                    switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
//                    FeignJson switchStrJson = rdFlowTaskFlowResolveService.getSwitchfromRdFlow(SwitchConstants.NAMESTANDARDSWITCH);
//                    switchStr = (String)switchStrJson.getObj();
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

                // TaskProcTemCellConnect cellConnect = new TaskProcTemCellConnect();
                // cellConnect.setTaskId(templateId);
                // List<TaskProcTemCellConnect> taskProcTemCellConnList =
                // taskProcTemCellConnService.findTaskProcTemCellConnList(cellConnect, 1, 10,
                // false);

                List<Map<String, Object>> mapList1 = new ArrayList<Map<String, Object>>();
                List<TaskProcTemCellConnectVO> taskProcTemCellConnList = new ArrayList<TaskProcTemCellConnectVO>();
                taskProcTemCellConnList = rdFlowTaskFlowResolveService.findTaskProcTemCellConnList(templateId);
//                AjaxJson ajaxJson1 = gson.fromJson(taskProcTemCellConnListStr, AjaxJson.class);
//                mapList1 = (List<Map<String, Object>>)ajaxJson1.getObj();
//                if (!CommonUtil.isEmpty(taskProcTemCellConnListStr)) {
//                    for (Map<String, Object> map : mapList1) {
//                        TaskProcTemCellConnectVO taskProcTemCellConnectVo = new TaskProcTemCellConnectVO();
//                        taskProcTemCellConnectVo.setId(StringUtils.isEmpty((String)map.get("id")) ? "" : map.get(
//                            "id").toString());
//                        taskProcTemCellConnectVo.setInfoId(StringUtils.isEmpty((String)map.get("infoId")) ? "" : map.get(
//                            "infoId").toString());
//                        taskProcTemCellConnectVo.setCellId(StringUtils.isEmpty((String)map.get("cellId")) ? "" : map.get(
//                            "cellId").toString());
//                        taskProcTemCellConnectVo.setTargetId(StringUtils.isEmpty((String)map.get("targetId")) ? "" : map.get(
//                            "targetId").toString());
//                        taskProcTemCellConnectVo.setTargetInfoId(StringUtils.isEmpty((String)map.get("targetInfoId")) ? "" : map.get(
//                            "targetInfoId").toString());
//                        taskProcTemCellConnectVo.setTaskId(StringUtils.isEmpty((String)map.get("taskId")) ? "" : map.get(
//                            "taskId").toString());
//                        taskProcTemCellConnList.add(taskProcTemCellConnectVo);
//                    }
//                }

                Map<String, List<TaskProcTemCellConnectVO>> connMap = new HashMap<String, List<TaskProcTemCellConnectVO>>();
                for (TaskProcTemCellConnectVO conn : taskProcTemCellConnList) {
                    List<TaskProcTemCellConnectVO> connList = new ArrayList<TaskProcTemCellConnectVO>();
                    if (!CommonUtil.isEmpty(connMap.get(conn.getCellId()))) {
                        connList = connMap.get(conn.getCellId());
                    }
                    connList.add(conn);
                    connMap.put(conn.getCellId(), connList);
                }

                // List<TaskCellDeliverItem> itemsInputList =
                // taskCellDeliverItemService.getTemplateInputs(templateId);

                List<Map<String, Object>> mapList2 = new ArrayList<Map<String, Object>>();
                List<TaskCellDeliverItemVO> itemsInputList = new ArrayList<TaskCellDeliverItemVO>();
                itemsInputList = rdFlowTaskFlowResolveService.getTemplateInputs(templateId);
//                AjaxJson ajaxJson2 = gson.fromJson(itemsInputListStr, AjaxJson.class);
//                mapList2 = (List<Map<String, Object>>)ajaxJson2.getObj();
//                if (!CommonUtil.isEmpty(itemsInputListStr)) {
//                    for (Map<String, Object> map : mapList2) {
//                        TaskCellDeliverItemVO taskCellDeliverItemVo = new TaskCellDeliverItemVO();
//                        taskCellDeliverItemVo.setId(StringUtils.isEmpty((String)map.get("id")) ? "" : map.get(
//                            "id").toString());
//                        taskCellDeliverItemVo.setCellId(StringUtils.isEmpty((String)map.get("cellId")) ? "" : map.get(
//                            "cellId").toString());
//                        taskCellDeliverItemVo.setDeliverId(StringUtils.isEmpty((String)map.get("deliverId")) ? "" : map.get(
//                            "deliverId").toString());
//                        taskCellDeliverItemVo.setDeliverName(StringUtils.isEmpty((String)map.get("deliverName")) ? "" : map.get(
//                            "deliverName").toString());
//                        taskCellDeliverItemVo.setFromDeliverId(StringUtils.isEmpty((String)map.get("fromDeliverId")) ? "" : map.get(
//                            "fromDeliverId").toString());
//                        taskCellDeliverItemVo.setOriginObjectId(StringUtils.isEmpty((String)map.get("originObjectId")) ? "" : map.get(
//                            "originObjectId").toString());
//                        taskCellDeliverItemVo.setOriginType(StringUtils.isEmpty((String)map.get("originType")) ? "" : map.get(
//                            "originType").toString());
//                        taskCellDeliverItemVo.setFileName(StringUtils.isEmpty((String)map.get("fileName")) ? "" : map.get(
//                            "fileName").toString());
//                        taskCellDeliverItemVo.setFilePath(StringUtils.isEmpty((String)map.get("filePath")) ? "" : map.get(
//                            "filePath").toString());
//                        taskCellDeliverItemVo.setUserObjectId(StringUtils.isEmpty((String)map.get("userObjectId")) ? "" : map.get(
//                            "userObjectId").toString());
//                        taskCellDeliverItemVo.setDeliverType(StringUtils.isEmpty((String)map.get("deliverType")) ? "" : map.get(
//                            "deliverType").toString());
//                        itemsInputList.add(taskCellDeliverItemVo);
//                    }
//                }

                Map<String, List<TaskCellDeliverItemVO>> itemsInputMap = new HashMap<String, List<TaskCellDeliverItemVO>>();
                for (TaskCellDeliverItemVO in : itemsInputList) {
                    List<TaskCellDeliverItemVO> inList = new ArrayList<TaskCellDeliverItemVO>();
                    if (!CommonUtil.isEmpty(itemsInputMap.get(in.getCellId()))) {
                        inList = itemsInputMap.get(in.getCellId());
                    }
                    inList.add(in);
                    itemsInputMap.put(in.getCellId(), inList);
                }

                // List<TaskCellDeliverItem> itemsOutputList =
                // taskCellDeliverItemService.getTemplateOutputs(templateId);
                List<Map<String, Object>> mapList3 = new ArrayList<Map<String, Object>>();
                List<TaskCellDeliverItemVO> itemsOutputList = new ArrayList<TaskCellDeliverItemVO>();
                itemsOutputList = rdFlowTaskFlowResolveService.getTemplateOutputs(templateId);
//                AjaxJson ajaxJson3 = gson.fromJson(itemsOutputListStr, AjaxJson.class);
//                mapList3 = (List<Map<String, Object>>)ajaxJson3.getObj();
//                if (!CommonUtil.isEmpty(itemsOutputListStr)) {
//                    for (Map<String, Object> map : mapList3) {
//                        TaskCellDeliverItemVO taskCellDeliverItemVo = new TaskCellDeliverItemVO();
//                        taskCellDeliverItemVo.setId(StringUtils.isEmpty((String)map.get("id")) ? "" : map.get(
//                            "id").toString());
//                        taskCellDeliverItemVo.setCellId(StringUtils.isEmpty((String)map.get("cellId")) ? "" : map.get(
//                            "cellId").toString());
//                        taskCellDeliverItemVo.setDeliverId(StringUtils.isEmpty((String)map.get("deliverId")) ? "" : map.get(
//                            "deliverId").toString());
//                        taskCellDeliverItemVo.setDeliverName(StringUtils.isEmpty((String)map.get("deliverName")) ? "" : map.get(
//                            "deliverName").toString());
//                        taskCellDeliverItemVo.setIsOutputDeliverRequired(StringUtils.isEmpty((String)map.get("isOutputDeliverRequired")) ? "" : map.get(
//                            "isOutputDeliverRequired").toString());
//                        taskCellDeliverItemVo.setOriginObjectId(StringUtils.isEmpty((String)map.get("originObjectId")) ? "" : map.get(
//                            "originObjectId").toString());
//                        taskCellDeliverItemVo.setUserObjectId(StringUtils.isEmpty((String)map.get("userObjectId")) ? "" : map.get(
//                            "userObjectId").toString());
//                        itemsOutputList.add(taskCellDeliverItemVo);
//                    }
//                }

                Map<String, List<TaskCellDeliverItemVO>> itemsOutputMap = new HashMap<String, List<TaskCellDeliverItemVO>>();
                for (TaskCellDeliverItemVO out : itemsOutputList) {
                    List<TaskCellDeliverItemVO> outList = new ArrayList<TaskCellDeliverItemVO>();
                    if (!CommonUtil.isEmpty(itemsOutputMap.get(out.getCellId()))) {
                        outList = itemsOutputMap.get(out.getCellId());
                    }
                    outList.add(out);
                    itemsOutputMap.put(out.getCellId(), outList);
                }
                // TaskCellBasicInfo infoCondition = new TaskCellBasicInfo();
                // infoCondition.setTaskId(templateId);
                // List<TaskCellBasicInfo> infoList =
                // taskCellBaseInfoService.getTasKCellBaseInfoByTaskIdAndCellId(infoCondition);

                List<Map<String, Object>> mapList4 = new ArrayList<Map<String, Object>>();
                List<TaskCellBasicInfoVO> infoList = new ArrayList<TaskCellBasicInfoVO>();
                infoList = rdFlowTaskFlowResolveService.getTasKCellBaseInfoByTemplateId(templateId);
//                AjaxJson ajaxJson4 = gson.fromJson(infoListStr, AjaxJson.class);
//                mapList4 = (List<Map<String, Object>>)ajaxJson4.getObj();
//                if (!CommonUtil.isEmpty(infoListStr)) {
//                    for (Map<String, Object> map : mapList4) {
//                        TaskCellBasicInfoVO taskCellBasicInfoVO = new TaskCellBasicInfoVO();
//                        taskCellBasicInfoVO.setId(StringUtils.isEmpty((String)map.get("id")) ? "" : map.get(
//                            "id").toString());
//                        taskCellBasicInfoVO.setTaskId(StringUtils.isEmpty((String)map.get("taskId")) ? "" : map.get(
//                            "taskId").toString());
//                        taskCellBasicInfoVO.setFrontCellIds(StringUtils.isEmpty((String)map.get("frontCellIds")) ? "" : map.get(
//                            "frontCellIds").toString());
//                        taskCellBasicInfoVO.setCellName(StringUtils.isEmpty((String)map.get("cellName")) ? "" : map.get(
//                            "cellName").toString());
//                        if (!CommonUtil.isEmpty((String)map.get("refDurationStr"))) {
//                            taskCellBasicInfoVO.setRefDuration(Integer.parseInt(map.get(
//                                "refDurationStr").toString()));
//                        }
//                        taskCellBasicInfoVO.setIsCellRequired(StringUtils.isEmpty((String)map.get("isCellRequired")) ? "" : map.get(
//                            "isCellRequired").toString());
//                        taskCellBasicInfoVO.setCellRemark(StringUtils.isEmpty((String)map.get("cellRemark")) ? "" : map.get(
//                            "cellRemark").toString());
//                        taskCellBasicInfoVO.setNameStandardId(StringUtils.isEmpty((String)map.get("nameStandardId")) ? "" : map.get(
//                            "nameStandardId").toString());
//                        taskCellBasicInfoVO.setCellIndex(StringUtils.isEmpty((String)map.get("cellIndex")) ? "" : map.get(
//                            "cellIndex").toString());
//                        infoList.add(taskCellBasicInfoVO);
//                    }
//                }

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
                            plan.setTaskNameType(defaultNameType);
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
                                    // inputNameAndFromIdOutputIdMap.put(input.getName() +","+
                                    // inputItem.getOriginObjectId(),input.getName() +","+
                                    // inputItem.getOriginObjectId());
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
                for (Plan t : planlist) {
                    if (CommonUtil.isEmpty(taskIds)) {
                        taskIds = t.getId();
                    }
                    else {
                        taskIds = taskIds + "," + t.getId();
                    }

                    if (CommonUtil.isEmpty(workIds)) {
                        workIds = tptmpl.getBizId() + ":" + t.getCellId();
                    }
                    else {
                        workIds = workIds + "," + tptmpl.getBizId() + ":" + t.getCellId();
                    }
                }
//                KnowledgeInfoReq req = new KnowledgeInfoReq();
//                req.setTaskId(taskIds);
//                req.setWorkId(workIds);
//                String userId = UserUtil.getInstance().getUser().getId();
//                req.setUserId(userId);
//
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
                rdTaskVO.setPlanStartTimeStr(DateUtil.formatDate(parent.getPlanStartTime(),
                    DateUtil.FORMAT_ONE));
                rdTaskVO.setPlanStartTime(parent.getPlanStartTime());
                rdTaskVO.setPlanEndTimeStr(DateUtil.formatDate(parent.getPlanEndTime(),
                    DateUtil.FORMAT_ONE));
                rdTaskVO.setPlanEndTime(parent.getPlanEndTime());
                rdTaskVO.setWorkTime(parent.getWorkTime());
                rdTaskVO.setRemark(parent.getRemark());
                rdTaskVO.setAssigner(parent.getAssigner());
                rdTaskVO.setAssignTimeStr(DateUtil.formatDate(parent.getAssignTime(),
                    DateUtil.FORMAT_ONE));
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
                rdFlowTaskFlowResolveService.getSaveRdTaskInfo(rdTaskVO, templateId, outUserId, approveType, procInstId,
                    parent.getFormId(), planMapStr, inputMapStr, outputMapStr);
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
                rdTaskVO.setPlanStartTimeStr(DateUtil.formatDate(parent.getPlanStartTime(),
                    DateUtil.FORMAT_ONE));
                rdTaskVO.setPlanStartTime(parent.getPlanStartTime());
                rdTaskVO.setPlanEndTimeStr(DateUtil.formatDate(parent.getPlanEndTime(),
                    DateUtil.FORMAT_ONE));
                rdTaskVO.setPlanEndTime(parent.getPlanEndTime());
                rdTaskVO.setWorkTime(parent.getWorkTime());
                rdTaskVO.setRemark(parent.getRemark());
                rdTaskVO.setAssigner(parent.getAssigner());
                rdTaskVO.setAssignTimeStr(DateUtil.formatDate(parent.getAssignTime(),
                    DateUtil.FORMAT_ONE));
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
                rdFlowTaskFlowResolveService.getSaveRdTaskInfo(rdTaskVO, templateId, outUserId, approveType, procInstId,
                    parent.getFormId(), "", "", "");
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

    @Override
    public void deleteFlowTaskCellConn(String parentPlanId) {
        String hql = "delete from PM_FLOW_TASK_CELL_CONNECT t where t.parentPlanId = '"
                + parentPlanId + "'";
        // Map<String, Object> param = new HashMap<String, Object>();
        // param.put("parentPlanId", parentPlanId);
        sessionFacade.executeSql2(hql);
    }

    @Override
    public List<ChangeFlowTaskCellConnectVo> getChangeFlowTaskConnectList(Plan plan)  throws GWException {
        List<ChangeFlowTaskCellConnectVo> list = new ArrayList<ChangeFlowTaskCellConnectVo>();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select t.id,t.cellId,t.formid,t.infoid,t.parentplanid,t.targetid,t.targetinfoid from PM_CHANGE_FLOWTASK_CONNECT t where 1=1");
        if (!StringUtils.isEmpty(plan.getFormId())) {
            hqlBuffer.append(" and t.formId = '" + plan.getFormId() + "'");
        }
        if (!StringUtils.isEmpty(plan.getParentPlanId())) {
            hqlBuffer.append(" and t.parentPlanId = '" + plan.getParentPlanId() + "'");
        }

        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    ChangeFlowTaskCellConnectVo iitem = new ChangeFlowTaskCellConnectVo();
                    iitem.setId(id);
                    iitem.setFormId((String)map.get("formid"));
                    iitem.setCellId((String)map.get("cellId"));
                    iitem.setInfoId((String)map.get("infoid"));
                    iitem.setParentPlanId((String)map.get("parentplanid"));
                    iitem.setTargetId((String)map.get("targetid"));
                    iitem.setTargetInfoId((String)map.get("targetinfoid"));
                    list.add(iitem);
                }
            }
        }
        return list;
    }

    @Override
    public List<FlowTaskPreposeVo> getChangeFlowTaskOutPreposeList(FlowTaskVo task) {
        List<FlowTaskPreposeVo> list = new ArrayList<FlowTaskPreposeVo>();
        if (StringUtils.isNotEmpty(task.getId())) {
            StringBuffer hqlBuffer = new StringBuffer();
            hqlBuffer.append("select a.id,a.flowtaskid,a.preposeid,a.formid from PM_FLOWTASK_PREPOSE  a where a.flowTaskId = '"
                    + task.getId() + "' ");
            if (!StringUtils.isEmpty(task.getFormId())) {
                hqlBuffer.append(" and a.formId = '" + task.getFormId() + "' ");
            }
            List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
            if (!CommonUtil.isEmpty(objArrayList)) {
                for (Map<String, Object> map : objArrayList) {
                    String id = (String)map.get("id");
                    if (StringUtils.isNotEmpty(id)) {
                        FlowTaskPreposeVo iitem = new FlowTaskPreposeVo();
                        iitem.setId(id);
                        iitem.setFlowTaskId((String)map.get("flowtaskid"));
                        iitem.setFormId((String)map.get("formid"));
                        iitem.setPreposeId((String)map.get("preposeid"));
                        list.add(iitem);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<FlowTaskCellConnectVo> getFlowTaskConnectList(String parentId) {
        List<FlowTaskCellConnectVo> list = new ArrayList<FlowTaskCellConnectVo>();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select c.id,c.cellid,c.formid,c.infoid,c.parentplanid,c.targetid,c.targetinfoid from PM_FLOW_TASK_CELL_CONNECT c");
        hqlBuffer.append(" where 1=1 AND c.parentPlanId = '" + parentId + "' ");

        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    FlowTaskCellConnectVo iitem = new FlowTaskCellConnectVo();
                    iitem.setId(id);
                    iitem.setCellId((String)map.get("cellid"));
                    iitem.setFormId((String)map.get("formid"));
                    iitem.setInfoId((String)map.get("infoid"));
                    iitem.setParentPlanId((String)map.get("parentplanid"));
                    iitem.setTargetId((String)map.get("targetid"));
                    iitem.setTargetInfoId((String)map.get("targetinfoid"));
                    list.add(iitem);
                }
            }
        }
        return list;
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
                    plan.setTaskNameType(defaultNameType);
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

                            Date date = (Date)startTime.clone();
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

    @Override
    public void startChangeApplyProcess(String leader,String userId,
                                        PlanownerApplychangeInfo planownerApplychangeInfo) {
        TSUserDto actor = userService.getUserByUserId(userId);
        actor.setId(actor.getId());
        actor.setUserName(actor.getUserName());
        actor.setRealName(actor.getRealName());

        planService.initBusinessObject(planownerApplychangeInfo);
        sessionFacade.saveOrUpdate(planownerApplychangeInfo);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", actor.getUserName());

        FeignJson proFj = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                BpmnConstants.BPMN_CHANGE_APLY, planownerApplychangeInfo.getId(), variables);
        String procInstId = ""; // 流程实例ID

        if (proFj.isSuccess()) {
            procInstId = proFj.getObj() == null ? "" : proFj.getObj().toString();
        }

        FeignJson taskIdFj = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(procInstId);
        String taskId = "";

        if (taskIdFj.isSuccess()) {
            taskId = taskIdFj.getObj() == null ? "" : taskIdFj.getObj().toString();
        }

        variables.put("cancelEventListener",
                "com.glaway.foundation.activiti.task.common.CommonCancelListener");
        variables.put("leader", leader);
        variables.put("assigner", actor.getUserName());

        variables.put("viewUrl",
                PlanConstants.URL_CHANGE_APPLY_VIEW + planownerApplychangeInfo.getId());
        variables.put("editUrl",
                PlanConstants.URL_CHANGE_APPLY_EDIT + planownerApplychangeInfo.getId());

        variables.put("oid", BpmnConstants.OID_CHANGEAPPLY + planownerApplychangeInfo.getId());
        variables.put("userId",userId);
        // 执行流程
        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

        FeignJson nextTasksFj = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = "";

        if (nextTasksFj.isSuccess()) {
            status = nextTasksFj.getObj() == null ? "" : nextTasksFj.getObj().toString();
        }

        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
        myStartedTask.setTaskNumber(planownerApplychangeInfo.getId());
        myStartedTask.setType(BpmnConstants.BPMN_CHANGE_APLY);
        myStartedTask.setObjectName(null);
        myStartedTask.setTitle(ProcessUtil.getProcessTitle("",
                BpmnConstants.BPMN_START_APPLY_CHANGENAME, procInstId));
        myStartedTask.setCreater(actor.getUserName());
        myStartedTask.setCreaterFullname(actor.getRealName());
        myStartedTask.setStartTime(new Date());
        myStartedTask.setProcType(BpmnConstants.BPMN_CHANGE_APLY);
        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);
    }

    @Override
    public void cancelChangeApplyForWorkFlow(String formId, String taskId) {
        // 执行流程
        ApprovePlanForm approvePlanForm = getEntity(ApprovePlanForm.class, formId);
        TaskDto task = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
        String procInstId = task.getProcessInstanceId();
        workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(formId, procInstId, "");

        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                formId);
        workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask.getTaskId()));
    }

    @Override
    public FeignJson startChangeApplyProcess(PlanownerApplychangeInfo planownerApplychangeInfo, String leader, String userId) {
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.startChangeApplyProcessSuccess");
        try {

            leader = URLDecoder.decode(leader, "UTF-8");

            startChangeApplyProcess(leader, userId, planownerApplychangeInfo);
            log.info(message, leader, "");
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.startChangeApplyProcessFailure");
            j.setSuccess(false);
            log.error(message, e, leader, "");
            Object[] params = new Object[] {message, Plan.class.getClass() + " oids:" + leader};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson startChangeApplyForWorkFlow(Map<String, Object> map) {
        PlanownerApplychangeInfo planownerApplychangeInfo = new PlanownerApplychangeInfo();
        ObjectMapper mapper = new ObjectMapper();
        PlanownerApplychangeInfoDto dto = mapper.convertValue(map.get("dto"),PlanownerApplychangeInfoDto.class);
        Dto2Entity.copyProperties(dto,planownerApplychangeInfo);
        String formId = map.get("formId") == null ? "" : map.get("formId").toString();
        String taskId = map.get("taskId") == null ? "" : map.get("taskId").toString();
        String userId = map.get("userId") == null ? "" : map.get("userId").toString();
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.startChangeApplyProcessSuccess");
        try {

            startChangeApplyForWorkFlow(planownerApplychangeInfo, formId,
                    taskId,userId);

            log.info(message, formId, "");
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.startChangeApplyProcessFailure");
            log.error(message, e, "", "");
            Object[] params = new Object[] {message, ApprovePlanForm.class.getClass() + " oids:"};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson cancelChangeApplyForWorkFlow(Map<String, Object> map) {
        FeignJson j = new FeignJson();
        String formId = map.get("formId") == null ? "" : map.get("formId").toString();
        String taskId = map.get("taskId") == null ? "" : map.get("taskId").toString();
        String message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.startChangeApplyProcessCancelSuccess");
        try {

            cancelChangeApplyForWorkFlow(formId, taskId);

            log.info(message, formId, "");
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.startChangeApplyProcessCancelFailure");
            log.error(message, e, "", "");
            Object[] params = new Object[] {message, ApprovePlanForm.class.getClass() + " oids:"};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public void startChangeApplyForWorkFlow(PlanownerApplychangeInfo planownerApplychangeInfo,
                                            String formId, String taskId,String userId) {
        Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                taskId);
        TSUserDto userDto = userService.getUserByUserId(userId);
        variables.put("user", userDto.getUserName());
        // 执行流程
        TaskDto task = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
        String procInstId = task.getProcessInstanceId();
        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

        FeignJson nextTasksFj = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = "";

        if (nextTasksFj.isSuccess()) {
            status = nextTasksFj.getObj() == null ? "" : nextTasksFj.getObj().toString();
        }

        PlanownerApplychangeInfo p = getEntity(PlanownerApplychangeInfo.class,
                planownerApplychangeInfo.getId());
        p.setChangeType(planownerApplychangeInfo.getChangeType());
        p.setChangeRemark(planownerApplychangeInfo.getChangeRemark());
        saveOrUpdate(p);

        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                formId);
        myStartedTask.setEndTime(new Date());
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
    }


    @Override
    public void goBackFlowResolveSetPlanInfo(String formId) {
        ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
        approvePlanInfo.setFormId(formId);
        String parentPlanId = "";
        List<ApprovePlanInfo> approve = planService.queryAssignList(approvePlanInfo, 1, 10, false);
        for (int i = 0; i < approve.size(); i++ ) {
            Plan p = (Plan)sessionFacade.getEntity(Plan.class, approve.get(i).getPlanId());
            p.setIsAssignBack("false");
            p.setIsAssignSingleBack("false");
            p.setFlowStatus("ORDERED");
            planService.saveOrUpdate(p);
            if(CommonUtil.isEmpty(parentPlanId)){
                parentPlanId = p.getParentPlanId();
            }
        }
        if(!CommonUtil.isEmpty(parentPlanId)){
            Plan parentPlanOld = (Plan)sessionFacade.getEntity(Plan.class, parentPlanId);
            parentPlanOld.setIsAssignSingleBack("false");
            planService.saveOrUpdate(parentPlanOld);
        }
    }

    @Override
    public void endPlanAssignExcution(String parentPlanId, String leaderId) {
        TSUserDto leader = userService.getUserByUserId(leaderId);
        List<Plan> childList = new ArrayList<Plan>();
        Plan parent = new Plan();
        parent.setParentPlanId(parentPlanId);
        childList = planService.queryPlanList(parent, 1, 10, false);
        if (childList.size() > 0) {
            ApprovePlanForm approvePlanForm = (ApprovePlanForm)sessionFacade.getEntity(ApprovePlanForm.class, childList.get(0).getFormId());
            if(!CommonUtil.isEmpty(approvePlanForm)){
                planService.planLifeCycleForward(childList.get(0).getFormId(), leader);
            }
//            for (Plan p : childList) {
//                p = planService.getEntity(Plan.class, p.getId());
//                p.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);                
//                p.setLauncher(leader.getId());
//                p.setLaunchTime(new Date());
//                p.setAssigner(leader.getId());
//                p.setAssignTime(new Date());
//                planService.saveOrUpdate(p);
//                planService.forwardBusinessObject(p);
//            }
        }
    }

    @Override
    public void planAssignBack(String parentPlanId) {
        Plan parentPlanOld = (Plan)sessionFacade.getEntity(Plan.class, parentPlanId);
        parentPlanOld.setIsAssignSingleBack("true");
        planService.saveOrUpdate(parentPlanOld);
        Plan planCon = new Plan();
        planCon.setParentPlanId(parentPlanId);
        List<Plan> list = planService.queryPlansExceptInvalid(planCon);
        for (Plan p : list) {
            Plan plan = (Plan)sessionFacade.getEntity(Plan.class, p.getId());
            plan.setIsAssignSingleBack("true");
            planService.saveOrUpdate(plan);
        }
    }

    @Override
    public void startAssignProcessSavePlanInfo(String procInstId,String parentPlanId ,String actorId ,String formId,String approveType) {

        TSUserDto actor = userService.getUserByUserId(actorId);
        // 将procInstId保存到approvePlanForm信息中
//        ApprovePlanForm approvePlanForm = new ApprovePlanForm();
//        approvePlanForm.setProcInstId(procInstId);
//        planService.saveOrUpdate(approvePlanForm);
        try {
            Connection conn = null;
            // 插入任务的formId到计划中
            if (!CommonUtil.isEmpty(formId)) {
                conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement psForApprovePlanForm = null;
                String sqlForApprovePlanForm = " insert into PM_APPROVE_PLAN_FORM (ID, approveType, procInstId) values (?, ?, ?)";
                psForApprovePlanForm = conn.prepareStatement(sqlForApprovePlanForm);
                psForApprovePlanForm.setObject(1, formId);
                psForApprovePlanForm.setObject(2, approveType);
                psForApprovePlanForm.setObject(3, procInstId);
                psForApprovePlanForm.execute();
                DBUtil.closeConnection(null, psForApprovePlanForm, conn);
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //查询出父计划的状态，如果是拟制中，父计划也要提交：
        Plan parentPlan = (Plan)sessionFacade.getEntity(Plan.class, parentPlanId);
        if(!CommonUtil.isEmpty(parentPlan) && "EDITING".equals(parentPlan.getBizCurrent())) {
            if ("1".equals(parentPlan.getAvaliable()) && !CommonUtil.isEmpty(parentPlan) && !CommonUtil.isEmpty(parentPlan.getFormId())) {
                //如果存在待办的拟制中父计划提交，删除待办流程：
                MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                    parentPlan.getFormId());
                List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(myStartedTask.getProcInstId(), actor.getUserName());
                String taskId = tasks.get(0).getId();
                TaskDto taskIn = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
                String oldProcInstId = taskIn.getProcessInstanceId();

                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(parentPlan.getId(),
                    oldProcInstId, "delete");
            }
            // 计划审批相关信息表中存放计划与approvePlanForm关系
            ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
            approvePlanInfo.setPlanId(parentPlan.getId());
            approvePlanInfo.setFormId(formId);
            sessionFacade.saveOrUpdate(approvePlanInfo);
            parentPlan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_ORDERED);
            parentPlan.setFormId(formId);
            parentPlan.setIsAssignSingleBack("false");
            parentPlan.setLauncher(actor.getId());
            parentPlan.setLaunchTime(new Date());
            sessionFacade.saveOrUpdate(parentPlan);
        }
        List<Plan> childList = new ArrayList<Plan>();
        Plan parent = new Plan();
        parent.setParentPlanId(parentPlanId);
        childList = planService.queryPlanList(parent, 1, 10, false);
        for (Plan child : childList) {
            Plan plan = (Plan)sessionFacade.getEntity(Plan.class, child.getId());
            if (plan != null) {
                // 计划审批相关信息表中存放计划与approvePlanForm关系
                ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                approvePlanInfo.setPlanId(child.getId());
                approvePlanInfo.setFormId(formId);
                sessionFacade.saveOrUpdate(approvePlanInfo);
                plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_ORDERED);
                plan.setFormId(formId);
                plan.setLauncher(actor.getId());
                plan.setLaunchTime(new Date());
                sessionFacade.saveOrUpdate(plan);
            }
        }
    }

    @Override
    public void startAssignProcessBackSavePlanInfo(String parentPlanId) {
        // 驳回节点变成正常：
        Plan parentPlanOld = (Plan)sessionFacade.getEntity(Plan.class, parentPlanId);
        parentPlanOld.setIsAssignSingleBack("false");
        planService.saveOrUpdate(parentPlanOld);

        List<Plan> childList = new ArrayList<Plan>();
        Plan parent = new Plan();
        parent.setParentPlanId(parentPlanId);
        childList = planService.queryPlanList(parent, 1, 10, false);
        for (Plan child : childList) {
            Plan plan = (Plan)sessionFacade.getEntity(Plan.class, child.getId());
            if (plan != null) {
                plan.setIsAssignSingleBack("false");
                sessionFacade.saveOrUpdate(plan);
            }
        }
    }

    @Override
    public List<FlowTaskVo> saveFlowTaskForChange1(FlowTaskVo plan,
                                                   List<FlowTaskVo> changeFlowTaskList,
                                                   List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList,
                                                   Plan parent) {
        Map<String, FlowTaskDeliverablesInfoVo> currentOutputMap = new HashMap<String, FlowTaskDeliverablesInfoVo>();
        Map<String, String> perposeIdMap = new HashMap<String, String>();
        Map<String, String> newInnerProposeCellIdMap = new HashMap<String, String>();
        for (FlowTaskVo t : changeFlowTaskList) {
            newInnerProposeCellIdMap.put(t.getCellId(), t.getId());
        }
        // 获取线上的前置计划id 即：内部前置计划
        for (ChangeFlowTaskCellConnectVo curInnerProposeId : changeFlowTaskConnectList) {
            if (curInnerProposeId.getTargetId().equals(plan.getCellId())
                && !"start".equals(curInnerProposeId.getCellId())) {
                perposeIdMap.put(newInnerProposeCellIdMap.get(curInnerProposeId.getCellId()),
                    newInnerProposeCellIdMap.get(curInnerProposeId.getCellId()));
            }
        }
        if (StringUtils.isNotEmpty(plan.getPreposeIds())) {
            for (String arr : plan.getPreposeIds().split(",")) {
                perposeIdMap.put(arr, arr);
            }
        }
        // 20160928 如果计划名称变更，则先删除该计划相关的输入及输入对应的输入
        boolean nameChange = false;
        Map<String, String> delOutputIds = new HashMap<String, String>();
        for (FlowTaskVo task : changeFlowTaskList) {
            if (task.getId().equals(plan.getId())) {
                if (!task.getPlanName().equals(plan.getPlanName())) {
                    task.getInputList().clear();
                    if (!CommonUtil.isEmpty(task.getOutputList())) {
                        for (FlowTaskDeliverablesInfoVo out : task.getOutputList()) {
                            delOutputIds.put(out.getId(), out.getId());
                        }
                    }
                    task.getOutputList().clear();
                    nameChange = true;
                }
                break;
            }
        }
        for (FlowTaskVo t : changeFlowTaskList) {
            // 20160928 去除删除的前置所对应的输入
            if (!CommonUtil.isEmpty(t.getInputList()) && !CommonUtil.isEmpty(delOutputIds)) {
                List<FlowTaskInputsVo> inputsList = new ArrayList<FlowTaskInputsVo>();
                for (FlowTaskInputsVo inputs : t.getInputList()) {
                    if (StringUtils.isEmpty(delOutputIds.get(inputs.getOriginDeliverablesInfoId()))) {
                        inputsList.add(inputs);
                    }
                    else if (!CommonUtil.isEmpty(t.getTaskNameType())) {
//                             && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(t.getTaskNameType())
//                             && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(t.getTaskNameType())
//                             && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(t.getTaskNameType())
                        inputs.setOriginObjectId(null);
                        inputs.setOriginDeliverablesInfoId(null);
                        inputsList.add(inputs);
                    }
                }
                t.getInputList().clear();
                t.getInputList().addAll(inputsList);
            }
            if (plan.getId().equals(t.getId())) {
                if ("true".equals(plan.getFromTemplate())) {
                    t.setOwner(plan.getOwner());
                    t.setPlanLevel(plan.getPlanLevel());
                    t.setRemark(plan.getRemark());
                    if (!CommonUtil.isEmpty(plan.getTaskNameType())) {
                        t.setTaskNameType(plan.getTaskNameType());
                        t.setTabCbTemplateId(plan.getTabCbTemplateId());
                    }
                    t.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                    t.setPreposeIds(plan.getPreposeIds());
                    t.setPreposePlans(plan.getPreposePlans());
                    t.setNameStandardId(plan.getNameStandardId());
                }
                else {
                    t.setPlanName(plan.getPlanName());
                    t.setOwner(plan.getOwner());
                    t.setPlanLevel(plan.getPlanLevel());
                    t.setRemark(plan.getRemark());
                    t.setWorkTime(plan.getWorkTime());
                    if (!CommonUtil.isEmpty(plan.getTaskNameType())) {
                        t.setTaskNameType(plan.getTaskNameType());
                        t.setTabCbTemplateId(plan.getTabCbTemplateId());
                    }
                    t.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
                    t.setPreposeIds(plan.getPreposeIds());
                    t.setPreposePlans(plan.getPreposePlans());
                    t.setNameStandardId(plan.getNameStandardId());
                }
                // 2.除去原来输入列表对应的外部输入：
                if (!CommonUtil.isEmpty(t.getInputList())) {
                    for (int i = t.getInputList().size() - 1; i >= 0; i-- ) {
                        if (StringUtils.isEmpty(perposeIdMap.get(t.getInputList().get(i).getOriginObjectId()))) {
                            t.getInputList().remove(i);
                        }
                    }
                }
                // 如果名称变更
                if (nameChange) {
                    if (!CommonUtil.isEmpty(t.getOutputList())) {
                        for (FlowTaskDeliverablesInfoVo out : t.getOutputList()) {
                            currentOutputMap.put(out.getName(), out);
                        }
                    }
                    String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
                    NameStandardDto condition2 = new NameStandardDto();
                    condition2.setStopFlag("启用");
                    condition2.setName(plan.getPlanName());
                    List<NameStandardDto> nameStandardList2 = nameStandardService.searchNameStandards(condition2);
                    if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                        || NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                        || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                        if (!CommonUtil.isEmpty(nameStandardList2)) {
                            t.setNameStandardId(nameStandardList2.get(0).getId());
                            for (NameStandardDto n : nameStandardList2) {
                                if ("启用".equals(n.getStopFlag())) {
                                    NameStandardDeliveryRelationDto relation = new NameStandardDeliveryRelationDto();
                                    relation.setNameStandardId(n.getId());
                                    List<NameStandardDeliveryRelationDto> list = nameStandardDeliveryRelationService.searchForPage(
                                        relation, 1, 10);
                                    if (list.size() > 0) {
                                        t.getOutputList().clear();
                                        for (NameStandardDeliveryRelationDto r : list) {
                                            if(!CommonUtil.isEmpty(r.getDeliveryStandardId())) {
                                                r.setDeliveryStandard(deliveryStandardService.getDeliveryStandardEntity(r.getDeliveryStandardId()));
                                            }
                                            if (r.getDeliveryStandardId() != null
                                                && r.getDeliveryStandard() != null
                                                && "启用".equals(r.getDeliveryStandard().getStopFlag())) {
                                                FlowTaskDeliverablesInfoVo deliverablesInfoTemp = new FlowTaskDeliverablesInfoVo();
                                                if (currentOutputMap.get(r.getDeliveryStandard().getName()) != null) {
                                                    deliverablesInfoTemp = currentOutputMap.get(r.getDeliveryStandard().getName());
                                                }
                                                else {
                                                    // deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
                                                    // 获取FlowTaskDeliverablesInfo的BusinessObject
//                                                    RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                                                    RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//                                                    String deliverablesInfoInitBusinessObjectJson = rdfConfigSupport.getDeliverablesInfoInitBusinessObject();
                                                    FeignJson ajaxJson = rdFlowTaskFlowResolveService.getDeliverablesInfoInitBusinessObject();
//                                                    Gson gson = new GsonBuilder().setDateFormat(
//                                                        "yyyy-MM-dd").setPrettyPrinting().create();
//                                                    AjaxJson ajaxJson = gson.fromJson(
//                                                        deliverablesInfoInitBusinessObjectJson,
//                                                        AjaxJson.class);
                                                    String mapList = (String)ajaxJson.getObj();
                                                    if (!CommonUtil.isEmpty(mapList)
                                                        && !CommonUtil.isEmpty(mapList.split(",")[0])
                                                        && !CommonUtil.isEmpty(mapList.split(",")[1])) {
                                                        deliverablesInfoTemp.setAvaliable("1");
                                                        deliverablesInfoTemp.setSecurityLevel((short)1);
                                                        deliverablesInfoTemp.setBizCurrent(mapList.split(",")[0].toString());
                                                        deliverablesInfoTemp.setPolicy_id(mapList.split(",")[1].toString());
                                                        deliverablesInfoTemp.setBizId(UUID.randomUUID().toString());
                                                    }

                                                    String uuid = PlanConstants.PLAN_CREATE_UUID
                                                                  + UUID.randomUUID().toString();
                                                    deliverablesInfoTemp.setId(uuid);
                                                    deliverablesInfoTemp.setName(r.getDeliveryStandard().getName());
                                                    deliverablesInfoTemp.setUseObjectId(plan.getId());
                                                    deliverablesInfoTemp.setUseObjectType("PLAN");
                                                    deliverablesInfoTemp.setDeliverId(r.getDeliveryStandard().getId());
                                                    // 查看系统参数，决定初始化带过来的交付项师傅必要
                                                    List<ParamSwitch> paramSwitchList = paramSwitchService.searchProconfigParamSwitch();
                                                    // String datagridStr =
                                                    // JSON.toJSONString(list);
                                                    // 国际化加载：活动名称库、启用活动名称库、关联交付项可以修改；
                                                    String activityNameBase = SwitchConstants.NAMESTANDARDSWITCH;
                                                    String startActivityNameBase = NameStandardSwitchConstants.USENAMESTANDARDLIB;
                                                    String deliverablesLinkCanUpdate = NameStandardSwitchConstants.DELIVERABLEUPATEABLE;
                                                    String flagUpdate = "true";
                                                    if (!CommonUtil.isEmpty(paramSwitchList)) {
                                                        for (ParamSwitch curStr : paramSwitchList) {
                                                            if (curStr.getName().equals(
                                                                activityNameBase)
                                                                && curStr.getStatus().contains(
                                                                    startActivityNameBase)
                                                                && curStr.getStatus().contains(
                                                                    deliverablesLinkCanUpdate)) {
                                                                flagUpdate = "false";
                                                            }
                                                        }
                                                    }
                                                    deliverablesInfoTemp.setRequired(flagUpdate);
                                                    deliverablesInfoTemp.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                                                }
                                                t.getOutputList().add(deliverablesInfoTemp);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (t.getPlanStartTime() == null) {
                    t.setPlanStartTime(parent.getPlanStartTime());
                }
                Date date = (Date)t.getPlanStartTime().clone();
                Project parentProject = projectService.getProjectEntity(parent.getProjectId());
                if (parentProject != null
                    && StringUtils.isNotEmpty(parentProject.getProjectTimeType())) {
                    if (ProjectConstants.WORKDAY.equals(parentProject.getProjectTimeType())) {
                        t.setPlanEndTime(com.glaway.foundation.common.util.DateUtil.nextWorkDay(
                            date, Integer.valueOf(t.getWorkTime()) - 1));
                    }
                    else if (ProjectConstants.COMPANYDAY.equals(parentProject.getProjectTimeType())) {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("startDate",date);
                        params.put("days",Integer.valueOf(t.getWorkTime()) - 1);
                        t.setPlanEndTime(calendarService.getNextWorkingDay(appKey,params));
                    }
                    else {
                        t.setPlanEndTime(TimeUtil.getExtraDate(date,
                            Integer.valueOf(t.getWorkTime()) - 1));
                    }
                }
                else {
                    t.setPlanEndTime(TimeUtil.getExtraDate(date,
                        Integer.valueOf(t.getWorkTime()) - 1));
                }
                break;
            }
        }
        return changeFlowTaskList;
    }

    @Override
    public List<FlowTaskVo> saveFlowTaskForChange2(FlowTaskVo plan,
                                                   Plan parent,
                                                   FlowTaskParentVo flowTaskParent,
                                                   List<FlowTaskVo> changeFlowTaskList,
                                                   List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList) {
        plan.setId(PlanConstants.PLAN_CREATE_UUID + UUID.randomUUID().toString());
        plan.setProjectId(parent.getProjectId());
        plan.setOwner(plan.getOwner());
        plan.setParentPlanId(flowTaskParent.getId());
        plan.setPlanStartTime(parent.getPlanStartTime());
        if (!CommonUtil.isEmpty(plan.getTaskNameType())) {
            plan.setTaskNameType(plan.getTaskNameType());
            plan.setTabCbTemplateId(plan.getTabCbTemplateId());
        }
        plan.setTaskType(PlanConstants.PLAN_TYPE_FLOW);
        Date date = (Date)plan.getPlanStartTime().clone();
        Map<String, String> perposeIdMap = new HashMap<String, String>();
        Map<String, String> newInnerProposeCellIdMap = new HashMap<String, String>();
        for (FlowTaskVo t : changeFlowTaskList) {
            newInnerProposeCellIdMap.put(t.getCellId(), t.getId());
        }
        // 获取线上的前置计划id 即：内部前置计划
        for (ChangeFlowTaskCellConnectVo curInnerProposeId : changeFlowTaskConnectList) {
            if (curInnerProposeId.getTargetId().equals(plan.getCellId())
                && !"start".equals(curInnerProposeId.getCellId())) {
                perposeIdMap.put(newInnerProposeCellIdMap.get(curInnerProposeId.getCellId()),
                    newInnerProposeCellIdMap.get(curInnerProposeId.getCellId()));
            }
        }
        if (StringUtils.isNotEmpty(plan.getPreposeIds())) {
            for (String arr : plan.getPreposeIds().split(",")) {
                perposeIdMap.put(arr, arr);
            }
        }
        // 2.除去原来输入列表对应的外部输入：
        if (plan.getInputList() != null) {
            for (int i = plan.getInputList().size() - 1; i >= 0; i-- ) {
                if (StringUtils.isEmpty(perposeIdMap.get(plan.getInputList().get(i).getOriginObjectId()))) {
                    plan.getInputList().remove(i);
                }
            }
        }
        String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
        NameStandardDto condition2 = new NameStandardDto();
        condition2.setStopFlag("启用");
        condition2.setName(plan.getPlanName());
        List<NameStandardDto> nameStandardList2 = nameStandardService.searchNameStandards(condition2);
        NameStandardDto nameStandard = new NameStandardDto();
        if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
            || NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
            || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
            if (!CommonUtil.isEmpty(nameStandardList2)) {
                nameStandard = nameStandardList2.get(0);
                plan.setNameStandardId(nameStandard.getId());
                NameStandardDeliveryRelationDto relation = new NameStandardDeliveryRelationDto();
                relation.setNameStandardId(nameStandard.getId());
                List<NameStandardDeliveryRelationDto> list = nameStandardDeliveryRelationService.searchForPage(
                    relation, 1, 10);
                if (list.size() > 0) {
                    plan.getOutputList().clear();
                    for (NameStandardDeliveryRelationDto r : list) {
                        if(!CommonUtil.isEmpty(r.getDeliveryStandardId())) {
                            r.setDeliveryStandard(deliveryStandardService.getDeliveryStandardEntity(r.getDeliveryStandardId()));
                        }
                        if (r.getDeliveryStandardId() != null && r.getDeliveryStandard() != null
                            && "启用".equals(r.getDeliveryStandard().getStopFlag())) {
                            FlowTaskDeliverablesInfoVo deliverablesInfoTemp = new FlowTaskDeliverablesInfoVo();
                            // deliverablesInfoService.initBusinessObject(deliverablesInfoTemp);
                            // 获取FlowTaskDeliverablesInfo的BusinessObject
//                            RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                            RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
//                            String deliverablesInfoInitBusinessObjectJson = rdfConfigSupport.getDeliverablesInfoInitBusinessObject();
//                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().create();
//                            AjaxJson ajaxJson = gson.fromJson(
//                                deliverablesInfoInitBusinessObjectJson, AjaxJson.class);
                            FeignJson ajaxJson = rdFlowTaskFlowResolveService.getDeliverablesInfoInitBusinessObject();
                            String mapList = (String)ajaxJson.getObj();
                            if (!CommonUtil.isEmpty(mapList)
                                && !CommonUtil.isEmpty(mapList.split(",")[0])
                                && !CommonUtil.isEmpty(mapList.split(",")[1])) {
                                deliverablesInfoTemp.setAvaliable("1");
                                deliverablesInfoTemp.setSecurityLevel((short)1);
                                deliverablesInfoTemp.setBizCurrent(mapList.split(",")[0].toString());
                                deliverablesInfoTemp.setPolicy_id(mapList.split(",")[1].toString());
                                deliverablesInfoTemp.setBizId(UUID.randomUUID().toString());
                            }
                            String uuid = PlanConstants.PLAN_CREATE_UUID
                                          + UUID.randomUUID().toString();
                            deliverablesInfoTemp.setId(uuid);
                            deliverablesInfoTemp.setName(r.getDeliveryStandard().getName());
                            deliverablesInfoTemp.setUseObjectId(plan.getId());
                            deliverablesInfoTemp.setUseObjectType("PLAN");
                            deliverablesInfoTemp.setDeliverId(r.getDeliveryStandard().getId());
                            // 查看系统参数，决定初始化带过来的交付项师傅必要
                            List<ParamSwitch> paramSwitchList = paramSwitchService.searchProconfigParamSwitch();
                            // String datagridStr = JSON.toJSONString(list);
                            // 国际化加载：活动名称库、启用活动名称库、关联交付项可以修改；
                            String activityNameBase = SwitchConstants.NAMESTANDARDSWITCH;
                            String startActivityNameBase = NameStandardSwitchConstants.USENAMESTANDARDLIB;
                            String deliverablesLinkCanUpdate = NameStandardSwitchConstants.DELIVERABLEUPATEABLE;
                            String flagUpdate = "true";
                            if (!CommonUtil.isEmpty(paramSwitchList)) {
                                for (ParamSwitch curStr : paramSwitchList) {
                                    if (curStr.getName().equals(activityNameBase)
                                        && curStr.getStatus().contains(startActivityNameBase)
                                        && curStr.getStatus().contains(deliverablesLinkCanUpdate)) {
                                        flagUpdate = "false";
                                    }
                                }
                            }
                            deliverablesInfoTemp.setRequired(flagUpdate);
                            deliverablesInfoTemp.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                            plan.getOutputList().add(deliverablesInfoTemp);
                        }
                    }
                }
            }
        }
        Project parentProject = projectService.getProjectEntity(parent.getProjectId());
        if (parentProject != null
            && StringUtils.isNotEmpty(parentProject.getProjectTimeType())) {
            if (ProjectConstants.WORKDAY.equals(parentProject.getProjectTimeType())) {
                plan.setPlanEndTime(com.glaway.foundation.common.util.DateUtil.nextWorkDay(date,
                    Integer.valueOf(plan.getWorkTime()) - 1));
            }
            else if (ProjectConstants.COMPANYDAY.equals(parentProject.getProjectTimeType())) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("startDate",date);
                params.put("days",Integer.valueOf(plan.getWorkTime()) - 1);

                plan.setPlanEndTime(calendarService.getNextWorkingDay(appKey,params));
            }
            else {
                plan.setPlanEndTime(TimeUtil.getExtraDate(date,
                    Integer.valueOf(plan.getWorkTime()) - 1));
            }
        }
        else {
            plan.setPlanEndTime(TimeUtil.getExtraDate(date,
                Integer.valueOf(plan.getWorkTime()) - 1));
        }
        plan.setPlanType(PlanConstants.PLAN_TYPE_FLOW);
        changeFlowTaskList.add(plan);
        return changeFlowTaskList;
    }

    @Override
    public boolean startPlanChangeforBackProcess(String leader, String deptLeader,
                                                 FlowTaskParentVo flowTaskParent,String userId) {
        boolean isSuccess = true;
        try {
            // 驳回节点：
            TSUserDto currentUser = userService.getUserByUserId(userId);
            String formId = flowTaskParent.getFormId();
            String creator = currentUser.getUserName();
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                formId);
            List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                myStartedTask.getProcInstId(), creator);
            String taskId = tasks.get(0).getId();
            // 流程部分
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                taskId);
            variables.put("user", creator);
            if (!CommonUtil.isEmpty(leader)) {
                variables.put("leader", leader);
            }
            if (!CommonUtil.isEmpty(deptLeader)) {
                variables.put("deptLeader", deptLeader);
            }
            // 执行流程
            TaskDto taskIn = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
            String procInstId = taskIn.getProcessInstanceId();
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            FeignJson nextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
            String status = nextTasks.getObj().toString();
            myStartedTask.setEndTime(new Date());
            myStartedTask.setStatus(status);
            workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);

        }
        catch (Exception ex) {
            isSuccess = false;
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 通过计划id获取对应的研发流程任务的id
     *
     * @param planId
     * @return
     */
    private String getRdTaskIdByPlanId(String planId) {
        String rdTaskId = "";
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select id from rdf_rdtask where linkPlanId ='" + planId + "'");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    rdTaskId = id;
                }
            }
        }
        return rdTaskId;
    }

    /**
     * 通过计划名称获取对应的研发流程任务的id
     *
     * @param cellId
     * @param parentPlanId
     * @return
     */
    private String getRdTaskIdByPlanCellId(String cellId, String parentPlanId) {
        String rdTaskId = "";
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select id from rdf_rdtask t where exists (select id from rdf_rdtask where t.parentplanid = id and id ='"
                         + parentPlanId + "') and cellId ='" + cellId + "'");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    rdTaskId = id;
                }
            }
        }
        return rdTaskId;
    }

    @Override
    public FlowTaskOutChangeVO startPlanChange(String leader,
                                               String deptLeader,
                                               String changeType,
                                               String changeRemark,
                                               FlowTaskParentVo flowTaskParent,
                                               List<FlowTaskVo> changeFlowTaskList,
                                               List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList,String userId) {

        PreparedStatement ps = null;
        Connection conn = null;
        FlowTaskOutChangeVO flowTaskOutChangeVO = new FlowTaskOutChangeVO();
        try {
            TSUserDto currentUser = userService.getUserByUserId(userId);
            Map<String, String> planIdAndRdTaskMap = new HashMap<String, String>();
            Map<String, String> rdTaskAndPlanIdMap = new HashMap<String, String>();
            String childrenPlanIds = "";
            String childrenRdTaskIds = "";
            Map<String, String> childrenPlanIdsAndCellMap = new HashMap<String, String>();
            Map<String, String> childrenRdTaskIdsAndCellMap = new HashMap<String, String>();

            String newFlowTaskParent = getRdTaskIdByPlanId(flowTaskParent.getParentId());
            String oldFlowTaskParentId = flowTaskParent.getId();

            // 子任务：
            if (!CommonUtil.isEmpty(changeFlowTaskList)) {
                for (FlowTaskVo curFlowTaskVo : changeFlowTaskList) {
                    // String curPlanId = getRdTaskIdByPlanId(curFlowTaskVo.getPlanId());
                    if (!CommonUtil.isEmpty(curFlowTaskVo.getPlanId())) {
                        childrenPlanIdsAndCellMap.put(curFlowTaskVo.getPlanId(),
                            curFlowTaskVo.getCellId());
                        if (CommonUtil.isEmpty(childrenPlanIds)) {
                            childrenPlanIds = curFlowTaskVo.getPlanId();
                        }
                        else {
                            childrenPlanIds = childrenPlanIds + "," + curFlowTaskVo.getPlanId();
                        }
                        String curPlanId = getRdTaskIdByPlanCellId(curFlowTaskVo.getCellId(),
                            newFlowTaskParent);
                        if (!CommonUtil.isEmpty(curPlanId)) {
                            childrenRdTaskIdsAndCellMap.put(curPlanId, curFlowTaskVo.getCellId());
                            if (CommonUtil.isEmpty(childrenRdTaskIds)) {
                                childrenRdTaskIds = curPlanId;
                            }
                            else {
                                childrenRdTaskIds = childrenRdTaskIds + "," + curPlanId;
                            }
                            rdTaskAndPlanIdMap.put(curPlanId, curFlowTaskVo.getPlanId());
                            planIdAndRdTaskMap.put(curFlowTaskVo.getPlanId(), curPlanId);
                        }
                    }
                }
            }

            if (!CommonUtil.isEmpty(flowTaskParent)
                && PlanConstants.PLAN_FLOWSTATUS_BACK.equals(flowTaskParent.getIsChangeSingleBack())) {
                // 驳回节点：
                String formId = flowTaskParent.getFormId();
                String creator = currentUser.getUserName();
                MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                    formId);
                List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    myStartedTask.getProcInstId(), creator);
                String taskId = tasks.get(0).getId();
                // // 流程部分
                // Map<String, Object> variables =
                // workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                // taskId);
                // variables.put("user", creator);
                // if (!CommonUtil.isEmpty(leader)) {
                // variables.put("leader", leader);
                // }
                // if (!CommonUtil.isEmpty(deptLeader)) {
                // variables.put("deptLeader", deptLeader);
                // }
                // 执行流程
                TaskDto taskIn = workFlowFacade.getWorkFlowCommonService().getTaskByTaskId(taskId);
                String procInstId = taskIn.getProcessInstanceId();
                // workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, variables);
                //
                // List<Task> nextTasks =
                // workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                // procInstId);
                // String status = ProcessUtil.getProcessStatus(nextTasks);
                //
                // myStartedTask.setEndTime(new Date());
                // myStartedTask.setStatus(status);
                // workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTask(myStartedTask);

                // 1.更新数据：
                // 获取FlowTaskDeliverablesInfo的BusinessObject
//                RdfTaskSupportImplService support = new RdfTaskSupportImplService();
//                RdfTaskSupport rdfConfigSupport = support.getRdfTaskSupportImplPort();
                FlowTaskDeliverablesInfoVo deliverablesInfoTemp = new FlowTaskDeliverablesInfoVo();
//                String businessObjectList = rdfConfigSupport.getDeliverablesInfoInitBusinessObject();
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm") // 时间转化为特定格式
//                .setPrettyPrinting() // 对json结果格式化
//                .create();
//                AjaxJson ajaxJson = gson.fromJson(businessObjectList, AjaxJson.class);
                FeignJson ajaxJson = rdFlowTaskFlowResolveService.getDeliverablesInfoInitBusinessObject();
                String mapList = (String)ajaxJson.getObj();
                if (!CommonUtil.isEmpty(mapList) && !CommonUtil.isEmpty(mapList.split(",")[0])
                    && !CommonUtil.isEmpty(mapList.split(",")[1])) {
                    deliverablesInfoTemp.setAvaliable("1");
                    deliverablesInfoTemp.setSecurityLevel((short)1);
                    deliverablesInfoTemp.setBizCurrent(mapList.split(",")[0].toString());
                    deliverablesInfoTemp.setPolicy_id(mapList.split(",")[1].toString());
                }

                // 获取FlowTaskResource的BusinessObject
                FlowTaskResourceLinkInfoVo resourceTemp = new FlowTaskResourceLinkInfoVo();
                FeignJson resourceInitBusinessObjectJson = rdFlowTaskFlowResolveService.getResourceInitBusinessObject();
                String resourceJson = (String)resourceInitBusinessObjectJson.getObj();
                if (!CommonUtil.isEmpty(resourceJson)
                    && !CommonUtil.isEmpty(resourceJson.split(",")[0])
                    && !CommonUtil.isEmpty(resourceJson.split(",")[1])) {
                    resourceTemp.setAvaliable("1");
                    resourceTemp.setSecurityLevel((short)1);
                    resourceTemp.setBizCurrent(resourceJson.split(",")[0].toString());
                    resourceTemp.setPolicy_id(resourceJson.split(",")[1].toString());
                }

                Map<String, String> originObject = new HashMap<String, String>();
                Map<String, String> originOutput = new HashMap<String, String>();
                List<ApprovePlanForm> formList = new ArrayList<ApprovePlanForm>();
                List<FlowTaskParentVo> flowTaskParentList = new ArrayList<FlowTaskParentVo>();
                List<FlowTaskVo> flowTaskList = new ArrayList<FlowTaskVo>();
                List<FlowTaskPreposeVo> flowTaskPreposeList = new ArrayList<FlowTaskPreposeVo>();
                List<FlowTaskInputsVo> inputList = new ArrayList<FlowTaskInputsVo>();
                List<FlowTaskDeliverablesInfoVo> outList = new ArrayList<FlowTaskDeliverablesInfoVo>();
                List<FlowTaskResourceLinkInfoVo> resourceList = new ArrayList<FlowTaskResourceLinkInfoVo>();
                List<ApprovePlanInfo> approvePlanInfoList = new ArrayList<ApprovePlanInfo>();
                List<ChangeFlowTaskCellConnectVo> connectList = new ArrayList<ChangeFlowTaskCellConnectVo>();

                ApprovePlanForm approvePlanForm = new ApprovePlanForm();
                approvePlanForm.setId(formId);
                approvePlanForm.setApproveType(PlanConstants.PLAN_APPROVE_TYPE_FLOWTASKCHANGE);

                // 将procInstId保存到approvePlanForm信息中
                approvePlanForm.setProcInstId(procInstId);
                formList.add(approvePlanForm);

                flowTaskParent.setFormId(approvePlanForm.getId());
                flowTaskParentList.add(flowTaskParent);

                flowTaskOutChangeVO.setParentPlanId(flowTaskParent.getParentId());

                List<FlowTaskNodeVO> nodes = new ArrayList<FlowTaskNodeVO>();
                List<FlowTaskDeliverVO> deliverVOs = new ArrayList<FlowTaskDeliverVO>();
                FlowTaskNodeVO node = new FlowTaskNodeVO();
                FlowTaskDeliverVO deliverVO = new FlowTaskDeliverVO();
                for (FlowTaskVo plan : changeFlowTaskList) {
                    node = new FlowTaskNodeVO();
                    deliverVOs = new ArrayList<FlowTaskDeliverVO>();
                    String oldTaskId = plan.getId();
                    plan.setId(UUIDGenerator.generate().toString());
                    plan.setParentPlanId(flowTaskParent.getId());
                    plan.setLauncher(UserUtil.getInstance().getUser().getId());
                    plan.setLaunchTime(new Date());
                    if (StringUtils.isNotEmpty(changeType)) {
                        plan.setChangeType(changeType);
                    }
                    if (StringUtils.isNotEmpty(changeRemark)) {
                        plan.setChangeRemark(changeRemark);
                    }
                    plan.setFormId(approvePlanForm.getId());
                    String preposeIds = plan.getPreposeIds();
                    if (CommonUtil.isEmpty(plan.getPlanId())) {
                        plan.setCreateTime(new Date());
                        plan.setCreateBy(currentUser.getId());
                        plan.setCreateFullName(currentUser.getRealName());
                        plan.setCreateName(currentUser.getUserName());
                    }
                    flowTaskList.add(plan);
                    node.setNameStandardId(plan.getNameStandardId());
                    node.setOldTaskId(!CommonUtil.isEmpty(plan.getPlanId()) ? plan.getPlanId() : oldTaskId);
                    node.setNewTaskId(plan.getId());

                    originObject.put(oldTaskId, plan.getId());
                    // 外部前置
                    if (StringUtils.isNotEmpty(preposeIds)) {
                        String[] preposeIdsArr = preposeIds.split(",");
                        for (String preposeId : preposeIdsArr) {
                            FlowTaskPreposeVo prepose = new FlowTaskPreposeVo();
                            prepose.setId(UUIDGenerator.generate().toString());
                            prepose.setPreposeId(preposeId);
                            prepose.setFlowTaskId(plan.getId());
                            prepose.setFormId(approvePlanForm.getId());
                            flowTaskPreposeList.add(prepose);
                        }
                    }
                    for (FlowTaskInputsVo inputs : plan.getInputList()) {
                        inputs.setId(UUIDGenerator.generate().toString());
                        inputs.setUseObjectId(plan.getId());
                        inputs.setFormId(approvePlanForm.getId());
                        inputs.setUseObjectId(plan.getId());
                        inputs.setCreateTime(new Date());
                        inputList.add(inputs);
                        deliverVO = new FlowTaskDeliverVO();
                        deliverVO.setDeliverId(inputs.getDeliverId());
                        deliverVO.setInOrOut("INPUT");
                        deliverVO.setNewId(inputs.getId());
                        deliverVO.setSourceId(inputs.getOriginObjectId());
                        deliverVO.setSourceDeliverSeq(inputs.getOriginDeliverablesInfoId());
                        deliverVOs.add(deliverVO);
                    }

                    for (FlowTaskDeliverablesInfoVo outputs : plan.getOutputList()) {
                        deliverVO = new FlowTaskDeliverVO();
                        String oldOutId = outputs.getId();
                        outputs.setId(UUIDGenerator.generate().toString());
                        // planService.initBusinessObject(outputs);
                        outputs.setAvaliable(deliverablesInfoTemp.getAvaliable());
                        outputs.setBizCurrent(deliverablesInfoTemp.getBizCurrent());
                        outputs.setBizVersion(deliverablesInfoTemp.getBizVersion());
                        outputs.setSecurityLevel(deliverablesInfoTemp.getSecurityLevel());
                        outputs.setPolicy_id(deliverablesInfoTemp.getPolicy_id());
                        outputs.setBizId(UUID.randomUUID().toString());
                        outputs.setUseObjectId(plan.getId());
                        outputs.setFormId(approvePlanForm.getId());
                        outputs.setCreateTime(new Date());
                        outList.add(outputs);
                        originOutput.put(oldOutId, outputs.getId());
                        deliverVO = new FlowTaskDeliverVO();
                        deliverVO.setDeliverId(outputs.getDeliverId());
                        deliverVO.setInOrOut("OUTPUT");
                        deliverVO.setNewId(outputs.getId());
                        deliverVOs.add(deliverVO);
                    }
                    node.setDelivers(deliverVOs);
                    nodes.add(node);
                    for (FlowTaskResourceLinkInfoVo resource : plan.getResourceLinkList()) {
                        resource.setId(UUIDGenerator.generate().toString());
                        // planService.initBusinessObject(resource);
                        resource.setAvaliable(deliverablesInfoTemp.getAvaliable());
                        resource.setBizCurrent(deliverablesInfoTemp.getBizCurrent());
                        resource.setBizVersion(deliverablesInfoTemp.getBizVersion());
                        resource.setSecurityLevel(deliverablesInfoTemp.getSecurityLevel());
                        resource.setPolicy_id(deliverablesInfoTemp.getPolicy_id());
                        resource.setBizId(UUID.randomUUID().toString());
                        resource.setUseObjectId(plan.getId());
                        resource.setFormId(approvePlanForm.getId());
                        resourceList.add(resource);
                    }
                    // 计划审批相关信息表中存放计划与approvePlanForm关系
                    ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                    approvePlanInfo.setId(UUIDGenerator.generate().toString());
                    approvePlanInfo.setPlanId(plan.getId());
                    approvePlanInfo.setFormId(approvePlanForm.getId());
                    approvePlanInfoList.add(approvePlanInfo);
                }

                for (ChangeFlowTaskCellConnectVo connect : changeFlowTaskConnectList) {
                    connect.setId(UUIDGenerator.generate().toString());
                    connect.setParentPlanId(flowTaskParent.getId());
                    connect.setFormId(approvePlanForm.getId());
                    // 通过节点与模板ID获得基本信息ID
                    if (StringUtils.isNotEmpty(connect.getCellId())) {
                        for (FlowTaskVo f : changeFlowTaskList) {
                            if (connect.getCellId().equals(f.getCellId())) {
                                connect.setInfoId(f.getId());
                                break;
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(connect.getTargetId())) {
                        for (FlowTaskVo f : changeFlowTaskList) {
                            if (connect.getTargetId().equals(f.getCellId())) {
                                connect.setTargetInfoId(f.getId());
                                break;
                            }
                        }
                    }
                    connectList.add(connect);
                }

                for (ChangeFlowTaskCellConnectVo cellConnect : changeFlowTaskConnectList) {
                    if (!TaskProcConstants.TASK_CELL_START.equals(cellConnect.getCellId())
                        && !TaskProcConstants.TASK_CELL_END.equals(cellConnect.getTargetId())) {
                        FlowTaskVo p1 = new FlowTaskVo();
                        for (FlowTaskVo f : changeFlowTaskList) {
                            if (cellConnect.getCellId().equals(f.getCellId())) {
                                p1 = f;
                                break;
                            }
                        }

                        FlowTaskVo p2 = new FlowTaskVo();
                        for (FlowTaskVo f : changeFlowTaskList) {
                            if (cellConnect.getTargetId().equals(f.getCellId())) {
                                p2 = f;
                                break;
                            }
                        }
                        FlowTaskPreposeVo prepose = new FlowTaskPreposeVo();
                        prepose.setId(UUIDGenerator.generate().toString());
                        prepose.setPreposeId(p1.getId());
                        prepose.setFlowTaskId(p2.getId());
                        prepose.setFormId(approvePlanForm.getId());
                        flowTaskPreposeList.add(prepose);
                    }
                }
                for (FlowTaskInputsVo in : inputList) {
                    if (!CommonUtil.isEmpty(in.getOriginObjectId())
                        && in.getOriginObjectId().startsWith(PlanConstants.PLAN_CREATE_UUID)) {
                        if (StringUtils.isNotEmpty(originObject.get(in.getOriginObjectId()))) {
                            in.setOriginObjectId(originObject.get(in.getOriginObjectId()));
                        }
                    }
                    if (!CommonUtil.isEmpty(in.getOriginDeliverablesInfoId())
                        && in.getOriginDeliverablesInfoId().startsWith(
                            PlanConstants.PLAN_CREATE_UUID)) {
                        if (StringUtils.isNotEmpty(originOutput.get(in.getOriginDeliverablesInfoId()))) {
                            in.setOriginDeliverablesInfoId(originOutput.get(in.getOriginDeliverablesInfoId()));
                        }
                    }
                }
                flowTaskOutChangeVO.setNodes(nodes);
                if (!CommonUtil.isEmpty(originObject) && !CommonUtil.isEmpty(originOutput)
                    && !CommonUtil.isEmpty(flowTaskOutChangeVO)
                    && !CommonUtil.isEmpty(flowTaskOutChangeVO.getNodes())) {
                    for (FlowTaskNodeVO nodeVo : flowTaskOutChangeVO.getNodes()) {
                        if (!CommonUtil.isEmpty(nodeVo)
                            && !CommonUtil.isEmpty(nodeVo.getDelivers())) {
                            for (FlowTaskDeliverVO dVo : nodeVo.getDelivers()) {
                                if ("INPUT".equals(dVo.getInOrOut())
                                    && !CommonUtil.isEmpty(dVo.getSourceId())
                                    && !CommonUtil.isEmpty(dVo.getSourceDeliverSeq())) {
                                    dVo.setSourceId(!CommonUtil.isEmpty(originObject.get(dVo.getSourceId())) ? originObject.get(dVo.getSourceId()) : dVo.getSourceId());
                                    dVo.setSourceDeliverSeq(!CommonUtil.isEmpty(originOutput.get(dVo.getSourceDeliverSeq())) ? originOutput.get(dVo.getSourceDeliverSeq()) : dVo.getSourceDeliverSeq());
                                }
                            }
                        }
                    }
                }

                // 2.删除原来的数据：
                // 删除计划与流程的关系：
                ApprovePlanInfo approvePlanInfoCon = new ApprovePlanInfo();
                approvePlanInfoCon.setFormId(formId);
                List<ApprovePlanInfo> approveList = planService.queryAssignList(
                    approvePlanInfoCon, 1, 10, false);
                if (!CommonUtil.isEmpty(approveList)) {
                    for (ApprovePlanInfo curRDTaskApproveInfo : approveList) {
                        sessionFacade.delete(curRDTaskApproveInfo);
                    }
                }
                try {
                    rdFlowTaskFlowResolveService.delFlowTaskInfoByFlowTaskParentId(oldFlowTaskParentId, formId);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (!CommonUtil.isEmpty(flowTaskList)) {
                    conn = jdbcTemplate.getDataSource().getConnection();
                    conn.setAutoCommit(false);
                    StringBuffer sqlStringBuffer = new StringBuffer();
                    int i = 0;
                    if (!CommonUtil.isEmpty(flowTaskParentList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" update PM_FLOWTASK_PARENT set FLOWRESOLVEXML = ? where id = ?");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskParentVo parentFlowTask : flowTaskParentList) {
                            i = 1;
                            ps.setObject(i++ , parentFlowTask.getFlowResolveXml());
                            ps.setObject(i++ , parentFlowTask.getId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    sqlStringBuffer = new StringBuffer();
                    sqlStringBuffer.append(" insert into PM_FLOWTASK ( ");
                    sqlStringBuffer.append(" ID, ACTUALENDTIME, ACTUALSTARTTIME, ASSIGNTIME, ASSIGNER, BEFOREPLANID, BIZCURRENT, CELLID, CHANGEINFODOCID, CHANGEINFODOCNAME, ");
                    sqlStringBuffer.append(" CHANGEINFODOCPATH, CHANGEREMARK, CHANGETYPE, FEEDBACKPROCINSTID, FEEDBACKRATEBEFORE, FLOWRESOLVEXML, FLOWSTATUS, FORMID, FROMTEMPLATE, IMPLEMENTATION, ");
                    sqlStringBuffer.append(" INVALIDTIME, LAUNCHTIME, LAUNCHER, MILESTONE, OPCONTENT, OWNER, PARENTPLANID, PLANENDTIME, PLANID, PLANLEVEL, ");
                    sqlStringBuffer.append(" PLANNAME, PLANNUMBER, PLANORDER, PLANSOURCE, PLANSTARTTIME, PLANTYPE, PROGRESSRATE, PROJECTID, PROJECTSTATUS, REMARK, ");
                    sqlStringBuffer.append(" REQUIRED, RISK, STOREYNO, TASKNAMETYPE, TASKTYPE, WORKTIME, WORKTIMEREFERENCE ,createBy, createTime,createFullName,createName,TABCBTEMPLATEID");
                    sqlStringBuffer.append(" ) ");
                    sqlStringBuffer.append(" values ( ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?,?");
                    sqlStringBuffer.append(" ) ");
                    ps = conn.prepareStatement(sqlStringBuffer.toString());
                    for (FlowTaskVo task : flowTaskList) {
                        i = 1;
                        ps.setObject(i++ , task.getId());
                        if (!CommonUtil.isEmpty(task.getActualEndTime())) {
                            ps.setObject(i++ , new Timestamp(task.getActualEndTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getActualStartTime())) {
                            ps.setObject(i++ , new Timestamp(task.getActualEndTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getAssignTime())) {
                            ps.setObject(i++ , new Timestamp(task.getAssignTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        ps.setObject(i++ , task.getAssigner());
                        ps.setObject(i++ , task.getBeforePlanId());
                        ps.setObject(i++ , task.getBizCurrent());
                        ps.setObject(i++ , task.getCellId());
                        ps.setObject(i++ , task.getChangeInfoDocId());
                        ps.setObject(i++ , task.getChangeInfoDocName());
                        ps.setObject(i++ , task.getChangeInfoDocPath());
                        ps.setObject(i++ , task.getChangeRemark());
                        ps.setObject(i++ , task.getChangeType());
                        ps.setObject(i++ , task.getFeedbackProcInstId());
                        ps.setObject(i++ , task.getFeedbackRateBefore());
                        ps.setObject(i++ , task.getFlowResolveXml());
                        ps.setObject(i++ , task.getFlowStatus());
                        ps.setObject(i++ , task.getFormId());
                        ps.setObject(i++ , task.getFromTemplate());
                        ps.setObject(i++ , task.getImplementation());

                        if (!CommonUtil.isEmpty(task.getInvalidTime())) {
                            ps.setObject(i++ , new Timestamp(task.getInvalidTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }

                        if (!CommonUtil.isEmpty(task.getLaunchTime())) {
                            ps.setObject(i++ , new Timestamp(task.getLaunchTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        ps.setObject(i++ , task.getLauncher());
                        ps.setObject(i++ , task.getMilestone());
                        ps.setObject(i++ , task.getOpContent());
                        ps.setObject(i++ , task.getOwner());
                        ps.setObject(i++ , task.getParentPlanId());

                        if (!CommonUtil.isEmpty(task.getPlanEndTime())) {
                            ps.setObject(i++ , new Timestamp(task.getPlanEndTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }

                        ps.setObject(i++ , task.getPlanId());
                        ps.setObject(i++ , task.getPlanLevel());
                        ps.setObject(i++ , task.getPlanName());
                        ps.setObject(i++ , task.getPlanNumber());
                        ps.setObject(i++ , task.getPlanOrder());
                        ps.setObject(i++ , task.getPlanSource());

                        if (!CommonUtil.isEmpty(task.getPlanStartTime())) {
                            ps.setObject(i++ , new Timestamp(task.getPlanStartTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }

                        ps.setObject(i++ , task.getPlanType());
                        ps.setObject(i++ , task.getProgressRate());
                        ps.setObject(i++ , task.getProjectId());
                        ps.setObject(i++ , task.getProjectStatus());
                        ps.setObject(i++ , task.getRemark());
                        ps.setObject(i++ , task.getRequired());
                        ps.setObject(i++ , task.getRisk());
                        ps.setObject(i++ , task.getStoreyNo());
                        ps.setObject(i++ , task.getTaskNameType());
                        ps.setObject(i++ , task.getTaskType());
                        ps.setObject(i++ , task.getWorkTime());
                        ps.setObject(i++ , task.getWorkTimeReference());
                        if (!CommonUtil.isEmpty(task.getCreateBy())) {
                            ps.setObject(i++ , task.getCreateBy());
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getCreateTime())) {
                            ps.setObject(i++ , new Timestamp(task.getCreateTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getCreateFullName())) {
                            ps.setObject(i++ , task.getCreateFullName());
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getCreateName())) {
                            ps.setObject(i++ , task.getCreateName());
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        ps.setObject(i++ , task.getTabCbTemplateId());
                        ps.addBatch();
                    }
                    ps.executeBatch();

                    if (!CommonUtil.isEmpty(flowTaskPreposeList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" insert INTO PM_FLOWTASK_PREPOSE ( ");
                        sqlStringBuffer.append(" ID, FLOWTASKID, PREPOSEID, AVALIABLE, FORMID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" values ( ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskPreposeVo prepose : flowTaskPreposeList) {
                            i = 1;
                            ps.setObject(i++ , prepose.getId());
                            ps.setObject(i++ , prepose.getFlowTaskId());
                            ps.setObject(i++ , prepose.getPreposeId());
                            ps.setObject(i++ , prepose.getAvaliable());
                            ps.setObject(i++ , prepose.getFormId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(inputList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_FLOWTASK_INPUTS ( ");
                        sqlStringBuffer.append(" ID, DOCID, DOCNAME, FILEID, FORMID, INPUTID, NAME, ORIGIN, ORIGINDELIVERABLESINFOID, ORIGINOBJECTID, ");
                        sqlStringBuffer.append(" REQUIRED, USEOBJECTID, USEOBJECTTYPE, originType, originTypeExt, createBy , createFullName, createName ,createTime");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskInputsVo input : inputList) {
                            i = 1;
                            ps.setObject(i++ , input.getId());
                            ps.setObject(i++ , input.getDocId());
                            ps.setObject(i++ , input.getDocName());
                            ps.setObject(i++ , input.getFileId());
                            ps.setObject(i++ , input.getFormId());
                            ps.setObject(i++ , input.getInputId());
                            ps.setObject(i++ , input.getName());
                            ps.setObject(i++ , input.getOrigin());
                            ps.setObject(i++ , input.getOriginDeliverablesInfoId());
                            ps.setObject(i++ , input.getOriginObjectId());
                            ps.setObject(i++ , input.getRequired());
                            ps.setObject(i++ , input.getUseObjectId());
                            ps.setObject(i++ , input.getUseObjectType());
                            ps.setObject(i++ , input.getOriginType());
                            ps.setObject(i++ , input.getOriginTypeExt());
                            ps.setObject(i++ , input.getCreateBy());
                            ps.setObject(i++ , input.getCreateName());
                            ps.setObject(i++ , input.getCreateFullName());
                            if (!CommonUtil.isEmpty(input.getCreateTime())) {
                                ps.setObject(i++ , new Timestamp(input.getCreateTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(outList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_FLOWTASK_DELIVERABLES_INFO ( ");
                        sqlStringBuffer.append(" ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, FIRSTBY, FIRSTFULLNAME, FIRSTNAME, FIRSTTIME, SECURITYLEVEL, ");
                        sqlStringBuffer.append(" POLICY_ID, DOCID, DOCNAME, FILEID, FORMID, NAME, ORIGIN, OUTPUTID, REQUIRED, USEOBJECTID, ");
                        sqlStringBuffer.append(" USEOBJECTTEMPID, USEOBJECTTYPE, createBy , createName, createFullName, createTime ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskDeliverablesInfoVo output : outList) {
                            i = 1;
                            ps.setObject(i++ , output.getId());
                            ps.setObject(i++ , output.getAvaliable());
                            ps.setObject(i++ , output.getBizCurrent());
                            ps.setObject(i++ , output.getBizId());
                            ps.setObject(i++ , output.getBizVersion());
                            ps.setObject(i++ , output.getFirstBy());
                            ps.setObject(i++ , output.getFirstFullName());
                            ps.setObject(i++ , output.getFirstName());

                            if (!CommonUtil.isEmpty(output.getFirstTime())) {
                                ps.setObject(i++ , new Timestamp(output.getFirstTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }

                            ps.setObject(i++ , output.getSecurityLevel());
                            ps.setObject(i++ , output.getPolicy_id());
                            ps.setObject(i++ , output.getDocId());
                            ps.setObject(i++ , output.getDocName());
                            ps.setObject(i++ , output.getFileId());
                            ps.setObject(i++ , output.getFormId());
                            ps.setObject(i++ , output.getName());
                            ps.setObject(i++ , output.getOrigin());
                            ps.setObject(i++ , output.getOutputId());
                            ps.setObject(i++ , output.getRequired());
                            ps.setObject(i++ , output.getUseObjectId());
                            ps.setObject(i++ , output.getUseObjectTempId());
                            ps.setObject(i++ , output.getUseObjectType());
                            ps.setObject(i++ , output.getCreateBy());
                            ps.setObject(i++ , output.getCreateName());
                            ps.setObject(i++ , output.getCreateFullName());
                            if (!CommonUtil.isEmpty(output.getCreateTime())) {
                                ps.setObject(i++ , new Timestamp(output.getCreateTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(resourceList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_FLOWTASK_RESOURCE_LINK_INFO ( ");
                        sqlStringBuffer.append(" ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, FIRSTBY, FIRSTFULLNAME, FIRSTNAME, FIRSTTIME, SECURITYLEVEL, ");
                        sqlStringBuffer.append(" POLICY_ID, ENDTIME, FORMID, LINKINFOID, RESOURCEID, STARTTIME, USEOBJECTID, USEOBJECTTEMPID, USEOBJECTTYPE, USERATE ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskResourceLinkInfoVo resource : resourceList) {
                            i = 1;
                            ps.setObject(i++ , resource.getId());
                            ps.setObject(i++ , resource.getAvaliable());
                            ps.setObject(i++ , resource.getBizCurrent());
                            ps.setObject(i++ , resource.getBizId());
                            ps.setObject(i++ , resource.getBizVersion());
                            ps.setObject(i++ , resource.getFirstBy());
                            ps.setObject(i++ , resource.getFirstFullName());
                            ps.setObject(i++ , resource.getFirstName());

                            if (!CommonUtil.isEmpty(resource.getFirstTime())) {
                                ps.setObject(i++ ,
                                    new Timestamp(resource.getFirstTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }

                            ps.setObject(i++ , resource.getSecurityLevel());
                            ps.setObject(i++ , resource.getPolicy_id());
                            ps.setObject(i++ , resource.getEndTime());
                            ps.setObject(i++ , resource.getFormId());
                            ps.setObject(i++ , resource.getLinkInfoId());
                            ps.setObject(i++ , resource.getResourceId());
                            ps.setObject(i++ , resource.getStartTime());
                            ps.setObject(i++ , resource.getUseObjectId());
                            ps.setObject(i++ , resource.getUseObjectTempId());
                            ps.setObject(i++ , resource.getUseObjectType());
                            ps.setObject(i++ , resource.getUseRate());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(approvePlanInfoList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO RDF_APPROVE_PLAN_INFO ( ");
                        sqlStringBuffer.append(" ID, FORMID, RDTASKID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (ApprovePlanInfo approvePlanInfo : approvePlanInfoList) {
                            i = 1;
                            ps.setObject(i++ , approvePlanInfo.getId());
                            ps.setObject(i++ , approvePlanInfo.getFormId());
                            if (!CommonUtil.isEmpty(planIdAndRdTaskMap.get(approvePlanInfo.getPlanId()))) {
                                ps.setObject(i++ ,
                                    planIdAndRdTaskMap.get(approvePlanInfo.getPlanId()));
                            }
                            else {
                                ps.setObject(i++ , approvePlanInfo.getPlanId());
                            }
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(approvePlanInfoList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_APPROVE_PLAN_INFO ( ");
                        sqlStringBuffer.append(" ID, FORMID, PLANID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (ApprovePlanInfo approvePlanInfo : approvePlanInfoList) {
                            i = 1;
                            ps.setObject(i++ , approvePlanInfo.getId());
                            ps.setObject(i++ , approvePlanInfo.getFormId());
                            ps.setObject(i++ , approvePlanInfo.getPlanId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(connectList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_CHANGE_FLOWTASK_CONNECT ( ");
                        sqlStringBuffer.append(" ID, CELLID, FORMID, INFOID, PARENTPLANID, TARGETID, TARGETINFOID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (ChangeFlowTaskCellConnectVo connect : connectList) {
                            i = 1;
                            ps.setObject(i++ , connect.getId());
                            ps.setObject(i++ , connect.getCellId());
                            ps.setObject(i++ , connect.getFormId());
                            ps.setObject(i++ , connect.getInfoId());
                            ps.setObject(i++ , connect.getParentPlanId());
                            ps.setObject(i++ , connect.getTargetId());
                            ps.setObject(i++ , connect.getTargetInfoId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    conn.commit();
                    conn.setAutoCommit(true);
                }
            }
            else {
                // 获取FlowTaskDeliverablesInfo的BusinessObject
                FlowTaskDeliverablesInfoVo deliverablesInfoTemp = new FlowTaskDeliverablesInfoVo();
                FeignJson ajaxJson = rdFlowTaskFlowResolveService.getDeliverablesInfoInitBusinessObject();
                String mapList = (String)ajaxJson.getObj();
                if (!CommonUtil.isEmpty(mapList) && !CommonUtil.isEmpty(mapList.split(",")[0])
                    && !CommonUtil.isEmpty(mapList.split(",")[1])) {
                    deliverablesInfoTemp.setAvaliable("1");
                    deliverablesInfoTemp.setSecurityLevel((short)1);
                    deliverablesInfoTemp.setBizCurrent(mapList.split(",")[0].toString());
                    deliverablesInfoTemp.setPolicy_id(mapList.split(",")[1].toString());
                }

                // 获取FlowTaskResource的BusinessObject
                FlowTaskResourceLinkInfoVo resourceTemp = new FlowTaskResourceLinkInfoVo();
                FeignJson ajaxJson1 = rdFlowTaskFlowResolveService.getResourceInitBusinessObject();
                String resourceJson = (String)ajaxJson1.getObj();
                if (!CommonUtil.isEmpty(resourceJson)
                    && !CommonUtil.isEmpty(resourceJson.split(",")[0])
                    && !CommonUtil.isEmpty(resourceJson.split(",")[1])) {
                    resourceTemp.setAvaliable("1");
                    resourceTemp.setSecurityLevel((short)1);
                    resourceTemp.setBizCurrent(resourceJson.split(",")[0].toString());
                    resourceTemp.setPolicy_id(resourceJson.split(",")[1].toString());
                }

                Map<String, String> originObject = new HashMap<String, String>();
                Map<String, String> originOutput = new HashMap<String, String>();
                List<ApprovePlanForm> formList = new ArrayList<ApprovePlanForm>();
                List<FlowTaskParentVo> flowTaskParentList = new ArrayList<FlowTaskParentVo>();
                List<FlowTaskVo> flowTaskList = new ArrayList<FlowTaskVo>();
                List<FlowTaskPreposeVo> flowTaskPreposeList = new ArrayList<FlowTaskPreposeVo>();
                List<FlowTaskInputsVo> inputList = new ArrayList<FlowTaskInputsVo>();
                List<FlowTaskDeliverablesInfoVo> outList = new ArrayList<FlowTaskDeliverablesInfoVo>();
                List<FlowTaskResourceLinkInfoVo> resourceList = new ArrayList<FlowTaskResourceLinkInfoVo>();
                List<ApprovePlanInfo> approvePlanInfoList = new ArrayList<ApprovePlanInfo>();
                List<ChangeFlowTaskCellConnectVo> connectList = new ArrayList<ChangeFlowTaskCellConnectVo>();

                ApprovePlanForm approvePlanForm = new ApprovePlanForm();
                approvePlanForm.setId(UUIDGenerator.generate().toString());
                approvePlanForm.setApproveType(PlanConstants.PLAN_APPROVE_TYPE_FLOWTASKCHANGE);

                String procInstId = startFlowTaskChangeProcess(leader, deptLeader,
                    approvePlanForm.getId(),userId);
                // 将procInstId保存到approvePlanForm信息中
                approvePlanForm.setProcInstId(procInstId);
                formList.add(approvePlanForm);

                flowTaskParent.setId(UUIDGenerator.generate().toString());
                flowTaskParent.setFormId(approvePlanForm.getId());
                flowTaskParentList.add(flowTaskParent);

                flowTaskOutChangeVO.setParentPlanId(flowTaskParent.getParentId());

                List<FlowTaskNodeVO> nodes = new ArrayList<FlowTaskNodeVO>();
                List<FlowTaskDeliverVO> deliverVOs = new ArrayList<FlowTaskDeliverVO>();
                FlowTaskNodeVO node = new FlowTaskNodeVO();
                FlowTaskDeliverVO deliverVO = new FlowTaskDeliverVO();
                for (FlowTaskVo plan : changeFlowTaskList) {
                    node = new FlowTaskNodeVO();
                    deliverVOs = new ArrayList<FlowTaskDeliverVO>();
                    String oldTaskId = plan.getId();
                    plan.setId(UUIDGenerator.generate().toString());
                    plan.setParentPlanId(flowTaskParent.getId());
                    plan.setLauncher(currentUser.getId());
                    plan.setLaunchTime(new Date());
                    if (StringUtils.isNotEmpty(changeType)) {
                        plan.setChangeType(changeType);
                    }
                    if (StringUtils.isNotEmpty(changeRemark)) {
                        plan.setChangeRemark(changeRemark);
                    }
                    plan.setFormId(approvePlanForm.getId());
                    String preposeIds = plan.getPreposeIds();
                    if (CommonUtil.isEmpty(plan.getPlanId())) {
                        plan.setCreateTime(new Date());
                        plan.setCreateBy(currentUser.getId());
                        plan.setCreateFullName(currentUser.getRealName());
                        plan.setCreateName(currentUser.getUserName());
                    }
                    flowTaskList.add(plan);
                    node.setNameStandardId(plan.getNameStandardId());
                    node.setOldTaskId(!CommonUtil.isEmpty(plan.getPlanId()) ? plan.getPlanId() : oldTaskId);
                    node.setNewTaskId(plan.getId());

                    originObject.put(oldTaskId, plan.getId());
                    // 外部前置
                    if (StringUtils.isNotEmpty(preposeIds)) {
                        String[] preposeIdsArr = preposeIds.split(",");
                        for (String preposeId : preposeIdsArr) {
                            FlowTaskPreposeVo prepose = new FlowTaskPreposeVo();
                            prepose.setId(UUIDGenerator.generate().toString());
                            prepose.setPreposeId(preposeId);
                            prepose.setFlowTaskId(plan.getId());
                            prepose.setFormId(approvePlanForm.getId());
                            flowTaskPreposeList.add(prepose);
                        }
                    }
                    for (FlowTaskInputsVo inputs : plan.getInputList()) {
                        inputs.setId(UUIDGenerator.generate().toString());
                        inputs.setUseObjectId(plan.getId());
                        inputs.setFormId(approvePlanForm.getId());
                        inputs.setUseObjectId(plan.getId());
                        inputs.setCreateTime(new Date());
                        inputList.add(inputs);
                        deliverVO = new FlowTaskDeliverVO();
                        deliverVO.setDeliverId(inputs.getDeliverId());
                        deliverVO.setInOrOut("INPUT");
                        deliverVO.setNewId(inputs.getId());
                        deliverVO.setSourceId(inputs.getOriginObjectId());
                        deliverVO.setSourceDeliverSeq(inputs.getOriginDeliverablesInfoId());
                        deliverVOs.add(deliverVO);
                    }

                    for (FlowTaskDeliverablesInfoVo outputs : plan.getOutputList()) {
                        deliverVO = new FlowTaskDeliverVO();
                        String oldOutId = outputs.getId();
                        outputs.setId(UUIDGenerator.generate().toString());
                        // planService.initBusinessObject(outputs);
                        outputs.setAvaliable(deliverablesInfoTemp.getAvaliable());
                        outputs.setBizCurrent(deliverablesInfoTemp.getBizCurrent());
                        outputs.setBizVersion(deliverablesInfoTemp.getBizVersion());
                        outputs.setSecurityLevel(deliverablesInfoTemp.getSecurityLevel());
                        outputs.setPolicy_id(deliverablesInfoTemp.getPolicy_id());
                        outputs.setBizId(UUID.randomUUID().toString());
                        outputs.setUseObjectId(plan.getId());
                        outputs.setFormId(approvePlanForm.getId());
                        outputs.setCreateTime(new Date());
                        outList.add(outputs);
                        originOutput.put(oldOutId, outputs.getId());
                        deliverVO = new FlowTaskDeliverVO();
                        deliverVO.setDeliverId(outputs.getDeliverId());
                        deliverVO.setInOrOut("OUTPUT");
                        deliverVO.setNewId(outputs.getId());
                        deliverVOs.add(deliverVO);
                    }
                    node.setDelivers(deliverVOs);
                    nodes.add(node);
                    for (FlowTaskResourceLinkInfoVo resource : plan.getResourceLinkList()) {
                        resource.setId(UUIDGenerator.generate().toString());
                        // planService.initBusinessObject(resource);
                        resource.setAvaliable(deliverablesInfoTemp.getAvaliable());
                        resource.setBizCurrent(deliverablesInfoTemp.getBizCurrent());
                        resource.setBizVersion(deliverablesInfoTemp.getBizVersion());
                        resource.setSecurityLevel(deliverablesInfoTemp.getSecurityLevel());
                        resource.setPolicy_id(deliverablesInfoTemp.getPolicy_id());
                        resource.setBizId(UUID.randomUUID().toString());
                        resource.setUseObjectId(plan.getId());
                        resource.setFormId(approvePlanForm.getId());
                        resourceList.add(resource);
                    }
                    // 计划审批相关信息表中存放计划与approvePlanForm关系
                    ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                    approvePlanInfo.setId(UUIDGenerator.generate().toString());
                    approvePlanInfo.setPlanId(plan.getId());
                    approvePlanInfo.setFormId(approvePlanForm.getId());
                    approvePlanInfoList.add(approvePlanInfo);
                }

                for (ChangeFlowTaskCellConnectVo connect : changeFlowTaskConnectList) {
                    connect.setId(UUIDGenerator.generate().toString());
                    connect.setParentPlanId(flowTaskParent.getId());
                    connect.setFormId(approvePlanForm.getId());
                    // 通过节点与模板ID获得基本信息ID
                    if (StringUtils.isNotEmpty(connect.getCellId())) {
                        for (FlowTaskVo f : changeFlowTaskList) {
                            if (connect.getCellId().equals(f.getCellId())) {
                                connect.setInfoId(f.getId());
                                break;
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(connect.getTargetId())) {
                        for (FlowTaskVo f : changeFlowTaskList) {
                            if (connect.getTargetId().equals(f.getCellId())) {
                                connect.setTargetInfoId(f.getId());
                                break;
                            }
                        }
                    }
                    connectList.add(connect);
                }

                for (ChangeFlowTaskCellConnectVo cellConnect : changeFlowTaskConnectList) {
                    if (!TaskProcConstants.TASK_CELL_START.equals(cellConnect.getCellId())
                        && !TaskProcConstants.TASK_CELL_END.equals(cellConnect.getTargetId())) {
                        FlowTaskVo p1 = new FlowTaskVo();
                        for (FlowTaskVo f : changeFlowTaskList) {
                            if (cellConnect.getCellId().equals(f.getCellId())) {
                                p1 = f;
                                break;
                            }
                        }

                        FlowTaskVo p2 = new FlowTaskVo();
                        for (FlowTaskVo f : changeFlowTaskList) {
                            if (cellConnect.getTargetId().equals(f.getCellId())) {
                                p2 = f;
                                break;
                            }
                        }
                        FlowTaskPreposeVo prepose = new FlowTaskPreposeVo();
                        prepose.setId(UUIDGenerator.generate().toString());
                        prepose.setPreposeId(p1.getId());
                        prepose.setFlowTaskId(p2.getId());
                        prepose.setFormId(approvePlanForm.getId());
                        flowTaskPreposeList.add(prepose);
                    }
                }
                for (FlowTaskInputsVo in : inputList) {
                    if (!CommonUtil.isEmpty(in.getOriginObjectId())
                        && in.getOriginObjectId().startsWith(PlanConstants.PLAN_CREATE_UUID)) {
                        if (StringUtils.isNotEmpty(originObject.get(in.getOriginObjectId()))) {
                            in.setOriginObjectId(originObject.get(in.getOriginObjectId()));
                        }
                    }
                    if (!CommonUtil.isEmpty(in.getOriginDeliverablesInfoId())
                        && in.getOriginDeliverablesInfoId().startsWith(
                            PlanConstants.PLAN_CREATE_UUID)) {
                        if (StringUtils.isNotEmpty(originOutput.get(in.getOriginDeliverablesInfoId()))) {
                            in.setOriginDeliverablesInfoId(originOutput.get(in.getOriginDeliverablesInfoId()));
                        }
                    }
                }
                flowTaskOutChangeVO.setNodes(nodes);
                if (!CommonUtil.isEmpty(originObject) && !CommonUtil.isEmpty(originOutput)
                    && !CommonUtil.isEmpty(flowTaskOutChangeVO)
                    && !CommonUtil.isEmpty(flowTaskOutChangeVO.getNodes())) {
                    for (FlowTaskNodeVO nodeVo : flowTaskOutChangeVO.getNodes()) {
                        if (!CommonUtil.isEmpty(nodeVo)
                            && !CommonUtil.isEmpty(nodeVo.getDelivers())) {
                            for (FlowTaskDeliverVO dVo : nodeVo.getDelivers()) {
                                if ("INPUT".equals(dVo.getInOrOut())
                                    && !CommonUtil.isEmpty(dVo.getSourceId())
                                    && !CommonUtil.isEmpty(dVo.getSourceDeliverSeq())) {
                                    dVo.setSourceId(!CommonUtil.isEmpty(originObject.get(dVo.getSourceId())) ? originObject.get(dVo.getSourceId()) : dVo.getSourceId());
                                    dVo.setSourceDeliverSeq(!CommonUtil.isEmpty(originOutput.get(dVo.getSourceDeliverSeq())) ? originOutput.get(dVo.getSourceDeliverSeq()) : dVo.getSourceDeliverSeq());
                                }
                            }
                        }
                    }
                }

                List<String> paramList1 = new ArrayList<String>();
                paramList1.add(PlanConstants.PLAN_FLOWSTATUS_CHANGE);
                paramList1.add(newFlowTaskParent);
                sessionFacade.executeSql("update rdf_rdtask set flowStatus = ? where parentPlanId = ? and avaliable = '1' ",
                    paramList1.toArray());

                List<String> paramList = new ArrayList<String>();
                paramList.add(PlanConstants.PLAN_FLOWSTATUS_CHANGE);
                paramList.add(flowTaskParent.getParentId());
                sessionFacade.executeSql(
                    "update pm_plan set flowStatus = ? where parentPlanId = ? and avaliable = '1' ",
                    paramList.toArray());

                if (!CommonUtil.isEmpty(flowTaskList)) {
                    conn = jdbcTemplate.getDataSource().getConnection();
                    conn.setAutoCommit(false);
                    StringBuffer sqlStringBuffer = new StringBuffer();
                    int i = 0;

                    if (!CommonUtil.isEmpty(formList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" insert into RDF_APPROVE_PLAN_FORM ( ");
                        sqlStringBuffer.append(" ID, APPROVETYPE, PROCINSTID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" values ( ");
                        sqlStringBuffer.append(" ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (ApprovePlanForm form : formList) {
                            i = 1;
                            ps.setObject(i++ , form.getId());
                            ps.setObject(i++ , form.getApproveType());
                            ps.setObject(i++ , form.getProcInstId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(formList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" insert into PM_APPROVE_PLAN_FORM ( ");
                        sqlStringBuffer.append(" ID, APPROVETYPE, PROCINSTID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" values ( ");
                        sqlStringBuffer.append(" ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (ApprovePlanForm form : formList) {
                            i = 1;
                            ps.setObject(i++ , form.getId());
                            ps.setObject(i++ , form.getApproveType());
                            ps.setObject(i++ , form.getProcInstId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(flowTaskParentList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" insert INTO PM_FLOWTASK_PARENT ( ");
                        sqlStringBuffer.append(" ID, PARENTID, FORMID, FLOWRESOLVEXML ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" values ( ");
                        sqlStringBuffer.append(" ?, ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskParentVo parentFlowTask : flowTaskParentList) {
                            i = 1;
                            ps.setObject(i++ , parentFlowTask.getId());
                            ps.setObject(i++ , parentFlowTask.getParentId());
                            ps.setObject(i++ , parentFlowTask.getFormId());
                            ps.setObject(i++ , parentFlowTask.getFlowResolveXml());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    sqlStringBuffer = new StringBuffer();
                    sqlStringBuffer.append(" insert into PM_FLOWTASK ( ");
                    sqlStringBuffer.append(" ID, ACTUALENDTIME, ACTUALSTARTTIME, ASSIGNTIME, ASSIGNER, BEFOREPLANID, BIZCURRENT, CELLID, CHANGEINFODOCID, CHANGEINFODOCNAME, ");
                    sqlStringBuffer.append(" CHANGEINFODOCPATH, CHANGEREMARK, CHANGETYPE, FEEDBACKPROCINSTID, FEEDBACKRATEBEFORE, FLOWRESOLVEXML, FLOWSTATUS, FORMID, FROMTEMPLATE, IMPLEMENTATION, ");
                    sqlStringBuffer.append(" INVALIDTIME, LAUNCHTIME, LAUNCHER, MILESTONE, OPCONTENT, OWNER, PARENTPLANID, PLANENDTIME, PLANID, PLANLEVEL, ");
                    sqlStringBuffer.append(" PLANNAME, PLANNUMBER, PLANORDER, PLANSOURCE, PLANSTARTTIME, PLANTYPE, PROGRESSRATE, PROJECTID, PROJECTSTATUS, REMARK, ");
                    sqlStringBuffer.append(" REQUIRED, RISK, STOREYNO, TASKNAMETYPE, TASKTYPE, WORKTIME, WORKTIMEREFERENCE ,createBy, createTime,createFullName,createName,TABCBTEMPLATEID");
                    sqlStringBuffer.append(" ) ");
                    sqlStringBuffer.append(" values ( ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                    sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?,?");
                    sqlStringBuffer.append(" ) ");
                    ps = conn.prepareStatement(sqlStringBuffer.toString());
                    for (FlowTaskVo task : flowTaskList) {
                        i = 1;
                        ps.setObject(i++ , task.getId());
                        if (!CommonUtil.isEmpty(task.getActualEndTime())) {
                            ps.setObject(i++ , new Timestamp(task.getActualEndTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getActualStartTime())) {
                            ps.setObject(i++ , new Timestamp(task.getActualEndTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getAssignTime())) {
                            ps.setObject(i++ , new Timestamp(task.getAssignTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        ps.setObject(i++ , task.getAssigner());
                        ps.setObject(i++ , task.getBeforePlanId());
                        ps.setObject(i++ , task.getBizCurrent());
                        ps.setObject(i++ , task.getCellId());
                        ps.setObject(i++ , task.getChangeInfoDocId());
                        ps.setObject(i++ , task.getChangeInfoDocName());
                        ps.setObject(i++ , task.getChangeInfoDocPath());
                        ps.setObject(i++ , task.getChangeRemark());
                        ps.setObject(i++ , task.getChangeType());
                        ps.setObject(i++ , task.getFeedbackProcInstId());
                        ps.setObject(i++ , task.getFeedbackRateBefore());
                        ps.setObject(i++ , task.getFlowResolveXml());
                        ps.setObject(i++ , task.getFlowStatus());
                        ps.setObject(i++ , task.getFormId());
                        ps.setObject(i++ , task.getFromTemplate());
                        ps.setObject(i++ , task.getImplementation());

                        if (!CommonUtil.isEmpty(task.getInvalidTime())) {
                            ps.setObject(i++ , new Timestamp(task.getInvalidTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }

                        if (!CommonUtil.isEmpty(task.getLaunchTime())) {
                            ps.setObject(i++ , new Timestamp(task.getLaunchTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        ps.setObject(i++ , task.getLauncher());
                        ps.setObject(i++ , task.getMilestone());
                        ps.setObject(i++ , task.getOpContent());
                        ps.setObject(i++ , task.getOwner());
                        ps.setObject(i++ , task.getParentPlanId());

                        if (!CommonUtil.isEmpty(task.getPlanEndTime())) {
                            ps.setObject(i++ , new Timestamp(task.getPlanEndTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }

                        ps.setObject(i++ , task.getPlanId());
                        ps.setObject(i++ , task.getPlanLevel());
                        ps.setObject(i++ , task.getPlanName());
                        ps.setObject(i++ , task.getPlanNumber());
                        ps.setObject(i++ , task.getPlanOrder());
                        ps.setObject(i++ , task.getPlanSource());

                        if (!CommonUtil.isEmpty(task.getPlanStartTime())) {
                            ps.setObject(i++ , new Timestamp(task.getPlanStartTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }

                        ps.setObject(i++ , task.getPlanType());
                        ps.setObject(i++ , task.getProgressRate());
                        ps.setObject(i++ , task.getProjectId());
                        ps.setObject(i++ , task.getProjectStatus());
                        ps.setObject(i++ , task.getRemark());
                        ps.setObject(i++ , task.getRequired());
                        ps.setObject(i++ , task.getRisk());
                        ps.setObject(i++ , task.getStoreyNo());
                        ps.setObject(i++ , task.getTaskNameType());
                        ps.setObject(i++ , task.getTaskType());
                        ps.setObject(i++ , task.getWorkTime());
                        ps.setObject(i++ , task.getWorkTimeReference());
                        if (!CommonUtil.isEmpty(task.getCreateBy())) {
                            ps.setObject(i++ , task.getCreateBy());
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getCreateTime())) {
                            ps.setObject(i++ , new Timestamp(task.getCreateTime().getTime()));
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getCreateFullName())) {
                            ps.setObject(i++ , task.getCreateFullName());
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        if (!CommonUtil.isEmpty(task.getCreateName())) {
                            ps.setObject(i++ , task.getCreateName());
                        }
                        else {
                            ps.setObject(i++ , null);
                        }
                        ps.setObject(i++ , task.getTabCbTemplateId());
                        ps.addBatch();
                    }
                    ps.executeBatch();

                    if (!CommonUtil.isEmpty(flowTaskPreposeList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" insert INTO PM_FLOWTASK_PREPOSE ( ");
                        sqlStringBuffer.append(" ID, FLOWTASKID, PREPOSEID, AVALIABLE, FORMID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" values ( ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskPreposeVo prepose : flowTaskPreposeList) {
                            i = 1;
                            ps.setObject(i++ , prepose.getId());
                            ps.setObject(i++ , prepose.getFlowTaskId());
                            ps.setObject(i++ , prepose.getPreposeId());
                            ps.setObject(i++ , prepose.getAvaliable());
                            ps.setObject(i++ , prepose.getFormId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(inputList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_FLOWTASK_INPUTS ( ");
                        sqlStringBuffer.append(" ID, DOCID, DOCNAME, FILEID, FORMID, INPUTID, NAME, ORIGIN, ORIGINDELIVERABLESINFOID, ORIGINOBJECTID, ");
                        sqlStringBuffer.append(" REQUIRED, USEOBJECTID, USEOBJECTTYPE, originType, originTypeExt, createBy , createFullName, createName ,createTime");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskInputsVo input : inputList) {
                            i = 1;
                            ps.setObject(i++ , input.getId());
                            ps.setObject(i++ , input.getDocId());
                            ps.setObject(i++ , input.getDocName());
                            ps.setObject(i++ , input.getFileId());
                            ps.setObject(i++ , input.getFormId());
                            ps.setObject(i++ , input.getInputId());
                            ps.setObject(i++ , input.getName());
                            ps.setObject(i++ , input.getOrigin());
                            ps.setObject(i++ , input.getOriginDeliverablesInfoId());
                            ps.setObject(i++ , input.getOriginObjectId());
                            ps.setObject(i++ , input.getRequired());
                            ps.setObject(i++ , input.getUseObjectId());
                            ps.setObject(i++ , input.getUseObjectType());
                            ps.setObject(i++ , input.getOriginType());
                            ps.setObject(i++ , input.getOriginTypeExt());
                            ps.setObject(i++ , input.getCreateBy());
                            ps.setObject(i++ , input.getCreateName());
                            ps.setObject(i++ , input.getCreateFullName());
                            if (!CommonUtil.isEmpty(input.getCreateTime())) {
                                ps.setObject(i++ , new Timestamp(input.getCreateTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(outList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_FLOWTASK_DELIVERABLES_INFO ( ");
                        sqlStringBuffer.append(" ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, FIRSTBY, FIRSTFULLNAME, FIRSTNAME, FIRSTTIME, SECURITYLEVEL, ");
                        sqlStringBuffer.append(" POLICY_ID, DOCID, DOCNAME, FILEID, FORMID, NAME, ORIGIN, OUTPUTID, REQUIRED, USEOBJECTID, ");
                        sqlStringBuffer.append(" USEOBJECTTEMPID, USEOBJECTTYPE, createBy , createName, createFullName, createTime ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskDeliverablesInfoVo output : outList) {
                            i = 1;
                            ps.setObject(i++ , output.getId());
                            ps.setObject(i++ , output.getAvaliable());
                            ps.setObject(i++ , output.getBizCurrent());
                            ps.setObject(i++ , output.getBizId());
                            ps.setObject(i++ , output.getBizVersion());
                            ps.setObject(i++ , output.getFirstBy());
                            ps.setObject(i++ , output.getFirstFullName());
                            ps.setObject(i++ , output.getFirstName());

                            if (!CommonUtil.isEmpty(output.getFirstTime())) {
                                ps.setObject(i++ , new Timestamp(output.getFirstTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }

                            ps.setObject(i++ , output.getSecurityLevel());
                            ps.setObject(i++ , output.getPolicy_id());
                            ps.setObject(i++ , output.getDocId());
                            ps.setObject(i++ , output.getDocName());
                            ps.setObject(i++ , output.getFileId());
                            ps.setObject(i++ , output.getFormId());
                            ps.setObject(i++ , output.getName());
                            ps.setObject(i++ , output.getOrigin());
                            ps.setObject(i++ , output.getOutputId());
                            ps.setObject(i++ , output.getRequired());
                            ps.setObject(i++ , output.getUseObjectId());
                            ps.setObject(i++ , output.getUseObjectTempId());
                            ps.setObject(i++ , output.getUseObjectType());
                            ps.setObject(i++ , output.getCreateBy());
                            ps.setObject(i++ , output.getCreateName());
                            ps.setObject(i++ , output.getCreateFullName());
                            if (!CommonUtil.isEmpty(output.getCreateTime())) {
                                ps.setObject(i++ , new Timestamp(output.getCreateTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(resourceList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_FLOWTASK_RESOURCE_LINK_INFO ( ");
                        sqlStringBuffer.append(" ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, FIRSTBY, FIRSTFULLNAME, FIRSTNAME, FIRSTTIME, SECURITYLEVEL, ");
                        sqlStringBuffer.append(" POLICY_ID, ENDTIME, FORMID, LINKINFOID, RESOURCEID, STARTTIME, USEOBJECTID, USEOBJECTTEMPID, USEOBJECTTYPE, USERATE ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (FlowTaskResourceLinkInfoVo resource : resourceList) {
                            i = 1;
                            ps.setObject(i++ , resource.getId());
                            ps.setObject(i++ , resource.getAvaliable());
                            ps.setObject(i++ , resource.getBizCurrent());
                            ps.setObject(i++ , resource.getBizId());
                            ps.setObject(i++ , resource.getBizVersion());
                            ps.setObject(i++ , resource.getFirstBy());
                            ps.setObject(i++ , resource.getFirstFullName());
                            ps.setObject(i++ , resource.getFirstName());

                            if (!CommonUtil.isEmpty(resource.getFirstTime())) {
                                ps.setObject(i++ ,
                                    new Timestamp(resource.getFirstTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }

                            ps.setObject(i++ , resource.getSecurityLevel());
                            ps.setObject(i++ , resource.getPolicy_id());

                            if (!CommonUtil.isEmpty(resource.getEndTime())) {
                                ps.setObject(i++ , new Timestamp(resource.getEndTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }
//                            ps.setObject(i++ , resource.getEndTime());
                            ps.setObject(i++ , resource.getFormId());
                            ps.setObject(i++ , resource.getLinkInfoId());
                            ps.setObject(i++ , resource.getResourceId());

                            if (!CommonUtil.isEmpty(resource.getStartTime())) {
                                ps.setObject(i++ , new Timestamp(resource.getStartTime().getTime()));
                            }
                            else {
                                ps.setObject(i++ , null);
                            }

//                            ps.setObject(i++ , resource.getStartTime());
                            ps.setObject(i++ , resource.getUseObjectId());
                            ps.setObject(i++ , resource.getUseObjectTempId());
                            ps.setObject(i++ , resource.getUseObjectType());
                            ps.setObject(i++ , resource.getUseRate());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(approvePlanInfoList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO RDF_APPROVE_PLAN_INFO ( ");
                        sqlStringBuffer.append(" ID, FORMID, RDTASKID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (ApprovePlanInfo approvePlanInfo : approvePlanInfoList) {
                            i = 1;
                            ps.setObject(i++ , approvePlanInfo.getId());
                            ps.setObject(i++ , approvePlanInfo.getFormId());
                            if (!CommonUtil.isEmpty(planIdAndRdTaskMap.get(approvePlanInfo.getPlanId()))) {
                                ps.setObject(i++ ,
                                    planIdAndRdTaskMap.get(approvePlanInfo.getPlanId()));
                            }
                            else {
                                ps.setObject(i++ , approvePlanInfo.getPlanId());
                            }
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(approvePlanInfoList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_APPROVE_PLAN_INFO ( ");
                        sqlStringBuffer.append(" ID, FORMID, PLANID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (ApprovePlanInfo approvePlanInfo : approvePlanInfoList) {
                            i = 1;
                            ps.setObject(i++ , approvePlanInfo.getId());
                            ps.setObject(i++ , approvePlanInfo.getFormId());
                            ps.setObject(i++ , approvePlanInfo.getPlanId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if (!CommonUtil.isEmpty(connectList)) {
                        sqlStringBuffer = new StringBuffer();
                        sqlStringBuffer.append(" INSERT INTO PM_CHANGE_FLOWTASK_CONNECT ( ");
                        sqlStringBuffer.append(" ID, CELLID, FORMID, INFOID, PARENTPLANID, TARGETID, TARGETINFOID ");
                        sqlStringBuffer.append(" ) ");
                        sqlStringBuffer.append(" VALUES (");
                        sqlStringBuffer.append(" ?, ?, ?, ?, ?, ?, ? ");
                        sqlStringBuffer.append(" ) ");
                        ps = conn.prepareStatement(sqlStringBuffer.toString());
                        for (ChangeFlowTaskCellConnectVo connect : connectList) {
                            i = 1;
                            ps.setObject(i++ , connect.getId());
                            ps.setObject(i++ , connect.getCellId());
                            ps.setObject(i++ , connect.getFormId());
                            ps.setObject(i++ , connect.getInfoId());
                            ps.setObject(i++ , connect.getParentPlanId());
                            ps.setObject(i++ , connect.getTargetId());
                            ps.setObject(i++ , connect.getTargetInfoId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    conn.commit();
                    conn.setAutoCommit(true);
                }
            }
        }
        catch (Exception ex) {
            try {
                conn.rollback();
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
        finally {
            try {
                conn.setAutoCommit(true);
                DBUtil.closeConnection(null, ps, conn);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flowTaskOutChangeVO;
    }


    private String startFlowTaskChangeProcess(String leader, String deptLeader, String formId,String userId) {
         String type = "PLAN";
         FeignJson procInstIdJson = rdFlowTaskFlowResolveService.startFlowTaskChangeProcessForPlan(leader, deptLeader, formId,userId,type);
         String procInstId = procInstIdJson.getObj().toString();
//        TSUserDto actor = userService.getUserByUserId(userId);
//        Map<String, Object> variables = new HashMap<String, Object>();
//        variables.put("approve", "third");
//        variables.put("user", actor.getUserName());
//        variables.put("userId", actor.getUserId());    
//        variables.put("assignPeoples", new ArrayList<String>());
//        variables.put("leader", leader);
//        variables.put("deptLeader", deptLeader);
//        variables.put("assigner", actor.getUserName());
//        variables.put("editUrl", PlanConstants.URL_FLOWTASK_CHANGE_VIEWPLAN + formId);
//        variables.put("viewUrl", PlanConstants.URL_FLOWTASK_CHANGE_VIEWNEW + formId);
//        variables.put("oid", BpmnConstants.OID_APPROVERDTASKFORM + formId);        
//        String feignSetUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/rdTaskBeginChangeExcutionRestController/notify.do";
//        variables.put("feignSetUrl", feignSetUrl);               
//        String feignEndUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/rdTaskChangeExcutionRestController/notify.do";
//        variables.put("feignEndUrl", feignEndUrl);
//        String feignGoBackUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/rdTaskCancelFlowtaskChangeRestController/notify.do";
//        variables.put("feignGoBackUrl", feignGoBackUrl);
//        
//        FeignJson pi = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
//            BpmnConstants.BPMN_CHANGE_MASS_RDTASKFLOWTASK, formId, variables);
//        String procInstId = pi.getObj().toString(); // 流程实例ID
//        // 将procInstId存放到approvePlanForm中
//        List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
//            procInstId, actor.getUserName());
//        String taskId = tasks.get(0).getId();
//        
//        // 执行流程
//        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);
//
//        FeignJson nextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
//            procInstId);
//        String status = nextTasks.getObj().toString();
//        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
//        myStartedTask.setTaskNumber(formId);
//        myStartedTask.setType(BpmnConstants.BPMN_CHANGE_MASS_RDTASKFLOWTASK);
//
//        // 流程已发起列表中、增加对象名称的记录,流程任务名称规范化
//        // 多个对象的批量审批任务，对象名称为null
//        myStartedTask.setObjectName(null);
//        myStartedTask.setTitle(ProcessUtil.getProcessTitle(null,
//            BpmnConstants.BPMN_CHANGE_PLAN_DISPLAYNAME, procInstId));
//
//        myStartedTask.setCreater(actor.getUserName());
//        myStartedTask.setCreaterFullname(actor.getRealName());
//        myStartedTask.setStartTime(new Date());
//        // myStartedTask.setProcType(BpmnConstants.BPMN_CHANGE_PLAN);
//        myStartedTask.setProcInstId(procInstId);
//        myStartedTask.setStatus(status);
//        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);
        return procInstId;
    }
}
