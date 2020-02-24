package com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.Impl;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.JsonUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.ActivityTypeManageEntityServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LHR on 2019/8/26.
 */
@Service("activityTypeManageEntityServiceI")
@Transactional
public class ActivityTypeManageEntityServiceImpl extends CommonServiceImpl implements ActivityTypeManageEntityServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    @Override
    public FeignJson queryEntity(List<ConditionVO> conditionList, Map<String, String> params, boolean isPage) {
        try {
            String hql = "";
            if (params.containsKey("type")) {
                hql += "from activityTypeManage where 1=1 and status = 'enable' and avaliable = '1' ";
            } else {
                hql = "from activityTypeManage where 1=1 and avaliable = '1'";
            }
            System.err.println(hql);
            Map<String, String> map = new HashMap<>();
            map.put("activityTypeManage.createTime", "desc");
            List<ActivityTypeManage> list = sessionFacade.searchByCommonFormHql(hql, conditionList, true, map);
            list.forEach(it -> {
                it.setStatusName(it.getStatus().equals("enable") ? "启用" : "禁用");
            });
            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            PageList pageList = new PageList(count, list);
            long count1 = pageList.getCount();
            String json = JsonUtil.getListJsonWithoutQuote(pageList.getResultList());
            String datagridStr = "{\"rows\":" + json + ",\"total\":" + count1 + "}";
            FeignJson feignJson = new FeignJson();
            feignJson.setObj(datagridStr);
            return feignJson;
        } catch (RecognitionException e) {
            new FdExceptionPolicy("basicException", "查询语句格式化异常", e);
        } catch (TokenStreamException e) {
            new FdExceptionPolicy("basicException", "查询语句开闭缺失", e);
        } catch (ClassNotFoundException e) {
            new FdExceptionPolicy("basicException", "查询实体不存在", e);
        }
        return null;
    }

    @Override
    public String saveActivityTypeManage(ActivityTypeManage ac) {
        Serializable id = sessionFacade.save(ac);
        return id.toString();
    }

    @Override
    public ActivityTypeManage queryActivityTypeManageById(String id) {
        return (ActivityTypeManage) sessionFacade.getEntity(ActivityTypeManage.class, id);
    }

    @Override
    public void updateActivityTypeManage(ActivityTypeManage ac) {
        sessionFacade.saveOrUpdate(ac);
    }

    @Override
    public void deleteActivityTypeManage(String id) {
        ActivityTypeManage activityTypeManage = (ActivityTypeManage) sessionFacade.getEntity(ActivityTypeManage.class, id);
        activityTypeManage.setAvaliable("0");
        sessionFacade.saveOrUpdate(activityTypeManage);
    }

    @Override
    public void doStartOrStop(String id, String status) {
        ActivityTypeManage activityTypeManage = this.queryActivityTypeManageById(id);
        activityTypeManage.setStatus(status);
        sessionFacade.saveOrUpdate(activityTypeManage);
    }

    @Override
    public List<ActivityTypeManage> queryActivityTypeManageByName(String name) {
        String hql = "from activityTypeManage where 1=1 and avaliable = '1' and name = ? ";
        List<ActivityTypeManage> list = sessionFacade.findHql(hql, name);
        return list;
    }

    @Override
    public List<ActivityTypeManage> getAllActivityTypeManage(Boolean flag) {
        String hql = "from activityTypeManage t where avaliable='1'";
        if (flag) {
            hql += " and status = 'enable'";
        }
        List<ActivityTypeManage> list = findByQueryString(hql);
        return list;
    }

    @Override
    public List<ActivityTypeManage> getAllActivityTypeManage() {
        String hql = "from activityTypeManage t";
        List<ActivityTypeManage> list = findByQueryString(hql);
        return list;
    }

    @Override
    public Map<String, ActivityTypeManage> getActivityTypeAndNameMap() {
        Map<String, ActivityTypeManage> map = new HashMap<>();
        String hql = "from activityTypeManage t where avaliable='1'";
        List<ActivityTypeManage> list = findByQueryString(hql);
        if(!CommonUtil.isEmpty(list)){
            for(ActivityTypeManage tag : list){
               map.put(tag.getName(),tag);
            }
        }
        return map;
    }
}
