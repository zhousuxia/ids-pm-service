/*
 * 文件名：BpmnServiceI.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：shitian
 * 修改时间：2018年8月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.common.service;

import java.util.List;
import java.util.Map;

import com.glaway.foundation.activiti.core.dto.FlowDeployEntityDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.ids.config.vo.BpmnTaskVo;

/**
 * 
 * 〈Bpmn工作流Service〉
 * 〈功能详细描述〉
 * @author shitian
 * @version 2018年8月9日
 * @see BpmnServiceI
 * @since
 */
public interface BpmnServiceI extends CommonService {


    /**
     * 从Redis中查询工作流节点Vo
     * @param type 获取节点redis值的键
     * @param typeId 获取节点redis值的键
     * @return
     */
    List<BpmnTaskVo> getFromRedis(String type, String typeId);


    /**
     * 清空redis中所有任务节点
     * @param type 获取节点redis值的键
     * @param typeId 获取节点redis值的键
     */
    void clearBpmnTaskVoList(String type, String typeId);

    /**
     * 新增task节点到列表中
     * @param type 获取节点redis值的键
     * @param typeId 获取节点redis值的键
     * @param taskVoList 任务节点列表
     * @return
     */
    List<BpmnTaskVo> addListToRedis(String type, String typeId, List<BpmnTaskVo> taskVoList);


    /**
     * 检查节点名称是否重复
     * @param type 获取节点redis值的键
     * @param typeId 获取节点redis值的键
     * @param name 名称
     * @return
     */
    boolean isTaskNameRepeat(String type, String typeId, String name);


    /**
     * 新增task节点到列表中
     * @param type 获取节点redis值的键
     * @param typeId 获取节点redis值的键
     * @param task 任务节点
     * @return
     */
    List<BpmnTaskVo> addTaskVoToRedis(String type, String typeId, BpmnTaskVo task);


    /**
     * 通过id移动节点
     * @param type 获取节点redis值的键
     * @param typeId 获取节点redis值的键
     * @param ids 任务节点id
     * @param moveType 移动类型
     * @return
     */
    List<BpmnTaskVo> moveTaskVoById(String type, String typeId, String ids, String moveType);


    /**
     * 从Redis批量删除工作流节点Vo
     * @param type 获取节点redis值的键
     * @param typeId 获取节点redis值的键
     * @param ids 任务节点ids
     * @return
     */
    List<BpmnTaskVo> batchDeleteFromRedis(String type, String typeId, String ids);

    /**
     * 查询流程节点列表
     * @param originId  流程节点来源
     * @return
     * @see
     */
    List<BpmnTaskVo> queryBpmnTaskList(String originId);

    /**
     * 查询节点的最新版本
     * @param originId  流程节点来源
     * @return
     * @see
     */
    String queryBpmnTaskLastVersion(String originId);

    /**
     * 发布工作流程
     * @param type 获取节点redis值的键
     * @param typeId 获取节点redis值的键
     * @param ids 任务节点ids
     * @param processName 流程名称
     * @param currentUser 用户对象
     * @return
     */
    List<BpmnTaskVo> deployTaskFlow(String type, String typeId, String ids, String processName, String basePath,TSUserDto currentUser);

    /**
     * 构建工作流
     * @param typeId 获取节点redis值的键
     * @param processId 流程id
     * @param processName 流程名称
     * @param list 流程节点列表
     * @param currentUser 用户对象
     */
    void dynamicDeployBpmnModel(String typeId,String processId, String processName, List<BpmnTaskVo> list, TSUserDto currentUser,String basePath);


    /**
     * 增加业务对象流程关联信息
     * @param columns 业务对象流程关联信息列
     * @return
     * @see
     */
    void addObjectBusinessBpmnLink(Map<String, String> columns);

    /**
     * 增加流程节点列表到数据库
     * @param taskList 流程节点列表
     * @see
     */
    void addTaskList(List<BpmnTaskVo> taskList, String processId);


    /**
     * 部署新流程
     * @param entity  流程对象
     */
    void deployNewFlowDeployEntity(FlowDeployEntityDto entity);

}
