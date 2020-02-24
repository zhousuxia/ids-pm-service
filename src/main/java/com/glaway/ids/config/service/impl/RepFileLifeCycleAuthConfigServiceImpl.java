package com.glaway.ids.config.service.impl;


import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.ids.config.entity.RepFile;
import com.glaway.ids.config.service.RepFileLifeCycleAuthConfigServiceI;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.JsonUtil;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("repFileLifeCycleAuthConfigService")
@Transactional
public class RepFileLifeCycleAuthConfigServiceImpl extends BusinessObjectServiceImpl<RepFile> implements RepFileLifeCycleAuthConfigServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(RepFileLifeCycleAuthConfigServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;


    @Override
    public String queryLifeCyclePolicyList(String policyName) {
        List<LifeCyclePolicy> lifeCyclePolicyEntityList =  sessionFacade.findHql(
                "from LifeCyclePolicy where name = ? ", policyName);
        String json = JsonUtil.getListJsonWithoutQuote(lifeCyclePolicyEntityList);
        return json;
    }

    @Override
    public String queryLifeCycleStatusEntityList(String policyId) {
        List<LifeCycleStatus> lifeCycleStatusEntityList = sessionFacade.findHql(
                "from LifeCycleStatus where 1 = 1 AND lifeCyclePolicy.id = ? ", policyId);
        String json = JsonUtil.getListJsonWithoutQuote(lifeCycleStatusEntityList);
        return json;
    }

    @Override
    public List<LifeCycleStatus> queryLifeCycleStatusByPolicyName(String policyName) {
        List<LifeCycleStatus> lifeCycleStatuses = new ArrayList<>();
        List<LifeCyclePolicy> lifeCyclePolicyEntityList =  sessionFacade.findHql(
                "from LifeCyclePolicy where name = ? ", policyName);
        if (!CommonUtil.isEmpty(lifeCyclePolicyEntityList)) {
            String policyId = lifeCyclePolicyEntityList.get(0).getId();
            lifeCycleStatuses = sessionFacade.findHql(
                    "from LifeCycleStatus where 1 = 1 AND lifeCyclePolicy.id = ?  order by orderNum asc", policyId);
        }
        return lifeCycleStatuses;
    }
}