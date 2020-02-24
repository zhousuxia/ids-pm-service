package com.glaway.ids.project.plan.fallback;

import com.glaway.ids.models.JsonResult;
import com.glaway.ids.project.plan.service.ReignKnowledgeReferenceServiceI;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 〈一句话功能简述〉
 * @author: wqb
 * @ClassName: FeignKnowledgeRefrenceServiceCallBack
 * @Date: 2019年11月29日 13:30:13
 * @since
 */
@Component
public class FeignKnowledgeRefrenceServiceCallBack implements FallbackFactory<ReignKnowledgeReferenceServiceI> {
    @Override
    public ReignKnowledgeReferenceServiceI create(Throwable throwable) {
        return new ReignKnowledgeReferenceServiceI() {
            @Override
            public JsonResult savePlanTaskKnowledgeReference(Map<String, Object> paramsMap) {
                return null;
            }
        };
    }
}
