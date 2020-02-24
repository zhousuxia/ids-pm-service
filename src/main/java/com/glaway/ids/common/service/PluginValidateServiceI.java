package com.glaway.ids.common.service;



/**
 * 
 * 插件验证接口服务
 * @author blcao
 * @version 2019年1月7日
 * @see PluginValidateServiceI
 * @since
 */
public interface PluginValidateServiceI {
    
    /**
     * 
     * 插件是否有效
     * 
     * @param pluginName 插件名称
     * @return 
     * @see
     */
    boolean isValidatePlugin(String pluginName);
}
