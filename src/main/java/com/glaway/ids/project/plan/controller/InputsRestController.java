package com.glaway.ids.project.plan.controller;

import io.swagger.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.glaway.foundation.fdk.dev.common.FeignJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.system.lifecycle.service.LifeCycleStatusServiceI;
import com.glaway.ids.project.plan.dto.InputsDto;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.InputsServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjDocVo;
import com.glaway.ids.util.Dto2Entity;

/**
 * @author blcao
 * @version V1.0
 * @Title: Controller
 * @Description: 交付物
 * @date 2016-03-30 09:11:17
 */
@Api(tags = {"计划输入接口"})
@RestController
@RequestMapping("/feign/inputsRestController")
public class InputsRestController extends BaseController {
    /**
     * Logger for this class
     */
    // private static final Logger logger = Logger.getLogger(DeliverablesInfoController.class);
    private static final OperationLog log = BaseLogFactory.getOperationLog(InputsRestController.class);
    /**
     * 计划处理Service
     */
    @Autowired
    private PlanServiceI planService;
    /**
     * 文档处理Service
     */
    @Autowired
    private ProjLibServiceI projLibService;
    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProjRoleServiceI projRoleService;
    /**
     * 项目计划管理接口
     */
    @Autowired
    private LifeCycleStatusServiceI lifeCycleStatusService;
    @Autowired
    private InputsServiceI inputsService;

    @ApiOperation(value = "获取输入列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryNewInputsList")
    public List<InputsDto> queryNewInputsList(@RequestBody InputsDto inputs) {
        Inputs inp = new Inputs();
        Dto2Entity.copyProperties(inputs, inp);
        List<Inputs> list = inputsService.queryNewInputsList(inp);
        List<InputsDto> resList = JSON.parseArray(JSON.toJSONString(list), InputsDto.class);
        return resList;
    }

    @ApiOperation(value = "根据项目id获得项目库id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getLibIdByProjectId")
    public FeignJson getLibIdByProjectId(@RequestParam(value = "projectId", required = false) String projectId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String libId = projRoleService.getLibIdByProjectId(projectId);
            j.setObj(libId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ApiOperation(value = "获取项目库文档的文档名称与bizId的集合",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "libId", value = "项目库id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getRepFileNameAndBizIdMap")
    public Map<String, String> getRepFileNameAndBizIdMap(@RequestParam("libId") String libId) {
        Map<String, String> map = inputsService.getRepFileNameAndBizIdMap(libId);
        return map;
    }

    @ApiOperation(value = "获取项目库文档的文档路径与bizId的集合",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "libId", value = "项目库id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getRepFilePathAndBizIdMap")
    public Map<String, String> getRepFilePathAndBizIdMap(@RequestParam("libId") String libId) {
        Map<String, String> map = inputsService.getRepFilePathAndBizIdMap(libId);
        return map;
    }

    @ApiOperation(value = "获取项目库文档的文档id与bizId的集合",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "libId", value = "项目库id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getRepFileIdAndBizIdMap")
    public Map<String, String> getRepFileIdAndBizIdMap(@RequestParam("libId") String libId) {
        Map<String, String> map = inputsService.getRepFileIdAndBizIdMap(libId);
        return map;
    }

    @ApiOperation(value = "获取项目库信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDocRelationList")
    public List<ProjDocVo> getDocRelationList(@RequestBody PlanDto planDto, @RequestParam("userId") String userId) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        List<ProjDocVo> list = inputsService.getDocRelationList(plan, userId);
        return list;
    }

    @ApiOperation(value = "获取输入对象信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "输入id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getInputEntity")
    public InputsDto getInputEntity(@RequestParam("id") String id) {
        Inputs inputs = inputsService.getEntity(Inputs.class, id);
        InputsDto dto = new InputsDto();
        try {
            dto = (InputsDto) VDataUtil.getVDataByEntity(inputs, InputsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "删除输入",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteInputs")
    public void deleteInputs(@RequestBody InputsDto inputsDto) {
        Inputs inputs = new Inputs();
        Dto2Entity.copyProperties(inputsDto, inputs);
        inputsService.delete(inputs);
    }

    @ApiOperation(value = "查询输入详细信息列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryInputsDetailList")
    List<InputsDto> queryInputsDetailList(@RequestBody InputsDto dto) {
        List<InputsDto> dtoList = new ArrayList<InputsDto>();
        Inputs inputs = new Inputs();
        Dto2Entity.copyProperties(dto, inputs);
        List<Inputs> list = inputsService.queryInputsDetailList(inputs);
        try {
            dtoList = JSON.parseArray(JSON.toJSONString(list), InputsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    @ApiOperation(value = "根据planParentId条件检索交付物",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planParentId", value = "父计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryInputsDetailListForString")
    List<InputsDto> queryInputsDetailListForString(@RequestParam("planParentId") String planParentId) {
        List<InputsDto> dtoList = new ArrayList<InputsDto>();
        List<Inputs> list = inputsService.queryInputsDetailList(planParentId);
        try {
            dtoList = JSON.parseArray(JSON.toJSONString(list), InputsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    @ApiOperation(value = "根据inputs条件检索交付物",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryInputList")
    List<InputsDto> queryInputList(@RequestBody InputsDto inputs, @RequestParam("page") int page, @RequestParam("rows") int rows, @RequestParam("isPage") boolean isPage) {
        Inputs inp = new Inputs();
        Dto2Entity.copyProperties(inputs, inp);
        List<Inputs> list = inputsService.queryInputList(inp, page, rows, isPage);
        List<InputsDto> resList = JSON.parseArray(JSON.toJSONString(list), InputsDto.class);
        return resList;
    }

    @ApiOperation(value = "删除来源输出相关的输入",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteInputsByOriginDeliverables")
    void deleteInputsByOriginDeliverables(String originDeliverablesInfoId, String useObjectType) {
        inputsService.deleteInputsByOriginDeliverables(originDeliverablesInfoId, useObjectType);
    }

    @ApiOperation(value = "根据id删除输入",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "输入id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteInputsById")
    void deleteInputsById(@RequestParam("id") String id) {
        inputsService.deleteInputsById(id);
    }

    ;

    @ApiOperation(value = "更新输入",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "docId", value = "项目库文档bizId或本地上传文档对应的jackrabbit路径", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "docName", value = "项目库文档名称", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateInputsForDocInfoById")
    void updateInputsForDocInfoById(@RequestParam("id") String id, @RequestParam("docId") String docId, @RequestParam("docName") String docName) {
        inputsService.updateInputsForDocInfoById(id, docId, docName);
    }

    /**
     * 获取父节点下的所有外部输入的数据
     *
     * @param planParentId
     */
    @ApiOperation(value = "获取父节点下的所有外部输入的数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planParentId", value = "父计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryOutInputsDetailList")
    List<InputsDto> queryOutInputsDetailList(@RequestParam("planParentId") String planParentId) {
        List<InputsDto> dtoList = new ArrayList<InputsDto>();
        List<Inputs> list = inputsService.queryOutInputsDetailList(planParentId);
        try {
            dtoList = JSON.parseArray(JSON.toJSONString(list), InputsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    ;

    /**
     * 获取项目库信息(自动匹配使用)
     *
     * @author wqb
     */
    @ApiOperation(value = "获取项目库信息(自动匹配使用)",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deliverName", value = "交付项名称", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDocRelationListMatch")
    List<ProjDocVo> getDocRelationListMatch(@RequestBody PlanDto planDto, @RequestParam("userId") String userId, @RequestParam("deliverName") String deliverName) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        List<ProjDocVo> aDocVos = inputsService.getDocRelationListMatch(plan, userId, deliverName);
        return aDocVos;
    }

    ;

    /**
     * 更新输入
     *
     * @param params
     */
    @ApiOperation(value = "更新输入",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "/updateInputsByAddAndDel")
    void updateInputsByAddAndDel(@RequestBody Map<String, Object> params) {
        Object addInputListObj = params.get("addInputList");
        List<Inputs> addInputList = new ArrayList<Inputs>();
        if (!CommonUtil.isEmpty(addInputListObj)) {
            addInputList = JSON.parseObject(JSON.toJSONString(addInputListObj), new TypeReference<List<Inputs>>() {
            });
        }
        Object delInputListObj = params.get("delInputList");
        List<Inputs> delInputList = new ArrayList<Inputs>();
        if (!CommonUtil.isEmpty(delInputListObj)) {
            delInputList = JSON.parseObject(JSON.toJSONString(delInputListObj), new TypeReference<List<Inputs>>() {
            });
        }
        inputsService.updateInputsByAddAndDel(addInputList, delInputList);
    }

    @ApiOperation(value = "项目计划页面查看初始化时获取输入",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "listView")
    public FeignJson listView(@RequestBody Map<String, Object> map) {
        return inputsService.listView(map);
    }

    @ApiOperation(value = "获取输入列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getInputsRelationList")
    public FeignJson getInputsRelationList(@RequestBody PlanDto dto, @RequestParam("page") int page, @RequestParam("rows") int rows,
                                           @RequestParam(value = "projectId", required = false) String projectId, @RequestParam(value = "userId", required = false) String userId) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(dto, plan);
        return inputsService.getInputsRelationList(plan, page, rows, projectId, userId);
    }

    @ApiOperation(value = "根据计划模板id获取输入列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templId", value = "计划模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getInputsInfoByPlanTemplateId")
    public List<InputsDto> getInputsInfoByPlanTemplateId(@RequestParam(value = "templId", required = false) String templId) {
        List<Inputs> list = inputsService.getInputsInfoByPlanTemplateId(templId);
        List<InputsDto> dtoList = JSON.parseArray(JSON.toJSONString(list), InputsDto.class);
        return dtoList;
    }
}
