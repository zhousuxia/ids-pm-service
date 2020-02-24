package com.glaway.ids.project.projecttemplate.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.BeanUtil;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.DateUtil;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.InputsServiceI;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.entity.PreposePlanTemplate;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.util.CommonInitUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.plantemplate.service.PlanTemplateDetailServiceI;
import com.glaway.ids.project.plantemplate.service.PreposePlanTemplateServiceI;
import com.glaway.ids.project.plantemplate.service.impl.PlanTemplateDetailServiceImpl;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplateDetail;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateDetailServiceI;


@Service("ProjTemplateDetailService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProjTemplateDetailServiceImpl extends BusinessObjectServiceImpl<ProjTemplateDetail> implements ProjTemplateDetailServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(PlanTemplateDetailServiceImpl.class);

    @Autowired
    private PlanServiceI planService;
    
    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;

    
    @Autowired
    private PlanTemplateDetailServiceI planTemplateDetailService;

    @Autowired
    private PreposePlanTemplateServiceI preposePlanTemplateService;

    @Autowired
    private ProjLibServiceI projLibService;

    @Autowired
    private InputsServiceI inputsService;

    @Override
    public List<Plan> convertPlanjTemplateDetail2Plan(String projTemplateId) {
        List<PlanTemplateDetail> detailList = queryProjTmplPlan(projTemplateId);
        List<Plan> plans = new ArrayList<Plan>();
        if(!CommonUtil.isEmpty(detailList)) {
            for(PlanTemplateDetail d : detailList) {
                Plan plan = new Plan();
                convertDeatilToPlan(d, plan);
                plans.add(plan);
            }
        }
        return plans;
    }

    @Override
    public List<PlanTemplateDetail> queryProjTmplPlan(String projTemplateId) {
        List<PlanTemplateDetail> details = sessionFacade.executeQuery("from PlanTemplateDetail where projectTemplateId=?", new Object[]{projTemplateId});
        Map<String, String> detailIdDeliverablesMap = planTemplateDetailService.getPlanTemplateOrProjTemplateDetailDeliverables(projTemplateId, "projecttemplate");
        Map<String, String> detailIdPreposeMap = planTemplateDetailService.getPlanTemplateOrProjTemplateDetailPreposes(projTemplateId, "projecttemplate");
        List<Inputs> inputs = sessionFacade.findHql("from Inputs where useObjectId in(select id from PlanTemplateDetail where projectTemplateId = ?)", projTemplateId);
        Map<String, String> inputsNameMap = planTemplateDetailService.getPlanTemplateDetailInputsName(inputs);
        Map<String, String> inputsOriginMap = planTemplateDetailService.getPlanTemplateDetailInputsOrigin(inputs, details, detailIdDeliverablesMap);
        List<PlanTemplateDetail> planTemplateDetailInfoList = new ArrayList<PlanTemplateDetail>();
        if(!CommonUtil.isEmpty(details)) {
            for (PlanTemplateDetail obj : details) {
                PlanTemplateDetail planTemplateDetailInfo = new PlanTemplateDetail();
                try {
                    BeanUtil.copyBeanNotNull2Bean(obj, planTemplateDetailInfo);
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               /* if (StringUtils.isNotEmpty(detailIdDeliverablesMap.get(obj.getId()))) {
                    planTemplateDetailInfo.setDeliverablesName(detailIdDeliverablesMap.get(obj.getId()));
                }*/
                if (StringUtils.isNotEmpty(detailIdPreposeMap.get(obj.getId()))) {
                    planTemplateDetailInfo.setPreposeNames(detailIdPreposeMap.get(obj.getId()));
                }
                if(!CommonUtil.isEmpty(inputsNameMap.get(obj.getId()))) {
                    planTemplateDetailInfo.setInputsName(inputsNameMap.get(obj.getId()));
                }
                if(!CommonUtil.isEmpty(inputsOriginMap.get(obj.getId()))) {
                    planTemplateDetailInfo.setOrigin(inputsOriginMap.get(obj.getId()));
                }
                planTemplateDetailInfoList.add(planTemplateDetailInfo);
            }
        }

        return details;
    }

    private void convertDeatilToPlan(PlanTemplateDetail d, Plan plan) {
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        deliverablesInfo.setUseObjectId(d.getId());
        deliverablesInfo.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE);
        List<DeliverablesInfo> deliverablesInfoList2 = deliverablesInfoService.queryDeliverableList(
                deliverablesInfo, 1, 10, false);
        PreposePlanTemplate preposePlan = null;

        List<Inputs> inputs = sessionFacade.findHql("from Inputs where useObjectId = ? ", d.getId());
        List<PreposePlanTemplate> preposeList = sessionFacade.findHql("from PreposePlanTemplate where avaliable='1' and  planId=?", d.getId());
        if(!CommonUtil.isEmpty(preposeList) && preposeList.size() > 0) {
            preposePlan = preposeList.get(0);
        }
        plan.setId(d.getId());
        plan.setPlanName(d.getName());
        plan.setMilestone(d.getMilestone());
        plan.setPlanOrder(String.valueOf(d.getNum()));
        plan.setParentPlanId(d.getParentPlanId());
        plan.setPlanLevel(d.getPlanLevel());
        plan.setPlanNumber(d.getPlanNumber());
        plan.setStoreyNo(d.getStoreyNo());
        plan.setProjectId(d.getProjectTemplateId());
        plan.setWorkTime(d.getWorkTime());
        plan.setRemark(d.getRemark());
        plan.setDeliInfoList(deliverablesInfoList2);
        plan.setInputList(inputs);
        if(!CommonUtil.isEmpty(preposePlan)) {
            plan.setPreposeIds(preposePlan.getPreposePlanId());
        }
    }

    @Override
    public boolean isFileNameRepeat(String templateId, String fileName) {
        Boolean b = false;
        List<RepFileDto> folderTree = projLibService.getFolderTree(templateId,"","");
        if(StringUtils.isBlank(fileName)){ return false; }
        if(!CommonUtil.isEmpty(folderTree)) {
            for (RepFileDto file : folderTree)
            {
                if(fileName.equals(file.getFileName())) {
                    b = true;
                }
            }
        }
        return b;
    }

    @Override
    public void saveProjTemplateDetailByPlan(String projectId, String planId, ProjTemplate projTemplate, TSUserDto userDto, String orgId) throws GWException {
        List<Plan> planList = new ArrayList<Plan>();
        if(StringUtils.isNotEmpty(projectId)){
            planList = planService.getPlanAllChildrenByProjectId(projectId);
        }

        // 如果获得的计划列表为空，返回
        if (CommonUtil.isEmpty(planList)) {
            return;
        }

        int i = 0;
        PlanTemplateDetail planTemplateDetail = new PlanTemplateDetail();
        CommonInitUtil.initGLObjectForCreate(planTemplateDetail,userDto,orgId);
        planTemplateDetailService.initBusinessObject(planTemplateDetail);
        String bizCurrent = planTemplateDetail.getBizCurrent();
        String bizId = planTemplateDetail.getBizId();
        LifeCyclePolicy policy = planTemplateDetail.getPolicy();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        Map<String, Object> paraMapPlan = new HashMap<String, Object>();

        for (Plan plan : planList) {
            PlanTemplateDetail detail = new PlanTemplateDetail();
            CommonInitUtil.initGLObjectForCreate(detail,userDto,orgId);
            detail.setProjectTemplateId(projTemplate.getId());
            detail.setName(plan.getPlanName());
            detail.setMilestone(plan.getMilestone());
            detail.setPlanLevel(plan.getPlanLevel());
            detail.setWorkTime(plan.getWorkTime());
            detail.setPlanNumber(plan.getPlanNumber());
            detail.setStoreyNo(plan.getStoreyNo());
            detail.setRemark(plan.getRemark());
            detail.setTaskNameType(plan.getTaskNameType());
            detail.setTabCbTemplateId(plan.getTabCbTemplateId());
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

                    String planStartTime= DateUtil.getStringFromDate(plan.getPlanStartTime(), DateUtil.YYYY_MM_DD);
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
            detail.setBizCurrent(bizCurrent);
            detail.setBizId(bizId);
            detail.setPolicy(policy);
            detail.setCreateBy(projTemplate.getCreateBy());
            detail.setCreateTime(new Date());
            sessionFacade.save(detail);
            paraMap.put(plan.getId(), detail.getId());
            paraMapPlan.put(plan.getId(), plan);
            deliverablesInfoService.saveDeliverableByObj(plan.getId(),
                    CommonConstants.DELIVERABLE_TYPE_PLAN,
                    CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE, detail,userDto,orgId);
            inputsService.saveInputsByObj(plan.getId(),
                    CommonConstants.DELIVERABLE_TYPE_PLAN,
                    CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE, detail,userDto,orgId);
        }
        for (Plan plan : planList) {
            preposePlanTemplateService.savePreposePlanTemplateByPlan(plan, paraMap, projTemplate.getCreateBy(),userDto,orgId);
        }
    }
}
