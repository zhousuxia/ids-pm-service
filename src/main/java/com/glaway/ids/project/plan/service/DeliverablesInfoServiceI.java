package com.glaway.ids.project.plan.service;


import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.DeliveryStandard;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.vo.PlanExcelVo;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import com.glaway.ids.project.projectmanager.entity.ProjDocRelation;

import java.util.List;
import java.util.Map;


public interface DeliverablesInfoServiceI extends BusinessObjectServiceI<DeliverablesInfo> {

    /**
     * 根据deliverablesInfo条件检索交付物
     *
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    List<DeliverablesInfo> queryDeliverableList(DeliverablesInfo deliverablesInfo, int page,
                                                int rows, boolean isPage);


    /**
     * 获取满足deliverablesInfo条件的交付物的数目
     *
     * @return
     * @see
     */
    long getCount(DeliverablesInfo deliverablesInfo);


    /**
     * 根据id删除交付物
     *
     * @see
     */
    void deleteDeliverablesById(String id);


    /**
     * 初始化交付项
     * @param deliverablesInfo 交付项对象
     * @return
     */
    String initDeliverablesInfo(DeliverablesInfo deliverablesInfo);


    /**
     * 保存交付项
     * @param deliverablesInfo 交付项对象
     */
    void saveDeliverablesInfo(DeliverablesInfo deliverablesInfo);


    /**
     * 根据useObjectType、useObjectId查找相关交付物
     *
     * @param useObjectType  关联的数据类型
     * @param useObjectId 关联对象的id
     * @return
     * @see
     */
    List<DeliverablesInfo> getDeliverablesByUseObeject(String useObjectType, String useObjectId);

    /**
     * 根据useObjectType、useObjectId查找相关交付物
     *
     * @param useObjectType  关联的数据类型
     * @param useObjectId 关联对象的id
     * @return
     * @see
     */
    List<DeliverablesInfo> getDeliverablesByObjTypeAndId(String useObjectType, String useObjectId);

    /**
     * 删除Object相关交付物（物理删除）
     *
     * @param obj 交付项对象
     * @return
     * @see
     */
    void deleteDeliverablesPhysicsByPlan(Object obj);


    /**
     * 获取交付项生命周期
     * @return
     */
    String getLifeCycleStatusList();

    /**
     * 获取计划交付项集合
     * @param plan 计划对象
     * @param switchStr 配置信息
     * @param detailDeliverables 交付项信息
     * @param nameDeliverables 名称信息
     * @param dBizCurrent 生命周期
     * @param dBizVersion 版本
     * @param dPolicy 生命周期策略
     * @return
     */
    List<DeliverablesInfo> getPlanDeliverablesByDetailAndName(Plan plan, String switchStr,
                                                              String detailDeliverables,
                                                              String nameDeliverables,
                                                              String dBizCurrent,
                                                              String dBizVersion,
                                                              LifeCyclePolicy dPolicy);

    /**
     * 获取交付项对象信息
     * @param id 交付项id
     * @return
     */
    DeliverablesInfo getDeliverablesInfoEntity(String id);
    
    /**
     * 删除计划的输入、输出、删除计划输入相关的输入
     * 
     * @param planId 计划id
     */
    void deleteDeliverablesByPlanId(String planId);
    
    /**
     * 获取前置计划的输出
     * 
     * @param preposeIds 前置计划id
     * @return
     */
    List<DeliverablesInfo> getPreposePlanDeliverables(String preposeIds);
    
    /**
     * 获取输入增加时所选择的deliverablesInfo的交付物信息
     * 
     * @param ids 前置计划ids
     * @return
     */
    List<DeliverablesInfo> getSelectedPreposePlanDeliverables(String ids);
    
    /**
     * 根据parentId查找相关交付物
     * 
     * @param parentId 计划id
     * @return
     * @see
     */
    List<DeliverablesInfo> getAllDeliverablesByUseObeject(String parentId);
    
    
    /**
     * 获取交付物判断的字段，将状态判断加入
     * 
     * @return
     */
    Integer getJudgePlanAllDocumantWithStatus(Plan plan,String isOut);

    /**
     * 获取子计划的名称为name的交付物的信息
     *
     * @param parentId 计划id
     * @param name 计划名称
     * @return
     */
    List<DeliverablesInfo> getDeliverablesByParentAndName(String parentId, String name);

    /**
     * 项目计划页面查看初始化时获取项目库
     *
     * @param map 数据集合
     * @return
     */
    FeignJson listView(Map<String,Object> map);

    /**
     * 更新交付物
     * @param deliverablesInfo 交付项对象
     * @return
     */
    FeignJson updateDeliverablesInfo(DeliverablesInfo deliverablesInfo);


    /**
     * 通过PLM更新交付物
     * @param deliverablesInfo 交付项对象
     * @return
     */
    FeignJson updateDeliverablesInfoByPlm(DeliverablesInfo deliverablesInfo);

    /**
     * 更新交付项信息
     *
     * @param deliverablesInfo 交付项对象
     */
    void updateDelAndProjLib(DeliverablesInfo deliverablesInfo);

    /**
     * 获取计划-交付项集合
     * @param planId 集合id
     * @param deliverablesMap 交付项集合
     * @return
     */
    Map<String, ProjDocRelation> queryFinishDeliverable(String planId, Map<String, ProjDocRelation> deliverablesMap);

    /**
     * 根据项目id获取交付项
     * @param projectId 项目id
     * @return
     */
    List<DeliverablesInfo> getDeliverablesByProject(String projectId);

    /**
     * 获取计划模板Excel中计划的输出信息
     * @param vo 计划模板的excel对象vo
     * @param switchStr 配置
     * @param dBizCurrent 生命周期
     * @param dBizVersion 版本
     * @param dPolicy 生命周期策略
     * @return
     */
    List<DeliverablesInfo> getDeliverablesInfoByPlanTemplateExcel(PlanTemplateExcelVo vo, String switchStr,
                                                                  String dBizCurrent, String dBizVersion, LifeCyclePolicy dPolicy,
                                                                  Map<String, String> deliverMap, String nameDeliverables, TSUserDto userDto,String orgId);


    /**
     * Description : 根据计划模板ID查询交付项信息
     *
     * @author likaiyong
     */
    List<DeliverablesInfo> queryDeliverablesInfoByTemplateId(String planTemplateId);

    /**
     * Description: <br>
     * 通过编号获得相应的交付项，保存到交付项中<br>
     * 计划导出为MPP/计划模板，以及计划模板导出为MPP均不做校验
     *
     * @param workId 任务id
     * @param queryType 查询类型
     * @param saveType 保存类型
     * @throws GWException
     * @see
     */
    Map saveDeliverableByObj(String workId, String queryType, String saveType, Object obj,TSUserDto userDto,String orgId)
            throws GWException;

    /**
     * 根据计划id获取交付项
     * @param toString 交付项信息
     * @return
     */
    List<DeliverablesInfo> queryDeliverablesInfoByPlanIds(String toString);

    /**
     * excel导入交付项
     * @param vo 计划的excel对象vo
     * @param switchStr 配置
     * @param dBizCurrent 生命周期
     * @param dBizVersion 版本
     * @param dPolicy 生命周期策略
     * @param deliverMap 交付项集合
     * @param s 交付项名称
     * @return
     */
    List<DeliverablesInfo> getDeliverablesInfoByExcel(PlanExcelVo vo, String switchStr, String dBizCurrent, String dBizVersion, LifeCyclePolicy dPolicy, Map<String, String> deliverMap, String s);

    /**
     * 获取交付项列表
     * @param ds 交付项对象
     * @return
     */
    List<DeliveryStandard> searchDeliveryStandardAccurate(DeliveryStandard ds);

    /**
     * Description: <br>
     * 通过计划保存交付项<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param workId 任务id
     * @param queryType 查询类型
     * @param saveType 保存类型
     * @param plan 计划对象
     * @param lineNo 行号
     * @throws GWException
     * @see
     */
    void saveDeliverableByPlan(String workId, String queryType, String saveType, Plan plan,
                               String lineNo,TSUserDto userDto,String orgId)
            throws GWException;

    /**
     * 获取交付项生命周期状态
     * @return
     */
    List<LifeCycleStatus> getDeliveryLifeCycleStatus();
}
