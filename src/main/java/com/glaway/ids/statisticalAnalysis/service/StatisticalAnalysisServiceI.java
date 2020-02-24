package com.glaway.ids.statisticalAnalysis.service;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.statisticalAnalysis.vo.*;
import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * @Description: 统计分析接口
 * @author: sunmeng
 * @ClassName: StatisticalAnalysisServiceI
 * @Date: 2019/8/16-17:14
 * @since
 */
public interface StatisticalAnalysisServiceI extends CommonService {

    /**
     * 获取年份下拉列表
     * @param uid 用户Id
     * @param page 页数
     * @param rows 行数
     * @param isPage 是否分页
     * @return 
     * @see
     */
    List<ProjectBoardVo> getProjectBoardVoList(String uid, int page, int rows, boolean isPage);

    /**
     * 获取项目看板数据数量
     * @param uid 用户Id
     * @param page 页数
     * @param rows 行数
     * @param isPage 是否分页
     * @return
     * @see
     */
    int getProjectBoardVoListSize(String uid, int page, int rows, boolean isPage);

    /**
     * 获取里程碑voList
     * @param pid 项目Id
     * @return 
     * @see
     */
    List<ProjectAnalysisVo> getMilestoneVoList(String pid);

    /**
     * 获取关键计划等级
     *
     * @return
     * @see
     */
    String getKeyPlanLevel();

    /**
     * 获取计划达成率vo
     * @param pid  项目Id
     * @param type 类型
     * @param year 年
     * @param month 月
     * @return 
     * @see
     */

    CompleteRateVo getCompleteRateVo(String pid, String type, String year, String month);

    /**
     * 根据年月日  获取底层WBS计划相关统计的VO
     * @param pid  项目Id
     * @param type 类型
     * @param year 年
     * @param month 月
     * @return
     */
    CompleteRateVo getWBSCompleteRateVo(String pid, String type, String year, String month);

    /**
     *  获取月度达成率List
     * @param pid  项目Id
     * @param year 年
     * @return 
     * @see
     */
    List<MonthRateVo> getMonthRateVoList(String pid, String year);

    JSONArray getPlanChangeHighchartsInfo(String condition, String conditionForManager, String type);

    /**
     * 获取已延期任务List
     * @param pid 项目Id
     * @param page 页数
     * @param rows 行数
     * @param isPage 是否分页
     * @return 
     * @see
     */
    List<DelayTaskVo> getDelayTaskVoList(String pid, int page, int rows, boolean isPage);

    /**
     * 获取年份下拉列表
     *
     * @return
     * @see
     */
    String getYearCombobox();

    /**
     * 获取项目下拉列表
     *
     * @return
     * @see
     */
    String getProjectCombobox(String orgId);

    /**
     * 获取计划变更分析报表数据
     * @param condition
     * @param conditionForManager
     * @return
     */
    List<planChangeAnalysisVo> conditionSearch(String condition, String conditionForManager,int page, int rows);

    /**
     * 获取首页部件项目看板报表数据
     * @param projectId 项目id
     * @param planLevel 计划等级
     * @return
     */
    List<ProjectBoardReportDataVo> getProjectBoardReportData(String projectId,String planLevel);

    /**
     * 人员负载分析列表数据加载
     * @param params
     * @return
     */
    FeignJson searchlaborLoadList(Map<String, Object> params);

    /**
     * 人员负载分析报表数据获取
     * @param params
     * @return
     */
    FeignJson getLaborLoadListCharts(Map<String, Object> params);
}
