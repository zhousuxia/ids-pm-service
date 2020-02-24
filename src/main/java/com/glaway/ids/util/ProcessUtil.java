/*
 * 文件名：ProcessUtil.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：wangyangzan
 * 修改时间：2015年4月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.util;


import java.util.List;

import com.glaway.foundation.common.dto.TSUserDto;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;


/**
 * 工作流相关工具类
 * 
 * @author wangyangzan
 * @version 2015年4月11日
 * @see ProcessUtil
 * @since
 */
public class ProcessUtil {

    /**
     * 获取流程当前节点状态
     * 
     * @param procInstId
     * @return
     * @see
     */
    public static String getProcessStatus(List<Task> nextTasks) {
        String status = "";
        if (nextTasks != null && !nextTasks.isEmpty()) {
            status = nextTasks.get(0).getName();
        }
        else {
            status = "已完成";
        }
        return status;
    }

    /**
     * 获取流程实例的标题
     * 
     * @param objectName
     *            调用流程的实际对象名称 (比如启动项目的名称，下达计划的名称)
     * @param processName
     *            调用的流程名称
     * @param procInstId
     *            流程的实例ID
     * @return
     * @see
     */
    public static String getProcessTitle(String objectName, String processName, String procInstId) {
        if (StringUtils.isNotEmpty(objectName)) {
            return objectName + "-" + processName + "：" + procInstId;
        }
        else {
            return processName + "：" + procInstId;
        }
    }

    /**
     * 组装用户信息
     * @param user
     * @return
     */
    public static String getFormatUser(TSUserDto user) {
        if (StringUtils.isNotBlank(user.getUserName()) && StringUtils.isNotBlank(user.getRealName())) {
            String code = user.getUserName();
            String name = user.getRealName();
            return name + "-" + code;
        } else {
            return "";
        }
    }

}
