package com.glaway.ids.message.listener;


import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.websocket.constant.WSConstant;
import com.glaway.foundation.websocket.processor.WSMessageProcessor;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 流程分解计划审批驳回通知
 * 
 * @author blcao
 * @version 2017年12月21日
 * @see FlowTaskAssignRejectNoticeListener
 * @since
 */
@Service("FlowTaskAssignRejectNoticeListener")
public class FlowTaskAssignRejectNoticeListener extends WSMessageProcessor {

    @Autowired
    private Environment env;

    @Override
    public String setMessageConfigKey() {
        return "flowTaskAssignRejectNotice";
    }

    @Override
    public String setSendMessageType() {
        return WSConstant.WEBSOCKET_SEND_TARGET_USERS;
    }

    @Override
    public List<TSUserDto> doSetReceiveUsers(JoinPoint joinPoint, Object returnValue) {
        TSUserDto user = (TSUserDto)returnValue;
        List<TSUserDto> messageReceivers = new ArrayList<TSUserDto>();
        if (!CommonUtil.isEmpty(user)) {
            messageReceivers.add(user);
        }
        return messageReceivers;
    }

    @Override
    public boolean setPushTaskQueueHandle() {
        return false;
    }

    @Override
    public Map<String, Object> doSetVariables(JoinPoint joinPoint, Object returnValue) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("appkeys", env.getProperty("server.runtime.wmsApplication"));
        return variables;
    }
}
