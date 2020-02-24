package com.glaway.ids.project.plan.service;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.ids.project.plan.entity.TempPlanResourceLinkInfo;

import java.util.List;

/**
 * Created by LHR on 2019/8/6.
 */
public interface ResourceChangeServiceI extends BusinessObjectServiceI<TempPlanResourceLinkInfo> {

    /**
     * 获取资源变更列表
     * @param tempPlanResourceLinkInfo 变更资源关联对象表
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     */
    List<TempPlanResourceLinkInfo> queryResourceChangeList(TempPlanResourceLinkInfo tempPlanResourceLinkInfo, int page,
                                                           int rows, boolean isPage);
}
