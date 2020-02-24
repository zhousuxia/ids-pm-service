package com.glaway.ids.planGeneral.plantabtemplate.businessserviceI.impl;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.plantabtemplate.businessserviceI.PlanTabTemplateBussinesServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.dto.*;
import com.glaway.ids.planGeneral.plantabtemplate.entity.DataSourceObject;
import com.glaway.ids.planGeneral.plantabtemplate.entity.ObjectPropertyInfo;
import com.glaway.ids.planGeneral.plantabtemplate.entity.TabTemplate;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.DataSourceObjectEntityServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.ObjectPropertyInfoEntityServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.TabTemplateEntityServiceI;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @Title: BussinesServiceImpl
 * @Description: 计划通用化(业务组装)PlanTabTemplateBussinesServiceImpl
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
@Service("planTabTemplateBussinesServiceI")
public class PlanTabTemplateBussinesServiceImpl implements PlanTabTemplateBussinesServiceI {

    //页签模版管理Service
    @Autowired
    private TabTemplateEntityServiceI tabTemplateEntityServiceImpl;

    //数据源对象信息Service
    @Autowired
    private DataSourceObjectEntityServiceI dataSourceObjectEntityServiceImpl;

    //数据源属性信息Service
    @Autowired
    private ObjectPropertyInfoEntityServiceI objectPropertyInfoEntityServiceImpl;

    @Autowired
    private SessionFacade sessionFacade;

    //控件标示数据集合
    private static Map<String, String> controlMap = new HashMap<>();

    //权限标示数据集合
    private static Map<String, String> accessMap = new HashMap<>();

    static {
        controlMap.put("0", "文本框");
        controlMap.put("1", "多行文本框");
        controlMap.put("2", "单选");
        controlMap.put("3", "选人框");
        controlMap.put("4", "日期框");
        controlMap.put("5", "下拉框");
        controlMap.put("6", "表格");
        controlMap.put("7", "列");
        controlMap.put("8", "按钮");
        controlMap.put("9", "选择框");
        controlMap.put("10", "可编辑下拉框");

        accessMap.put("0", "编制");
        accessMap.put("1", "编制&启动");
        accessMap.put("2", "启动");
        accessMap.put("3", "/");
    }

    /**
     * 操作日志
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(
        PlanTabTemplateBussinesServiceImpl.class);

    /**
     * 功能描述：根据查询条件查询数据列表
     * @param params 查询条件Map
     */
    @Override
    public PageList searchDatagrid(Map<String, String> params) {
        return tabTemplateEntityServiceImpl.searchDatagrid(params);
    }

    /**
     * 功能描述：批量/单条 启用或禁用页签模版
     * @param ids id集合
     * @param status 状态(启用“1”或者禁用“0”)
     */
    @Override
    public FeignJson doStartOrStop(String ids, String status) {
        int successFlag = tabTemplateEntityServiceImpl.doStartOrStop(ids, status);
        FeignJson feignJson = new FeignJson();
        if (successFlag==1){
            feignJson.setMsg(status+"成功");
        }else{
            feignJson.setMsg(status+"失败");
            feignJson.setSuccess(false);
        }
        return feignJson;
    }

    /**
     * 功能描述：批量/单条 根据主键ID删除页签模版
     * @param ids id集合(“，”分隔)
     */
    @Override
    public FeignJson doBatchDelete(String ids) {
        FeignJson feignJson = new FeignJson();
        try{
            //通过页签模版ID获取数据源属性信息集合
            List<ObjectPropertyInfo> objectPropertyInfoList =queryObjPropertList(ids);
            //执行数据源属性信息删除操作
            for (ObjectPropertyInfo info: objectPropertyInfoList) {
                objectPropertyInfoEntityServiceImpl.deleteObjectPropertyInfo(info);
            }
            //通过页签模版ID获取数据源对象数据集合
            List<DataSourceObject> dataSourceObjectList = dataSourceObjectEntityServiceImpl.queryEntityListByTabId(ids);
            //执行数据源对象删除操作
            for (DataSourceObject info: dataSourceObjectList) {
                dataSourceObjectEntityServiceImpl.deleteDataSourceObject(info);
            }
            //执行页签模版删除操作
            int successFlag = tabTemplateEntityServiceImpl.doBatchDelete(ids);
            if (successFlag==1){
                feignJson.setMsg("删除数据成功");
            }else{
                feignJson.setMsg("删除数据失败");
                feignJson.setSuccess(false);
            }
        }catch (Exception e){
            feignJson.setMsg("删除数据失败！");
            feignJson.setSuccess(false);
            log.error("PlanTabTemplateBussinesServiceImpl#doBatchDelete--删除页签模版失败！");
        }
        return feignJson;
    }

    /**
     * 功能描述：根据查询条件查询实体对象选择数据列表
     * @param params 查询条件Map
     */
    @Override
    public PageList searchObjectSelectsDatagrid(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int rows = Integer.parseInt(params.get("rows"));
        //项目所属模块
        String projectModel = "";
        if (StringUtil.isNotEmpty(params.get("projectModel"))) {
            projectModel = params.get("projectModel");
        }else{
            projectModel = "ids-pm-service";
        }
        //对象类路径
        String objectPath = "";
        if (StringUtil.isNotEmpty(params.get("objectPath"))) {
            objectPath = params.get("objectPath");
        }
        List<ObjectSelectInfo> objectSelectInfoList =dataSourceObjectEntityServiceImpl.searchObjectSelectsDatagrid(projectModel, objectPath);
        int count = objectSelectInfoList.size();
        List<ObjectSelectInfo> resultList = new ArrayList<>();
        int startNum = (page-1)*rows;
        int endNum = page*rows;
        if (endNum>count){
            endNum = count;
        }
        for (int i =startNum; i<endNum; i++){
            resultList.add(objectSelectInfoList.get(i));
        }
        PageList pageList = new PageList(count, resultList);
        pageList.setCurPageNO(page);
        return pageList;
    }

    /**
     * 功能描述：根据数据对象ID删除数据
     * @param id
     */
    @Override
    public FeignJson doDeleteDataSourcrObj(String id) {
        FeignJson feignJson = new FeignJson();
        try{
            //根据数据源对象ID查询元数据集合
            List<ObjectPropertyInfo> objectPropertyInfoList =
                objectPropertyInfoEntityServiceImpl.queryInfoListByDataSourceId(id);
            //如果当前有数据，则进行删除操作
            if (objectPropertyInfoList!=null){
                for (ObjectPropertyInfo info:objectPropertyInfoList) {
                    objectPropertyInfoEntityServiceImpl.deleteObjectPropertyInfo(info);
                }
            }
            DataSourceObject dataSourceObject = dataSourceObjectEntityServiceImpl.queryEntityById(id);
            dataSourceObjectEntityServiceImpl.deleteDataSourceObject(dataSourceObject);
            feignJson.setMsg("删除数据成功");
        }catch (Exception e){
            feignJson.setMsg("删除数据失败");
            feignJson.setSuccess(false);
            log.error("PlanTabTemplateBussinesServiceImpl#doDeleteDataSourcrObj--执行失败!");
        }
        return feignJson;
    }

    /**
     * 功能描述：根据页签模板TabId查询所有数据对象(id,objectPath)集合
     * @param tabId
     */
    @Override
    public List<Map<String, String>> getAllDataSourceObject(String tabId) {
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map;
        List<DataSourceObject> allDataSourceObject = dataSourceObjectEntityServiceImpl.queryEntityListByTabId(tabId);
        if (allDataSourceObject!=null){
            for (DataSourceObject object: allDataSourceObject) {
                map = new HashMap<>();
                map.put("id", object.getId());
                map.put("objectPath", object.getObjectPath());
                mapList.add(map);
            }
        }
        return mapList;
    }

    @Override
    public FeignJson saveAllInfo(List<DataSourceObjectDto> dataSourceObjectList, List<ObjectPropertyInfoDto> objectPropertyList) {
        FeignJson feignJson = new FeignJson();
        try{
            for (DataSourceObjectDto dto: dataSourceObjectList) {
                DataSourceObject info = new DataSourceObject();
                Dto2Entity.copyProperties(dto, info);
                dataSourceObjectEntityServiceImpl.saveOrUpdate(info);
            }
            for (ObjectPropertyInfoDto dto: objectPropertyList) {
                ObjectPropertyInfo info = new ObjectPropertyInfo();
                Dto2Entity.copyProperties(dto, info);
                objectPropertyInfoEntityServiceImpl.saveOrUpdateEntity(info);
            }
            feignJson.setMsg("数据保存成功");
        }catch (Exception e){
            feignJson.setMsg("数据保存失败");
            feignJson.setSuccess(false);
            log.error("PlanTabTemplateBussinesServiceImpl#saveAllInfo--执行失败!");
        }
        return feignJson;
    }

    /**
     * 功能描述：判断名称是否重复
     * @param name
     * @return boolean
     */
    @Override
    public boolean isRepeatTabTemplateInfoByName(String name, String id) {
        List<TabTemplate> list = tabTemplateEntityServiceImpl.queryTabTemplateByName(name, id);
        if (list.size()==0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 功能描述：根据页签模板ID查询所有数据
     * @param tabId
     */
    @Override
    public List<DataSourceObjectDto> getAllDataByTabId(String tabId) {
        List<DataSourceObject> allDataSourceObject = dataSourceObjectEntityServiceImpl.queryEntityListByTabId(tabId);
        List<DataSourceObjectDto> dtoList = new ArrayList<>();
        for (DataSourceObject info : allDataSourceObject) {
            DataSourceObjectDto dto =(DataSourceObjectDto)CodeUtils.JsonBeanToBean(info, DataSourceObjectDto.class);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<ObjectPropertyInfoDto> getAllPropertyByTabId(String tabId) {
        List<ObjectPropertyInfo> infoList = queryObjPropertList(tabId);
        List<ObjectPropertyInfoDto> allList = new ArrayList<>();
        for (ObjectPropertyInfo info: infoList) {
            ObjectPropertyInfoDto dto =(ObjectPropertyInfoDto)CodeUtils.JsonBeanToBean(info, ObjectPropertyInfoDto.class);
            dto.setControl(controlMap.get(info.getControl()));
            dto.setReadWriteAccess(accessMap.get(info.getReadWriteAccess()));
            allList.add(dto);
        }
        return allList;
    }

    /**
     * 功能描述：根据数据对象ID删除数据
     * @param id
     */
    @Override
    public FeignJson doDeleteObjProperty(String id) {
        FeignJson feignJson = new FeignJson();
        try{
            ObjectPropertyInfo info = objectPropertyInfoEntityServiceImpl.queryEntityById(id);
            objectPropertyInfoEntityServiceImpl.deleteObjectPropertyInfo(info);
            feignJson.setMsg("删除数据成功");
        }catch (Exception e){
            feignJson.setMsg("删除数据失败");
            feignJson.setSuccess(false);
            log.error("PlanTabTemplateBussinesServiceImpl#doDeleteDataSourcrObj--执行失败!");
        }
        return feignJson;
    }

    /**
     * 功能描述：根据ID复制数据
     * @param id
     * @return TabTemplateDto
     */
    @Override
    public TabTemplateDto copyTabTemplateEntity(String id) {
        //获取旧数据
        TabTemplate oldInfo = tabTemplateEntityServiceImpl.queryEntityById(id);
        //获取对象数据集合
        List<DataSourceObject> allDataSourceObject = dataSourceObjectEntityServiceImpl.queryEntityListByTabId(oldInfo.getId());
        //通过签模版ID获取数据源属性信息集合
        List<ObjectPropertyInfo> objectPropertyInfoList = queryObjPropertList(oldInfo.getId());
        TabTemplate tabTemplate = tabTemplateEntityServiceImpl.doSave(copTabTemplate(oldInfo));
        for (DataSourceObject info:allDataSourceObject) {
            String dataSourceId = dataSourceObjectEntityServiceImpl.getIdBySaveEntity(copDataSourceObject(info, tabTemplate.getId()));
            for (ObjectPropertyInfo obj: objectPropertyInfoList) {
                if (obj.getDataSourceId().equals(info.getId())){
                    objectPropertyInfoEntityServiceImpl.saveOrUpdateEntity(copObjectPropertyInfo(obj,dataSourceId));
                }
            }
        }
        TabTemplateDto dto = (TabTemplateDto)CodeUtils.JsonBeanToBean(tabTemplate, TabTemplateDto.class);
        return dto;
    }

    //HIB缓存问题，只能new新对象
    private TabTemplate copTabTemplate(TabTemplate oldInfo){
        TabTemplate info = new TabTemplate();
        info.setName(oldInfo.getName()+"副本");
        info.setAvaliable("0");
        info.setDisplayUsage(oldInfo.getDisplayUsage());
        info.setStopFlag(oldInfo.getStopFlag());
        info.setExternalURL(oldInfo.getExternalURL());
        info.setTabType(oldInfo.getTabType());
        info.setRemake(oldInfo.getRemake());
        info.setSource(oldInfo.getSource());
        return info;
    }

    //HIB缓存问题，只能new新对象
    private DataSourceObject copDataSourceObject(DataSourceObject oldInfo, String tabId){
        DataSourceObject info = new DataSourceObject();
        info.setTabId(tabId);
        info.setObjectModelProperty(oldInfo.getObjectModelProperty());
        info.setDataToInterface(oldInfo.getDataToInterface());
        info.setObjectPath(oldInfo.getObjectPath());
        info.setProjectModel(oldInfo.getProjectModel());
        info.setResultSql(oldInfo.getResultSql());
        info.setTableName(oldInfo.getTableName());
        return info;
    }

    //HIB缓存问题，只能new新对象
    private ObjectPropertyInfo copObjectPropertyInfo(ObjectPropertyInfo oldInfo, String dataSourceId){
        ObjectPropertyInfo info = new ObjectPropertyInfo();
        info.setDataSourceId(dataSourceId);
        info.setControl(oldInfo.getControl());
        info.setDisplay(oldInfo.getDisplay());
        info.setFormat(oldInfo.getFormat());
        info.setObjectPath(oldInfo.getObjectPath());
        info.setOperationEvent(oldInfo.getOperationEvent());
        info.setOrderNumber(oldInfo.getOrderNumber());
        info.setPropertyName(oldInfo.getPropertyName());
        info.setPropertyValue(oldInfo.getPropertyValue());
        info.setReadWriteAccess(oldInfo.getReadWriteAccess());
        info.setRequired(oldInfo.isRequired());
        return info;
    }

    /**
     * 功能描述：通过签模版ID获取数据源属性信息集合
     * @param ids id集合
     */
    private List<ObjectPropertyInfo> queryObjPropertList(String ids){
        //通过页签模版ID获取数据源对象ID数据集合
        List<String> idList = dataSourceObjectEntityServiceImpl.queryIdListByTabId(ids);
        //获取数据源属性信息集合
        List<ObjectPropertyInfo> objectPropertyInfoList =
            objectPropertyInfoEntityServiceImpl.queryInfoListByDataSourceId(getDataSourceIdStr(idList));
        return objectPropertyInfoList;
    }

    /**
     * 功能描述：数据源属性信息Id集合转换String字符
     * @param dataSourceIdList
     */
    private String getDataSourceIdStr(List<String> dataSourceIdList){
        StringBuffer dataSourceIdStr = new StringBuffer();
        for (String id : dataSourceIdList) {
            dataSourceIdStr.append(id).append(",");
        }
        return dataSourceIdStr.toString();
    }

    @Override
    public Map<String, String> getProjectModel() {
        Map<String, String> map = new HashMap<String, String>();
        String sql = "select t.id,o.projectmodel from pm_tab_template t" +
                "  left join pm_data_source_Object o on t.id = o.tabid";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> obj : objArrayList) {
                String id = String.valueOf(obj.get("id"));
                String projectmodel = String.valueOf(obj.get("projectmodel"));
                map.put(id, projectmodel);
            }
        }
        return map;
    }

    @Override
    public FeignJson dataRollBack(List<DataRollBack> dataSourceObjectList, List<String> objectPropertyList) {
        FeignJson feignJson = new FeignJson();
        try{
            for (DataRollBack dto: dataSourceObjectList){
                String sql = "update pm_data_source_Object set avaliable='"+dto.getStatus()+"' where id='"+dto.getId()+"'";
                sessionFacade.executeSql2(sql);
            }
            for (String id: objectPropertyList){
                String sql = "update pm_object_property_info set avaliable='1' where id='"+id+"'";
                sessionFacade.executeSql2(sql);
            }
        }catch (Exception e){
            feignJson.setMsg("回滚数据失败");
            feignJson.setSuccess(false);
            log.error("PlanTabTemplateBussinesServiceImpl#dataRollBack--执行失败!");
        }
        return feignJson;
    }

    @Override
    public FeignJson updateOrReviseInfo(List<DataSourceObjectDto> dataSourceObjectList, List<ObjectPropertyInfoDto> objectPropertyList) {
        FeignJson feignJson = new FeignJson();
        try{
            for (DataSourceObjectDto dto:dataSourceObjectList) {
                DataSourceObject dataSourceObject = new DataSourceObject();
                Dto2Entity.copyProperties(dto, dataSourceObject);
                String dataSourceId = dataSourceObjectEntityServiceImpl.getIdBySaveEntity(copDataSourceObject(dataSourceObject, dataSourceObject.getTabId()));
                for (ObjectPropertyInfoDto obj: objectPropertyList) {
                    if (obj.getDataSourceId().equals(dto.getId())){
                        ObjectPropertyInfo info = new ObjectPropertyInfo();
                        Dto2Entity.copyProperties(obj, info);
                        objectPropertyInfoEntityServiceImpl.saveOrUpdateEntity(copObjectPropertyInfo(info,dataSourceId));
                    }
                }
            }
        }catch (Exception e){
            feignJson.setSuccess(false);
        }
        return feignJson;
    }
}
