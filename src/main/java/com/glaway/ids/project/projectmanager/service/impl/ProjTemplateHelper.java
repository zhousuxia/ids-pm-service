/*
 * 文件名：ProjTemplateConvertHelper.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：wangshen
 * 修改时间：2015年5月7日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.FdTeamRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.service.calendar.FeignCalendarService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignTeamService;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.service.InputsServiceI;
import com.glaway.ids.util.CommonInitUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.DateUtil;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.constant.ProjectRoleConstants;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.plan.service.PreposePlanServiceI;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.entity.PreposePlanTemplate;
import com.glaway.ids.project.plantemplate.service.PlanTemplateDetailServiceI;
import com.glaway.ids.project.plantemplate.service.PreposePlanTemplateServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateDetailServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateLibServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateRoleServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;

@Service("projTemplateHelper")
public class ProjTemplateHelper {
    
    @Autowired
    private PlanServiceI planService;
    
    @Autowired
    private ProjectServiceI projectService;
    
    @Autowired
    private ProjRoleServiceI projRoleService;
    
    @Autowired
    private ProjTemplateRoleServiceI projTemplateRoleService;
    
    @Autowired
    private ProjTemplateDetailServiceI projTemplateDetailService;
    
    @Autowired
    private PlanTemplateDetailServiceI planTemplateDetailService;
    
  /*  @Autowired
    private PreposeProjTemplateServiceI preposeProjTemplateService;*/
    
    @Autowired
    private PreposePlanTemplateServiceI preposePlanTemplateService;
    
    @Autowired
    private ProjTemplateServiceI projTemplateService;
    
    @Autowired
    private ProjTemplateLibServiceI projTemplateLibService;
    
    @Autowired
    private FeignTeamService teamService;
    
    @Autowired
    private ProjLibServiceI projLibService;
    
    @Autowired
    private FeignCalendarService calendarService;


    @Value(value="${spring.application.name}")
    private String appKey;

    @Autowired
    private SessionFacade sessionFacade;
    
    /**
     * 交付物信息
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;

    @Autowired
    private InputsServiceI inputsService;
    
    /**
     * 计划前置表
     */
    @Autowired
    private PreposePlanServiceI preposePlanService;
    
    public void saveProjRoles(String projectId, String templateId,String teamId) {
        ProjTemplate template = projTemplateService.getProjEntity(templateId);
        if (template != null) {
            String templateTeamId = projTemplateRoleService.getTeamIdByTemplateId(templateId);
            List<FdTeamRoleDto> roles = teamService.getRoleByTeamId(appKey,templateTeamId);
            for (FdTeamRoleDto role : roles) {
                if (!StringUtils.equals(role.getRoleCode(), ProjectRoleConstants.PROJ_MANAGER)) {
                    projRoleService.addTeamRoleByCode(teamId, role.getRoleCode());
                }
            }
        }

    }
    
    
    public  HashMap<String, String> saveProjLib(String projectId, String templateId,String userId) {
        String templateLibId=  projTemplateLibService.getLibIdByTemplateId(templateId);
        //项目库Id
        String libId= projRoleService.getLibIdByProjectId(projectId);
        HashMap<String, String> idsMap = projTemplateLibService.copyProjLibFolders(libId, templateLibId,ProjectConstants.PROJECT,userId);
        return idsMap;
    }
    
    
    public void savePlans(String projectId, String templateId, TSUserDto userDto,String orgId) {
        ProjTemplate template = projTemplateService.getProjEntity(templateId);
        Project project = projectService.getProjectEntity(projectId);
        if (template != null) {
            Map<String, Object> paraMap = new HashMap<String, Object>();
            List<PlanTemplateDetail> planTemplateDetail = sessionFacade.executeQuery("from PlanTemplateDetail where projectTemplateId=?", new Object[]{templateId});
            saveProjTmplPlan(planTemplateDetail,project,paraMap,userDto,orgId);
            savePreposePlanByProjTemplate(planTemplateDetail, paraMap);
        }
    }
    
    private void saveProjTmplPlan(List<PlanTemplateDetail> projTemplateDetailList, Project project, Map<String, Object> paraMap,TSUserDto userDto,String orgId) {
        for (PlanTemplateDetail detail : projTemplateDetailList) {
            Plan plan = new Plan();
            String[] workTime = detail.getWorkTime().split("[.]");
            Date startProjectTime = (Date)project.getStartProjectTime().clone();
            Date planEndTime = setPlanTime(project.getProjectTimeType(), startProjectTime, workTime[0]);

            plan.setProjectId(project.getId());

            // 如果有上级的ID，需要获得上级对应的记录的ID
            if (StringUtils.isNotEmpty(detail.getParentPlanId())
                && !CommonUtil.mapIsEmpty(paraMap, detail.getParentPlanId())) {

                // 如果有上方计划，需要把序号填充(未做)
                String parentId = (String)paraMap.get(detail.getParentPlanId());
                plan.setParentPlanId(parentId);
            }
//            else {// 没有上级的ID,下方导入和导入子计划需要用到上级计划ID为上级ID
//                plan.setParentPlanId(planTemplateDetailReq.getPlanId());
//            }

            // 如果导入模板时计划节点需要重新排序
            // 每次都是最大的planNumber+1
            int maxPlanNumber = projectService.getMaxPlanNumber(plan);
            plan.setPlanNumber(maxPlanNumber + 1);

            int maxStoreyNo = projectService.getMaxStoreyNo(plan);
            plan.setStoreyNo(maxStoreyNo + 1);

            // 如果是上方计划导入并且计划的上级与上方的上级相同，需要记录下最终的序号
//            if (StringUtils.isNotEmpty(planTemplateDetailReq.getUpPlanId())
//                && (planTemplateDetailReq.getPlanId() == null || plan.getParentPlanId().equals(
//                    planTemplateDetailReq.getPlanId()))) {
//                storeyNo = plan.getStoreyNo();
//            }

            // 如果有上级时，计划结束时间少于上级的结束时间,则不导入
//            if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
//                Plan planDb = getEntity(Plan.class, plan.getParentPlanId());
//                long dayDiff = DateUtil.dayDiff(planEndTime, planDb.getPlanEndTime());
//                if (dayDiff < 0) {
//                    throw new GWException("计划结束时间晚于上级结束时间，请修改后重新上传导入");
//                }
//            }
//            else { // 如果没有上级，则计划结束时间少于项结束时间，则不导入
//                long dayDiff = DateUtil.dayDiff(planEndTime, project.getEndProjectTime());
//                if (dayDiff < 0) {
//                    throw new GWException("计划结束时间晚于项目结束时间，请修改后重新上传导入");
//                }
//            }
            plan.setPlanStartTime(project.getStartProjectTime());
            plan.setPlanEndTime(planEndTime);
            
            plan.setPlanName(detail.getName());
            plan.setMilestone(detail.getMilestone());
            plan.setPlanLevel(detail.getPlanLevel());
            plan.setWorkTime(detail.getWorkTime());
            plan.setTaskNameType("0");
            plan.setTaskType("WBS计划");

            if (StringUtils.isNotBlank(detail.getTaskNameType())) {
                plan.setTaskNameType(detail.getTaskNameType());
            }
            if (StringUtils.isNotBlank(detail.getTabCbTemplateId())) {
                plan.setTabCbTemplateId(detail.getTabCbTemplateId());
            }

            CommonInitUtil.initGLObjectForCreate(plan,userDto,orgId);
            planService.initBusinessObject(plan);
            plan.setCreateBy(project.getCreateBy());
            plan.setCreateTime(new Date());
            
            if(planService.isReviewTask(detail.getName())){
                plan.setTaskNameType("评审任务");
            }
            sessionFacade.save(plan);
            paraMap.put(detail.getId(), plan.getId());
            deliverablesInfoService.saveDeliverableByPlan(detail.getId(),
                CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE,
                CommonConstants.DELIVERABLE_TYPE_PLAN, plan,null,userDto,orgId);
            inputsService.saveInputsByObj(detail.getId(),
                    CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE,
                    CommonConstants.DELIVERABLE_TYPE_PLAN, plan,userDto,orgId);

        }
    }
    
    
    private void savePreposePlanByProjTemplate(List<PlanTemplateDetail> projTemplateDetailList,
                                                      Map<String, Object> paraMap)
        throws GWException {
        for (PlanTemplateDetail detail : projTemplateDetailList) {
            PreposePlanTemplate preposePlanTemplate = new PreposePlanTemplate();
            preposePlanTemplate.setPlanId(detail.getId());
            List<PreposePlanTemplate> preposePlanTemplateList = preposePlanTemplateService.getPreposePlansByPreposePlanTemplate(
                preposePlanTemplate, 1, 10, false);
            for (PreposePlanTemplate preposeProjTemplateDb : preposePlanTemplateList) {

                // 如果前置计划ID没有，则不需要保存
                if (CommonUtil.mapIsEmpty(paraMap, preposeProjTemplateDb.getPreposePlanId())) {
                    return;
                }
                PreposePlan preposePlan = new PreposePlan();
                preposePlan.setPlanId(paraMap.get(detail.getId()).toString());
                preposePlan.setPreposePlanId(paraMap.get(preposeProjTemplateDb.getPreposePlanId()).toString());
                preposePlanService.save(preposePlan);
            }
        }
    }
    
    private Date setPlanTime(String projectTimeType, Date projStartTime, String workTime)
        throws GWException {
        if (projStartTime == null) {
            throw new GWException("项目开始时间为空，不可以导入");
        }
        Date planEndTime = null;
        try {
            if (StringUtils.isNotEmpty(projectTimeType)) {                
//                if (ProjectConstants.NATURALDAY.equals(projectTimeType)) {
//                    planEndTime = DateUtil.nextDay(projStartTime, Integer.valueOf(workTime));
//                }
//                else {
//                    planEndTime = DateUtil.nextWorkDay(projStartTime, Integer.valueOf(workTime));
//                }
                                
                if (ProjectConstants.NATURALDAY.equals(projectTimeType)) {
                    planEndTime = DateUtil.nextDay(projStartTime, Integer.valueOf(workTime)-1);
                }
                else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("startDate",projStartTime);
                    params.put("days",Integer.valueOf(workTime) - 1);
                    planEndTime = calendarService.getNextWorkingDay(appKey,params);
                }
                else {
                    planEndTime = DateUtil.nextWorkDay(projStartTime, Integer.valueOf(workTime)-1);
                }

            }
        }
        catch (GWException e) {
            throw new GWException("通过上级开始时间,获得工期之后的时间出错");
        }

        return planEndTime;
    }
    
   
   
}
