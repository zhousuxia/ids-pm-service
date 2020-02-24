package com.glaway.ids.project.plan.fallback;

import com.glaway.ids.project.plan.dto.NameStandardDeliveryRelationDto;
import com.glaway.ids.project.plan.dto.NameStandardDto;
import com.glaway.ids.project.plan.service.NameStandardFeignService;
import feign.hystrix.FallbackFactory;

import java.util.List;
import java.util.Map;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: NameStandardFeignServiceCallback
 * @Date: 2019/7/30-10:04
 * @since
 */
public class NameStandardFeignServiceCallback implements FallbackFactory<NameStandardFeignService> {
    @Override
    public NameStandardFeignService create(Throwable throwable) {
        return new NameStandardFeignService() {
            @Override
            public List<NameStandardDto> searchNameStandardsAccurate(NameStandardDto nameStandardDto) {
                return null;
            }

            @Override
            public Map<String, String> getNameDeliverysMap() {
                return null;
            }

            @Override
            public List<NameStandardDeliveryRelationDto> searchNoPage(NameStandardDeliveryRelationDto relation) {
                return null;
            }

            @Override
            public List<NameStandardDto> searchNameStandards(NameStandardDto nameStandardDto) {
                return null;
            }
        };
    }
}
