package com.glaway.ids.review.service;

import com.glaway.ids.common.constant.FeignConstants;
import feign.Feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.glaway.ids.review.fallback.ReviewFeignServiceFallback;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: ReviewFeignServiceI
 * @Date: 2019/8/13-16:02
 * @since
 */
@FeignClient(value = FeignConstants.IDS_REVIEW_SERVICE,fallbackFactory = ReviewFeignServiceFallback.class)
public interface ReviewFeignServiceI {
    /**保存评审任务相关信息
     * @param jsonStr 评审任务基本信息vo的jsonStr
     * @see
     */
    @RequestMapping(FeignConstants.IDS_REVIEW_FEIGN_SERVICE+"/feign/reviewSupportRestController/saveReviewBaceInfo.do")
    void saveReviewBaceInfo(@RequestParam(value ="jsonStr",required = false) String jsonStr);

    /**修改评审任务相关信息
     * @param jsonStr 评审任务基本信息vo的jsonStr
     * @see
     */
    @RequestMapping(FeignConstants.IDS_REVIEW_FEIGN_SERVICE+"/feign/reviewSupportRestController/updateReviewBaceInfo.do")
    void updateReviewBaceInfo(@RequestParam(value ="jsonStr",required = false) String jsonStr);
}
