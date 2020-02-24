package com.glaway.ids.project.plan.service;

import com.glaway.ids.common.constant.FeignConstants;
import com.glaway.ids.models.JsonResult;
import com.glaway.ids.project.plan.fallback.FeignKnowledgeRefrenceServiceCallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Description: 〈知识参考信息〉
 * @author: wqb
 * @ClassName: ReignKnowledgeReferenceServiceI
 * @Date: 2019年12月24日 16:22:23
 * @since
 */
@FeignClient(value = FeignConstants.KES_KLM_SERVICE,fallbackFactory = FeignKnowledgeRefrenceServiceCallBack.class)
public interface ReignKnowledgeReferenceServiceI {

    @RequestMapping(FeignConstants.KES_KLM_FEIGN_SERVICE+"/feign/knowledgeOperationRestController/savePlanTaskKnowledgeReference.do")
    JsonResult savePlanTaskKnowledgeReference(@RequestBody Map<String, Object> paramsMap);

}
