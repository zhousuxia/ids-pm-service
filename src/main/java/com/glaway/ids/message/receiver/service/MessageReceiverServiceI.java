package com.glaway.ids.message.receiver.service;


import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.CommonService;

import java.util.List;


/**
 * 获取消息接收人
 * 
 * @author blcao
 * @version 2016年6月15日
 * @see MessageReceiverServiceI
 * @since
 */
public interface MessageReceiverServiceI extends CommonService {

    /**
     * 获取项目的所有成员
     * 
     * @param projectId
     * @return
     */
    List<TSUserDto> getProjectMembers(String projectId);

    /**
     * 获取父计划负责人
     * 
     * @param planId
     * @return
     */
    List<TSUserDto> getParentPlanOwner(String planId);

    /**
     * 获取子计划负责人
     * 
     * @param planId
     * @return
     */
    List<TSUserDto> getSonPlanOwner(String planId);

    /**
     * 获取后置计划负责人
     * 
     * @param planId
     * @return
     */
    List<TSUserDto> getBackPlanOwner(String planId);

    /**
     * 获取消息接收人
     * 
     * @param type
     *            :项目状态更新|计划变更|计划完工|计划废弃
     * @param businessId
     * @return
     */
    List<TSUserDto> getMessageReceivers(String type, String businessId);
}
