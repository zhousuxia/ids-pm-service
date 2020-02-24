package com.glaway.ids.config.controller;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.config.constant.ConfigStateConstants;
import com.glaway.ids.config.dto.EpsConfigDto;
import com.glaway.ids.config.dto.ProjectLibTemplateDto;
import com.glaway.ids.config.dto.ProjectLibTemplateFileCategoryDto;
import com.glaway.ids.config.entity.EpsConfig;
import com.glaway.ids.config.entity.ProjectLibTemplate;
import com.glaway.ids.config.entity.ProjectLibTemplateFileCategory;
import com.glaway.ids.config.service.EpsConfigServiceI;
import com.glaway.ids.config.service.ProjectLibTemplateServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projectmanager.vo.RepFileAuthVo;
import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目库模板接口
 */
@Api(tags = {"项目库模板接口"})
@RestController
@RequestMapping("/feign/projectLibTemplateRestController")
public class ProjectLibTemplateRestController extends BaseController {
    /**
     * 消息信息
     */
    private String message = "";
    /**
     * 项目库权限模板处理
     */
    @Autowired
    private ProjectLibTemplateServiceI projectLibTemplateService;

    /**
     *
     * @return
     */
    @ApiOperation(value = "获取启用的项目库模板", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllUseProjectLibTemplate")
    public List<ProjectLibTemplateDto> getAllUseProjectLibTemplate() {
        List<ProjectLibTemplate> list = projectLibTemplateService.getAllUseProjectLibTemplate();
        List<ProjectLibTemplateDto> dtoList = JSON.parseArray(JSON.toJSONString(list), ProjectLibTemplateDto.class);
        return dtoList;
    }

    /**
     *
     * @param templateId
     * @return
     */
    @ApiOperation(value = "根据id获取项目库模板信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目库模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjLibTemplateEntity")
    public ProjectLibTemplateDto getProjLibTemplateEntity(@RequestParam("templateId") String templateId) {
        ProjectLibTemplate projectLibTemplate = projectLibTemplateService.getProjLibTemplateEntity(templateId);
        ProjectLibTemplateDto dto = new ProjectLibTemplateDto();
        try {
            dto = (ProjectLibTemplateDto) VDataUtil.getVDataByEntity(projectLibTemplate, ProjectLibTemplateDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     *
     * @param templateId
     * @return
     */
    @ApiOperation(value = "获取项目库权限模板的目录的根节点的ID",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目库模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTemplateCategoryRootNodeId")
    public FeignJson getTemplateCategoryRootNodeId(@RequestParam("templateId") String templateId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String rootId = projectLibTemplateService.getTemplateCategoryRootNodeId(templateId);
            j.setObj(rootId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    /**
     *
     * @param fileId
     * @return
     */
    @ApiOperation(value = "获取项目库权限模板目录角色权限",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "目录ID", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjLibTemplateRoleFileAuths")
    public List<ProjLibRoleFileAuthVo> getProjLibTemplateRoleFileAuths(@RequestParam(value = "fileId", required = false) String fileId) {
        List<ProjLibRoleFileAuthVo> list = projectLibTemplateService.getProjLibTemplateRoleFileAuths(fileId);
        return list;
    }

    /**
     *
     * @param templateId
     * @return
     */
    @ApiOperation(value = "获取项目库权限模板目录结构",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目库模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectLibTemplateFileCategorys")
    public List<TreeNode> getProjectLibTemplateFileCategorys(@RequestParam("templateId") String templateId) {
        List<TreeNode> list = projectLibTemplateService.getProjectLibTemplateFileCategorys(templateId);
        return list;
    }

    /**
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除项目库权限模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "项目库权限模板ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteProjectLibTemplateByIds")
    public FeignJson deleteProjectLibTemplateByIds(@RequestParam("ids") String ids) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projectLibTemplateService.deleteProjectLibTemplateByIds(ids);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    /**
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "项目库权限模板清单查询",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryProjectLibTemplates")
    public PageList queryProjectLibTemplates(@RequestBody Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        List<ConditionVO> conditionList = mapper.convertValue(map.get("conditionList"), new TypeReference<List<ConditionVO>>() {
        });
        Map<String, String> params = (Map<String, String>) map.get("params");
        PageList pageList = projectLibTemplateService.queryProjectLibTemplates(conditionList, params);
        return pageList;
    }

    /**
     *
     * @param ids
     * @param status
     * @param userDto
     * @param orgId
     * @return
     */
    @ApiOperation(value = "根据id批量启用或禁用项目库权限模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "项目库权限模板ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "启用或禁用标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startOrStopTemplateByIds")
    public FeignJson startOrStopTemplateByIds(@RequestParam("ids") String ids, @RequestParam("status") String status, @RequestBody TSUserDto userDto, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projectLibTemplateService.startOrStopTemplateByIds(ids, status, userDto, orgId);
            j.setMsg("成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("失败");
        } finally {
            return j;
        }
    }

    /**
     *
     * @param name
     * @param remark
     * @param userDto
     * @param orgId
     * @return
     */
    @ApiOperation(value = "新增项目库权限模板保存其基本信息及初始化目录结构",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "项目库权限模板名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "项目库权限模板备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveProjectLibTemplate")
    public FeignJson saveProjectLibTemplate(@RequestParam("name") String name, @RequestParam(value = "remark", required = false) String remark, @RequestBody TSUserDto userDto, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ProjectLibTemplate projectLibTemplate = new ProjectLibTemplate();
            projectLibTemplate.setName(name.trim());
            boolean isExist = projectLibTemplateService.checkTemplateNameExist(projectLibTemplate);
            if (!isExist) {
                projectLibTemplate.setRemark(remark);
                projectLibTemplate.setStatus(ConfigStateConstants.START_KEY);
                projectLibTemplate = projectLibTemplateService.saveOrUpdateProjectLibTemplate(projectLibTemplate, userDto, orgId);
                j.setObj(projectLibTemplate.getId());
                j.setSuccess(true);
            } else {
                message = I18nUtil.getValue("com.glaway.ids.pm.config.projectLibTemplate.nameHaveExits");
                j.setSuccess(false);
                j.setMsg(message);
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("失败");
        } finally {
            return j;
        }
    }

    /**
     *
     * @param templateId
     * @param fileId
     * @param repFileAuthVoList
     * @return
     */
    @ApiOperation(value = "判断目录角色权限是否变化",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目库权限模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "项目库权限模板目录id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkRoleFileAuthExistChange")
    public boolean checkRoleFileAuthExistChange(@RequestParam(value = "templateId", required = false) String templateId, @RequestParam(value = "fileId", required = false) String fileId,
                                                @RequestBody List<RepFileAuthVo> repFileAuthVoList) {
        boolean flag = true;
        flag = projectLibTemplateService.checkRoleFileAuthExistChange(templateId, fileId, repFileAuthVoList);
        return flag;
    }

    /**
     *
     * @param templateId
     * @param fileId
     * @param repFileAuthVoList
     * @param userId
     * @param orgId
     * @return
     */
    @ApiOperation(value = "保存项目库权限模板目录角色权限",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目库权限模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "项目库权限模板目录id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveProjLibRoleFileAuth")
    public FeignJson saveProjLibRoleFileAuth(@RequestParam(value = "templateId", required = false) String templateId, @RequestParam(value = "fileId", required = false) String fileId,
                                             @RequestBody List<RepFileAuthVo> repFileAuthVoList, @RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projectLibTemplateService.saveProjLibRoleFileAuth(templateId, fileId, repFileAuthVoList, userId, orgId);
            j.setMsg("成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("失败");
        } finally {
            return j;
        }
    }

    /**
     *
     * @param name
     * @param parentId
     * @param templateId
     * @param userDto
     * @param orgId
     * @return
     */
    @ApiOperation(value = "新增项目库权限模板文件目录",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "项目库权限模板文件目录名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "项目库权限模板文件目录父节点id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目库权限模板文件目录id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doAddTreeNode")
    public FeignJson doAddTreeNode(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "parentId", required = false) String parentId,
                                   @RequestParam(value = "templateId", required = false) String templateId, @RequestBody TSUserDto userDto, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ProjectLibTemplateFileCategory ptfc = new ProjectLibTemplateFileCategory();
            ptfc.setName(name.trim());
            ptfc.setParentId(parentId);
            boolean isExist = projectLibTemplateService.checkCategoryNameExist(ptfc);
            if (!isExist) {
                long maxOrderNum = projectLibTemplateService.getMaxOrderNum(parentId);
                ptfc.setOrderNum(maxOrderNum + 1);
                ptfc.setTemplateId(templateId);
                projectLibTemplateService.saveOrUpdateProjectLibTemplateFileCategory(ptfc, userDto, orgId);
                TreeNode menu = new TreeNode();
                menu.setId(ptfc.getId());
                menu.setPid(parentId);
                menu.setTitle(name);
                menu.setName(name);
                menu.setOpen(true);
                menu.setIconClose("webpage/com/glaway/ids/common/zTreeIcon_close.png");
                menu.setIconOpen("webpage/com/glaway/ids/common/zTreeIcon_open.png");
                menu.setIcon("webpage/com/glaway/ids/common/zTreeIcon_open.png");
                j.setObj(menu);
            } else {
                message = I18nUtil.getValue("com.glaway.ids.pm.config.projectLibTemplate.categoryNameHaveExits");
                j.setSuccess(false);
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("失败");
        } finally {
            return j;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取项目库权限模板文件目录信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目库权限模板文件目录id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectLibTemplateCategoryEntity")
    public ProjectLibTemplateFileCategoryDto getProjectLibTemplateCategoryEntity(@RequestParam("id") String id) {
        ProjectLibTemplateFileCategory projectLibTemplateFileCategory = projectLibTemplateService.getProjectLibTemplateCategoryEntity(id);
        ProjectLibTemplateFileCategoryDto dto = new ProjectLibTemplateFileCategoryDto();
        Dto2Entity.copyProperties(projectLibTemplateFileCategory, dto);
        return dto;
    }

    /**
     *
     * @param id
     * @param name
     * @param userDto
     * @param orgId
     * @return
     */
    @ApiOperation(value = "更新项目库权限模板文件目录信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目库权限模板文件id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "项目库权限模板文件名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doUpdateTreeNode")
    public FeignJson doUpdateTreeNode(@RequestParam("id") String id, @RequestParam("name") String name, @RequestBody TSUserDto userDto, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ProjectLibTemplateFileCategory ptfc = projectLibTemplateService.getProjectLibTemplateCategoryEntity(id);
            ptfc.setName(name.trim());
            boolean isExist = projectLibTemplateService.checkCategoryNameExist(ptfc);
            if (!isExist) {
                projectLibTemplateService.saveOrUpdateProjectLibTemplateFileCategory(ptfc, userDto, orgId);
            } else {
                message = I18nUtil.getValue("com.glaway.ids.pm.config.projectLibTemplate.categoryNameHaveExits");
                j.setSuccess(false);
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "检查项目库权限模板的目录是否存在子节点",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目库权限模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkCategoryExistChildNode")
    public boolean checkCategoryExistChildNode(@RequestParam("id") String id) {
        boolean flag = true;
        flag = projectLibTemplateService.checkCategoryExistChildNode(id);
        return flag;
    }

    /**
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除项目库权限模板目录",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目库权限模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteProjectLibTemplateFileCategory")
    public FeignJson deleteProjectLibTemplateFileCategory(@RequestParam("id") String id) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projectLibTemplateService.deleteProjectLibTemplateFileCategory(id);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    /**
     *
     * @param id
     * @param name
     * @param targetId
     * @param moveType
     * @param userDto
     * @param orgId
     * @return
     */
    @ApiOperation(value = "项目库权限模板文件目录节点移动",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目库权限模板文件目录id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "项目库权限模板文件目录名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "targetId", value = "被移动项目库权限模板文件目录id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "moveType", value = "移动类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doMoveNode")
    public FeignJson doMoveNode(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "targetId", required = false) String targetId,
                                @RequestParam(value = "moveType", required = false) String moveType, @RequestBody TSUserDto userDto, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ProjectLibTemplateFileCategory moveNodeInfo = new ProjectLibTemplateFileCategory();
            moveNodeInfo.setId(id);
            moveNodeInfo.setName(name);
            message = projectLibTemplateService.doMoveNode(moveNodeInfo, targetId, moveType, userDto, orgId);
            j.setMsg(message);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    /**
     *
     * @param templateId
     * @param name
     * @param remark
     * @param userDto
     * @param orgId
     * @return
     */
    @ApiOperation(value = "新增或修改项目库权限模板保存其基本信息及初始化目录结构",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目库权限模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "项目库权限模板名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "项目库权限模板备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doUpdateProjectLibTemplate")
    public FeignJson doUpdateProjectLibTemplate(@RequestParam(value = "templateId", required = false) String templateId, @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "remark", required = false) String remark, @RequestBody TSUserDto userDto, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ProjectLibTemplate projectLibTemplate = projectLibTemplateService.getProjLibTemplateEntity(templateId);
            ProjectLibTemplate p = new ProjectLibTemplate();
            p.setName(name.trim());
            p.setId(projectLibTemplate.getId());
            boolean isExist = projectLibTemplateService.checkTemplateNameExist(p);
            if (!isExist) {
                projectLibTemplate.setName(name.trim());
                projectLibTemplate.setRemark(remark);
                projectLibTemplateService.saveOrUpdateProjectLibTemplate(projectLibTemplate, userDto, orgId);
                j.setObj(templateId);
                j.setSuccess(true);
            } else {
                message = I18nUtil.getValue("com.glaway.ids.pm.config.projectLibTemplate.nameHaveExits");
                j.setSuccess(false);
                j.setMsg(message);
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }
}
