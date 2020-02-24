/*
 * 文件名：PreposePlanServiceI.java 版权：Copyright by www.glaway.com 描述： 修改人：blcao 修改时间：2015年4月20日 跟踪单号：
 * 修改单号： 修改内容：
 */

package com.glaway.ids.project.plan.service;


import com.glaway.foundation.common.service.CommonService;
import com.glaway.ids.project.plan.dto.PreposePlanDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.vo.PlanExcelVo;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import net.sf.mpxj.Task;

import java.util.List;
import java.util.Map;


/**
 * 〈计划模板前置〉 〈功能详细描述〉
 * 
 * @author blcao
 * @version 2015年4月20日
 * @see PreposePlanServiceI
 * @since
 */

public interface PreposePlanServiceI extends CommonService
{

    /**
     * 根据计划ID查询其前置计划
     * 
     * @param plan 计划对象
     * @return
     * @see
     */
    List<PreposePlan> getPreposePlansByPlanId(Plan plan);



    /**
     * 根据计划ID删除其所有前置计划
     * 
     * @param plan 计划对象
     * @return
     * @see
     */
    void removePreposePlansByPlanId(Plan plan);

    /**
     * 根据计划ID查询其后置计划
     *
     * @param plan 计划对象
     * @return
     * @see
     */
    List<PreposePlan> getPostposesByPreposeId(Plan plan);

    /**
     * Description: <br> 保存计划前置任务<br> Implement: <br> 通过MPP保存计划前置任务<br>
     *
     * @param taskList 任务集合
     * @param paraMap 前置计划集合
     * @see
     */
    List<PreposePlan> savePreposePlanTemplateByMpp(List<Task> taskList, Map<String, String> paraMap);


    /**
     * 根据计划ID查询其前置计划
     *
     * @return
     * @see
     */
    List<PreposePlan> getPreposePlansByParent(Plan parent);

    /**
     * 获取前置计划信息
     * @param id 前置计划id
     * @return
     */
    PreposePlan getPreposePlanEntity(String id);
    
    void deleteById(String id);
    
    /**
     * 根据计划ID和前置ID查询其某条前置计划
     * 
     * @param prepose 前置计划对象
     * @return
     * @see
     */
    List<PreposePlan> searchPrepose(PreposePlan prepose);

    /**
     * Description: <br> 保存计划前置任务<br> Implement: <br> 通过计划模板保存计划前置任务<br>
     *
     * @param paraMap 前置计划信息集合
     * @see
     */
    List<PreposePlan> getPreposePlans(List<PlanTemplateDetail> planTemplateDetailList,
                                      Map<String, List<PlanTemplateDetail>> preposeMap,
                                      Map<String, String> paraMap);


    /**
     * 根据项目id获取前置计划
     * @param projectId 项目id
     * @return
     */
    List<PreposePlan> queryPreposePlanByProjectId(String projectId);

    /**
     * 根据计划id获取前置计划
     * @param toString 计划ids
     * @return
     */
    List<PreposePlan> queryPreposePlanByPlanIds(String toString);

    /**
     * 获取excel中前置计划信息
     * @param vo 计划excel对象的vo
     * @param excelMap  excel对象集合
     * @param preposeInput 前置信息
     * @return
     */
    List<PreposePlan> getPreposePlanInfoByExcel(PlanExcelVo vo, Map<String, PlanExcelVo> excelMap, Map<String, String> preposeInput);

}
