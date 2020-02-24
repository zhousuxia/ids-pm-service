package com.glaway.ids.project.plan.service;


import com.glaway.ids.common.constant.FeignConstants;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.dto.ResourceDto;
import com.glaway.ids.project.plan.dto.ResourceEverydayuseInfoDto;
import com.glaway.ids.project.plan.dto.ResourceLinkInfoDto;
import com.glaway.ids.project.plan.entity.ResourceEverydayuseInfo;
import com.glaway.ids.project.plan.entity.ResourceLinkInfo;
import com.glaway.ids.project.plan.fallback.ResourceLinkInfoRemoteFeignServiceCallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 任务输入
 * 
 * @author blcao
 * @version 2015年7月6日
 * @see ResourceLinkInfoRemoteFeignServiceI
 * @since
 */

@FeignClient(value = FeignConstants.ID_COMMON_SERVICE,fallback = ResourceLinkInfoRemoteFeignServiceCallBack.class)
public interface ResourceLinkInfoRemoteFeignServiceI {


    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/ResourceRestController/getList.do")
    List<ResourceLinkInfoDto> getList(@RequestBody ResourceLinkInfoDto resourceLinkInfo);

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceLinkInfoRestController/getResourceLinkInfoEntity.do")
    ResourceLinkInfoDto getResourceLinkInfo(@RequestParam(value = "id",required = false) String id);

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/ResourceRestController/updateResourceEverydayuseInfos.do")
    void updateResourceEverydayuseInfos(@RequestParam("oldUseObjectId")  String oldUseObjectId,
                                        @RequestParam("newUseObjectId")  String newUseObjectId, @RequestParam("projectId")  String projectId);

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceLinkInfoRestController/queryResourceList.do")
    List<ResourceLinkInfoDto> queryResourceList(@RequestBody ResourceLinkInfoDto dto, @RequestParam(value = "page") int page,
                                                @RequestParam(value = "rows") int rows, @RequestParam(value = "isPage") boolean isPage);

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceLinkInfoRestController/deleteResourceByCondition.do")
    void deleteResourceByCondition(@RequestBody ResourceLinkInfoDto resourceLinkInfo);

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceLinkInfoRestController/queryResourceEverydayuseInfoList.do")
    List<ResourceEverydayuseInfoDto> queryResourceEverydayuseInfoList(@RequestBody ResourceEverydayuseInfoDto resourceLinkInfo,
                                                                   @RequestParam(value = "page") int page, @RequestParam(value = "rows")int rows,
                                                                   @RequestParam(value = "isPage")boolean isPage);

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceLinkInfoRestController/saveResourceLinkInfoById.do")
    void saveResourceLinkInfoById(@RequestParam(value = "id",required = false) String id,@RequestParam(value = "planId",required = false) String planId);

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceLinkInfoRestController/doAddResourceForPlan.do")
    void doAddResourceForPlan(@RequestParam(value = "resourceIds",required = false) String resourceIds,@RequestParam(value = "planId",required = false) String planId,
                              @RequestParam(value = "planStartTime",required = false) String planStartTime,@RequestParam(value = "planEndTime",required = false) String planEndTime,
                              @RequestParam(value = "useObjectType",required = false) String useObjectType);
    
    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceLinkInfoRestController/updateResourceLinkInfoTimeByDto.do")
    void updateResourceLinkInfoTimeByDto(@RequestBody List<ResourceLinkInfoDto> res);

    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/resourceLinkInfoRestController/doBatchDelResourceForWork.do")
    void doBatchDelResourceForWork(@RequestBody ResourceLinkInfoDto resourceLinkInfoDto,@RequestParam(value = "planId",required = false) String planId,@RequestParam(value = "projectId",required = false) String projectId);
}
