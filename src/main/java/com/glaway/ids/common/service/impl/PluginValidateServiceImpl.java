package com.glaway.ids.common.service.impl;

import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.ids.common.service.PluginValidateServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 插件验证接口实现服务
 * @author blcao
 * @version 2019年1月7日
 * @see PluginValidateServiceImpl
 * @since
 */
@Service("pluginValidateService")
@Transactional
public class PluginValidateServiceImpl implements PluginValidateServiceI{

    /**
     * 接口
     */
    @Autowired
    DiscoveryClient discoveryClient;
   
    @Override
    public boolean isValidatePlugin(String pluginName) {
        boolean pluginValid = false;
        if(!CommonUtil.isEmpty(pluginName)){
            List<ServiceInstance> inss = discoveryClient.getInstances(pluginName);
            if (inss.size()>0) {                
                pluginValid = true;
            }
        }
        return pluginValid;
    }
}
