/*
 * 文件名：ProjRoleServiceImpl.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：wangshen
 * 修改时间：2015年5月13日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.service.impl;


import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.*;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.core.common.extend.hqlsearch.HqlGenerateUtil;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.threemember.FeignGroupService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignRoleService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignTeamService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.config.constant.ConfigStateConstants;
import com.glaway.ids.constant.ProjectRoleConstants;
import com.glaway.ids.project.menu.entity.ProjTeamLink;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.menu.service.RecentlyProjectServiceI;
import com.glaway.ids.project.projectmanager.service.ProjGroupServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRolesServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.project.projectmanager.vo.TeamVo;
import com.glaway.ids.util.CommonInitUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("projRoleService")
@Transactional
public class ProjRoleServiceImpl implements ProjRoleServiceI {

    /**
     * hibernate持久化工具类
     */
    @Autowired
    private SessionFacade sessionFacade;

    /**
     * 
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(ProjRoleServiceImpl.class);

    @Autowired
    private FeignGroupService groupService;

    @Autowired
    private FeignUserService userService;

    @Autowired
    private FeignRoleService roleService;

    @Autowired
    private FeignTeamService teamService;

    @Autowired
    private ProjRolesServiceI projRolesServiceI;

    @Autowired
    private ProjectServiceI projectService;


    @Autowired
    private ProjGroupServiceI projGroupService;

    @Autowired
    private ProjRoleServiceI projRoleService;

    @Autowired
    private RecentlyProjectServiceI recentlyProjectService;

    @Value(value="${spring.application.name}")
    private String appKey;

    @Override
    public boolean isSysRoleByUserIdAndRoleCode(String userId, String roleCode) {
        boolean flag = false;
        StringBuilder hql = new StringBuilder("");
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(roleCode)) {
            /*hql.append("from TSRoleUser t where t.TSUser.id=? and t.TSRole.roleCode=?");
            List<Object> paramList = new ArrayList<Object>();
            paramList.add(userId);
            paramList.add(roleCode);*/

            List<TSRoleUserDto> roleUsers = roleService.getTSRoleUserByUserIdAndRoleCode(appKey,userId,roleCode);
            if (!CommonUtil.isEmpty(roleUsers)) {
                flag = true;
            }
        }
        return flag;
    }


    @Override
    public String saveTeamLink(String projectId, String teamId, String libId,TSUserDto userDto,String orgId) {
        ProjTeamLink link = new ProjTeamLink();
        link.setProjectId(projectId);
        link.setTeamId(teamId);
        link.setLibId(libId);
        CommonInitUtil.initGLObjectForCreate(link,userDto,orgId);
        projectService.initBusinessObject(link);
        sessionFacade.save(link);
        return link.getId();
    }

    @Override
    public String getLibIdByProjectId(String projectId) {
        if (StringUtils.isNotEmpty(projectId)) {
            ProjTeamLink projTeamLink = new ProjTeamLink();
            projTeamLink.setProjectId(projectId);
            List<ProjTeamLink> links = searchProjTeamLink(projTeamLink);
            if (!CommonUtil.isEmpty(links)) {
                return links.get(0).getLibId();
            }
        }
        return "";
    }


    @Override
    public List<ProjTeamLink> searchProjTeamLink(ProjTeamLink projTeamLink) {
        CriteriaQuery cq = new CriteriaQuery(ProjTeamLink.class);
        HqlGenerateUtil.installHql(cq, projTeamLink, null);
        return sessionFacade.getListByCriteriaQuery(cq, false);
    }


    //新增type属性  用来处理 kdd 产品复制成项目  项目经理参数传入问题   原因  由于webService 存表事务问题upadteProjectManagerNames 导致方法查询团队中的角色信息查询不到 productAdd产品新增   productUpdate产品修改
    @Override
    public void reSaveManager(String[] managerIds, String teamId,String type) {
        if (StringUtils.isBlank(teamId) || managerIds == null || managerIds.length == 0) {
            // return;
        }
        else {

            FdTeamRoleDto role = teamService.getFdTeamRoleByTeamIdAndRoleCode(teamId,
                    ProjectRoleConstants.PROJ_MANAGER);

            FdTeamRoleUserDto roleUserDto = new FdTeamRoleUserDto();
            roleUserDto.setTeamId(teamId);
            TSRoleDto roleDto = roleService.getRoleByRoleCode(ProjectRoleConstants.PROJ_MANAGER);
            roleUserDto.setRoleId(roleDto.getId());
            List<FdTeamRoleUserDto> managers = teamService.getFdTeamRoleUser
                    (appKey,roleUserDto);

            if (null == role) {
                addTeamRoleByCode(teamId, ProjectRoleConstants.PROJ_MANAGER);
            }

            teamService.deleteFdTeamRoleUserByFdTeamRoleUser(appKey,managers);
           /* // 循环删除项目经理
            for(FdTeamRoleUserDto manager : managers){
                sessionFacade.delete(managers);
            }
*/

            // 循环保存项目经理
            for (String userId : managerIds) {
                addRoleUser(teamId, ProjectRoleConstants.PROJ_MANAGER, userId);
            }

            // 更新pm_project表ProjectManagerNames字段
            //TODO---KDD相关
            projectService.upadteProjectManagerNames(getProjectIdByTeamId(teamId),type);
        }

    }



    @Override
    public String  getTeamIdByProjectId(String projectId) {
        if (StringUtils.isNotEmpty(projectId)) {
            ProjTeamLink projTeamLink = new ProjTeamLink();
            projTeamLink.setProjectId(projectId);
            List<ProjTeamLink> links = searchProjTeamLink(projTeamLink);
            if (!CommonUtil.isEmpty(links)) {
                return links.get(0).getTeamId();
            }
        }
        return "";
    }


    @Override
    public String getProjectIdByTeamId(String teamId) {
        if (StringUtils.isNotEmpty(teamId)) {
            ProjTeamLink projTeamLink = new ProjTeamLink();
            projTeamLink.setTeamId(teamId);
            List<ProjTeamLink> links = searchProjTeamLink(projTeamLink);
            if (!CommonUtil.isEmpty(links)) {
                return links.get(0).getProjectId();

            }
        }
        return "";
    }

    /**
     * Description: <br>保存角色用户
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param teamId
     * @param roleCode
     * @param userId
     * @see
     */
    @Override
    public void addRoleUser(String teamId, String roleCode, String userId) {
        FdTeamRoleUserDto fdTeamRoleUser = new FdTeamRoleUserDto();
        fdTeamRoleUser.setTeamId(teamId);
        fdTeamRoleUser.setRoleId(getRoleIdByRoleCode(roleCode));
        FdTeamRoleDto teamRoleDto = teamService.getFdTeamRoleByTeamIdAndRoleCode(teamId,roleCode);
        fdTeamRoleUser.setTeamRoleId(teamRoleDto.getId());
        fdTeamRoleUser.setUserId(userId);
        teamService.addFdTeamRoleUser(fdTeamRoleUser);
    }

    @Override
    public List<TSUserDto> getUserInProjectByTeamId(String projectId, String teamId) {
        List<TSUserDto> gUsers = projGroupService.getUnderGoupPartUsersByTeamId(teamId);
        List<TSUserDto> cUsers = userService.getSysUserListByTeamId(appKey,teamId);

        return unionUserList(gUsers, cUsers);
    }

    @Override
    public List<TSUserDto> getUserInProject(String projectId) {
        String teamId = getTeamIdByProjectId(projectId);
        List<TSUserDto> gUsers = projGroupService.getUnderGoupPartUsersByTeamId(teamId);
        List<TSUserDto> cUsers = userService.getSysUserListByTeamId(appKey,teamId);

        return unionUserList(gUsers, cUsers);
    }


    @Override
    public String getProjectIdByLibId(String libId) {
        if (StringUtils.isNotEmpty(libId)) {
            ProjTeamLink projTeamLink = new ProjTeamLink();
            projTeamLink.setLibId(libId);
            List<ProjTeamLink> links = searchProjTeamLink(projTeamLink);
            if (!CommonUtil.isEmpty(links)) {
                return links.get(0).getProjectId();

            }
        }
        return "";
    }

    @Override
    public void addRoleGroup(String teamId, String roleCode, String groupId) {
        FdTeamRoleGroupDto group = new FdTeamRoleGroupDto();
        String roleId = projRoleService.getRoleIdByRoleCode(roleCode);
        group.setTeamId(teamId);
        group.setGroupId(groupId);
        group.setRoleId(roleId);
        group.setTeamRoleId(projRoleService.getTeamRoleIdByRoleCode(teamId, roleCode));

        teamService.addFdTeamRoleGroup(appKey,group);
    }

    @Override
    public String getTeamRoleIdByRoleCode(String teamId, String roleCode) {
        FdTeamRoleDto fdTeamRole = teamService.getFdTeamRoleByTeamIdAndRoleCode(teamId, roleCode);
        if (fdTeamRole == null) {
            return "";
        }
        return fdTeamRole.getId();
    }

    @Override
    public void BatchDelRoleAndUsers(List<TeamVo> users, List<TeamVo> roles, List<TeamVo> groups,
                                     String teamId, Project p, List<TeamVo> delManagerList, String kddProductTeamType, String projectId, String userid) {
        Project project = projectService.getProjectEntity(projectId);
        //获取项目下所有团队的人员  未删除之前的所有团队中的成员
        List<TSUserDto>  tSUserListOld=getUserInProject(projectId);
        // 删除角色下用户
        for (TeamVo roleVo : roles) {

            FdTeamRoleUserDto roleUserDto = new FdTeamRoleUserDto();
            roleUserDto.setTeamId(teamId);
            TSRoleDto roleDto = roleService.getRoleByRoleCode(ProjectRoleConstants.PROJ_MANAGER);
            roleUserDto.setRoleId(roleDto.getId());
            List<FdTeamRoleUserDto> usersUnderRoles = teamService.getFdTeamRoleUser(appKey,roleUserDto);

            for (FdTeamRoleUserDto usersUnderRole : usersUnderRoles) {

                teamService.deleteFdTeamRoleUserByTeamIdAndRoleIdAndUserId(teamId,
                        usersUnderRole.getRoleId(), usersUnderRole.getUserId());
                recentlyProjectService.deleteRecentlyByProjectIdAndUserId(p.getId(),
                        usersUnderRole.getUserId());
            }
        }

        // 删除用户
        for (TeamVo userVo : users) {
            String userId = userVo.getUserId();
            String roleId = userVo.getRoleId();
            teamService.deleteFdTeamRoleUserByTeamIdAndRoleIdAndUserId(teamId, roleId, userId);
            recentlyProjectService.deleteRecentlyByProjectIdAndUserId(p.getId(), userId);
        }

        // 删除组
        for (TeamVo groupVo : groups) {
            String groupId = groupVo.getUserId();
            String roleId = groupVo.getRoleId();
            FeignJson feignJson = teamService.deleteFdTeamRoleGroupByTeamIdAndRoleIdAndGroupId(appKey, teamId, roleId, groupId);
            System.err.println(feignJson.isSuccess());
            System.err.println(feignJson.getMsg());
            System.err.println(feignJson.getObj());
        }

        // 删除角色
        for (TeamVo roleVo : roles) {
            String roleId = roleVo.getRoleId();
            teamService.deleteFdTeamRoleUserByTeamIdAndRoleId(teamId, roleId);
        }

        if (!CommonUtil.isEmpty(delManagerList)) {
            // 删除重新保存项目经理
            copyManagerNamesToProject(project);
        }
        //判断是否需要调用webservice
        if(StringUtil.isNotEmpty(kddProductTeamType)&&"kddProduct".equals(kddProductTeamType)){

            String delUserIds="";
            //获取项目下所有团队的人员 删除之后的团队成员
            List<TSUserDto>  tSUserList=getUserInProject(projectId);
            for (TSUserDto tsUserOld : tSUserListOld) {
                boolean flagIsDel=true;
                for (TSUserDto tsUserNew : tSUserList) {
                    //判断删除之后去除两个集合的交集
                    if(tsUserOld.getId().equals(tsUserNew.getId())){
                        flagIsDel=false;
                        break;
                    }
                }
                if(flagIsDel){
                    delUserIds+=","+tsUserOld.getId();
                }
            }

/*        	//改参数用来判断是否需要删除最近访问产品
        	boolean flag=true;
        	for (TSUser tsUser : tSUserList) {
        		if(user.getId().equals(tsUser.getId())){
        			flag=false;
        			break;
        		}
			}*/
            if(StringUtil.isNotEmpty(delUserIds)){
//                //修改时候操作
//                //保存产品的同时
//                //webService调用相关的方法
//                ProductSupportImplService productSupportImplService=new ProductSupportImplService();
//                //获取webService实例调用方法
//                ProductSupport productSupport=productSupportImplService.getProductSupportImplPort();
//                productSupport.deleteRecentlyByProductIdAndUserId(projectId, delUserIds.substring(1));
            }


        }
        users.clear();
        roles.clear();

    }

    /**
     * Description: <br>合并两个userList;
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param userList1
     * @param userList2
     * @return
     * @see
     */
    private List<TSUserDto> unionUserList(List<TSUserDto> userList1, List<TSUserDto> userList2) {
        List<TSUserDto> users = new ArrayList<TSUserDto>();
        users.addAll(userList1);
        for (TSUserDto user2 : userList2) {
            if (users.contains(user2)
                    || ConfigStateConstants.STOP_KEY.equals(user2.getStatus() + "")) {
                continue;
            }
            users.add(user2);
        }
        return users;
    }

    public Project copyManagerNamesToProject(Project project) {
        String teamId = getTeamIdByProjectId(project.getId());
        if (null != project && StringUtils.isNotBlank(project.getProjectNumber())) {

            List<TSUserDto> managers = teamService.getSysUserListByTeamIdAndRoleCode(teamId,
                    ProjectRoleConstants.PROJ_MANAGER);

            List<FdTeamRoleGroupDto> roleGroups = projRolesServiceI.getFdTeamRoleGroupListByTeamIdAndRoleCode(teamId, ProjectRoleConstants.PROJ_MANAGER);



            String userIdsStr = "";
            if(!CommonUtil.isEmpty(managers)){
                for(TSUserDto user : managers){
                    if(CommonUtil.isEmpty(userIdsStr)){
                        userIdsStr = user.getId();
                    }else{
                        userIdsStr = userIdsStr + "," +user.getId();
                    }
                }
            }

            if(!CommonUtil.isEmpty(roleGroups)){
                for(FdTeamRoleGroupDto dto : roleGroups){
                    List<TSUserDto> roleGroupUsers = userService.getSysUsersByGroupId(appKey,dto.getGroupId());
                    if(!CommonUtil.isEmpty(roleGroupUsers)){
                        for(TSUserDto gUser : roleGroupUsers){
                            if(CommonUtil.isEmpty(userIdsStr)){
                                managers.add(gUser);
                                userIdsStr = gUser.getId();
                            }else{
                                if(!userIdsStr.contains(gUser.getId())){
                                    managers.add(gUser);
                                    userIdsStr = userIdsStr + "," +gUser.getId();
                                }
                            }

                        }
                    }
                }
            }

       //     List<TSGroupDto> groups = groupService.getSysGroupListByTeamIdAndRoleId(appKey,teamId, ProjectRoleConstants.PROJ_MANAGER);

       //     List<TSUserDto> managers = projRoleService.getUserInProject(project.getId());

            List<String> nameList = new ArrayList<String>();
            List<String> idList = new ArrayList<String>();
            for (TSUserDto manager : managers) {
                if (null != manager && !idList.contains(manager.getId())) {
                    idList.add(manager.getId());
                    nameList.add(manager.getRealName() + "-" + manager.getUserName());
                }
            }
            project.setProjectManagers(StringUtils.join(idList, ","));
            project.setProjectManagerNames(StringUtils.join(nameList, ","));
            //手动保存
            sessionFacade.updateEntitie(project);
            //判断是否是KDD与IDS关联的处理
//            List<TSRole> tSRoleList=sessionFacade.findHql("from TSRole where  roleCode = '"+ProjectRoleConstants.PROJ_MANAGER+"'");
//            if(tSRoleList!=null&&tSRoleList.size()==1&&tSRoleList.get(0).getRoleName().equals("总体设计师")){
//                //修改时候操作  项目的项目负责人修改之后相关的产品中的总体设计师也进行修改
//                //保存产品的同时保存项目信息
//                //webService调用相关的方法
//                ProductSupportImplService productSupportImplService=new ProductSupportImplService();
//                //获取webService实例调用方法
//                ProductSupport productSupport=productSupportImplService.getProductSupportImplPort();
//                productSupport.updateKddProductTotalStylistByProjectId(project.getId(), project.getProjectManagers());
//            }
        }
        return project;
    }

    @Override
    public boolean isProjRoleByUserIdAndRoleCode(String projectId, String roleCode, String userId) {
        String teamId = getTeamIdByProjectId(projectId);
        if (StringUtils.isBlank(teamId)) {
            return false;
        }
        boolean isInGroup = projGroupService.isGroupRoleByRoleCodeAndUserId(projectId, roleCode,
                userId);
        boolean isInRole = isProjRoleByUserIdAndRoleCode1(projectId, roleCode, userId);
        if (isInGroup || isInRole) {
            return true;
        }
        return false;
    }

    @Override
    public String getRoleIdByRoleCode(String roleCode) {
        TSRoleDto role = roleService.getRoleByRoleCode(roleCode);
        if (role == null) {
            return "";
        }
        return role.getId();
    }

    @Override
    public String addTeamRoleByCode(String teamId, String roleCode) {
        FdTeamRoleDto fdTeamRole = new FdTeamRoleDto();
        fdTeamRole.setTeamId(teamId);
        fdTeamRole.setRoleCode(roleCode);
        fdTeamRole.setRoleId(getRoleIdByRoleCode(roleCode));
        FeignJson serializable = teamService.addFdTeamRole(fdTeamRole);
        if (serializable != null) {
            return String.valueOf(serializable.getObj());
        }
        else {
            return "";
        }
    }


    private boolean isProjRoleByUserIdAndRoleCode1(String projectId, String roleCode, String userId) {
        String teamId = getTeamIdByProjectId(projectId);
        if (StringUtils.isBlank(teamId)) {
            return false;
        }
        boolean result = false;
        FeignJson fj = teamService.isProjRoleByUserIdAndRoleCode1(teamId,roleCode,userId);
        if (fj.isSuccess()) {
            result = fj.getObj() == null ? false : (boolean) fj.getObj();
        }
        return result;
    }

}
