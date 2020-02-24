package com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.impl;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.ids.constant.PlanGeneralConstants;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.TabCbTemplateEntityServiceI;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: TabCbTemplateEntityServiceImpl
 * @Date: 2019/8/29-19:01
 * @since
 */
@Service("tabCbTemplateEntityService")
@Transactional
public class TabCbTemplateEntityServiceImpl extends CommonServiceImpl implements TabCbTemplateEntityServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    @Override
    public TabCombinationTemplate findTabCbTempById(String id) {
        return (TabCombinationTemplate)sessionFacade.getEntity(TabCombinationTemplate.class,id);
    }

    @Override
    public List<TabCombinationTemplate> findTabCbTemplatesByActivityId(String id) {
        String hql = "from TabCombinationTemplate t where 1=1 and t.avaliable='1' and t.bizCurrent = '" + PlanGeneralConstants.TABCBTEMPLATE_QIYONG + "'";
        List<TabCombinationTemplate> templates = new ArrayList<>();
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            if (ids.length > 1) {
                List<String> idsList = Arrays.asList(ids);
                List<String> idsLists = new ArrayList<>();
                idsList.forEach(str -> {
                    idsLists.add("'"+str+"'");
                });
                String activityId = String.join(",",idsLists);
                hql += " and t.activityId in ("+activityId+")";
            } else {
                hql += " and t.activityId = '"+ id +"'";

            }
            templates = findByQueryString(hql);
        }
        return templates;
    }

    @Override
    public List<TabCombinationTemplate> findAllTabCbTemplatesByActivityId(String id, String templateId) {
        String hql = "from TabCombinationTemplate t where 1=1 and t.avaliable='1'";
        List<TabCombinationTemplate> templates = new ArrayList<>();
        if (StringUtils.isNotBlank(templateId)) {
            hql += " and t.id != '"+ templateId +"'";
        }
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            if (ids.length > 1) {
                List<String> idsList = Arrays.asList(ids);
                List<String> idsLists = new ArrayList<>();
                idsList.forEach(str -> {
                    idsLists.add("'"+str+"'");
                });
                String activityId = String.join(",",idsLists);
                hql += " and t.activityId in ("+activityId+")";
            } else {
                hql += " and t.activityId = '"+ id +"'";

            }
            templates = findByQueryString(hql);
        }
        return templates;
    }

    @Override
    public String saveTabCombationTemplate(TabCombinationTemplate template) {
        Serializable id = sessionFacade.save(template);
        return id.toString();
    }

    @Override
    public List<TabCombinationTemplate> queryEntity(List<ConditionVO> conditionList, boolean isPage) {
        try {
            ConditionVO ava = new ConditionVO();
            ava.setCondition("eq");
            ava.setKey("TabCombinationTemplate.avaliable");
            ava.setValue("1");
            conditionList.add(ava);

            Map<String, String> aliasMap = new HashMap<String, String>();
            aliasMap.put("TabCombinationTemplate.createTime", "desc");
            String hql = "from TabCombinationTemplate t ";
            List<TabCombinationTemplate> list = sessionFacade.searchByCommonFormHql(hql, conditionList, isPage, aliasMap);
            return list;
          /*  List<TabCombinationTemplateDto> dtoList = new ArrayList<>();
            dtoList = (List<TabCombinationTemplateDto>) VDataUtil.getVDataByEntity(list,TabCombinationTemplateDto.class);
            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            PageList pageList = new PageList(count, dtoList);
            return pageList;*/
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
    public int getCount(List<ConditionVO> conditionList) {
        try {
            ConditionVO ava = new ConditionVO();
            ava.setCondition("eq");
            ava.setKey("TabCombinationTemplate.avaliable");
            ava.setValue("1");
            conditionList.add(ava);

            Map<String, String> aliasMap = new HashMap<String, String>();
            aliasMap.put("TabCombinationTemplate.createTime", "desc");
            String hql = "from TabCombinationTemplate t ";
            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            return count;
        } catch (RecognitionException e) {
            new FdExceptionPolicy("basicException", "查询语句格式化异常", e);
        } catch (TokenStreamException e) {
            new FdExceptionPolicy("basicException", "查询语句开闭缺失", e);
        } catch (ClassNotFoundException e) {
            new FdExceptionPolicy("basicException", "查询实体不存在", e);
        }
        return 0;
    }

    @Override
    public List<String> queryAllName() {
        List<String> list = new ArrayList<>();
        String hql = "from TabCombinationTemplate t where t.avaliable='1'";
        List<TabCombinationTemplate> infos = sessionFacade.findHql(hql);
        if (!CollectionUtils.isEmpty(infos)){
            for (TabCombinationTemplate tabCombinationTemplate:infos){
                list.add(tabCombinationTemplate.getName());
            }
        }
        return list;
    }


    @Override
    public String queryIdByName(String name) {
        String id = "";
        String hql = "from TabCombinationTemplate t where t.avaliable='1' and t.name='" + name + "'";
        List<TabCombinationTemplate> infos = sessionFacade.findHql(hql);
        if (!CollectionUtils.isEmpty(infos)){
            for (TabCombinationTemplate tabCombinationTemplate:infos){
                id = tabCombinationTemplate.getId();
            }
        }
        return id;
    }

    @Override
    public <T> List<T> getVersionHistory(String bizId, Integer pageSize, Integer pageNum) {
        List<T> list = new ArrayList<T>();
        StringBuilder hql = new StringBuilder("");
        hql.append(" from TabCombinationTemplate t where t.bizId=? order by t.createTime desc");
        String[] params = {bizId};

        if (pageSize != null) {
            list = sessionFacade.pageList(hql.toString(), params, (pageNum - 1) * pageSize, pageSize);
        }
        else {
            list = sessionFacade.executeQuery(hql.toString(), params);
        }
        return list;
    }

    @Override
    public long getVersionCount(String bizId) {
        StringBuilder hql = new StringBuilder("");
        hql.append(" select count(*) from TabCombinationTemplate t  where t.bizId=?");
        String[] params = {bizId};
        return sessionFacade.getCount(hql.toString(), params);
    }

    @Override
    public List<TabCombinationTemplate> findTemplatesByBizIdOrBizVersion(String bizId, String bizversion) {
        StringBuilder sb = new StringBuilder("from TabCombinationTemplate t where 1=1");
        if (StringUtils.isNotBlank(bizId)) {
            sb.append(" and t.bizId = '" + bizId + "'");
        }
        if (StringUtils.isNotBlank(bizversion)) {
            sb.append(" and t.bizVersion like '%" + bizversion + "%'");
        }
        sb.append(" order by t.createTime desc");
        List<TabCombinationTemplate> templates = findHql(sb.toString());
        return templates;
    }

    @Override
    public TabCombinationTemplate findAvaliableTabCbTemplate(String bizId) {
        TabCombinationTemplate avaliableTabCbTemplate = (TabCombinationTemplate)findHql("from TabCombinationTemplate t where t.bizId=? and t.avaliable=1", bizId).get(0);
        return avaliableTabCbTemplate;
    }

    @Override
    public Map<String, String> getTabCbTempIdAndActivityIdMap() {
        Map<String, String> map = new HashMap<>();
        String hql = "from TabCombinationTemplate t where 1=1 and t.avaliable='1' and t.bizCurrent = '" + PlanGeneralConstants.TABCBTEMPLATE_QIYONG + "'";
        List<TabCombinationTemplate> tabList = sessionFacade.findByQueryString(hql);
        if(!CommonUtil.isEmpty(tabList)){
            for(TabCombinationTemplate tab : tabList){
                map.put(tab.getActivityId(),tab.getId());
            }
        }
        return map;
    }
}
