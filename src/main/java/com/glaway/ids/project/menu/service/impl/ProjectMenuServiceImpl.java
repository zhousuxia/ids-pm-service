package com.glaway.ids.project.menu.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.project.menu.entity.ProjectMenu;
import com.glaway.ids.project.menu.service.ProjectMenuServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;


/**
 * 左侧项目列表树
 * 〈功能详细描述〉
 * 
 * @author wangshen
 * @version 2015年4月14日
 * @see ProjectMenuServiceImpl
 * @since
 */
@Service("projectMenuService")
@Transactional
public class ProjectMenuServiceImpl extends CommonServiceImpl implements ProjectMenuServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(ProjectMenuServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectMenu> getProjectMenuList(ProjectMenu projectMenu) {
        StringBuffer hql = new StringBuffer();
        hql.append("from ProjectMenu t where 1=1 ");

        Map<String, Object> queryMap = getQueryParam(hql, projectMenu);
        String hqlStr = (String)queryMap.get("hqlStr");
        List<Object> paramList = (List<Object>)queryMap.get("paramList");

        List<ProjectMenu> list = new ArrayList<ProjectMenu>();
        list = executeQuery(hqlStr, paramList.toArray());

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectMenu> getUsableProjectMenuList(ProjectMenu projectMenu) {

        StringBuffer hql = new StringBuffer();
        hql.append("from ProjectMenu t where 1=1 ");
        hql.append(" and t.status = '1' ");

        Map<String, Object> queryMap = getQueryParam(hql, projectMenu);
        String hqlStr = (String)queryMap.get("hqlStr");
        List<Object> paramList = (List<Object>)queryMap.get("paramList");

        List<ProjectMenu> list = new ArrayList<ProjectMenu>();

        list = executeQuery(hqlStr, paramList.toArray());

        return list;
    }

    /**
     * Description: 组装查询方法
     * 
     * @param hql
     * @return
     * @see
     */
    private Map<String, Object> getQueryParam(StringBuffer hql, ProjectMenu menu) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<Object> paramList = new ArrayList<Object>();
        if (menu != null) {
            if (StringUtils.isNotEmpty(menu.getText())) {
                hql.append(" and t.text = ?");
                paramList.add(menu.getText());
            }

            if (StringUtils.isNotEmpty(menu.getId())) {
                hql.append(" and t.id = ?");
                paramList.add(menu.getId());
            }

            if (null != menu.getUnderProject()) {
                hql.append(" and t.underProject = ?");
                paramList.add(menu.getUnderProject());
            }

            if (null != menu.getStatus()) {
                hql.append("and  t.status =?");
                paramList.add(menu.getStatus());
            }

        }
        resultMap.put("hqlStr", hql.toString());
        resultMap.put("paramList", paramList);

        return resultMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectMenu> getUsableChildrenList(String parentId) {
        StringBuffer hql = new StringBuffer();
        hql.append("from ProjectMenu t where 1=1 ");
        hql.append(" and t.status = '1' and parentId= ? order by sort");

        List<Object> paramList = new ArrayList<Object>();
        paramList.add(parentId);
        return sessionFacade.executeQuery(hql.toString(), paramList.toArray());

    }

    @SuppressWarnings("unchecked")
    @Override
    public String constructionProjectMenuTree(String projectId,String currentUserId) {

        List<TreeNode> list = new ArrayList<TreeNode>();
        String projLibFlag = "0";
        if (StringUtils.isEmpty(projectId)) {
            List<ProjectMenu> parentMenus = findByQueryString("FROM ProjectMenu M WHERE M.underProject = 0 AND ( M.text <> '"
                                                              + ProjectConstants.PROJ_MENU_PROJECT
                                                              + "' OR M.parentId is null ) AND M.status = 1");
            for (ProjectMenu menu : parentMenus) {
                TreeNode node = new TreeNode(menu.getId(), menu.getParentId(), menu.getText(),
                    menu.getText(), true);
                node.setDataObject(menu);
                node.setIcon("webpage/com/glaway/ids/common/tree-point.png");
                list.add(node);
            }
        }
        else {
            projLibFlag = "1";
        }
        // boolean isPMO = projRoleService.isSysRoleByUserIdAndRoleCode(
        // UserUtil.getInstance().getUser().getId(), ProjectRoleConstants.PMO);
        // String pmoCondition = "";
        // if(isPMO){
        // pmoCondition = "&authRoleCode='PMO'";
        // }
        StringBuffer menuHqlBuffer = new StringBuffer();
        if (StringUtils.isNotEmpty(projectId)) {

            menuHqlBuffer.append(" SELECT P.ID ID,");
            menuHqlBuffer.append(" P.NAME || '-' || P.PROJECTNUMBER TEXT,");
            menuHqlBuffer.append(" 'webpage/com/glaway/ids/common/tree-point.png' ICON,");
            menuHqlBuffer.append(" L.TEAMID TEAMID,");
            menuHqlBuffer.append(" M.ID M_ID,");
            menuHqlBuffer.append(" M.PARENTID M_PARENTID,");
            menuHqlBuffer.append(" M.PROJECTID M_PROJECTID,");
            menuHqlBuffer.append(" M.TEXT M_TEXT,");
            menuHqlBuffer.append(" M.ICONCLS M_ICONCLS,");
            menuHqlBuffer.append(" M.URL M_URL,");
            menuHqlBuffer.append(" M.UNDERPROJECT M_UNDERPROJECT,");
            menuHqlBuffer.append(" M.STATUS M_STATUS,");
            menuHqlBuffer.append(" M.INSERTRECENTLY M_INSERTRECENTLY,");
            menuHqlBuffer.append(" M.SORT M_SORT");

            menuHqlBuffer.append(" FROM PM_MENU M,");
            menuHqlBuffer.append(" PM_PROJECT P,");
            menuHqlBuffer.append(" PM_PROJ_TEAM_LINK L");
            menuHqlBuffer.append(" WHERE P.ID = '" + projectId + "'");
            menuHqlBuffer.append(" AND P.ID = L.PROJECTID");
            menuHqlBuffer.append(" AND( M.UNDERPROJECT = 1 OR M.TEXT = '"
                                 + ProjectConstants.PROJ_MENU_PROJECT + "')");
            menuHqlBuffer.append(" AND M.PARENTID IS NOT NULL");
            menuHqlBuffer.append(" AND M.STATUS = 1");
            menuHqlBuffer.append(" ORDER BY M.INSERTRECENTLY ASC, M.SORT ASC");
        }
        else {
            menuHqlBuffer.append(" SELECT R.PROJECTID ID,");
            menuHqlBuffer.append(" R.CREATETIME CREATETIME,");
            menuHqlBuffer.append(" R.NAME TEXT,");
            menuHqlBuffer.append(" 'webpage/com/glaway/ids/common/tree-point.png' ICON,");
            menuHqlBuffer.append(" R.TEAMID TEAMID,");
            menuHqlBuffer.append(" M.ID M_ID,");
            menuHqlBuffer.append(" M.PARENTID M_PARENTID,");
            menuHqlBuffer.append(" M.PROJECTID M_PROJECTID,");
            menuHqlBuffer.append(" M.TEXT M_TEXT,");
            menuHqlBuffer.append(" M.ICONCLS M_ICONCLS,");
            menuHqlBuffer.append(" M.URL M_URL,");
            menuHqlBuffer.append(" M.UNDERPROJECT M_UNDERPROJECT,");
            menuHqlBuffer.append(" M.STATUS M_STATUS,");
            menuHqlBuffer.append(" M.INSERTRECENTLY M_INSERTRECENTLY,");
            menuHqlBuffer.append(" M.SORT M_SORT");
            menuHqlBuffer.append(" FROM PM_MENU M,");

            menuHqlBuffer.append(" (SELECT ROWNUM RN, R1.PROJECTID,R1.CREATETIME,R1.NAME,R1.TEAMID");
            menuHqlBuffer.append(" FROM");
            menuHqlBuffer.append(" (SELECT RE.PROJECTID, RE.CREATETIME,P.NAME || '-' || P.PROJECTNUMBER NAME,L.TEAMID");
            menuHqlBuffer.append(" FROM PM_RECENTLY_PROJECT RE,PM_PROJECT P,PM_PROJ_TEAM_LINK L");
            menuHqlBuffer.append(" WHERE");
            menuHqlBuffer.append(" RE.CREATEBY = '" + currentUserId
                                 + "'");
            menuHqlBuffer.append(" AND P.ID = RE.PROJECTID");
            menuHqlBuffer.append(" AND P.ID = L.PROJECTID");
            menuHqlBuffer.append(" AND P.AVALIABLE = '1'");
            menuHqlBuffer.append(" ORDER BY RE.CREATETIME DESC) R1");
            menuHqlBuffer.append(" ) R");
            menuHqlBuffer.append(" WHERE R.RN <= 10");
            menuHqlBuffer.append(" AND( M.UNDERPROJECT = 1 OR M.TEXT = '" 
                                 + ProjectConstants.PROJ_MENU_PROJECT + "')");
            menuHqlBuffer.append(" AND M.PARENTID IS NOT NULL");
            menuHqlBuffer.append(" AND M.STATUS = 1");
            menuHqlBuffer.append(" ORDER BY R.CREATETIME DESC, M.INSERTRECENTLY ASC, M.SORT ASC");
        }
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(menuHqlBuffer.toString());

        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String text = map.get("M_TEXT").toString().trim();
                if (ProjectConstants.PROJ_MENU_PROJECT.equals(text)) {
                    ProjectMenu pmenu = new ProjectMenu();

                    pmenu.setId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                    pmenu.setText(StringUtils.isNotEmpty((String)map.get("TEXT")) ? (String)map.get("TEXT") : "");
                    pmenu.setParentId(StringUtils.isNotEmpty((String)map.get("M_PARENTID")) ? (String)map.get("M_PARENTID") : "");
                    pmenu.setInsertRecently(Integer.parseInt(map.get("M_INSERTRECENTLY").toString()));

                    TreeNode node = new TreeNode(pmenu.getId(), pmenu.getParentId(),
                        pmenu.getText(), pmenu.getText(), true);
                    node.setDataObject(pmenu);
                    node.setPid(pmenu.getParentId());
                    node.setIcon(StringUtils.isNotEmpty((String)map.get("ICON")) ? (String)map.get("ICON") : "");
                    list.add(node);
                }
                else {
                    ProjectMenu child = new ProjectMenu();
                    child.setId(StringUtils.isNotEmpty((String)map.get("M_ID")) ? (String)map.get("M_ID") : "");
                    child.setParentId(StringUtils.isNotEmpty((String)map.get("M_PARENTID")) ? (String)map.get("M_PARENTID") : "");
                    child.setProjectId(StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "");
                    child.setText(StringUtils.isNotEmpty((String)map.get("M_TEXT")) ? (String)map.get("M_TEXT") : "");
                    child.setIconCls(StringUtils.isNotEmpty((String)map.get("ICON")) ? (String)map.get("ICON") : "");
                    child.setUrl(StringUtils.isNotEmpty((String)map.get("M_URL")) ? (String)map.get("M_URL") : "");
                    if (StringUtils.isNotEmpty(child.getUrl())) {

                        if ("计划".equals(child.getText())) {
                            child.setUrl(child.getUrl()
                                         + "&isIframe=true"
                                         + "&id="
                                         + (StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "")
                                         + "&teamId="
                                         + (StringUtils.isNotEmpty((String)map.get("TEAMID")) ? (String)map.get("TEAMID") : "")
                                         + "&projLibFlag=" + projLibFlag + "&authRoleCode="
                                         + "PMO");
                        }
                        else {
                            child.setUrl(child.getUrl()
                                         + "&id="
                                         + (StringUtils.isNotEmpty((String)map.get("ID")) ? (String)map.get("ID") : "")
                                         + "&teamId="
                                         + (StringUtils.isNotEmpty((String)map.get("TEAMID")) ? (String)map.get("TEAMID") : "")
                                         + "&projLibFlag=" + projLibFlag + "&authRoleCode="
                                         + "PMO");
                        }

                    }
                    child.setUnderProject(Integer.parseInt(map.get("M_UNDERPROJECT").toString()));
                    child.setStatus(Integer.parseInt(map.get("M_STATUS").toString()));
                    child.setInsertRecently(Integer.parseInt(map.get("M_INSERTRECENTLY").toString()));
                    child.setSort(Integer.parseInt(map.get("M_SORT").toString()));

                    TreeNode node = new TreeNode(child.getId(), child.getProjectId(),
                        child.getText(), child.getText(), true);
                    node.setDataObject(child);
                    node.setIcon(child.getIconCls());
                    list.add(node);
                }
            }
        }
        String json = JsonUtil.getListJsonWithoutQuote(list);
        return json;
    }
}