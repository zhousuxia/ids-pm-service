package com.glaway.ids.project.plan.service;

import com.glaway.ids.common.constant.FeignConstants;
import com.glaway.ids.project.plan.dto.NameStandardDeliveryRelationDto;
import com.glaway.ids.project.plan.dto.NameStandardDto;
import com.glaway.ids.project.plan.fallback.NameStandardFeignServiceCallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: NameStandardFeignService
 * @Date: 2019/7/30-10:04
 * @since
 */
@FeignClient(value = FeignConstants.ID_COMMON_SERVICE,fallback = NameStandardFeignServiceCallback.class)
public interface NameStandardFeignService {

    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/nameStandardRestController/searchNameStandardsAccurate.do")
    List<NameStandardDto> searchNameStandardsAccurate(@RequestBody NameStandardDto nameStandardDto);

    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/nameStandardRestController/getNameDeliverysMap.do")
    Map<String, String> getNameDeliverysMap();

    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/nameStandardRestController/searchNameStandards.do")
    List<NameStandardDto> searchNameStandards(@RequestBody NameStandardDto nameStandardDto);

    @RequestMapping(FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/nameStandardRestController/searchNoPage.do")
    List<NameStandardDeliveryRelationDto> searchNoPage(@RequestBody NameStandardDeliveryRelationDto relation);
}
