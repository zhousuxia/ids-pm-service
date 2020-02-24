package com.glaway.ids.project.projectmanager.service;


import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.menu.dto.ProjectDto;
import com.glaway.ids.project.menu.entity.Project;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.projectmanager.dto.ProjWarnDto;
import com.glaway.ids.project.projectmanager.entity.ProjLog;
import com.glaway.ids.project.projectmanager.entity.ProjWarn;
import com.glaway.ids.project.projectmanager.vo.ProjWarnForGridVo;
import com.glaway.ids.project.projectmanager.vo.ProjWarnVo;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 〈项目管理接口〉
 * 〈功能详细描述〉
 * 
 * @author songjie
 * @version 2015年3月24日
 * @see ProjectServiceI
 * @since
 */
public interface ProjectServiceI extends BusinessObjectServiceI<Project> {


    /**
     * 初始化项目
     * @param project 项目对象
     * @return
     */
    Project initProject(Project project);

    /**
     * 获取项目列表
     * @param conditionList 查询条件
     * @param projectManager 项目经理
     * @param createName 创建者姓名
     * @param entryPage 入口
     * @param isTeamMember 是否团队成员
     * @param currentUserId 当前用户id
     * @return
     */
    PageList queryEntityBySql(List<ConditionVO> conditionList, String projectManager,
                              String createName, String entryPage, String isTeamMember,String currentUserId,String orgId);

    /**
     * 根据项目编号获取项目信息
     * @param projectNumber 项目编号
     * @return
     */
    String getProjectbyNumber(String projectNumber);

    /**
     * 根据角色编码获取角色
     * @param code 角色编码
     * @return
     */
    TSRoleDto getRole(String code);


    /**
     * 根据projectNumber获得项目
     *
     * @param projectNumber 项目编号
     * @return
     * @see
     */
    Project getProjectByProjectNumber(String projectNumber);

    /**
     * 添加项目 forFeign
     * @param project 项目对象
     * @param userDto 用户信息
     * @param orgId 组织id
     * @return
     */
    FeignJson createProject(Project project, TSUserDto userDto,String orgId);


    /**
     * 添加项目
     * @param project 项目对象
     * @param userId 用户id
     * @param orgId 组织id
     * @return
     */
    Project addProject(Project project,String userId,String orgId);

    /**
     * 更新项目经理
     * @param projectId 项目id
     * @param type 数据类型
     */
    void upadteProjectManagerNames(String projectId,String type);

    /**
     * 创建团队
     * @param projectId 项目id
     * @return
     */
    String addTeam(String projectId);

    /**
     * 获取项目对象信息
     * @param projectId 项目id
     * @return
     */
    Project getProjectEntity(String projectId);

    /**
     * 根据projectId获取项目日志列表
     *
     * @param projectId 项目id
     * @param page 当前页
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    String getProjLogByProjectId(String projectId, int page, int rows, boolean isPage);

    /**
     * 根据projectId获取项目日志列表
     *
     * @param projectId 项目id
     * @param page 当前页
     * @param rows 每页数量
     * @param isPage
     *            是否分页
     * @return
     * @see
     */
    List<ProjLog> getProjLogListByProjectId(String projectId, int page, int rows, boolean isPage);


    /**
     * 根据项目编号获取项目日志数量
     *
     * @param projectNumber 项目编号
     * @return
     * @see
     */
    long getProjLogListCount(String projectNumber);

    /**
     * 根据项目状态判断计划是否可编辑
     *
     * @param projectId 项目id
     * @return
     * @see
     */
    boolean isModifyForPlan(String projectId);

    /**
     * 获取项目生命周期状态
     * @return
     */
    String getLifeCycleStatusList();

    /**
     * 分页查询项目列表
     *
     * @param project 项目对象
     * @param page    页码
     * @param rows    每页数量
     * @param isPage  是否分页
     * @return
     * @see
     */
    List<Project> getProjectListForWeb(Project project, int page, int rows, boolean isPage);

    /**
     * 发起项目的流程：启动、暂停、恢复、关闭<br>
     * 暂停和关闭的互斥：项目处于执行中状态，可以发起暂停或关闭流程<br>
     * 发起暂停流程时，需检查是否已发起关闭流程，如已发起，需先终止关闭流程，再发起暂停流程；反之亦然
     *
     * @param project              项目
     * @param approvePerson        室主任
     * @param deptApprovePerson    部领导
     * @param remark               备注
     * @param processDefinitionKey 流程类型Key
     * @see
     */
    void submitProjectFlow(Project project, String approvePerson, String deptApprovePerson, String remark, String processDefinitionKey, String userId);

    /**
     * 业务管理界面重新提交项目的流程：启动、暂停、恢复、关闭<br>
     * 如果当前用户和已发起流程的创建人一致，返回true（流程重新提交成功）；<br>
     * 否则，返回false（流程重新提交失败）
     *
     * @param project           项目
     * @param approvePerson     室主任
     * @param deptApprovePerson 部领导
     * @param remark            备注
     * @see
     */
    boolean submitProjectFlowAgain(Project project, String approvePerson, String deptApprovePerson, String remark, String userId);

    /**
     * 根据不同的业务类型，更新项目生命周期及日志
     *
     * @param projectId
     *            :项目ID
     * @param businessType
     *            :业务类型：START|PAUSE|RESUME|CLOSE
     */
    void updateProjectBizCurrent(String projectId, String businessType, String leader);

    /**
     * 根据项目ID获取项目库ID
     *
     * @param projectId 项目id
     * @return
     * @see
     */
    String getLibIdByProjectId(String projectId);

    /**
     * 修改项目接口暴露
     *
     * @param project  项目
     * @param pagetype 数据类型
     * @param isUpdate 是否更新
     * @param userId   用户id
     * @return
     */
    FeignJson doProjectUpdate(Project project, String pagetype, String isUpdate, String userId);

    /**
     * 修改项目
     *
     * @param managerIds 项目经理ID组
     * @param teamId     项目团队ID
     * @param project    项目
     * @see
     */
    void updateProject(String[] managerIds, String teamId, Project project, String userId);

    /**
     * 更新项目
     * @param project  需要修改的项目
     */
    void update(Project project);

    /**
     * 更新项目库名称
     *
     * @param libId    项目库ID
     * @param fileName 项目名+编号
     * @see
     */
    void updateProjectLibName(String libId, String fileName);

    /**
     * 项目启动审批流程
     *
     * @param params
     * @return
     */
    FeignJson doStartProject(Map<String, Object> params);

    /**
     * 项目关闭审批流程
     *
     * @param params
     * @return
     */
    FeignJson doCloseProject(Map<String, Object> params);

    /**
     * 项目暂停/恢复审批流程.
     *
     * @param params
     * @return
     */
    FeignJson doPauseOrResumeProject(Map<String, Object> params);

    /**
     *  判断是否是项目团队的项目经理
     * @param projectId  项目id
     * @param userId     用户id
     * @return
     */
    FeignJson judgeIsTeamManager(String projectId,String userId);

    /**
     * 通过项目id获取计划列表
     *
     * @param projectId  项目id
     * @return
     */
    List<Plan> getOneLevelPlanByProject(String projectId);

    /**
     * 批量删除项目
     *
     * @param project 项目
     * @param ids     项目ID，以","隔开
     * @see
     */
    void batchDeleteProject(List<Project> project, String ids,String userId);

    /**
     * 获取项目
     * @param id 用户id
     * @return
     * @see
     */
    String getProj(String id);

    /**
     * 获取预警项目编号
     * @param id 用户id
     * @return
     * @see
     */
    String getProjWarm(String id);

    /**
     * 获取预警项目编号
     * @param ids 项目id逗号隔开
     * @param id  用户id
     * @see
     */
    void getProjWarn(String ids, String id);

    /**
     * 项目流程驳回到首节点再次提交工作流：启动、暂停、恢复、关闭
     * @param project 项目
     * @param oper    操作
     * @param id      项目id
     * @param userId  用户id
     * @return
     */
    FeignJson resubmitProjectFlow(Project project, String oper, String id, String userId);

    /**
     * 待办中重新提交项目的流程：启动、暂停、恢复、关闭
     *
     * @param project 项目
     * @param taskId  流程任务ID
     * @see
     */
    void resubmitProjectFlow(Project project, String taskId, String userId);

    /**
     * 获取最大的planNumber
     *
     * @param plan 计划
     * @return
     * @see
     */
    int getMaxPlanNumber(Plan plan);

    /**
     * 获取同级最大的storeyNo
     *
     * @param plan 计划
     * @return
     * @see
     */
    int getMaxStoreyNo(Plan plan);

    /**
     * 获取门户项目列表
     * @param userDto 用户对象
     * @return
     */
    List<Project> getProjectListForPortlet(TSUserDto userDto);

    /**
     * 获取门户项目列表
     * @param userId 用户id
     * @return
     */
    List<Project> getProjectPortletList(String userId);

    /**
     * 新增计划预警信息
     * @param projWarn 计划预警对象
     * @param userId 用户id
     * @param orgId 组织id
     * @return
     */
    ProjWarn addProjWarn(ProjWarn projWarn, String userId,String orgId);


    /**
     * 获取计划预警报表数据
     * @param projectIds 项目ids
     * @return
     */
    List<ProjWarnVo> getProjWarnReportData(String projectIds);

    /**
     * 获取统计分析中计划预警报表数据
     * @param projectId 项目id
     * @return
     */
    List<Map<String,Object>> getProjWarnDataForProjStatistics(String projectId);

    /**
     * 获取统计分析-计划预警列表
     * @param projectId 项目id
     * @return
     */
    List<ProjWarnForGridVo> queryProjectwarnGrid(String projectId);
    /**
     * 获取当前用户下已经执行的项目列表数据
     * @param userId 用户Id
     * @return
     */
    List<Project> getStartingProjByUserId(String userId);
}
