package com.glaway.ids.project.plan.service;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.ids.project.plan.entity.TempPlanDeliverablesInfo;
import com.glaway.ids.project.plan.entity.TempPlanInputs;

import java.util.List;

/**
 * Created by LHR on 2019/8/6.
 */
public interface DeliverablesChangeServiceI extends BusinessObjectServiceI<TempPlanDeliverablesInfo> {

    /**
     * 获取变更输出列表
     * @param tempPlanDeliverablesInfo 输出变更对象
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     */
    List<TempPlanDeliverablesInfo> queryDeliverableChangeList(TempPlanDeliverablesInfo tempPlanDeliverablesInfo, int page,
                                                              int rows, boolean isPage);

    /**
     * 获取变更输入列表
     * @param deliverablesInfo 输入变更对象
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     */
    List<TempPlanInputs> queryInputChangeList(TempPlanInputs deliverablesInfo, int page, int rows,
                                              boolean isPage);
}
