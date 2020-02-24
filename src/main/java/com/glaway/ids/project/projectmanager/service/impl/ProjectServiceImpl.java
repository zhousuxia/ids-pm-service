package com.glaway.ids.project.projectmanager.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.common.dto.*;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.core.common.model.json.AjaxJson;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepLibraryDto;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignGroupService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignRoleService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignTeamService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.config.constant.PlanviewConstant;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.constant.*;
import com.glaway.ids.project.menu.dto.ProjectDto;
import com.glaway.ids.project.menu.entity.ProjTeamLink;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.BusinessConfigDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.dto.ProjWarnDto;
import com.glaway.ids.project.projectmanager.entity.ProjLog;
import com.glaway.ids.project.projectmanager.entity.ProjWarn;
import com.glaway.ids.project.projectmanager.service.*;
import com.glaway.ids.project.projectmanager.vo.ProjWarnForGridVo;
import com.glaway.ids.project.projectmanager.vo.ProjWarnVo;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.Dto2Entity;
import com.glaway.ids.util.ProcessUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.ids.project.menu.service.RecentlyProjectServiceI;


/**
 * 〈项目管理服务接口〉
 * 〈功能详细描述〉
 * 
 * @author songjie
 * @version 2015年3月24日
 * @see ProjectServiceImpl
 * @since
 */
@Service("projectService")
@Transactional
public class ProjectServiceImpl extends BusinessObjectServiceImpl<Project> implements ProjectServiceI {
    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private static final SystemLog log = BaseLogFactory.getSystemLog(ProjectServiceImpl.class);

    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private ProjRoleServiceI projRoleService;

    @Autowired
    private ProjRolesServiceI projRolesServiceI;

    @Autowired
    private PlanBusinessConfigServiceI businessConfigService;



    @Autowired
    private WorkFlowFacade workFlowFacade;

    @Autowired
    private RecentlyProjectServiceI recentlyProjectService;

    /**
     * 项目日志服务实现接口<br>
     */
    @Autowired
    private ProjLogServiceI projLogService;


    @Autowired
    private FeignUserService userService;


    @Autowired
    private FeignTeamService teamService;


    @Autowired
    private FeignRoleService roleService;

    @Autowired
    private ProjLibServiceI projLibService;

    @Autowired
    private ProjTemplateHelper projTemplateHelper;

    @Autowired
    private ProjTemplateServiceI projTemplateService;

    /**
     * 项目计划
     */
    @Autowired
    private PlanServiceI planService;

    @Autowired
    private FeignRepService repService;

    @Autowired
    private FeignGroupService groupService;


    @Value(value="${spring.application.name}")
    private String appKey;


    @Autowired
    private Environment env;


    @Override
    public Project initProject(Project project) {
        Project p = initBusinessObject(project);
        return p;
    }

    @Override
    public PageList queryEntityBySql(List<ConditionVO> conditionList, String projectManager, String createName, String entryPage, String isTeamMember,String currentUserId,String orgId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.* from PM_PROJECT t where 1=1 ");
        String appSql = "";
        if(PlanviewConstant.PAGE_NAME.equals(entryPage)) {
            //计划视图查询项目
            appSql = getProjectListAuthorityAppendHql(isTeamMember,currentUserId);
        } else {
            appSql = getProjectListAuthorityAppendHql(currentUserId);
        }

        if (StringUtils.isNotEmpty(appSql)) {
            sql.append(appSql);
        }
        List<Project> list = new ArrayList<Project>();
        if (StringUtils.isNotEmpty(projectManager)) {
            sql.append(" and t.projectManagerNames like '%" + projectManager + "%'");
        }
        if(!CommonUtil.isEmpty(orgId)){
            sql.append( " and t.createOrgId = '" + orgId + "'");
        }
        if (StringUtils.isNotEmpty(createName)) {
            sql.append( " and ( t.createName like '%" + createName + "%'");
            sql.append( " or t.createFullName like '%" + createName + "%' )");
        }

        StringBuffer orderSql = new StringBuffer();
        orderSql.append("order by ");
        for (ConditionVO p : conditionList) {

            if ("Project.projectNumber".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    sql.append( " and lower(t.projectNumber) like lower('%" + p.getValue() + "%')");
                }
            }

            if ("Project.name".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    sql.append( " and lower(t.name) like lower('%" + p.getValue() + "%')");
                }
            }

            if ("Project.phaseInfo.name".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    String attrList = "";
                    for(String val : p.getValue().split(",")){
                        if(!CommonUtil.isEmpty(val)){
                            if(CommonUtil.isEmpty(attrList)){
                                attrList = "'"+val+"'";
                            }else{
                                attrList = attrList+","+"'"+val+"'";
                            }
                        }

                    }
                    sql.append( " and t.phase in ("+attrList+")");
                }
            }

            if ("Project.bizCurrent".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    String attrList = "";
                    for(String val : p.getValue().split(",")){
                        if(!CommonUtil.isEmpty(val)){
                            if(CommonUtil.isEmpty(attrList)){
                                attrList = "'"+val+"'";
                            }else{
                                attrList = attrList+","+"'"+val+"'";
                            }
                        }
                    }
                    sql.append( " and t.bizCurrent in ("+attrList+")");
                }
            }

            if ("Project.eps".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    String attrList = "";
                    for(String val : p.getValue().split(",")){
                        if(!CommonUtil.isEmpty(val)){
                            if(CommonUtil.isEmpty(attrList)){
                                attrList = "'"+val+"'";
                            }else{
                                attrList = attrList+","+"'"+val+"'";
                            }
                        }
                    }
                    sql.append( " and t.eps in ("+attrList+")");
                }
            }

            if ("Project.createTime".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    String[] attr = p.getValue().split(",");
                    sql.append(" and to_date(to_char(t.createTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }

            if ("Project.startProjectTime".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    String[] attr = p.getValue().split(",");
                    sql.append(" and to_date(to_char(t.startProjectTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }

            if ("Project.endProjectTime".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    String[] attr = p.getValue().split(",");
                    sql.append(" and to_date(to_char(t.endProjectTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }

            if ("Project.process".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getValue())) {
                    sql.append( " and t.process = " + p.getValue().replace('%', ' '));
                }
            }

            if ("order".equals(p.getKey())) {
                if (StringUtils.isNotEmpty(p.getSort())
                        && StringUtils.isNotEmpty(p.getValue())) {

                    if("createName".equals(p.getSort())){
                        orderSql.append(" t.createFullName "+p.getValue()+",");
                    }else if("phaseInfo".equals(p.getSort())){
                        orderSql.append(" t.phase "+p.getValue()+",");
                    }else{
                        orderSql.append(" t."+p.getSort()+" "+p.getValue()+",");
                    }


                }
            }

        }
        for (ConditionVO p : conditionList) {
            if ("Project.process".equals(p.getKey())) {
                conditionList.remove(p);
                break;
            }
        }

        for (ConditionVO p : conditionList) {
            if ("Project.eps".equals(p.getKey())) {
                if (",".equals(p.getValue())) {
                    conditionList.remove(p);
                    break;
                }
            }
        }

        for (ConditionVO p : conditionList) {
            if ("Project.name".equals(p.getKey())) {
                conditionList.remove(p);
                break;
            }
        }

        orderSql.append(" t.createTime desc, t.id desc");
        String orderSqlStr = orderSql.toString();
        sql.append(orderSqlStr);

        int page= 0;
        int rows= 0;
        for(ConditionVO conditionVo:conditionList){
            if(conditionVo.getKey().equals("page")){
                page= Integer.parseInt(conditionVo.getValue());
            }
            if(conditionVo.getKey().equals("rows")){
                rows= Integer.parseInt(conditionVo.getValue());
            }
        }
        int beginIndex = (page-1)*rows;
        int endIndex = page*rows;
        String sqlStr = sql.toString();
        String sqlStrNew = "select * from (select row_.*,rownum rownum_ from ("+sql+") row_) where rownum_ between "+beginIndex+" and "+endIndex+"";
        List<Map<String, Object>> projects = sessionFacade.findForJdbc(sqlStr,page ,rows);
        try {
            for (Map<String, Object> map : projects) {
                Project p = new Project();
                p.setId(CommonUtil.isEmpty(String.valueOf(map.get("ID"))) ? "" : String.valueOf(map.get("ID")));
                p.setName(CommonUtil.isEmpty(String.valueOf(map.get("NAME"))) ? "" : String.valueOf(map.get("NAME")));
                p.setProjectNumber(CommonUtil.isEmpty(String.valueOf(map.get("PROJECTNUMBER"))) ? "" : String.valueOf(map.get("PROJECTNUMBER")));
                p.setStatus(CommonUtil.isEmpty(String.valueOf(map.get("STATUS"))) ? "" : String.valueOf(map.get("STATUS")));
                p.setCreateBy(CommonUtil.isEmpty(String.valueOf(map.get("CREATEBY"))) ? "" : String.valueOf(map.get("CREATEBY")));
                p.setCreateName(CommonUtil.isEmpty(String.valueOf(map.get("CREATENAME"))) ? "" : String.valueOf(map.get("CREATENAME")));
                if(!CommonUtil.isEmpty(String.valueOf(map.get("CREATEBY")))){
                    TSUserDto tsUserDto = userService.getUserByUserId(String.valueOf(map.get("CREATEBY")));
                   /* TSUser tsUser = getEntity(TSUser.class,String.valueOf(map.get("CREATEBY")));*/
                    p.setCreator(tsUserDto);
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if (!CommonUtil.isEmpty(map.get("CREATETIME"))) {
                    try {
                        Date createTime = df.parse(String.valueOf(map.get("CREATETIME")));
                        p.setCreateTime(createTime);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                p.setUpdateBy(CommonUtil.isEmpty(String.valueOf(map.get("UPDATEBY"))) ? "" : String.valueOf(map.get("UPDATEBY")));
                p.setUpdateName(CommonUtil.isEmpty(String.valueOf(map.get("UPDATENAME"))) ? "" : String.valueOf(map.get("UPDATENAME")));
                if (!CommonUtil.isEmpty(map.get("UPDATETIME"))) {
                    try {
                        Date updateTime = df.parse(String.valueOf(map.get("UPDATETIME")));
                        p.setUpdateTime(updateTime);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                p.setAvaliable(CommonUtil.isEmpty(String.valueOf(map.get("AVALIABLE"))) ? "" : String.valueOf(map.get("AVALIABLE")));
                p.setBizCurrent(CommonUtil.isEmpty(String.valueOf(map.get("BIZCURRENT"))) ? "" : String.valueOf(map.get("BIZCURRENT")));
                p.setBizId(CommonUtil.isEmpty(String.valueOf(map.get("BIZID"))) ? "" : String.valueOf(map.get("BIZID")));
                p.setBizVersion(CommonUtil.isEmpty(String.valueOf(map.get("BIZVERSION"))) ? "" : String.valueOf(map.get("BIZVERSION")));
                if(!CommonUtil.isEmpty(String.valueOf(map.get("SECURITYLEVEL"))) && String.valueOf(map.get("SECURITYLEVEL")) != "null"){
                    p.setSecurityLevel(Short.valueOf(String.valueOf(map.get("SECURITYLEVEL"))));
                }

                if (!CommonUtil.isEmpty(map.get("ENDPROJECTTIME"))) {
                    try {
                        Date endProjectTime = df.parse(String.valueOf(map.get("ENDPROJECTTIME")));
                        p.setEndProjectTime(endProjectTime);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                p.setEps(CommonUtil.isEmpty(String.valueOf(map.get("EPS"))) ? "" : String.valueOf(map.get("EPS")));
                p.setIsRefuse(CommonUtil.isEmpty(String.valueOf(map.get("ISREFUSE"))) ? "" : String.valueOf(map.get("ISREFUSE")));
                p.setPhase(CommonUtil.isEmpty(String.valueOf(map.get("PHASE"))) ? "" : String.valueOf(map.get("PHASE")));
                if(!CommonUtil.isEmpty(String.valueOf(map.get("PHASE")))){
                    p.setPhaseInfo((BusinessConfig)sessionFacade.getEntity(BusinessConfig.class, String.valueOf(map.get("PHASE"))));
                }
                p.setProcess(Double.valueOf(String.valueOf(map.get("PROCESS"))));
                p.setProjectManagerNames(CommonUtil.isEmpty(String.valueOf(map.get("PROJECTMANAGERNAMES"))) ? "" : String.valueOf(map.get("PROJECTMANAGERNAMES")));
                p.setProjectTemplate(CommonUtil.isEmpty(String.valueOf(map.get("PROJECTTEMPLATE"))) ? "" : String.valueOf(map.get("PROJECTTEMPLATE")));
                p.setProjectTimeType(CommonUtil.isEmpty(String.valueOf(map.get("PROJECTTIMETYPE"))) ? "" : String.valueOf(map.get("PROJECTTIMETYPE")));
                p.setRemark(CommonUtil.isEmpty(String.valueOf(map.get("REMARK"))) ? "" : String.valueOf(map.get("REMARK")));
                p.setProjectManagers(CommonUtil.isEmpty(String.valueOf(map.get("PROJECTMANAGERS"))) ? "" : String.valueOf(map.get("PROJECTMANAGERS")));
                if (!CommonUtil.isEmpty(map.get("STARTPROJECTTIME"))) {
                    try {
                        Date startProjectTime = df.parse(String.valueOf(map.get("STARTPROJECTTIME")));
                        p.setStartProjectTime(startProjectTime);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                p.setCreateFullName(CommonUtil.isEmpty(String.valueOf(map.get("CREATEFULLNAME"))) ? "" : String.valueOf(map.get("CREATEFULLNAME")));
                p.setUpdateFullName(CommonUtil.isEmpty(String.valueOf(map.get("UPDATEFULLNAME"))) ? "" : String.valueOf(map.get("UPDATEFULLNAME")));
                p.setFirstBy(CommonUtil.isEmpty(String.valueOf(map.get("FIRSTBY"))) ? "" : String.valueOf(map.get("FIRSTBY")));
                p.setFirstFullName(CommonUtil.isEmpty(String.valueOf(map.get("FIRSTFULLNAME"))) ? "" : String.valueOf(map.get("FIRSTFULLNAME")));
                p.setFirstName(CommonUtil.isEmpty(String.valueOf(map.get("FIRSTNAME"))) ? "" : String.valueOf(map.get("FIRSTNAME")));
                if (!CommonUtil.isEmpty(map.get("FIRSTTIME"))) {
                    try {
                        Date firstTime = df.parse(String.valueOf(map.get("FIRSTTIME")));
                        p.setFirstTime(firstTime);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (!CommonUtil.isEmpty(map.get("CLOSETIME"))) {
                    try {
                        Date closeTime = df.parse(String.valueOf(map.get("CLOSETIME")));
                        p.setCloseTime(closeTime);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (!CommonUtil.isEmpty(map.get("PAUSETIME"))) {
                    try {
                        Date pauseTime = df.parse(String.valueOf(map.get("PAUSETIME")));
                        p.setPauseTime(pauseTime);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                p.setProcInstId(CommonUtil.isEmpty(String.valueOf(map.get("PROCINSTID"))) ? "" : String.valueOf(map.get("PROCINSTID")));
                list.add(p);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        String sqlForCnt = "select count(id) as cnt from("+sqlStr+")";
        /* Long count =  sessionFacade.getCountForJdbc(sqlForCnt);*/

        List<Map<String, Object>> cntList = sessionFacade.findForJdbc(sqlForCnt, null);
        Long count  = Long.valueOf(String.valueOf(cntList.get(0).get("CNT")));
        List<ProjectDto> result = CodeUtils.JsonListToList(list,ProjectDto.class);
        PageList pageList = new PageList(Integer.valueOf( String.valueOf(count )), result);
        return pageList;
    }

    @Override
    public TSRoleDto getRole(String code) {
       /* String hql = " select l from TSRole l where 1= 1";
        if (StringUtil.isNotEmpty(code) && StringUtil.isNotEmpty(code)) {
            hql = hql + " and l.roleCode = '" + code + "' ";
        }*/

        TSRoleDto tsRoleDto = roleService.getRoleByRoleCode(code);

        return tsRoleDto;
    }

    @Override
    public String getProjectbyNumber(String projectNumber) {
        Project project = null;
        if (StringUtils.isNotEmpty(projectNumber)) {
            project = getProjectByProjectNumber(projectNumber);
        }
        String json = JSON.toJSONString(project);
        return json;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Project getProjectByProjectNumber(String projectNumber) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from Project t where 1=1 and t.projectNumber=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(projectNumber);
        List<Project> projects = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        if (null != projects && projects.size() > 0) {
            return projects.get(0);
        }
        return null;
    }

    @Override
    public FeignJson createProject(Project project, TSUserDto userDto,String orgId) {
        FeignJson j = new FeignJson();
        String projectIdOld=project.getId();
        Project p = addProject(project,project.getCreateBy(),orgId);
        recentlyProjectService.updateRecentlyByProjectId(projectIdOld,userDto);
        j.setObj(p.getId());
        return j;
    }


    @Override
    public Project addProject(Project project,String userId,String orgId) {
        //添加从产品复制成项目的的相关信息  对项目ID的处理  如果是产品到IDS中项目新增的  项目  其项目ID之后有,+UserID  为了处理webService无法调用到当前用户的问题
        //该参数用来传递项目ID和当前用户的ID
        //从KDDwebService来的处理
        //改参数用来判断是否是从产品 复制成项目的新增
        String type=null;
/*    	if(StringUtil.isNotEmpty(project.getId())){
    		String projectIdOld=project.getId();
        	String [] idAndUserId=projectIdOld.split(",");

        	//判断是否是从 产品中复制到项目的处理
        	if(idAndUserId.length>1){
        		project.setId(idAndUserId[0]);

        	}
    	}*/
        TSUserDto userDto = userService.getUserByUserId(userId);
        if(StringUtil.isNotEmpty(userId)){
            type="productAdd";
        }
        try{
            CommonInitUtil.initGLObjectForCreate(project,userDto,orgId);
            initBusinessObject(project);
            sessionFacade.save(project);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        String projectId = project.getId();
        // 创建团队

        String teamId = addTeam(projectId);
        // 创建项目库
        RepLibraryDto repLibraryDto = new RepLibraryDto();
        repLibraryDto.setLibStatus(RepConstant.LIB_STATUS_WORKING);
        repLibraryDto.setTeamId(teamId);
        repLibraryDto.setTeamName(teamId);
        repLibraryDto.setLibName(projectId);
        repLibraryDto.setLibType(type);
        FeignJson repLibraryJson = repService.addRepLibrary(appKey,repLibraryDto);
        String libId = String.valueOf(repLibraryJson.getObj());
        // 保存项目，团队，项目关系表
        projRoleService.saveTeamLink(projectId, teamId, libId,userDto,orgId);

        projRoleService.addTeamRoleByCode(teamId, ProjectRoleConstants.PROJ_MANAGER);

        // 使用项目模板信息
        if (StringUtils.isNotBlank(project.getProjectTemplate())) {
            createProjectByTemplate(project.getId(), project.getProjectTemplate(), teamId,userId,orgId);
        }
        else {
            projLibService.cerateDefaultFolder(projectId, "",userId);
        }

        String[] managerIds = StringUtils.split(project.getProjectManagers(), ",");
        // 保存项目经理
        projRoleService.reSaveManager(managerIds, teamId,type);

        return project;

    }



    /**
     * Description: <br>
     * 将项目模板中内容复制至项目中
     *
     * @param projectId
     * @param templateId
     * @param teamId
     * @see
     */
    private void createProjectByTemplate(String projectId, String templateId, String teamId,String userId,String orgId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        if (StringUtils.isNotBlank(projectId) && StringUtils.isNotBlank(templateId)) {
            // 团队从模板复制到项目
            projTemplateHelper.saveProjRoles(projectId, templateId, teamId);
            // 计划从模板复制到项目
            projTemplateHelper.savePlans(projectId, templateId,userDto,orgId);
            // 项目库文件夹结构从模板复制到项目
            HashMap<String, String> idsMap = projTemplateHelper.saveProjLib(projectId, templateId,userId);
            // 复制项目库目录权限
            projTemplateService.saveRoleFileAuth2Project(projectId, templateId, idsMap,userDto,orgId);
        }
    }



    /**
     * Description: <br>
     *
     * @return
     * @see
     */
    private String getProjectListAuthorityAppendHql(String isTeamMember,String currentUserId) {
        StringBuffer hql = new StringBuffer();
        if("true".equalsIgnoreCase(isTeamMember)) {
            // 如果不是项目项目管理员，查出其所在的项目非拟制的项目  非拟制中筛选条件已经废弃
            /*hql.append(
                       " and "
                       + "(t.id in (select link.projectId  from FdTeamRoleUser u, Project p, ProjTeamLink link where "
                       + "link.teamId = u.teamId and p.id=link.projectId and u.userId = '"
                       + cruUserId
                       + "') or t.id in (select  distinct p.id from Project p, ProjTeamLink link ,FdTeamRoleGroup rg ,TSGroupUser gu where "
                       + "link.projectId = p.id and rg.teamId=link.teamId and gu.tsGroup.id=rg.groupId  and p.id = link.projectId  and gu.tsUser.id='"
                       + cruUserId + "' )"
                       //新增判断如果不是项目管理员  可以查询到自己创建的项目
                       +"or t.id in( select p.id from Project p where p.createBy='"+cruUserId+"')"
                       + ")");*/
            //性能优化(1w条数据32s左右)  --zsx
            // 如果不是项目项目管理员，查出其所在的项目非拟制的项目  非拟制中筛选条件已经废弃
           /* hql.append("and (t.createBy = '"+cruUserId+"' or t.id in (select link.projectId "
                + " from ProjTeamLink link where link.teamId in "
                + " (select u.teamId from FdTeamRoleUser u "
                + " where u.userId = '"+cruUserId+"') "
                + " or link.teamId in (select rg.teamId from FdTeamRoleGroup rg, TSGroupUser gu where 1 = 1 "
                + " and gu.tsGroup.id = rg.groupId and gu.tsUser.id = '"+cruUserId+"')))");*/

            //性能优化(1w条数据6s左右)  --zsx(由hql改为sql)
            // 如果不是项目项目管理员，查出其所在的项目非拟制的项目  非拟制中筛选条件已经废弃
            hql.append("and t.id in (select link.projectId from PM_PROJ_TEAM_LINK link "
                    + " where link.teamId in ((select u.teamId from fd_team_role_user u "
                    + " where u.userId = '"+currentUserId+"') union all (select rg.teamId "
                    + " from fd_team_role_group rg, t_s_group_user gu "
                    + " where 1 = 1 and gu.groupId = rg.groupId "
                    + " and gu.userId = '"+currentUserId+"')) union all  "
                    + " (select t.id  from PM_PROJECT t where  t.createBy = '"+currentUserId+"'))");

        }

        return hql.toString();
    }


    /**
     * Description: <br>
     *
     * @return
     * @see
     */
    private String getProjectListAuthorityAppendHql(String currentUserId) {
        StringBuffer hql = new StringBuffer();

        boolean isPMO = projRoleService.isSysRoleByUserIdAndRoleCode(currentUserId,
                ProjectRoleConstants.PMO);

        // 1.是否是令号项目经理组
        if (isPMO) {
            // hql.append(" and t.projectManagers like '%"+cruUserId+"%'");
            // 如果是项目管理员，查出拟制中和其所在的项目
            // hql.append(" and t.projectNumber in(select distinct p.projectNumber from
            // ProjRoleUsers u,
            // Project p where ((u.projectNumber=p.projectNumber and u.userId='"
            // + cruUserId +"') or(p.bizCurrent='" + ConfigTypeConstants.EDITING + "'))) ");
        }
        else {
            // 如果不是项目项目管理员，查出其所在的项目非拟制的项目  非拟制中筛选条件已经废弃
           /* hql.append(
                       " and "
                       + "(t.id in (select link.projectId  from FdTeamRoleUser u, Project p, ProjTeamLink link where "
                       + "link.teamId = u.teamId and p.id=link.projectId and u.userId = '"
                       + cruUserId
                       + "') or t.id in (select  distinct p.id from Project p, ProjTeamLink link ,FdTeamRoleGroup rg ,TSGroupUser gu where "
                       + "link.projectId = p.id and rg.teamId=link.teamId and gu.tsGroup.id=rg.groupId  and p.id = link.projectId  and gu.tsUser.id='"
                       + cruUserId + "' )"
                       //新增判断如果不是项目管理员  可以查询到自己创建的项目
                       +"or t.id in( select p.id from Project p where p.createBy='"+cruUserId+"')"
                       + ")");*/
            //性能优化(1w条数据32s左右)  --zsx
            // 如果不是项目项目管理员，查出其所在的项目非拟制的项目  非拟制中筛选条件已经废弃
           /* hql.append("and (t.createBy = '"+cruUserId+"' or t.id in (select link.projectId "
                + " from ProjTeamLink link where link.teamId in "
                + " (select u.teamId from FdTeamRoleUser u "
                + " where u.userId = '"+cruUserId+"') "
                + " or link.teamId in (select rg.teamId from FdTeamRoleGroup rg, TSGroupUser gu where 1 = 1 "
                + " and gu.tsGroup.id = rg.groupId and gu.tsUser.id = '"+cruUserId+"')))");*/

            //性能优化(1w条数据6s左右)  --zsx
            // 如果不是项目项目管理员，查出其所在的项目非拟制的项目  非拟制中筛选条件已经废弃
            hql.append("and t.id in (select link.projectId from PM_PROJ_TEAM_LINK link "
                    + " where link.teamId in ((select u.teamId from fd_team_role_user u "
                    + " where u.userId = '"+currentUserId+"') union all (select rg.teamId "
                    + " from fd_team_role_group rg, t_s_group_user gu "
                    + " where 1 = 1 and gu.groupId = rg.groupId "
                    + " and gu.userId = '"+currentUserId+"')) union all  "
                    + " (select t.id  from PM_PROJECT t where  t.createBy = '"+currentUserId+"'))");
        }
        return hql.toString();
    }


    //新增type属性  用来处理 kdd 产品复制成项目  项目经理参数传入问题   原因  由于webService 存表事务问题projRoleService.getRoleUsersByRoleCodeAndTeamId 导致方法查询团队中的角色信息查询不到 productAdd产品新增   productUpdate产品修改
    @Override
    public void upadteProjectManagerNames(String projectId,String type) {
        String teamId = projRoleService.getTeamIdByProjectId(projectId);
        // 更新pm_project表ProjectManagerNames字段
    //    List<FdTeamRoleUserDto> projRoleUsers = teamService.getRoleUsersByRoleCodeAndTeamId(appKey,ProjectRoleConstants.PROJ_MANAGER,teamId);

        List<FdTeamRoleGroupDto> roleGroups = projRolesServiceI.getFdTeamRoleGroupListByTeamIdAndRoleCode(teamId, ProjectRoleConstants.PROJ_MANAGER);


        List<TSUserDto> users = teamService.getSysUserListByTeamIdAndRoleCode(teamId,
                ProjectRoleConstants.PROJ_MANAGER);

        String userIdsStr = "";
        if(!CommonUtil.isEmpty(users)){
            for(TSUserDto user : users){
                if(CommonUtil.isEmpty(userIdsStr)){
                    userIdsStr = user.getId();
                }else{
                    userIdsStr = userIdsStr + "," +user.getId();
                }
            }
        }

        if(!CommonUtil.isEmpty(roleGroups)){
            for(FdTeamRoleGroupDto dto : roleGroups){
                List<TSUserDto> roleGroupUsers = userService.getSysUsersByGroupId(appKey,dto.getGroupId());
                if(!CommonUtil.isEmpty(roleGroupUsers)){
                    for(TSUserDto gUser : roleGroupUsers){
                        if(CommonUtil.isEmpty(userIdsStr)){
                            users.add(gUser);
                            userIdsStr = gUser.getId();
                        }else{
                            if(!userIdsStr.contains(gUser.getId())){
                                users.add(gUser);
                                userIdsStr = userIdsStr + "," +gUser.getId();
                            }
                        }

                    }
                }
            }
        }


        Map<String, TSUserDto> userMap = userService.getAllUserIdsMap();
        List<String> nameList = new ArrayList<String>();
        List<String> idList = new ArrayList<String>();
       /* for (FdTeamRoleUserDto u : projRoleUsers) {
            TSUserDto user = userMap.get(u.getUserId());
            if (null != user && !idList.contains(user.getId())) {
                idList.add(user.getId());
                nameList.add(user.getRealName() + "-" + user.getUserName());
            }
        }*/

        for(TSUserDto user : users){
            if (null != user && !idList.contains(user.getId())) {
                idList.add(user.getId());
                nameList.add(user.getRealName() + "-" + user.getUserName());
            }
        }

        if(StringUtil.isEmpty(type)){
            Project project = (Project)sessionFacade.getEntity(Project.class,projectId);
            project.setProjectManagers(StringUtils.join(idList, ","));
            project.setProjectManagerNames(StringUtils.join(nameList, ","));
            sessionFacade.updateEntitie(project);
            //判断是否是KDD与IDS关联的处理
            //TSRoleDto tSRoleList = roleService.getRoleByRoleCode(ProjectRoleConstants.PROJ_MANAGER);
          /*  List<TSRole> tSRoleList=sessionFacade.findHql("from TSRole where  roleCode = '"+ProjectRoleConstants.PROJ_MANAGER+"'");*//*
            if(!CommonUtil.isEmpty(tSRoleList)&&tSRoleList.getRoleName().equals("总体设计师")){
                //修改时候操作  项目的项目负责人修改之后相关的产品中的总体设计师也进行修改
                //保存产品的同时保存项目信息
                //webService调用相关的方法
               *//* ProductSupportImplService productSupportImplService=new ProductSupportImplService();
                //获取webService实例调用方法
                ProductSupport productSupport=productSupportImplService.getProductSupportImplPort();
                productSupport.updateKddProductTotalStylistByProjectId(project.getId(), project.getProjectManagers());*//*
            }

        }*/
        }
    }

    @Override
    public String addTeam(String projectId) {
        FdTeamDto fdTeam = new FdTeamDto();
        fdTeam.setTeamName(projectId);
        FeignJson teamJson = teamService.addFdTeam(fdTeam);
        return String.valueOf(teamJson.getObj());
    }

    @Override
    public Project getProjectEntity(String projectId) {
        Project project = (Project)sessionFacade.getEntity(Project.class,projectId);
        return project;
    }

    @Override
    public String getProjLogByProjectId(String projectId, int page, int rows,
                                        boolean isPage) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ProjLog t where 1=1 and t.projectId=? order by createtime desc");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(projectId);

        List<ProjLog> list = new ArrayList<ProjLog>();
        if (isPage) {
            list = sessionFacade.pageList(hql.toString(), paramList.toArray(), (page - 1) * rows, rows);
        }
        else {
            list = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        }
        Map<String,TSUserDto> userDtoMap = userService.getAllUserIdsMap();
        for (ProjLog log : list) {
            if (StringUtils.isNotEmpty(log.getCreateBy())){
                TSUserDto userDto = userDtoMap.get(log.getCreateBy());
                if (StringUtils.isNotEmpty(userDto.getUserName())){
                    log.setShowName(userDto.getRealName()+"-"+userDto.getUserName());
                }
            }
        }
        String json = JsonUtil.getListJsonWithoutQuote(list);
        return json;

    }

    @Override
    public List<ProjLog> getProjLogListByProjectId(String projectId, int page, int rows, boolean isPage) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ProjLog t where 1=1 and t.projectId=? order by createtime desc");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(projectId);

        List<ProjLog> list = new ArrayList<ProjLog>();
        if (isPage) {
            list = sessionFacade.pageList(hql.toString(), paramList.toArray(), (page - 1) * rows, rows);
        }
        else {
            list = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        }
        for (ProjLog log : list) {
            log.setShowName(UserUtil.getFormatUserNameId(log.getCreateBy()));
        }
        return list;
    }

    @Override
    public long getProjLogListCount(String projectNumber) {
        StringBuilder hql = new StringBuilder("");
        hql.append("select count(*) from ProjLog t where 1=1 and t.projectNumber=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(projectNumber);
        return sessionFacade.getCount(hql.toString(), paramList.toArray());
    }

    @Override
    public boolean isModifyForPlan(String projectId) {
        Project p = (Project)sessionFacade.getEntity(Project.class,projectId);
        if (p == null) {
            p = new Project();
        }

        if (ProjectConstants.PAUSED.equals(p.getBizCurrent())
                || ProjectConstants.CLOSED.equals(p.getBizCurrent())) {
            return false;
        }
        else {
            if (ProjectConstants.EDITING.equals(p.getBizCurrent())
                    && ProjectConstants.APPROVING.equals(p.getStatus())
                    && !ProjectConstants.REFUSED.equals(p.getIsRefuse())) {
                return false;
            }
            return true;
        }
    }


    @Override
    public String getLifeCycleStatusList() {
        Project p = new Project();
        initBusinessObject(p);
        List<LifeCycleStatus> lifeCycleStatus = p.getPolicy().getLifeCycleStatusList();
        List<LifeCycleStatus> orderList = new ArrayList<LifeCycleStatus>();
        for (int i = 0; i < lifeCycleStatus.size(); i++ ) {
            for (LifeCycleStatus status : lifeCycleStatus) {
                if (status.getOrderNum() == i) {
                    orderList.add(status);
                    break;
                }
            }
        }

        String aString = JsonUtil.getListJsonWithoutQuote(orderList);
        return aString;

    }

    @Override
    public List<Project> getProjectListForWeb(Project project, int page, int rows, boolean isPage) {
        return getProjectList(project, page, rows, isPage);
    }

    /**
     * Description: <br>
     *
     * @param project
     * @param page
     * @param rows
     * @param isPage
     * @return
     * @see
     */
    @SuppressWarnings("unchecked")
    private List<Project> getProjectList(Project project, int page, int rows, boolean isPage) {
        StringBuffer hql = new StringBuffer();
        hql.append("from Project t where t.bizCurrent = '" + ProjectConstants.STARTING + "'");

        Map<String, Object> queryMap = getQueryParam(hql, project);
        String hqlStr = (String)queryMap.get("hqlStr");
        List<Object> paramList = (List<Object>)queryMap.get("paramList");

        List<Project> list = new ArrayList<Project>();

        if (isPage) {
            list = sessionFacade.pageList(hqlStr, paramList.toArray(), (page - 1) * rows, rows);
        }
        else {
            list = sessionFacade.executeQuery(hqlStr, paramList.toArray());
        }

        return list;
    }

    /**
     * 组装查询方法
     * @param hql
     * @param project
     * @return
     */
    private Map<String, Object> getQueryParam(StringBuffer hql, Project project) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        hql.append(" and t.avaliable = '1'");

        List<Object> paramList = new ArrayList<Object>();
        if (project != null) {
            if (StringUtils.isNotEmpty(project.getProjectNumber())) {
                hql.append(" and t.projectNumber like ?");
                paramList.add('%' + project.getProjectNumber() + '%');
            }
            if (StringUtils.isNotEmpty(project.getName())) {
                hql.append(" and t.name like ?");
                paramList.add('%' + project.getName() + '%');
            }
            if (StringUtils.isNotEmpty(project.getBizCurrent())) {
                hql.append(" and t.bizCurrent = ?");
                paramList.add(project.getBizCurrent());
            }

            if (project.getQueryBefStartProjTime() != null) {
                hql.append(" and t.startProjectTime >= ?");
                paramList.add(project.getQueryBefStartProjTime());
            }

            if (project.getQueryAftStartProjTime() != null) {
                hql.append(" and t.startProjectTime <= ?");
                paramList.add(project.getQueryAftStartProjTime());
            }

            if (project.getQueryBefEndProjTime() != null) {
                hql.append(" and t.endProjectTime >= ?");
                paramList.add(project.getQueryBefEndProjTime());
            }

            if (project.getQueryAftEndProjTime() != null) {
                hql.append(" and t.endProjectTime <= ?");
                paramList.add(project.getQueryAftEndProjTime());
            }
        }
        hql.append(" order by t.createTime desc");
        resultMap.put("hqlStr", hql.toString());
        resultMap.put("paramList", paramList);

        return resultMap;
    }

    @Override
    public void submitProjectFlow(Project project, String approvePerson, String deptApprovePerson,
                                  String remark, String processDefinitionKey ,String userId) {
        String creator = userService.getUserByUserId(userId).getUserName();
        TSUserDto curUser = userService.getUserByUserId(userId);
        if (!CommonUtil.isEmpty(project.getProcInstId())) {
            List<TaskDto> nextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    project.getProcInstId(),creator);
            // 判断是否有流程（暂停或关闭流程），如有：结束流程
            if (!CommonUtil.isEmpty(nextTasks)) {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                        project.getId(), project.getProcInstId(), "delete");
            }
        }

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", creator);
        variables.put("leader", approvePerson);
        variables.put("deptLeader", deptApprovePerson);
        variables.put("oid", BpmnConstants.OID_PROJECT + project.getId());
        variables.put("currentUserId", userId);


        if (BpmnConstants.BPMN_START_PROJECT.equals(processDefinitionKey)) {

            String feignUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projectEndListenerRestController/notify.do";
            variables.put("feignStartProListener", feignUrl);
            variables.put("feignProRefuseListener","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projectRefuseListenerRestController/notify.do");


            variables.put("cancelEventListener",
                    "com.glaway.foundation.activiti.task.common.CommonCancelListener");
            variables.put(
                    "editUrl",
                    "/ids-pm-web/projectController.do?goUpdate&dataHeight=600&dataWidth=1250&id="
                            + project.getId() + "&oper=startProject");
            variables.put(
                    "viewUrl",
                    "/ids-pm-web/projectController.do?goUpdate&dataHeight=600&dataWidth=1250&id="
                            + project.getId() + "&oper=startProject&isView=true");
        }
        else if (BpmnConstants.BPMN_PAUSE_PROJECT.equals(processDefinitionKey)) {

            String feignUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projectPauseEndListenerRestController/notify.do";
            variables.put("feignProPauseListener", feignUrl);
            variables.put("feignProPauseRefuseListener","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projectPauseRefuseListenerRestController/notify.do");


            variables.put("cancelEventListener",
                    "com.glaway.foundation.activiti.task.common.CommonCancelListener");
            variables.put(
                    "editUrl",
                    "/ids-pm-web/projectController.do?goUpdate&dataHeight=600&dataWidth=1250&id="
                            + project.getId() + "&oper=pauseProject");
            variables.put(
                    "viewUrl",
                    "/ids-pm-web/projectController.do?goUpdate&dataHeight=600&dataWidth=1250&id="
                            + project.getId() + "&oper=pauseProject&isView=true&viewPlan=view");
        }
        else if (BpmnConstants.BPMN_RESUME_PROJECT.equals(processDefinitionKey)) {

            String feignUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projectResumeEndListenerRestController/notify.do";
            variables.put("feignProResumeListener", feignUrl);
            variables.put("feignProResumeRefuseListener","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projectResumeRefuseListenerRestController/notify.do");

            variables.put("cancelEventListener",
                    "com.glaway.foundation.activiti.task.common.CommonCancelListener");
            variables.put(
                    "editUrl",
                    "/ids-pm-web/projectController.do?goUpdate&dataHeight=600&dataWidth=1250&id="
                            + project.getId() + "&oper=resumeProject");
            variables.put(
                    "viewUrl",
                    "/ids-pm-web/projectController.do?goUpdate&dataHeight=600&dataWidth=1250&id="
                            + project.getId() + "&oper=resumeProject&isView=true&viewPlan=view");
        }
        else if (BpmnConstants.BPMN_CLOSE_PROJECT.equals(processDefinitionKey)) {

            String feignUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projectCloseEndListenerRestController/notify.do";
            variables.put("feignProCloseListener", feignUrl);
            variables.put("feignProCloseRefuseListener","http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projectCloseRefuseListenerRestController/notify.do");


            variables.put("cancelEventListener",
                    "com.glaway.foundation.activiti.task.common.CommonCancelListener");
            variables.put(
                    "editUrl",
                    "/ids-pm-web/projectController.do?goUpdate&dataHeight=600&dataWidth=1250&id="
                            + project.getId() + "&oper=closeProject");
            variables.put(
                    "viewUrl",
                    "/ids-pm-web/projectController.do?goUpdate&dataHeight=600&dataWidth=1250&id="
                            + project.getId() + "&oper=closeProject&isView=true&viewPlan=view");
        }

        FeignJson j = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                processDefinitionKey, project.getId(), variables);
        String procInstId = j.getObj().toString(); // 流程实例ID
        List<TaskDto> tasks = null;
        try {
            tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    procInstId, creator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String taskId = tasks.get(0).getId();

        List<String> comments = new ArrayList<String>();
        comments.add(remark);
        Map<String,Object> params = new HashMap<>();
        params.put("comments",comments);
        params.put("variables",variables);
        params.put("userId",userId);

        workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, params);

        FeignJson nextTask = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = nextTask.getObj().toString();

        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
        myStartedTask.setTaskNumber(project.getId());
        myStartedTask.setType(processDefinitionKey);
        myStartedTask.setCreater(creator);
        myStartedTask.setCreaterFullname(curUser.getRealName());
        myStartedTask.setStartTime(new Date());
        myStartedTask.setProcType(processDefinitionKey);
        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        // 流程已发起列表中：增加对象名称的记录，流程任务名称规范化
        myStartedTask.setObjectName(project.getName());
        if (BpmnConstants.BPMN_START_PROJECT.equals(processDefinitionKey)) {
            myStartedTask.setTitle(ProcessUtil.getProcessTitle(project.getName(),
                    BpmnConstants.BPMN_START_PROJECT_DISPLAYNAME, procInstId));
        }
        else if (BpmnConstants.BPMN_PAUSE_PROJECT.equals(processDefinitionKey)) {
            myStartedTask.setTitle(ProcessUtil.getProcessTitle(project.getName(),
                    BpmnConstants.BPMN_PAUSE_PROJECT_DISPLAYNAME, procInstId));
        }
        else if (BpmnConstants.BPMN_RESUME_PROJECT.equals(processDefinitionKey)) {
            myStartedTask.setTitle(ProcessUtil.getProcessTitle(project.getName(),
                    BpmnConstants.BPMN_RESUME_PROJECT_DISPLAYNAME, procInstId));
        }
        else if (BpmnConstants.BPMN_CLOSE_PROJECT.equals(processDefinitionKey)) {
            myStartedTask.setTitle(ProcessUtil.getProcessTitle(project.getName(),
                    BpmnConstants.BPMN_CLOSE_PROJECT_DISPLAYNAME, procInstId));
        }
        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

        project.setProcInstId(procInstId);
        project.setStatus(ProjectConstants.APPROVING);
        project.setIsRefuse(ProjectConstants.NORMAL);
        sessionFacade.updateEntitie(project);
    }

    @Override
    public boolean submitProjectFlowAgain(Project project, String approvePerson,
                                          String deptApprovePerson, String remark, String userId) {
        String creator = userService.getUserByUserId(userId).getUserName();
        String taskId = "";
        FeignJson taskIdFj = workFlowFacade.getWorkFlowTodoTaskService() .getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                project.getProcInstId(), creator);

        if (taskIdFj.isSuccess()) {
            taskId = taskIdFj.getObj() == null ? "" : taskIdFj.getObj().toString();
        }

        // 判断当前用户是否有流程（对应项目的启动、暂停、恢复、关闭流程），如有：重新提交启动流程
        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);
            variables.put("leader", approvePerson);
            variables.put("deptLeader", deptApprovePerson);

            List<String> comments = new ArrayList<String>();
            comments.add(remark);
            Map<String,Object> params = new HashMap<>();
            params.put("comments",comments);
            params.put("variables",variables);
            params.put("userId",userId);

            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, params);

            FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    project.getProcInstId());
            String status = newNextTasks.getObj().toString();
            // 更新我的已发起流程状态
            updateMyStartedTaskStatus(status, project);

            // 更新项目状态
            project.setStatus(ProjectConstants.APPROVING);
            project.setIsRefuse(ProjectConstants.NORMAL);
            sessionFacade.updateEntitie(project);

            return true;
        }
        return false;
    }

    /**
     * 更新我的已发起流程状态
     *
     * @param status
     *            状态
     * @param project
     *            项目
     * @return
     * @see
     */
    private void updateMyStartedTaskStatus(String status, Project project) {
        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                project.getId(), project.getProcInstId());
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
    }

    @Override
    public void updateProjectBizCurrent(String projectId, String businessType, String leader) {
        Project project = (Project) sessionFacade.getEntity(Project.class, projectId);
        TSUserDto userDto = userService.getUserByUserName(leader);
        if (ProjectStatusConstants.PROJECT_STATUS_CHANGE_START.equals(businessType)) {
            forwardBusinessObject(project);
            project.setStatus(ProjectConstants.APPROVED);
            sessionFacade.updateEntitie(project);
            ProjLog projLog = new ProjLog();
            String message = "项目启动流程审批通过";
            projLog.setProjectId(projectId);
            projLog.setProjectNumber(project.getProjectNumber());
            projLog.setLogInfo(message);
            projLog.setCreateBy(userDto.getId());
            projLog.setCreateTime(new Date());
            projLog.setCreateUserName(userDto.getRealName() + "-" + userDto.getUserName());
            projLogService.initBusinessObject(projLog);
            projLogService.save(projLog);
        }
        else if (ProjectStatusConstants.PROJECT_STATUS_CHANGE_PAUSE.equals(businessType)) {
            project = (Project) sessionFacade.getEntity(Project.class, projectId);
            // 暂停计划
            planService.setPlanProjectStatus(project, ProjectStatusConstants.PAUSE);
            forwardBusinessObject(project);
            project.setStatus(ProjectConstants.APPROVED);
            project.setPauseTime(new Date());
            sessionFacade.updateEntitie(project);

            // 暂停文件审批
            projLogService.setLibProjectStatus(project, ProjectStatusConstants.PAUSE);
            ProjLog projLog = new ProjLog();
            String message = "项目暂停流程审批通过";
            projLog.setProjectId(projectId);
            projLog.setProjectNumber(project.getProjectNumber());
            projLog.setLogInfo(message);
            projLog.setCreateBy(userDto.getId());
            projLog.setCreateTime(new Date());
            projLog.setCreateUserName(userDto.getRealName() + "-" + userDto.getUserName());
            projLogService.initBusinessObject(projLog);
            projLogService.save(projLog);
        }
        else if (ProjectStatusConstants.PROJECT_STATUS_CHANGE_RESUME.equals(businessType)) {
            // 恢复计划
            planService.setPlanProjectStatus(project, ProjectStatusConstants.RECOVER);
            backwardBusinessObject(project);
            project.setStatus(ProjectConstants.APPROVED);
            sessionFacade.updateEntitie(project);
            // 恢复文件流程
            projLogService.setLibProjectStatus(project, ProjectStatusConstants.RECOVER);
            ProjLog projLog = new ProjLog();
            String message = "项目恢复流程审批通过";
            projLog.setProjectId(projectId);
            projLog.setProjectNumber(project.getProjectNumber());
            projLog.setLogInfo(message);
            projLog.setCreateBy(userDto.getId());
            projLog.setCreateTime(new Date());
            projLog.setCreateUserName(userDto.getRealName() + "-" + userDto.getUserName());
            projLogService.initBusinessObject(projLog);
            projLogService.save(projLog);
        }
        else if (ProjectStatusConstants.PROJECT_STATUS_CHANGE_CLOSE.equals(businessType)) {
            project = (Project) sessionFacade.getEntity(Project.class, projectId);
            // 关闭计划
            planService.setPlanProjectStatus(project, ProjectStatusConstants.CLOSE);
            forwardBusinessObjectByStep(project, 2);
            project.setStatus(ProjectConstants.APPROVED);
            project.setCloseTime(new Date());
            sessionFacade.updateEntitie(project);
            // 终止文件审批流程
            projLogService.setLibProjectStatus(project, ProjectStatusConstants.CLOSE);
            ProjLog projLog = new ProjLog();
            String message = "项目关闭流程审批通过";
            projLog.setProjectId(projectId);
            projLog.setProjectNumber(project.getProjectNumber());
            projLog.setLogInfo(message);
            projLog.setCreateBy(userDto.getId());
            projLog.setCreateTime(new Date());
            projLog.setCreateUserName(userDto.getRealName() + "-" + userDto.getUserName());
            projLogService.initBusinessObject(projLog);
            projLogService.save(projLog);
        }
    }

    @Override
    public FeignJson doProjectUpdate(Project project, String pagetype, String isUpdate, String userId) {
        FeignJson j = new FeignJson();
        j.setSuccess(false);
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.updateSuccess");

        try {

            Project t = getProjectEntity(project.getId());
            if (StringUtils.isNotEmpty(t.getBizCurrent())
                    && ProjectConstants.PAUSED.equals(t.getBizCurrent())) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.cannotUpdateEditing");
                return j;
            }
            if (StringUtils.isNotEmpty(t.getBizCurrent())
                    && ProjectConstants.CLOSED.equals(t.getBizCurrent())) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.cannotUpdateClosed");
                return j;
            }
            if (ProjectConstants.APPROVING.equals(t.getStatus())
                    && !ProjectConstants.REFUSED.equals(t.getIsRefuse())) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.cannotUpdateRevising");
                return j;
            }

            Date minDate = planService.getMinPlanStartTimeByProject(t);
            Date maxDate = planService.getMaxPlanEndTimeByProject(t);
            Date startProjectTime = project.getStartProjectTime();
            Date endProjectTime = project.getEndProjectTime();
            if (minDate != null) {
                if (startProjectTime.after(minDate)) {
                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.startMustEarlierThanPlanStart");
                    return j;
                }
            }
            if (maxDate != null) {
                if (endProjectTime.before(maxDate)) {
                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.endMustLaterThanPlanEnd");
                    return j;
                }
            }

            project.setEps(project.getEpsName());
            project.setBizCurrent(t.getBizCurrent());

            if(ProjectConstants.STARTING.equals(t.getBizCurrent()) && !"info".equals(pagetype) && !"true".equals(isUpdate)) {
                project.setProjectManagers(t.getProjectManagers());
            }
            BeanUtil.copyBeanNotNull2Bean(project, t);

            Map<String,TSUserDto> userMap = userService.getCommonUserAll();
            String managerNames = "";
            if(!CommonUtil.isEmpty(t.getProjectManagers())){
                for(String managerId : t.getProjectManagers().split(",")){
                    TSUserDto user = userMap.get(managerId);
                    if(CommonUtil.isEmpty(managerNames)){
                        managerNames = user.getRealName()+"-"+user.getUserName();
                    }else{
                        managerNames = managerNames + ","+user.getRealName()+"-"+user.getUserName();
                    }
                }
            }
            t.setProjectManagerNames(managerNames);

            // TODO 为什么存在分号
            String[] managerIds = null;
            if (project.getProjectManagers() != null) {
                if (project.getProjectManagers().indexOf(",") != -1) {
                    managerIds = StringUtils.split(project.getProjectManagers(), ",");
                }
                else {
                    managerIds = StringUtils.split(project.getProjectManagers(), ";");
                }
            }

            ProjTeamLink projTeamLink = new ProjTeamLink();
            projTeamLink.setProjectId(t.getId());
            List<ProjTeamLink> links = projRoleService.searchProjTeamLink(projTeamLink);
            String teamId = "";
            if (!CommonUtil.isEmpty(links)) {
                ProjTeamLink p = links.get(0);
                teamId = p.getTeamId();
            }

            // 重新保存项目及项目经理
            updateProject(managerIds, teamId, t,userId);
            projLogService.saveProjLog(t.getProjectNumber(),t.getId(), message, "",userService.getUserByUserId(userId));
            j.setSuccess(true);
            j.setObj("true");
            log.info(message, project.getId(), project.getId().toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.updateFailure");
            j.setSuccess(false);
            log.error(message, e, project.getId(), project.getId().toString());
            Object[] params = new Object[] {message, project.getId().toString()};// 异常原因：{0}；详细信息：{1}
            throw new GWException(GWConstants.ERROR_2003, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public void updateProject(String[] managerIds, String teamId, Project project, String userId) {
        // 重新保存项目经理
        TSUserDto curUser = userService.getUserByUserId(userId);
        projRoleService.reSaveManager(managerIds, teamId,null);
        project.setCreateName(curUser.getRealName()+"-"+curUser.getUserName());
        sessionFacade.updateEntitie(project);
        String libId = getLibIdByProjectId(project.getId());
        updateProjectLibName(libId, project.getName() + "-" + project.getProjectNumber());
        if(!CommonUtil.isEmpty(project.getProcInstId()) && "1".equals(project.getStatus())){
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                    project.getId(), project.getProcInstId());
            /*ProcessDefinition processDefinition = workFlowFacade.getWorkFlowCommonService().getProcessDefinitionByProcInstId(
                    project.getProcInstId(), false);*/

            FeignJson PDNmae = workFlowFacade.getWorkFlowCommonService().getProcessDefinitionJsonMapByProcInstId(
                    project.getProcInstId(), false);
            Map<String, Object> attributes = new HashMap<>();

           FeignJson fj = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    project.getProcInstId());
           String status = fj.getObj().toString();
            if (PDNmae.isSuccess()) {
                attributes = PDNmae.getAttributes();
                String bpmnDisplayName = attributes.get("name") == null ? "" : attributes.get("name").toString();
                myStartedTask.setObjectName(project.getName());
                myStartedTask.setTitle(ProcessUtil.getProcessTitle(project.getName(), bpmnDisplayName,
                        project.getProcInstId()));
                myStartedTask.setStatus(status);
                workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
            }

        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateProjectLibName(String libId, String fileName) {
        /*StringBuilder hql = new StringBuilder("");
        hql.append("from RepFile t where 1=1 and t.libId=? and parentId=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(libId);
        paramList.add(libId);
        List<RepFile> repFileList = sessionFacade.executeQuery(hql.toString(), paramList.toArray());*/
        List<RepFileDto> repFileList = repService.getRepFileByLibIdAndParentId(appKey,libId,libId);
        RepFileDto r = repFileList.get(0);
        RepFileDto file = repService.getRepFileByRepFileId(appKey,r.getId());
        file.setFileName(fileName);
        repService.updateRepFileById(appKey,file);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getLibIdByProjectId(String projectId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ProjTeamLink t where 1=1 and t.projectId=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(projectId);
        List<ProjTeamLink> projTeamLinkList = sessionFacade.executeQuery(hql.toString(),
                paramList.toArray());
        ProjTeamLink p = projTeamLinkList.get(0);
        return p.getLibId();
    }

    @Override
    public void update(Project project) {
        sessionFacade.updateEntitie(project);
    }

    @Override
    public FeignJson doStartProject(Map<String, Object> params) {

        FeignJson j = new FeignJson();
        j.setSuccess(false);

        ProjectDto dto = new ObjectMapper().convertValue(params.get("dto"),ProjectDto.class);
        Project project = new Project();
        Dto2Entity.copyProperties(dto,project);
        String approvePerson = params.get("approvePerson").toString();
        String deptApprovePerson = params.get("deptApprovePerson").toString();
        String remark = params.get("remark").toString();
        String userId = params.get("userId").toString();

        String message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.sumbitSuccess");

        Project t = getProjectEntity(project.getId());
        String lifeStatus = t.getBizCurrent();


        try {
            if (org.apache.commons.lang3.StringUtils.isEmpty(lifeStatus) || !ProjectConstants.EDITING.equals(lifeStatus)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.lifeStatusError");
                return j;
            }
            if (ProjectConstants.APPROVING.equals(t.getStatus())
                    && !ProjectConstants.REFUSED.equals(t.getIsRefuse())) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.cannotRepeatSubmit");
                return j;
            }

            FdTeamRoleDto listTeamUser = teamService.getFdTeamRoleByTeamIdAndRoleCode(projRoleService.getTeamIdByProjectId(t.getId()),ProjectRoleConstants.PROJ_MANAGER);
            if (CommonUtil.isEmpty(listTeamUser)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.atLeastAddOneManager");
                return j;
            }

            if (StringUtil.isNotEmpty(t.getProcInstId())) { // 如果存在流程实例（驳回状态）：重新提交该启动流程
                boolean result = submitProjectFlowAgain(t, approvePerson, deptApprovePerson, remark,userId);
                if (!result) {
                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.sumbitFailure");
                    return j;
                }
            }
            else { // 不存在流程：直接发起新的启动流程
                submitProjectFlow(t, approvePerson, deptApprovePerson, remark,
                        BpmnConstants.BPMN_START_PROJECT,userId);
            }
            TSUserDto userDto = userService.getUserByUserId(userId);
            projLogService.saveProjLog(t.getProjectNumber(),t.getId(), message, remark , userDto);
            j.setSuccess(true);
            log.info(message, t.getId(), t.getId().toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.sumbitFailure");
            log.error(message, e, t.getId(), t.getId().toString());
            Object[] objs = new Object[] {message, t.getId().toString()};// 异常原因：{0}；详细信息：{1}
            TSUserDto userDto = userService.getUserByUserId(userId);
            projLogService.saveProjLog(t.getProjectNumber(),t.getId(), message, remark,userDto);
            throw new GWException(GWConstants.ERROR_2003, objs, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson doPauseOrResumeProject(Map<String, Object> params) {
        ProjectDto dto = new ObjectMapper().convertValue(params.get("project"),ProjectDto.class);
        String operation = params.get("operation") == null ? "" : params.get("operation").toString();
        String approvePerson = params.get("approvePerson") == null ? "" : params.get("approvePerson").toString();
        String deptApprovePerson = params.get("deptApprovePerson") == null ? "" : params.get("deptApprovePerson").toString();
        String remark = params.get("remark") == null ? "" : params.get("remark").toString();
        String userId = params.get("userId") == null ? "" : params.get("userId").toString();

        FeignJson j = new FeignJson();
        j.setSuccess(false);
        String processDefinitionKey = "";
        String message = "";

        Project t = getProjectEntity(dto.getId());
        String lifeStatus = t.getBizCurrent();


        try {
            if (StringUtils.isEmpty(lifeStatus)
                    || (ProjectConstants.PAUSE.equals(operation) && !ProjectConstants.STARTING.equals(lifeStatus))) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.lifeStatusError");
                return j;
            }
            else if (StringUtils.isEmpty(lifeStatus)
                    || (ProjectConstants.RESUME.equals(operation) && !ProjectConstants.PAUSED.equals(lifeStatus))) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.lifeStatusError");
                return j;
            }
            if (ProjectConstants.APPROVING.equals(t.getStatus())
                    && !ProjectConstants.REFUSED.equals(t.getIsRefuse())) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.cannotRepeatSubmit");
                return j;
            }

            if (StringUtils.isNotEmpty(lifeStatus)) {
                if (ProjectConstants.STARTING.equals(lifeStatus)) {
                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.pauseSuccess");
                    processDefinitionKey = BpmnConstants.BPMN_PAUSE_PROJECT;
                }
                else if (ProjectConstants.PAUSED.equals(lifeStatus)) {
                    message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.resumeSuccess");
                    processDefinitionKey = BpmnConstants.BPMN_RESUME_PROJECT;
                }
            }

            String nextTaskId = "";
            if (t.getProcInstId() != null) {
                FeignJson fJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                        t.getProcInstId());
                nextTaskId = fJson.getObj() == null ? "" : fJson.getObj().toString();
            }
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                    t.getId(), t.getProcInstId());

            if (!CommonUtil.isEmpty(nextTaskId)) { // 存在流程实例（驳回状态）
                if (processDefinitionKey.equals(myStartedTask.getType())) { // 流程是暂停或恢复流程：重新提交该流程
                    boolean result = submitProjectFlowAgain(t, approvePerson,
                            deptApprovePerson, remark,userId);
                    if (!result) {
                        if (ProjectConstants.STARTING.equals(lifeStatus)) {
                            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.pauseFailure");
                        }
                        else if (ProjectConstants.PAUSED.equals(lifeStatus)) {
                            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.resumeFailure");
                        }
                        return j;
                    }
                }
                else { // 流程不是暂停或恢复流程：终止已有的流程，并发起新的流程
                    submitProjectFlow(t, approvePerson, deptApprovePerson, remark,
                            processDefinitionKey,userId);
                }
            }
            else { // 不存在流程：直接发起新的暂停或恢复流程
                submitProjectFlow(t, approvePerson, deptApprovePerson, remark,
                        processDefinitionKey,userId);
            }
            TSUserDto userDto = userService.getUserByUserId(userId);
            projLogService.saveProjLog(t.getProjectNumber(),t.getId(), message, remark,userDto);
            j.setSuccess(true);
            log.info(message, t.getId(), t.getId().toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            if (ProjectConstants.STARTING.equals(lifeStatus)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.pauseFailure");
            }
            else if (ProjectConstants.PAUSED.equals(lifeStatus)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.resumeFailure");
            }
            log.error(message, e, t.getId(), t.getId().toString());
            Object[] objs = new Object[] {message, t.getId().toString()};// 异常原因：{0}；详细信息：{1}
            TSUserDto userDto = userService.getUserByUserId(userId);
            projLogService.saveProjLog(t.getProjectNumber(),t.getId(), message, remark,userDto);
            throw new GWException(GWConstants.ERROR_2003, objs, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson doCloseProject(Map<String, Object> params) {

        ProjectDto dto = new ObjectMapper().convertValue(params.get("dto"),ProjectDto.class);
        String approvePerson = params.get("approvePerson") == null ? "" : params.get("approvePerson").toString();
        String deptApprovePerson = params.get("deptApprovePerson") == null ? "" : params.get("deptApprovePerson").toString();
        String remark = params.get("remark") == null ? "" : params.get("remark").toString();
        String userId = params.get("userId") == null ? "" : params.get("userId").toString();

        FeignJson j = new FeignJson();
        j.setSuccess(false);
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.sumbitCloseSuccess");

        Project t = getProjectEntity(dto.getId());
        String lifeStatus = t.getBizCurrent();


        try {
            if (StringUtils.isEmpty(lifeStatus) || !ProjectConstants.STARTING.equals(lifeStatus)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.lifeStatusError");
                return j;
            }
            if (ProjectConstants.APPROVING.equals(t.getStatus())
                    && !ProjectConstants.REFUSED.equals(t.getIsRefuse())) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.cannotRepeatSubmit");
                return j;
            }

            String nextTaskId = "";
            FeignJson fJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                    t.getProcInstId());
            if (fJson.isSuccess()) {
                nextTaskId = fJson.getObj() == null ? "" : fJson.getObj().toString();
            }
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                    t.getId(), t.getProcInstId());

            if (StringUtils.isNotBlank(nextTaskId)) { // 存在流程实例（驳回状态）
                if (BpmnConstants.BPMN_CLOSE_PROJECT.equals(myStartedTask.getType())) { // 流程是关闭流程：重新提交该关闭流程
                    boolean result = submitProjectFlowAgain(t, approvePerson,
                            deptApprovePerson, remark,userId);
                    if (!result) {
                        message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.sumbitCloseFailure");
                        return j;
                    }
                }
                else { // 流程不是关闭流程：终止已有的流程，并发起新的关闭流程
                    submitProjectFlow(t, approvePerson, deptApprovePerson, remark,
                            BpmnConstants.BPMN_CLOSE_PROJECT,userId);
                }
            }
            else { // 不存在流程：直接发起新的关闭流程
                submitProjectFlow(t, approvePerson, deptApprovePerson, remark,
                        BpmnConstants.BPMN_CLOSE_PROJECT,userId);
            }

            TSUserDto curUser = userService.getUserByUserId(userId);
            projLogService.saveProjLog(t.getProjectNumber(),t.getId(), message, remark,curUser);
            j.setSuccess(true);
            log.info(message, t.getId(), t.getId().toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.sumbitCloseFailure");
            log.error(message, e, t.getId(), t.getId().toString());
            Object[] objects = new Object[] {message, t.getId().toString()};// 异常原因：{0}；详细信息：{1}
            TSUserDto curUser = userService.getUserByUserId(userId);
            projLogService.saveProjLog(t.getProjectNumber(),t.getId(), message, remark,curUser);
            throw new GWException(GWConstants.ERROR_2003, objects, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson judgeIsTeamManager(String projectId, String userId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try{

            boolean isTeamManager = projRoleService.isProjRoleByUserIdAndRoleCode(projectId,ProjectRoleConstants.PROJ_MANAGER,userId);
            if(!isTeamManager){
                j.setSuccess(false);
            }
        }catch(Exception e){
            j.setSuccess(false);
        }finally{
            return j;
        }
    }

    @Override
    public List<Plan> getOneLevelPlanByProject(String projectId) {
        Project project = getProjectEntity(projectId);
        List<Plan> planList =  planService.getOneLevelPlanByProject(project);
        return planList;
    }

    @Override
    public void batchDeleteProject(List<Project> delList, String ids,String userId) {
        for (Project p : delList) {
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                    p.getId());
            if (StringUtils.isNotBlank(myStartedTask.getProcInstId())) {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(p.getId(),
                        myStartedTask.getProcInstId(), "delete");
            }

            String teamId = projRoleService.getTeamIdByProjectId(p.getId());
            if (StringUtils.isNotBlank(teamId)) {
                teamService.deleteFdTeamRoleListByTeamId(appKey,teamId);
                teamService.deleteFdTeamRoleUserListByTeamId(appKey,teamId);
            }
            String userID="";
            if(StringUtil.isNotEmpty(userId)){
                userID=userId;
            }
            String[] split;
            String projid = "";
            String proj = getProj(userID);
            if (StringUtil.isNotEmpty(proj)) {
                split = proj.split(",");
                for (int i = 0; i < split.length; i++ ) {
                    if (split[i].equals(ids)) {

                    }
                    else {
                        projid = projid + split[i];
                        if (StringUtil.isNotEmpty(split[i])) {
                            projid = projid + ",";
                        }
                    }
                }

                String projids = projid.substring(0, projid.length() - 1);
                getProjWarn(projids, userID);
            }

            String[] splits;
            String proid = "";
            String projWarm = getProjWarm(userID);
            if (StringUtil.isNotEmpty(projWarm)) {
                splits = projWarm.split(",");
                for (String s : splits) {
                    if (s.equals(ids)) {}
                    else {
                        proid = proid + s;
                        proid = proid + ",";
                    }
                }
                saveIds(proid.substring(0, proid.length() - 1), userID);
            }
            if(StringUtil.isNotEmpty(userId)){
                recentlyProjectService.deleteRecentlyByProjectIdAndUserId(p.getId(), userId);
            }else{
                recentlyProjectService.deleteRecentlyByProjectIdAndUserId(p.getId(), null);
            }

            planService.deletePhysicsPlansByProjectId(p.getId());
            sessionFacade.delete(p);
            projLogService.deleteProjLog(p.getProjectNumber());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getProj(String id) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ProjWarn t where 1=1 and flag='1' and t.userName=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(id);
        List<ProjWarn> projects = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        if (projects.size() != 0) {
            ProjWarn pj = projects.get(0);
            String ss = pj.getProjectNum();
            return ss;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getProjWarm(String id) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ProjWarn t where 1=1 and flag='0' and t.userName=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(id);
        List<ProjWarn> projects = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        if (projects.size() != 0) {
            ProjWarn pj = projects.get(0);
            String ss = pj.getProjectNum();
            return ss;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getProjWarn(String ids, String id) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ProjWarn t where 1=1 and flag='1' and t.userName=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(id);
        List<ProjWarn> projects = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        ProjWarn ss = projects.get(0);
        ss.setProjectNum(ids);
        sessionFacade.saveOrUpdate(ss);
    }

    /**
     * Description: <br>
     * 储存文件ID,流程ID
     *
     * @param ids
     * @param id
     * @see
     */
    @SuppressWarnings("unchecked")
    private void saveIds(String ids, String id) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ProjWarn t where 1=1 and flag='0' and t.userName=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(id);
        List<ProjWarn> projects = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        ProjWarn ss = projects.get(0);
        ss.setProjectNum(ids);
        sessionFacade.saveOrUpdate(ss);
    }

    @Override
    public FeignJson resubmitProjectFlow(Project project, String oper, String id, String userId) {
        FeignJson j = new FeignJson();
        TSUserDto curUser = userService.getUserByUserId(userId);
        String message = "";
        try {
            if ("startProject".equals(oper)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.reStartSuccess");
            }
            else if ("pauseProject".equals(oper)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.rePauseSuccess");
            }
            else if ("resumeProject".equals(oper)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.reResumeSuccess");
            }
            else if ("closeProject".equals(oper)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.reSubmitCloseSuccess");
            }


            Project t = getProjectEntity(id);
            String taskId = "";
            FeignJson fj = workFlowFacade.getWorkFlowTodoTaskService().getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,t.getProcInstId(), curUser.getUserName());
            if (fj.isSuccess()) {
                taskId = fj.getObj() == null ? "" : fj.getObj().toString();
            }
            resubmitProjectFlow(t, taskId, userId);
            projLogService.saveProjLog(t.getProjectNumber(),t.getId(), message, "",curUser);
            j.setSuccess(true);
            log.info(message, project.getId(), "");
        }
        catch (Exception e) {
            if ("startProject".equals(oper)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.reStartFailure");
            }
            else if ("pauseProject".equals(oper)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.rePauseFailure");
            }
            else if ("resumeProject".equals(oper)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.reResumeFailure");
            }
            else if ("closeProject".equals(oper)) {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.projectmanager.reSubmitCloseFailure");
            }
            j.setSuccess(false);
            log.error(message, e, project.getId(), "");
            Object[] params = new Object[] {message,
                    Project.class.getClass() + " oids:" + project.getId()};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2005, params, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public void resubmitProjectFlow(Project project, String taskId, String userId) {
        // 执行流程
        Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                taskId);
        variables.put("userId",userId);
        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

        String procInstId = project.getProcInstId();
        FeignJson fj = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = "";
        if (fj.isSuccess()) {
            status = fj.getObj() == null ? "" : fj.getObj().toString();
        }
        // 更新我的已发起流程状态
        updateMyStartedTaskStatus(status, project);

        // 更新项目状态
        project.setStatus(ProjectConstants.APPROVING);
        project.setIsRefuse(ProjectConstants.NORMAL);
        update(project);
    }

    @Override
    public int getMaxPlanNumber(Plan plan) {
        StringBuilder hql = new StringBuilder("");
        hql.append("select max(t.planNumber) from Plan t");
        hql.append(" where 1 = 1");

        List<Object> paramList = new ArrayList<Object>();
        if (plan != null) {
            if (StringUtils.isNotEmpty(plan.getProjectId())) {
                hql.append(" and t.projectId = ?");
                paramList.add(plan.getProjectId());
            }
        }
        List<Object> list = sessionFacade.executeQuery(hql.toString(), paramList.toArray());

        if (!CommonUtil.isEmpty(list)) {
            int i = 0;
            if (list.get(0) != null) {
                i = (int)list.get(0);
            }
            return i;
        }
        else {
            return 0;
        }
    }

    @Override
    public int getMaxStoreyNo(Plan plan) {
        StringBuilder hql = new StringBuilder("");
        hql.append("select max(storeyNo) from Plan t");
        hql.append(" where 1 = 1");

        List<Object> paramList = new ArrayList<Object>();
        if (plan != null) {
            if (StringUtils.isNotEmpty(plan.getProjectId())) {
                hql.append(" and t.projectId = '" + plan.getProjectId() + "'");
            }
            if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
                hql.append(" and t.parentPlanId = '" + plan.getParentPlanId()
                        + "' order by storeyNo");
            }
            else {
                hql.append(" and t.parentPlanId is null order by storeyNo");
            }
        }
        List<Object> list = sessionFacade.executeQuery(hql.toString(), paramList.toArray());

        if (!CommonUtil.isEmpty(list)) {
            int i = 0;
            if (list.get(0) != null) {
                i = (int)list.get(0);
            }
            return i;
        }
        else {
            return 0;
        }
    }

    @Override
    public List<Project> getProjectListForPortlet(TSUserDto userDto) {
        StringBuffer hql = new StringBuffer();
        boolean isPmo = groupService.judgeHasLimit(appKey,ProjectRoleConstants.PMO, userDto.getId(), null);
        if (isPmo) {
            hql.append("select id,name from pm_project where bizcurrent = 'STARTING' order by createtime desc");
        }
        else {
            hql.append(" select id,name from ");
            hql.append(" (select pj.id id, pj.name name,pj.createtime createtime ");
            hql.append(" from pm_project pj ");
            hql.append(" join pm_proj_team_link pjtmlk on pjtmlk.projectid = pj.id ");
            hql.append(" join fd_team_role_user tu on tu.teamid = pjtmlk.teamid ");
            hql.append(" where pj.bizcurrent = 'STARTING' ");
            hql.append(" and tu.userid = '" + userDto.getId() + "' ");
            hql.append(" union ");
            hql.append(" select pj.id id, pj.name name,pj.createtime createtime ");
            hql.append(" from pm_project pj ");
            hql.append(" join pm_proj_team_link pjtmlk on pjtmlk.projectid = pj.id ");
            hql.append(" join fd_team_role_group tg on tg.teamid = pjtmlk.teamid ");
            hql.append(" join t_s_group_user gu on gu.groupid = tg.groupid ");
            hql.append(" where pj.bizcurrent = 'STARTING' ");
            hql.append(" and gu.userid = '" + userDto.getId() + "') ");
            hql.append(" order by createtime desc ");
        }
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hql.toString());
        List<Project> list = new ArrayList<Project>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    Project project = new Project();
                    project.setId(id);
                    project.setName(StringUtils.isNotEmpty((String)map.get("name")) ? (String)map.get("name") : "");
                    list.add(project);
                }
            }
        }
        return list;
    }

    @Override
    public List<Project> getProjectPortletList(String userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.* from PM_PROJECT t where 1=1 ");
        sql.append("and t.id in (select link.projectId from PM_PROJ_TEAM_LINK link "
                + " where link.teamId in (select u.teamId from fd_team_role_user u "
                + " where u.userId = '"+userId+"') union all (select rg.teamId "
                + " from fd_team_role_group rg, t_s_group_user gu "
                + " where 1 = 1 and gu.groupId = rg.groupId "
                + " and gu.userId = '"+userId+"') union all  "
                + " (select t.id  from PM_PROJECT t where  t.createBy = '"+userId+"'))");
        sql.append(" order by t.createTime desc, t.id desc");
        String searchSql = "select * from (select ROWNUM as num, row_.* from (" + sql + ") row_ where ROWNUM <= "+5+") where num > "+0+"";
        List<Map<String, Object>> projects = sessionFacade.findForJdbc(searchSql);
        List<Project> projectList = new ArrayList<>();
        if (!CommonUtil.isEmpty(projects)) {
            for (Map<String, Object> map : projects) {
                Project p = new Project();
                p.setId(map.get("ID") == null ? "" : map.get("ID").toString());
                p.setName(map.get("NAME") == null ? "" : map.get("NAME").toString());
                p.setBizCurrent(map.get("BIZCURRENT") == null ? "" : map.get("BIZCURRENT").toString());
                p.setProcess(Double.valueOf(String.valueOf(map.get("PROCESS"))));
                String managerName = map.get("PROJECTMANAGERNAMES") == null ? "" : map.get("PROJECTMANAGERNAMES").toString();
                if (managerName.contains(",")) {
                    managerName = managerName.split(",")[0];
                }
                p.setProjectManagerNames(managerName);
                p.setProcInstId(map.get("PROCINSTID") == null ? null : map.get("PROCINSTID").toString());
                projectList.add(p);
            }
        }
        return projectList;
    }

    @Override
    public ProjWarn addProjWarn(ProjWarn projWarn,  String userId,String orgId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        StringBuilder hql = new StringBuilder("");
        hql.append("from ProjWarn t where 1=1 and flag='1' and t.userName=?");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(userDto.getId());
        List<ProjWarn> projects = sessionFacade.executeQuery(hql.toString(), paramList.toArray());

        if (projects.size() != 0) {
            ProjWarn ss = projects.get(0);
            ss.setUserName(projWarn.getUserName());
            ss.setProjectNum(projWarn.getProjectNum());
            ss.setFlag(projWarn.getFlag());
            CommonInitUtil.initGLObjectForUpdate(ss,userDto,orgId);
            sessionFacade.saveOrUpdate(ss);
        }
        else {
            CommonInitUtil.initGLObjectForCreate(projWarn,userDto,orgId);
            sessionFacade.save(projWarn);
        }
        return projWarn;
    }

    @Override
    public List<ProjWarnVo> getProjWarnReportData(String projectIds) {
        List<ProjWarnVo> projWarnVoList = new ArrayList<>();
        String sqlStr = "";
        sqlStr = " select A.projectid, A.name, A.warn, A.createtime, sum(A.warnnumber) warnnumber ";
        sqlStr += " from (select p.projectid, ";
        sqlStr += "              pj.name as name, ";
        sqlStr += "               case ";
        sqlStr += "                 when decode(p.bizcurrent, ";
        sqlStr += "                             'FINISH', ";
        sqlStr += "                             1, ";
        sqlStr += "                             ceil(p.planendtime - sysdate) - s.status) > 0 then ";
        sqlStr += "                  '否' ";
        sqlStr += "                 else ";
        sqlStr += "                  '是' ";
        sqlStr += "               end as warn, ";
        sqlStr += "               count(*) as warnnumber, ";
        sqlStr += "               to_char(pj.createtime, 'yyyy-mm-dd') createtime ";
        sqlStr += "          from pm_plan p, cm_param_switch s,pm_project pj ";
        sqlStr += "         where s.name = '项目计划提前预警' ";
        sqlStr += "           and p.avaliable = 1 ";
        sqlStr += "           and p.projectid = pj.id ";
        sqlStr += "           and p.projectid in ("+projectIds+")";
        sqlStr += "         group by p.projectid, ";
        sqlStr += "               	 pj.name, ";
        sqlStr += "                  pj.createtime, ";
        sqlStr += "                  case ";
        sqlStr += "                    when decode(p.bizcurrent, ";
        sqlStr += "                                'FINISH', ";
        sqlStr += "                                1, ";
        sqlStr += "                                ceil(p.planendtime - sysdate) - s.status) > 0 then ";
        sqlStr += "                     '否' ";
        sqlStr += "                    else ";
        sqlStr += "                     '是' ";
        sqlStr += "                  end ";
        sqlStr += "        union ";
        sqlStr += "        select t1.id as projectId, ";
        sqlStr += "               t1.name as name, ";
        sqlStr += "               '是' as warn, ";
        sqlStr += "               0 as warnnumber, ";
        sqlStr += "               to_char(t1.createtime, 'yyyy-mm-dd') createtime ";
        sqlStr += "          from pm_project t1 ";
        sqlStr += "         where t1.id in ("+projectIds+")";
        sqlStr += "        union ";
        sqlStr += "        select t2.id as projectId, ";
        sqlStr += "               t2.name as name, ";
        sqlStr += "               '否' as warn, ";
        sqlStr += "               0 as warnnumber, ";
        sqlStr += "               to_char(t2.createtime, 'yyyy-mm-dd') createtime ";
        sqlStr += "          from pm_project t2 ";
        sqlStr += "         where t2.id in ("+projectIds+")) A ";
        sqlStr += " group by A.projectid, A.name, A.warn, A.createtime ";
        List<Map<String,Object>> mapList = sessionFacade.findForJdbc(sqlStr);
        if(!CommonUtil.isEmpty(mapList)){
            for(Map<String,Object> map : mapList){
                ProjWarnVo vo = new ProjWarnVo();
                vo.setProjectId(String.valueOf(map.get("PROJECTID")));
                vo.setProjectName(String.valueOf(map.get("NAME")));
                vo.setCreateTime(String.valueOf(map.get("CREATETIME")));
                vo.setWarn(String.valueOf(map.get("WARN")));
                vo.setWarnNumber(String.valueOf(map.get("WARNNUMBER")));
                projWarnVoList.add(vo);
            }
        }

        return projWarnVoList;
    }

    @Override
    public List<Map<String, Object>> getProjWarnDataForProjStatistics(String projectId) {
        String sqlStr = "";
        sqlStr = "select case" +
                "         when decode(p.bizcurrent," +
                "                     'FINISH'," +
                "                     1," +
                "                     ceil(p.planendtime - sysdate) - s.status) > 0 then" +
                "          '否'" +
                "         else" +
                "          '是'" +
                "       end as warn," +
                "       count(*) as warnnumber" +
                "  from pm_plan p, cm_param_switch s" +
                " where s.name = '项目计划提前预警'" +
                "   and p.projectid = '"+projectId+"'" +
                "   and p.avaliable = 1" +
                " group by case" +
                "            when decode(p.bizcurrent," +
                "                        'FINISH'," +
                "                        1," +
                "                        ceil(p.planendtime - sysdate) - s.status) > 0 then" +
                "             '否'" +
                "            else" +
                "             '是'" +
                "          end";
        List<Map<String, Object>> list = sessionFacade.findForJdbc(sqlStr);
        return list;
    }

    @Override
    public List<ProjWarnForGridVo> queryProjectwarnGrid(String projectId) {
        List<ProjWarnForGridVo> list = new ArrayList<>();
        String sqlStr = "";
        sqlStr = "select p.*," +
                "       case" +
                "         when p.projectstatus = 'PAUSE' then" +
                "          '已暂停'" +
                "         when p.projectstatus = 'CLOSE' then" +
                "          '已关闭'" +
                "         else" +
                "          p.bizcurrent" +
                "       end as planstatus," +
                "       (select b.name from cm_business_config b where b.id = p.planlevel) as levelname," +
                "       (select u.realname || '-' || u.username" +
                "          from t_s_base_user u" +
                "         where u.id = p.owner) as ownername," +
                "       to_char(p.planstarttime, 'YYYY-MM-DD') as starttime," +
                "       to_char(p.planendtime, 'YYYY-MM-DD') as endtime," +
                "       case" +
                "         when decode(p.bizcurrent," +
                "                     'FINISH'," +
                "                     1," +
                "                     ceil(p.planendtime - sysdate) - s.status) > 0 then" +
                "          '否'" +
                "         else" +
                "          '是'" +
                "       end as" +
                "       warn" +
                "  from pm_plan p, cm_param_switch s" +
                " where s.name = '项目计划提前预警'" +
                //原brs的sql中有此段sql,暂时不知道其中两个参数是什么，去除对结果集也没有产生影响，先注释
                /*"   and ((case when decode(p.bizcurrent," +
                "                          'FINISH'," +
                "                          1," +
                "                          ceil(p.planendtime - sysdate) - s.status)" +
                "        > 0 then '否' else '是' end) = ? or ? is null)"+*/
                "   and p.projectid = '"+projectId+"'" +
                "   and p.avaliable = 1";
        List<Map<String,Object>> mapList = sessionFacade.findForJdbc(sqlStr);
        if(!CommonUtil.isEmpty(mapList)){
            for(Map<String,Object> map : mapList){
                ProjWarnForGridVo vo = new ProjWarnForGridVo();
                vo.setId(String.valueOf(map.get("ID")));
                vo.setOwner(String.valueOf(map.get("OWNER")));
                vo.setPlanName(String.valueOf(map.get("PLANNAME")));
                if(!CommonUtil.isEmpty(String.valueOf(map.get("OWNERNAME"))) && String.valueOf(map.get("OWNERNAME")) != "null" && String.valueOf(map.get("OWNERNAME")) != null){
                    vo.setOwnerShow(String.valueOf(map.get("OWNERNAME")));
                }else{
                    vo.setOwnerShow("");
                }

                vo.setBizCurrent(String.valueOf(map.get("PLANSTATUS")));
                vo.setPlanLevel(String.valueOf(map.get("PLANLEVEL")));
                if(!CommonUtil.isEmpty(String.valueOf(map.get("PLANLEVEL")))){
                    BusinessConfig bc = (BusinessConfig)sessionFacade.getEntity(BusinessConfig.class,String.valueOf(map.get("PLANLEVEL")));
                    if(!CommonUtil.isEmpty(bc)){
                        vo.setPlanLevelName(bc.getName());
                    }
                }
                vo.setPlanStartTime(String.valueOf(map.get("STARTTIME")));
                vo.setPlanEndTime(String.valueOf(map.get("ENDTIME")));
                vo.setProgressRate(String.valueOf(map.get("PROGRESSRATE")));
                vo.setWorkTime(String.valueOf(map.get("WORKTIME")));
                list.add(vo);
            }
        }
        return list;
    }

    @Override
    public List<Project> getStartingProjByUserId(String userId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from Project t where 1=1 and t.bizCurrent='STARTING' and t.createBy=? order by createtime desc");
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(userId);
        List<Project> projects = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        return projects;
    }
}
