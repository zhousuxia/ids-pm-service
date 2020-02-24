/*
 * 文件名：ProjGroupServiceI.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：wangshen
 * 修改时间：2015年5月20日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.service;


import com.glaway.foundation.common.dto.TSGroupDto;
import com.glaway.foundation.common.dto.TSUserDto;

import java.util.List;



public interface ProjGroupServiceI {
   /**
    * 判断某人是否在某个项目的角色组下
    * @param projectId 项目id
    * @param roleCode  角色编码
    * @param userId    用户id
    * @return
    */
    boolean isGroupRoleByRoleCodeAndUserId(String projectId, String roleCode, String userId);


    /**
     * 获得team里所有组里的人
     *
     * @param teamId 团队id
     * @return
     * @see
     */
    List<TSUserDto> getUnderGoupPartUsersByTeamId(String teamId);


    /**
     * 通过团队id与角色编码获取用户组
     * @param teamId   团队id
     * @param roleCode 角色编码
     * @return
     */
    List<TSGroupDto> getSysGroupListByTeamIdAndRoleCode(String teamId, String roleCode);
}
