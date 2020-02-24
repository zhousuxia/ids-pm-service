/*
 * 文件名：FlowElementModifyNode.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：shitian
 * 修改时间：2018年9月7日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.common.pbmn.activity.entity;

import java.util.HashMap;
import java.util.Map;

import org.activiti.bpmn.model.FlowElement;

/**
 * 
 * 〈一句话功能简述〉保存工作流中需要修改的流程元素信息
 * 〈功能详细描述〉
 * @author shitian
 * @version 2018年9月7日
 * @see FlowElementModifyNode
 * @since
 */
public class FlowElementModifyNode {
    
    /**
     * 流程节点类型<br>e.g process,startEvent,userTask...
     */
    private String flowType;
    
    /**
     * 流程节点Id<br>
     */
    private String flowId;
    
    /**
     * 流程节点name<br>
     */
    private String flowName;
    
    /**
     * 流程节点需要修改的属性<br>
     */
    private Map<String, Object> properties = new HashMap<String, Object>();

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
        
}
