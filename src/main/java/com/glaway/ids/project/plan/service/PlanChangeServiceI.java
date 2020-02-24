package com.glaway.ids.project.plan.service;


import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.plan.dto.TemporaryPlanDto;
import com.glaway.ids.project.plan.entity.*;

import java.util.List;


/**
 * 项目计划
 * 
 * @author sji
 * @version 2015年3月27日
 * @see PlanChangeServiceI
 * @since
 */

public interface PlanChangeServiceI extends BusinessObjectServiceI<TemporaryPlan> {

    /**
     * 根据temporaryPlan条件检索计划
     *
     * @param
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    List<TemporaryPlan> queryTemporaryPlanList(TemporaryPlan temporaryPlan, int page, int rows, boolean isPage);


    <T> List<T> pageList(String hql, int firstResult, int maxResult);

    <T> List<T> findByQueryString(String query);

    /**
     * 初始化计划变更
     * @param temporaryPlan 变更计划对象
     * @return
     */
    String initPlanChange(TemporaryPlanDto temporaryPlan);

    /**
     * 保存计划变更
     * @param temporaryPlan 变更计划对象
     * @param curUserId 用户id
     * @param orgId 组织id
     * @return
     */
    String saveOrUpdateTemporaryPlan(TemporaryPlan temporaryPlan,String curUserId,String orgId);

    /**
     * 获取计划变更id
     * @param temporaryPlan 变更计划对象
     * @param uploadSuccessPath 附件路径
     * @param uploadSuccessFileName 附件名称
     * @param userId 用户id
     * @param orgId 组织id
     * @return
     */
    String getTemporaryPlanId(TemporaryPlan temporaryPlan, String uploadSuccessPath, String uploadSuccessFileName,String userId,String orgId);

    /**
     * 启动计划变更流程
     * @param planList 计划集合
     * @param resourceLinkInfoList 计划-资源关联信息集合
     * @param inputList 输入集合
     * @param deliverablesInfoList 输出集合
     * @param actor 角色
     * @param leader 室领导
     * @param deptLeader 部门领导
     * @param changeType 变更类型
     * @param temporaryId  计划变更id
     * @param userId 用户id
     * @param typeIds
     */
    void startPlanChangeForWork(List<Plan> planList, List<TempPlanResourceLinkInfo> resourceLinkInfoList, List<TempPlanInputs> inputList, List<TempPlanDeliverablesInfo> deliverablesInfoList,
                                TSUserDto actor, String leader, String deptLeader, String changeType, String temporaryId, String userId,String typeIds);

    /**
     * 获取变更计划信息
     * @param id 计划变更id
     * @return
     */
    TemporaryPlanDto getTemporaryPlan(String id);


    /**
     * 根据计划Id查找计划变更历史
     *
     * @author zhousuxia
     * @version 2018年5月3日
     * @see PlanChangeServiceI
     * @since
     */
    List<PlanHistory> getPlanHistoryListByPlanId(String planId);

    /**
     * 根据计划Id查找计划变更临时历史数据
     *
     * @author zhousuxia
     * @version 2018年5月3日
     * @see PlanChangeServiceI
     * @since
     */
    List<TemporaryPlan> getTemporaryPlanListByPlanId(String planId);

    /**
     *
     * 查找申请关联信息
     *
     * @param approvePlanInfo 计划审批对象
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    List<ApprovePlanInfo> queryApprovePlanInfoList(ApprovePlanInfo approvePlanInfo, int page, int rows, boolean isPage);

    /**
     *
     * 查找资源临时信息
     *
     * @param tempPlanResourceLinkInfo 资源临时信息
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    List<TempPlanResourceLinkInfo> queryTempPlanResourceLinkList(TempPlanResourceLinkInfo tempPlanResourceLinkInfo,
                                                                 int page, int rows, boolean isPage);
}




