package com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.impl;

import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.system.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.common.util.BeanUtil;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.constant.LifeCycleConstant;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.foundation.system.serial.SerialNumberManager;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.PlanGeneralConstants;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.planGeneral.plantabtemplate.dto.TabTemplateDto;
import com.glaway.ids.planGeneral.plantabtemplate.entity.TabTemplate;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.TabTemplateEntityServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.ProcessUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;


/**
 * @Title: EntityServiceImpl
 * @Description: 页签模版管理EntityServiceImpl
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
@Service("tabTemplateEntityServiceI")
public class TabTemplateEntityServiceImpl extends BusinessObjectServiceImpl<TabTemplate> implements TabTemplateEntityServiceI {

    //禁用启用标示数据集合
    private static Map<String, String> stopFlagMap = new HashMap<>();

    @Autowired
    private FeignUserService userService;

    @Autowired
    private CommonService commonServiceImpl;

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private WorkFlowFacade workFlowFacade;

    private String message;

    @Autowired
    private Environment env;

    @Value(value="${spring.application.name}")
    private String appKey;

    /**
     * 操作日志
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(TabTemplateEntityServiceImpl.class);

    //类型常量
    private static final String TAB_TYPE = "全部";
    private static final String TAB_TEMPLATE_CODE = "tabTemplateCode";


    static {
        stopFlagMap.put("1", "启用");
        stopFlagMap.put("0", "禁用");
    }

    /**
     * 功能描述：根据查询条件查询数据(HQL组装)
     * @param params 查询条件Map
     */
    @Override
    public PageList searchDatagrid(Map<String, String> params) {
        try {
            // 条件查询
            String searchHql = "select t from TabTemplate t where 1=1 and avaliable =\'1\'";
            // 查询返回数据的数量
            String countHql = "select count(1) from TabTemplate t where 1=1 and avaliable =\'1\'";

            //名称过滤（模糊查询）
            if (StringUtil.isNotEmpty(params.get("name"))) {
                searchHql += " and (lower(t.name) like '%'||lower(\'" + params.get("name") + "\')||'%') ";
                countHql += " and (lower(t.name) like '%'||lower(\'" + params.get("name") + "\')||'%') ";
            }
            //状态标志过滤
            if (StringUtil.isNotEmpty(params.get("bizCurrent"))) {
                searchHql += " and t.bizCurrent=\'" + params.get("bizCurrent") + "\'";
                countHql += " and t.bizCurrent=\'" + params.get("bizCurrent") + "\'";
            }

            //类型过滤
            if (StringUtil.isNotEmpty(params.get("tabType"))&&!params.get("tabType").equals(TAB_TYPE)) {
                searchHql += " and t.tabType=\'" + params.get("tabType") + "\'";
                countHql += " and t.tabType=\'" + params.get("tabType") + "\'";
            }

            //编码过滤
            if (StringUtil.isNotEmpty(params.get("code"))) {
                searchHql += " and (lower(t.code) like '%'||lower(\'" + params.get("code") + "\')||'%') ";
                countHql += " and (lower(t.code) like '%'||lower(\'" + params.get("code") + "\')||'%') ";
            }

            //排序过滤
            String orderBy = "";
            if (StringUtil.isNotEmpty(params.get("order"))
                && StringUtil.isNotEmpty(params.get("sort"))) {
                if ("status".equals(params.get("sort"))) {
                    orderBy = "t.stopFlag " + params.get("order") + ",";
                }
                else if ("code".equals(params.get("sort"))) {
                    orderBy = "t.code " + params.get("order") + ",";
                }
                else if ("name".equals(params.get("sort"))) {
                    orderBy = "t.name " + params.get("order") + ",";
                }
                else if ("tabType".equals(params.get("sort"))) {
                    orderBy = "t.tabType " + params.get("order") + ",";
                }
                else {
                    orderBy = "t." + params.get("sort") + " " + params.get("order") + ",";
                }

            }
            //组装排序过滤HQL
            searchHql += " order by "+orderBy+" t.code desc ";

            int page = Integer.parseInt(params.get("page"));
            int rows = Integer.parseInt(params.get("rows"));
            //查询数据
            List<TabTemplate> list = commonServiceImpl.pageList(searchHql, (page - 1) * rows, rows);
            //查询总条数
            int count = (int)commonServiceImpl.getCount(countHql, null);
            List<TabTemplateDto> dtoList = new ArrayList<TabTemplateDto>();
            //数据转换DTO
            for (TabTemplate info : list) {
                TabTemplateDto dto = new TabTemplateDto();
                dto.setId(info.getId());
                dto.setName(info.getName());
                dto.setRemake(info.getRemake());
                dto.setCreateName(info.getCreateFullName()+"-"+info.getCreateName());
                dto.setCreateTime(info.getCreateTime());
                dto.setSource(info.getSource());
                dto.setCode(info.getCode());
                dto.setTabType(info.getTabType());
                dto.setDisplayUsage(info.getDisplayUsage());
                dto.setBizCurrent(info.getBizCurrent());
                dto.setBizVersion(info.getBizVersion());
                dto.setProcessInstanceId(info.getProcessInstanceId());
                dto.setBizId(info.getBizId());
                dtoList.add(dto);
            }
            PageList pageList = new PageList(count, dtoList);
            return pageList;
        }
        catch (Exception e) {
            log.error("TabTemplateEntityServiceImpl#searchDatagrid--查询失败！");
            throw new GWException("HQL查询失败！");
        }
    }

    /**
     * 功能描述：批量/单条 根据主键ID和状态Status启用或禁用页签模版
     * @param ids id集合
     * @param status 状态(启用“1”或者禁用“0”)
     */
    @Override
    public int doStartOrStop(String ids, String status) {
        //执行成功状态1-成功，0失败！
        int successFlag = 1;
        try{
            TabTemplate info;
            //数据切分处理
            for(String id : ids.split(",")){
                info = commonServiceImpl.getEntity(TabTemplate.class, id);
                info.setBizCurrent(status);
                commonServiceImpl.saveOrUpdate(info);
            }
        }catch (Exception e){
            successFlag = 0;
            log.error("TabTemplateEntityServiceImpl#doStartOrStop--启用或禁用页签模版失败！");
        }
        return successFlag;
    }

    /**
     * 功能描述：批量/单条 根据主键ID删除页签模版
     * @param ids id集合(“，”分隔)
     */
    @Override
    public int doBatchDelete(String ids) {
        //执行成功状态1-成功，0失败！
        int successFlag = 1;
        try{
            TabTemplate info;
            //数据切分处理
            for(String id : ids.split(",")){
                if (!StringUtils.isEmpty(id)){
                    info = commonServiceImpl.getEntity(TabTemplate.class, id);
                    info.setAvaliable("0");
                    commonServiceImpl.update(info);
                }
            }
        }catch (Exception e){
            successFlag = 0;
            log.error("TabTemplateEntityServiceImpl#doStartOrStop--删除页签模版失败！");
        }
        return successFlag;
    }

    /**
     * 功能描述：根据主键ID查询对象
     * @param id 主键ID
     */
    @Override
    public TabTemplate queryEntityById(String id) {
        return (TabTemplate)sessionFacade.getEntity(TabTemplate.class, id);
    }

    /**
     * 功能描述：数据保存
     * @param tabTemplate
     */
    public TabTemplate doSave(TabTemplate tabTemplate){
        try{
            String userId = tabTemplate.getExt1();
            tabTemplate.setExt1("");
            tabTemplate.setCreateBy(userId);
            TabTemplate newTab = new TabTemplate();
            TSUserDto userDto = new TSUserDto();
            if (StringUtils.isNotEmpty(userId)){
                userDto = userService.getUserByUserId(userId);
                tabTemplate.setCreateName(userDto.getUserName());
                tabTemplate.setCreateFullName(userDto.getRealName());
                tabTemplate.setCreateTime(new Date());
            }
            if (!StringUtils.isEmpty(tabTemplate.getId())){
                newTab = queryEntityById(tabTemplate.getId());
                if (userDto!=null){
                    newTab.setCreateFullName(userDto.getRealName());
                    newTab.setCreateName(userDto.getUserName());
                    newTab.setCreateBy(userId);
                }
                newTab.setAvaliable(tabTemplate.getAvaliable());
                newTab.setSource(tabTemplate.getSource());
                newTab.setRemake(tabTemplate.getRemake());
                newTab.setTabType(tabTemplate.getTabType());
                newTab.setExternalURL(tabTemplate.getExternalURL());
                newTab.setStopFlag(tabTemplate.getStopFlag());
                newTab.setDisplayUsage(tabTemplate.getDisplayUsage());
                newTab.setName(tabTemplate.getName());
                newTab.setCode(tabTemplate.getCode());
                newTab.setProcessInstanceId(tabTemplate.getProcessInstanceId());
                newTab.setCreateTime(new Date());
                commonServiceImpl.update(newTab);
            }else {
                //新增数据进行初始化
                initBusinessObject(tabTemplate);
                String code =SerialNumberManager.getSerialNumber(TAB_TEMPLATE_CODE);
                tabTemplate.setCode(code);
                Serializable id = commonServiceImpl.save(tabTemplate);
                tabTemplate.setId(id.toString());
            }
        }catch (Exception e){
            log.error("TabTemplateEntityServiceImpl#doSave--数据保存失败！", e);
        }
        return tabTemplate;
    }

    /**
     * 功能描述：通过名称获取信息
     * @param name
     */
    @Override
    public List<TabTemplate> queryTabTemplateByName(String name, String id) {
        // 条件查询
        String searchHql = "select t from TabTemplate t where 1=1 and avaliable =\'1\'";
        //编码过滤
        if (StringUtil.isNotEmpty(name)) {
            searchHql += " and t.name=\'" + name + "\'";
        }
        if (StringUtil.isNotEmpty(id)){
            searchHql +="and t.id <>\'"+id+"\'";
        }
        List<TabTemplate> list = commonServiceImpl.executeQuery(searchHql, null);
        return list;
    }

    @Override
    public List<TabTemplate> getAllTabTemplates(String avaliable, String stopFlag) {
        String hql = "from TabTemplate t where 1=1";
        if (StringUtils.isNotBlank(avaliable)) {
            hql += " and t.avaliable='"+avaliable+"'";
        }
        if (StringUtils.isNotBlank(stopFlag)) {
            hql += "  and t.bizCurrent='"+stopFlag+"'";
        }
        List<TabTemplate> tabTemplates = commonServiceImpl.findByQueryString(hql);
        return tabTemplates;
    }

    @Override
    public TabTemplate doUpdateOrRevise(TabTemplate tabTemplate, String userId, String updateOrRevise) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        TabTemplate newTabTemplate = new TabTemplate();
        TabTemplate oldTab = queryEntityById(tabTemplate.getId());
        //修改，小版本升级
        if (updateOrRevise.equals("update")){
            String processInstanceId = oldTab.getProcessInstanceId();
            revise(oldTab.getId(), LifeCycleConstant.ReviseModel.MINER);
            sessionFacade.saveOrUpdate(oldTab);
            newTabTemplate = findBusinessObjectByBizId(TabTemplate.class, oldTab.getBizId());
            newTabTemplate.setName(tabTemplate.getName());
            newTabTemplate.setRemake(tabTemplate.getRemake());
            newTabTemplate.setCode(tabTemplate.getCode());
            newTabTemplate.setDisplayUsage(tabTemplate.getDisplayUsage());
            newTabTemplate.setExternalURL(tabTemplate.getExternalURL());
            newTabTemplate.setTabType(tabTemplate.getTabType());
            newTabTemplate.setSource(tabTemplate.getSource());
            newTabTemplate.setCreateTime(new Date());
            newTabTemplate.setCreateFullName(userDto.getRealName());
            newTabTemplate.setCreateBy(userDto.getId());
            newTabTemplate.setCreateName(userDto.getUserName());
            if(StringUtils.isNotBlank(processInstanceId)) {
                tabTemplate.setProcessInstanceId(null);
                newTabTemplate.setProcessInstanceId(processInstanceId);
                sessionFacade.saveOrUpdate(newTabTemplate);
                forwardProjTemplate(processInstanceId, newTabTemplate, tabTemplate.getBizCurrent(),userDto);
            }else{
                sessionFacade.saveOrUpdate(newTabTemplate);
            }
            //修订，大版本升级
        }else if (updateOrRevise.equals("revise")){
            String processInstanceId = oldTab.getProcessInstanceId();
            if(StringUtils.isNotBlank(processInstanceId)) {
                revise(oldTab.getId(), LifeCycleConstant.ReviseModel.MAJOR);
                sessionFacade.saveOrUpdate(oldTab);
                newTabTemplate = findBusinessObjectByBizId(TabTemplate.class, oldTab.getBizId());
                newTabTemplate.setName(tabTemplate.getName());
                newTabTemplate.setRemake(tabTemplate.getRemake());
                newTabTemplate.setCode(tabTemplate.getCode());
                newTabTemplate.setDisplayUsage(tabTemplate.getDisplayUsage());
                newTabTemplate.setExternalURL(tabTemplate.getExternalURL());
                newTabTemplate.setTabType(tabTemplate.getTabType());
                newTabTemplate.setSource(tabTemplate.getSource());
                newTabTemplate.setBizCurrent(ProjectConstants.PROJTEMPLATE_XIUDING);
                newTabTemplate.setProcessInstanceId("");
                newTabTemplate.setCreateTime(new Date());
                newTabTemplate.setCreateFullName(userDto.getRealName());
                newTabTemplate.setCreateBy(userDto.getId());
                newTabTemplate.setCreateName(userDto.getUserName());
                sessionFacade.saveOrUpdate(newTabTemplate);
                forwardProjTemplate(processInstanceId, newTabTemplate, tabTemplate.getBizCurrent(),userDto);
            } else {
                revise(oldTab.getId(), LifeCycleConstant.ReviseModel.MAJOR);
                sessionFacade.saveOrUpdate(oldTab);
                newTabTemplate = findBusinessObjectByBizId(TabTemplate.class, oldTab.getBizId());
                newTabTemplate.setName(tabTemplate.getName());
                newTabTemplate.setRemake(tabTemplate.getRemake());
                newTabTemplate.setCode(tabTemplate.getCode());
                newTabTemplate.setDisplayUsage(tabTemplate.getDisplayUsage());
                newTabTemplate.setExternalURL(tabTemplate.getExternalURL());
                newTabTemplate.setTabType(tabTemplate.getTabType());
                newTabTemplate.setSource(tabTemplate.getSource());
                newTabTemplate.setBizCurrent(ProjectConstants.PROJTEMPLATE_XIUDING);
                newTabTemplate.setProcessInstanceId("");
                newTabTemplate.setCreateTime(new Date());
                newTabTemplate.setCreateFullName(userDto.getRealName());
                newTabTemplate.setCreateBy(userDto.getId());
                newTabTemplate.setCreateName(userDto.getUserName());
                sessionFacade.saveOrUpdate(newTabTemplate);
            }
        }
        return newTabTemplate;
    }

    /**
     * 功能描述：回退/撤回
     * @param: params
     * @Date: 2019/11/29
     * @return: FeignJson
     */
    @Override
    public FeignJson doBack(Map<String, String> params) {
        FeignJson j = new FeignJson();
        String id = params.get("id") == null ? "" : params.get("id").toString();
        String bizId = params.get("bizId") == null ? "" : params.get("bizId").toString();
        String bizVersion = params.get("bizVersion") == null ? "" : params.get("bizVersion").toString();
        String type = params.get("type") == null ? "" : params.get("type").toString();
        String userId = params.get("userId") == null ? "" : params.get("userId").toString();
        String orgId = params.get("orgId") == null ? "" : params.get("orgId").toString();

        message = I18nUtil.getValue("com.glaway.ids.pm.tabTemplate.backSuccess" + type);
        try {
            TabTemplate tabTemplate = queryEntityById(id);
            if(!CommonUtil.isEmpty(tabTemplate.getBizVersion())){
                if(!bizVersion.equals(tabTemplate.getBizVersion())){
                    message = I18nUtil.getValue("com.glaway.ids.pm.projectemplate.backCannotNew"+type);
                    j.setSuccess(false);
                    return j;
                }
                String verson = tabTemplate.getBizVersion();
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
            message = I18nUtil.getValue("com.glaway.ids.pm.tabTemplate.backFailure"+type);
            log.error(message, e, id, "");
        } finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson doSubmitApprove(Map<String, String> params) {
        FeignJson j = new FeignJson();
        String tabId = params.get("tabId") == null ? "" : params.get("tabId").toString();
        String leader = params.get("leader") == null ? "" : params.get("leader").toString();
        String deptLeader = params.get("deptLeader") == null ? "" : params.get("deptLeader").toString();
        String userId = params.get("userId") == null ? "" : params.get("userId").toString();
        String orgId = params.get("orgId") == null ? "" : params.get("orgId").toString();
        message = I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.doSubmitApproveSuccess");
        try {
            startTabCbTemplateProcess(tabId, leader, deptLeader, userId, orgId);
            j.setObj(tabId);
            log.info(message);
        } catch (Exception e) {
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.doSubmitApproveFail");
            log.error(message, e, tabId, "");
            Object[] paramsObj = new Object[] {message, TabTemplate.class.getClass() + " oids:" + tabId};// 异常原因：{0}；异常描述：{1}
            throw new com.glaway.foundation.common.exception.GWException(GWConstants.ERROR_2005, paramsObj, e);
        } finally {
            return j;
        }
    }

    @Override
    public void updateBizCurrentForProcessComplete(String tabId) {
        TabTemplate tabTemplate = queryEntityById(tabId);;
        tabTemplate.setBizCurrent(PlanGeneralConstants.TABCBTEMPLATE_QIYONG);
        commonServiceImpl.update(tabTemplate);
    }

    private void startTabCbTemplateProcess(String tabId, String leader, String deptLeader, String userId, String orgId) {
        TabTemplate tabTemplate=  queryEntityById(tabId);
        TSUserDto userDto = userService.getUserByUserId(userId);
        // 设置流程变量
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("leader", leader);
        variables.put("assigner", userDto.getUserName());
        variables.put("deptLeader", deptLeader);
        variables.put("editUrl",
            "/ids-pm-web/tabTemplateController.do?goTebTemplateUpdate&dataHeight=610&dataWidth=1200&id=" + tabId);
        variables.put("viewUrl",
            "/ids-pm-web/tabTemplateController.do?goVersionDetailHistory&dataHeight=610&dataWidth=1200&tabId=" + tabId);
        variables.put("oid", BpmnConstants.OID_TABTEMPLATE + tabId);
        variables.put("flowStatus", tabTemplate.getBizCurrent());
        variables.put("cancelEventListener", "com.glaway.foundation.activiti.task.common.CommonCancelListener");
        // 设置流程审批意见变量
        variables.put("desc", "");
        variables.put("userId", userDto.getId());
        //执行监听
        String feignContinueUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/tabTemplateListenerRestController/notify.do";
        variables.put("feignTabTemplateContinueListener", feignContinueUrl);
        //驳回监听
        String feignRefuseUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/tabTemplateGoBackListenerRestController/notify.do";
        variables.put("feignTabTemplateRefuseListener", feignRefuseUrl);
        if(CommonUtil.isEmpty(tabTemplate.getProcessInstanceId())){
            submitProjectTemplateFlow(tabTemplate,variables,BpmnConstants.BPMN_START_TABTEMPLATE,userDto,orgId);
        }else{
            submitProjectTemplateFlowAagin(tabTemplate,userDto,orgId);
        }
    }
    @Override
    public void updateBizCurrentForProcessRefuse(String templateId, String bizCurrent) {
        TabTemplate template = queryEntityById(templateId);
        if(StringUtils.equals(bizCurrent, PlanGeneralConstants.TABCBTEMPLATE_XIUDING)) {
            template.setBizCurrent(PlanGeneralConstants.TABCBTEMPLATE_XIUDING);
        } else {
            template.setBizCurrent(PlanGeneralConstants.TABCBTEMPLATE_NIZHL);
        }
        commonServiceImpl.update(template);
    }

    private void backToVersion(String id, String bizId, String type, String userId, String orgId) {
        TabTemplate template = queryEntityById(id);
        if (StringUtils.equalsIgnoreCase(type, "Min")) {
            //如果有流程变量，则删除对应的流程
            if(!CommonUtil.isEmpty(template.getProcessInstanceId())) {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(template.getId(),
                    template.getProcessInstanceId(), "delete");
            }
            //删除当前版本对应的数据
            sessionFacade.deleteEntityById(TabTemplate.class, id);
            //查询上一个版本的数据
            List<TabTemplate> taskTabCbTemplates = findBusinessObjectHistoryByBizId(TabTemplate.class, bizId);
            if (!CommonUtil.isEmpty(taskTabCbTemplates)) {
                TabTemplate t = taskTabCbTemplates.get(0);
                t.setAvaliable(PlanGeneralConstants.TABCBTEMPLATE_USED);
                commonServiceImpl.update(t);
            }
        } else if (StringUtils.equalsIgnoreCase(type, "Maj")) {
            String bizversion = null;
            if (StringUtil.isNotEmpty(template.getBizVersion())) {
                bizversion = template.getBizVersion().split("\\.")[0] + ".";
            }
            List<TabTemplate> templates = findTabTemplatesByBizIdOrBizVersion(bizId, bizversion);
            for (TabTemplate temp: templates) {
                if ("1".equals(temp.getAvaliable()) && !CommonUtil.isEmpty(temp.getProcessInstanceId())) {
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(temp.getId(),
                        temp.getProcessInstanceId(), "delete");
                }
                commonServiceImpl.delete(temp);
            }
            List<TabTemplate> lists = findTabTemplatesByBizIdOrBizVersion(bizId,null);
            if (lists.size() > 0) {
                TabTemplate tabTemplate = lists.get(0);
                tabTemplate.setAvaliable(PlanGeneralConstants.TABCBTEMPLATE_USED);
                commonServiceImpl.update(tabTemplate);
            }
        }
    }

    private void revise(String tabId, String mode) {
        TabTemplate info =  findBusinessObjectById(TabTemplate.class, tabId);
        reviseBusinessObject(info, mode);
        if (mode.equals(LifeCycleConstant.ReviseModel.MAJOR)) {
            TabTemplate tabTemplate = findBusinessObjectHistoryByBizId(TabTemplate.class, info.getBizId()).get(0);
            moveBusinessObjectByOrder(tabTemplate, 0);
        }
    }

    private void forwardProjTemplate(String processInstanceId, TabTemplate t, String bizCurrent, TSUserDto userDto) {
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
                    "/ids-pm-web/tabTemplateController.do?goTebTemplateUpdate&dataHeight=610&dataWidth=1200&id=" + t.getId());
                variables.put("viewUrl",
                    "/ids-pm-web/tabTemplateController.do?goVersionDetailHistory&dataHeight=610&dataWidth=1200&tabId=" + t.getId());
                variables.put("oid", BpmnConstants.OID_PROJECTTEMPLATE + t.getId());
                variables.put("taskNumber", t.getId());
                variables.put("flowStatus", bizCurrent);
                workFlowFacade.getWorkFlowCommonService().setRunVariables(processInstanceId, variables);
            }
            // 获取流程显示名称
            ProcessDefinition processDefinition = null;
            try{
                processDefinition = workFlowFacade.getWorkFlowCommonService().getProcessDefinitionByProcInstId(
                    processInstanceId, false);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(!CommonUtil.isEmpty(processDefinition)) {
                String bpmnDisplayName = processDefinition.getName();
                myStartedTask.setObjectName(t.getName());
                myStartedTask.setTaskNumber(t.getId());
                myStartedTask.setTitle(
                    ProcessUtil.getProcessTitle(t.getName(), bpmnDisplayName, processInstanceId));
                workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
            }
        }
    }

    private List<TabTemplate> findTabTemplatesByBizIdOrBizVersion(String bizId, String bizversion) {
        StringBuilder sb = new StringBuilder("from TabTemplate t where 1=1");
        if (StringUtils.isNotBlank(bizId)) {
            sb.append(" and t.bizId = '" + bizId + "'");
        }
        if (StringUtils.isNotBlank(bizversion)) {
            sb.append(" and t.bizVersion like '%" + bizversion + "%'");
        }
        sb.append(" order by t.createTime desc");
        List<TabTemplate> tabTemplateList = sessionFacade.findHql(sb.toString());
        return tabTemplateList;
    }

    private void submitProjectTemplateFlow(TabTemplate template, Map<String, Object> variables, String processDefinitionKey, TSUserDto curUser, String orgId) {
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
            BpmnConstants.BPMN_TABTEMPLATE_APPLY_DISPLAYNAME, procInstId));

        myStartedTask.setObjectName(template.getName());

        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

        template.setProcessInstanceId(procInstId);
        template.setBizCurrent(BpmnConstants.PROJTEMPLATE_SHENHE);
        CommonInitUtil.initGLObjectForUpdate(template,curUser,orgId);
        commonServiceImpl.updateEntitie(template);
    }

    public void submitProjectTemplateFlowAagin(TabTemplate template, TSUserDto curUser, String orgId) {
        String creator = curUser.getUserName();
        FeignJson fj = workFlowFacade.getWorkFlowTodoTaskService().getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
            template.getProcessInstanceId(), creator);
        String taskId = String.valueOf(fj.getObj());
        // 判断当前用户是否有流程（对应项目的启动、暂停、恢复、关闭流程），如有：重新提交启动流程
        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                taskId);

            Map<String, Object> params = new HashMap<>();
            params.put("variables", variables);
            params.put("userId", curUser.getId());

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
            commonServiceImpl.updateEntitie(template);
        }
    }

    @Override
    public <T> List<T> getVersionHistory(String bizId, Integer pageSize, Integer pageNum) {
        List<T> list = new ArrayList<T>();
        StringBuilder hql = new StringBuilder("");
        hql.append(" from TabTemplate t where t.bizId=? order by t.createTime desc");
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
    public List<TabTemplateDto> setStatusByLifeCycleStatus(List<TabTemplateDto> tabTemplateDtoList) {
        //转换生命周期状态
        Map<String, String> statusMap = new HashMap<String, String>();
        List<LifeCycleStatus> lifeCycleStatuses = getLifeCycleStatusList();
        for(LifeCycleStatus status : lifeCycleStatuses){
            statusMap.put(status.getName(), status.getTitle());
        }
        if(!CommonUtil.isEmpty(tabTemplateDtoList)){
            for(TabTemplateDto templateDto : tabTemplateDtoList){
                templateDto.setBizCurrent(statusMap.get(templateDto.getBizCurrent()));
            }
        }
        return tabTemplateDtoList;
    }

    @Override
    public long getVersionCount(String bizId) {
        StringBuilder hql = new StringBuilder("");
        hql.append(" select count(*) from TabTemplate t  where t.bizId=?");
        String[] params = {bizId};
        return sessionFacade.getCount(hql.toString(), params);
    }

    public List<LifeCycleStatus> getLifeCycleStatusList() {
        TabTemplate tabTemplate = new TabTemplate();
        initBusinessObject(tabTemplate);
        List<LifeCycleStatus> lifeCycleStatus = tabTemplate.getPolicy().getLifeCycleStatusList();
        return lifeCycleStatus;
    }
}
