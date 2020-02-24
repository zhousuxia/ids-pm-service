package com.glaway.ids.project.plan.service.impl;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.alibaba.fastjson.JSONObject;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.core.common.model.json.AjaxJson;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plan.dto.ApprovePlanFormDto;
import com.glaway.ids.project.plan.dto.BasicLineDto;
import com.glaway.ids.project.plan.dto.BasicLinePlanDto;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.BasicLine;
import com.glaway.ids.project.plan.entity.BasicLinePlan;
import com.glaway.ids.project.plan.service.BasicLineServiceI;
import com.glaway.ids.project.plan.service.PlanFlowForworkServiceI;
import com.glaway.ids.project.plan.service.RdFlowTaskFlowResolveRemoteFeignServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.*;

/**
 * Created by LHR on 2019/8/12.
 */
@Service("basicLineService")
@Transactional(propagation = Propagation.REQUIRED)
public class BasicLineServiceImpl extends BusinessObjectServiceImpl<BasicLine> implements BasicLineServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private FeignUserService userService;

    @Autowired
    private PlanFlowForworkServiceI planFlowForworkService;

    @Autowired
    private RdFlowTaskFlowResolveRemoteFeignServiceI flowTaskService;

    @Override
    public String getLifeCycleStatusList() {
        BasicLine p = new BasicLine();
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
    public BasicLine getBasicLineEntity(String id) {
        return (BasicLine) sessionFacade.getEntity(BasicLine.class,id);
    }

    @Override
    public List<BasicLinePlan> queryBasicLinePlanList(BasicLinePlan basicLinePlan, int page, int rows, boolean isPage) {
        String hql = createBasicLinePlanHql(basicLinePlan);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }

    /**
     * 根据BasicLinePlan中的传值拼接HQL
     *
     * @param plan
     * @return
     * @see
     */
    private String createBasicLinePlanHql(BasicLinePlan plan) {
        String hql = "from BasicLinePlan l where 1 = 1";
        // 计划ID
        if (StringUtils.isNotEmpty(plan.getId())) {
            hql = hql + " and l.id = '" + plan.getId() + "'";
        }
        // 所属项目
        if (StringUtils.isNotEmpty(plan.getProjectId())) {
            hql = hql + " and l.projectId = '" + plan.getProjectId() + "'";
        }
        // 基线id
        if (StringUtils.isNotEmpty(plan.getBasicLineId())) {
            hql = hql + " and l.basicLineId = '" + plan.getBasicLineId() + "'";
        }
        // 父计划
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            hql = hql + " and l.parentPlanId = '" + plan.getParentPlanId() + "'";
        }
        // 计划名称
        if (StringUtils.isNotEmpty(plan.getPlanName())) {
            hql = hql + " and l.planName like '%" + plan.getPlanName() + "%'";
        }
        // 责任人
        if (StringUtils.isNotEmpty(plan.getOwner())) {
            hql = hql + " and l.owner = '" + plan.getOwner() + "'";
        }
        // 生命周期状态
        if (StringUtils.isNotEmpty(plan.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + plan.getBizCurrent() + "'";
        }
        if (StringUtils.isNotEmpty(plan.getAssigner())) {
            hql = hql + " and l.assigner = '" + plan.getAssigner() + "'";
        }
        // 计划等级
        if (StringUtils.isNotEmpty(plan.getPlanLevel())) {
            hql = hql + " and l.planLevel = '" + plan.getPlanLevel() + "'";
        }
        hql = hql + " order by parentPlanId, storeyno";
        return hql;
    }

    @Override
    public PageList queryEntity(List<ConditionVO> conditionList, String projectId, String userName) {
        try {
            String hql = "from BasicLine t where t.projectId = '" + projectId + "' and t.avaliable = 1";
//            if (StringUtils.isNotEmpty(userName)) {
//                hql = hql + " and (t.createByInfo.realName like '%" + userName + "%'";
//                hql = hql + " or t.createByInfo.userName like '%" + userName + "%')";
//            }

            Map<String,String> map=new HashMap<String,String>();
            //排序
            map.put("BasicLine.createTime", "asc");
            List list = sessionFacade.searchByCommonFormHql(hql, conditionList, true, map);

            //List list = sessionFacade.searchByCommonFormHql(hql, conditionList, true);
            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            PageList pageList = new PageList(count, list);
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
        return null;
    }

    @Override
    public List<JSONObject> changePlansToJSONObjects(List<BasicLinePlan> basicLineList) {

        List<JSONObject> rootList = new ArrayList<JSONObject>();

        List<String> parentPlanIds = new ArrayList<String>();
        Map<String, String> planIdMaps = new HashMap<String, String>();

        if (!CommonUtil.isEmpty(basicLineList)) {
            for (BasicLinePlan p : basicLineList) {
                planIdMaps.put(p.getId(), p.getId());
                String parentPlanId = p.getParentPlanId();
                if (!StringUtils.isEmpty(parentPlanId) && !parentPlanIds.contains(parentPlanId)) {
                    parentPlanIds.add(parentPlanId);
                }
            }
        }

        for (BasicLinePlan p : basicLineList) {
            // 若其无父计划或者其父计划不在结果集中，则将其设为root节点
            if (StringUtils.isEmpty(p.getParentPlanId())
                    || StringUtils.isEmpty(planIdMaps.get(p.getParentPlanId()))) {
                JSONObject root = new JSONObject();
                root.put("id", p.getId());
                root.put("basicLineId", p.getBasicLineId());
                root.put("planId", p.getPlanId());
                root.put("parentPlanId", p.getParentPlanId());
                root.put("displayName", p.getPlanName());
                if (parentPlanIds.contains(p.getId())) {
                    JSONObject treeNode = new JSONObject();
                    treeNode.put("value", this.generatePlanNameUrl(p));
                    treeNode.put("image", "folder.gif");
                    root.put("planName", treeNode);
                }
                else {
                    root.put("planName", this.generatePlanNameUrl(p));
                }
                root.put("planLevelInfo",
                        p.getPlanLevelInfo() == null ? "" : p.getPlanLevelInfo().getName());
                String ownerInfo = "";
                if (StringUtils.isNotEmpty(p.getOwner())) {
                    TSUserDto ownerDto = userService.getUserByUserId(p.getOwner());
                    ownerInfo = ownerDto.getRealName() + "-" +ownerDto.getUserName();
                }
                root.put("ownerInfo",ownerInfo);
                root.put("planStartTime",
                        DateUtil.dateToString(p.getPlanStartTime(), DateUtil.LONG_DATE_FORMAT));
                root.put("planEndTime",
                        DateUtil.dateToString(p.getPlanEndTime(), DateUtil.LONG_DATE_FORMAT));

                root.put("workTime", p.getWorkTime());

                root.put("bizCurrent", p.getBizCurrent());

                root.put("createTime",
                        DateUtil.dateToString(p.getCreateTime(), DateUtil.LONG_DATE_FORMAT));

                root.put("status", p.getStatus());
                List<JSONObject> rows = new ArrayList<JSONObject>();
                root.put("rows", rows);
                rootList.add(root);
            }
        }

        for (int i = 0; i < rootList.size(); i++ ) {
            this.findSubNodeByPid(parentPlanIds, basicLineList, rootList.get(i));
        }
        return rootList;
    }

    @Override
    public BasicLinePlan getBasicLinePlanEntity(String id) {
        return (BasicLinePlan) sessionFacade.getEntity(BasicLinePlan.class,id);
    }

    @Override
    public FeignJson saveBasicLine(BasicLine basicLine, String ids, String basicLineName, String remark, String type, String projectId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.saveBasicLineSuccess");
        try {

            basicLine.setAvaliable("1");
            if (StringUtils.isNotEmpty(type)) { // 当前计划与基线对比，需优化，不要存在数据库中
                basicLine.setAvaliable("0");
            }
            basicLine.setProjectId(projectId);
            basicLine.setBasicLineName(basicLineName);
            basicLine.setCreateTime(new Date());
            basicLine.setRemark(remark);
            initBusinessObject(basicLine);
            sessionFacade.saveOrUpdate(basicLine);

            planFlowForworkService.saveBasicLineForWork(ids, basicLine, projectId);
        }
        catch (Exception e) {
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.saveBasicLineFail");
            Object[] params = new Object[] {message, basicLine.getId().toString()};// 异常原因：{0}；详细信息：{1}
            throw new GWException(GWConstants.ERROR_2002, params, e);
        }
        finally {
            j.setMsg(message);;
            j.setObj(basicLine.getId());
            // systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
            return j;
        }
    }

    @Override
    public void deleteBasicLine(BasicLine o) {
        sessionFacade.delete(o);
    }

    @Override
    public FeignJson doFrozeBasicLine(String id) {
        FeignJson j = new FeignJson();
        String message = j.getMsg();
        message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.doFrozeBasicLineSuccess");
        try {

            BasicLine basicLine = (BasicLine) sessionFacade.getEntity(BasicLine.class, id);
            basicLine.setBizCurrent(PlanConstants.BASICLINE_FLOWSTATUS_FREEZING);
            sessionFacade.saveOrUpdate(basicLine);
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.doFrozeBasicLineFail");
            Object[] params = new Object[] {message, BasicLine.class.getClass() + " oids:" + id};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson doUseBasicLine(String id) {
        FeignJson j = new FeignJson();
        String message = j.getMsg();
        message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.doUseBasicLineSuccess");
        try {
            BasicLine basicLine = (BasicLine) sessionFacade.getEntity(BasicLine.class, id);
            basicLine.setBizCurrent(PlanConstants.BASICLINE_FLOWSTATUS_USEING);
            sessionFacade.saveOrUpdate(basicLine);
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.doUseBasicLineFail");
            Object[] params = new Object[] {message, BasicLine.class.getClass() + " oids:" + id};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson startBasicLine(String basicLineId, String leader, String deptLeader, String userId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.startBasicLineSuccess");
        try {
            TSUserDto actor = new TSUserDto();
            actor.setId(userId);
            actor.setUserName(userDto.getUserName());
            actor.setRealName(userDto.getRealName());

            leader = URLDecoder.decode(leader, "UTF-8");
            deptLeader = URLDecoder.decode(deptLeader, "UTF-8");

            planFlowForworkService.startBasicLineForWork(actor, leader, deptLeader, basicLineId,userId);
        }
        catch (Exception e) {
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.startBasicLineFail");
            Object[] params = new Object[] {message,
                    PlanDto.class.getClass() + " oids:" + basicLineId};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson startBasicLineFlow(BasicLine basicLine,String basicLineId, String taskId,String basicLineName,String remark) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.startBasicLineFlowSuccess");
        try {
            basicLine = (BasicLine) sessionFacade.getEntity(BasicLine.class,basicLineId);
            basicLine.setBasicLineName(basicLineName);
            basicLine.setRemark(remark);
            basicLine.setBizCurrent(PlanConstants.BASICLINE_FLOWSTATUS_VERFYING);
            sessionFacade.saveOrUpdate(basicLine);
            planFlowForworkService.startBasicLineFlowForWork(taskId, basicLineId);

        }
        catch (Exception e) {
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.basicLine.startBasicLineFlowFail");
            Object[] params = new Object[] {message, ApprovePlanFormDto.class.getClass() + " oids:"};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            // systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public String searchDatagrid(List<ConditionVO> conditionList, String projectId, String userName) {
        List<BasicLine> basicLineList = new ArrayList<>();
        PageList pageList = queryEntity(conditionList, projectId, userName);
        basicLineList = pageList.getResultList();
        long count = pageList.getCount();

        Map<String, String> taskNumberMap = new HashMap<String, String>();
        //TODO
//        List<MyStartedTaskDto> myStartedTaskList = flowTaskService.getMyStartedTasks(BpmnConstants.BPMN_START_BASICLINE);
//        for (MyStartedTaskDto task : myStartedTaskList) {
//            taskNumberMap.put(task.getTaskNumber(), task.getTaskNumber());
//        }

        Map<String, TSUserDto> allUsersMap = userService.getAllUsersMap();

        for (BasicLine line : basicLineList) {
            if (StringUtils.isNotEmpty(line.getCreateBy())){
                TSUserDto userDto = allUsersMap.get(line.getCreateName());
                line.setCreateByInfo(userDto);
            }
            if (StringUtils.isNotEmpty(taskNumberMap.get(line.getId()))) {
                line.setFlowFlag(CommonConstants.FLOW_FLAG_EXIST);
            }
            else {
                line.setFlowFlag(CommonConstants.FLOW_FLAG_NO_EXIST);
            }
        }

        List<BasicLine> newBasicLineList = new ArrayList<>();

        String json = "";
        //根据人名做筛选
        if (StringUtils.isNotEmpty(userName)) {
            basicLineList.forEach(it -> {
                if (StringUtils.isNotEmpty(it.getCreateName())){
                    TSUserDto userDto = allUsersMap.get(it.getCreateName());
                    if (userDto.getUserName().contains(userName) || userDto.getRealName().contains(userName)){
                        newBasicLineList.add(it);
                    }
                }
            });
            json = JsonUtil.getListJsonWithoutQuote(newBasicLineList);
        }else {
            json = JsonUtil.getListJsonWithoutQuote(basicLineList);
        }



        String datagridStr = "{\"rows\":" + json + ",\"total\":" + count + "}";
        return datagridStr;
    }

    @Override
    public void basicLineExcution(String basicLineId) {
        BasicLine basicLine = (BasicLine) sessionFacade.getEntity(BasicLine.class, basicLineId);
        basicLine.setBizCurrent(PlanConstants.BASICLINE_FLOWSTATUS_USEING);
        sessionFacade.saveOrUpdate(basicLine);
    }

    /**
     * 构造计划名称页面链接
     *
     * @param plan
     * @return
     * @see
     */
    private String generatePlanNameUrl(BasicLinePlan plan) {
        return "<a href='#' onclick=\"viewPlan(\'" + plan.getPlanId()
                + "\')\" style='color:blue'>" + plan.getPlanName() + "</a>";
    }

    /**
     * Description:递归查询获取所有子节点
     *
     * @param
     * @param parentObject
     * @see
     */
    private void findSubNodeByPid(List<String> parentPlanIds, List<BasicLinePlan> basicLineList,
                                  JSONObject parentObject) {
        String pid = parentObject.getString("id");
        List<JSONObject> subNodeList = new ArrayList<JSONObject>();
        for (BasicLinePlan plan : basicLineList) {
            if (pid.equals(plan.getParentPlanId())) {
                JSONObject newNode = new JSONObject();
                newNode.put("id", plan.getId());
                newNode.put("basicLineId", plan.getBasicLineId());
                newNode.put("planId", plan.getPlanId());
                newNode.put("parentPlanId", plan.getParentPlanId());
                newNode.put("displayName", plan.getPlanName());
                if (parentPlanIds.contains(plan.getId())) {
                    JSONObject treeNode = new JSONObject();
                    treeNode.put("value", this.generatePlanNameUrl(plan));
                    treeNode.put("image", "folder.gif");
                    newNode.put("planName", treeNode);
                }
                else {
                    newNode.put("planName", this.generatePlanNameUrl(plan));
                }
                newNode.put("planLevelInfo",
                        plan.getPlanLevelInfo() == null ? "" : plan.getPlanLevelInfo().getName());
                newNode.put("ownerInfo",
                        plan.getOwnerInfo() == null ? "" : plan.getOwnerInfo().getRealName() + "-"
                                + plan.getOwnerInfo().getUserName());
                newNode.put("planStartTime",
                        DateUtil.dateToString(plan.getPlanStartTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("planEndTime",
                        DateUtil.dateToString(plan.getPlanEndTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("workTime", plan.getWorkTime());

                newNode.put("bizCurrent", plan.getBizCurrent());

                newNode.put("createTime",
                        DateUtil.dateToString(plan.getCreateTime(), DateUtil.LONG_DATE_FORMAT));

                newNode.put("status", plan.getStatus());
                List<JSONObject> rows = new ArrayList<JSONObject>();
                newNode.put("rows", rows);
                subNodeList.add(newNode);
            }
        }
        if (subNodeList.size() > 0) {
            for (int i = 0; i < subNodeList.size(); i++ ) {
                List<JSONObject> rows = (List)parentObject.get("rows");
                this.findSubNodeByPid(parentPlanIds, basicLineList, subNodeList.get(i));
                JSONObject currentNode = subNodeList.get(i);
                rows.add(currentNode);
                parentObject.put("rows", rows);
            }
        }
        else {
            return;
        }
    }
}
