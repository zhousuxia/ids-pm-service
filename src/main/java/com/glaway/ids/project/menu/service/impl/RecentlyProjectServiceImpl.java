package com.glaway.ids.project.menu.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.menu.entity.RecentlyProject;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.ids.project.menu.service.RecentlyProjectServiceI;


/**
 * 最近十条
 * 〈功能详细描述〉
 * 
 * @author wangshen
 * @version 2015年4月14日
 * @see RecentlyProjectServiceImpl
 * @since
 */
@Service("recentlyProjectService")
@Transactional
public class RecentlyProjectServiceImpl extends CommonServiceImpl implements RecentlyProjectServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(RecentlyProjectServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;
    

    @Autowired
    private FeignUserService userService;
    
    @Override
    public void updateRecentlyByProjectId(String projectId, TSUserDto userDto) {
        if (StringUtils.isNotEmpty(projectId)) {
        	//处理项目ID和webServic中调用不到当前用户的问题  如果是从kdd集成来的  项目ID 项目ID中包含 ,+当前用户的ID 为防止空指针问题处理   因为 webServic UserUtil.getInstance().getUser()  获取不到当前用户  服务端无法从页面获取到相关参数  
        	//改参数保存之前项目ID
        	String projectIdOld=projectId;
        	String [] idAndUserId=projectId.split(",");
        	TSUserDto user =new TSUserDto();
        	//判断是否是从 产品中复制到项目的处理  
        	if(idAndUserId.length>1){
        		projectId=idAndUserId[0];
        		user = userService.getUserByUserId(idAndUserId[1]);;
        	}else{
        		user = userDto;
        	}
            // 根据项目id查询当前用户的记录并删除
            deleteRecentlyByProjectIdAndUserId(projectId, user.getId());
            Project project = (Project)sessionFacade.getEntity(Project.class,projectId);
            //重新保存项目
            if (null != project) {
                RecentlyProject recently = new RecentlyProject();
                recently.setProjectId(projectId);
                recently.setCreateBy(user.getId());
                recently.setCreateName(user.getRealName());
                recently.setCreateTime(new Date());
                save(recently);
            }
        }
    }



    @Override
    public void deleteRecentlyByProjectIdAndUserId(String projectId, String userId) {
        if (StringUtils.isNotEmpty(projectId)) {
            RecentlyProject recentlyProject = new RecentlyProject();
            recentlyProject.setProjectId(projectId);
            if (StringUtils.isNotEmpty(userId)) {
                recentlyProject.setCreateBy(userId);
            }
            // 根据项目id查询当前用户的记录并删除
            List<RecentlyProject> recentlyProjects = searchRecentlyProject(recentlyProject);
            batchDelRecently(recentlyProjects);

        }

    }


    @SuppressWarnings("unchecked")
    @Override
    public List<RecentlyProject> searchRecentlyProject(RecentlyProject recentlyProject) {
        //修改BUG 项目树点击计划的时候项目树 刷新不出来问题   疑似框架缺陷  重写查询方法
        StringBuffer hql = new StringBuffer();
        hql.append("from RecentlyProject t where 1=1 and t.createBy=?  ");
        //
        if(StringUtils.isNotEmpty(recentlyProject.getProjectId()))
        {
            hql.append(" and t.projectId=?  ");
        }

        hql.append(" order by t.createTime desc ");

        List<Object> paramList = new ArrayList<Object>();
        //处理产品复制成项目的处理   由于webServic UserUtil.getInstance().getUser()  获取不到当前用户  所以通过客户端传递的Userid塞入相关当前用户的值
        paramList.add(StringUtil.isNotEmpty(recentlyProject.getCreateBy())?recentlyProject.getCreateBy():UserUtil.getInstance().getUser().getId());
        paramList.add(recentlyProject.getProjectId());

        List<RecentlyProject> list = new ArrayList<RecentlyProject>();
        list = pageList(hql.toString(), paramList.toArray(), 0,
                Integer.parseInt(ProjectConstants.RECENTLY_PROJECT_NUM));

        return list;
    }


    @Override
    public void batchDelRecently(List<RecentlyProject> recentlyProjects) {
        for (RecentlyProject recently : recentlyProjects) {
            RecentlyProject project = new RecentlyProject();
            project.setCreateBy(recently.getCreateBy());
            project.setProjectId(recently.getProjectId());
            List<RecentlyProject> result = searchRecentlyProject(project);
            deleteAllEntitie(result);
        }
    }


}