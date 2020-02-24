/*
 * 文件名：ChangeFlowTaskInfoVO.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：blcao
 * 修改时间：2016年7月28日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.plan.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wqb
 */
public class ChangeFlowTaskInfoVO {

    /**
     * 流程变更-节点信息
     */
    private List<FlowTaskDto> changeFlowTaskList = new ArrayList<FlowTaskDto>();

    /**
     * 流程变更-前后置关系信息
     */
    private List<FlowTaskPreposeDto> flowTaskPreposeList = new ArrayList<FlowTaskPreposeDto>();

    /**
     * 流程变更-前后指向关系信息
     */
    private List<ChangeFlowTaskCellConnectDto> changeFlowTaskConnectList = new ArrayList<ChangeFlowTaskCellConnectDto>();

    public List<FlowTaskDto> getChangeFlowTaskList() {
        return changeFlowTaskList;
    }

    public void setChangeFlowTaskList(List<FlowTaskDto> changeFlowTaskList) {
        this.changeFlowTaskList = changeFlowTaskList;
    }

    public List<FlowTaskPreposeDto> getFlowTaskPreposeList() {
        return flowTaskPreposeList;
    }

    public void setFlowTaskPreposeList(List<FlowTaskPreposeDto> flowTaskPreposeList) {
        this.flowTaskPreposeList = flowTaskPreposeList;
    }

    public List<ChangeFlowTaskCellConnectDto> getChangeFlowTaskConnectList() {
        return changeFlowTaskConnectList;
    }

    public void setChangeFlowTaskConnectList(List<ChangeFlowTaskCellConnectDto> changeFlowTaskConnectList) {
        this.changeFlowTaskConnectList = changeFlowTaskConnectList;
    }
}
