package com.glaway.ids.project.projectmanager.controller;

import io.swagger.annotations.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.dto.FdTeamRoleGroupDto;
import com.glaway.foundation.common.dto.FdTeamRoleUserDto;
import com.glaway.foundation.common.dto.TSGroupDto;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.menu.dto.ProjectDto;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRolesServiceI;
import com.glaway.ids.project.projectmanager.vo.GroupUserDetailVo;
import com.glaway.ids.project.projectmanager.vo.TeamVo;
import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Period;
import java.util.List;
import java.util.Map;

/**
 * Created by LHR on 2019/7/26.
 */
@Api(tags = {"项目团队角色接口"})
@RestController
@RequestMapping("/feign/projRolesRestController")
public class ProjRolesRestController {
    @Autowired
    private ProjRolesServiceI projRolesServiceI;
    @Autowired
    private ProjRoleServiceI projRoleServiceI;

    @ApiOperation(value = "根据项目的teamId获取该项目团队下的用户",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "teamId", value = "团队id", dataType = "String")
    })
    @RequestMapping(value = "getSysUserListByTeamId")
    public List<TeamVo> getSysUserListByTeamId(@RequestParam(value = "teamId") String teamId) {
        return projRolesServiceI.getSysUserListByTeamId(teamId);
    }

    @ApiOperation(value = "根据项目的teamId获取该项目团队下的团队",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "teamId", value = "团队id", dataType = "String")
    })
    @RequestMapping(value = "getSysGroupListByTeamId")
    public List<TeamVo> getSysGroupListByTeamId(@RequestParam(value = "teamId") String teamId) {
        return projRolesServiceI.getSysGroupListByTeamId(teamId);
    }

    @ApiOperation(value = "根据团队ID和角色code添加角色",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "teamId", value = "团队id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "角色编码", dataType = "String")
    })
    @RequestMapping(value = "addTeamRoleByCode")
    public void addTeamRoleByCode(@RequestParam(value = "teamId") String teamId, @RequestParam(value = "code") String code) {
        projRoleServiceI.addTeamRoleByCode(teamId, code);
    }

    @ApiOperation(value = "根据项目id获得团队id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @RequestMapping(value = "getTeamIdByProjectId")
    public FeignJson getTeamIdByProjectId(@RequestParam(value = "projectId") String projectId) {
        String teamIdByProjectId = projRoleServiceI.getTeamIdByProjectId(projectId);
        FeignJson feignJson = new FeignJson();
        feignJson.setObj(teamIdByProjectId);
        return feignJson;
    }

    @ApiOperation(value = "新增用户组",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "teamId", value = "团队id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "roleCode", value = "角色编码", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户组id", dataType = "String")
    })
    @RequestMapping(value = "addRoleGroup")
    public void addRoleGroup(@RequestParam(value = "teamId") String teamId, @RequestParam(value = "roleCode") String roleCode, @RequestParam(value = "id") String id) {
        projRoleServiceI.addRoleGroup(teamId, roleCode, id);
    }

    @ApiOperation(value = "批量删除用户和角色",httpMethod = "POST")
    @RequestMapping(value = "BatchDelRoleAndUsers")
    public void BatchDelRoleAndUsers(@RequestBody FeignJson json) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = json.getAttributes();
        List<TeamVo> users = mapper.convertValue(map.get("users"), new TypeReference<List<TeamVo>>() {
        });
        List<TeamVo> roles = mapper.convertValue(map.get("roles"), new TypeReference<List<TeamVo>>() {
        });
        List<TeamVo> groups = mapper.convertValue(map.get("groups"), new TypeReference<List<TeamVo>>() {
        });
        String teamId = (String) map.get("teamId");
        ProjectDto dto = new ObjectMapper().convertValue(map.get("p"), ProjectDto.class);
        Project p = new Project();
        Dto2Entity.copyProperties(dto, p);
        List<TeamVo> delManagerList = mapper.convertValue(map.get("delManagerList"), new TypeReference<List<TeamVo>>() {
        });
        String kddProductTeamType = (String) map.get("kddProductTeamType");
        String projectId = (String) map.get("projectId");
        String userId = (String) map.get("userId");
        projRoleServiceI.BatchDelRoleAndUsers(users, roles, groups, teamId, p, delManagerList, kddProductTeamType, projectId, userId);
    }

    @ApiOperation(value = "根据teamId和roleCode获得系统组",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "teamId", value = "团队id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "roleCode", value = "角色编码", dataType = "String")
    })
    @RequestMapping(value = "getSysGroupListByTeamIdAndRoleCode")
    public List<TSGroupDto> getSysGroupListByTeamIdAndRoleCode(@RequestParam(value = "teamId") String teamId, @RequestParam(value = "roleCode") String roleCode) {
        return projRolesServiceI.getSysGroupListByTeamIdAndRoleCode(teamId, roleCode);
    }

    @ApiOperation(value = "根据teamId和角色Code获得组",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "teamId", value = "团队id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "roleCode", value = "角色编码", dataType = "String")
    })
    @RequestMapping(value = "getFdTeamRoleGroupListByTeamIdAndRoleCode")
    public List<FdTeamRoleGroupDto> getFdTeamRoleGroupListByTeamIdAndRoleCode(@RequestParam(value = "teamId") String teamId, @RequestParam(value = "roleCode") String roleCode) {
        return projRolesServiceI.getFdTeamRoleGroupListByTeamIdAndRoleCode(teamId, roleCode);
    }

    @ApiOperation(value = "根据团队id获取该项目团队下的团队",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "groupId", value = "团队id", dataType = "String")
    })
    @RequestMapping(value = "getGroupUserDetailVoList")
    public List<GroupUserDetailVo> getGroupUserDetailVoList(@RequestParam(value = "groupId") String groupId) {
        return projRolesServiceI.getGroupUserDetailVoList(groupId);
    }
}
