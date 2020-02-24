/*
 * 文件名：PreposePlanTemplateServiceI.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：duanpengfei
 * 修改时间：2015年4月20日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.plantemplate.service;

import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.TempPlanResourceLinkInfo;
import com.glaway.ids.project.plantemplate.entity.PreposePlanTemplate;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import net.sf.mpxj.Task;

import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.service.CommonService;

/**
 * 〈计划模板前置计划接口〉
 * 〈功能详细描述〉
 * @author zhousuxia
 * @version 2019年8月14日
 * @see PreposePlanTemplateServiceI
 * @since
 */

public interface PreposePlanTemplateServiceI extends CommonService{

    
    /**
     * 获取该计划的后置计划ids
     * @param planId 计划id
     * @return
     */
    String getPostPlanListByPlanId(String planId, Map<String, String> preposeMap);


    /**
     * Description: <br>
     * 保存计划模板前置任务<br>
     * Implement: <br>
     * 通过MPP保存计划模板前置任务<br>
     *
     * @param taskList 任务列表
     * @param paraMap 参数
     * @param userId 用户id
     * @see
     */
    void savePreposePlanTemplateByMpp(List<Task> taskList,Map<String, Object> paraMap,String userId) throws GWException;


    /**
     * Description: <br>
     * 查询前置计划模板<br>
     * Implement: <br>
     * 根据计划模板ID查询其前置计划模板<br>
     *
     * @param preposePlanTemplate 模板前置计划对象
     * @return
     * @see
     */
    List<PreposePlanTemplate> getPreposePlansByPreposePlanTemplate(PreposePlanTemplate preposePlanTemplate,
                                                                   Integer pageSize, Integer pageNum,
                                                                   Boolean isPage);


    /**
     * 获取计划模板Excel计划节点的前置计划信息
     * @param vo 计划模板excel对象vo
     * @param excelMap excel信息
     * @param preposeInput 前置信息
     * @param detail 计划对象
     * @param userDto 用户对象
     * @param orgId 组织id
     * @return
     */
    List<PreposePlanTemplate> getPreposePlanInfoByPlanTemplateExcel(PlanTemplateExcelVo vo, Map<String, PlanTemplateExcelVo> excelMap,
                                                                    Map<String, String> preposeInput, Plan detail, TSUserDto userDto,String orgId);


    /**
     * 根据计划模板id获取前置计划列表
     * @param planTemplateId 计划模板id
     * @return
     */
    List<PreposePlanTemplate> getPreposePlansByPlanTemplateId(String planTemplateId);


    /**
     * Description: <br>
     * 保存计划模板前置任务<br>
     * Implement: <br>
     * 通过计划保存计划模板前置任务<br>
     *
     * @param plan 计划对象
     * @param paraMap 参数
     * @param userId 用户id
     * @see
     */
    void savePreposePlanTemplateByPlan(Plan plan,Map<String, Object> paraMap,String userId,TSUserDto userDto,String orgId) throws GWException;

    /**
     * 查询变更资源列表
     * @param tempPlanResourceLinkInfo 变更资源关联对象信息
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     */
    List<TempPlanResourceLinkInfo> queryResourceChangeListOrderBy(TempPlanResourceLinkInfo tempPlanResourceLinkInfo, int page, int rows, boolean isPage);
}
