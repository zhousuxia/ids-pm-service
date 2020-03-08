package com.glaway.ids.project.projecttemplate.controller;

import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.project.plantemplate.dto.PlanTemplateDto;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.project.menu.dto.ProjTemplateMenuDto;
import com.glaway.ids.project.menu.dto.ProjectDto;
import com.glaway.ids.project.menu.entity.ProjTemplateMenu;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projecttemplate.dto.ProjTemplateDto;
import com.glaway.ids.project.projecttemplate.dto.ProjTmpLibAuthLibTmpLinkDto;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.project.projecttemplate.entity.ProjTmpLibAuthLibTmpLink;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateLibServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangshen
 * @version V1.0
 * @Title: Controller
 * @Description: 项目模板Controller
 * @date 2015-03-23 15:59:25
 */
@Api(tags = {"项目模板接口"})
@RestController
@RequestMapping("/feign/projTemplateRestController")
public class ProjTemplateRestController extends BaseController {
    /**
     * Logger for this class
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(ProjTemplateRestController.class);
    @Autowired
    private ProjTemplateServiceI projTemplateService;
    @Autowired
    private FeignUserService userService;
    @Autowired
    private ProjLibServiceI projLibService;
    @Autowired
    private ProjTemplateLibServiceI projTemplateLibService;
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

    @ApiOperation(value = "获取项目模板信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目模板id", dataType = "String")
    })
    @RequestMapping(value = "getProjTemplateEntity")
    public ProjTemplateDto getProjTemplateEntity(@RequestParam("id") String id) {
        ProjTemplate projTemplate = projTemplateService.getProjEntity(id);
        ProjTemplateDto dto = new ProjTemplateDto();
        try {
            if (!CommonUtil.isEmpty(projTemplate)) {
                Dto2Entity.copyProperties(projTemplate, dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "删除项目模板",httpMethod = "POST")
    @RequestMapping(value = "deleteTemplate")
    public FeignJson deleteTemplate(@RequestBody List<ProjTemplateDto> projTemplateDtos) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            List<ProjTemplate> projTemplateList = JSON.parseArray(JSON.toJSONString(projTemplateDtos), ProjTemplate.class);
            projTemplateService.deleteTemplate(projTemplateList);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "是否是项目模板管理员",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @RequestMapping(value = "isPTOM")
    public boolean isPTOM(@RequestParam("userId") String userId) {
        boolean flag = true;
        flag = projTemplateService.isPTOM(userId);
        return flag;
    }

    @ApiOperation(value = "启用禁用项目模板",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "启用/禁用类型", dataType = "String")
    })
    @RequestMapping(value = "openOrClose")
    public FeignJson openOrClose(@RequestBody ProjTemplateDto templateDto, @RequestParam("type") String type) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ProjTemplate projTemplate = new ProjTemplate();
            Dto2Entity.copyProperties(templateDto, projTemplate);
            projTemplateService.openOrClose(projTemplate, type);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取项目模板列表信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @RequestMapping(value = "queryEntity")
    public PageList queryEntity(@RequestBody List<ConditionVO> conditionList, @RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "orgId", required = false) String orgId) {
        PageList pageList = projTemplateService.queryEntity(conditionList, userName, orgId);
        return pageList;
    }

    @ApiOperation(value = "获取项目模板生命周期信息",httpMethod = "GET")
    @RequestMapping(value = "getLifeCycleStatusList")
    public FeignJson getLifeCycleStatusList() {
        FeignJson fj = new FeignJson();
        String lifeCycleStr = projTemplateService.getLifeCycleStatusList();
        fj.setObj(lifeCycleStr);
        return fj;
    }

    @ApiOperation(value = "保存项目模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "method", value = "方法", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @RequestMapping(value = "doSaveNewTemplate")
    public FeignJson doSaveNewTemplate(@RequestParam(value = "templateId", required = false) String templateId, @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "remark", required = false) String remark, @RequestParam(value = "method", required = false) String method,
                                       @RequestBody Map<String,Object> map, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        message = I18nUtil.getValue("com.glaway.ids.pm.project.projecttemplate.saveSuccess");
        try {
            ObjectMapper mapper = new ObjectMapper();
            TSUserDto userDto = mapper.convertValue(map.get("currentUser"),TSUserDto.class);
            List<TSRoleDto> roleList = mapper.convertValue(map.get("projTemplateRoleList"),new TypeReference<List<TSRoleDto>>(){});
            if (!isTemplateNameRepeat(name, templateId)) {
                if (StringUtils.isNotBlank(templateId)) {// 修改模板
                    ProjTemplate template2 = projTemplateService.getProjEntity(templateId);
                    ProjTemplate template = projTemplateService.getProjTemplateByBizId(template2.getBizId());
                    String oldName = template.getProjTmplName();
                    String oldRemark = template.getRemark();
                    if (StringUtils.isNotBlank(name)) {
                        template.setProjTmplName(name);
                    }
                    if (StringUtils.isNotBlank(remark)) {
                        template.setRemark(remark);
                    }
                    ProjTemplate vo = projTemplateService.updateProjTemplate(template, method, oldName, oldRemark, userDto, orgId,roleList);
                    ProjTemplate t = new ProjTemplate();
                    t.setProjTmplName(name);
                    t.setRemark(remark);
                    if (!CommonUtil.isEmpty(vo)) {
                        t.setPersientId(vo.getId());
                        t.setBizCurrent(vo.getBizCurrent());
                    } else {
                        t.setPersientId(templateId);
                    }
                    j.setObj(t);
                    j.setSuccess(true);
                } else {
                    if (StringUtils.isNotBlank(name)) {// 新增模板
                        ProjTemplate template = new ProjTemplate();
                        template.setProjTmplName(name);
                        template.setRemark(remark);
                        templateId = projTemplateService.SaveProjTemplate(template, userDto, orgId);
                        ProjTemplate t = new ProjTemplate();
                        t.setProjTmplName(name);
                        t.setRemark(remark);
                        t.setPersientId(templateId);
                        j.setObj(t);
                        j.setSuccess(true);
                        log.info(message);
                    } else {
                        message = I18nUtil.getValue("com.glaway.ids.pm.project.projecttemplate.saveFail");
                        log.error(message);
                    }
                }
            } else {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projecttemplate.nameRepeat");
                j.setSuccess(false);
                log.error(message);
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            j.setMsg(message);
            return j;
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isTemplateNameRepeat(String name, String tmpId) {
        try {
            if (StringUtils.isNotBlank(name)) {
                ProjTemplate p = new ProjTemplate();
                p.setProjTmplName(name);
                List<ProjTemplate> list = new ArrayList<ProjTemplate>();
                if (StringUtils.isNotBlank(tmpId)) {
                    ProjTemplate t = projTemplateService.getProjEntity(tmpId);
                    list = projTemplateService.getProjTemplateListByNameAndBizId(t.getBizId(), tmpId);
                } else {
                    list = projTemplateService.getProjTemplateListByName(name);
                }
                if (list.size() > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projecttemplate.nameQueryFail");
            log.error(message, e, "", "");
            Object[] params = new Object[]{message, ProjTemplate.class.getClass()};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
    }

    @ApiOperation(value = "通过BizId获取项目模板",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String")
    })
    @RequestMapping(value = "getProjTemplateByBizId")
    public ProjTemplateDto getProjTemplateByBizId(@RequestParam(value = "bizId", required = false) String bizId) {
        ProjTemplate projTemplate = projTemplateService.getProjTemplateByBizId(bizId);
        ProjTemplateDto dto = new ProjTemplateDto();
        try {
            if (!CommonUtil.isEmpty(projTemplate)) {
                Dto2Entity.copyProperties(projTemplate, dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "查询项目模板菜单",httpMethod = "GET")
    @RequestMapping(value = "searchProjTemplateMenu")
    public List<ProjTemplateMenuDto> searchProjTemplateMenu(@RequestBody ProjTemplateMenuDto projTemplateMenuDto) {
        ProjTemplateMenu projTemplateMenu = new ProjTemplateMenu();
        Dto2Entity.copyProperties(projTemplateMenuDto, projTemplateMenu);
        List<ProjTemplateMenu> list = projTemplateService.searchProjTemplateMenu(projTemplateMenu);
        List<ProjTemplateMenuDto> dtoList = JSON.parseArray(JSON.toJSONString(list), ProjTemplateMenuDto.class);
        return dtoList;
    }

    @ApiOperation(value = "回退项目模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @RequestMapping(value = "backToVersion")
    public FeignJson backToVersion(@RequestParam("id") String id, @RequestParam("bizId") String bizId, @RequestParam("type") String type,
                                   @RequestBody TSUserDto userDto, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projTemplateService.backToVersion(id, bizId, type, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "通过BizId及名称获取项目模板",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String")
    })
    @RequestMapping(value = "getProjTemplateListByNameAndBizId")
    public List<ProjTemplateDto> getProjTemplateListByNameAndBizId(@RequestParam("name") String name, @RequestParam("bizId") String bizId) {
        List<ProjTemplate> list = projTemplateService.getProjTemplateListByNameAndBizId(name, bizId);
        List<ProjTemplateDto> dtoList = JSON.parseArray(JSON.toJSONString(list), ProjTemplateDto.class);
        return dtoList;
    }

    @ApiOperation(value = "通过名称获取项目模板",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String")
    })
    @RequestMapping(value = "getProjTemplateListByName")
    public List<ProjTemplateDto> getProjTemplateListByName(@RequestParam("name") String name) {
        List<ProjTemplate> list = projTemplateService.getProjTemplateListByName(name);
        List<ProjTemplateDto> dtoList = JSON.parseArray(JSON.toJSONString(list), ProjTemplateDto.class);
        return dtoList;
    }

    @ApiOperation(value = "复制项目模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @RequestMapping(value = "copyProjTemplate")
    public FeignJson copyProjTemplate(@RequestBody ProjTemplateDto projTemplateDto, @RequestParam("templateId") String templateId,
                                      @RequestParam("userId") String userId, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ProjTemplate projTemplate = new ProjTemplate();
            Dto2Entity.copyProperties(projTemplateDto, projTemplate);
            String newTempId = projTemplateService.copyProjTemplate(projTemplate, templateId, userId, orgId);
            j.setObj(newTempId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取项目模板版本信息及数量",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页码", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "每页数量", dataType = "Integer")
    })
    @RequestMapping(value = "getVersionHistoryAndCount")
    public FeignJson getVersionHistoryAndCount(@RequestParam("bizId") String bizId, @RequestParam("pageSize") Integer pageSize,
                                               @RequestParam("pageNum") Integer pageNum) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            List<ProjTemplate> projTemplateList = projTemplateService.getVersionHistory(bizId, pageSize, pageNum);
            List<ProjTemplateDto> dtoList = CodeUtils.JsonListToList(projTemplateList, ProjTemplateDto.class);
            long cnt = 0;
            cnt = projTemplateService.getVersionCount(bizId);
            Map<String, Object> map = new HashMap<>();
            map.put("projTemplateList", dtoList);
            map.put("count", cnt);
            j.setObj(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "根据项目模板id获取项目模板权限",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目模板id", dataType = "String")
    })
    @RequestMapping(value = "getProjTmpLibAuthLibTmpLinkByTemplateId")
    public List<ProjTmpLibAuthLibTmpLinkDto> getProjTmpLibAuthLibTmpLinkByTemplateId(@RequestParam(value = "templateId", required = false) String templateId) {
        List<ProjTmpLibAuthLibTmpLink> list = projTemplateService.getProjTmpLibAuthLibTmpLinkByTemplateId(templateId);
        List<ProjTmpLibAuthLibTmpLinkDto> dtoList = JSON.parseArray(JSON.toJSONString(list), ProjTmpLibAuthLibTmpLinkDto.class);
        return dtoList;
    }

    @ApiOperation(value = "启动项目模板审批流程",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "leader", value = "室领导", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deptLeader", value = "部门领导", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @RequestMapping(value = "startProjTemplateProcess")
    public FeignJson startProjTemplateProcess(@RequestParam(value = "templateId", required = false) String templateId, @RequestParam(value = "leader", required = false) String leader,
                                              @RequestParam(value = "deptLeader", required = false) String deptLeader, @RequestBody TSUserDto userDto, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projTemplateService.startProjTemplateProcess(templateId, leader, deptLeader, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "继续项目模板审批流程",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @RequestMapping(value = "completeProjTemplateProcess")
    public FeignJson completeProjTemplateProcess(@RequestBody ProjTemplateDto projTemplateDto, @RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ProjTemplate projTemplate = new ProjTemplate();
            Dto2Entity.copyProperties(projTemplateDto, projTemplate);
            TSUserDto userDto = userService.getUserByUserId(userId);
            projTemplateService.submitProjectTemplateFlowAagin(projTemplate, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "搜索项目模板",httpMethod = "GET")
    @RequestMapping(value = "searchProjTemplate")
    public List<ProjTemplateDto> searchProjTemplate(@RequestBody ProjTemplateDto projTemplateDto) {
        ProjTemplate projTemplate = new ProjTemplate();
        Dto2Entity.copyProperties(projTemplateDto, projTemplate);
        List<ProjTemplate> list = projTemplateService.searchProjTemplate(projTemplate);
        List<ProjTemplateDto> dtoList = CodeUtils.JsonListToList(list, ProjTemplateDto.class);
        return dtoList;
    }

    @ApiOperation(value = "项目模板新增保存",httpMethod = "POST")
    @RequestMapping(value = "SaveAsTemplate")
    public FeignJson SaveAsTemplate(@RequestBody Map<String, Object> map) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProjTemplateDto projTemplateDto = mapper.convertValue(map.get("projTemplate"), ProjTemplateDto.class);
            ProjectDto projectDto = mapper.convertValue(map.get("project"), ProjectDto.class);
            ProjTemplate projTemplate = new ProjTemplate();
            Dto2Entity.copyProperties(projTemplateDto, projTemplate);
            Project project = new Project();
            Dto2Entity.copyProperties(projectDto, project);
            List<String> plans = mapper.convertValue(map.get("plans"), new TypeReference<List<String>>() {
            });
            List<String> teams = mapper.convertValue(map.get("teams"), new TypeReference<List<String>>() {
            });
            List<String> libs = mapper.convertValue(map.get("libs"), new TypeReference<List<String>>() {
            });
            List<String> libPower = mapper.convertValue(map.get("libPower"), new TypeReference<List<String>>() {
            });
            TSUserDto curUser = mapper.convertValue(map.get("curUser"), TSUserDto.class);
            String orgId = String.valueOf(map.get("orgId"));
            String projectId = String.valueOf(map.get("projectId"));
            String templateId = projTemplateService.SaveAsTemplate(projTemplate, project, plans, teams, libs, libPower, projectId, curUser, orgId);
            j.setObj(templateId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获得项目的文件夹树",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "havePower", value = "权限", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @RequestMapping(value = "getFolderTree")
    public List<RepFileDto> getFolderTree(@RequestParam("projectId") String projectId, @RequestParam("havePower") String havePower, @RequestParam("userId") String userId) {
        List<RepFileDto> dtoList = projLibService.getFolderTree(projectId, havePower, userId);
        return dtoList;
    }

    @ApiOperation(value = "根据项目模板Id查询存储库Id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目模板Id", dataType = "String")
    })
    @RequestMapping(value = "getLibIdByTemplateId")
    public FeignJson getLibIdByTemplateId(@RequestParam(value = "templateId", required = false) String templateId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String libId = projTemplateLibService.getLibIdByTemplateId(templateId);
            j.setObj(libId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "项目模板计划导入EXCEL",httpMethod = "GET")
    @RequestMapping(value = "saveProjectTemplateDetailByExcel")
    public FeignJson saveProjectTemplateDetailByExcel(@RequestBody Map<String,Object> objMap)
    {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProjTemplateDto projTemplateDto = mapper.convertValue(objMap.get("projectTemplate"), ProjTemplateDto.class);
            ProjTemplate projTemplate = new ProjTemplate();
            Dto2Entity.copyProperties(projTemplateDto, projTemplate);
            List<PlanTemplateExcelVo> dataTempList = mapper.convertValue(objMap.get("dataTempList"), new TypeReference<List<PlanTemplateExcelVo>>() {
            });
            Map<String, PlanTemplateExcelVo> excelMap = mapper.convertValue(objMap.get("excelMap"), new TypeReference<Map<String, PlanTemplateExcelVo>>() {
            });
            Map<String, String> planLevelMap = mapper.convertValue(objMap.get("planLevelMap"), new TypeReference<Map<String, String>>() {
            });
            String switchStr = String.valueOf(objMap.get("switchStr"));
            String orgId = String.valueOf(objMap.get("orgId"));
            TSUserDto userDto = mapper.convertValue(objMap.get("curUser"), TSUserDto.class);
            projTemplateService.saveProjectTemplateDetailByExcel(projTemplate, dataTempList, excelMap, planLevelMap, switchStr, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @RequestMapping(value = "getImportDataList")
    public FeignJson getImportDataList(@RequestBody List<Map<String,Object>> map,@RequestParam(value = "userId",required = false) String userId,@RequestParam(value = "projectTemplateId",required = false) String projectTemplateId,@RequestParam(value = "orgId",required = false) String orgId){
        FeignJson feignJson = new FeignJson();
        try {
            Map<String,Object> returnMap = projTemplateService.doImportPlanTemplateExcel(map,userId,projectTemplateId,orgId);
            feignJson.setObj(returnMap);
        } catch (Exception e) {
            feignJson.setMsg(e.getMessage());
            feignJson.setSuccess(false);
            e.printStackTrace();
        }
        return feignJson;
    }
}
