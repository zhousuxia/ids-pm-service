/*
 * 文件名：ProjLibServiceImpl.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：wangshen
 * 修改时间：2015年5月17日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.service.impl;


import com.alibaba.fastjson.JSON;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.core.dto.ObjectBusinessBPMNLinkDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.attribute.constant.DisplayStyleConstant;
import com.glaway.foundation.businessobject.attribute.dto.AdditionalAttributeDto;
import com.glaway.foundation.businessobject.attribute.dto.EntityAttributeAdditionalAttributeDto;
import com.glaway.foundation.businessobject.attribute.dto.InstanceAttributeValDto;
import com.glaway.foundation.businessobject.constant.BusinessObjectConstant;
import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.entity.GLObject;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileAttachmentDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileTypeDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepRoleFileAuthDto;
import com.glaway.foundation.fdk.dev.service.FeignAttributeService;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignRoleService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.constant.LifeCycleConstant;
import com.glaway.ids.config.auth.ProjLibAuthManager;
import com.glaway.ids.config.auth.ProjectLibraryAuthorityEnum;
import com.glaway.ids.config.entity.ProjectLibRoleFileAuth;
import com.glaway.ids.config.entity.ProjectLibTemplateFileCategory;
import com.glaway.ids.config.entity.RepFile;
import com.glaway.ids.config.service.ProjectLibTemplateServiceI;
import com.glaway.ids.constant.*;
import com.glaway.ids.project.menu.entity.ProjTeamLink;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PlanLog;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.entity.ProjDocRelation;
import com.glaway.ids.project.projectmanager.entity.ProjLibFile;
import com.glaway.ids.project.projectmanager.entity.ProjectLibAuthTemplateLink;
import com.glaway.ids.project.projectmanager.service.ProjLibAuthServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjLibDocumentVo;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projectmanager.vo.RepFileAuthVo;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateLibServiceI;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.ProcessUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("projLibService")
@Transactional
public class ProjLibServiceImpl extends CommonServiceImpl implements ProjLibServiceI {

    private static final SystemLog LOG = BaseLogFactory.getSystemLog(ProjLibServiceImpl.class);
    /**
     *  sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;

    /**
     * 项目Service
     */
    @Autowired
    private ProjectServiceI projectService;

    /**
     * 项目角色Service
     */
    @Autowired
    private ProjRoleServiceI projRoleService;


    @Autowired
    private FeignRepService repService;


    @Autowired
    private FeignRoleService roleService;

    /**
     * 项目库权限处理
     */
    @Autowired
    private ProjLibAuthServiceI projLibAuthService;


    @Autowired
    private FeignAttributeService attributeService;

    @Autowired
    private FeignUserService userService;


    @Autowired
    private ProjTemplateLibServiceI projTemplateLibService;

    @Autowired
    private ProjectLibTemplateServiceI projectLibTemplateService;

    /**
     * 注入工作流
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;


    @Autowired
    private PlanServiceI planService;

    @Autowired
    private Environment env;

    /**
     * 项目库业务对象
     */
    @Autowired
    @Qualifier("businessObjectService")
    private BusinessObjectServiceI<ProjLibFile> boServiceForProjLibFile;

    @Value(value="${spring.application.name}")
    private String appKey;


    private String docPath = "";



    @Override
    public String cerateDefaultFolder(String projectId, String fileName,String userId) {
        Project project = (Project)sessionFacade.getEntity(Project.class,projectId);
        return createFile(projectId, null, project.getName() + "-" + project.getProjectNumber(), 0,userId);
    }


    @Override
    public List<ProjDocRelation> getRelationDocByDocId(String docId) {
        return this.findHql("from ProjDocRelation t where t.docId=? ", docId);
    }

    @Override
    public List<ProjLibFile> getProjLibFile(String fileId) {
        return this.findHql("from ProjLibFile t where t.projectFileId=? ", fileId);
    }

    @Override
    public void addOrUpdateEntityAttrAdditionalAttribute(GLObject obj, Enumeration<String> attrNames, Map<String, Object> paramsMap) {
        String[] arr = null;
        boolean updateFlag = false;
        List<String[]> maybeList = new ArrayList<String[]>();// 可能是对象属性值对应的软属性name
        for (String key : paramsMap.keySet()) {
            String requestName = key;
            if (requestName.split("_").length >= 3) {
                maybeList.add(new String[] {getEntityAttrNameAttrVal(requestName),
                        requestName.substring(requestName.lastIndexOf("_") + 1), requestName});
            }
        }
        if(!CommonUtil.isEmpty(attrNames)) {
            for (String key : paramsMap.keySet()) {
                String requestName = key;
                if (requestName.split("_").length >= 3) {
                    maybeList.add(new String[] {getEntityAttrNameAttrVal(requestName),
                            requestName.substring(requestName.lastIndexOf("_") + 1), requestName});
                }
            }
        }
        for (String[] maybe : maybeList) {
            String instanceid = obj.getClass().getName() + ":" + maybe[0] + ":" + obj.getId();
            // 根据可能拼出的oid到数据库进行比较 如果查处有数据，则视为修改操作
            if (attributeService.findAttrMapByOid(instanceid).size() > 0) {
                updateFlag = true;
                break;
            }
        }
        // 获取所有软属性
        List<AdditionalAttributeDto> allAdditionalAttributeList = attributeService.getAllAdditionalAttribute();
        Map<String, AdditionalAttributeDto> allAdditionalAttributeMap = new HashMap<String, AdditionalAttributeDto>();
        if (allAdditionalAttributeList.size() > 0) {
            for (AdditionalAttributeDto additionalAttribute : allAdditionalAttributeList) {
                allAdditionalAttributeMap.put(additionalAttribute.getId(), additionalAttribute);
            }
        }
        // 新增逻辑
        if (!updateFlag) {
            // 获取跟当前实体相关的所有属性值对应的软属性 因为是新增，所以需要从EntityAttributeAdditionalAttribute关系中获取最新的关联关系
            List<EntityAttributeAdditionalAttributeDto> entityAttrAllAdditionalAttributeList = attributeService.getEntityAttributeAdditionalAttributeListByCondition(
                    obj.getClass().getName(), "", "");
            // 存放配置软属性的顺序
            Map<String, Integer> addAttrOrderMap = new HashMap<String, Integer>();
            // 属性及属性值 对应的所有软属性map
            Map<String, List<AdditionalAttributeDto>> entityAttrNameAttrValMap = new HashMap<String, List<AdditionalAttributeDto>>();// key为entityAttrName_entityAttrVal
            if (entityAttrAllAdditionalAttributeList.size() > 0) {
                String mapKey = null;
                for (EntityAttributeAdditionalAttributeDto entityAttributeAdditionalAttribute : entityAttrAllAdditionalAttributeList) {
                    List<AdditionalAttributeDto> list = new ArrayList<AdditionalAttributeDto>();
                    mapKey = entityAttributeAdditionalAttribute.getEntityAttrName() + "_"
                            + entityAttributeAdditionalAttribute.getEntityAttrVal();
                    if (entityAttrNameAttrValMap.get(mapKey) != null) {
                        list = entityAttrNameAttrValMap.get(mapKey);
                    }
                    list.add(allAdditionalAttributeMap.get(entityAttributeAdditionalAttribute.getAddAttrId()));
                    entityAttrNameAttrValMap.put(mapKey, list);
                    addAttrOrderMap.put(
                            mapKey + "_" + entityAttributeAdditionalAttribute.getAddAttrId(),
                            entityAttributeAdditionalAttribute.getAddAttrOrder());
                }
            }
            Map<String, String> addAttrVal = new HashMap<String, String>();
            Map<String, Integer> addAttrOrder = new HashMap<String, Integer>();
            for (String[] maybe : maybeList) {
                String maybeEntityAttrNameAttrVal = maybe[0];// entityAttrName:entityAttrVal
                // 需要转换成entityAttrName_entityAttrVal
                String mapKey = maybeEntityAttrNameAttrVal.split(":")[0]
                        + "_"
                        + maybeEntityAttrNameAttrVal.replace(
                        maybeEntityAttrNameAttrVal.split(":")[0] + ":", "");
                if (entityAttrNameAttrValMap.get(mapKey) != null
                        && entityAttrNameAttrValMap.get(mapKey).size() > 0) {
                    for (AdditionalAttributeDto addAttr : entityAttrNameAttrValMap.get(mapKey)) {
                        if (maybe[1].substring(maybe[1].lastIndexOf("_") + 1).equals(
                                addAttr.getName())) {
                            addAttrVal.put(addAttr.getId(), paramsMap.get(maybe[2]).toString());
                            if (DisplayStyleConstant.DATADICT_COMBOBOX.equals(addAttr.getDisplayStyle())
                                    && "1".equals(addAttr.getMultiSelect())) {
                                for (int i = 0; i < paramsMap.get(maybe[2]).toString().split(",").length; i++) {
                                    arr[i] = paramsMap.get(maybe[2]).toString();
                                }
                                addAttrVal.put(addAttr.getId(), StringUtils.join(arr, ","));
                            }
                            if(paramsMap.get(maybe[2]).toString()!=null) {
                                addAttrVal.put(addAttr.getId(), paramsMap.get(maybe[2]).toString());
                            }
                            addAttrOrder.put(
                                    addAttr.getId(),
                                    addAttrOrderMap.get(mapKey + "_" + addAttr.getId()) == null ? 0 : addAttrOrderMap.get(mapKey
                                            + "_"
                                            + addAttr.getId()));
                            Map<String, Object> attrMap = new HashMap<>();
                            attrMap.put("glObjId",addAttr.getId());
                            attrMap.put("glObjClassName",obj.getClass().getName());
                            attrMap.put("addAttrVal",addAttrVal);
                            attrMap.put("addAttrValOrder",addAttrOrder);

                            attributeService.updateEntityAttrAddAttribute(
                                    maybeEntityAttrNameAttrVal.split(":")[0],
                                    maybeEntityAttrNameAttrVal.replace(
                                            maybeEntityAttrNameAttrVal.split(":")[0] + ":", ""),
                                    attrMap);
                            addAttrVal = new HashMap<String, String>();
                            break;
                        }
                    }
                }
            }
        }
        // 修改逻辑
        else {
            for (String[] maybe : maybeList) {
                String instanceid = obj.getClass().getName() + ":" + maybe[0] + ":" + obj.getId();
                // 根据可能拼出的oid到数据库进行比较 如果查处有数据，则视为修改操作
                List<InstanceAttributeValDto> list = attributeService.getInstanceAttributeValByInstanceId(instanceid);
                if (list.size() > 0) {
                    List<InstanceAttributeValDto> updateList = new ArrayList<InstanceAttributeValDto>();
                    for (InstanceAttributeValDto val : list) {
                        String fieldName = val.getInstanceId().split(":")[1];
                        String fieldVal = val.getInstanceId().split(":")[2];
                        String attrName = allAdditionalAttributeMap.get(val.getAddAttrId()).getName();
                        if ((fieldName + "_" + fieldVal + "_" + attrName).equals(maybe[2])) {
                            val.setAddAttrVal(paramsMap.get(maybe[2]).toString());
                            if (DisplayStyleConstant.DATADICT_COMBOBOX.equals(allAdditionalAttributeMap.get(val.getAddAttrId()).getDisplayStyle())
                                    && "1".equals(allAdditionalAttributeMap.get(val.getAddAttrId()).getMultiSelect())) {
                                for (int i = 0; i < paramsMap.get(maybe[2]).toString().split(",").length; i++) {
                                    arr[i] = paramsMap.get(maybe[2]).toString();
                                }


                                val.setAddAttrVal(StringUtils.join(arr, ","));
                            }
                            updateList.add(val);
                            attributeService.updateInstanceAttributeVal(updateList);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initEntityAttrAdditionalAttribute(GLObject obj, String entityAttrName, String entityAttrVal, Map<String, String> entityAttrAdditionalAttributeValMap) {
        // 获取跟当前实体相关的所有属性值对应的软属性 因为是新增，所以需要从EntityAttributeAdditionalAttribute关系中获取最新的关联关系
        List<EntityAttributeAdditionalAttributeDto> entityAttrAllAdditionalAttributeList = attributeService.getEntityAttributeAdditionalAttributeListByCondition(
                "com.glaway.foundation.rep.entity.RepFile", entityAttrName, entityAttrVal);
        if(!CommonUtil.isEmpty(entityAttrAllAdditionalAttributeList)) {
            int orderByNum = 1;
            for(EntityAttributeAdditionalAttributeDto attr : entityAttrAllAdditionalAttributeList) {
                InstanceAttributeValDto val = new InstanceAttributeValDto();
                val.setInstanceId("com.glaway.foundation.rep.entity.RepFile"+":"+attr.getEntityAttrName()+":"+attr.getEntityAttrVal()+":"+obj.getId());
                val.setAddAttrId(attr.getAddAttrId());
                if(!CommonUtil.isEmpty(entityAttrAdditionalAttributeValMap)&&!CommonUtil.isEmpty(entityAttrAdditionalAttributeValMap.get(attr.getAddAttrId()))){
                    val.setAddAttrVal(entityAttrAdditionalAttributeValMap.get(attr.getAddAttrId()));
                }
                val.setOrderNum(orderByNum++);
                val.setAttrType(1);
                val.setOid(obj.getId());
                attributeService.saveInstanceAttrVal(appKey,val);
            }
        }
    }

    @Override
    public void saveProjLog(PlanLog planLog) {
        CommonUtil.glObjectSet(planLog);
        sessionFacade.save(planLog);
    }

    @Override
    public List<ProjLibDocumentVo> getFilesByBizId(String bizId) {
        List<RepFileDto> repFileList = repService.getRepFileByBizId(appKey,bizId);
        List<ProjLibDocumentVo> vos = convertToVos(repFileList);
        return vos;
    }

    @Override
    public void backVersion(String id, String bizId) {
        repService.deleteRepFileByRepFileId(appKey, id);
        backToPrevDocVersion(bizId);
    }

    @Override
    public void backToPrevDocVersion(String bizId) {
        List<RepFileDto> repFileList = repService.findBusinessObjectHistoryByBizId(appKey,
                bizId);
        RepFileDto r = repFileList.get(0);
        r.setAvaliable(BusinessObjectConstant.Avaliable.USED);
        repService.updateRepFileById(appKey,r);
    }

    @Override
    public Map<String, String> saveEntityAttrAdditionalAttributeVal(Map<String, Object> paramsMap) {
        Map<String,String> entityAttributeAdditionalAttributeMap = new HashMap<String, String>();
        Map<String,String> additionalAttributeMap = new HashMap<String, String>();
        List<AdditionalAttributeDto> additionalAttributeList= attributeService.getAllAdditionalAttribute();
        if(additionalAttributeList.size()>0){
            for(AdditionalAttributeDto curAdditionalAttribute:additionalAttributeList){
                additionalAttributeMap.put(curAdditionalAttribute.getName(), curAdditionalAttribute.getId());
            }
        }
        String[] arr = null;
        boolean updateFlag = false;
        List<String[]> maybeList = new ArrayList<String[]>();// 可能是对象属性值对应的软属性name
        for (String key : paramsMap.keySet()) {
            String requestName = key;
            if (requestName.split("_").length >= 3) {
                maybeList.add(new String[] {getEntityAttrNameAttrVal(requestName),
                        requestName.substring(requestName.lastIndexOf("_") + 1), requestName});
            }
        }
        for(String[] curString: maybeList){
            if(curString.length>=3){
                String value = String.valueOf(paramsMap.get(curString[2]));
                String name = curString[1];
                if(!CommonUtil.isEmpty(additionalAttributeMap.get(name))){
                    entityAttributeAdditionalAttributeMap.put(additionalAttributeMap.get(name), value);
                }
            }
        }
        return entityAttributeAdditionalAttributeMap;
    }


    /**
     * Description: <br>
     * 根据requestName获取实体属性及实体属性值 如: level_1_addattr则取出的为level:1
     *
     * @param requestName
     * @return
     * @see
     */
    public static String getEntityAttrNameAttrVal(String requestName) {
        String tmp = requestName.substring(0, requestName.lastIndexOf("_"));
        return tmp.substring(0, tmp.indexOf("_")) + ":" + tmp.substring(tmp.indexOf("_") + 1);
    }

    @Override
    public void saveEntityAttrAdditionalAttribute(RepFileDto repFile, List<EntityAttributeAdditionalAttributeDto> list) {
        for(EntityAttributeAdditionalAttributeDto curEntityAttributeAdditionalAttribute : list){
            EntityAttributeAdditionalAttributeDto entityAttributeAdditionalAttribute = new EntityAttributeAdditionalAttributeDto();
            entityAttributeAdditionalAttribute.setAddAttrId(curEntityAttributeAdditionalAttribute.getAddAttrId());
            entityAttributeAdditionalAttribute.setAddAttrOrder(curEntityAttributeAdditionalAttribute.getAddAttrOrder());
            entityAttributeAdditionalAttribute.setEntityAttrVal(repFile.getId());
            entityAttributeAdditionalAttribute.setEntityAttrName(repFile.getId());
            entityAttributeAdditionalAttribute.setEntityUri("com.glaway.foundation.rep.entity.RepFile");
            CommonUtil.glObjectSet(entityAttributeAdditionalAttribute);
            attributeService.saveEntityAttributeAdditionalAttribute(appKey,entityAttributeAdditionalAttribute);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageList queryEntity(List<ConditionVO> conditionList, String folderId,
                                String projectId, String createName, String modifiName, Map<String,String> nameAndValueMap, String docTypeId,String userId) {
        String libId = projRoleService.getLibIdByProjectId(projectId);

        FeignJson fJson = repService.getPageListForIDS(appKey,conditionList,libId,folderId,projectId,createName,modifiName,docTypeId,userId);
            /*String hql = "";
            if (StringUtils.isEmpty(createName) && StringUtils.isEmpty(modifiName)) {
                hql = " from RepFile t where t.fileType=1 and t.libId='" + libId
                        + "' and t.parentId='" + folderId + "' and t.avaliable=1";
            }
            else {
                hql = " from RepFile t where t.fileType=1 and t.libId='" + libId
                        + "' and t.parentId='" + folderId + "' and t.avaliable=1";
            }
            if (UserUtil.getInstance().getUser().getSecurityLevel() != null) {
                hql += " and t.securityLevel <= "
                        + UserUtil.getInstance().getUser().getSecurityLevel();
            }
            if (StringUtils.isNotEmpty(createName)) {
                hql = hql
                        + " and t.firstBy in (select u.id from TSUser u where (u.realName like '%"
                        + createName + "%' or u.userName like '%" + createName + "%'))";
            }
            if (StringUtils.isNotEmpty(docTypeId)) {
                hql += " and t.fileTypeId = '" + docTypeId + "'";
            }
            if (StringUtils.isNotEmpty(modifiName)) {
                hql = hql
                        + " and t.createBy in (select u.id from TSUser u where (u.realName like '%"
                        + modifiName + "%' or u.userName like '%" + modifiName + "%'))";
            }

            Map<String, String> map = new HashMap<String, String>();
            for (ConditionVO p : conditionList) {
                if (StringUtil.isNotEmpty(p.getSort()) && "order".equals(p.getKey())) {
                    if (p.getSort().equals("status")) {
                        p.setSort("bizCurrent");
                    }
                    if (p.getSort().equals("docName")) {
                        p.setSort("fileName");
                    }
                    if (p.getSort().equals("docNumber")) {
                        p.setSort("fileNumber");
                    }
                    if ("version".equals(p.getSort())) {
                        p.setSort("bizVersion");
                    }
                    if ("createName".equals(p.getSort())) {
                        p.setSort("createFullName");
                    }
                }

            }
            // hql=hql+" order by "+orderBy+" t.createTime ";
            map.put("RepFile.createTime", "desc");
            List<RepFile> list = sessionFacade.searchByCommonFormHql(hql, conditionList, true, map);*/
        Map<String,Object> map = fJson.getAttributes();
        /*List<RepFileDto> list = new ArrayList<>();
        for (Map<String,Object> link : (List<Map<String,Object>> )map.get("list")) {
            RepFileDto dto = new RepFileDto();
            try {
                BeanUtil.copyMap2Bean(dto,link);
                list.add(dto);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }*/
        List<RepFileDto> list = JSON.parseArray(JSON.toJSONString(map.get("list")),RepFileDto.class);
        List<ProjLibDocumentVo> voList = convertToVosByPower(list, projectId, folderId, userId);
        List<ProjLibDocumentVo> voListEnd = new ArrayList<ProjLibDocumentVo>();
//            List<ProjLibDocumentVo> voListEndPage = new ArrayList<ProjLibDocumentVo>();
        if(!CommonUtil.isEmpty(nameAndValueMap)){
            Map<String,String> additionalAttributeMap = new HashMap<String, String>();
            List<AdditionalAttributeDto> additionalAttributeList= attributeService.getAllAdditionalAttribute();
            if(additionalAttributeList.size()>0){
                for(AdditionalAttributeDto curAdditionalAttribute:additionalAttributeList){
                    additionalAttributeMap.put(curAdditionalAttribute.getName(), curAdditionalAttribute.getId());
                }
            }
            Map<String,String> idAndValueMap = new HashMap<String, String>();
            for(String cur :nameAndValueMap.keySet()){
                if(!CommonUtil.isEmpty(additionalAttributeMap.get(cur))){
                    idAndValueMap.put(additionalAttributeMap.get(cur), nameAndValueMap.get(cur));
                }
            }


            Map<String,String> repFileIdMap = new HashMap<String, String>();
            if(!CommonUtil.isEmpty(voList)){
                for(ProjLibDocumentVo curProjLibDocumentVo : voList){
                    repFileIdMap.put(curProjLibDocumentVo.getId(), curProjLibDocumentVo.getId());
                }
            }


            //查询出绑定的数值：
            Map<String,String> instanceAttributeValMap = new HashMap<String, String>();

            List<InstanceAttributeValDto> instanceAttributeValList= attributeService.getAllInstanceAttributeVal(appKey);
            if(!CommonUtil.isEmpty(instanceAttributeValList)){
                for(InstanceAttributeValDto curInstanceAttributeVal : instanceAttributeValList){
                    if(!CommonUtil.isEmpty(repFileIdMap) && (!CommonUtil.isEmpty(repFileIdMap.get(curInstanceAttributeVal.getOid())))){
                        instanceAttributeValMap.put(curInstanceAttributeVal.getOid()+"+"+curInstanceAttributeVal.getAddAttrId(), curInstanceAttributeVal.getAddAttrVal());

                    }
                }
            }

            //除去软属性对应的搜索条件，不满足的数据
            if(!CommonUtil.isEmpty(voList)){
                for(ProjLibDocumentVo curProjLibDocumentVo : voList){
                    String id = curProjLibDocumentVo.getId();
                    boolean flag = false;
                    for(String curKey : idAndValueMap.keySet()){
                        if(instanceAttributeValMap.containsKey(id+"+"+curKey)){
                            if(CommonUtil.isEmpty(instanceAttributeValMap.get(id+"+"+curKey))){
                                flag = true;
                                break;
                            }else{
                                if(!instanceAttributeValMap.get(id+"+"+curKey).toUpperCase().contains(idAndValueMap.get(curKey).toUpperCase())){
                                    flag = true;
                                    break;
                                }
                            }
                        }else{
                            continue;
                        }
                    }
                    if(!flag){
                        voListEnd.add(curProjLibDocumentVo);
                    }
                }
            }

        }else{
            voListEnd = voList;
        }

        int count = Integer.valueOf(map.get("count").toString());
        PageList pageList = new PageList(count, voListEnd);

        return pageList;
    }

    @Override
    public void updatePathRepFile(String parentId, String repFileIds) {
        // 批量修改路径
        String[] str = repFileIds.split(",");
        String temp = "";
        for (String codeTem : str) {
            if(!CommonUtil.isEmpty(temp)){
                temp = temp+","+codeTem;
            }else{
                temp = codeTem;
            }
        }
        List<RepFileDto> repList = new ArrayList<>();
        if(!CommonUtil.isEmpty(temp)){
            for(String id : temp.split(",")){
                RepFileDto dto = repService.getRepFileByRepFileId(appKey,id);
                repList.add(dto);
            }
        }
        if(!CommonUtil.isEmpty(repList)){
            for(RepFileDto repFileDto : repList){
                repFileDto.setParentId(parentId);
                repService.updateRepFileById(appKey,repFileDto);
            }
        }

       /* String hql = " update RepFile p set p.parentId='"+parentId+"' where p.id in("
                + temp.toString().substring(1) + ") ";
        sessionFacade.executeHql(hql);*/
    }

    @Override
    public void batchDel(List<ProjLibDocumentVo> docVos) {
        for (ProjLibDocumentVo doc : docVos) {
            String bizId = doc.getBizId();
            String bizversion = null;
            if (StringUtil.isNotEmpty(doc.getVersion())) {
                bizversion = doc.getVersion().split("\\.")[0] + ".";
            }

            List<RepFileDto> repFileList = repService.getRepFileByBizIdAndbizversion(appKey,bizId, bizversion);
            for (RepFileDto p : repFileList) {
                List<ProjLibFile> projLibFile = getProjLibFile(p.getId());
                if (projLibFile.size() > 0 && "1".equals(p.getAvaliable())) {
                    String procInstId = projLibFile.get(0).getFeedbackProcInstId();
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(p.getId(),
                            procInstId, "delete");
                }
                if ("A.".equals(bizversion)) {
                    sessionFacade.deleteAllEntitie(projLibFile);
                }
            }

            repService.deleteRepFileByRepFile(appKey,repFileList);
            List<RepFileDto> list = repService.getRepFileByBizId(appKey,bizId);
            // 删除文档时，删除其和计划或者流程任务的关系
            deleteRelationToPlanOrFlow(repFileList);
            // 2.将数据设置为启用状态
            if (list.size() > 0) {
                RepFileDto p = list.get(0);
                p.setAvaliable("1");
                repService.updateRepFileById(appKey,p);
            }
        }
    }

    // 项目库文档删除时，检查该文档是否作为计划或流程任务的输入，如果是，则同步删掉其对应的输入关系
    private void deleteRelationToPlanOrFlow(List<RepFileDto> repFileList) {
        if (!CommonUtil.isEmpty(repFileList)) {
            for (RepFileDto f : repFileList) {
                sessionFacade.executeHql(
                        "update Inputs set docId = '', docName = '', originType='' where  docId = ? ",
                        f.getBizId());
            };
            LOG.info("delete repfile's relation to plan or flow success.");;
        }
    }


    // 文件类型，0：目录，1：文件
    // 新建根目录
    @Override
    public String createFile(String projectId, String parentId, String fileName, int type,String userId) {
        String libId = projRoleService.getLibIdByProjectId(projectId);
        RepFileDto repFile = new RepFileDto();
        repFile.setCreateBy(userId);
        repFile.setFileName(fileName);
        repFile.setFileType(type);
        repFile.setLibId(libId);
        repFile.setFileTypeId("");
        //TODO,不确定SecurityLevel到底是什么，先给个默认值
        repFile.setSecurityLevel(Short.valueOf("1"));
        if (StringUtils.isNotBlank(parentId)) {
            repFile.setParentId(parentId);
        }
        else {
            repFile.setParentId(libId);
        }
        FeignJson repFileJson = repService.addRepFile(appKey,repFile,userId);
        String fileId = String.valueOf(repFileJson.getObj());
        return fileId;
    }

    @Override
    public String getDocRelation(String quoteId) {
        List<ProjDocRelation> list = sessionFacade.findHql("from ProjDocRelation t where t.quoteId=?", quoteId);
        String json = "";
        if(list.size()>0) {
            json = JsonUtil.getListJsonWithoutQuote(list);
        }
        return json;
    }

    @Override
    public List<ProjDocRelation> getDocRelationList(String quoteId) {
        return this.findHql("from ProjDocRelation t where t.quoteId=?", quoteId);
    }

    @Override
    public ProjLibDocumentVo getProjDocmentVoById(String id) {
        RepFileDto repFileDto = repService.getRepFileByRepFileId(appKey,id);
        return convertToVo(repFileDto);
    }


    @Override
    public ProjLibDocumentVo convertToVo(RepFileDto repFile) {
        ProjLibDocumentVo vo = new ProjLibDocumentVo();
        vo.setId(repFile.getId());
        vo.setDocNumber(repFile.getFileNumber());
        List<RepFileDto> sameBizIdRepList = repService.getRepFileByBizId(appKey,repFile.getBizId());
        if (sameBizIdRepList.size() > 1) {
            if(!CommonUtil.isEmpty(sameBizIdRepList.get(0).getCreateBy())){
                vo.setCreateName(ProcessUtil.getFormatUser(userService.getUserByUserId(sameBizIdRepList.get(0).getCreateBy())));
            }

            vo.setCreateTime(sameBizIdRepList.get(0).getCreateTime());
            if(!CommonUtil.isEmpty(repFile.getUpdateBy())){
                vo.setUpdateName(ProcessUtil.getFormatUser(userService.getUserByUserId(repFile.getUpdateBy())));
            }

            vo.setUpdateTime(repFile.getUpdateTime());
        }
        else {
            if(!CommonUtil.isEmpty(repFile.getCreateBy())){
                vo.setCreateName( ProcessUtil.getFormatUser(userService.getUserByUserId(repFile.getCreateBy())));
            }

            vo.setCreateTime(repFile.getCreateTime());
            if(!CommonUtil.isEmpty(repFile.getUpdateBy())){
                vo.setUpdateName(ProcessUtil.getFormatUser(userService.getUserByUserId(repFile.getUpdateBy())));
            }

            vo.setUpdateTime(repFile.getUpdateTime());
        }
        /*vo.setCreateName(UserUtil.getFormatUserNameId(repFile.getFirstBy()));
        vo.setCreateTime(repFile.getFirstTime());
        vo.setUpdateName(UserUtil.getFormatUserNameId(repFile.getCreateBy()));
        vo.setUpdateTime(repFile.getCreateTime());*/
        vo.setDocName(repFile.getFileName());
        String path = getDocNamePath(repFile.getId());
        String[] temp = path.split("/");
        String realPath = "";
        for (int i = 1; i < temp.length - 1; i++ ) {
            realPath += "/" + temp[i];
        }
        vo.setPath(realPath);
        vo.setVersion(repFile.getBizVersion());
        vo.setStatus(repFile.getBizCurrent());
        if(!CommonUtil.isEmpty(repFile.getSecurityLevel())){
            vo.setSecurityLevel(repFile.getSecurityLevel().toString());
            vo.setSecurityLevelId(repFile.getSecurityLevel().toString());
        }
        vo.setBizId(repFile.getBizId());
        vo.setRemark(repFile.getFileRemark());
        vo.setParentId(repFile.getParentId());
        vo.setType(repFile.getFileType());
        vo.setOrderNum(repFile.getOrderNum());
        vo.setOperStatus(repFile.getOperStatus());
        vo.setFileTypeId(repFile.getFileTypeId());
        return vo;
    }


    @Override
    public String getDocNamePath(String id) {
        docPath = "";
        culPath(id);
        String[] temp = docPath.split("/");
        String realPath = "";
        for (int i = temp.length - 1; i > 0; i-- ) {
            realPath += "/" + temp[i];
        }
        return realPath;
    }

    @Override
    public boolean validateReptDocNum(String docNumber) {
        if (docNumber != null && !docNumber.equals("")) {

            List<RepFileDto> repFileList = repService.getRepFileByFileNumber(appKey,docNumber);
            if(!CommonUtil.isEmpty(repFileList)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addRepFileAttachment(RepFileAttachmentDto attachment) {
        if(CommonUtil.isEmpty(attachment.getId())){
            repService.addRepFileAttachment(attachment);
        }else{
            repService.updateRepFileAttachment(appKey,attachment);
        }
    }

    @Override
    public void updateFileAttachment(RepFileAttachmentDto attachment) {
        repService.updateRepFileAttachment(appKey,attachment);
    }

    @Override
    public String createFile(ProjLibDocumentVo vo,String userId) {
        String libId = projRoleService.getLibIdByProjectId(vo.getProjectId());
        RepFileDto repFile = convertToRepFile(vo);
        repFile.setFileTypeId(vo.getFileTypeId());
        repFile.setLibId(libId);
        if (vo.getType().equals(1)) {
            repFile.setFirstBy(ResourceUtil.getCurrentUser().getId());
            repFile.setFirstTime(new Date());
            repFile.setFirstName(ResourceUtil.getCurrentUser().getUserName());
            repFile.setFirstFullName(ResourceUtil.getCurrentUser().getRealName());
        }
        if (StringUtils.isNotBlank(vo.getParentId())) {
            repFile.setParentId(vo.getParentId());
        }
        else {
            repFile.setParentId(libId);
        }

        FeignJson res = repService.addRepFile(appKey,repFile,userId);
        return String.valueOf(res.getObj());
    }

    @Override
    public String createFile(ProjLibDocumentVo vo, TSUserDto currentUser) {
        String libId = projRoleService.getLibIdByProjectId(vo.getProjectId());
        RepFileDto repFile = convertToRepFile(vo);
        repFile.setFileTypeId(vo.getFileTypeId());
        repFile.setLibId(libId);
        if (vo.getType().equals(1)) {
            repFile.setFirstBy(currentUser.getId());
            repFile.setFirstTime(new Date());
            repFile.setFirstName(currentUser.getUserName());
            repFile.setFirstFullName(currentUser.getRealName());
            repFile.setCreateBy(currentUser.getId());
        }
        if (StringUtils.isNotBlank(vo.getParentId())) {
            repFile.setParentId(vo.getParentId());
        }
        else {
            repFile.setParentId(libId);
        }

        FeignJson res = repService.addRepFile(appKey,repFile,currentUser.getId());
        return String.valueOf(res.getObj());
    }


    private RepFileDto convertToRepFile(ProjLibDocumentVo vo) {
        RepFileDto repFile = new RepFileDto();
        repFile.setId(vo.getId());
        repFile.setFileNumber(vo.getDocNumber());
        repFile.setFileName(vo.getDocName());
        String securityLevel = StringUtils.defaultIfBlank(vo.getSecurityLevel(), "1");
        repFile.setSecurityLevel(Short.parseShort(securityLevel));
        repFile.setBizId(vo.getBizId());
        repFile.setParentId(vo.getParentId());
        repFile.setFileRemark(vo.getRemark());
        repFile.setFileType(vo.getType());
        repFile.setOrderNum(vo.getOrderNum());
        repFile.setFileTypeId(vo.getFileTypeId());
        return repFile;
    }


    @Override
    public String getFoldIdByProjectId(String projectId) {
        ProjTeamLink projTeamLink = new ProjTeamLink();
        List<ProjTeamLink> projTeamLinkList = this.findHql(
                "from ProjTeamLink t where  t.projectId=? ", projectId);
        if (projTeamLinkList != null && projTeamLinkList.size() > 0) {
            projTeamLink = projTeamLinkList.get(0);
        }

        String libId = projTeamLink.getLibId();
        if(CommonUtil.isEmpty(libId)){
            libId = "";
        }

        List<RepFileDto> temp =repService.getRepFileByLibIdAndParentId(appKey,libId,"");
        if (temp != null && temp.size() > 0) {
            return temp.get(0).getId();
        }
        else {
            return null;
        }
    }


    private void culPath(String id) {
        RepFileDto r = repService.getRepFileByRepFileId(appKey,id);
        if (!r.getLibId().equals(r.getParentId())) {
            docPath += "/" + r.getFileName();
            culPath(r.getParentId());
        }
        else {
            docPath += "/" + r.getFileName();
        }
    }

    @Override
    public List<ProjLibDocumentVo> getDocFoldDataPageForWeb(String projectId, String foldId,
                                                            Short securityLevel, boolean isPage,
                                                            int page, int rows) {
        String libId = projRoleService.getLibIdByProjectId(projectId);
        //FD接口调用
        List<RepFileDto> repFileList = repService.getRepFileByLibIdAndFoldIdPageForWeb(appKey,libId, foldId,
                securityLevel, isPage, page, rows);
        return convertToVos(repFileList);

    }

    private List<ProjLibDocumentVo> convertToVos(List<RepFileDto> repFileList) {
        List<ProjLibDocumentVo> vos = new ArrayList<ProjLibDocumentVo>();
        for (RepFileDto repFile : repFileList) {
            ProjLibDocumentVo vo = convertToVo(repFile);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public String createFile1(ProjLibDocumentVo vo, String users, String securityLevel) {
        String libId = projRoleService.getLibIdByProjectId(vo.getProjectId());
        RepFileDto repFile = convertToRepFile(vo);
        repFile.setFileTypeId(vo.getFileTypeId());
        repFile.setLibId(libId);
        repFile.setSecurityLevel(Short.valueOf(securityLevel));
        if (vo.getType().equals(1)) {
            String[] suer = users.split(",");
            repFile.setFirstBy(suer[2]);
            repFile.setFirstTime(new Date());
            repFile.setFirstName(suer[1]);
            repFile.setFirstFullName(suer[0]);
        }
        if (StringUtils.isNotBlank(vo.getParentId())) {
            repFile.setParentId(vo.getParentId());
        }
        else {
            List<RepFileDto> files = getFolderTree(vo.getProjectId(), "0", "");
            for (RepFileDto p : files) {
                if (p.getParentId().equals(libId)) {
                    repFile.setParentId(p.getId());
                }
            }

        }

        //FD接口调用
        FeignJson j = repService.addRepFileByUsers(appKey,repFile, users);
        return j.getObj().toString();
    }

    /**
     * Description: <br>获取项目库下目录
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param projectId
     * @return
     * @see
     */
    @Override
    public List<RepFileDto> getFolderTree(String projectId, String havePower, String userId) {
        List<RepFileDto> files = new ArrayList<RepFileDto>();
        String libId = projTemplateLibService.getLibIdByTemplateId(projectId);
        if ("1".equals(havePower)) {
            //FD接口调用
            files = repService.getRepFileByLibIdAndType(appKey,libId, 0); // getImmediateChildrenDirs
            List<RepFileDto> list = new ArrayList<RepFileDto>();
            for (RepFileDto r : files) {
                Boolean isCreate = getPower(r.getId(), projectId, userId);
                if (isCreate) {
                    r.setOperStatus("1");
                    list.add(r);
                }
            }
            List<RepFileDto> listRepFile = new ArrayList<RepFileDto>();
            listRepFile.addAll(list);
            processNodes2(list, libId, listRepFile);
            files.clear();
            files.addAll(listRepFile);
        }
        else {
            //FD接口调用
            files = repService.getRepFileByLibIdAndType(appKey,libId, 0); // getImmediateChildrenDirs
        }
        return files;
    }

    @Override
    public List<RepFileDto> getFolderTreeForProjLib(String projectId, String havePower, String userId) {
        List<RepFileDto> files = new ArrayList<RepFileDto>();
        String libId = projRoleService.getLibIdByProjectId(projectId);
        if ("1".equals(havePower)) {
            //FD接口调用
            files = repService.getRepFileByLibIdAndType(appKey,libId, 0); // getImmediateChildrenDirs
            List<RepFileDto> list = new ArrayList<RepFileDto>();
            for (RepFileDto r : files) {
                Boolean isCreate = getPower(r.getId(), projectId, userId);
                if (isCreate) {
                    r.setOperStatus("1");
                    list.add(r);
                }
            }
            List<RepFileDto> listRepFile = new ArrayList<RepFileDto>();
            listRepFile.addAll(list);
            processNodes2(list, libId, listRepFile);
            files.clear();
            files.addAll(listRepFile);
        }
        else {
            //FD接口调用
            files = repService.getRepFileByLibIdAndType(appKey,libId, 0); // getImmediateChildrenDirs
        }
        return files;
    }

    /**
     * 返回权限
     *
     * @return
     * @see
     */
    @SuppressWarnings("unused")
    @Override
    public Boolean getPower(String folderId, String projectId, String userId) {
        List<String> codeList = ProjLibAuthManager.getAllAuthActionCode();
        Boolean a = false;
        if (!StringUtils.isEmpty(userId)) {
            a = true;
        }
        RepFile r = getEntity(RepFile.class, folderId);
        String createRep = r.getCreateBy();
        Boolean havePower = false;
        Boolean isCreate = false;
        Boolean create = false;
        String categoryFileAuths = projLibAuthService.getCategoryFileAuths(folderId, userId);
        if (StringUtil.isNotEmpty(categoryFileAuths)) {
            if (a) {
                if (categoryFileAuths.contains("create") && categoryFileAuths.contains("upload")) {
                    create = true;
                }
            }
            else {
                if (categoryFileAuths.contains("create")) {
                    create = true;
                }
            }
        }
        else {
            create = false;
        }

        if (create == true) {
            return true;
        }
        return false;
    }

    /**
     * 循环处理树节点
     * @param all
     * @param libId
     * @param listRepFile
     */
    private void processNodes2(List<RepFileDto> all, String libId, List<RepFileDto> listRepFile) {
        if (!CommonUtil.isEmpty(all)) {
            for (RepFileDto node : all) {
                String parentId = node.getParentId();
                processNodes3(listRepFile, parentId, libId);
            }
        }
    }

    /**
     *  循环处理树节点
     * @param listRepFile
     * @param parentId
     * @param libId
     */
    private void processNodes3(List<RepFileDto> listRepFile, String parentId, String libId) {
        //获取文档时经过接口调用同一对象的内存对象发生变化contains不适用，需要用id去判断
        List<String> ids = listRepFile.stream().map(RepFileDto::getId).collect(Collectors.toList());
        if (!parentId.equals(libId)) {
            RepFileDto r = repService.getRepFileByRepFileId(appKey,parentId);
            if (!ids.contains(r.getId())) {
                r.setOperStatus("noPower");
                listRepFile.add(r);
            }
            parentId = r.getParentId();
            processNodes3(listRepFile, parentId, libId);
        }
    }

    @Override
    public List<ProjLibDocumentVo> getRepList(String fileId, String folderId, String projectId, String userId) {
        List<RepFileDto> repList = new ArrayList<RepFileDto>();
        RepFileDto rep = repService.getRepFileByRepFileId(appKey,fileId);
        repList.add(rep);
        List<ProjLibDocumentVo> list = convertToVosByPower(repList, projectId, folderId, userId);
        return list;
    }

    @Override
    public boolean isHidProjLibOperForDir(String projectId) {
        Project p = (Project)sessionFacade.getEntity(Project.class,projectId);
        if (null == p) {
            return true;
        }
        // String isReject = p.getIsRefuse();
        // 项目暂停关闭文件夹树不可以修改
        if (p.getBizCurrent().equalsIgnoreCase(ProjectConstants.PAUSED)
                || p.getBizCurrent().equalsIgnoreCase(ProjectConstants.CLOSED)) {
            return true;

        }

        return false;
    }

    @Override
    public String getProjectIdByFileId(String fileId) {
        RepFileDto bo = repService.getRepFileByRepFileId(appKey, fileId);
        String libId = bo.getLibId();
        return projRoleService.getProjectIdByLibId(libId);
    }

    @Override
    public boolean isRootFolder(String fileId) {
        RepFileDto bo = repService.getRepFileByRepFileId(appKey, fileId);
        if (bo.getParentId().equals(bo.getLibId())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delDocRelation(String quoteId, String docId) {
        boolean sucess = false;
        String hql = " delete ProjDocRelation p where p.quoteId= '" + quoteId
                + "' and p.docId = '" + docId + "' ";
        sessionFacade.executeHql(hql);
        sucess = true;
        return sucess;
    }

    @Override
    public boolean addDocRelation(String quoteId, String docId) {
        boolean sucess = false;
        ProjDocRelation pdr = new ProjDocRelation();
        pdr.setQuoteId(quoteId);
        pdr.setDocId(docId);
        boServiceForProjLibFile.initBusinessObject(pdr);
        this.save(pdr);
        sucess = true;
        return sucess;
    }

    @Override
    public boolean isHidProjLibOper(String projectId) {
        Project p = (Project)sessionFacade.getEntity(Project.class,projectId);
        if (null == p) {
            return true;
        }
        // String isReject = p.getIsRefuse();
        // 项目暂停关闭拟制中文档不可以修改
        if (p.getBizCurrent().equalsIgnoreCase(ProjectConstants.PAUSED)
                || p.getBizCurrent().equalsIgnoreCase(ProjectConstants.CLOSED)
//            || p.getBizCurrent().equalsIgnoreCase(ProjectConstants.EDITING)  //任务3648，与团队计划等保持一致，拟制中的项目也有操作按钮 --zhousuxia 2019/1/4
                || "1".equals(p.getStatus())) {
            return true;

        }

        // if (StringUtils.equals(ConfigTypeConstants.APPROVING, p.getStatus())
        // && !StringUtils.equals(ConfigTypeConstants.REFUSED, isReject) &&
        // p.getBizCurrent().equalsIgnoreCase(ConfigTypeConstants.EDITING)) {
        // return true;
        //
        // }
        return false;
    }

    @Override
    public boolean delFileById(String fileId) {
        boolean sucess = false;
        RepFileDto bo = repService.getRepFileByRepFileId(appKey, fileId);
        //List<RepFileDto> children = repService.getRepFileByLibIdAndParentId(appKey, bo.getLibId(), fileId);
        //文档的集合
        List<RepFileDto> childrens = new ArrayList<RepFileDto>();
        //目录的集合
        List<RepFileDto> floders = new ArrayList<>();
        List<RepFileDto> resList = new ArrayList<RepFileDto>();
        getAllChildrenList( bo.getLibId(), fileId, floders, childrens);
        if(!CommonUtil.isEmpty(childrens)){
            for(RepFileDto dto : childrens){
                if("1".equals(dto.getAvaliable())){
                    resList.add(dto);
                }
            }
        }
        if (CommonUtil.isEmpty(resList)) {
            floders.add(bo);
            repService.deleteRepFileByRepFile(appKey, floders);
            sucess = true;
        }
        return sucess;
    }

    @Override
    public FeignJson beforeDelFolder(String folderId) {
        FeignJson j = new FeignJson();
        String message = "";
        boolean isRootFolder = isRootFolder(folderId);
        if (isRootFolder) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.rootCannotDelete");
            j.setSuccess(false);
            j.setMsg(message);
            return j;
        }
        RepFileDto bo = repService.getRepFileByRepFileId(appKey, folderId);
        //List<RepFileDto> children = repService.getRepFileByLibIdAndParentId(appKey, bo.getLibId(), fileId);
        //文档的集合
        List<RepFileDto> childrens = new ArrayList<RepFileDto>();
        //目录的集合
        List<RepFileDto> floders = new ArrayList<>();
        List<RepFileDto> resList = new ArrayList<RepFileDto>();
        getAllChildrenList( bo.getLibId(), folderId, floders, childrens);
        if(!CommonUtil.isEmpty(childrens)){
            for(RepFileDto dto : childrens){
                if("1".equals(dto.getAvaliable())){
                    resList.add(dto);
                }
            }
        }
        if (!CommonUtil.isEmpty(resList)) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.folderCannotDelete");
            j.setSuccess(false);
            j.setMsg(message);
            return j;
        }
        return j;
    }

    public void getAllChildrenList(String libId, String fileId, List<RepFileDto> floders, List<RepFileDto> childrens) {
        List<RepFileDto> children = repService.getRepFileByLibIdAndParentId(appKey, libId, fileId);
        if (!CommonUtil.isEmpty(children)) {
            for (RepFileDto repFileDto : children) {
                //获取文件
                if (repFileDto.getFileType() == 1) {
                    childrens.add(repFileDto);
                } else {
                    floders.add(repFileDto);
                    getAllChildrenList(repFileDto.getLibId(), repFileDto.getId(), floders, childrens);
                }
            }
        }
    }

    @Override
    public List<ProjectLibAuthTemplateLink> getProjectLibAuthTemplateLinkId(String projectId) {
        return findByQueryString("from ProjectLibAuthTemplateLink t where 1=1 and t.projectId = '"
                + projectId + "'");
    }

    private List<ProjLibDocumentVo> convertToVosByPower(List<RepFileDto> repFileList,
                                                        String projectId, String folderId, String userId) {
        List<ProjLibDocumentVo> vos = new ArrayList<ProjLibDocumentVo>();
        Map<String, String> folderAuthMap = projLibAuthService.getFolderAuthMap(folderId, userId);
        Map<String, String> fileLifeCycleAuthMap = projLibAuthService.getRepFileLifeCycleAuths();
        boolean isModify = true;
        // isModify = projectService.isModifyForPlan(projectId);
        for (RepFileDto repFile : repFileList) {
            ProjLibDocumentVo vo = convertToVoByPower(repFile, projectId, folderAuthMap,
                    fileLifeCycleAuthMap, isModify, userId);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public boolean checkCategoryNameExist(RepFileDto category) {


        List<RepFileDto> list = repService.getRepFileListByrepFileIdAndparentIdAndfileName(appKey,category.getId(),category.getParentId(),category.getFileName());
       /*
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("from RepFile t where t.fileType = 0 and t.avaliable ='1' ");
        if (StringUtils.isNotEmpty(category.getId())) {
            hqlBuffer.append(" and t.id <> '" + category.getId() + "'");
        }
        if (StringUtils.isNotEmpty(category.getParentId())) {
            hqlBuffer.append(" and t.parentId = '" + category.getParentId() + "'");
        }

        if (StringUtils.isNotEmpty(category.getFileName())) {
            hqlBuffer.append(" and t.fileName = '" + category.getFileName() + "'");
        }

     *//*   List<RepFileDto> list = repService.getRepFileListByrepFileIdAndparentIdAndfileName(appKey,category.getId(),category.getParentId(),category.getFileName());*//*
        List<ProjectLibTemplateFileCategory> list = findByQueryString(hqlBuffer.toString());*/
        if (CommonUtil.isEmpty(list)) {
            return false;
        }
        return true;
    }

    private ProjLibDocumentVo convertToVoByPower(RepFileDto repFile, String projectId,
                                                 Map<String, String> folderAuthMap,
                                                 Map<String, String> fileLifeCycleAuthMap,
                                                 Boolean isModify, String userId) {
        ProjLibDocumentVo vo = new ProjLibDocumentVo();
        vo.setId(repFile.getId());
        vo.setDocTypeId(repFile.getFileTypeId());
        vo.setDocNumber(repFile.getFileNumber());
        vo.setOperStatus(repFile.getOperStatus());
        List<RepFileDto> sameBizIdRepList = repService.getRepFileByBizId(appKey,repFile.getBizId());
        if (sameBizIdRepList.size() > 1) {
            if (StringUtils.isNotBlank(sameBizIdRepList.get(0).getCreateBy())) {
                vo.setCreateName(ProcessUtil.getFormatUser(userService.getUserByUserId(sameBizIdRepList.get(0).getCreateBy())));
            } else {
                vo.setCreateName("");
            }
            vo.setCreateTime(sameBizIdRepList.get(0).getCreateTime());
            if (StringUtils.isNotBlank(repFile.getUpdateBy())) {
                vo.setUpdateName(ProcessUtil.getFormatUser(userService.getUserByUserId(repFile.getUpdateBy())));
            } else {
                vo.setUpdateName("");
            }
            vo.setUpdateTime(repFile.getUpdateTime());
        }
        else {
            if (StringUtils.isNotBlank(repFile.getCreateBy())) {
                vo.setCreateName(ProcessUtil.getFormatUser(userService.getUserByUserId(repFile.getCreateBy())));
            } else {
                vo.setCreateName("");
            }
            vo.setCreateTime(repFile.getCreateTime());
            if (StringUtils.isNotBlank(repFile.getUpdateBy())) {
                vo.setUpdateName(ProcessUtil.getFormatUser(userService.getUserByUserId(repFile.getUpdateBy())));
            } else {
                vo.setUpdateName("");
            }
            vo.setUpdateTime(repFile.getUpdateTime());
        }
/*        vo.setCreateName(ProcessUtil.getFormatUser(userService.getUserByUserId(repFile.getFirstBy())));
        vo.setCreateTime(repFile.getFirstTime());
        vo.setUpdateName(ProcessUtil.getFormatUser(userService.getUserByUserId(repFile.getCreateBy())));
        vo.setUpdateTime(repFile.getCreateTime());*/
        vo.setDocName(repFile.getFileName());
        String path = getDocNamePath(repFile.getId());
        String[] temp = path.split("/");
        String realPath = "";
        for (int i = 1; i < temp.length - 1; i++ ) {
            realPath += "/" + temp[i];
        }
        vo.setPath(realPath);
        vo.setVersion(repFile.getBizVersion());
        vo.setStatus(repFile.getBizCurrent());
        vo.setSecurityLevel(repFile.getSecurityLevel().toString());
        vo.setSecurityLevelId(repFile.getSecurityLevel().toString());
        vo.setBizId(repFile.getBizId());
        vo.setRemark(repFile.getFileRemark());
        vo.setParentId(repFile.getParentId());
        vo.setType(repFile.getFileType());
        vo.setOrderNum(repFile.getOrderNum());
        vo.setOperStatus(repFile.getOperStatus());
        String docBizcurrentAuths = fileLifeCycleAuthMap.get(repFile.getBizCurrent());
        String[] docBizcurrentAuthArr = docBizcurrentAuths.split(",");
        StringBuffer authBuffer = new StringBuffer("");
        Map<String, String> authMap = new HashMap<String, String>();
        if (CommonUtil.isEmpty(folderAuthMap)) {
            if (userId.equals(repFile.getCreateBy())) {
                ProjectLibraryAuthorityEnum[] authArray = ProjectLibraryAuthorityEnum.values();
                for (ProjectLibraryAuthorityEnum auth : authArray) {
                    authMap.put(auth.getActionCode(), auth.getActionCode());
                }
            }
            else {
                ProjectLibraryAuthorityEnum[] authArray = ProjectLibraryAuthorityEnum.values();
                for (ProjectLibraryAuthorityEnum auth : authArray) {
                    if ("list".equals(auth.getActionCode())
                            || "detail".equals(auth.getActionCode())
                            || "create".equals(auth.getActionCode())
                            || "download".equals(auth.getActionCode())
                            || "upload".equals(auth.getActionCode())
                            || "history".equals(auth.getActionCode())) {
                        authMap.put(auth.getActionCode(), auth.getActionCode());
                    }
                }
            }
            for (String str : docBizcurrentAuthArr) {
                if (StringUtils.isNotEmpty(authMap.get(str))) {
                    if (StringUtils.isNotEmpty(authBuffer.toString())) {
                        authBuffer.append(",").append(str);
                    }
                    else {
                        authBuffer.append(str);
                    }
                }
            }
        }
        else {
            for (String str : docBizcurrentAuthArr) {
                if (StringUtils.isNotEmpty(folderAuthMap.get(str))) {
                    if (StringUtils.isNotEmpty(authBuffer.toString())) {
                        authBuffer.append(",").append(str);
                    }
                    else {
                        authBuffer.append(str);
                    }
                }
            }
        }
        String categoryFileAuths = authBuffer.toString();
        // Boolean havePower = false;
        // Boolean isCreate = false;
        Boolean detail = true;
        Boolean remove = false;
        Boolean update = false;
        Boolean download = true;
        Boolean upload = true;
        Boolean history = true;
        Boolean revise = false;
        Boolean rollback = false;
        Boolean approve = false;
        if (StringUtil.isNotEmpty(categoryFileAuths)) {
            if (categoryFileAuths.contains("detail")) {
                detail = true;
            }
            else {
                detail = false;
            }
            if (categoryFileAuths.contains("remove")) {
                remove = true;
            }
            else {
                remove = false;
            }
            if (categoryFileAuths.contains("update")) {
                update = true;
            }
            else {
                update = false;
            }
            if (categoryFileAuths.contains("download")) {
                download = true;
            }
            else {
                download = false;
            }
            if (categoryFileAuths.contains("upload")) {
                upload = true;
            }
            else {
                upload = false;
            }
            if (categoryFileAuths.contains("history")) {
                history = true;
            }
            else {
                history = false;
            }
            if (categoryFileAuths.contains("revise")) {
                revise = true;
            }
            else {
                revise = false;
            }
            if (categoryFileAuths.contains("rollback")) {
                rollback = true;
            }
            else {
                rollback = false;
            }
            if (categoryFileAuths.contains("approve")) {
                approve = true;
            }
            else {
                approve = false;
            }
        }
        else {
            detail = true;
            remove = false;
            update = false;
            download = true;
            upload = true;
            history = true;
            revise = false;
            rollback = false;
            approve = false;
        }
        if (!isModify) {
            detail = false;
            remove = false;
            update = false;
            download = false;
            upload = false;
            history = false;
            revise = false;
            rollback = false;
            approve = false;
        }
        vo.setDetail(detail);
        vo.setRemove(remove);
        vo.setUpdate(update);
        vo.setDownload(download);
        vo.setUpload(upload);
        vo.setHistory(history);
        vo.setRevise(revise);
        vo.setRollback(rollback);
        vo.setApprove(approve);
        return vo;
    }

    @Override
    public List<ProjLibRoleFileAuthVo> getProjLibRoleFileAuths(String fileId) {
        List<ProjLibRoleFileAuthVo> roleAuthList = new ArrayList<ProjLibRoleFileAuthVo>();
        // 获取有效的角色权限
        List<RepRoleFileAuthDto> roleFileAuthsList = repService.getRepRoleFileAuthList(fileId);

        // 将其转为ProjLibRoleFileAuthVo
        for (RepRoleFileAuthDto roleFileAuth : roleFileAuthsList) {
            ProjLibRoleFileAuthVo vo = new ProjLibRoleFileAuthVo();
            vo.setId(roleFileAuth.getId());
            vo.setFileId(roleFileAuth.getFileId());
            vo.setRoleId(roleFileAuth.getRoleId());
            long permissionCode = 0;
            if (StringUtils.isNotEmpty(roleFileAuth.getPermissionCode())) {
                permissionCode = Long.valueOf(roleFileAuth.getPermissionCode());
            }
            Map<String, Boolean> authMap = new HashMap<String, Boolean>();
            Map<ProjectLibraryAuthorityEnum, Boolean> map = ProjLibAuthManager.getActionMapByPermissionCode(permissionCode);
            for (ProjectLibraryAuthorityEnum authEnum : map.keySet()) {
                authMap.put(authEnum.getActionCode(), map.get(authEnum));
            }
            vo.setAuthMap(authMap);
            roleAuthList.add(vo);
        }
        return roleAuthList;
    }

    @Override
    public boolean checkRoleFileAuthExistChange(String fileId,
                                                List<RepFileAuthVo> repFileAuthVoList) {
        // 先获取表中fileId相关的角色权限数据
        List<RepRoleFileAuthDto> roleAuthlist = repService.getRepRoleFileAuthsByTSRoleAndFileId(appKey,fileId);
        // 既有数据的角色、权限编号Map
        Map<String, String> roleAuthCodeMap = new HashMap<String, String>();
        for (RepRoleFileAuthDto roleAuth : roleAuthlist) {
            roleAuthCodeMap.put(roleAuth.getRoleId(), roleAuth.getPermissionCode());
        }
        // 比较页面上设置的权限编码与既有数据的角色、权限编号Map的值
        List<String> authActionCodeList = ProjLibAuthManager.getAllAuthActionCode();
        for (RepFileAuthVo vo : repFileAuthVoList) {
            List<ProjectLibraryAuthorityEnum> actionList = new ArrayList<ProjectLibraryAuthorityEnum>();
            // 将权限编号转为十进制
            char[] charArr = vo.getCheckValue().toCharArray();
            for (int i = 0; i < charArr.length; i++ ) {
                if ("1".equals(String.valueOf(charArr[i]))) {
                    ProjectLibraryAuthorityEnum action = ProjectLibraryAuthorityEnum.valueOfActionCode(authActionCodeList.get(i));
                    if (action != null) {
                        actionList.add(action);
                    }
                }
            }
            String permissionCode = String.valueOf(ProjLibAuthManager.encodeAuthorityActions(actionList));
            if (!"0".equals(permissionCode)) {
                if (StringUtils.isEmpty(roleAuthCodeMap.get(vo.getRoleId()))
                        || !permissionCode.equals(roleAuthCodeMap.get(vo.getRoleId()))) {
                    return true;
                }
            }
            else {
                if (StringUtils.isNotEmpty(roleAuthCodeMap.get(vo.getRoleId()))
                        && !permissionCode.equals(roleAuthCodeMap.get(vo.getRoleId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void saveProjLibRoleFileAuth(String fileId, List<RepFileAuthVo> repFileAuthVoList, String userId) {
        // 删除fileId相关的角色权限
        repService.deleteRepRoleFileAuthsByFileId(appKey,fileId);
        //sessionFacade.executeHql("delete from RepRoleFileAuth t where t.fileId = '" + fileId + "'");
        List<RepRoleFileAuthDto> repRoleFileAuthList = new ArrayList<RepRoleFileAuthDto>();
        List<String> authActionCodeList = ProjLibAuthManager.getAllAuthActionCode();
        for (RepFileAuthVo vo : repFileAuthVoList) {
            List<ProjectLibraryAuthorityEnum> actionList = new ArrayList<ProjectLibraryAuthorityEnum>();
            // 将RepFileAuthVo转为ProjectLibRoleFileAuth对象
            RepRoleFileAuthDto roleFileAuth = new RepRoleFileAuthDto();
            //初始化生命周期
           /* roleFileAuth = repService.initRepFileAttachmentBusinessObject(appKey,userId,roleFileAuth);*/
            roleFileAuth.setFileId(fileId);
            roleFileAuth.setRoleId(vo.getRoleId());
            // 将权限编号转为十进制
            char[] charArr = vo.getCheckValue().toCharArray();
            for (int i = 0; i < charArr.length; i++ ) {
                if ("1".equals(String.valueOf(charArr[i]))) {
                    ProjectLibraryAuthorityEnum action = ProjectLibraryAuthorityEnum.valueOfActionCode(authActionCodeList.get(i));
                    if (action != null) {
                        actionList.add(action);
                    }
                }
            }
            String permissionCode = String.valueOf(ProjLibAuthManager.encodeAuthorityActions(actionList));
            roleFileAuth.setPermissionCode(permissionCode);
            repRoleFileAuthList.add(roleFileAuth);
        }
        if (!CommonUtil.isEmpty(repRoleFileAuthList)) {
            repService.batchSaveRepRoleFileAuthDto(repRoleFileAuthList);
        }
    }

    @Override
    public void attachmentBatchDel(String ids) {
        // 批量逻辑删除
        String[] str = ids.split(",");
        StringBuffer temp = new StringBuffer();
        for (String codeTem : str) {
            temp.append(",'" + codeTem + "'");
        }
        String hql = " update RepFileAttachment p set p.avaliable='0' where p.id in("
                + temp.toString().substring(1) + ") ";
        sessionFacade.executeHql(hql);
    }

    @Override
    public String updateFile(ProjLibDocumentVo vo, String userId) {
        TSUserDto curUser = userService.getUserByUserId(userId);
        String bizId = vo.getBizId();
        // 最新的文档
        RepFileDto repFileDB = repService.findBusinessObjectByBizId(appKey,bizId);
        short securityLevel = Short.parseShort(StringUtils.defaultIfBlank(vo.getSecurityLevel(),
                "1"));
        repFileDB.setCreateName(curUser.getRealName());
        repFileDB.setCreateBy(userId);
        repFileDB.setCreateTime(new Date());
        repFileDB.setFileName(vo.getDocName());
        repFileDB.setFileRemark(vo.getRemark());
        repFileDB.setSecurityLevel(securityLevel);
        repFileDB.setFileTypeId(vo.getFileTypeId());
        repService.updateRepFileById(appKey,repFileDB);
        return repFileDB.getId();
    }


    @Override
    public String updateFile(String method, ProjLibDocumentVo document, String id, String message, String userId) {
        String fileId = null;
        if (StringUtils.equalsIgnoreCase(method, "update")) {
            // 升级小版本
            repService.reviseRepFile(appKey,id,LifeCycleConstant.ReviseModel.MINER);
            fileId = updateFile(document,userId);
            List<ProjLibFile> projFiles = getProjLibFile(document.getId());
            if (StringUtil.isNotEmpty(projFiles) && projFiles.size() > 0) {
                ProjLibFile projLibFile = projFiles.get(0);
                projLibFile.setOldTaskId(projLibFile.getProjectFileId());
                projLibFile.setProjectFileId(fileId);
                sessionFacade.saveOrUpdate(projLibFile);
            }
        }
        else if (StringUtils.equalsIgnoreCase(method, "revise")) {
            // 升级大版本
            repService.reviseRepFile(appKey,id,LifeCycleConstant.ReviseModel.MAJOR);
            fileId = updateFile(document,userId);
            message = "文档修订成功";
        }

        // 更新文档基本信息

        return fileId + ";" + message;
    }

    @Override
    public void updateVariablesAndTodoTask(String newFiledId, String procInstId, String docName) {
        if(!CommonUtil.isEmpty(procInstId)) {
            String taskId = "";
            FeignJson taskFeign = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(procInstId);
            if (taskFeign.isSuccess()) {
                taskId = taskFeign.getObj() == null ? "" : taskFeign.getObj().toString();
            }
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByProcInstId(procInstId);
            Map<String, Object> variables = new HashMap<String, Object>();
            if(StringUtils.isNotBlank(taskId)) {
                variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(taskId);
            }
            if(!CommonUtil.isEmpty(variables)) {
                // 设置viewUrl
                variables.put("viewUrl", "/ids-pm-web/projLibController.do?viewProjectDocDetail&dataHeight=580&dataWidth=870&id="
                        + newFiledId);
                // 设置oid
                variables.put("oid", BpmnConstants.OID_PROJECT + newFiledId);
                workFlowFacade.getWorkFlowCommonService().setRunVariables(procInstId, variables);
            }
            // 获取流程显示名称
            ProcessDefinition processDefinition = workFlowFacade.getWorkFlowCommonService().getProcessDefinitionByProcInstId(
                    procInstId, false);
            if(!CommonUtil.isEmpty(processDefinition)) {
                myStartedTask.setTaskNumber(newFiledId);
                myStartedTask.setObjectName(docName);
                myStartedTask.setTitle(ProcessUtil.getProcessTitle(docName,
                        BpmnConstants.BPMN_SUBMIT_DOCUMENT_DISPLAYNAME, procInstId));
                workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
            }
        }

    }
    
    @Override
    public List<ProjDocRelation> getAllDocRelationList() {
        return this.findHql("from ProjDocRelation");
    }

    @Override
    public void saveProcessProInstance(String proInstanceId, String id) {
        ProjLibFile projLibFile = new ProjLibFile();
        projLibFile.setFeedbackProcInstId(proInstanceId);
        projLibFile.setProjectFileId(id);
        projLibFile.setOldTaskId(id);
        saverProcessInstanceId(projLibFile);

    }

    @Override
    public boolean delDocRelation(String quoteId) {
        boolean sucess = false;
        List<ProjDocRelation> docRelationList = getDocRelationList(quoteId);
        this.deleteAllEntitie(docRelationList);
        sucess = true;
        return sucess;
    }

    @Override
    public void saverProcessInstanceId(ProjLibFile projLibFile) {
        boServiceForProjLibFile.initBusinessObject(projLibFile);
        saveOrUpdate(projLibFile);
    }

    @Override
    public FeignJson submitProcess(Map<String, String> params) {
        String id = params.get("id");
        String entityName = params.get("entityName");
        String businessType = params.get("businessType");
        String docName = params.get("docName");
        String userId = params.get("userId");
        TSUserDto curUSer = userService.getUserByUserId(userId);
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.sumbitSuccess");
        try {
            // 查找对象业务类型相关的流程信息
            ObjectBusinessBPMNLinkDto objectBusinessBPMNLinkInfo = null;
            if(!CommonUtil.isEmpty(businessType)) {
                String[] businessTypeArr = businessType.split(":");
                if(CommonUtil.isEmpty(businessTypeArr[1]) ||
                        RepFileTypeConfigConstants.REPFILETYPECONFIG_PARENTID.equals(businessTypeArr[1])) {
                    businessType = BpmnConstants.BUSINESSTYPE_DEFAULT;
                } else {
                    objectBusinessBPMNLinkInfo = workFlowFacade.getWorkFlowCommonService().getObjectBusinessBPMNLinkInfo(
                            entityName, businessType);
                    if (CommonUtil.isEmpty(objectBusinessBPMNLinkInfo)) {
                        businessType = BpmnConstants.BUSINESSTYPE_DEFAULT;
                    }
                }
            }
            List<ProjLibFile> projLibFile = getProjLibFile(id);
            if(!CommonUtil.isEmpty(projLibFile) &&
                    !CommonUtil.isEmpty(projLibFile.get(0).getFeedbackProcInstId())) {
                j.setSuccess(true);
                j.setObj(businessType);
            } else {
                objectBusinessBPMNLinkInfo = workFlowFacade.getWorkFlowCommonService().getObjectBusinessBPMNLinkInfo(
                        entityName, businessType);
                if (CommonUtil.isEmpty(objectBusinessBPMNLinkInfo.getBpmnName())) {
                    j.setSuccess(false);
                    message = "找不到对应的流程信息";
                    j.setObj(businessType);
                    j.setMsg(message);
                }
                else {
                    Map<String, Object> variables = new HashMap<String, Object>();
                    variables.put("user", curUSer.getUserName());
                    // 设置流程审批结果变量
                    variables.put("approve", "true");
                    // 设置流程审批意见变量
                    variables.put("desc", "");
                    // 设置viewUrl
                    variables.put("viewUrl", "/ids-pm-web/projLibController.do?viewProjectDocDetail&dataHeight=580&dataWidth=870&id="
                            + id);
                /*    variables.put("editUrl", "/ids-pm-web/projLibController.do?viewProjectDocDetail&dataHeight=580&dataWidth=870&oper=docRefused&id="
                            + id);*/
                    // 设置oid
                    variables.put("oid", BpmnConstants.OID_PROJECT + id);

                    //监听设置
                    variables.put("stopUrl","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/repFileTypeStopRestController/notify.do");
                    variables.put("refuseUrl","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/repFileTypeRefuseRestController/notify.do");
                    variables.put("repFileTypeEditUrl","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/repFileTypeEditRestController/notify.do");
                    variables.put("userId", userId);

                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("variables",variables);
                    map.put("objectBusinessBPMNLink",objectBusinessBPMNLinkInfo);
                    map.put("userId",userId);
                    String proInstanceId = "";
                    FeignJson fj = workFlowFacade.getWorkFlowCommonService().getProcInstIdBystartProcessAndCreateStartedTask(map , docName, id);
                    if (fj.isSuccess()) {
                        proInstanceId = fj.getObj() == null ? "" : fj.getObj().toString();
                    }
                    saveProcessProInstance(proInstanceId, id);
                    j.setSuccess(true);
                    j.setObj(businessType);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.sumbitFailure");
            j.setSuccess(false);
        }
        finally {
            j.setMsg(message);
            return j;
       }
    }

    @Override
    public void forward(String fileId,String userId) {
        //文档生命周期前进
        repService.forwardBusinessObjectByStep(appKey,fileId,"","1");
        RepFileDto repFileDto = repService.getRepFileByRepFileId(appKey,fileId);
        List<Plan> planList = new ArrayList<>();
        List<ProjDocRelation> projDocList = sessionFacade.findHql("from ProjDocRelation where docId = ?",repFileDto.getId());
        if("guidang".equals(repFileDto.getBizCurrent())){
            List<Inputs> inputsList = sessionFacade.findHql("from Inputs where docId=?",repFileDto.getBizId());
            if(!CommonUtil.isEmpty(inputsList)){
                for(Inputs inp : inputsList){
                    Plan plan = (Plan)sessionFacade.getEntity(Plan.class,inp.getUseObjectId());
                    planList.add(plan);
                }
            }
            if(!CommonUtil.isEmpty(projDocList)){
                for(ProjDocRelation projDoc : projDocList){
                    Plan plan = (Plan)sessionFacade.getEntity(Plan.class,projDoc.getQuoteId());
                    planList.add(plan);
                }
            }
            if(!CommonUtil.isEmpty(planList)){
                for(Plan pl : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pl.getBizCurrent())){
                        planService.updateBizCurrent(pl);
                    }
                }
            }
            sessionFacade.batchUpdate(planList);
        }

       /* //文档生命周期前期一步，更新计划进度
        if(!CommonUtil.isEmpty(projDocList)){
            for(ProjDocRelation projDoc : projDocList){
                DeliverablesInfo deliverablesInfo = (DeliverablesInfo) sessionFacade.getEntity(DeliverablesInfo.class,projDoc.getQuoteId());
                if (!CommonUtil.isEmpty(deliverablesInfo)) {
                    Plan plan = planService.getEntity(deliverablesInfo.getUseObjectId());
                    if (!CommonUtil.isEmpty(plan)) {
                        planService.updateProgress(plan);
                    }
                }
            }
        }*/

        if(!CommonUtil.isEmpty(planList)){
            for(Plan pla : planList){
                if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pla.getBizCurrent())){
                    if(CommonUtil.isEmpty(pla.getPlanReceivedProcInstId())){
                        planService.startPlanReceivedProcess(pla,userId);
                    }else if(!CommonUtil.isEmpty(pla.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(pla.getPlanReceivedCompleteTime())){
                        planService.startPlanReceivedProcess(pla,userId);
                    }

                }
            }
        }

    }

    @Override
    public void backward(String fileId) {
        repService.backwardBusinessObjectByStep(appKey,fileId,"reject","1");
       /* RepFileDto repFileDto = repService.getRepFileByRepFileId(appKey,fileId);
        List<ProjDocRelation> projDocList = sessionFacade.findHql("from ProjDocRelation where docId = ?",repFileDto.getId());
        //文档生命周期前期一步，更新计划进度
        if(!CommonUtil.isEmpty(projDocList)){
            for(ProjDocRelation projDoc : projDocList){
                DeliverablesInfo deliverablesInfo = (DeliverablesInfo) sessionFacade.getEntity(DeliverablesInfo.class,projDoc.getQuoteId());
                if (!CommonUtil.isEmpty(deliverablesInfo)) {
                    Plan plan = planService.getEntity(deliverablesInfo.getUseObjectId());
                    if (!CommonUtil.isEmpty(plan)) {
                        planService.updateProgress(plan);
                    }
                }
            }
        }*/
    }

    @Override
    public FeignJson getfolderIdByDeliverableId(String deliverableId, String projectId) {
        FeignJson fj = new FeignJson();
        String folderId = "";
        if (StringUtil.isNotEmpty(deliverableId)) {
            List<RepFileDto> allRepFile = repService.getRepFileAll(appKey);
            List<ProjDocRelation> projDocRelations = getDocRelationList(deliverableId);
            List<String> docIds = projDocRelations.stream().map(p -> p.getDocId()).collect(Collectors.toList());
            for (RepFileDto repFile : allRepFile) {
                if (docIds.indexOf(repFile.getId())>-1) {
                    ProjLibDocumentVo projLibDocumentVo = convertToVo(repFile);
                    folderId = projLibDocumentVo.getParentId();
                }
            }
            /*List<RepFileDto> repFileList = projLibService.getRepFileByQuoteId(deliverableId);
            if (!CommonUtil.isEmpty(repFileList)) {
                for (RepFileDto repFile : repFileList) {

                }
            }*/
        }
        else {
            folderId = getFoldIdByProjectId(projectId);
        }
        fj.setObj(folderId);
        return fj;
    }

    @Override
    public List<RepFileDto> getImmediateChildrenFolders(String fileId) {
        RepFileDto bo = repService.getRepFileByRepFileId(appKey, fileId);
        return repService.getRootDirsByParams(appKey,fileId,"0",bo.getLibId());
        /* this.findHql(
                "from RepFile t where t.parentId=? and t.libId=? and t.fileType=0  order by t.orderNum, t.fileName",
                fileId, bo.getLibId());*/
    }

    @Override
    public void changeEachOtherForVo(String srcId, String destId) {
        RepFileDto scr = repService.getRepFileByRepFileId(appKey, srcId);
        RepFileDto dest = repService.getRepFileByRepFileId(appKey, destId);
        changeEachOtherForRepFile(scr, dest);
    }


    private void changeEachOtherForRepFile(RepFileDto f1, RepFileDto f2){
        String parentId1 = f1.getParentId();
        long orderNum1 = f1.getOrderNum();
        String parentId2 = f2.getParentId();
        long orderNum2 = f2.getOrderNum();
        f1.setParentId(parentId2);
        f1.setOrderNum(orderNum2);
        repService.updateRepFileById(appKey,f1);
        f2.setParentId(parentId1);
        f2.setOrderNum(orderNum1);
        repService.updateRepFileById(appKey,f2);
    }

    @Override
    public void updateTreeOrderNum(String docId, String parentId) {

        long maxOrderNum = 0;
        maxOrderNum  = repService.getMaxOrderNumForRepFile();
        RepFileDto file = repService.getRepFileByRepFileId(appKey, docId);
        file.setParentId(parentId);
        file.setOrderNum(maxOrderNum);
        repService.updateRepFileById(appKey,file);
    }

    @Override
    public Map<String, List<RepRoleFileAuthDto>> getTemplateRoleAuths(String projectId, String templateId, List<TSRoleDto> roles) {
        Map<String, List<RepRoleFileAuthDto>> roleAuthMap = new HashMap<String, List<RepRoleFileAuthDto>>();
        // 拼接根节点名称
        Project project = getEntity(Project.class, projectId);
        String rootPath = project.getName() + "-" + project.getProjectNumber();
        // 获取模板相关的文件角色权限
        List<ProjectLibRoleFileAuth> templateRoleFileAuths = new ArrayList<>();
        List<ProjectLibRoleFileAuth> templateRoleFileList = findByQueryString("from ProjectLibRoleFileAuth t where t.templateId = '"+ templateId+"'");
        List<TSRoleDto> roleList = roleService.getCommonRole();
        if(!CommonUtil.isEmpty(templateRoleFileList)){
            if(!CommonUtil.isEmpty(roleList)){
                for(ProjectLibRoleFileAuth auth : templateRoleFileList){
                    for(TSRoleDto role : roleList){
                        if(auth.getRoleId().equals(role.getId()) && role.getStatus() == 1){
                            templateRoleFileAuths.add(auth);
                        }
                    }
                }
            }
        }
        // 获取模板相关的目录、目录路径Map
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select category.id ID, ");
        hqlBuffer.append(" sys_connect_by_path(category.name, '/') PATHNAME ");
        hqlBuffer.append(" from (select f.id, ");
        hqlBuffer.append("  f.parentid, ");
        hqlBuffer.append(" case ");
        hqlBuffer.append("  when f.parentid = 'ROOT' then ");
        hqlBuffer.append("  '" + rootPath + "'");
        hqlBuffer.append("  else ");
        hqlBuffer.append("  f.name ");
        hqlBuffer.append(" end name ");
        hqlBuffer.append(" from CM_PROJLIB_TEMPLATE_FILE f ");
        hqlBuffer.append("  where f.templateid = '" + templateId + "') category ");
        hqlBuffer.append("  start with category.parentid = 'ROOT' ");
        hqlBuffer.append(" connect by category.parentid = prior category.id ");
        hqlBuffer.append("  order by category.id ");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        Map<String, String> categoryIdPathMap = new HashMap<String, String>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("ID");
                String namePath = (String)map.get("PATHNAME");
                categoryIdPathMap.put(id, namePath);
            }
        }
        Map<String, String> roleMap = new HashMap<String, String>();
        for (TSRoleDto role : roles) {
            roleMap.put(role.getId(), role.getRoleCode());
        }
        // 将满足roles的文件路径、文件角色权限以Map<String, List<RepRoleFileAuth>>返回
        for (ProjectLibRoleFileAuth roleFileAuth : templateRoleFileAuths) {
            if (StringUtils.isNotEmpty(categoryIdPathMap.get(roleFileAuth.getFileId()))) {
                String fileId = categoryIdPathMap.get(roleFileAuth.getFileId());
                List<RepRoleFileAuthDto> list = new ArrayList<RepRoleFileAuthDto>();
                RepRoleFileAuthDto fileAuth = new RepRoleFileAuthDto();
                String roleId = roleFileAuth.getRoleId();
                if (StringUtils.isNotEmpty(roleMap.get(roleId))) {
                    if (!CommonUtil.isEmpty(roleAuthMap.get(categoryIdPathMap.get(roleFileAuth.getFileId())))) {
                        list = roleAuthMap.get(categoryIdPathMap.get(roleFileAuth.getFileId()));
                        fileAuth.setRoleId(roleFileAuth.getRoleId());
                        fileAuth.setPermissionCode(roleFileAuth.getPermissionCode());
                    }
                    else {
                        fileAuth.setRoleId(roleFileAuth.getRoleId());
                        fileAuth.setPermissionCode(roleFileAuth.getPermissionCode());
                    }
                    list.add(fileAuth);
                    roleAuthMap.put(fileId, list);
                }
            }
        }
        return roleAuthMap;
    }

    @Override
    public void applyTemplate(String projectId, Map<String, List<RepRoleFileAuthDto>> map, String templateId,String userId,String orgId) {
        String libId = projRoleService.getLibIdByProjectId(projectId);
        List<RepFileDto> files = getFolderTree(projectId, "0", "");
        repService.deleteRepRoleFileAuthByLibId(libId);

        List<TreeNode> list = projectLibTemplateService.getProjectLibTemplateFileCategorys(templateId);
        String parentId = projectLibTemplateService.getTemplateCategoryRootNodeId(templateId);
        ProjectLibTemplateFileCategory projectLibTemplateFileCategory = getEntity(
                ProjectLibTemplateFileCategory.class, parentId);
        List<RepFileDto> listRepFile = new ArrayList<RepFileDto>();
        if (files.size() == 1) {
            if (!CommonUtil.isEmpty(list)) {
                for (TreeNode t : list) {
                    ProjLibDocumentVo vo = new ProjLibDocumentVo();
                    // 生命周期控制
                    // vo.setFileTypeId("4028ef2d504608ba0150462418bf0001");
                    // 需求变更typeID更改为CODE
                   /* String fileTypeId = repFileTypeQueryService.getFileTypeIdByCode(RepFileTypeConstants.REP_FILE_TYPE_PRO);*/
                    String fileTypeId= "";
                    List<RepFileTypeDto> fileTypeList = repService.getRepFileTypesByFileTypeCode(ResourceUtil.getApplicationInformation().getAppKey(), RepFileTypeConstants.REP_FILE_TYPE_PRO);
                    if(!CommonUtil.isEmpty(fileTypeList)){
                        fileTypeId = fileTypeList.get(0).getId();
                    }
                    vo.setFileTypeId(fileTypeId);
                    // String libId = projRoleService.getLibIdByProjectId(vo.getProjectId());
                    RepFileDto repFile = new RepFileDto();
                    // repFile.setFileTypeId("4028ef2d504608ba0150462418bf0001");
                    // 需求变更typeID更改为CODE
                    repFile.setFileTypeId(fileTypeId);
                    repFile.setFileType(0);
                    repFile.setLibId(libId);
                    if (StringUtil.isEmpty(t.getPid())) {
                        // TreeNode rootNode = getEntity(TreeNode.class, t.getId());
                        repFile.setId(files.get(0).getId());
                        repFile.setParentId("ROOT");
                    }
                    else {
                        repFile.setFileName(t.getName());
                        repFile.setId(t.getId());
                        repFile.setParentId(t.getPid());
                    }
                    listRepFile.add(repFile);
                    // repService.addRepFile(repFile);
                }
            }
            List<ProjectLibTemplateFileCategory> childrenList = getTemplateByParentId(projectLibTemplateFileCategory.getId());
            List<RepFileDto> allAfter = new ArrayList<RepFileDto>();
            for (ProjectLibTemplateFileCategory pro : childrenList) {
                for (RepFileDto rep : listRepFile) {
                    if (StringUtil.isNotEmpty(rep.getParentId())
                            && rep.getParentId().equals(parentId)) {
                        rep.setParentId(files.get(0).getId());
                        allAfter.add(rep);
                    }
                }
            }
            List<String> parentIdList = new ArrayList<String>();
            parentIdList.add(files.get(0).getId());
            processNodes(allAfter, parentIdList, listRepFile,userId);
        }

        Map<String, String> map2 = getPath(projectId);
        List<RepRoleFileAuthDto> repRoleFileAuthList = new ArrayList<RepRoleFileAuthDto>();
        for (String p : map2.keySet()) {
            if (!CommonUtil.isEmpty(map.get(p))) {
                List<RepRoleFileAuthDto> repList = map.get(p);
                for (RepRoleFileAuthDto repRoleFileAuth : repList) {
                    repRoleFileAuth.setFileId(map2.get(p));
                    repRoleFileAuthList.add(repRoleFileAuth);
                }
            }
        }
        repService.batchSaveRepRoleFileAuthDto(repRoleFileAuthList);
        String hql = " delete from ProjectLibAuthTemplateLink p where p.projectId = '" + projectId
                + "'";
        sessionFacade.executeHql(hql);
        TSUserDto userDto = userService.getUserByUserId(userId);
        ProjectLibAuthTemplateLink projectLibAuthTemplateLink = new ProjectLibAuthTemplateLink();
        projectLibAuthTemplateLink.setProjectId(projectId);
        projectLibAuthTemplateLink.setTemplateId(templateId);
        CommonInitUtil.initGLObjectForCreate(projectLibAuthTemplateLink,userDto,orgId);
        save(projectLibAuthTemplateLink);
    }

    private List<ProjectLibTemplateFileCategory> getTemplateByParentId(String parentId) {
        return findByQueryString("from ProjectLibTemplateFileCategory t where 1=1 and t.parentId = '"
                + parentId + "'");
    }

    /**
     * 循环处理树节点
     *
     * @param all
     * @param parentIds
     * @see
     */
    private void processNodes(List<RepFileDto> all, List<String> parentIds, List<RepFileDto> listRepFile,String userId) {
        List<String> parentIdList = new ArrayList<String>();
        List<RepFileDto> allAfter = new ArrayList<RepFileDto>();
        if (!CommonUtil.isEmpty(all)) {
            for (RepFileDto node : all) {
                String parentId = "";
                for (String parentIdss : parentIds) {
                    if (parentIdss.equals(node.getParentId())) {
                        String id = node.getId();
                        node.setId(null);
                        FeignJson fj = repService.addRepFile(appKey,node,userId);
                        String idNew = String.valueOf(fj.getObj());
                        parentId = node.getId();
                        parentIdList.add(parentId);
                        List<ProjectLibTemplateFileCategory> childrenList = getTemplateByParentId(id);
                        for (ProjectLibTemplateFileCategory pro : childrenList) {
                            for (RepFileDto rep : listRepFile) {
                                if (rep.getParentId().equals(id)) {
                                    rep.setParentId(idNew);
                                    allAfter.add(rep);
                                }
                            }
                        }
                    }
                }
            }
            processNodes(allAfter, parentIdList, listRepFile,userId);
        }
    }

    @Override
    public Map<String, String> getPath(String projectId) {
        Map<String, String> allPathMap = new HashMap<String, String>();
        String libId = projRoleService.getLibIdByProjectId(projectId);
        Project project = getEntity(Project.class, projectId);
        String rootPath = project.getName() + "-" + project.getProjectNumber();
        // List<RepFile> files = getRepFileByLibIdAndType(libId, 0);
        // String hqlStr = "from RepFile t where t.libId=? and t.fileType=0";
        allPathMap = repService.getRepFileAllPath(libId,rootPath);
        return allPathMap;
    }

    @Override
    public List<ProjLibDocumentVo> getVersionHistory(String bizId, Integer pageSize, Integer pageNum, boolean isPage) {
        List<ProjLibDocumentVo> voList = new ArrayList<ProjLibDocumentVo>();

        String sql = " select rf.id, rf.filename, rf.filenumber, rf.bizversion, rf.fileremark, "
                + " rf.bizcurrent,rf.updatetime,rf.updateby,rf.createtime,rf.createby, t.procinstid, t.title "
                + " from rep_file rf "
                + " left join act_fd_started_task t on t.tasknumber = rf.id and t.status not in ('已终止', '已删除') "
                + " where rf.bizId= '" + bizId + "' order by rf.createtime desc";
        if (isPage) {
            sql = "SELECT IDSRV.* FROM (SELECT IRV.*, ROWNUM RN FROM (" + sql
                    + ")IRV WHERE ROWNUM <= " + pageSize * pageNum + ") IDSRV WHERE RN > "
                    + (pageNum - 1) * pageSize;
        }
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        Map<String,TSUserDto> userDtoMap = userService.getCommonPrepUserAllByUUid();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                ProjLibDocumentVo vo = new ProjLibDocumentVo();
                vo.setId(StringUtils.isEmpty((String)map.get("id")) ? "" : map.get("id").toString());
                vo.setDocName(StringUtils.isEmpty((String)map.get("filename")) ? "" : map.get(
                        "filename").toString());
                vo.setDocNumber(StringUtils.isEmpty((String)map.get("filenumber")) ? "" : map.get(
                        "filenumber").toString());
                vo.setVersion(StringUtils.isEmpty((String)map.get("bizversion")) ? "" : map.get(
                        "bizversion").toString());
                vo.setRemark(StringUtils.isEmpty((String)map.get("fileremark")) ? "" : map.get(
                        "fileremark").toString());
                vo.setStatus(StringUtils.isEmpty((String)map.get("bizcurrent")) ? "" : map.get(
                        "bizcurrent").toString());
                vo.setProcInstId(StringUtils.isEmpty((String)map.get("procInstId")) ? "" : map.get(
                        "procInstId").toString());
                vo.setTitle(StringUtils.isEmpty((String)map.get("title")) ? "" : map.get("title").toString());
                vo.setUpdateName(StringUtils.isEmpty((String)map.get("updateName")) ? "" : map.get(
                        "updateName").toString());
                vo.setUpdateTime((Date)map.get("updatetime"));
                if(!CommonUtil.isEmpty(map.get("createBy"))){
                    TSUserDto creataUserDto = userDtoMap.get(String.valueOf(map.get("createBy")));
                    if(!CommonUtil.isEmpty(creataUserDto)){
                        vo.setCreateName(creataUserDto.getRealName()+"-"+creataUserDto.getUserName());
                    }

                }

                vo.setCreateTime((Date)map.get("createtime"));
                voList.add(vo);
            }
        }

        return voList;
    }

    @Override
    public void updatePlanProcess(String fileId) {
        RepFileDto repFileDto = repService.getRepFileByRepFileId(appKey,fileId);
        List<ProjDocRelation> projDocList = sessionFacade.findHql("from ProjDocRelation where docId = ?",repFileDto.getId());

        //文档生命周期前期一步，更新计划进度
        if(!CommonUtil.isEmpty(projDocList)){
            for(ProjDocRelation projDoc : projDocList){
                DeliverablesInfo deliverablesInfo = (DeliverablesInfo) sessionFacade.getEntity(DeliverablesInfo.class,projDoc.getQuoteId());
                if (!CommonUtil.isEmpty(deliverablesInfo)) {
                    Plan plan = planService.getEntity(deliverablesInfo.getUseObjectId());
                    if (!CommonUtil.isEmpty(plan)) {
                        planService.updateProgress(plan);
                    }
                }
            }
        }
    }

}
