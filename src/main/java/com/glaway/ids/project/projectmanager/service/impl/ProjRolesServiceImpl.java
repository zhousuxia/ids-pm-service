package com.glaway.ids.project.projectmanager.service.impl;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.*;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.ResourceUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.threemember.FeignGroupService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignTeamService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.constant.ProjectRoleConstants;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRolesServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.project.projectmanager.vo.GroupUserDetailVo;
import com.glaway.ids.project.projectmanager.vo.TeamVo;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LHR on 2019/7/26.
 */
@Service("projRolesService")
@Transactional
public class ProjRolesServiceImpl implements ProjRolesServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private ProjRoleServiceI projRoleService;

    @Value(value="${spring.application.name}")
    private String appKey;

    @Autowired
    private FeignGroupService groupService;


    @Autowired
    private FeignUserService userService;


    @Autowired
    private FeignTeamService teamService;


    @Override
    public List<TeamVo> getSysUserListByTeamId(String teamId) {
        List<TeamVo> list = new ArrayList<TeamVo>();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select ");
        hqlBuffer.append(" t.userId   	userId, ");
        hqlBuffer.append(" u.username   userName, ");
        hqlBuffer.append(" u.realname   realName, ");
        hqlBuffer.append(" d.id 		departId, ");
        hqlBuffer.append(" d.departname departName, ");
        hqlBuffer.append(" tr.rolecode  roleCode, ");
        hqlBuffer.append(" r.id         roleId, ");
        hqlBuffer.append(" r.rolename   roleName, ");
        hqlBuffer.append(" r.roletype   roleType, ");
        hqlBuffer.append(" r.status     roleStatus, ");
        hqlBuffer.append(" us.email     userEmail, ");
        hqlBuffer.append(" u.usertype   userType, ");
        hqlBuffer.append(" u.status     userStatus ");
        hqlBuffer.append(" from fd_team_role_user t, ");
        hqlBuffer.append(" fd_team_role      tr, ");
        hqlBuffer.append(" t_s_base_user     u, ");
        hqlBuffer.append(" t_s_role          r, ");
        hqlBuffer.append(" t_s_depart        d, ");
        hqlBuffer.append(" t_s_user          us ");
        hqlBuffer.append(" where t.teamRoleId = tr.id ");
        hqlBuffer.append(" and t.teamId = '" + teamId + "' ");
        hqlBuffer.append(" and t.userid = u.id ");
        hqlBuffer.append(" and tr.roleid = r.id ");
        hqlBuffer.append(" and u.departid = d.id ");
        hqlBuffer.append(" and u.id = us.id ");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        for (Map<String, Object> map : objArrayList) {
            if (map != null) {

                TSRoleDto role = new TSRoleDto();
                role.setId((String) map.get("roleId"));
                role.setRoleCode((String) map.get("roleCode"));
                role.setRoleName((String) map.get("roleName"));
                role.setRoleType(Short.valueOf(map.get("roleType").toString()));
                role.setStatus(Short.valueOf(map.get("roleStatus").toString()));

                TSUserDto user = new TSUserDto();
                user.setId((String) map.get("userId"));
                user.setUserName((String) map.get("userName"));
                user.setRealName((String) map.get("realName"));
                user.setEmail((String) map.get("userEmail"));
                user.setUserType(Short.valueOf(map.get("userType").toString()));
                user.setStatus(Short.valueOf(map.get("userStatus").toString()));
                TSDepartDto depart = new TSDepartDto();
                depart.setId((String) map.get("departId"));
                depart.setDepartname((String) map.get("departName"));
                user.setTSDepart(depart);

                TeamVo teamUser = new TeamVo();
                teamUser.setId(role.getRoleCode() + "-" + user.getId());
                teamUser.setRoleId(role.getId());
                teamUser.setRole(role);
                teamUser.setUserId(user.getId());
                teamUser.setDept((String) map.get("departName"));
                teamUser.setEmail(user.getEmail());
                teamUser.setUser(user);
                teamUser.setType("USER");
                teamUser.setIconCls("basis ui-icon-space");
                teamUser.set_parentId(role.getId());

                list.add(teamUser);
            }
        }
        return list;
    }

    @Override
    public List<TeamVo> getSysGroupListByTeamId(String teamId) {
        List<TeamVo> list = new ArrayList<TeamVo>();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select ");
        hqlBuffer.append(" r.id         roleId, ");
        hqlBuffer.append(" r.rolecode  roleCode, ");
        hqlBuffer.append(" r.rolename   roleName, ");
        hqlBuffer.append(" r.roletype   roleType, ");
        hqlBuffer.append(" r.status     roleStatus, ");
        hqlBuffer.append(" g.id groupId, ");
        hqlBuffer.append(" g.groupcode groupCode, ");
        hqlBuffer.append(" g.groupname groupName, ");
        hqlBuffer.append(" g.status groupStatus, ");
        hqlBuffer.append(" g.groupdesc groupDesc ");
        hqlBuffer.append(" from fd_team_role_group tr, ");
        hqlBuffer.append(" t_s_role          r, ");
        hqlBuffer.append(" t_s_group g ");
        hqlBuffer.append(" where tr.roleid = r.id ");
        hqlBuffer.append(" and tr.teamId = '" + teamId + "' ");
        hqlBuffer.append(" and tr.groupid = g.id ");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        for (Map<String, Object> map : objArrayList) {
            if (map != null) {

                TSRoleDto role = new TSRoleDto();
                role.setId((String) map.get("roleId"));
                role.setRoleCode((String) map.get("roleCode"));
                role.setRoleName((String) map.get("roleName"));
                role.setRoleType(Short.valueOf(map.get("roleType").toString()));
                role.setStatus(Short.valueOf(map.get("roleStatus").toString()));

                TSGroupDto group = new TSGroupDto();
                group.setId((String) map.get("groupId"));
                group.setGroupCode((String) map.get("groupCode"));
                group.setGroupName((String) map.get("groupName"));
                group.setGroupDesc((String) map.get("groupDesc"));
                group.setStatus(Short.valueOf(map.get("groupStatus").toString()));

                TeamVo teamGroup = new TeamVo();
                teamGroup.setId(role.getRoleCode() + "-" + group.getId());
                teamGroup.setRoleId(role.getId());
                teamGroup.setRole(role);
                teamGroup.setGroup(group);
                teamGroup.setType("GROUP");
                teamGroup.setUserId(group.getId());
                teamGroup.setGroupId(group.getId());
                teamGroup.set_parentId(role.getId());
                teamGroup.setIconCls("basis ui-icon-space");

                list.add(teamGroup);

            }
        }
        return list;
    }

    @Override
    public List<TSGroupDto> getSysGroupListByTeamIdAndRoleCode(String teamId, String roleCode) {
        String roleId = projRoleService.getRoleIdByRoleCode(roleCode);
        List<FdTeamRoleGroupDto> roleGroups = teamService.getFdTeamRoleGroupListByTeamIdAndRoleId(appKey,teamId, roleId);
        List<TSGroupDto> groups = new ArrayList<TSGroupDto>();
        for (FdTeamRoleGroupDto roleGroup : roleGroups) {
            Criteria cr = this.sessionFacade.createCriteria(null);

            CriteriaQuery criteriaQuery = new CriteriaQuery();
            criteriaQuery.add(Restrictions.eq("id", roleGroup.getGroupId()));
//            cr.add(Restrictions.eq("id", roleGroup.getGroupId()));
            List<TSGroupDto> groupListByCriteriaQuery = groupService.getGroupListByCriteriaQuery(criteriaQuery, false);
            TSGroupDto group = groupListByCriteriaQuery.get(0);
            if (group != null) {
                groups.add(group);
            }
        }
        return groups;
    }

    @Override
    public List<FdTeamRoleGroupDto> getFdTeamRoleGroupListByTeamIdAndRoleCode(String teamId, String roleCode) {
        String roleId = projRoleService.getRoleIdByRoleCode(roleCode);

        if (StringUtils.isEmpty(roleCode) || StringUtils.isEmpty(teamId)) {
            return null;
        }
        return teamService.getFdTeamRoleGroupListByTeamIdAndRoleId(appKey,teamId, roleId);
    }

    @Override
    public List<GroupUserDetailVo> getGroupUserDetailVoList(String groupId) {
        String sql = "select bu.realname || '-' || bu.username uname, d.departname dname, u.email, "
                + " case when bu.status = 1 then '有效' when bu.status = 0 then '失效' end status "
                + " from t_s_group_user gu "
                + " join t_s_user u on gu.userid = u.id "
                + " join t_s_base_user bu on gu.userid = bu.id "
                + " join t_s_depart d on d.id = bu.departid "
                + " where gu.groupid = '"
                + groupId + "'";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        List<GroupUserDetailVo> voList = new ArrayList<GroupUserDetailVo>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                GroupUserDetailVo vo = new GroupUserDetailVo();
                vo.setUserName(StringUtils.isEmpty((String)map.get("uname")) ? "" : map.get(
                        "uname").toString());
                vo.setDepartName(StringUtils.isEmpty((String)map.get("dname")) ? "" : map.get(
                        "dname").toString());
                vo.setEmail(StringUtils.isEmpty((String)map.get("email")) ? "" : map.get("email").toString());
                vo.setStatus(StringUtils.isEmpty((String)map.get("status")) ? "" : map.get(
                        "status").toString());
                voList.add(vo);
            }
        }
        return voList;
    }

    //TODO
//    @Override
//    public FeignJson doBatchDelRoleAndUsers(String projectId, String teamId, String userStr, String roleStr, String groupStr) {
//        String[] userArr = userStr.split(",");
//        String[] roleArr = roleStr.split(",");
//        String[] groupArr = groupStr.split(",");
//
//        Project p = projectService.getProjectEntity(projectId);
//
//        List<FdTeamRoleUser> managerList = projRoleService.getRoleUsersByRoleCodeAndTeamId(
//                ProjectRoleConstants.PROJ_MANAGER, teamId);
//
//        List<FdTeamRoleGroup> managerList2 = projGroupService.getRoleGroupsByRoleCodeAndTeamId(
//                ProjectRoleConstants.PROJ_MANAGER, teamId);
//
//        List<TeamVo> delManagerList = new ArrayList<TeamVo>();
//
//        List<TeamVo> users = new ArrayList<TeamVo>();
//        List<TeamVo> roles = new ArrayList<TeamVo>();
//        List<TeamVo> groups = new ArrayList<TeamVo>();
//
//        List<TeamVo> teamUsers = projRoleService.getSysUserListByTeamId(teamId);
//        List<TeamVo> teamGroups = projRoleService.getSysGroupListByTeamId(teamId);
//        String appKey = ResourceUtil.getApplicationInformation().getAppKey();
//        List<TSRoleDto> list = feignRoleService.getSysRoleListByTeamId(appKey,teamId);// 获得团队先角色
//        List<TeamVo> teamRoles = new ArrayList<TeamVo>();
//        for (TSRoleDto role : list) {
//            TeamVo teamrole = new TeamVo();
//            teamrole.setId(role.getId());
//            teamrole.setRoleId(role.getId());
//            teamrole.setRole(role);
//            teamrole.setType("ROLE");
//            teamrole.setUser(null);
//            teamrole.setUserId("");
//            teamrole.set_parentId(null);
//            teamrole.setIconCls("basis ui-icon-person");
//            teamRoles.add(teamrole);
//        }
//        for (int i = 0; i < userArr.length; i++ ) {
//            for (TeamVo user : teamUsers) {
//                if (userArr[i].equals(user.getId())) {
//                    users.add(user);
//                }
//            }
//        }
//
//        for (int i = 0; i < roleArr.length; i++ ) {
//            for (TeamVo role : teamRoles) {
//                if (roleArr[i].equals(role.getId())) {
//                    roles.add(role);
//                }
//            }
//        }
//
//        for (int i = 0; i < groupArr.length; i++ ) {
//            for (TeamVo group : teamGroups) {
//                if (groupArr[i].equals(group.getId())) {
//                    groups.add(group);
//                }
//            }
//        }
//
//        try {
//
//            for (TeamVo userVo : users) {
//                if (userVo.getRole().getRoleCode().equals(ProjectRoleConstants.PROJ_MANAGER)) {
//                    delManagerList.add(userVo);
//                }
//            }
//
//            for (TeamVo userVo : groups) {
//                if (userVo.getRole().getRoleCode().equals(ProjectRoleConstants.PROJ_MANAGER)) {
//                    delManagerList.add(userVo);
//                }
//            }
//            String kddProductTeamType=request.getParameter("kddProductTeamType");
//            if (delManagerList.size() >= managerList.size() + managerList2.size()) {
//                //判断条件变跟根据页面传入的参数进行判断
//                if(StringUtil.isNotEmpty(kddProductTeamType)&&"kddProduct".equals(kddProductTeamType)){
//                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.role.cannotBatchDeleteKdd");
//                }else{
//
//                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.role.cannotBatchDelete");
//                }
//                return j;
//            }
//            projRoleService.BatchDelRoleAndUsers(users, roles, groups, teamId, p, delManagerList,kddProductTeamType,projectId);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.role.batchDeleteFaliure");
//            log.error(message, e);
//            Object[] params = new Object[] {message, projectId};// 异常原因：{0}；详细信息：{1}
//            throw new GWException(GWConstants.ERROR_2003, params, e);
//        }
//        finally {
//            j.setMsg(message);
//            return j;
//        }
//    }
}
