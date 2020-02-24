package com.glaway.ids.config.service;


import java.util.List;

import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.config.entity.EpsConfig;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;

/**
 * 配置EPS接口
 * 
 * @author wangshen
 * @version 2015年5月26日
 * @see EpsConfigServiceI
 * @since
 */
public interface EpsConfigServiceI extends BusinessObjectServiceI<EpsConfig> {

    /**
     * 获取项目分类信息
     * @param id 项目分类id
     * @return
     */
    String getEpsConfig(String id);

    /**
     * 新增节点
     *
     * @param epsConfig 项目分类对象
     * @return
     * @see
     */
    EpsConfig add(EpsConfig epsConfig);

    /**
     * 项目分类中 获取最大项目分类的位置序号
     * @return
     */
    int getMaxEpsConfigPlace();

    /**
     * 获取项目分类  位置之后的项目分类信息
     * @param rankQuality 项目分类排序属性
     * @return
     */
    List<EpsConfig> getAfterEpsList(String rankQuality);
    
    
    /**
     * 查询树节点
     * 
     * @return
     * @see
     */
    String searchTreeNode(EpsConfig epsConfig);

    /**
     * 查询树节点
     *
     * @return
     * @see
     */
    List<EpsConfig> searchTreeNodeList(EpsConfig epsConfig);

    
    /**
     * 根据ID获得所有子节点
     * @return
     * @see
     */
    void getEpsParentList(EpsConfig targetNode, List<EpsConfig> allList,
                                   List<EpsConfig> parentList);
    
    /**
     * 新增/修改项目分类
     * @param epsConfig 项目分类对象
     */
    void saveOrUpdateEpsConfig(EpsConfig epsConfig);

    /**
     *
     * 获取父节点
     * @param epsConfig 项目分类对象
     * @return
     */
    String getParentNode(EpsConfig epsConfig);


    /**
     * 修改节点
     * @param epsConfig 项目分类对象
     * @param nochange 是否修改
     * @return
     * @see
     */
    EpsConfig modify(EpsConfig epsConfig,boolean nochange);

    /**
     * 条件查询
     * @param epsConfig 项目分类对象
     * @return
     */
    String getEpsConfigList(EpsConfig epsConfig);


    /**
     * 批量删除
     * @param ids 项目分类ids
     * @return
     * @see
     */
    void doBatchDel(String ids);

    /**
     * 逻辑删除节点
     * @param epsConfig 项目分类对象
     * @return
     * @see
     */
    void logicDelete(EpsConfig epsConfig);


    /**
     * 批量修改状态
     * @param ids 项目分类ids
     * @return
     * @see
     */
    void doBatchStartOrStop(String ids, String state);


    /**
     * 启用或禁用
     * @param epsConfig 项目分类对象
     * @return
     * @see
     */
    EpsConfig startOrStop(EpsConfig epsConfig,String type);

    /**
     * 获取数据列表
     * @return
     */
    String getList();


    /**
     * 获取id获得名称路径名
     *
     * @param id 项目分类id
     * @see
     */
    String getEpsNamePathById(String id);


    /**
     * Description: <br>
     * 获取EPS项目分类树
     *
     * @return
     * @see
     */
    String getEpsTreeNodes();

    /**
     * 获取树
     * @param epsConfig 项目分类对象
     * @return
     */
    public String getTreeNodes(EpsConfig epsConfig);

    /**
     * 批量删除项目分类判断其下有没有子节点
     *
     * @param ids 项目分类ids
     * @return
     */
    FeignJson doBatchDelIsHaveChildList(String ids);

    /**
     * 更新项目分类
     *
     * @param epsConfig 项目分类对象
     * @return
     */
    FeignJson doUpdate(EpsConfig epsConfig);
    
}
