/*
 * 文件名：ProjRoleServiceI.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：wangshen
 * 修改时间：2015年5月13日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.service;


import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.menu.entity.ProjTeamLink;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.vo.TeamVo;

import java.util.List;


public interface ProjRoleServiceI {

    /**
     * 判断某一人是否属于某一个系统角色
     *
     * @param userId   用户id
     * @param roleCode 角色编码
     * @return
     * @see
     */
    boolean isSysRoleByUserIdAndRoleCode(String userId, String roleCode);

    /**
     * 保存项目团队关联表
     *
     * @param projectId 项目id
     * @param teamId    团队id
     * @param libId     项目库id
     * @param userDto   用户信息
     * @param orgId     组织id
     * @return
     */
    String saveTeamLink(String projectId, String teamId, String libId,TSUserDto userDto,String orgId);


    /**
     * Description: <br>根据项目id获得项目库id
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param projectId 项目id
     * @return
     * @see
     */
    String getLibIdByProjectId(String projectId);



    /**
     * Description: <br>条件查询 项目、团队、项目库 关联表
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param projTeamLink 项目团队关联信息
     * @return
     * @see
     */
    List<ProjTeamLink> searchProjTeamLink(ProjTeamLink projTeamLink);


    /**
     * 先删除后保存项目经理
     *
     * @param managerIds 项目经理id
     * @param projectId  项目id
     * @param type       文档类型
     */
    void reSaveManager(String[] managerIds, String projectId,String type);

    /**
     * Description: <br>根据项目id获得团队id
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param projectId 项目id
     * @return
     * @see
     */
    String getTeamIdByProjectId(String projectId);


    /**
     * Description: <br>根据团队id获得项目id
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param teamId 团队id
     * @return
     * @see
     */
    String getProjectIdByTeamId(String teamId);


    /**
     * 批量删除角色与成员
     * @param users              用户
     * @param roles              角色
     * @param groups             组织
     * @param teamId             团队id
     * @param p                  项目
     * @param delManagerList     项目经理
     * @param kddProductTeamType kdd项目团队类型
     * @param projectId          项目id
     * @param userId             用户id
     */
    void BatchDelRoleAndUsers(List<TeamVo> users, List<TeamVo> roles, List<TeamVo> groups,
                              String teamId, Project p, List<TeamVo> delManagerList, String kddProductTeamType, String projectId, String userId);

    /**
     * Description: <br>判断用户是否是项目的某一角色
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param projectId 项目id
     * @param roleCode  角色编码
     * @param userId    用户id
     * @return
     * @see
     */
    boolean isProjRoleByUserIdAndRoleCode(String projectId, String roleCode, String userId);


    /**
     * Description: <br>根据角色code获得角色id
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param roleCode 角色编码
     * @return
     * @see
     */
    String getRoleIdByRoleCode(String roleCode);


    /**
     * Description: <br>根据团队ID和角色code添加角色
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param teamId   团队id
     * @param roleCode 角色编码
     * @return
     * @see
     */
    String addTeamRoleByCode(String teamId, String roleCode);


    /**
     * Description: <br>增加团队角色用户
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param teamId   团队id
     * @param roleCode 角色编码
     * @param userId   用户id
     * @see
     */
    void addRoleUser(String teamId, String roleCode, String userId);

    /**
     * 根据项目的teamId获取该项目团队下的团队
     * @param teamId 团队id
     * @return
     */
    List<TSUserDto> getUserInProjectByTeamId(String projectId, String teamId);


    /**
     * Description: <br>获得项目下的所有系统人员
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param projectId 项目id
     * @return
     * @see
     */
    List<TSUserDto> getUserInProject(String projectId);

    /**
     * Description: <br>根据库Id获得项目Id
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param libId 项目库id
     * @return
     * @see
     */
    String getProjectIdByLibId(String libId);

    /**
     * 添加用户组
     * @param teamId   团队id
     * @param roleCode 角色编码
     * @param id       用户组id
     */
    void addRoleGroup(String teamId, String roleCode, String id);

    /**
     * 根据角色编号获取团队角色id
     * @param teamId   团队id
     * @param roleCode 角色编码
     * @return
     */
    String getTeamRoleIdByRoleCode(String teamId, String roleCode);

}
