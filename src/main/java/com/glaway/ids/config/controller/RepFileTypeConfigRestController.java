package com.glaway.ids.config.controller;

import com.glaway.ids.common.pbmn.activity.dto.BpmnTaskDto;
import com.glaway.ids.common.pbmn.activity.entity.BpmnTask;
import com.glaway.ids.util.CodeUtils;
import io.swagger.annotations.*;
import com.glaway.foundation.businessobject.attribute.dto.EntityAttributeAdditionalAttributeDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.POIExcelUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.system.serial.dto.SerialNumberGeneratorInfoDto;
import com.glaway.foundation.system.serial.entity.SerialNumberGeneratorInfo;
import com.glaway.ids.common.service.BpmnServiceI;
import com.glaway.ids.config.service.RepFileTypeConfigServiceI;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileTypeDto;
import com.glaway.ids.config.vo.BpmnTaskVo;
import com.glaway.ids.config.vo.RepFileTypeConfigVo;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.RepFileTypeConfigConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 文件名：RepFileTypeConfigController.java
 * 版权：Copyright by www.glaway.com
 * 描述：文档类型设置
 * 修改人：zhousuxia
 * 修改时间：2018年7月26日
 * 跟踪单号：queryBpmnTaskList
 * 修改单号：
 * 修改内容：
 */
@Api(tags = {"文档类型设置接口"})
@RestController
@RequestMapping(value = "/feign/repFileTypeConfigRestController")
public class RepFileTypeConfigRestController extends BaseController {
    private static final OperationLog log = BaseLogFactory.getOperationLog(RepFileTypeConfigRestController.class);
    @Autowired
    private RepFileTypeConfigServiceI repFileTypeConfigService;
    @Autowired
    private BpmnServiceI bpmnService;

    /**
     * @param repFileTypeConfigParentId
     * @param fileTypeCode
     * @param fileTypeName
     * @param entrance
     * @param docTypeId
     * @return
     */
    @ApiOperation(value = "获取文档类型设置数据列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "", value = "父文件类型id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fileTypeCode", value = "文件类型编号", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fileTypeName", value = "文件类型名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "docTypeId", value = "文件类型id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getRepFileTypeConfigList")
    public List<RepFileTypeDto> getRepFileTypeConfigList(@RequestParam(value = "repFileTypeConfigParentId", required = false) String repFileTypeConfigParentId, @RequestParam(value = "fileTypeCode", required = false) String fileTypeCode,
                                                         @RequestParam(value = "fileTypeName", required = false) String fileTypeName, @RequestParam(value = "entrance", required = false) String entrance, @RequestParam(value = "docTypeId", required = false) String docTypeId) {
        List<RepFileTypeDto> list = repFileTypeConfigService.getRepFileTypeConfigList(repFileTypeConfigParentId, fileTypeCode, fileTypeName, entrance, docTypeId);
        return list;
    }

    /**
     * @param parentId
     * @param repFileTypeId
     * @param fileTypeCode
     * @return
     */
    @ApiOperation(value = "判断文档类型编号是否重复", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "父文件类型id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "repFileTypeId", value = "文件类型id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fileTypeCode", value = "文件类型编号", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkFileTypeCodeBeforeSave")
    public Boolean checkFileTypeCodeBeforeSave(@RequestParam("parentId") String parentId, @RequestParam("repFileTypeId") String repFileTypeId,
                                               @RequestParam("fileTypeCode") String fileTypeCode) {
        return repFileTypeConfigService.checkFileTypeCodeBeforeSave(parentId, repFileTypeId, fileTypeCode);
    }

    /**
     * @param repFileTypeId
     * @param fileTypeCode
     * @param fileTypeName
     * @param generatorInfoId
     * @param description
     * @param userId
     * @return
     */
    @ApiOperation(value = "保存文档类型设置", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "repFileTypeId", value = "文档类型id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fileTypeCode", value = "文档类型编号", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fileTypeName", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "generatorInfoId", value = "编号规则id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "文档类型描述", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveRepFileTypeConfig")
    public FeignJson saveRepFileTypeConfig(@RequestParam("repFileTypeId") String repFileTypeId, @RequestParam("fileTypeCode") String fileTypeCode,
                                           @RequestParam("fileTypeName") String fileTypeName, @RequestParam("generatorInfoId") String generatorInfoId,
                                           @RequestParam("description") String description, @RequestParam("userId") String userId) {
        return repFileTypeConfigService.saveRepFileTypeConfig(repFileTypeId, fileTypeCode, fileTypeName, generatorInfoId, description, userId);
    }

    /**
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除文档类型设置", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "文档类型设置ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteRepFileTypeConfig")
    public FeignJson deleteRepFileTypeConfig(@RequestParam("ids") String ids) {
        FeignJson j = new FeignJson();
        try {
            repFileTypeConfigService.deleteRepFileTypeConfig(ids);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * @param type
     * @param ids
     * @return
     */
    @ApiOperation(value = "启用/禁用文档类型设置", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "启用/禁用操作", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "ids", value = "文档类型设置ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "changeRepFileTypeStatus")
    public FeignJson changeRepFileTypeStatus(@RequestParam("type") String type, @RequestParam("ids") String ids) {
        FeignJson j = new FeignJson();
        try {
            repFileTypeConfigService.changeRepFileTypeStatus(type, ids);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "文档类型导出Excel", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doExportXls")
    public void doExportXls(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileTypeCode = request.getParameter("fileTypeCode");
            String fileTypeName = request.getParameter("fileTypeName");
            List<RepFileTypeConfigVo> list = new ArrayList<RepFileTypeConfigVo>();
            List<RepFileTypeDto> paramslist = repFileTypeConfigService.getRepFileTypeConfigList(RepFileTypeConfigConstants.REPFILETYPECONFIG_PARENTID, fileTypeCode, fileTypeName, null, null);
            Map<String, String> idAttrMap = new HashMap<String, String>();
            Map<String, SerialNumberGeneratorInfo> idRuleDescMap = new HashMap<String, SerialNumberGeneratorInfo>();
            Map<String, String> idRemarkMap = new HashMap<String, String>();
            repFileTypeConfigService.getCustomAttrMap(idAttrMap, RepFileTypeConfigConstants.ENTITY_URI);
            repFileTypeConfigService.getRuleAndRemarkMap(idRuleDescMap, idRemarkMap);
            //最多的审批环节数,默认为0
            int maxApprove = 0;
            if (!CommonUtil.isEmpty(paramslist)) {
                for (RepFileTypeDto filetype : paramslist) {
                    RepFileTypeConfigVo vo = new RepFileTypeConfigVo();
                    vo.setCode(filetype.getFileTypeCode());
                    vo.setName(filetype.getFileTypeName());
                    if (!CommonUtil.isEmpty(filetype.getGenerateRuleId())) {
                        vo.setGenerateRuleName(idRuleDescMap.get(filetype.getGenerateRuleId()).getName());
                        vo.setGenerateRuleDesc(idRuleDescMap.get(filetype.getGenerateRuleId()).getDescription());
                    }
                    //审批流程
                    List<BpmnTaskVo> taskList = bpmnService.getFromRedis(BpmnConstants.TASKVO_REPFILETYPE, filetype.getId());
                    if (!CommonUtil.isEmpty(taskList)) {
                        for (BpmnTaskVo bt : taskList) {
                            String approveType = bt.getApproveType();
                            if ("singleSign".equals(approveType)) {
                                bt.setApproveTypeName("单人审批");
                            } else if ("vieSign".equals(approveType)) {
                                bt.setApproveTypeName("竞争审批");
                            } else {
                                bt.setApproveTypeName("会签模式");
                            }
                        }
                    }
                    if (!CommonUtil.isEmpty(taskList) && taskList.size() > maxApprove) {
                        maxApprove = taskList.size();
                    }
                    vo.setList(taskList);
                    vo.setStatus(filetype.getStatus());
                    vo.setCustomAttr(idAttrMap.get(filetype.getId()));
                    vo.setRemark(idRemarkMap.get(filetype.getId()));
                    list.add(vo);
                }
            }
            String excelName = POIExcelUtil.createExcelName("true", RepFileTypeConfigConstants.EXPORT_TITLE, null);
            HSSFWorkbook workbook = repFileTypeConfigService.export(list, maxApprove);
            POIExcelUtil.responseReportWithName(response, workbook, excelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type
     * @param typeId
     */
    @ApiOperation(value = "清空redis中所有流程节点", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "获取节点redis值的键", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "clearBpmnTaskVoList")
    public void clearBpmnTaskVoList(@RequestParam("type") String type, @RequestParam("typeId") String typeId) {
        bpmnService.clearBpmnTaskVoList(type, typeId);
    }

    /**
     * @param originId
     * @return
     */
    @ApiOperation(value = "查询流程节点列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "originId", value = "流程节点来源id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryBpmnTaskList")
    public List<BpmnTaskVo> queryBpmnTaskList(@RequestParam("originId") String originId) {
        List<BpmnTaskVo> list = bpmnService.queryBpmnTaskList(originId);
        return list;
    }

    /**
     * @param type
     * @param typeId
     * @param taskVoList
     * @return
     */
    @ApiOperation(value = "添加流程节点到redis中", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "获取节点redis值的键", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "addListToRedis")
    public List<BpmnTaskVo> addListToRedis(@RequestParam("type") String type, @RequestParam("typeId") String typeId,
                                           @RequestBody List<BpmnTaskVo> taskVoList) {
        List<BpmnTaskVo> list = bpmnService.addListToRedis(type, typeId, taskVoList);
        return list;
    }

    /**
     * @param type
     * @param typeId
     * @param name
     * @return
     */
    @ApiOperation(value = "检查节点名称是否重复", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "节点名称", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "isTaskNameRepeat")
    public boolean isTaskNameRepeat(@RequestParam("type") String type, @RequestParam("typeId") String typeId, @RequestParam("name") String name) {
        boolean flag = bpmnService.isTaskNameRepeat(type, typeId, name);
        return flag;
    }

    /**
     * @param type
     * @param typeId
     * @param task
     * @return
     */
    @ApiOperation(value = "添加工作流节点到redis中", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "获取节点redis值的键", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "addTaskVoToRedis")
    public List<BpmnTaskVo> addTaskVoToRedis(@RequestParam("type") String type, @RequestParam("typeId") String typeId, @RequestBody BpmnTaskVo task) {
        List<BpmnTaskVo> list = bpmnService.addTaskVoToRedis(type, typeId, task);
        return list;
    }

    /**
     * @param type
     * @param typeId
     * @param ids
     * @param moveType
     * @return
     */
    @ApiOperation(value = "移动redis中的工作流节点顺序", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "ids", value = "流程节点ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "moveType", value = "移动类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "moveTaskVoById")
    public List<BpmnTaskVo> moveTaskVoById(@RequestParam("type") String type, @RequestParam("typeId") String typeId,
                                           @RequestParam("ids") String ids, @RequestParam("moveType") String moveType) {
        List<BpmnTaskVo> list = bpmnService.moveTaskVoById(type, typeId, ids, moveType);
        return list;
    }

    /**
     * @param type
     * @param typeId
     * @param ids
     * @return
     */
    @ApiOperation(value = "从Redis批量删除工作流节点Vo", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "流程节点id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "batchDeleteFromRedis")
    public List<BpmnTaskVo> batchDeleteFromRedis(String type, String typeId, String ids) {
        List<BpmnTaskVo> list = bpmnService.batchDeleteFromRedis(type, typeId, ids);
        return list;
    }

    /**
     * @param type
     * @param id
     * @return
     */
    @ApiOperation(value = "从redis中获取流程节点信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "获取节点redis值的键", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getFromRedis")
    public List<BpmnTaskVo> getFromRedis(@RequestParam("type") String type, @RequestParam("id") String id) {
        List<BpmnTaskVo> list = bpmnService.getFromRedis(type, id);
        return list;
    }

    /**
     * @param oldId
     * @param entityAttributeAdditionalAttributeList
     * @return
     */
    @ApiOperation(value = "文档类型软属性保存", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "oldId", value = "软属性id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "editEntityAttributeAdditionalAttribute")
    public FeignJson editEntityAttributeAdditionalAttribute(@RequestParam("oldId") String oldId,
                                                            @RequestBody List<EntityAttributeAdditionalAttributeDto> entityAttributeAdditionalAttributeList) {
        return repFileTypeConfigService.editEntityAttributeAdditionalAttribute(oldId, entityAttributeAdditionalAttributeList);
    }

    /**
     * @param type
     * @param typeId
     * @param ids
     * @param processName
     * @param currentUser
     * @return
     */
    @ApiOperation(value = "发布工作流程", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "获取节点redis值的键", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "流程来源Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "ids", value = "流程节点ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "processName", value = "工作流名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "basePath", value = "工作流路径", dataType = "String"),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deployTaskFlow")
    public List<BpmnTaskVo> deployTaskFlow(@RequestParam("type") String type, @RequestParam("typeId") String typeId, @RequestParam("ids") String ids,
                                           @RequestParam("processName") String processName, @RequestParam("basePath") String basePath, @RequestBody TSUserDto currentUser) {
        return bpmnService.deployTaskFlow(type, typeId, ids, processName, basePath, currentUser);
    }

    /**
     * 根据entityUri获取软属性Map
     *
     * @param idAttrMap
     * @param entityUri 软属性entityUri
     * @return
     */
    @ApiOperation(value = "根据entityUri获取软属性Map", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "entityUri", value = "软属性entityUri", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getCustomAttrMap")
    public Map<String, String> getCustomAttrMap(@RequestBody Map<String, String> idAttrMap, @RequestParam("entityUri") String entityUri) {
        Map<String, String> mapList = repFileTypeConfigService.getCustomAttrMap(idAttrMap, entityUri);
        return mapList;
    }

    /**
     * 获取流程节点信息
     *
     * @param originId 流程来源Id
     * @return
     */
    @ApiOperation(value = "获取流程节点信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "originId", value = "流程来源Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getBpmnTaskList")
    public List<BpmnTaskDto> getBpmnTaskList(@RequestParam(value = "originId", required = false) String originId) {
        List<BpmnTask> bpmnList = repFileTypeConfigService.getBpmnTaskList(originId);
        List<BpmnTaskDto> resList = CodeUtils.JsonListToList(bpmnList, BpmnTaskDto.class);
        return resList;
    }
}
