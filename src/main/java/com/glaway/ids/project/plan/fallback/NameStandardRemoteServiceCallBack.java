package com.glaway.ids.project.plan.fallback;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.glaway.ids.project.plan.dto.DeliveryStandardDto;
import com.glaway.ids.project.plan.dto.NameStandardDeliveryRelationDto;
import com.glaway.ids.project.plan.dto.NameStandardDto;
import com.glaway.ids.project.plan.service.NameStandardRemoteFeignServiceI;

import feign.hystrix.FallbackFactory;

@Component
public class NameStandardRemoteServiceCallBack implements FallbackFactory<NameStandardRemoteFeignServiceI> {

	@Override
	public NameStandardRemoteFeignServiceI create(Throwable throwable) {
		return new NameStandardRemoteFeignServiceI(){


			@Override
			public List<NameStandardDto> searchNameStandardsExceptDesign(NameStandardDto nameStandard) {
				return null;
			}

			@Override
			public List<NameStandardDto> searchNameStandards(NameStandardDto nameStandardDto) {
				return null;
			}

			@Override
			public List<DeliveryStandardDto> searchDeliverablesForPage(DeliveryStandardDto ds, int page, int rows, String notIn) {
				return null;
			}

			@Override
			public List<DeliveryStandardDto> searchDeliveryStandardsForPage(DeliveryStandardDto ds, int page, int rows) {
				return null;
			}

			@Override
			public long getDeliverablesCount(DeliveryStandardDto ds, String notIn) {
				return 0;
			}

			@Override
			public long getSearchCount(DeliveryStandardDto ds) {
				return 0;
			}

			@Override
			public DeliveryStandardDto getDeliveryStandardEntity(String id) {
				return null;
			}

            @Override
            public List<DeliveryStandardDto> searchDeliveryStandards(DeliveryStandardDto ds) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<NameStandardDeliveryRelationDto> searchForPage(NameStandardDeliveryRelationDto dto,
                                                                       int page, int rows) {
                // TODO Auto-generated method stub
                return null;
            }

			@Override
			public Map<String, String> getNameDeliverysMap() {
				return null;
			}

			@Override
            public List<NameStandardDto> searchNameStandardsAccurate(NameStandardDto ns) {
                // TODO Auto-generated method stub
                return null;
            }
		};
	}
}

