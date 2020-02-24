package com.glaway.ids.project.projectmanager.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 文档类型修改
 * @author: sunmeng
 * @ClassName: RepFileTypeEditRestController
 * @Date: 2019/8/5-10:20
 * @since
 */
@Api(tags = {"文档类型修改接口"})
@RestController
@RequestMapping("/feign/repFileTypeEditRestController")
public class RepFileTypeEditRestController {
    @Autowired
    private ProjLibServiceI projLibService;

    @ApiOperation(value = "notify")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "variables", dataType = "String")
    })
    @RequestMapping("/notify")
    public void notify(@RequestParam("variables") String variables) throws Exception {
        Map<String, Object> variablesMap = JSON.parseObject(variables, Map.class);
        String oid = variablesMap.get("oid").toString();
        String userId = variablesMap.get("userId").toString();
        if (oid == null || oid.equals("")) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String id = arrays[1];
        try {
            projLibService.forward(id, userId);
        } finally {
            projLibService.updatePlanProcess(id);
        }
    }
}
