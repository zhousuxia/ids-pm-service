package com.glaway.ids.project.plan.listener;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.project.plan.service.PlanServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by LHR on 2019/8/15.
 */
@Api(tags = {"计划下发流程结束监听接口"})
@RestController
@RequestMapping("/feign/stopPlanAssignExcutionRestController")
public class StopPlanAssignExcutionRestController {
    @Autowired
    private FeignUserService userService;
    @Autowired
    private SessionFacade sessionFacade;
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
        String userId = variablesMap.get("userId").toString();
        if (StringUtils.isEmpty(oid)) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String formId = arrays[1];
//        WorkFlowFacade workFlowFacade = getWorkFlowFacade();
//        PlanServiceI planService = getPlanService();
//        PlanAssignBackMsgNoticeI planAssignBackMsgNotice = getPlanAssignBackMsgNotice();
        planService.StopPlanAssignExcution(formId,userId);
//        ApprovePlanForm approvePlanForm = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, formId);
//        String taskId;
//        if (approvePlanForm.getProcInstId() != null && !"".equals(approvePlanForm.getProcInstId())) {
//            List<Task> taskList = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
//                    approvePlanForm.getProcInstId());
//            if (taskList != null && taskList.size() > 0) {
//                taskId = taskList.get(0).getId();
//            }
//        }
    }
}
