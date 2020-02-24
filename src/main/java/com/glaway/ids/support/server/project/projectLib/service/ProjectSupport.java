package com.glaway.ids.support.server.project.projectLib.service;

import com.glaway.foundation.common.exception.GWException;
import com.glaway.ids.project.projectmanager.vo.ProjVo;

import java.util.List;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: ProjectSupport
 * @Date: 2019/7/19-14:01
 * @since
 */
public interface ProjectSupport {

    /**
     * Description: <br>
     * 根据项目id获取项目信息<br>
     *
     * @param id
     * @return
     * @throws GWException
     * @see
     */
    String findProjectInfo(String id) throws GWException;

    /**
     * 获取项目库权限<br>
     * @param fileId
     * @param projectId
     * @param status
     * @param isExpert
     * @param userId
     * @return
     * @throws GWException
     */
    String getDocumentPower(String fileId,String projectId,String status,String isExpert,String userId)throws GWException;

    /**
     * 通过条件查询项目库
     * @param projectId      项目id
     * @param folderId       文件夹id
     * @param securityLevel  安全等级
     * @return
     * @throws GWException
     */
    String getDocumentList(String projectId,String folderId,Short securityLevel)throws GWException;

    /**
     * Description: <br>
     *储存评审报告<br>
     *
     * @return
     * @throws GWException
     * @see
     */
    void saveReviewReport(String projectId,String securityLevel,String path,String pathName,String docName,String remark,String users,String pathid,String docTypeId)throws GWException;

     /**
     * 通过条件查询项目库
     * @param id
     * @param havePower
     * @param userId
     * @return
     * @throws GWException
     */
    String operationProject(String id, String havePower,String userId)throws GWException;

    /**
     *获取人员
     * @param projectId
     * @return
     */
    String findProjectUsersList(String projectId) throws GWException;

    /**
     * 获取项目列表vo
     * @return
     */
    List<ProjVo> getAllProjectList();

    /**
     * 获取项目列表
     * @return
     */
    List<ProjVo> findProjectListForRP();


}
