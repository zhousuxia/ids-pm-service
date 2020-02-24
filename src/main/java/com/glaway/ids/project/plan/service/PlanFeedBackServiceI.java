package com.glaway.ids.project.plan.service;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.entity.PlanFeedBack;

import java.util.List;
import java.util.Map;

/**
 * @Description: 计划反馈引擎
 * @author: sunmeng
 * @ClassName: PlanFeedBackServiceI
 * @Date: 2019/10/16-14:42
 * @since
 */
public interface PlanFeedBackServiceI extends CommonService {

    /**
     * 获取计划反馈信息
     * @return
     */
    List<PlanFeedBack> getPlanFeedBack();

    /**
     * 自动计算反馈进度
     * @param status 生命周期状态
     * @param delivery 交付物数量
     * @param params 权重数据
     * @return
     */
    Double calculateWeight(String status, int delivery,  Map<String, Integer> number, Map<String, String> params);

    /**
     * 计划状态变自动计算进度
     * @param status 生命周期
     * @param deliveryCount 交付物数量
     * @param deliveryNumber  交付物信息
     * @return
     */
    Double calculateProgressRate(String status, int deliveryCount, Map<String, Integer> deliveryNumber);

    /**
     * 根据相应的生命周期状态获取权重占比
     * @param status 生命周期
     * @return
     */
    int calculateWeightByStatus(String status);

    /**
     * 计划反馈引擎页面信息保存
     * @param params 参数
     * @return
     */
    FeignJson saveFeedBack(Map<String,String> params, String userId);

    /**
     * 计算反馈进度
     * @param map 参数
     * @return
     */
    FeignJson calculateWeightForFeign(Map<String,Object> map);

    /**
     * 通过生命周期状态获取权重值
     * @param status 生命周期状态
     * @return
     */
    String getWeightByStatus(String status);

}
