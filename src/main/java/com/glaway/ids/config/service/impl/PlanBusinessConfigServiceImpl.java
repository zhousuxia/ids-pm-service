package com.glaway.ids.config.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.config.constant.ConfigStateConstants;
import com.glaway.ids.config.constant.ConfigTypeConstants;
import com.glaway.ids.config.constant.SerialNumberConstants;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.foundation.core.common.extend.hqlsearch.HqlGenerateUtil;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.tag.vo.datatable.SortDirection;
import com.glaway.ids.project.plan.dto.BusinessConfigDto;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.CommonInitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.system.serial.SerialNumberManager;



/**
 * 配置接口实现
 * 
 * @author xshen
 * @version 2015年3月26日
 * @see PlanBusinessConfigServiceImpl
 * @since
 */
@Service("planBusinessConfigService")
@Transactional
public class PlanBusinessConfigServiceImpl extends BusinessObjectServiceImpl<BusinessConfig> implements PlanBusinessConfigServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(PlanBusinessConfigServiceImpl.class);

    /**
     * sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;


    @Autowired
    private FeignUserService userService;



    /**
     * 分页查询
     * 
     * @param cq
     * @param ispage
     * @return
     * @see
     */
    private List getListByCriteriaQuery(final CriteriaQuery cq, Boolean ispage) {
        Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
            sessionFacade.getSession());
        // 判断是否有排序字段
        if (cq.getOrdermap() != null) {
            cq.setOrder(cq.getOrdermap());
        }
        if (ispage) {
            criteria.setFirstResult((cq.getCurPage() - 1));
            criteria.setMaxResults(cq.getPageSize());
        }
        criteria.addOrder(Order.asc("no"));
        return criteria.list();

    }

    @Override
    public List<BusinessConfig> searchUseableBusinessConfigs(BusinessConfig bc) {
        bc.setStopFlag(ConfigStateConstants.START);
        bc.setAvaliable("1");
        List<BusinessConfig> list = searchBusinessConfigs(bc);
        return list;
    }

    @Override
    public List<BusinessConfig> searchUseableBusinessConfigsForStr(BusinessConfig bc) {
        bc.setStopFlag(ConfigStateConstants.START);
        bc.setAvaliable("1");
        List<BusinessConfig> list = searchBusinessConfigs(bc);
    //    String json = JsonUtil.getListJsonWithoutQuote(list);
        return list;
    }

    @Override
    public List<BusinessConfig> searchBusinessConfigAccurate(BusinessConfig bcon) {
        List<BusinessConfig> list = sessionFacade.findByQueryString(createHql(bcon));
        return list;
    }

    @Override
    public String searchBusinessConfigAccurateForStr(BusinessConfig bcon) {
        List<BusinessConfig> list = sessionFacade.findByQueryString(createHql(bcon));
        String json = JsonUtil.getListJsonWithoutQuote(list);
        return json;
    }

    @Override
    public List<BusinessConfig> getBusinessConfigsByDetailNames(BusinessConfig bc) {
        List<BusinessConfig> rts = new ArrayList<BusinessConfig>();
        List<BusinessConfig> list = searchBusinessConfigs(bc);
        if (list != null) {
            for (BusinessConfig bsc : list) {
                if (bsc.getName().equals(bc.getName())) {
                    rts.add(bsc);
                }
            }
        }
        return rts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BusinessConfig> searchBusinessConfigs(BusinessConfig bc) {
        List<BusinessConfig> list = new ArrayList<BusinessConfig>();
        if (bc.getId() != null && bc.getId().trim().length() > 0) {
            bc = (BusinessConfig)sessionFacade.getEntity(BusinessConfig.class, bc.getId());
            list.add(bc);
        }
        else {
            CriteriaQuery cq = new CriteriaQuery(BusinessConfig.class);
            cq.addOrder("no", SortDirection.asc);
            HqlGenerateUtil.installHql(cq, bc, null);
            list = sessionFacade.getListByCriteriaQuery(cq, false);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BusinessConfig> searchBusinessConfigsForPage(BusinessConfig bc, int page, int rows) {
        CriteriaQuery cq = new CriteriaQuery();
        cq.setDetachedCriteria(DetachedCriteria.forClass(BusinessConfig.class));
        cq.setPageSize(rows);
        cq.setCurPage((page - 1) * rows + 1);
        cq.addOrder("no", SortDirection.asc);
        HqlGenerateUtil.installHql(cq, bc, null);
        List<BusinessConfig> list = getListByCriteriaQuery(cq, true);
        return list;
    }

    @Override
    public List<BusinessConfig> searchDeliverablesForPage(BusinessConfig bc, int page, int rows,
                                                          String notIn) {
        return sessionFacade.pageList(createNotInHql(bc, notIn), (page - 1) * rows, rows);
    }

    @Override
    public long getDeliverablesCount(BusinessConfig bc, String notIn) {
        List<BusinessConfig> list = sessionFacade.findByQueryString(createNotInHql(bc, notIn));
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public long getSearchCount(BusinessConfig bc) {
        long num = 0;
        List<BusinessConfig> list = searchBusinessConfigs(bc);
        if (list != null) {
            num = list.size();
        }
        return num;
    }

    private String getUniqueNo(List<BusinessConfig> list, String curNo) {
        String resNo = curNo;
        boolean isRepeat = false;
        for (BusinessConfig ds : list) {
            if (ds.getNo().equals(curNo)) {
                isRepeat = true;
                break;
            }
        }
        if (isRepeat) {
            resNo = SerialNumberManager.getSerialNumber(SerialNumberConstants.PLAN_LEVEL);
            return getUniqueNo(list, resNo);
        }
        else {
            return resNo;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public BusinessConfig add(BusinessConfig bc) {
    	//项目配置 计划变更类别新增的处理
    	if(bc.getConfigType().equals("PLANCHANGECATEGORY"))
    	{
    		BusinessConfig businessConfig = new BusinessConfig();
            businessConfig.setAvaliable("1");
            businessConfig.setConfigType("PLANCHANGECATEGORY");
            List<BusinessConfig> list = sessionFacade.findByQueryString(createHql(businessConfig));
            
            if (list != null && list.size() > 0) 
            {
                for (BusinessConfig se : list) {
                    if (se.getName().equals(bc.getName().trim())) {
                        throw new GWException(I18nUtil.getValue("com.glaway.ids.pm.config.epsNameIsExitsPleaseUpdate"));
                    }
                }
            }
            initBusinessObject(bc);
            String no = SerialNumberManager.getSerialNumber(SerialNumberConstants.PLAN_CHANGE_CATEGORY);
            no = getUniqueNo(list, no);
            bc.setNo(no);
            bc.setStopFlag(ConfigStateConstants.START);
            sessionFacade.save(bc);
            //参数用于判断是否是从下方插入
            boolean flag=false;
          //新增的时候给项目分类赋值排序的属性    最大排序属性值
            int maxPlace=this.getMaxPlace("PLANCHANGECATEGORY");
            //如果不是从下方插入  则排序字段的值 为最大排序字段的值+1 如果是从下方插入  排序字段的值为排序的前一个顺序的项目分类的排序字段值 +1 且排序在他之后的值所有排序字段值+1
            if(StringUtil.isNotEmpty(bc.getRankQuality()))
            {
            	flag=true;
            	List<BusinessConfig> epsConfigAfterList=this.getListByAfter(bc.getRankQuality(), "PLANCHANGECATEGORY");
            	if(!CommonUtil.isEmpty(epsConfigAfterList)){
                    for (BusinessConfig epsConfig2 : epsConfigAfterList) 
                    {
                        epsConfig2.setRankQuality(""+(Integer.valueOf(epsConfig2.getRankQuality()).intValue()+1));
                        sessionFacade.saveOrUpdate(epsConfig2);
                    }
            	}
            	bc.setRankQuality(""+(Integer.valueOf(bc.getRankQuality()).intValue()+1));
            }else
            {
            	bc.setRankQuality(""+(maxPlace+1));
            }
            
            if (bc.getParentId() != null && !bc.getParentId().equals("")) {
            	BusinessConfig parent = (BusinessConfig)sessionFacade.getEntity(BusinessConfig.class,
                		bc.getParentId());
            	if(flag)
            	{
            		if(StringUtil.isNotEmpty(parent.getParentId())&&!("ROOT".equals(parent.getParentId())))
            		{
            			bc.setParentId(parent.getParentId());
            			bc.setPath(parent.getPath() + "/" + bc.getId());
            		}else
            		{
            			bc.setParentId("ROOT");
                    	bc.setPath("/" + bc.getId());
            		}
            	}else
            	{
            		 bc.setPath(parent.getPath() + "/" + bc.getId());
            	}
               
            }
            else {
            	bc.setParentId("ROOT");
            	bc.setPath("/" + bc.getId());
            }
            sessionFacade.updateEntitie(bc);
           
            
    		
    	}else
    	{
    		BusinessConfig businessConfig = new BusinessConfig();
            if ("PHARSE".equals(bc.getConfigType())) {
                businessConfig.setNo(bc.getNo().trim());
                businessConfig.setConfigType(bc.getConfigType());
                businessConfig.setAvaliable("1");

                List<BusinessConfig> list = sessionFacade.findByQueryString(createHql(businessConfig));
                if (list != null && list.size() > 0) {
                    for (BusinessConfig sb : list) {
                        if (sb.getNo().equals(bc.getNo().trim())) {
                            throw new GWException(
                                I18nUtil.getValue("com.glaway.ids.common.config.configCodeIsExitsPleaseUpdate"));
                        }
                    }
                }
            }
            businessConfig = new BusinessConfig();
            businessConfig.setConfigType(bc.getConfigType());
            businessConfig.setName(bc.getName().trim());
            businessConfig.setAvaliable("1");
            List<BusinessConfig> list = sessionFacade.findByQueryString(createHql(businessConfig));
            if (list != null && list.size() > 0) {
                for (BusinessConfig sb : list) {
                    if (sb.getName().equals(bc.getName().trim())) {
                        throw new GWException(
                            I18nUtil.getValue("com.glaway.ids.common.config.configNameIsExitsPleaseUpdate"));
                    }
                }
            }

            initBusinessObject(bc);
            if (!"PHARSE".equals(bc.getConfigType())) {
                businessConfig = new BusinessConfig();
                businessConfig.setAvaliable("1");
                list = sessionFacade.findByQueryString(createHql(businessConfig));
                String no = "";
                if("PLANLEVEL".equals(bc.getConfigType())){
                    no = SerialNumberManager.getSerialNumber(SerialNumberConstants.PLAN_LEVEL);
                }else if("PLANCHANGECATEGORY".equals(bc.getConfigType())){
                    no = SerialNumberManager.getSerialNumber(SerialNumberConstants.PLAN_CHANGE_CATEGORY);
                }
                no = getUniqueNo(list, no);
                bc.setBizVersion(null);
                bc.setNo(no);
            }
            bc.setName(bc.getName().trim());
            bc.setConfigComment(bc.getConfigComment().trim());
            bc.setStopFlag(ConfigStateConstants.START);
            sessionFacade.save(bc);
    	}
        

        return bc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BusinessConfig modify(BusinessConfig bc) {
        // 判断编号是否有重名
        BusinessConfig businessConfig = new BusinessConfig();
        String no = bc.getNo().trim();
        String name = bc.getName().trim();
        String configComment = bc.getConfigComment().trim();
        if("PHARSE".equals(bc.getConfigType())){
            businessConfig.setNo(no);
        }
        businessConfig.setConfigType(bc.getConfigType());
        businessConfig.setAvaliable("1");
        List<BusinessConfig> list = sessionFacade.findByQueryString(createHql(businessConfig));
        if (list != null) {
            Iterator<BusinessConfig> ite = list.iterator();
            while (ite.hasNext()) {
                BusinessConfig iteb = ite.next();
                if (iteb.getId().equals(bc.getId())) {
                    ite.remove();
                }
            }
            if (list != null && list.size() > 0) {
                for (BusinessConfig sb : list) {
                    if (sb.getNo().equals(no)) {
                        throw new GWException(
                            I18nUtil.getValue("com.glaway.ids.common.config.configCodeIsExitsPleaseUpdate"));
                    }
                }
            }
        }

        // 判断名称是否有重名
        businessConfig.setNo(null);
        businessConfig.setName(name);
        businessConfig.setAvaliable("1");
        list = sessionFacade.findByQueryString(createHql(businessConfig));
        if (list != null) {
            Iterator<BusinessConfig> ite = list.iterator();
            while (ite.hasNext()) {
                BusinessConfig iteb = ite.next();
                if (iteb.getId().equals(bc.getId())) {
                    ite.remove();
                }
            }
            if (list != null && list.size() > 0) {
                for (BusinessConfig sb : list) {
                    if (sb.getName().equals(name)) {
                        throw new GWException(
                            I18nUtil.getValue("com.glaway.ids.common.config.configNameIsExitsPleaseUpdate"));
                    }
                }
            }
        }
        bc.setNo(no);
        bc.setName(name);
        bc.setConfigComment(configComment);
        sessionFacade.updateEntitie(bc);
        return bc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void logicDelete(BusinessConfig bc) {
        bc.setAvaliable("0");
        sessionFacade.updateEntitie(bc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BusinessConfig startOrStop(BusinessConfig bc, String type) {
        if (type.equals(ConfigStateConstants.START)) {
            bc.setStopFlag(ConfigStateConstants.START);
        }
        else {
            bc.setStopFlag(ConfigStateConstants.STOP);
        }
        sessionFacade.updateEntitie(bc);
        return bc;
    }
    /**
     * 迭代获取 获取改计划变更类别下所有的子分类  
     * 目前只有计划变更类别   有Path属性所以 SQL查询是更具Path 查询  如果后面拓展改条件
     * @return
     */
    @Override
    public List<BusinessConfig>  getChildList(BusinessConfig bc){
    	
    	String sql="from BusinessConfig bc where CONTAINS(bc.path,?) and bc.configType = ?";
    	
    	List<BusinessConfig> businessConfigList=sessionFacade.findHql(sql, bc.getPath(),bc.getConfigType());
    	
    	
    	
    	if(businessConfigList!=null&&businessConfigList.size()>0)
    	{
    		
    		for (int i=0;i<businessConfigList.size();i++) 
    		{
    			//排除自己本身 因为path存放是以/加ID的形式存储
				if(bc.getId().equals(businessConfigList.get(i).getId()))
				{
					businessConfigList.remove(i);
				}
			}
    		//排除了自己本身的数据之后再判断size
    		if(businessConfigList!=null&&businessConfigList.size()>0)
    		{
    			return businessConfigList;
    		}
    	}
    	
    	return null;
    }

    @Override
    public void businessConfigSaveOrUpdate(BusinessConfig config) {
        sessionFacade.saveOrUpdate(config);
    }

    @Override
    public void getDataGrid(CriteriaQuery cq, boolean flag) {
        sessionFacade.getDataGridReturn(cq,flag);
    }

    @Override
    public BusinessConfig getBusinessConfig(String id) {
        return (BusinessConfig)sessionFacade.getEntity(BusinessConfig.class,id);
    }

    @Override
    public List<BusinessConfig> getBusinessConfigListByConfigType(String configType) {
        List<BusinessConfig> list = sessionFacade.findHql("from BusinessConfig p where p.avaliable = 1 and p.configType = ?",configType);
        return list;
    }

    @Override
    public List<BusinessConfig> getAllBusinessConfigListByConfigType(String configType) {
        StringBuffer sb = new StringBuffer();
        sb.append("from BusinessConfig where 1=1 ");
        if(!CommonUtil.isEmpty(configType)){
            sb.append(" and configType = '"+configType+"' ");
        }
        List<BusinessConfig> list = sessionFacade.findByQueryString(sb.toString());
        return list;
    }

    @Override
    public String getProjectPhaseList() {
        BusinessConfig phase = new BusinessConfig();
        phase.setConfigType(ConfigTypeConstants.PHARSE);
        List<BusinessConfig> list =  sessionFacade.findHql("from BusinessConfig where avaliable='1' and stopFlag='启用' and configType=? order by no asc",ConfigTypeConstants.PHARSE);
        String json = JsonUtil.getListJsonWithoutQuote(list);
        return json;
    }



    @SuppressWarnings("unchecked")
    @Override
    public PageList queryEntity(List<ConditionVO> conditionList, boolean isPage) {
        try {
            ConditionVO ava = new ConditionVO();
            ava.setCondition("eq");
            ava.setKey("BusinessConfig.avaliable");
            ava.setValue("1");
            conditionList.add(ava);

            Map<String, String> aliasMap = new HashMap<String, String>();
            aliasMap.put("BusinessConfig.no", "asc");

            String hql = "from BusinessConfig t";
            List list = sessionFacade.searchByCommonFormHql(hql, conditionList, isPage, aliasMap);
            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            List<BusinessConfigDto> resultList = CodeUtils.JsonListToList(list,BusinessConfigDto.class);
            PageList pageList = new PageList(count, resultList);
            return pageList;
        }
        catch (RecognitionException e) {
            new FdExceptionPolicy("basicException", "查询语句格式化异常", e);
        }
        catch (TokenStreamException e) {
            new FdExceptionPolicy("basicException", "查询语句开闭缺失", e);
        }
        catch (ClassNotFoundException e) {
            new FdExceptionPolicy("basicException", "查询实体不存在", e);
        }
        return null;
    }

    /**
     * Description:
     * 
     * @param businessConfig
     * @return
     * @see
     */
    private String createHql(BusinessConfig businessConfig) {
        String hql = "from BusinessConfig l where 1 = 1 ";
        if (StringUtils.isNotEmpty(businessConfig.getId())) {
            hql = hql + " and l.id = '" + businessConfig.getId() + "'";
        }
        if (StringUtils.isNotEmpty(businessConfig.getNo())) {
            hql = hql + " and l.no = '" + businessConfig.getNo() + "'";
        }
        if (StringUtils.isNotEmpty(businessConfig.getName())) {
            hql = hql + " and l.name = '" + businessConfig.getName() + "'";
        }
        if (StringUtils.isNotEmpty(businessConfig.getStopFlag())) {
            hql = hql + " and l.stopFlag = '" + businessConfig.getStopFlag() + "'";
        }
        if (StringUtils.isNotEmpty(businessConfig.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + businessConfig.getAvaliable() + "'";
        }
        if (StringUtils.isNotEmpty(businessConfig.getConfigType())) {
            hql = hql + " and l.configType = '" + businessConfig.getConfigType() + "'";
        }
        hql = hql + " order by l.no asc";
        return hql;
    }

    /**
     * Description:
     * 
     * @param businessConfig
     * @param notIn
     * @return
     * @see
     */
    private String createNotInHql(BusinessConfig businessConfig, String notIn) {
        String hql = "from BusinessConfig l where 1 = 1 ";
        if (StringUtils.isNotEmpty(businessConfig.getNo())) {
            hql = hql + " and l.no like '%" + businessConfig.getNo() + "%'";
        }
        if (StringUtils.isNotEmpty(businessConfig.getName())) {
            hql = hql + " and l.name like '%" + businessConfig.getName() + "%'";
        }
        if (StringUtils.isNotEmpty(businessConfig.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + businessConfig.getAvaliable() + "'";
        }
        if (StringUtils.isNotEmpty(businessConfig.getConfigType())) {
            hql = hql + " and l.configType = '" + businessConfig.getConfigType() + "'";
        }
        // 默认查询启用状态的交付项
        if (StringUtils.isNotEmpty(businessConfig.getStopFlag())) {
            hql = hql + " and l.stopFlag = '" + businessConfig.getStopFlag() + "'";
        }

        if (StringUtils.isNotEmpty(notIn)) {
            hql = hql + " and l.id not in (";
            String[] arras = notIn.split(",");
            for (int i = 0; i < arras.length; i++ ) {
                if (i == (arras.length - 1)) {
                    hql = hql + "'" + arras[i] + "'";
                }
                else {
                    hql = hql + "'" + arras[i] + "',";
                }
            }
            hql = hql + ")";
        }
        hql = hql + " order by l.no asc";
        return hql;
    }

    @SuppressWarnings("unchecked")
    @Override
    // 导入数据保存
    public String doData(List<String> dataList,String configType,String userId,String orgId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        int addNewCount = 0;
        BusinessConfig businessConfig = new BusinessConfig();
        businessConfig.setConfigType(configType);
        businessConfig.setAvaliable("1");
        List<BusinessConfig> list = sessionFacade.findByQueryString(createHql(businessConfig));
        if("PHARSE".equals(configType)){
            for (String s : dataList) {
                String[] data = s.split(",");
                String bcNo = data[0].trim();
                String bcName = data[1].trim();
                String bcConfigComment = "";
                if (StringUtils.isNotEmpty(data[2].trim())) {
                    bcConfigComment = data[2].trim();
                }
                String bcConfigType = data[3];
                BusinessConfig bc = new BusinessConfig();
                bc.setNo(bcNo);
                bc.setName(bcName);
                bc.setConfigComment(bcConfigComment);
                bc.setConfigType(bcConfigType);
                bc.setStopFlag(ConfigStateConstants.START);
                initBusinessObject(bc);
                CommonInitUtil.initGLObjectForCreate(bc,userDto,orgId);
                sessionFacade.save(bc);
                addNewCount++ ;
            }
        }else{
            for (String s : dataList) {
                String[] data = s.split(",");
                String bcName = data[0].trim();
                String bcConfigComment = "";
                if (StringUtils.isNotEmpty(data[1].trim())) {
                    bcConfigComment = data[1].trim();
                }
                String bcConfigType = data[2];
                BusinessConfig bc = new BusinessConfig();
                String bcNo = "";
                if("PLANLEVEL".equals(bcConfigType)){
                    bcNo = SerialNumberManager.getSerialNumber(SerialNumberConstants.PLAN_LEVEL);
                    bcNo = getUniqueNo(list, bcNo);
                }else if("PLANCHANGECATEGORY".equals(bcConfigType)){
                    bcNo = SerialNumberManager.getSerialNumber(SerialNumberConstants.PLAN_CHANGE_CATEGORY);
                    bcNo = getUniqueNo(list, bcNo);
                }
                bc.setNo(bcNo);
                bc.setName(bcName);
                bc.setConfigComment(bcConfigComment);
                bc.setConfigType(bcConfigType);
                bc.setStopFlag(ConfigStateConstants.START);
                initBusinessObject(bc);
                CommonInitUtil.initGLObjectForCreate(bc,userDto,orgId);
                sessionFacade.save(bc);
                addNewCount++ ;
            }
        }
        String resCount = "" + addNewCount;
        return resCount;
    }

    @Override
    public Map<String, String> checkData(int row, String strForBc, Map<String, String> errorMsgMap) {
        String[] data = strForBc.split(",");

        String bcNo = data[0].trim();
        String bcName = data[1].trim();
        String bcConfigType = data[3];
        String typeName = getConfigTypeName(bcConfigType);
        if (!"".equals(bcNo) && !"".equals(bcName)) {
            BusinessConfig bcConditionForNo = new BusinessConfig();
            bcConditionForNo.setNo(bcNo);
            bcConditionForNo.setConfigType(bcConfigType);
            List<BusinessConfig> bcResult = searchBusinessConfigAccurate(bcConditionForNo);
            if (bcResult.size() == 0) {
                BusinessConfig bcConditionForName = new BusinessConfig();
                bcConditionForName.setName(bcName);
                bcConditionForName.setConfigType(bcConfigType);
                bcResult = searchBusinessConfigAccurate(bcConditionForName);
                if (bcResult.size() != 0) {
                    Object[] arguments = new String[] {bcName};
                    POIExcelUtil.addErrorMsg(row, I18nUtil.getValue(
                        "com.glaway.ids.common.config.configCheckNameIsExits", arguments), errorMsgMap);
                }
            }
            else {
                Object[] arguments = new String[] {bcNo};
                POIExcelUtil.addErrorMsg(row,
                    I18nUtil.getValue("com.glaway.ids.common.config.configCheckCodeIsExits", arguments),
                    errorMsgMap);
            }
        }
        else {
            if ("".equals(bcNo)) {
                POIExcelUtil.addErrorMsg(row,
                    I18nUtil.getValue("com.glaway.ids.common.config.configCheckCodeIsNull"), errorMsgMap);
            }
            else if ("".equals(bcName)) {
                POIExcelUtil.addErrorMsg(row,
                    I18nUtil.getValue("com.glaway.ids.common.config.configCheckNameIsNull"), errorMsgMap);
            }
        }
        return errorMsgMap;
    }

    @Override
    public String getConfigTypeName(String configType) {
        String typeName = "";
        if (configType != null) {
            if (ConfigTypeConstants.PHARSE.equals(configType)) {
                typeName = ConfigTypeConstants.getName(ConfigTypeConstants.PHARSE);
            }
            else if (ConfigTypeConstants.PLANLEVEL.equals(configType)) {
                typeName = ConfigTypeConstants.getName(ConfigTypeConstants.PLANLEVEL);
            }
            else if (ConfigTypeConstants.PLANCHANGECATEGORY.equals(configType)) {
                typeName = ConfigTypeConstants.getName(ConfigTypeConstants.PLANCHANGECATEGORY);
            }
        }
        return typeName;
    }

    @Override
    public String doBatchDel(String ids, String message) {
        String msg = "";
        for (String id : ids.split(",")) {
            BusinessConfig businessConfig = (BusinessConfig)sessionFacade.getEntity(BusinessConfig.class, id);
            logicDelete(businessConfig);
        }
        if (!"".equals(msg)) {
            message = msg;
        }
        else {
            for (String id : ids.split(",")) {
                BusinessConfig businessConfig = (BusinessConfig)sessionFacade.getEntity(BusinessConfig.class, id);
                logicDelete(businessConfig);
            }
        }
        return message;
    }

    @Override
    public void doBatchStartOrStop(String ids, String state) {
        for (String id : ids.split(",")) {
            BusinessConfig businessConfig = (BusinessConfig)sessionFacade.getEntity(BusinessConfig.class, id);
            if (state.equals("start")) {
                startOrStop(businessConfig, ConfigStateConstants.START);
            }
            else {
                startOrStop(businessConfig, ConfigStateConstants.STOP);
            }
        }
    }

    @Override
    public Map<String, String> checkImportNames(List<String> names, Map<String, String> errorMsgMap) {
        if (names.size() > 1) {
            for (int i = 0; i < names.size() - 1; i++ ) {
                for (int j = i + 1; j < names.size(); j++ ) {
                    if (names.get(i).equals(names.get(j))) {
                        POIExcelUtil.addErrorMsg(names.size() + 2,
                            I18nUtil.getValue("com.glaway.ids.common.config.configCheckNameIsNotReset"),
                            errorMsgMap);
                        return errorMsgMap;
                    }
                }
            }
        }
        return errorMsgMap;
    }

    @Override
    public Map<String, String> checkImportNos(List<String> nos, Map<String, String> errorMsgMap) {
        if (nos.size() > 1) {
            for (int i = 0; i < nos.size() - 1; i++ ) {
                for (int j = i + 1; j < nos.size(); j++ ) {
                    if (nos.get(i).equals(nos.get(j))) {
                        POIExcelUtil.addErrorMsg(nos.size() + 2,
                            I18nUtil.getValue("com.glaway.ids.common.config.configCheckCodeIsNotReset"),
                            errorMsgMap);
                        return errorMsgMap;
                    }
                }
            }
        }
        return errorMsgMap;
    }

    @Override
    public String getBusinessConfigsList(BusinessConfig bc) {
        String hql = "from BusinessConfig t where avaliable='1' and stopFlag='启用' and configType='"
                     + bc.getConfigType() + "'";
        List<BusinessConfig> bcList = sessionFacade.findByQueryString(hql);
        JSONArray jList = new JSONArray();
        for (BusinessConfig config : bcList) {
            JSONObject obj = new JSONObject();
            obj.put("id", config.getId());
            obj.put("name", config.getName());
            jList.add(obj);
        }
        String bcListStr = jList.toString();
        return bcListStr;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<BusinessConfig> searchTreeNode(BusinessConfig businessConfig) {
        CriteriaQuery cq = new CriteriaQuery(BusinessConfig.class);
        HqlGenerateUtil.installHql(cq, businessConfig, null);
        cq.addOrder("no", SortDirection.asc);
        List<BusinessConfig> list = sessionFacade.getListByCriteriaQuery(cq, false);
        return list;
    }
    
    
    @Override
    public void getBusinessConfigParentList(BusinessConfig targetNode, List<BusinessConfig> allList,
                                   List<BusinessConfig> parentList) {
        for (BusinessConfig eps : allList) {
            if (eps.getId().equals(targetNode.getParentId())) {
                parentList.add(eps);
                getBusinessConfigParentList(eps, allList, parentList);
            }
        }
    }
    
    
    @Override
    public BusinessConfig getParentNode(BusinessConfig epsConfig) {
        CriteriaQuery cq = new CriteriaQuery(BusinessConfig.class);
        HqlGenerateUtil.installHql(cq, epsConfig, null);
        List<BusinessConfig> list = sessionFacade.getListByCriteriaQuery(cq, false);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 根据项目配置的类别信息获取最大的排序字段的值
     */
	@Override
	public int getMaxPlace(String configType) {
		  String hql="select max(to_number(decode(cbc.rankQuality,null,'0',cbc.rankQuality))) as maxPlace from CM_BUSINESS_CONFIG cbc where cbc.configType = '"+configType+"'";
		   
		   List<Map<String, Object>> maxPlaceList=sessionFacade.findForJdbc(hql);
		   
		   if(null!=maxPlaceList&&maxPlaceList.size()>0)
		   {
			   
			   if(maxPlaceList.get(0).get("MAXPLACE")!=null){
				   BigDecimal b = (BigDecimal)maxPlaceList.get(0).get("MAXPLACE");
				   int maxPlace=b.intValue();
				   //int maxPlace=Integer.valueOf((String)maxPlaceList.get(0).get("MAXPLACE")).intValue();
				   if(maxPlace>0)
				   {
					   return maxPlace;
				   }else
				   {
					   return 0;
				   }
			   }else
			   {
				   return 0;
			   }
			   
		   }
		return 0;
	}
	/**
	 * 根据选中的字段获取该字段之后 项目配置实体类的集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessConfig> getListByAfter(String rankQuality,String configType) 
	{
	
		String hql="from BusinessConfig cbc where to_number(decode(cbc.rankQuality,null,'0',cbc.rankQuality)) > ? and cbc.configType = ?";
		    
		List<BusinessConfig> businessConfigList=sessionFacade.findHql(hql, rankQuality,configType);
		if(businessConfigList!=null&&businessConfigList.size()>0)
		{
			return businessConfigList;
		}
		return null;
	}

}
