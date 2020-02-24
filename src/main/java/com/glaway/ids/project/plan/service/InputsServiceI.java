package com.glaway.ids.project.plan.service;


import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.vo.PlanExcelVo;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import org.springframework.web.bind.annotation.RequestParam;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.projectmanager.vo.ProjDocVo;


/**
 * 任务输入
 * 
 * @author blcao
 * @version 2015年7月6日
 * @see InputsServiceI
 * @since
 */


public interface InputsServiceI extends CommonService {

    /**
     * 获取满足deliverablesInfo条件的交付物的数目
     *
     * @param inputs 输入对象
     * @return
     * @see
     */
    long getCount(Inputs inputs);

    /**
     * 获取输入列表
     * 
     * @author zhousuxia
     * @version 2018年5月19日
     * @see InputsServiceI
     * @since
     */
    List<Inputs> queryNewInputsList(Inputs input);


    /**
     * 获取项目库文档的文档名称与bizId的集合
     *
     * @author zhousuxia
     * @version 2018年5月24日
     * @see InputsServiceI
     * @since
     */
    Map<String,String> getRepFileNameAndBizIdMap(String libId);


    /**
     * 获取项目库文档的文档路径与bizId的集合
     *
     * @author zhousuxia
     * @version 2018年5月24日
     * @see InputsServiceI
     * @since
     */
    Map<String,String> getRepFilePathAndBizIdMap(String libId);

    /**
     * 获取项目库文档的文档id与bizId的集合
     *
     * @author zhousuxia
     * @version 2018年5月24日
     * @see InputsServiceI
     * @since
     */
    Map<String,String> getRepFileIdAndBizIdMap(String libId);


    /**
     * 获取项目库信息
     *
     * @author zhousuxia
     * @version 2018年5月19日
     * @see InputsServiceI
     * @since
     */
    List<ProjDocVo> getDocRelationList(Plan plan, String userId);

    /**
     * 删除前置对应的输入
     *
     * @param originObjectId
     * @param useObjectType
     * @param useObjectId
     */
    void deleteInputsByPreposePlan(String originObjectId, String useObjectType, String useObjectId);

    /**
     * 删除来源输出相关的输入
     *
     * @param originDeliverablesInfoId
     * @param useObjectType
     */
    void deleteInputsByOriginDeliverables(String originDeliverablesInfoId, String useObjectType);

    /**
     * 删除输入
     * @param id
     */
    void deleteInputsById(String id);

    /**
     * 根据useObjectType、useObjectId查找相关输入
     *
     * @param useObjectType 关联对象的类型
     * @param useObjectId 关联对象的id
     * @return
     * @see
     */
    List<Inputs> getInputsByUseObeject(String useObjectType, String useObjectId);

    /**
     * 删除来源对象相关的输入
     *
     * @param originObjectId 关联对象的类型
     * @param useObjectType 关联对象的id
     */
    void deleteInputsByOriginObject(String originObjectId, String useObjectType);

    /**
     * 删除Object相关输入
     *
     * @param obj 输入对象
     * @return
     * @see
     */
    void deleteInputsByPlan(Object obj);

    /**
     * 获取输入详细信息
     * @param inputs 输入对象
     * @return
     */
    List<Inputs> queryInputsDetailList(Inputs inputs);
    
    /**
     * 根据planParentId条件检索交付物
     * 
     * @param planParentId 计划id
     * @return
     * @see
     */
    List<Inputs> queryInputsDetailList(String planParentId);
    
    /**
     * 根据inputs条件检索交付物
     * 
     * @param inputs 输入对象
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    List<Inputs> queryInputList(Inputs inputs, int page, int rows, boolean isPage);

    /**
     * 更新输入
     * @param id 输入id
     * @param docId 文档id
     * @param docName 文档名称
     */
    void updateInputsForDocInfoById(String id, String docId,String docName);
    
    /**获取父节点下的所有外部输入的数据
     * @param planParentId 计划id
     * @return 
     * @see
     */
    List<Inputs> queryOutInputsDetailList(String planParentId);
    
    /**
     * 获取项目库信息(自动匹配使用)
     * 
     * @author wqb
     * @version 2018年6月22日 17:13:56
     * @see 
     * @since
     */
    List<ProjDocVo> getDocRelationListMatch(Plan plan,String userId,String deliverName);
    
    /**
     * 更新输入
     * @param addInputList 输入集合
     * @param delInputList 输入集合
     */
    void updateInputsByAddAndDel(List<Inputs> addInputList, List<Inputs> delInputList);

    /**
     * 批量删除输入
     * @param delInputs 输入集合
     */
    void deleteInputList(List<Inputs> delInputs);
    
    /**
     * 批量更新输入的来源对象ID、来源交付物ID
     * @param attribute 来源对象信息
     * @param updateInputs 输入集合
     */
    void batchUpdateInputsAttribute(String attribute, List<Inputs> updateInputs);

    /**
     * 项目计划页面查看初始化时获取输入
     *
     * @param map
     * @return
     */
    FeignJson listView(Map<String,Object> map);

    /**
     * 输入查询
     *
     * @param plan 项目对象
     * @param page 页码
     * @param rows 每页数量
     * @param projectId 项目id
     * @return
     */
    FeignJson getInputsRelationList(Plan plan, int page, int rows, String projectId,String userId);

    /**
     * 通过计划模板id获取输入列表
     * @param templId 计划模板id
     * @return
     */
    List<Inputs> getInputsInfoByPlanTemplateId(String templId);

    /**
     * 计划模板：获取前置计划的输出作为后置计划的输入
     * @param map
     * @param excelMap 计划模板excel集合
     * @param deliverMap 交付项集合
     * @return
     */
    Map<String, List<Inputs>> getInputsInfoByPlanTemplateExcel(Map<String, String> map,
                                                               Map<String, PlanTemplateExcelVo> excelMap, Map<String, String> deliverMap, TSUserDto userDto,String orgId);

    /**
     * excel导入输入
     * @param preposeInput 输入集合
     * @param excelMap excel信息集合
     * @param deliverMap 交付项集合
     * @return
     */
    List<Inputs> getInputsInfoByExcel(Map<String, String> preposeInput, Map<String, PlanExcelVo> excelMap, Map<String, String> deliverMap);

    /**
     * Description: <br>
     * 通过编号获得相应的输入项，保存到输入中<br>
     *
     * @param workId      任务id
     * @param queryType   查询类型
     * @param saveType    保存类型
     * @throws GWException
     * @see
     */
    Map saveInputsByObj(String workId, String queryType, String saveType, Object obj,TSUserDto userDto,String orgId)
            throws GWException;
}
