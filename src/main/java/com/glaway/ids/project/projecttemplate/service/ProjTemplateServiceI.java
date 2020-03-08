package com.glaway.ids.project.projecttemplate.service;


import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.ids.project.menu.entity.ProjTemplateMenu;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import com.glaway.ids.project.projecttemplate.dto.ProjTemplateDto;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.project.projecttemplate.entity.ProjTmpLibAuthLibTmpLink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目模板service
 * 
 * @author zhosuuxia
 * @version 2019年8月20日
 * @see ProjTemplateServiceI
 * @since
 */
public interface ProjTemplateServiceI extends BusinessObjectServiceI<ProjTemplate> {


    /**
     * 是否是项目模板管理员
     *
     * @param userId 用户id
     * @return
     */
    boolean isPTOM(String userId);


    /**
     * 获取项目模板信息
     *
     * @param id 项目模板id
     * @return
     */
    ProjTemplate getProjEntity(String id);


    /**
     * Description: <br>删除项目模板
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param templates 项目模板集合
     * @return
     * @see
     */
    void deleteTemplate(List<ProjTemplate> templates);

    /**
     * Description: <br>启用禁用项目模板
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param template 项目模板
     * @param type     启用/禁用类型
     * @return
     * @see
     */
    void openOrClose(ProjTemplate template, String type);

    /**
     * 项目模板列表查询方法
     *
     * @param conditionList 查询条件
     * @param userName      用户名
     * @param orgId         组织id
     * @return
     */
    PageList queryEntity(List<ConditionVO> conditionList, String userName,String orgId);

    /**
     * 获取项目模板生命周期状态
     * @return
     */
    String getLifeCycleStatusList();

    /**
     * 通过BizId获取项目模板
      * @param bizId 版本id
     * @return
     */
    ProjTemplate getProjTemplateByBizId(String bizId);

    /**
     * 通过版本Id及名称获取项目模板
     *
     * @param name  项目模板名称
     * @param bizId 版本id
     * @return
     */
    List<ProjTemplate> getProjTemplateListByNameAndBizId(String name,String bizId);

    /**
     * 通过名称获取项目模板
     *
     * @param name 项目模板名称
     * @return
     */
    List<ProjTemplate> getProjTemplateListByName(String name);

    /**
     * 修改项目模板
     *
     * @param template  项目模板
     * @param method    方法
     * @param oldName   项目模板名称
     * @param oldRemark 备注
     * @param userDto   用户信息
     * @param orgId     组织id
     * @return
     */
    ProjTemplate updateProjTemplate(ProjTemplate template, String method, String oldName, String oldRemark, TSUserDto userDto,String orgId,List<TSRoleDto> roleList);

    /**
     * Description: <br>保存项目模板
     *
     * @param projTemplate 需要保存或修改的项目模板
     * @param userDto      用户信息
     * @param orgId        组织id
     * @return
     * @see
     */
    String SaveProjTemplate(ProjTemplate projTemplate,TSUserDto userDto,String orgId);

    /**
     * Description: <br>条件查询项目模板菜单
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param menu 项目模板树
     * @return
     * @see
     */
    List<ProjTemplateMenu> searchProjTemplateMenu(ProjTemplateMenu menu);

    /**
     * 回退项目模板 版本
     *
     * @param id 项目模板id
     * @param bizId 项目模板版本Id
     * @param type 类型： （0，回退小版本； 1，回退大版本）
     * @see
     */
    void backToVersion(String id, String bizId, String type,TSUserDto userDto,String orgId);

    /**
     * Description: <br>复制项目模板
     *
     * @param projTemplate 需要复制的项目模板
     * @param oldTmpId     被复制的项目模板ID
     * @param orgId        组织id
     * @return
     * @see
     */
    String copyProjTemplate(ProjTemplate projTemplate, String oldTmpId,String userId,String orgId);

    /**
     * 获取项目模板版本信息
     *
     * @param bizId    版本id
     * @param pageSize 页码
     * @param pageNum  每页数量
     * @param <T>
     * @return
     */
    <T> List<T> getVersionHistory(String bizId, Integer pageSize,
                                  Integer pageNum);


    /**
     * 获取项目模板历史版本数量
     *
     * @param bizId 版本id
     * @return
     */
    long getVersionCount(String bizId);

    /**
     * 根据项目模板id获取项目模板权限
     * @param templateId 模板id
     * @return
     */
    List<ProjTmpLibAuthLibTmpLink> getProjTmpLibAuthLibTmpLinkByTemplateId(String templateId);

    /**
     * 文档排序
     *
     * @param root    文档
     * @param results 文档集合
     * @return
     */
    List<RepFileDto> orderByParent(RepFileDto root, List<RepFileDto> results);

    /**
     *
     *  保存项目模板的关系到ProjTmplTeamLink表中
     * @param projTemplateId 项目模板集合
     * @param teamId         团队id
     * @param libId          项目库id
     * @param userDto        用户信息
     * @param orgId          组织id
     * @return
     * @see
     */
    String saveTeamLink(String projTemplateId, String teamId, String libId,TSUserDto userDto,String orgId);

    /**
     * 启用项目模板审批流程
     * @param templateId 模板id
     * @param leader     领导信息
     * @param deptLeader 室领导
     * @param userDto    用户信息
     * @param orgId      组织id
     */
    void startProjTemplateProcess(String templateId,String leader,String deptLeader,TSUserDto userDto,String orgId);

    /**流程结束时更新数据状态
     * @param templateId 模板id
     * @return
     */
    void updateBizCurrentForProcessComplete(String templateId);

    /**
     * 流程驳回时更新数据状态
     *
     * @param templateId 模板id
     * @param bizCurrent 生命周期状态
     */
    void updateBizCurrentForProcessRefuse(String templateId,String bizCurrent);

    /**
     * 流程启动
     *
     * @param projTemplate         项目库模板
     * @param variables            流程变量
     * @param processDefinitionKey 流程标识
     * @param curUser              当前用户信息
     * @param orgId                组织id
     */
    void submitProjectTemplateFlow(ProjTemplate projTemplate, Map<String, Object> variables,String processDefinitionKey ,TSUserDto curUser,String orgId);

    /**
     * 流程继续
     *
     * @param projTemplate 项目模板
     * @param curUser      当前用户信息
     * @param orgId        组织id
     */
    void submitProjectTemplateFlowAagin(ProjTemplate projTemplate,TSUserDto curUser,String orgId);

    /**
     * Description: <br>搜索项目模板
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param projTemplate 项目模板
     * @return
     * @see
     */
    List<ProjTemplate> searchProjTemplate(ProjTemplate projTemplate);

    /**
     * 项目模板新增保存
     *
     * @param projTemplate 项目模板
     * @param project      项目信息
     * @param plans        计划集合
     * @param teams        团队
     * @param libs         项目库
     * @param libPower     项目库权限
     * @param projectId    项目id
     * @param userDto      用户信息
     * @param orgId        组织id
     * @return
     */
    String  SaveAsTemplate(ProjTemplate projTemplate, Project project, List<String> plans, List<String> teams , List<String> libs, List<String> libPower, String projectId,TSUserDto userDto,String orgId);


    /**
     * 新建项目时，使用项目模板，把项目库目录权限复制到项目中
     *
     * @param projectId  项目id
     * @param templateId 模板id
     * @param idMap      项目库文件夹结构
     * @param userDto    用户信息
     * @param orgId      组织id
     */
    void saveRoleFileAuth2Project(String projectId,  String templateId, HashMap<String, String> idMap,TSUserDto userDto,String orgId);

    /**
     * 保存项目模板计划导入信息
     * @param projTemplate 项目模板信息
     * @param dataList 导入信息
     * @param excelMap excel数据集
     * @param planLevelMap 计划等级集合
     * @param switchStr 标准名称库
     * @param userDto 当前用户信息
     * @param orgId 组织id
     * @return
     */
    String saveProjectTemplateDetailByExcel(ProjTemplate projTemplate, List<PlanTemplateExcelVo> dataList,
                                         Map<String, PlanTemplateExcelVo> excelMap, Map<String, String> planLevelMap, String switchStr, TSUserDto userDto, String orgId);

    /**
     * 项目模板计划excel导入
     * @param map 计划模板excel信息
     * @param userId 用户id
     * @param projectTemplateId 项目模板id
     * @return
     */
    Map<String,Object> doImportPlanTemplateExcel(List<Map<String,Object>> map,String userId,String projectTemplateId,String orgId);
}
