package com.glaway.ids.planGeneral.plantabtemplate.entityserviceI;

import com.glaway.foundation.fdk.rt.feign.FeignObjectModelService;
import com.glaway.ids.planGeneral.plantabtemplate.dto.ObjectSelectInfo;
import com.glaway.ids.planGeneral.plantabtemplate.entity.DataSourceObject;

import java.util.List;

/**
 * @Title: EntityServiceI
 * @Description: 数据源对象信息EntityServiceI
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
public interface DataSourceObjectEntityServiceI {

    /**
     * 功能描述：根据页签模版ID查询数据集合
     * @param tabId 页签模版Id
     */
    List<DataSourceObject> queryEntityListByTabId(String tabId);

    /**
     * 功能描述：根据页签模版ID查询对象ID集合
     * @param tabId 页签模版Id
     */
    List<String> queryIdListByTabId(String tabId);

    /**
     * 功能描述：删除数据
     * @param entity 页签模版对象
     * @Date: 2019/8/29
     * @return:
     */
    void deleteDataSourceObject(DataSourceObject entity);

    /**
     * 功能描述：查询实体对象接口数据
     * @param projectModel 对象
     * @param objectPath 路径
     * @Date: 2019/8/29
     * @return:
     */
    List<ObjectSelectInfo> searchObjectSelectsDatagrid(String projectModel, String objectPath);

    /**
     * 功能描述：保存数据
     * @param info  数据源对象
     * @Date: 2019/8/29
     * @return:
     */
    DataSourceObject saveOrUpdate(DataSourceObject info);

    /**
     * 功能描述：根据ID获取对象信息
     * @param id  数据源id
     * @Date: 2019/8/29
     * @return:
     */
    DataSourceObject queryEntityById(String id);

    /**
     * 通过tabId查询页签模板
     * @param id 数据源id
     * @return
     */
    List<DataSourceObject> queryDataSourceByTabId(String id);

    /**
     * 功能描述：保存数据且获取ID
     * @param: info 数据源对象
     * @Date: 2019/9/5
     * @return:
     */
    String getIdBySaveEntity(DataSourceObject info);
}
