package com.glaway.ids.project.plantemplate.service.impl;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.core.dto.MyTodoTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.constant.LifeCycleConstant;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.config.constant.ConfigStateConstants;
import com.glaway.ids.config.constant.ConfigTypeConstants;
import com.glaway.ids.config.constant.NameStandardSwitchConstants;
import com.glaway.ids.config.constant.SwitchConstants;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.ActivityTypeManageEntityServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.TabCbTemplateEntityServiceI;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.InputsServiceI;
import com.glaway.ids.project.plan.service.NameStandardFeignService;
import com.glaway.ids.project.plan.vo.PlanExcelSaveVo;
import com.glaway.ids.project.plantemplate.constant.PlantemplateConstant;
import com.glaway.ids.project.plantemplate.dto.PlanTemplateDto;
import com.glaway.ids.project.plantemplate.entity.PlanTempOptLog;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.entity.PreposePlanTemplate;
import com.glaway.ids.project.plantemplate.service.PlanTemplateDetailServiceI;
import com.glaway.ids.project.plantemplate.service.PreposePlanTemplateServiceI;
import com.glaway.ids.project.plantemplate.support.planTemplate.vo.PlanTemplateReq;
import com.glaway.ids.project.plantemplate.support.planTemplate.vo.PlanTemplateRspItem;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateDetailReq;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.CommonInitUtil;

import com.glaway.ids.util.Dto2Entity;
import com.glaway.ids.util.ProcessUtil;
import com.glaway.ids.util.mpputil.MppDirector;
import com.glaway.ids.util.mpputil.MppInfo;
import com.glaway.ids.util.mpputil.MppParseUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.service.PlanTemplateServiceI;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


@Service("planTemplateService")
@Transactional(propagation = Propagation.REQUIRED)
public class PlanTemplateServiceImpl extends BusinessObjectServiceImpl<PlanTemplate> implements PlanTemplateServiceI {


    private static final SystemLog log = BaseLogFactory.getSystemLog(PlanTemplateServiceImpl.class);

    @Autowired
    private FeignUserService userService;


    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private PlanBusinessConfigServiceI businessConfigService;

    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 注入WorkFlowFacade
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;

    /**
     * 输入信息
     */
    @Autowired
    private InputsServiceI inputsService;


    @Autowired
    private PlanTemplateDetailServiceI planTemplateDetailService;


    @Autowired
    private ParamSwitchServiceI paramSwitchService;


    @Autowired
    private NameStandardFeignService nameStandardService;

    @Autowired
    private DeliverablesInfoServiceI deliveryStandardService;


    @Autowired
    private PreposePlanTemplateServiceI preposePlanTemplateService;

    @Autowired
    private TabCbTemplateEntityServiceI tabCbTemplateEntityService;

    @Autowired
    private ActivityTypeManageEntityServiceI activityTypeManageEntityService;

    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;

    @Value(value="${spring.application.name}")
    private String appKey;

    @Autowired
    private Environment env;


    @Override
    public String getLifeCycleStatusList() {
        PlanTemplate p = new PlanTemplate();
        initBusinessObject(p);
        List<LifeCycleStatus> lifeCycleStatus = p.getPolicy().getLifeCycleStatusList();
        List<LifeCycleStatus> orderList = new ArrayList<LifeCycleStatus>();
        for (int i = 0; i < lifeCycleStatus.size(); i++ ) {
            for (LifeCycleStatus status : lifeCycleStatus) {
                if (status.getOrderNum() == i) {
                    orderList.add(status);
                    break;
                }
            }
        }

        String aString = JsonUtil.getListJsonWithoutQuote(orderList);
        return aString;
    }

    @Override
    public PageList queryEntity(List<ConditionVO> conditionList, Map<String, String> params) {
        Map<String, String> map = new HashMap<String, String>();
        try {

            String hql = "from PlanTemplate t where t.avaliable = '1' ";
            if (StringUtil.isNotEmpty(params.get("name"))) {
                hql += " and (lower(t.name) like '%'||lower('" + params.get("name") + "')||'%') ";
            }
            if (StringUtil.isNotEmpty(params.get("createName"))) {
                // hql = hql + " and (t.user.realName like '%" + params.get("createName") + "%'";
                // hql = hql + " or t.user.userName like '%" + params.get("createName") + "%')";
                hql += " and (lower(t.createName) like '%'||lower('" + params.get("createName")
                        + "')||'%' or lower(t.createFullName) like '%'||lower('"
                        + params.get("createName") + "')||'%') ";
            }

            for (ConditionVO conditionVO : conditionList) {
                if ("order".equals(conditionVO.getKey())
                        && StringUtils.isNotEmpty(conditionVO.getSort())
                        && StringUtils.isNotEmpty(conditionVO.getValue())) {
                    if ("createTimeStr".equals(conditionVO.getSort())) {
                        conditionVO.setSort("createTime");
                    }
                    else if ("createName".equals(conditionVO.getSort())) {
                        conditionVO.setSort("createFullName");
                    }
                    else if ("approveStatus".equals(conditionVO.getSort())) {
                        conditionVO.setSort("bizCurrent");
                        // map.put("PlanTemplate.bizCurrent", conditionVO.getValue());
                    }
                }
                if ("PlanTemplate.bizCurrent".equals(conditionVO.getKey())) {
                    if (conditionVO.getValue().length() > 1) {
                        String[] biz = conditionVO.getValue().substring(1).split(",");
                        String str = "";
                        for (String s : biz) {
                            str += ",\'" + s + "\'";
                        }
                        hql += " and t.bizCurrent in (" + str.substring(1) + ")";
                    }
                    conditionVO.setValue(null);
                    conditionVO.setValueArr(null);
                }
            }

            // 排序
            map.put("PlanTemplate.createTime", "desc");
            List<T> list = sessionFacade.searchByCommonFormHql(hql, conditionList, true, map);
            // List<T> list = sessionFacade.searchByCommonFormHql(hql, conditionList, true);
            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            List<PlanTemplateRspItem> items = new ArrayList<PlanTemplateRspItem>();
            if (list != null && list.size() > 0) {
                for (Object t : list) {
                    PlanTemplate planTemplateDb = (PlanTemplate)t;
                    PlanTemplateRspItem item = new PlanTemplateRspItem();
                    item.setId(planTemplateDb.getId());
                    item.setName(planTemplateDb.getName());
                    item.setRemark(planTemplateDb.getRemark());
                    // item.setFlowFlag(planTemplateDb.getProcessInstanceId());
                    item.setBizVersion(planTemplateDb.getBizVersion());
                    item.setProcessInstanceId(planTemplateDb.getProcessInstanceId());
                    // FirstFullName不为空取FirstFullName，否则取User
                    if (StringUtils.isNotBlank(planTemplateDb.getFirstFullName())) {
                        item.setCreateName(planTemplateDb.getFirstFullName() + "-"
                                + planTemplateDb.getFirstName());
                    }
                    else if (planTemplateDb.getCreateBy() != null) {
                        item.setCreateName(planTemplateDb.getCreateFullName() + "-"
                                + planTemplateDb.getCreateName());
                    }
                    else {
                        item.setCreateName("");
                    }
                    String approveStatus = planTemplateDb.getBizCurrent();
                    item.setApproveStatus(approveStatus);
                    if (planTemplateDb.getFirstTime() != null) {
                        String createTimeStr = DateUtil.dateToString(planTemplateDb.getFirstTime(),
                                DateUtil.FORMAT_ONE);
                        item.setCreateTimeStr(createTimeStr);
                    } else {
                        String createTimeStr = DateUtil.dateToString(planTemplateDb.getCreateTime(),
                                DateUtil.FORMAT_ONE);
                        item.setCreateTimeStr(createTimeStr);
                    }
                    items.add(item);
                }

            }
            PageList pageList = new PageList(count, items);
            return pageList;
        }
        catch (RecognitionException e) {
            new FdExceptionPolicy("basicException", "查询语句格式化异常", e);
        }
        catch (TokenStreamException e) {
            new FdExceptionPolicy("basicException", "查询语句开闭缺失", e);
        }
        catch (ClassNotFoundException e) {
            new FdExceptionPolicy("basicException", "查询实体不存在", e);
        }
        catch (Exception e) {
            throw new GWException("其他错误");
        }
        return null;
    }

    @Override
    public PlanTemplate getPlanTemplateEntity(String id) {
        PlanTemplate planTemplate = (PlanTemplate)sessionFacade.getEntity(PlanTemplate.class,id);
        return planTemplate;
    }

    @Override
    public String saveTemplateOptLog(String bizId, String logInfo,String curUserId,String orgId) {
        PlanTempOptLog tempOptLog = new PlanTempOptLog();
        CommonUtil.glObjectSet(tempOptLog);
        tempOptLog.setTmplId(bizId);
        tempOptLog.setLogInfo(logInfo);
        TSUserDto curUser = userService.getUserByUserId(curUserId);
        CommonInitUtil.initGLObjectForCreate(tempOptLog,curUser,orgId);
        sessionFacade.save(tempOptLog);
        return tempOptLog.getId();
    }

    @Override
    public List<PlanTemplateDetail> getTemplatePlanDetailById(String planTemplateId) {
        List<PlanTemplateDetail> detailList = sessionFacade.findHql(
                "from PlanTemplateDetail where planTemplateId=? order by parentPlanId,storeyNo asc",
                planTemplateId);
        return detailList;
    }


    @Override
    public Map<String, List<InputsDto>> queryInputsListMap() {
        Map<String, List<InputsDto>> mapList = new HashMap<String, List<InputsDto>>();
        List<Inputs> tempInputsList = sessionFacade.findHql("from Inputs where useObjectType = 'PLANTEMPLATE' order by createtime desc");
        List<InputsDto> inputsList = JSON.parseArray(JSON.toJSONString(tempInputsList),InputsDto.class);
        if(!CommonUtil.isEmpty(inputsList)){
            for(InputsDto input : inputsList){
                if(CommonUtil.isEmpty(mapList.get(input.getUseObjectId()))){
                    List<InputsDto> inputs = new ArrayList<InputsDto>();
                    inputs.add(input);
                    mapList.put(input.getUseObjectId(), inputs);
                }else{
                    List<InputsDto> inputs = mapList.get(input.getUseObjectId());
                    inputs.add(input);
                    mapList.put(input.getUseObjectId(), inputs);
                }
            }
        }
        return mapList;
    }

    @Override
    public boolean checkTemplateNameBeforeSave(String templateName, String planTemplateId) {
        boolean flag = true;
        List<PlanTemplate> templateList = sessionFacade.findHql(
                "from PlanTemplate where name=? and avaliable = 1", templateName);
        if (!CommonUtil.isEmpty(templateList)) {
            for (PlanTemplate temp : templateList) {
                if (!temp.getId().equals(planTemplateId)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public void savePlanTemplateAndDetail(PlanTemplate template, String curUserId, String orgId, List<Plan> planList) {
        TSUserDto userDto = userService.getUserByUserId(curUserId);
        String planTemplateId = template.getId();
        template.setId(null);
        CommonInitUtil.initGLObjectForCreate(template,userDto,orgId);
        initBusinessObject(template);
        sessionFacade.save(template);
        // 日志
        saveTemplateOptLog(template.getBizId(), PlantemplateConstant.PLAN_TEMPLATE_ADD,userDto,orgId);
        if(!CommonUtil.isEmpty(planList)){
            saveTemplateDetailFromRedis(planTemplateId, template.getId(),planList,userDto,orgId);
        }
    }

    @Override
    public String saveTemplateOptLog(String bizId, String logInfo,TSUserDto userDto,String orgId) {
         PlanTempOptLog tempOptLog = new PlanTempOptLog();
        CommonInitUtil.initGLObjectForCreate(tempOptLog,userDto,orgId);
        tempOptLog.setTmplId(bizId);
        tempOptLog.setLogInfo(logInfo);
        sessionFacade.saveOrUpdate(tempOptLog);
        return tempOptLog.getId();
    }

    private void saveTemplateDetailFromRedis(String planTemplateId, String newTemplateId,List<Plan> detailList,TSUserDto userDto,String orgId) {
        Map<String, String> map = new HashMap<String, String>();

        List<PlanTemplateDetail> detailInsert = new ArrayList<PlanTemplateDetail>();
        List<DeliverablesInfo> deliverablesInsert = new ArrayList<DeliverablesInfo>();
        List<PreposePlanTemplate> preposePlanInsert = new ArrayList<PreposePlanTemplate>();
        List<Inputs> inputsInsert = new ArrayList<Inputs>();
        Map<String, String> deliverMap = new HashMap<String, String>();
        PlanTemplateDetail pd = new PlanTemplateDetail();
        initBusinessObject(pd);
        String bizCurrent = pd.getBizCurrent();
        String bizVersion = pd.getBizVersion();
        LifeCyclePolicy policy = pd.getPolicy();

        DeliverablesInfo d1 = new DeliverablesInfo();
        initBusinessObject(d1);
        String dBizCurrent = d1.getBizCurrent();
        String dBizVersion = d1.getBizVersion();
        LifeCyclePolicy dPolicy = d1.getPolicy();
        int maxPlanNumber = 0;
        int maxStoreyNo = 0;
        if (!CommonUtil.isEmpty(detailList)) {

            Map<String, String> planNameMap = new HashMap<String, String>();
            Map<String, String> planParentMap = new HashMap<String, String>();
            Map<String, Integer> planWorktimeMap = new HashMap<String, Integer>();

            String path = "";
            for (Plan plan : detailList) {
                String tempId = UUIDGenerator.generate();
                map.put(plan.getId(), tempId);
                planNameMap.put(plan.getId(), String.valueOf(plan.getPlanName()));
                planParentMap.put(plan.getId(), plan.getParentPlanId());
                planWorktimeMap.put(plan.getId(), Integer.valueOf(plan.getWorkTime()));
            }

            for (Plan dPlan : detailList) {
                Map<String, String> preposeMapForChildPlan = new HashMap<String, String>();
                List<String> pathListForChild = new ArrayList<String>();
                for (Plan planTem : detailList) {
                    if (!CommonUtil.isEmpty(planTem.getParentPlanId())
                            && dPlan.getId().equals(planTem.getParentPlanId())) {
                        if (Integer.valueOf(planTem.getWorkTime()) > Integer.valueOf(dPlan.getWorkTime())) {
                            throw new GWException(I18nUtil.getValue(planTem.getPlanName()
                                    + "工期超过父计划工期"));
                        }
                        if (!CommonUtil.isEmpty(planTem.getPreposeIds())) {
                            preposeMapForChildPlan.put(planTem.getId(), planTem.getPreposeIds());
                            if (!CommonUtil.isEmpty(path)) {
                                if (!path.contains(planTem.getId())) {
                                    path = path + "," + planTem.getId();
                                }
                                if (!path.contains(planTem.getPreposeIds())) {
                                    path = path + "," + planTem.getPreposeIds();
                                }
                            }
                            else {
                                path = planTem.getId();
                                path = path + "," + planTem.getPreposeIds();
                            }

                        }
                    }
                }
                pathListForChild.add(path);
                if (!CommonUtil.isEmpty(preposeMapForChildPlan)) {
                    checkPreposePostTime(new ArrayList<String>(), pathListForChild, planNameMap,
                            planParentMap, planWorktimeMap);
                }
            }

            for (Plan detail : detailList) {

                PlanTemplateDetail temp = new PlanTemplateDetail();
                CommonInitUtil.initGLObjectForCreate(temp,userDto,orgId);
                // tempId = map.get(detail.getId());
                temp.setId(map.get(detail.getId()));
                temp.setName(detail.getPlanName());
                temp.setIsNecessary(detail.getIsNecessary());
                temp.setMilestone(detail.getMilestone());
                // temp.setPlanNumber(detail.getPlanNumber());
                if (!CommonUtil.isEmpty(detail.getParentPlanId())) {
                    temp.setParentPlanId(map.get(detail.getParentPlanId()));
                }

                temp.setPlanLevel(detail.getPlanLevel());
                temp.setPlanTemplateId(newTemplateId);
                temp.setRemark(detail.getRemark());
                temp.setWorkTime(detail.getWorkTime());
                // temp.setStoreyNo(detail.getStoreyNo());
                temp.setPolicy(policy);
                temp.setBizCurrent(bizCurrent);
                temp.setBizVersion(bizVersion);
                temp.setBizId(UUID.randomUUID().toString());
                temp.setTaskNameType(detail.getTaskNameType());
                temp.setTabCbTemplateId(detail.getTabCbTemplateId());
                temp.setTaskType(detail.getTaskType());

                // 如果导入模板时计划节点需要重新排序
                // 每次都是最大的planNumber+1
                temp.setPlanNumber(maxPlanNumber + 1);
                maxPlanNumber++ ;

                temp.setStoreyNo(maxStoreyNo + 1);
                maxStoreyNo++ ;
                detailInsert.add(temp);
                // save(temp);
                map.put(detail.getId(), temp.getId());
                /*
                 * if(!CommonUtil.isEmpty(detail.getParentPlanId())){
                 * map1.put(detail.getParentPlanId(), temp.getId());
                 * }
                 */
                List<DeliverablesInfo> deliverablesList = detail.getDeliInfoList();
                if (!CommonUtil.isEmpty(deliverablesList)) {
                    for (DeliverablesInfo d : deliverablesList) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        CommonUtil.glObjectSet(info);
                        String newId = UUIDGenerator.generate().toString();
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        deliverMap.put(d.getId(), newId);
                        info.setId(newId);
                        info.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                        info.setUseObjectId(map.get(detail.getId()));
                        info.setName(d.getName());
                        info.setOrigin(d.getOrigin());
                        deliverablesInsert.add(info);
                    }
                }
            }

            for (Plan del : detailList) {
                String preposeIds = del.getPreposeIds();
                if (!CommonUtil.isEmpty(preposeIds)) {
                    String[] preposeIdsArr = preposeIds.split(",");
                    for (String preposeId : preposeIdsArr) {
                        if (!CommonUtil.isEmpty(map.get(preposeId))
                                && !CommonUtil.isEmpty(map.get(del.getId()))) {
                            PreposePlanTemplate p = new PreposePlanTemplate();
                            CommonUtil.glObjectSet(p);
                            p.setId(UUIDGenerator.generate().toString());
                            p.setPlanId(map.get(del.getId()));
                            p.setPreposePlanId(map.get(preposeId));
                            preposePlanInsert.add(p);
                        }
                    }
                }
                // 原数据
                List<Inputs> inputsList = del.getInputList();

                if (!CommonUtil.isEmpty(inputsList)) {
                    for (Inputs input : inputsList) {
                        if (!CommonUtil.isEmpty(map.get(del.getId()))) {
                            CommonUtil.glObjectSet(input);
                            input.setId(UUIDGenerator.generate().toString());
                            input.setUseObjectId(map.get(del.getId()));
                            input.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                            if (PlanConstants.USEOBJECT_TYPE_PLAN.equals(input.getOriginType())
                                    || CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE.equals(input.getOriginType())) {
                                if (CommonUtil.isEmpty(map.get(input.getOriginObjectId()))) {
                                    input.setOriginObjectId(null);
                                    input.setOriginDeliverablesInfoId(null);
                                    input.setOriginType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                                }
                                else {
                                    input.setOriginObjectId(map.get(input.getOriginObjectId()));
                                    input.setOriginDeliverablesInfoId(deliverMap.get(input.getOriginDeliverablesInfoId()));
                                    input.setOriginType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                                }
                            }
                            else if (PlanConstants.PROJECTLIBDOC.equals(input.getOriginType())) {
                                input.setOriginType(null);
                                input.setDocId(null);
                                input.setDocName(null);
                            }
                        }
                    }
                    inputsInsert.addAll(inputsList);
                    String inpStr = JSON.toJSONString(inputsList);
                    redisService.setToRedis("INPUTSLIST", map.get(del.getId()), inpStr);
                }
            }
            savePlanTemplateAllByList(detailInsert, deliverablesInsert, preposePlanInsert,
                    inputsInsert);
        }
    }


    private void checkPreposePostTime(List<String> repeatPathList, List<String> pathList,
                                      Map<String, String> planNameMap,
                                      Map<String, String> planParentMap,
                                      Map<String, Integer> planWorktimeMap) {
        if (!CommonUtil.isEmpty(repeatPathList)) {
            String repeatMessage = "";
            String[] repeatIdList = repeatPathList.get(0).split(",");
            for (int i = 0; i < repeatIdList.length; i++ ) {
                if (!CommonUtil.isEmpty(repeatMessage)) {
                    repeatMessage = repeatMessage + "【" + planNameMap.get(repeatIdList[i]) + "】";
                }
                else {
                    repeatMessage = "【" + planNameMap.get(repeatIdList[i]) + "】";
                }
            }
            Object[] repeatArguments = new String[] {repeatMessage};
            throw new GWException(I18nUtil.getValue(
                    "com.glaway.ids.pm.project.plantemplate.savePlan.checkPlanRelation",
                    repeatArguments));
        }
        else if (!CommonUtil.isEmpty(pathList)) {
            int length = 0;
            String message = "";
            for (String node : pathList) {
                if (!CommonUtil.isEmpty(node)) {
                    String[] nodeArr = node.split(",");
                    for (int i = 0; i < nodeArr.length; i++ ) {
                        if (!CommonUtil.isEmpty(message)) {
                            message = message + "【" + planNameMap.get(nodeArr[i]) + "】";
                        }
                        else {
                            message = "【" + planNameMap.get(nodeArr[i]) + "】";
                        }
                        length = length + planWorktimeMap.get(nodeArr[i]);
                        if (i >= 1 && !CommonUtil.isEmpty(planParentMap.get(nodeArr[i]))
                                && length > planWorktimeMap.get(planParentMap.get(nodeArr[i]))) {
                            Object[] arguments = new String[] {message};
                            throw new GWException(
                                    I18nUtil.getValue(
                                            "com.glaway.ids.pm.project.plantemplate.savePlan.checkPreposeWorkTime",
                                            arguments));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void savePlanTemplateAllByList(List<PlanTemplateDetail> detailInsert, List<DeliverablesInfo> deliverablesInsert, List<PreposePlanTemplate> preposePlanInsert, List<Inputs> inputsInsert) {
        Connection conn = null;

        // 批量插入计划模板相关信息
        PreparedStatement psFordelDetail = null;
        PreparedStatement psFordelDeliverables = null;
        PreparedStatement psFordelPreposePlan = null;
        PreparedStatement psFordelInputs = null;
        Statement st = null;
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        Timestamp updateTime = new Timestamp(System.currentTimeMillis());
        Timestamp firstTime = new Timestamp(System.currentTimeMillis());
        try {
            try {
                conn = jdbcTemplate.getDataSource().getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.setAutoCommit(false);
            // 批量插入计划模板的详细信息
            if (!CommonUtil.isEmpty(detailInsert)) {
                if (StringUtil.isNotEmpty(detailInsert.get(0).getProjectTemplateId())) {
                    st = conn.createStatement();
                    st.execute("delete from PM_PLAN_TEMPLATE_DL where projectTemplateId='"
                            + detailInsert.get(0).getProjectTemplateId() + "'");
                }
                String insertDetail = "insert into PM_PLAN_TEMPLATE_DL ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                        + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
                        + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
                        + " SECURITYLEVEL, AVALIABLE,"
                        + " MILESTONE, NAME, NUM, PARENTPLANID, PLANLEVEL, PLANNUMBER, PLANTEMPLATEID,"
                        + " WORKTIME, STOREYNO, ISNECESSARY, PROJECTTEMPLATEID, REMARK, TASKNAMETYPE, TABCBTEMPLATEID, TASKTYPE)"
                        + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, "
                        + " ?, ?, ?, ?, " + " ?, ?, ?, ?, " + " ?, ?,"
                        + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                psFordelDetail = conn.prepareStatement(insertDetail);
                for (int i = 0; i < detailInsert.size(); i++ ) {
                    psFordelDetail.setObject(1, detailInsert.get(i).getId());
                    psFordelDetail.setObject(2,
                            new Timestamp(System.currentTimeMillis() + (i + 1)));
                    psFordelDetail.setObject(3, detailInsert.get(i).getCreateBy());
                    psFordelDetail.setObject(4, detailInsert.get(i).getCreateFullName());
                    psFordelDetail.setObject(5, detailInsert.get(i).getCreateName());
                    psFordelDetail.setObject(6,
                            new Timestamp(System.currentTimeMillis() + (i + 1)));
                    psFordelDetail.setObject(7, detailInsert.get(i).getUpdateBy());
                    psFordelDetail.setObject(8, detailInsert.get(i).getUpdateFullName());
                    psFordelDetail.setObject(9, detailInsert.get(i).getUpdateName());
                    psFordelDetail.setObject(10, new Timestamp(System.currentTimeMillis()
                            + (i + 1)));
                    psFordelDetail.setObject(11, detailInsert.get(i).getFirstBy());
                    psFordelDetail.setObject(12, detailInsert.get(i).getFirstFullName());
                    psFordelDetail.setObject(13, detailInsert.get(i).getFirstName());
                    psFordelDetail.setObject(14, detailInsert.get(i).getPolicy().getId());
                    psFordelDetail.setObject(15, detailInsert.get(i).getBizCurrent());
                    psFordelDetail.setObject(16, detailInsert.get(i).getBizId());
                    psFordelDetail.setObject(17, detailInsert.get(i).getBizVersion());
                    psFordelDetail.setObject(18, detailInsert.get(i).getSecurityLevel());
                    psFordelDetail.setObject(19, detailInsert.get(i).getAvaliable());
                    psFordelDetail.setObject(20, detailInsert.get(i).getMilestone());
                    psFordelDetail.setObject(21, detailInsert.get(i).getName());
                    psFordelDetail.setObject(22, detailInsert.get(i).getNum());
                    psFordelDetail.setObject(23, detailInsert.get(i).getParentPlanId());
                    psFordelDetail.setObject(24, detailInsert.get(i).getPlanLevel());
                    psFordelDetail.setObject(25, detailInsert.get(i).getPlanNumber());
                    psFordelDetail.setObject(26, detailInsert.get(i).getPlanTemplateId());
                    psFordelDetail.setObject(27, detailInsert.get(i).getWorkTime());
                    psFordelDetail.setObject(28, detailInsert.get(i).getStoreyNo());
                    psFordelDetail.setObject(29, detailInsert.get(i).getIsNecessary());
                    psFordelDetail.setObject(30, detailInsert.get(i).getProjectTemplateId());
                    psFordelDetail.setObject(31, detailInsert.get(i).getRemark());
                    psFordelDetail.setObject(32, detailInsert.get(i).getTaskNameType());
                    psFordelDetail.setObject(33, detailInsert.get(i).getTabCbTemplateId());
                    psFordelDetail.setObject(34, detailInsert.get(i).getTaskType());
                    psFordelDetail.addBatch();
                }
                psFordelDetail.executeBatch();
            }

            // 批量插入交付项先判断是否存在相同数据
            List<DeliverablesInfo> templates = new ArrayList<>();
            if (!CommonUtil.isEmpty(deliverablesInsert)) {
                for (DeliverablesInfo deliverablesInfo : deliverablesInsert) {
                    List<DeliverablesInfo> deliverablesInfos = deliverablesInfoService.queryDeliverableList(deliverablesInfo, 1, 10, false);
                    if (CommonUtil.isEmpty(deliverablesInfos)) {
                        templates.add(deliverablesInfo);
                    }

                }
            }
            if (!CommonUtil.isEmpty(templates)) {
                String insertDeliverables = " insert into PM_DELIVERABLES_INFO ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                        + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
                        + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
                        + " SECURITYLEVEL, AVALIABLE,"
                        + " USEOBJECTID, USEOBJECTTYPE, NAME, ORIGIN, REQUIRED) "
                        + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?,?)";

                psFordelDeliverables = conn.prepareStatement(insertDeliverables);
                for (DeliverablesInfo info : templates) {
                    psFordelDeliverables.setObject(1, info.getId());
                    psFordelDeliverables.setObject(2, createTime);
                    psFordelDeliverables.setObject(3, info.getCreateBy());
                    psFordelDeliverables.setObject(4, info.getCreateFullName());
                    psFordelDeliverables.setObject(5, info.getCreateName());
                    psFordelDeliverables.setObject(6, updateTime);
                    psFordelDeliverables.setObject(7, info.getUpdateBy());
                    psFordelDeliverables.setObject(8, info.getUpdateFullName());
                    psFordelDeliverables.setObject(9, info.getUpdateName());
                    psFordelDeliverables.setObject(10, firstTime);
                    psFordelDeliverables.setObject(11, info.getFirstBy());
                    psFordelDeliverables.setObject(12, info.getFirstFullName());
                    psFordelDeliverables.setObject(13, info.getFirstName());
                    psFordelDeliverables.setObject(14, info.getPolicy().getId());
                    psFordelDeliverables.setObject(15, info.getBizCurrent());
                    psFordelDeliverables.setObject(16, info.getBizId());
                    psFordelDeliverables.setObject(17, info.getBizVersion());
                    psFordelDeliverables.setObject(18, info.getSecurityLevel());
                    psFordelDeliverables.setObject(19, info.getAvaliable());
                    psFordelDeliverables.setObject(20, info.getUseObjectId());
                    psFordelDeliverables.setObject(21, info.getUseObjectType());
                    psFordelDeliverables.setObject(22, info.getName());
                    psFordelDeliverables.setObject(23, info.getOrigin());
                    psFordelDeliverables.setObject(24, info.getRequired());

                    psFordelDeliverables.addBatch();
                }

                psFordelDeliverables.executeBatch();

            }

            // 批量插入前后置计划的信息
            if (!CommonUtil.isEmpty(preposePlanInsert)) {
                String insertPreposePlan = " insert into PM_PREPOSE_PLAN_TEMPLATE ("
                        + "ID, CREATETIME, CREATEBY," + " AVALIABLE,"
                        + " PLANID, PREPOSEPLANID)" + " values ("
                        + " ?, ?, ?, ?," + " ?, ?)";

                psFordelPreposePlan = conn.prepareStatement(insertPreposePlan);

                for (PreposePlanTemplate prepose : preposePlanInsert) {
                    psFordelPreposePlan.setObject(1, prepose.getId());
                    psFordelPreposePlan.setObject(2, createTime);
                    psFordelPreposePlan.setObject(3, prepose.getCreateBy());
                    psFordelPreposePlan.setObject(4, "1");
                    psFordelPreposePlan.setObject(5, prepose.getPlanId());
                    psFordelPreposePlan.setObject(6, prepose.getPreposePlanId());

                    psFordelPreposePlan.addBatch();
                }
                psFordelPreposePlan.executeBatch();

            }
            // 批量保存输入信息
            if (!CommonUtil.isEmpty(inputsInsert)) {
                String sqlForInputs = " insert into PM_INPUTS ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, USEOBJECTID,"
                        + " USEOBJECTTYPE, NAME, ORIGINOBJECTID, ORIGINDELIVERABLESINFOID,DOCID,DOCNAME,OriginType,OriginTypeExt) "
                        + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?,?,?,?,?)";
                psFordelInputs = conn.prepareStatement(sqlForInputs);
                for (Inputs inputs : inputsInsert) {
                    psFordelInputs.setObject(1, inputs.getId());
                    psFordelInputs.setObject(2, createTime);
                    psFordelInputs.setObject(3, inputs.getCreateBy());
                    psFordelInputs.setObject(4, inputs.getCreateFullName());
                    psFordelInputs.setObject(5, inputs.getCreateName());
                    psFordelInputs.setObject(6, updateTime);
                    psFordelInputs.setObject(7, inputs.getUpdateBy());
                    psFordelInputs.setObject(8, inputs.getUpdateFullName());
                    psFordelInputs.setObject(9, inputs.getUpdateName());
                    psFordelInputs.setObject(10, inputs.getUseObjectId());
                    psFordelInputs.setObject(11, inputs.getUseObjectType());
                    psFordelInputs.setObject(12, inputs.getName());
                    psFordelInputs.setObject(13, inputs.getOriginObjectId());
                    psFordelInputs.setObject(14, inputs.getOriginDeliverablesInfoId());
                    psFordelInputs.setObject(15, inputs.getDocId());
                    psFordelInputs.setObject(16, inputs.getDocName());
                    psFordelInputs.setObject(17, inputs.getOriginType());
                    psFordelInputs.setObject(18, inputs.getOriginTypeExt());
                    psFordelInputs.addBatch();
                }
                psFordelInputs.executeBatch();
            }
            conn.commit();
        }
        catch (SQLException ex) {
            try {
                conn.rollback();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
        finally {
            try {
                conn.setAutoCommit(true);
                if (!CommonUtil.isEmpty(st)) {
                    st.close();
                }
                DBUtil.closeConnection(null, psFordelDetail, conn);
                DBUtil.closeConnection(null, psFordelDeliverables, conn);
                DBUtil.closeConnection(null, psFordelPreposePlan, conn);
                DBUtil.closeConnection(null, psFordelInputs, conn);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updatePlanTemplateAndDetail(PlanTemplate template, String type, String name, String remark,String curUserId,String orgId,List<Plan> delList) {
        TSUserDto userDto = userService.getUserByUserId(curUserId);
        PlanTemplate templateNew = new PlanTemplate();
        if (PlantemplateConstant.UPADTE.equalsIgnoreCase(type)) {
            templateNew = upgradeVersion(template, LifeCycleConstant.ReviseModel.MINER, name,
                    remark,userDto,orgId);
        }
        else if (PlantemplateConstant.REVISE.equalsIgnoreCase(type)) {
            templateNew = upgradeVersion(template, LifeCycleConstant.ReviseModel.MAJOR, name,
                    remark,userDto,orgId);
        }

        saveTemplateDetailFromRedis(template.getId(), templateNew.getId(),delList,userDto,orgId);
        String processInstanceId = templateNew.getProcessInstanceId();
        // 更新我的待办及流程信息
        if (!CommonUtil.isEmpty(processInstanceId)) {
            FeignJson fJson= workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    processInstanceId);
            List<Task> tasks =(List<Task>)fJson.getObj();
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByProcInstId(
                    processInstanceId);
            String taskId = "";
            Map<String, Object> variables = new HashMap<String, Object>();
            if (!CommonUtil.isEmpty(tasks)) {
                taskId = tasks.get(0).getId();
                variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(taskId);
            }
            if (!CommonUtil.isEmpty(variables)) {
                variables.put(
                        "editUrl",
                        "/ids-pm-web/planTemplateDetailController.do?planTemplateDetailEdit&dataHeight=500&dataWidth=900&planTemplateId="
                                + templateNew.getId());
                variables.put("viewUrl",
                        "/ids-pm-web/planTemplateDetailController.do?planTemplateDetail&dataHeight=500&dataWidth=900&planTemplateId="
                                + templateNew.getId());
                variables.put("oid", BpmnConstants.OID_PLANTEMPLATE + templateNew.getId());
                workFlowFacade.getWorkFlowCommonService().setRunVariables(processInstanceId,
                        variables);
            }
            // 获取流程显示名称
            ProcessDefinition processDefinition = workFlowFacade.getWorkFlowCommonService().getProcessDefinitionByProcInstId(
                    processInstanceId, false);
            if (!CommonUtil.isEmpty(processDefinition)) {
                String bpmnDisplayName = processDefinition.getName();
                myStartedTask.setObjectName(name);
                myStartedTask.setTaskNumber(templateNew.getId());
                myStartedTask.setTitle(ProcessUtil.getProcessTitle(name, bpmnDisplayName,
                        processInstanceId));
                workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
            }
        }
    }

    @Override
    public PlanTemplate upgradeVersion(PlanTemplate template, String mode, String name, String remark,TSUserDto userDto,String orgId) {
        List<PlanTemplate> templateList = findBusinessObjectHistoryByBizId(PlanTemplate.class,
                template.getBizId());
        PlanTemplate templateLatest = new PlanTemplate();
        if (!CommonUtil.isEmpty(templateList)) {
            templateLatest = templateList.get(0);
        }
        PlanTemplate templateNew = reviseBusinessObject(templateLatest, mode);
        templateNew.setName(name);
        templateNew.setRemark(remark);
        if (LifeCycleConstant.ReviseModel.MAJOR.equals(mode)) {
            saveTemplateOptLog(template.getBizId(), PlantemplateConstant.PLAN_TEMPLATE_REVISE,userDto,orgId);
            templateNew.setBizCurrent(PlantemplateConstant.STATUS_XIUDING);
            templateNew.setProcessInstanceId(null);
            sessionFacade.saveOrUpdate(templateNew);
        }
        else {
            if (!CommonUtil.isEmpty(template.getProcessInstanceId())) {
                template.setProcessInstanceId(null);
                sessionFacade.saveOrUpdate(template);
            }
        }
        return templateNew;
    }

    @Override
    public void deletePlanTemplate(PlanTemplateReq planTemplateReq, String userId) throws GWException {
        String ids = planTemplateReq.getId();
        for (String id : ids.split(",")) {
            PlanTemplate planTemplate = (PlanTemplate)sessionFacade.getEntity(PlanTemplate.class, id);

            // 获得此记录是否在流程中
            MyTodoTaskDto searchTask = new MyTodoTaskDto();
            searchTask.setTaskNumber(id);
            long t = workFlowFacade.getWorkFlowTodoTaskService().getActRuTaskByConditionPageCount(
                    searchTask);
            if (t == 0L) { // 不在流程中
                planTemplate.setAvaliable("0"); // 不可用
                planTemplate.setUpdateBy(userId);
                planTemplate.setUpdateTime(new Date());
                sessionFacade.saveOrUpdate(planTemplate);
            }
            else if ("shenhe".equals(planTemplate.getBizCurrent())) {// 在流程中,并且状态为 "shenhe"
                // 返回不能删除
                Object[] arguments = new String[] {planTemplate.getName()};
                throw new GWException(I18nUtil.getValue(
                        "com.glaway.ids.pm.project.plantemplate.plantemplate.delete", arguments));
            }
            else { // 在在流程中并且被驳回,状态重新回到"nizhi"
                MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                        planTemplate.getId());
                // 1.首先终止流程
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                        planTemplate.getId(), myStartedTask.getProcInstId(), "");
                // 2.更新该业务数据及其相关信息
                planTemplate.setAvaliable("0"); // 不可用
                planTemplate.setUpdateBy(userId);
                planTemplate.setUpdateTime(new Date());
                sessionFacade.saveOrUpdate(planTemplate);
                // 3.更新业务数据生命周期、状态 1中已经处理完

            }
        }
    }

    @Override
    public void updatePlanTemplate(PlanTemplateReq planTemplateReq, String userId) throws GWException {
        String ids = planTemplateReq.getId();
        for (String id : ids.split(",")) {
            PlanTemplate planTemplate = (PlanTemplate)sessionFacade.getEntity(PlanTemplate.class, id);
            if (StringUtils.isNotEmpty(planTemplateReq.getBizCurrent())) {
                planTemplate.setBizCurrent(planTemplateReq.getBizCurrent());
            }
            planTemplate.setUpdateBy(userId);
            planTemplate.setUpdateTime(new Date());
            sessionFacade.saveOrUpdate(planTemplate);
        }
    }

    @Override
    public PlanTemplate savePlanTemplateDetail(PlanTemplate planTemplate, List<net.sf.mpxj.Task> taskList,TSUserDto userDto,String orgId) throws GWException {
        CommonInitUtil.initGLObjectForCreate(planTemplate,userDto,orgId);
        // 保存计划模板
        initBusinessObject(planTemplate);
        planTemplate.setCreateTime(new Date());
        // planTemplate.setStatus(ConfigStateConstants.START);
        sessionFacade.save(planTemplate);

        // 如果配置为不需要审批流，则直接生效
        /*
         * if(true){
         * moveBusinessObjectByStep(planTemplate,
         * planTemplate.getPolicy().getLifeCycleStatusList().size()-1);
         * }
         */

        Map<String, Object> paraMap = new HashMap<String, Object>();

        // 遍历开始

        List<net.sf.mpxj.Task> taskListTemp = new ArrayList<net.sf.mpxj.Task>();

        for (net.sf.mpxj.Task task : taskList) {
            if (task.getID() != 0 && StringUtils.isNotEmpty(task.getName())) {
                taskListTemp.add(task);
            }
        }
        taskList.clear();
        taskList.addAll(taskListTemp);

        // 若模板中的业务数据为空，则提示且不进行导入操作
        if (CommonUtil.isEmpty(taskList)) {
            throw new GWException(I18nUtil.getValue("com.glaway.ids.common.importDataIsNull"));
        }

        // 性能优化计划模板插入数据多的情况下 减少与数据库的交互缩短时间
        // 存放批量插入的计划模板中计划信息
        List<PlanTemplateDetail> detailInsert = new ArrayList<PlanTemplateDetail>();
        // 存放计划模板中 批量插入交付项的信息
        List<DeliverablesInfo> deliverablesInsertOld = new ArrayList<DeliverablesInfo>();

        List<DeliverablesInfo> deliverablesInsertNew = new ArrayList<DeliverablesInfo>();
        // 存放计划模板中前后置计划的相关信息
        List<PreposePlanTemplate> preposePlanInsert = new ArrayList<PreposePlanTemplate>();

        // 存放计划交付项的map key计划详情的ID value详情绑定的交付项目实例
        Map<String, Object> paraMapDeliverablesInfo = new HashMap<String, Object>();

        PlanTemplateDetail d = new PlanTemplateDetail();
        initBusinessObject(d);
        CommonInitUtil.initGLObjectForCreate(d,userDto,orgId);
        String bizCurrent = d.getBizCurrent();
        String bizVersion = d.getBizVersion();
        LifeCyclePolicy policy = d.getPolicy();

        DeliverablesInfo dbi = new DeliverablesInfo();
        initBusinessObject(dbi);
        CommonInitUtil.initGLObjectForCreate(dbi,userDto,orgId);
        String bizCurrentdbi = dbi.getBizCurrent();
        String bizVersiondbi = dbi.getBizVersion();
        LifeCyclePolicy policydbi = d.getPolicy();
        // 修改性能BUG计划等级查询问题
        BusinessConfig bcQuery = new BusinessConfig();
        bcQuery.setConfigType(ConfigTypeConstants.PLANLEVEL);

        List<BusinessConfig> businessConfigList = businessConfigService.searchUseableBusinessConfigs(bcQuery);
        int maxPlanNumber = 0;
        int maxStoreyNo = 0;
        for (net.sf.mpxj.Task task : taskListTemp) {
            if (task.getID() == 0 || StringUtils.isEmpty(task.getName())) {
                continue;
            }

            PlanTemplateDetail detail = new PlanTemplateDetail();
            detail.setPlanNumber(task.getID());
            detail.setPlanTemplateId(planTemplate.getId());

            // 如果里程碑为否,则工期不能为零,提示"修改后重新上传"
            if (task.getMilestone() == false && task.getDuration() == null) {
                throw new GWException(
                        I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.savePlanTemplateDetailOne"));
            }
            if (task.getMilestone() == false && task.getDuration().getDuration() == 0) {
                throw new GWException(
                        I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.savePlanTemplateDetailTwo"));
            }

            if (StringUtils.isNotEmpty(task.getName())) {
                if (task.getName().trim().length() > 30) {
                    Object[] arguments = new String[] {task.getID().toString(), task.getName()};
                    throw new GWException(
                            I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plantemplate.plantemplate.savePlanTemplateDetailThree",
                                    arguments));
                }
            }
            detail.setName(task.getName().trim());

            if (!CommonUtil.mapIsEmpty(MppParseUtil.columnIndexMap, ConfigTypeConstants.PLANLEVEL)) {
                // 判断级别是否存在,如果存在保存级别编号
                /*
                 * List<BusinessConfig> businessConfigList = getBusinessConfigByType(task,
                 * ConfigTypeConstants.PLANLEVEL);
                 * if (businessConfigList != null && businessConfigList.size() > 0) {
                 * detail.setPlanLevel(businessConfigList.get(0).getId().trim());
                 * }
                 */

                String planLevel = null;
                // 重新判断计划等级是否存在 润在保存级别的编号
                if (!CommonUtil.isEmpty(task.getText((int)MppParseUtil.columnIndexMap.get(ConfigTypeConstants.PLANLEVEL)))
                        && !CommonUtil.isEmpty(businessConfigList)) {
                    planLevel = task.getText((int)MppParseUtil.columnIndexMap.get(ConfigTypeConstants.PLANLEVEL));
                    for (BusinessConfig businessConfig : businessConfigList) {
                        if (businessConfig.getName().equals(planLevel)) {
                            detail.setPlanLevel(businessConfig.getId());
                            break;
                        }
                    }

                }

            }
            detail.setMilestone(String.valueOf(task.getMilestone()).trim());

            if (task.getManualDuration() != null) {
                String worktime = String.valueOf(task.getManualDuration().getDuration()).trim();
                if (!CommonUtil.isEmpty(worktime)) {
                    int l = worktime.lastIndexOf(".");
                    if (l != -1) {
                        worktime = worktime.substring(0, l);
                    }
                }
                detail.setWorkTime(worktime);
            }
            else {
                String worktime = String.valueOf(task.getDuration().getDuration()).trim();
                if (!CommonUtil.isEmpty(worktime)) {
                    int l = worktime.lastIndexOf(".");
                    if (l != -1) {
                        worktime = worktime.substring(0, l);
                    }
                }
                detail.setWorkTime(worktime);
            }
            if (!CommonUtil.isEmpty(task.getParentTask())) {
                // 父计划工期
                String parentWorkTime = "";
                if (task.getParentTask().getManualDuration() != null) {
                    parentWorkTime = String.valueOf(
                            task.getParentTask().getManualDuration().getDuration()).trim();
                    if (!CommonUtil.isEmpty(parentWorkTime)) {
                        int l = parentWorkTime.lastIndexOf(".");
                        if (l != -1) {
                            parentWorkTime = parentWorkTime.substring(0, l);
                        }
                        if (Integer.valueOf(detail.getWorkTime()) > Integer.valueOf(parentWorkTime)) {
                            Object[] errrrArguments = new String[] {task.getID().toString(),
                                    task.getName()};
                            throw new GWException(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plantemplate.import.checkWorktimeFour",
                                    errrrArguments));
                        }
                    }
                }
                else {
                    parentWorkTime = String.valueOf(
                            task.getParentTask().getDuration().getDuration()).trim();
                    if (!CommonUtil.isEmpty(parentWorkTime)) {
                        int l = parentWorkTime.lastIndexOf(".");
                        if (l != -1) {
                            parentWorkTime = parentWorkTime.substring(0, l);
                        }
                        if (Integer.valueOf(detail.getWorkTime()) > Integer.valueOf(parentWorkTime)) {
                            Object[] errrrArguments = new String[] {task.getID().toString(),
                                    task.getName()};
                            throw new GWException(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plantemplate.import.checkWorktimeFour",
                                    errrrArguments));
                        }
                    }
                }
            }
            // 如果有上级的ID，需要获得上级对应的记录的ID
            if (task.getParentTask() != null
                    && !CommonUtil.mapIsEmpty(paraMap, task.getParentTask().getID().toString())) {
                String parentId = (String)paraMap.get(task.getParentTask().getID().toString());
                detail.setParentPlanId(parentId.trim());
            }
            detail.setNum(task.getID());

            // 保存计划模板详细
            // planTemplateDetailService.initBusinessObject(detail);
            detail.setCreateBy(planTemplate.getCreateBy());
            detail.setCreateTime(new Date());

            // 手动赋值
            detail.setId(UUIDGenerator.generate().toString());
            // planTemplateDetailService.save(detail);
            detail.setUpdateTime(new Date());
            detail.setUpdateBy(planTemplate.getCreateBy());
            detail.setAvaliable("1");
            // 如果导入模板时计划节点需要重新排序
            // 每次都是最大的planNumber+1
            detail.setPlanNumber(maxPlanNumber + 1);
            maxPlanNumber++ ;

            detail.setStoreyNo(maxStoreyNo + 1);
            maxStoreyNo++ ;

            detail.setBizCurrent(bizCurrent);
            detail.setBizVersion(bizVersion);
            detail.setPolicy(policy);
            detail.setBizId(UUID.randomUUID().toString());
            detail.setFirstTime(new Date());
            Short securityLevel = 1;
            detail.setSecurityLevel(securityLevel);

            detailInsert.add(detail);

            // 计划上级的ID保存计划模板详细的ID
            paraMap.put(task.getID().toString(), detail.getId());

            // 判断交付项名称是否存在,如果存在保存交付项名称编号 获取交付项的名称信息
            String documents = task.getText((int)MppParseUtil.columnIndexMap.get(ConfigTypeConstants.DELIVER_STANDARDNAME));
            detail.setDocuments(documents);
            // 先获取所有的交付项然后做处理
            if (StringUtils.isNotEmpty(documents)) {
                List<DeliverablesInfo> deliverables = new ArrayList<DeliverablesInfo>();
                for (String document : documents.split(",")) {
                    DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
                    deliverablesInfo.setName(document);
                    deliverablesInfo.setUseObjectId(detail.getId());
                    deliverablesInfo.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                    deliverablesInfo.setId(UUIDGenerator.generate().toString());
                    deliverablesInfo.setUpdateTime(new Date());
                    deliverablesInfo.setUpdateBy(planTemplate.getCreateBy());
                    deliverablesInfo.setAvaliable("1");

                    deliverablesInfo.setCreateBy(planTemplate.getCreateBy());
                    deliverablesInfo.setCreateTime(new Date());
                    deliverablesInfo.setBizCurrent(bizCurrentdbi);
                    deliverablesInfo.setBizVersion(bizVersiondbi);
                    deliverablesInfo.setPolicy(policydbi);
                    deliverablesInfo.setBizId(UUID.randomUUID().toString());
                    deliverablesInfo.setFirstTime(new Date());
                    Short securityLeveldbi = 1;
                    deliverablesInfo.setSecurityLevel(securityLeveldbi);
                    deliverablesInsertOld.add(deliverablesInfo);

                    deliverables.add(deliverablesInfo);

                }
                paraMapDeliverablesInfo.put(detail.getId(), deliverables);
            }

            /*
             * List<DeliverablesInfo>
             * deliverablesInfos=deliverablesInfoService.saveDeliverableByPlanTemplateDetail(null,
             * null,
             * CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE, detail, task.getID().toString());
             */

            /* String documentNew=""; */

            /*
             * //传入批量插入的实例
             * if(deliverablesInfos!=null&&deliverablesInfos.size()>0)
             * {
             * for (DeliverablesInfo deliverablesInfo : deliverablesInfos)
             * {
             * deliverablesInfo.setId(UUIDGenerator.generate().toString());
             * deliverablesInfo.setUpdateTime(new Date());
             * deliverablesInfo.setUpdateBy(planTemplate.getCreateBy());
             * deliverablesInfo.setAvaliable("1");
             * deliverablesInfo.setCreateBy(planTemplate.getCreateBy());
             * deliverablesInfo.setCreateTime(new Date());
             * deliverablesInfo.setBizCurrent(bizCurrentdbi);
             * deliverablesInfo.setBizVersion(bizVersiondbi);
             * deliverablesInfo.setPolicy(policydbi);
             * deliverablesInfo.setBizId(UUID.randomUUID().toString());
             * deliverablesInfo.setFirstTime(new Date());
             * Short securityLeveldbi=1;
             * deliverablesInfo.setSecurityLevel(securityLeveldbi);
             * deliverablesInsert.add(deliverablesInfo);
             * }
             * }
             */
        }
        paraMap.put("list", preposePlanInsert);

        deliverablesInsertNew = getNewDeliverables(deliverablesInsertOld, paraMapDeliverablesInfo,
                detailInsert, bizCurrentdbi, bizVersiondbi, policydbi);
        preposePlanTemplateService.savePreposePlanTemplateByMpp(taskList, paraMap,
                planTemplate.getCreateBy());

        Map<String, String> preposeMap = new HashMap<String, String>();
        Map<String, Integer> planWorktimeMap = new HashMap<String, Integer>();
        Map<String, String> planNameMap = new HashMap<String, String>();
        Map<String, String> planParentMap = new HashMap<String, String>();
        String path = "";

        if (!CommonUtil.isEmpty(detailInsert)) {
            for (PlanTemplateDetail detail : detailInsert) {
                planNameMap.put(detail.getId(), detail.getName());
                planWorktimeMap.put(detail.getId(), Integer.valueOf(detail.getWorkTime()));
                planParentMap.put(detail.getId(), detail.getParentPlanId());
            }
        }
        if (!CommonUtil.isEmpty(preposePlanInsert)) {
            for (PreposePlanTemplate pose : preposePlanInsert) {
                preposeMap.put(pose.getPlanId(), pose.getPreposePlanId());
                /*
                 * if(!CommonUtil.isEmpty(path)){
                 * if(!path.contains(pose.getPlanId())){
                 * path = path +","+ pose.getPlanId();
                 * }
                 * if(!path.contains(pose.getPreposePlanId())){
                 * path = path +","+ pose.getPreposePlanId();
                 * }
                 * }else{
                 * path = pose.getPlanId();
                 * path = path +","+ pose.getPreposePlanId();
                 * }
                 */
            }
            for (PreposePlanTemplate pose : preposePlanInsert) {
                List<String> repeatPathList = new ArrayList<String>();
                List<String> pathList = new ArrayList<String>();
                getLinkPath(pose.getPlanId(), pose.getPlanId(), preposeMap, pathList,
                        repeatPathList);
                checkPreposePostTime(new ArrayList<String>(), pathList, planNameMap,
                        planParentMap, planWorktimeMap);
            }
        }

        savePlanTemplateAllByList(detailInsert, deliverablesInsertNew, preposePlanInsert, null);
        return planTemplate;
    }


    // 根据项目配置再次筛选交付项目
    private List<DeliverablesInfo> getNewDeliverables(List<DeliverablesInfo> deliverablesInsertOld,
                                                      Map<String, Object> paraMapDeliverablesInfo,
                                                      List<PlanTemplateDetail> detailInsert,
                                                      String bizCurrentdbi, String bizVersiondbi,
                                                      LifeCyclePolicy policydbi) {
        List<DeliverablesInfo> deliverablesInsertNew = new ArrayList<DeliverablesInfo>();
        String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
        // 如果是可以修改的或强制启用名称的，只导入系统的交付项
        if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {

            for (PlanTemplateDetail planTemplateDetail : detailInsert) {
                Map<String, Object> parmMap = new HashMap<String, Object>();

                List<DeliverablesInfo> deliverables = new ArrayList<DeliverablesInfo>();
                if (paraMapDeliverablesInfo.get(planTemplateDetail.getId()) != null) {
                    deliverables = (List<DeliverablesInfo>)paraMapDeliverablesInfo.get(planTemplateDetail.getId());
                }
                for (DeliverablesInfo deliverablesInfo : deliverables) {
                    parmMap.put(deliverablesInfo.getName(), deliverablesInfo.getName());
                }

                List<DeliverablesInfo> deliverablesInsert = new ArrayList<DeliverablesInfo>();

                deliverablesInsert = saveSystemDeliverable(deliverablesInsertOld, parmMap,
                        planTemplateDetail, true, bizCurrentdbi, bizVersiondbi, policydbi);
                if (deliverablesInsert != null && deliverablesInsert.size() > 0) {
                    for (DeliverablesInfo deliverablesInfo : deliverablesInsert) {
                        deliverablesInsertNew.add(deliverablesInfo);
                    }

                }
            }

            return deliverablesInsertNew;

        }
        else if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)) { // 如果是启用名称库导入系统的交付项，并导入自己的交付项

            for (PlanTemplateDetail planTemplateDetail : detailInsert) {

                Map<String, Object> parmMap = new HashMap<String, Object>();

                List<DeliverablesInfo> deliverables = new ArrayList<DeliverablesInfo>();
                if (paraMapDeliverablesInfo.get(planTemplateDetail.getId()) != null) {
                    deliverables = (List<DeliverablesInfo>)paraMapDeliverablesInfo.get(planTemplateDetail.getId());
                }

                for (DeliverablesInfo deliverablesInfo : deliverables) {
                    parmMap.put(deliverablesInfo.getName(), deliverablesInfo.getName());
                }

                saveSystemDeliverable(deliverablesInsertOld, parmMap, planTemplateDetail, false,
                        bizCurrentdbi, bizVersiondbi, policydbi);
            }

            return deliverablesInsertOld;

        }
        else { // 其它的只导入自己的交付项

            return deliverablesInsertOld;

        }

    }


    // 如果是启用活动名称库 或者 是强制启用 活动名称库 交付项的插入 flag 为false 启用活动名称库 为true 为强制启用活动名称库
    private List<DeliverablesInfo> saveSystemDeliverable(List<DeliverablesInfo> deliverablesInsertOld,
                                                         Map<String, Object> paraMapDeliverablesInfo,
                                                         PlanTemplateDetail planTemplateDetail,
                                                         Boolean flag, String bizCurrentdbi,
                                                         String bizVersiondbi,
                                                         LifeCyclePolicy policydbi) {
        List<DeliverablesInfo> deliverablesInsertNew = new ArrayList<DeliverablesInfo>();

        // 如果系统中不存在计划名称，不可以导入
        NameStandardDto condition = new NameStandardDto();
        condition.setName(planTemplateDetail.getName());
        condition.setStopFlag(ConfigStateConstants.START);
        /**
         * 标准名称库查询由于多个地方被重复调用，需要做缓存
         */
        List<NameStandardDto> nameStandardList = nameStandardService.searchNameStandardsAccurate(condition);
        if (nameStandardList == null || nameStandardList.size() <= 0) {
            if (flag) {
                log.warn("计划名称【" + planTemplateDetail.getName() + "】不在名称库中，请先联系名称库管理员后再重新上传导入!");
                Object[] arguments = new String[] {planTemplateDetail.getName()};
                throw new GWException(I18nUtil.getValue(
                        "com.glaway.ids.pm.project.plan.deliverables.saveSystemDeliverable", arguments));
            }
            else {
                return deliverablesInsertNew;
            }
        }
        NameStandardDto nameStandard = nameStandardList.get(0);
        List<NameStandardDeliveryRelationDto> nameStandardDeliveryRelationList = new ArrayList<NameStandardDeliveryRelationDto>();
        if ("启用".equals(nameStandard.getStopFlag())) {
            NameStandardDeliveryRelationDto relation = new NameStandardDeliveryRelationDto();
            relation.setNameStandardId(nameStandard.getId());
            nameStandardDeliveryRelationList = nameStandardService.searchNoPage(relation);
        }
        if (nameStandardDeliveryRelationList == null
                || nameStandardDeliveryRelationList.size() <= 0) {
            log.info("没有可以导入的交付项");
            return deliverablesInsertNew;
        }
        for (NameStandardDeliveryRelationDto nameStandardDeliveryRelation : nameStandardDeliveryRelationList) {
            DeliveryStandardDto deliveryStandardDto = nameStandardDeliveryRelation.getDeliveryStandard();
            DeliveryStandard deliveryStandard = new DeliveryStandard();
            Dto2Entity.copyProperties(deliveryStandardDto,deliveryStandard);
            if (nameStandardDeliveryRelation.getDeliveryStandardId() != null) {
                if (deliveryStandard != null && "启用".equals(deliveryStandard.getStopFlag())) {
                    // 判断是否强制启用活动名称库
                    if (flag) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        info.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                        info.setUseObjectId(planTemplateDetail.getId());
                        info.setName(deliveryStandard.getName());
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);

                        info.setId(UUIDGenerator.generate().toString());
                        info.setUpdateTime(new Date());
                        info.setUpdateBy(planTemplateDetail.getCreateBy());
                        info.setAvaliable("1");

                        info.setCreateBy(planTemplateDetail.getCreateBy());
                        info.setCreateTime(new Date());
                        info.setBizCurrent(bizCurrentdbi);
                        info.setBizVersion(bizVersiondbi);
                        info.setPolicy(policydbi);
                        info.setBizId(UUID.randomUUID().toString());
                        info.setFirstTime(new Date());
                        Short securityLeveldbi = 1;
                        info.setSecurityLevel(securityLeveldbi);
                        deliverablesInsertNew.add(info);
                    }
                    else {
                        if (!CommonUtil.mapIsEmpty(paraMapDeliverablesInfo,
                                deliveryStandard.getName())) { // 如果此名称已存在，不需要保存
                            log.info("交付项名称【" + planTemplateDetail.getName() + "】已存在");
                            continue;
                        }

                        DeliverablesInfo info = new DeliverablesInfo();
                        info.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                        info.setUseObjectId(planTemplateDetail.getId());
                        info.setName(deliveryStandard.getName());
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);

                        info.setId(UUIDGenerator.generate().toString());
                        info.setUpdateTime(new Date());
                        info.setUpdateBy(planTemplateDetail.getCreateBy());
                        info.setAvaliable("1");

                        info.setCreateBy(planTemplateDetail.getCreateBy());
                        info.setCreateTime(new Date());
                        info.setBizCurrent(bizCurrentdbi);
                        info.setBizVersion(bizVersiondbi);
                        info.setPolicy(policydbi);
                        info.setBizId(UUID.randomUUID().toString());
                        info.setFirstTime(new Date());
                        Short securityLeveldbi = 1;
                        info.setSecurityLevel(securityLeveldbi);
                        deliverablesInsertOld.add(info);
                    }

                    // 保存到交付项表

                }
            }

        }

        if (flag) {

            return deliverablesInsertNew;
        }
        else {
            return deliverablesInsertOld;
        }
    }


    private void getLinkPath(String tempPlanId, String path, Map<String, String> preposeMap,
                             List<String> pathList, List<String> repeatPathList) {
        if (!CommonUtil.isEmpty(preposeMap) && !CommonUtil.isEmpty(preposeMap.get(tempPlanId))) {
            String preposeIds = preposeMap.get(tempPlanId);
            String[] detailIds = preposeIds.split(",");
            for (String detailId : detailIds) {
                String[] pathArr = path.split(",");
                List<String> tempPathList = Arrays.asList(pathArr);
                if (tempPathList.contains(detailId)) {
                    // String repeatPath = path + "," + detailId;
                    repeatPathList.add(path);
                    break;
                }
                else {
                    String curPath = path + "," + detailId;
                    getLinkPath(detailId, curPath, preposeMap, pathList, repeatPathList);
                }
            }
        }
        else {
            pathList.add(path);
        }
    }

    @Override
    public List<MppInfo> saveMppInfo(PlanTemplateReq planTemplateReq) {
        // 通过计划模板ID获得WBS计划
        PlanTemplateDetailReq planTemplateDetailReq = new PlanTemplateDetailReq();
        if("projectTemplate".equals(planTemplateReq.getExportType())){
            planTemplateDetailReq.setProjectTemplateId(planTemplateReq.getId());
        }else{
            planTemplateDetailReq.setPlanTemplateId(planTemplateReq.getId());
        }

        List<PlanTemplateDetail> planTemplateDetailList = new ArrayList<>();
        List<T> ptList = planTemplateDetailService.getList(planTemplateDetailReq,
                null, null);
        if(CommonUtil.isEmpty(planTemplateDetailList)){
            String projStr= (String)redisService.getFromRedis("PROJTMPPLANLIST", planTemplateReq.getId());
            if(!CommonUtil.isEmpty(projStr)){
                List<Plan> planList = JSON.parseArray(projStr, Plan.class);
                if(!CommonUtil.isEmpty(planList)){
                    for(Plan p : planList){
                        PlanTemplateDetail detail = new PlanTemplateDetail();
                        detail.setId(p.getId());
                        detail.setName(p.getPlanName());
                        detail.setPlanLevel(p.getPlanLevel());
                        detail.setWorkTime(p.getWorkTime());
                        detail.setMilestone(p.getMilestone());
                        detail.setPreposeIds(p.getPreposeIds());
                        planTemplateDetailList.add(detail);
                    }
                }
            }
        }else{
            planTemplateDetailList = CodeUtils.JsonListToList(ptList,PlanTemplateDetail.class);
        }


        List<Plan> planTemplateDetailList1 = new ArrayList<Plan>();
        if(CommonUtil.isEmpty(planTemplateDetailList)){
            if(!CommonUtil.isEmpty(planTemplateDetailReq.getPlanTemplateId())){

                String detailStr = (String)redisService.getFromRedis("TEMPLATEPLANLIST", planTemplateDetailReq.getPlanTemplateId());
                planTemplateDetailList1 = JSON.parseArray(detailStr,Plan.class);
            }

        }

        List<MppInfo> mppList = new ArrayList<MppInfo>();

        // 通过WBS计划获得交付项ID
        int i = 1;
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if(!CommonUtil.isEmpty(planTemplateDetailList)){
            for (PlanTemplateDetail planTemplateDetail : planTemplateDetailList) {

                // 在这保存的不是taskId需要转换
                mppList.add(getMppInfo(planTemplateDetail, i));
                // 计划上级的ID保存MPP的ID
                paraMap.put(planTemplateDetail.getId(), i++ );
            }
        }else{
            if(!CommonUtil.isEmpty(planTemplateDetailList1)){
                for (Plan t : planTemplateDetailList1) {

                    // 通过交付项获ID得交付项
                    PlanTemplateDetail planTemplateDetail = new PlanTemplateDetail();
                    planTemplateDetail.setId(t.getId());
                    planTemplateDetail.setMilestone(t.getMilestone());
                    planTemplateDetail.setPreposeIds(t.getPreposeIds());
                    planTemplateDetail.setWorkTime(t.getWorkTime());
                    planTemplateDetail.setParentPlanId(t.getParentPlanId());
                    planTemplateDetail.setName(t.getPlanName());
                    planTemplateDetail.setPlanLevel(t.getPlanLevel());
                    // 在这保存的不是taskId需要转换
                    mppList.add(getMppInfo(planTemplateDetail, i));
                    // 计划上级的ID保存MPP的ID
                    paraMap.put(planTemplateDetail.getId(), i++ );
                }
            }

        }


        for (MppInfo mppInfo : mppList) {
            if (mppInfo.getParentId() != null
                    && !CommonUtil.mapIsEmpty(paraMap, mppInfo.getParentId())) {
                mppInfo.setParentId(paraMap.get(mppInfo.getParentId()).toString());
            }
        }

        // 通过计划编号获得前置Id
        for (MppInfo mppInfo : mppList) {
            String[] preposeSplit = mppInfo.getPreposeName().split("[,]");
            // 把数据组成MPP的数据
            StringBuffer preposeSb = new StringBuffer();
            int k = 0;
            for (String preposeStr : preposeSplit) {
                if (preposeStr != null && !CommonUtil.mapIsEmpty(paraMap, preposeStr)) {
                    if (k > 0) {
                        preposeSb.append(",");
                    }
                    preposeSb.append(paraMap.get(preposeStr));
                    k++ ;
                }
            }
            mppInfo.setPreposeName(preposeSb.toString());

        }

        return mppList;
    }


    /**
     * Description: <br>
     * 组装MPP数据<br>
     *
     * @param planTemplateDetail
     * @param i
     * @return
     * @see
     */
    private MppInfo getMppInfo(PlanTemplateDetail planTemplateDetail, int i) {
        MppInfo mppInfo = new MppInfo();

        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        deliverablesInfo.setUseObjectId(planTemplateDetail.getId());
        List<DeliverablesInfo> deliverablesList = deliverablesInfoService.queryDeliverableList(
                deliverablesInfo, 0, 0, false);

        // 把数据组成MPP的数据
        StringBuffer sb = new StringBuffer();
        int j = 0;
        for (DeliverablesInfo deliverables : deliverablesList) {

            if (j > 0) {
                sb.append(",");
            }
            sb.append(deliverables.getName());
            j++ ;
        }
        mppInfo.setDocumentName(sb.toString());

        PreposePlanTemplate preposePlanTemplate = new PreposePlanTemplate();
        preposePlanTemplate.setPlanId(planTemplateDetail.getId());
        List<PreposePlanTemplate> preposePlanTemplateList = preposePlanTemplateService.getPreposePlansByPreposePlanTemplate(
                preposePlanTemplate, 0, 10, false);
        // 把数据组成MPP的数据
        StringBuffer preposeSb = new StringBuffer();
        int k = 0;
        for (PreposePlanTemplate preposePlanTemplates : preposePlanTemplateList) {
            if (k > 0) {
                preposeSb.append(",");
            }
            preposeSb.append(preposePlanTemplates.getPreposePlanId());
            k++ ;
        }
        mppInfo.setPreposeName(preposeSb.toString());

        mppInfo.setId(i);
        mppInfo.setDuration(planTemplateDetail.getWorkTime());
        mppInfo.setName(planTemplateDetail.getName());
        mppInfo.setParentId(planTemplateDetail.getParentPlanId());

        // 如果等级不为空，需要获得等级的名称
        if (StringUtils.isNotEmpty(planTemplateDetail.getPlanLevel())) {
            BusinessConfig businessConfig =(BusinessConfig) sessionFacade.getEntity(
                    BusinessConfig.class, planTemplateDetail.getPlanLevel());
            if (businessConfig != null && StringUtils.isNotEmpty(businessConfig.getName())) {
                mppInfo.setPlanLevel(businessConfig.getName());
            }
        }
        if (StringUtils.isNotEmpty(planTemplateDetail.getMilestone())) {
            mppInfo.setMilestone(Boolean.valueOf(planTemplateDetail.getMilestone()));
        }
        return mppInfo;
    }


    private Map<String,String> checkDataNew(int rowNum, String strForBc, String switchStr, Map<String, String> standardNameMap, Map<String, String> errorMsgMap, Map<String, String> deliveryNameMap, List<String> numList, Map<String, String> planLevelMap) {
        String[] data = strForBc.split(";");
        if (!CommonUtil.isEmpty(data)) {
            String number = data[0];
            String name = data[1];
            String taskNameType = data[2];
            String level = data[3];
            String worktime = data[4];
            String milestone = data[5];
            String deliverName = data[6];

            if (CommonUtil.isEmpty(number)) {
                POIExcelUtil.addErrorMsg(
                        rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberRequired")
                                + ";", errorMsgMap);
            }
            else {
                Pattern pattern = Pattern.compile("[0-9]*");
                if (pattern.matcher(number).matches()) {
                    if (numList.contains(number)) {
                        POIExcelUtil.addErrorMsg(
                                rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberTwo")
                                        + ";", errorMsgMap);
                    }
                    else {
                        numList.add(number);
                    }

                }
                else {
                    POIExcelUtil.addErrorMsg(
                            rowNum,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberOne")
                                    + ";", errorMsgMap);
                }
            }

            if(CommonUtil.isEmpty(taskNameType)){
                POIExcelUtil.addErrorMsg(rowNum,
                        "计划类型不能为空;", errorMsgMap);
            }

            if (!CommonUtil.isEmpty(level) && CommonUtil.isEmpty(planLevelMap.get(level))) {
                POIExcelUtil.addErrorMsg(rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanLevel")
                                + ";", errorMsgMap);
            }
            if (!CommonUtil.isEmpty(name)) {
                if (name.length() > 30) {
                    POIExcelUtil.addErrorMsg(
                            rowNum,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanNameLength")
                                    + ";", errorMsgMap);
                }
                else {
                    if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)) {
                        if (CommonUtil.isEmpty(standardNameMap.get(name))) {
                            POIExcelUtil.addErrorMsg(
                                    rowNum,
                                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanName")
                                            + ";", errorMsgMap);
                        }
                    }
                }
            }
            else {
                POIExcelUtil.addErrorMsg(
                        rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanNameRequired")
                                + ";", errorMsgMap);
            }

            if (CommonUtil.isEmpty(milestone)) {
                POIExcelUtil.addErrorMsg(
                        rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkMilestoneRequired")
                                + ";", errorMsgMap);
            }

            if (!CommonUtil.isEmpty(worktime)) {
                if ("是".equals(milestone)) {
                    if (!"0".equals(worktime)) {
                        Pattern pattern = Pattern.compile("^[1-9][0-9]{0,3}$");
                        if (!pattern.matcher(worktime).matches()) {
                            POIExcelUtil.addErrorMsg(
                                    rowNum,
                                    I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeTwo")
                                            + ";", errorMsgMap);
                        }
                    }
                }
                else {
                    Pattern pattern = Pattern.compile("^[1-9][0-9]{0,3}$");
                    if (!pattern.matcher(worktime).matches()) {
                        POIExcelUtil.addErrorMsg(
                                rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeTwo")
                                        + ";", errorMsgMap);
                    }
                }

            }
            else {
                POIExcelUtil.addErrorMsg(
                        rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeOne")
                                + ";", errorMsgMap);
            }
            if (!CommonUtil.isEmpty(deliverName)) {
                if (NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                    if (deliverName.length() > 30) {
                        POIExcelUtil.addErrorMsg(
                                rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkDeliverTwo")
                                        + ";", errorMsgMap);
                    }
                }
                else {
                    if (!CommonUtil.isEmpty(switchStr)) {
                        String[] deliverNames = deliverName.split(",");
                        for (String dn : deliverNames) {
                            if (CommonUtil.isEmpty(deliveryNameMap.get(dn))) {
                                POIExcelUtil.addErrorMsg(
                                        rowNum,
                                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkDeliver")
                                                + ";", errorMsgMap);
                            }
                        }
                    }
                }
            }

        }
        return errorMsgMap;
    }

    @Override
    public void checkData(int rowNum, String strForBc, String switchStr, Map<String, String> standardNameMap, Map<String, String> errorMsgMap, Map<String, String> deliveryNameMap, List<String> numList, Map<String, String> planLevelMap) {
        String[] data = strForBc.split(";");
        if (!CommonUtil.isEmpty(data)) {
            String number = data[0];
            String name = data[1];
            String taskNameType = data[2];
            String level = data[3];
            String worktime = data[4];
            String milestone = data[5];
            String deliverName = data[6];

            if (CommonUtil.isEmpty(number)) {
                POIExcelUtil.addErrorMsg(
                        rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberRequired")
                                + ";", errorMsgMap);
            }
            else {
                Pattern pattern = Pattern.compile("[0-9]*");
                if (pattern.matcher(number).matches()) {
                    if (numList.contains(number)) {
                        POIExcelUtil.addErrorMsg(
                                rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberTwo")
                                        + ";", errorMsgMap);
                    }
                    else {
                        numList.add(number);
                    }

                }
                else {
                    POIExcelUtil.addErrorMsg(
                            rowNum,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberOne")
                                    + ";", errorMsgMap);
                }
            }

            if(CommonUtil.isEmpty(taskNameType)){
                POIExcelUtil.addErrorMsg(rowNum,
                        "计划类型不能为空;", errorMsgMap);
            }

            if (!CommonUtil.isEmpty(level) && CommonUtil.isEmpty(planLevelMap.get(level))) {
                POIExcelUtil.addErrorMsg(rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanLevel")
                                + ";", errorMsgMap);
            }
            if (!CommonUtil.isEmpty(name)) {
                if (name.length() > 30) {
                    POIExcelUtil.addErrorMsg(
                            rowNum,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanNameLength")
                                    + ";", errorMsgMap);
                }
                else {
                    if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)) {
                        if (CommonUtil.isEmpty(standardNameMap.get(name))) {
                            POIExcelUtil.addErrorMsg(
                                    rowNum,
                                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanName")
                                            + ";", errorMsgMap);
                        }
                    }
                }
            }
            else {
                POIExcelUtil.addErrorMsg(
                        rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanNameRequired")
                                + ";", errorMsgMap);
            }

            if (CommonUtil.isEmpty(milestone)) {
                POIExcelUtil.addErrorMsg(
                        rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkMilestoneRequired")
                                + ";", errorMsgMap);
            }

            if (!CommonUtil.isEmpty(worktime)) {
                if ("是".equals(milestone)) {
                    if (!"0".equals(worktime)) {
                        Pattern pattern = Pattern.compile("^[1-9][0-9]{0,3}$");
                        if (!pattern.matcher(worktime).matches()) {
                            POIExcelUtil.addErrorMsg(
                                    rowNum,
                                    I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeTwo")
                                            + ";", errorMsgMap);
                        }
                    }
                }
                else {
                    Pattern pattern = Pattern.compile("^[1-9][0-9]{0,3}$");
                    if (!pattern.matcher(worktime).matches()) {
                        POIExcelUtil.addErrorMsg(
                                rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeTwo")
                                        + ";", errorMsgMap);
                    }
                }

            }
            else {
                POIExcelUtil.addErrorMsg(
                        rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeOne")
                                + ";", errorMsgMap);
            }
            if (!CommonUtil.isEmpty(deliverName)) {
                if (NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                    if (deliverName.length() > 30) {
                        POIExcelUtil.addErrorMsg(
                                rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkDeliverTwo")
                                        + ";", errorMsgMap);
                    }
                }
                else {
                    if (!CommonUtil.isEmpty(switchStr)) {
                        String[] deliverNames = deliverName.split(",");
                        for (String dn : deliverNames) {
                            if (CommonUtil.isEmpty(deliveryNameMap.get(dn))) {
                                POIExcelUtil.addErrorMsg(
                                        rowNum,
                                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkDeliver")
                                                + ";", errorMsgMap);
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void checkData2(Map<String, PlanTemplateExcelVo> excelMap, Map<String, String> errorMsgMap, List<String> numList) {
        // 互为前置
        List<String> preposeList = new ArrayList<String>();
        // 互为父子
        List<String> parentChildList = new ArrayList<String>();
        for (String number : excelMap.keySet()) {
            String parentNo = excelMap.get(number).getParentNo();
            String preposeNos = excelMap.get(number).getPreposeNos();
            String worktime = "";
            if (!CommonUtil.isEmpty(excelMap.get(number).getWorktime())) {
                worktime = excelMap.get(number).getWorktime();
            }
            // 校验依赖关系
            if (!CommonUtil.isEmpty(parentNo)
                    && (number.equals(parentNo) || !numList.contains(parentNo))) {
                POIExcelUtil.addErrorMsg(
                        excelMap.get(number).getRowNum(),
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkParentPlanNumber")
                                + ";", errorMsgMap);
            }
            else {
                if (!CommonUtil.isEmpty(number) && !CommonUtil.isEmpty(parentNo)) {
                    if (!parentChildList.contains(number)
                            && !CommonUtil.isEmpty(excelMap.get(parentNo))) {
                        if (number.equals(excelMap.get(parentNo).getParentNo())) {
                            parentChildList.add(parentNo);
                            parentChildList.add(number);
                            POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                                    "不能与序号为" + parentNo + "的计划互为父子计划;", errorMsgMap);
                        }
                    }
                }
            }

            // 前置计划
            if (!preposeList.contains(number) && !CommonUtil.isEmpty(preposeNos)) {
                String[] preposeNoArr = preposeNos.split(",");
                for (String preposeNo : preposeNoArr) {
                    if (!CommonUtil.isEmpty(preposeNo)
                            && (number.equals(preposeNo) || !numList.contains(preposeNo))) {
                        POIExcelUtil.addErrorMsg(
                                excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPreposePlanNumber")
                                        + ";", errorMsgMap);
                    }
                    else {
                        if (!CommonUtil.isEmpty(excelMap.get(preposeNo).getPreposeNos())
                                && excelMap.get(preposeNo).getPreposeNos().indexOf(number) != -1) {
                            preposeList.add(preposeNo);
                            preposeList.add(number);
                            POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                                    "不能与序号为" + preposeNo + "的计划互为前后置;", errorMsgMap);
                        }
                    }

                  /*  String workTimeAdd = "";
                    workTimeAdd = getWorkTimeAddWithPreposePlan(number,preposeNo,excelMap);*/

                }
            }

            if (!CommonUtil.isEmpty(parentNo) && !CommonUtil.isEmpty(excelMap.get(parentNo))) {
                String rowErrorInfo = "";
                String parentRowErrorInfo = "";
                if (!CommonUtil.isEmpty(errorMsgMap.get(String.valueOf(excelMap.get(number).getRowNum())))) {
                    rowErrorInfo = errorMsgMap.get(String.valueOf(excelMap.get(number).getRowNum()));
                }
                if (!CommonUtil.isEmpty(errorMsgMap.get(String.valueOf(excelMap.get(parentNo).getRowNum())))) {
                    parentRowErrorInfo = errorMsgMap.get(String.valueOf(excelMap.get(parentNo).getRowNum()));
                }
                if (!CommonUtil.isEmpty(worktime)
                        && !CommonUtil.isEmpty(excelMap.get(parentNo).getWorktime())
                        && rowErrorInfo.indexOf(I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeTwo")) == -1
                        && parentRowErrorInfo.indexOf(I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeTwo")) == -1) {
                    if (Integer.valueOf(worktime) > Integer.valueOf(excelMap.get(parentNo).getWorktime())) {
                        POIExcelUtil.addErrorMsg(
                                excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.import.checkWorktimeThree")
                                        + ";", errorMsgMap);
                    }
                }
            }
        }
    }

    @Override
    public String savePlanTemplateDetailByExcel(PlanTemplate planTemplate, List<PlanTemplateExcelVo> dataList, Map<String, PlanTemplateExcelVo> excelMap,
                                                Map<String, String> planLevelMap, String switchStr,TSUserDto userDto,String orgId) {
        List<Plan> templateDetailList = new ArrayList<Plan>();
        // 存放计划模板中 批量插入的计划信息
        List<Plan> detailInsert = new ArrayList<Plan>();
        // 存放计划模板中 批量插入交付项的信息
        // List<DeliverablesInfo> deliverablesInsert=new ArrayList<DeliverablesInfo>();
        // 存放计划模板中 批量插入输入的信息
        // List<Inputs> inputsInsert=new ArrayList<Inputs>();
        // 存放计划模板中 前置计划的相关信息
        // List<PreposePlanTemplate> preposePlanInsert=new ArrayList<PreposePlanTemplate>();
        PlanTemplateDetail d = new PlanTemplateDetail();
        CommonInitUtil.initGLObjectForCreate(d,userDto,orgId);
        planTemplateDetailService.initBusinessObject(d);
        String bizCurrent = d.getBizCurrent();
        String bizVersion = d.getBizVersion();
        LifeCyclePolicy policy = d.getPolicy();

        DeliverablesInfo dbi = new DeliverablesInfo();
        CommonInitUtil.initGLObjectForCreate(dbi,userDto,orgId);
        deliverablesInfoService.initBusinessObject(dbi);
        String bizCurrentdbi = dbi.getBizCurrent();
        String bizVersiondbi = dbi.getBizVersion();
        LifeCyclePolicy policydbi = dbi.getPolicy();
        // 记录 计划id+交付项名称:输出ID
        Map<String, String> deliverMap = new HashMap<String, String>();
        Map<String, String> nameDeliverablesMap = nameStandardService.getNameDeliverysMap();
        Map<String, String> preposeInput = new HashMap<String, String>();

        // Integer storeyNo = 0;
        int maxPlanNumber = 0;
        int maxStoreyNo = 0;
        /*
         * if (!needDelete) {
         * maxPlanNumber = getMaxPlanNumber(plan);
         * maxStoreyNo = getMaxStoreyNo(plan);
         * }
         */
        Map<String,ActivityTypeManage> activityTypeNameMap = activityTypeManageEntityService.getActivityTypeAndNameMap();
        Map<String,String> activityIdAndTabCbTempMap = tabCbTemplateEntityService.getTabCbTempIdAndActivityIdMap();
        for (PlanTemplateExcelVo vo : dataList) {
            Plan detail = new Plan();
            CommonUtil.glObjectSet(detail);
            detail.setId(vo.getId());
            // detail.setPlanNumber(Integer.valueOf(vo.getPlanNumber()));
            detail.setPlanTemplateId(planTemplate.getId());
            detail.setPlanName(vo.getPlanName());
            detail.setWorkTime(vo.getWorktime());
            ActivityTypeManage activityTypeManages = activityTypeNameMap.get(vo.getTaskNameType());
            String taskNameType = "";
            if(!CommonUtil.isEmpty(activityTypeManages)){
                taskNameType = activityTypeManages.getId();
            }
            detail.setTaskNameType(taskNameType);
         //   List<TabCombinationTemplate> tabList = tabCbTemplateEntityService.findTabCbTemplatesByActivityId(taskNameType);
            if(!CommonUtil.isEmpty(activityIdAndTabCbTempMap.get(taskNameType))){
                detail.setTabCbTemplateId(activityIdAndTabCbTempMap.get(taskNameType));
            }
            if (!CommonUtil.isEmpty(vo.getParentNo())) {
                detail.setParentPlanId(excelMap.get(vo.getParentNo()).getId());
            }
            if (PlanConstants.PLAN_MILESTONE_TRUE.equals(vo.getMilestone())) {
                detail.setMilestone("true");
            }
            else {
                detail.setMilestone("false");
            }
            if (!CommonUtil.isEmpty(vo.getIsNecessary())) {
                // 是否必要
                if (PlanConstants.PLAN_MILESTONE_TRUE.equals(vo.getMilestone())) {
                    detail.setMilestone("true");
                }
                else {
                    detail.setMilestone("false");
                }
            }
            if (!CommonUtil.isEmpty(vo.getPlanLevel())) {
                detail.setPlanLevel(planLevelMap.get(vo.getPlanLevel()));
            }
            detail.setPlanNumber(Integer.valueOf(vo.getPlanNumber()));
            detail.setBizCurrent(bizCurrent);
            detail.setBizVersion(bizVersion);
            detail.setPolicy(policy);
            detail.setBizId(UUID.randomUUID().toString());
            detail.setFirstTime(new Date());
            Short securityLevel = 1;
            detail.setSecurityLevel(securityLevel);

            // 如果导入模板时计划节点需要重新排序
            // 每次都是最大的planNumber+1
            detail.setPlanNumber(maxPlanNumber + 1);
            maxPlanNumber++ ;

            detail.setStoreyNo(maxStoreyNo + 1);
            maxStoreyNo++ ;

            detailInsert.add(detail);
            // 输出
            List<DeliverablesInfo> deliverables = deliverablesInfoService.getDeliverablesInfoByPlanTemplateExcel(
                    vo, switchStr, bizCurrentdbi, bizVersiondbi, policydbi, deliverMap,
                    nameDeliverablesMap.get(detail.getPlanName()),userDto,orgId);
            if (!CommonUtil.isEmpty(deliverables)) {
                // deliverablesInsert.addAll(deliverables);
                detail.setDeliInfoList(deliverables);
            }
            // 前置计划
            if (!CommonUtil.isEmpty(vo.getPreposeNos())) {
                List<PreposePlanTemplate> preposePlans = preposePlanTemplateService.getPreposePlanInfoByPlanTemplateExcel(
                        vo, excelMap, preposeInput, detail,userDto,orgId);
                /*
                 * if (!CommonUtil.isEmpty(preposePlans)) {
                 * preposePlanInsert.addAll(preposePlans);
                 * }
                 */
            }
            templateDetailList.add(detail);
        }
        // 输入
        Map<String, List<Inputs>> inputsMap = new HashMap<String, List<Inputs>>();
        inputsMap = inputsService.getInputsInfoByPlanTemplateExcel(preposeInput, excelMap,
                deliverMap,userDto,orgId);
        for (Plan d1 : templateDetailList) {
            if (!CommonUtil.isEmpty(inputsMap.get(d1.getId()))) {
                d1.setInputList(inputsMap.get(d1.getId()));
                String inpStr = JSON.toJSONString(inputsMap.get(d1.getId()));
                redisService.setToRedis("INPUTSLIST", d1.getId(), inpStr);
            }
        }
        // savePlanTemplateAllByList(detailInsert, deliverablesInsert, preposePlanInsert,
        // inputsInsert);
        String detailStr = JSON.toJSONString(templateDetailList);
        redisService.setToRedis("TEMPLATEPLANLIST", planTemplate.getId(), detailStr);
        return planTemplate.getId();
    }

    @Override
    public void copyTemplate(String oldTemplId, String newTemplName, String remark,TSUserDto userDto,String orgId) {
        // 新增模板
        PlanTemplate template = new PlanTemplate();
        initBusinessObject(template);
        CommonInitUtil.initGLObjectForCreate(template,userDto,orgId);
        template.setName(newTemplName);
        template.setRemark(remark);
        sessionFacade.save(template);
        // 日志
        saveTemplateOptLog(template.getBizId(), PlantemplateConstant.PLAN_TEMPLATE_ADD,userDto,orgId);
        //
        Map<String, String> planIdMap = new HashMap<String, String>();
        List<PlanTemplateDetail> detailNewInsert = new ArrayList<PlanTemplateDetail>();
        List<DeliverablesInfo> deliverablesNewInsert = new ArrayList<DeliverablesInfo>();
        List<PreposePlanTemplate> preposePlanNewInsert = new ArrayList<PreposePlanTemplate>();
        List<Inputs> inputsNewInsert = new ArrayList<Inputs>();

        List<PlanTemplateDetail> detailList = planTemplateDetailService.getPlanTemplateDetailList(oldTemplId);

        PlanTemplateDetail pd = new PlanTemplateDetail();
        initBusinessObject(pd);
        CommonInitUtil.initGLObjectForCreate(pd,userDto,orgId);
        String bizCurrent = pd.getBizCurrent();
        String bizVersion = pd.getBizVersion();
        LifeCyclePolicy policy = pd.getPolicy();

        DeliverablesInfo d1 = new DeliverablesInfo();
        deliverablesInfoService.initBusinessObject(d1);
        CommonInitUtil.initGLObjectForCreate(d1,userDto,orgId);
        String dBizCurrent = d1.getBizCurrent();
        String dBizVersion = d1.getBizVersion();
        LifeCyclePolicy dPolicy = d1.getPolicy();
        if (!CommonUtil.isEmpty(detailList)) {
            for (PlanTemplateDetail plan : detailList) {
                String tempId = UUIDGenerator.generate();
                planIdMap.put(plan.getId(), tempId);
            }
            // 计划
            for (PlanTemplateDetail detail : detailList) {
                PlanTemplateDetail detailNew = new PlanTemplateDetail();
                detailNew.setId(planIdMap.get(detail.getId()));
                detailNew.setName(detail.getName());
                detailNew.setIsNecessary(detail.getIsNecessary());
                detailNew.setMilestone(detail.getMilestone());
                detailNew.setPlanNumber(detail.getPlanNumber());
                if (!CommonUtil.isEmpty(detail.getParentPlanId())) {
                    detailNew.setParentPlanId(planIdMap.get(detail.getParentPlanId()));
                }
                detailNew.setPlanLevel(detail.getPlanLevel());
                detailNew.setPlanTemplateId(template.getId());
                detailNew.setRemark(detail.getRemark());
                detailNew.setWorkTime(detail.getWorkTime());
                detailNew.setStoreyNo(detail.getStoreyNo());
                detailNew.setPolicy(policy);
                detailNew.setBizCurrent(bizCurrent);
                detailNew.setBizVersion(bizVersion);
                detailNew.setBizId(UUID.randomUUID().toString());
                CommonInitUtil.initGLObjectForCreate(detailNew,userDto,orgId);
                detailNewInsert.add(detailNew);
            }
            // 输出
            Map<String, String> deliverIdMap = new HashMap<String, String>();
            List<DeliverablesInfo> deliverInfoList = deliverablesInfoService.queryDeliverablesInfoByTemplateId(oldTemplId);
            if (!CommonUtil.isEmpty(deliverInfoList)) {
                for (DeliverablesInfo d : deliverInfoList) {
                    DeliverablesInfo dNew = new DeliverablesInfo();
                    CommonInitUtil.initGLObjectForCreate(dNew,userDto,orgId);
                    String newId = UUIDGenerator.generate();
                    deliverIdMap.put(d.getId(), newId);
                    dNew.setPolicy(dPolicy);
                    dNew.setBizCurrent(dBizCurrent);
                    dNew.setBizVersion(dBizVersion);
                    dNew.setBizId(UUID.randomUUID().toString());
                    dNew.setId(newId);
                    dNew.setUseObjectType(d.getUseObjectType());
                    dNew.setUseObjectId(planIdMap.get(d.getUseObjectId()));
                    dNew.setName(d.getName());
                    deliverablesNewInsert.add(dNew);
                }
            }
            // 输入
            List<Inputs> inputsList = inputsService.getInputsInfoByPlanTemplateId(oldTemplId);
            if (!CommonUtil.isEmpty(inputsList)) {
                for (Inputs in : inputsList) {
                    Inputs input = new Inputs();
                    CommonInitUtil.initGLObjectForCreate(input,userDto,orgId);
                    input.setId(UUIDGenerator.generate().toString());
                    input.setUseObjectId(planIdMap.get(in.getUseObjectId()));
                    input.setUseObjectType(in.getUseObjectType());
                    input.setName(in.getName());
                    if (!CommonUtil.isEmpty(in.getOriginObjectId())) {
                        input.setOriginObjectId(planIdMap.get(in.getOriginObjectId()));
                    }
                    if (!CommonUtil.isEmpty(in.getOriginDeliverablesInfoId())) {
                        input.setOriginDeliverablesInfoId(deliverIdMap.get(in.getOriginDeliverablesInfoId()));
                    }
                    input.setOriginType(in.getOriginType());
                    if (PlanConstants.LOCAL_EN.equals(in.getOriginType())) {
                        input.setDocId(in.getDocId());
                        input.setDocName(in.getDocName());
                    }
                    inputsNewInsert.add(input);
                }
            }
            // 前置
            List<PreposePlanTemplate> preposePlanList = preposePlanTemplateService.getPreposePlansByPlanTemplateId(oldTemplId);
            if (!CommonUtil.isEmpty(preposePlanList)) {
                for (PreposePlanTemplate prepose : preposePlanList) {
                    PreposePlanTemplate preposeNew = new PreposePlanTemplate();
                    preposeNew.setId(UUIDGenerator.generate());
                    preposeNew.setPlanId(planIdMap.get(prepose.getPlanId()));
                    preposeNew.setPreposePlanId(planIdMap.get(prepose.getPreposePlanId()));
                    preposePlanNewInsert.add(preposeNew);
                }
            }
        }
        savePlanTemplateAllByList(detailNewInsert, deliverablesNewInsert, preposePlanNewInsert,
                inputsNewInsert);
    }

    @Override
    public void backVesion(PlanTemplate template, String userId, String orgId) {
        List<PlanTemplate> listToDelete = new ArrayList<PlanTemplate>();
        listToDelete.add(template);
        deleteTemplateInfo(listToDelete);
        List<PlanTemplate> templateList = findBusinessObjectHistoryByBizId(PlanTemplate.class,
                template.getBizId());
        TSUserDto userDto = userService.getUserByUserId(userId);
        // 将最新版本设置为启用状态
        if (!CommonUtil.isEmpty(templateList)) {
            PlanTemplate templateLatest = templateList.get(0);
            templateLatest.setAvaliable(String.valueOf(LifeCycleConstant.PolicyAvaliable.USED));
            CommonInitUtil.initGLObjectForUpdate(templateLatest,userDto,orgId);
            sessionFacade.saveOrUpdate(templateLatest);
        }
        // 回退版本的日志
        saveTemplateOptLog(template.getBizId(), PlantemplateConstant.PLAN_TEMPLATE_BACK,userDto,orgId);
    }


    private void deleteTemplateInfo(List<PlanTemplate> listToDelete) {
        if (!CommonUtil.isEmpty(listToDelete)) {
            for (PlanTemplate t : listToDelete) {
                if ("1".equals(t.getAvaliable()) && !CommonUtil.isEmpty(t.getProcessInstanceId())) {
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(t.getId(),
                            t.getProcessInstanceId(), "delete");
                }
                sessionFacade.executeHql(
                        "delete from Inputs where useObjectId in(select id from PlanTemplateDetail where planTemplateId = ?)",
                        t.getId());
                sessionFacade.executeHql(
                        "delete from DeliverablesInfo where useObjectId in(select id from PlanTemplateDetail where planTemplateId = ?)",
                        t.getId());
                sessionFacade.executeHql(
                        "delete from PlanTemplateDetail where planTemplateId = ?", t.getId());
                sessionFacade.executeHql("delete from PlanTemplate where id = ?", t.getId());
            }
        }
    }

    @Override
    public void revokeVesion(PlanTemplate template, String userId, String orgId) {
        String bizId = template.getBizId();
        String bizversion = "";
        TSUserDto userDto = userService.getUserByUserId(userId);
        if (!CommonUtil.isEmpty(template.getBizVersion())) {
            bizversion = template.getBizVersion().split("\\.")[0] + ".";
        }
        List<PlanTemplate> listToDelete = getPlanTemplateByBizIdForBizversion(bizId, bizversion);
        /*
         * for (PlanTemplate p : listToDelete) {
         * if ("1".equals(p.getAvaliable()) && !CommonUtil.isEmpty(p.getProcessInstanceId())) {
         * workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(p.getId(),
         * p.getProcessInstanceId(), "delete");
         * }
         * }
         */
        deleteTemplateInfo(listToDelete);
        List<PlanTemplate> templateList = findBusinessObjectHistoryByBizId(PlanTemplate.class,
                template.getBizId());
        // 将最新版本设置为启用状态
        if (!CommonUtil.isEmpty(templateList)) {
            PlanTemplate templateLatest = templateList.get(0);
            templateLatest.setAvaliable(String.valueOf(LifeCycleConstant.PolicyAvaliable.USED));
            CommonInitUtil.initGLObjectForUpdate(templateLatest,userDto,orgId);
            sessionFacade.saveOrUpdate(templateLatest);
        }
        // 撤销版本的日志
        saveTemplateOptLog(bizId, PlantemplateConstant.PLAN_TEMPLATE_REVOKE,userDto,orgId);
    }


    private List<PlanTemplate> getPlanTemplateByBizIdForBizversion(String bizId, String bizversion) {
        List<PlanTemplate> list = new ArrayList<PlanTemplate>();
        if (!CommonUtil.isEmpty(bizId) && !CommonUtil.isEmpty(bizversion)) {
            String hql = "from PlanTemplate t where t.bizId ='" + bizId
                    + "' and t.bizVersion like '%" + bizversion + "%'";
            list = sessionFacade.findHql(hql);
        }
        return list;
    }

    @Override
    public void startPlanTemplateProcess(PlanTemplate planTemplate, Map<String, Object> variables, TSUserDto userDto, String orgId) {
        String feignContinueUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/planTemplateListenerRestController/notify.do";
        variables.put("feignPlanTemplateContinueListener", feignContinueUrl);
        String feignRefuseUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/planTemplateGoBackListenerRestController/notify.do";
        variables.put("feignPlanTemplateRefuseListener", feignRefuseUrl);
        if (StringUtils.isEmpty(planTemplate.getProcessInstanceId())) {
            // 3.流程启动并创建待办任务
            submitPlanTemplateFlow(planTemplate, variables,
                    BpmnConstants.BPMN_STRAT_PLAN_TEMPLATE,userDto,orgId);

            // saveOrUpdateViolations(qualityViolations, actor, procInstId);
        }
        else {
            submitProjectFlowAgain(planTemplate,userDto,orgId);
        }
    }


    @Override
    public void submitPlanTemplateFlow(PlanTemplate planTemplate, Map<String, Object> variables, String processDefinitionKey ,TSUserDto curUser,String orgId) {
        if(!CommonUtil.isEmpty(planTemplate.getId())){
            planTemplate = (PlanTemplate)sessionFacade.getEntity(PlanTemplate.class,planTemplate.getId());
        }

        String creator = curUser.getUserName();
        if (!CommonUtil.isEmpty(planTemplate.getProcessInstanceId())) {
            List<TaskDto> nextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    planTemplate.getProcessInstanceId(),creator);
            // 判断是否有流程（暂停或关闭流程），如有：结束流程
            if (!CommonUtil.isEmpty(nextTasks)) {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                        planTemplate.getId(), planTemplate.getProcessInstanceId(), "delete");
            }
        }

        FeignJson j = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                processDefinitionKey, planTemplate.getId(), variables);
        String procInstId = j.getObj().toString(); // 流程实例ID
        List<TaskDto> tasks = null;
        try {
            tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    procInstId, creator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String taskId = tasks.get(0).getId();

        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

        FeignJson nextTask = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = nextTask.getObj().toString();

        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
        myStartedTask.setTaskNumber(planTemplate.getId());
        myStartedTask.setType(processDefinitionKey);
        myStartedTask.setCreater(creator);
        myStartedTask.setCreaterFullname(curUser.getRealName());
        myStartedTask.setStartTime(new Date());
        myStartedTask.setProcType(processDefinitionKey);
        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        // 流程已发起列表中：增加对象名称的记录，流程任务名称规范化
        myStartedTask.setTitle(ProcessUtil.getProcessTitle(planTemplate.getName(),
                BpmnConstants.BPMN_PLANTEMPLATE_APPLY_DISPLAYNAME, procInstId));

        myStartedTask.setObjectName(planTemplate.getName());

        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

        planTemplate.setProcessInstanceId(procInstId);
        planTemplate.setBizCurrent("shenhe");
        CommonInitUtil.initGLObjectForUpdate(planTemplate,curUser,orgId);
        /*planTemplate.setIsRefuse(ProjectConstants.NORMAL);*/
        sessionFacade.updateEntitie(planTemplate);
    }


    @Override
    public void submitProjectFlowAgain(PlanTemplate planTemplate,TSUserDto userDto,String orgId) {
        planTemplate = (PlanTemplate)sessionFacade.getEntity(PlanTemplate.class,planTemplate.getId());
        String creator = userDto.getUserName();
        FeignJson fj = workFlowFacade.getWorkFlowTodoTaskService() .getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                planTemplate.getProcessInstanceId(), creator);
        String taskId = String.valueOf(fj.getObj());

        // 判断当前用户是否有流程（对应项目的启动、暂停、恢复、关闭流程），如有：重新提交启动流程
        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);

            Map<String,Object> params = new HashMap<>();
            params.put("variables",variables);
            params.put("userId",userDto.getId());

            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, params);

            FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    planTemplate.getProcessInstanceId());
            String status = newNextTasks.getObj().toString();
            // 更新我的已发起流程状态
            updateMyStartedTaskStatus(status, planTemplate);

            /*// 更新项目状态
            project.setStatus(ProjectConstants.APPROVING);
            project.setIsRefuse(ProjectConstants.NORMAL);
            sessionFacade.updateEntitie(project);*/

        }

    }

    /**
     * 更新我的已发起流程状态
     *
     * @param status
     *            状态
     * @param planTemplate
     *            项目
     * @return
     * @see
     */
    private void updateMyStartedTaskStatus(String status, PlanTemplate planTemplate) {
        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                planTemplate.getId(), planTemplate.getProcessInstanceId());
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
    }

    @Override
    public void completePlanTemplateProcess(String taskId, PlanTemplate planTemplate, String userId, String orgId) {
/*        if (!CommonUtil.isEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);
            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, variables);
            FeignJson fJson= workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    planTemplate.getProcessInstanceId());
            List<Task> nextTasks =(List<Task>)fJson.getObj();
            String status = ProcessUtil.getProcessStatus(nextTasks);
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                    planTemplate.getId(), planTemplate.getProcessInstanceId());
            myStartedTask.setStatus(status);
            myStartedTask.setEndTime(new Date());
            workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
        }*/

        planTemplate = (PlanTemplate)sessionFacade.getEntity(PlanTemplate.class,planTemplate.getId());
        String creator = userService.getUserByUserId(userId).getUserName();
        FeignJson fj = workFlowFacade.getWorkFlowTodoTaskService() .getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                planTemplate.getProcessInstanceId(), creator);
        taskId = String.valueOf(fj.getObj());
        // 判断当前用户是否有流程（对应项目的启动、暂停、恢复、关闭流程），如有：重新提交启动流程
        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);

            Map<String,Object> params = new HashMap<>();
            params.put("variables",variables);
            params.put("userId",userId);

            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    planTemplate.getProcessInstanceId());
            String status = newNextTasks.getObj().toString();
            // 更新我的已发起流程状态
            updateMyStartedTaskStatus(status, planTemplate);

        }

    }

    @Override
    public void updateBizCurrent(String id) {
        PlanTemplate planTemplate = getPlanTemplateEntity(id);
        if(PlanConstants.PLANTEMPLATE_SHENHE.equals(planTemplate.getBizCurrent())){
            planTemplate.setBizCurrent("qiyong");
        }
        else if(PlantemplateConstant.STATUS_XIUDING.equals(planTemplate.getBizCurrent()) ||
                PlanConstants.PLANTEMPLATE_NIZHL.equals(planTemplate.getBizCurrent())){
            planTemplate.setBizCurrent("shenhe");
        }
        sessionFacade.saveOrUpdate(planTemplate);
    }

    @Override
    public void updateBizCurrentWhenProcessRefuse(String status, String id) {
        PlanTemplate planTemplate = getPlanTemplateEntity(id);
        if(PlanConstants.PLANTEMPLATE_NIZHL.equals(status)) {
            planTemplate.setBizCurrent(PlanConstants.PLANTEMPLATE_NIZHL);
        } else if(PlantemplateConstant.STATUS_XIUDING.equals(status)) {
            planTemplate.setBizCurrent(PlantemplateConstant.STATUS_XIUDING);
        }
        sessionFacade.saveOrUpdate(planTemplate);
    }

    @Override
    public <T> List<T> getVersionHistory(String bizId, Integer pageSize, Integer pageNum) {
        List<T> list = new ArrayList<T>();
        StringBuilder hql = new StringBuilder("");
        hql.append(" from PlanTemplate t where t.bizId=? order by t.createTime desc");
        String[] params = {bizId};

        if (pageSize != null) {
            list = sessionFacade.pageList(hql.toString(), params, (pageNum - 1) * pageSize, pageSize);
        }
        else {
            list = sessionFacade.executeQuery(hql.toString(), params);
        }
        return list;
    }

    @Override
    public long getVersionCount(String bizId) {
        StringBuilder hql = new StringBuilder("");
        hql.append(" select count(*) from PlanTemplate t  where t.bizId=?");
        String[] params = {bizId};
        return sessionFacade.getCount(hql.toString(), params);
    }

    @Override
    public List<PlanTempOptLog> findPlanTempOptLogById(String bizId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from PlanTempOptLog t where t.tmplId=? order by t.createTime desc");
        String[] params = {bizId};
        List<PlanTempOptLog> list = new ArrayList<PlanTempOptLog>();

        list = sessionFacade.executeQuery(hql.toString(), params);

        return list;
    }

    @Override
    public void savePlanTemplateByPlanject(String projectId, String planId, PlanTemplate planTemplate,String userId,String orgId) throws GWException {
        TSUserDto userDto = userService.getUserByUserId(userId);
        CommonInitUtil.initGLObjectForCreate(planTemplate,userDto,orgId);
        // 保存计划模板
        initBusinessObject(planTemplate);
        sessionFacade.save(planTemplate);

        // 如果配置为不需要审批流，则直接生效
        /*
         * if(true){
         * moveBusinessObjectByStep(planTemplate,
         * planTemplate.getPolicy().getLifeCycleStatusList().size()-1);
         * }
         */

        planTemplateDetailService.savePlanTemplateDetailByPlan(projectId, planId, planTemplate,userDto,orgId);
    }

    @Override
    public void doStatusChange(PlanTemplate planTemplate, String status, String userId, String orgId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        planTemplate.setBizCurrent(status);
        CommonInitUtil.initGLObjectForUpdate(planTemplate,userDto,orgId);
        sessionFacade.saveOrUpdate(planTemplate);
    }

    @Override
    public void importPlanTemplateMpp(PlanTemplate planTemplate, Set<String> mapKeys, TSUserDto userDto, String orgId,List<List<Map<String, Object>>> taskMapList,List<List<String>> preposePlanIdList) throws IOException {
        // 遍历开始
        List<net.sf.mpxj.Task> taskList = new ArrayList<net.sf.mpxj.Task>();
        List<Map<String, Object>> mapList = new ArrayList<>();

        int count = 0;
        for (String mapKey : mapKeys) {
            if (!CollectionUtils.isEmpty(taskMapList.get(count))){
                mapList = taskMapList.get(count);
            }

            List<String> idList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(preposePlanIdList.get(count))){
                idList = preposePlanIdList.get(count);
            }


          /*  for (net.sf.mpxj.Task task : taskListTemp) {
                if (task.getID() != 0 && StringUtils.isNotEmpty(task.getName())) {
                    taskList.add(task);
                }
            }*/
        }

        // 若模板中的业务数据为空，则提示且不进行导入操作
        if (CommonUtil.isEmpty(mapList)) {
            throw new GWException(I18nUtil.getValue("com.glaway.ids.common.importDataIsNull"));
        }

        // 性能优化计划模板插入数据多的情况下 减少与数据库的交互缩短时间
        // 存放批量插入的计划模板中计划信息
        List<PlanTemplateDetail> detailInsert = new ArrayList<PlanTemplateDetail>();
        // 存放计划模板中 批量插入交付项的信息
        List<DeliverablesInfo> deliverablesInsertOld = new ArrayList<DeliverablesInfo>();

        List<DeliverablesInfo> deliverablesInsertNew = new ArrayList<DeliverablesInfo>();
        // 存放计划模板中前后置计划的相关信息
        List<PreposePlanTemplate> preposePlanInsert = new ArrayList<PreposePlanTemplate>();


        Map<String, Object> paraMap = new HashMap<String, Object>();

        // 存放计划交付项的map key计划详情的ID value详情绑定的交付项目实例
        Map<String, Object> paraMapDeliverablesInfo = new HashMap<String, Object>();

        PlanTemplateDetail d = new PlanTemplateDetail();
        planTemplateDetailService.initBusinessObject(d);
        CommonInitUtil.initGLObjectForCreate(d,userDto,orgId);
        String bizCurrent = d.getBizCurrent();
        String bizVersion = d.getBizVersion();
        LifeCyclePolicy policy = d.getPolicy();

        DeliverablesInfo dbi = new DeliverablesInfo();
        CommonInitUtil.initGLObjectForCreate(dbi,userDto,orgId);
        deliverablesInfoService.initBusinessObject(dbi);
        String bizCurrentdbi = dbi.getBizCurrent();
        String bizVersiondbi = dbi.getBizVersion();
        LifeCyclePolicy policydbi = d.getPolicy();
        // 修改性能BUG计划等级查询问题
        BusinessConfig bcQuery = new BusinessConfig();
        bcQuery.setConfigType(ConfigTypeConstants.PLANLEVEL);

        List<BusinessConfig> businessConfigList = businessConfigService.searchUseableBusinessConfigs(bcQuery);
        int maxPlanNumber = 0;
        int maxStoreyNo = 0;
       for (Map<String,Object> map : mapList) {
            if (Integer.valueOf(String.valueOf(map.get("id"))) == 0 || StringUtils.isEmpty(String.valueOf(map.get("name")))) {
                continue;
            }

            PlanTemplateDetail detail = new PlanTemplateDetail();
            CommonInitUtil.initGLObjectForCreate(detail,userDto,orgId);
            detail.setPlanNumber(Integer.valueOf(String.valueOf(map.get("id"))));
            detail.setPlanTemplateId(planTemplate.getId());

            // 如果里程碑为否,则工期不能为零,提示"修改后重新上传"
            if (Boolean.valueOf(String.valueOf(map.get("milestone"))) == false && String.valueOf(map.get("duration")) == null) {
                throw new GWException(
                        I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.savePlanTemplateDetailOne"));
            }
            if(!CommonUtil.isEmpty(String.valueOf(map.get("duration"))) && String.valueOf(map.get("duration")) != null && String.valueOf(map.get("duration")) != "null"){
                if (Boolean.valueOf(String.valueOf(map.get("milestone")))== false && Integer.valueOf(String.valueOf(map.get("duration"))) == 0) {
                    throw new GWException(
                            I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.savePlanTemplateDetailTwo"));
                }
            }


            if (StringUtils.isNotEmpty(String.valueOf(map.get("name")))) {
                if (String.valueOf(map.get("name")).trim().length() > 30) {
                    Object[] arguments = new String[] {String.valueOf(map.get("id")), String.valueOf(map.get("name"))};
                    throw new GWException(
                            I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plantemplate.plantemplate.savePlanTemplateDetailThree",
                                    arguments));
                }
            }
            detail.setName(String.valueOf(map.get("name")).trim());

            if (!CommonUtil.mapIsEmpty(MppParseUtil.columnIndexMap, ConfigTypeConstants.PLANLEVEL)) {


                String planLevel = null;
                // 重新判断计划等级是否存在 润在保存级别的编号
                if (!CommonUtil.isEmpty(String.valueOf(map.get("planLevelpd")))
                        && !CommonUtil.isEmpty(businessConfigList)) {
                    planLevel = String.valueOf(map.get("planlevelpd"));
                    for (BusinessConfig businessConfig : businessConfigList) {
                        if (businessConfig.getName().equals(planLevel)) {
                            detail.setPlanLevel(businessConfig.getId());
                            break;
                        }
                    }

                }

            }
            detail.setMilestone(String.valueOf(map.get("milestone")));

            if (String.valueOf(map.get("duration")) != null) {
                String worktime = String.valueOf(String.valueOf(map.get("duration"))).trim();
                if (!CommonUtil.isEmpty(worktime)) {
                    int l = worktime.lastIndexOf(".");
                    if (l != -1) {
                        worktime = worktime.substring(0, l);
                    }
                }
                detail.setWorkTime(worktime);
            }
            else {
                String worktime = String.valueOf(String.valueOf(map.get("duration"))).trim();
                if (!CommonUtil.isEmpty(worktime)) {
                    int l = worktime.lastIndexOf(".");
                    if (l != -1) {
                        worktime = worktime.substring(0, l);
                    }
                }
                detail.setWorkTime(worktime);
            }
            //TODO
           /* if (!CommonUtil.isEmpty(map.get("parentId"))) {
                // 父计划工期
                String parentWorkTime = "";
                if (task.getParentTask().getManualDuration() != null) {
                    parentWorkTime = String.valueOf(
                            task.getParentTask().getManualDuration().getDuration()).trim();
                    if (!CommonUtil.isEmpty(parentWorkTime)) {
                        int l = parentWorkTime.lastIndexOf(".");
                        if (l != -1) {
                            parentWorkTime = parentWorkTime.substring(0, l);
                        }
                        if (Integer.valueOf(detail.getWorkTime()) > Integer.valueOf(parentWorkTime)) {
                            Object[] errrrArguments = new String[] {task.getID().toString(),
                                    task.getName()};
                            throw new GWException(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plantemplate.import.checkWorktimeFour",
                                    errrrArguments));
                        }
                    }
                }
                else {
                    parentWorkTime = String.valueOf(
                            task.getParentTask().getDuration().getDuration()).trim();
                    if (!CommonUtil.isEmpty(parentWorkTime)) {
                        int l = parentWorkTime.lastIndexOf(".");
                        if (l != -1) {
                            parentWorkTime = parentWorkTime.substring(0, l);
                        }
                        if (Integer.valueOf(detail.getWorkTime()) > Integer.valueOf(parentWorkTime)) {
                            Object[] errrrArguments = new String[] {task.getID().toString(),
                                    task.getName()};
                            throw new GWException(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plantemplate.import.checkWorktimeFour",
                                    errrrArguments));
                        }
                    }
                }
            }
            // 如果有上级的ID，需要获得上级对应的记录的ID
            if (task.getParentTask() != null
                    && !CommonUtil.mapIsEmpty(paraMap, task.getParentTask().getID().toString())) {
                String parentId = (String)paraMap.get(task.getParentTask().getID().toString());
                detail.setParentPlanId(parentId.trim());
            }*/
            detail.setNum(Integer.valueOf(String.valueOf(map.get("id"))));

            // 保存计划模板详细
            // planTemplateDetailService.initBusinessObject(detail);
            detail.setCreateBy(planTemplate.getCreateBy());
            detail.setCreateTime(new Date());

            // 手动赋值
            detail.setId(UUIDGenerator.generate().toString());
            // planTemplateDetailService.save(detail);
            detail.setUpdateTime(new Date());
            detail.setUpdateBy(planTemplate.getCreateBy());
            detail.setAvaliable("1");
            // 如果导入模板时计划节点需要重新排序
            // 每次都是最大的planNumber+1
            detail.setPlanNumber(maxPlanNumber + 1);
            maxPlanNumber++ ;

            detail.setStoreyNo(maxStoreyNo + 1);
            maxStoreyNo++ ;

            detail.setBizCurrent(bizCurrent);
            detail.setBizVersion(bizVersion);
            detail.setPolicy(policy);
            detail.setBizId(UUID.randomUUID().toString());
            detail.setFirstTime(new Date());
            Short securityLevel = 1;
            detail.setSecurityLevel(securityLevel);

            detailInsert.add(detail);

            // 计划上级的ID保存计划模板详细的ID
            paraMap.put(String.valueOf(map.get("id")), detail.getId());

            // 判断交付项名称是否存在,如果存在保存交付项名称编号 获取交付项的名称信息
            String documents = String.valueOf(map.get("documents"));
            detail.setDocuments(documents);
            // 先获取所有的交付项然后做处理
            if (StringUtils.isNotEmpty(documents)) {
                List<DeliverablesInfo> deliverables = new ArrayList<DeliverablesInfo>();
                for (String document : documents.split(",")) {
                    DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
                    CommonInitUtil.initGLObjectForCreate(deliverablesInfo,userDto,orgId);
                    deliverablesInfo.setName(document);
                    deliverablesInfo.setUseObjectId(detail.getId());
                    deliverablesInfo.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                    deliverablesInfo.setId(UUIDGenerator.generate().toString());
                    deliverablesInfo.setUpdateTime(new Date());
                    deliverablesInfo.setUpdateBy(planTemplate.getCreateBy());
                    deliverablesInfo.setAvaliable("1");

                    deliverablesInfo.setCreateBy(planTemplate.getCreateBy());
                    deliverablesInfo.setCreateTime(new Date());
                    deliverablesInfo.setBizCurrent(bizCurrentdbi);
                    deliverablesInfo.setBizVersion(bizVersiondbi);
                    deliverablesInfo.setPolicy(policydbi);
                    deliverablesInfo.setBizId(UUID.randomUUID().toString());
                    deliverablesInfo.setFirstTime(new Date());
                    Short securityLeveldbi = 1;
                    deliverablesInfo.setSecurityLevel(securityLeveldbi);
                    deliverablesInsertOld.add(deliverablesInfo);

                    deliverables.add(deliverablesInfo);

                }
                paraMapDeliverablesInfo.put(detail.getId(), deliverables);
            }


        }
        paraMap.put("list", preposePlanInsert);

        deliverablesInsertNew = getNewDeliverables(deliverablesInsertOld, paraMapDeliverablesInfo,
                detailInsert, bizCurrentdbi, bizVersiondbi, policydbi);
        preposePlanTemplateService.savePreposePlanTemplateByMpp(taskList, paraMap,
                planTemplate.getCreateBy());

        Map<String, String> preposeMap = new HashMap<String, String>();
        Map<String, Integer> planWorktimeMap = new HashMap<String, Integer>();
        Map<String, String> planNameMap = new HashMap<String, String>();
        Map<String, String> planParentMap = new HashMap<String, String>();

        if (!CommonUtil.isEmpty(detailInsert)) {
            for (PlanTemplateDetail detail : detailInsert) {
                planNameMap.put(detail.getId(), detail.getName());
                planWorktimeMap.put(detail.getId(), Integer.valueOf(detail.getWorkTime()));
                planParentMap.put(detail.getId(), detail.getParentPlanId());
            }
        }
        if (!CommonUtil.isEmpty(preposePlanInsert)) {
            for (PreposePlanTemplate pose : preposePlanInsert) {
                preposeMap.put(pose.getPlanId(), pose.getPreposePlanId());
                /*
                 * if(!CommonUtil.isEmpty(path)){
                 * if(!path.contains(pose.getPlanId())){
                 * path = path +","+ pose.getPlanId();
                 * }
                 * if(!path.contains(pose.getPreposePlanId())){
                 * path = path +","+ pose.getPreposePlanId();
                 * }
                 * }else{
                 * path = pose.getPlanId();
                 * path = path +","+ pose.getPreposePlanId();
                 * }
                 */
            }
            for (PreposePlanTemplate pose : preposePlanInsert) {
                List<String> repeatPathList = new ArrayList<String>();
                List<String> pathList = new ArrayList<String>();
                getLinkPath(pose.getPlanId(), pose.getPlanId(), preposeMap, pathList,
                        repeatPathList);
                checkPreposePostTime(new ArrayList<String>(), pathList, planNameMap,
                        planParentMap, planWorktimeMap);
            }
        }

//        savePlanTemplateAllByList(detailInsert, deliverablesInsertNew, preposePlanInsert, null);
        savePlanTemplateByMpp(detailInsert, deliverablesInsertNew, preposePlanInsert,paraMapDeliverablesInfo,userDto,orgId);
    }


    private void savePlanTemplateByMpp(List<PlanTemplateDetail> planTemplateList,List<DeliverablesInfo> deliverablesList,
                                       List<PreposePlanTemplate> preposePlanList,Map<String, Object> paraMapDeliverablesInfo,TSUserDto userDto,String orgId){

        List<Plan> detailList = new ArrayList<Plan>();

        if(!CommonUtil.isEmpty(planTemplateList)){
            for(PlanTemplateDetail del : planTemplateList){

                Plan plan = new Plan();
                CommonInitUtil.initGLObjectForCreate(plan,userDto,orgId);
                plan.setId(del.getId());
                plan.setPlanName(del.getName());
                plan.setMilestone(del.getMilestone());
                plan.setPlanOrder(String.valueOf(del.getNum()));
                plan.setParentPlanId(del.getParentPlanId());
                plan.setPlanLevel(del.getPlanLevel());
                plan.setPlanNumber(del.getPlanNumber());
                plan.setStoreyNo(del.getStoreyNo());
                plan.setPlanTemplateId(del.getPlanTemplateId());
                plan.setWorkTime(del.getWorkTime());
                plan.setRemark(del.getRemark());
                plan.setDeliInfoList((List<DeliverablesInfo>)paraMapDeliverablesInfo.get(del.getId()));
                String preposeIds = "";
                if(!CommonUtil.isEmpty(preposePlanList)){
                    for(PreposePlanTemplate pre : preposePlanList){
                        if(pre.getPlanId().equals(del.getId())){
                            if(CommonUtil.isEmpty(preposeIds)){
                                preposeIds = pre.getPreposePlanId();
                            }else{
                                preposeIds = preposeIds+","+pre.getPreposePlanId();
                            }
                        }
                    }
                }
                plan.setPreposeIds(preposeIds);
                detailList.add(plan);
            }

            String detailStr = JSON.toJSONString(detailList);
            redisService.setToRedis("TEMPLATEPLANLIST", planTemplateList.get(0).getPlanTemplateId(), "");
            redisService.setToRedis("TEMPLATEPLANLIST", planTemplateList.get(0).getPlanTemplateId(), detailStr);

        }

    }

    @Override
    public void doDeletePlanTemplate(String ids, String planTemplateId, String projTemplateId) {
        List<Plan> detailList = new ArrayList<Plan>();
        if (!CommonUtil.isEmpty(planTemplateId)) {
            String delStr = (String)redisService.getFromRedis("TEMPLATEPLANLIST", planTemplateId);
            if(!CommonUtil.isEmpty(delStr)){
                detailList = JSON.parseArray(delStr,Plan.class);
            }
        }
        else if (!CommonUtil.isEmpty(projTemplateId)) {
            String delStr = (String)redisService.getFromRedis("PROJTMPPLANLIST", projTemplateId);
            if(!CommonUtil.isEmpty(delStr)){
                detailList = JSON.parseArray(delStr,Plan.class);
            }
        }
        List<String> childList = new ArrayList<String>();
        planTemplateDetailService.getPlanAllChildren(childList, ids, detailList);
        if (!CommonUtil.isEmpty(ids) && !CommonUtil.isEmpty(detailList)) {
            int size = detailList.size() - 1;
            for (int i = size; i >= 0; i-- ) {
                if (!CommonUtil.isEmpty(childList)
                        && childList.contains(detailList.get(i).getId())) {
                    detailList.remove(i);
                }
            }
        }
        String detailStr = JSON.toJSONString(detailList);
        if (!CommonUtil.isEmpty(planTemplateId)) {
            redisService.setToRedis("TEMPLATEPLANLIST", planTemplateId, detailStr);
        }
        else if (!CommonUtil.isEmpty(projTemplateId)) {
            redisService.setToRedis("PROJTMPPLANLIST", projTemplateId, detailStr);
        }
    }

    @Override
    public Map<String, Object> doImportPlanTemplateExcel(List<Map<String, String>> map, String userId, String planTemplateId,String orgId) {
        Map<String,Object> returnMap = new HashMap<>();
        Map<String, String> errorMsgMap = new HashMap<>();
        List<PlanTemplateExcelVo> dataTempList = new ArrayList<>();
        List<PlanExcelSaveVo> errorDataTempList = new ArrayList<>();
        String res = "";
        TSUserDto userDto = userService.getUserByUserId(userId);

        // 获取当前系统是否启用标准名称库
        String switchStr = paramSwitchService
                .getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
        boolean isStandard = false;
        if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                || NameStandardSwitchConstants.FORCEUSENAMESTANDARD
                .equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE
                .equals(switchStr)) {
            isStandard = true;
        }
        Map<String, String> standardNameMap = new HashMap<String, String>();
        Map<String, String> deliveryNameMap = new HashMap<String, String>();
        if (isStandard) {
            NameStandardDto ns = new NameStandardDto();
            ns.setStopFlag(ConfigStateConstants.START);
            List<NameStandardDto> list = nameStandardService.searchNameStandardsAccurate(ns);
            for (NameStandardDto n : list) {
                standardNameMap.put(n.getName(), n.getActiveCategory());
            }

            DeliveryStandard ds = new DeliveryStandard();
            ds.setStopFlag(ConfigStateConstants.START);
            List<DeliveryStandard> list2 = deliveryStandardService.searchDeliveryStandardAccurate(ds);
            for (DeliveryStandard d : list2) {
                deliveryNameMap.put(d.getName(), d.getName());
            }
        }
        Map<String, String> planLevelMap = new HashMap<String, String>();
        BusinessConfig businessConfig = new BusinessConfig();
        businessConfig.setConfigType(ConfigTypeConstants.PLANLEVEL);
        businessConfig.setStopFlag(ConfigStateConstants.START);
        businessConfig.setAvaliable("1");
        List<BusinessConfig> planLevelConfigs = businessConfigService.searchUseableBusinessConfigs(businessConfig);
        for (BusinessConfig confog : planLevelConfigs) {
            planLevelMap.put(confog.getName(), confog.getId());
        }
        // 用于校验和预设数据的保存
        Map<String, PlanTemplateExcelVo> excelMap = new HashMap<String, PlanTemplateExcelVo>();
        Map<String,ActivityTypeManage> activityTypeNameMap = activityTypeManageEntityService.getActivityTypeAndNameMap();
        int rowNum = 3;
        // 计划序号集合
        List<String> numList = new ArrayList<String>();
        for (Map<String, String> dataMap : map){
            String number = dataMap.get("number");
            String parentNumber = dataMap.get("parentNumber");
            ActivityTypeManage activityTypeManages = activityTypeNameMap.get(dataMap.get("taskNameType"));
            String taskNameType = "";
            if(!CommonUtil.isEmpty(activityTypeManages)){
                taskNameType = activityTypeManages.getId();
            }
            String name = dataMap.get("name");
            String worktime = dataMap.get("worktime");
            String level = dataMap.get("level");
            String milestone = dataMap.get("milestone");
            String preposeNumbers = dataMap.get("preposeNumbers");
            String deliverNames =dataMap.get("deliverName");

            String isNecessary = dataMap.get("isNecessary");

            String checkStr = number+";" + name + ";" + taskNameType + ";" + level + ";" + worktime + ";" + milestone + ";" +
                    deliverNames + ";" + rowNum;
            /*Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("rowNum",rowNum);
            paramMap.put("checkStr",checkStr);
            paramMap.put("switchStr",switchStr);
            paramMap.put("standardNameMap",standardNameMap);
            paramMap.put("errorMsgMap",errorMsgMap);
            paramMap.put("deliveryNameMap",deliveryNameMap);
            paramMap.put("numList",numList);
            paramMap.put("planLevelMap",planLevelMap);*/
            errorMsgMap = checkDataNew(rowNum,checkStr,switchStr,standardNameMap,errorMsgMap,deliveryNameMap,numList,planLevelMap);
            returnMap.put("errorMsgMap", errorMsgMap);
            PlanTemplateExcelVo vo =  new PlanTemplateExcelVo();
            //计划ID
            vo.setId(UUIDGenerator.generate());
            vo.setPlanNumber(number);
            vo.setParentNo(parentNumber);
            vo.setPlanName(name);
            vo.setTaskNameType(taskNameType);
            vo.setPlanLevel(level);
            vo.setWorktime(worktime);
            vo.setMilestone(milestone);
            vo.setIsNecessary(isNecessary);
            vo.setPreposeNos(preposeNumbers);
            vo.setDeliverablesName(deliverNames);
            vo.setRowNum(rowNum);
            dataTempList.add(vo);
            excelMap.put(number, vo);
        }
        // 若模板中的业务数据为空，则提示且不进行导入操作
        if (CommonUtil.isEmpty(dataTempList)) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.common.importDataIsNull"));
        } else if (dataTempList.size() < 1) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkLeastOnePlan"));
        }
        //父计划,前置计划
        this.checkData2(excelMap,errorMsgMap,numList);
        if (0 == errorMsgMap.size()) {
            //计划模板ID
            PlanTemplate planTemplate = new PlanTemplate();
            planTemplate.setId(planTemplateId);
            res = savePlanTemplateDetailByExcel(planTemplate,dataTempList,excelMap,planLevelMap,switchStr,userDto,orgId);
        }

        returnMap.put("res", res);
        returnMap.put("dataTempList",errorDataTempList);
        return returnMap;
    }
}
