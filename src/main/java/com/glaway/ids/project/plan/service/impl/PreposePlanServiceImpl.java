/*
 * 文件名：PreposePlanServiceImpl.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：blcao
 * 修改时间：2015年4月20日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.plan.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.ids.project.plan.dto.PreposePlanDto;
import com.glaway.ids.project.plan.vo.PlanExcelVo;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Task;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.UUIDGenerator;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.service.PreposePlanServiceI;


/**
 * 计划的前置计划service
 * 
 * @author blcao
 * @version 2015年4月20日
 * @see PreposePlanServiceImpl
 * @since
 */
@Service("preposePlanService")
@Transactional(propagation = Propagation.REQUIRED)
public class PreposePlanServiceImpl extends CommonServiceImpl implements PreposePlanServiceI {


    @Autowired
    private SessionFacade sessionFacade;
    
    /**
     * 根据计划ID查询其前置计划
     * 
     * @param plan
     * @return
     * @see
     */
    @Override
    public List<PreposePlan> getPreposePlansByPlanId(Plan plan) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        if (plan != null && StringUtils.isNotEmpty(plan.getId())) {
            String hql = "from PreposePlan p where p.planId = '" + plan.getId() + "'";
            list = findByQueryString(hql);
        }
        return list;
    }


    /**
     * 根据计划ID删除其所有前置计划
     * 
     * @param plan
     * @return
     * @see
     */
    @Override
    public void removePreposePlansByPlanId(Plan plan) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        if (plan != null && StringUtils.isNotEmpty(plan.getId())) {
            String hql = "from PreposePlan p where p.planId = '" + plan.getId() + "'";
            list = findByQueryString(hql);
            for (PreposePlan preposePlan : list) {
                delete(preposePlan);
            }
        }
    }

    /**
     * 根据计划ID查询其后置计划
     *
     * @param plan
     * @return
     * @see
     */
    @Override
    public List<PreposePlan> getPostposesByPreposeId(Plan plan) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        if (plan != null && StringUtils.isNotEmpty(plan.getPreposeIds())) {
            String hql = "from PreposePlan p where p.preposePlanId = '" + plan.getPreposeIds()
                    + "' and p.planInfo.bizCurrent != 'INVALID'";
            list = findByQueryString(hql);
        }
        return list;
    }

    @Override
    public List<PreposePlan> savePreposePlanTemplateByMpp(List<Task> taskList,
                                                          Map<String, String> paraMap) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        for (Task task : taskList) {
            if (task.getID() != 0 && StringUtils.isNotEmpty(task.getName())
                    && !CommonUtil.isEmpty(task.getPredecessors())) {
                List<Relation> predList = task.getPredecessors();
                for (Relation relation : predList) {
                    String planId = (String)paraMap.get(task.getID().toString());
                    String preposePlanId = (String)paraMap.get(relation.getTargetTask().getID().toString());
                    if (StringUtils.isNotEmpty(planId) && StringUtils.isNotEmpty(preposePlanId)) {
                        PreposePlan preposePlan = new PreposePlan();
                        preposePlan.setId(UUIDGenerator.generate().toString());
                        preposePlan.setPlanId(planId);
                        preposePlan.setPreposePlanId(preposePlanId);
                        list.add(preposePlan);
                    }
                }
            }
        }
        return list;
    }
    
    /**
     * 根据计划ID查询其前置计划
     * 
     * @param plan
     * @return
     * @see
     */
    @Override
    public List<PreposePlan> getPreposePlansByParent(Plan plan) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        if (plan != null && StringUtils.isNotEmpty(plan.getId())) {
            String hql = "from PreposePlan p where p.planInfo.parentPlanId = '" + plan.getId()
                         + "' and p.preposePlanInfo.parentPlanId = '" + plan.getId() + "'";
            list = findByQueryString(hql);
        }
        return list;
    }
    
    @Override
    public PreposePlan getPreposePlanEntity(String id) {
        PreposePlan preposePlan = (PreposePlan) sessionFacade.getEntity(PreposePlan.class,id);
        return preposePlan;
    }
    
    @Override
    public void deleteById(String id) {
        PreposePlan preposePlan = (PreposePlan) sessionFacade.getEntity(PreposePlan.class,id);
        sessionFacade.delete(preposePlan);
    }
    
    /**
     * 根据计划ID和前置ID查询其某条前置计划
     *
     * @return
     * @see
     */
    @Override
    public List<PreposePlan> searchPrepose(PreposePlan prepose) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        if (prepose != null && StringUtils.isNotEmpty(prepose.getPlanId())
            && StringUtils.isNotEmpty(prepose.getPreposePlanId())) {
            String hql = "from PreposePlan p where p.planId = '" + prepose.getPlanId()
                         + "' and p.preposePlanId = '" + prepose.getPreposePlanId() + "'";
            list = findByQueryString(hql);
        }
        return list;
    }

    @Override
    public List<PreposePlan> queryPreposePlanByProjectId(String projectId) {
        List<PreposePlan> preposeList = new ArrayList<PreposePlan>();
        preposeList = findHql("from PreposePlan where planId in(select id from Plan where projectId = ?)", projectId);
        return preposeList;
    }

    @Override
    public List<PreposePlan> queryPreposePlanByPlanIds(String planIds) {
        List<PreposePlan> preposeList = new ArrayList<PreposePlan>();
        String hql = "from PreposePlan where planId in('" + planIds + ")";
        preposeList = findByQueryString(hql);
        return preposeList;
    }

    @Override
    public List<PreposePlan> getPreposePlanInfoByExcel(PlanExcelVo vo,
                                                       Map<String, PlanExcelVo> excelMap, Map<String, String> preposeInput) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        String preposeNos = vo.getPreposeNos();
        preposeInput.put(vo.getId(), preposeNos);
        Map<String, String> map = new HashMap<String, String>();
        String[] nos = preposeNos.split(",");
        for(String no : nos) {
            if(!CommonUtil.isEmpty(no) && CommonUtil.isEmpty(map.get(no))) {
                PreposePlan p = new PreposePlan();
                CommonUtil.glObjectSet(p);
                p.setId(UUIDGenerator.generate().toString());
                p.setPlanId(vo.getId());
                p.setPreposePlanId(excelMap.get(no).getId());
                list.add(p);
                map.put(no, no);
            }
        }
        return list;
    }

    @Override
    public List<PreposePlan> getPreposePlans(List<PlanTemplateDetail> planTemplateDetailList, Map<String, List<PlanTemplateDetail>> preposeMap, Map<String, String> paraMap) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        for (PlanTemplateDetail detail : planTemplateDetailList) {
            List<PlanTemplateDetail> preposePlanTemplateList = preposeMap.get(detail.getId());
            if (!CommonUtil.isEmpty(preposePlanTemplateList)) {
                for (PlanTemplateDetail preposePlanTemplateDb : preposePlanTemplateList) {
                    PreposePlan preposePlan = new PreposePlan();
                    preposePlan.setId(UUIDGenerator.generate().toString());
                    preposePlan.setPlanId(paraMap.get(detail.getId()));
                    preposePlan.setPreposePlanId(paraMap.get(preposePlanTemplateDb.getId()));
                    list.add(preposePlan);
                }
            }
        }
        return list;
    }
}
