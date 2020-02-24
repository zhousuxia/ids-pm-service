package com.glaway.ids.project.plantemplate.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import com.glaway.foundation.common.log.BaseLog;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.util.ServiceDelegate;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plantemplate.constant.PlantemplateConstant;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.service.PlanTemplateServiceI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 计划模板驳回处理方式
 *
 * @author duanpengfei
 * @filename ActivitiTaskListener.java
 * @date 2015-1-5
 * @copyright
 */
@Api(tags = {"计划模板驳回处理监听接口"})
@RestController
@RequestMapping("/feign/planTemplateGoBackListenerRestController")
public class PlanTemplateGoBackListenerRestController {
    /**
     * 操作日志接口
     */
    private static final BaseLog log = BaseLogFactory.getSystemLog(PlanTemplateGoBackListenerRestController.class);
    private static final long serialVersionUID = 1L;
    /**
     * 注入项目planTemplateService
     */
    @Autowired
    private PlanTemplateServiceI planTemplateService;

    /**
     * 实现监听器方法
     */
    @ApiOperation(value = "notify")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "variables", dataType = "String")
    })
    @RequestMapping("/notify")
    public void notify(@RequestParam("variables") String variables)
            throws Exception {
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
        String statusBeforeCommit = String.valueOf(variablesMap.get("status"));
        planTemplateService.updateBizCurrentWhenProcessRefuse(statusBeforeCommit, id);
    }
}
