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

import java.util.Map;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.constant.ProjectStatusConstants;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.util.ServiceDelegate;

@Api(tags = {"项目启动流程监听接口"})
@RestController
@RequestMapping("/feign/projectEndListenerRestController")
public class ProjectEndListenerRestController {
    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private static final long serialVersionUID = 1L;

    public RuntimeService getRuntimeService() {
        return (RuntimeService) ServiceDelegate.getService("runtimeService");
    }

    public ProjectServiceI getProjectService() {
        return (ProjectServiceI) ServiceDelegate.getService("projectService");
    }

    @ApiOperation(value = "notify")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "variables", dataType = "String")
    })
    @RequestMapping("/notify")
    public void notify(@RequestParam("variables") String variables) {
        Map<String, Object> variablesMap = JSON.parseObject(variables, Map.class);
        String oid = variablesMap.get("oid").toString();
        String leader = variablesMap.get("leader").toString();
        if (oid == null || oid.equals("")) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String id = arrays[1];
        ProjectServiceI projectService = getProjectService();
        // 更新项目生命周期及日志
        projectService.updateProjectBizCurrent(id,
                ProjectStatusConstants.PROJECT_STATUS_CHANGE_START, leader);
    }
}
