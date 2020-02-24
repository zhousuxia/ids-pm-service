/*
 * 文件名：ProjLibAuthServiceI.java
 * 版权：Copyright by www.glaway.com
 * 描述：项目库权限处理
 * 修改人：blcao
 * 修改时间：2016年7月6日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.service;


import java.util.Map;


/**
 * 项目库权限处理
 * 
 * @author blcao
 * @version 2016年7月6日
 * @see ProjLibAuthServiceI
 * @since
 */
public interface ProjLibAuthServiceI {
    /**
     * 获取用户对目录的权限
     *
     * @param fileId
     *            目录ID
     * @param userId
     *            用户ID
     * @return
     */
    String getCategoryFileAuths(String fileId, String userId);

    /**
     * 获取用户对文档的权限
     * 
     * @param fileId
     *            文档ID
     * @param userId
     *            用户ID
     * @return
     */
    String getDocumentFileAuths(String fileId, String userId);


    /**
     * 获取目录配置或者继承的权限
     *
     * @param fileId 文件夹ID
     * @param userId 用户ID
     * @return
     */
    Map<String, String> getFolderAuthMap(String fileId, String userId);


    /**
     * 获取某目录下用户文档生命周期的权限
     *
     * @return
     */
    Map<String, String> getRepFileLifeCycleAuths();

    /**
     *
     * 删除文件夹和其角色目录权限关系
     * @param fileId 文档ID
     * @return
     * @see
     */
    boolean delFileAndAuthById(String fileId);


}
