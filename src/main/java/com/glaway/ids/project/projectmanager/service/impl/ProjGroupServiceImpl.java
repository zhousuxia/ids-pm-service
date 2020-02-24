package com.glaway.ids.project.projectmanager.service.impl;


import java.util.ArrayList;
import java.util.List;

import com.glaway.foundation.common.dto.FdTeamRoleGroupDto;
import com.glaway.foundation.common.dto.TSGroupDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.fdk.dev.service.threemember.FeignGroupService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignTeamService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.config.constant.ConfigStateConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.ids.project.projectmanager.service.ProjGroupServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;


@Service("projGroupService")
@Transactional
public class ProjGroupServiceImpl extends CommonServiceImpl implements ProjGroupServiceI {

    private static final SystemLog log = BaseLogFactory.getSystemLog(ProjGroupServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;



    @Autowired
    private ProjRoleServiceI projRoleService;


    @Autowired
    private FeignGroupService groupService;


    @Autowired
    private FeignUserService userService;


    @Autowired
    private FeignTeamService teamService;

    @Value(value="${spring.application.name}")
    private String appKey;




    @Override
    public boolean isGroupRoleByRoleCodeAndUserId(String projectId, String roleCode, String userId) {
        // String groupId = getGroupIdByGroupCode(groupCode);
        String roleId = projRoleService.getRoleIdByRoleCode(roleCode);
        String teamId = projRoleService.getTeamIdByProjectId(projectId);
        if (StringUtils.isBlank(teamId) || StringUtils.isBlank(roleId)
            || StringUtils.isBlank(userId)) {
            return false;
        }


       List<TSGroupDto> groups = groupService.getSysGroupListByTeamIdAndRoleId(appKey,teamId, roleCode);

        for (TSGroupDto group : groups) {

            List<TSUserDto> users = userService.getSysUsersByGroupId(appKey,group.getId());
            for (TSUserDto user : users) {
                if (userId.equals(user.getId())) {
                    return true;
                }
            }

        }
        return false;

    }

    @Override
    public List<TSUserDto> getUnderGoupPartUsersByTeamId(String teamId) {
        List<FdTeamRoleGroupDto> roleGroups = teamService.getFdTeamRoleGroupListByTeamIdAndRoleId(appKey,teamId,"");
        List<TSUserDto> users = new ArrayList<TSUserDto>();
        for (FdTeamRoleGroupDto rGroup : roleGroups) {
            if (StringUtils.isNotEmpty(rGroup.getGroupId())) {
                List<TSUserDto> gUsers = userService.getSysUsersByGroupId(appKey,rGroup.getGroupId());
                for (TSUserDto user : gUsers) {
                    if (users.contains(user)
                            || ConfigStateConstants.STOP_KEY.equals(user.getStatus() + "")) {
                        continue;
                    }
                    users.add(user);
                }
            }
        }
        return users;
    }

    @Override
    public List<TSGroupDto> getSysGroupListByTeamIdAndRoleCode(String teamId, String roleCode) {
        String roleId = projRoleService.getRoleIdByRoleCode(roleCode);

        List<FdTeamRoleGroupDto> roleGroups = teamService.getFdTeamRoleGroupListByTeamIdAndRoleId(appKey,teamId, roleId);
        List<TSGroupDto> groups = new ArrayList<>();
        for (FdTeamRoleGroupDto roleGroup : roleGroups) {
            //TODo
//            TSGroupDto group = groupService.getGroupById(roleGroup.getGroupId());
//            if (group != null) {
//                groups.add(group);
//            }
        }
        return groups;
    }


}
