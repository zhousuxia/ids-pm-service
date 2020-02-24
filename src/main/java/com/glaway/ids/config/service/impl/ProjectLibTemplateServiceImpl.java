package com.glaway.ids.config.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.service.threemember.FeignRoleService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.config.auth.ProjLibAuthManager;
import com.glaway.ids.config.auth.ProjectLibraryAuthorityEnum;
import com.glaway.ids.config.dto.ProjectLibTemplateDto;
import com.glaway.ids.config.entity.ProjectLibTemplateFileCategory;
import com.glaway.ids.config.service.ProjectLibTemplateServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projectmanager.vo.RepFileAuthVo;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.CommonInitUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.ids.config.entity.ProjectLibRoleFileAuth;
import com.glaway.ids.config.entity.ProjectLibTemplate;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.project.projecttemplate.service.impl.ProjTemplateServiceImpl;


/**
 * 项目库权限模板处理
 * 
 * @author blcao
 * @version 2016年6月29日
 * @see ProjectLibTemplateServiceImpl
 * @since
 */
@Service("projectLibTemplateService")
@Transactional
public class ProjectLibTemplateServiceImpl extends BusinessObjectServiceImpl<ProjectLibRoleFileAuth> implements ProjectLibTemplateServiceI {

    private static final SystemLog log = BaseLogFactory.getSystemLog(ProjTemplateServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private FeignUserService userService;

    @Autowired
    private FeignRoleService roleService;

    @Override
    public List<ProjectLibTemplate> getAllUseProjectLibTemplate() {
        return sessionFacade.findByQueryString("from ProjectLibTemplate t where 1=1 and t.avaliable=1 and t.status =1");
    }

    @Override
    public ProjectLibTemplate getProjLibTemplateEntity(String templateId) {
        ProjectLibTemplate projectLibTemplate = (ProjectLibTemplate)sessionFacade.getEntity(ProjectLibTemplate.class,templateId);
        return projectLibTemplate;
    }

    @Override
    public String getTemplateCategoryRootNodeId(String templateId) {
        String rootId = "";
        List<ProjectLibTemplateFileCategory> list = sessionFacade.findByQueryString("from ProjectLibTemplateFileCategory t where t.templateId = '"
                + templateId
                + "' and t.parentId = 'ROOT'");
        if (!CommonUtil.isEmpty(list)) {
            rootId = list.get(0).getId();
        }
        return rootId;
    }

    @Override
    public List<ProjLibRoleFileAuthVo> getProjLibTemplateRoleFileAuths(String fileId) {
        List<ProjLibRoleFileAuthVo> roleAuthList = new ArrayList<ProjLibRoleFileAuthVo>();
        List<ProjectLibRoleFileAuth> roleFileAuthsList = new ArrayList<>();
        List<ProjectLibRoleFileAuth> fileAuthsList = getProjectLibRoleFileAuths(fileId);
        List<TSRoleDto> roleList = roleService.getCommonRole();
        if(!CommonUtil.isEmpty(fileAuthsList)){
            for(ProjectLibRoleFileAuth auth : fileAuthsList){
                if(!CommonUtil.isEmpty(roleList)){
                    for(TSRoleDto role : roleList){
                        if(auth.getRoleId().equals(role.getId()) && role.getStatus() == 1){
                            auth.setRoleName(role.getRoleName());
                            roleFileAuthsList.add(auth);
                        }
                    }
                }
            }
        }
        for (ProjectLibRoleFileAuth roleFileAuth : roleFileAuthsList) {
            ProjLibRoleFileAuthVo vo = new ProjLibRoleFileAuthVo();
            vo.setId(roleFileAuth.getId());
            vo.setFileId(roleFileAuth.getFileId());
            vo.setRoleId(roleFileAuth.getRoleId());
            vo.setRoleName(roleFileAuth.getRoleName());
            long permissionCode = 0;
            if (!CommonUtil.isEmpty(roleFileAuth.getPermissionCode())) {
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
        return roleAuthList;
    }

    @Override
    public List<ProjectLibRoleFileAuth> getProjectLibRoleFileAuths(String fileId) {
        return sessionFacade.findByQueryString("from ProjectLibRoleFileAuth t where t.fileId = '" + fileId
                + "' order by t.roleId");
    }

    @Override
    public List<TreeNode> getProjectLibTemplateFileCategorys(String projectLibTemplateId) {
        @SuppressWarnings("unchecked")
        List<ProjectLibTemplateFileCategory> list = sessionFacade.findHql("from ProjectLibTemplateFileCategory c where c.templateId = ? order by c.orderNum", projectLibTemplateId);
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (ProjectLibTemplateFileCategory file : list) {
            TreeNode node = null;
            if (CommonConstants.PROJECT_AUTH_TEMPLATE_CATEGORY_ROOT.equals(file.getParentId())) {
                node = new TreeNode(file.getId(), null, file.getName(), file.getName(), true);

            }
            else {
                node = new TreeNode(file.getId(), file.getParentId(), file.getName(),
                        file.getName(), true);

            }
            node.setIconClose("webpage/com/glaway/ids/common/zTreeIcon_close.png");
            node.setIconOpen("webpage/com/glaway/ids/common/zTreeIcon_open.png");
            node.setIcon("webpage/com/glaway/ids/common/zTreeIcon_open.png");
            node.setOpen(true);
            node.setDataObject(file);
            treeNodes.add(node);
        }
        return treeNodes;
    }

    @Override
    public void deleteProjectLibTemplateByIds(String ids) {
        // 将avaliable设为删除标识
        if (StringUtils.isNotEmpty(ids)) {
            StringBuffer idsBuffer = new StringBuffer("");
            String[] idsArr = ids.split(",");
            for (String id : idsArr) {
                if (idsBuffer.length() > 0) {
                    idsBuffer.append(",'").append(id).append("'");
                }
                else {
                    idsBuffer.append("'").append(id).append("'");
                }
            }
            sessionFacade.executeHql("update ProjectLibTemplate t set t.avaliable = '0' where t.id in ("
                    + idsBuffer.toString() + ")");
        }
    }

    @Override
    public PageList queryProjectLibTemplates(List<ConditionVO> conditionList, Map<String, String> params) {
        try {
            String hql = "from ProjectLibTemplate t where t.avaliable='1' ";
            String userName = "";
            for (ConditionVO conditionVO : conditionList) {
                if ("ProjectLibTemplate.creator".equals(conditionVO.getKey())) {
                    userName = conditionVO.getValue();
                    conditionList.remove(conditionVO);
                    break;
                }
            }
            if (StringUtils.isNotEmpty(userName)) {
                hql = hql + " and (lower(t.createFullName) like lower('%" + userName + "%')";
                hql = hql + " or lower(t.createName) like lower('%" + userName + "%'))";
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("ProjectLibTemplate.createTime", "desc");
            for (ConditionVO p : conditionList) {
                if ("creator".equals(p.getSort())) {
                    p.setSort("createFullName");
                }
            }
            List list = sessionFacade.searchByCommonFormHql(hql, conditionList, true, map);
            List<ProjectLibTemplateDto> dtoList = CodeUtils.JsonListToList(list,ProjectLibTemplateDto.class);
            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            PageList pageList = new PageList(count, dtoList);
            return pageList;
        }
        catch (RecognitionException e) {
            new FdExceptionPolicy("basicException", "查询语句格式化异常", e);
        }
        catch (TokenStreamException e) {
            new FdExceptionPolicy("basicException", "查询语句开闭缺失", e);
        }
        catch (ClassNotFoundException e) {
            new FdExceptionPolicy("basicException", "查询实体不存在", e);
        }
        return null;
    }

    @Override
    public void startOrStopTemplateByIds(String ids, String startOrStop, TSUserDto userDto, String orgId) {
        // 将status设为启用或禁用标识
        if (StringUtils.isNotEmpty(ids)) {
            StringBuffer idsBuffer = new StringBuffer("");
            String[] idsArr = ids.split(",");
            for (String id : idsArr) {
                if (idsBuffer.length() > 0) {
                    idsBuffer.append(",'").append(id).append("'");
                }
                else {
                    idsBuffer.append("'").append(id).append("'");
                }
            }
            sessionFacade.executeHql("update ProjectLibTemplate t set t.status = '" + startOrStop
                    + "' where t.id in (" + idsBuffer.toString() + ")");
        }
    }

    @Override
    public boolean checkTemplateNameExist(ProjectLibTemplate template) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("from ProjectLibTemplate t where t.avaliable = '1'");
        if (StringUtils.isNotEmpty(template.getId())) {
            hqlBuffer.append(" and t.id <> '" + template.getId() + "'");
        }
        if (StringUtils.isNotEmpty(template.getName())) {
            hqlBuffer.append(" and t.name = '" + template.getName() + "'");
        }

        List<ProjectLibTemplate> list = sessionFacade.findByQueryString(hqlBuffer.toString());
        if (CommonUtil.isEmpty(list)) {
            return false;
        }
        return true;
    }

    @Override
    public ProjectLibTemplate saveOrUpdateProjectLibTemplate(ProjectLibTemplate template, TSUserDto userDto, String orgId) {
        if(CommonUtil.isEmpty(template.getId())){
            CommonInitUtil.initGLObjectForCreate(template,userDto,orgId);
            // 保存项目库权限模板基本信息
            sessionFacade.save(template);
            // 初始化项目库权限模板目录的根目录
            ProjectLibTemplateFileCategory rootFileCategory = new ProjectLibTemplateFileCategory();
            rootFileCategory.setName(CommonConstants.PROJECT_AUTH_TEMPLATE_CATEGORY_ROOT_CHI);
            rootFileCategory.setParentId(CommonConstants.PROJECT_AUTH_TEMPLATE_CATEGORY_ROOT);
            rootFileCategory.setTemplateId(template.getId());
            CommonInitUtil.initGLObjectForCreate(rootFileCategory,userDto,orgId);
            sessionFacade.save(rootFileCategory);
        }else{
            CommonInitUtil.initGLObjectForUpdate(template,userDto,orgId);
            sessionFacade.saveOrUpdate(template);
        }
        return template;
    }

    @Override
    public boolean checkRoleFileAuthExistChange(String templateId, String fileId, List<RepFileAuthVo> repFileAuthVoList) {
        // 先获取表中fileId相关的角色权限数据
        List<ProjectLibRoleFileAuth> roleAuthlist = getProjectLibRoleFileAuths(fileId);
        // 既有数据的角色、权限编号Map
        Map<String, String> roleAuthCodeMap = new HashMap<String, String>();
        for (ProjectLibRoleFileAuth roleAuth : roleAuthlist) {
            roleAuthCodeMap.put(roleAuth.getRoleId(), roleAuth.getPermissionCode());
        }
        // 比较页面上设置的权限编码与既有数据的角色、权限编号Map的值
        List<String> authActionCodeList = ProjLibAuthManager.getAllAuthActionCode();
        for (RepFileAuthVo vo : repFileAuthVoList) {
            List<ProjectLibraryAuthorityEnum> actionList = new ArrayList<ProjectLibraryAuthorityEnum>();
            // 将权限编号转为十进制
            char[] charArr = vo.getCheckValue().toCharArray();
            for (int i = 0; i < charArr.length; i++ ) {
                if ("1".equals(String.valueOf(charArr[i]))) {
                    ProjectLibraryAuthorityEnum action = ProjectLibraryAuthorityEnum.valueOfActionCode(authActionCodeList.get(i));
                    if (action != null) {
                        actionList.add(action);
                    }
                }
            }
            String permissionCode = String.valueOf(ProjLibAuthManager.encodeAuthorityActions(actionList));
            if (!"0".equals(permissionCode)) {
                if (StringUtils.isEmpty(roleAuthCodeMap.get(vo.getRoleId()))
                        || !permissionCode.equals(roleAuthCodeMap.get(vo.getRoleId()))) {
                    return true;
                }
            }
            else {
                if (StringUtils.isNotEmpty(roleAuthCodeMap.get(vo.getRoleId()))
                        && !permissionCode.equals(roleAuthCodeMap.get(vo.getRoleId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void saveProjLibRoleFileAuth(String templateId, String fileId, List<RepFileAuthVo> repFileAuthVoList, String userId, String orgId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        // 先删除fileId相关的角色权限数据
        sessionFacade.executeHql("delete from ProjectLibRoleFileAuth t where t.fileId = '"
                + fileId + "'");
        List<ProjectLibRoleFileAuth> repRoleFileAuthList = new ArrayList<ProjectLibRoleFileAuth>();
        List<String> authActionCodeList = ProjLibAuthManager.getAllAuthActionCode();
        for (RepFileAuthVo vo : repFileAuthVoList) {
            List<ProjectLibraryAuthorityEnum> actionList = new ArrayList<ProjectLibraryAuthorityEnum>();
            // 将RepFileAuthVo转为ProjectLibRoleFileAuth对象
            ProjectLibRoleFileAuth roleFileAuth = new ProjectLibRoleFileAuth();
            CommonInitUtil.initGLObjectForCreate(roleFileAuth,userDto,orgId);
            initBusinessObject(roleFileAuth);
            roleFileAuth.setTemplateId(templateId);
            roleFileAuth.setFileId(fileId);
            roleFileAuth.setRoleId(vo.getRoleId());
            // 将权限编号转为十进制
            char[] charArr = vo.getCheckValue().toCharArray();
            for (int i = 0; i < charArr.length; i++ ) {
                if ("1".equals(String.valueOf(charArr[i]))) {
                    ProjectLibraryAuthorityEnum action = ProjectLibraryAuthorityEnum.valueOfActionCode(authActionCodeList.get(i));
                    if (action != null) {
                        actionList.add(action);
                    }
                }
            }
            String permissionCode = String.valueOf(ProjLibAuthManager.encodeAuthorityActions(actionList));
            roleFileAuth.setPermissionCode(String.valueOf(permissionCode));
            repRoleFileAuthList.add(roleFileAuth);
        }
        if (!CommonUtil.isEmpty(repRoleFileAuthList)) {
            sessionFacade.batchSave(repRoleFileAuthList);
        }
    }

    @Override
    public boolean checkCategoryNameExist(ProjectLibTemplateFileCategory category) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("from ProjectLibTemplateFileCategory t where 1 = 1");
        if (StringUtils.isNotEmpty(category.getId())) {
            hqlBuffer.append(" and t.id <> '" + category.getId() + "'");
        }
        if (StringUtils.isNotEmpty(category.getParentId())) {
            hqlBuffer.append(" and t.parentId = '" + category.getParentId() + "'");
        }

        if (StringUtils.isNotEmpty(category.getName())) {
            hqlBuffer.append(" and t.name = '" + category.getName() + "'");
        }

        List<ProjectLibTemplateFileCategory> list = sessionFacade.findByQueryString(hqlBuffer.toString());
        if (CommonUtil.isEmpty(list)) {
            return false;
        }
        return true;
    }

    @Override
    public long getMaxOrderNum(String parentId) {
        List<ProjectLibTemplateFileCategory> list = sessionFacade.findByQueryString("from ProjectLibTemplateFileCategory t where t.parentId = '"
                + parentId + "' order by t.orderNum desc");
        long maxOrderNum = 0;
        if (list.size() > 0) {
            maxOrderNum = list.get(0).getOrderNum();
        }
        return maxOrderNum;
    }

    @Override
    public void saveOrUpdateProjectLibTemplateFileCategory(ProjectLibTemplateFileCategory projectLibTemplateFileCategory,TSUserDto userDto,String orgId) {
        if(CommonUtil.isEmpty(projectLibTemplateFileCategory.getId())){
            CommonInitUtil.initGLObjectForCreate(projectLibTemplateFileCategory,userDto,orgId);
            sessionFacade.saveOrUpdate(projectLibTemplateFileCategory);
        }else {
            CommonInitUtil.initGLObjectForUpdate(projectLibTemplateFileCategory,userDto,orgId);
            sessionFacade.saveOrUpdate(projectLibTemplateFileCategory);
        }

    }

    @Override
    public ProjectLibTemplateFileCategory getProjectLibTemplateCategoryEntity(String id) {
        return (ProjectLibTemplateFileCategory)sessionFacade.get(ProjectLibTemplateFileCategory.class,id);
    }

    @Override
    public boolean checkCategoryExistChildNode(String fileId) {
        // 获取其子节点
        List<ProjectLibTemplateFileCategory> list = sessionFacade.findByQueryString("from ProjectLibTemplateFileCategory c where c.parentId = '"
                + fileId + "'");
        // 若存在子节点，则返回true
        if (!CommonUtil.isEmpty(list)) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteProjectLibTemplateFileCategory(String fileId) {
        // 先删除fileId相关的角色权限数据
        sessionFacade.executeHql("delete from ProjectLibRoleFileAuth t where t.fileId = '"
                + fileId + "'");
        sessionFacade.deleteEntityById(ProjectLibTemplateFileCategory.class, fileId);
    }

    @Override
    public String doMoveNode(ProjectLibTemplateFileCategory moveNodeInfo, String targetId, String moveType, TSUserDto userDto, String orgId) {
        String msg = "";
        ProjectLibTemplateFileCategory targetNode = (ProjectLibTemplateFileCategory)sessionFacade.getEntity(
                ProjectLibTemplateFileCategory.class, targetId);// 获取目标节点信息
        if ("inner".equals(moveType)) {
            ProjectLibTemplateFileCategory ptfc = new ProjectLibTemplateFileCategory();
            ptfc.setName(moveNodeInfo.getName().trim());
            ptfc.setParentId(targetNode.getId());
            boolean isExist = checkCategoryNameExist(ptfc);
            if (!isExist) {
                increaseOrderNum(targetNode.getId(), 0, null,userDto,orgId);
                ptfc = (ProjectLibTemplateFileCategory)sessionFacade.getEntity(ProjectLibTemplateFileCategory.class, moveNodeInfo.getId());
                ptfc.setParentId(targetNode.getId());
                ptfc.setOrderNum(1);
                CommonInitUtil.initGLObjectForUpdate(ptfc,userDto,orgId);
                sessionFacade.saveOrUpdate(ptfc);
            }
            else {
                msg = "同级节点不能重名";
            }
        }
        else if ("prev".equals(moveType)) {
            ProjectLibTemplateFileCategory ptfc = (ProjectLibTemplateFileCategory)sessionFacade.getEntity(ProjectLibTemplateFileCategory.class,
                    moveNodeInfo.getId());
            CommonInitUtil.initGLObjectForUpdate(ptfc,userDto,orgId);
            ptfc.setName(moveNodeInfo.getName().trim());
            ptfc.setParentId(targetNode.getParentId());
            ptfc.setOrderNum(targetNode.getOrderNum());
            increaseOrderNum(targetNode.getParentId(), targetNode.getOrderNum(), ptfc.getId(),userDto,orgId); // 如果移动到目标节点前一个位置
            // 则目标节点及后续节点序号加1
            sessionFacade.saveOrUpdate(ptfc);
        }
        else if ("next".equals(moveType)) {
            ProjectLibTemplateFileCategory ptfc = (ProjectLibTemplateFileCategory)sessionFacade.getEntity(ProjectLibTemplateFileCategory.class,
                    moveNodeInfo.getId());
            CommonInitUtil.initGLObjectForUpdate(ptfc,userDto,orgId);
            ptfc.setName(moveNodeInfo.getName().trim());
            ptfc.setParentId(targetNode.getParentId());
            ptfc.setOrderNum(targetNode.getOrderNum() + 1);
            increaseOrderNum(targetNode.getParentId(), targetNode.getOrderNum() + 1, ptfc.getId(),userDto,orgId); // 如果移动到目标节点的后一个位置
            // 则后续节点序号加1
            sessionFacade.saveOrUpdate(ptfc);
        }
        return msg;
    }

    private void increaseOrderNum(String parentId, long orderNum, String exceptId, TSUserDto userDto, String orgId) {
        String exceptSql = "";
        List<ProjectLibTemplateFileCategory> list = new ArrayList<ProjectLibTemplateFileCategory>();
        if (StringUtil.isNotEmpty(exceptId)) {
            exceptSql = " t.id != '" + exceptId + "'";
            list = sessionFacade.findByQueryString("from ProjectLibTemplateFileCategory t where"
                    + exceptSql
                    + " and t.parentId = '"
                    + parentId
                    + "' and t.orderNum >= "
                    + orderNum);
        }else{
            list = sessionFacade.findByQueryString("from ProjectLibTemplateFileCategory t where t.parentId = '"
                    + parentId
                    + "' and t.orderNum >= "
                    + orderNum);
        }

        for (ProjectLibTemplateFileCategory ptfc : list) {
            CommonInitUtil.initGLObjectForUpdate(ptfc,userDto,orgId);
            long newOrderNum = ptfc.getOrderNum() + 1;
            ptfc.setOrderNum(newOrderNum);
            sessionFacade.saveOrUpdate(ptfc);
        }
    }
}
