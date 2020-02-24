package com.glaway.ids.project.plan.fallback;

import com.glaway.ids.project.plan.entity.ResourceEverydayuseInfo;
import com.glaway.ids.project.plan.service.ResourceEverydayuseFeignServiceI;
import feign.hystrix.FallbackFactory;

import java.util.Date;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: ResourceEverydayuseFeignServiceICallback
 * @Date: 2019/7/29-19:40
 * @since
 */
public class ResourceEverydayuseFeignServiceICallback implements FallbackFactory<ResourceEverydayuseFeignServiceI> {
    @Override
    public ResourceEverydayuseFeignServiceI create(Throwable throwable) {
        return new ResourceEverydayuseFeignServiceI() {
            @Override
            public void delResourceUseByPlan(String id) {

            }

            @Override
            public void delResourceUseByPlanAndOperationTime(String id, Date invalidTime) {

            }

            @Override
            public void deleteResourceEverydayuseInfos(ResourceEverydayuseInfo info) {

            }
        };
    }
}
