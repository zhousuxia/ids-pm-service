package com.glaway.ids.project.plan.service;


import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PlanownerApplychangeInfo;

import java.util.List;
import java.util.Map;


/**
 * 任务输入
 * 
 * @author blcao
 * @version 2015年7月6日
 * @see TaskFlowResolveServiceI
 * @since
 */


public interface TaskFlowResolveServiceI extends CommonService {
    /**
     *  获取parent计划的子流程任务，并将其转换为FlowTask（包括输入输出及资源）
     * @param parent 父计划
     * @param userId 用户id
     * @return 
     * @see
     */
    List<FlowTaskVo> getChangeFlowTaskList(FlowTaskParentVo parent,String userId);
    
    /**获取变更的前置计划
     * @param plan 计划对象
     * @return 
     * @see
     */
    List<FlowTaskPreposeVo> getChangeFlowTaskPreposeList(Plan plan);

    /**获取变更的节点list
     * @param plan 计划对象
     * @param userId 用户id 
     * @return
     * @throws GWException
     */
    List<FlowTaskVo> getChangeFlowTaskList(Plan plan,String userId)
            throws GWException;
    
    /**
     * 获取parent计划的子流程任务节点的前后置关系，并将其转换为ChangeFlowTaskCellConnect
     * 
     * @param parent 变更父节点id
     * @return
     * @throws GWException
     * @see
     */
    List<ChangeFlowTaskCellConnectVo> getChangeFlowTaskConnectList(FlowTaskParentVo parent)
        throws GWException;
    
    /**
     * 更新编辑后的流程任务
     * 
     * @param temp 计划对象
     * @param cellIds 节点id
     * @param parentPlanId 父计划id
     * @param cellContact 连接线
     * @param userId 用户id  
     * @throws GWException
     * @see
     */
    void updateFlowTasks(Plan temp, String cellIds, String parentPlanId, String cellContact,String userId)
        throws GWException;
    
    /**
     * 保存输入信息
     * @param ids 计划ids
     * @param currentinputs 输入
     * @param currentUserId 当前用户id
     * @see
     */
    void doAddInputsNew(String ids,Inputs currentinputs,String currentUserId);
    
    /**
     * 保存流程任务基本信息
     * 事物重构
     * 
     * @param plan 计划对象
     * @param parent 父计划
     */
    void saveFlowTask1(Plan plan, Plan parent,TSUserDto user);
    
    /**保存到临时变更对象中
     * @param plan 计划对象
     * @param parent 父计划
     */
    String saveFlowTask2(Plan plan, Plan parent,String currentUserId);
    
    /**
     * 批量删除输入
     * @param ids 输入ids
     * @return
     * @see
     */
    void doBatchDelInputsForWork(String ids);
    
    /**
     * 新增交付物-继承父项目输出
     * @param names 名称
     * @param plan 计划对象
     * @see
     */
    void doAddInheritDocument(String names, Plan plan);
    
    /**
     *  新增交付物
     * @param type 类型
     * @param names 名称
     * @param userId 用户id
     * @param childList 子计划list
     * @see
     */
    void doAdd(String type, String names, String userId, List<Plan> childList);
    
    /**保存输入数据
     * @param ids 交付项ids
     * @param inputs 输入对象
     */
    void doAddInputs(String ids, Inputs inputs);
    
    /**
     * 批量修改基本信息保存
     * @param fromTemplate 是否来源于研发流程模板
     * @param task 计划
     * @param planStartTime 开始时间
     * @param planEndTime 结束时间
     * @param workTime 工期
     * @param nameChange 名称
     * @see
     */
    void doBatchSaveBasicInfo(String fromTemplate, Plan task, String planStartTime,
                              String planEndTime, String workTime, boolean nameChange);
    
    /**
     * 删除线上的输入输出关系
     * @param to 去向节点
     * @param from 来源节点
     * @see
     */
    void deleteLineConnect(Plan to, Plan from);
    
    /**
     * 删除关联的输入
     * @param to  去向节点
     * @param toList 计划list
     * @param from  来源节点
     * @see
     */
    void deleteLinkInput(Plan to, List<Plan> toList, Plan from);
    
    /**
     * 删除所选节点
     * @param parentPlanId 父计划id
     * @param cellId  节点id
     * @see
     */
    void deleteSelectedCell(String parentPlanId, String cellId);
    
    /**
     * 保存前置计划，根据前置计划id
     * @param preposeIds 前置计划ids
     * @param useObjectId  当前所属计划id
     * @see
     */
    void saveOutPreposePlan(String preposeIds, String useObjectId);
    
    /**
     * 调用研发流程模板分解任务/计划
     * 
     * @param parentId
     *            任务/计划ID
     * @param templateId
     *            研发流程模板ID
     * @param currentId
     *            当前人员id         
     * @return
     * @see
     */
    boolean templateResolveForPlan(String parentId, String templateId,String currentId);

    /**
     * 启动并提交变更申请流程
     * @param planownerApplychangeInfo  负责人申请变更信息对象
     * @param leader 领导审批人
     * @param userId 人员id
     * @return
     */
    FeignJson startChangeApplyProcess(PlanownerApplychangeInfo planownerApplychangeInfo, String leader, String userId);

    /**
     * 驳回到首节点再次提交工作流
     *
     * @param map 流程的相关信息map
     * @return
     */
    FeignJson startChangeApplyForWorkFlow(Map<String,Object> map);

    /**
     * 终止流程
     *
     * @param map 流程的相关信息map
     * @return
     */
    FeignJson cancelChangeApplyForWorkFlow(Map<String,Object> map);

    /**
     * 变更申请驳回重新提交
     * @param planownerApplychangeInfo  负责人申请变更信息对象
     * @param formId 流程应用对象id
     * @param taskId 流程任务id
     * @see
     */
    void startChangeApplyForWorkFlow(PlanownerApplychangeInfo planownerApplychangeInfo,
                                     String formId, String taskId,String userId);

    /**
     * 终止变更申请流程
     * @param formId  流程应用对象id
     * @param taskId  流程任务id
     * @see
     */
    void cancelChangeApplyForWorkFlow(String formId, String taskId);

    /**
     *  变更启动流程
     * @param leader 审批人
     * @param userId 用户
     * @param planownerApplychangeInfo   负责人申请变更信息对象
     * @see
     */
    void startChangeApplyProcess(String leader,String userId, PlanownerApplychangeInfo planownerApplychangeInfo);
    
    /**刷新流程分解编辑器图信息
     * @param flowResolveXml 编辑器图文件
     * @param cellWorkTimeMap 节点map
     * @return 
     * @see
     */
    String refreshFlowResolveXml(String flowResolveXml, Map<String, String> cellWorkTimeMap);

    /**删除流程变更临时对象的连接线数据
     * @param id 临时变更连接线id
     * @see
     */
    void deleteFlowTaskCellConn(String id);

    /**根据父节点获取变更临时对象中的节点list
     * @param temPlan 父计划
     * @return
     * @throws GWException 
     * @see
     */
    List<ChangeFlowTaskCellConnectVo> getChangeFlowTaskConnectList(Plan temPlan) throws GWException;

    /**根据变更临时对象获取变更的输出
     * @param flowTask 变更节点
     * @return 
     * @see
     */
    List<FlowTaskPreposeVo> getChangeFlowTaskOutPreposeList(FlowTaskVo flowTask);

    /**获取变更节点连接线list
     * @param id 变更节点连接线id
     * @return 
     * @see
     */
    List<FlowTaskCellConnectVo> getFlowTaskConnectList(String id);
    
    /**驳回设置计划相关信息
     * @param formId 流程应用对象id
     * @see
     */
    void goBackFlowResolveSetPlanInfo(String formId);
    
    /**流程分解提交完成时保存相关信息
     * @param parentPlanId 父计划id
     * @param leaderId 审批人
     * @see
     */
    void endPlanAssignExcution(String parentPlanId, String leaderId);
    
    /**流程分解提交驳回
     * @param parentPlanId 父计划id
     * @see
     */
    void planAssignBack(String parentPlanId);
    
    /**流程分解启动工作台并保存相关信息
     * @param procInstId 流程id
     * @param parentPlanId 父计划id
     * @param actorId 人员id
     * @param formId  流程应用对象id
     * @param approveType 类型
     * @see
     */
    void startAssignProcessSavePlanInfo(String procInstId,String parentPlanId ,String actorId ,String formId,String approveType);
    
    /**驳回相关信息回退（驳回节点变成正常）
     * @param parentPlanId 父计划id
     * @see
     */
    void startAssignProcessBackSavePlanInfo(String parentPlanId);
    
    /**流程变更驳回流程
     * @param leader 室领导
     * @param deptLeader 部领导
     * @param flowTaskParent 变更父节点对象
     * @param userId 当前人员
     * @return 
     * @see
     */
    boolean startPlanChangeforBackProcess(String leader, String deptLeader,
                                          FlowTaskParentVo flowTaskParent,String userId);
    
    /**流程变更启动流程并保存相关信息
     * @param leader  室领导
     * @param deptLeader 部领导
     * @param changeType 类型
     * @param changeRemark 说明
     * @param flowTaskParent  变更父节点对象
     * @param changeFlowTaskList 变更节点list
     * @param changeFlowTaskConnectList 变更节点连接线list
     * @param userId 当前用户id
     * @return 
     * @see
     */
    public FlowTaskOutChangeVO startPlanChange(String leader,
                                               String deptLeader,
                                               String changeType,
                                               String changeRemark,
                                               FlowTaskParentVo flowTaskParent,
                                               List<FlowTaskVo> changeFlowTaskList,
                                               List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList,String userId);
    /**保存流程变更节点的数据（修改节点）
     * @param plan 计划对象
     * @param changeFlowTaskList 变更节点list
     * @param changeFlowTaskConnectList 变更节点连接线list
     * @param parent 父计划对象
     * @return 
     * @see
     */
    List<FlowTaskVo> saveFlowTaskForChange1(FlowTaskVo plan,
                                            List<FlowTaskVo> changeFlowTaskList,
                                            List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList,
                                            Plan parent);
    /**
     * 保存流程变更节点的数据（新增节点）
     * @param plan 计划对象
     * @param parent  父计划对象
     * @param flowTaskParent  变更父节点对象
     * @param changeFlowTaskList 变更节点list
     * @param changeFlowTaskConnectList 变更节点连接线list
     * @return 
     * @see
     */
    List<FlowTaskVo> saveFlowTaskForChange2(FlowTaskVo plan,
                                            Plan parent,
                                            FlowTaskParentVo flowTaskParent,
                                            List<FlowTaskVo> changeFlowTaskList,
                                            List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList);
}
