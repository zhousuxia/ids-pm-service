package com.glaway.ids.planGeneral.tabCombinationTemplate.businessServiceI;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import com.glaway.ids.planGeneral.tabCombinationTemplate.dto.TabCombinationTemplateDto;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.planGeneral.tabCombinationTemplate.vo.CombinationTemplateVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: TabCbTemplateBusinessServiceI
 * @Date: 2019/8/29-18:55
 * @since
 */
public interface TabCbTemplateBusinessServiceI extends BusinessObjectServiceI<TabCombinationTemplate> {

    /**
     * 保存页签组合模板信息forFeign
     * @param params  页签组合模板信息
     * @return
     */
    FeignJson saveTabCbTemplateInfo(Map<String,Object> params);

    /**
     * 保存页签组合模板信息
     * @param params 页签组合模板信息
     * @return
     */
    FeignJson updateTabCbTemplateInfo(Map<String,Object> params);

    /**
     * 保存页签组合模板信息
     * @param voList 页签组合模板集合
     * @param template 页签组合模板
     * @param curUser 用户对象
     * @param orgId 组织id
     */
    void saveInfo(List<CombinationTemplateVo> voList, TabCombinationTemplate template, TSUserDto curUser, String orgId);

    /**
     * 页签组合模板启用禁用
     * @param ids 页签组合模板ids
     * @param status 状态
     * @return
     */
    FeignJson doStatusChange(String ids,String status,String userId);

    /**
     * 页签组合模板删除
     * @param ids 页签组合模板ids
     * @return
     */
    FeignJson doBatchDel(String ids);

    /**
     * 通过活动类型ID判断当前活动类型是否绑定页签组合模板
     * @param id          活动类型
     * @param templateId  页签组合模板id
     * @return
     */
    FeignJson isActivityTypeManageUse(String id, String templateId);

    /**
     * datagrid条件查询
     * @param conditionList 查询条件
     * @param isPage 是否分页
     * @return
     */
    FeignJson searchDataGrid(List<ConditionVO> conditionList, boolean isPage);

    /**
     * 页签组合模板提交审批
     * @param map 数据集合
     * @return
     */
    FeignJson doSubmitApprove(Map<String,String> map);

    /**
     * 版本回退
     * @param params 页签组合模板id,版本id,版本号，回退方式(回退&撤消)
     * @return
     */
    FeignJson backVersion(Map<String,String> params);

    /**
     * 通过计划Id获取对应的页签组合模板
     * @param planId 计划id
     * @return
     */
    TabCombinationTemplate getTabCbTemplateByPlanId(String planId);

    /**
     * 根据生命周期设置页签组合模板状态
     * @param templateDtos 页签组合模板数据
     * @return
     */
    List<TabCombinationTemplateDto> setStatusByLifeCycleStatus(List<TabCombinationTemplateDto> templateDtos);

    /**
     * 获取页签组合模板生命周期
     * @return
     */
    List<LifeCycleStatus> getLifeCycleStatusList();

    /**
     * 页签组合模板启动审批流程
     * @param tabCbTemplateId 页签组合模板id
     * @param leader          室领导
     * @param deptLeader      部领导
     * @param userId          当前用户id
     * @param orgId           组织id
     */
    void startTabCbTemplateProcess(String tabCbTemplateId, String leader, String deptLeader, String userId, String orgId);

    /**
     * 流程启动
     *
     * @param template             页签组合模板信息
     * @param variables            流程变量
     * @param processDefinitionKey 流程标识
     * @param curUser              当前用户信息
     * @param orgId                组织id
     */
    void submitProjectTemplateFlow(TabCombinationTemplate template, Map<String, Object> variables,String processDefinitionKey ,TSUserDto curUser,String orgId);

    /**
     * 流程继续
     *
     * @param template     页签组合模板信息
     * @param curUser      当前用户信息
     * @param orgId        组织id
     */
    void submitProjectTemplateFlowAagin(TabCombinationTemplate template,TSUserDto curUser,String orgId);

    /**
     * 回退项目模板 版本
     *
     * @param id      项目模板id
     * @param bizId   项目模板版本Id
     * @param type    类型： （0，回退小版本； 1，回退大版本）
     * @param userId  用户id
     * @param orgId   组织id
     */
    void backToVersion(String id, String bizId, String type,String userId,String orgId);

    /**流程结束时更新数据状态
     * @param templateId 页签组合模板id
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
     * 根据活动类型同步更新页签组合模板
     * @param activityTypeManage 活动类型
     */
    void updateTabCbTemplateByActivity(ActivityTypeManage activityTypeManage);
}
