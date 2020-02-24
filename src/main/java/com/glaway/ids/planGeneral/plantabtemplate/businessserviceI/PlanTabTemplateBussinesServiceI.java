package com.glaway.ids.planGeneral.plantabtemplate.businessserviceI;

import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.plantabtemplate.dto.DataRollBack;
import com.glaway.ids.planGeneral.plantabtemplate.dto.DataSourceObjectDto;
import com.glaway.ids.planGeneral.plantabtemplate.dto.ObjectPropertyInfoDto;
import com.glaway.ids.planGeneral.plantabtemplate.dto.TabTemplateDto;

import java.util.List;
import java.util.Map;

/**
 * @Title: BussinesServiceI
 * @Description: 计划通用化(业务组装)BussinesServiceI
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
public interface PlanTabTemplateBussinesServiceI {

    /**
     * 功能描述：根据查询条件查询数据列表
     * @param params 查询条件Map
     */
    PageList searchDatagrid(Map<String, String> params);

    /**
     * 功能描述：批量/单条 根据主键ID和状态Status启用或禁用页签模版
     * @param ids id集合
     * @param status 状态(启用“1”或者禁用“0”)
     */
    FeignJson doStartOrStop(String ids, String status);

    /**
     * 功能描述：批量/单条 根据主键ID删除页签模版
     * @param ids id集合(“，”分隔)
     */
    FeignJson doBatchDelete(String ids);

    /**
     * 功能描述：根据查询条件查询实体对象选择数据列表
     * @param params 查询条件Map
     */
    PageList searchObjectSelectsDatagrid(Map<String, String> params);

    /**
     * 功能描述：根据数据对象ID删除数据
     * @param id
     */
    FeignJson doDeleteDataSourcrObj(String id);

    /**
     * 功能描述：根据页签模板TabId查询所有数据对象(id,objectPath)集合
     * @param tabId
     */
    List<Map<String, String>> getAllDataSourceObject(String tabId);

    /**
     * 功能描述：保存所有信息
     * @param dataSourceObjectList
     * @param objectPropertyList
     */
    FeignJson saveAllInfo(List<DataSourceObjectDto> dataSourceObjectList, List<ObjectPropertyInfoDto> objectPropertyList);

    /**
     * 功能描述：判断名称是否重复
     * @param name
     * @return boolean
     */
    boolean isRepeatTabTemplateInfoByName(String name, String id);

    /**
     * 功能描述：根据页签模板ID查询实列对象所有数据
     * @param tabId
     */
    List<DataSourceObjectDto> getAllDataByTabId(String tabId);

    /**
     * 功能描述：根据页签模板ID查询元属性所有数据
     * @param tabId
     */
    List<ObjectPropertyInfoDto> getAllPropertyByTabId(String tabId);

    /**
     * 功能描述：根据数据对象ID删除数据
     * @param id
     */
    FeignJson doDeleteObjProperty(String id);

    /**
     * 功能描述：根据ID复制数据
     * @param id
     * @return TabTemplateDto
     */
    TabTemplateDto copyTabTemplateEntity(String id);

    /**
     * 获取所有页签模板的所属模块
     * @return
     */
    Map<String,String> getProjectModel();

    /**
     * 功能描述：数据回滚
     * @param dataSourceObjectList
     * @param objectPropertyList
     */
    FeignJson dataRollBack(List<DataRollBack> dataSourceObjectList, List<String> objectPropertyList);

    /**
     * 功能描述：保存所有信息
     * @param dataSourceObjectList
     * @param objectPropertyList
     */
    FeignJson updateOrReviseInfo(List<DataSourceObjectDto> dataSourceObjectList, List<ObjectPropertyInfoDto> objectPropertyList);
}
