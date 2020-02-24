package com.glaway.ids.config.service;


import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.config.entity.RepFile;
import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;

import java.util.List;

/**
 * 配置EPS接口
 * 
 * @author wangshen
 * @version 2015年5月26日
 * @see RepFileLifeCycleAuthConfigServiceI
 * @since
 */
public interface RepFileLifeCycleAuthConfigServiceI extends BusinessObjectServiceI<RepFile> {

    /**
     * 获取文档的生命周期列表
     * @param policyName 生命周期策略名称
     * @return
     */
    String queryLifeCyclePolicyList(String policyName);

    /**
     * 获取文档生命周期状态列表
     * @param policyId 生命周期策略id
     * @return
     */
    String queryLifeCycleStatusEntityList(String policyId);


    /**
     * 根据生命周期策略名称获取生命周期状态
     * @param policyName 生命周期策略名称
     * @return
     */
    List<LifeCycleStatus> queryLifeCycleStatusByPolicyName(String policyName);
}
