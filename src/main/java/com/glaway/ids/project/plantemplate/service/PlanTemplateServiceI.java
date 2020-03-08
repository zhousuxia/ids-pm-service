package com.glaway.ids.project.plantemplate.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.ids.project.plan.dto.InputsDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plantemplate.entity.PlanTempOptLog;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.entity.PreposePlanTemplate;
import com.glaway.ids.project.plantemplate.support.planTemplate.vo.PlanTemplateReq;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import net.sf.mpxj.Task;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.util.mpputil.MppInfo;


/**
 * 〈计划模板接口〉
 * 〈功能详细描述〉
 * @author duanpengfei
 * @version 2015年4月22日
 * @see PlanTemplateServiceI
 * @since
 */

public interface PlanTemplateServiceI extends BusinessObjectServiceI<PlanTemplate> {

    /**
     * 获取计划模板生命周期状态列表
     * @return
     */
    String getLifeCycleStatusList();


    /**
     * 条件查询
     * @param conditionList 查询条件
     * @return
     */
    PageList queryEntity(List<ConditionVO> conditionList, Map<String,String> params);

    /**
     * 获取计划模板信息
     * @param id 计划模板id
     * @return
     */
    PlanTemplate getPlanTemplateEntity(String id);

    /**
     * 保存操作日志
     * @param bizId 版本id
     * @param logInfo 日志信息
     * @param curUserId 用户id
     * @param orgId 组织id
     * @return
     */
    String saveTemplateOptLog(String bizId, String logInfo,String curUserId,String orgId);


    /**
     * 获取模板中的计划
     * @param planTemplateId 计划模板id
     * @return
     */
    List<PlanTemplateDetail> getTemplatePlanDetailById(String planTemplateId);

    /**
     * 计划模板同名校验
     *
     * @author zhousuxia
     * @version 2018年7月2日
     * @see PlanTemplateServiceI
     * @since
     */
    boolean checkTemplateNameBeforeSave(String templateName,String planTemplateId);


    /**
     * 获取计划id-输入项集合
     *
     * @author zhousuxia
     * @version 2019年5月15日
     * @see PlanTemplateServiceI
     * @since
     */
    Map<String,List<InputsDto>> queryInputsListMap();

    /**
     * 保存计划模板及相关计划信息
     * @param template 计划模板
     * @param curUserId 用户id
     * @param orgId 组织id
     * @param planList 计划集合
     */
    void savePlanTemplateAndDetail(PlanTemplate template,String curUserId,String orgId,List<Plan> planList);

    /**
     * 保存操作日志
     * @param bizId 版本id
     * @param logInfo 日志信息
     * @param userDto 用户信息
     * @param orgId 组织id
     * @return
     */
    String saveTemplateOptLog(String bizId, String logInfo, TSUserDto userDto,String orgId);

    /**
     * 保存计划模板信息
     * @param detailInsert  计划模版中计划集合
     * @param deliverablesInsert 交付项集合
     * @param preposePlanInsert 前置计划集合
     * @param inputsInsert 输入集合
     */
    void savePlanTemplateAllByList(List<PlanTemplateDetail> detailInsert, List<DeliverablesInfo> deliverablesInsert,
                                   List<PreposePlanTemplate> preposePlanInsert, List<Inputs> inputsInsert);


    /**
     * 修改或者修订模板
     * @param template 计划模板对象
     * @param type 操作类型
     * @param name 计划模板名称
     * @param remark 计划模板备注
     * @param curUserId 用户id
     * @param orgId 组织id
     * @param delList 计划集合
     */
    void updatePlanTemplateAndDetail(PlanTemplate template, String type, String name, String remark,String curUserId,String orgId,List<Plan> delList);

    /**
     * 计划模板升版
     * @param template 计划模板对象
     * @param mode 操作类型
     * @param name 计划模板名称
     * @param remark 计划模板备注
     * @param userDto 用户对象
     * @param orgId 组织id
     * @return
     */
    PlanTemplate upgradeVersion(PlanTemplate template, String mode, String name, String remark, TSUserDto userDto,String orgId);


    /**
     * Description: <br>
     * 删除计划模板<br>
     * Implement: <br>
     * 通过计划模板编号把计划模板逻辑删除<br>
     *
     * @param planTemplateReq 计划模板详细信息对象
     * @param userId 用户id
     * @throws GWException
     * @see
     */
    void deletePlanTemplate(PlanTemplateReq planTemplateReq, String userId)
            throws GWException;


    /**
     * Description: <br>
     * 更新计划模板<br>
     * Implement: <br>
     * 通过不同的参数更新计划模板<br>
     *
     * @param planTemplateReq 计划模板详细信息对象
     * @param userId 用户id
     * @throws GWException
     * @see
     */
    void updatePlanTemplate(PlanTemplateReq planTemplateReq, String userId)
            throws GWException;


    /**
     * Description: <br>
     * 通过MPP保存计划模板详细<br>
     *
     * @return
     * @see
     */
    PlanTemplate savePlanTemplateDetail(PlanTemplate planTemplate, List<Task> taskList,TSUserDto userDto,String orgId)
            throws GWException;


    /**
     * Description: <br>
     * 通过计划模板详细组成MPP<br>
     *
     * @param planTemplateReq 计划模板详细信息对象
     * @return
     * @see
     */
    List<MppInfo> saveMppInfo(PlanTemplateReq planTemplateReq);


    /**
     * 校验计划模板导入EXCEL数据
     *
     * @param strForBc excel数据信息
     * @param errorMsgMap 错误信息
     * @return
     * @see
     */
    void checkData(int rowNum, String strForBc, String switchStr, Map<String, String> standardNameMap,
        Map<String, String> errorMsgMap,  Map<String, String> deliveryNameMap, List<String> numList,
        Map<String, String> planLevelMap);


    /**
     * 再校验计划模板导入EXCEL数据
     *
     * @param errorMsgMap 错误信息
     * @return
     * @see
     */
    void checkData2(Map<String, PlanTemplateExcelVo> excelMap, Map<String, String> errorMsgMap,
                    List<String> numList);


    /**
     * Description: <br>
     * 通过Excel保存计划模板详细<br>
     *
     * @param dataList
     * @return
     * @see
     */
    String savePlanTemplateDetailByExcel(PlanTemplate planTemplate, List<PlanTemplateExcelVo> dataList,
                                         Map<String, PlanTemplateExcelVo> excelMap, Map<String, String> planLevelMap, String switchStr,TSUserDto userDto,String orgId);


    /**
     * 复制模板
     * @param oldTemplId 计划模板id
     * @param newTemplName 计划模板名称
     * @param remark 备注
     * @return
     */
    void copyTemplate(String oldTemplId, String newTemplName, String remark,TSUserDto userDto,String orgId);


    /**
     * 计划模板版本回退
     * @param template 计划模板信息
     * @param userId 用户id
     * @param orgId 组织id
     */
    void backVesion(PlanTemplate template,String userId,String orgId);


    /**
     * 计划模板版本撤销
     * @param template 计划模板信息
     * @param userId 用户id
     * @param orgId 组织id
     */
    void revokeVesion(PlanTemplate template,String userId,String orgId);


    /**启动流程
     * @author zhousuxia
     * @param planTemplate 计划模板信息
     * @param variables 流程变量
     * @param userDto 用户信息
     * @param orgId 组织id
     */
    void startPlanTemplateProcess(PlanTemplate planTemplate,Map<String, Object> variables,TSUserDto userDto,String orgId);

    /**
     * 提交计划模板流程
     * @param planTemplate 计划模板信息
     * @param variables 流程变量
     * @param processDefinitionKey 流程实例id
     * @param userDto 用户信息
     * @param orgId 组织id
     */
    void submitPlanTemplateFlow(PlanTemplate planTemplate, Map<String, Object> variables, String processDefinitionKey , TSUserDto userDto,String orgId);

    /**
     * 驳回后再提交计划模板流程
     * @param planTemplate 计划模板信息
     * @param userDto 用户信息
     * @param orgId 组织id
     */
    void submitProjectFlowAgain(PlanTemplate planTemplate,TSUserDto userDto,String orgId);

    /**
     * 继续执行计划模板
     * @param taskId 任务id
     * @param planTemplate 计划模板信息
     * @param userId 用户id
     * @param orgId 组织id
     */
    void completePlanTemplateProcess(String taskId, PlanTemplate planTemplate,String userId,String orgId);

    /**
     * 更新状态
     * @param id 计划模板信息id
     */
    void updateBizCurrent(String id);

    /**
     * 获取版本
     * @param bizId 版本id
     * @param pageSize 页码
     * @param pageNum 每页数量
     * @param <T>
     * @return
     */
    <T> List<T> getVersionHistory(String bizId, Integer pageSize, Integer pageNum);

    /**
     * 获取模板版本数量
     *
     * @param bizId 版本id
     * @return
     */
    long getVersionCount(String bizId);

    /**
     * 操作日志
     * @param bizId 版本id
     * @return
     */
    List<PlanTempOptLog> findPlanTempOptLogById(String bizId);

    /**
     * 通过计划保存计划模板
     * @param projectId 项目id
     * @param planId 计划id
     * @param planTemplate 计划模板对象
     * @param userId 用户id
     * @param orgId 组织id
     * @throws GWException
     */
    void savePlanTemplateByPlanject(String projectId,String planId, PlanTemplate planTemplate,String userId,String orgId)
            throws GWException;

    /**
     * 启用/禁用计划模板
     * @param planTemplate 计划模板对象
     * @param status 状态
     * @param userId 用户id
     * @param orgId 组织id
     */
    void doStatusChange(PlanTemplate planTemplate,String status,String userId,String orgId);

    /**
     * 计划模板mpp导入
     * @param planTemplate 计划模板id
     * @param mapKeys mpp信息
     * @param userDto 用户对象
     * @param orgId 组织id
     * @throws IOException
     */
    void importPlanTemplateMpp(PlanTemplate planTemplate,Set<String> mapKeys,TSUserDto userDto,String orgId,List<List<Map<String, Object>>> taskMapList,List<List<String>> preposePlanIdList) throws IOException;

    /**
     * 更新状态
     * @param status 状态
     * @param id 计划模板id
     */
    void updateBizCurrentWhenProcessRefuse(String status,String id);

    /**
     * 删除模板中的计划
     *
     * @author zhousuxia
     * @param planTemplateId 计划模板id
     * @param projTemplateId 项目模板id
     * @version 2018年7月2日
     * @see PlanTemplateServiceI
     * @since
     */
    void doDeletePlanTemplate(String ids,String planTemplateId, String projTemplateId);

    /**
     * 计划模板excel导入
     * @param map 计划模板excel信息
     * @param userId 用户id
     * @param planTemplateId 计划模板id
     * @return
     */
    Map<String,Object> doImportPlanTemplateExcel(List<Map<String,String>> map,String userId,String planTemplateId,String orgId);

    /**
     * 校验数据
     * @return
     */
    Map<String,String> checkDataNew(int rowNum, String strForBc, String switchStr, Map<String, String> standardNameMap, Map<String, String> errorMsgMap, Map<String, String> deliveryNameMap, List<String> numList, Map<String, String> planLevelMap);

}
