package com.glaway.ids.message.controller;

import com.glaway.foundation.common.util.DateUtil;
import com.glaway.ids.config.constant.SwitchConstants;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.PlanFlowForworkServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api(tags = {"计划逾期预警接口"})
@RestController
@RequestMapping("/feign/planWarnAndOverTimerRestController")
public class PlanWarnAndOverTimerRestController{
	
    @Autowired
    private PlanFlowForworkServiceI planFlowForworkService;
    
    @Autowired
    private PlanServiceI planService;
    
    @Autowired
    private ParamSwitchServiceI paramSwitchService;

    @ApiOperation(value = "定时任务", httpMethod = "GET")
    @RequestMapping(value = "executeJob")
    public void executeJob()
        throws JobExecutionException {
        Plan p = new Plan();
        List<Plan> planList = planService.queryPlanListForTime(p, 1, 10, false);
        String warningDay = paramSwitchService.getSwitch(SwitchConstants.PLANWARNINGDAYS);
        String currentDate = DateUtil.getStringFromDate(new Date(),
            DateUtil.YYYY_MM_DD);
        for (Plan plan : planList) {
            String warningDayFlag = "before";
            if (StringUtils.isNotEmpty(warningDay)) {
                if (warningDay.trim().startsWith("-")) {
                    warningDayFlag = "after";
                    warningDay = warningDay.trim().replace("-", "");
                }
            }
            else {
                warningDay = "0";
            }

            if (StringUtils.isEmpty(warningDay)) {
                warningDay = "0";
            }
            Date planEndTime = plan.getPlanEndTime();
            if (planEndTime != null) {
                Date warningDate = planFlowForworkService.dateChange(planEndTime,
                    Integer.parseInt(warningDay), warningDayFlag);
                Date nowDate = DateUtil.convertStringDate2DateFormat(
                    currentDate, DateUtil.YYYY_MM_DD);
                if (warningDate.getTime() <= nowDate.getTime()) {
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    } 
                    planFlowForworkService.getPlanWarnAndOver(plan);
                }
            }
        }
    }

}
