package com.glaway.ids.project.plan.service;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.entity.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @Description: 项目计划
 * @author: sunmeng
 * @ClassName: PlanFlowForworkServiceI
 * @Date: 2019/7/24-9:24
 * @since
 */
public interface PlanFlowForworkServiceI extends BusinessObjectServiceI<Plan> {

    /**
     * 新增交付物事务
     *
     * @return
     * @see
     */
    void doAddDelDeliverForWork(String names, DeliverablesInfo deliverablesInfo);

    /**
     * 删除交付物事务
     *
     * @param ids 交付物ids
     * @return
     * @see
     */
    void doBatchDelDeliverForWork(String ids);

    /**
     * 撤销分解删除拟制中的子计划事务
     *
     * @param plan 计划对象
     * @param userId 用户id
     * @return
     * @see
     */
    void deleteChildPlan(Plan plan,String userId);

    /**
     * 计划下达事务处理
     *
     * @return
     * @see
     */
    void startAssignForWork(String ids, String leader, String deptLeader, String assignType,
                            TSUserDto actor, String assignId);

    /**
     * 计划下达事务驳回再发起处理
     *
     * @param formId 表单id
     * @param taskId 任务id
     * @param leader 室领导
     * @param deptLeader 部门领导
     * @return
     * @see
     */
    void startAssignForWorkFlow(String formId, String taskId,String leader,String deptLeader, String userId);

    /**
     * 修改资源事务
     *
     * @return
     * @throws ParseException
     * @see
     */
    void modifyResourceMassForWork(List<ResourceLinkInfo> resourceLst, String[] useRate,
                                   String[] endTime, String[] startTime)
            throws ParseException;

    /**
     * 批量删除资源
     *
     * @param ids 资源ids
     * @return
     */
    FeignJson doBatchDel(String ids);

    /**
     * 删除资源事务
     *
     * @param ids 资源ids
     * @return
     * @see
     */
    void doBatchDelResourceForWork(String ids);


    /**
     * 单条发布计划
     * @param formId 表单id
     * @param taskId 任务id
     * @param leader 室领导
     * @param deptLeader 部门领导
     * @param userId 用户id
     */
    void startAssignForWorkFlowOnTree(String formId, String taskId, String leader,
                                      String deptLeader, String userId);

    /**
     * 计划发布
     * @param oid 流程oid
     * @param user 用户id
     * @param flowTaskIdStr 流程任务信息
     * @param flowTaskInputsIdAndRDTaskInputsIdStr 流程输入信息
     * @param flowTaskDeliversIdAndRDTaskDeliversIdStr 流程输出信息
     */
    void startPlanChangeFlowForWork(String oid,String user,String flowTaskIdStr,String flowTaskInputsIdAndRDTaskInputsIdStr,String flowTaskDeliversIdAndRDTaskDeliversIdStr);
    
    /**
     * 计划的流程流程变更发布
     * @param oid 流程oid
     * @param user 用户id
     */
    void startPlanChangeFlowForPlan(String oid,String user);

    /**
     * 计划变更驳回
     * @param oid 流程oid
     */
    void planChangeBack(String oid,String userId);

    /**
     * 获取变更计划列表
     * @param temporaryPlan 计划变更对象
     * @param planChangeList 计划变更集合
     * @param userId 用户id
     * @return
     */
    List<TemporaryPlan> getTempPlanListForWork(TemporaryPlan temporaryPlan, List<Plan> planChangeList,String userId);

    /**
     * 计划批量变更
     * @param tempPlanList  计划变更集合
     * @param resourceLinkInfoList 资源关联关系集合
     * @param actor 用户信息
     * @param leader 室领导
     * @param deptLeader 部门领导
     * @param changeType 变更类型
     * @param userId 用户id
     */
    void startPlanChangeMassForWork(List<TemporaryPlan> tempPlanList, List<TempPlanResourceLinkInfo> resourceLinkInfoList, TSUserDto actor, String leader, String deptLeader, String changeType, String userId);

    /**
     * 保存批量修改事务
     *
     * @return
     * @throws ParseException
     * @see
     */
    void saveModifyListForWork(List<Object> planList)
            throws ParseException;

    /**
     * 保存基线
     * @param ids 基线ids
     * @param basicLine 基线对象
     * @param projectId 项目id
     */
    void saveBasicLineForWork(String ids, BasicLine basicLine, String projectId);

    /**
     * 复制基线
     * @param o 基线对象
     * @param ids 基线ids
     * @param projectId 项目id
     * @param basicLineIdForCopy 基线id
     */
    void copyBasicLineForWork(BasicLine o, String ids, String projectId, String basicLineIdForCopy);

    /**
     * 基线提交审批
     * @param actor 用户信息
     * @param leader 室领导
     * @param deptLeader 部门领导
     * @param basicLineId 基线id
     * @param userId 用户id
     */
    void startBasicLineForWork(TSUserDto actor, String leader, String deptLeader, String basicLineId,String userId);

    /**
     * 基线提交审批
     * @param taskId 任务id
     * @param basicLineId 基线id
     */
    void startBasicLineFlowForWork(String taskId, String basicLineId);

    /**
     * 输出继承父项
     * @param names 交付项名称
     * @param planIdForInherit 计划id
     * @param useObjectType 关联对象类型
     * @param userDto 用户独享
     * @param orgId 组织id
     */
    void doAddInheritlistForTemplate(String names, String planIdForInherit,String useObjectType,TSUserDto userDto,String orgId);

    /**
     * 通过表单id获取计划变更的任务id
     * @param formId 表单id
     * @return
     */
    String getChangeTaskIdByFormId(String formId);

    /**
     * 根据项目id获取资源
     * @param projectId 项目id
     * @return
     */
    List<ResourceLinkInfo> getResourceLinkInfosByProject(String projectId);

    /**
     * 计划变更提交审批
     * @param formId 表单id
     * @param taskId 任务id
     * @param temporaryId 计划变更id
     * @param resourceLinkInfoList 资源关联信息集合
     * @param deliverablesInfoList 交付项集合
     * @param inputList 输入集合
     * @param leader 室领导
     * @param deptLeader 部门领导
     * @param userId 用户id
     */
    void startPlanChangeFlowForWork(String formId, String taskId, String temporaryId,
                                    List<TempPlanResourceLinkInfo> resourceLinkInfoList,
                                    List<TempPlanDeliverablesInfo> deliverablesInfoList,
                                    List<TempPlanInputs> inputList,
                                    String leader, String deptLeader,String userId);

    /**
     * 新增继承的交付物事务
     *
     * @return
     * @see
     */
    void doAddInheritlist(String names, String planIdForInherit,TSUserDto curUserDto,String orgId);
    
    /**计划变更时删除之前变更的待办
     * @param plan
     * @param processInstanceId
     */
    void delPlanReceivedProcInst(Plan plan,String processInstanceId);

    /**
     * 计划批量变更结束监听并保存数据
     * @param oid 计划id
     * @param userId 用户id
     */
    void stopPlanChangeAndSaveData(String oid,String userId);

    /**
     * 时间处理
     * @param date
     * @param day
     * @param beforOrAfter
     * wqb 2020年1月3日 15:33:31
     */
    Date dateChange(Date date, int day, String beforOrAfter);

    /**
     * 获取逾期预警的数据
     * @param plan
     * @return
     */
    Plan getPlanWarnAndOver(Plan plan);
}
