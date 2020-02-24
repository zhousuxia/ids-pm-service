package com.glaway.ids.support.server.project.projectLib.service.impl;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileAttachmentDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.system.serial.SerialNumberManager;
import com.glaway.ids.config.entity.RepFile;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjLibDocumentVo;
import com.glaway.ids.project.projectmanager.vo.ProjVo;
import com.glaway.ids.support.server.project.projectLib.service.ProjectSupport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: ProjectSupportImpl
 * @Date: 2019/7/19-14:01
 * @since
 */
@Service
@Transactional
public class ProjectSupportImpl implements ProjectSupport {

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private PlanServiceI planService;

    @Autowired
    private ProjRoleServiceI projRoleService;

    @Autowired
    private ProjLibServiceI projLibService;

    @Override
    public String findProjectInfo(String id) throws GWException {
        Project project = new Project();
        List<Project> projectList = projectService.getProjectListForWeb(project, 1, 10, false);
        List<ProjVo> lstProjVo = new ArrayList<ProjVo>();
        for (Project p : projectList) {
            ProjVo p1 = new ProjVo();
            p1.setId(p.getId());
            p1.setName(p.getName());
            p1.setProjectNumber(p.getProjectNumber());
            lstProjVo.add(p1);
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转化为特定格式
                .setPrettyPrinting() // 对json结果格式化
                .create();
        String jsonRsp = gson.toJson(lstProjVo);
        return jsonRsp;
    }

    @Override
    public String getDocumentPower(String fileId, String projectId, String status,
                                   String isExpert, String userId)
            throws GWException {
        String havePower = "";
        if (StringUtils.isNotEmpty(fileId)) {
            havePower = planService.getOutPowerForPlanListForReview(fileId, userId);
            // 不是已完工状态
            if (!"finish".equals(status)) {
                // 判断否涉及专家那几个几点
                if ("true".equals(isExpert)) {
                    havePower = "listDownloadDetail";
                }
                else {
                    List<TSUserDto> users = projRoleService.getUserInProject(projectId);
                    Boolean a = false;
                    for (TSUserDto r : users) {
                        if (userId.equals(r.getId())) {
                            a = true;
                            break;
                        }
                    }
                    // 是否在项目团队中
                    if (!a) {
                        havePower = "";
                    }
                }
            }
        }
        else {
            havePower = "true";
            // 只判断是否属于当前项目团队
            List<TSUserDto> users = projRoleService.getUserInProject(projectId);
            Boolean a = false;
            for (TSUserDto r : users) {
                if (userId.equals(r.getId())) {
                    a = true;
                    break;
                }
            }
            // 是否在项目团队中
            if (!a) {
                havePower = "false";
            }
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转化为特定格式
                .setPrettyPrinting() // 对json结果格式化
                .create();
        // String jsonRsp = gson.toJson(havePower);
        return havePower;
    }

    @Override
    public String getDocumentList(String projectId, String folderId, Short securityLevel)
            throws GWException {
        List<ProjLibDocumentVo> docs = new ArrayList<ProjLibDocumentVo>();
        if (StringUtils.isNotEmpty(folderId)) {
            String page = "1";
            String rows = "10";
            docs = projLibService.getDocFoldDataPageForWeb(projectId, folderId, securityLevel,
                    false, Integer.parseInt(page), Integer.parseInt(rows));
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转化为特定格式
                .setPrettyPrinting() // 对json结果格式化
                .create();
        String jsonRsp = gson.toJson(docs);
        return jsonRsp;
    }

    @Override
    public void saveReviewReport(String projectId, String securityLevel, String path,
                                 String pathName, String docName, String remark, String users,
                                 String pathid,String docTypeId)
            throws GWException {
        ProjLibDocumentVo projLib = new ProjLibDocumentVo();
        projLib.setProjectId(projectId);
        projLib.setPath(path);
        projLib.setPathName(pathName);
        projLib.setDocName(docName);
        projLib.setRemark(remark);
        projLib.setSecurityLevel(securityLevel);

        Project project = (Project) sessionFacade.getEntity(Project.class, projectId);
        projLib.setParentId(pathid);
        // projLib.setPath(pathid);
        String projectNo = project.getProjectNumber();
        projLib.setType(1);
        projLib.setParentId(pathid);
        SerialNumberManager.getDataMap().put("projectNo", projectNo);
        // String id = projLib.getId();
        // String type = "文档";
        /**
         * 文件类型由RepFileType
         */
        // projLib.setFileTypeId("4028ef2d504608ba0150462418bf0001");
        // 需求变更typeID更改为CODE
//        String fileTypeId = repFileTypeQueryService.getFileTypeIdByCode(RepFileTypeConstants.REP_FILE_TYPE_PRO);
        //缺陷18883，在报告归档页面添加文档类型选择，此处由默认的根节点改为用户选择的文档类型    ---zsx 2019/5/17
        projLib.setFileTypeId(docTypeId);
        String fileId = projLibService.createFile1(projLib, users, securityLevel);
        RepFileAttachmentDto fileAttachment = new RepFileAttachmentDto();
        fileAttachment.setAttachmentName(pathName);
        if (StringUtils.isNotEmpty(securityLevel)) {
            fileAttachment.setSecurityLevel(Short.valueOf(securityLevel));
        }
        fileAttachment.setAttachmentURL(path);
        fileAttachment.setFileId(fileId);
        String[] suer = users.split(",");
        fileAttachment.setFirstBy(suer[2]);
        fileAttachment.setFirstTime(new Date());
        fileAttachment.setFirstName(suer[1]);
        fileAttachment.setFirstFullName(suer[0]);
        projLibService.addRepFileAttachment(fileAttachment);

    }

    @Override
    public String operationProject(String id, String havePower, String userId)
            throws GWException {
        List<TreeNode> list = new ArrayList<TreeNode>();
        if (StringUtils.isNotEmpty(id)) {
            List<TSUserDto> users = projRoleService.getUserInProject(id);
            Boolean a = false;
            for (TSUserDto r : users) {
                if (userId.equals(r.getId())) {
                    a = true;
                    break;
                }
            }
            // 是否在项目团队中
            if (a) {
                if (StringUtils.isNotEmpty(havePower) && "1".equals(havePower)) {
                    Project project = (Project) sessionFacade.getEntity(Project.class, id);
                    if (null != project) {
                        List<RepFileDto> files = projLibService.getFolderTreeForProjLib(id, "1", userId);

                        for (RepFileDto file : files) {
                            TreeNode menu = null;
                            if (StringUtils.equals(file.getLibId(), file.getParentId())) {
                                menu = new TreeNode(file.getId(), null, file.getFileName(),
                                        file.getFileName(), true);
                            }
                            else {
                                menu = new TreeNode(file.getId(), file.getParentId(),
                                        file.getFileName(), file.getFileName(), true);

                            }
                            menu.setDataObject("reviewFlowController.do?goProjDocList&folderId="
                                    + menu.getId() + "&projectId=" + id + "&canSelect="
                                    + file.getOperStatus());
                            list.add(menu);

                        }
                    }
                }
                else {
                    Project project = (Project) sessionFacade.getEntity(Project.class, id);
                    if (null != project) {
                        List<RepFileDto> files = projLibService.getFolderTreeForProjLib(id, "0", userId);

                        for (RepFileDto file : files) {
                            TreeNode menu = null;
                            if (StringUtils.equals(file.getLibId(), file.getParentId())) {
                                menu = new TreeNode(file.getId(), null, file.getFileName(),
                                        file.getFileName(), true);
                            }
                            else {
                                menu = new TreeNode(file.getId(), file.getParentId(),
                                        file.getFileName(), file.getFileName(), true);

                            }
                            menu.setDataObject("reviewFlowController.do?goProjDocList&folderId="
                                    + menu.getId() + "&projectId=" + id);
                            list.add(menu);

                        }
                    }
                }
            }
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转化为特定格式
                .setPrettyPrinting() // 对json结果格式化
                .create();
        String jsonRsp = gson.toJson(list);
        return jsonRsp;
    }

    @Override
    public List<ProjVo> getAllProjectList() {
        List<ProjVo> lstProjVo = new ArrayList<ProjVo>();
        String sql = "SELECT ID,NAME,PROJECTNUMBER FROM PM_PROJECT WHERE BIZCURRENT != 'EDITING' AND BIZCURRENT !='CLOSED'";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                if (!CommonUtil.isEmpty(map.get("ID"))) {
                    ProjVo projVo = new ProjVo();
                    projVo.setId(String.valueOf(map.get("ID")));
                    projVo.setName(CommonUtil.isEmpty(map.get("NAME")) ? "" : String.valueOf(map.get("NAME")));
                    projVo.setProjectNumber(CommonUtil.isEmpty(map.get("PROJECTNUMBER")) ? "" : String.valueOf(map.get("PROJECTNUMBER")));
                    lstProjVo.add(projVo);
                }
            }
        }
        return lstProjVo;
    }

    @Override
    public List<ProjVo> findProjectListForRP() {
        Project project = new Project();
        List<Project> projectList = projectService.getProjectListForWeb(project, 1, 10, false);
        List<ProjVo> lstProjVo = new ArrayList<ProjVo>();
        for (Project p : projectList) {
            ProjVo p1 = new ProjVo();
            p1.setId(p.getId());
            p1.setName(p.getName());
            p1.setProjectNumber(p.getProjectNumber());
            lstProjVo.add(p1);
        }
        return lstProjVo;
    }

    @Override
    public String findProjectUsersList(String projectId) throws GWException {
        List<TSUserDto> list = new ArrayList<TSUserDto>();
        String result = "";
        if(!CommonUtil.isEmpty(projectId)) {
            list = projRoleService.getUserInProject(projectId);
        }
        if(!CommonUtil.isEmpty(list)) {
            for(TSUserDto t : list) {
                if(CommonUtil.isEmpty(result)) {
                    result = t.getId();
                } else {
                    result = result + "," + t.getId();
                }
            }
        }
        return result;
    }
}
