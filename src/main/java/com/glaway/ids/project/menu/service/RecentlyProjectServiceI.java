package com.glaway.ids.project.menu.service;


import java.util.List;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.ids.project.menu.entity.RecentlyProject;


/**
 * 左侧项目最近N条列表树
 * 
 * @author wangshen
 * @version 2015年4月14日
 * @see RecentlyProjectServiceI
 * @since
 */
public interface RecentlyProjectServiceI extends CommonService {


    /**
     * 根据项目id更新最近访问项目
     * @param projectId 项目id
     * @param userDto 用户对象
     */
    void updateRecentlyByProjectId(String projectId, TSUserDto userDto);



    /**
     * Description: <br>根据项目id和用户id删除最近项目
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param projectId 项目id
     * @param userId 用户id
     * @see
     */
    void deleteRecentlyByProjectIdAndUserId(String projectId, String userId);


    /**
     * Description: <br>根据条件查询记录
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param recentlyProject 最近访问项目对象
     * @return
     * @see
     */

    List<RecentlyProject> searchRecentlyProject(RecentlyProject recentlyProject);


    /**
     * Description: <br>批量删除最近记录
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param recentlyProjects 最近访问项目集合
     * @see
     */
    void batchDelRecently(List<RecentlyProject> recentlyProjects);

}
