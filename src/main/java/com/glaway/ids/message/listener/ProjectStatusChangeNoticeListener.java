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
import com.glaway.foundation.common.util.ServiceDelegate;
import com.glaway.foundation.websocket.constant.WSConstant;
import com.glaway.foundation.websocket.processor.WSMessageProcessor;
import com.glaway.ids.constant.ProjectStatusConstants;
import com.glaway.ids.message.constant.MessageConstants;
import com.glaway.ids.message.receiver.service.MessageReceiverServiceI;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目状态变更消息监听
 * 
 * @author blcao
 * @version 2016年6月16日
 * @see ProjectStatusChangeNoticeListener
 * @since
 */
@Service("projectStatusChangeNoticeListener")
public class ProjectStatusChangeNoticeListener extends WSMessageProcessor {

    /**
     * 注入项目projectService
     */
    @Autowired
    private MessageReceiverServiceI messageReceiverService;

    /**
     * 注入项目planService
     */
    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private Environment env;


    public MessageReceiverServiceI getMessageReceiverService() {
        return (MessageReceiverServiceI)ServiceDelegate.getService("messageReceiverService");
    }

    @Override
    public String setMessageConfigKey() {
        return "projectStatusChangeNotice";
    }

    @Override
    public String setSendMessageType() {
        return WSConstant.WEBSOCKET_SEND_TARGET_USERS;
    }

//    @Override
//    public TSUserDto doSetSendUser(JoinPoint joinPoint, Object returnValue) {
//        Object[] argArray = joinPoint.getArgs();
//        TSUserDto sendUser = (TSUserDto)argArray[2];
//        return sendUser;
//    }

    @Override
    public List<TSUserDto> doSetReceiveUsers(JoinPoint joinPoint, Object returnValue) {
        Object[] argArray = joinPoint.getArgs();
        String projectId = (String)argArray[0];
        // 获取项目团队成员
        MessageReceiverServiceI messageReceiverService = getMessageReceiverService();
        return messageReceiverService.getMessageReceivers(
            MessageConstants.MESSAGE_STATUS_CHANGE, projectId);
    }

    @Override
    public Map<String, Object> doSetVariables(JoinPoint joinPoint, Object returnValue) {
        Object[] argArray = joinPoint.getArgs();
        String projectId = (String)argArray[0];
        String businessType = (String)argArray[1];
        MessageReceiverServiceI messageReceiverService = getMessageReceiverService();
        Project project = projectService.getProjectEntity(projectId);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("projectId", project.getId());
        variables.put("projectName", project.getName() + "-" + project.getProjectNumber());
        String status = "";
        if (ProjectStatusConstants.PROJECT_STATUS_CHANGE_START.equals(businessType)) {
            status = ProjectStatusConstants.PROJECT_STATUS_CHANGE_START_CHI;
        }
        else if (ProjectStatusConstants.PROJECT_STATUS_CHANGE_PAUSE.equals(businessType)) {
            status = ProjectStatusConstants.PROJECT_STATUS_CHANGE_PAUSE_CHI;
        }
        else if (ProjectStatusConstants.PROJECT_STATUS_CHANGE_RESUME.equals(businessType)) {
            status = ProjectStatusConstants.PROJECT_STATUS_CHANGE_RESUME_CHI;
        }
        else if (ProjectStatusConstants.PROJECT_STATUS_CHANGE_CLOSE.equals(businessType)) {
            status = ProjectStatusConstants.PROJECT_STATUS_CHANGE_CLOSE_CHI;
        }
        variables.put("status", status);
        variables.put("appkeys", env.getProperty("server.runtime.wmsApplication"));
        return variables;
    }
    
    @Override
    public boolean setPushTaskQueueHandle() {
        // TODO Auto-generated method stub
        return false;
    }
}
