package com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;

import java.util.List;
import java.util.Map;

/**
 * Created by LHR on 2019/8/26.
 */
public interface ActivityTypeManageEntityServiceI extends CommonService {


    /**
     * 查询活动类型
     * @param conditionList 查询条件
     * @param params 参数
     * @param isPage 是否分页
     * @return
     */
    FeignJson queryEntity(List<ConditionVO> conditionList, Map<String, String> params, boolean isPage);


    /**
     * 保存活动类型
     * @param ac 活动类型对象
     * @return
     */
    String saveActivityTypeManage(ActivityTypeManage ac);

    /**
     * 根据id获取活动类型信息
     * @param id  活动类型id
     * @return
     */
    ActivityTypeManage queryActivityTypeManageById(String id);

    /**
     * 更新活动类型
     * @param ac 活动类型对象
     */
    void updateActivityTypeManage(ActivityTypeManage ac);

    /**
     * 删除互动类型
     * @param id 活动类型id
     */
    void deleteActivityTypeManage(String id);

    /**
     * 启用/禁用活动类型
     * @param it 活动类型id
     * @param status 状态
     */
    void doStartOrStop(String it, String status);

    /**
     * 根据名称获取活动类型
     * @param name 活动类型名称
     * @return
     */
    List<ActivityTypeManage> queryActivityTypeManageByName(String name);

    /**
     * 获取所有未删除的活动类型
     * @return
     */
    List<ActivityTypeManage> getAllActivityTypeManage(Boolean flag);

    /**
     * 获取所有活动类型
     * @return
     */
    List<ActivityTypeManage> getAllActivityTypeManage();

    /**
     * 获取活动类型名称-活动类型集合
     * @return
     */
    Map<String,ActivityTypeManage> getActivityTypeAndNameMap();
}
