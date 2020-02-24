package com.glaway.ids.config.service;


import java.util.List;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.ids.config.entity.ParamSwitch;

/**
 * 项目计划参数接口
 * @author xshen
 * @version 2015年4月16日
 * @see ParamSwitchServiceI
 * @since
 */
public interface ParamSwitchServiceI extends BusinessObjectServiceI<ParamSwitch> {

    /**
     * 根据开关名称获取开关状态
     * 
     * @param switchName 名称
     * @return
     * @see
     */
    String getSwitch(String switchName);

    /**
     * 搜索参数列表
     *
     * @param paramSwitch 参数对象
     * @return
     * @see
     */
    List<ParamSwitch> search(ParamSwitch paramSwitch);

    /**
     * 搜索参数列表(研发流程配置)
     * @return
     * @see
     */
    List<ParamSwitch> searchProconfigParamSwitch();
    
    /**设置状态
     * @param status 状态
     * @param id  参数id
     * @see
     */
    void updateStatusById(String status,String id);

}
