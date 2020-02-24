package com.glaway.ids.project.plan.service;


import java.util.List;

import com.glaway.ids.common.constant.FeignConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.glaway.ids.project.plan.dto.ResourceDto;
import com.glaway.ids.project.plan.fallback.ResourceRemoteFeignServiceCallBack;


/**
 * 资源管理接口
 * @author wangyangzan
 * @version 2016年1月7日
 * @see ResourceRemoteFeignServiceI
 * @since
 */

@FeignClient(value = FeignConstants.ID_COMMON_SERVICE,fallback = ResourceRemoteFeignServiceCallBack.class)
public interface ResourceRemoteFeignServiceI {


    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/ResourceRestController/getAllResourceInfos.do")
    List<ResourceDto> getAllResourceInfos();

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/ResourceRestController/searchUsables.do")
    List<ResourceDto> searchUsables(@RequestBody ResourceDto resourceDto);
    
    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/ResourceRestController/getEntity.do")
    ResourceDto getEntity(@RequestParam(value = "id",required = false) String id);
    
    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/ResourceRestController/doBatchDel.do")
    void doBatchDel(@RequestParam(value = "ids",required = false) String ids);
    
    /**
     * 删除计划时，删除计划相关的资源
     * @param planId
     */
    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/ResourceRestController/delResourceUseByPlan.do")
    void delResourceUseByPlan(@RequestParam(value = "planId",required = false)  String planId);
}
