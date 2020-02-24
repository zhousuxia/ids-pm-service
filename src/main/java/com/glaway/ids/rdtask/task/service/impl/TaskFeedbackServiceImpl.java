package com.glaway.ids.rdtask.task.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.DateUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.dto.outwardextension.OutwardExtensionDto;
import com.glaway.foundation.fdk.dev.dto.outwardextension.OutwardExtensionUrlDto;
import com.glaway.foundation.fdk.dev.service.FeignOutwardExtensionService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.common.constant.PluginConstants;
import com.glaway.ids.common.service.PluginValidateServiceI;
import com.glaway.ids.common.service.ResourceEverydayuseServiceI;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.NameStandardConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.constant.TaskFeedbackConstants;
import com.glaway.ids.message.notice.service.PlanFinishMsgNoticeI;
import com.glaway.ids.models.HttpClientUtil;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PlanLog;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.entity.TaskFeedback;
import com.glaway.ids.project.plan.service.PlanLogServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.plan.service.RdFlowTaskFlowResolveRemoteFeignServiceI;
import com.glaway.ids.rdtask.task.form.TaskFeedbackInfo;
import com.glaway.ids.rdtask.task.pbmn.activity.entity.TaskActityInfo;
import com.glaway.ids.rdtask.task.service.TaskFeedbackServiceI;
import com.glaway.ids.util.ProcessUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: TaskFeedbackServiceImpl
 * @Date: 2019/8/8-13:59
 * @since
 */
@Service("taskFeedbackService")
@Transactional
public class TaskFeedbackServiceImpl extends CommonServiceImpl implements TaskFeedbackServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(TaskFeedbackServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private FeignUserService userService;

    @Autowired
    private PlanLogServiceI planLogService;


    @Autowired
    private PlanServiceI planService;

    @Autowired
    private PluginValidateServiceI pluginValidateService;

    @Autowired
    private RdFlowTaskFlowResolveRemoteFeignServiceI rdFlowTaskFlowResolveFeignService;

    @Autowired
    private WorkFlowFacade workFlowFacade;

    @Autowired
    private FeignOutwardExtensionService outwardExtensionService;

    @Autowired
    private ResourceEverydayuseServiceI resourceEverydayuseService;

    @Autowired
    private Environment env;

    @Autowired
    private PlanFinishMsgNoticeI planFinishMsgNotice;

    /**
     * 接口
     */
    @Value(value="${spring.application.name}")
    private String appKey;

    @Override
    public List<TaskFeedback> findTaskFeedbackList(TaskFeedbackInfo taskFeedbackInfo, int page,
                                                   int rows, boolean isPage) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from TaskFeedback t");
        Map<String, Object> resultMap = getQueryParam(hql, taskFeedbackInfo);
        String hqlStr = (String)resultMap.get("hql");
        List<TaskFeedback> paramList = (List<TaskFeedback>)resultMap.get("queryList");
        List<TaskFeedback> list = new ArrayList<TaskFeedback>();
        if (isPage) {
            list = pageList(hqlStr, paramList.toArray(), (page - 1) * rows, rows);
        }
        else {
            list = executeQuery(hqlStr, paramList.toArray());
        }
        return list;
    }

    /**
     * Description: 组装查询方法
     *
     * @param hql
     * @return
     * @see
     */
    private Map<String, Object> getQueryParam(StringBuilder hql, TaskFeedbackInfo taskFeedbackInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        hql.append(" where t.avaliable!=0");

        List<Object> paramList = new ArrayList<Object>();
        if (taskFeedbackInfo != null) {
            if (StringUtils.isNotEmpty(taskFeedbackInfo.getTaskId())) {
                hql.append(" and t.taskId = ?");
                paramList.add(taskFeedbackInfo.getTaskId());
            }
        }
        hql.append(" order by t.createTime desc");
        resultMap.put("hql", hql.toString());
        resultMap.put("queryList", paramList);
        return resultMap;
    }

    /**
     * Description: <br>
     * 保存进度记录<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param taskFeedback
     * @see
     */
    private void savePlanLogByRate(TaskFeedback taskFeedback,TSUserDto currentUser)
            throws GWException {
        PlanLog planLog = new PlanLog();
        planLog.setPlanId(taskFeedback.getTaskId());
        planLog.setLogInfo(TaskFeedbackConstants.TASK_FEEDBACK_RATE_INFO
                + taskFeedback.getProgressRate()
                + TaskFeedbackConstants.TASK_FEEDBACK_RATE_SUFFIX);
        planLog.setRemark(taskFeedback.getProgressRateRemark());
        if(!CommonUtil.isEmpty(taskFeedback.getFilePathP())){
            planLog.setFilePath(taskFeedback.getFilePathP());
        }
        planLog.setCreateBy(currentUser.getId());
        planLog.setCreateName(currentUser.getUserName());
        planLog.setCreateFullName(currentUser.getRealName());
        planLog.setCreateTime(new Date());
        planLogService.save(planLog);

        // 通过任务编号获得计划数据
        Plan plan = planService.findBusinessObjectById(Plan.class, taskFeedback.getTaskId());
        // 把计划的完成进度变更
        plan.setFeedbackRateBefore(plan.getProgressRate());
        plan.setProgressRate(taskFeedback.getProgressRate());
    }

    @Override
    public FeignJson saveTaskFeedback(Map<String,Object> map) {
        TaskFeedbackInfo taskFeedbackInfo = new ObjectMapper().convertValue(map.get("taskFeedbackInfo"),TaskFeedbackInfo.class);
        String checkP = map.get("checkP") == null ? "" : map.get("checkP").toString();
        String userId = map.get("userId") == null ? "" : map.get("userId").toString();
        String taskId = map.get("taskId") == null ? "" : map.get("taskId").toString();
        String progressRate = map.get("progressRate") == null ? "" : map.get("progressRate").toString();
        String progressRateRemark = map.get("progressRateRemark") == null ? "" : map.get("progressRateRemark").toString();
        Object[] arguments = new String[] {I18nUtil.getValue("com.glaway.ids.common.msg.create"),
                I18nUtil.getValue("com.glaway.ids.pm.rdtask.task.taskfeedbackinfo")};
        String message = I18nUtil.getValue("com.glaway.ids.common.search.success", arguments);
        FeignJson j = new FeignJson();
        try {
            if (StringUtils.isNotEmpty(taskId)) {
                taskFeedbackInfo.setTaskId(taskId);
            }
            if (StringUtils.isNotEmpty(checkP) && "on".equals(checkP)) {
                if (StringUtils.isNotEmpty(progressRate)) {
                    progressRate = StringUtil.repairNum(progressRate);
                    taskFeedbackInfo.setProgressRate(progressRate);
                }
                else {
                    taskFeedbackInfo.setProgressRate(TaskFeedbackConstants.TASK_FEEDBACK_RATE_FIRST);
                }

                if (StringUtils.isNotEmpty(progressRateRemark)) {
                    taskFeedbackInfo.setProgressRateRemark(progressRateRemark);
                }
            }
            saveTaskFeedback(taskFeedbackInfo, checkP , userId , "plan");

            Plan planOld = planService.getEntity(taskFeedbackInfo.getTaskId());
            String taskTitle = planOld.getId();
            // 如果进度100就发起流程
            if (taskFeedbackInfo.getProgressRate().equals("100.00")) {
                taskTitle += ",doSubmit";
            }
            j.setObj(taskTitle);
        }
        catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            log.error(e.getMessage());
            throw new GWException(e.getMessage());
        }
        finally {
            // systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public void saveTaskFeedback(TaskFeedbackInfo taskFeedbackInfo, String checkP , String currentId , String type)
            throws GWException {
        // 获取所有人员
        Map<String, TSUserDto> userMap = userService.getCommonPrepUserAllByUUid();
        TSUserDto currentUser = userMap.get(currentId);
        TaskFeedback taskFeedback = new TaskFeedback();
        try {
            PropertyUtils.copyProperties(taskFeedback, taskFeedbackInfo);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.warn(e.getMessage());
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.dataTransferFailure"));
        }
        Boolean saveFlag = judgeSavePlanLogBytaskId(taskFeedback, checkP,currentUser);
        if (saveFlag) {
            save(taskFeedback);
            if(!CommonUtil.isEmpty(type)){
                //同步任务的流程数据：
                try{
                    //获取rdflow插件状态
                    boolean rdflowPluginValid = pluginValidateService.isValidatePlugin(PluginConstants.RDFLOW_PLUGIN_NAME);
                    if(rdflowPluginValid){
                        rdFlowTaskFlowResolveFeignService.saveTaskFeedbackByPlanInfo(taskFeedbackInfo.getTaskId(),taskFeedbackInfo.getProgressRate(), taskFeedbackInfo.getProgressRateRemark(), taskFeedbackInfo.getFilePathP() ,checkP ,currentId,"");
                    }

                }catch(Exception e){

                }

            }
        }
        taskFeedbackInfo.setId(taskFeedback.getId());
    }

    @Override
    public void notify(String id, Map<String, Object> map) {
        String userId = (String)map.get("userId");
        TSUserDto user = userService.getUserByUserId(userId);
        Plan plan = planService.getEntity(id);
        String logInfo = "";
        String flowStaus = "";
        String allFinishMsg = "";
        String leader = (String)map.get("leader");
        String procInstId = (String)map.get("procInstId");
        //提交完工反馈
        if (PlanConstants.PLAN_ORDERED.equals(plan.getBizCurrent())) {
            logInfo = PlanConstants.PLAN_LOGINFO_FEEDBACK;
            flowStaus = PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING;
            plan.setBizCurrent(PlanConstants.PLAN_FEEDBACKING);
            plan.setFeedbackProcInstId(procInstId);
            plan.setFlowStatus(flowStaus);
            plan.setProgressRate(planService.getProgress(plan));
            sessionFacade.saveOrUpdate(plan);

            PlanLog planLog = new PlanLog();
            planLog.setPlanId(plan.getId());
            planLog.setLogInfo(logInfo);


            planLog.setCreateBy(user.getId());
            planLog.setCreateName(user.getUserName());
            planLog.setCreateFullName(user.getRealName());
            planLog.setCreateTime(new Date());

            sessionFacade.save(planLog);


            if(user.getUserName().equals(leader)){
                allFinishMsg = id;
                logInfo = PlanConstants.PLAN_LOGINFO_FINISH;
                flowStaus = PlanConstants.PLAN_FLOWSTATUS_NORMAL;
                plan.setBizCurrent(PlanConstants.PLAN_FINISH);
                procInstId = "";
                plan.setFeedbackProcInstId(procInstId);
                plan.setFlowStatus(flowStaus);
                plan.setActualEndTime(new Date());
                Plan p = (Plan)plan.clone();
                plan.setProgressRate(planService.getProgress(p));

                boolean needHttp = false;
                if(!CommonUtil.isEmpty(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(plan.getTaskNameType())){
                    needHttp = true;
                }
                if(needHttp){
                    List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"flowActiveCategoryHttpServer");
                    if (!CommonUtil.isEmpty(outwardExtensionList)) {
                        for (OutwardExtensionDto ext : outwardExtensionList) {
                            if (!CommonUtil.isEmpty(ext.getUrlList())) {
                                List<String> urls = new ArrayList<String>();
                                for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                                    if ("finishAllTaskStep".equals(out.getOperateCode())) {
                                        urls.add(out.getOperateUrl() + "&planId=" + plan.getId());
                                    }
                                }
                                if(!CommonUtil.isEmpty(urls)){
                                    for(String operateUrl : urls){
                                        try {
                                            HttpClientUtil.httpClientPostByTest(operateUrl, null);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                sessionFacade.saveOrUpdate(plan);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time1 = sdf.format(plan.getPlanEndTime());
                String time2 = sdf.format(plan.getActualEndTime());
                long endTime = DateUtil.stringtoDate(time1, DateUtil.LONG_DATE_FORMAT).getTime();
                long actualTime = DateUtil.stringtoDate(time2, DateUtil.LONG_DATE_FORMAT).getTime();
                if ((actualTime < endTime)) {
                    resourceEverydayuseService.delResourceUseByPlanAndOperationTime(plan.getId(),
                            plan.getActualEndTime());
                }
                PlanLog planLog1 = new PlanLog();
                planLog1.setPlanId(plan.getId());
                planLog1.setLogInfo(logInfo);


                planLog1.setCreateBy(user.getId());
                planLog1.setCreateName(user.getUserName());
                planLog1.setCreateFullName(user.getRealName());
                planLog1.setCreateTime(new Date());

                sessionFacade.save(planLog1);
            }

            List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where preposePlanId = ?",plan.getId());  //查询所有前置计划为当前计划的计划列表
            List<Plan> planList = new ArrayList<>();
            if(!CommonUtil.isEmpty(preposePlanList)){
                for(PreposePlan prepose : preposePlanList){
                    Plan pl = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPlanId());
                    planList.add(pl);
                }
                if(!CommonUtil.isEmpty(planList)){
                    for(Plan pl : planList){
                        if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pl.getBizCurrent())){
                            planService.updateBizCurrent(pl);
                            //更新进度
                            planService.updateProgressRate(pl);
                        }
                    }
                    sessionFacade.batchUpdate(planList);

                    for(Plan pla : planList){
                        if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pla.getBizCurrent())){
                            if(CommonUtil.isEmpty(pla.getPlanReceivedProcInstId())){
                                planService.startPlanReceivedProcess(pla,userId);
                            }else if(!CommonUtil.isEmpty(pla.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(pla.getPlanReceivedCompleteTime())){
                                planService.startPlanReceivedProcess(pla,userId);
                            }

                        }
                    }
                }
            }


        }//完工反馈审批通过
        else if (PlanConstants.PLAN_FEEDBACKING.equals(plan.getBizCurrent())) {
            allFinishMsg = id;
            logInfo = PlanConstants.PLAN_LOGINFO_FINISH;
            flowStaus = PlanConstants.PLAN_FLOWSTATUS_NORMAL;
            plan.setBizCurrent(PlanConstants.PLAN_FINISH);
            plan.setFeedbackProcInstId(procInstId);
            plan.setBizCurrent(PlanConstants.PLAN_FINISH);
            plan.setFlowStatus(flowStaus);
            plan.setActualEndTime(new Date());
            plan.setProgressRate(planService.getProgress(plan));

            boolean needHttp = false;
            if(!CommonUtil.isEmpty(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(plan.getTaskNameType())){
                needHttp = true;
            }
            if(needHttp){
                List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"flowActiveCategoryHttpServer");
                if (!CommonUtil.isEmpty(outwardExtensionList)) {
                    for (OutwardExtensionDto ext : outwardExtensionList) {
                        if (!CommonUtil.isEmpty(ext.getUrlList())) {
                            List<String> urls = new ArrayList<String>();
                            for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                                if ("finishAllTaskStep".equals(out.getOperateCode())) {
                                    urls.add(out.getOperateUrl() + "&planId=" + plan.getId());
                                }
                            }
                            if(!CommonUtil.isEmpty(urls)){
                                for(String operateUrl : urls){
                                    try {
                                        HttpClientUtil.httpClientPostByTest(operateUrl, null);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                }
            }

            sessionFacade.saveOrUpdate(plan);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time1 = sdf.format(plan.getPlanEndTime());
            String time2 = sdf.format(plan.getActualEndTime());
            long endTime = DateUtil.stringtoDate(time1, DateUtil.LONG_DATE_FORMAT).getTime();
            long actualTime = DateUtil.stringtoDate(time2, DateUtil.LONG_DATE_FORMAT).getTime();
            if ((actualTime < endTime)) {
                resourceEverydayuseService.delResourceUseByPlanAndOperationTime(plan.getId(),
                        plan.getActualEndTime());
            }

            PlanLog planLog = new PlanLog();
            planLog.setPlanId(plan.getId());
            planLog.setLogInfo(logInfo);


            planLog.setCreateBy(user.getId());
            planLog.setCreateName(user.getUserName());
            planLog.setCreateFullName(user.getRealName());
            planLog.setCreateTime(new Date());

           sessionFacade.save(planLog);

            List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where preposePlanId = ?",plan.getId());  //查询所有前置计划为当前计划的计划列表
            List<Plan> planList = new ArrayList<>();
            if(!CommonUtil.isEmpty(preposePlanList)){
                for(PreposePlan prepose : preposePlanList){
                    Plan pl = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPlanId());
                    planList.add(pl);
                }
            }

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pl : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pl.getBizCurrent())){
                        planService.updateBizCurrent(pl);
                        //更新进度
                        planService.updateProgressRate(pl);
                    }
                }
            }
            sessionFacade.batchUpdate(planList);

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pla : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pla.getBizCurrent())){
                        if(CommonUtil.isEmpty(pla.getPlanReceivedProcInstId())){
                            planService.startPlanReceivedProcess(pla,userId);
                        }else if(!CommonUtil.isEmpty(pla.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(pla.getPlanReceivedCompleteTime())){
                            planService.startPlanReceivedProcess(pla,userId);
                        }

                    }
                }
            }
        }

        if(StringUtils.isNotEmpty(allFinishMsg) && plan.getBizCurrent().equals(PlanConstants.PLAN_FINISH)){
            try {
                planFinishMsgNotice.getAllMessage(allFinishMsg,user);
            }
            catch (Exception e) {
            }
        }
       /* try{
            boolean rdflowPluginValid = pluginValidateService.isValidatePlugin(PluginConstants.RDFLOW_PLUGIN_NAME);
            if(rdflowPluginValid){
                String linkFlag = "false";
                FeignJson linkFlagFj = rdFlowTaskFlowResolveFeignService.isHaveLinkPlanId(id);
                if (linkFlagFj.isSuccess()) {
                    linkFlag = linkFlagFj.getObj() == null ? "false" : linkFlagFj.getObj().toString();
                }
                if (linkFlag.equals("true")) {
                    rdFlowTaskFlowResolveFeignService.saveRdTaskFeedBackInfoByPlanTaskFeedBackInfo(id,procInstId,plan.getBizCurrent(),user,leader);
                }
            }

        }catch(Exception e){

        }*/


    }

    @Override
    public void updateForFeedBack(String id, Map<String, Object> map) {
        String userId = (String)map.get("userId");
        TSUserDto user = userService.getUserByUserId(userId);
        Plan plan = planService.getEntity(id);
        String logInfo = "";
        String flowStaus = "";
        String allFinishMsg = "";
        String leader = (String)map.get("leader");
        String procInstId = (String)map.get("procInstId");
        //提交完工反馈
        if (PlanConstants.PLAN_ORDERED.equals(plan.getBizCurrent())) {
            logInfo = PlanConstants.PLAN_LOGINFO_FEEDBACK;
            flowStaus = PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING;
            plan.setBizCurrent(PlanConstants.PLAN_FEEDBACKING);
            plan.setFeedbackProcInstId(procInstId);
            plan.setFlowStatus(flowStaus);
            plan.setProgressRate(planService.getProgress(plan));
            sessionFacade.updateEntitie(plan);

            if(user.getUserName().equals(leader)){
                allFinishMsg = id;
                logInfo = PlanConstants.PLAN_LOGINFO_FINISH;
                flowStaus = PlanConstants.PLAN_FLOWSTATUS_NORMAL;
                plan.setBizCurrent(PlanConstants.PLAN_FINISH);
                procInstId = "";
                plan.setFeedbackProcInstId(procInstId);
                plan.setFlowStatus(flowStaus);
                plan.setActualEndTime(new Date());
                plan.setProgressRate(planService.getProgress(plan));

                boolean needHttp = false;
                if(!CommonUtil.isEmpty(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(plan.getTaskNameType())){
                    needHttp = true;
                }
                if(needHttp){
                    List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"flowActiveCategoryHttpServer");
                    if (!CommonUtil.isEmpty(outwardExtensionList)) {
                        for (OutwardExtensionDto ext : outwardExtensionList) {
                            if (!CommonUtil.isEmpty(ext.getUrlList())) {
                                List<String> urls = new ArrayList<String>();
                                for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                                    if ("finishAllTaskStep".equals(out.getOperateCode())) {
                                        urls.add(out.getOperateUrl() + "&planId=" + plan.getId());
                                    }
                                }
                                if(!CommonUtil.isEmpty(urls)){
                                    for(String operateUrl : urls){
                                        try {
                                            HttpClientUtil.httpClientPostByTest(operateUrl, null);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                sessionFacade.saveOrUpdate(plan);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time1 = sdf.format(plan.getPlanEndTime());
                String time2 = sdf.format(plan.getActualEndTime());
                long endTime = DateUtil.stringtoDate(time1, DateUtil.LONG_DATE_FORMAT).getTime();
                long actualTime = DateUtil.stringtoDate(time2, DateUtil.LONG_DATE_FORMAT).getTime();
                if ((actualTime < endTime)) {
                    resourceEverydayuseService.delResourceUseByPlanAndOperationTime(plan.getId(),
                            plan.getActualEndTime());
                }
            }

            List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where preposePlanId = ?",plan.getId());  //查询所有前置计划为当前计划的计划列表
            List<Plan> planList = new ArrayList<>();
            if(!CommonUtil.isEmpty(preposePlanList)){
                for(PreposePlan prepose : preposePlanList){
                    Plan pl = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPlanId());
                    planList.add(pl);
                }
            }

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pl : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pl.getBizCurrent())){
                        planService.updateBizCurrent(pl);
                        //更新进度
                        planService.updateProgressRate(pl);
                    }
                }
            }
            sessionFacade.batchUpdate(planList);

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pla : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pla.getBizCurrent())){
                        if(CommonUtil.isEmpty(pla.getPlanReceivedProcInstId())){
                            planService.startPlanReceivedProcess(pla,userId);
                        }else if(!CommonUtil.isEmpty(pla.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(pla.getPlanReceivedCompleteTime())){
                            planService.startPlanReceivedProcess(pla,userId);
                        }

                    }
                }
            }

        }//完工反馈审批通过
        else if (PlanConstants.PLAN_FEEDBACKING.equals(plan.getBizCurrent())) {
            allFinishMsg = id;
            logInfo = PlanConstants.PLAN_LOGINFO_FINISH;
            flowStaus = PlanConstants.PLAN_FLOWSTATUS_NORMAL;
            plan.setBizCurrent(PlanConstants.PLAN_FINISH);
            plan.setFeedbackProcInstId(procInstId);
            plan.setBizCurrent(PlanConstants.PLAN_FINISH);
            plan.setFlowStatus(flowStaus);
            plan.setActualEndTime(new Date());
            plan.setProgressRate(planService.getProgress(plan));

            boolean needHttp = false;
            if(!CommonUtil.isEmpty(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(plan.getTaskNameType())){
                needHttp = true;
            }
            if(needHttp){
                List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"flowActiveCategoryHttpServer");
                if (!CommonUtil.isEmpty(outwardExtensionList)) {
                    for (OutwardExtensionDto ext : outwardExtensionList) {
                        if (!CommonUtil.isEmpty(ext.getUrlList())) {
                            List<String> urls = new ArrayList<String>();
                            for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                                if ("finishAllTaskStep".equals(out.getOperateCode())) {
                                    urls.add(out.getOperateUrl() + "&planId=" + plan.getId());
                                }
                            }
                            if(!CommonUtil.isEmpty(urls)){
                                for(String operateUrl : urls){
                                    try {
                                        HttpClientUtil.httpClientPostByTest(operateUrl, null);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                }
            }

            sessionFacade.saveOrUpdate(plan);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time1 = sdf.format(plan.getPlanEndTime());
            String time2 = sdf.format(plan.getActualEndTime());
            long endTime = DateUtil.stringtoDate(time1, DateUtil.LONG_DATE_FORMAT).getTime();
            long actualTime = DateUtil.stringtoDate(time2, DateUtil.LONG_DATE_FORMAT).getTime();
            if ((actualTime < endTime)) {
                resourceEverydayuseService.delResourceUseByPlanAndOperationTime(plan.getId(),
                        plan.getActualEndTime());
            }

            List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where preposePlanId = ?",plan.getId());  //查询所有前置计划为当前计划的计划列表
            List<Plan> planList = new ArrayList<>();
            if(!CommonUtil.isEmpty(preposePlanList)){
                for(PreposePlan prepose : preposePlanList){
                    Plan pl = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPlanId());
                    planList.add(pl);
                }
            }

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pl : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pl.getBizCurrent())){
                        planService.updateBizCurrent(pl);
                        //更新进度
                        planService.updateProgressRate(pl);
                    }
                }
            }
            sessionFacade.batchUpdate(planList);

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pla : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pla.getBizCurrent())){
                        if(CommonUtil.isEmpty(pla.getPlanReceivedProcInstId())){
                            planService.startPlanReceivedProcess(pla,userId);
                        }else if(!CommonUtil.isEmpty(pla.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(pla.getPlanReceivedCompleteTime())){
                            planService.startPlanReceivedProcess(pla,userId);
                        }

                    }
                }
            }
        }
        try{
            boolean rdflowPluginValid = pluginValidateService.isValidatePlugin(PluginConstants.RDFLOW_PLUGIN_NAME);
            if(rdflowPluginValid){
                String linkFlag = "false";
                FeignJson linkFlagFj = rdFlowTaskFlowResolveFeignService.isHaveLinkPlanId(id);
                if (linkFlagFj.isSuccess()) {
                    linkFlag = linkFlagFj.getObj() == null ? "false" : linkFlagFj.getObj().toString();
                }
                if (linkFlag.equals("true")) {
                    rdFlowTaskFlowResolveFeignService.saveRdTaskFeedBackInfoByPlanTaskFeedBackInfo(id,procInstId,plan.getBizCurrent(),user,leader);
                }
            }

        }catch(Exception e){

        }
    }

    @Override
    public void goBackNotify(String id, Map<String, Object> map) {
        Plan plan = planService.getEntity(id);
        plan.setProgressRate(plan.getFeedbackRateBefore());
        planService.updateEntity(plan);
        planService.backwardBusinessObject(plan);

        PlanLog planLog = new PlanLog();
        planLog.setPlanId(plan.getId());
        planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_FEEDBACK_GOBACK);

        String userId = (String)map.get("userId");
        TSUserDto user = userService.getUserByUserId(userId);
        planLog.setCreateBy(user.getId());
        planLog.setCreateName(user.getUserName());
        planLog.setCreateFullName(user.getRealName());
        planLog.setCreateTime(new Date());

        sessionFacade.save(planLog);

        try{
            boolean rdflowPluginValid = pluginValidateService.isValidatePlugin(PluginConstants.RDFLOW_PLUGIN_NAME);
            if(rdflowPluginValid){
                //数据同步到任务：
                String linkFlag = "false";
                FeignJson linkFlagFj = rdFlowTaskFlowResolveFeignService.isHaveLinkPlanId(id);
                if (linkFlagFj.isSuccess()) {
                    linkFlag = linkFlagFj.getObj() == null ? "false" : linkFlagFj.getObj().toString();
                }
                if(linkFlag.equals("true")){
                    rdFlowTaskFlowResolveFeignService.saveRdTaskFeedBackInfoByPlanTaskFeedBackGoBackInfo(id,user.getId());
                }
            }
        }catch(Exception e){

        }

    }

    /**
     * Description: <br>
     * 保存反馈之后入日志的逻辑判断<br>
     * Implement: <br>
     * 开始时，如果风险为无，反馈为0.00不保存日志。如果数据库中有值时，如果值不一致，才需要保存日志记录<br>
     *
     * @param taskFeedback
     * @return 是否保存反馈记录
     * @throws GWException
     * @see
     */
    private Boolean judgeSavePlanLogBytaskId(TaskFeedback taskFeedback, String checkP, TSUserDto currentUser)
            throws GWException {
        Boolean saveFlag = false;
        if (StringUtils.isEmpty(taskFeedback.getTaskId())) {
            log.warn(I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.taskNumberEmptySaveFailure"));
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.rdtask.flowResolve.taskNumberEmptySaveFailure"));
        }

        TaskFeedbackInfo taskFeedbackInfo = new TaskFeedbackInfo();
        taskFeedbackInfo.setTaskId(taskFeedback.getTaskId());
        List<TaskFeedback> taskFeedbackList = findTaskFeedbackList(taskFeedbackInfo, 1, 10, false);

        // 如果数据库没有值
        if (CommonUtil.isEmpty(taskFeedbackList) || taskFeedbackList.size() < 1) {

            // 如果反馈不为0.00，保存
            if ("on".equals(checkP)) {
                savePlanLogByRate(taskFeedback,currentUser);
                saveFlag = true;
            }
        }
        else { // 如果数据库有值
            // 如果风险等级与风险描述与数据库的不一致，则记录
            TaskFeedback taskFeedbackDb = taskFeedbackList.get(0);

            // 如果已完成进度与进度描述与数据库的不一致，则记录
            String progressRate = TaskFeedbackConstants.TASK_FEEDBACK_RATE_FIRST;
            String progressRateRemark = "";
            if (StringUtils.isNotEmpty(taskFeedbackDb.getProgressRate())) {
                progressRate = taskFeedbackDb.getProgressRate();
            }
            if (StringUtils.isNotEmpty(taskFeedbackDb.getProgressRateRemark())) {
                progressRateRemark = taskFeedbackDb.getProgressRateRemark();
            }
            if ((!taskFeedback.getProgressRate().equals(progressRate) || !taskFeedback.getProgressRateRemark().equals(
                    progressRateRemark)||!taskFeedback.getFilePathP().equals(taskFeedbackDb.getFilePath()))
                    && "on".equals(checkP)) {
                savePlanLogByRate(taskFeedback,currentUser);
                saveFlag = true;
            }
        }

        // 如果反馈进度为100.00.自动发起流程（未做）
        if ("100.00".equals(taskFeedback.getProgressRate())) {

        }

        return saveFlag;
    }

    /**
     * 任务完工反馈,提交审批
     * Description: <br>
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param plan
     * @param leader
     * @see
     */
    @Override
    public void taskApprove(Plan plan, String leader, String userId) {

        TSUserDto actor = userService.getUserByUserId(userId);
        actor.setId(actor.getId());
        actor.setUserName(actor.getUserName());
        actor.setRealName(actor.getRealName());

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("assigner", actor.getUserName());

        variables.put("cancelEventListener",
                "com.glaway.foundation.activiti.task.common.CommonCancelListener");

        variables.put("leader", leader);
        variables.put("editUrl",
                "/ids-pm-web/taskDetailController.do?goUpdateFlow&dataHeight=650&dataWidth=800&taskNumber="
                        + plan.getId());
        variables.put(
                "viewUrl",
                "/ids-pm-web/planController.do?goCheck&dataHeight=550&dataWidth=800&id="
                        + plan.getId());
        variables.put("oid", BpmnConstants.OID_PLAN + plan.getId());
        variables.put("userId",userId);

        //流程变量添加监听
        variables.put("taskFeedbackUrl","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/taskFeedbackListenerRestController/notify.do");
        variables.put("gobackUrl","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/taskFeedbackGoBackRestController/notify.do");


        String procInstId = "";

        // 老流程
        if (plan.getFeedbackProcInstId() != null && !"".equals(plan.getFeedbackProcInstId())) {
            procInstId = plan.getFeedbackProcInstId();
            // 将procInstId存放到approvePlanForm中
            List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    procInstId, actor.getUserName());
            String taskId = tasks.get(0).getId();
            // 如果是从首页我的任务重新提交审批,则使用原有的参数项
            if (leader == null) {
                Map<String, Object> oldVariables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                        taskId);
                variables = oldVariables;
            }
            variables.put("procInstId",procInstId);
            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            // 如果审批人是当前用户则直接通过审批
            if (!actor.getUserName().equals(leader)) {
                FeignJson nextTasksFj = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                        procInstId);
                String status = "";

                if (nextTasksFj.isSuccess()) {
                    status = nextTasksFj.getObj() == null ? "" : nextTasksFj.getObj().toString();
                }

                MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                        plan.getId());

                myStartedTask.setStartTime(new Date());
                myStartedTask.setStatus(status);
                workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
            }else{

                MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                        plan.getId());
                workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask.getTaskId()));
            }

        }
        else {
            // 新启流程
            FeignJson piFj = new FeignJson();
            piFj = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                    BpmnConstants.BPMN_START_TASKFEEDBACK, plan.getId(), variables);
            procInstId = ""; // 流程实例ID
            if (piFj.isSuccess()) {
                procInstId = piFj.getObj() == null ? "" : piFj.getObj().toString();
            }

            // 将procInstId存放到approvePlanForm中
            List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    procInstId, actor.getUserName());
            String taskId = tasks.get(0).getId();
            // 执行流程
            variables.put("procInstId",procInstId);
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            // 如果审批人是当前用户则直接通过审批
            if (!actor.getUserName().equals(leader)) {
                FeignJson nextTasksFj = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                        procInstId);
                String status = "";

                if (nextTasksFj.isSuccess()) {
                    status = nextTasksFj.getObj() == null ? "" : nextTasksFj.getObj().toString();
                }

                MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
                myStartedTask.setTaskNumber(plan.getId());
                myStartedTask.setType(BpmnConstants.BPMN_START_TASKFEEDBACK);

                // 流程已发起列表中、增加对象名称的记录,流程任务名称规范化
                // 多个对象的批量审批任务，对象名称为null
                myStartedTask.setObjectName(null);
                myStartedTask.setTitle(ProcessUtil.getProcessTitle("",
                        BpmnConstants.BPMN_FEEDBACK, procInstId));

                myStartedTask.setCreater(actor.getUserName());
                myStartedTask.setCreaterFullname(actor.getRealName());
                myStartedTask.setStartTime(new Date());
                myStartedTask.setProcType(BpmnConstants.BPMN_START_TASKFEEDBACK);
                myStartedTask.setProcInstId(procInstId);
                myStartedTask.setStatus(status);
                workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);
            }else{
                //当计划完工时，判断将其作为前置计划的计划状态是否可以从待发布变为待接收
                /*List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where preposePlanId = ?",plan.getId());  //查询所有前置计划为当前计划的计划列表
                List<Plan> planList = new ArrayList<>();
                if(!CommonUtil.isEmpty(preposePlanList)){
                    for(PreposePlan prepose : preposePlanList){
                        Plan pl = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPlanId());
                        planList.add(pl);
                    }
                }

                if(!CommonUtil.isEmpty(planList)){
                    for(Plan pl : planList){
                        if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pl.getBizCurrent())){
                            planService.updateBizCurrent(pl);
                        }
                    }
                }
                sessionFacade.batchUpdate(planList);

                if(!CommonUtil.isEmpty(planList)){
                    for(Plan pla : planList){
                        if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pla.getBizCurrent())){
                            planService.startPlanReceivedProcess(pla,userId);
                        }
                    }
                }*/

            }
        }

        /*
         * plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING);
         * plan.setFeedbackProcInstId(procInstId);
         * plan.setBizCurrent(PlanConstants.PLAN_FEEDBACKING);
         * planService.update(plan);
         */
        planService.updateForFeedBack(plan.getId(),variables);

    }

    @Override
    public FeignJson doSubmitApprove(String leader, String planId, String userId) {
        TaskActityInfo info = new TaskActityInfo();
        FeignJson j = new FeignJson();

        // 通过ID获得对象
        Plan plan = planService.getEntity(planId);
        // 提交审批之前，需要记录最近的进度
        if (!TaskFeedbackConstants.TASK_FEEDBACK_RATE_VALUE.equals(plan.getProgressRate())) {
            plan.setFeedbackRateBefore(plan.getProgressRate());
        }

        // 完工反馈被驳回
        if ("ORDERED".equals(plan.getBizCurrent()) && "FEEDBACKING".equals(plan.getFlowStatus())) {
            // 通过计划找到流程中的任务的taskId
            planService.updatePlan(plan);

            j.setSuccess(true);
            String message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.task.submitsuccess");
            try {
                taskApprove(plan, leader, userId);

            }
            catch (Exception e) {
                e.printStackTrace();
                message = e.getMessage();
                j.setSuccess(false);
            }
            finally {
                planService.updatePlan(plan);
                j.setMsg(message);
                return j;
            }

        }
        else {
            String message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.task.submitsuccess");
            j.setSuccess(true);
            try {
                taskApprove(plan, leader, userId);
            }
            catch (Exception e) {
                e.printStackTrace();
                message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.task.submitfailure");
                log.info(message);
                j.setSuccess(false);
            }
            finally {
                j.setMsg(message);
                return j;
            }
        }
    }
}
