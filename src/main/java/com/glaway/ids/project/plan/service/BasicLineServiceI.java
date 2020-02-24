package com.glaway.ids.project.plan.service;

import com.alibaba.fastjson.JSONObject;
import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.BasicLineDto;
import com.glaway.ids.project.plan.dto.BasicLinePlanDto;
import com.glaway.ids.project.plan.entity.BasicLine;
import com.glaway.ids.project.plan.entity.BasicLinePlan;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by LHR on 2019/8/12.
 */
public interface BasicLineServiceI extends BusinessObjectServiceI<BasicLine> {

    /**
     * 获取基线的生命周期
     * @return
     */
    String getLifeCycleStatusList();

    /**
     * 获取基线对象信息
     * @param id 基线对象id
     * @return
     */
    BasicLine getBasicLineEntity(String id);

    /**
     * 获取基线中计划列表
     * @param basicLinePlan 基线对象
     * @param page 页码
     * @param rows 每页数量
     * @param isPage 是否分页
     * @return
     */
    List<BasicLinePlan> queryBasicLinePlanList(BasicLinePlan basicLinePlan, int page, int rows, boolean isPage);

    /**
     * 获取基线列表
     * @param conditionList 查询条件
     * @param projectId 项目id
     * @param userName 用户名
     * @return
     */
    PageList queryEntity(List<ConditionVO> conditionList, String projectId, String userName);

    /**
     * 将基线数据转为json格式
     * @param validList  基线计划信息集合
     * @return
     */
    List<JSONObject> changePlansToJSONObjects(List<BasicLinePlan> validList);


    /**
     * 获取基线计划信息
     * @param id 基线计划信息id
     * @return
     */
    BasicLinePlan getBasicLinePlanEntity(String id);


    /**
     * 保存基线
     * @param o 基线对象
     * @param ids 基线ids
     * @param basicLineName 基线名称
     * @param remark 基线备注
     * @param type 类型
     * @param projectId 项目id
     * @return
     */
    FeignJson saveBasicLine(BasicLine o, String ids, String basicLineName, String remark, String type, String projectId);

    /**
     * 删除基线
     * @param o 基线对象
     */
    void deleteBasicLine(BasicLine o);

    /**
     * 冻结基线
     * @param id  基线id
     * @return
     */
    FeignJson doFrozeBasicLine(String id);

    /**
     * 启用基线
     * @param id 基线id
     * @return
     */
    FeignJson doUseBasicLine(String id);

    /**
     * 启用基线
     * @param basicLineId 基线id
     * @param leader 室领导
     * @param deptLeader 部门领导
     * @param userId 用户id
     * @return
     */
    FeignJson startBasicLine(String basicLineId, String leader, String deptLeader,String userId);

    /**
     * 基线提交审批
     * @param basicLine 基线
     * @param basicLineId 基线id
     * @param taskId 任务id
     * @param basicLineName 基线名称
     * @param remark 备注
     * @return
     */
    FeignJson startBasicLineFlow(BasicLine basicLine,String basicLineId, String taskId,String basicLineName,String remark);

    /**
     * 基线列表查询
     * @param conditionList 查询条件
     * @param projectId 项目id
     * @param userName 用户名
     * @return
     */
    String searchDatagrid(List<ConditionVO> conditionList, String projectId, String userName);

    /**
     * 提交流程通过，更改基线状态为启用
     * @param basicLineId 基线id
     */
    void basicLineExcution(String basicLineId);
}
