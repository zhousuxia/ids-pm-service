package com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: TabCbTemplateEntityServiceI
 * @Date: 2019/8/29-19:00
 * @since
 */
public interface TabCbTemplateEntityServiceI extends CommonService {

    /**
     * 根据活动类型id&组合页签模板id获取绑定的页签组合模板
     * @param id 页签组合模板id
     * @return
     */
    List<TabCombinationTemplate> findTabCbTemplatesByActivityId(String id);

    /**
     * 根据活动类型id查询页签组合模板
     * @param id         活动类型
     * @param templateId 页签组合模板id
     * @return
     */
    List<TabCombinationTemplate> findAllTabCbTemplatesByActivityId(String id, String templateId);

    /**
     * 保存页签组合模板信息并返回id
     * @param template 页签组合模板对象
     * @return
     */
    String saveTabCombationTemplate(TabCombinationTemplate template);

    /**
     * datagrid条件查询
     * @param conditionList 查询条件
     * @param isPage 是否分页
     * @return
     */
    List<TabCombinationTemplate> queryEntity(List<ConditionVO> conditionList, boolean isPage);

    /**
     * 获取数据条数
     * @param conditionList 查询条件
     * @return
     */
    int getCount(List<ConditionVO> conditionList);

    /**
     * 通过id获取页签组合模板信息
     * @param id 页签组合模板id
     * @return
     */
    TabCombinationTemplate findTabCbTempById(String id);

    /**
     * 获取所有组合模板名称
     * @return
     */
    List<String> queryAllName();

    /**
     * 通过名称获取页签组合模板ID
     * @param name 页签组合模板名称
     * @return
     */
    String queryIdByName(String name);


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
     * 根据版本id或版本号查询页签组合模板
     * @param bizId      版本id
     * @param bizversion 版本号
     * @return
     */
    List<TabCombinationTemplate> findTemplatesByBizIdOrBizVersion(String bizId, String bizversion);

    /**
     * 查询当前有效的页签组合模板
     * @param bizId 版本id
     * @return
     */
    TabCombinationTemplate findAvaliableTabCbTemplate(String bizId);

    /**
     * 获取活动类型id-页签组合模板id集合
     * @return
     */
    Map<String,String> getTabCbTempIdAndActivityIdMap();
}
