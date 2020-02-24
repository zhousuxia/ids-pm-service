package com.glaway.ids.config.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.core.common.extend.hqlsearch.HqlGenerateUtil;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.ids.config.constant.NameStandardSwitchConstants;
import com.glaway.ids.config.constant.SwitchConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.config.entity.ParamSwitch;
import com.glaway.ids.config.service.ParamSwitchServiceI;


/**
 * 接口实现
 * 
 * @author xshen
 * @version 2015年4月15日
 * @see ParamSwitchServiceImpl
 * @since
 */
@Service("paramSwitchService")
@Transactional
public class ParamSwitchServiceImpl extends BusinessObjectServiceImpl<ParamSwitch> implements ParamSwitchServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(ParamSwitchServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    @Override
    public String getSwitch(String switchName) {
        /**
         * getSwitch该方法多次被调用，请做缓存，使用fd缓存框架
         */
        ParamSwitch paramSwitch = new ParamSwitch();
        paramSwitch.setName(switchName);
        List<ParamSwitch> list = search(paramSwitch);
        String status = "true";
        if (list != null && list.size() > 0) {
            if(list.get(0).getName().equals(SwitchConstants.NAMESTANDARDSWITCH)){
                status = NameStandardSwitchConstants.getLastSwitch(list.get(0).getStatus());
            }
            else if(list.get(0).getName().equals(SwitchConstants.FLOWTEMPLATE)){
                if(StringUtil.isEmpty(list.get(0).getStatus())){
                    status = "false";
                }
                else{
                    status = "true";
                }
            }
            else{
                status = list.get(0).getStatus();
            }
        }
        else{
        	 list = search(new ParamSwitch());
        	 if(!CommonUtil.isEmpty(list)){
     	        for(ParamSwitch p : list){
     	        	if(switchName.contains(p.getName())){
     	        		if(p.getName().equals(SwitchConstants.NAMESTANDARDSWITCH)){
     	                    status = NameStandardSwitchConstants.getLastSwitch(p.getStatus());
     	                }
     	                else if(p.getName().equals(SwitchConstants.FLOWTEMPLATE)){
     	                    if(StringUtil.isEmpty(p.getStatus())){
     	                        status = "false";
     	                    }
     	                    else{
     	                        status = "true";
     	                    }
     	                }
     	                else{
     	                    status = p.getStatus();
     	                }
     	        		break;
     	        	}
     	        }
             }
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ParamSwitch> search(ParamSwitch paramSwitch) {
        String hql = "from BusinessConfig t where avaliable='1' and stopFlag='启用' and configType='PLANLEVEL'";
        List<BusinessConfig> bcList = sessionFacade.findByQueryString(hql);
        String planLevelNameList = "";
        for(BusinessConfig bc : bcList){
            planLevelNameList += "," + bc.getName();
        }
        ParamSwitch planLevel = (ParamSwitch)sessionFacade.getEntity(ParamSwitch.class, "4028ef314cdea407014ce01623d70005a");
        planLevel.setStatusList("".equals(planLevelNameList)?"":planLevelNameList.substring(1));
        sessionFacade.updateEntitie(planLevel);
        CriteriaQuery cq = new CriteriaQuery(ParamSwitch.class);
        HqlGenerateUtil.installHql(cq, paramSwitch, null);
        List<ParamSwitch> list = sessionFacade.getListByCriteriaQuery(cq, false);  
//        String hql1 = "from ParamSwitch t where 1=1 ";
//        if (!CommonUtil.isEmpty(paramSwitch.getName())) {
//            hql1 = hql1 +" and t.name = '"+ paramSwitch.getName() +"'";
//        }
//        hql1 = hql1 + " order by t.createTime asc";
//        List<ParamSwitch> list = findByQueryString(hql1);
        return list;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ParamSwitch> searchProconfigParamSwitch() {
        StringBuilder hqlBuffer = new StringBuilder("");
        hqlBuffer.append("select t.id,t.name,t.status from CM_RD_PARAM_SWITCH t");        
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hqlBuffer.toString());
        List<ParamSwitch> list = new ArrayList<ParamSwitch>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                ParamSwitch d = new ParamSwitch();
                d.setId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                d.setName(StringUtils.isNotEmpty((String)map.get("NAME")) ? (String)map.get("NAME") : "");
                d.setStatus(StringUtils.isNotEmpty((String)map.get("STATUS")) ? (String)map.get("STATUS") : "");
                if (StringUtils.isNotEmpty(d.getId())) {
                    list.add(d);
                }
            }
        }
        return  list ;
    }
    
    @Override
    public void updateStatusById(String status,String id) {
        ParamSwitch paramSwitch = (ParamSwitch)sessionFacade.getEntity(ParamSwitch.class, id);
        paramSwitch.setStatus(status);
        sessionFacade.saveOrUpdate(paramSwitch);
    }
    
}