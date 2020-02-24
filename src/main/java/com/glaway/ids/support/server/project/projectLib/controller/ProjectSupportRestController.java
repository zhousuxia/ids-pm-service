package com.glaway.ids.support.server.project.projectLib.controller;

import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.projectmanager.vo.ProjVo;
import com.glaway.ids.support.server.project.projectLib.service.ProjectSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: ProjectSupportRestController
 * @Date: 2019/7/19-13:54
 * @since
 */
@Api(tags = {"项目计划相关接口"})
@RestController
@RequestMapping("/feign/projectSupportRestController")
public class ProjectSupportRestController {

    @Autowired
    private ProjectSupport projectSupport;

    @ApiOperation(value = "根据项目id获取项目信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目id", dataType = "String")
    })
    @RequestMapping("/findProjectInfo")
    public FeignJson findProjectInfo(@RequestParam(value = "id",required = false) String id) {
        String str = projectSupport.findProjectInfo(id);
        FeignJson j = new FeignJson();
        j.setObj(str);
        return j;
    }

    @ApiOperation(value = "获取项目库权限",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileId", value = "文档id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isExpert", value = "是否专家相关", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @RequestMapping("/getDocumentPower")
    public FeignJson getDocumentPower(@RequestParam(value = "fileId",required = false) String fileId,
                                      @RequestParam(value = "projectId",required = false) String projectId,@RequestParam(value = "status",required = false) String status,
                                      @RequestParam(value = "isExpert",required = false) String isExpert,@RequestParam(value = "userId",required = false) String userId) {
        String str = projectSupport.getDocumentPower(fileId, projectId, status, isExpert, userId);
        FeignJson j = new FeignJson();
        j.setObj(str);
        return j;
    }

    @ApiOperation(value = "通过条件查询项目库",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "securityLevel", value = "安全等级", dataType = "String")
    })
    @RequestMapping("/getDocumentList")
    public FeignJson getDocumentList(@RequestParam(value = "projectId",required = false) String projectId,
                                     @RequestParam(value = "folderId",required = false) String folderId,@RequestParam(value = "securityLevel",required = false) Short securityLevel) {
        String str = projectSupport.getDocumentList(projectId, folderId, securityLevel);
        FeignJson j = new FeignJson();
        j.setObj(str);
        return j;
    }

    @ApiOperation(value = "保存评审报告",httpMethod = "POST")
    @RequestMapping("/saveReviewReport")
    public void saveReviewReport(@RequestBody Map<String,String> params) {
        String projectId = params.get("projectId");
        String securityLevel = params.get("securityLevel");
        String path = params.get("path");
        String pathName = params.get("pathName");
        String docName = params.get("docName");
        String remark = params.get("remark");
        String users = params.get("users");
        String pathid = params.get("pathid");
        String docTypeId = params.get("docTypeId");
        projectSupport.saveReviewReport(projectId,securityLevel,path,pathName,docName,remark,users,pathid,docTypeId);

    }

    @RequestMapping("/operationProject")
    public FeignJson operationProject(@RequestParam(value = "id",required = false) String id,
                                      @RequestParam(value = "havePower",required = false) String havePower,@RequestParam(value = "userId",required = false) String userId) {
        String str = projectSupport.operationProject(id, havePower, userId);
        FeignJson j = new FeignJson();
        j.setObj(str);
        return j;
    }

    @ApiOperation(value = "获取所有项目数据",httpMethod = "GET")
    @RequestMapping("/getAllProjectList")
    public List<ProjVo> getAllProjectList() {
        return projectSupport.getAllProjectList();
    }


    @RequestMapping("/findProjectListForRP")
    public List<ProjVo> findProjectListForRP() {
        return projectSupport.findProjectListForRP();
    }

    @ApiOperation(value = "获取项目团队成员",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @RequestMapping("/findProjectUsersList")
    public FeignJson findProjectUsersList(@RequestParam(value = "projectId",required = false) String projectId) {
        String userIds = projectSupport.findProjectUsersList(projectId);
        FeignJson fj = new FeignJson();
        fj.setObj(userIds);
        return fj;
    }

}
