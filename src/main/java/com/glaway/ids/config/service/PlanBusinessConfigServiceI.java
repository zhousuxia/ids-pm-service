package com.glaway.ids.config.service;


import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;

import java.util.List;
import java.util.Map;


/**
 * 配置业务接口
 * 
 * @author Administrator
 * @version 2015年3月26日
 * @see PlanBusinessConfigServiceI
 * @since
 */
public interface PlanBusinessConfigServiceI extends BusinessObjectServiceI<BusinessConfig> {
    
    /**
     * 获取comboboxList
     * 
     * @param bc 计划阶段对象
     * @return
     * @see
     */
    String getBusinessConfigsList(BusinessConfig bc);
    
    /**
     * 搜索启用的配置
     * 
     * @param bc 计划阶段对象
     * @return
     * @see
     */
    List<BusinessConfig> searchUseableBusinessConfigs(BusinessConfig bc);

    /**
     * 搜索启用的配置
     *
     * @param bc 计划阶段对象
     * @return
     * @see
     */
    List<BusinessConfig> searchUseableBusinessConfigsForStr(BusinessConfig bc);

    /**
     * 根据业务配置属性搜索
     * 
     * @param bc 计划阶段对象
     * @return
     * @see
     */
    List<BusinessConfig> searchBusinessConfigs(BusinessConfig bc);
    
    /**
     * 根据具体名称获取配置
     * 
     * @param bc 计划阶段对象
     * @return 
     * @see
     */
    List<BusinessConfig> getBusinessConfigsByDetailNames(BusinessConfig bc);
    
    /**
     * 根据业务配置属性分页搜索
     * 
     * @param bc 计划阶段对象
     * @param page 页码
     * @param rows 每页数量
     * @return
     * @see
     */
    List<BusinessConfig> searchBusinessConfigsForPage(BusinessConfig bc, int page, int rows);
    
    /**
     * 根据业务配置属性分页搜索交付项
     * @param bc 计划阶段对象
     * @param page 页码
     * @param rows 每页数量
     * @param notIn 不包含的id
     * @return
     * @see
     */
    List<BusinessConfig> searchDeliverablesForPage(BusinessConfig bc, int page, int rows, String notIn);
    
    /**
     * 获取交付项数量
     * @param bc 计划阶段对象
     * @param notIn 不包含的id
     * @return
     * @see
     */
    long getDeliverablesCount(BusinessConfig bc, String notIn);
    
    /**
     * 根据搜索条件获取记录总条数
     * 
     * @param bc 计划阶段对象
     * @return 
     * @see
     */
    long getSearchCount(BusinessConfig bc);
    
    /**
     * 增加业务配置
     * 
     * @param bc 计划阶段对象
     * @return 
     * @see
     */
    BusinessConfig add(BusinessConfig bc);
    
    /**
     * 修改业务配置
     * 
     * @param bc 计划阶段对象
     * @return 
     * @see
     */
    BusinessConfig modify(BusinessConfig bc);
    
    /**
     * 逻辑删除
     * 
     * @param bc  计划阶段对象
     * @see
     */
    void logicDelete(BusinessConfig bc);
    
    
    /**
     * 启用/禁用
     * 
     * @param bc 计划阶段对象
     * @param type 操作类型
     * @return 
     * @see
     */
    BusinessConfig startOrStop(BusinessConfig bc, String type);
    
    /**
     * 搜索数据列表
     * 
     * @param conditionList 查询条件
     * @return 
     * @see
     */
    PageList queryEntity(List<ConditionVO> conditionList, boolean isPage);
    
    /**
     * 查询项目分类列表
     * 
     * @param bcon 计划阶段对象
     * @return 
     * @see 
     */
    List<BusinessConfig> searchBusinessConfigAccurate(BusinessConfig bcon);

    /**
     * 查询项目分类列表
     *
     * @param bcon 计划阶段对象
     * @return
     * @see
     */
    String searchBusinessConfigAccurateForStr(BusinessConfig bcon);

    /**
     * 处理excel导入数据
     * 
     * @param dataFromExcel 导入的数据
     * @return 
     * @see
     */
    String doData(List<String> dataFromExcel, String configType,String userId,String orgId);

    /**
     * 校验数据
     * 
     * @param row 计划阶段对象
     * @param strForBc 导入的数据
     * @param errorMsgMap 错误信息
     * @return 
     * @see
     */
    Map<String, String> checkData(int row, String strForBc, Map<String, String> errorMsgMap);
    
    /**
     * 获取配置名
     * 
     * @param configTypeName 配置名称
     * @return 
     * @see
     */
    String getConfigTypeName(String configTypeName);
    
    /**
     * 批量删除
     *
     * @param ids 计划阶段ids
     * @param msg 消息
     * @return 
     * @see
     */
    String doBatchDel(String ids, String msg);
    
    /**
     * 批量更改状态
     * 
     * @param ids 计划阶段ids
     * @param state 状态
     * @return 
     * @see
     */
    void doBatchStartOrStop(String ids, String state);
    
    /**
     * 导入重名校验
     * 
     * @param names 名称
     * @param errorMsgMap 错误信息
     * @return 
     * @see
     */
    Map<String, String> checkImportNames(List<String> names, Map<String, String> errorMsgMap);
    
    /**
     * 导入重编号校验
     * 
     * @param nos 编号
     * @param errorMsgMap 错误信息
     * @return 
     * @see
     */
    Map<String, String> checkImportNos(List<String> nos, Map<String, String> errorMsgMap);

    
    /**
     * 变更类别的查询
     *
     * @return 
     * @see
     */
    List<BusinessConfig> searchTreeNode(BusinessConfig businessConfig);

    /**
     * 变更类别的查询
     *
     * @return 
     * @see
     */
    void getBusinessConfigParentList(BusinessConfig targetNode, List<BusinessConfig> allList,
                                     List<BusinessConfig> parentList);
    /**
     * 变更类别的查询
     *
     * @return 
     * @see
     */
    BusinessConfig getParentNode(BusinessConfig epsConfig);
    /**
     * 根据项目配置的分类  获取该类别下最大位置的信息 属性字段的信息
     * @return
     */
    int getMaxPlace(String configType);
    
    /**
     * 根据项目配置的  获取不同配置下  改排序属性之后字段的所有配置的集合
     * @param rankQuality 配置
     * @return
     */
    List<BusinessConfig> getListByAfter(String rankQuality, String configType);
    
    /**
     * 更具配置的类别 获取相关配置下所有的子配置集合
     * @param bc 计划阶段对象
     * @return
     */
    List<BusinessConfig>  getChildList(BusinessConfig bc);

    /**
     * 项目阶段新增/修改
     * @param config 计划阶段对象
     */
    void businessConfigSaveOrUpdate(BusinessConfig config);

    /**
     * 获取数据列表数据
     * @param cq
     * @param flag
     */
    void getDataGrid(CriteriaQuery cq,boolean flag);

    /**
     * 获取项目阶段对象信息
     * @param id 计划阶段id
     * @return
     */
    BusinessConfig getBusinessConfig(String id);

    /**
     * 通过业务配置类型获取项目阶段信息
     * @param configType 类型
     * @return
     */
    List<BusinessConfig> getBusinessConfigListByConfigType(String configType);

    /**
     * 通过业务配置类型获取所有项目阶段信息（包括逻辑删除的数据）
     * @param configType 类型
     * @return
     */
    List<BusinessConfig> getAllBusinessConfigListByConfigType(String configType);


    /**
     * Description: <br>
     * 获得启用状态的项目阶段
     *
     * @return
     * @see
     */
    String getProjectPhaseList();


}
