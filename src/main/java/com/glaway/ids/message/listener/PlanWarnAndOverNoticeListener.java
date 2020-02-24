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
import com.glaway.foundation.websocket.constant.WSConstant;
import com.glaway.foundation.websocket.processor.WSMessageProcessor;
import com.glaway.ids.message.constant.MessageConstants;
import com.glaway.ids.message.receiver.service.MessageReceiverServiceI;
import com.glaway.ids.project.plan.entity.Plan;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计划预警和预期监听
 * 〈功能详细描述〉
 * @author jishuai
 * @version 2016年6月15日
 * @see PlanWarnAndOverNoticeListener
 * @since
 */
@Service("PlanWarnAndOverNoticeListener")
public class PlanWarnAndOverNoticeListener extends WSMessageProcessor {

    /**
     * 获取消息接收人接口
     */
    @Autowired
    private MessageReceiverServiceI messageReceiverService;

    @Autowired
    private Environment env;

    @Override
    public String setMessageConfigKey() {
        return "planWarnAndOverNotice";
    }

    @Override
    public String setSendMessageType() {
        return WSConstant.WEBSOCKET_SEND_TARGET_USERS;
    }

    @Override
    public List<TSUserDto> doSetReceiveUsers(JoinPoint joinPoint, Object returnValue) {
        Object[] argArray = joinPoint.getArgs();
        Plan p = (Plan)returnValue;
        return messageReceiverService.getMessageReceivers(
            MessageConstants.MESSAGE_PLAN_WARNANDOVER, p.getId());
    }

    @Override
    public Map<String, Object> doSetVariables(JoinPoint joinPoint, Object returnValue) {
        Object[] argArray = joinPoint.getArgs();
        Plan p = (Plan)returnValue;
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("projectName", p.getProjectName());
        variables.put("planName", p.getPlanName());
        variables.put("planEndTime", p.getPlanEndTime());
        variables.put("planId", p.getId());
        variables.put("appkeys", env.getProperty("server.runtime.wmsApplication"));
        return variables;
    }

/*    public String setSendSyncOrAsyncHandle() {
        return WSConstant.WEBSOCKET_SEND_ASYNC_MESSGAE;
    }
*/
    @Override
    public boolean setPushTaskQueueHandle() {
        // TODO Auto-generated method stub
        return false;
    }
}
