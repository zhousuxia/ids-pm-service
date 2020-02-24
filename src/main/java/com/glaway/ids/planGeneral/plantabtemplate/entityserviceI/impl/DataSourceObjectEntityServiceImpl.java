package com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.impl;

import com.alibaba.fastjson.JSONObject;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.container.containershow.service.FeignObjectModelServiceI;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.rt.feign.FeignObjectModelService;
import com.glaway.ids.planGeneral.plantabtemplate.dto.ObjectSelectInfo;
import com.glaway.ids.planGeneral.plantabtemplate.entity.DataSourceObject;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.DataSourceObjectEntityServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Title: EntityServiceImpl
 * @Description: 数据源对象信息EntityServiceImpl
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
@Service("dataSourceObjectEntityServiceI")
public class DataSourceObjectEntityServiceImpl extends CommonServiceImpl implements DataSourceObjectEntityServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    /**
     * 操作日志
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(DataSourceObjectEntityServiceImpl.class);

    private FeignObjectModelService feignObjectModelService;

    @Autowired
    private FeignObjectModelServiceI feignObjectModelServiceImpl;

    //foundation包
    private final static String FOUNDATION = "com.glaway.foundation";

    /**
     * 功能描述：根据页签模版ID查询对象集合
     * @param tabId 页签模版Id
     */
    @Override
    public List<DataSourceObject> queryEntityListByTabId(String tabId) {
        String[] temp=tabId.split(",");
        String ids = "";
        //数据过滤-获取in查询条件值
        for (String id : temp) {
            ids+=",\'"+id+"\'";
        }
        if(ids.length()>1){
            ids=ids.substring(1);
        }
        String searchHql = "select d from DataSourceObject d where d.avaliable='1' and d.tabId in ("+ids+")";
        List<DataSourceObject> dataSourceObjectList = executeQuery(searchHql, null);
        return dataSourceObjectList;
    }

    /**
     * 功能描述：根据页签模版ID查询对象ID集合
     * @param tabId 页签模版Id
     */
    @Override
    public List<String> queryIdListByTabId(String tabId) {
        String[] temp=tabId.split(",");
        String ids = "";
        //数据过滤-获取in查询条件值
        for (String id : temp) {
            ids+=",\'"+id+"\'";
        }
        if(ids.length()>1){
            ids=ids.substring(1);
        }
        String searchHql = "select d.id from DataSourceObject d where d.avaliable='1' and d.tabId in ("+ids+")";
        List<String> idList = executeQuery(searchHql, null);
        return idList;
    }

    /**
     * 功能描述：删除数据
     * @param entity
     * @Date: 2019/8/29
     * @return:
     */
    @Override
    public void deleteDataSourceObject(DataSourceObject entity) {
        try{
            entity.setAvaliable("0");
            update(entity);
        }catch (Exception e){
            log.error("DataSourceObjectEntityServiceImpl#deleteDataSourceObject--执行失败!");
        }
    }

    /**
     * 功能描述：查询实体对象接口数据
     * @param projectModel
     * @param objectPath
     * @Date: 2019/8/29
     * @return:
     */
    @Override
    public List<ObjectSelectInfo> searchObjectSelectsDatagrid(String projectModel, String objectPath) {
        List<ObjectSelectInfo> objectSelectInfoList = new ArrayList<>();
        feignObjectModelService = null;
        feignObjectModelService = feignObjectModelServiceImpl.getObjectModelService(feignObjectModelService, projectModel);
        //获取所有对象集合
        if (feignObjectModelService != null){
            List<Map<String, Object>> objList = feignObjectModelService.getAllObjectModelsOfGlobj();
            String uri;
            ObjectSelectInfo info;
            for (Map<String, Object> entityMap: objList){
                uri = (String)entityMap.get("uri");
                //objectPath为空内存模糊查询，不为空获取全部数据(去除Foundation数据)
                if (StringUtils.isNotEmpty(objectPath) && uri.contains(objectPath)&& !uri.contains(FOUNDATION)){
                    info = getInfo(uri, (String)entityMap.get("tableName"), feignObjectModelService);
                    objectSelectInfoList.add(info);
                }else if (StringUtils.isEmpty(objectPath)&& !uri.contains(FOUNDATION)){
                    info = getInfo(uri, (String)entityMap.get("tableName"), feignObjectModelService);
                    objectSelectInfoList.add(info);
                }
            }
        }
        return objectSelectInfoList;
    }

    /**
     * 功能描述：根据类路径获取属性集合字段
     * @param uri
     * @Date: 2019/8/29
     * @return:
     */
    private String getObjectModelProperty(String uri, FeignObjectModelService feignObjectModelService){
        List<JSONObject> fieldsList = feignObjectModelService.getObjectDeclaredFieldsByEntityUri(uri);
        StringBuffer sb = new StringBuffer();
        for (JSONObject jsonObject: fieldsList){
            sb.append(jsonObject.get("name")).append(",");
        }
        String objectModelProperty = sb.toString();
        return objectModelProperty;
    }

    //数据解析
    private ObjectSelectInfo getInfo(String uri, String tableName, FeignObjectModelService feignObjectModelService){
        String objectModelProperty = getObjectModelProperty(uri, feignObjectModelService);
        ObjectSelectInfo info = new ObjectSelectInfo();
        info.setObjectModelProperty(objectModelProperty.substring(0, objectModelProperty.length()-1));
        info.setObjectPath(uri);
        info.setTableName(tableName);
        return info;
    }

    /**
     * 功能描述：保存数据
     * @param info
     */
    @Override
    public DataSourceObject saveOrUpdate(DataSourceObject info) {
        if (info.getId()!=null){
            update(info);
            info = queryEntityById(info.getId());
        }else {
            Serializable id = save(info);
            info.setId(id.toString());
        }
        return info;
    }

    /**
     * 功能描述：根据ID获取对象信息
     * @param id
     * @Date: 2019/8/29
     * @return:
     */
    @Override
    public DataSourceObject queryEntityById(String id) {
        return getEntity(DataSourceObject.class, id);
    }

    @Override
    public List<DataSourceObject> queryDataSourceByTabId(String id) {
        String hql = "from DataSourceObject where 1=1 and avaliable='1' and tabId = ? ";
        List<DataSourceObject> list = sessionFacade.findHql(hql, id);
        return list;
    }

    /**
     * 功能描述：保存数据且获取ID
     * @param: info
     * @Date: 2019/9/5
     * @return:
     */
    @Override
    public String getIdBySaveEntity(DataSourceObject info) {
        Serializable id = save(info);
        return id.toString();
    }

}
