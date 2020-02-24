package com.glaway.ids.project.plan.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.DBUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.UUIDGenerator;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.system.lifecycle.service.LifeCycleStatusServiceI;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plan.dto.InputsDto;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.InputsServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.plan.service.PreposePlanServiceI;
import com.glaway.ids.project.plan.vo.PlanExcelVo;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import com.glaway.ids.project.projectmanager.entity.ProjDocRelation;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjDocVo;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.Dto2Entity;
import com.glaway.ids.util.JsonFromatUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 交付物信息
 * 
 * @author blcao
 * @version 2015年4月1日
 * @see InputsServiceImpl
 * @since
 */
@Service("inputsService")
@Transactional(propagation = Propagation.REQUIRED)
public class InputsServiceImpl extends CommonServiceImpl implements InputsServiceI {

    /**
     * 
     */
    private static final SystemLog log = BaseLogFactory.getSystemLog(InputsServiceImpl.class);

    /**
     * sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;

    /**
     * 计划处理Service
     */
    @Autowired
    private PlanServiceI planService;


    /**
     * 项目计划管理接口
     */
    @Autowired
    private LifeCycleStatusServiceI lifeCycleStatusService;



    /**
     * 计划输出Service
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;

    /**
     * 文档处理Service
     */
    @Autowired
    private ProjLibServiceI projLibService;

    @Autowired
    private ProjRoleServiceI projRoleService;

    /**
     * 计划前置表
     */
    @Autowired
    private PreposePlanServiceI preposePlanService;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private FeignRepService repFileService;
    
    @Value(value="${spring.application.name}")
    private String appKey;

    /**
     * 获取满足条件inputs的输入数目
     */
    @Override
    public long getCount(Inputs inputs) {
        String hql = "select count(*) " + createHql(inputs);
        return getCount(hql, null);
    }

    @Override
    public List<Inputs> queryNewInputsList(Inputs input) {
        String hql = "select i.id id,i.name name ,i.fileid fileid,i.origin origin,i.originType originType,"
                     + "i.required required,i.docName docName,i.docId docId,i.origindeliverablesinfoid origindeliverablesinfoid,i.originobjectid originobjectid,"
                     + "i.useobjectid useobjectid,i.useobjecttype useobjecttype,i.ext1 ext1,i.ext2 ext2,i.ext3 ext3"
                     + " from PM_INPUTS i "
                     + " where i.useObjectId = '"
                     + input.getUseObjectId()
                     + "' and i.useObjectType = '" + input.getUseObjectType() + "' order by i.createTime asc";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<Inputs> list = changeResultsToInputsList(objArrayList);
        return list;
    }

    @Override
    public Map<String, String> getRepFileNameAndBizIdMap(String libId) {
        Map<String, String> map = new HashMap<String, String>();
        String sql = "select f.id, f.filename, f.bizid, pf.path " + " from rep_file f,"
                + " (select category.id parentId, "
                + " sys_connect_by_path(category.filename, '/') path "
                + " from (select rf.id, rf.parentid, rf.filename, rf.filetype "
                + " from rep_file rf where rf.Libid = '" + libId + "' "
                + " and rf.filetype = 0) category " + " start with category.parentid = '"
                + libId + "' " + " connect by category.parentid = prior category.id "
                + " order by category.id) pf " + " where f.Libid = '" + libId + "' "
                + " and f.filetype = 1" + " and f.avaliable = '1' "
                + " and f.parentid = pf.parentId";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> obj : objArrayList) {
                String bizId = String.valueOf(obj.get("BIZID"));
                String fileName = String.valueOf(obj.get("FILENAME"));
                map.put(bizId, fileName);
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getRepFilePathAndBizIdMap(String libId) {
        Map<String, String> map = new HashMap<String, String>();
        String sql = "select f.id, f.filename, f.bizid, pf.path " + " from rep_file f,"
                + " (select category.id parentId, "
                + " sys_connect_by_path(category.filename, '/') path "
                + " from (select rf.id, rf.parentid, rf.filename, rf.filetype "
                + " from rep_file rf where rf.Libid = '" + libId + "' "
                + " and rf.filetype = 0) category " + " start with category.parentid = '"
                + libId + "' " + " connect by category.parentid = prior category.id "
                + " order by category.id) pf " + " where f.Libid = '" + libId + "' "
                + " and f.filetype = 1" + " and f.avaliable = '1' "
                + " and f.parentid = pf.parentId";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> obj : objArrayList) {
                String bizId = String.valueOf(obj.get("BIZID"));
                String path = String.valueOf(obj.get("PATH"));
                map.put(bizId, path);
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getRepFileIdAndBizIdMap(String libId) {
        Map<String, String> map = new HashMap<String, String>();
        String sql = "select f.id, f.filename, f.bizid, pf.path " + " from rep_file f,"
                + " (select category.id parentId, "
                + " sys_connect_by_path(category.filename, '/') path "
                + " from (select rf.id, rf.parentid, rf.filename, rf.filetype "
                + " from rep_file rf where rf.Libid = '" + libId + "' "
                + " and rf.filetype = 0) category " + " start with category.parentid = '"
                + libId + "' " + " connect by category.parentid = prior category.id "
                + " order by category.id) pf " + " where f.Libid = '" + libId + "' "
                + " and f.filetype = 1" + " and f.avaliable = '1' "
                + " and f.parentid = pf.parentId";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> obj : objArrayList) {
                String bizId = String.valueOf(obj.get("BIZID"));
                String fileId = String.valueOf(obj.get("ID"));
                map.put(bizId, fileId);
            }
        }
        return map;
    }

    @Override
    public void deleteInputsByPreposePlan(String originObjectId, String useObjectType, String useObjectId) {
        sessionFacade.executeHql("delete Inputs i where i.originObjectId = '" + originObjectId
                + "' and i.useObjectType = '" + useObjectType
                + "' and i.useObjectId = '" + useObjectId + "'");
    }

    @Override
    public List<ProjDocVo> getDocRelationList(Plan plan, String userId) {
        // 通过交付项判断子计划是否包括项目库
        // 获取计划的输出
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        deliverablesInfo.setUseObjectId(plan.getId());
        deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
        List<DeliverablesInfo> deliverablesList = deliverablesInfoService.queryDeliverableList(
                deliverablesInfo, 1, 10, false);

        if (CommonUtil.isEmpty(deliverablesList)) {
            return null;
        }

        // 获取其子计划的所有输出、并将其存入deliverablesMap
        Map<String, Object> deliverablesMap = new HashMap<String, Object>();
        Plan childPlan = new Plan();
        childPlan.setParentPlanId(plan.getId());
        List<Plan> childList = planService.queryPlanList(childPlan, 1, 10, false);

        for (Plan child : childList) {
            deliverablesInfo = new DeliverablesInfo();
            deliverablesInfo.setUseObjectId(child.getId());
            deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            List<DeliverablesInfo> childDeliverables = deliverablesInfoService.queryDeliverableList(
                    deliverablesInfo, 1, 10, false);
            for (DeliverablesInfo childDeli : childDeliverables) {
                String projDocRelationStr = projLibService.getDocRelation(childDeli.getId());
                List<ProjDocRelation> projDocRelationList = new ArrayList<>();
                if(!CommonUtil.isEmpty(projDocRelationStr)){
                    projDocRelationList = JSON.parseArray(projDocRelationStr,ProjDocRelation.class);
                }
                if (CommonUtil.isEmpty(projDocRelationList)) {
                    continue;
                }
                deliverablesMap.put(childDeli.getName(), projDocRelationList.get(0));
            }
        }

        List<ProjDocVo> projDocRelationList = new ArrayList<ProjDocVo>();

        for (DeliverablesInfo parentDeli : deliverablesList) {
            ProjDocVo projDocVo = new ProjDocVo();
            projDocVo.setDeliverableId(parentDeli.getId());
            projDocVo.setDeliverableName(parentDeli.getName());
            if (deliverablesMap.get(parentDeli.getName()) != null) { // 如果在子计划中包含交付项，需要使用子计划的交付项
                ProjDocRelation projDocRelation = (ProjDocRelation)deliverablesMap.get(parentDeli.getName());
                convertToVo(projDocVo, projDocRelation, plan.getId(),userId);
                projDocVo.setDeliverableName(parentDeli.getName());
                projDocVo.setOpFlag(false);
                projDocRelationList.add(projDocVo);
            }
            else { // 如果子计划中没有的，需要查询自己的交付项
                String projDocRelationStr = projLibService.getDocRelation(parentDeli.getId());
                List<ProjDocRelation> projDocRelationDbList = new ArrayList<>();
                if(!CommonUtil.isEmpty(projDocRelationStr)){
                    projDocRelationDbList= JSON.parseArray(projDocRelationStr,ProjDocRelation.class);
                }

                if (!CommonUtil.isEmpty(projDocRelationDbList)) {
                    convertToVo(projDocVo, projDocRelationDbList.get(0), plan.getId(),userId);
                }
                projDocVo.setOpFlag(true);
                projDocRelationList.add(projDocVo);
            }
        }

        return projDocRelationList;
    }



    /**
     * 组装挂接交付项的VO对象
     *
     * @param projDocRelation
     * @return
     */
    private ProjDocVo convertToVo(ProjDocVo projDocVo, ProjDocRelation projDocRelation,
                                  String planId,String userId) {
        RepFileDto rep = repFileService.getRepFileByRepFileId(appKey, projDocRelation.getDocId());
        if (StringUtils.isEmpty(projDocRelation.getDocId())) {
            return projDocVo;
        }
        projDocVo.setDocId(projDocRelation.getDocId());
        String havePower = planService.getOutPowerForPlanList(projDocRelation.getDocId(), planId,userId);
        if ("downloadDetail".equals(havePower)) {
            projDocVo.setDownload(true);
            projDocVo.setDetail(true);
            projDocVo.setHavePower(true);
        }
        else if ("detail".equals(havePower)) {
            projDocVo.setDownload(false);
            projDocVo.setDetail(true);
            projDocVo.setHavePower(true);
        }
        else {
            projDocVo.setDownload(false);
            projDocVo.setDetail(false);
            projDocVo.setHavePower(false);
        }
        if (rep == null) {
            return projDocVo;
        }else {
            projDocVo.setDocName(rep.getFileName());
            projDocVo.setVersion(rep.getBizVersion());
            String approveStatus = lifeCycleStatusService.getTitleByPolicyIdAndName(
                rep.getPolicy().getId(),
                rep.getBizCurrent());
            projDocVo.setStatus(approveStatus);
            projDocVo.setSecurityLevel(rep.getSecurityLevel());
        }
        return projDocVo;
    }


    /**
     * 将查询的结果转换为List<Inputs>
     *
     * @param objArrayList
     * @return
     */
    private List<Inputs> changeResultsToInputsList(List<Map<String, Object>> objArrayList) {
        List<Inputs> list = new ArrayList<Inputs>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    Inputs input = new Inputs();
                    input.setId(id);
                    input.setName(StringUtils.isNotEmpty((String)map.get("name")) ? (String)map.get("name") : "");
                    input.setFileId(StringUtils.isNotEmpty((String)map.get("fileid")) ? (String)map.get("fileid") : "");
                    input.setOrigin(StringUtils.isNotEmpty((String)map.get("origin")) ? (String)map.get("origin") : "");
                    input.setRequired(StringUtils.isNotEmpty((String)map.get("required")) ? (String)map.get("required") : "");
                    input.setDocId(StringUtils.isNotEmpty((String)map.get("docId")) ? (String)map.get("docId") : "");
                    input.setDocName(StringUtils.isNotEmpty((String)map.get("docname")) ? (String)map.get("docname") : "");
                    input.setUseObjectId(StringUtils.isNotEmpty((String)map.get("useobjectid")) ? (String)map.get("useobjectid") : "");
                    input.setUseObjectType(StringUtils.isNotEmpty((String)map.get("useobjecttype")) ? (String)map.get("useobjecttype") : "");
                    input.setOriginObjectId(StringUtils.isNotEmpty((String)map.get("originObjectId")) ? (String)map.get("originObjectId") : "");
                    input.setOriginType(StringUtils.isNotEmpty((String)map.get("originType")) ? (String)map.get("originType") : "");
                    input.setOriginTypeExt(StringUtils.isNotEmpty((String)map.get("originTypeExt")) ? (String)map.get("originTypeExt") : "");
                    if(!CommonUtil.isEmpty((String)map.get("originObjectId"))){
                        input.setOriginObjectName(StringUtils.isNotEmpty((String)map.get("originObjectName")) ? (String)map.get("originObjectName") : "");
                    }
                    else {
                        if ((!CommonUtil.isEmpty((String)map.get("originType")) && PlanConstants.LOCAL_EN.equals((String)map.get("originType")))
                                || (!CommonUtil.isEmpty((String)map.get("originTypeExt")) && PlanConstants.LOCAL_EN.equals((String)map.get("originTypeExt")))
                        ) {
                            input.setOriginObjectName(PlanConstants.LOCAL);
                        }
                        else if(!CommonUtil.isEmpty((String)map.get("originType")) && PlanConstants.PROJECTLIBDOC.equals((String)map.get("originType"))){
//                            input.setOriginObjectName(PlanConstants.LOCAL);
                        }
                    }
                    input.setOriginDeliverablesInfoId(StringUtils.isNotEmpty((String)map.get("originDeliverablesInfoId")) ? (String)map.get("originDeliverablesInfoId") : "");
                    input.setOriginDeliverablesInfoName(StringUtils.isNotEmpty((String)map.get("originDeliverablesInfoName")) ? (String)map.get("originDeliverablesInfoName") : "");
                    input.setOriginType(StringUtils.isNotEmpty((String)map.get("originType")) ? (String)map.get("originType") : "");
                    input.setExt1(StringUtils.isNotEmpty((String)map.get("ext1")) ? (String)map.get("ext1") : "");
                    input.setExt2(StringUtils.isNotEmpty((String)map.get("ext2")) ? (String)map.get("ext2") : "");
                    input.setExt3(StringUtils.isNotEmpty((String)map.get("ext3")) ? (String)map.get("ext3") : "");
                    input.setOriginDeliverablesInfoId(StringUtils.isNotEmpty((String)map.get("originDeliverablesInfoId")) ? (String)map.get("originDeliverablesInfoId") : "");
                    input.setOriginObjectId(StringUtils.isNotEmpty((String)map.get("originObjectId")) ? (String)map.get("originObjectId") : "");
                    list.add(input);
                }
            }
        }
        return list;
    }

    @Override
    public void deleteInputsByOriginDeliverables(String originDeliverablesInfoId,
                                                 String useObjectType) {
        sessionFacade.executeHql("delete Inputs i where i.originDeliverablesInfoId = '"
                + originDeliverablesInfoId + "' and i.useObjectType = '"
                + useObjectType + "'");
    }
    
    @Override
    public void deleteInputsById(String id) {
        sessionFacade.executeHql("delete Inputs i where i.id = '" + id + "'");
    }

    @Override
    public List<Inputs> getInputsByUseObeject(String useObjectType, String useObjectId) {
        String hql = "from Inputs l where l.useObjectType = '" + useObjectType
                + "' and l.useObjectId = '" + useObjectId + "'";
        return findByQueryString(hql);
    }

    @Override
    public void deleteInputsByOriginObject(String originObjectId, String useObjectType) {
        sessionFacade.executeHql(
                "update Inputs set originObjectId='',originDeliverablesInfoId='',docId='',docName='',originType='' where  useObjectType=? and originObjectId=?",
                useObjectType, originObjectId);

    }

    @Override
    public void deleteInputsByPlan(Object obj) {
        if (obj instanceof Plan) {
            Plan plan = (Plan)obj;
            sessionFacade.executeHql(
                    "delete from Inputs where  useObjectType=? and useObjectId=?",
                    PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());
        }

    }
    
    /**
     * 获取满足条件inputs的输入信息
     */
    @Override
    public List<Inputs> queryInputsDetailList(Inputs inputs) {
        String hql = "select * from ( select i.id id,i.name name ,i.fileid fileid,i.origin origin,"
                     + "i.required required,r.docid docId,t.filename docname,"
                     + "i.useobjectid useobjectid,i.useobjecttype useobjecttype,"
                     + "p.id originObjectId, p.planname originObjectName, i.originType originType , i.originTypeExt originTypeExt,"
                     + "d.id originDeliverablesInfoId,d.name originDeliverablesInfoName , i.createtime    createtime"
                     + " from PM_INPUTS i "
                     + " left join pm_plan p on (i.originobjectid = p.id and i.useobjecttype = 'PLAN')"
                     + " left join PM_DELIVERABLES_INFO d on i.originDeliverablesInfoId = d.id"
                     + " left join PM_PROJ_DOC_RELATION r on i.origindeliverablesinfoid = r.quoteid "
                     + " left join rep_file t on t.id = r.docid" + " where i.useObjectId = '"
                     + inputs.getUseObjectId() + "' and d.useObjectType = '"
                     + inputs.getUseObjectType()
                     + "' and i.originObjectId = p.id and i.originDeliverablesInfoId = d.id and (i.origintypeext <> 'DELIEVER' or i.origintypeext is null) "
                     + " union all  "
                     + " select i.id            id, "
                     + " i.name          name,"
                     + " i.fileid        fileid,"
                     + " i.origin        origin,"
                     + " i.required      required,"
                     + " i.docid         docId,"
                     + " i.docname      docname,"
                     + " i.useobjectid   useobjectid,"
                     + " i.useobjecttype useobjecttype,"
                     + " i.originObjectId            originObjectId,"
                     + " ''      originObjectName,  i.originType originType , i.originTypeExt originTypeExt,"
                     + " i.originDeliverablesInfoId            originDeliverablesInfoId,"
                     + " ''          originDeliverablesInfoName, i.createtime    createtime"
                     + " from PM_INPUTS i where  i.useObjectId = '"+ inputs.getUseObjectId() + "' "
                     + " and i.useObjectType = 'PLAN' and (i.origintype='LOCAL' or i.origintypeext='DELIEVER')) a where 1=1 order by a.createtime asc";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<Inputs> list = changeResultsToInputsList(objArrayList);
        return list;
    }
    
    /**
     * 获取满足条件inputs的输入信息
     */
    @Override
    public List<Inputs> queryInputsDetailList(String planParentId) {
        String hql = "select * from (select i.id id,i.name name ,i.fileid fileid,i.origin origin,"
                     + "i.required required,r.docid docId,t.filename docname,"
                     + "i.useobjectid useobjectid,i.useobjecttype useobjecttype,"
                     + "p.id originObjectId, p.planname originObjectName,i.originType originType , i.originTypeExt originTypeExt,"
                     + "d.id originDeliverablesInfoId,d.name originDeliverablesInfoName, i.createtime"
                     + " from PM_INPUTS i "
                     + " left join pm_plan p on (i.originobjectid = p.id and i.useobjecttype = 'PLAN')"
                     + " left join PM_DELIVERABLES_INFO d on i.originDeliverablesInfoId = d.id"
                     + " left join PM_PROJ_DOC_RELATION r on i.origindeliverablesinfoid = r.quoteid "
                     + " left join rep_file t on t.id = r.docid" + " where p.parentPlanId = '"
                     + planParentId + "' and d.useObjectType = 'PLAN' "
                     + " and p.avaliable='1' "
                     + " and i.originObjectId = p.id and i.originDeliverablesInfoId = d.id  and (i.origintypeext <> 'DELIEVER' or i.origintypeext is null)"
                     + "  union all "
                     + "  select i.id id,"
                     + "  i.name name,"
                     + "  i.fileid fileid,"
                     + "  i.origin origin,"
                     + "  i.required required,"
                     + "  i.docId docId,"
                     + "  i.docName docname,"
                     + "  i.useobjectid useobjectid,"
                     + "  i.useobjecttype useobjecttype,"
                     + "  i.originObjectId originObjectId,"
                     + "  '' originObjectName,"
                     + "  i.originType originType , i.originTypeExt originTypeExt,"
                     + "  i.originDeliverablesInfoId originDeliverablesInfoId,"
                     + "  '' originDeliverablesInfoName,"
                     + "  i.createtime"
                     + "  from PM_INPUTS i"
                     + "  left join pm_plan p on (i.useobjectid = p.id and"
                     + "                       i.useobjecttype = 'PLAN')"
                     + "  where p.parentPlanId = '"+ planParentId + "'"
                     + "  and p.avaliable = '1' and (i.origintype='LOCAL' or i.origintypeext='DELIEVER')) t where 1=1 order by t.useobjectid,t.name";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<Inputs> list = changeResultsToInputsList(objArrayList);
        return list;
    }
    
    /**
     * 获取满足条件inputs的输入信息
     */
    @Override
    public List<Inputs> queryInputList(Inputs inputs, int page, int rows, boolean isPage) {
        String hql = createHql(inputs);
        if (isPage) {
            return pageList(hql, (page - 1) * rows, rows);
        }
        return findByQueryString(hql);
    }
    
    /**
     * 根据Plan中的传值拼接HQL
     * 
     * @param plan
     * @return
     * @see
     */
    private String createHql(Inputs inputs) {
        String hql = "from Inputs l where  1 = 1 ";
        // 计划名称
        if (!StringUtils.isEmpty(inputs.getUseObjectId())) {
            hql = hql + " and l.useObjectId = '" + inputs.getUseObjectId() + "'";
        }
        if (!StringUtils.isEmpty(inputs.getUseObjectType())) {
            hql = hql + " and l.useObjectType = '" + inputs.getUseObjectType() + "'";
        }
        // 计划名称
        if (!StringUtils.isEmpty(inputs.getName())) {
            hql = hql + " and l.name = '" + inputs.getName() + "'";
        }
        // 来源
        if (!StringUtils.isEmpty(inputs.getOrigin())) {
            hql = hql + " and l.origin = '" + inputs.getOrigin() + "'";
        }
        // 来源计划Id
        if (!StringUtils.isEmpty(inputs.getOriginObjectId())) {
            hql = hql + " and l.originObjectId = '" + inputs.getOriginObjectId() + "'";
        }
        return hql;
    }
    
    @Override
    public void updateInputsForDocInfoById(String id, String docId,String docName) {
        Inputs inputs = (Inputs)sessionFacade.getEntity(Inputs.class, id);
        inputs.setDocId(docId);
        inputs.setDocName(docName);
        sessionFacade.saveOrUpdate(inputs);
    }
    
    /**
     * 获取满足条件inputs的输入信息
     */
    @Override
    public List<Inputs> queryOutInputsDetailList(String planParentId) {
        String hql = "  select i.id id,"
                     + "  i.name name,"
                     + "  i.fileid fileid,"
                     + "  i.origin origin,"
                     + "  i.required required,"
                     + "  i.docId docId,"
                     + "  i.docName docname,"
                     + "  i.useobjectid useobjectid,"
                     + "  i.useobjecttype useobjecttype,"
                     + "  i.originObjectId originObjectId,"
                     + "  '' originObjectName,"
                     + "  i.originType originType , i.originTypeExt originTypeExt,"
                     + "  i.originDeliverablesInfoId originDeliverablesInfoId,"
                     + "  '' originDeliverablesInfoName,"
                     + "  i.createtime"
                     + "  from PM_INPUTS i"
                     + "  left join pm_plan p on (i.useobjectid = p.id and"
                     + "                       i.useobjecttype = 'PLAN')"
                     + "  where p.parentPlanId = '"+ planParentId + "'"
                     + "  and p.avaliable = '1' and i.origintypeext='DELIEVER' order by p.cellId,p.planname";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<Inputs> list = changeResultsToInputsList(objArrayList);
        return list;
    }
    
    @Override
    public List<ProjDocVo> getDocRelationListMatch(Plan plan,String userId,String deliverName) {
        // 通过交付项判断子计划是否包括项目库
        // 获取计划的输出
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        deliverablesInfo.setUseObjectId(plan.getId());
        deliverablesInfo.setName(deliverName);
        deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
        List<DeliverablesInfo> deliverablesList = deliverablesInfoService.queryDeliverableList(
            deliverablesInfo, 1, 10, false);

        if (CommonUtil.isEmpty(deliverablesList)) {
            return null;
        }

        List<ProjDocVo> projDocRelationList = new ArrayList<ProjDocVo>();
        for (DeliverablesInfo parentDeli : deliverablesList) {
            ProjDocVo projDocVo = new ProjDocVo();
            projDocVo.setDeliverableId(parentDeli.getId());
            projDocVo.setDeliverableName(parentDeli.getName());
            String curProjDocRelationListJson = projLibService.getDocRelation(parentDeli.getId());
            List<ProjDocRelation> curProjDocRelationList = null;
            if(!CommonUtil.isEmpty(curProjDocRelationListJson)){
                curProjDocRelationList = JSON.parseObject(JsonFromatUtil.formatJsonToList(curProjDocRelationListJson),new TypeReference<List<ProjDocRelation>>(){});
            }
            
            ProjDocRelation projDocRelation = null;
            if(!CommonUtil.isEmpty(curProjDocRelationList)){
                projDocRelation = curProjDocRelationList.get(0);
            }             
            if (projDocRelation != null) { // 如果在子计划中包含交付项，需要使用子计划的交付项                            
                convertToVo(projDocVo, projDocRelation, plan.getId(),userId);
                projDocVo.setDeliverableName(parentDeli.getName());
                projDocVo.setOpFlag(false);
                projDocRelationList.add(projDocVo);
            }
            else { // 如果子计划中没有的，需要查询自己的交付项
                String projDocRelationDbListJson = projLibService.getDocRelation(parentDeli.getId());
                List<ProjDocRelation> projDocRelationDbList = null;
                if(!CommonUtil.isEmpty(projDocRelationDbListJson)){
                    projDocRelationDbList = JSON.parseObject(JsonFromatUtil.formatJsonToList(projDocRelationDbListJson),new TypeReference<List<ProjDocRelation>>(){});
                }
                if (!CommonUtil.isEmpty(projDocRelationDbList)) {
                    convertToVo(projDocVo, projDocRelationDbList.get(0), plan.getId(),userId);
                }
                projDocVo.setOpFlag(true);
                projDocRelationList.add(projDocVo);
            }
            break;

        }
        return projDocRelationList;
    }
    
    @Override
    public void updateInputsByAddAndDel(List<Inputs> addInputList, List<Inputs> delInputList) {
        if (!CommonUtil.isEmpty(addInputList)) {
            batchSave(addInputList);
        }
        if (!CommonUtil.isEmpty(delInputList)) {
            deleteInputList(delInputList);
        }
    }
    
    @Override
    public void deleteInputList(List<Inputs> delInputs) {
        for (Inputs in : delInputs) {
            if (in != null && StringUtils.isNotEmpty(in.getId())) {
                deleteInputsById(in.getId());
            }
        }
    }
    
    @Override
    public void batchUpdateInputsAttribute(String attribute, List<Inputs> updateInputs) {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            conn.setAutoCommit(false);
            StringBuffer sqlStringBuffer = new StringBuffer();
            if (!CommonUtil.isEmpty(updateInputs)) {
                sqlStringBuffer.append("UPDATE PM_INPUTS SET  ");
                if ("fromDeliverId".equals(attribute)) {
                    sqlStringBuffer.append(" ORIGINOBJECTID = ?, ");
                    sqlStringBuffer.append(" ORIGINDELIVERABLESINFOID = ? ");
                    sqlStringBuffer.append(" WHERE ID = ? ");
                }
                ps = conn.prepareStatement(sqlStringBuffer.toString());
                for (Inputs input : updateInputs) {
                    if ("fromDeliverId".equals(attribute)) {
                        ps.setObject(1, input.getOriginObjectId());
                        ps.setObject(2, input.getOriginDeliverablesInfoId());
                        ps.setObject(3, input.getId());
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch (Exception ex) {
            try {
                conn.rollback();
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
        finally {
            try {
                conn.setAutoCommit(true);
                DBUtil.closeConnection(null, ps, conn);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public FeignJson listView(Map<String, Object> map) {
        FeignJson j = new FeignJson();
        ObjectMapper mapper = new ObjectMapper();
        InputsDto inpsDto = mapper.convertValue(map.get("Inputs"),InputsDto.class);
        PlanDto planDto = mapper.convertValue(map.get("Plan"),PlanDto.class);
        Inputs inputs = new Inputs();
        Plan plan = new Plan();
        Dto2Entity.copyProperties(inpsDto,inputs);
        Dto2Entity.copyProperties(planDto,plan);
        String userId = map.get("userId").toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().setVersion(
                1.0).create();
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.inputs.listViewSuccess");
        try {
            List<Inputs> list = new ArrayList<Inputs>();
            if (inputs != null && StringUtils.isNotEmpty(inputs.getUseObjectId())
                    && StringUtils.isNotEmpty(inputs.getUseObjectType())) {
//                list = inputsService.getInputsAndDocuments(inputs);
                list = queryNewInputsList(inputs);
            }

            String libId = "";
            if(!CommonUtil.isEmpty(plan) && !CommonUtil.isEmpty(plan.getProjectId())){
                String projectId = plan.getProjectId();
                libId = projRoleService.getLibIdByProjectId(projectId);
            }else{
                String projectId = map.get("projectId").toString();
                libId = projRoleService.getLibIdByProjectId(projectId);
            }


            Map<String, String> fileNameMap = new HashMap<String, String>();

            Map<String, String> filePathMap = new HashMap<String, String>();

            Map<String, String> fileIdMap = new HashMap<String, String>();

            if(!CommonUtil.isEmpty(libId)){
                fileNameMap = getRepFileNameAndBizIdMap(libId);

                filePathMap = getRepFilePathAndBizIdMap(libId);

                fileIdMap = getRepFileIdAndBizIdMap(libId);
            }


            if(!CommonUtil.isEmpty(list)){
                for(Inputs input : list){
                    if(!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals("LOCAL")){
                        input.setDocNameShow(input.getDocName());
                    }else if(!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals("PROJECTLIBDOC")){
                        if(!CommonUtil.isEmpty(fileNameMap.get(input.getDocId()))){
                            input.setDocNameShow(fileNameMap.get(input.getDocId()));
                        }
                        if(!CommonUtil.isEmpty(filePathMap.get(input.getDocId()))){
                            input.setOriginPath(filePathMap.get(input.getDocId()));
                        }
                        if(!CommonUtil.isEmpty(fileIdMap.get(input.getDocId()))){
                            input.setDocIdShow(fileIdMap.get(input.getDocId()));
                        }
                    }else if(!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals("PLAN")){
                        Plan p = planService.getEntity(input.getOriginObjectId());
                        List<ProjDocVo> projDocRelationList = new ArrayList<ProjDocVo>();
                        if(!CommonUtil.isEmpty(p)){
                            projDocRelationList = getDocRelationList(p, userId);
                        }
                        ProjDocVo projDoc = new ProjDocVo();
                        if(!CommonUtil.isEmpty(projDocRelationList)){
                            for(ProjDocVo vo:projDocRelationList){
                                if(vo.getDeliverableId().equals(input.getOriginDeliverablesInfoId())){
                                    projDoc = vo;
                                    break;
                                }
                            }
                        }
                        input.setOriginPath(p.getPlanNumber()+"."+p.getPlanName());
                        input.setDocId(projDoc.getDocId());
                        input.setDocNameShow(projDoc.getDocName());
                        input.setExt1(String.valueOf(projDoc.isDownload()));
                        input.setExt2(String.valueOf(projDoc.isHavePower()));
                        input.setExt3(String.valueOf(projDoc.isDetail()));
                    }
                }
            }


            for (Inputs i : list) {
                if (!StringUtils.isEmpty(i.getDocId())) {
                    if (StringUtils.isNotEmpty(i.getOriginDeliverablesInfoId())) {
                        List<ProjDocRelation> projDocRelations = projLibService.getDocRelationList(i.getOriginDeliverablesInfoId());
                        if (!CommonUtil.isEmpty(projDocRelations)) {
                            i.setDocId(projDocRelations.get(0).getDocId());
                            i.setDocName(projDocRelations.get(0).getRepFile().getFileName());
                            String havePower = planService.getOutPower(
                                    projDocRelations.get(0).getDocId(), i.getUseObjectId(),userId);
                            if ("downloadDetail".equals(havePower)) {
                                i.setDownload(true);
                                i.setDetail(true);
                                i.setHavePower(true);
                            }
                            else if ("detail".equals(havePower)) {
                                i.setDownload(false);
                                i.setDetail(true);
                                i.setHavePower(true);
                            }
                            else {
                                i.setDownload(false);
                                i.setDetail(false);
                                i.setHavePower(false);
                            }
                            i.setSecurityLeve(projDocRelations.get(0).getRepFile().getSecurityLevel());
                        }
                        else if (StringUtils.isNotEmpty(i.getOriginObjectId())) {
                            List<DeliverablesInfo> deliverablesInfos = deliverablesInfoService.getDeliverablesByParentAndName(
                                    i.getOriginObjectId(), i.getName());
                            if (!CommonUtil.isEmpty(deliverablesInfos)) {
                                i.setDocId(deliverablesInfos.get(0).getDocId());
                                i.setDocName(deliverablesInfos.get(0).getName());
                                String havePower = planService.getOutPower(
                                        projDocRelations.get(0).getDocId(), i.getUseObjectId(),userId);
                                if ("downloadDetail".equals(havePower)) {
                                    i.setDownload(true);
                                    i.setDetail(true);
                                    i.setHavePower(true);
                                }
                                else if ("detail".equals(havePower)) {
                                    i.setDownload(false);
                                    i.setDetail(true);
                                    i.setHavePower(true);
                                }
                                else {
                                    i.setDownload(false);
                                    i.setDetail(false);
                                    i.setHavePower(false);
                                }
                                i.setSecurityLeve(projDocRelations.get(0).getRepFile().getSecurityLevel());
                            }
                        }
                    }
                }
            }
            String json = gson.toJson(list);
            j.setObj(json);
            return j;
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.inputs.listViewFail");
            j.setMsg(message);
            j.setSuccess(false);
            return j;
        }
    }

    @Override
    public FeignJson getInputsRelationList(Plan plan, int page, int rows, String projectId1,String userId) {
        FeignJson j = new FeignJson();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().setVersion(
                1.0).create();
        plan = planService.getEntity(plan.getId());
        List<Inputs> inputList = new ArrayList<Inputs>();
        long count = 0;
        Inputs inputs = new Inputs();
        inputs.setUseObjectId(plan.getId());
        inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
        inputList = queryInputList(inputs, page, rows, true);
        count = getCount(inputs);
        // 设置输入的附件
        List<Plan> preposeList = new ArrayList<Plan>();
        Map<String, Plan> preposeNameMap = new HashMap<String, Plan>();
        List<PreposePlan> preposePlans = preposePlanService.getPreposePlansByPlanId(plan);
        if (!CommonUtil.isEmpty(preposePlans)) {
            Map<String, String> preposeMap = new HashMap<String, String>();
            for (PreposePlan pre : preposePlans) {
                preposeMap.put(pre.getPreposePlanId(), pre.getPreposePlanId());
            }
            Plan condition = new Plan();
            condition.setProjectId(plan.getProjectId());
            condition.setAvaliable("1");
            List<Plan> planList = planService.queryPlanList(condition, 1, 10, false);
            for (Plan p : planList) {
                if (StringUtils.isNotEmpty(preposeMap.get(p.getId()))) {
                    preposeList.add(p);
                    preposeNameMap.put(p.getId(), p);
                }
            }
        }
        for (Inputs in : inputList) {
            if (StringUtils.isNotEmpty(in.getOriginObjectId())) {
                if (!CommonUtil.isEmpty(preposeNameMap.get(in.getOriginObjectId()))) {
                    Plan planNew = preposeNameMap.get(in.getOriginObjectId());
                    in.setOriginObjectName(planNew.getPlanName());
                    List<ProjDocVo> ProjDocRelationList = getDocRelationList(planNew,userId);
                    for (ProjDocVo p : ProjDocRelationList) {
                        if (in.getName().equals(p.getDeliverableName())
                                && StringUtils.isNotEmpty(p.getDocId())) {
                            in.setDocId(p.getDocId());
                            in.setDocName(p.getDocName());
                            in.setSecurityLeve(p.getSecurityLevel());
                            String havePower = planService.getOutPower(p.getDocId(), plan.getId(),userId);
                            if ("downloadDetail".equals(havePower)) {
                                in.setDownload(true);
                                in.setDetail(true);
                                in.setHavePower(true);
                            }
                            else if ("detail".equals(havePower)) {
                                in.setDownload(false);
                                in.setDetail(true);
                                in.setHavePower(true);
                            }
                            else {
                                in.setDownload(false);
                                in.setDetail(false);
                                in.setHavePower(false);
                            }
                            break;
                        }
                    }
                }
            }
        }

        String libId = "";
        if(!CommonUtil.isEmpty(plan) && !CommonUtil.isEmpty(plan.getProjectId())){
            String projectId = plan.getProjectId();
            libId = projRoleService.getLibIdByProjectId(projectId);
        }else{
            libId = projRoleService.getLibIdByProjectId(projectId1);
        }

        Map<String, String> fileNameMap = new HashMap<String, String>();

        Map<String, String> filePathMap = new HashMap<String, String>();

        Map<String, String> fileIdMap = new HashMap<String, String>();

        if(!CommonUtil.isEmpty(libId)){
            fileNameMap = getRepFileNameAndBizIdMap(libId);

            filePathMap = getRepFilePathAndBizIdMap(libId);

            fileIdMap = getRepFileIdAndBizIdMap(libId);
        }


        if(!CommonUtil.isEmpty(inputList)){
            for(Inputs input : inputList){
                if(!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals("LOCAL")){
                    input.setDocNameShow(input.getDocName());
                }else if(!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals("PROJECTLIBDOC")){
                    if(!CommonUtil.isEmpty(fileNameMap.get(input.getDocId()))){
                        input.setDocNameShow(fileNameMap.get(input.getDocId()));
                    }
                    if(!CommonUtil.isEmpty(filePathMap.get(input.getDocId()))){
                        input.setOriginPath(filePathMap.get(input.getDocId()));
                    }
                    if(!CommonUtil.isEmpty(fileIdMap.get(input.getDocId()))){
                        input.setDocIdShow(fileIdMap.get(input.getDocId()));
                    }
                }else if(!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals("PLAN")){
                    Plan p = planService.getEntity(input.getOriginObjectId());
                    List<ProjDocVo> projDocRelationList = new ArrayList<ProjDocVo>();
                    if(!CommonUtil.isEmpty(p)){
                        projDocRelationList = getDocRelationList(p,userId);
                    }
                    ProjDocVo projDoc = new ProjDocVo();
                    if(!CommonUtil.isEmpty(projDocRelationList)){
                        for(ProjDocVo vo:projDocRelationList){
                            if(vo.getDeliverableId().equals(input.getOriginDeliverablesInfoId())){
                                projDoc = vo;
                                break;
                            }
                        }
                    }
                    input.setOriginPath(p.getPlanNumber()+"."+p.getPlanName());
                    input.setDocId(projDoc.getDocId());
                    input.setDocIdShow(projDoc.getDocId());
                    input.setDocNameShow(projDoc.getDocName());
                    input.setExt1(String.valueOf(projDoc.isDownload()));
                    input.setExt2(String.valueOf(projDoc.isHavePower()));
                    input.setExt3(String.valueOf(projDoc.isDetail()));
                }
            }
        }

        String json = gson.toJson(inputList);
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("json",json);
        result.put("count",count);
        j.setAttributes(result);
        return j;
    }

    @Override
    public List<Inputs> getInputsInfoByPlanTemplateId(String templId) {
        List<Inputs> list = new ArrayList<Inputs>();
        if(!CommonUtil.isEmpty(templId)) {
            String hql = "from Inputs where useObjectId in(select id from PlanTemplateDetail where planTemplateId = ?)";
            list = sessionFacade.findHql(hql, templId);
        }
        return list;
    }

    @Override
    public Map<String, List<Inputs>> getInputsInfoByPlanTemplateExcel(Map<String, String> map, Map<String, PlanTemplateExcelVo> excelMap, Map<String, String> deliverMap, TSUserDto userDto, String orgId) {
        Map<String, List<Inputs>> resultMap = new HashMap<String, List<Inputs>>();
        for(String id : map.keySet()) {
            List<Inputs> list = new ArrayList<Inputs>();
            String preposeNos = map.get(id);
            Map<String, String> map2 = new HashMap<String, String>();
            String[] nos = preposeNos.split(",");
            for(String no : nos) {
                if(!CommonUtil.isEmpty(no) && CommonUtil.isEmpty(map2.get(no))) {
                    PlanTemplateExcelVo excelVo = excelMap.get(no);
                    String deliverablesNames = excelVo.getDeliverablesName();
                    if(!CommonUtil.isEmpty(deliverablesNames)) {
                        String[] deliverables = deliverablesNames.split(",");
                        Map<String, String> map3 = new HashMap<String, String>();
                        for (String name : deliverables) {
                            name = name.trim();
                            if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map3.get(name))) {
                                Inputs inputs = new Inputs();
                                CommonInitUtil.initGLObjectForCreate(inputs,userDto,orgId);
                                inputs.setId(UUIDGenerator.generate().toString());
                                inputs.setName(name);
                                inputs.setUseObjectId(id);
                                inputs.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                                inputs.setOriginType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                                inputs.setOriginObjectId(excelMap.get(no).getId());
                                inputs.setOriginDeliverablesInfoId(deliverMap.get(excelMap.get(no).getId()+name));
                                list.add(inputs);
                                map3.put(name, name);
                            }
                        }
                    }
                    map2.put(no, no);
                }
            }
            if(!CommonUtil.isEmpty(list)) {
                resultMap.put(id, list);
            }
        }
        return resultMap;
    }

    @Override
    public List<Inputs> getInputsInfoByExcel(Map<String, String> map,
                                             Map<String, PlanExcelVo> excelMap, Map<String, String> deliverMap) {
        List<Inputs> list = new ArrayList<Inputs>();
        for(String id : map.keySet()) {
            String preposeNos = map.get(id);
            Map<String, String> map2 = new HashMap<String, String>();
            String[] nos = preposeNos.split(",");
            for(String no : nos) {
                if(!CommonUtil.isEmpty(no) && CommonUtil.isEmpty(map2.get(no))) {
                    PlanExcelVo excelVo = excelMap.get(no);
                    String deliverablesNames = excelVo.getDeliverablesName();
                    if(!CommonUtil.isEmpty(deliverablesNames)) {
                        String[] deliverables = deliverablesNames.split(",");
                        Map<String, String> map3 = new HashMap<String, String>();
                        for (String name : deliverables) {
                            name = name.trim();
                            if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map3.get(name))) {
                                Inputs inputs = new Inputs();
                                CommonUtil.glObjectSet(inputs);
                                inputs.setId(UUIDGenerator.generate().toString());
                                inputs.setName(name);
                                inputs.setUseObjectId(id);
                                inputs.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                inputs.setOriginType(PlanConstants.USEOBJECT_TYPE_PLAN);
                                inputs.setExt1("true");
                                inputs.setExt2("true");
                                inputs.setExt3("true");
                                inputs.setOriginObjectId(excelMap.get(no).getId());
                                inputs.setOriginDeliverablesInfoId(deliverMap.get(excelMap.get(no).getId()+name));
                                list.add(inputs);
                                map3.put(name, name);
                            }
                        }
                    }
                    map2.put(no, no);
                }
            }
        }
        return list;
    }

    @Override
    public Map saveInputsByObj(String workId, String queryType, String saveType, Object obj, TSUserDto userDto, String orgId) throws GWException {
        List<Inputs> inputsList = getInputsByUseObeject(queryType,workId);

        Class<?> clazz = obj.getClass();
        Map<String, Object> paraMap = new HashMap<String, Object>();

        if (!CommonUtil.isEmpty(inputsList)) {
            for (Inputs input : inputsList) {
                String id = "";
                String create = "";
                try {
                    Method idMethod = clazz.getMethod("getId");
                    Method createMethod = clazz.getMethod("getCreateBy");
                    id = (String)idMethod.invoke(obj);
                    create = (String)createMethod.invoke(obj);
                }
                catch (NoSuchMethodException | SecurityException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
                    log.warn(e.getMessage());
                    throw new GWException(e.getMessage());
                }

                Inputs inputs = new Inputs();
                inputs.setUseObjectType(saveType);
                inputs.setUseObjectId(id);
                inputs.setName(input.getName());
                CommonInitUtil.initGLObjectForCreate(inputs,userDto,orgId);
                inputs.setCreateBy(create);
                save(inputs);
                paraMap.put(input.getName(), input.getName());
            }
        }
        return paraMap;
    }
}
