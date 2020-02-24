package com.glaway.ids.rdtask.task.controller;

import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.rdtask.task.form.TaskFeedbackInfo;
import com.glaway.ids.rdtask.task.service.TaskFeedbackServiceI;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 任务反馈控制
 * @author: sunmeng
 * @ClassName: TaskFeedbackRestController
 * @Date: 2019/8/8-13:53
 * @since
 */
@Api(tags = {"任务完工反馈接口"})
@RestController
@RequestMapping("/feign/taskFeedbackRestController")
public class TaskFeedbackRestController {
    /**
     * 任务反馈接口服务
     */
    @Autowired
    private TaskFeedbackServiceI taskFeedbackService;

    /**
     * 反馈信息保存
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "反馈信息保存", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/saveTaskFeedback")
    public FeignJson saveTaskFeedback(@RequestBody Map<String, Object> map) {
        return taskFeedbackService.saveTaskFeedback(map);
    }

    /**
     * 完工反馈提交审批
     *
     * @param leader
     * @param planId
     * @param userId
     * @return
     */
    @ApiOperation(value = "完工反馈提交审批", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "leader", value = "室领导", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划主键", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户主键", required = true, dataType = "String")})
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/doSubmitApprove")
    public FeignJson doSubmitApprove(@RequestParam(value = "leader", required = false) String leader,
                                     @RequestParam(value = "planId", required = false) String planId, @RequestParam(value = "userId", required = false) String userId) {
        return taskFeedbackService.doSubmitApprove(leader, planId, userId);
    }
}
