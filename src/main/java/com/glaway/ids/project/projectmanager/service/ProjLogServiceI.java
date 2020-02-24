package com.glaway.ids.project.projectmanager.service;


import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.entity.ProjLog;


/**
 * 〈项目日志服务接口〉
 * 〈功能详细描述〉
 * @author Songjie
 * @version 2015年7月22日
 * @see ProjLogServiceI
 * @since
 */
public interface ProjLogServiceI extends BusinessObjectServiceI<ProjLog> {

    /**
     * 保存项目日志
     * @param projectNumber 项目编号
     * @param projectId     项目id
     * @param logInfo       日志信息
     * @param remark        备注
     * @param userDto       用户信息
     * @return
     */
    String saveProjLog(String projectNumber, String projectId, String logInfo, String remark, TSUserDto userDto);

    /**
     * 文件流程状态
     *
     * @param project       项目对象
     * @param projectStatus 流程状态
     */
    void setLibProjectStatus(Project project, String projectStatus);

    /**
     * 保存项目日志
     *
     * @param projectNumber 项目编号
     * @param projectId     项目id
     * @param logInfo       日志信息
     * @param remark        备注
     * @return
     */
    String saveProjLog(String projectNumber,String projectId, String logInfo, String remark);

    /**
     * Description: <br>删除项目日志
     *
     * @param projectId 项目id
     * @return
     * @see
     */
    void deleteProjLog(String projectId);

    /**
     * 保存项目日志
     * @param projLog 项目日志
     */
    void save(ProjLog projLog);
}
