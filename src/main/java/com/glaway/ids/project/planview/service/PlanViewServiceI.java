package com.glaway.ids.project.planview.service;

import java.util.List;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.ids.project.plan.dto.PlanViewColumnInfoDto;
import com.glaway.ids.project.plan.dto.PlanViewInfoDto;
import com.glaway.ids.project.plan.dto.PlanViewSearchConditionDto;
import com.glaway.ids.project.plan.entity.PlanViewColumnInfo;
import com.glaway.ids.project.plan.entity.PlanViewInfo;
import com.glaway.ids.project.plan.entity.PlanViewSearchCondition;
import com.glaway.ids.project.plan.entity.PlanViewSetCondition;
import com.glaway.ids.project.plan.service.PlanServiceI;

/**
 * 视图服务接口
 * @author likaiyong
 * @version 2018年5月31日
 */

public interface PlanViewServiceI extends CommonService{

    /**
     *
     * 视图树
     *
     * @param projectId 项目id
     * @param type 操作类型
     * @param userId 用户id
     * @return
     * @see
     */
    List<PlanViewInfoDto> constructionPlanViewTree(String projectId, String type, String userId,String orgId);


    /**
     * 获取视图的展示列信息
     *
     * @param planViewInfoId 视图id
     * @author zhousuxia
     * @version 2018年6月13日
     * @since
     */
    List<PlanViewColumnInfoDto> getColumnInfoListByPlanViewInfoId(String planViewInfoId);

    /**
     * 获取计划视图信息
     * @param id 视图id
     * @return
     */
    PlanViewInfoDto getPlanViewEntity(String id);


    /**
     * 通过视图id查找视图查询条件
     *
     * @param planViewId 视图id
     * @author zhousuxia
     * @version 2018年6月5日
     * @see PlanServiceI
     * @since
     */
    List<PlanViewSearchConditionDto> getSearchConditionListByViewId(String planViewId);

    /**
     * 获取计划视图查询条件
     * @param planViewId 视图id
     * @return
     */
    int getPlanViewTypeById(String planViewId);


    /**
     * 更新试图条件：
     * 公共视图发布者是否是当前登录用户或私有视图
     * @param planViewInfoId 视图id
     * @param userId 用户id
     */
    boolean isUpdateCondition(String planViewInfoId,String userId);


    /**
     * 获取视图查询条件
     * @param planViewInfoId 视图id
     */
    List<PlanViewSearchCondition> getSearchConditionByPlanView(String planViewInfoId);


    /**
     * 查询条件保存
     * @param planViewInfoId 视图id
     * @param conditionList 查询条件
     */
    void saveSearchCondition(List<ConditionVO> conditionList, String planViewInfoId, List<String> existConditionList, TSUserDto currentUser,String orgId);


    /**
     * 更新视图的展示列信息
     *
     * @param planViewInfoId 视图id
     * @param showColumnIds 展示列
     * @param curUser 用户信息
     * @param orgId 组织id
     * @author zhousuxia
     * @version 2018年6月13日
     * @see PlanViewServiceI
     * @since
     */
    void updatePlanViewColumn(String planViewInfoId,String showColumnIds,TSUserDto curUser,String orgId);

    /**
     * 获取视图的展示列信息
     *
     * @param planViewInfoId 视图id
     * @author zhousuxia
     * @version 2018年6月13日
     * @see PlanViewServiceI
     * @since
     */
    List<PlanViewColumnInfo> getColumnInfoListByPlanViewInfoId_(String planViewInfoId);


    /**
     * 校验视图名称是否重复
     * @param planViewInfoName 视图名称
     * @param status 视图状态
     * @param createName 创建者姓名
     * @return
     */
    List<PlanViewInfo> getPlanViewInfoByViewNameAndStatusAndCreateName(String planViewInfoName, String status, String createName);


    /**
     * 另存新视图
     *
     * @param oldViewId 视图id
     * @param newViewName 视图名称
     * @see
     */
    String saveAsNewView(String oldViewId, String newViewName,TSUserDto curUser,String orgId);


    /**
     * 用于判断视图名称是否重复
     * @param name 视图名称
     * @param status 视图状态
     * @return
     */
    boolean getViewCountByStatusAndName(String name, String status, String id);


    /**
     * 按部门设置保存
     * @param departmentId 部门id
     * @param name 视图名称
     * @return planViewInfoId
     */
    String saveSetConditionByDeaprtment(String departmentId, String name,TSUserDto curUser,String orgId);


    /**
     * 保存展示列信息
     * @param planViewInfoId 视图id
     */
    void saveColumnInfo(String planViewInfoId,TSUserDto curUser,String orgId);


    /**
     * 根据视图ID查询设置条件对象
     *
     * @param viewId 视图id
     * @return
     * @see
     */
    PlanViewSetCondition getBeanByPlanViewInfoId(String viewId);

    /**
     * 保存自定义视图设置
     *
     * @param info 视图对象
     * @param conditionList 查询条件
     * @param projectId 项目id
     * @param curUser 用户信息
     * @param orgId 组织id
     * @author zhousuxia
     * @version 2018年6月13日
     * @see PlanViewServiceI
     * @since
     */
    void saveCustomView(PlanViewInfo info,List<PlanViewSetCondition> conditionList,String projectId,TSUserDto curUser,String orgId);


    /**
     * 获取视图列表
     *
     * @param projectId 项目id
     * @param userDto 用户信息
     *
     */
    List<PlanViewInfo> getViewList(String projectId,TSUserDto userDto);

    /**
     *
     * 视图树
     *
     * @param projectId 项目id
     * @param type 操作类型
     * @param userDto 用户信息
     * @return
     * @see
     */
    List<PlanViewInfo> constructionPlanViewTree(String projectId, String type,TSUserDto userDto);


    /**
     * 获取视图设置条件
     * @param planViewInfoId 视图id
     */
    List<PlanViewSetCondition> getSetConditionByPlanView(String planViewInfoId);


    /**
     * 用于发布视图
     * @return
     * @param planViewInfoId  视图id
     * @param projectIds  项目id
     * @param userIds  用户id
     * @param name  视图名称
     * @param curUser  用户信息
     * @param orgId  组织id
     */
    void publishView(String planViewInfoId, String projectIds, String userIds, String name,TSUserDto curUser,String orgId);


    /**
     * 用于取消发布视图
     * @param id 视图id
     * @param status 状态
     * @return
     */
    void cancelPublishView(String id, String status);

}

