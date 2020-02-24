package com.glaway.ids.project.plan.service;


import com.alibaba.fastjson.JSONObject;
import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.vo.PlanExcelSaveVo;
import com.glaway.ids.project.plan.vo.PlanExcelVo;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateDetailReq;
import com.glaway.ids.review.vo.ReviewBaseInfoVo;
import com.glaway.ids.util.mpputil.MppInfo;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 项目计划
 *
 * @author blcao
 * @version 2015年3月27日
 * @see PlanServiceI
 * @since
 */

public interface PlanServiceI extends BusinessObjectServiceI<Plan> {


    /**
     * 根据plan条件检索计划
     *
     * @param plan 计划对象
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    List<Plan> queryPlanList(Plan plan, int page, int rows, boolean isPage);


    /**
     * 获取满足plan条件的计划的数目
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    long getCount(PlanDto plan);


    /**
     * 通过项目id和当前用户id查找用户设置的视图关系
     *
     * @author zhousuxia
     * @version 2018年6月4日
     * @see PlanServiceI
     * @since
     */
    UserPlanViewProject getUserViewProjectLinkByProjectId(String projectId,String userId);



    /**
     * 保存用户与项目视图的关系
     *
     * @author zhousuxia
     * @version 2018年6月4日
     * @see PlanServiceI
     * @since
     */
    void saveUserViewProjectLink(String projectId,String planViewId,TSUserDto curUser,String orgId);


    /**
     * 根据plan条件检索计划
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    List<PlanDto> queryPlanListForTreegridView(PlanDto plan,String planViewId,List<ConditionVO> conditionList,String userName, String progressRate, String workTime,String userId);

    /**
     * 根据plan条件检索计划
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    List<PlanDto> queryPlanListForTreegrid(PlanDto plan);

    /**
     * 初始化计划
     * @param planDto 计划对象
     * @return
     */
    String initPlan(PlanDto planDto);

    /**
     * 获取计划生命周期
     * @return
     */
    String getLifeCycleStatusList();

    /**
     *  通过id获取计划
     *
     * @param id 计划id
     * @return
     */
    Plan getEntity(String id);

    /**
     * 通过id获取计划
     * @param id 计划id
     * @return
     */
    PlanDto getPlanEntity(String id);

    /**
     * 根据plan条件检索非废弃的计划
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    List<Plan> queryPlansExceptInvalid(Plan plan);

    /**
     * 查询子计划
     * @param parentPlanId 计划id
     * @return
     */
    FeignJson queryPlanIdAndNameMap(String parentPlanId);

    /**
     * 获取项目库文档权限
     * @param folderId 文档id
     * @param planId 计划id
     * @param userId 用户id
     * @return
     */
    String getOutPowerForPlanList(String folderId, String planId,String userId);


    /**
     * 新增保存
     *
     * @param plan 计划对象
     * @see
     */
    FeignJson saveAsCreate(Plan plan, TSUserDto user,List<Inputs> inputsList,List<ResourceLinkInfo> resourceLinkList,String orgId);

    /**
     * 获取项目下最大的planNumber
     *
     * @param plan 计划对象
     * @return
     */
    int getMaxPlanNumber(Plan plan);

    /**
     * 获取项目下最大的StoreyNo
     *
     * @param plan 计划对象
     * @return
     */
    int getMaxStoreyNo(Plan plan);


    /**
     * 修改保存
     *
     * @param plan 计划对象
     * @see
     */
    void saveAsUpdate(Plan plan, TSUserDto user, List<ResourceLinkInfo> resourceLinkInfo,String orgId);

    /**
     * 根据计划ID查找其所有子计划（包括计划本身和所有子孙计划）
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    List<Plan> getPlanAllChildren(Plan plan);

    /**
     * 获取项目列表
     * @param conditionList 查询条件
     * @return
     */
    PageList queryProjectList(List<ConditionVO> conditionList);

    /**
     * 获取项目库权限
     * @param folderId 文档id
     * @param userId 用户id
     * @return
     */
    String getOutPowerForPlanListForReview(String folderId,String userId);

    /**
     * 根据项目的暂停/恢复设置该项目下计划的暂停/恢复
     *
     * @param project 项目对象
     * @param projectStatus 项目状态
     * @see
     */
    void setPlanProjectStatus(Project project, String projectStatus);

    /**
     *  计划下达前校验来源是否为空
     *
     * @param ids 计划ids
     * @param useObjectType 关联的对象类型
     * @return
     */
    FeignJson checkOriginIsNullBeforeSub(String ids,String useObjectType);

    /**
     * 判断父节点是否是拟制中
     *
     * @param id 计划id
     * @return
     */
    FeignJson pdAssign(Plan plan, String id);

    /**
     * 取消计划
     * @param plan 计划对象
     * @param userId 用户id
     */
    void cancelPlan(Plan plan, String userId);

    /**
     * 获取项目所有非“已完工”状态的一级计划
     *
     * @param project 项目对象
     * @return
     * @see
     */
    List<Plan> getOneLevelPlanByProject(Project project);


    /**
     * 获取该项目下计划的最小开始时间
     *
     * @param project 项目对象
     * @return
     * @see
     */
    Date getMinPlanStartTimeByProject(Project project);


    /**
     * 获取该项目下计划的最大结束时间
     *
     * @param project 项目对象
     * @return
     * @see
     */
    Date getMaxPlanEndTimeByProject(Project project);

    /**
     * 物理删除项目下的计划及其相关信息
     *
     * @param projectId 项目id
     */
    void deletePhysicsPlansByProjectId(String projectId);

    /**
     * 删除计划及其相关的交付物、资源
     *
     * @param planList 计划集合
     * @return
     * @see
     */
    void deletePlans(List<Plan> planList);

    /**
     * 获得可以清除的子计划（拟制中，无流程的可以清除）如果数量为0,可以清除
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    long judgePlanStatusByPlan(Plan plan);


    /**
     * 获取状态为拟制中的子计划数量
     *
     * @author zhousuxia
     * @version 2018年6月22日
     * @see PlanServiceI
     * @since
     */
    long getEditingPlanCount(Plan plan);

    /**
     * 导入计划事务处理
     *
     * @return
     * @see
     */
    void inputForWork(Set<String> mapKeys, String projectIdForPlan, Plan plan, String type, String userId,List<List<Map<String, Object>>> taskMapList,List<List<String>> preposePlanIdList)
            throws IOException;

    /**
     * 通过MPP保存计划信息
     *
     * @param plan 计划对象
     * @param mapList 任务集合
     * @param standardNameMap 标准名称集合
     * @param paraMap 参数集合
     */
    void savePlanImport(Plan plan, List<Map<String, Object>> mapList,
                        Map<String, String> standardNameMap, Map<String, Object> paraMap, List<String> idList);

    /**
     * Description: <br>
     * 获得可以清除计划（拟制中，无流程的可以清除）如果数量为0,可以清除<br>
     * Implement: <br>
     * 通过项目获得下面的计划不是拟制中，在流程中的数量<br>
     *
     * @param project 项目对象
     * @return
     * @see
     */
    long judgePlanStatus(Project project);

    /**
     * sql addBatch
     *
     * @param insertPlans 计划集合
     * @param insertPlanLogs 计划日志集合
     * @param insertDeliverablesInfos 计划交付项集合
     * @param insertPreposePlans 计划前置计划集合
     */
    String batchSaveDatas(Map<String, Object> flgMap, List<Plan> insertPlans,
                          List<PlanLog> insertPlanLogs, List<Inputs> insertInputs,
                          List<DeliverablesInfo> insertDeliverablesInfos,
                          List<PreposePlan> insertPreposePlans, List<Inputs> inputsList);

    /**
     * 项目计划单条下达选人页面信息获取
     *
     * @param assignId 计划id
     * @return
     */
    FeignJson goAssignPlanOne(String assignId, String userId);

    /**
     * 根据plan条件检索计划
     *
     * @return
     * @see
     */
    List<PlanDto> queryPlanListForTreegridWithView(List<PlanDto> planList,String planViewId, String userId);



    /**
     * 启动并提交计划下达审批流
     *
     * @param params 参数
     * @return
     */
    FeignJson startAssignProcess(Map<String,String> params);

    /**
     * 驳回到首节点再次提交工作流
     *
     * @param map 提交参数
     * @return
     */
    FeignJson startPlanFlow(Map<String,String> map);

    /**
     * 根据approvePlanInfo条件检索计划
     *
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    List<ApprovePlanInfo> queryAssignList(ApprovePlanInfo approvePlanInfo, int page, int rows,
                                          boolean isPage);

    /**
     * 根据approvePlanInfo条件检索计划
     *
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    void queryAssignListAndUpdate(ApprovePlanInfo approvePlanInfo, int page, int rows,
                                  boolean isPage);

    /**
     * 监听回调
     *
     * @param formId 表单id
     * @param assigner 用户信息
     */
    void planLifeCycleForward(String formId, TSUserDto assigner);

    /**
     * 通过ApprovePlanForm的ID获取其相关的所有计划
     *
     * @param approvePlanForm 计划提交信息
     * @return
     * @see
     */
    List<Plan> getPlanListByApprovePlanForm(ApprovePlanForm approvePlanForm);

    /**
     * 保存&更新计划
     * @param plan 计划对象
     */
    void updatePlan(Plan plan);

    /**
     * 更新计划
     * @param plan 计划对象
     */
    void updateEntity(Plan plan);

    /**通过planId判断是否存在于流程任务中
     * @param
     * @return
     * @see
     */
    String getPlanIdByLinkPlanId2(String planId);

    /**
     * 获取项目库权限
     * @param folderId 文档id
     * @param planId 计划id
     * @param userId 用户id
     * @return
     */
    String getOutPower(String folderId, String planId,String userId);

    /**
     * 流程分解时、查询节点前后置关系
     *
     * @param flowTaskCellConnect 流程分解对象
     * @return
     * @see
     */
    public List<FlowTaskCellConnectVo> queryFlowTaskCellConnectList(FlowTaskCellConnectVo flowTaskCellConnect);


    /**
     * @param changeFlowTaskList 流程分解对象
     * @return
     * @see
     */
    List<FlowTaskVo> taskSort(List<FlowTaskVo> changeFlowTaskList);

    /**
     * 保存流程分解产生的计划
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    Plan saveFlowResolvePlan(Plan plan,String userId);
    /**
     * 查询选择来源计划列表
     */
    List<Plan> queryPlanInputsList(Plan plan);

    /**
     * 根据plan条件检索计划
     *
     * @param plan 计划对象
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     */
    List<Plan> queryPlanOrderByStarttime(Plan plan, int page, int rows, boolean isPage);

    /**
     * 保存计划
     * @param Plan 计划对象
     */
    void savePlanByPlanDto(Plan Plan);

    /**
     * 获取流程应用对象信息
     * @param id 流程应用对象id
     * @return
     */
    ApprovePlanFormDto getApprovePlanFormEntity(String id);


    /**
     * 获取计划数据
     * @param conditionList 查询条件
     * @param projectId 项目id
     * @param userName 用户名
     * @param progressRate 进度
     * @param workTime 工期
     * @return
     */
    PageList queryEntity(List<ConditionVO> conditionList, String projectId, String userName, String progressRate, String workTime);

    /**
     *
     * 计划保存
     *
     * @param plan 计划对象
     */
    void saveOrUpdate(Plan plan);

    /**
     * 审批通过之后更新计划生命周期并将记录下达人
     *
     * @param formId 表单id
     */
    void planAssignBack(String formId);

    /**
     * 废弃计划
     * @param id 计划id
     * @param userId 用户id
     */
    void discardPlans(String id, String userId);

    /**
     * 计划前置选择页面初始化
     *
     * @param plan 计划对象
     * @param formId 表单id
     * @param userId 用户id
     * @return
     */
    FeignJson assignListViewTree(Plan plan, String formId, String userId);

    /**
     * 项目计划基本信息tab页面跳转 数据获取
     * @param plan 计划对象
     * @return
     */
    Map<String,Object> goBasicCheck(Plan plan);

    /**
     * 获取计划生命周期
     *
     * @return
     */
    FeignJson getPlanLifeStatus();

    /**
     * 删除计划及其相关的交付物、资源(物理删除)
     *
     * @param planList 计划集合
     * @return
     * @see
     */
    void deletePlansPhysics(List<Plan> planList);

    /**
     * 获取计划类型
     *
     * @param planName 计划名称
     * @return
     */
    FeignJson getTaskNameType(String planName);

    /**
     * 通过id获取变更信息
     * @param id 计划id
     * @return
     */
    PlanownerApplychangeInfo getPlanownerApplychangeInfo(String id);

    /**
     *获取变更计划列表
     * @param planId 计划id
     * @return
     */
    List<TemporaryPlan> getTemporaryPlanList(String planId);

    /**
     * 根据计划id更新任务操作类型
     * @param usetDto 用户信息
     * @param planId 计划id
     * @param opContent 操作
     */
    void updateOpContentByPlanId(TSUserDto usetDto , String planId,String opContent);

    /**
     * 删除资源
     * @param id 资源id
     */
    void deleteResourceByPlanId(String id);

    /**
     * 删除交付项
     * @param id 交付项id
     */
    void deleteDeliverablesByPlanId(String id);

    /**
     * 获取研发流程vo
     * @param planId 计划id
     * @return
     */
    FlowTaskVo getFlowTaskVo(String planId);

    /**
     * 获取研发流程vo
     * @param parentPlanId 计划id
     * @return
     */
    FlowTaskParentVo getFlowTaskParentVoInfo(String parentPlanId);

    /**
     * 通过流程任务id获取计划id
     * @param parentPlanId 计划id
     * @return
     */
    String getPlanIdByFlowTaskParentId(String parentPlanId);

    /**
     *
     * @param useObjectId 关联的对象id
     * @return
     */
    String getPlanIdByUseObjectId(String useObjectId);

    /**
     * 计划废弃流程
     * @param id 计划id
     * @param user 用户信息
     */
    void discardPlansForFlow(String id, TSUserDto user);

    /**
     * 保存研发流程数据
     * @param plan 计划信息
     * @param user 用户信息
     */
    void saveFlowTaskAsCreate(Plan plan, TSUserDto user);

    /**
     * 是否是评审任务
     * @param planName 计划名称
     * @return
     */
    boolean isReviewTask(String planName);

    /**
     * 保存研发流程任务数据
     * @param flowTaskCellConnectVoList 研发流程任务数据集合
     */
    void saveFlowTaskCellConnect(List<FlowTaskCellConnectVo> flowTaskCellConnectVoList);

    /**
     * 将计划数据转为json格式返回
     * @param list 计划集合
     * @return
     */
    List<JSONObject> changePlansToJSONObjects(List<Plan> list);

    /**
     * 获取计划视图中已选择计划列表
     * @param conditionList 查询条件
     * @param projectId 项目id
     * @param userName 用户名
     * @param progressRate 进度
     * @param workTime 工期
     * @param planIds 计划ids
     * @return
     */
    PageList queryEntityForSelectedPlan(List<ConditionVO> conditionList, String projectId,
                                        String userName, String progressRate, String workTime,
                                        String planIds);

    /**
     * 获取视图计划树列表
     * @param plan 计划对象
     * @param planIds 计划ids
     * @return
     */
    List<Plan> queryPlanListForCustomViewTreegrid(Plan plan, String planIds);


    /**
     * 获取除拟制中状态外的计划列表
     * @param plan 计划对象
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     */
    List<Plan> queryPlanListExceptEditing(Plan plan, int page, int rows, boolean isPage);


    /**
     * 获取导入计划列表
     * @param sheet exel数据
     * @param plan 计划对象
     * @param type 类型
     * @param dataTempList 数据
     * @param errorMsgMap 错误信息
     * @return
     */
    Map<String, Object> getImportDataList(Sheet sheet, Plan plan, String type,
                             List<PlanExcelSaveVo> dataTempList, Map<String, String> errorMsgMap);

    /**
     * 获取导入计划列表
     * @param plan 计划对象
     * @param type 类型
     * @param map 数据
     * @return
     */
    Map<String, Object> doImportExcel(String userId,Plan plan, String type, List<Map<String,String>> map);

    /**
     * 数据校验
     * @param project 项目对象
     * @param excelMap excel集合
     * @param errorMsgMap 错误信息
     * @param numList 数据集合
     * @param type 类型
     * @param plan 计划对象
     */
    void checkData2(Project project, Map<String, PlanExcelVo> excelMap,
                    Map<String, String> errorMsgMap, List<String> numList, String type, Plan plan);

    /**
     * excel导入
     * @param plan 计划对象
     * @param paraMap 参数聚合
     * @param list excel数据集合
     * @param excelMap excel集合
     * @param teamUsersMap 团队用户集合
     * @param standardNameMap 标准名称集合
     * @param planLevelMap 计划等级集合
     * @param userId 用户id
     * @return
     */
    String savePlanExcelData(Plan plan, Map<String, Object> paraMap, List<PlanExcelSaveVo> list,
                             Map<String, PlanExcelVo> excelMap, Map<String, String> teamUsersMap,
                             Map<String, String> standardNameMap, Map<String, String> planLevelMap,String userId,Map<String,String> activityIdAndTabCbTempMap);

    /**
     * 根据开始时间及结束时间获取工期
     * @param projectTimeType 项目工期设置
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    long getWorkTimeByStartAndEnd(String projectTimeType,
                                  Date startTime, Date endTime);

    /**
     * 下发评审任务
     * @param plan 计划独享
     * @param orderReviewTaskType 评审任务类型
     * @param userId 用户id
     * @return
     */
    FeignJson orderReviewTaskFj(Plan plan, String orderReviewTaskType, String userId);

    /**
     * 下发评审任务
     *
     * @param plan 计划独享
     * @param vo 评审对象
     * @see
     */
    void orderReviewTask(Plan plan, ReviewBaseInfoVo vo, String userId);


    List<T> getList(PlanTemplateDetailReq planTemplateDetailRep, Integer pageSize,
                    Integer pageNum);

    /**
     * 获取计划模板的计划树列表
     *
     * @author zhousuxia
     * @version 2018年7月6日
     * @see PlanServiceI
     * @since
     */
    List<Plan> queryPlanListForTemplateTreegrid(Plan plan);

    /**
     * 获取项目计划所对应的输入列表
     * @param plan 计划对象
     * @return
     */
    Map<String, List<InputsDto>> getDetailInputsList(Plan plan);

    /**
     * 获取项目计划所对应的输入列表
     * @param plan 计划对象
     * @return
     */
    Map<String, List<Inputs>> getDetailInputs(Plan plan);

    /**
     * 获取计划id与计划输出的集合
     *
     * @author zhousuxia
     * @version 2018年7月6日
     * @see PlanServiceI
     * @since
     */
    Map<String,List<DeliverablesInfoDto>> getDeliverableListMap();

    /**
     * 获取计划列表
     * @param o 计划对象
     * @return
     */
    List<PlanDto> getPlanInfoList(PlanDto o);

    /**
     * 获取变更计划对象
     * @param id 计划变更id
     * @return
     */
    TemporaryPlanDto getTemporaryPlanEntity(String id);

    /**
     * 停止计划发布流程
     * @param formId 表单id
     */
    void StopPlanAssignExcution(String formId,String userId);

    /**
     * 根据计划ID查找其所有子计划（包括计划本身和所有子孙计划）
     *
     * @return
     * @see
     */
    List<Plan> getPlanAllChildrenByProjectId(String projectId);

    /**
     * Description: <br>
     * 通过计划模板编号保存计划及子计划<br>
     *
     * @param planTemplateDetailReq 计划模板详细信息id
     * @see
     */
    void savePlanByPlanTemplateId(PlanTemplateDetailReq planTemplateDetailReq, String planId,
                                  String type, String userId,String orgId)
            throws GWException;

    /**
     * 通过计划编号获得Form编号
     * @param taskNumber 表单编码
     * @param type 类型
     * @return
     */
    String getTaskNumberByPlan(String taskNumber, String type);

    /**
     * 通过计划的ID获取其相关的所有ApprovePlanForm
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    List<ApprovePlanForm> getApprovePlanFormListByPlan(Plan plan);

    /**
     * 导出excel
     * @param plan 计划对象
     * @param projectId 项目id
     * @return
     */
    List<Plan> exportXls(Plan plan, String projectId);

    /**
     * 获取计划历史数据
     * @param planId 计划id
     * @return
     */
    List<PlanHistory> getPlanByHistory(String planId);

    /**
     * excel单条导出
     * @param plan 计划对象
     * @return
     */
    List<Plan> exportXlsSingle(Plan plan);

    /**
     * mpp导入
     * @param list 计划集合
     * @return
     */
    List<MppInfo> saveMppInfo(List<Plan> list);

    /**
     * 获取项目分类/计划阶段信息
     * @param id 计划阶段id
     * @return
     */
    BusinessConfigDto getBusinessConfigEntity(String id);

    /**
     * excel导入
     * @param map excel集合
     * @param projectId 项目id
     * @param planId 计划id
     * @param type 类型
     * @param dataTempList excel数据集合
     * @param errorMsgMap 错误信息集合
     * @return
     */
    String doImportExcel(Sheet map, String projectId, String planId, String type, List<PlanExcelSaveVo> dataTempList, Map<String, String> errorMsgMap);

    /**
     * 通过开始时间及工期获取结束时间
     * @param projectTimeType 工期设置
     * @param planStartTime 计划开始时间
     * @param i 工期
     * @return
     */
    Date getEndTimeByStartAndWorkTime(String projectTimeType, Date planStartTime, int i);

    /**
     * 删除资源及交付项
     * @param formId 表单id
     */
    void deleteDeliverablesAndResourceByTempForm(String formId);

    /**
     * 关注计划
     * @param planId 计划id
     * @param concernCode 关注类型
     * @param userId 用户id
     * @return
     */
    FeignJson  concernPlan(String planId, String concernCode, String userId);


    /**
     * 更新生命周期状态
     *
     * @author zhousuxia
     * @version 2019年10月11日
     * @see PlanServiceI
     * @since
     */
    void updateBizCurrent(Plan plan);


    /**
     * 启动计划待接收流程
     *
     * @author zhousuxia
     * @version 2019年10月17日
     * @see PlanServiceI
     * @since
     */
    void startPlanReceivedProcess(Plan plan,String userId);

    /**
     * 计划待接收流程---生命周期前进
     *
     * @author zhousuxia
     * @version 2019年10月17日
     * @see PlanServiceI
     * @since
     */
    void forwardPlanStatus(String planId,String userId);


    /**
     * 计划待接收流程---生命周期回退
     *
     * @author zhousuxia
     * @version 2019年10月17日
     * @see PlanServiceI
     * @since
     */
    void backwardPlanStatus(String planId);


    /**
     * 计划待接收流程---接收分支
     *
     * @author zhousuxia
     * @version 2019年10月17日
     * @see PlanServiceI
     * @since
     */
    void continuePlanReceiveProcess(String planId,String flag,TSUserDto curUser);

    /**
     * 计划待接收流程---驳回分支
     *
     * @author zhousuxia
     * @version 2019年10月17日
     * @see PlanServiceI
     * @since
     */
    void refusePlanReceiveProcess(String planId,TSUserDto curUser,String refuseReason,String refuseRemark,String orgId);

    /**
     * 获取计划驳回信息
     *
     * @author zhousuxia
     * @version 2019年10月17日
     * @see PlanServiceI
     * @since
     */
    PlanRefuseInfo getPlanRefuseInfoEntity(String id);

    /**
     * 计划待接收流程---委派分支
     *
     * @author zhousuxia
     * @version 2019年10月17日
     * @see PlanServiceI
     * @since
     */
    void delegatePlanReceiveProcess(String planId,TSUserDto curUser,String delegateUserId,String changeType,String leaderId,String departLeaderId,String changeRemark,String flag,String orgId);

    /**
     * 更新计划责任人并给新的责任人发送计划待接收流程
     *
     * @author zhousuxia
     * @version 2019年10月17日
     * @see PlanServiceI
     * @since
     */
    void updatePlanOwnerAndRestartPlanReceivedProcess(String planId,String delegateUserId,String userId);

    /**
     * 更新计划进度
     * @param plan 计划对象
     */
    void updateProgressRate(Plan plan);

    /**
     * 更新计划进度并保存
     * @param plan 计划对象
     */
    void updateProgress(Plan plan);

    /**
     * 获取进度
     * @param plan 计划对象
     * @return
     */
    String getProgress(Plan plan);

    /**
     * 获取计划生命周期
     * @return
     */
    List<LifeCycleStatus> getPlanLifeCycleStatus();

    /**
     * 根据BOM节点生成计划信息
     * @return
     */
    FeignJson doSaveBom(String planId, TSUserDto curUser, String bomId,
                        String projectId, String code, String name,
                        String orgId);

    /**
     * 完工反馈流程监听执行
     * @param id 计划id
     * @param map 完工反馈流程变量
     */
    void updateForFeedBack(String id, Map<String, Object> map);


    /**
     * 获取前置计划列表数据
     * @param conditionList 查询条件
     * @param projectId 项目id
     * @param userName 用户名
     * @param progressRate 进度
     * @param workTime 工期
     * @return
     */
    PageList queryEntityForPrepose(List<ConditionVO> conditionList, String projectId,
                                   String userName, String progressRate, String workTime);

    /**
     * 通过项目id获取该项目下所有流程分解计划
     * @param projectId 项目id
     * @param userId 用户id
     * @return
     */
    List<TreeNode> getProcPlanList(String projectId, String userId);
    
    /**变更审批通过时，通过formId查询出变更的数据
     * @param formId
     * @return 
     * @see
     */
    List<FlowTaskVo> getFlowTaskVoList(String formId);   
    
    /**
     *  判断是否是父子节点一起发布的
     */
    FeignJson checkisParentChildAllPublish(String id);

    /**
     * 启动计划委派流程
     *
     * @author zhousuxia
     * @version 2019年11月28日
     * @see PlanServiceI
     * @since
     */
    void startPlanDelegateProcess(String planId,TSUserDto curUser,String delegateUserId,String changeType,String leaderId,String departLeaderId,String changeRemark,String orgId);

    /**
     * 计划委派流程同意后更新计划责任人
     * @param planId 计划id
     * @param delegateUserId 被委派人id
     */
    void updatePlanOwner(String planId,String delegateUserId);

    /**
     * 计划委派流程驳回后更新流程标识
     * @param planId 计划id
     */
    void updatePlanDelegateFlag(String planId);

    /**获取当前项目下所有计划的生命周期状态数量
     * @param projId 项目ID
     */
    FeignJson getAllPlanLifeCycleArrayByProjId(String projId);

    /**获取当前项目下所有计划完成情况
     * @param projId 项目ID
     */
    FeignJson getAllPlanCompletionByProjId(String projId);

    /**
     * 通过项目id以及需要查询的计划id集合快速查询计划
     * @param projectId  项目id
     * @param avaliable  是否可以
     * @param planIdList 计划id集合
     * @return
     */
    Map<String, PlanDto> queryPlanMap(String projectId, String avaliable, List<String> planIdList);

    /**
     * 计划批量变更驳回后更新计划信息
     * @param formId 计划id
     */
    void updatePlanInfo(String formId);

    /**
     * 计划逾期预警定时任务查询
     * @param plan
     * @param page
     * @param rows
     * @param isPage
     * wqb 2020年1月3日 15:29:59
     */
    List<Plan> queryPlanListForTime(Plan plan, int page, int rows, boolean isPage);

    /**
     * 通过项目id查询前置计划
     * @param projId 项目ID
     */
    List<PreposePlanDto> allPreposePlanList(String projId);
}
