package com.glaway.ids.project.plan.controller;

import io.swagger.annotations.*;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.ids.project.plan.service.ApplyProcTemplateServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wqb
 * @version V1.0
 * @Title: Controller
 * @Description: 调用研发流程模板
 * @date 2019年10月20日 09:31:04
 */
@Api(tags = {"调用研发流程模板接口"})
@RestController
@RequestMapping("/feign/applyProcTemplateRestController")
public class ApplyProcTemplateRestController extends BaseController {
    /**
     * Logger for this class
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(ApplyProcTemplateRestController.class);
    /**
     * 调用研发流程模板接口
     */
    @Autowired
    private ApplyProcTemplateServiceI applyProcTemplateService;

    /**
     * 调用研发流程模板分解计划
     *
     * @param parentId
     * @param templateId
     * @param currentId
     * @return
     */
    @ApiOperation(value = "调用研发流程模板分解计划", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "计划Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "研发流程模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "templateResolveForPlan")
    boolean templateResolveForPlan(@RequestParam(value = "parentId") String parentId, @RequestParam(value = "templateId") String templateId, @RequestParam(value = "currentId", required = false) String currentId) {
        boolean flag = applyProcTemplateService.templateResolveForPlan(parentId, templateId, currentId);
        return flag;
    }
}
