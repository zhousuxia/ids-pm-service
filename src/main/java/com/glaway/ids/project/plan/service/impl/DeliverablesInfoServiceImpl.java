package com.glaway.ids.project.plan.service.impl;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.outwardextension.OutwardExtensionDto;
import com.glaway.foundation.fdk.dev.dto.outwardextension.OutwardExtensionUrlDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.service.FeignOutwardExtensionService;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.foundation.system.lifecycle.service.LifeCycleStatusServiceI;
import com.glaway.ids.config.constant.ConfigStateConstants;
import com.glaway.ids.config.constant.NameStandardSwitchConstants;
import com.glaway.ids.config.constant.SwitchConstants;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.models.HttpClientUtil;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.DeliveryStandard;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.*;
import com.glaway.ids.project.plan.vo.PlanExcelVo;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import com.glaway.ids.project.projectmanager.entity.ProjDocRelation;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjDocVo;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.Dto2Entity;
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
import java.util.*;


/**
 * 交付物信息
 * 
 * @author blcao
 * @version 2015年4月1日
 * @see DeliverablesInfoServiceImpl
 * @since
 */
@Service("deliverablesInfoService")
@Transactional(propagation = Propagation.REQUIRED)
public class DeliverablesInfoServiceImpl extends BusinessObjectServiceImpl<DeliverablesInfo> implements DeliverablesInfoServiceI {

    private static final SystemLog log = BaseLogFactory.getSystemLog(DeliverablesInfoServiceImpl.class);


    @Autowired
    private PlanBusinessConfigServiceI businessConfigService;

    @Autowired
    private ParamSwitchServiceI paramSwitchService;

    @Autowired
    private ProjLibServiceI projLibService;

    @Autowired
    private PlanServiceI planService;


    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private DeliveryStandardRemoteFeignServiceI deliverablesInfoService;

    @Autowired
    private NameStandardRemoteFeignServiceI nameStandardService;

    @Autowired
    private NameStandardFeignService nameStandardFeignService;
    /**
     * 项目计划管理接口
     */
    @Autowired
    private LifeCycleStatusServiceI lifeCycleStatusService;

    @Autowired
    private FeignOutwardExtensionService outwardExtensionService;

    /**
     * 接口
     */
    @Value(value="${spring.application.name}")
    private String appKey;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    
    /**
     * repFile接口
     */
    @Autowired
    private FeignRepService repFileService;


    @Override
    public List<DeliverablesInfo> queryDeliverableList(DeliverablesInfo deliverablesInfo, int page, int rows, boolean isPage) {
        String hql = createHql(deliverablesInfo);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }

    @Override
    public long getCount(DeliverablesInfo deliverablesInfo) {
        String hql = "select count(*) " + createHql(deliverablesInfo);
        return sessionFacade.getCount(hql, null);
    }

    @Override
    public void deleteDeliverablesById(String id) {
        List<DeliverablesInfo> approvePlanInfoList = sessionFacade.findByQueryString(creatChangeDeleteHql(id));
        for (DeliverablesInfo approvePlanInfo : approvePlanInfoList) {
            sessionFacade.delete(approvePlanInfo);
        }
    }

    @Override
    public String initDeliverablesInfo(DeliverablesInfo deliverablesInfo) {
        initBusinessObject(deliverablesInfo);
        String json = JSON.toJSONString(deliverablesInfo);
        return json;
    }

    @Override
    public void saveDeliverablesInfo(DeliverablesInfo deliverablesInfo) {
        initBusinessObject(deliverablesInfo);
        sessionFacade.save(deliverablesInfo);
    }

    @Override
    public List<DeliverablesInfo> getDeliverablesByUseObeject(String useObjectType, String useObjectId) {
        String hql = "from DeliverablesInfo l where l.useObjectType = '" + useObjectType
                + "' and l.useObjectId = '" + useObjectId + "'";
        return sessionFacade.findByQueryString(hql);
    }

    @Override
    public List<DeliverablesInfo> getDeliverablesByObjTypeAndId(String useObjectType, String useObjectId) {
        List<DeliverablesInfo> deliverablesInfos = new ArrayList<>();
        StringBuffer sb = new StringBuffer("select * from PM_DELIVERABLES_INFO t where 1=1");
        if (StringUtils.isNotBlank(useObjectType)) {
            sb.append(" and t.useobjecttype = '" + useObjectType + "'");
        }
        if (StringUtils.isNotBlank(useObjectId)) {
            sb.append(" and t.useobjectid = '" + useObjectId + "'");
        }
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sb.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
                LifeCyclePolicy newPolicy = new LifeCyclePolicy();
                deliverablesInfo.setId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                deliverablesInfo.setCreateBy(StringUtils.isNotEmpty((String)map.get("CREATEBY")) ? (String)map.get("CREATEBY") : "");
                deliverablesInfo.setCreateFullName(StringUtils.isNotEmpty((String)map.get("CREATEFULLNAME")) ? (String)map.get("CREATEFULLNAME") : "");
                deliverablesInfo.setCreateName(StringUtils.isNotEmpty((String)map.get("CREATENAME")) ? (String)map.get("CREATENAME") : "");
                deliverablesInfo.setUpdateBy(StringUtils.isNotEmpty((String)map.get("UPDATEBY")) ? (String)map.get("UPDATEBY") : "");
                deliverablesInfo.setUpdateFullName(StringUtils.isNotEmpty((String)map.get("UPDATEFULLNAME")) ? (String)map.get("UPDATEFULLNAME") : "");
                deliverablesInfo.setUpdateName(StringUtils.isNotEmpty((String)map.get("UPDATENAME")) ? (String)map.get("UPDATENAME") : "");
                deliverablesInfo.setFirstBy(StringUtils.isNotEmpty((String)map.get("FIRSTBY")) ? (String)map.get("FIRSTBY") : "");
                deliverablesInfo.setFirstFullName(StringUtils.isNotEmpty((String)map.get("FIRSTFULLNAME")) ? (String)map.get("FIRSTFULLNAME") : "");
                deliverablesInfo.setFirstName(StringUtils.isNotEmpty((String)map.get("FIRSTNAME")) ? (String)map.get("FIRSTNAME") : "");
                newPolicy.setId(StringUtils.isNotEmpty((String)map.get("POLICY_ID")) ? (String)map.get("POLICY_ID") : "");
                deliverablesInfo.setPolicy(newPolicy);
                deliverablesInfo.setBizCurrent(StringUtils.isNotEmpty((String)map.get("BIZCURRENT")) ? (String)map.get("BIZCURRENT") : "");
                deliverablesInfo.setBizId(StringUtils.isNotEmpty((String)map.get("BIZID")) ? (String)map.get("BIZID") : "");
                deliverablesInfo.setBizVersion(StringUtils.isNotEmpty((String)map.get("BIZVERSION")) ? (String)map.get("BIZVERSION") : "");
                deliverablesInfo.setSecurityLevel(map.get("SECURITYLEVEL") == null ? 0 : Short.valueOf(map.get("SECURITYLEVEL").toString()));
                deliverablesInfo.setAvaliable(StringUtils.isNotEmpty((String)map.get("AVALIABLE")) ? (String)map.get("AVALIABLE") : "");
                deliverablesInfo.setUseObjectId(StringUtils.isNotEmpty((String)map.get("USEOBJECTID")) ? (String)map.get("USEOBJECTID") : "");
                deliverablesInfo.setUseObjectType(StringUtils.isNotEmpty((String)map.get("USEOBJECTTYPE")) ? (String)map.get("USEOBJECTTYPE") : "");
                deliverablesInfo.setName(StringUtils.isNotEmpty((String)map.get("NAME")) ? (String)map.get("NAME") : "");
                deliverablesInfo.setOrigin(StringUtils.isNotEmpty((String)map.get("ORIGIN")) ? (String)map.get("ORIGIN") : "");
                deliverablesInfo.setRequired(StringUtils.isNotEmpty((String)map.get("REQUIRED")) ? (String)map.get("REQUIRED") : "");
                deliverablesInfos.add(deliverablesInfo);
            }
        }
        return deliverablesInfos;
    }

    /**
     * 组装HQL
     *
     * @return
     * @see
     */
    private String creatChangeDeleteHql(String id) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from DeliverablesInfo t");
        hql.append(" where 1 = 1");

        if (id != null) {
            if (StringUtils.isNotEmpty(id)) {
                hql.append(" and t.id = '" + id + "'");
            }
        }
        return hql.toString();
    }

    /**
     * 根据Plan中的传值拼接HQL
     *
     * @return
     * @see
     */
    private String createHql(DeliverablesInfo deliverablesInfo) {
        String hql = "from DeliverablesInfo l where  1 = 1 ";
        // 计划名称
        if (!StringUtils.isEmpty(deliverablesInfo.getUseObjectId())) {
            hql = hql + " and l.useObjectId = '" + deliverablesInfo.getUseObjectId() + "'";
        }
        if (!StringUtils.isEmpty(deliverablesInfo.getUseObjectType())) {
            hql = hql + " and l.useObjectType = '" + deliverablesInfo.getUseObjectType() + "'";
        }
        // 计划名称
        if (!StringUtils.isEmpty(deliverablesInfo.getName())) {
            hql = hql + " and l.name = '" + deliverablesInfo.getName() + "'";
        }
        // 是否可用
        if (!StringUtils.isEmpty(deliverablesInfo.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + deliverablesInfo.getAvaliable() + "'";
        }
        return hql;
    }

    /**
     * 删除计划相关交付物（物理删除）
     *
     * @param obj
     * @return
     * @see
     */
    @Override
    public void deleteDeliverablesPhysicsByPlan(Object obj) {
        if (obj instanceof Plan) {
            Plan plan = (Plan)obj;
            List<DeliverablesInfo> list = getDeliverablesByUseObeject("PLAN", plan.getId());
            for (DeliverablesInfo deliverablesInfo : list) {
                sessionFacade.deleteEntityById(DeliverablesInfo.class, deliverablesInfo.getId());
            }
        }
    }

    @Override
    public String getLifeCycleStatusList() {
        Plan p = new Plan();
        initBusinessObject(p);
        List<LifeCycleStatus> lifeCycleStatus = p.getPolicy().getLifeCycleStatusList();
        List<LifeCycleStatus> orderList = new ArrayList<LifeCycleStatus>();
        for (int i = 0; i < lifeCycleStatus.size(); i++ ) {
            for (LifeCycleStatus status : lifeCycleStatus) {
                if (status.getOrderNum() == i) {
                    orderList.add(status);
                    break;
                }
            }
        }
    
        String aString = JsonUtil.getListJsonWithoutQuote(orderList);
        return aString;
    
    }

    @Override
    public List<DeliverablesInfo> getPlanDeliverablesByDetailAndName(Plan plan, String switchStr,
                                                                     String detailDeliverables,
                                                                     String nameDeliverables,
                                                                     String dBizCurrent,
                                                                     String dBizVersion,
                                                                     LifeCyclePolicy dPolicy) {
        List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
        // 如果是可以修改的或强制启用名称的，只导入系统的交付项
        if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
            if (StringUtils.isNotEmpty(nameDeliverables)) {
                Map<String, String> map = new HashMap<String, String>();
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (StringUtils.isNotEmpty(name) && StringUtils.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        info.setId(UUIDGenerator.generate().toString());
                        info.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        info.setUseObjectId(plan.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        list.add(info);
                        map.put(name, name);
                    }
                }
            }
        }
        else if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)) {

            Map<String, String> map = new HashMap<String, String>();
            if (StringUtils.isNotEmpty(detailDeliverables)) {
                String[] deliverables = detailDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (StringUtils.isNotEmpty(name) && StringUtils.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        info.setId(UUIDGenerator.generate().toString());
                        info.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        info.setUseObjectId(plan.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        list.add(info);
                        map.put(name, name);
                    }
                }
            }

            if (StringUtils.isNotEmpty(nameDeliverables)) {
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (StringUtils.isNotEmpty(name) && StringUtils.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        info.setId(UUIDGenerator.generate().toString());
                        info.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        info.setUseObjectId(plan.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        list.add(info);
                        map.put(name, name);
                    }
                }
            }
        }
        else {
            // 其它的只导入自己的交付项
            if (StringUtils.isNotEmpty(nameDeliverables)) {
                Map<String, String> map = new HashMap<String, String>();
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (StringUtils.isNotEmpty(name) && StringUtils.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        info.setId(UUIDGenerator.generate().toString());
                        info.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        info.setUseObjectId(plan.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        list.add(info);
                        map.put(name, name);
                    }
                }
            }

        }
        return list;
    }
    
    @Override
    public DeliverablesInfo getDeliverablesInfoEntity(String id) {
        DeliverablesInfo d = (DeliverablesInfo)sessionFacade.getEntity(DeliverablesInfo.class, id);
        return d;
    }
    
    /**
     * 删除计划的输入、输出、删除计划输入相关的输入
     * 
     * @param planId
     */
    @Override
    public void deleteDeliverablesByPlanId(String planId) {
        sessionFacade.executeSql2("delete from PM_INPUTS where originDeliverablesInfoId "
                                  + "in (select id from PM_DELIVERABLES_INFO "
                                  + "where useobjectid = '" + planId + "')");
        sessionFacade.executeSql2("delete from PM_DELIVERABLES_INFO where useobjectid = '"
                                  + planId + "'");
        sessionFacade.executeSql2("delete from PM_INPUTS where useobjectid = '" + planId + "'");
    }
    
    /**
     * 获取输入增加时所选择的前置计划的输出
     * preposeIds 前置计划ids
     */
    @Override
    public List<DeliverablesInfo> getPreposePlanDeliverables(String preposeIds) {
        String hql = "select d.id id,d.name name ,d.fileid fileid,d.origin origin,"
                     + "d.required required,d.docId docId,d.docname docname,"
                     + "d.useobjectid useobjectid,d.useobjecttype useobjecttype,"
                     + "p.planname useobjectName " + "from PM_DELIVERABLES_INFO d,pm_plan p "
                     + "where d.useObjectId in (" + this.changeToContainsemicolon(preposeIds)
                     + ") and d.useObjectType = '" + PlanConstants.USEOBJECT_TYPE_PLAN
                     + "' and d.useObjectId = p.id";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<DeliverablesInfo> list = changeResultsToDeliverablesList(objArrayList);
        return list;
    }
    
    /**
     * 将传入的str每个ID拼接单引号
     * 
     * @return
     */
    private String changeToContainsemicolon(String str) {
        if (StringUtils.isNotEmpty(str)) {
            str = "'" + str.replace(",", "','") + "'";
        }
        return str;
    }
    
    /**
     * 将查询的结果转换为List<DeliverablesInfo>
     * 
     * @param objArrayList
     * @return
     */
    private List<DeliverablesInfo> changeResultsToDeliverablesList(List<Map<String, Object>> objArrayList) {
        List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    DeliverablesInfo deliver = new DeliverablesInfo();
                    deliver.setId(id);
                    deliver.setName(StringUtils.isNotEmpty((String)map.get("name")) ? (String)map.get("name") : "");
                    deliver.setFileId(StringUtils.isNotEmpty((String)map.get("fileid")) ? (String)map.get("fileid") : "");
                    deliver.setOrigin(StringUtils.isNotEmpty((String)map.get("origin")) ? (String)map.get("origin") : "");
                    deliver.setRequired(StringUtils.isNotEmpty((String)map.get("required")) ? (String)map.get("required") : "");
                    deliver.setDocId(StringUtils.isNotEmpty((String)map.get("docId")) ? (String)map.get("docId") : "");
                    deliver.setDocName(StringUtils.isNotEmpty((String)map.get("docname")) ? (String)map.get("docname") : "");
                    deliver.setUseObjectId(StringUtils.isNotEmpty((String)map.get("useobjectid")) ? (String)map.get("useobjectid") : "");
                    deliver.setUseObjectType(StringUtils.isNotEmpty((String)map.get("useobjecttype")) ? (String)map.get("useobjecttype") : "");
                    deliver.setUseObjectName(StringUtils.isNotEmpty((String)map.get("useobjectName")) ? (String)map.get("useobjectName") : "");
                    list.add(deliver);
                }
            }
        }
        return list;
    }
    
    /**
     * 获取输入增加时所选择的前置计划的输出信息，包括其所关联的文档信息
     * ids 输出的ids
     */
    @Override
    public List<DeliverablesInfo> getSelectedPreposePlanDeliverables(String ids) {
        String hql = " select d.id id, d.name name," + " d.fileid fileid, d.origin origin,"
                     + " d.required required, r.docId docId,"
                     + " t.filename docname, d.useobjectid useobjectid,"
                     + " d.useobjecttype useobjecttype, p.planname useobjectName"
                     + " from PM_DELIVERABLES_INFO d" + " join pm_plan p on d.useObjectId = p.id"
                     + " left join PM_PROJ_DOC_RELATION r on d.id = r.quoteid"
                     + " left join rep_file t on t.id = r.docid" + " where d.id in ("
                     + this.changeToContainsemicolon(ids) + ") and d.useObjectType = '"
                     + PlanConstants.USEOBJECT_TYPE_PLAN + "'";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<DeliverablesInfo> list = changeResultsToDeliverablesList(objArrayList);
        return list;
    }
    
    /**
     * 根据parentId查找相关交付物
     * 
     * @param parentId
     * @return
     * @see
     */
    @Override
    public List<DeliverablesInfo> getAllDeliverablesByUseObeject(String parentId) {
        StringBuilder hqlBuffer = new StringBuilder("");
        hqlBuffer.append(" SELECT D.ID            ID, ");
        hqlBuffer.append(" D.NAME          NAME, ");
        hqlBuffer.append(" D.USEOBJECTID   USEOBJECTID, ");
        hqlBuffer.append(" D.USEOBJECTTYPE USEOBJECTTYPE, p.CELLID ");
        hqlBuffer.append(" FROM PM_DELIVERABLES_INFO D ");
        hqlBuffer.append(" LEFT JOIN PM_PLAN P ON P.ID = D.USEOBJECTID ");
        hqlBuffer.append(" WHERE D.USEOBJECTTYPE = 'PLAN' ");
        hqlBuffer.append(" and p.avaliable='1' ");
        hqlBuffer.append(" AND P.Parentplanid in '" + parentId + "' ");
        
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hqlBuffer.toString());
        List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                DeliverablesInfo d = new DeliverablesInfo();
                d.setId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                d.setName(StringUtils.isNotEmpty((String)map.get("NAME")) ? (String)map.get("NAME") : "");
                d.setUseObjectId(StringUtils.isNotEmpty((String)map.get("USEOBJECTID")) ? (String)map.get("USEOBJECTID") : "");
                d.setUseObjectType(StringUtils.isNotEmpty((String)map.get("USEOBJECTTYPE")) ? (String)map.get("USEOBJECTTYPE") : "");
                if (StringUtils.isNotEmpty(d.getId()) && StringUtils.isNotEmpty(d.getName())
                    && StringUtils.isNotEmpty(d.getUseObjectId())) {
                    list.add(d);
                }
            }
        }
        return list;
    }
    
    /**
     * 获取交付物判断的字段
     * 
     * @return
     */
    @Override
    public Integer getJudgePlanAllDocumantWithStatus(Plan plan,String isOut) {
        Integer planFeedbackFlag = null;

        Plan newPlan = new Plan();
        newPlan.setParentPlanId(plan.getId());
        List<Plan> list = planService.queryPlansExceptInvalid(newPlan);
        long planCount = list.size();

        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        deliverablesInfo.setUseObjectId(plan.getId());
        deliverablesInfo.setAvaliable("1");
        List<DeliverablesInfo> deliverablesList = queryDeliverableList(deliverablesInfo, 1, 10, false);
        
    /*    //如果要判断计划完工与交付项状态关联的话加入下列判断，可以与下面的判断是否有交付物合并
        // 如果是叶子节点的底层任务并且有交付项
        if (planCount == 0 && deliverablesList != null && deliverablesList.size() > 0) {
            for (DeliverablesInfo childDeli : deliverablesList) {
                //判断交付物的状态，如果有一个状态不是最终状态，就break出去， planFeedbackFlag = 4; 
            }
        }
        else if (planCount > 0 && (deliverablesList == null || deliverablesList.size() <= 0)) {
         // 有子但本身无交付项
        }
        else if (planCount > 0 && deliverablesList != null && deliverablesList.size() > 0) {
            List<Plan> childList = planService.queryPlansExceptInvalid(newPlan);
          //拿到所有的子计划
            for (Plan child : childList) {
                DeliverablesInfo deliverablesInfoTwo = new DeliverablesInfo();
                deliverablesInfoTwo.setUseObjectId(child.getId());
                deliverablesInfoTwo.setAvaliable("1");
                //拿到每个子计划的交付物
                List<DeliverablesInfo> deliverablesTwoList = queryDeliverableList(
                    deliverablesInfoTwo, 1, 10, false);
                planFeedbackFlag = 3; // 有子，本身有交付项。子全部挂接成功
                if (deliverablesTwoList != null && deliverablesTwoList.size() > 0) { // 子计划无交付项
                    for (DeliverablesInfo childDeli : deliverablesList) {
                      //判断交付物的状态，如果有一个状态不是最终状态，就break出去， planFeedbackFlag = 4; 
                    }
                }
            }
        }*/
        if("1".equals(isOut)){
            planFeedbackFlag = 0; 
            String initStr = "";
            //TODO..
//            List<OutwardExtension> outwardExtensionList = outwardExtensionService.getOutwardExtensionList("flowActiveCategoryHttpServer");
//            if (!CommonUtil.isEmpty(outwardExtensionList)) {
//                for (OutwardExtension ext : outwardExtensionList) {
//                    if (!CommonUtil.isEmpty(ext.getUrlList())) {
//                        for (OutwardExtensionUrl out : ext.getUrlList()) {
//                            if ("checkTaskStepStatus".equals(out.getOperateCode())) {
//                                initStr = out.getOperateUrl() + "&planId=" + plan.getId();
//                            }
//                        }
//                    }
//
//                }
//                try {
//                    JsonResult json = HttpClientUtil.httpClientPostByTest(initStr, null);
//                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转化为特定格式
//                    .setPrettyPrinting() // 对json结果格式化
//                    .create();
//                    String jsonRsp = gson.toJson(json);
//                    List<JsonResult> list2 = com.alibaba.fastjson.JSONObject.parseArray("["
//                                                                                      + jsonRsp
//                                                                                      + "]",
//                                                                                      JsonResult.class);
//                    if (!CommonUtil.isEmpty(list2)) {
//                        JsonResult j = list2.get(0);
//                        if (!j.isSuccess()) {
//                            planFeedbackFlag = 4;
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }else{
         // 如果是叶子节点的底层任务并且有交付项
            if (planCount == 0 && deliverablesList != null && deliverablesList.size() > 0) {
                planFeedbackFlag = 0; // 无子有交付项全部挂接
                for (DeliverablesInfo childDeli : deliverablesList) {
                    if (StringUtils.isEmpty(childDeli.getFileId())) {
                        planFeedbackFlag = 1; // 无子有交付项没有全部挂接
                    }else{
                        /*RepFile r = getEntity(RepFile.class, childDeli.getFileId());
                        if(!CommonUtil.isEmpty(r)){
                            if(!"guidang".equals(r.getBizCurrent())){
                                planFeedbackFlag = 4; 
                                break;
                            }
                        }*/
                    }
                }
            }
            else if (planCount > 0 && (deliverablesList == null || deliverablesList.size() <= 0)) {
                planFeedbackFlag = 2; // 有子但本身无交付项
            }
            else if (planCount > 0 && deliverablesList != null && deliverablesList.size() > 0) {
                List<Plan> childList = planService.queryPlansExceptInvalid(newPlan);
                for (Plan child : childList) {
                    DeliverablesInfo deliverablesInfoTwo = new DeliverablesInfo();
                    deliverablesInfoTwo.setUseObjectId(child.getId());
                    deliverablesInfoTwo.setAvaliable("1");
                    List<DeliverablesInfo> deliverablesTwoList = queryDeliverableList(deliverablesInfoTwo, 1, 10, false);
                    planFeedbackFlag = 3; // 有子，本身有交付项。子全部挂接成功
                    if (deliverablesTwoList != null && deliverablesTwoList.size() > 0) { // 子计划无交付项
                        for (DeliverablesInfo childDeli : deliverablesList) {
                            if (StringUtils.isEmpty(childDeli.getFileId())) {
                                planFeedbackFlag = 4; // 有子，本身有交付项。子交付项没有全部挂接
                            }else{
                              /*  RepFile r = getEntity(RepFile.class, childDeli.getFileId());
                                if(!CommonUtil.isEmpty(r)){
                                    if("guidang".equals(r.getBizCurrent())){
                                        planFeedbackFlag = 4; 
                                    }
                                }*/
                            }
                        }
                    }
                    else {
                        planFeedbackFlag = 4; // 有子，本身有交付项。子无交付项
                    }
                }
            }
        }
        return planFeedbackFlag;
    }

    /**
     * 获取子计划的名称为name的交付物的信息
     * ids 输出的ids
     */
    @Override
    public List<DeliverablesInfo> getDeliverablesByParentAndName(String parentId, String name) {
        String hql = " select d.id id, d.name name," + " d.fileid fileid, d.origin origin,"
                + " d.required required, r.docId docId,"
                + " t.filename docname, d.useobjectid useobjectid,"
                + " d.useobjecttype useobjecttype, p.planname useobjectName"
                + " from PM_DELIVERABLES_INFO d"
                + " join pm_plan p on (d.useObjectId = p.id and p.parentplanid = '"
                + parentId + "')" + " join PM_PROJ_DOC_RELATION r on d.id = r.quoteid"
                + " join rep_file t on t.id = r.docid" + " where d.name = '" + name + "'";

        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql);
        List<DeliverablesInfo> list = changeResultsToDeliverablesList(objArrayList);
        return list;
    }

    @Override
    public FeignJson listView(Map<String, Object> map) {
        FeignJson j = new FeignJson();
        ObjectMapper mapper = new ObjectMapper();
        DeliverablesInfoDto deliverablesInfoDto = mapper.convertValue(map.get("DeliverablesInfoDto"),DeliverablesInfoDto.class);
        PlanDto planDto = mapper.convertValue(map.get("PlanDto"),PlanDto.class);
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        Plan plan = new Plan();
        Dto2Entity.copyProperties(deliverablesInfoDto,deliverablesInfo);
        Dto2Entity.copyProperties(planDto,plan);
        String userId = map.get("userId").toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().setVersion(
                1.0).create();
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.deliverables.listViewSuccess");
        if (deliverablesInfo != null
                && StringUtils.isNotEmpty(deliverablesInfo.getUseObjectId())
                && StringUtils.isNotEmpty(deliverablesInfo.getUseObjectType())) {
            plan = planService.getEntity(deliverablesInfo.getUseObjectId());
        }
        List<ProjDocVo> projDocRelationList = getDocRelationList(plan, userId);
        if (projDocRelationList == null) {
            projDocRelationList = new ArrayList<ProjDocVo>();
        }
        String json = gson.toJson(projDocRelationList);
        j.setObj(json);
        return j;
    }

    /**
     * 项目计划页面初始化时获取项目库
     *
     * @param plan
     * @see
     */
    public List<ProjDocVo> getDocRelationList(Plan plan, String userId) {
        // 通过交付项判断子计划是否包括项目库
        // 获取计划的输出
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        deliverablesInfo.setUseObjectId(plan.getId());
        deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
        List<DeliverablesInfo> deliverablesList = queryDeliverableList(
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
            List<DeliverablesInfo> childDeliverables = queryDeliverableList(
                    deliverablesInfo, 1, 10, false);
            for (DeliverablesInfo childDeli : childDeliverables) {
                List<ProjDocRelation> projDocRelationList = projLibService.getDocRelationList(childDeli.getId());
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
                List<ProjDocRelation> projDocRelationDbList = projLibService.getDocRelationList(parentDeli.getId());
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
        if (StringUtils.isEmpty(projDocRelation.getDocId())) {
            return projDocVo;
        }
        projDocVo.setDocId(projDocRelation.getDocId());
        String havePower = planService.getOutPowerForPlanList(projDocRelation.getDocId(), planId, userId);
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
        if (projDocRelation.getRepFile() == null && !CommonUtil.isEmpty(projDocRelation.getDocId())) {
            RepFileDto rep = repFileService.getRepFileByRepFileId(appKey, projDocRelation.getDocId());  
            projDocRelation.setRepFile(rep);
        }
        projDocVo.setDocName(projDocRelation.getRepFile().getFileName());
        projDocVo.setVersion(projDocRelation.getRepFile().getBizVersion());
        String approveStatus = lifeCycleStatusService.getTitleByPolicyIdAndName(
                projDocRelation.getRepFile().getPolicy().getId(),
                projDocRelation.getRepFile().getBizCurrent());
        projDocVo.setStatus(approveStatus);
        projDocVo.setSecurityLevel(projDocRelation.getRepFile().getSecurityLevel());
        return projDocVo;
    }

    @Override
    public Map<String, ProjDocRelation> queryFinishDeliverable(String planId,
                                                      Map<String, ProjDocRelation> deliverablesMap) {
        Plan childPlan = new Plan();
        childPlan.setParentPlanId(planId);
        List<Plan> childList = planService.queryPlanList(childPlan, 1, 10, false);
        if (CommonUtil.isEmpty(childList)) {
            return deliverablesMap;
        }
        for (Plan child : childList) {
            DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
            deliverablesInfo.setUseObjectId(child.getId());
            deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            List<DeliverablesInfo> childDeliverables = queryDeliverableList(deliverablesInfo, 1,
                    10, false);
            if (CommonUtil.isEmpty(childDeliverables)) {
                return deliverablesMap;
            }
            deliverablesMap = queryFinishDeliverable(child.getId(), deliverablesMap);
            for (DeliverablesInfo childDeli : childDeliverables) {
                List<ProjDocRelation> ProjDocRelationList = projLibService.getDocRelationList(childDeli.getId());
                if (ProjDocRelationList == null || ProjDocRelationList.size() < 1) {
                    continue;
                }
                ProjDocRelation relation = (ProjDocRelation)deliverablesMap.get(childDeli.getName());
                if (relation == null) {
                    deliverablesMap.put(childDeli.getName(), ProjDocRelationList.get(0));
                }
            }
        }
        return deliverablesMap;
    }

    @Override
    public List<DeliverablesInfo> getDeliverablesByProject(String projectId) {
        StringBuilder hqlBuffer = new StringBuilder("");
        hqlBuffer.append(" SELECT D.ID            ID, ");
        hqlBuffer.append(" D.NAME          NAME, ");
        hqlBuffer.append(" D.USEOBJECTID   USEOBJECTID, ");
        hqlBuffer.append(" D.USEOBJECTTYPE USEOBJECTTYPE ");
        hqlBuffer.append(" FROM PM_DELIVERABLES_INFO D ");
        hqlBuffer.append(" LEFT JOIN PM_PLAN P ON P.ID = D.USEOBJECTID ");
        hqlBuffer.append(" WHERE D.USEOBJECTTYPE = 'PLAN' ");
        hqlBuffer.append(" AND P.PROJECTID = '" + projectId + "' ");
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hqlBuffer.toString());
        List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                DeliverablesInfo d = new DeliverablesInfo();
                d.setId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                d.setName(StringUtils.isNotEmpty((String)map.get("NAME")) ? (String)map.get("NAME") : "");
                d.setUseObjectId(StringUtils.isNotEmpty((String)map.get("USEOBJECTID")) ? (String)map.get("USEOBJECTID") : "");
                d.setUseObjectType(StringUtils.isNotEmpty((String)map.get("USEOBJECTTYPE")) ? (String)map.get("USEOBJECTTYPE") : "");
                if (StringUtils.isNotEmpty(d.getId()) && StringUtils.isNotEmpty(d.getName())
                        && StringUtils.isNotEmpty(d.getUseObjectId())) {
                    list.add(d);
                }
            }
        }
        return list;
    }

    @Override
    public FeignJson updateDeliverablesInfo(DeliverablesInfo deliverablesInfo) {
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.deliverables.updateSuccess");
        String failMessage = "";
        String failMessageCode = "";
        try {
            DeliverablesInfo deliverablesInfoTemp = getDeliverablesInfoEntity(deliverablesInfo.getId());
            deliverablesInfoTemp.setFileId(deliverablesInfo.getFileId());
            updateDelAndProjLib(deliverablesInfoTemp);
            log.info(message, deliverablesInfo.getId(), deliverablesInfo.getId().toString());
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.deliverables.updateFail");
            j.setSuccess(false);
            log.error(failMessage, e, "", deliverablesInfo.getId().toString());
            Object[] params = new Object[] {failMessage, deliverablesInfo.getId().toString()};// 异常原因：{0}；详细信息：{1}
            throw new GWException(failMessageCode, params, e);
        }
        finally {
            // systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson updateDeliverablesInfoByPlm(DeliverablesInfo deliverablesInfo) {
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.deliverables.updateSuccess");
        String failMessage = "";
        String failMessageCode = "";
        try {
            DeliverablesInfo deliverablesInfoTemp = getDeliverablesInfoEntity(deliverablesInfo.getId());
            deliverablesInfoTemp.setFileId(deliverablesInfo.getFileId());
            deliverablesInfoTemp.setDocId(deliverablesInfo.getDocId());
            deliverablesInfoTemp.setDocName(deliverablesInfo.getDocName());
            deliverablesInfoTemp.setFileType(deliverablesInfo.getFileType());
            deliverablesInfoTemp.setOrginType(deliverablesInfo.getOrginType());
            deliverablesInfoTemp.setVersionCode(deliverablesInfo.getVersionCode());
            deliverablesInfoTemp.setStatusCode(deliverablesInfo.getStatusCode());
            // 更新数据
            sessionFacade.updateEntitie(deliverablesInfoTemp);
            List<ProjDocRelation> repFileList = projLibService.getDocRelationList(deliverablesInfo.getId());
            if (!CommonUtil.isEmpty(repFileList)) {
                for (ProjDocRelation projDocRelation : repFileList) {
                    RepFileDto repFile = projDocRelation.getRepFile();
                    if (repFile != null) {
                        projLibService.delDocRelation(deliverablesInfo.getId(), repFile.getId());
                    }
                }
                projLibService.delDocRelation(deliverablesInfo.getId());
            }
            log.info(message, deliverablesInfo.getId(), deliverablesInfo.getId().toString());
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.deliverables.updateFail");
            j.setSuccess(false);
            log.error(failMessage, e, "", deliverablesInfo.getId().toString());
            Object[] params = new Object[] {failMessage, deliverablesInfo.getId().toString()};// 异常原因：{0}；详细信息：{1}
            throw new GWException(failMessageCode, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public void updateDelAndProjLib(DeliverablesInfo deliverablesInfo) {
        // 更新数据
        sessionFacade.updateEntitie(deliverablesInfo);
        List<ProjDocRelation> repFileList = projLibService.getDocRelationList(deliverablesInfo.getId());
        if (!CommonUtil.isEmpty(repFileList)) {
            for (ProjDocRelation projDocRelation : repFileList) {
                RepFileDto repFile = projDocRelation.getRepFile();
                if (repFile != null) {
                    projLibService.delDocRelation(deliverablesInfo.getId(), repFile.getId());
                }
            }
            projLibService.delDocRelation(deliverablesInfo.getId());
        }
        if (StringUtil.isNotEmpty(deliverablesInfo.getFileId())) {
            String fileId = deliverablesInfo.getFileId();
            if (fileId.indexOf("<br/>") != -1) {
                fileId = fileId.substring(0, fileId.indexOf("<br/>"));
            }
            projLibService.addDocRelation(deliverablesInfo.getId(), fileId);


            List<String> httpUrls = new ArrayList<String>();
            List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"taskExcuteCategoryHttpServer");
            if (!CommonUtil.isEmpty(outwardExtensionList)) {
                for (OutwardExtensionDto ext : outwardExtensionList) {
                    for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                        if ("addInputUpDoc".equals(out.getOperateCode())) {
                            httpUrls.add(out.getOperateUrl());
                        }
                    }
                }
            }
            if(!CommonUtil.isEmpty(httpUrls)){
                Map<String, String> paramMap = new HashMap<String, String>();
                Plan plan = planService.getEntity(deliverablesInfo.getUseObjectId());
                List<DeliveryStandardDto> deliveryStandardList = sessionFacade.findHql("from DeliveryStandard where name = ? ", deliverablesInfo.getName());
                String deliveryId = "";
                if(!CommonUtil.isEmpty(deliveryStandardList)){
                    deliveryId = deliveryStandardList.get(0).getId();
                }
                if(!CommonUtil.isEmpty(plan)){
                    paramMap.put("parentPlanId", plan.getParentPlanId());
                    paramMap.put("planId", plan.getId());
                    paramMap.put("deliveryId", deliveryId);
                    paramMap.put("fileId", fileId);
                    for(String url : httpUrls){
                        try {
                            HttpClientUtil.httpClientPostByTest(url, paramMap);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            //更新计划进度
            Plan plan = planService.getEntity(deliverablesInfo.getUseObjectId());
            if (!CommonUtil.isEmpty(plan)) {
                planService.updateProgress(plan);
            }
        }
        else {
            throw new GWException("交付项不能为空");
        }
    }

    @Override
    public List<DeliverablesInfo> getDeliverablesInfoByPlanTemplateExcel(PlanTemplateExcelVo vo, String switchStr, String dBizCurrent, String dBizVersion, LifeCyclePolicy dPolicy,
                                                                         Map<String, String> deliverMap, String nameDeliverables, TSUserDto userDto, String orgId) {
        List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
        String detailDeliverables = vo.getDeliverablesName();
        // 如果是可以修改的或强制启用名称的，只导入系统的交付项
        if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
            if (!CommonUtil.isEmpty(nameDeliverables)) {
                Map<String, String> map = new HashMap<String, String>();
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
//                        String id = UUIDGenerator.generate().toString();
//                        info.setId(id);
                        CommonInitUtil.initGLObjectForCreate(info,userDto,orgId);
                        info.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                        info.setUseObjectId(vo.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        map.put(name, name);
                        sessionFacade.save(info);
                        list.add(info);
                        deliverMap.put(vo.getId()+name, info.getId());
                    }
                }
            }
        }
        else if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)) {

            Map<String, String> map = new HashMap<String, String>();
            if (!CommonUtil.isEmpty(detailDeliverables)) {
                String[] deliverables = detailDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
//                        String id = UUIDGenerator.generate().toString();
//                        info.setId(id);
                        CommonInitUtil.initGLObjectForCreate(info,userDto,orgId);
                        info.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                        info.setUseObjectId(vo.getId());
                        info.setName(name);
                        //info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        sessionFacade.save(info);
                        list.add(info);
                        map.put(name, name);
                        deliverMap.put(vo.getId()+name, info.getId());
                    }
                }
            }

            if (!CommonUtil.isEmpty(nameDeliverables)) {
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
//                        String id = UUIDGenerator.generate().toString();
//                        info.setId(id);
                        CommonInitUtil.initGLObjectForCreate(info,userDto,orgId);
                        info.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                        info.setUseObjectId(vo.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        sessionFacade.save(info);
                        list.add(info);
                        map.put(name, name);
                        deliverMap.put(vo.getId()+name, info.getId());
                    }
                }
            }
        }
        else {
            // 其它的只导入自己的交付项
            if (!CommonUtil.isEmpty(nameDeliverables)) {
                Map<String, String> map = new HashMap<String, String>();
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
//                        String id = UUIDGenerator.generate().toString();
//                        info.setId(id);
                        CommonInitUtil.initGLObjectForCreate(info,userDto,orgId);
                        info.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE);
                        info.setUseObjectId(vo.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        sessionFacade.save(info);
                        list.add(info);
                        map.put(name, name);
                        deliverMap.put(vo.getId()+name, info.getId());
                    }
                }
            }

        }
        return list;
    }

    @Override
    public List<DeliverablesInfo> queryDeliverablesInfoByTemplateId(String planTemplateId) {
        List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
        if(!CommonUtil.isEmpty(planTemplateId)) {
            String hql = "from DeliverablesInfo where useObjectId in(select id from PlanTemplateDetail where planTemplateId = ?)";
            list = sessionFacade.findHql(hql, planTemplateId);
        }
        return list;
    }

    @Override
    public Map saveDeliverableByObj(String workId, String queryType, String saveType, Object obj, TSUserDto userDto, String orgId) throws GWException {
        // 通过计划编号获得相应的交付项，保存到交付项中
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        deliverablesInfo.setUseObjectId(workId);
        deliverablesInfo.setUseObjectType(queryType);
        List<DeliverablesInfo> deliverablesList = queryDeliverableList(deliverablesInfo, 1, 10,
                false);

        Class<?> clazz = obj.getClass();
        Map<String, Object> paraMap = new HashMap<String, Object>();

        for (DeliverablesInfo infoDb : deliverablesList) {
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

            DeliverablesInfo info = new DeliverablesInfo();
            info.setUseObjectType(saveType);
            info.setUseObjectId(id);
            info.setName(infoDb.getName());

            // 保存到交付项表
            initBusinessObject(info);
            CommonInitUtil.initGLObjectForCreate(info,userDto,orgId);
            info.setCreateBy(create);
            sessionFacade.save(info);
            paraMap.put(infoDb.getName(), infoDb.getName());
        }
        return paraMap;
    }

    @Override
    public List<DeliverablesInfo> queryDeliverablesInfoByPlanIds(String planIds) {
        List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
        String hql = "from DeliverablesInfo where useObjectId in('" + planIds + ")";
        list = sessionFacade.findByQueryString(hql);
        return list;
    }

    @Override
    public List<DeliverablesInfo> getDeliverablesInfoByExcel(PlanExcelVo vo,
                                                             String switchStr, String dBizCurrent, String dBizVersion,
                                                             LifeCyclePolicy dPolicy, Map<String, String> deliverMap,
                                                             String nameDeliverables) {
        List<DeliverablesInfo> list = new ArrayList<DeliverablesInfo>();
        String detailDeliverables = vo.getDeliverablesName();
        // 如果是可以修改的或强制启用名称的，只导入系统的交付项
        if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
            if (!CommonUtil.isEmpty(nameDeliverables)) {
                Map<String, String> map = new HashMap<String, String>();
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        String id = UUIDGenerator.generate().toString();
                        info.setId(id);
                        info.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        info.setUseObjectId(vo.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        list.add(info);
                        map.put(name, name);
                        deliverMap.put(vo.getId()+name, id);
                    }
                }
            }
        }
        else if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)) {

            Map<String, String> map = new HashMap<String, String>();
            if (!CommonUtil.isEmpty(detailDeliverables)) {
                String[] deliverables = detailDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        String id = UUIDGenerator.generate().toString();
                        info.setId(id);
                        info.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        info.setUseObjectId(vo.getId());
                        info.setName(name);
                        //info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        list.add(info);
                        map.put(name, name);
                        deliverMap.put(vo.getId()+name, id);
                    }
                }
            }

            if (!CommonUtil.isEmpty(nameDeliverables)) {
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        String id = UUIDGenerator.generate().toString();
                        info.setId(id);
                        info.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        info.setUseObjectId(vo.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        list.add(info);
                        map.put(name, name);
                        deliverMap.put(vo.getId()+name, id);
                    }
                }
            }
        }
        else {
            // 其它的只导入自己的交付项
            if (!CommonUtil.isEmpty(nameDeliverables)) {
                Map<String, String> map = new HashMap<String, String>();
                String[] deliverables = nameDeliverables.split(",");
                for (String name : deliverables) {
                    name = name.trim();
                    if (!CommonUtil.isEmpty(name) && CommonUtil.isEmpty(map.get(name))) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        String id = UUIDGenerator.generate().toString();
                        info.setId(id);
                        info.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        info.setUseObjectId(vo.getId());
                        info.setName(name);
                        info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        list.add(info);
                        map.put(name, name);
                        deliverMap.put(vo.getId()+name, id);
                    }
                }
            }

        }
        return list;
    }

    @Override
    public List<DeliveryStandard> searchDeliveryStandardAccurate(DeliveryStandard dson) {
        List<DeliveryStandard> list = sessionFacade.findByQueryString(createHql(dson));
        return list;
    }

    private String createHql(DeliveryStandard deliveryStandard)
    {
        String hql = "from DeliveryStandard l where 1 = 1 ";
        if (StringUtils.isNotEmpty(deliveryStandard.getNo()))
        {
            hql = hql + " and l.no like '%" + deliveryStandard.getNo() + "%'";
        }
        if (StringUtils.isNotEmpty(deliveryStandard.getName()))
        {
            hql = hql + " and l.name like '%" + deliveryStandard.getName() + "%'";
        }
        if (StringUtils.isNotEmpty(deliveryStandard.getAvaliable()))
        {
            hql = hql + " and l.avaliable = '" + deliveryStandard.getAvaliable() + "'";
        }
        if (StringUtils.isNotEmpty(deliveryStandard.getStopFlag()))
        {
            hql = hql + " and l.stopFlag = '" + deliveryStandard.getStopFlag() + "'";
        }
        hql = hql + " order by l.no asc";
        return hql;
    }

    @Override
    public void saveDeliverableByPlan(String workId, String queryType, String saveType, Plan plan, String lineNo, TSUserDto userDto, String orgId) throws GWException {
        saveDeliverableByPara(workId, queryType, saveType, plan.getId(), plan.getPlanName(),
                plan.getDocuments(), plan, lineNo,userDto,orgId);
    }

    /**
     * Description: <br>
     * 保存交付项<br>
     * Implement: <br>
     * 1、如果启用名称库
     * 名称在名称库时，需要把名称库对应的交付项都导入，并把交付项存在于交付库中的都导入，如果有不在交付库中的名称需要报出异常;如果名称不在名称库时,把交付项存在于交付库中的都导入
     * ，如果有不在交付库中的名称需要报出异常<br>
     * 2、如果强制启用名称 名称在名称库时，需要把名称库对应的交付项都导入，并把交付项存在于交付库中的都导入，如果有不在交付库中的名称需要报出异常;如果名称不在名称库时 需要报出异常<br>
     *
     * @param workId
     *            查询交付项用的编号
     * @param queryType
     *            查询交付项类型
     * @param saveType
     *            保存交付项类型
     * @param id
     *            保存交付项用的编号
     * @param name
     *            保存计划名称
     * @param documents
     *            保存交付项名称
     * @param obj
     *            保存对象
     * @param obj
     *            行号
     * @see
     */
    @SuppressWarnings("unchecked")
    private void saveDeliverableByPara(String workId, String queryType, String saveType,
                                       String id, String name, String documents, Object obj,
                                       String lineNo,TSUserDto userDto,String orgId) {
        /**
         * 此处可以讲switchStr直接作为一个参数传递进来，没有必要再方法里面再次查询，防止该方法被重复调用
         */
        String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
        // 如果是可以修改的或强制启用名称的，只导入系统的交付项
        if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap = savePersonDeliverable(id, saveType, documents, lineNo,userDto,orgId);
            saveSystemDeliverable(saveType, id, name, new HashMap<String, Object>(), true,userDto,orgId);
        }
        else if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)) { // 如果是启用名称库导入系统的交付项，并导入自己的交付项
            Map<String, Object> paraMap = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(workId)) {
                paraMap = saveDeliverableByObj(workId, queryType, saveType, obj,userDto,orgId);
            }
            else {
                paraMap = savePersonDeliverable(id, saveType, documents, lineNo,userDto,orgId);
            }
            // 如果计划名称在系统中存在，导入系统中的计划名称
            saveSystemDeliverable(saveType, id, name, paraMap, false,userDto,orgId);
        }
        else { // 其它的只导入自己的交付项
            if (StringUtils.isNotEmpty(workId)) {
                saveDeliverableByObj(workId, queryType, saveType, obj,userDto,orgId);
            }
            else {
                saveDeliverable(id, saveType, documents,userDto,orgId);
            }

        }
    }

    /**
     * Description: <br>
     * 保存存在交付库中的交付项<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param id
     * @param saveType
     * @param documents
     * @param lineNo
     * @see
     */
    private Map<String, Object> savePersonDeliverable(String id, String saveType,
                                                      String documents, String lineNo,TSUserDto userDto,String orgId)
            throws GWException {
        Map<String, Object> paraMap = new HashMap<String, Object>();

        // 判断交付项名称是否存在,如果存在保存交付项名称
        if (StringUtils.isEmpty(documents)) {
            return paraMap;
        }

        for (String document : documents.split(",")) {
            DeliveryStandardDto documentInfo = new DeliveryStandardDto();
            documentInfo.setName(document);
            List<DeliveryStandardDto> deliveryStandardDtoList = deliverablesInfoService.getDeliveryStandardsByDetailNames(documentInfo);
            List<DeliveryStandard> deliveryStandardList = JSON.parseArray(JSON.toJSONString(deliveryStandardDtoList),DeliveryStandard.class);
            String stopFlag = "";
            if (deliveryStandardList != null && deliveryStandardList.size() > 0) {
                DeliveryStandard deliveryStandard = deliveryStandardList.get(0);
                stopFlag = deliveryStandard.getStopFlag();
                if ("禁用".equals(stopFlag)) {
                    Object[] arguments = new String[] {document};
                    String message = I18nUtil.getValue(
                            "com.glaway.ids.pm.project.plan.deliverables.savePersonDeliverableOne",
                            arguments);
                    if (StringUtils.isNotEmpty(lineNo)) {
                        message = "第" + lineNo + "行" + message;
                    }
                    log.warn(message);
                    throw new GWException(message);
                }
                // 用到计划模板详细的ID
                DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
                deliverablesInfo.setName(deliveryStandard.getName());
                deliverablesInfo.setUseObjectType(saveType);
                deliverablesInfo.setUseObjectId(id);

                // 保存到交付项表
                CommonInitUtil.initGLObjectForCreate(deliverablesInfo,userDto,orgId);
                initBusinessObject(deliverablesInfo);
                sessionFacade.save(deliverablesInfo);
                paraMap.put(document, document);
            }
            else {
                Object[] arguments = new String[] {document};
                String message = I18nUtil.getValue(
                        "com.glaway.ids.pm.project.plan.deliverables.savePersonDeliverableTwo", arguments);
                if (StringUtils.isNotEmpty(lineNo)) {
                    message = "第" + lineNo + "行" + message;
                }
                log.warn(message);
                throw new GWException(message);
            }
        }
        return paraMap;
    }


    /**
     *  Description: <br>
     * 保存系统的交付项<br>
     * Implement: <br>
     * 保存交付项名称是否已存在<br>
     *
     * @param id
     * @param saveType
     * @param name
     * @param paraMap
     * @param flag
     *            判断是否需要返回不存在系统中的计划名称的异常
     * @see
     */
    private void saveSystemDeliverable(String saveType, String id, String name,
                                       Map<String, Object> paraMap, Boolean flag,TSUserDto userDto,String orgId) {
        // 如果系统中不存在计划名称，不可以导入
        NameStandardDto condition = new NameStandardDto();
        condition.setName(name);
        condition.setStopFlag(ConfigStateConstants.START);
        /**
         * 标准名称库查询由于多个地方被重复调用，需要做缓存
         */
        List<NameStandardDto> nameStandardList = nameStandardService.searchNameStandardsAccurate(condition);
        if (nameStandardList == null || nameStandardList.size() <= 0) {
            if (flag) {
                log.warn("计划名称【" + name + "】不在名称库中或已被禁用，请先联系名称库管理员后再重新上传导入!");
                Object[] arguments = new String[] {name};
                throw new GWException(I18nUtil.getValue(
                        "com.glaway.ids.pm.project.plan.deliverables.saveSystemDeliverable", arguments));
            }
            else {
                return;
            }
        }
        NameStandardDto nameStandard = nameStandardList.get(0);
        List<NameStandardDeliveryRelationDto> nameStandardDeliveryRelationList = new ArrayList<NameStandardDeliveryRelationDto>();
        if ("启用".equals(nameStandard.getStopFlag())) {
            NameStandardDeliveryRelationDto relation = new NameStandardDeliveryRelationDto();
            relation.setNameStandardId(nameStandard.getId());
            nameStandardDeliveryRelationList = nameStandardFeignService.searchNoPage(relation);
        }
        if (nameStandardDeliveryRelationList == null
                || nameStandardDeliveryRelationList.size() <= 0) {
            log.info("没有可以导入的交付项");
            return;
        }
        for (NameStandardDeliveryRelationDto nameStandardDeliveryRelation : nameStandardDeliveryRelationList) {
            DeliveryStandardDto deliveryStandard = nameStandardDeliveryRelation.getDeliveryStandard();
            if (nameStandardDeliveryRelation.getDeliveryStandardId() != null) {
                if (deliveryStandard != null && "启用".equals(deliveryStandard.getStopFlag())) {
                    if (!CommonUtil.mapIsEmpty(paraMap, deliveryStandard.getName())) { // 如果此名称已存在，不需要保存
                        log.info("交付项名称【" + name + "】已存在");
                        continue;
                    }
                    DeliverablesInfo info = new DeliverablesInfo();
                    info.setUseObjectType(saveType);
                    info.setUseObjectId(id);
                    info.setName(deliveryStandard.getName());
                    info.setOrigin(PlanConstants.DELIVERABLES_ORIGIN_NAMESTANDARD);
                    // 保存到交付项表
                    initBusinessObject(info);
                    CommonInitUtil.initGLObjectForCreate(info,userDto,orgId);
                    sessionFacade.save(info);
                }
            }
        }
    }

    /**
     * Description: <br>
     * 保存个人的交付项<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param id
     * @param saveType
     * @param documents
     * @see
     */
    private void saveDeliverable(String id, String saveType, String documents,TSUserDto userDto,String orgId) {
        // 判断交付项名称是否存在,如果存在保存交付项名称
        if (StringUtils.isEmpty(documents)) {
            return;
        }
        for (String document : documents.split(",")) {
            // 用到计划模板详细的ID
            DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
            deliverablesInfo.setName(document);
            deliverablesInfo.setUseObjectType(saveType);
            deliverablesInfo.setUseObjectId(id);

            // 保存到交付项表
            CommonInitUtil.initGLObjectForCreate(deliverablesInfo,userDto,orgId);
            initBusinessObject(deliverablesInfo);
            sessionFacade.save(deliverablesInfo);
        }
    }

    @Override
    public List<LifeCycleStatus> getDeliveryLifeCycleStatus() {
        DeliverablesInfo d = new DeliverablesInfo();
        initBusinessObject(d);
        List<LifeCycleStatus> lifeCycleStatus = d.getPolicy().getLifeCycleStatusList();
        List<LifeCycleStatus> orderList = new ArrayList<LifeCycleStatus>();
        for (int i = 0; i < lifeCycleStatus.size(); i++ ) {
            for (LifeCycleStatus status : lifeCycleStatus) {
                if (status.getOrderNum() == i) {
                    orderList.add(status);
                    break;
                }
            }
        }
        return orderList;
    }
}
