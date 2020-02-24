package com.glaway.ids.project.plantemplate.service;


import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateDetailReq;
import org.apache.poi.ss.formula.functions.T;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;


/**
 * 〈计划模板WBS计划接口〉
 * 〈功能详细描述〉
 * 
 * @author zhousuxia
 * @version 2019年4月22日
 * @see PlanTemplateDetailServiceI
 * @since
 */
public interface PlanTemplateDetailServiceI extends BusinessObjectServiceI<PlanTemplateDetail> {


    /**
     * 计划模板数据id与前置计划集合
     * @author zhousuxia
     * @param plantemplateId  计划模板id
     * @param type 查询类型
     * @return
     */
    Map<String, String> getPlanTemplateOrProjTemplateDetailPreposes(String plantemplateId, String type);


    /**
     * 获取计划模板数据id与前置计划id集合
     * @author zhousuxia
     * @param plantemplateId 计划模板id
     * @return
     */
    Map<String, String> getPlanTemplateDetailPreposesId(String plantemplateId);


    /**
     * 获取计划模板数据列表
     * @author zhousuxia
     * @param planTemplateId 计划模板id
     * @return
     */
    List<PlanTemplateDetail> getPlanTemplateDetailList(String planTemplateId);


    /**
     * 获取计划模板/项目模板数据交付项
     * @param plantemplateId 计划模板id
     * @return
     */
    Map<String, String> getPlanTemplateOrProjTemplateDetailDeliverables(String plantemplateId, String type);


    /**
     * 获取计划模板数据输入项名称
     * @param list 输入集合
     * @return
     */
    Map<String, String> getPlanTemplateDetailInputsName(List<Inputs> list);

    /**
     * 获取输入项来源
     * @param list 输入集合
     * @param detailList 计划模板中计划信息集合
     * @param detailIdDeliverablesMap 计划模板id-前置计划集合
     * @return
     */
    Map<String, String> getPlanTemplateDetailInputsOrigin(List<Inputs> list, List<PlanTemplateDetail> detailList,
                                                          Map<String, String> detailIdDeliverablesMap);


    /**
     * Description: WBS计划查询
     *
     * @param planTemplateDetailRep 计划模板详细信息
     *            查询条件
     * @return
     * @see
     */
    List<T> getList(PlanTemplateDetailReq planTemplateDetailRep, Integer pageSize, Integer pageNum);

    /**
     * Description: <br>
     * 通过计划编号或计划编号保存计划模板详细<br>
     *
     * @param projectId 项目id
     * @param planId 计划id
     * @param planTemplate 计划模板
     * @throws GWException
     * @see
     */
    void savePlanTemplateDetailByPlan(String projectId, String planId, PlanTemplate planTemplate, TSUserDto userDto,String orgId)
            throws GWException;

    /**
     * 获取计划模板中计划详细信息
     * @param planTemplateId 计划模板id
     * @return
     */
    List<PlanTemplateDetail> getDetailList(String planTemplateId);

    /**
     * 获取计划模板中计划-前置计划的集合
     * id:preposeList
     * @param plantemplateId 计划模板id
     * @return
     */
    Map<String, List<PlanTemplateDetail>> getDetailPreposes(String plantemplateId);

    /**
     * 根据计划ID查找其所有子计划（包括计划本身和所有子孙计划）
     * @param list 计划id集合
     * @param planId 计划id
     * @param detailList 计划集合
     * @return
     */
    List<String> getPlanAllChildren(List<String> list, String planId, List<Plan> detailList);



}
