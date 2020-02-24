package com.glaway.ids.project.projectmanager.service;

import com.glaway.foundation.common.dto.FdTeamRoleGroupDto;
import com.glaway.foundation.common.dto.FdTeamRoleUserDto;
import com.glaway.foundation.common.dto.TSGroupDto;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.projectmanager.vo.GroupUserDetailVo;
import com.glaway.ids.project.projectmanager.vo.TeamVo;

import java.util.List;

/**
 * Created by LHR on 2019/7/26.
 */
public interface ProjRolesServiceI {

    /**
     * 根据项目的teamId获取该项目团队下的用户
     * @param teamId 团队id
     * @return
     */
    List<TeamVo> getSysUserListByTeamId(String teamId);

    /**
     * 根据项目的teamId获取该项目团队下的团队
     * @param teamId 团队id
     * @return
     */
    List<TeamVo> getSysGroupListByTeamId(String teamId);

    /**
     * 根据teamId和roleCode获得系统组
     * @param teamId   团队id
     * @param roleCode 角色编码
     * @return
     */
    List<TSGroupDto> getSysGroupListByTeamIdAndRoleCode(String teamId, String roleCode);

    /**
     * 根据teamId和角色Code获得组
     * @param teamId   团对id
     * @param roleCode 角色编码
     * @return
     */
    List<FdTeamRoleGroupDto> getFdTeamRoleGroupListByTeamIdAndRoleCode(String teamId, String roleCode);

    /**
     * 根据团队id获取该项目团队下的团队
     * @param groupId 组织id
     * @return
     */
    List<GroupUserDetailVo> getGroupUserDetailVoList(String groupId);
}
