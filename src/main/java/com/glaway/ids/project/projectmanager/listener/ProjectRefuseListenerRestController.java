/*
 * 文件名：RiskProblemStartRestController.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：hp
 * 修改时间：2019年7月5日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.glaway.ids.project.projectmanager.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.ResourceUtil;
import com.glaway.foundation.common.util.ServiceDelegate;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.constant.ProjectStatusConstants;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.entity.ProjLog;
import com.glaway.ids.project.projectmanager.service.ProjLogServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@Api(tags = {"项目启动流程驳回监听接口"})
@RestController
@RequestMapping("/feign/projectRefuseListenerRestController")
public class ProjectRefuseListenerRestController {
    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private FeignUserService userService;

    public ProjectServiceI getProjectService() {
        return (ProjectServiceI) ServiceDelegate.getService("projectService");
    }

    public ProjLogServiceI getProjLogService() {
        return (ProjLogServiceI) ServiceDelegate.getService("projLogService");
    }

    @ApiOperation(value = "notify")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "variables", dataType = "String")
    })
    @RequestMapping("/notify")
    public void notify(@RequestParam("variables") String variables) {
        Map<String, Object> variablesMap = JSON.parseObject(variables, Map.class);
        String oid = variablesMap.get("oid").toString();
        if (oid == null || oid.equals("")) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String id = arrays[1];
        ProjectServiceI projectService = getProjectService();
        Project project = projectService.getProjectEntity(id);
        project.setIsRefuse(ProjectConstants.REFUSED);
        projectService.update(project);
        ProjLogServiceI projLogService = getProjLogService();
        /*String common = (String)runtimeService.getVariable(executionId, "act_last_comment");*/
        String common = variablesMap.get("act_last_comment").toString();
        String message = "项目启动流程被驳回";
        TSUserDto userDto = userService.getUserByUserName(variablesMap.get("leader").toString());
        ProjLog projLog = new ProjLog();
        projLog.setProjectId(id);
        projLog.setProjectNumber(project.getProjectNumber());
        projLog.setLogInfo(message);
        projLog.setRemark(common);
        projLog.setCreateBy(userDto.getId());
        projLog.setCreateTime(new Date());
        projLog.setCreateUserName(userDto.getRealName() + "-" + userDto.getUserName());
        projLogService.initBusinessObject(projLog);
        projLogService.save(projLog);
    }
}
