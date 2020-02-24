package com.glaway.ids.project.plan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plan.entity.PlanLog;
import com.glaway.ids.project.plan.form.PlanLogInfo;
import com.glaway.ids.project.plan.service.PlanLogServiceI;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: PlanLogServiceImpl
 * @Date: 2019/7/31-18:32
 * @since
 */
@Service("planLogService")
@Transactional
public class PlanLogServiceImpl extends CommonServiceImpl implements PlanLogServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(PlanLogServiceImpl.class);
    
    @Autowired
    private SessionFacade sessionFacade;

    /**
     * Description: 组装查询方法
     *
     * @return
     * @see
     */
    private Map<String, Object> getQueryParam(StringBuilder hql, PlanLogInfo planLogInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        hql.append(" where t.avaliable!=0");

        List<Object> paramList = new ArrayList<Object>();
        if (planLogInfo != null) {
            if (StringUtils.isNotEmpty(planLogInfo.getPlanId())) {
                hql.append(" and t.planId = ?");
                paramList.add(planLogInfo.getPlanId());
            }
        }
        hql.append(" order by t.createTime");
        resultMap.put("hql", hql.toString());
        resultMap.put("queryList", paramList);
        return resultMap;
    }

    @Override
    public List<PlanLog> findPlanLogByPlanId(PlanLogInfo planLogInfo, int page, int rows,
                                             boolean isPage) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from PlanLog t");
        Map<String, Object> resultMap = getQueryParam(hql, planLogInfo);
        String hqlStr = (String)resultMap.get("hql");
        List<PlanLog> paramList = (List<PlanLog>)resultMap.get("queryList");
        List<PlanLog> list = new ArrayList<PlanLog>();
        if (isPage) {
            list = pageList(hqlStr, paramList.toArray(), (page - 1) * rows, rows);
        }
        else {
            list = executeQuery(hqlStr, paramList.toArray());
        }
        return list;
    }

    @Override
    public boolean checkSonAssignInfo(String parentId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from PlanLog t");
        hql.append(" where t.planId = '" + parentId + "'");
        hql.append(" and t.logInfo = '" + PlanConstants.PLAN_LOGINFO_SON_ORDERED + "'");
        List<PlanLog> list = findByQueryString(hql.toString());
        if (!CommonUtil.isEmpty(list)) {
            return true;
        }
        return false;
    }
    
    @Override
    public void savePlanLog(PlanLog p) {
        sessionFacade.save(p);
    }
    
    @Override
    public PlanLog getPlanLogEntity(String id) {
        return (PlanLog)sessionFacade.getEntity(PlanLog.class, id);
    };
}
