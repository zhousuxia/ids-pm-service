package com.glaway.ids.statisticalAnalysis.controller;

import java.util.List;

import com.glaway.ids.statisticalAnalysis.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.statisticalAnalysis.service.StatisticalAnalysisServiceI;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import java.util.Map;

/**
 * @Description: 项目计划统计分析
 * @author: sunmeng
 * @ClassName: StatisticalAnalysisRestController
 * @Date: 2019/8/16-16:37
 * @since
 */
@Api(tags = {"项目计划统计分析接口"})
@RestController
@RequestMapping("/feign/statisticalAnalysisRestController")
public class StatisticalAnalysisRestController extends BaseController {
    @Autowired
    private StatisticalAnalysisServiceI statisticalAnalysisService;

    @ApiOperation(value = "获取项目看板数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @RequestMapping(value = "getProjectBoardVoList")
    public List<ProjectBoardVo> getProjectBoardVoList(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "page", required = false) int page,
                                                      @RequestParam(value = "rows", required = false) int rows, @RequestParam(value = "isPage", required = false) boolean isPage) {
        return statisticalAnalysisService.getProjectBoardVoList(userId, page, rows, isPage);
    }

    @ApiOperation(value = "获取项目看板数据数量",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @RequestMapping(value = "getProjectBoardVoListSize")
    public int getProjectBoardVoListSize(@RequestParam(value = "userId",required = false) String userId,@RequestParam(value = "page",required = false) int page,
                                         @RequestParam(value = "rows",required = false) int rows,@RequestParam(value = "isPage",required = false) boolean isPage){
        int size = statisticalAnalysisService.getProjectBoardVoListSize(userId, page, rows, isPage);
        return size;
    }

    @ApiOperation(value = "获取底层WBS计划相关统计的VO",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pid", value = "父id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年份", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "month", value = "月份", dataType = "String")
    })
    @RequestMapping(value = "getWBSCompleteRateVo")
    public FeignJson getWBSCompleteRateVo(@RequestParam(value = "pid", required = false) String pid, @RequestParam(value = "type", required = false) String type,
                                          @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {
        FeignJson j = new FeignJson();
        CompleteRateVo completeRateVo = statisticalAnalysisService.getWBSCompleteRateVo(pid, type, year, month);
        j.setObj(completeRateVo);
        return j;
    }

    @ApiOperation(value = "获取里程碑列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @RequestMapping(value = "getMilestoneVoList")
    public List<ProjectAnalysisVo> getMilestoneVoList(@RequestParam(value = "projectId", required = false) String projectId) {
        return statisticalAnalysisService.getMilestoneVoList(projectId);
    }

    @ApiOperation(value = "获取计划达成率vo",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pid", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年份", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "month", value = "月份", dataType = "String")
    })
    @RequestMapping(value = "getCompleteRateVo")
    public FeignJson getCompleteRateVo(@RequestParam(value = "pid", required = false) String pid, @RequestParam(value = "type", required = false) String type,
                                       @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {
        FeignJson j = new FeignJson();
        CompleteRateVo completeRateVo = statisticalAnalysisService.getCompleteRateVo(pid, type, year, month);
        j.setObj(completeRateVo);
        return j;
    }

    @ApiOperation(value = "获取月度达成率列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年份", dataType = "String")
    })
    @RequestMapping(value = "getMonthRateVoList")
    public FeignJson getMonthRateVoList(@RequestParam(value = "projectId", required = false) String projectId,
                                        @RequestParam(value = "year", required = false) String year) {
        FeignJson fj = new FeignJson();
        List<MonthRateVo> list = statisticalAnalysisService.getMonthRateVoList(projectId, year);
        fj.setObj(list);
        return fj;
    }

    @ApiOperation(value = " 获取已延期任务列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pid", value = "父id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @RequestMapping(value = "getDelayTaskVoList")
    public List<DelayTaskVo> getDelayTaskVoList(@RequestParam(value = "pid", required = false) String pid, @RequestParam(value = "page", required = false) int page,
                                                @RequestParam(value = "rows", required = false) int rows, @RequestParam(value = "isPage", required = false) boolean isPage) {
        return statisticalAnalysisService.getDelayTaskVoList(pid, page, rows, isPage);
    }

    @ApiOperation(value = "获取年份下拉列表",httpMethod = "GET")
    @RequestMapping(value = "getYearCombobox")
    public FeignJson getYearCombobox() {
        String jsonStr = statisticalAnalysisService.getYearCombobox();
        FeignJson j = new FeignJson();
        j.setObj(jsonStr);
        return j;
    }

    @ApiOperation(value = "获取项目下拉列表",httpMethod = "GET")
    @ApiImplicitParams(@ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"))
    @RequestMapping(value = "getProjectCombobox")
    public FeignJson getProjectCombobox(@RequestParam(value = "orgId",required = false) String orgId) {
        String jsonStr = statisticalAnalysisService.getProjectCombobox(orgId);
        FeignJson j = new FeignJson();
        j.setObj(jsonStr);
        return j;
    }

    @ApiOperation(value = "获取计划变更分析报表数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "condition", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "conditionForManager", value = "查询人员", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", dataType = "String")
    })
    @RequestMapping(value = "getPlanChangeHighchartsInfo")
    public FeignJson getPlanChangeHighchartsInfo(@RequestParam(value = "condition",required = false) String condition,
                                          @RequestParam(value = "conditionForManager",required = false) String conditionForManager,
                                          @RequestParam(value = "type",required = false) String type){
        FeignJson feignJson = new FeignJson();
        JSONArray jList = statisticalAnalysisService.getPlanChangeHighchartsInfo(condition,conditionForManager,type);
        if(jList.size()>0) {
                feignJson.setObj(jList.toString());
            }
        return feignJson;
    }

    @ApiOperation(value = "获取计划变更分析报表数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "condition", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "conditionForManager", value = "查询人员", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int")
    })
    @RequestMapping(value = "conditionSearch")
    public List<planChangeAnalysisVo> conditionSearch(@RequestParam(value = "condition",required = false) String condition,
                                                      @RequestParam(value = "conditionForManager",required = false) String conditionForManager,
                                                      @RequestParam(value = "page", required = false) int page,
                                                      @RequestParam(value = "rows", required = false) int rows){
        List<planChangeAnalysisVo> jList = statisticalAnalysisService.conditionSearch(condition,conditionForManager,page,rows);
        return jList;
    }

    @ApiOperation(value = "获取首页部件项目看板报表数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planLevel", value = "计划等级", dataType = "String")
    })
    @RequestMapping(value = "getProjectBoardReportData")
    public List<ProjectBoardReportDataVo> getProjectBoardReportData(@RequestParam(value = "projectId",required = false) String projectId,@RequestParam(value = "planLevel",required = false) String planLevel){
        List<ProjectBoardReportDataVo> list = statisticalAnalysisService.getProjectBoardReportData(projectId,planLevel);
        return list;
    }


    @ApiOperation(value = "人员负载分析列表数据加载",httpMethod = "GET")
    @RequestMapping(value = "searchlaborLoadList")
    public FeignJson searchlaborLoadList(@RequestBody Map<String, Object> params) {
        FeignJson j = statisticalAnalysisService.searchlaborLoadList(params);
        return j;
    }

    @ApiOperation(value = "人员负载分析报表数据获取",httpMethod = "GET")
    @RequestMapping(value = "getLaborLoadListCharts")
    public FeignJson getLaborLoadListCharts(@RequestBody Map<String, Object> params) {
        FeignJson j = statisticalAnalysisService.getLaborLoadListCharts(params);
        return j;
    }
}
