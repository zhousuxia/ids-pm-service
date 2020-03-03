package com.glaway.ids.project.plantemplate.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.plantemplate.service.PreposePlanTemplateServiceI;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateDetailReq;
import com.glaway.ids.util.CommonInitUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.DateUtil;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.InputsServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.service.PlanTemplateDetailServiceI;

/**
 * 
 * 计划模板计划
 *
 */
@Service("planTemplateDetailService")
@Transactional(propagation = Propagation.REQUIRED)
public class PlanTemplateDetailServiceImpl extends BusinessObjectServiceImpl<PlanTemplateDetail> implements PlanTemplateDetailServiceI {
    
    /**
     * 项目管理计划服务
     */
    @Autowired
    private PlanServiceI planService;

    /**
     * 计划输出服务
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;


    @Autowired
    private SessionFacade sessionFacade;

    /**
     * 模板计划服务
     */
    @Autowired
    private PreposePlanTemplateServiceI preposePlanTemplateService;
    
    /**
     * 任务输入Service
     */
    @Autowired
    private InputsServiceI inputsService;

    @Value(value="${spring.application.name}")
    private String appKey;


    @Override
    public Map<String, String> getPlanTemplateOrProjTemplateDetailPreposes(String plantemplateId, String type) {
        String templateTableStr = " join pm_plan_template t on t.id = dl.plantemplateid";
        if(StringUtils.equalsIgnoreCase(type, "projecttemplate")){
            templateTableStr = " join PM_PROJ_TEMPLATE t on t.id = dl.projecttemplateid";
        }

        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select dl.id id, replace(wm_concat(dlp.name), ',', ',') name");
        hqlBuffer.append(" from pm_plan_template_dl dl");
        hqlBuffer.append(templateTableStr);
        hqlBuffer.append(" join PM_PREPOSE_PLAN_TEMPLATE p on dl.id = p.planid");
        hqlBuffer.append(" join pm_plan_template_dl dlp on dlp.id = p.preposeplanid");
        hqlBuffer.append(" where t.id = '" + plantemplateId + "'");
        hqlBuffer.append(" group by dl.id");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        Map<String, String> detailIdPreposeMap = new HashMap<String, String>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String name = StringUtils.isNotEmpty((String)map.get("name")) ? (String)map.get("name") : "";
                detailIdPreposeMap.put((String)map.get("id"), name);
            }
        }
        return detailIdPreposeMap;
    }

    @Override
    public Map<String, String> getPlanTemplateDetailPreposesId(String plantemplateId) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select dl.id id, replace(wm_concat(dlp.id), ',', ',') preposeId");
        hqlBuffer.append(" from pm_plan_template_dl dl");
        hqlBuffer.append(" join pm_plan_template t on t.id = dl.plantemplateid");
        hqlBuffer.append(" join PM_PREPOSE_PLAN_TEMPLATE p on dl.id = p.planid");
        hqlBuffer.append(" join pm_plan_template_dl dlp on dlp.id = p.preposeplanid");
        hqlBuffer.append(" where t.id = '" + plantemplateId + "'");
        hqlBuffer.append(" group by dl.id");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        Map<String, String> detailIdPreposeMap = new HashMap<String, String>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String preposeId = StringUtils.isNotEmpty((String)map.get("preposeId")) ? (String)map.get("preposeId") : "";
                detailIdPreposeMap.put((String)map.get("id"), preposeId);
            }
        }
        return detailIdPreposeMap;
    }

    @Override
    public List<PlanTemplateDetail> getPlanTemplateDetailList(String planTemplateId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from PlanTemplateDetail t");
        if (StringUtils.isNotEmpty(planTemplateId)) {
            hql.append(" where t.planTemplateId = '" + planTemplateId + "'");
        }
        hql.append(" order by t.planNumber");
        return sessionFacade.findByQueryString(hql.toString());

    }

    @Override
    public List<PlanTemplateDetail> getPlanTemplateDetailListByProjTemplateId(String projTemplateId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from PlanTemplateDetail t");
        if (StringUtils.isNotEmpty(projTemplateId)) {
            hql.append(" where t.projectTemplateId = '" + projTemplateId + "'");
        }
        hql.append(" order by t.planNumber");
        return sessionFacade.findByQueryString(hql.toString());
    }

    @Override
    public Map<String, String> getPlanTemplateOrProjTemplateDetailDeliverables(String plantemplateId, String type) {
        String templateTableStr = " join pm_plan_template t on t.id = dl.plantemplateid";
        String useobjecttype = "PLANTEMPLATE";
        if(StringUtils.equalsIgnoreCase(type, "projecttemplate")){
            templateTableStr = " join PM_PROJ_TEMPLATE t on t.id = dl.projecttemplateid";
            useobjecttype = CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE;
        }

        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select dl.id id,replace(wm_concat(d.name), ',', ',') name");
        hqlBuffer.append(" from PM_DELIVERABLES_INFO d");
        hqlBuffer.append(" join pm_plan_template_dl dl on dl.id = d.useobjectid");
        hqlBuffer.append(templateTableStr);
        hqlBuffer.append(" left join CM_DELIVERY_STANDARD st on st.name = d.name");
        hqlBuffer.append(" and st.stopflag = '启用'");
        hqlBuffer.append(" where t.id = '" + plantemplateId + "' and d.useobjecttype = '"+useobjecttype+"'");
        hqlBuffer.append(" group by dl.id");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        Map<String, String> detailIdDeliverablesMap = new HashMap<String, String>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String name = StringUtils.isNotEmpty((String)map.get("name")) ? (String)map.get("name") : "";
                detailIdDeliverablesMap.put((String)map.get("id"), name);
            }
        }
        return detailIdDeliverablesMap;
    }

    @Override
    public Map<String, String> getPlanTemplateDetailInputsName(List<Inputs> list) {
        Map<String, String> map = new HashMap<String, String>();
        if(!CommonUtil.isEmpty(list)){
            for(Inputs input : list){
                if(!CommonUtil.isEmpty(input.getName()) && !CommonUtil.isEmpty(input.getUseObjectId())) {
                    if(CommonUtil.isEmpty(map.get(input.getUseObjectId()))) {
                        map.put(input.getUseObjectId(), input.getName());
                    } else {
                        String inputName = map.get(input.getUseObjectId());
                        map.put(input.getUseObjectId(), inputName + "," + input.getName());
                    }
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getPlanTemplateDetailInputsOrigin(List<Inputs> list, List<PlanTemplateDetail> detailList, Map<String, String> detailIdDeliverablesMap) {
        Map<String, String> map = new HashMap<String, String>();
        if(!CommonUtil.isEmpty(list)) {
            for(Inputs input : list) {
                if(!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals(PlanConstants.LOCAL_EN)){
                    if(CommonUtil.isEmpty(map.get(input.getUseObjectId()))) {
                        map.put(input.getUseObjectId(), PlanConstants.LOCAL);
                    } else {
                        String origin = map.get(input.getUseObjectId());
                        map.put(input.getUseObjectId(), origin+","+PlanConstants.LOCAL);
                    }

                } else if(!CommonUtil.isEmpty(input.getOriginType()) && input.getOriginType().equals("PLANTEMPLATE")){
                    PlanTemplateDetail plan = new PlanTemplateDetail();
                    if(!CommonUtil.isEmpty(detailList)){
                        for(PlanTemplateDetail detail : detailList){
                            if(detail.getId().equals(input.getOriginObjectId())){
                                if(!CommonUtil.isEmpty(detailIdDeliverablesMap.get(detail.getId()))){
                                    String[] detailIdDeliverables = detailIdDeliverablesMap.get(detail.getId()).split(",");
                                    for(String d : detailIdDeliverables) {
                                        if(!CommonUtil.isEmpty(d) && d.equals(input.getName())){
                                            plan = detail;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(!CommonUtil.isEmpty(plan.getId())){
//                        input.setOriginPath(plan.getPlanNumber()+"."+plan.getName());
                        if(CommonUtil.isEmpty(map.get(input.getUseObjectId()))) {
                            map.put(input.getUseObjectId(), plan.getPlanNumber()+"."+plan.getName());
                        } else {
                            String origin = map.get(input.getUseObjectId());
                            map.put(input.getUseObjectId(), origin+","+plan.getPlanNumber()+"."+plan.getName());
                        }
                    } else {
                        if(CommonUtil.isEmpty(map.get(input.getUseObjectId()))) {
                            map.put(input.getUseObjectId(), "交付项名称库");
                        } else {
                            String origin = map.get(input.getUseObjectId());
                            map.put(input.getUseObjectId(), origin+",交付项名称库");
                        }
                    }
                } else {
                    if(CommonUtil.isEmpty(map.get(input.getUseObjectId()))) {
                        map.put(input.getUseObjectId(), "交付项名称库");
                    } else {
                        String origin = map.get(input.getUseObjectId());
                        map.put(input.getUseObjectId(), origin+",交付项名称库");
                    }
                }
            }
        }
        return map;
    }

    @Override
    public List<T> getList(PlanTemplateDetailReq planTemplateDetailRep, Integer pageSize, Integer pageNum) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from PlanTemplateDetail t");
        Map<String, Object> resultMap = getQueryParam(hql, planTemplateDetailRep);
        String hqlStr = (String)resultMap.get("hql");
        List<T> paramList = (List<T>)resultMap.get("queryList");
        List<T> list = new ArrayList<T>();
        if (pageSize != null) {
            list = sessionFacade.pageList(hqlStr, paramList.toArray(), (pageNum - 1) * pageSize, pageSize);
        }
        else {
            list = sessionFacade.executeQuery(hqlStr, paramList.toArray());
        }
        return list;
    }


    /**
     * Description: <br>
     * 组装查询方法<br>
     *
     * @param hql
     * @param planTemplateDetailRep
     * @return
     * @see
     */
    private Map<String, Object> getQueryParam(StringBuilder hql,
                                              PlanTemplateDetailReq planTemplateDetailRep) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        hql.append(" where t.avaliable!=0");

        List<Object> paramList = new ArrayList<Object>();
        if (planTemplateDetailRep != null) {
            if (StringUtils.isNotEmpty(planTemplateDetailRep.getPlanTemplateId())) {
                hql.append(" and t.planTemplateId=?");
                paramList.add(planTemplateDetailRep.getPlanTemplateId());
            }else if(!CommonUtil.isEmpty(planTemplateDetailRep)){
                hql.append(" and t.projectTemplateId=?");
                paramList.add(planTemplateDetailRep.getProjectTemplateId());
            }
        }
        hql.append(" order by t.createTime,t.parentPlanId,t.planNumber");
        //hql.append(" order by t.createTime");
        resultMap.put("hql", hql.toString());
        resultMap.put("queryList", paramList);
        return resultMap;
    }

    @Override
    public void savePlanTemplateDetailByPlan(String projectId, String planId, PlanTemplate planTemplate, TSUserDto userDto, String orgId) throws GWException {
        // 通过计划编号或项目编号获得计划中的计划名称、等级、阶段、工期,保存到计划模板中
        Plan planInfo = new Plan();
        List<Plan> planList = new ArrayList<Plan>();
        if (StringUtils.isNotEmpty(projectId)) {
            planList = planService.getPlanAllChildrenByProjectId(projectId);
        }
        if (StringUtils.isNotEmpty(planId)) {
            planInfo.setId(planId);
            planList = planService.getPlanAllChildren(planInfo);
        }

        // 如果获得的计划列表为空，返回
        if (CommonUtil.isEmpty(planList)) {
            return;
        }

        int i = 0;
        Map<String, Object> paraMap = new HashMap<String, Object>();
        //存放计划的MAP  key计划ID Value计划实体类
        Map<String, Object> paraMapPlan = new HashMap<String, Object>();
        for (Plan plan : planList) {
            PlanTemplateDetail detail = new PlanTemplateDetail();
            detail.setPlanTemplateId(planTemplate.getId());
            detail.setPlanNumber(plan.getPlanNumber());
            detail.setName(plan.getPlanName());
            detail.setMilestone(plan.getMilestone());
            detail.setPlanLevel(plan.getPlanLevel());
            detail.setWorkTime(plan.getWorkTime());
            detail.setStoreyNo(plan.getStoreyNo());

            // 如果有上级的ID，需要获得上级对应的记录的ID
            if (StringUtils.isNotEmpty(plan.getParentPlanId())
                    && !CommonUtil.mapIsEmpty(paraMap, plan.getParentPlanId())) {
                String parentId = (String)paraMap.get(plan.getParentPlanId());
                detail.setParentPlanId(parentId);
                if(plan.getParentPlan() != null){
                    if(StringUtils.isNotEmpty(plan.getWorkTime())&&StringUtils.isNotEmpty(plan.getParentPlan().getWorkTime())){
                        if(Integer.valueOf(plan.getWorkTime()) > Integer.valueOf(plan.getParentPlan().getWorkTime())){
                            throw new GWException("子计划【"+plan.getPlanName()+"】时间未收敛在父计划范围内,不能另存为模板");
                        }
                    }
                }

                //需求计划转为计划模板时候子父计划时间校验   子计划必须收敛于父计划中
                if(!CommonUtil.mapIsEmpty(paraMapPlan, plan.getParentPlanId()))
                {
                    Plan parentPlan=(Plan)paraMapPlan.get(plan.getParentPlanId());

                    String planStartTime=DateUtil.getStringFromDate(plan.getPlanStartTime(), DateUtil.YYYY_MM_DD);
                    String planEndTime=DateUtil.getStringFromDate(plan.getPlanEndTime(), DateUtil.YYYY_MM_DD);
                    String parentPlanStartTime=DateUtil.getStringFromDate(parentPlan.getPlanStartTime(), DateUtil.YYYY_MM_DD);
                    String parentPlanEndTime=DateUtil.getStringFromDate(parentPlan.getPlanEndTime(), DateUtil.YYYY_MM_DD);
                    //验证子计划的时间必须收敛于父计划中
                    if(!(
                            (DateUtil.dateCompare(parentPlan.getPlanStartTime(),plan.getPlanStartTime())||planStartTime.equals(parentPlanStartTime))
                                    &&
                                    (DateUtil.dateCompare(plan.getPlanEndTime(), parentPlan.getPlanEndTime())||planEndTime.equals(parentPlanEndTime))
                    ))
                    {
                        throw new GWException("子计划【"+plan.getPlanName()+"】时间未收敛在父计划范围内,不能另存为模板");
                    }

                }

            }
            detail.setNum(i++ );
            initBusinessObject(detail);
            CommonInitUtil.initGLObjectForCreate(detail,userDto,orgId);
            detail.setCreateBy(planTemplate.getCreateBy());
            sessionFacade.save(detail);
            paraMap.put(plan.getId(), detail.getId());

            paraMapPlan.put(plan.getId(), plan);
            deliverablesInfoService.saveDeliverableByObj(plan.getId(),
                    CommonConstants.DELIVERABLE_TYPE_PLAN,
                    CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE, detail,userDto,orgId);
        }
        for (Plan plan : planList) {
            preposePlanTemplateService.savePreposePlanTemplateByPlan(plan, paraMap,
                    planTemplate.getCreateBy(),userDto,orgId);
        }
    }

    @Override
    public List<PlanTemplateDetail> getDetailList(String planTemplateId) {
        List<PlanTemplateDetail> list = new ArrayList<PlanTemplateDetail>();
        StringBuilder hql = new StringBuilder("");
        hql.append("from PlanTemplateDetail t");
        hql.append(" where t.planTemplateId = '" + planTemplateId + "'");
        hql.append(" order by t.parentPlanId, t.planNumber");
        List<PlanTemplateDetail> dList = sessionFacade.findByQueryString(hql.toString());
        Map<String, PlanTemplateDetail> dMap = new HashMap<String, PlanTemplateDetail>();
        for(PlanTemplateDetail d : dList){
            dMap.put(d.getId(), d);
        }

        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select id,pathlength,planNumber from (");
        hqlBuffer.append(" select dl.id, dl.planNumber,");
        hqlBuffer.append(" length(sys_connect_by_path(id, ',')) pathlength");
        hqlBuffer.append(" from pm_plan_template_dl dl");
        hqlBuffer.append(" where dl.planTemplateId = '" + planTemplateId + "'");
        hqlBuffer.append(" start with dl.Parentplanid is null");
        hqlBuffer.append(" connect by dl.Parentplanid = prior dl.Id)");
        hqlBuffer.append(" order by pathlength, planNumber");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if(dMap.get(id) != null){
                    list.add(dMap.get(id));
                }
            }
        }
        return list;
    }

    @Override
    public Map<String, List<PlanTemplateDetail>> getDetailPreposes(String plantemplateId) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select dl.id id, replace(wm_concat(dlp.id), ',', ',') preposeid");
        hqlBuffer.append(" from pm_plan_template_dl dl");
        hqlBuffer.append(" join pm_plan_template t on t.id = dl.plantemplateid");
        hqlBuffer.append(" join PM_PREPOSE_PLAN_TEMPLATE p on dl.id = p.planid");
        hqlBuffer.append(" join pm_plan_template_dl dlp on dlp.id = p.preposeplanid");
        hqlBuffer.append(" where t.id = '" + plantemplateId + "'");
        hqlBuffer.append(" group by dl.id");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        Map<String, List<PlanTemplateDetail>> detailIdPreposeMap = new HashMap<String, List<PlanTemplateDetail>>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            List<PlanTemplateDetail> list = getPlanTemplateDetailList(plantemplateId);
            Map<String, PlanTemplateDetail> detailMap = new HashMap<String, PlanTemplateDetail>();
            for(PlanTemplateDetail detail : list){
                detailMap.put(detail.getId(), detail);
            }
            for (Map<String, Object> map : objArrayList) {
                List<PlanTemplateDetail> preposes = new ArrayList<PlanTemplateDetail>();
                String preposeid = StringUtils.isNotEmpty((String)map.get("preposeid")) ? (String)map.get("preposeid") : "";
                if(StringUtils.isNotEmpty(preposeid)){
                    String[] preposeidArr = preposeid.split(",");
                    for(String pid : preposeidArr){
                        if(detailMap.get(pid) != null){
                            preposes.add(detailMap.get(pid));
                        }
                    }
                    if(!CommonUtil.isEmpty(preposes)){
                        detailIdPreposeMap.put((String)map.get("id"), preposes);
                    }
                }
            }
        }
        return detailIdPreposeMap;
    }

    @Override
    public Map<String, List<PlanTemplateDetail>> getDetailPreposesByProjTemplateId(String projTemplateId) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select dl.id id, replace(wm_concat(dlp.id), ',', ',') preposeid");
        hqlBuffer.append(" from pm_plan_template_dl dl");
        hqlBuffer.append(" join PM_PROJ_TEMPLATE t on t.id = dl.projecttemplateid");
        hqlBuffer.append(" join PM_PREPOSE_PLAN_TEMPLATE p on dl.id = p.planid");
        hqlBuffer.append(" join pm_plan_template_dl dlp on dlp.id = p.preposeplanid");
        hqlBuffer.append(" where t.id = '" + projTemplateId + "'");
        hqlBuffer.append(" group by dl.id");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        Map<String, List<PlanTemplateDetail>> detailIdPreposeMap = new HashMap<String, List<PlanTemplateDetail>>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            List<PlanTemplateDetail> list = getPlanTemplateDetailListByProjTemplateId(projTemplateId);
            Map<String, PlanTemplateDetail> detailMap = new HashMap<String, PlanTemplateDetail>();
            for(PlanTemplateDetail detail : list){
                detailMap.put(detail.getId(), detail);
            }
            for (Map<String, Object> map : objArrayList) {
                List<PlanTemplateDetail> preposes = new ArrayList<PlanTemplateDetail>();
                String preposeid = StringUtils.isNotEmpty((String)map.get("preposeid")) ? (String)map.get("preposeid") : "";
                if(StringUtils.isNotEmpty(preposeid)){
                    String[] preposeidArr = preposeid.split(",");
                    for(String pid : preposeidArr){
                        if(detailMap.get(pid) != null){
                            preposes.add(detailMap.get(pid));
                        }
                    }
                    if(!CommonUtil.isEmpty(preposes)){
                        detailIdPreposeMap.put((String)map.get("id"), preposes);
                    }
                }
            }
        }
        return detailIdPreposeMap;
    }

    @Override
    public List<String> getPlanAllChildren(List<String> list, String planId, List<Plan> detailList) {
        if(!CommonUtil.isEmpty(planId)) {
            list.add(planId);
            Plan conditionPlan = new Plan();
            conditionPlan.setParentPlanId(planId);
            List<Plan> childrenPlan = queryPlanList(conditionPlan, detailList);
            if (!CommonUtil.isEmpty(childrenPlan)) {
                for (Plan condition : childrenPlan) {
                    getPlanAllChildren(list, condition.getId(), detailList);
                }
            }
        }
        return list;
    }

    private List<Plan> queryPlanList(Plan conditionPlan, List<Plan> detailList) {
        List<Plan> list = new ArrayList<Plan>();
        for(Plan plan : detailList) {
            if(conditionPlan.getParentPlanId().equals(plan.getParentPlanId())) {
                list.add(plan);
            }
        }
        return list;
    }
}
