package com.glaway.ids.config.service;


import java.util.List;
import java.util.Map;


import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.ids.config.entity.ProjectLibRoleFileAuth;
import com.glaway.ids.config.entity.ProjectLibTemplate;
import com.glaway.ids.config.entity.ProjectLibTemplateFileCategory;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projectmanager.vo.RepFileAuthVo;


/**
 * 项目库权限模板处理
 * 
 * @author blcao
 * @version 2016年6月29日
 * @see ProjectLibTemplateServiceI
 * @since
 */
public interface ProjectLibTemplateServiceI extends BusinessObjectServiceI<ProjectLibRoleFileAuth> {

    /**
     * 获得有用的模板
     *
     */
    List<ProjectLibTemplate> getAllUseProjectLibTemplate();

    /**
     * 获取项目模板对象信息
     * @param templateId 项目模板id
     * @return
     */
    ProjectLibTemplate getProjLibTemplateEntity(String templateId);

    /**
     * 获取项目库权限模板的目录的根节点的ID
     *
     * @param templateId 项目模板id
     * @return
     */
    String getTemplateCategoryRootNodeId(String templateId);

    /**
     * 获取项目库权限模板目录角色权限
     *
     * @param fileId 文档id
     * @return
     */
    List<ProjLibRoleFileAuthVo> getProjLibTemplateRoleFileAuths(String fileId);

    /**
     * 获取项目库权限模板目录权限
     *
     * @param fileId 文档id
     * @return
     */
    List<ProjectLibRoleFileAuth> getProjectLibRoleFileAuths(String fileId);

    /**
     * 获取项目库权限模板目录结构
     *
     * @param projectLibTemplateId 项目模板id
     * @return
     */
    List<TreeNode> getProjectLibTemplateFileCategorys(String projectLibTemplateId);

    /**
     * 批量删除项目库权限模板
     *
     * @param ids 项目模板id
     */
    void deleteProjectLibTemplateByIds(String ids);

    /**
     * 项目库权限模板清单查询
     *
     * @param conditionList 查询条件
     * @return
     */
    PageList queryProjectLibTemplates(List<ConditionVO> conditionList, Map<String, String> params);

    /**
     * 批量启用或禁用项目库权限模板
     *
     * @param ids 项目模板id
     * @param startOrStop 操作
     */
    void startOrStopTemplateByIds(String ids, String startOrStop, TSUserDto userDto,String orgId);

    /**
     * 校验模板名称是否已存在
     *
     * @param template 项目模板对象
     * @return
     */
    boolean checkTemplateNameExist(ProjectLibTemplate template);

    /**
     * 新增项目库权限模板保存其基本信息及初始化目录结构
     *
     * @param template 项目模板对象
     */
    ProjectLibTemplate saveOrUpdateProjectLibTemplate(ProjectLibTemplate template,TSUserDto userDto,String orgId);

    /**
     * 判断目录角色权限是否变化
     *
     * @param templateId 项目模板id
     * @param fileId 文档id
     * @param repFileAuthVoList 权限列表
     * @return
     */
    boolean checkRoleFileAuthExistChange(String templateId, String fileId,
                                         List<RepFileAuthVo> repFileAuthVoList);


    /**
     * 保存项目库权限模板目录角色权限
     *
     * @param templateId 项目模板id
     * @param fileId 文档id
     * @param repFileAuthVoList 权限列表
     */
    void saveProjLibRoleFileAuth(String templateId, String fileId,
                                 List<RepFileAuthVo> repFileAuthVoList,String userId,String orgId);

    /**
     * 校验同级的目录名是否已存在
     *
     * @param category 项目库权限模板对象
     * @return
     */
    boolean checkCategoryNameExist(ProjectLibTemplateFileCategory category);

    /**
     * 获取当前层级最大序号
     *
     * @return
     * @see
     */
    long getMaxOrderNum(String parentId);

    /**
     * 保存权限模板树
     * @param projectLibTemplateFileCategory 项目库权限模板对象
     */
    void saveOrUpdateProjectLibTemplateFileCategory(ProjectLibTemplateFileCategory projectLibTemplateFileCategory,TSUserDto userDto,String orgId);

    /**
     * 获取项目库权限模板对象信息
     * @param id  项目库权限模板id
     * @return
     */
    ProjectLibTemplateFileCategory getProjectLibTemplateCategoryEntity(String id);

    /**
     * 检查项目库权限模板的目录是否存在子节点
     *
     * @param fileId 文档id
     */
    boolean checkCategoryExistChildNode(String fileId);

    /**
     * 删除项目库权限模板的目录
     *
     * @param fileId 文档id
     */
    void deleteProjectLibTemplateFileCategory(String fileId);

    /**
     * 对移动节点所在层级的其他节点进行处理
     *
     * @return
     * @see
     */
    String doMoveNode(ProjectLibTemplateFileCategory moveNodeInfo, String targetId, String moveType,TSUserDto userDto,String orgId);
}
