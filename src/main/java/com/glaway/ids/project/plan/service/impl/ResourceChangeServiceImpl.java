package com.glaway.ids.project.plan.service.impl;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.ids.project.plan.entity.TempPlanResourceLinkInfo;
import com.glaway.ids.project.plan.service.ResourceChangeServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LHR on 2019/8/6.
 */
@Service("resourceChangeService")
@Transactional(propagation = Propagation.REQUIRED)
public class ResourceChangeServiceImpl  extends BusinessObjectServiceImpl<TempPlanResourceLinkInfo> implements ResourceChangeServiceI {
    @Autowired
    private SessionFacade sessionFacade;

    @Override
    public List<TempPlanResourceLinkInfo> queryResourceChangeList(TempPlanResourceLinkInfo deliverablesInfo,
                                                                  int page, int rows, boolean isPage) {
        String hql = createHqlOrderBy(deliverablesInfo);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }

    private String createHqlOrderBy(TempPlanResourceLinkInfo resourceLinkInfo){
        String hql = "from TempPlanResourceLinkInfo l  where  1 = 1 ";
        //  资源名称
        if(!StringUtils.isEmpty(resourceLinkInfo.getResourceName())){
            hql = hql + " and l.resourceInfo.name like '%" + resourceLinkInfo.getResourceName() + "%'";
        }
        //  资源ID
        if(!StringUtils.isEmpty(resourceLinkInfo.getResourceId())){
            hql = hql + " and l.resourceId = '" + resourceLinkInfo.getResourceId() + "'";
        }
        if (!StringUtils.isEmpty(resourceLinkInfo.getUseObjectId())) {
            hql = hql + " and l.useObjectId = '" + resourceLinkInfo.getUseObjectId() + "'";
        }

        // 生命周期状态
        if (!StringUtils.isEmpty(resourceLinkInfo.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + resourceLinkInfo.getBizCurrent() + "'";
        }
        //是否可用
        if (!StringUtils.isEmpty(resourceLinkInfo.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + resourceLinkInfo.getAvaliable() + "'";
        }
        hql = hql + " order by l.resourceLinkId";
        return hql;
    }
}
