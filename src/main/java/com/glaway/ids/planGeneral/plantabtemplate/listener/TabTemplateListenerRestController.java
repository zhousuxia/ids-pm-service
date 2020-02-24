package com.glaway.ids.planGeneral.plantabtemplate.listener;

import com.alibaba.fastjson.JSON;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.TabTemplateEntityServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @Description: 页签模板流程监听
 * @author: WUXING
 * @ClassName: TabTemplateListenerRestController
 * @Date: 2019/11/29-9:40
 * @since
 */
@RestController
@RequestMapping("/feign/tabTemplateListenerRestController")
public class TabTemplateListenerRestController {

    @Autowired
    private TabTemplateEntityServiceI tabTemplateEntityServiceImpl;

    @RequestMapping(value = "notify")
    public void notify(@RequestParam("variables") String variables) throws Exception {
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

        tabTemplateEntityServiceImpl.updateBizCurrentForProcessComplete(id);
    }
}
