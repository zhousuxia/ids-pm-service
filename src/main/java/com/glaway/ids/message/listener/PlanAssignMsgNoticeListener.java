/*
 * 文件名：NoticeMessageProcessor.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：yuyang
 * 修改时间：2016年4月27日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.message.listener;


import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.websocket.constant.WSConstant;
import com.glaway.foundation.websocket.processor.WSMessageProcessor;
import com.glaway.ids.message.receiver.service.MessageReceiverServiceI;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 计划废弃监听
 * 〈功能详细描述〉
 * 
 * @author wqb
 * @version 2016年6月15日 14:47:09
 * @see PlanAssignMsgNoticeListener
 * @since
 */
@Service("PlanAssignBackMsgNoticeListener")
public class PlanAssignMsgNoticeListener extends WSMessageProcessor {
    
    /**
     * 获取消息接收人接口
     */
    @Autowired
    private MessageReceiverServiceI messageReceiverService;
    
    /**
     * 注入计划planService
     */
    @Autowired
    private PlanServiceI planService;

    /**
     * 注入项目planService
     */
    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private FeignUserService userService;

    @Autowired
    private Environment env;
   
    @Override
    public String setMessageConfigKey() {
        return "planAssignBackMsgNotice";
    }

    @Override
    public String setSendMessageType() {
        return WSConstant.WEBSOCKET_SEND_TARGET_USERS;
    }
    
//    @Override
//    public TSUserDto doSetSendUser(JoinPoint joinPoint, Object returnValue) {
//        Object[] argArray = joinPoint.getArgs();
//        TSUserDto sendUser = (TSUserDto)argArray[1];
//        return sendUser;
//    }

    @Override
    public List<TSUserDto> doSetReceiveUsers(JoinPoint joinPoint, Object returnValue) {
        Object[] argArray = joinPoint.getArgs();
        String planId = (String)argArray[0];
        List<TSUserDto> messageReceivers = new ArrayList<TSUserDto>();
        Plan p = planService.getEntity(planId);
        if(!CommonUtil.isEmpty(p)&&!CommonUtil.isEmpty(p.getLauncher())){
            TSUserDto t =userService.getUserByUserId(p.getLauncher());
            if(!CommonUtil.isEmpty(t)){
                messageReceivers.add(t);
            }
        }
        //return messageReceiverService.getMessageReceivers(MessageConstants.MESSAGE_PLAN_CHANGE, planId);
        return messageReceivers;
    }

    @Override
    public Map<String, Object> doSetVariables(JoinPoint joinPoint, Object returnValue) {
        Object[] argArray = joinPoint.getArgs();
        String planId = (String)argArray[0];
        Plan plan = planService.getEntity(planId);
        Project project = projectService.getProjectEntity(plan.getProjectId());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("projectName", project.getName()+"-"+project.getProjectNumber());
        variables.put("planName", plan.getPlanName());
        variables.put("planId", planId);
        variables.put("appkeys", env.getProperty("server.runtime.wmsApplication"));
        return variables;
    }
    
    @Override
    public boolean setPushTaskQueueHandle() {
        // TODO Auto-generated method stub
        return false;
    }

}
