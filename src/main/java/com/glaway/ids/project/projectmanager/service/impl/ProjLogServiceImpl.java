package com.glaway.ids.project.projectmanager.service.impl;


import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.ids.constant.ProjectStatusConstants;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projectmanager.entity.ProjLibFile;
import com.glaway.ids.project.projectmanager.entity.ProjLog;
import com.glaway.ids.project.projectmanager.service.ProjLogServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service("projLogService")
@Transactional
public class ProjLogServiceImpl extends BusinessObjectServiceImpl<ProjLog> implements ProjLogServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(ProjLogServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private WorkFlowFacade workFlowFacade;

    @Autowired
    private FeignRepService repService;

    @Value(value="${spring.application.name}")
    private String appKey;

    
    @Override
    public String saveProjLog(String projectNumber,String projectId, String logInfo, String remark,TSUserDto userDto) {
        ProjLog projLog = new ProjLog();
        projLog.setProjectId(projectId);
        projLog.setProjectNumber(projectNumber);
        projLog.setLogInfo(logInfo);
        projLog.setRemark(remark);
        projLog.setCreateBy(userDto.getId());
        projLog.setCreateTime(new Date());
        projLog.setCreateUserName(userDto.getRealName() + "-" + userDto.getUserName());
        initBusinessObject(projLog);
        sessionFacade.save(projLog);
        return projLog.getId();
    }

    @Override
    public String saveProjLog(String projectNumber,String projectId, String logInfo, String remark) {
        ProjLog projLog = new ProjLog();
        projLog.setProjectId(projectId);
        projLog.setProjectNumber(projectNumber);
        projLog.setLogInfo(logInfo);
        projLog.setRemark(remark);
        projLog.setCreateUserName(UserUtil.getInstance().getUser().getRealName() + "-" + UserUtil.getInstance().getUser().getUserName());
        initBusinessObject(projLog);
        return sessionFacade.save(projLog).toString();
    }

    @Override
    public void setLibProjectStatus(Project project, String projectStatus) {
        String libId = projectService.getLibIdByProjectId(project.getId());
        List<ProjLibFile> list = null;
        if (StringUtil.isNotEmpty(libId)) {
            /*List<RepFileDto> repFiles = repService.getRepFileByLibId(libId);*/
            List<RepFileDto> repFiles =repService.getRepFileByLibIdAndParentId(appKey,libId,"");
            if (repFiles != null && repFiles.size() > 0) {
                if (ProjectStatusConstants.CLOSE.equals(projectStatus)) {
                    for (RepFileDto s : repFiles) {
                        if ("shenpi".equals(s.getBizCurrent())) {
                            String hql = createHql(s.getId());
                            list = sessionFacade.findByQueryString(hql);
                            if (list != null && list.size() > 0) {
                                for (ProjLibFile p : list) {
                                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                                            p.getProjectFileId(), p.getFeedbackProcInstId(),
                                            "项目关闭，项目下所有项目相关流程终止");
                                }
                            }
                        }
                    }
                }

                if (ProjectStatusConstants.PAUSE.equals(projectStatus)) {
                    for (RepFileDto s : repFiles) {
                        if ("shenpi".equals(s.getBizCurrent())) {
                            String hql = createHql(s.getId());
                            list = sessionFacade.findByQueryString(hql);
                            if (list != null && list.size() > 0) {
                                for (ProjLibFile p : list) {
                                    // 计划暂停时，计划相关的流程挂起
                                    workFlowFacade.getWorkFlowMonitorService().suspendProcessInstance(
                                            p.getProjectFileId(), p.getFeedbackProcInstId(),
                                            "项目暂停，项目下所有项目文件相关流程挂起");
                                }
                            }
                        }
                    }
                }
                else if (ProjectStatusConstants.RECOVER.equals(projectStatus)) {
                    for (RepFileDto s : repFiles) {
                        if ("shenpi".equals(s.getBizCurrent())) {
                            String hql = createHql(s.getId());
                            list = sessionFacade.findByQueryString(hql);
                            if (list != null && list.size() > 0) {
                                for (ProjLibFile p : list) {
                                    workFlowFacade.getWorkFlowMonitorService().activateSelectProcesses(
                                            p.getProjectFileId(), p.getFeedbackProcInstId());
                                }
                            }
                        }
                    }
                }

            }

        }

    }

    /**
     * Description: <br>
     *
     * @param repFile
     * @return
     * @see
     */
    private String createHql(String repFile) {
        String hql = "from ProjLibFile l where 1 = 1";
        if (StringUtil.isNotEmpty(repFile)) {
            hql = hql + " and l.projectFileId = '" + repFile + "'";
        }
        return hql;
    }

    @Override
    public void deleteProjLog(String projectId) {
        List<ProjLog> projLogList = projectService.getProjLogListByProjectId(projectId, 0, 10, false);
        sessionFacade.deleteAllEntitie(projLogList);
    }

    @Override
    public void save(ProjLog projLog) {
        sessionFacade.save(projLog);
    }
}