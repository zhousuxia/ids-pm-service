package com.glaway.ids.project.projecttemplate.service;


import java.util.List;

import com.glaway.foundation.common.dto.TSUserDto;
import org.apache.poi.ss.formula.functions.T;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplateDetail;


/**
 * 〈项目模板WBS计划接口〉
 * 〈功能详细描述〉
 * @author zhousuxia
 * @version 2018年8月20日
 * @see ProjTemplateDetailServiceI
 * @since
 */
public interface ProjTemplateDetailServiceI extends BusinessObjectServiceI<ProjTemplateDetail> {

    /**
     * Description: 项目模板下计划列表查询,并转换为Plan
     *
     * @param projTemplateId 项目模板Id
     * @return 项目模板下计划列表
     * @see
     */
    List<Plan> convertPlanjTemplateDetail2Plan(String projTemplateId);

    /**
     * Description: 项目模板下计划列表查询
     *
     * @param projTemplateId 项目模板Id
     * @return 项目模板下计划列表
     * @see
     */
    List<PlanTemplateDetail> queryProjTmplPlan(String projTemplateId);

    /**
     *  判断项目模板中是否存在重名文件夹
     * @param templateId 模板id
     * @param fileName   文件夹名称
     * @return
     * @see
     */
    boolean isFileNameRepeat(String templateId, String fileName);

    /**
     * 通过计划编号或计划编号保存计划模板详细
     *
     * @param projectId    项目id
     * @param planId       计划id
     * @param projTemplate 项目id
     * @param userDto      用户信息
     * @param orgId        组织id
     * @throws GWException
     */
    void saveProjTemplateDetailByPlan(String projectId, String planId, ProjTemplate projTemplate, TSUserDto userDto,String orgId)
            throws GWException;

}
