package com.glaway.ids.project.plan.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plan.entity.BasicLine;
import com.glaway.ids.project.plan.service.BasicLineServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by LHR on 2019/8/13.
 */
@Api(tags = {"基线流程驳回监听"})
@RestController
@RequestMapping("/feign/basicLineBackExcutionLRestController")
public class BasicLineBackExcutionLRestController {
    @Autowired
    private SessionFacade sessionFacade;
    @Autowired
    BasicLineServiceI basicLineServiceI;

    /**
     * 基线流程驳回监听处理
     *
     * @param variables
     */
    @ApiOperation(value = "基线流程驳回监听处理", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "流程变量", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "notify")
    public void notify(@RequestParam("variables") String variables) {
        Map<String, Object> variablesMap = JSON.parseObject(variables, Map.class);
        String oid = variablesMap.get("oid").toString();
        if (StringUtils.isEmpty(oid)) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String basicLineId = arrays[1];
        if (StringUtils.isNotEmpty(basicLineId)) {
            basicLineServiceI.basicLineExcution(basicLineId);
//            BasicLine basicLine = (BasicLine) sessionFacade.getEntity(BasicLine.class, basicLineId);
//            basicLine.setBizCurrent(PlanConstants.PLAN_EDITING);
//            sessionFacade.saveOrUpdate(basicLine);
        }
    }
}
