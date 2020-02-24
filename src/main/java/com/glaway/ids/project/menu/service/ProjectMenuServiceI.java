package com.glaway.ids.project.menu.service;


import java.util.List;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.ids.project.menu.entity.ProjectMenu;


/**
 * 左侧项目列表树
 * 
 * @author wangshen
 * @version 2015年4月14日
 * @see ProjectMenuServiceI
 * @since
 */
public interface ProjectMenuServiceI extends CommonService {
    /**
     * Description: <br>条件查询项目菜单
     * 
     * @param projectMenu 项目菜单对象
     * @return
     * @see
     */

    List<ProjectMenu> getProjectMenuList(ProjectMenu projectMenu);

    /**
     * Description: <br>条件查询可用项目菜单
     * 
     * @param projectMenu 项目菜单对象
     * @return
     * @see
     */

    List<ProjectMenu> getUsableProjectMenuList(ProjectMenu projectMenu);

    /**
     * Description: <br>根据父节点查询可用子节点
     * 
     * @param parentId 父菜单id
     * @return
     * @see
     */

    List<ProjectMenu> getUsableChildrenList(String parentId);
    
    /**
     * 
     * 根据项目ID是否有值和他的值组装项目管理页面的左侧最新访问项目树
     * 
     * @param projectId 项目id
     * @return 
     * @see
     */
    String constructionProjectMenuTree(String projectId,String currentUserId);

}
