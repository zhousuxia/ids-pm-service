package com.glaway.ids.project.plan.service.impl;

import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.UUIDGenerator;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plan.entity.PlanExcuteInfo;
import com.glaway.ids.project.plan.service.PlanExcuteServiceI;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 〈计划执行Service〉
 * @author: wuxing
 * @ClassName: planExcuteServiceImpl
 * @Date: 2019/12/6-18:32
 * @since
 */
@Service("planExcuteServiceImpl")
@Transactional
public class PlanExcuteServiceImpl extends CommonServiceImpl implements PlanExcuteServiceI {

    /**
     * 通过计划ID和项目ID查询主数据
     * @param planId 计划ID
     * @param projId 项目ID
     * @return
     */
    @Override
    public PlanExcuteInfo queryInfoByPlanIdAndProjId(String planId, String projId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from PlanExcuteInfo p where 1=1 and p.planId=? and p.projectId=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(planId);
        paramList.add(projId);
        List<PlanExcuteInfo> planExcuteInfoList = executeQuery(hql.toString(), paramList.toArray());
        PlanExcuteInfo info = null;
        if (!CollectionUtils.isEmpty(planExcuteInfoList)){
            info = planExcuteInfoList.get(0);
        }
        return info;
    }

    /**
     * 插入新数据
     * @param planId 计划ID
     * @param projId 项目ID
     * @param taskType 任务类型
     * @param excuteType 执行方式
     * @return
     */
    @Override
    public void saveInfo(String planId, String projId, String taskType, String excuteType) {
        PlanExcuteInfo info = new PlanExcuteInfo();
        info.setId(UUIDGenerator.generate().toString());
        info.setPlanId(planId);
        info.setProjectId(projId);
        info.setTaskType(taskType);
        info.setExcuteType(excuteType);
        save(info);
    }

    /**
     * 通过项目ID查询报表数据
     * @param projId 项目ID
     * @return
     */
    @Override
    public FeignJson queryPlanExcuetReport(String projId) {
        FeignJson feignJson = new FeignJson();
        try{
            StringBuilder hql = new StringBuilder("");
            hql.append("from PlanExcuteInfo p where 1=1 and p.projectId=?");
            List<Object> paramList = new ArrayList<Object>();
            paramList.add(projId);
            List<PlanExcuteInfo> planExcuteInfoList = executeQuery(hql.toString(), paramList.toArray());
            Map<String, String> map = new HashMap<>();
            //WBS计划接收数量
            int wbsReceiveCount = 0;
            //流程计划接收数量
            int flowReceiveCount = 0;
            //任务计划接收数量
            int taskReceiveCount = 0;

            //WBS计划驳回数量
            int wbsTurnDownCount = 0;
            //流程计划驳回数量
            int flowTurnDownCount = 0;
            //任务计划驳回数量
            int taskTurnDownCount = 0;

            //WBS计划委派数量
            int wbsDelegateCount = 0;
            //流程计划委派数量
            int flowDelegateCount = 0;
            //任务计划委派数量
            int taskDelegateCount = 0;
            if (!CollectionUtils.isEmpty(planExcuteInfoList)){
                for (PlanExcuteInfo info:planExcuteInfoList){
                    //WBS计划
                    if (PlanConstants.PLAN_TYPE_WBS.equals(info.getTaskType())){
                        if (PlanConstants.PLAN_EXCUTE_REPORT_RECEIVE.equals(info.getExcuteType())){
                            wbsReceiveCount++;
                        }else if (PlanConstants.PLAN_EXCUTE_REPORT_TURN_DOWN.equals(info.getExcuteType())){
                            wbsTurnDownCount++;
                        }else if (PlanConstants.PLAN_EXCUTE_REPORT_DELEGATE.equals(info.getExcuteType())){
                            wbsDelegateCount++;
                        }
                        //流程计划
                    }else if (PlanConstants.PLAN_TYPE_FLOW.equals(info.getTaskType())){
                        if (PlanConstants.PLAN_EXCUTE_REPORT_RECEIVE.equals(info.getExcuteType())){
                            flowReceiveCount++;
                        }else if (PlanConstants.PLAN_EXCUTE_REPORT_TURN_DOWN.equals(info.getExcuteType())){
                            flowTurnDownCount++;
                        }else if (PlanConstants.PLAN_EXCUTE_REPORT_DELEGATE.equals(info.getExcuteType())){
                            flowDelegateCount++;
                        }
                        //任务计划
                    }else if (PlanConstants.PLAN_TYPE_TASK.equals(info.getTaskType())){
                        if (PlanConstants.PLAN_EXCUTE_REPORT_RECEIVE.equals(info.getExcuteType())){
                            taskReceiveCount++;
                        }else if (PlanConstants.PLAN_EXCUTE_REPORT_TURN_DOWN.equals(info.getExcuteType())){
                            taskTurnDownCount++;
                        }else if (PlanConstants.PLAN_EXCUTE_REPORT_DELEGATE.equals(info.getExcuteType())){
                            taskDelegateCount++;
                        }
                    }
                }
            }
            String wbs = new StringBuffer().append(wbsTurnDownCount+",").append(flowTurnDownCount+",").append(taskTurnDownCount).toString();
            String flow= new StringBuffer().append(wbsDelegateCount+",").append(flowDelegateCount+",").append(taskDelegateCount).toString();
            String task = new StringBuffer().append(wbsReceiveCount+",").append(flowReceiveCount+",").append(taskReceiveCount).toString();
            map.put("wbs", wbs);
            map.put("flow", flow);
            map.put("task", task);
            feignJson.setObj(map);
            feignJson.setMsg("操作成功");
        }catch (Exception e){
            feignJson.setSuccess(false);
            feignJson.setMsg("数据查询失败");
        }
        return feignJson;
    }
}
