/*
 * 文件名：PreposePlanTemplateServiceImpl.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：duanpengfei
 * 修改时间：2015年4月20日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.plantemplate.service.impl;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.plan.entity.TempPlanResourceLinkInfo;
import com.glaway.ids.project.plantemplate.entity.PreposePlanTemplate;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import com.glaway.ids.util.CommonInitUtil;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Task;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.UUIDGenerator;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.service.PreposePlanServiceI;
import com.glaway.ids.project.plantemplate.service.PreposePlanTemplateServiceI;


@Service("preposePlanTemplateService")
@Transactional
public class PreposePlanTemplateServiceImpl extends CommonServiceImpl implements PreposePlanTemplateServiceI {
    /**
     * 计划前置
     */
    @Autowired
    private PreposePlanServiceI preposePlanService;
    
    /**
     * sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;


    @Override
    public String getPostPlanListByPlanId(String planId, Map<String, String> preposeMap) {
        String result = "";
        if(!CommonUtil.isEmpty(preposeMap)) {
            Set<String> keySet = preposeMap.keySet();
            for(String key : keySet) {
                if(!CommonUtil.isEmpty(preposeMap.get(key))) {
                    String[] valueArrray = preposeMap.get(key).split(",");
                    for(String tempValue : valueArrray) {
                        if(planId.equals(tempValue)) {
                            if(CommonUtil.isEmpty(result)) {
                                result = key;
                            } else {
                                result = result + "," + key;
                            }                           
                        }
                    }
                }
            }           
        }
        return result;
    }

    @Override
    public void savePreposePlanTemplateByMpp(List<Task> taskList, Map<String, Object> paraMap, String userId) throws GWException {
        for (Task task : taskList) {
            if (task.getID() == 0 || StringUtils.isEmpty(task.getName())) {
                continue;
            }
            List<Relation> PredList = task.getPredecessors();
            if (PredList == null) {
                continue;
            }

            // 如果前置计划ID没有，则不需要保存
            if (CommonUtil.mapIsEmpty(paraMap, task.getID().toString())) {
                continue;
            }
            String planTemplateDetailId = (String)paraMap.get(task.getID().toString());
            for (Relation relation : PredList) {
                PreposePlanTemplate preposePlanTemplate = new PreposePlanTemplate();
                String preposePlanTemplateId = (String)paraMap.get(relation.getTargetTask().getID().toString());
                preposePlanTemplate.setPlanId(planTemplateDetailId);
                preposePlanTemplate.setPreposePlanId(preposePlanTemplateId);
                preposePlanTemplate.setCreateBy(userId);
                preposePlanTemplate.setCreateTime(new Date());
                preposePlanTemplate.setId(UUIDGenerator.generate().toString());
                ((List)paraMap.get("list")).add(preposePlanTemplate);
            }
        }
    }

    @Override
    public List<PreposePlanTemplate> getPreposePlansByPreposePlanTemplate(PreposePlanTemplate preposePlanTemplate, Integer pageSize, Integer pageNum, Boolean isPage) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from PreposePlanTemplate t");
        Map<String, Object> resultMap = getQueryParam(hql, preposePlanTemplate);
        String hqlStr = (String)resultMap.get("hql");
        List<PreposePlanTemplate> paramList = (List<PreposePlanTemplate>)resultMap.get("queryList");
        List<PreposePlanTemplate> list = new ArrayList<PreposePlanTemplate>();
        if (isPage) {
            list = pageList(hqlStr, paramList.toArray(), (pageNum - 1) * pageSize, pageSize);
        }
        else {
            list = executeQuery(hqlStr, paramList.toArray());
        }
        return list;
    }

    /**
     * Description: <br>
     * 组装查询方法<br>
     *
     * @param hql
     * @return
     * @see
     */
    private Map<String, Object> getQueryParam(StringBuilder hql,
                                              PreposePlanTemplate preposePlanTemplate) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        hql.append(" where t.avaliable!=0");

        List<Object> paramList = new ArrayList<Object>();
        if (preposePlanTemplate != null) {
            if (StringUtils.isNotEmpty(preposePlanTemplate.getPlanId())) {
                hql.append(" and t.planId=?");
                paramList.add(preposePlanTemplate.getPlanId());
            }
        }
        hql.append(" order by t.createTime asc");
        resultMap.put("hql", hql.toString());
        resultMap.put("queryList", paramList);
        return resultMap;
    }

    @Override
    public List<PreposePlanTemplate> getPreposePlanInfoByPlanTemplateExcel(PlanTemplateExcelVo vo, Map<String, PlanTemplateExcelVo> excelMap, Map<String, String> preposeInput, Plan detail, TSUserDto userDto, String orgId) {
        List<PreposePlanTemplate> list = new ArrayList<PreposePlanTemplate>();
        String preposeNos = vo.getPreposeNos();
        String preposeIds = "";
        preposeInput.put(vo.getId(), preposeNos);
        Map<String, String> map = new HashMap<String, String>();
        String[] nos = preposeNos.split(",");
        for(String no : nos) {
            if(!CommonUtil.isEmpty(no) && CommonUtil.isEmpty(map.get(no))) {
                PreposePlanTemplate p = new PreposePlanTemplate();
                CommonInitUtil.initGLObjectForCreate(p,userDto,orgId);
                p.setId(UUIDGenerator.generate().toString());
                p.setPlanId(vo.getId());
                p.setPreposePlanId(excelMap.get(no).getId());
                list.add(p);
                if(CommonUtil.isEmpty(preposeIds)) {
                    preposeIds = excelMap.get(no).getId();
                } else {
                    preposeIds = preposeIds + "," + excelMap.get(no).getId();
                }
                map.put(no, no);
            }
        }
        if(!CommonUtil.isEmpty(preposeIds)) {
            detail.setPreposeIds(preposeIds);
        }
        return list;
    }

    @Override
    public List<PreposePlanTemplate> getPreposePlansByPlanTemplateId(String planTemplateId) {
        List<PreposePlanTemplate> list = new ArrayList<PreposePlanTemplate>();
        if(!CommonUtil.isEmpty(planTemplateId)) {
            String hql = "from PreposePlanTemplate t where t.planId in(select id from PlanTemplateDetail where planTemplateId = ?)";
            list = sessionFacade.findHql(hql, planTemplateId);
        }
        return list;
    }

    @Override
    public void savePreposePlanTemplateByPlan(Plan plan, Map<String, Object> paraMap, String userId,TSUserDto userDto,String orgId) throws GWException {
        // 通过计划ID获得计划前置表中的数据
        List<PreposePlan> preposePlanList = preposePlanService.getPreposePlansByPlanId(plan);

        // 遍历计划前置数据
        for (PreposePlan preposePlan : preposePlanList) {

            // 如果前置计划ID没有，则不需要保存
            if (CommonUtil.mapIsEmpty(paraMap, preposePlan.getPreposePlanId())) {
                continue;
            }
            PreposePlanTemplate preposePlanTemplate = new PreposePlanTemplate();
            CommonInitUtil.initGLObjectForCreate(preposePlanTemplate,userDto,orgId);
            String planTemplateDetailId = (String)paraMap.get(preposePlan.getPlanId());
            String preposePlanTemplateId = (String)paraMap.get(preposePlan.getPreposePlanId());
            preposePlanTemplate.setPlanId(planTemplateDetailId);
            preposePlanTemplate.setPreposePlanId(preposePlanTemplateId);
            preposePlanTemplate.setCreateBy(userId);
            sessionFacade.save(preposePlanTemplate);
        }
    }

    @Override
    public List<TempPlanResourceLinkInfo> queryResourceChangeListOrderBy(TempPlanResourceLinkInfo tempPlanResourceLinkInfo, int page, int rows, boolean isPage) {
        String hql = createHqlOrderBy(tempPlanResourceLinkInfo);
        if (isPage) {
            return pageList(hql, (page - 1) * rows, rows);
        }
        return findByQueryString(hql);
    }

    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param
     * @return
     * @see
     */
    private String createHqlOrderBy(TempPlanResourceLinkInfo resourceLinkInfo){
        String hql = "from TempPlanResourceLinkInfo l  where  1 = 1 ";
        //  资源名称
        if(!StringUtils.isEmpty(resourceLinkInfo.getResourceName())){
            hql = hql + " and l.resourceInfo.name like '%" + resourceLinkInfo.getResourceName() + "%'";
        }
        //  资源ID
        if(!StringUtils.isEmpty(resourceLinkInfo.getResourceId())){
            hql = hql + " and l.resourceId = '" + resourceLinkInfo.getResourceId() + "'";
        }
        if (!StringUtils.isEmpty(resourceLinkInfo.getUseObjectId())) {
            hql = hql + " and l.useObjectId = '" + resourceLinkInfo.getUseObjectId() + "'";
        }

        // 生命周期状态
        if (!StringUtils.isEmpty(resourceLinkInfo.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + resourceLinkInfo.getBizCurrent() + "'";
        }
        //是否可用
        if (!StringUtils.isEmpty(resourceLinkInfo.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + resourceLinkInfo.getAvaliable() + "'";
        }
        hql = hql + " order by l.resourceLinkId";
        return hql;
    }
}
