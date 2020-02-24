package com.glaway.ids.project.menu.controller;

import io.swagger.annotations.*;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.menu.service.ProjectMenuServiceI;
import com.glaway.ids.project.menu.service.RecentlyProjectServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangshen
 * @version V1.0
 * @Title: projectMenuController
 * @Description: 项目菜单
 */
@Api(tags = {"项目菜单接口"})
@RestController
@RequestMapping("/feign/projectMenuRestController")
public class ProjectMenuRestController extends BaseController {
    /**
     * 左侧项目列表树
     */
    @Autowired
    private ProjectMenuServiceI projectMenuService;
    /**
     * 最近访问项目
     */
    @Autowired
    private RecentlyProjectServiceI recentlyProjectService;
    /**
     * 消息信息
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ApiOperation(value = "更新最近访问的项目",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @RequestMapping(value = "updateRecentlyByProjectId")
    public void updateRecentlyByProjectId(@RequestParam("projectId") String projectId, @RequestBody TSUserDto userDto) {
        recentlyProjectService.updateRecentlyByProjectId(projectId, userDto);
    }

    @ApiOperation(value = "根据项目ID是否有值和他的值组装项目管理页面的左侧最新访问项目树",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentUserId", value = "用户id", dataType = "String")
    })
    @RequestMapping(value = "constructionProjectMenuTree")
    public String constructionProjectMenuTree(@RequestParam(value = "projectId", required = false) String projectId, @RequestParam(value = "currentUserId", required = false) String currentUserId) {
        String listStr = projectMenuService.constructionProjectMenuTree(projectId, currentUserId);
        return listStr;
    }

    @ApiOperation(value = "获取最近访问树",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentUserId", value = "用户id", dataType = "String")
    })
    @RequestMapping(value = "getconstructionProjectMenuTree")
    public FeignJson getconstructionProjectMenuTree(@RequestParam(value = "projectId", required = false) String projectId, @RequestParam(value = "currentUserId", required = false) String currentUserId) {
        String listStr = projectMenuService.constructionProjectMenuTree(projectId, currentUserId);
        FeignJson j = new FeignJson();
        j.setObj(listStr);
        return j;
    }
}
