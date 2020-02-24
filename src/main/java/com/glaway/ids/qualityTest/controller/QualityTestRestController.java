package com.glaway.ids.qualityTest.controller;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.qualityTest.dto.CofigDataGridTestDto;
import com.glaway.ids.qualityTest.dto.CofigFormTestDto;
import com.glaway.ids.qualityTest.entity.CofigDataGridTest;
import com.glaway.ids.qualityTest.entity.CofigFormTest;
import com.glaway.ids.qualityTest.service.QualityTestServiceI;
import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: RestController
 * @author: sunmeng
 * @ClassName: QualityTestRestController
 * @Date: 2019/10/30-16:16
 * @since
 */
@RestController
@RequestMapping("/feign/qualityTestRestController")
public class QualityTestRestController extends BaseController {

    @Autowired
    private QualityTestServiceI qualityTestService;

    @RequestMapping("/addQualityDataGrid")
    public FeignJson addQualityDataGrid(@RequestParam(value = "useObjectId",required = false) String useObjectId) {
       return  qualityTestService.addQualityDataGrid(useObjectId);

    }

    @RequestMapping("/searchDataGrid")
    public FeignJson searchDataGrid(@RequestParam(value = "useObjectId",required = false) String useObjectId) {
        return qualityTestService.searchDataGrid(useObjectId);
    }

    @RequestMapping("/saveQualityDataGrid")
    public FeignJson saveQualityDataGrid(@RequestParam(value = "planId",required = false) String planId,
                                         @RequestParam(value = "useObjectId",required = false) String useObjectId) {
        return qualityTestService.saveQualityDataGrid(planId, useObjectId);
    }

    @RequestMapping("/saveFormTest")
    public FeignJson saveFormTest(@RequestBody CofigFormTestDto dto) {
        FeignJson j = new FeignJson();
        try {
            CofigFormTest formTest = new CofigFormTest();
            Dto2Entity.copyProperties(dto,formTest);
            qualityTestService.save(formTest);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }

    }

    @RequestMapping("/getFormTestByPlanId")
    public FeignJson getFormTestByPlanId(@RequestParam(value = "planId",required = false) String planId) {
        return qualityTestService.getFormTestByPlanId(planId);
    }

    @RequestMapping("/searchList")
    public FeignJson searchList(@RequestParam(value = "planId",required = false) String planId) {
        return qualityTestService.searchList(planId);
    }

    @RequestMapping("/updateFormTest")
    public FeignJson updateFormTest (@RequestBody CofigFormTestDto dto) {
        CofigFormTest formTest = new CofigFormTest();
        Dto2Entity.copyProperties(dto,formTest);
        return qualityTestService.updateFormTest(formTest);
    }

}
