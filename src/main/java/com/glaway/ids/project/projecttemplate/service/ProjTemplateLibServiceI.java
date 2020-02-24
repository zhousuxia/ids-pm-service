package com.glaway.ids.project.projecttemplate.service;


import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepRoleFileAuthDto;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projecttemplate.entity.ProjTmplTeamLink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目模板文档库service
 * 
 * @author zhousuxia
 * @version 2019年8月20日
 * @see ProjTemplateLibServiceI
 * @since
 */
public interface ProjTemplateLibServiceI {


    /**
     * 根据项目模板Id查询存储库Id
     * @param templateId 模板id
     * @return
     * @see
     */
    String getLibIdByTemplateId(String templateId);

    /**
     * 查询项目模板关系
     *
     * @param projTmplTeamLink 项目模板关联关系
     * @return
     * @see
     */
    List<ProjTmplTeamLink> searchProjTmplTeamLink(ProjTmplTeamLink projTmplTeamLink);

    /**
     * 判断文件夹角色权限列表的长度和合法性
     * @param roleFileAuthsList 库结构与库角色权限映射
     * @return
     * @see
     */
    boolean judgeAndAddValidRoleAuthSize(List<RepRoleFileAuthDto> roleFileAuthsList);


    /**
     * 文件夹角色权限列表转为对应的视图对象列表
     *
     * @param roleFileAuthsList 库结构与库角色权限映射
     * @return
     * @see
     */
    List<ProjLibRoleFileAuthVo> convertProjTemplateRoleFileAuthsVO(List<RepRoleFileAuthDto> roleFileAuthsList);

    /**
     * 项目库模板角色目录权限应用到项目库模板对应的项目库角色目录权限
     *
     * @param projTmpId 项目模板Id
     * @param libTmpId  项目库模板Id
     * @param roles 项目模板团队角色列表
     * @return
     * @see
     */
    Map<String, List<RepRoleFileAuthDto>> getTemplateRoleAuths(String projTmpId, String libTmpId, List<TSRoleDto> roles);


    /**
     * 项目库模板角色目录权限应用到项目库模板对应的项目库角色目录权限
     *
     * @param templateId 项目模板Id
     * @param libId 项目库模板Id
     * @param roles 项目模板团队角色列表
     * @return
     */
    void applyTemplate(String templateId, Map<String, List<RepRoleFileAuthDto>> roles, String libId, TSUserDto user,String orgId);

    /**
     * 复制项目库文件夹到项目模板的项目库文件夹中
     * @param newLibId 新项目库id
     * @param oldLibId 旧项目库id
     * @param type     项目类型
     * @param userId   用户id
     * @return
     */
    HashMap<String, String> copyProjLibFolders(String newLibId, String oldLibId, String type,String userId);

    /**
     * 根据存储库Id查询项目模板Id
     * @param libId 存储库id
     * @return
     * @see
     */
    String getTemplateIdByLibId(String libId);

    /**
     * 批量复制项目库目录文件夹
     * @param projectId   项目Id（源）
     * @param templateId    项目模板Id（目标）
     * @return
     * @see
     */
    HashMap<String, String> batchInitSaveLib(String projectId, String templateId,TSUserDto userDto);

    /**
     *  复制项目库文件夹到项目模板的项目库文件夹中
     * @param newLibId
     * @param oldLibId
     * @param type
     * @return
     * @see
     */
    HashMap<String, String> copyProjLibFoldersForSaveASTemplate(String newLibId, String oldLibId, String type,TSUserDto userDto);

    /**
     * 创建项目库文件夹
     *
     * @param repFile 项目库文档
     * @param userId  用户id
     * @return
     */
    String createFile(RepFileDto repFile,String userId);
}
