package com.glaway.ids.project.plan.fallback;

import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.dto.ResourceDto;
import com.glaway.ids.project.plan.dto.ResourceEverydayuseInfoDto;
import com.glaway.ids.project.plan.dto.ResourceLinkInfoDto;
import com.glaway.ids.project.plan.entity.ResourceEverydayuseInfo;
import com.glaway.ids.project.plan.entity.ResourceLinkInfo;
import com.glaway.ids.project.plan.service.ResourceLinkInfoRemoteFeignServiceI;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ResourceLinkInfoRemoteFeignServiceCallBack implements FallbackFactory<ResourceLinkInfoRemoteFeignServiceI> {

	@Override
	public ResourceLinkInfoRemoteFeignServiceI create(Throwable throwable) {
		return new ResourceLinkInfoRemoteFeignServiceI(){


			@Override
			public List<ResourceLinkInfoDto> getList(ResourceLinkInfoDto resourceLinkInfo) {
				return null;
			}

			@Override
			public ResourceLinkInfoDto getResourceLinkInfo(String id) {
				return null;
			}

			@Override
			public void updateResourceEverydayuseInfos(String oldUseObjectId, String newUseObjectId, String projectId) {

			}

			@Override
			public List<ResourceLinkInfoDto> queryResourceList(ResourceLinkInfoDto dto, int page, int rows, boolean isPage) {
				return null;
			}

			@Override
			public void deleteResourceByCondition(ResourceLinkInfoDto resourceLinkInfo) {

			}

			@Override
			public void doAddResourceForPlan(String resourceIds, String planId, String planStartTime, String planEndTime, String useObjectType) {

			}

			@Override
			public List<ResourceEverydayuseInfoDto> queryResourceEverydayuseInfoList(ResourceEverydayuseInfoDto resourceLinkInfo, int page, int rows, boolean isPage) {
				return null;
			}

			@Override
			public void doBatchDelResourceForWork(ResourceLinkInfoDto resourceLinkInfoDto, String planId, String projectId) {

			}

			@Override
			public void saveResourceLinkInfoById(String id,String planId) {

			}

            @Override
            public void updateResourceLinkInfoTimeByDto(List<ResourceLinkInfoDto> res) {
                // TODO Auto-generated method stub
                
            }
		};
	}
}

