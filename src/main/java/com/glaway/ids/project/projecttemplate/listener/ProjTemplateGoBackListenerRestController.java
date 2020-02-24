package com.glaway.ids.project.projecttemplate.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import com.glaway.foundation.common.util.ServiceDelegate;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目模板驳回处理方式
 *
 * @author duanpengfei
 * @filename ActivitiTaskListener.java
 * @date 2015-1-5
 * @copyright
 */
@Api(tags = {"项目模板审批流程驳回监听接口"})
@RestController
@RequestMapping("/feign/projTemplateGoBackListenerRestController")
public class ProjTemplateGoBackListenerRestController {
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
        String flowStatus = variablesMap.get("flowStatus").toString();
        if (oid == null || oid.equals("")) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String id = arrays[1];
        projTemplateService.updateBizCurrentForProcessRefuse(id, flowStatus);
    }

    /**
     * 通过代理来获取projTemplateService
     *
     * @return
     * @see
     */
    public ProjTemplateServiceI getProjTemplateService() {
        return (ProjTemplateServiceI) ServiceDelegate.getService("projTemplateService");
    }
}
