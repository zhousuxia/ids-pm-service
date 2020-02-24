package com.glaway.ids.project.plan.service;

import com.glaway.ids.common.constant.FeignConstants;
import com.glaway.ids.project.plan.entity.ResourceEverydayuseInfo;
import com.glaway.ids.project.plan.fallback.ResourceEverydayuseFeignServiceICallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: ResourceEverydayuseFeignServiceI
 * @Date: 2019/7/29-19:36
 * @since
 */
@FeignClient(value = FeignConstants.ID_COMMON_SERVICE,fallback = ResourceEverydayuseFeignServiceICallback.class)
public interface ResourceEverydayuseFeignServiceI {

    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceEverydayuseRestController/delResourceUseByPlan.do")
    void delResourceUseByPlan(@RequestParam(value = "id",required = false) String id);

    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceEverydayuseRestController/delResourceUseByPlanAndOperationTime.do")
    void delResourceUseByPlanAndOperationTime(@RequestParam(value = "id") String id, @RequestBody Date invalidTime);

    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceEverydayuseRestController/deleteResourceEverydayuseInfos.do")
    void deleteResourceEverydayuseInfos(@RequestBody ResourceEverydayuseInfo info);
}


