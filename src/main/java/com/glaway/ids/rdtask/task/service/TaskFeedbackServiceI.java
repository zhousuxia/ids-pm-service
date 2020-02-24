package com.glaway.ids.rdtask.task.service;

import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.TaskFeedback;
import com.glaway.ids.rdtask.task.form.TaskFeedbackInfo;

/**
 * @Description: 〈任务反馈信息〉
 * @author: sunmeng
 * @ClassName: TaskFeedbackServiceI
 * @Date: 2019/8/8-13:58
 * @since
 */
public interface TaskFeedbackServiceI extends CommonService {

    /**
     * 查询任务反馈信息
     * @param taskFeedbackInfo 反馈对象
     * @param page 页数
     * @param rows 行数
     * @param isPage 是否分页
     * @return
     * @see
     */
    public List<TaskFeedback> findTaskFeedbackList(TaskFeedbackInfo taskFeedbackInfo, int page, int rows, boolean isPage);

    /**
     * 保存任务反馈信息
     * @param taskFeedbackInfo 反馈对象
     * @param checkP 校验
     * @param currentId 当前人员
     * @param type 类型
     * @throws GWException 
     * @see
     */
    public void saveTaskFeedback(TaskFeedbackInfo taskFeedbackInfo,String checkP , String currentId , String type) throws GWException;

    /**
     * 任务完工反馈,提交审批
     * @param plan 计划对象
     * @param leader 审批人
     * @param userId 用户id
     * @see
     */
    void taskApprove(Plan plan, String leader, String userId);

    /**
     * 完工反馈流程监听执行
     * @param id 流程id
     * @param map 流程相关信息
     */
    void notify(String id, Map<String, Object> map);

    /**
     * 完工反馈流程监听执行
     * @param id 流程id
     * @param map 流程相关信息
     */
    void updateForFeedBack(String id, Map<String, Object> map);

    /**
     * 驳回流程监听执行
     * @param id 流程id
     * @param map 流程相关信息
     */
    void goBackNotify(String id, Map<String, Object> map);

    /**
     * 反馈信息保存
     * @param map 流程相关信息map
     * @return
     */
    FeignJson saveTaskFeedback(Map<String,Object> map);

    /**
     * 完工反馈提交审批
     *
     * @param leader 审批人
     * @param planId 计划id
     * @param userId 用户id
     * @return
     */
    FeignJson doSubmitApprove(String leader, String planId, String userId);
}
