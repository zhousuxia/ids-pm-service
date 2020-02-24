/*
 * 文件名：ProjLibController.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：wangshen
 * 修改时间：2015年5月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.glaway.ids.project.projectmanager.controller;

import com.glaway.ids.project.projecttemplate.dto.ProjTemplateDto;
import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.attribute.dto.EntityAttributeAdditionalAttributeDto;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSTypeDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileAttachmentDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileTypeDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepRoleFileAuthDto;
import com.glaway.foundation.fdk.dev.service.FeignAttributeService;
import com.glaway.foundation.fdk.dev.service.FeignSystemService;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.config.service.RepFileTypeConfigServiceI;
import com.glaway.ids.constant.RepFileTypeConstants;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.menu.service.RecentlyProjectServiceI;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PlanLog;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.dto.ProjDocRelationDto;
import com.glaway.ids.project.projectmanager.dto.ProjLibFileDto;
import com.glaway.ids.project.projectmanager.dto.ProjectLibAuthTemplateLinkDto;
import com.glaway.ids.project.projectmanager.entity.ProjDocRelation;
import com.glaway.ids.project.projectmanager.entity.ProjLibFile;
import com.glaway.ids.project.projectmanager.entity.ProjectLibAuthTemplateLink;
import com.glaway.ids.project.projectmanager.service.*;
import com.glaway.ids.project.projectmanager.vo.ProjLibDocumentVo;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projectmanager.vo.RepFileAuthVo;
import org.jasypt.commons.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author wangshen
 * @version V1.0
 * @Title: Controller
 * @Description: 项目库
 * @date 2015-05-11 13:00:25
 */
@Api(tags = {"项目团队接口"})
@RestController
@RequestMapping("/feign/projLibRestController")
public class ProjLibRestController extends BaseController {
    /**
     * 操作日志接口
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(ProjLibRestController.class);
    /**
     * 交付项接口
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;
    /**
     *
     */
    @Autowired
    private ProjectServiceI projectService;
    /**
     *
     */
    @Autowired
    private ProjLibServiceI projLibService;
    /**
     *
     */
    @Autowired
    private ProjLogServiceI projLogService;
    @Autowired
    private FeignSystemService feignSystemService;
    /**
     * 项目库目录角色权限Service
     */
    @Autowired
    private ProjLibAuthServiceI projLibAuthService;
    /**
     *
     */
    @Autowired
    private RecentlyProjectServiceI recentlyProjectService;
    @Autowired
    private ProjRoleServiceI projRoleService;
    @Autowired
    private FeignAttributeService attributeService;
    @Autowired
    private FeignUserService userService;
    @Autowired
    private PlanServiceI planService;
    /**
     *
     */
    @Autowired
    private FeignRepService repService;
    /**
     * 注入WorkFlowFacade
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;
    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;
    @Value(value = "${spring.application.name}")
    private String appKey;
    /**
     * message全局变量<br>
     */
    private String message;
    /**
     * 文档类型设置服务类
     */
    @Autowired
    private RepFileTypeConfigServiceI repFileTypeConfigService;

    /**
     * 获取消息全局变量
     *
     * @param
     * @return message
     * @see
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置消息全局变量
     *
     * @param message
     * @return
     * @see
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @ApiOperation(value = "验证文档编号是否重复",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "docNumber", value = "文档编号", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "validateReptDocNum")
    public boolean validateReptDocNum(@RequestParam(value = "docNumber", required = false) String docNumber) {
        boolean flag = projLibService.validateReptDocNum(docNumber);
        return flag;
    }

    @ApiOperation(value = "创建文件夹",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "createFile")
    public String createFile(@RequestBody ProjLibDocumentVo document, @RequestParam("userId") String userId) {
        String fileId = projLibService.createFile(document, userId);
        return fileId;
    }

    @ApiOperation(value = "新增附件",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "addRepFileAttachment")
    public void addRepFileAttachment(@RequestBody RepFileAttachmentDto fileAttachment) {
        projLibService.addRepFileAttachment(fileAttachment);
    }

    @ApiOperation(value = "更新附件",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateFileAttachment")
    public void updateFileAttachment(@RequestBody RepFileAttachmentDto fileAttachment) {
        projLibService.updateFileAttachment(fileAttachment);
    }

    @ApiOperation(value = "查询项目库目录列表并转成vo",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getRepList")
    public List<ProjLibDocumentVo> getRepList(@RequestParam("fileId") String fileId, @RequestParam("folderId") String folderId,
                                              @RequestParam("projectId") String projectId, @RequestParam("userId") String userId) {
        return projLibService.getRepList(fileId, folderId, projectId, userId);
    }

    @ApiOperation(value = "是否隐藏项目库文件夹树页面操作按钮",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "文件夹id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "isHidProjLibOperForDir")
    public boolean isHidProjLibOperForDir(@RequestParam("id") String id) {
        boolean flag = projLibService.isHidProjLibOperForDir(id);
        return flag;
    }

    @ApiOperation(value = "根据文件id获得vo对象",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjDocmentVoById")
    public ProjLibDocumentVo getProjDocmentVoById(@RequestParam("folderId") String folderId) {
        ProjLibDocumentVo projDocVo = projLibService.getProjDocmentVoById(folderId);
        return projDocVo;
    }

    @ApiOperation(value = "根据文件id获得项目id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectIdByFileId")
    public String getProjectIdByFileId(@RequestParam("folderId") String folderId) {
        String data = projLibService.getProjectIdByFileId(folderId);
        return data;
    }

    @ApiOperation(value = "获得项目的文件夹树",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "havePower", value = "权限", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getFolderTree")
    public List<RepFileDto> getFolderTree(@RequestParam("projectId") String projectId, @RequestParam("havePower") String havePower, @RequestParam("userId") String userId) {
        List<RepFileDto> dtoList = projLibService.getFolderTreeForProjLib(projectId, havePower, userId);
        return dtoList;
    }

    @ApiOperation(value = "查询项目库权限模板与项目关联对象",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectLibAuthTemplateLinkId")
    public List<ProjectLibAuthTemplateLinkDto> getProjectLibAuthTemplateLinkId(@RequestParam("projectId") String projectId) {
        List<ProjectLibAuthTemplateLink> linkList = projLibService.getProjectLibAuthTemplateLinkId(projectId);
        List<ProjectLibAuthTemplateLinkDto> resList = JSON.parseArray(JSON.toJSONString(linkList), ProjectLibAuthTemplateLinkDto.class);
        return resList;
    }

    @ApiOperation(value = "校验同级的目录名是否已存在",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkCategoryNameExist")
    public Boolean checkCategoryNameExist(@RequestBody RepFileDto repFileDto) {
        Boolean flag = projLibService.checkCategoryNameExist(repFileDto);
        return flag;
    }

    @ApiOperation(value = "判断文件夹是否为根目录",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "isRootFolder")
    public Boolean isRootFolder(@RequestParam("fileId") String fileId) {
        Boolean isRootFolder = projLibService.isRootFolder(fileId);
        return isRootFolder;
    }

    @ApiOperation(value = "删除文档",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "delFileById")
    public Boolean delFileById(@RequestParam("fileId") String fileId) {
        Boolean isSuccess = projLibService.delFileById(fileId);
        return isSuccess;
    }

    @ApiOperation(value = "删除文档前校验是否可以删除",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "目标id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "beforeDelFolder")
    public FeignJson beforeDelFolder(@RequestParam(value = "folderId",required = false) String folderId) {
        return projLibService.beforeDelFolder(folderId);
    }

    @ApiOperation(value = "是否隐藏项目库文档页面操作按钮",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "isHidProjLibOper")
    public Boolean isHidProjLibOper(@RequestParam("projectId") String projectId) {
        Boolean isHidProjLibOper = projLibService.isHidProjLibOper(projectId);
        return isHidProjLibOper;
    }

    @ApiOperation(value = "获取用户对目录的权限",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getCategoryFileAuths")
    public String getCategoryFileAuths(@RequestParam("fileId") String fileId, @RequestParam("userId") String userId) {
        String categoryFileAuths = projLibAuthService.getCategoryFileAuths(fileId, userId);
        return categoryFileAuths;
    }

    @ApiOperation(value = "获取文档路径",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDocNamePath")
    public FeignJson getDocNamePath(@RequestParam("folderId") String folderId) {
        FeignJson j = new FeignJson();
        String docNamePath = projLibService.getDocNamePath(folderId);
        j.setObj(docNamePath);
        return j;
    }

    @ApiOperation(value = "批量删除文档",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "datasStr", value = "文档数据", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doBatchDel")
    public FeignJson doBatchDel(@RequestBody List<ProjLibDocumentVo> docVos, @RequestParam("datasStr") String datasStr) {
        FeignJson j = new FeignJson();
        message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.deleteSuccess");
        j.setMsg(message);
        j.setSuccess(true);
        try {
            List<String> httpUrls = new ArrayList<String>();
       /*     List<OutwardExtension> outwardExtensionList = outwardExtensionService.getOutwardExtensionList("taskExcuteCategoryHttpServer");
            if (!CommonUtil.isEmpty(outwardExtensionList)) {
                for (OutwardExtension ext : outwardExtensionList) {
                    for (OutwardExtensionUrl out : ext.getUrlList()) {
                        if ("checkDocIsUsed".equals(out.getOperateCode())) {
                            httpUrls.add(out.getOperateUrl());
                        }
                    }
                }
            }*/
            // FIXME doing
            for (ProjLibDocumentVo doc : docVos) {
                String id = doc.getId();
                RepFileDto r = repService.getRepFileByRepFileId(ResourceUtil.getApplicationInformation().getAppKey(), id);
                if (!r.getBizCurrent().equals("nizhi")) {
                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.onlyEditingDelete");
                    return j;
                }
                String bizversion = null;
                String bizId = doc.getBizId();
                if (StringUtil.isNotEmpty(doc.getVersion())) {
                    bizversion = doc.getVersion().split("\\.")[0] + ".";
                }
                List<RepFileDto> repFileList = repService.getRepFileByBizIdAndbizversion(ResourceUtil.getApplicationInformation().getAppKey(), bizId, bizversion);
                for (RepFileDto repFile : repFileList) {
                    String docId = repFile.getId();
 /*                   if(!CommonUtil.isEmpty(httpUrls)){
                        for(String url : httpUrls){
                            try {
                                JsonResult res = HttpClientUtil.httpClientPostByTest(url+"&upDocId="+docId, null);
                                if (res.isSuccess()) {
                                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.cannotDeleteUsed");
                                    return j;
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }*/
                    List<ProjDocRelation> projDocRelList = projLibService.getRelationDocByDocId(docId);
                    if (projDocRelList != null && projDocRelList.size() > 0) {
                        message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.cannotDeleteUsed");
                        j.setMsg(message);
                        j.setSuccess(false);
                        return j;
                    }
                }
            }
            projLibService.batchDel(docVos);
            log.info(message, "", "");
        } catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.deleteFailure");
            j.setMsg(message);
            j.setSuccess(false);
            log.error(message, e, datasStr, "");
            Object[] params = new Object[] {message,
                    ProjTemplateDto.class.getClass() + " json:" + datasStr};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "更新项目库文档路径",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "父文档id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "repFileIds", value = "文档id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updatePathRepFile")
    public void updatePathRepFile(@RequestParam("parentId") String parentId, @RequestParam("repFileIds") String repFileIds) {
        projLibService.updatePathRepFile(parentId, repFileIds);
    }

    @ApiOperation(value = "查询项目库文档列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createName", value = "创建者姓名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "modifiName", value = "修改者姓名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "docTypeId", value = "文档类型id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryEntity")
    public PageList queryEntity(@RequestBody Map<String, Object> map, @RequestParam("folderId") String folderId, @RequestParam("projectId") String projectId,
                                @RequestParam("createName") String createName, @RequestParam("modifiName") String modifiName, @RequestParam("docTypeId") String docTypeId, @RequestParam("userId") String userId) {
        List<ConditionVO> conditionList = (List<ConditionVO>) map.get("conditionList");
        Map<String, String> nameAndValueMap = (Map<String, String>) map.get("nameAndValueMap");
        PageList pageList = projLibService.queryEntity(conditionList, folderId, projectId,
                createName, modifiName, nameAndValueMap, docTypeId, userId);
        return pageList;
    }

    @ApiOperation(value = "新增项目库文档",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "docattachmentNames", value = "附件名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "docattachmentURLs", value = "附件地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "docSecurityLevelFroms", value = "附件安全等级", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doAddProjLibDoc")
    public FeignJson doAddProjLibDoc(@RequestBody Map<String, Object> map, @RequestParam("docattachmentNames") String docattachmentNames,
                                     @RequestParam("docattachmentURLs") String docattachmentURLs, @RequestParam("docSecurityLevelFroms") String docSecurityLevelFroms, @RequestParam("type") String type) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        j.setMsg("新增成功");
        try {
            String oldFileTypeId = "";
            ObjectMapper mapper = new ObjectMapper();
            ProjLibDocumentVo document = mapper.convertValue(map.get("document"), ProjLibDocumentVo.class);
            RepFileTypeDto repFileType = mapper.convertValue(map.get("repFileType"), RepFileTypeDto.class);
            TSUserDto currentUser = mapper.convertValue(map.get("currentUser"), TSUserDto.class);
            Map<String, Object> paramsMap = (Map<String, Object>) map.get("paramsMap");
            String opType = map.get("opType") == null ? "" : map.get("opType").toString();
            String planId = map.get("planId") == null ? "" : map.get("planId").toString();
            String sad = document.getProjectId();
            Project project = projectService.getProjectEntity(document.getProjectId());
            if (!CommonUtil.isEmpty(document.getFileTypeId())) {
                oldFileTypeId = document.getFileTypeId();
                /**
                 * 文件类型由RepFileType
                 */
                RepFileTypeDto fileType = repService.getRepFileTypeById(appKey, document.getFileTypeId());
                if (CommonUtil.isEmpty(document.getFileTypeId()) || (!CommonUtil.isEmpty(fileType) && CommonUtil.isEmpty(fileType.getGenerateRuleId()))) {
                    //如果编号为空，则选择数据库中默认编号规则
                    List<RepFileTypeDto> repFileTypeList = repService.getRepFileTypesByFileTypeCode(appKey, RepFileTypeConstants.REP_FILE_TYPE_PRO);
                    String fileTypeId = "";
                    if (!CommonUtil.isEmpty(repFileTypeList)) {
                        for (RepFileTypeDto repDto : repFileTypeList) {
                            if ("1".equals(repDto.getAvaliable())) {
                                fileTypeId = repDto.getId();
                                break;
                            }
                        }
                    }
                    document.setFileTypeId(fileTypeId);
                }
            }
            String docNameNew = document.getDocName();
            if (document.getType().equals(0)) {
                type = "文件夹";
            }
            message = type + I18nUtil.getValue("com.glaway.ids.common.msg.create") + I18nUtil.getValue("com.glaway.ids.knowledge.support.success");
            String dictCode = "secretLevel";
            Map<String, List<TSTypeDto>> tsMap = feignSystemService.getAllTypesOfTypeGroup(ResourceUtil.getApplicationInformation().getAppKey());
            List<TSTypeDto> curTypes = tsMap.get(dictCode);
            Map<String, String> secretLevelCodeAndNameMap = new HashMap<String, String>();
            for (TSTypeDto curData : curTypes) {
                secretLevelCodeAndNameMap.put(curData.getTypename(), curData.getTypecode());
            }
            if (projLibService.validateReptDocNum(document.getDocNumber())) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.docNumberExist");
                j.setSuccess(false);
                return j;
            }
            String fileId = projLibService.createFile(document, currentUser);
            RepFileDto repFile = repService.getRepFileByRepFileId(appKey, fileId);
            //更新类型：
            repFile.setFileTypeId(oldFileTypeId);
            //TODO
            //软属性保存前，切换到对应的对象，即保存新的对象绑定关系：
            /*if(!CommonUtil.isEmpty(document.getFileTypeId())){
                List<EntityAttributeAdditionalAttributeDto> allList = attributeService.getEntityAttributeAdditionalAttributeListByCondition(repFileType.getClass().getName(), oldFileTypeId, oldFileTypeId);
                if(!CommonUtil.isEmpty(allList)){
                    projLibService.saveEntityAttrAdditionalAttribute(repFile,allList);
                    Map<String, String> entityAttrAdditionalAttributeValMap = projLibService.saveEntityAttrAdditionalAttributeVal(attrNames,paramsMap);
                    projLibService.initEntityAttrAdditionalAttribute(
                            repFile, repFile.getId(),
                            repFile.getId(),entityAttrAdditionalAttributeValMap);
                    //软属性数据保存：
                    projLibService.addOrUpdateEntityAttrAdditionalAttribute(
                            repFile, attrNames,paramsMap);
                }
            }
*/
            if (CommonUtils.isNotEmpty(docattachmentNames)) {
                for (int i = 0; i < docattachmentNames.split(",").length; i++) {
                    RepFileAttachmentDto fileAttachment = new RepFileAttachmentDto();
                    fileAttachment.setAttachmentName(docattachmentNames.split(",")[i]);
                    fileAttachment.setAttachmentURL(docattachmentURLs.split(",")[i]);
                    // 保存附件信息
                    String attachmentSecurityLevel = docSecurityLevelFroms.split(",")[i];
                    if (!CommonUtil.isEmpty(secretLevelCodeAndNameMap.get(attachmentSecurityLevel))) {
                        fileAttachment.setSecurityLevel((short) Integer.parseInt(secretLevelCodeAndNameMap.get(attachmentSecurityLevel)));
                    } else {
                        fileAttachment.setSecurityLevel((short) Integer.parseInt(curTypes.get(0).getTypecode()));
                    }
                    fileAttachment.setFileId(fileId);
                    fileAttachment.setCreateBy(currentUser.getId());
                    fileAttachment.setCreateName(currentUser.getUserName());
                    fileAttachment.setCreateFullName(currentUser.getRealName());
                    projLibService.addRepFileAttachment(fileAttachment);
                    fileAttachment.setFirstName(fileAttachment.getCreateName());
                    fileAttachment.setFirstFullName(fileAttachment.getCreateFullName());
                    fileAttachment.setFirstTime(fileAttachment.getCreateTime());
                    projLibService.updateFileAttachment(fileAttachment);
                }
            }
            TreeNode menu = new TreeNode();
            menu.setId(fileId);
            menu.setPid(document.getParentId());
            menu.setTitle(document.getDocName());
            menu.setName(document.getDocName());
            menu.setOpen(true);
            menu.setDataObject("projLibController.do?goProjDocList&teamId=" + null
                    + "&folderId=" + menu.getId() + "&projectId="
                    + sad + "&canSelect=" + document.getOperStatus());
            menu.setIconClose("webpage/com/glaway/ids/common/zTreeIcon_close.png");
            menu.setIconOpen("webpage/com/glaway/ids/common/zTreeIcon_open.png");
            menu.setIcon("webpage/com/glaway/ids/common/zTreeIcon_open.png");
            j.setObj(menu);
            // 计划提交项操作记录
            if (!CommonUtil.isEmpty(opType)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.addDeliverySuccess");
            }
            PlanLog planLog = new PlanLog();
            try {
                planLog.setPlanId(planId);
                planLog.setLogInfo("挂接交付物:" + docNameNew);
                planLog.setCreateBy(currentUser.getId());
                planLog.setCreateName(currentUser.getUserName());
                planLog.setCreateFullName(currentUser.getRealName());
                planLog.setCreateTime(new Date());
                projLibService.saveProjLog(planLog);
            } catch (Exception e) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.library.saveLogFailure");
                log.error(message, e, null, message);
                Object[] params = new Object[]{message};// 异常原因：{0}；异常描述：{1}
                throw new GWException(GWConstants.ERROR_2001, params, e);
            }
            j.setObj(fileId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("新增失败");
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取项目库目录角色权限",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjLibRoleFileAuths")
    public List<ProjLibRoleFileAuthVo> getProjLibRoleFileAuths(@RequestParam("fileId") String fileId) {
        return projLibService.getProjLibRoleFileAuths(fileId);
    }

    @ApiOperation(value = "判断目录角色权限是否变化",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkRoleFileAuthExistChange")
    public Boolean checkRoleFileAuthExistChange(@RequestParam("fileId") String fileId, @RequestBody List<RepFileAuthVo> repList) {
        return projLibService.checkRoleFileAuthExistChange(fileId, repList);
    }

    @ApiOperation(value = "保存项目库权目录角色权限",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveProjLibRoleFileAuth")
    public FeignJson saveProjLibRoleFileAuth(@RequestParam("fileId") String fileId, @RequestBody List<RepFileAuthVo> repFileAuthVoList,
                                             @RequestParam("userId") String userId) {
        FeignJson j = new FeignJson();
        try {
            projLibService.saveProjLibRoleFileAuth(fileId, repFileAuthVoList, userId);
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

    @ApiOperation(value = "批量删除数据库附件",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "附件ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "attachmentBatchDel")
    public FeignJson attachmentBatchDel(@RequestParam("ids") String ids) {
        FeignJson j = new FeignJson();
        try {
            projLibService.attachmentBatchDel(ids);
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

    @ApiOperation(value = "根据文件id获取项目库文件信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文件id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjLibFile")
    public List<ProjLibFileDto> getProjLibFile(@RequestParam("fileId") String fileId) {
        List<ProjLibFile> list = projLibService.getProjLibFile(fileId);
        List<ProjLibFileDto> results = new ArrayList<>();
        try {
            results = (List<ProjLibFileDto>) VDataUtil.getVDataByEntity(list, ProjLibFileDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @ApiOperation(value = "更新文件",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "method", value = "标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "文件id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "message", value = "提示信息", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateFile")
    public FeignJson updateFile(@RequestParam("method") String method, @RequestBody ProjLibDocumentVo document,
                                @RequestParam("id") String id, @RequestParam("message") String message, @RequestParam("userId") String userId) {
        FeignJson j = new FeignJson();
        String str = projLibService.updateFile(method, document, id, message, userId);
        j.setObj(str);
        return j;
    }

    @ApiOperation(value = "更新流程变量及我的待办",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "newFiledId", value = "文件id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "procInstId", value = "流程id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "docName", value = "文件名", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateVariablesAndTodoTask")
    public FeignJson updateVariablesAndTodoTask(@RequestParam("newFiledId") String newFiledId, @RequestParam("procInstId") String procInstId,
                                                @RequestParam("docName") String docName) {
        FeignJson j = new FeignJson();
        try {
            projLibService.updateVariablesAndTodoTask(newFiledId, procInstId, docName);
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

    @ApiOperation(value = "根据版本id获得文件vo",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getFilesByBizId")
    public List<ProjLibDocumentVo> getFilesByBizId(@RequestParam("bizId") String bizId) {
        List<ProjLibDocumentVo> list = projLibService.getFilesByBizId(bizId);
        return list;
    }

    @ApiOperation(value = "项目库文档版本回退",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "文档id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "backVersion")
    public FeignJson backVersion(@RequestParam("id") String id, @RequestParam("bizId") String bizId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projLibService.backVersion(id, bizId);
        } catch (Exception e) {
            j.setSuccess(false);
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取所有的项目文档关系",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllDocRelationList")
    public List<ProjDocRelationDto> getAllDocRelationList() {
        List<ProjDocRelation> list = projLibService.getAllDocRelationList();
        List<ProjDocRelationDto> results = new ArrayList<ProjDocRelationDto>();
        try {
            results = (List<ProjDocRelationDto>) VDataUtil.getVDataByEntity(list, ProjDocRelationDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @ApiOperation(value = "提交文档审批流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "submitProcess")
    public FeignJson submitProcess(@RequestBody Map<String, String> params) {
        return projLibService.submitProcess(params);
    }

    @ApiOperation(value = "保存软属性",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveEntityAttrAdditionalAttribute")
    public void saveEntityAttrAdditionalAttribute(@RequestBody Map<String, Object> attrMap) {
        ObjectMapper mapper = new ObjectMapper();
        RepFileDto repFile = mapper.convertValue(attrMap.get("repFile"), RepFileDto.class);
        List<EntityAttributeAdditionalAttributeDto> allList = mapper.convertValue(attrMap.get("allList"), new TypeReference<List<EntityAttributeAdditionalAttributeDto>>() {
        });
        projLibService.saveEntityAttrAdditionalAttribute(repFile, allList);
    }

    @ApiOperation(value = "初始化软属性",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "entityAttrName", value = "软属性名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "entityAttrVal", value = "软属性值", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "initEntityAttrAdditionalAttribute")
    public void initEntityAttrAdditionalAttribute(@RequestBody Map<String, Object> map, @RequestParam("entityAttrName") String entityAttrName, @RequestParam("entityAttrVal") String entityAttrVal) {
        ObjectMapper mapper = new ObjectMapper();
        RepFileDto repFile = mapper.convertValue(map.get("repFile"), RepFileDto.class);
        Map<String, String> entityAttrAdditionalAttributeValMap = (Map<String, String>) map.get("entityAttrAdditionalAttributeValMap");
        projLibService.initEntityAttrAdditionalAttribute(repFile, entityAttrName, entityAttrVal, entityAttrAdditionalAttributeValMap);
    }

    @ApiOperation(value = "根据引用文档的Id查询项目文档关系对象",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "quoteId", value = "引用文档Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDocRelation")
    public List<ProjDocRelationDto> getDocRelation(@RequestParam(value = "quoteId", required = false) String quoteId) {
        List<ProjDocRelation> list = projLibService.getDocRelationList(quoteId);
        List<ProjDocRelationDto> results = new ArrayList<>();
        try {
            results = (List<ProjDocRelationDto>) VDataUtil.getVDataByEntity(list, ProjDocRelationDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @ApiOperation(value = "保存软属性值",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveEntityAttrAdditionalAttributeVal")
    public Map<String, String> saveEntityAttrAdditionalAttributeVal(@RequestBody Map<String, Object> paramMap) {
        return projLibService.saveEntityAttrAdditionalAttributeVal(paramMap);
    }

    @ApiOperation(value = "通过交付项id获取文件夹id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "deliverableId", value = "交付项id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getfolderIdByDeliverableId")
    public FeignJson getfolderIdByDeliverableId(@RequestParam(value = "deliverableId", required = false) String deliverableId,
                                                @RequestParam(value = "projectId", required = false) String projectId) {
        return projLibService.getfolderIdByDeliverableId(deliverableId, projectId);
    }

    @ApiOperation(value = "交换两个文件的位置",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "srcId", value = "文件夹id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "destId", value = "目标文件夹id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "changeEachOtherForVo")
    public FeignJson changeEachOtherForVo(@RequestParam(value = "srcId", required = false) String srcId, @RequestParam(value = "destId", required = false) String destId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projLibService.changeEachOtherForVo(srcId, destId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "左右移动更新文件夹的ordernumber和父节点",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "docId", value = "文档id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "父文件夹id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateTreeOrderNum")
    public FeignJson updateTreeOrderNum(@RequestParam(value = "docId", required = false) String docId, @RequestParam(value = "parentId", required = false) String parentId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projLibService.updateTreeOrderNum(docId, parentId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "删除文件夹和其角色目录权限关系",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "delFileAndAuthById")
    public boolean delFileAndAuthById(@RequestParam(value = "folderId", required = false) String folderId) {
        boolean flag = true;
        flag = projLibAuthService.delFileAndAuthById(folderId);
        return flag;
    }

    @ApiOperation(value = "获取应用模板的路径权限信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTemplateRoleAuths")
    public Map<String, List<RepRoleFileAuthDto>> getTemplateRoleAuths(@RequestParam(value = "projectId", required = false) String projectId,
                                                                      @RequestParam(value = "templateId", required = false) String templateId,
                                                                      @RequestBody List<TSRoleDto> roles) {
        Map<String, List<RepRoleFileAuthDto>> map = new HashMap<>();
        map = projLibService.getTemplateRoleAuths(projectId, templateId, roles);
        return map;
    }

    @ApiOperation(value = "项目库模板应用",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "applyTemplate")
    public FeignJson applyTemplate(@RequestParam(value = "projectId", required = false) String projectId, @RequestBody Map<String, List<RepRoleFileAuthDto>> map,
                                   @RequestParam(value = "templateId", required = false) String templateId,
                                   @RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            projLibService.applyTemplate(projectId, map, templateId, userId, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取项目库文档版本信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页码", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "每页数量", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getVersionHistory")
    public List<ProjLibDocumentVo> getVersionHistory(@RequestParam(value = "bizId", required = false) String bizId, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                     @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "isPage", required = false) boolean isPage) {
        List<ProjLibDocumentVo> list = projLibService.getVersionHistory(bizId, pageSize, pageNum, isPage);
        return list;
    }

    @ApiOperation(value = "获取项目库文档权限",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDocumentFileAuths")
    public FeignJson getDocumentFileAuths(@RequestParam(value = "fileId",required = false) String fileId,@RequestParam(value = "userId",required = false) String userId){
        FeignJson j = new FeignJson();
        try{
            String auth = projLibAuthService.getDocumentFileAuths(fileId,userId);
            j.setObj(auth);
            j.setSuccess(true);
        }catch (Exception e){
            j.setSuccess(false);
        }finally {
            return j;
        }
    }
}
