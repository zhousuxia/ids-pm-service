package com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageBusinessServiceI;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.fdk.dev.common.FeignJson;

import java.util.List;
import java.util.Map;

/**
 * Created by LHR on 2019/8/26.
 */
public interface ActivityTypeManageBusinessServiceI extends CommonService {

    /**
     * 保存活动类型
     * @param userId 用户id
     * @param name 活动类型名称
     * @param remark 活动类型备注
     * @param id 活动类型id
     * @return
     */
    FeignJson doAddActivityTypeManage(String userId, String name, String remark, String id);

    /**
     * 批量删除活动类型
     * @param userId 用户id
     * @param ids 活动类型ids
     * @return
     */
    FeignJson doDeleteBatch(String userId, String ids);

    /**
     * 启用/禁用活动类型
     * @param userId 用户id
     * @param ids 活动类型ids
     * @param status 状态
     * @return
     */
    FeignJson doStartOrStop(String userId, String ids, String status);
    
    /**活动类型批量删除前的校验
     * @param ids 活动类型ids
     * wqb 2019年9月5日 17:59:12
     */
    FeignJson deleteBatchBeforeCheckDate(String ids);
}
