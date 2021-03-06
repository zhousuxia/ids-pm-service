package com.glaway.ids.project.plan.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.project.plan.service.PlanServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description: 计划下发审批通过监听
 * @author: sunmeng
 * @ClassName: PlanAssignExcutionRestController
 * @Date: 2019/7/30-16:15
 * @since
 */
@Api(tags = {"计划下发审批通过监听服务接口"})
@RestController
@RequestMapping("/feign/planAssignExcutionRestController")
public class PlanAssignExcutionRestController {
    @Autowired
    private FeignUserService userService;
    @Autowired
    private PlanServiceI planService;

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
        String formId = arrays[1];
        String assignerName = variablesMap.get("assigner").toString();
        TSUserDto assigner = new TSUserDto();
        if (StringUtils.isNotEmpty(assignerName)) {
            assigner = userService.getUserByUserName(assignerName);
        }
        // 审批通过之后更新计划生命周期并将记录下达人
        if (StringUtils.isNotEmpty(formId) && StringUtils.isNotBlank(assigner.getId())) {
            planService.planLifeCycleForward(formId, assigner);
        }
    }
}
