package com.glaway.ids.project.projecttemplate.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.BeanUtil;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.core.common.extend.hqlsearch.HqlGenerateUtil;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepRoleFileAuthDto;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignRoleService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.config.auth.ProjLibAuthManager;
import com.glaway.ids.config.auth.ProjectLibraryAuthorityEnum;
import com.glaway.ids.config.entity.ProjectLibRoleFileAuth;
import com.glaway.ids.config.entity.ProjectLibTemplateFileCategory;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.project.projecttemplate.entity.ProjTmplTeamLink;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateLibServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateRoleServiceI;


/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * 
 * @author wangshen
 * @version 2015年3月24日
 * @see ProjTemplateLibServiceImpl
 * @since
 */
@Service("projTemplateLibService")
@Transactional
public class ProjTemplateLibServiceImpl extends CommonServiceImpl implements ProjTemplateLibServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(ProjTemplateLibServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    /**
     * 项目角色接口
     */
    @Autowired
    private ProjRoleServiceI projRoleService;


    @Autowired
    private FeignRoleService roleService;

    
    /**
     *  项目模板角色接口
     */
    @Autowired
    private ProjTemplateRoleServiceI projTemplateRoleService;


    @Autowired
    private ProjTemplateServiceI projTemplateService;


    @Autowired
    private FeignRepService repService;

    @Value(value="${spring.application.name}")
    private String appKey;

    /**
     *  项目库接口
     */
    @Autowired
    private ProjLibServiceI projLibService;

    @Override
    public String getLibIdByTemplateId(String templateId) {
        if (StringUtils.isNotEmpty(templateId)) {
            ProjTmplTeamLink projTmplTeamLink = new ProjTmplTeamLink();
            projTmplTeamLink.setProjTemplateId(templateId);
            List<ProjTmplTeamLink> links = searchProjTmplTeamLink(projTmplTeamLink);
            if (!CommonUtil.isEmpty(links)) {
                return links.get(0).getLibId();
            }
        }
        return "";
    }

    @Override
    public List<ProjTmplTeamLink> searchProjTmplTeamLink(ProjTmplTeamLink projTmplTeamLink) {
        CriteriaQuery cq = new CriteriaQuery(ProjTmplTeamLink.class);
        HqlGenerateUtil.installHql(cq, projTmplTeamLink, null);
        return sessionFacade.getListByCriteriaQuery(cq, false);
    }

    @Override
    public boolean judgeAndAddValidRoleAuthSize(List<RepRoleFileAuthDto> roleFileAuthsList) {
        List<TSRoleDto> roleList = roleService.getCommonRole();
        Boolean b=true;
        if(roleList.size()==roleFileAuthsList.size()){
            b= true;
        }else {
            List<RepRoleFileAuthDto> addRoleFileAuths=new ArrayList<RepRoleFileAuthDto>();
            int mm=roleFileAuthsList.size();
            outer: for(TSRoleDto r: roleList)
            {
                int i=0;
                for(RepRoleFileAuthDto auth: roleFileAuthsList){
                    if(r.getId().equals(auth.getRoleId()))
                    {
                        continue outer;
                    }else{
                        if((i++)==mm) {
                            RepRoleFileAuthDto sub=new RepRoleFileAuthDto();
                            CommonUtil.glObjectSet(sub);
                            sub.setFileId(auth.getFileId());
                            sub.setRoleId(r.getId());
                            sub.setPermissionCode("0");
                            addRoleFileAuths.add(sub);
                        }
                    }
                }
            }
            batchSave(addRoleFileAuths);
            b= false;
        }
        return b;
    }

    @Override
    public List<ProjLibRoleFileAuthVo> convertProjTemplateRoleFileAuthsVO(List<RepRoleFileAuthDto> roleFileAuthsList) {
        List<ProjLibRoleFileAuthVo> roleAuthList = new ArrayList<ProjLibRoleFileAuthVo>();
        for (RepRoleFileAuthDto roleFileAuth : roleFileAuthsList) {
            ProjLibRoleFileAuthVo vo = new ProjLibRoleFileAuthVo();
            vo.setId(roleFileAuth.getId());
            vo.setFileId(roleFileAuth.getFileId());
            vo.setRoleId(roleFileAuth.getRoleId());

            TSRoleDto roleByRoleId = roleService.getRoleByRoleId(roleFileAuth.getRoleId());
            List<TSRoleDto> allRoleList = roleService.getCommonRole();
            if(!CommonUtil.isEmpty(allRoleList)){
                for(TSRoleDto role :allRoleList ){
                    if(role.getId().equals(roleFileAuth.getRoleId())){
                        roleByRoleId = role;
                        break;
                    }
                }
            }
            vo.setRoleName(roleByRoleId.getRoleName());
            long permissionCode = 0;
            if (StringUtils.isNotEmpty(roleFileAuth.getPermissionCode())) {
                permissionCode = Long.valueOf(roleFileAuth.getPermissionCode());
            }
            // 将其权限信息由十进制值转为对应的权限code及是否为true的Map
            Map<String, Boolean> authMap = new HashMap<String, Boolean>();
            Map<ProjectLibraryAuthorityEnum, Boolean> map = ProjLibAuthManager.getActionMapByPermissionCode(permissionCode);
            for (ProjectLibraryAuthorityEnum authEnum : map.keySet()) {
                authMap.put(authEnum.getActionCode(), map.get(authEnum));
            }
            vo.setAuthMap(authMap);
            roleAuthList.add(vo);
        }
        // FIXME 1

       /* List<TSRole> roleList = projectLibTemplateService.getAllValidRoles();
       outer:  for(TSRole r: roleList) {
            for (RepRoleFileAuth roleFileAuth : roleFileAuthsList) {
                TSRole role = roleService.getRoleByRoleId(roleFileAuth.getRoleId());
                if(r.getId().equals(role.getId()))
                {
                    ProjLibRoleFileAuthVo vo = new ProjLibRoleFileAuthVo();
                    vo.setId(roleFileAuth.getId());
                    vo.setFileId(roleFileAuth.getFileId());
                    vo.setRoleId(roleFileAuth.getRoleId());
                    vo.setRoleName(role.getRoleName());
                    long permissionCode = 0;
                    if (StringUtils.isNotEmpty(roleFileAuth.getPermissionCode())) {
                        permissionCode = Long.valueOf(roleFileAuth.getPermissionCode());
                    }
                    // 将其权限信息由十进制值转为对应的权限code及是否为true的Map
                    Map<String, Boolean> authMap = new HashMap<String, Boolean>();
                    Map<ProjectLibraryAuthorityEnum, Boolean> map = ProjLibAuthManager.getActionMapByPermissionCode(permissionCode);
                    for (ProjectLibraryAuthorityEnum authEnum : map.keySet()) {
                        authMap.put(authEnum.getActionCode(), map.get(authEnum));
                    }
                    vo.setAuthMap(authMap);
                    roleAuthList.add(vo);
                    continue outer;
                }else {
                    ProjLibRoleFileAuthVo vo = new ProjLibRoleFileAuthVo();
                    vo.setId(roleFileAuth.getId());
                    vo.setFileId(roleFileAuth.getFileId());
                    vo.setRoleId(r.getId());
                    vo.setRoleName(r.getRoleName());
                    long permissionCode = 0;
                    if (StringUtils.isNotEmpty(roleFileAuth.getPermissionCode())) {
                        permissionCode = Long.valueOf(roleFileAuth.getPermissionCode());
                    }
                    // 将其权限信息由十进制值转为对应的权限code及是否为true的Map
                    Map<String, Boolean> authMap = new HashMap<String, Boolean>();
                    Map<ProjectLibraryAuthorityEnum, Boolean> map = ProjLibAuthManager.getActionMapByPermissionCode(permissionCode);
                    for (ProjectLibraryAuthorityEnum authEnum : map.keySet()) {
                        authMap.put(authEnum.getActionCode(), map.get(authEnum));
                    }
                    vo.setAuthMap(authMap);
                    roleAuthList.add(vo);
                    continue outer;
                }
            }
        }*/
        return roleAuthList;
    }

    @Override
    public Map<String, List<RepRoleFileAuthDto>> getTemplateRoleAuths(String projTmpId, String libTmpId, List<TSRoleDto> roles) {
        Map<String, List<RepRoleFileAuthDto>> roleAuthMap = new HashMap<String, List<RepRoleFileAuthDto>>();
        // 拼接根节点名称
        ProjTemplate projTemplate = getEntity(ProjTemplate.class, projTmpId);
        String rootPath = projTemplate.getProjTmplName() + "-" + projTemplate.getProjTmplNumber();
        // 获取模板相关的文件角色权限
        List<ProjectLibRoleFileAuth> templateRoleFileAuths = new ArrayList<>();
        List<ProjectLibRoleFileAuth> templateRoleFileList = sessionFacade.findHql("from ProjectLibRoleFileAuth t where t.templateId =? ", libTmpId);
        List<TSRoleDto> roleList = roleService.getCommonRole();
        if(!CommonUtil.isEmpty(templateRoleFileList)){
            if(!CommonUtil.isEmpty(roleList)){
                for(ProjectLibRoleFileAuth auth : templateRoleFileList){
                    for(TSRoleDto role : roleList){
                        if(auth.getRoleId().equals(role.getId()) && role.getStatus() == 1){
                            templateRoleFileAuths.add(auth);
                        }
                    }
                }
            }
        }
        // 获取模板相关的目录、目录路径Map
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select category.id ID, ");
        hqlBuffer.append(" sys_connect_by_path(category.name, '/') PATHNAME ");
        hqlBuffer.append(" from (select f.id, ");
        hqlBuffer.append("  f.parentid, ");
        hqlBuffer.append(" case ");
        hqlBuffer.append("  when f.parentid = 'ROOT' then ");
        hqlBuffer.append("  '" + rootPath + "'");
        hqlBuffer.append("  else ");
        hqlBuffer.append("  f.name ");
        hqlBuffer.append(" end name ");
        hqlBuffer.append(" from CM_PROJLIB_TEMPLATE_FILE f ");
        hqlBuffer.append("  where f.templateid = '" + libTmpId + "') category ");
        hqlBuffer.append("  start with category.parentid = 'ROOT' ");
        hqlBuffer.append(" connect by category.parentid = prior category.id ");
        hqlBuffer.append("  order by category.id ");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        Map<String, String> categoryIdPathMap = new HashMap<String, String>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("ID");
                String namePath = (String)map.get("PATHNAME");
                categoryIdPathMap.put(id, namePath);
            }
        }
        Map<String, String> roleMap = new HashMap<String, String>();
        for (TSRoleDto role : roles) {
            roleMap.put(role.getId(), role.getRoleCode());
        }
        // 将满足roles的文件路径、文件角色权限以Map<String, List<RepRoleFileAuth>>返回
        for (ProjectLibRoleFileAuth roleFileAuth : templateRoleFileAuths) {
            if (StringUtils.isNotEmpty(categoryIdPathMap.get(roleFileAuth.getFileId()))) {
                String fileId = categoryIdPathMap.get(roleFileAuth.getFileId());
                List<RepRoleFileAuthDto> list = new ArrayList<RepRoleFileAuthDto>();
                RepRoleFileAuthDto fileAuth = new RepRoleFileAuthDto();
                String roleId = roleFileAuth.getRoleId();
                if (StringUtils.isNotEmpty(roleMap.get(roleId))) {
                    if (!CommonUtil.isEmpty(roleAuthMap.get(categoryIdPathMap.get(roleFileAuth.getFileId())))) {
                        list = roleAuthMap.get(categoryIdPathMap.get(roleFileAuth.getFileId()));
                        fileAuth.setRoleId(roleFileAuth.getRoleId());
                        fileAuth.setPermissionCode(roleFileAuth.getPermissionCode());
                    }
                    else {
                        fileAuth.setRoleId(roleFileAuth.getRoleId());
                        fileAuth.setPermissionCode(roleFileAuth.getPermissionCode());
                    }
                    list.add(fileAuth);
                    roleAuthMap.put(fileId, list);
                }
            }
        }
        return roleAuthMap;
    }

    @Override
    public void applyTemplate(String templateId, Map<String, List<RepRoleFileAuthDto>> roles, String libId, TSUserDto user, String orgId) {
        String tLibId = getLibIdByTemplateId(templateId);
        String teamId = projTemplateRoleService.getTeamIdByTemplateId(templateId);
        List<RepFileDto> repFiles = repService.getRepFileByLibIdAndType(appKey,tLibId,0);
        if(repFiles.size() < 1) {
            // create rootFile & re query repFiles
        }
        if(repFiles.size() == 1) {
            applyFileAuth(teamId, tLibId, libId,user);
        }else {
            applyFileAuth( templateId, tLibId, roles);
        }
    }

    private void applyFileAuth(String teamId, String tLibId, String libId,TSUserDto userDto) {
        // 获取项目库模板相关的文件角色权限
        List<ProjectLibRoleFileAuth> authes = new ArrayList<>();
        List<ProjectLibRoleFileAuth> authList = findByQueryString("from ProjectLibRoleFileAuth t where t.templateId = '"+ libId + "'");
        List<TSRoleDto> roleList = roleService.getCommonRole();
        if(!CommonUtil.isEmpty(authList)){
            if(!CommonUtil.isEmpty(roleList)){
                for(ProjectLibRoleFileAuth auth : authList){
                    for(TSRoleDto role : roleList){
                        if(auth.getRoleId().equals(role.getId()) && role.getStatus() == 1){
                            authes.add(auth);
                        }
                    }
                }
            }
        }
        // 根据项目库模板的目录 创建 项目模板的库目录
        HashMap<String, String> idMaps  = copyProjLibFolders(tLibId, libId,userDto.getId());
        repService.deleteRepRoleFileAuthByLibId(tLibId);


        List<TSRoleDto> roles = roleService.getSysRoleListByTeamId(appKey,teamId);
        List<String> roleIds = new ArrayList<String>();
        for(TSRoleDto role : roles){
            roleIds.add(role.getId());
        }

        List<RepRoleFileAuthDto> aList = new ArrayList<RepRoleFileAuthDto>();
        RepRoleFileAuthDto boAuth = new RepRoleFileAuthDto();

        /*repService.initBusinessObject(boAuth);
        String bizCurrent = boAuth.getBizCurrent();
        String bizId = boAuth.getBizId();
        String bizVersion = boAuth.getBizVersion();*/
        for(ProjectLibRoleFileAuth auth : authes){
            if(StringUtils.isNotBlank(idMaps.get(auth.getFileId()))){
                if(roleIds.contains(auth.getRoleId())){
                    RepRoleFileAuthDto a = new RepRoleFileAuthDto();
                  /*  a.setBizCurrent(bizCurrent);
                    a.setBizId(bizId);
                    a.setBizVersion(bizVersion);*/
                    a.setFileId(idMaps.get(auth.getFileId()));
                    a.setRoleId(auth.getRoleId());
                    a.setPermissionCode(auth.getPermissionCode());
                    aList.add(a);
                }
            }
        }
        repService.batchSaveRepRoleFileAuthDto(aList);
    }


    private void applyFileAuth(String templateId,String tLibId, Map<String, List<RepRoleFileAuthDto>> map) {

        repService.deleteRepRoleFileAuthByLibId(tLibId);
        /*sessionFacade.executeHql("delete from RepRoleFileAuth t where t.fileId in (select f.id from RepFile f where f.fileType = '0' and f.libId = '"
                + tLibId + "')");*/
        Map<String, String> map2 = getPath(templateId);
        List<RepRoleFileAuthDto> repRoleFileAuthList = new ArrayList<RepRoleFileAuthDto>();
        for (String p : map2.keySet()) {
            if (!CommonUtil.isEmpty(map.get(p))) {
                List<RepRoleFileAuthDto> repList = map.get(p);
                for (RepRoleFileAuthDto repRoleFileAuth : repList) {
                  /*  boService2.initBusinessObject(repRoleFileAuth);*/
                    repRoleFileAuth.setFileId(map2.get(p));
                    repRoleFileAuthList.add(repRoleFileAuth);
                }
            }
        }

        repService.batchSaveRepRoleFileAuthDto(repRoleFileAuthList);
    }

    private Map<String, String> getPath(String templateId) {
        Map<String, String> allPathMap = new HashMap<String, String>();
        String libId = getLibIdByTemplateId(templateId);
        ProjTemplate template = getEntity(ProjTemplate.class, templateId);
        String rootPath = template.getProjTmplName() + "-" + template.getProjTmplNumber();
        // List<RepFile> files = getRepFileByLibIdAndType(libId, 0);
        // String hqlStr = "from RepFile t where t.libId=? and t.fileType=0";

        allPathMap = repService.getRepFileAllPath(libId,rootPath);
        /*StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select repFile.id ID, ");
        hqlBuffer.append(" sys_connect_by_path(repFile.fileName, '/') PATHNAME ");
        hqlBuffer.append(" from (select f.id, ");
        hqlBuffer.append("  f.parentid, ");
        hqlBuffer.append(" (case ");
        hqlBuffer.append("  when f.parentid = '" + libId + "' then ");
        hqlBuffer.append("  '" + rootPath + "'");
        hqlBuffer.append("  else ");
        hqlBuffer.append("  f.fileName ");
        hqlBuffer.append(" end) fileName ");
        hqlBuffer.append(" from rep_file f ");
        hqlBuffer.append("  where f.fileType = '" + "0" + "' and f.libId = '" + libId
                + "') repFile ");
        hqlBuffer.append("  start with repFile.parentid = '" + libId + "' ");
        hqlBuffer.append(" connect by repFile.parentid = prior repFile.id ");
        hqlBuffer.append("  order by repFile.id ");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("ID");
                String namePath = (String)map.get("PATHNAME");
                allPathMap.put(namePath, id);
            }
        }*/
        return allPathMap;
    }


    @Override
    public HashMap<String, String> copyProjLibFolders(String newLibId, String oldLibId, String type,String userId) {
        String fileName = "";
        String projectId = "";
        Project project = null;
        HashMap<String, String> idMap = new HashMap<String, String>();
        // 新建项目模板,从项目的文档库copy到模板的文档库
        if (ProjectConstants.PROJECTTEMPLATE.equals(type)) {
            String templateId = getTemplateIdByLibId(newLibId);
            ProjTemplate projTemplate = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, templateId);
            fileName = projTemplate.getProjTmplName();
        }
        else {
            projectId = projRoleService.getProjectIdByLibId(newLibId);
            project = (Project)sessionFacade.getEntity(Project.class, projectId);
            fileName =  project.getName() + "-" + project.getProjectNumber();
        }
        List<RepFileDto> roots = repService.getRootDirsByParams(appKey,oldLibId,"0",oldLibId);
        if (roots == null || roots.size() == 0) {
            projLibService.createFile(projectId, null, project.getName() + "-" + project.getProjectNumber(), 0,userId);
        }
        else {

            List<RepFileDto> files = new ArrayList<RepFileDto>();
            //files.addAll(roots);
            if (!CommonUtil.isEmpty(roots) && roots.size() == 1) {
                RepFileDto r = roots.get(0);
                RepFileDto root = new RepFileDto();
                try {
                    BeanUtil.copyBeanNotNull2Bean(r, root);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                root.setFileName(fileName);
                files.add(root);
                files = projTemplateService.orderByParent(root, files);

            }
            for (RepFileDto file : files) {
                RepFileDto newFile = new RepFileDto();
                String oldId = file.getId();
                newFile = copyProjectLibFileToTemplateLibFile(file, newLibId);
                if (file.getLibId().equals(file.getParentId())) {
                    newFile.setParentId(newLibId);
                    // 创建文件
                    String fileId = createFile(newFile,userId);
                    idMap.put(oldId, fileId);

                }
                else {
                    newFile = copyProjectLibFileToTemplateLibFile(file, newLibId);
                    String newParentId = idMap.get(newFile.getParentId());
                    newFile.setParentId(newParentId);
                    String fileId = createFile(newFile,userId);
                    idMap.put(oldId, fileId);
                }

            }
        }
        return idMap;
    }


    @Override
    public String getTemplateIdByLibId(String libId) {
        if (StringUtils.isNotEmpty(libId)) {
            ProjTmplTeamLink projTmplTeamLink = new ProjTmplTeamLink();
            projTmplTeamLink.setLibId(libId);
            List<ProjTmplTeamLink> links = searchProjTmplTeamLink(projTmplTeamLink);
            if (!CommonUtil.isEmpty(links)) {
                return links.get(0).getProjTemplateId();

            }
        }
        return "";
    }


    private HashMap<String, String> copyProjLibFolders(String newLibId, String libId,String userId) {
        HashMap<String, String> idMap = new HashMap<String, String>();
        // 项目库模板 目录列表
        List<ProjectLibTemplateFileCategory> files = new ArrayList<ProjectLibTemplateFileCategory>();
        List<ProjectLibTemplateFileCategory> rootList = sessionFacade.findHql("from ProjectLibTemplateFileCategory t where t.templateId=? and t.parentId='ROOT'", libId);
        ProjectLibTemplateFileCategory root = new ProjectLibTemplateFileCategory();
        if(!CommonUtil.isEmpty(rootList)){
            root = rootList.get(0);
        }
        files.add(root);
        orderByParent(root, files, libId);
        for(ProjectLibTemplateFileCategory f : files){
            if("ROOT".equalsIgnoreCase(f.getParentId())){
                List<RepFileDto> roottList = repService.getRootDirsByParams(appKey,newLibId,"0",newLibId);
                RepFileDto roott = new RepFileDto();
                if(!CommonUtil.isEmpty(roottList)){
                    roott = roottList.get(0);
                }
                idMap.put(f.getId(), roott.getId());
            }else{
                String oldId = f.getId();
                RepFileDto newFile = new RepFileDto();
                newFile.setParentId(idMap.get(f.getParentId()));
                newFile.setFileName(f.getName());
                newFile.setCreateBy(userId);
                newFile.setLibId(newLibId);
                newFile.setSecurityLevel(Short.valueOf("1"));
                FeignJson repFileJson = repService.addRepFile(appKey,newFile,userId);
                String fileId = String.valueOf(repFileJson.getObj());
                idMap.put(oldId, fileId);
            }
        }
        return idMap;
    }

    private List<ProjectLibTemplateFileCategory> orderByParent(ProjectLibTemplateFileCategory root, List<ProjectLibTemplateFileCategory> results,String libId) {
        ProjectLibTemplateFileCategory bo = new ProjectLibTemplateFileCategory();
        if(!CommonUtil.isEmpty(root.getId())){
            bo =(ProjectLibTemplateFileCategory)sessionFacade.getEntity(ProjectLibTemplateFileCategory.class, root.getId());
        }
        List<ProjectLibTemplateFileCategory> children = new ArrayList<>();
        children = this.findHql(
                "from ProjectLibTemplateFileCategory t where t.parentId=? and t.templateId=? order by t.orderNum, t.name",
                bo.getId(),libId);
        if(!CommonUtil.isEmpty(children)){
            for (ProjectLibTemplateFileCategory child : children) {
                results.add(child);
                List<ProjectLibTemplateFileCategory> gChildren = this.findHql(
                        "from ProjectLibTemplateFileCategory t where t.parentId=? and t.templateId=? order by t.orderNum, t.name",
                        child.getId(),libId);
                if (!CommonUtil.isEmpty(gChildren)) {
                    orderByParent(child, results, libId);
                }
            }
        }

        return results;
    }

    @Override
    public HashMap<String, String> batchInitSaveLib(String projectId, String templateId,TSUserDto userDto) {
        String newLibId = getLibIdByTemplateId(templateId);// 模板的库id
        String oldLibId = projRoleService.getLibIdByProjectId(projectId);// 项目模板的库id
        HashMap<String, String> idMap = copyProjLibFoldersForSaveASTemplate(newLibId, oldLibId, ProjectConstants.PROJECTTEMPLATE,userDto);
        return idMap;
    }

    @Override
    public HashMap<String, String> copyProjLibFoldersForSaveASTemplate(String newLibId, String oldLibId, String type,TSUserDto userDto) {
        String fileName = "";
        String projectId = "";
        Project project = null;
        HashMap<String, String> idMap = new HashMap<String, String>();
        // 新建项目模板,从项目的文档库copy到模板的文档库
        if (ProjectConstants.PROJECTTEMPLATE.equals(type)) {
            String templateId = getTemplateIdByLibId(newLibId);
            ProjTemplate projTemplate = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, templateId);
            fileName = projTemplate.getProjTmplName();
        }
        else {
            projectId = projRoleService.getProjectIdByLibId(newLibId);
            project = (Project)sessionFacade.getEntity(Project.class, projectId);
            fileName =  project.getName() + "-" + project.getProjectNumber();
        }
        List<RepFileDto> roots = repService.getRootDirsByParams(appKey,oldLibId,"0",oldLibId);
        if (roots == null || roots.size() == 0) {
            projLibService.createFile(projectId, null, project.getName() + "-" + project.getProjectNumber(), 0,userDto.getId());
        }
        else {

            List<RepFileDto> files = new ArrayList<RepFileDto>();
            //files.addAll(roots);
            if (!CommonUtil.isEmpty(roots) && roots.size() == 1) {
                RepFileDto r = roots.get(0);
                RepFileDto root = new RepFileDto();
                try {
                    BeanUtil.copyBeanNotNull2Bean(r, root);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                root.setFileName(fileName);
                files.add(root);
                files = projTemplateService.orderByParent(root, files);

            }
            for (RepFileDto file : files) {
                RepFileDto newFile = new RepFileDto();
                String oldId = file.getId();
                newFile.setSecurityLevel(Short.valueOf("1"));
                newFile.setCreateBy(userDto.getId());
                newFile = copyProjectLibFileToTemplateLibFile(file, newLibId);
                if (file.getLibId().equals(file.getParentId())) {
                    newFile.setParentId(newLibId);
                    // 创建文件
                    String fileId = createFile(newFile,userDto.getId());
                    idMap.put(oldId, fileId);

                }
                else {
                    newFile = copyProjectLibFileToTemplateLibFile(file, newLibId);
                    String newParentId = idMap.get(newFile.getParentId());
                    newFile.setParentId(newParentId);
                    String fileId = createFile(newFile,userDto.getId());
                    idMap.put(oldId, fileId);
                }

            }
        }
        return idMap;
    }


    RepFileDto copyProjectLibFileToTemplateLibFile(RepFileDto file, String libId) {
        RepFileDto newFile = new RepFileDto();
        try {
            BeanUtil.copyBeanNotNull2Bean(file, newFile);
            newFile.setId(null);
            newFile.setLibId(libId);;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return newFile;
        }

    }

    @Override
    public String createFile(RepFileDto repFile,String userId) {
        FeignJson repFileJson = repService.addRepFile(appKey,repFile,userId);
        String fileId = String.valueOf(repFileJson.getObj());
        return fileId;
    }
}