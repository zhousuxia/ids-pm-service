/*
 * 文件名：ProjLibAuthServiceImpl.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：blcao
 * 修改时间：2016年7月6日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepRoleFileAuthDto;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.ids.config.auth.ProjLibAuthManager;
import com.glaway.ids.config.auth.ProjectLibraryAuthorityEnum;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.project.menu.entity.ProjTeamLink;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePermission;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.foundation.system.lifecycle.service.LifeCyclePermissionServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibAuthServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;


@Service("projLibAuthService")
@Transactional
public class ProjLibAuthServiceImpl extends CommonServiceImpl implements ProjLibAuthServiceI {

    /**
     *  sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;
    
    /**
     *  项目库Service
     */
    @Autowired
    private ProjLibServiceI projLibService;



    @Autowired
    private FeignRepService repService;


    @Value(value="${spring.application.name}")
    private String appKey;



    /**
     * 注入生命周期权限策略服务接口<br>
     */
    @Autowired
    private LifeCyclePermissionServiceI permissionService;

    @Override
    public String getCategoryFileAuths(String fileId, String userId) {
        String categoryFileAuths = "";
        RepFileDto repFile = repService.getRepFileByRepFileId(appKey,fileId);
        if (repFile != null) {
            // 获取目录所属的存储库
            String libId = repFile.getLibId();
            boolean isSetAuth = checkLibSetAuth(libId);
            Map<String, String> authMap = new HashMap<String, String>();
            if (isSetAuth) {
                authMap = getFolderAuthMap(fileId, userId);
            }
            else {
                // 如不设置项目库权限，默认规则生效，团队所有角色都可以新增、上传、查看、下载
                ProjectLibraryAuthorityEnum[] authArray = ProjectLibraryAuthorityEnum.values();
                for (ProjectLibraryAuthorityEnum auth : authArray) {
                    if ("list".equals(auth.getActionCode())
                            || "detail".equals(auth.getActionCode())
                            || "create".equals(auth.getActionCode())
                            || "download".equals(auth.getActionCode())
                            || "upload".equals(auth.getActionCode())
                            || "history".equals(auth.getActionCode())) {
                        authMap.put(auth.getActionCode(), auth.getActionCode());
                    }
                }
            }
            if (!CommonUtil.isEmpty(authMap)) {
                Set<String> mapKeys = authMap.keySet();
                // 将权限以","连接起来组成字符串
                StringBuffer categoryFileAuthBuffer = new StringBuffer("");
                for (String str : mapKeys) {
                    if (StringUtils.isNotEmpty(categoryFileAuthBuffer.toString())) {
                        categoryFileAuthBuffer.append(",").append(str);
                    }
                    else {
                        categoryFileAuthBuffer.append(str);
                    }
                }
                categoryFileAuths = categoryFileAuthBuffer.toString();
            }
        }
        return categoryFileAuths;
    }

    @Override
    public String getDocumentFileAuths(String fileId, String userId) {

        String documentFileAuths = "";
        RepFileDto docFile = repService.getRepFileByRepFileId(appKey, fileId);
        if (docFile != null && StringUtils.isNotEmpty(docFile.getParentId())) {
            String libId = docFile.getLibId();
            boolean isSetAuth = checkLibSetAuth(libId);
            Map<String, String> authMap = new HashMap<String, String>();
            if(isSetAuth){
                authMap = getFolderAuthMap(docFile.getParentId(), userId);
            }
            else{
                if (CommonUtil.isEmpty(authMap)) {
                    if (userId.equals(docFile.getCreateBy())) {
                        ProjectLibraryAuthorityEnum[] authArray = ProjectLibraryAuthorityEnum.values();
                        for (ProjectLibraryAuthorityEnum auth : authArray) {
                            authMap.put(auth.getActionCode(), auth.getActionCode());
                        }
                    }
                    else {
                        ProjectLibraryAuthorityEnum[] authArray = ProjectLibraryAuthorityEnum.values();
                        for (ProjectLibraryAuthorityEnum auth : authArray) {
                            if ("list".equals(auth.getActionCode())
                                || "detail".equals(auth.getActionCode())
                                || "create".equals(auth.getActionCode())
                                || "download".equals(auth.getActionCode())
                                || "upload".equals(auth.getActionCode())
                                || "history".equals(auth.getActionCode())) {
                                authMap.put(auth.getActionCode(), auth.getActionCode());
                            }
                        }
                    }
                }
            }
            Map<String, String> fileLifeCycleAuthMap = getRepFileLifeCycleAuths();
            String docBizcurrentAuths = fileLifeCycleAuthMap.get(docFile.getBizCurrent());
            StringBuffer authBuffer = new StringBuffer("");
            String[] docBizcurrentAuthArr = docBizcurrentAuths.split(",");
            for (String str : docBizcurrentAuthArr) {
                if (StringUtils.isNotEmpty(authMap.get(str))) {
                    if (StringUtils.isNotEmpty(authBuffer.toString())) {
                        authBuffer.append(",").append(str);
                    }
                    else {
                        authBuffer.append(str);
                    }
                }
            }
            documentFileAuths = authBuffer.toString();
        }
        return documentFileAuths;
    }


    /**
     * 判断项目库是否设置过权限
     *
     * @param libId
     * @return
     */
    private boolean checkLibSetAuth(String libId) {
        // 获取fileId所在的存储库的权限数据

        List<RepRoleFileAuthDto> roleAuthlist = repService.getRepRoleFileAuthListBylibId(appKey,libId);
        String authFileId = "";
        Map<String, List<RepRoleFileAuthDto>> fileAuthMap = new HashMap<String, List<RepRoleFileAuthDto>>();
        boolean isSetAuth = false;
        if (!CommonUtil.isEmpty(roleAuthlist)) {
            for (RepRoleFileAuthDto auth : roleAuthlist) {
                List<RepRoleFileAuthDto> auths = new ArrayList<RepRoleFileAuthDto>();
                if (CommonUtil.isEmpty(fileAuthMap.get(auth.getFileId()))) {
                    auths.add(auth);
                    fileAuthMap.put(auth.getFileId(), auths);
                }
                else {
                    auths = fileAuthMap.get(auth.getFileId());
                    auths.add(auth);
                    fileAuthMap.put(auth.getFileId(), auths);
                }
                if (StringUtils.isNotEmpty(auth.getPermissionCode())
                        && !"0".equals(auth.getPermissionCode()) && !isSetAuth) {
                    isSetAuth = true;
                }
            }
        }
        return isSetAuth;
    }



    @Override
    public Map<String, String> getFolderAuthMap(String fileId, String userId) {
        Map<String, String> authMap = new HashMap<String, String>();
        RepFileDto repFile = repService.getRepFileByRepFileId(appKey,fileId);
        if (repFile != null) {
            // 获取目录所属的存储库
            String libId = repFile.getLibId();
            List<ProjTeamLink> projTeamLinks = findByProperty(ProjTeamLink.class, "libId", libId);
            if (!CommonUtil.isEmpty(projTeamLinks)) {
                // 获取存储库所对应的组
                String teamId = projTeamLinks.get(0).getTeamId();
                // 获取用户所属的角色list
                StringBuffer roleHqlBuffer = new StringBuffer();
                roleHqlBuffer.append(" select tu.roleid ROLEID");
                roleHqlBuffer.append(" from fd_team_role_user tu ");
                roleHqlBuffer.append(" where tu.userid = '" + userId + "' ");
                roleHqlBuffer.append(" and tu.teamid = '" + teamId + "' ");
                roleHqlBuffer.append(" union ");
                roleHqlBuffer.append(" select tg.roleid ROLEID");
                roleHqlBuffer.append(" from fd_team_role_group tg ");
                roleHqlBuffer.append(" join t_s_group_user gu on gu.groupid = tg.groupid ");
                roleHqlBuffer.append(" where gu.userid = '" + userId + "' ");
                roleHqlBuffer.append(" and tg.teamid = '" + teamId + "' ");
                List<Map<String, Object>> roleArrayList = this.sessionFacade.findForJdbc(roleHqlBuffer.toString());
                List<String> roleList = new ArrayList<String>();
                if (!CommonUtil.isEmpty(roleArrayList)) {
                    for (Map<String, Object> map : roleArrayList) {
                        roleList.add((String)map.get("ROLEID"));
                    }
                }
                // 获取fileId所在的存储库的权限数据

                List<RepRoleFileAuthDto> roleAuthlist = repService.getRepRoleFileAuthListBylibId(appKey,libId);
                String authFileId = "";
                Map<String, List<RepRoleFileAuthDto>> fileAuthMap = new HashMap<String, List<RepRoleFileAuthDto>>();
                boolean isSetAuth = false;
                if (!CommonUtil.isEmpty(roleAuthlist)) {
                    for (RepRoleFileAuthDto auth : roleAuthlist) {
                        List<RepRoleFileAuthDto> auths = new ArrayList<RepRoleFileAuthDto>();
                        if (CommonUtil.isEmpty(fileAuthMap.get(auth.getFileId()))) {
                            auths.add(auth);
                            fileAuthMap.put(auth.getFileId(), auths);
                        }
                        else {
                            auths = fileAuthMap.get(auth.getFileId());
                            auths.add(auth);
                            fileAuthMap.put(auth.getFileId(), auths);
                        }
                        if (StringUtils.isNotEmpty(auth.getPermissionCode())
                                && !"0".equals(auth.getPermissionCode()) && !isSetAuth) {
                            isSetAuth = true;
                        }
                    }
                }
                if (isSetAuth) {
                    if (!CommonUtil.isEmpty(fileAuthMap.get(fileId))) {
                        for (RepRoleFileAuthDto auth : fileAuthMap.get(fileId)) {
                            // 判断该目录是否设置了权限
                            if (StringUtils.isNotEmpty(auth.getPermissionCode())
                                    && !"0".equals(auth.getPermissionCode())) {
                                authFileId = auth.getFileId();
                                break;
                            }
                        }
                    }
                    if (StringUtils.isEmpty(authFileId)) {
                        String idPath = "";
                        StringBuffer hqlBuffer = new StringBuffer();
                        hqlBuffer.append("select category.id ID, sys_connect_by_path(category.id, '/') IDPATH");
                        hqlBuffer.append(" from (select f.id, f.parentid, f.filename, f.filetype");
                        hqlBuffer.append(" from rep_file f");
                        hqlBuffer.append(" where f.Libid = '" + libId + "') category");
                        hqlBuffer.append(" where category.id = '" + fileId + "'");
                        hqlBuffer.append(" and category.filetype = 0");
                        hqlBuffer.append(" start with category.parentid = '" + libId + "'");
                        hqlBuffer.append(" connect by category.parentid = prior category.id");
                        hqlBuffer.append(" order by category.id");
                        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
                        if (!CommonUtil.isEmpty(objArrayList)) {
                            for (Map<String, Object> map : objArrayList) {
                                idPath = (String)map.get("IDPATH");
                                break;
                            }
                            List<String> pathList = new ArrayList<String>();
                            if (StringUtils.isNotEmpty(idPath)) {
                                String[] idPathArr = idPath.split("/");
                                for (int i = idPathArr.length; i > 0; i-- ) {
                                    if (!fileId.equals(idPathArr[i - 1])
                                            && StringUtils.isNotEmpty(idPathArr[i - 1])) {
                                        pathList.add(idPathArr[i - 1]);
                                    }
                                }
                            }
                            if (!CommonUtil.isEmpty(pathList)) {
                                pathLoop : for (int i=0;i<pathList.size();i++) {
                                    if(!CommonUtil.isEmpty(fileAuthMap.get(pathList.get(i)))){
                                        for (RepRoleFileAuthDto auth : fileAuthMap.get(pathList.get(i))) {
                                            if (StringUtils.isNotEmpty(auth.getPermissionCode())
                                                    && !"0".equals(auth.getPermissionCode())) {
                                                authFileId = auth.getFileId();
                                                break pathLoop;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(authFileId)) {
                        // 将角色权限放到authMap中
                        for (RepRoleFileAuthDto auth : fileAuthMap.get(authFileId)) {
                            if (roleList.contains(auth.getRoleId())) {
                                List<String> actionCodes = ProjLibAuthManager.decodePermissionCode(auth.getPermissionCode());
                                for (String str : actionCodes) {
                                    authMap.put(str, str);
                                }
                            }
                        }
                    }
                }
            }
        }
        return authMap;
    }


    @Override
    public Map<String, String> getRepFileLifeCycleAuths() {
        Map<String, String> fileLifeCycleAuthMap = new HashMap<String, String>();
        List<LifeCyclePolicy> lifeCyclePolicyEntityList = sessionFacade.findHql(
                "from LifeCyclePolicy where name = ? ", CommonConstants.REPFILE_LIFECYCLE_POLICY_NAME);
        if (!CommonUtil.isEmpty(lifeCyclePolicyEntityList)) {
            String policyId = lifeCyclePolicyEntityList.get(0).getId();
            List<LifeCycleStatus> lifeCycleStatusEntityList = sessionFacade.findHql(
                    "from LifeCycleStatus where 1 = 1 AND lifeCyclePolicy.id = ? ", policyId);
            // 获取文档生命周期权限
            List<LifeCyclePermission> permissionList = permissionService.getPermissionListByPolicyId(
                    policyId, RepFileDto.class);
            // 取文档生命周期权限与人员目录权限交集
            for (LifeCyclePermission permission : permissionList) {
                List<String> actionCodes = ProjLibAuthManager.decodePermissionCode(permission.getPermissionCode());
                StringBuffer authBuffer = new StringBuffer("");
                for (String str : actionCodes) {
                    if (StringUtils.isNotEmpty(authBuffer.toString())) {
                        authBuffer.append(",").append(str);
                    }
                    else {
                        authBuffer.append(str);
                    }
                }
                fileLifeCycleAuthMap.put(permission.getStatusName(), authBuffer.toString());
            }
        }
        return fileLifeCycleAuthMap;

    }

    @Override
    public boolean delFileAndAuthById(String fileId) {
        boolean result = false;
        if(StringUtils.isEmpty(fileId)) {
            return false;
        }
        result = projLibService.delFileById(fileId);
        if(result){
            repService.deleteRepRoleFileAuthsByFileId(appKey,fileId);
        }
        return result;
    }
}
