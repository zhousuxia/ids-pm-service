package com.glaway.ids.project.projecttemplate.service.impl;


import java.util.List;

import com.glaway.foundation.common.dto.FdTeamRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.core.common.extend.hqlsearch.HqlGenerateUtil;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.fdk.dev.service.threemember.FeignTeamService;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.projecttemplate.entity.ProjTmplTeamLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateRoleServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;


/**
 * 项目模板角色Service
 * 
 * @author wangshen
 * @version 2015年5月4日
 * @see ProjTemplateRoleServiceImpl
 * @since
 */
@Service("projTemplateRoleService")
@Transactional
public class ProjTemplateRoleServiceImpl implements ProjTemplateRoleServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(ProjTemplateRoleServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private ProjTemplateServiceI projTemplateService;
    
    @Autowired
    private ProjRoleServiceI projRoleService;

    @Autowired
    private FeignTeamService teamService;

    @Value(value="${spring.application.name}")
    private String appKey;


    
    @Override
    public String getTeamIdByTemplateId(String templateId) {
        ProjTmplTeamLink projTmplTeamLink=new ProjTmplTeamLink();
        projTmplTeamLink.setProjTemplateId(templateId);
        List<ProjTmplTeamLink>  links = searchProjTmplTeamLink(projTmplTeamLink);
        if (!CommonUtil.isEmpty(links)) {
            return links.get(0).getTeamId();

        }
        return "";
    }

    
    private List<ProjTmplTeamLink> searchProjTmplTeamLink(ProjTmplTeamLink projTmplTeamLink) {
        CriteriaQuery cq = new CriteriaQuery(ProjTmplTeamLink.class);
        HqlGenerateUtil.installHql(cq, projTmplTeamLink, null);
        return sessionFacade.getListByCriteriaQuery(cq, false);
    }

    @Override
    public void batchInitSaveRoles(Project project, String templateId) {
        String projTeamId = projRoleService.getTeamIdByProjectId(project.getId());
        List<FdTeamRoleDto> fdTeamRoles = teamService.getRoleByTeamId(appKey,projTeamId);;
        String teamId= getTeamIdByTemplateId(templateId);
        for(FdTeamRoleDto role:fdTeamRoles){
            projRoleService.addTeamRoleByCode(teamId, role.getRoleCode());
        }
    }
}