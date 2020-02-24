package com.glaway.ids.planGeneral.tabCombinationTemplate.businessServiceI.impI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.constant.LifeCycleConstant;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.foundation.system.serial.SerialNumberManager;
import com.glaway.ids.config.constant.SerialNumberConstants;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.PlanGeneralConstants;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import com.glaway.ids.planGeneral.tabCombinationTemplate.businessServiceI.TabCbTemplateBusinessServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.dto.TabCombinationTemplateDto;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.CombinationTemplateInfo;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.CombinationTemplateEntityServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.TabCbTemplateEntityServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.vo.CombinationTemplateVo;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.Dto2Entity;
import com.glaway.ids.util.ProcessUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: TabCbTemplateBusinessServiceImpl
 * @Date: 2019/8/29-18:57
 * @since
 */
@Service("tabCbTemplateBusinessService")
@Transactional
public class TabCbTemplateBusinessServiceImpl extends BusinessObjectServiceImpl<TabCombinationTemplate> implements TabCbTemplateBusinessServiceI {
    /**
     * Logger for this class
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(TabCbTemplateBusinessServiceImpl.class);

    @Autowired
    private FeignUserService userService;

    @Autowired
    private TabCbTemplateEntityServiceI tabCbTemplateEntityService;

    @Autowired
    private CombinationTemplateEntityServiceI combinationTemplateEntityService;

    @Autowired
    private CbTemplateBusinessServiceImpl cbTemplateBusinessService;

    @Autowired
    private PlanServiceI planService;

    /**
     * 注入WorkFlowFacade
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;

    @Autowired
    private Environment env;

    @Value(value="${spring.application.name}")
    private String appKey;

    private String message;

    @Override
    public FeignJson searchDataGrid(List<ConditionVO> conditionList, boolean isPage) {
        FeignJson j = new FeignJson();
        try {
            //列表数据查询
            List<TabCombinationTemplate> list = tabCbTemplateEntityService.queryEntity(conditionList,isPage);
            int count = tabCbTemplateEntityService.getCount(conditionList);
            List<TabCombinationTemplateDto> dtoList = new ArrayList<>();
            dtoList = (List<TabCombinationTemplateDto>) VDataUtil.getVDataByEntity(list,TabCombinationTemplateDto.class);
            //转换生命周期状态
            dtoList = setStatusByLifeCycleStatus(dtoList);
            PageList pageList = new PageList(count, dtoList);
            long count1 = pageList.getCount();
            String json = JsonUtil.getListJsonWithoutQuote(pageList.getResultList());
            String datagridStr = "{\"rows\":" + json + ",\"total\":" + count1 + "}";
            j.setObj(datagridStr);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @Override
    public FeignJson saveTabCbTemplateInfo(Map<String, Object> params) {
        FeignJson j = new FeignJson();
        message = I18nUtil.getValue("com.glaway.ids.pm.general.addCbtemplateSuccess");
        String userId = params.get("userId") == null ? "" : params.get("userId").toString();
        String orgId = params.get("orgId") == null ? "" : params.get("orgId").toString();
        TSUserDto curUser = userService.getUserByUserId(userId);
        ObjectMapper mapper = new ObjectMapper();
        TabCombinationTemplateDto tabCombinationTemplateDto = mapper.convertValue(params.get("dto"),TabCombinationTemplateDto.class);
        List<CombinationTemplateVo> voList = mapper.convertValue(params.get("list"),new TypeReference<List<CombinationTemplateVo>>(){});
        TabCombinationTemplate tabCombinationTemplate = new TabCombinationTemplate();
        Dto2Entity.copyProperties(tabCombinationTemplateDto,tabCombinationTemplate);
        saveInfo(voList,tabCombinationTemplate,curUser,orgId);
        j.setMsg(message);
        return j;
    }

    @Override
    public void saveInfo(List<CombinationTemplateVo> voList, TabCombinationTemplate tabCombinationTemplate, TSUserDto curUser, String orgId) {
        //获取页签组合模板编号
        String no = SerialNumberManager.getSerialNumber(SerialNumberConstants.Tab_Combation_Template);
        tabCombinationTemplate.setCode(no);
        //初始化页签组合模板生命周期
        initBusinessObject(tabCombinationTemplate);
        //先保存页签组合模板信息
        String templateId = tabCbTemplateEntityService.saveTabCombationTemplate(tabCombinationTemplate);
        //根据保存的页签组合模板信息id再保存组合模板信息
        List<CombinationTemplateInfo> combInfos = new ArrayList<>();
        for(int i = 0; i < voList.size(); i++) {
            CombinationTemplateInfo combInfo = new CombinationTemplateInfo();
            CommonInitUtil.initGLObjectForCreate(combInfo,curUser,orgId);
            combInfo.setOrderNum(i);
            combInfo.setName(voList.get(i).getName());
            combInfo.setTypeId(voList.get(i).getTypeId());
            combInfo.setTabCombinationTemplateId(templateId);
            combInfo.setDisplayAccess(voList.get(i).getDisplayAccess());
            combInfos.add(combInfo);
        }
        combinationTemplateEntityService.saveCbTemplateInfos(combInfos);
    }

    @Override
    public FeignJson updateTabCbTemplateInfo(Map<String, Object> params) {
        FeignJson j = new FeignJson();
        message = I18nUtil.getValue("com.glaway.ids.pm.general.updateCbtemplateSuccess");
        String userId = params.get("userId") == null ? "" : params.get("userId").toString();
        String orgId = params.get("orgId") == null ? "" : params.get("orgId").toString();
        TSUserDto curUser = userService.getUserByUserId(userId);
        ObjectMapper mapper = new ObjectMapper();
        TabCombinationTemplateDto dto = mapper.convertValue(params.get("dto"),TabCombinationTemplateDto.class);
        List<CombinationTemplateVo> voList = mapper.convertValue(params.get("list"),new TypeReference<List<CombinationTemplateVo>>(){});
        String method = params.get("method") == null ? "" : params.get("method").toString();
        //更新组合页签模板信息
        TabCombinationTemplate tabCombinationTemplate = new TabCombinationTemplate();
        Dto2Entity.copyProperties(dto,tabCombinationTemplate);
        /*TabCombinationTemplate tabCombinationTemplate = tabCbTemplateEntityService.findTabCbTempById(dto.getId());
        tabCombinationTemplate.setActivityId(dto.getActivityId());
        tabCombinationTemplate.setName(dto.getName());
        tabCombinationTemplate.setRemake(dto.getRemake());
        tabCombinationTemplate.setTemplateName(dto.getTemplateName());
        CommonInitUtil.initGLObjectForUpdate(tabCombinationTemplate,curUser,orgId);*/
        updateTabCbTemplate(tabCombinationTemplate, method, voList, curUser, orgId);
        /*//移除旧的组合模板信息
        cbTemplateBusinessService.removeCbTemplateInfos(tabCombinationTemplate.getId());
        //更新页签组合模板信息
        cbTemplateBusinessService.updateCbTemplateInfos(voList,tabCombinationTemplate,curUser,orgId);*/
        j.setMsg(message);
        return j;
    }

    public void updateTabCbTemplate(TabCombinationTemplate tabCombinationTemplate, String method,
                                    List<CombinationTemplateVo> voList, TSUserDto curUser, String orgId) {
        TabCombinationTemplate oldTemplate = tabCbTemplateEntityService.findTabCbTempById(tabCombinationTemplate.getId());
        TabCombinationTemplate avaliableTabCbTemplate = tabCbTemplateEntityService.findAvaliableTabCbTemplate(oldTemplate.getBizId());
        String id = avaliableTabCbTemplate.getId();
        if (StringUtils.equalsIgnoreCase(method, "miner")) {// 升级小版本
            String processInstanceId = oldTemplate.getProcessInstanceId();
            revise(id, LifeCycleConstant.ReviseModel.MINER);
            tabCbTemplateEntityService.saveOrUpdate(oldTemplate);
            TabCombinationTemplate r = findBusinessObjectByBizId(TabCombinationTemplate.class, oldTemplate.getBizId());
            //初始化对象
            CommonInitUtil.initGLObjectForCreate(r, curUser, orgId);
            r.setActivityId(tabCombinationTemplate.getActivityId());
            r.setName(tabCombinationTemplate.getName());
            r.setRemake(tabCombinationTemplate.getRemake());
            r.setTemplateName(tabCombinationTemplate.getTemplateName());
            //更新页签组合模板信息
            cbTemplateBusinessService.updateCbTemplateInfos(voList,r,curUser,orgId);
            if(StringUtils.isNotBlank(processInstanceId)) {
                oldTemplate.setProcessInstanceId(null);
                r.setProcessInstanceId(processInstanceId);
                tabCbTemplateEntityService.saveOrUpdate(r);
                forwardTabCbTemplate(processInstanceId, r, oldTemplate.getBizCurrent(), curUser);
            } else {
                tabCbTemplateEntityService.saveOrUpdate(r);
            }
        } else if (StringUtils.equalsIgnoreCase(method, "revise")) {// 升级大版本
            if(StringUtils.equalsIgnoreCase(oldTemplate.getBizCurrent(), PlanGeneralConstants.TABCBTEMPLATE_XIUDING)) {
                revise(id, LifeCycleConstant.ReviseModel.MINER);
                tabCbTemplateEntityService.saveOrUpdate(oldTemplate);
                TabCombinationTemplate r = findBusinessObjectByBizId(TabCombinationTemplate.class, oldTemplate.getBizId());
                //更新页签组合模板信息
                cbTemplateBusinessService.updateCbTemplateInfos(voList,r,curUser,orgId);
                //初始化对象
                CommonInitUtil.initGLObjectForCreate(r, curUser, orgId);
                r.setActivityId(tabCombinationTemplate.getActivityId());
                r.setName(tabCombinationTemplate.getName());
                r.setRemake(tabCombinationTemplate.getRemake());
                r.setTemplateName(tabCombinationTemplate.getTemplateName());
                String processInstanceId = oldTemplate.getProcessInstanceId();
                if(StringUtils.isNotBlank(processInstanceId)) {
                    oldTemplate.setProcessInstanceId(null);
                    r.setProcessInstanceId(processInstanceId);
                    tabCbTemplateEntityService.saveOrUpdate(r);
                    forwardTabCbTemplate(processInstanceId, r, oldTemplate.getBizCurrent(), curUser);
                } else {
                    tabCbTemplateEntityService.saveOrUpdate(r);
                }
            } else {
                String processInstanceId = oldTemplate.getProcessInstanceId();
                if (StringUtils.isNotBlank(processInstanceId)) {
                    revise(id, LifeCycleConstant.ReviseModel.MAJOR);
                    tabCbTemplateEntityService.saveOrUpdate(oldTemplate);
                    TabCombinationTemplate r = findBusinessObjectByBizId(TabCombinationTemplate.class, oldTemplate.getBizId());
                    //更新页签组合模板信息
                    cbTemplateBusinessService.updateCbTemplateInfos(voList,r,curUser,orgId);
                    r.setBizCurrent(PlanGeneralConstants.TABCBTEMPLATE_XIUDING);
                    r.setProcessInstanceId("");
                    //初始化对象
                    CommonInitUtil.initGLObjectForCreate(r, curUser, orgId);
                    r.setActivityId(tabCombinationTemplate.getActivityId());
                    r.setName(tabCombinationTemplate.getName());
                    r.setRemake(tabCombinationTemplate.getRemake());
                    r.setTemplateName(tabCombinationTemplate.getTemplateName());
                    tabCbTemplateEntityService.saveOrUpdate(r);
                    forwardTabCbTemplate(processInstanceId, r, oldTemplate.getBizCurrent(),curUser);
                } else {
                    revise(id, LifeCycleConstant.ReviseModel.MAJOR);
                    tabCbTemplateEntityService.saveOrUpdate(oldTemplate);
                    TabCombinationTemplate r = findBusinessObjectByBizId(TabCombinationTemplate.class, oldTemplate.getBizId());
                    //更新页签组合模板信息
                    cbTemplateBusinessService.updateCbTemplateInfos(voList,r,curUser,orgId);
                    r.setBizCurrent(PlanGeneralConstants.TABCBTEMPLATE_XIUDING);
                    r.setProcessInstanceId("");
                    //初始化对象
                    CommonInitUtil.initGLObjectForCreate(r, curUser, orgId);
                    r.setActivityId(tabCombinationTemplate.getActivityId());
                    r.setName(tabCombinationTemplate.getName());
                    r.setRemake(tabCombinationTemplate.getRemake());
                    r.setTemplateName(tabCombinationTemplate.getTemplateName());
                    tabCbTemplateEntityService.saveOrUpdate(r);
                }
            }
        }
    }

    private void revise(String projTmpId, String mode) {
        TabCombinationTemplate bo = findBusinessObjectById(TabCombinationTemplate.class, projTmpId);
        reviseBusinessObject(bo, mode);
        if (mode.equals(LifeCycleConstant.ReviseModel.MAJOR)) {
            TabCombinationTemplate r = findBusinessObjectHistoryByBizId(TabCombinationTemplate.class, bo.getBizId()).get(0);
            moveBusinessObjectByOrder(r, 0);
        }

    }

    @Override
    public FeignJson doStatusChange(String ids, String status,String userId) {
        FeignJson j = new FeignJson();
        TSUserDto curUser = userService.getUserByUserId(userId);
        if (status.equals(PlanGeneralConstants.TABCBTEMPLATE_QIYONG)) {
            message = I18nUtil.getValue("com.glaway.ids.pm.general.cbtemplateStartSuccess");
        } else {
            message = I18nUtil.getValue("com.glaway.ids.pm.general.cbtemplateStopSuccess");
        }
        try {
            if (!CommonUtil.isEmpty(ids)) {
                for (String id : ids.split(",")) {
                    TabCombinationTemplate t = tabCbTemplateEntityService.findTabCbTempById(id);
                    if(!CommonUtil.isEmpty(t)){
                        if (!t.getBizCurrent().equals(status)) {
                            CommonInitUtil.initGLObjectForUpdate(t,curUser,"");
                            t.setBizCurrent(status);
                            tabCbTemplateEntityService.saveOrUpdate(t);
                        }
                    }
                }
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson doBatchDel(String ids) {
        FeignJson j = new FeignJson();
        message = I18nUtil.getValue("com.glaway.ids.pm.general.cbtemplatedeleteSuccess");
        try {
            if (StringUtils.isNotEmpty(ids)) {
                for (String id : ids.split(",")) {
                    TabCombinationTemplate t = tabCbTemplateEntityService.findTabCbTempById(id);
                    if(!CommonUtil.isEmpty(t)){
                        if (StringUtils.isNotBlank(t.getProcessInstanceId())) {
                            //如果有流程变量，则删除对应的流程
                            if(!CommonUtil.isEmpty(t.getProcessInstanceId())) {
                                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(t.getId(),
                                        t.getProcessInstanceId(), "delete");
                            }
                        }
                        t.setAvaliable("0");
                        tabCbTemplateEntityService.saveOrUpdate(t);
                    }
                }
            }
        } catch (Exception e) {
            j.setMsg(e.getMessage());
            j.setSuccess(false);
        } finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson isActivityTypeManageUse(String id, String templateId) {
        FeignJson j = new FeignJson();
        //根据活动类型id获取启用的页签组合模板
        List<TabCombinationTemplate> templates = tabCbTemplateEntityService.findAllTabCbTemplatesByActivityId(id, templateId);
        if (!CommonUtil.isEmpty(templates)) {
            String message = I18nUtil.getValue("com.glaway.ids.pm.general.activityTypeIsInUse");
            j.setSuccess(false);
            j.setMsg(message);
        }
        return j;
    }

    @Override
    public FeignJson doSubmitApprove(Map<String, String> map) {
        FeignJson j = new FeignJson();
        String tabCbTemplateId = map.get("tabCbTemplateId") == null ? "" : map.get("tabCbTemplateId").toString();
        String leader = map.get("leader") == null ? "" : map.get("leader").toString();
        String deptLeader = map.get("deptLeader") == null ? "" : map.get("deptLeader").toString();
        String userId = map.get("userId") == null ? "" : map.get("userId").toString();
        String orgId = map.get("orgId") == null ? "" : map.get("orgId").toString();
        message = I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.doSubmitApproveSuccess");
        try {
            startTabCbTemplateProcess(tabCbTemplateId, leader, deptLeader, userId, orgId);
            j.setObj(tabCbTemplateId);
            log.info(message);
        } catch (Exception e) {
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.doSubmitApproveFail");
            log.error(message, e, tabCbTemplateId, "");
            Object[] params = new Object[] {message,
                    TabCombinationTemplate.class.getClass() + " oids:" + tabCbTemplateId};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        } finally {
            return j;
        }
    }

    @Override
    public FeignJson backVersion(Map<String, String> params) {
        FeignJson j = new FeignJson();

        String id = params.get("id") == null ? "" : params.get("id").toString();
        String bizId = params.get("bizId") == null ? "" : params.get("bizId").toString();
        String bizVersion = params.get("bizVersion") == null ? "" : params.get("bizVersion").toString();
        String type = params.get("type") == null ? "" : params.get("type").toString();
        String userId = params.get("userId") == null ? "" : params.get("userId").toString();
        String orgId = params.get("orgId") == null ? "" : params.get("orgId").toString();

        message = I18nUtil.getValue("com.glaway.ids.pm.general.backSuccess"+type);
        try {
            TabCombinationTemplate tabCombinationTemplate = tabCbTemplateEntityService.findTabCbTempById(id);
            if(!CommonUtil.isEmpty(tabCombinationTemplate.getBizVersion())){
                if(!bizVersion.equals(tabCombinationTemplate.getBizVersion())){
                    message = I18nUtil.getValue("com.glaway.ids.pm.projectemplate.backCannotNew"+type);
                    j.setSuccess(false);
                    return j;
                }
                String verson = tabCombinationTemplate.getBizVersion();
                String vs[] = verson.split("\\.");
                if(StringUtils.equalsIgnoreCase(type, "Min")){//回退
                    if (vs[1].equals("1")) {
                        message = I18nUtil.getValue("com.glaway.ids.pm.projectemplate.cannotBackFirst"+type);
                        j.setSuccess(false);
                        return j;
                    }
                }
                if(StringUtils.equalsIgnoreCase(type, "Maj")){//撤回
                    if (vs[0].equals("A")) {
                        message = I18nUtil.getValue("com.glaway.ids.pm.projectemplate.cannotBackFirst"+type);
                        j.setSuccess(false);
                        return j;
                    }
                }
            }
            backToVersion(id, bizId, type, userId, orgId);
            log.info(message);
        } catch (Exception e) {
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.general.backFailure"+type);
            log.error(message, e, id, "");
            Object[] param = new Object[] {message,
                    TabCombinationTemplate.class.getClass() + " oids:" + id};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, param, e);
        } finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public TabCombinationTemplate getTabCbTemplateByPlanId(String planId) {
        Plan plan = planService.getEntity(planId);
        if (!CommonUtil.isEmpty(plan)) {
            if (StringUtils.isNotBlank(plan.getTabCbTemplateId())) {
                TabCombinationTemplate tabCombinationTemplate = tabCbTemplateEntityService.findTabCbTempById(plan.getTabCbTemplateId());
                return tabCombinationTemplate;
            }
        }
        return null;
    }

    @Override
    public List<TabCombinationTemplateDto> setStatusByLifeCycleStatus(List<TabCombinationTemplateDto> templateDtos) {
        //转换生命周期状态
        Map<String, String> statusMap = new HashMap<String, String>();
        List<LifeCycleStatus> lifeCycleStatuses = getLifeCycleStatusList();
        for(LifeCycleStatus status : lifeCycleStatuses){
            statusMap.put(status.getName(), status.getTitle());
        }
        if(!CommonUtil.isEmpty(templateDtos)){
            for(TabCombinationTemplateDto templateDto : templateDtos){
                templateDto.setStatus(statusMap.get(templateDto.getBizCurrent()));
            }
        }
        return templateDtos;
    }

    @Override
    public void backToVersion(String id, String bizId, String type, String userId, String orgId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        TabCombinationTemplate template = tabCbTemplateEntityService.findTabCbTempById(id);
        if (StringUtils.equalsIgnoreCase(type, "Min")) {
            //如果有流程变量，则删除对应的流程
            if(!CommonUtil.isEmpty(template.getProcessInstanceId())) {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(template.getId(),
                        template.getProcessInstanceId(), "delete");
            }
            //删除当前版本对应的数据
            tabCbTemplateEntityService.deleteEntityById(TabCombinationTemplate.class, id);
            //查询上一个版本的数据
            List<TabCombinationTemplate> taskTabCbTemplates = findBusinessObjectHistoryByBizId(TabCombinationTemplate.class, bizId);
            if (!CommonUtil.isEmpty(taskTabCbTemplates)) {
                TabCombinationTemplate t = taskTabCbTemplates.get(0);
                CommonInitUtil.initGLObjectForUpdate(t, userDto , orgId);
                t.setAvaliable(PlanGeneralConstants.TABCBTEMPLATE_USED);
                tabCbTemplateEntityService.update(t);
            }
        } else if (StringUtils.equalsIgnoreCase(type, "Maj")) {
            String bizversion = null;
            if (StringUtil.isNotEmpty(template.getBizVersion())) {
                bizversion = template.getBizVersion().split("\\.")[0] + ".";
            }
            List<TabCombinationTemplate> templates = tabCbTemplateEntityService.findTemplatesByBizIdOrBizVersion(bizId, bizversion);
            for (TabCombinationTemplate temp : templates) {
                if ("1".equals(temp.getAvaliable()) && !CommonUtil.isEmpty(temp.getProcessInstanceId())) {
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(temp.getId(),
                            temp.getProcessInstanceId(), "delete");
                }
                tabCbTemplateEntityService.delete(temp);
            }
            List<TabCombinationTemplate> lists = tabCbTemplateEntityService.findTemplatesByBizIdOrBizVersion(bizId,null);
            if (lists.size() > 0) {
                TabCombinationTemplate tabCombinationTemplate = lists.get(0);
                tabCombinationTemplate.setAvaliable(PlanGeneralConstants.TABCBTEMPLATE_USED);
                CommonInitUtil.initGLObjectForUpdate(tabCombinationTemplate, userDto , orgId);
                tabCbTemplateEntityService.update(tabCombinationTemplate);
            }
        }
    }

    @Override
    public List<LifeCycleStatus> getLifeCycleStatusList() {
        TabCombinationTemplate tabCombinationTemplate = new TabCombinationTemplate();
        initBusinessObject(tabCombinationTemplate);
        List<LifeCycleStatus> lifeCycleStatus = tabCombinationTemplate.getPolicy().getLifeCycleStatusList();
        return lifeCycleStatus;
    }

    @Override
    public void startTabCbTemplateProcess(String tabCbTemplateId, String leader, String deptLeader, String userId, String orgId) {
        TabCombinationTemplate tabCombinationTemplate =  tabCbTemplateEntityService.findTabCbTempById(tabCbTemplateId);
        TSUserDto userDto = userService.getUserByUserId(userId);
        // 设置流程变量
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("leader", leader);
        variables.put("assigner", userDto.getUserName());
        variables.put("deptLeader", deptLeader);
        variables.put("editUrl",
                "/ids-pm-web/tabCombinationTemplateController.do?goTabCbTTemplateEdit&dataHeight=500&dataWidth=900&tabCbTemplateId="
                        + tabCbTemplateId);
        variables.put("viewUrl",
                "/ids-pm-web/tabCombinationTemplateController.do?goTabCbTemplateLayout&dataHeight=500&dataWidth=900&id="
                        + tabCbTemplateId);
        variables.put("oid", BpmnConstants.OID_TABCBTEMPLATE + tabCbTemplateId);
        variables.put("flowStatus", tabCombinationTemplate.getBizCurrent());
        variables.put("cancelEventListener",
                "com.glaway.foundation.activiti.task.common.CommonCancelListener");
        // 设置流程审批意见变量
        variables.put("desc", "");
        variables.put("userId", userDto.getId());
        //执行监听
        String feignContinueUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/tabCbTemplateListenerRestController/notify.do";
        variables.put("feignTabCbTemplateContinueListener", feignContinueUrl);
        //驳回监听
        String feignRefuseUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/tabCbTemplateGoBackListenerRestController/notify.do";
        variables.put("feignTabCbTemplateRefuseListener", feignRefuseUrl);
        if(CommonUtil.isEmpty(tabCombinationTemplate.getProcessInstanceId())){
            submitProjectTemplateFlow(tabCombinationTemplate,variables,BpmnConstants.BPMN_START_TABCBTEMPLATE,userDto,orgId);
        }else{
            submitProjectTemplateFlowAagin(tabCombinationTemplate,userDto,orgId);
        }
    }

    @Override
    public void submitProjectTemplateFlow(TabCombinationTemplate template, Map<String, Object> variables, String processDefinitionKey, TSUserDto curUser, String orgId) {
        String creator = curUser.getUserName();
        if (!CommonUtil.isEmpty(template.getProcessInstanceId())) {
            List<TaskDto> nextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    template.getProcessInstanceId(),creator);
            // 判断是否有流程（暂停或关闭流程），如有：结束流程
            if (!CommonUtil.isEmpty(nextTasks)) {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                        template.getId(), template.getProcessInstanceId(), "delete");
            }
        }

        FeignJson j = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                processDefinitionKey, template.getId(), variables);
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
        myStartedTask.setTaskNumber(template.getId());
        myStartedTask.setType(processDefinitionKey);
        myStartedTask.setCreater(creator);
        myStartedTask.setCreaterFullname(curUser.getRealName());
        myStartedTask.setStartTime(new Date());
        myStartedTask.setProcType(processDefinitionKey);
        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        // 流程已发起列表中：增加对象名称的记录，流程任务名称规范化
        myStartedTask.setTitle(ProcessUtil.getProcessTitle(template.getName(),
                BpmnConstants.BPMN_TABCBTEMPLATE_APPLY_DISPLAYNAME, procInstId));

        myStartedTask.setObjectName(template.getName());

        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

        template.setProcessInstanceId(procInstId);
        template.setBizCurrent(BpmnConstants.PROJTEMPLATE_SHENHE);
        CommonInitUtil.initGLObjectForUpdate(template,curUser,orgId);
        tabCbTemplateEntityService.updateEntitie(template);
    }

    @Override
    public void submitProjectTemplateFlowAagin(TabCombinationTemplate template, TSUserDto curUser, String orgId) {
        String creator = curUser.getUserName();
        FeignJson fj = workFlowFacade.getWorkFlowTodoTaskService().getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                template.getProcessInstanceId(), creator);
        String taskId = String.valueOf(fj.getObj());
        // 判断当前用户是否有流程（对应项目的启动、暂停、恢复、关闭流程），如有：重新提交启动流程
        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);

            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    template.getProcessInstanceId());
            String status = newNextTasks.getObj().toString();
            // 更新我的已发起流程状态
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                    template.getId(), template.getProcessInstanceId());
            myStartedTask.setStatus(status);
            workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);

            template.setBizCurrent(BpmnConstants.PROJTEMPLATE_SHENHE);
            CommonInitUtil.initGLObjectForUpdate(template,curUser,orgId);
            tabCbTemplateEntityService.updateEntitie(template);
        }
    }

    public void forwardTabCbTemplate(String processInstanceId, TabCombinationTemplate t, String bizCurrent,TSUserDto userDto) {
        //更新我的待办及流程信息
        if(!CommonUtil.isEmpty(processInstanceId)) {
            List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    processInstanceId,userDto.getUserName());

            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByProcInstId(processInstanceId);
            String taskId = "";
            Map<String, Object> variables = new HashMap<String, Object>();
            if(!CommonUtil.isEmpty(tasks)) {
                taskId = tasks.get(0).getId();
                variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(taskId);
            }
            if(!CommonUtil.isEmpty(variables)) {
                variables.put("editUrl",
                        "/ids-pm-web/tabCombinationTemplateController.do?goTabCbTTemplateEdit&dataHeight=500&dataWidth=900&tabCbTemplateId="
                                + t.getId());
                variables.put("viewUrl",
                        "/ids-pm-web/tabCombinationTemplateController.do?goTabCbTemplateLayout&dataHeight=500&dataWidth=900&id="
                                + t.getId());
                variables.put("oid", BpmnConstants.OID_TABCBTEMPLATE + t.getId());
                variables.put("taskNumber", t.getId());
                variables.put("flowStatus", bizCurrent);
                // 执行流程
                //workFlowFacade.getWorkFlowCommonService().setRunVariables(processInstanceId, variables);
                workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

                FeignJson fj = workFlowFacade.getWorkFlowCommonService().getProcessDefinitionJsonMapByProcInstId(
                        processInstanceId, false);

                Map<String, Object> attributes = new HashMap<>();

                if (fj.isSuccess()) {
                    attributes = fj.getAttributes();
                    String bpmnDisplayName = attributes.get("name") == null ? "" : attributes.get("name").toString();
                    myStartedTask.setObjectName(t.getName());
                    myStartedTask.setTaskNumber(t.getId());
                    myStartedTask.setTitle(ProcessUtil.getProcessTitle(t.getName(), bpmnDisplayName, processInstanceId));
                    FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                            processInstanceId);
                    String status = newNextTasks.getObj().toString();
                    myStartedTask.setStatus(status);
                    workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
                }
            }
        }
    }

    @Override
    public void updateBizCurrentForProcessComplete(String templateId) {
        TabCombinationTemplate template = tabCbTemplateEntityService.findTabCbTempById(templateId);
        template.setBizCurrent(PlanGeneralConstants.TABCBTEMPLATE_QIYONG);
        tabCbTemplateEntityService.update(template);
    }

    @Override
    public void updateBizCurrentForProcessRefuse(String templateId, String bizCurrent) {
        TabCombinationTemplate template = tabCbTemplateEntityService.findTabCbTempById(templateId);
        if(StringUtils.equals(bizCurrent, PlanGeneralConstants.TABCBTEMPLATE_XIUDING)) {
            template.setBizCurrent(PlanGeneralConstants.TABCBTEMPLATE_XIUDING);
        } else {
            template.setBizCurrent(PlanGeneralConstants.TABCBTEMPLATE_NIZHL);
        }
        tabCbTemplateEntityService.update(template);
    }

    @Override
    public void updateTabCbTemplateByActivity(ActivityTypeManage activityTypeManage) {
        //更加活动类型id获取所有未删除的页签组合模板
        List<TabCombinationTemplate> templates = tabCbTemplateEntityService.findAllTabCbTemplatesByActivityId(activityTypeManage.getId(), "");
        if (!CommonUtil.isEmpty(templates)) {
            for (TabCombinationTemplate template : templates) {
                template.setTemplateName(activityTypeManage.getName());
            }
            tabCbTemplateEntityService.batchUpdate(templates);
        }
    }
}
