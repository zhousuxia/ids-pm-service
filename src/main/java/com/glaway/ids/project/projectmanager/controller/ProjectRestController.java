package com.glaway.ids.project.projectmanager.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.project.menu.dto.ProjTeamLinkDto;
import com.glaway.ids.project.menu.dto.ProjectDto;
import com.glaway.ids.project.menu.entity.ProjTeamLink;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.menu.service.RecentlyProjectServiceI;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.dto.ProjWarnDto;
import com.glaway.ids.project.projectmanager.entity.ProjWarn;
import com.glaway.ids.project.projectmanager.service.ProjLogServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjWarnForGridVo;
import com.glaway.ids.project.projectmanager.vo.ProjWarnVo;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Songjie
 * @version V1.0
 * @Title: Controller
 * @Description: 项目维护
 */
@Api(tags = {"项目接口"})
@RestController
@RequestMapping("/feign/projectRestController")
public class ProjectRestController extends BaseController {
    /**
     * 操作日志
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(ProjectRestController.class);
    /**
     * 注入WorkFlowFacade
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;
    /**
     * 最近访问项目服务实现接口
     */
    @Autowired
    private RecentlyProjectServiceI recentlyProjectService;
    /**
     * 项目属性服务实现接口
     */
    @Autowired
    private PlanBusinessConfigServiceI businessConfigService;
    /**
     * 项目服务实现接口
     */
    @Autowired
    private ProjectServiceI projectService;
    /**
     * 项目团队服务实现接口
     */
    @Autowired
    private ProjRoleServiceI projRoleService;
    @Autowired
    private ProjLogServiceI projLogService;
    @Autowired
    private PlanServiceI planService;

    @ApiOperation(value = "初始化项目信息",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "initProject")
    public String initProject(@RequestBody ProjectDto project) {
        Project pro = new Project();
        Dto2Entity.copyProperties(project, pro);
        Project p = projectService.initProject(pro);
        String proStr = JSON.toJSONString(p);
        return proStr;
    }

    @ApiOperation(value = "判断用户是否属于某一个系统角色",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "PMO", value = "系统角色", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "isSysRoleByUserIdAndRoleCode")
    public boolean isSysRoleByUserIdAndRoleCode(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "PMO", required = false) String PMO) {
        boolean role = projRoleService.isSysRoleByUserIdAndRoleCode(userId, PMO);
        return role;
    }

    @ApiOperation(value = "获取项目列表数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectManager", value = "项目经理", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createName", value = "创建者姓名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "entryPage", value = "入口", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isTeamMember", value = "是否团队成员", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentUserId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryEntityBySql")
    public PageList queryEntityBySql(@RequestBody List<ConditionVO> conditionList, @RequestParam(value = "projectManager", required = false) String projectManager,
                                     @RequestParam(value = "createName", required = false) String createName, @RequestParam(value = "entryPage", required = false) String entryPage,
                                     @RequestParam(value = "isTeamMember", required = false) String isTeamMember, @RequestParam(value = "currentUserId", required = false) String currentUserId,
                                     @RequestParam(value="orgId",required = false) String orgId) {
        PageList pageList = projectService.queryEntityBySql(conditionList, projectManager,
                createName, entryPage, isTeamMember, currentUserId,orgId);
        return pageList;
    }

    @ApiOperation(value = "根据项目编号获取项目信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectNumber", value = "项目编号", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectbyNumber")
    public String getProjectbyNumber(@RequestParam("projectNumber") String projectNumber) {
        String str = projectService.getProjectbyNumber(projectNumber);
        return str;
    }

    @ApiOperation(value = "保存项目",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentUserStr", value = "用户信息", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "createProject")
    public FeignJson createProject(@RequestBody ProjectDto projectDto, @RequestParam("currentUserStr") String currentUserStr, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        Project project = new Project();
        Dto2Entity.copyProperties(projectDto, project);
        TSUserDto currentUser = JSON.parseObject(currentUserStr, TSUserDto.class);
        FeignJson fJson = projectService.createProject(project, currentUser, orgId);
        j.setObj(String.valueOf(fJson.getObj()));
        return j;
    }

    @ApiOperation(value = "保存项目日志",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectNumber", value = "项目编号", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "logInfo", value = "日志信息", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveProjLog")
    public void saveProjLog(@RequestParam(value = "projectNumber", required = false) String projectNumber, @RequestParam(value = "projectId", required = false) String projectId,
                            @RequestParam(value = "logInfo", required = false) String logInfo, @RequestParam(value = "remark", required = false) String remark, @RequestBody TSUserDto userDto) {
        projLogService.saveProjLog(projectNumber, projectId, logInfo, remark, userDto);
    }

    @ApiOperation(value = "获取项目信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectEntity")
    public ProjectDto getProjectEntity(@RequestParam("projectId") String projectId) {
        Project project = projectService.getProjectEntity(projectId);
        ProjectDto dto = new ProjectDto();
        try {
            dto = (ProjectDto) VDataUtil.getVDataByEntity(project,ProjectDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "获取满足plan条件的计划的数目",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getCount")
    public long getCount(@RequestBody PlanDto plan) {
        long count = planService.getCount(plan);
        return count;
    }

    @ApiOperation(value = "判断用户是否是项目的某一角色",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "roleCode", value = "角色编码", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "isProjRoleByUserIdAndRoleCode")
    public boolean isProjRoleByUserIdAndRoleCode(@RequestParam(value = "projectId", required = false) String projectId, @RequestParam(value = "roleCode", required = false) String roleCode,
                                                 @RequestParam(value = "userId", required = false) String userId) {
        boolean isProjRole = projRoleService.isProjRoleByUserIdAndRoleCode(projectId, roleCode, userId);
        return isProjRole;
    }

    @ApiOperation(value = "根据项目id获得团队id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTeamIdByProjectId")
    public FeignJson getTeamIdByProjectId(@RequestParam(value = "projectId", required = false) String projectId) {
        FeignJson j = new FeignJson();
        String teamId = projRoleService.getTeamIdByProjectId(projectId);
        j.setObj(teamId);
        return j;
    }

    @ApiOperation(value = "根据projectId获取项目日志列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjLogByProjectId")
    public String getProjLogByProjectId(@RequestParam("projectId") String projectId, @RequestParam("page") int page,
                                        @RequestParam("rows") int rows, @RequestParam("isPage") boolean isPage) {
        String logStr = projectService.getProjLogByProjectId(projectId, page, rows, isPage);
        return logStr;
    }

    @ApiOperation(value = "根据项目编号获取项目日志数量",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectNumber", value = "项目编号", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjLogListCount")
    public long getProjLogListCount(@RequestParam("projectNumber") String projectNumber) {
        long cnt = projectService.getProjLogListCount(projectNumber);
        return cnt;
    }

    @ApiOperation(value = "根据项目的teamId获取该项目团队下的团队",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "teamId", value = "团队id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getUserInProjectByTeamId")
    public List<TSUserDto> getUserInProjectByTeamId(@RequestParam("projectId") String projectId, @RequestParam("teamId") String teamId) {
        List<TSUserDto> list = projRoleService.getUserInProjectByTeamId(projectId, teamId);
        return list;
    }

    @ApiOperation(value = "获得项目下的所有系统人员",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getUserInProject")
    public List<TSUserDto> getUserInProject(@RequestParam("projectId") String projectId) {
        List<TSUserDto> list = projRoleService.getUserInProject(projectId);
        return list;
    }

    @ApiOperation(value = "根据项目状态判断计划是否可编辑",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "isModifyForPlan")
    public boolean isModifyForPlan(@RequestParam(value = "projectId", required = false) String projectId) {
        boolean isModifyBoo = projectService.isModifyForPlan(projectId);
        return isModifyBoo;
    }

    @ApiOperation(value = "获取项目生命周期数据",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getLifeCycleStatusList")
    public FeignJson getLifeCycleStatusList() {
        FeignJson j = new FeignJson();
        try {
            String lifeCycleStr = projectService.getLifeCycleStatusList();
            j.setObj(lifeCycleStr);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "发起项目流程",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "approvePerson", value = "室领导", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deptApprovePerson", value = "部门领导", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "processDefinitionKey", value = "流程实例", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "submitProjectFlow")
    public void submitProjectFlow(@RequestBody ProjectDto dto, @RequestParam(value = "approvePerson") String approvePerson,
                                  @RequestParam(value = "deptApprovePerson") String deptApprovePerson, @RequestParam(value = "remark") String remark,
                                  @RequestParam(value = "processDefinitionKey") String processDefinitionKey, @RequestParam(value = "userId") String userId) {
        Project project = new Project();
        Dto2Entity.copyProperties(dto, project);
        projectService.submitProjectFlow(project, approvePerson, deptApprovePerson, remark, processDefinitionKey, userId);
    }

    @ApiOperation(value = "重新提交项目流程",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "approvePerson", value = "室领导", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deptApprovePerson", value = "部门领导", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "submitProjectFlowAgain")
    public boolean submitProjectFlowAgain(@RequestBody ProjectDto dto, @RequestParam(value = "approvePerson") String approvePerson,
                                          @RequestParam(value = "deptApprovePerson") String deptApprovePerson, @RequestParam(value = "remark") String remark,
                                          @RequestParam(value = "userId") String userId) {
        Project project = new Project();
        Dto2Entity.copyProperties(dto, project);
        return projectService.submitProjectFlowAgain(project, approvePerson, deptApprovePerson, remark, userId);
    }

    @ApiOperation(value = "项目启动审批流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doStartProject")
    public FeignJson doStartProject(@RequestBody Map<String, Object> params) {
        return projectService.doStartProject(params);
    }

    @ApiOperation(value = "项目暂停/恢复审批流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doPauseOrResumeProject")
    public FeignJson doPauseOrResumeProject(@RequestBody Map<String, Object> params) {
        return projectService.doPauseOrResumeProject(params);
    }

    @ApiOperation(value = "项目关闭审批流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doCloseProject")
    public FeignJson doCloseProject(@RequestBody Map<String, Object> params) {
        return projectService.doCloseProject(params);
    }

    @ApiOperation(value = "判断是否是项目团队的项目经理",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "judgeIsTeamManager")
    public FeignJson judgeIsTeamManager(@RequestParam(value = "projectId", required = false) String projectId,
                                        @RequestParam(value = "userId", required = false) String userId) {
        return projectService.judgeIsTeamManager(projectId, userId);
    }

    @ApiOperation(value = "通过项目id获取计划列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getOneLevelPlanByProject")
    public List<PlanDto> getOneLevelPlanByProject(@RequestParam(value = "projectId", required = false) String projectId) {
        List<Plan> list = projectService.getOneLevelPlanByProject(projectId);
        List<PlanDto> result = new ArrayList<>();
        try {
            result = (List<PlanDto>) VDataUtil.getVDataByEntity(list, PlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "增加团队角色用户",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "teamId", value = "团队id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "roleCode", value = "角色编码", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "addRoleUser")
    public void addRoleUser(@RequestParam(value = "teamId") String teamId, @RequestParam(value = "roleCode") String roleCode, @RequestParam(value = "userId") String userId) {
        projRoleService.addRoleUser(teamId, roleCode, userId);
    }

    @ApiOperation(value = "更新项目经理",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "数据类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "upadteProjectManagerNames")
    public void upadteProjectManagerNames(@RequestParam(value = "projectId") String projectId, @RequestParam(value = "type", required = false) String type) {
        projectService.upadteProjectManagerNames(projectId, type);
    }

    @ApiOperation(value = "根据角色编码获取角色",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleCode", value = "角色编码", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getRole")
    public TSRoleDto getRole(@RequestParam("roleCode") String roleCode) {
        TSRoleDto tsRoleDto = projectService.getRole(roleCode);
        return tsRoleDto;
    }

    @ApiOperation(value = "更新项目",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pagetype", value = "数据类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isUpdate", value = "是否更新", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doProjectUpdate")
    public FeignJson doProjectUpdate(@RequestBody ProjectDto dto, @RequestParam(value = "pagetype", required = false) String pagetype,
                                     @RequestParam(value = "isUpdate", required = false) String isUpdate, @RequestParam(value = "userId", required = false) String userId) {
        Project project = new Project();
        Dto2Entity.copyProperties(dto, project);
        return projectService.doProjectUpdate(project, pagetype, isUpdate, userId);
    }

    @ApiOperation(value = "条件查询项目、团队、项目库关联关系",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "searchProjTeamLink")
    public List<ProjTeamLinkDto> searchProjTeamLink(@RequestBody ProjTeamLinkDto projTeamLink) {
        ProjTeamLink pLink = new ProjTeamLink();
        Dto2Entity.copyProperties(projTeamLink, pLink);
        List<ProjTeamLink> linkList = projRoleService.searchProjTeamLink(pLink);
        List<ProjTeamLinkDto> list = JSON.parseArray(JSON.toJSONString(linkList), ProjTeamLinkDto.class);
        return list;
    }

    @ApiOperation(value = "批量删除项目",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "项目ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "batchDeleteProject")
    public void batchDeleteProject(@RequestBody List<ProjectDto> projectDtos,@RequestParam(value = "ids", required = false) String ids,
                                   @RequestParam(value = "userId",required = false) String userId) {
        List<Project> project = new ObjectMapper().convertValue(projectDtos, new TypeReference<List<Project>>(){});
        projectService.batchDeleteProject(project, ids, userId);
    }

    @ApiOperation(value = "批量删除项目Feign",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "项目ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "projectBatchDelete")
    public FeignJson projectBatchDelete(@RequestParam(value = "ids", required = false) String ids, @RequestParam(value = "userId",required = false) String userId) {
        FeignJson j = new FeignJson();
        List<Project> delList = new ArrayList<Project>();
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.deleteSuccess");
        try {
            for (String id : ids.split(",")) {
                Project project = projectService.getProjectEntity(id);
                if ((StringUtils.isNotEmpty(project.getBizCurrent()) && !ProjectConstants.EDITING.equals(project.getBizCurrent()))
                        || (ProjectConstants.APPROVING.equals(project.getStatus()) && !ProjectConstants.REFUSED.equals(project.getIsRefuse()))) {
                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.onlyEditingDelete");
                    return j;
                }
                delList.add(project);
            }

            projectService.batchDeleteProject(delList, ids, userId);

            log.info(message, ids, "");
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.deleteFailure");
            log.error(message, e, ids, "");
            Object[] params = new Object[] {message, Project.class.getClass() + " oids:" + ids};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }


    @ApiOperation(value = "项目流程驳回到首节点再次提交工作流",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "oper", value = "操作", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "resubmitProjectFlow")
    public FeignJson resubmitProjectFlow(@RequestBody ProjectDto dto, @RequestParam(value = "oper", required = false) String oper,
                                         @RequestParam(value = "id", required = false) String id, @RequestParam(value = "userId", required = false) String userId) {
        Project project = new Project();
        Dto2Entity.copyProperties(dto, project);
        return projectService.resubmitProjectFlow(project, oper, id, userId);
    }

    @ApiOperation(value = "根据用户id从计划预警中获取项目编号",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProj")
    public FeignJson getProj(@RequestParam(value = "userId",required = false) String userId){
        FeignJson fj = new FeignJson();
        String projName = projectService.getProj(userId);
        fj.setObj(projName);
        return fj;
    }

    @ApiOperation(value = "获取门户项目列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectListForPortlet")
    public List<ProjectDto> getProjectListForPortlet(@RequestBody TSUserDto userDto){
        List<Project> projectList = projectService.getProjectListForPortlet(userDto);
        List<ProjectDto> proList = CodeUtils.JsonListToList(projectList,ProjectDto.class);
        return proList;
    }

    @ApiOperation(value = "门户项目列表数据获取",httpMethod = "GET")
    @ApiImplicitParams(@ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"))
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectPortletList")
    public List<ProjectDto> getProjectPortletList(@RequestParam(value = "userId",required = false) String userId){
        List<Project> projectList = projectService.getProjectPortletList(userId);
        List<ProjectDto> proList = CodeUtils.JsonListToList(projectList,ProjectDto.class);
        return proList;
    }


    @ApiOperation(value = "根据用户id从计划预警中获取项目编号",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjWarm")
    public FeignJson getProjWarm(@RequestParam(value = "userId",required = false) String userId){
        FeignJson fj = new FeignJson();
        String projNum = projectService.getProjWarm(userId);
        fj.setObj(projNum);
        return fj;
    }

    @ApiOperation(value = "新增项目预警信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "addProjWarn")
    public FeignJson addProjWarn(@RequestBody ProjWarnDto projWarnDto, @RequestParam(value = "userId",required = false) String userId, @RequestParam(value = "orgId",required = false) String orgId){
        FeignJson fj = new FeignJson();
        fj.setSuccess(true);
        try{
            ProjWarn projWarn = new ProjWarn();
            Dto2Entity.copyProperties(projWarnDto,projWarn);
            projectService.addProjWarn(projWarn,userId,orgId);
        }catch (Exception e){
            fj.setSuccess(false);
        }finally {
            return fj;
        }
    }

    @ApiOperation(value = "获取计划预警报表数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectIds", value = "项目ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjWarnReportData")
    public List<ProjWarnVo> getProjWarnReportData(@RequestParam(value = "projectIds",required = false) String projectIds){
        List<ProjWarnVo> projWarnVoList = projectService.getProjWarnReportData(projectIds);
        return projWarnVoList;
    }

    @ApiOperation(value = "获取统计分析中计划预警报表数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjWarnDataForProjStatistics")
    public List<Map<String,Object>> getProjWarnDataForProjStatistics(@RequestParam(value = "projectId",required = false) String projectId){
        List<Map<String,Object>> list = projectService.getProjWarnDataForProjStatistics(projectId);
        return list;
    }

    @ApiOperation(value = "获取统计分析-计划预警列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryProjectwarnGrid")
    public List<ProjWarnForGridVo> queryProjectwarnGrid(@RequestParam(value = "projectId",required = false) String projectId){
        List<ProjWarnForGridVo> list = projectService.queryProjectwarnGrid(projectId);
        return list;
    }

    @ApiOperation(value = "获取当前用户下已经执行的项目列表数据",httpMethod = "GET")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getStartingProjByUserId")
    public List<ProjectDto> getStartingProjByUserId(@RequestParam(value = "userId",required = false) String userId){
        List<Project> projectList = projectService.getStartingProjByUserId(userId);
        List<ProjectDto> proList = CodeUtils.JsonListToList(projectList,ProjectDto.class);
        return proList;
    }
}
