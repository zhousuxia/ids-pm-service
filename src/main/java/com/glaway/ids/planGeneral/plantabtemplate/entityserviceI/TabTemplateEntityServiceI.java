package com.glaway.ids.planGeneral.plantabtemplate.entityserviceI;

import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.plantabtemplate.dto.TabTemplateDto;
import com.glaway.ids.planGeneral.plantabtemplate.entity.TabTemplate;

import java.util.List;
import java.util.Map;


/**
 * @Title: EntityServiceI
 * @Description: 页签模版管理EntityServiceI
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
public interface TabTemplateEntityServiceI {

    /**
     * 功能描述：根据查询条件查询数据
     * @param params 查询条件Map
     */
    PageList searchDatagrid(Map<String, String> params);

    /**
     * 功能描述：批量/单条 根据主键ID和状态Status启用或禁用页签模版
     * @param ids id集合
     * @param status 状态(启用“1”或者禁用“0”)
     */
    int doStartOrStop(String ids, String status);

    /**
     * 功能描述：批量/单条 根据主键ID删除页签模版
     * @param ids id集合(“，”分隔)
     */
    int doBatchDelete(String ids);

    /**
     * 功能描述：根据主键ID查询对象
     * @param id 主键ID
     */
    TabTemplate queryEntityById(String id);

    /**
     * 功能描述：数据保存
     * @param tabTemplate
     */
    TabTemplate doSave(TabTemplate tabTemplate);

    /**
     * 功能描述：通过名称获取信息
     * @param name
     */
    List<TabTemplate> queryTabTemplateByName(String name, String id);

    /**
     * 获取所有页签模板
     * @return
     */
    List<TabTemplate> getAllTabTemplates(String avaliable, String stopFlag);

    /**
     * 功能描述：数据保存
     * @param tabTemplate
     */
    TabTemplate doUpdateOrRevise(TabTemplate tabTemplate, String userId, String updateOrRevise);

    /**
     * 功能描述：回退/撤回
     * @param params
     */
    FeignJson doBack(Map<String, String> params);

    /**
     * 功能描述：提交审批
     * @param params
     */
    FeignJson doSubmitApprove(Map<String, String> params);

    /**流程结束时更新数据状态
     * @param tabId 页签模板id
     * @return
     */
    void updateBizCurrentForProcessComplete(String tabId);

    /**
     * 获取项目模板版本信息
     *
     * @param bizId    版本id
     * @param pageSize 页码
     * @param pageNum  每页数量
     * @param <T>
     * @return
     */
    <T> List<T> getVersionHistory(String bizId, Integer pageSize, Integer pageNum);

    /**
     * 根据生命周期设置页签模板状态
     * @param tabTemplateDtoList 页签模板数据
     * @return
     */
    List<TabTemplateDto> setStatusByLifeCycleStatus(List<TabTemplateDto> tabTemplateDtoList);

    /**
     * 获取项目模板历史版本数量
     *
     * @param bizId 版本id
     * @return
     */
    long getVersionCount(String bizId);

    /**
     * 功能描述：驳回状态回滚
     * @param: templateId 主键ID
     * @param: bizCurrent 状态
     * @Date: 2019/12/10
     * @return:
     */
    void updateBizCurrentForProcessRefuse(String templateId, String bizCurrent);
}
