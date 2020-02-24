package com.glaway.ids.project.plan.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.glaway.ids.project.plan.dto.ResourceDto;
import com.glaway.ids.project.plan.service.ResourceRemoteFeignServiceI;

import feign.hystrix.FallbackFactory;

@Component
public class ResourceRemoteFeignServiceCallBack implements FallbackFactory<ResourceRemoteFeignServiceI> {

	@Override
	public ResourceRemoteFeignServiceI create(Throwable throwable) {
		return new ResourceRemoteFeignServiceI(){

			@Override
			public List<ResourceDto> searchUsables(ResourceDto resourceDto) {
				return null;
			}

			@Override
			public List<ResourceDto> getAllResourceInfos() {
				return null;
			}

            @Override
            public ResourceDto getEntity(String id) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void doBatchDel(String ids) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void delResourceUseByPlan(String planId) {
                // TODO Auto-generated method stub
                
            }
		};
	}
}

