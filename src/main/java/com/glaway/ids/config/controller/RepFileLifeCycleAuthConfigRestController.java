package com.glaway.ids.config.controller;

import io.swagger.annotations.*;
import com.glaway.ids.config.service.RepFileLifeCycleAuthConfigServiceI;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhosuuxia
 * @version V1.0
 * @Title: Controller
 * @Description: 文档生命周期权限配置接口
 * @date 2019-06-13
 */
@Api(tags = {"文档生命周期权限配置接口"})
@RestController
@RequestMapping("/feign/repFileLifeCycleAuthConfigRestController")
public class RepFileLifeCycleAuthConfigRestController extends BaseController {
    private static final OperationLog log = BaseLogFactory.getOperationLog(RepFileLifeCycleAuthConfigRestController.class);
    /**
     * message
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Autowired
    private RepFileLifeCycleAuthConfigServiceI repFileLifeCycleAuthConfigService;

    /**
     * 根据policyName获取生命周期状态
     *
     * @param policyName
     * @return
     */
    @ApiOperation(value = "根据policyName获取生命周期状态", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "policyName", value = "生命周期策略名称", dataType = "String")
    })
    @RequestMapping(value = "queryLifeCyclePolicyList")
    public String queryLifeCyclePolicyList(@RequestParam("policyName") String policyName) {
        String list = repFileLifeCycleAuthConfigService.queryLifeCyclePolicyList(policyName);
        return list;
    }

    /**
     * 根据policyId获取生命周期状态
     *
     * @param policyId
     * @return
     */
    @ApiOperation(value = "根据policyId获取生命周期状态", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "policyId", value = "生命周期策略Id", dataType = "String")
    })
    @RequestMapping(value = "queryLifeCycleStatusEntityList")
    public String queryLifeCycleStatusEntityList(@RequestParam("policyId") String policyId) {
        String list = repFileLifeCycleAuthConfigService.queryLifeCycleStatusEntityList(policyId);
        return list;
    }
}
