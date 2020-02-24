package com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.impl;

import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.ids.planGeneral.plantabtemplate.entity.ObjectPropertyInfo;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.ObjectPropertyInfoEntityServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Title: EntityServiceImpl
 * @Description: 数据源属性信息EntityServiceImpl
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
@Service("objectPropertyInfoEntityServiceI")
public class ObjectPropertyInfoEntityServiceImpl extends CommonServiceImpl implements ObjectPropertyInfoEntityServiceI {

    /**
     * 操作日志
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(ObjectPropertyInfoEntityServiceImpl.class);

    /**
     * 功能描述：根据数据源对象ID查询数据集合
     * @param dataSourceId 数据源对象Id
     */
    @Override
    public List<ObjectPropertyInfo> queryInfoListByDataSourceId(String dataSourceId) {
        String[] temp=dataSourceId.split(",");
        String ids = "";
        //数据过滤-获取in查询条件值
        for (String id : temp) {
            ids+=",\'"+id+"\'";
        }
        if(ids.length()>1){
            ids=ids.substring(1);
        }
        String searchHql = "select o from ObjectPropertyInfo o where o.avaliable='1' and o.dataSourceId in ("+ids+") order by TO_NUMBER(o.orderNumber) asc";
        List<ObjectPropertyInfo> objectPropertyInfoList = executeQuery(searchHql, null);
        return objectPropertyInfoList;
    }

    /**
     * 功能描述：删除数据
     * @param entity
     * @Date: 2019/8/29
     * @return:
     */
    @Override
    public void deleteObjectPropertyInfo(ObjectPropertyInfo entity) {
        try{
            entity.setAvaliable("0");
            update(entity);
        }catch (Exception e){
            log.error("ObjectPropertyInfoEntityServiceImpl#deleteObjectPropertyInfo--执行失败!");
        }
    }

    /**
     * 功能描述：更新或者保存
     * @param: entity
     * @Date: 2019/9/2
     */
    @Override
    public void saveOrUpdateEntity(ObjectPropertyInfo entity) {
        if (StringUtils.isNotEmpty(entity.getId())){
            update(entity);
        }else{
            save(entity);
        }
    }

    @Override
    public ObjectPropertyInfo queryEntityById(String id) {
        return getEntity(ObjectPropertyInfo.class, id);
    }

}
