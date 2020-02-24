package com.glaway.ids.project.projecttemplate.service;


import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.menu.entity.Project;

/**
 * 项目模板service
 * 
 * @author zhousuxia
 * @version 2019年8月20日
 * @see ProjTemplateRoleServiceI
 * @since
 */
public interface ProjTemplateRoleServiceI {
    
    /**


    /**
     * 
     *  根据项目模板Id查询项目模板的团队Id
     * @param templateId 模板id
     * @return 
     * @see
     */
    String getTeamIdByTemplateId(String templateId);


    /**
     * 保存项目的角色到项目模板中
     * @param project    项目id
     * @param templateId 模板id
     * @see
     */
    void batchInitSaveRoles(Project project, String templateId);
    

}
