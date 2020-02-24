package com.glaway.ids.project.plan.service;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.ids.project.plan.entity.PlanLog;
import com.glaway.ids.project.plan.form.PlanLogInfo;

import java.util.List;

/**
 * @Description:  〈计划记录信息〉
 * @author: sunmeng
 * @ClassName: PlanLogServiceI
 * @Date: 2019/7/31-18:30
 * @since
 */
public interface PlanLogServiceI extends CommonService {
    /**
     * Description: <br>
     * 通过计划记录编号获得计划列表<br>
     * Implement: <br>
     * <br>
     *
     * @param planLogInfo 计划日志对象
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     * @see
     */
    List<PlanLog> findPlanLogByPlanId(PlanLogInfo planLogInfo, int page, int rows, boolean isPage);

    /**
     * 检查该计划是否已记录子计划下达日志
     * @param parentId 计划id
     * @return
     */
    boolean checkSonAssignInfo(String parentId);

    /**
     * 保存计划日志
     * @param p 计划日志对象
     */
    void savePlanLog(PlanLog p);

    /**通过id获取对象
     * @param id 计划日志id
     * wqb 2019年9月19日 17:00:53
     */
    PlanLog getPlanLogEntity(String id);
}
