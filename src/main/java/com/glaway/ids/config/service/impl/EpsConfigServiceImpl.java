package com.glaway.ids.config.service.impl;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.config.constant.ConfigStateConstants;
import com.glaway.ids.config.constant.SerialNumberConstants;
import com.glaway.ids.config.entity.EpsConfig;
import com.glaway.ids.config.service.EpsConfigServiceI;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.JsonUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.core.common.extend.hqlsearch.HqlGenerateUtil;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.system.serial.SerialNumberManager;
import com.glaway.foundation.tag.vo.datatable.SortDirection;

import com.glaway.ids.util.JsonFromatUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import springfox.documentation.spring.web.json.Json;

import java.math.BigDecimal;
import java.util.*;


@Service("epsConfigService")
@Transactional
public class EpsConfigServiceImpl extends BusinessObjectServiceImpl<EpsConfig> implements EpsConfigServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(EpsConfigServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;


    @Override
    public String getEpsConfig(String id) {
        EpsConfig epsConfig = (EpsConfig)sessionFacade.getEntity(EpsConfig.class,id);
        String json = JSON.toJSONString(epsConfig);
        return json;
    }

    @Override
    public EpsConfig add(EpsConfig epsConfig) {
        // 需要校验编号唯一
        EpsConfig checkNo = new EpsConfig();
        checkNo.setAvaliable("1");
        List<EpsConfig> list = sessionFacade.findByQueryString(createHql(checkNo));

        if (list != null && list.size() > 0) {
            for (EpsConfig se : list) {
                if (se.getName().equals(epsConfig.getName().trim())) {
                    throw new GWException(I18nUtil.getValue("com.glaway.ids.pm.config.epsNameIsExitsPleaseUpdate"));
                }
            }
        }
        initBusinessObject(epsConfig);
        String no = SerialNumberManager.getSerialNumber(SerialNumberConstants.EPS_CONFIG);
        if(!CommonUtil.isEmpty(no)){
            no = getUniqueNo(list, no);
        }

        epsConfig.setNo(no);
        epsConfig.setStopFlag(ConfigStateConstants.START_KEY);
        //新增的时候给项目分类赋值排序的属性    最大排序属性值
        int maxPlace=getMaxEpsConfigPlace();
        //如果不是从下方插入  则排序字段的值 为最大排序字段的值+1 如果是从下方插入  排序字段的值为排序的前一个顺序的项目分类的排序字段值 +1 且排序在他之后的值所有排序字段值+1
        if(StringUtil.isNotEmpty(epsConfig.getRankQuality()))
        {
            List<EpsConfig> epsConfigAfterList=getAfterEpsList(epsConfig.getRankQuality());
            if(!CommonUtil.isEmpty(epsConfigAfterList)){
                for (EpsConfig epsConfig2 : epsConfigAfterList)
                {
                    epsConfig2.setRankQuality(""+(Integer.valueOf(epsConfig2.getRankQuality()).intValue()+1));
                    sessionFacade.saveOrUpdate(epsConfig2);
                }
            }
            epsConfig.setRankQuality(""+(Integer.valueOf(epsConfig.getRankQuality()).intValue()+1));
        }else
        {
            epsConfig.setRankQuality(""+(maxPlace+1));
        }


        sessionFacade.save(epsConfig);

        if (epsConfig.getParentId() != null && !epsConfig.getParentId().equals("")) {
            EpsConfig parent = (EpsConfig)sessionFacade.getEntity(EpsConfig.class,
                    epsConfig.getParentId());
            epsConfig.setPath(parent.getPath() + "/" + epsConfig.getId());
        }
        else {
            epsConfig.setParentId("ROOT");
            epsConfig.setPath("/" + epsConfig.getId());
        }
        sessionFacade.updateEntitie(epsConfig);
        return epsConfig;
    }

    private String createHql(EpsConfig EpsConfig) {
        String hql = "from EpsConfig l where 1 = 1 ";
        if (StringUtils.isNotEmpty(EpsConfig.getNo())) {
            hql = hql + " and l.no = '" + EpsConfig.getNo() + "'";
        }
        if (StringUtils.isNotEmpty(EpsConfig.getName())) {
            hql = hql + " and l.name = '" + EpsConfig.getName() + "'";
        }
        if (StringUtils.isNotEmpty(EpsConfig.getStopFlag())) {
            hql = hql + " and l.stopFlag = '" + EpsConfig.getStopFlag() + "'";
        }
        if (StringUtils.isNotEmpty(EpsConfig.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + EpsConfig.getAvaliable() + "'";
        }
        if (StringUtils.isNotEmpty(EpsConfig.getParentId())) {
            hql = hql + " and l.parentId = '" + EpsConfig.getParentId() + "'";
        }
        hql = hql + " order by l.no asc";
        return hql;
    }

    private String getUniqueNo(List<EpsConfig> list, String curNo){
        String resNo = curNo;
        boolean isRepeat = false;
        for(EpsConfig se : list){
            if (se.getNo().equals(curNo)) {
                isRepeat = true;
                break;
            }
        }
        if(isRepeat){
            resNo = SerialNumberManager.getSerialNumber(SerialNumberConstants.EPS_CONFIG);
            return getUniqueNo(list, resNo);
        }else{
            return resNo;
        }
    }

    /**
     * 项目分类中  获取最大项目分类的位置序号
     */
    @Override
    public int getMaxEpsConfigPlace()
    {
        //如果字段为空的DECODE
        String hql="select max(to_number(decode(cec.rankQuality,null,'0',cec.rankQuality))) as maxPlace from CM_EPS_CONFIG cec";

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
     * 获取项目分类  位置之后的项目分类信息
     */
    @Override
    public List<EpsConfig> getAfterEpsList(String rankQuality)
    {
        String hql="from EpsConfig ec where to_number(decode(ec.rankQuality,null,'0',ec.rankQuality)) > ?";

        List<EpsConfig> epsConfigList=sessionFacade.findHql(hql, rankQuality);
        if(epsConfigList!=null&&epsConfigList.size()>0)
        {
            return epsConfigList;
        }
        return null;
    }

    @Override
    public List<EpsConfig> searchTreeNodeList(EpsConfig epsConfig) {
        CriteriaQuery cq = new CriteriaQuery(EpsConfig.class);
        HqlGenerateUtil.installHql(cq, epsConfig, null);
        cq.addOrder("no", SortDirection.asc);
        List<EpsConfig> list = sessionFacade.getListByCriteriaQuery(cq, false);
        return list;
    }

	@Override
	public String searchTreeNode(EpsConfig epsConfig) {
		 CriteriaQuery cq = new CriteriaQuery(EpsConfig.class);
	     HqlGenerateUtil.installHql(cq, epsConfig, null);
	     cq.addOrder("no", SortDirection.asc);
	     List<EpsConfig> list = sessionFacade.getListByCriteriaQuery(cq, false);
	     String json = JsonUtil.getListJsonWithoutQuote(list);
	     return json;
	}

	@Override
	public void getEpsParentList(EpsConfig targetNode, List<EpsConfig> allList, List<EpsConfig> parentList) {
		for (EpsConfig eps : allList) {
	        if (eps.getId().equals(targetNode.getParentId())) {
	            parentList.add(eps);
	            getEpsParentList(eps, allList, parentList);
	        }
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdateEpsConfig(EpsConfig epsConfig) {
		sessionFacade.saveOrUpdate(epsConfig);
	}

	@Override
	public String getParentNode(EpsConfig epsConfig) {
		CriteriaQuery cq = new CriteriaQuery(EpsConfig.class);
        HqlGenerateUtil.installHql(cq, epsConfig, null);
        List<EpsConfig> list = sessionFacade.getListByCriteriaQuery(cq, false);
        String json = "";
        json = JsonUtil.getListJsonWithoutQuote(list);
        return json;
	}

    @Override
    public EpsConfig modify(EpsConfig epsConfig, boolean nochange) {
        if (nochange) {
            EpsConfig checkNo = new EpsConfig();
            checkNo.setNo(epsConfig.getNo());
            checkNo.setAvaliable("1");
            List<EpsConfig> list = sessionFacade.findByQueryString(createHql(checkNo));
            if (list != null) {
                Iterator<EpsConfig> ite = list.iterator();
                while (ite.hasNext()) {
                    EpsConfig e = ite.next();
                    if (e.getId().equals(epsConfig.getId())) {
                        ite.remove();
                    }
                }
            }

            if (list != null && list.size() > 0) {
                for (EpsConfig se : list) {
                    if (se.getNo().equals(epsConfig.getNo().trim())) {
                        throw new GWException(I18nUtil.getValue("com.glaway.ids.pm.config.epsCodeIsExitsPleaseUpdate"));
                    }
                }
            }
        }
        sessionFacade.updateEntitie(epsConfig);
        return epsConfig;
    }

    @Override
    public String getEpsConfigList(EpsConfig epsConfig) {
        String hql = createHql(epsConfig);
        List<EpsConfig> list = sessionFacade.findByQueryString(hql);
        String json = JsonUtil.getListJsonWithoutQuote(list);
        return json;
    }

    @Override
    public void doBatchDel(String ids) {
        for (String id : ids.split(",")) {
            EpsConfig epsConfig = (EpsConfig)sessionFacade.getEntity(EpsConfig.class, id);
            logicDelete(epsConfig);
        }
    }


    private List<EpsConfig> searchNodeForList(EpsConfig epsConfig){
        CriteriaQuery cq = new CriteriaQuery(EpsConfig.class);
        HqlGenerateUtil.installHql(cq, epsConfig, null);
        cq.addOrder("no", SortDirection.asc);
        List<EpsConfig> list = sessionFacade.getListByCriteriaQuery(cq, false);
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void logicDelete(EpsConfig epsConfig) {
        List<EpsConfig> list = searchNodeForList(new EpsConfig());
        List<EpsConfig> childrens = new ArrayList<EpsConfig>();
        getAllChildrens(list, epsConfig, childrens);// 循环获取所有子孙节点
        childrens.add(epsConfig);
        for (EpsConfig child : childrens) {
            child.setAvaliable("0");
            sessionFacade.updateEntitie(child);
        }
    }

    @Override
    public void doBatchStartOrStop(String ids, String state) {
        for (String id : ids.split(",")) {
            EpsConfig epsConfig = (EpsConfig)sessionFacade.getEntity(EpsConfig.class, id);
            if (state.equals("start")) {
                startOrStop(epsConfig, ConfigStateConstants.START_KEY);
            }
            else {
                startOrStop(epsConfig, ConfigStateConstants.STOP_KEY);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public EpsConfig startOrStop(EpsConfig epsConfig, String type) {
        List<EpsConfig> list = searchNodeForList(new EpsConfig());

        List<EpsConfig> childrens = new ArrayList<EpsConfig>();

        childrens.add(epsConfig);

        if (type.equals(ConfigStateConstants.STOP_KEY)) {
            getAllChildrens(list, epsConfig, childrens);// 循环获取所有子孙节点
            for (EpsConfig eps : childrens) {
                eps.setStopFlag(ConfigStateConstants.STOP_KEY);
                sessionFacade.updateEntitie(eps);
            }
        }
        else {
            for (EpsConfig eps : childrens) {
                eps.setStopFlag(ConfigStateConstants.START_KEY);
                sessionFacade.updateEntitie(eps);
            }
        }

        return epsConfig;
    }

    @Override
    public String getList() {
        List<EpsConfig> epsList = sessionFacade.findHql("from EpsConfig where 1=1 order by createTime desc");
        String json = JsonUtil.getListJsonWithoutQuote(epsList);
        return json;
    }

    @Override
    public String getEpsNamePathById(String id) {
        String realNamePath = "";
        if (StringUtils.isNotBlank(id)) {
            EpsConfig espConfig = null;
            try {
                espConfig = (EpsConfig)sessionFacade.getEntity(EpsConfig.class, id);
            }
            catch (Exception e) {
                return realNamePath;
            }
            if (espConfig != null) {
                String path = espConfig.getPath();
                String[] ids = StringUtils.split(path, "/");
                for (String epsId : ids) {
                    if (StringUtils.isNotBlank(epsId)) {
                        EpsConfig esp = (EpsConfig)sessionFacade.getEntity(EpsConfig.class, epsId);
                        if (esp != null) {
                            realNamePath = realNamePath + "/" + esp.getName();
                        }
                    }
                }
            }
        }
        if (realNamePath.length() > 1) {
            realNamePath = realNamePath.substring(1);
        }
        return realNamePath;
    }

    @Override
    public String getEpsTreeNodes() {
        List<EpsConfig> list = getAllForderList();// 所有节点列表
        List<TreeNode> allnodes = new ArrayList<TreeNode>();
        for (EpsConfig f : list) {// 获取所有跟节点
            TreeNode treeNode = transfer(f);
            allnodes.add(treeNode);
        }
        String json = JsonUtil.getListJsonWithoutQuote(allnodes);
        return json;
    }

    @Override
    public String getTreeNodes(EpsConfig epsConfig) {
        List<EpsConfig> list = searchTreeNodeList(epsConfig);// 所有节点列表
        // List<TreeNode> roots = new ArrayList<TreeNode>();// 根节点列表
        List<TreeNode> allnodes = new ArrayList<TreeNode>();
        for (EpsConfig f : list) {// 获取所有跟节点
            if ("1".equals(f.getStopFlag())) {
                TreeNode treeNode = transfer(f);
                allnodes.add(treeNode);
            }
        }

        String json = JsonUtil.getListJsonWithoutQuote(allnodes);
        return json;
    }


    /**
     * 获取所有节点列表
     *
     * @return
     * @see
     */
    private List<EpsConfig> getAllForderList() {
        CriteriaQuery cq = new CriteriaQuery(EpsConfig.class);
        HqlGenerateUtil.installHql(cq, new EpsConfig(),
                null);
        List<EpsConfig> list = getListByCriteriaQuery(cq, false);
        return list;
    }

    /**
     * 查询
     *
     * @param cq
     * @param ispage
     * @return
     * @see
     */
    @SuppressWarnings("unchecked")
    private List<EpsConfig> getListByCriteriaQuery(final CriteriaQuery cq, Boolean ispage) {
        Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
                sessionFacade.getSession());
        criteria.addOrder(Order.desc("createTime"));
        return criteria.list();
    }


    /**
     * 转换树节点
     *
     * @param folder
     * @return
     * @see
     */
    private TreeNode transfer(EpsConfig folder) {
        TreeNode node = new TreeNode();
        node.setId(folder.getId());
        node.setPid(folder.getParentId());
        node.setName(folder.getName());
        node.setTitle(folder.getName());
        node.setOpen(true);
        return node;
    }



    /**
     * 获取所有子孙节点
     *
     * @param all
     * @param parent
     * @param childrens
     * @see
     */
    private void getAllChildrens(List<EpsConfig> all, EpsConfig parent, List<EpsConfig> childrens) {
        for (EpsConfig node : all) {
            if (node.getParentId().equals(parent.getId())) {
                childrens.add(node);
                parent.getChildren().add(node);
                getAllChildrens(all, node, childrens);
            }
        }

    }

    @Override
    public FeignJson doBatchDelIsHaveChildList(String ids) {
        FeignJson j = new FeignJson();
        j.setSuccess(false);
        if(!CommonUtil.isEmpty(ids)){
            //校验判断项目分类下是否有子节点
            for(int i=0;i<ids.split(",").length;i++){
                EpsConfig epsConfig=new EpsConfig();
                epsConfig.setParentId(ids.split(",")[i]);
                String hql = createHql(epsConfig);
                List<EpsConfig> epsConfigList = sessionFacade.findByQueryString(hql);
                if(epsConfigList!=null&&epsConfigList.size()>0){
                    j.setSuccess(true);
                    break;
                }
            }
        }
        return j;
    }

    @Override
    public FeignJson doUpdate(EpsConfig epsConfig) {
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.pm.config.eps.updateSuccess");
        EpsConfig t = (EpsConfig)sessionFacade.getEntity(EpsConfig.class,epsConfig.getId());
        try {
            String originalNo = t.getNo();
            t.setName(epsConfig.getName());
            t.setConfigComment(epsConfig.getConfigComment());
            modify(t, !originalNo.equals(epsConfig.getNo()));
            log.info(message, epsConfig.getId(), epsConfig.getId().toString());
        }
        catch (Exception e) {
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.config.eps.updateError");
            if (e instanceof GWException) {
                message = e.getMessage();
            }
            log.error(message, e, "", epsConfig.getId().toString());
            Object[] params = new Object[] {message, epsConfig.getId().toString()};// 异常原因：{0}；详细信息：{1}
            throw new GWException(GWConstants.ERROR_2002, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }
}