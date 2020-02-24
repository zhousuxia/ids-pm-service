package com.glaway.ids.project.projecttemplate.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.glaway.foundation.common.util.ServiceDelegate;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目模板正常流程处理方式
 *
 * @author duanpengfei
 * @filename ActivitiTaskListener.java
 * @date 2015-1-5
 * @copyright
 */
@Api(tags = {"项目模板审批流程通过监听接口"})
@RestController
@RequestMapping("/feign/projTemplateListenerRestController")
public class ProjTemplateListenerRestController {
    private static final long serialVersionUID = 1L;
    /**
     * 注入项目projTemplateService
     */
    @Autowired
    private ProjTemplateServiceI projTemplateService;

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
        projTemplateService.updateBizCurrentForProcessComplete(id);
    }

    public ProjTemplateServiceI getProjTemplateService() {
        return (ProjTemplateServiceI) ServiceDelegate.getService("projTemplateService");
    }
}
