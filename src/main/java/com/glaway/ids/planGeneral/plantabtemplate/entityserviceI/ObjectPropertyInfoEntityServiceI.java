package com.glaway.ids.planGeneral.plantabtemplate.entityserviceI;

import com.glaway.ids.planGeneral.plantabtemplate.entity.ObjectPropertyInfo;

import java.util.List;


/**
 * @Title: EntityServiceI
 * @Description: 数据源属性信息EntityServiceI
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
public interface ObjectPropertyInfoEntityServiceI {

    /**
     * 功能描述：根据数据源对象ID查询数据集合
     * @param dataSourceId 数据源对象Id
     */
    List<ObjectPropertyInfo> queryInfoListByDataSourceId(String dataSourceId);

    /**
     * 功能描述：删除数据
     * @param entity 数据源属性对象
     */
    void deleteObjectPropertyInfo(ObjectPropertyInfo entity);

    /**
     * 功能描述：更新或者保存
     * @param: entity 数据源属性对象
     * @Date: 2019/9/2
     */
    void saveOrUpdateEntity(ObjectPropertyInfo entity);

    /**
     * 功能描述：根据ID查询数据
     * @param id 数据源属性对象id
     */
    ObjectPropertyInfo queryEntityById(String id);
}
