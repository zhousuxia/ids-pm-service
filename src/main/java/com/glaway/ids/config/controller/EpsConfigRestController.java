package com.glaway.ids.config.controller;

import io.swagger.annotations.*;
import com.glaway.foundation.common.util.JsonUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.config.dto.EpsConfigDto;
import com.glaway.ids.config.entity.EpsConfig;
import com.glaway.ids.config.service.EpsConfigServiceI;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.core.common.controller.BaseController;

import java.util.List;
import java.util.Map;

import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 项目分类
 * @date 2015-03-27 17:06:18
 */
@Api(tags = {"项目分类接口"})
@RestController
@RequestMapping("/feign/epsConfigRestController")
public class EpsConfigRestController extends BaseController {
    private static final OperationLog log = BaseLogFactory.getOperationLog(EpsConfigRestController.class);
    /**
     * message
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 项目分类接口服务
     */
    @Autowired
    private EpsConfigServiceI epsConfigService;

    /**
     * 根据id获取项目分类信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取项目分类信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目分类id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getEpsConfig")
    public String getEpsConfig(@RequestParam("id") String id) {
        String epsConfig = epsConfigService.getEpsConfig(id);
        return epsConfig;
    }

    /**
     * 增加项目分类
     *
     * @param epsConfig
     */
    @ApiOperation(value = "增加项目分类", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "addEpsConfig")
    public void addEpsConfig(@RequestBody EpsConfigDto epsConfig) {
        EpsConfig eps = new EpsConfig();
        Dto2Entity.copyProperties(epsConfig, eps);
        epsConfigService.add(eps);
    }

    /**
     * 查找项目分类树节点
     *
     * @param epsConfig
     * @return
     */
    @ApiOperation(value = "查找项目分类树节点", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "searchTreeNode")
    public String searchTreeNode(@RequestBody EpsConfigDto epsConfig) {
        EpsConfig eps = new EpsConfig();
        Dto2Entity.copyProperties(epsConfig, eps);
        String configList = epsConfigService.searchTreeNode(eps);
        return configList;
    }

    /**
     * 根据ID获得所有子节点
     *
     * @param mapList
     */
    @ApiOperation(value = "根据ID获得所有子节点", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getEpsParentList")
    public void getEpsParentList(@RequestBody Map<String, Object> mapList) {
        EpsConfig epsConfig = (EpsConfig) mapList.get("hitEps");
        List<EpsConfig> allList = (List<EpsConfig>) mapList.get("allEpsList");
        List<EpsConfig> parentList = (List<EpsConfig>) mapList.get("parentList");
        epsConfigService.getEpsParentList(epsConfig, allList, parentList);
    }

    /**
     * 项目分类中获取最大项目分类的位置序号
     *
     * @return
     */
    @ApiOperation(value = "项目分类中获取最大项目分类的位置序号", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getMaxEpsConfigPlace")
    public int getMaxEpsConfigPlace() {
        int maxPlace = epsConfigService.getMaxEpsConfigPlace();
        return maxPlace;
    }

    /**
     * 新增/修改项目分类
     *
     * @param epsConfig
     */
    @ApiOperation(value = "新增/修改项目分类", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveOrUpdateEpsConfig")
    public void saveOrUpdateEpsConfig(@RequestBody EpsConfig epsConfig) {
        epsConfigService.saveOrUpdateEpsConfig(epsConfig);
    }

    /**
     * 获取项目分类父节点
     *
     * @param epsConfig
     * @return
     */
    @ApiOperation(value = "获取项目分类父节点", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getParentNode")
    public String getParentNode(@RequestBody EpsConfigDto epsConfig) {
        EpsConfig eps = new EpsConfig();
        Dto2Entity.copyProperties(epsConfig, eps);
        String config = epsConfigService.getParentNode(eps);
        return config;
    }

    /**
     * 修改项目分类节点
     *
     * @param dto
     * @param nochange
     */
    @ApiOperation(value = "修改项目分类节点", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "nochange", value = "编号变更标识", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "modify")
    public void modify(@RequestBody EpsConfigDto dto, @RequestParam("nochange") boolean nochange) {
        EpsConfig epsConfig = new EpsConfig();
        Dto2Entity.copyProperties(dto, epsConfig);
        epsConfigService.modify(epsConfig, nochange);
    }

    /**
     * 根据条件查询项目分类
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "根据条件查询项目分类", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getEpsconfigList")
    public FeignJson getEpsconfigList(@RequestBody EpsConfigDto dto) {
        EpsConfig epsConfig = new EpsConfig();
        Dto2Entity.copyProperties(dto, epsConfig);
        String epsConfigListStr = epsConfigService.getEpsConfigList(epsConfig);
        FeignJson j = new FeignJson();
        j.setObj(epsConfigListStr);
        return j;
    }

    /**
     * 批量删除项目分类
     *
     * @param ids
     */
    @ApiOperation(value = "批量删除项目分类", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "项目分类ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doBatchDel")
    public void doBatchDel(@RequestParam("ids") String ids) {
        epsConfigService.doBatchDel(ids);
    }

    /**
     * 批量启用禁用项目分类
     *
     * @param ids
     * @param state
     */
    @ApiOperation(value = "批量启用禁用项目分类", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "项目分类ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "启用禁用状态", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doBatchStartOrStop")
    public void doBatchStartOrStop(@RequestParam("ids") String ids, @RequestParam("state") String state) {
        epsConfigService.doBatchStartOrStop(ids, state);
    }

    /**
     * 获取项目分类
     *
     * @return
     */
    @ApiOperation(value = "获取项目分类", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getList")
    public String getList() {
        String list = epsConfigService.getList();
        return list;
    }

    /**
     * 根据项目分类id获得项目分类名称路径名
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据项目分类id获得项目分类名称路径名", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目分类id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getEpsNamePathById")
    public FeignJson getEpsNamePathById(@RequestParam(value = "id", required = false) String id) {
        FeignJson j = new FeignJson();
        String realPath = epsConfigService.getEpsNamePathById(id);
        j.setObj(realPath);
        return j;
    }

    /**
     * 获取EPS项目分类树
     *
     * @return
     */
    @ApiOperation(value = "获取EPS项目分类树", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getEpsTreeNodes")
    public String getEpsTreeNodes() {
        String str = epsConfigService.getEpsTreeNodes();
        return str;
    }

    /**
     * 根据项目分类条件获取项目分类树
     *
     * @param epsConfig
     * @return
     */
    @ApiOperation(value = "根据项目分类条件获取项目分类树", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTreeNodes")
    public String getTreeNodes(@RequestBody EpsConfigDto epsConfig) {
        EpsConfig eps = new EpsConfig();
        Dto2Entity.copyProperties(epsConfig, eps);
        String str = epsConfigService.getTreeNodes(eps);
        return str;
    }

    /**
     * 批量删除项目分类判断其下有没有子节点
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除项目分类判断其下有没有子节点", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "项目分类ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doBatchDelIsHaveChildList")
    public FeignJson doBatchDelIsHaveChildList(@RequestParam(value = "ids", required = false) String ids) {
        return epsConfigService.doBatchDelIsHaveChildList(ids);
    }

    /**
     * 更新项目分类
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "更新项目分类", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doUpdate")
    public FeignJson doUpdate(@RequestBody EpsConfigDto dto) {
        EpsConfig epsConfig = new EpsConfig();
        Dto2Entity.copyProperties(dto, epsConfig);
        return epsConfigService.doUpdate(epsConfig);
    }
}
