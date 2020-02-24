package com.glaway.ids.project.plan.service.impl;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.ids.project.plan.entity.TempPlanDeliverablesInfo;
import com.glaway.ids.project.plan.entity.TempPlanInputs;
import com.glaway.ids.project.plan.service.DeliverablesChangeServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LHR on 2019/8/6.
 */
@Service("deliverablesChangeService")
@Transactional(propagation = Propagation.REQUIRED)
public class DeliverablesChangeServiceImpl extends BusinessObjectServiceImpl<TempPlanDeliverablesInfo> implements DeliverablesChangeServiceI {
    @Autowired
    private SessionFacade sessionFacade;

    @Override
    public List<TempPlanDeliverablesInfo> queryDeliverableChangeList(TempPlanDeliverablesInfo deliverablesInfo,
                                                                     int page, int rows, boolean isPage) {
        String hql = createHql(deliverablesInfo);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }

    @Override
    public List<TempPlanInputs> queryInputChangeList(TempPlanInputs deliverablesInfo, int page, int rows, boolean isPage) {
        String hql = createHql2(deliverablesInfo);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }

    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param
     * @return
     * @see
     */
    private String createHql(TempPlanDeliverablesInfo deliverablesInfo) {
        String hql = "from TempPlanDeliverablesInfo l where  1 = 1 ";
        // 计划名称
        if (!StringUtils.isEmpty(deliverablesInfo.getUseObjectId())) {
            hql = hql + " and l.useObjectId = '" + deliverablesInfo.getUseObjectId() + "'";
        }
        if (!StringUtils.isEmpty(deliverablesInfo.getFormId())) {
            hql = hql + " and l.formId = '" + deliverablesInfo.getFormId() + "'";
        }
        if (!StringUtils.isEmpty(deliverablesInfo.getUseObjectType())) {
            hql = hql + " and l.useObjectType = '" + deliverablesInfo.getUseObjectType() + "'";
        }
        // 计划名称
        if (!StringUtils.isEmpty(deliverablesInfo.getName())) {
            hql = hql + " and l.name like '%" + deliverablesInfo.getName() + "%'";
        }
        // 生命周期状态
        if (!StringUtils.isEmpty(deliverablesInfo.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + deliverablesInfo.getBizCurrent() + "'";
        }
        // 是否可用
        if (!StringUtils.isEmpty(deliverablesInfo.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + deliverablesInfo.getAvaliable() + "'";
        }
        return hql;
    }


    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param
     * @return
     * @see
     */
    private String createHql2(TempPlanInputs deliverablesInfo) {
        String hql = "from TempPlanInputs l where  1 = 1 ";
        // 计划名称
        if (!StringUtils.isEmpty(deliverablesInfo.getUseObjectId())) {
            hql = hql + " and l.useObjectId = '" + deliverablesInfo.getUseObjectId() + "'";
        }
        if (!StringUtils.isEmpty(deliverablesInfo.getFormId())) {
            hql = hql + " and l.formId = '" + deliverablesInfo.getFormId() + "'";
        }
        if (!StringUtils.isEmpty(deliverablesInfo.getUseObjectType())) {
            hql = hql + " and l.useObjectType = '" + deliverablesInfo.getUseObjectType() + "'";
        }
        // 计划名称
        if (!StringUtils.isEmpty(deliverablesInfo.getName())) {
            hql = hql + " and l.name like '%" + deliverablesInfo.getName() + "%'";
        }
        return hql;
    }
}
