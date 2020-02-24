package com.glaway.ids.project.projecttemplate.service.impl;


import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.common.dto.FdTeamRoleDto;
import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.extend.hqlsearch.HqlGenerateUtil;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepLibraryDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepRoleFileAuthDto;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignRoleService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignTeamService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.constant.LifeCycleConstant;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.foundation.tag.vo.datatable.SortDirection;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.constant.ProjectConstants;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.ActivityTypeManageEntityServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.impl.TabCbTemplateEntityServiceImpl;
import com.glaway.ids.project.menu.entity.ProjTemplateMenu;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.InputsServiceI;
import com.glaway.ids.project.plan.service.NameStandardFeignService;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.entity.PreposePlanTemplate;
import com.glaway.ids.project.plantemplate.service.PlanTemplateServiceI;
import com.glaway.ids.project.plantemplate.service.PreposePlanTemplateServiceI;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.project.projecttemplate.dto.ProjTemplateDto;
import com.glaway.ids.project.projecttemplate.entity.ProjTemplate;
import com.glaway.ids.project.projecttemplate.entity.ProjTmpLibAuthLibTmpLink;
import com.glaway.ids.project.projecttemplate.entity.ProjTmplTeamLink;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateDetailServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateLibServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateRoleServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;
import com.glaway.ids.project.projectmanager.constant.ProjectRoleConstants;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.ProcessUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * 
 * @author wangshen
 * @version 2015年3月24日
 * @see ProjTemplateServiceImpl
 * @since
 */
@Service("projTemplateService")
@Transactional
public class ProjTemplateServiceImpl extends BusinessObjectServiceImpl<ProjTemplate> implements ProjTemplateServiceI {
    private static final SystemLog log = BaseLogFactory.getSystemLog(ProjTemplateServiceImpl.class);

    /**
     *  sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;

    /**
     *  项目库计划Service
     */
    @Autowired
    private ProjTemplateDetailServiceI projTemplateDetailService;

    /**
     *  项目角色Service
     */
    @Autowired
    private ProjRoleServiceI projRoleService;


    @Autowired
    private FeignRoleService roleService;


    @Autowired
    private FeignUserService userService;

    @Autowired
    private NameStandardFeignService nameStandardService;

    /**
     *  交付项Service
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;

    @Autowired
    private PreposePlanTemplateServiceI preposePlanTemplateService;

    /**
     * 输入信息
     */
    @Autowired
    private InputsServiceI inputsService;

    /**
     *  redisService
     */
    @Autowired
    private RedisService redisService;


    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private ProjLibServiceI projLibService;

    @Autowired
    private FeignRepService repService;

    @Autowired
    private FeignTeamService teamService;

    /**
     * 注入WorkFlowFacade
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;

    @Autowired
    private Environment env;

    /**
     *  项目模板角色Service
     */
    @Autowired
    private ProjTemplateRoleServiceI projTemplateRoleService;

    /**
     *  计划模板Service
     */
    @Autowired
    private PlanTemplateServiceI planTemplateService;

    /**
     *  项目模板库结构Service
     */
    @Autowired
    private ProjTemplateLibServiceI projTemplateLibService;

    @Autowired
    private TabCbTemplateEntityServiceImpl tabCbTemplateEntityService;

    @Autowired
    private ActivityTypeManageEntityServiceI activityTypeManageEntityService;


    /**
     *  jdbcTemplate
     */
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value(value="${spring.application.name}")
    private String appKey;


    @Override
    public boolean isPTOM(String userId) {
        return projRoleService.isSysRoleByUserIdAndRoleCode(
                userId, ProjectRoleConstants.PTMO);
    }

    @Override
    public ProjTemplate getProjEntity(String id) {
        ProjTemplate proj = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class,id);
        return proj;
    }

    @Override
    public void deleteTemplate(List<ProjTemplate> templates) {

        for (ProjTemplate template : templates) {
            template = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class,template.getId());   //勿删
            template.setAvaliable("0");
            sessionFacade.saveOrUpdate(template);
        }
    }

    @Override
    public void openOrClose(ProjTemplate template, String type) {
        template.setBizCurrent(type);
        Statement st = null;
        Connection conn = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            st = conn.createStatement();
            String sql = "update PM_PROJ_TEMPLATE set bizCurrent = '" + type + "' where id ='" + template.getId() + "'";
            st.executeUpdate(sql);
        }
        catch (Exception ex) {
            try {
                conn.rollback();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
        finally {
            if (st != null) {
                try {
                    st.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public PageList queryEntity(List<ConditionVO> conditionList, String userName,String orgId) {
        try {
            String hql = "from ProjTemplate t where 1=1 and t.avaliable=1 ";
            if (StringUtils.isNotEmpty(userName)) {
                if(userName.contains("-")){
                    String[] split = userName.split("-");
                    hql = hql + " and (t.createFullName like '%" + split[0] + "%'";
                    hql = hql + " or t.createName like '%" + split[1] + "%')";
                }else{
                    hql = hql + " and (t.createFullName like '%" + userName + "%'";
                    hql = hql + " or t.createName like '%" + userName + "%')";
                }
            }
            if (!CommonUtil.isEmpty(orgId)) {
                hql = hql + "and t.createOrgId = '"+orgId+"'";
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("ProjTemplate.createTime", "desc");
            for (ConditionVO p : conditionList) {
                if ("creator".equals(p.getSort())) {
                    p.setSort("createFullName");
                }
            }
            List<ProjTemplate> list = sessionFacade.searchByCommonFormHql(hql, conditionList, true, map);
            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            List<ProjTemplateDto> dtoList = CodeUtils.JsonListToList(list,ProjTemplateDto.class);
            PageList pageList = new PageList(count, dtoList);
            return pageList;
        }
        catch (RecognitionException e) {
            new FdExceptionPolicy("basicException", "查询语句格式化异常", e);
        }
        catch (TokenStreamException e) {
            new FdExceptionPolicy("basicException", "查询语句开闭缺失", e);
        }
        catch (ClassNotFoundException e) {
            new FdExceptionPolicy("basicException", "查询实体不存在", e);
        }
        return null;
    }

    @Override
    public String getLifeCycleStatusList() {
        ProjTemplate p = new ProjTemplate();
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
    public ProjTemplate getProjTemplateByBizId(String bizId) {
        List<ProjTemplate> list = sessionFacade.findHql("from ProjTemplate where bizId=? and avaliable=1",bizId);
        if(!CommonUtil.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ProjTemplate> getProjTemplateListByNameAndBizId(String name, String bizId) {
        List<ProjTemplate> list = sessionFacade.findHql("from ProjTemplate where projTmplName=? and bizId !=? and avaliable=1", name,bizId);
        return list;
    }

    @Override
    public List<ProjTemplate> getProjTemplateListByName(String name) {
        List<ProjTemplate> list = sessionFacade.findHql("from ProjTemplate where projTmplName=? and avaliable=1", name);
        return list;
    }

    @Override
    public ProjTemplate updateProjTemplate(ProjTemplate template, String method, String oldName, String oldRemark, TSUserDto userDto, String orgId,List<TSRoleDto> roleList) {
        String idOrigin = template.getId();
        ProjTemplate projTemplate = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, idOrigin);
        ProjTemplate projTemplate2 = (ProjTemplate)sessionFacade.findHql("from ProjTemplate t where t.bizId=? and t.avaliable=1", projTemplate.getBizId()).get(0);
        String id = projTemplate2.getId();
        String newName = template.getProjTmplName();
        String newRemark = template.getRemark();
        template.setProjTmplName(oldName);
        template.setRemark(oldRemark);
        CommonInitUtil.initGLObjectForUpdate(template,userDto,orgId);
        String teamId = "";
        if (StringUtils.equalsIgnoreCase(method, "miner")) {// 升级小版本
            String processInstanceId = template.getProcessInstanceId();
            revise(id, LifeCycleConstant.ReviseModel.MINER);
            sessionFacade.saveOrUpdate(template);
            ProjTemplate r = findBusinessObjectByBizId(ProjTemplate.class, template.getBizId());
            // r.setProcessInstanceId(null);
            copyProjTmpRelation(r, id,userDto,orgId,roleList);

            r.setProjTmplName(newName);
            r.setRemark(newRemark);
            if(StringUtils.isNotBlank(processInstanceId)) {
                template.setProcessInstanceId(null);
                r.setProcessInstanceId(processInstanceId);
                forwardProjTemplate(processInstanceId, r, template.getBizCurrent(),userDto);
            }
            return null;
        }
        else if (StringUtils.equalsIgnoreCase(method, "revise")) {// 升级大版本
            if(StringUtils.equalsIgnoreCase(projTemplate2.getBizCurrent(), ProjectConstants.PROJTEMPLATE_XIUDING)) {
                String processInstanceId = template.getProcessInstanceId();
                revise(id, LifeCycleConstant.ReviseModel.MINER);
                sessionFacade.saveOrUpdate(template);
                ProjTemplate r = findBusinessObjectByBizId(ProjTemplate.class, template.getBizId());
                r.setProjTmplName(newName);
                r.setRemark(newRemark);
                // r.setProcessInstanceId(null);
                copyProjTmpRelation(r, id,userDto,orgId,roleList);

                if(StringUtils.isNotBlank(processInstanceId)) {
                    template.setProcessInstanceId(null);
                    r.setProcessInstanceId(processInstanceId);
                    forwardProjTemplate(processInstanceId, r, template.getBizCurrent(),userDto);
                }
                return null;
            }else{
                String processInstanceId = template.getProcessInstanceId();
                ProjTemplate vo = new ProjTemplate();
                if(StringUtils.isNotBlank(processInstanceId)) {
                    revise(id, LifeCycleConstant.ReviseModel.MAJOR);
                    sessionFacade.saveOrUpdate(template);
                    ProjTemplate r = findBusinessObjectByBizId(ProjTemplate.class, template.getBizId());
                    r.setProjTmplName(newName);
                    r.setRemark(newRemark);
                    r.setBizCurrent(ProjectConstants.PROJTEMPLATE_XIUDING);
                    copyProjTmpRelation(r, id,userDto,orgId,roleList);

                    r.setProcessInstanceId("");
                    forwardProjTemplate(processInstanceId, r, template.getBizCurrent(),userDto);
                    try {
                        BeanUtil.copyBeanNotNull2Bean(r, vo);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    revise(id, LifeCycleConstant.ReviseModel.MAJOR);
                    sessionFacade.saveOrUpdate(template);
                    ProjTemplate r = findBusinessObjectByBizId(ProjTemplate.class,
                            template.getBizId());
                    r.setProjTmplName(newName);
                    r.setRemark(newRemark);
                    r.setBizCurrent(ProjectConstants.PROJTEMPLATE_XIUDING);
                    r.setProcessInstanceId("");
                    copyProjTmpRelation(r, id,userDto,orgId,roleList);
                    try {
                        BeanUtil.copyBeanNotNull2Bean(r, vo);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return vo;
            }
        }
        else {// 不升级
            template.setRemark(newRemark);
            sessionFacade.saveOrUpdate(template);
            saveTemplateDetailFromRedis(id,null,userDto,orgId);
            teamId = projTemplateRoleService.getTeamIdByTemplateId(template.getId());
            List<TSRoleDto> list = roleService.getSysRoleListByTeamId(appKey,teamId);
            List<FdTeamRoleDto> fdTeamRoleDtoList = new ArrayList<>();
            if(list!=null&&list.size()>0)
            {
                for (TSRoleDto tsRole : list)
                {
                    FdTeamRoleDto roleDto = new FdTeamRoleDto();
                    roleDto.setTeamId(teamId);
                    roleDto.setRoleId(tsRole.getId());
                    fdTeamRoleDtoList.add(roleDto);
                }
            }
            teamService.deleteFdTeamRoleByTeamIdAndRoleId(fdTeamRoleDtoList);
            for(TSRoleDto role : roleList){
                projRoleService.addTeamRoleByCode(teamId, role.getRoleCode());
            }
            return null;
        }
    }

    private void revise(String projTmpId, String mode) {
        ProjTemplate bo = findBusinessObjectById(ProjTemplate.class, projTmpId);
        reviseBusinessObject(bo, mode);
        if (mode.equals(LifeCycleConstant.ReviseModel.MAJOR)) {
            ProjTemplate r = findBusinessObjectHistoryByBizId(ProjTemplate.class, bo.getBizId()).get(0);
            moveBusinessObjectByOrder(r, 0);
        }

    }

    private String copyProjTmpRelation(ProjTemplate upVersionProjTmp, String oldTmpId,TSUserDto userDto,String orgId,List<TSRoleDto> roleList) {
        String newTmpId = upVersionProjTmp.getId();
        String newTeamId = projectService.addTeam(newTmpId);
        RepLibraryDto repLibraryDto = new RepLibraryDto();
        repLibraryDto.setTeamId(newTeamId);
        repLibraryDto.setTeamName(newTeamId);
        repLibraryDto.setLibType(ProjectConstants.PROJECTTEMPLATE);
        repLibraryDto.setLibName(newTmpId);
        FeignJson repLibraryJson = repService.addRepLibrary(appKey,repLibraryDto);
        String newlibId = String.valueOf(repLibraryJson.getObj());
        saveTeamLink(newTmpId, newTeamId, newlibId,userDto,orgId);
        String oldTeamId = projTemplateRoleService.getTeamIdByTemplateId(oldTmpId);
        // 复制计划（从redis复制计划到升级后的项目模板）
        saveTemplateDetailFromRedis(oldTmpId,newTmpId,userDto,orgId);
        // 复制团队角色
        copyTeam(oldTeamId, newTeamId);

        List<TSRoleDto> list = roleService.getSysRoleListByTeamId(appKey,newTeamId);
        List<FdTeamRoleDto> fdTeamRoleDtoList = new ArrayList<>();
        if(list!=null&&list.size()>0)
        {
            for (TSRoleDto tsRole : list)
            {
                FdTeamRoleDto roleDto = new FdTeamRoleDto();
                roleDto.setTeamId(newTeamId);
                roleDto.setRoleId(tsRole.getId());
                fdTeamRoleDtoList.add(roleDto);
            }
        }
        teamService.deleteFdTeamRoleByTeamIdAndRoleId(fdTeamRoleDtoList);
        for(TSRoleDto role : roleList){
            projRoleService.addTeamRoleByCode(newTeamId, role.getRoleCode());
        }

        // 复制项目库
        HashMap<String, String> idMap = copyProjLibFolders(upVersionProjTmp.getProjTmplName(),
                oldTmpId, newlibId,userDto);
        // 复制项目库角色目录权限
        String oldlibId = projTemplateLibService.getLibIdByTemplateId(oldTmpId);
        saveRoleFileAuth2(oldTeamId, oldlibId, newlibId, idMap);
        return newTmpId;
    }


    private void saveTemplateDetailFromRedis(String projectTemplateId, String newTemplateId,TSUserDto userDto,String orgId) {
        Map<String,String> map = new HashMap<String, String>();
        List<Plan> detailList = new ArrayList<>();
        String detailStr = (String)redisService.getFromRedis("PROJTMPPLANLIST", projectTemplateId);
        if(!CommonUtil.isEmpty(detailStr)){
            detailList = JSON.parseArray(detailStr,Plan.class);
        }
        List<PlanTemplateDetail> detailInsert = new ArrayList<PlanTemplateDetail>();
        List<DeliverablesInfo> deliverablesInsert = new ArrayList<DeliverablesInfo>();
        List<PreposePlanTemplate> preposePlanInsert = new ArrayList<PreposePlanTemplate>();
        List<Inputs> inputsInsert = new ArrayList<Inputs>();
        Map<String, String> deliverMap = new HashMap<String, String>();
        PlanTemplateDetail pd = new PlanTemplateDetail();
        initBusinessObject(pd);
        CommonInitUtil.initGLObjectForCreate(pd,userDto,orgId);
        String bizCurrent = pd.getBizCurrent();
        String bizVersion = pd.getBizVersion();
        LifeCyclePolicy policy = pd.getPolicy();

        DeliverablesInfo d1 = new DeliverablesInfo();
        initBusinessObject(d1);
        CommonInitUtil.initGLObjectForCreate(d1,userDto,orgId);
        String dBizCurrent = d1.getBizCurrent();
        String dBizVersion = d1.getBizVersion();
        LifeCyclePolicy dPolicy = d1.getPolicy();
        if(!CommonUtil.isEmpty(detailList)){

            for(Plan p : detailList){
                Plan detail = new Plan();
                try {
                    BeanUtil.copyBeanNotNull2Bean(p, detail);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                String tempId = UUIDGenerator.generate();
                map.put(detail.getId(), tempId);
            }

            for(Plan p : detailList){
                Plan detail = new Plan();
                try {
                    BeanUtil.copyBeanNotNull2Bean(p, detail);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                PlanTemplateDetail temp = new PlanTemplateDetail();
//                tempId = map.get(detail.getId());
                temp.setId(newTemplateId == null ? detail.getId() : map.get(detail.getId()));
                temp.setName(detail.getPlanName());
                temp.setIsNecessary(detail.getIsNecessary());
                temp.setMilestone(detail.getMilestone());
                temp.setPlanNumber(detail.getPlanNumber());
                if(!CommonUtil.isEmpty(detail.getParentPlanId())){
                    temp.setParentPlanId(newTemplateId == null ? detail.getParentPlanId() : map.get(detail.getParentPlanId()));
                }
                temp.setPlanLevel(detail.getPlanLevel());
                temp.setPlanTemplateId("");
                temp.setProjectTemplateId(newTemplateId == null ? projectTemplateId : newTemplateId );
                temp.setRemark(detail.getRemark());
                temp.setWorkTime(detail.getWorkTime());
                temp.setCreateBy(userDto.getId());
                temp.setCreateFullName(userDto.getRealName());
                temp.setCreateName(userDto.getUserName());
                temp.setCreateTime(new Date());
                temp.setStoreyNo(detail.getStoreyNo());
                temp.setPolicy(policy);
                temp.setBizCurrent(bizCurrent);
                temp.setBizVersion(bizVersion);
                temp.setBizId(UUID.randomUUID().toString());
                temp.setTaskNameType(detail.getTaskNameType());
                temp.setTabCbTemplateId(detail.getTabCbTemplateId());
                CommonInitUtil.initGLObjectForCreate(temp,userDto,orgId);
                detailInsert.add(temp);
                //  save(temp);
                map.put(detail.getId(), temp.getId());
                /*if(!CommonUtil.isEmpty(detail.getParentPlanId())){
                    map1.put(detail.getParentPlanId(), temp.getId());
                }
*/
                //先从数据库获取数据，没有的话再从redis取
                List<DeliverablesInfo> deliverablesList = new ArrayList<>();
                if (StringUtils.isNotBlank(detail.getId())) {
                    deliverablesList = deliverablesInfoService.getDeliverablesByUseObeject(CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE,detail.getId());
                }
                if (CommonUtil.isEmpty(deliverablesList)) {
                    deliverablesList = detail.getDeliInfoList();
                }
                if(!CommonUtil.isEmpty(deliverablesList)){
                    for(DeliverablesInfo d: deliverablesList) {
                        DeliverablesInfo info = new DeliverablesInfo();
                        String newId = UUIDGenerator.generate().toString();
                        info.setPolicy(dPolicy);
                        info.setBizCurrent(dBizCurrent);
                        info.setBizVersion(dBizVersion);
                        info.setBizId(UUID.randomUUID().toString());
                        deliverMap.put(d.getId(), newId);
                        info.setId(newId);
                        info.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE);
                        info.setUseObjectId(map.get(detail.getId()));
                        info.setName(d.getName());
                        info.setOrigin(d.getOrigin());
                        CommonInitUtil.initGLObjectForCreate(info,userDto,orgId);
                        deliverablesInsert.add(info);
                    }
                }
            }

            for(Plan pp : detailList) {
                Plan del = new Plan();
                try {
                    BeanUtil.copyBeanNotNull2Bean(pp, del);
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String preposeIds = del.getPreposeIds();
                if(!CommonUtil.isEmpty(preposeIds)){
                    String[] preposeIdsArr = preposeIds.split(",");
                    for(String preposeId : preposeIdsArr) {
                        if(!CommonUtil.isEmpty(map.get(preposeId)) && !CommonUtil.isEmpty(map.get(del.getId()))) {
                            PreposePlanTemplate p = new PreposePlanTemplate();
                            p.setId(UUIDGenerator.generate().toString());
                            p.setPlanId(map.get(del.getId()));
                            p.setPreposePlanId(map.get(preposeId));
                            CommonInitUtil.initGLObjectForCreate(p,userDto,orgId);
                            preposePlanInsert.add(p);
                        }
                    }
                }
                //原数据
                List<Inputs> inputsList = new ArrayList<>();
                String inpStr = (String)redisService.getFromRedis("INPUTSLIST", del.getId());
                if(!CommonUtil.isEmpty(inpStr)){
                    inputsList = JSON.parseArray(inpStr,Inputs.class);
                }
                List<Inputs> inputList = del.getInputList();
                if(!CommonUtil.isEmpty(inputsList)){
                    for(Inputs input : inputsList) {
                        if(!CommonUtil.isEmpty(map.get(del.getId()))) {
                            CommonInitUtil.initGLObjectForCreate(input,userDto,orgId);
                            input.setId(UUIDGenerator.generate().toString());
                            input.setUseObjectId(map.get(del.getId()));
                            input.setUseObjectType(CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE);
                            if(PlanConstants.USEOBJECT_TYPE_PLAN.equals(input.getOriginType()) ||
                                    CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE.equals(input.getOriginType()) || CommonConstants.DELIVERABLE_TYPE_PLANTEMPLATE.equals(input.getOriginType())) {
                                if(CommonUtil.isEmpty(map.get(input.getOriginObjectId()))) {
                                    input.setOriginObjectId(null);
                                    input.setOriginDeliverablesInfoId(null);
                                    input.setOriginType(CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE);
                                } else {
                                    input.setOriginObjectId(map.get(input.getOriginObjectId()));
                                    input.setOriginDeliverablesInfoId(deliverMap.get(input.getOriginDeliverablesInfoId()));
                                    input.setOriginType(CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE);
                                }
                            } else if(ProjectConstants.PROJECTTEMPLATE.equals(input.getOriginType())) {
                                input.setOriginType(null);
                                input.setDocId(null);
                                input.setDocName(null);
                            }
                        }
                    }
                    inputsInsert.addAll(inputsList);
                    String inputStr = JSON.toJSONString(inputsList);
                    redisService.setToRedis("INPUTSLIST", map.get(del.getId()), inputStr);
                }
            }
            redisService.deleteFromRedis("PROJTMPPLANLIST", projectTemplateId);
            planTemplateService.savePlanTemplateAllByList(detailInsert, deliverablesInsert, preposePlanInsert, inputsInsert);
        }
    }

    private void copyTeam(String oldTeamId, String newTeamId) {
        List<FdTeamRoleDto> oldRoles = teamService.getRoleByTeamId(appKey,oldTeamId);
        if (!CommonUtil.isEmpty(oldRoles)) {
            for (FdTeamRoleDto role : oldRoles) {
                projRoleService.addTeamRoleByCode(newTeamId, role.getRoleCode());
            }
        }
    }

    private void forwardProjTemplate(String processInstanceId, ProjTemplate t, String bizCurrent,TSUserDto userDto) {
        //更新我的待办及流程信息
        if(!CommonUtil.isEmpty(processInstanceId)) {
            List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    processInstanceId,userDto.getUserName());

  /*          FeignJson fj = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(processInstanceId);
            List<org.activiti.engine.task.Task> tasks = new ArrayList<>();
            if(!CommonUtil.isEmpty(fj.getObj()) && !fj.getObj().equals("已完成")){
                tasks = (List<org.activiti.engine.task.Task>)fj.getObj();
            }*/

            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByProcInstId(processInstanceId);
            String taskId = "";
            Map<String, Object> variables = new HashMap<String, Object>();
            if(!CommonUtil.isEmpty(tasks)) {
                taskId = tasks.get(0).getId();
                variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(taskId);
            }
            if(!CommonUtil.isEmpty(variables)) {
                variables.put("editUrl",
                        "/ids-pm-web/projTemplateDetailController.do?projTemplateDetailEdit&dataHeight=500&dataWidth=900&projTmpId="
                                + t.getId());
                variables.put("viewUrl",
                        "/ids-pm-web/projTemplateDetailController.do?projTemplateDetail&dataHeight=500&dataWidth=900&projTmpId="
                                + t.getId());
                variables.put("oid", BpmnConstants.OID_PROJECTTEMPLATE + t.getId());
                variables.put("taskNumber", t.getId());
                variables.put("flowStatus", bizCurrent);
                workFlowFacade.getWorkFlowCommonService().setRunVariables(processInstanceId, variables);
            }
            // 获取流程显示名称
            ProcessDefinition processDefinition = null;
            try{
                processDefinition = workFlowFacade.getWorkFlowCommonService().getProcessDefinitionByProcInstId(
                        processInstanceId, false);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(!CommonUtil.isEmpty(processDefinition)) {
                String bpmnDisplayName = processDefinition.getName();
                myStartedTask.setObjectName(t.getProjTmplName());
                myStartedTask.setTaskNumber(t.getId());
                myStartedTask.setTitle(ProcessUtil.getProcessTitle(t.getProjTmplName(), bpmnDisplayName, processInstanceId));
                workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
            }
        }

    }


    /**
     * 复制项目库
     *
     * @param fileName
     * @param oldTmpId
     * @return
     * @see
     */
    private HashMap<String, String> copyProjLibFolders(String fileName, String oldTmpId,
                                                       String newlibId,TSUserDto userDto) {
        HashMap<String, String> idMap = new HashMap<String, String>();
        String oldLibId = projTemplateLibService.getLibIdByTemplateId(oldTmpId);
        List<RepFileDto> roots = repService.getRootDirsByParams(appKey,oldLibId,"0",oldLibId);
        try {
            if (roots == null || roots.size() == 0) {
                RepFileDto repFile = new RepFileDto();
                repFile.setFileName(fileName);
                repFile.setFileType(0);
                repFile.setLibId(oldLibId);
                repFile.setParentId(oldLibId);
                repService.addRepFile(appKey,repFile,userDto.getId());
            }
            else {
                List<RepFileDto> files = new ArrayList<RepFileDto>();
                if (!CommonUtil.isEmpty(roots) && roots.size() == 1) {
                    RepFileDto r = roots.get(0);
                    RepFileDto root = new RepFileDto();
                    BeanUtil.copyBeanNotNull2Bean(r, root);
                    root.setFileName(fileName);
                    files.add(root);
                    files = orderByParent(root, files);
                }
                for (RepFileDto file : files) {
                    RepFileDto newFile = new RepFileDto();
                    String oldId = file.getId();
                    BeanUtil.copyBeanNotNull2Bean(file, newFile);
                    newFile.setId(null);
                    if (file.getLibId().equals(file.getParentId())) {
                        newFile.setLibId(newlibId);
                        newFile.setParentId(newlibId);
                        // 创建文件
                        FeignJson fj = repService.addRepFile(appKey,newFile,userDto.getId());
                        String fileId = String.valueOf(fj.getObj());
                        idMap.put(oldId, fileId);
                    }
                    else {
                        String newParentId = idMap.get(newFile.getParentId());
                        newFile.setLibId(newlibId);
                        newFile.setParentId(newParentId);
                        FeignJson fj = repService.addRepFile(appKey,newFile,userDto.getId());
                        String fileId = String.valueOf(fj.getObj());
                        idMap.put(oldId, fileId);
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("复制项目库失败", e);
            e.printStackTrace();
        }
        return idMap;
    }

    private void saveRoleFileAuth2(String oldTeamId, String oldlibId, String newLibId,
                                   HashMap<String, String> idMap) {
        List<TSRoleDto> list = roleService.getSysRoleListByTeamId(appKey,oldTeamId);// 获得团队角色

        List<RepFileDto> roots = repService.getRootDirsByParams(appKey,oldlibId,"0",oldlibId);
        List<RepFileDto> files = new ArrayList<RepFileDto>();
        files.addAll(roots);
        if (!CommonUtil.isEmpty(roots) && files.size() == 1) {
            RepFileDto root = roots.get(0);
            files = orderByParent(root, files);

        }
        List<RepFileDto> roots_ = repService.getRootDirsByParams(appKey,newLibId,"0",newLibId);
        List<RepFileDto> files_ = new ArrayList<RepFileDto>();
        files_.addAll(roots_);
        if (!CommonUtil.isEmpty(roots_) && files_.size() == 1) {
            RepFileDto root = roots_.get(0);
            files_ = orderByParent(root, files_);

        }

        for (RepFileDto f : files) {
            List<RepRoleFileAuthDto> roleFileAuthsList = repService.getRepRoleFileAuthList(f.getId());
                   /* sessionFacade.findHql(
                    "from RepRoleFileAuth t where t.fileId = ? order by t.roleId", f.getId());*/
            for (RepFileDto ff : files_) {
                if (idMap.get(f.getId()).equalsIgnoreCase(ff.getId())) {
                    copyRoleFileAuth(roleFileAuthsList, ff.getId());
                }
            }
        }
        // FIXME 增加默认角色的默认权限,单独建一个关系表（现在和项目共用一个角色目录权限关系表）?

    }


    @Override
    public List<RepFileDto> orderByParent(RepFileDto root, List<RepFileDto> results) {
        List<RepFileDto> children = projLibService.getImmediateChildrenFolders(root.getId());
        for (RepFileDto child : children) {
            results.add(child);
            List<RepFileDto> gChildren = projLibService.getImmediateChildrenFolders(child.getId());
            if (!CommonUtil.isEmpty(gChildren)) {
                orderByParent(child, results);
            }

        }
        return results;
    }


    private void copyRoleFileAuth(List<RepRoleFileAuthDto> list, String templateLibFileId) {
        for (RepRoleFileAuthDto auth : list) {
            RepRoleFileAuthDto auth2 = new RepRoleFileAuthDto();
            try {
                BeanUtil.copyBeanNotNull2Bean(auth, auth2);
                auth2.setId(null);
                auth2.setFileId(templateLibFileId);
                repService.addRepRoleFileAuth(auth2);
            }
            catch (Exception e) {
                Throwable es = new Throwable("项目模板角色权限保存失败", e);
                es.printStackTrace();
            }
        }
    }

    @Override
    public String SaveProjTemplate(ProjTemplate projTemplate,TSUserDto userDto,String orgId) {
        String templateId = "";
        if (StringUtils.isNotBlank(projTemplate.getId())) {
            CommonInitUtil.initGLObjectForUpdate(projTemplate,userDto,orgId);
            sessionFacade.saveOrUpdate(projTemplate);
        }
        else {
            CommonInitUtil.initGLObjectForCreate(projTemplate,userDto,orgId);
            initBusinessObject(projTemplate);
            projTemplate.setBizCurrent(ProjectConstants.PROJTEMPLATE_NIZHL);
            if(StringUtil.isEmpty(projTemplate.getProjTmplNumber())){
                projTemplate.setProjTmplNumber(projTemplate.getProjTmplName());
            }
            sessionFacade.save(projTemplate);
            templateId =projTemplate.getId();
            String teamId = projectService.addTeam(templateId);

            RepLibraryDto repLibraryDto = new RepLibraryDto();
            repLibraryDto.setTeamId(teamId);
            repLibraryDto.setTeamName(teamId);
            repLibraryDto.setLibType(ProjectConstants.PROJECTTEMPLATE);
            repLibraryDto.setLibName(templateId);
            FeignJson repLibraryJson = repService.addRepLibrary(appKey,repLibraryDto);
            String libId = String.valueOf(repLibraryJson.getObj());

            saveTeamLink(templateId, teamId, libId,userDto,orgId);
            // 保存团队角色
            List<TSRoleDto> managerList = roleService.getTSRoleByParams(appKey,"manager","1");
            TSRoleDto manager = new TSRoleDto();
            if(!CommonUtil.isEmpty(managerList)){
                manager = managerList.get(0);
            }
            if(!CommonUtil.isEmpty(manager)) {
                projRoleService.addTeamRoleByCode(teamId, manager.getRoleCode());
            }

            // 保存项目库根目录
            RepFileDto repFile = new RepFileDto();
            repFile.setFileName(projTemplate.getProjTmplName());
            repFile.setFileType(0);
            repFile.setLibId(libId);
            repFile.setParentId(libId);
            FeignJson fj = repService.addRepFile(appKey,repFile,userDto.getId());
            String rootIdString = String.valueOf(fj.getObj());

            // 保存项目库根目录角色权限
            RepRoleFileAuthDto rootFileAuth = new RepRoleFileAuthDto();
            if(!CommonUtil.isEmpty(manager)) {
                projRoleService.addTeamRoleByCode(teamId, manager.getRoleCode());
                rootFileAuth.setRoleId(manager.getId());
            }
            rootFileAuth.setFileId(rootIdString);
            rootFileAuth.setPermissionCode("0");
            repService.addRepRoleFileAuth(rootFileAuth);
        }
        return templateId;
    }

    @Override
    public String saveTeamLink(String projTemplateId, String teamId, String libId, TSUserDto userDto, String orgId) {
        ProjTmplTeamLink link = new ProjTmplTeamLink();
        link.setProjTemplateId(projTemplateId);
        link.setTeamId(teamId);
        link.setLibId(libId);
        initBusinessObject(link);
        CommonInitUtil.initGLObjectForCreate(link,userDto,orgId);
        sessionFacade.save(link);
        return link.getId();
    }

    @Override
    public List<ProjTemplateMenu> searchProjTemplateMenu(ProjTemplateMenu menu) {
        /*CriteriaQuery cq = new CriteriaQuery(ProjTemplateMenu.class);
        cq.addOrder("sort", SortDirection.asc);
        HqlGenerateUtil.installHql(cq, menu, null);*/
        List<ProjTemplateMenu> list = sessionFacade.findHql("from ProjTemplateMenu where status = ? order by sort asc",menu.getStatus());
        return list;
    }

    @Override
    public void backToVersion(String id, String bizId, String type, TSUserDto userDto, String orgId) {
        if (StringUtils.equalsIgnoreCase(type, "Min")) {
            ProjTemplate p = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, id);
            if(!CommonUtil.isEmpty(p.getProcessInstanceId())) {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(p.getId(),
                        p.getProcessInstanceId(), "delete");
            }
            sessionFacade.deleteEntityById(ProjTemplate.class, id);
            List<ProjTemplate> taskProcTemplateList = findBusinessObjectHistoryByBizId(
                    ProjTemplate.class, bizId);
            ProjTemplate r = taskProcTemplateList.get(0);
            CommonInitUtil.initGLObjectForUpdate(r,userDto,orgId);
            r.setAvaliable(ProjectConstants.PROJTMP_USED);
            sessionFacade.updateEntitie(r);
            // 回退版本的日志：
        }
        else if (StringUtils.equalsIgnoreCase(type, "Maj")) {
            ProjTemplate p = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, id);
            String bizversion = null;
            if (StringUtil.isNotEmpty(p.getBizVersion())) {
                bizversion = p.getBizVersion().split("\\.")[0] + ".";
            }
            List<ProjTemplate> projTemplateLists = sessionFacade.findHql("from ProjTemplate t where t.bizId='" + bizId
                    + "' and t.bizVersion like '%" + bizversion
                    + "%' order by createtime desc");
            for (ProjTemplate sub : projTemplateLists) {
                if ("1".equals(sub.getAvaliable()) && !CommonUtil.isEmpty(sub.getProcessInstanceId())) {
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(p.getId(),
                            sub.getProcessInstanceId(), "delete");
                }
                sessionFacade.delete(sub);
            }
            List<ProjTemplate> list = sessionFacade.findHql("from ProjTemplate t where t.bizId=? order by createtime desc", bizId);
            // 2.将数据设置为启用状态
            if (list.size() > 0) {
                ProjTemplate p2 = list.get(0);
                CommonInitUtil.initGLObjectForUpdate(p2,userDto,orgId);
                p2.setAvaliable(ProjectConstants.PROJTMP_USED);
                sessionFacade.saveOrUpdate(p2);
                // 撤销版本的日志：
            }
        }
    }

    @Override
    public String copyProjTemplate(ProjTemplate projTemplate, String oldTmpId,String userId,String orgId) {
        TSUserDto userDto = userService.getUserByUserId(userId);
        String newtemplateId = "";
        // 初始化项目模板对象
        initBusinessObject(projTemplate);
        CommonInitUtil.initGLObjectForCreate(projTemplate,userDto,orgId);
        projTemplate.setProcessInstanceId(null);
        projTemplate.setBizCurrent(ProjectConstants.PROJTEMPLATE_NIZHL);
        if(StringUtil.isEmpty(projTemplate.getProjTmplNumber())){
            projTemplate.setProjTmplNumber(projTemplate.getProjTmplName());
        }
        sessionFacade.save(projTemplate);
        newtemplateId = projTemplate.getId();
        String newTeamId = projectService.addTeam(newtemplateId);
        RepLibraryDto repLibraryDto = new RepLibraryDto();
        repLibraryDto.setTeamId(newTeamId);
        repLibraryDto.setTeamName(newTeamId);
        repLibraryDto.setLibType(ProjectConstants.PROJECTTEMPLATE);
        repLibraryDto.setLibName(newtemplateId);
        FeignJson repLibraryJson = repService.addRepLibrary(appKey,repLibraryDto);
        String newlibId = String.valueOf(repLibraryJson.getObj());

        saveTeamLink(newtemplateId, newTeamId, newlibId,userDto,orgId);
        String oldTeamId = projTemplateRoleService.getTeamIdByTemplateId(oldTmpId);
        // 复制计划
        copyPlan(oldTmpId, newtemplateId,userDto,orgId);
        // 复制团队角色
        copyTeam(oldTeamId, newTeamId);
        // 复制项目库
        String rootName = projTemplate.getProjTmplName()+"-"+projTemplate.getProjTmplNumber();
        HashMap<String, String> idMap = copyProjLibFolders(rootName,
                oldTmpId, newlibId,userDto);
        // 复制项目库角色目录权限
        String oldlibId = projTemplateLibService.getLibIdByTemplateId(oldTmpId);
        saveRoleFileAuth2(oldTeamId, oldlibId, newlibId, idMap);
        return newtemplateId;
    }

    @SuppressWarnings("unchecked")
    private void copyPlan(String oldTmpId, String newtemplateId,TSUserDto userDto,String orgId)
            throws GWException {
        List<PlanTemplateDetail> oldProjTmpDetailsList = new ArrayList<PlanTemplateDetail>();
        try {
            oldProjTmpDetailsList = sessionFacade.findHql(
                    "from PlanTemplateDetail where projectTemplateId=?", oldTmpId);

            List<Plan> toCopyPlan = new ArrayList<Plan>();
            String planStr = (String)redisService.getFromRedis("PROJTMPPLANLIST", oldTmpId);
            if(!CommonUtil.isEmpty(planStr)){
                toCopyPlan = JSON.parseArray(planStr,Plan.class);
            }
            if(CommonUtil.isEmpty(toCopyPlan)) {
                toCopyPlan = projTemplateDetailService.convertPlanjTemplateDetail2Plan(oldTmpId);
            }

            if (!CommonUtil.isEmpty(oldProjTmpDetailsList)) {
                Map<String, Object> paraMap = new HashMap<String, Object>();
                for (PlanTemplateDetail oldDetail : oldProjTmpDetailsList) {
                    PlanTemplateDetail detail = new PlanTemplateDetail();
                    BeanUtil.copyBeanNotNull2Bean(oldDetail, detail);
                    detail.setProjectTemplateId(newtemplateId);
                    detail.setId(null);
                    // 如果有上级的ID，需要获得上级对应的记录的ID
                    if (StringUtils.isNotEmpty(oldDetail.getParentPlanId())
                            && !CommonUtil.mapIsEmpty(paraMap, oldDetail.getParentPlanId())) {
                        String parentId = (String)paraMap.get(oldDetail.getParentPlanId());
                        detail.setParentPlanId(parentId);
                    }
                    CommonInitUtil.initGLObjectForCreate(detail,userDto,orgId);
                    sessionFacade.save(detail);
                    paraMap.put(oldDetail.getId(), detail.getId());
                    deliverablesInfoService.saveDeliverableByObj(oldDetail.getId(),
                            CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE,
                            CommonConstants.DELIVERABLE_TYPE_PROJECTTEMPLATE, detail,userDto,orgId);
                }
                for (PlanTemplateDetail oldDetail : oldProjTmpDetailsList) {
                    copyPreposeProjTemplate(oldDetail, paraMap);
                }
            }
        }
        catch (Exception e) {
            log.error("复制项目库计划失败", e);
        }
    }

    /**
     * 复制项目模板计划的前置关系
     *
     * @param oldDetail
     * @param paraMap
     * @see
     */
    private void copyPreposeProjTemplate(PlanTemplateDetail oldDetail, Map<String, Object> paraMap) {
        List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where planId=?",oldDetail.getId());
        try {
            if (preposePlanList != null && preposePlanList.size() > 0) {
                // 遍历计划前置数据
                for (PreposePlan originPlan : preposePlanList) {
                    PreposePlan toPlan = new PreposePlan();
                    BeanUtil.copyBeanNotNull2Bean(originPlan, toPlan);
                    toPlan.setId(null);
                    // 如果前置计划ID没有，则不需要保存
                    if (CommonUtil.mapIsEmpty(paraMap, originPlan.getPreposePlanId())) {
                        String planTemplateDetailId = (String)paraMap.get(originPlan.getPlanId());
                        toPlan.setPlanId(planTemplateDetailId);
                    }
                    else {

                        String planTemplateDetailId = (String)paraMap.get(originPlan.getPlanId());
                        String preposePlanTemplateId = (String)paraMap.get(originPlan.getPreposePlanId());
                        toPlan.setPlanId(planTemplateDetailId);
                        toPlan.setPreposePlanId(preposePlanTemplateId);
                    }
                    sessionFacade.save(toPlan);
                }
            }
        }
        catch (Exception e) {
            log.error("复制项目库计划前置关系失败  ", e);
        }
    }

    @Override
    public <T> List<T> getVersionHistory(String bizId, Integer pageSize, Integer pageNum) {
        List<T> list = new ArrayList<T>();
        StringBuilder hql = new StringBuilder("");
        hql.append(" from ProjTemplate t where t.bizId=? order by t.createTime desc");
        String[] params = {bizId};

        if (pageSize != null) {
            list = sessionFacade.pageList(hql.toString(), params, (pageNum - 1) * pageSize, pageSize);
        }
        else {
            list = sessionFacade.executeQuery(hql.toString(), params);
        }
        return list;
    }

    @Override
    public long getVersionCount(String bizId) {
        StringBuilder hql = new StringBuilder("");
        hql.append(" select count(*) from ProjTemplate t  where t.bizId=?");
        String[] params = {bizId};
        return sessionFacade.getCount(hql.toString(), params);
    }

    @Override
    public List<ProjTmpLibAuthLibTmpLink> getProjTmpLibAuthLibTmpLinkByTemplateId(String templateId) {
        List<ProjTmpLibAuthLibTmpLink> list= sessionFacade.findHql("from ProjTmpLibAuthLibTmpLink where projTmpId=?", templateId);
        return list;
    }

    @Override
    public void startProjTemplateProcess(String templateId, String leader, String deptLeader, TSUserDto userDto, String orgId) {
        ProjTemplate projTemplate = new ProjTemplate();
        projTemplate = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, templateId);
        // 1.获取设置map
        Map<String, Object> variables = setFlowMap(leader, deptLeader, projTemplate.getId(),userDto);
        // 2.获取业务
        if(CommonUtil.isEmpty(projTemplate.getProcessInstanceId())){
            submitProjectTemplateFlow(projTemplate,variables,BpmnConstants.BPMN_START_PROJTEMPLATE,userDto,orgId);
        }else{
            submitProjectTemplateFlowAagin(projTemplate,userDto,orgId);
        }

    }

    /**
     * 获取工作流map信息
     *
     * @param leader
     * @return
     */
    private Map<String, Object> setFlowMap(String leader, String deptLeader,
                                           String id,TSUserDto userDto) {
        ProjTemplate p =  (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, id);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("leader", leader);
        variables.put("assigner", userDto.getUserName());
        variables.put("deptLeader", deptLeader);
        variables.put("editUrl",
                "/ids-pm-web/projTemplateDetailController.do?projTemplateDetailEdit&dataHeight=500&dataWidth=900&projTmpId="
                        + id);
        variables.put("viewUrl",
                "/ids-pm-web/projTemplateDetailController.do?projTemplateDetail&dataHeight=500&dataWidth=900&projTmpId="
                        + id);
        variables.put("oid", BpmnConstants.OID_PROJECTTEMPLATE + id);
        variables.put("flowStatus", p.getBizCurrent());
        variables.put("cancelEventListener",
                "com.glaway.foundation.activiti.task.common.CommonCancelListener");
        // 设置流程审批意见变量
        variables.put("desc", "");
        variables.put("userId", userDto.getId());
        String feignContinueUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projTemplateListenerRestController/notify.do";
        variables.put("feignProjectTemplateContinueListener", feignContinueUrl);
        String feignRefuseUrl = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/projTemplateGoBackListenerRestController/notify.do";
        variables.put("feignProjectTemplateRefuseListener", feignRefuseUrl);
        return variables;
    }

    @Override
    public void updateBizCurrentForProcessComplete(String templateId) {
        ProjTemplate projTemplate = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, templateId);
        projTemplate.setBizCurrent(ProjectConstants.PROJTEMPLATE_QIYONG);
        sessionFacade.saveOrUpdate(projTemplate);
    }

    @Override
    public void updateBizCurrentForProcessRefuse(String templateId, String bizCurrent) {
        ProjTemplate projTemplate = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class, templateId);
        if(StringUtils.equals(bizCurrent, "xiuding")) {
            projTemplate.setBizCurrent(ProjectConstants.PROJTEMPLATE_XIUDING);
        } else {
            projTemplate.setBizCurrent(ProjectConstants.PROJTEMPLATE_NIZHL);
        }
        sessionFacade.saveOrUpdate(projTemplate);
    }

    @Override
    public void submitProjectTemplateFlow(ProjTemplate projTemplate, Map<String, Object> variables, String processDefinitionKey, TSUserDto curUser, String orgId) {
        if(!CommonUtil.isEmpty(projTemplate.getId())){
            projTemplate = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class,projTemplate.getId());
        }

        String creator = curUser.getUserName();
        if (!CommonUtil.isEmpty(projTemplate.getProcessInstanceId())) {
            List<TaskDto> nextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    projTemplate.getProcessInstanceId(),creator);
            // 判断是否有流程（暂停或关闭流程），如有：结束流程
            if (!CommonUtil.isEmpty(nextTasks)) {
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                        projTemplate.getId(), projTemplate.getProcessInstanceId(), "delete");
            }
        }

        FeignJson j = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                processDefinitionKey, projTemplate.getId(), variables);
        String procInstId = j.getObj().toString(); // 流程实例ID
        List<TaskDto> tasks = null;
        try {
            tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    procInstId, creator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String taskId = tasks.get(0).getId();

        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

        FeignJson nextTask = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = nextTask.getObj().toString();

        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
        myStartedTask.setTaskNumber(projTemplate.getId());
        myStartedTask.setType(processDefinitionKey);
        myStartedTask.setCreater(creator);
        myStartedTask.setCreaterFullname(curUser.getRealName());
        myStartedTask.setStartTime(new Date());
        myStartedTask.setProcType(processDefinitionKey);
        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        // 流程已发起列表中：增加对象名称的记录，流程任务名称规范化
        myStartedTask.setTitle(ProcessUtil.getProcessTitle(projTemplate.getProjTmplName(),
                BpmnConstants.BPMN_PROJECTTEMPLATE_APPLY_DISPLAYNAME, procInstId));

        myStartedTask.setObjectName(projTemplate.getProjTmplName());

        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

        projTemplate.setProcessInstanceId(procInstId);
        projTemplate.setBizCurrent(BpmnConstants.PROJTEMPLATE_SHENHE);
        CommonInitUtil.initGLObjectForUpdate(projTemplate,curUser,orgId);
        /*planTemplate.setIsRefuse(ProjectConstants.NORMAL);*/
        sessionFacade.updateEntitie(projTemplate);
    }

    @Override
    public void submitProjectTemplateFlowAagin(ProjTemplate projTemplate, TSUserDto curUser, String orgId) {
        projTemplate = (ProjTemplate)sessionFacade.getEntity(ProjTemplate.class,projTemplate.getId());
        String creator = curUser.getUserName();
        FeignJson fj = workFlowFacade.getWorkFlowTodoTaskService() .getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                projTemplate.getProcessInstanceId(), creator);
        String taskId = String.valueOf(fj.getObj());
        // 判断当前用户是否有流程（对应项目的启动、暂停、恢复、关闭流程），如有：重新提交启动流程
        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);

            Map<String,Object> params = new HashMap<>();
            params.put("variables",variables);
            params.put("userId",curUser.getId());

            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    projTemplate.getProcessInstanceId());
            String status = newNextTasks.getObj().toString();
            // 更新我的已发起流程状态
            updateMyStartedTaskStatus(status, projTemplate);

            /*// 更新项目状态
            project.setStatus(ProjectConstants.APPROVING);
            project.setIsRefuse(ProjectConstants.NORMAL);
            sessionFacade.updateEntitie(project);*/

        }
    }


    /**
     * 更新我的已发起流程状态
     *
     * @param status
     *            状态
     * @param projTemplate
     *            项目
     * @return
     * @see
     */
    private void updateMyStartedTaskStatus(String status, ProjTemplate projTemplate) {
        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                projTemplate.getId(), projTemplate.getProcessInstanceId());
        myStartedTask.setStatus(status);
        workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
    }

    @Override
    public List<ProjTemplate> searchProjTemplate(ProjTemplate projTemplate) {
        /*CriteriaQuery cq = new CriteriaQuery(ProjTemplate.class);
        HqlGenerateUtil.installHql(cq, projTemplate, null);*/
        List<ProjTemplate> list = sessionFacade.findHql("from ProjTemplate where bizCurrent = ? and avaliable = '1' order by createTime desc",projTemplate.getBizCurrent());
        return list;
    }

    @Override
    public String SaveAsTemplate(ProjTemplate projTemplate, Project project, List<String> plans, List<String> teams, List<String> libs, List<String> libPower, String projectId, TSUserDto userDto, String orgId) {
        CommonInitUtil.initGLObjectForCreate(projTemplate,userDto,orgId);
        initBusinessObject(projTemplate);
        projTemplate.setBizCurrent(ProjectConstants.PROJTEMPLATE_NIZHL);
        if(StringUtils.isNotBlank(projTemplate.getProjTmplNumber())){
            projTemplate.setProjTmplNumber(projTemplate.getProjTmplName());
        }

        sessionFacade.save(projTemplate);
        String templateId =projTemplate.getId();
        String teamId = projectService.addTeam(templateId);

        RepLibraryDto repLibraryDto = new RepLibraryDto();
        repLibraryDto.setTeamId(teamId);
        repLibraryDto.setTeamName(teamId);
        repLibraryDto.setLibType(ProjectConstants.PROJECTTEMPLATE);
        repLibraryDto.setLibName(templateId);
        FeignJson repLibraryJson = repService.addRepLibrary(appKey,repLibraryDto);
        String libId = String.valueOf(repLibraryJson.getObj());

        saveTeamLink(templateId, teamId, libId,userDto,orgId);

        HashMap<String, String> idMap = new HashMap<String, String>();
        // 保存计划
        if (plans != null && (plans.get(0).equals("on") || plans.get(0).equals("true"))) {
            projTemplateDetailService.saveProjTemplateDetailByPlan(projectId, null, projTemplate,userDto,orgId);
        }
        // 保存团队角色
        if (teams != null && (teams.get(0).equals("on") || teams.get(0).equals("true"))) {
            projTemplateRoleService.batchInitSaveRoles(project, templateId);
        }
        // 保存项目库
        if (libs != null && (libs.get(0).equals("on") || libs.get(0).equals("true"))) {
            idMap = projTemplateLibService.batchInitSaveLib(projectId, templateId,userDto);
        }
        // 保存项目库目录权限
        if (libPower != null && (libPower.get(0).equals("on") || libPower.get(0).equals("true"))) {
            saveRoleFileAuth(projectId, projTemplate, templateId, idMap);
        }
        return templateId;
    }

    private void saveRoleFileAuth(String projectId, ProjTemplate projTemplate, String templateId,
                                  HashMap<String, String> idMap) {
        String teamId = projRoleService.getTeamIdByProjectId(projectId);
        String libId = projRoleService.getLibIdByProjectId(projectId);
        String templateLibId = projTemplateLibService.getLibIdByTemplateId(templateId);

        List<RepFileDto> roots = repService.getRootDirsByParams(appKey,libId,"0",libId);
        List<RepFileDto> files = new ArrayList<RepFileDto>();
        files.addAll(roots);
        if (!CommonUtil.isEmpty(roots) && files.size() == 1) {
            RepFileDto root = roots.get(0);
            files = orderByParent(root, files);
        }

        List<RepFileDto> roots_ = repService.getRootDirsByParams(appKey,templateLibId,"0",templateLibId);
        List<RepFileDto> files_ = new ArrayList<RepFileDto>();
        files_.addAll(roots_);
        if (!CommonUtil.isEmpty(roots_) && files_.size() == 1) {
            RepFileDto root = roots_.get(0);
            files_ = orderByParent(root, files_);
        }

        List<RepRoleFileAuthDto> allFileAuthsList = new ArrayList<RepRoleFileAuthDto>();
        allFileAuthsList = repService.getAllFileAuthsList(libId);
        for (RepFileDto f : files) {
            List<RepRoleFileAuthDto> roleFileAuthsList = new ArrayList<RepRoleFileAuthDto>();
            if(!CommonUtil.isEmpty(allFileAuthsList)){
                String id = f.getId();
                for(RepRoleFileAuthDto auth: allFileAuthsList){
                    if(StringUtil.equals(auth.getFileId(), id)){
                        roleFileAuthsList.add(auth);
                    }
                }
            }
            Collections.sort(roleFileAuthsList, new Comparator<RepRoleFileAuthDto>() {
                @Override
                public int compare(RepRoleFileAuthDto o1, RepRoleFileAuthDto o2) {
                    return o1.getRoleId().compareTo(o2.getRoleId());
                }});
            for (RepFileDto ff : files_) {
                if (idMap.get(f.getId()).equalsIgnoreCase(ff.getId())) {
                    copyRoleFileAuth(roleFileAuthsList, ff.getId());
                }
            }
        }
    }

    @Override
    public void saveRoleFileAuth2Project(String projectId, String templateId, HashMap<String, String> idMap, TSUserDto userDto, String orgId) {
        String templateLibId = projTemplateLibService.getLibIdByTemplateId(templateId);
        String libId= projRoleService.getLibIdByProjectId(projectId);
        List<FdTeamRoleDto> roles = teamService.getRoleByTeamId(appKey,projectId);

        // 获得项目模板所有项目库目录文件夹
        List<RepFileDto> roots = repService.getRootDirsByParams(appKey,templateLibId,"0",templateLibId);
        List<RepFileDto> files = new ArrayList<RepFileDto>();
        files.addAll(roots);
        if (!CommonUtil.isEmpty(roots) && files.size() == 1) {
            RepFileDto root = roots.get(0);
            files = orderByParent(root, files);
        }

        // 获得新建项目的所有项目库目录文件夹
        List<RepFileDto> roots_ = repService.getRootDirsByParams(appKey,libId,"0",libId);
        List<RepFileDto> files_ = new ArrayList<RepFileDto>();
        files_.addAll(roots_);
        if (!CommonUtil.isEmpty(roots_) && files_.size() == 1) {
            RepFileDto root = roots_.get(0);
            files_ = orderByParent(root, files_);
        }
        // 复制目录文件夹角色权限到项目 的项目库文件夹中
        for (RepFileDto f : files) {
            List<RepRoleFileAuthDto> roleFileAuthsList = repService.getRepRoleFileAuthList(f.getId());
            for (RepFileDto ff : files_) {
                if (idMap.get(f.getId()).equalsIgnoreCase(ff.getId())) {
                    copyRoleFileAuth(roleFileAuthsList, ff.getId());
                }
            }
        }
    }

    @Override
    public String saveProjectTemplateDetailByExcel(ProjTemplate projTemplate, List<PlanTemplateExcelVo> dataList, Map<String, PlanTemplateExcelVo> excelMap, Map<String, String> planLevelMap, String switchStr, TSUserDto userDto, String orgId) {
        List<Plan> templateDetailList = new ArrayList<Plan>();
        //存放计划模板中 批量插入的计划信息
        List<Plan> detailInsert=new ArrayList<Plan>();
        PlanTemplateDetail d = new PlanTemplateDetail();
        initBusinessObject(d);
        String bizCurrent = d.getBizCurrent();
        String bizVersion = d.getBizVersion();
        LifeCyclePolicy policy = d.getPolicy();


        DeliverablesInfo dbi = new DeliverablesInfo();
        deliverablesInfoService.initBusinessObject(dbi);
        String bizCurrentdbi = dbi.getBizCurrent();
        String bizVersiondbi = dbi.getBizVersion();
        LifeCyclePolicy policydbi = dbi.getPolicy();
        //记录 计划id+交付项名称:输出ID
        Map<String, String> deliverMap = new HashMap<String, String>();
        Map<String, String> nameDeliverablesMap = nameStandardService.getNameDeliverysMap();
        Map<String, String> preposeInput = new HashMap<String, String>();

//        Integer storeyNo = 0;
        int maxPlanNumber = 0;
        int maxStoreyNo = 0;
        /*if (!needDelete) {
            maxPlanNumber = getMaxPlanNumber(plan);
            maxStoreyNo = getMaxStoreyNo(plan);
        }*/
        for (PlanTemplateExcelVo vo : dataList) {
            Plan detail = new Plan();
            CommonUtil.glObjectSet(detail);
            detail.setId(vo.getId());
//            detail.setPlanNumber(Integer.valueOf(vo.getPlanNumber()));
//            detail.setPlanTemplateId(projectTemplate.getId());
            detail.setPlanName(vo.getPlanName());
            detail.setWorkTime(vo.getWorktime());
            List<ActivityTypeManage> activityTypeManages = activityTypeManageEntityService.queryActivityTypeManageByName(vo.getTaskNameType());
            String taskNameType = "";
            if(!CommonUtil.isEmpty(activityTypeManages)){
                taskNameType = activityTypeManages.get(0).getId();
            }
            detail.setTaskNameType(taskNameType);
            List<TabCombinationTemplate> tabList = tabCbTemplateEntityService.findTabCbTemplatesByActivityId(taskNameType);
            if(!CommonUtil.isEmpty(tabList)){
                detail.setTabCbTemplateId(tabList.get(0).getId());
            }
            if(!CommonUtil.isEmpty(vo.getParentNo())) {
                detail.setParentPlanId(excelMap.get(vo.getParentNo()).getId());
            }
            if(PlanConstants.PLAN_MILESTONE_TRUE.equals(vo.getMilestone())) {
                detail.setMilestone("true");
            } else {
                detail.setMilestone("false");
            }
            if(!CommonUtil.isEmpty(vo.getIsNecessary())) {
                //是否必要
                if(PlanConstants.PLAN_MILESTONE_TRUE.equals(vo.getMilestone())) {
                    detail.setMilestone("true");
                } else {
                    detail.setMilestone("false");
                }
            }
            if(!CommonUtil.isEmpty(vo.getPlanLevel())) {
                detail.setPlanLevel(planLevelMap.get(vo.getPlanLevel()));
            }
            detail.setPlanNumber(Integer.valueOf(vo.getPlanNumber()));
            detail.setBizCurrent(bizCurrent);
            detail.setBizVersion(bizVersion);
            detail.setPolicy(policy);
            detail.setBizId(UUID.randomUUID().toString());
            detail.setFirstTime(new Date());
            Short securityLevel=1;
            detail.setSecurityLevel(securityLevel);

            // 如果导入模板时计划节点需要重新排序
            // 每次都是最大的planNumber+1
            detail.setPlanNumber(maxPlanNumber + 1);
            maxPlanNumber++ ;

            detail.setStoreyNo(maxStoreyNo + 1);
            maxStoreyNo++ ;

            detailInsert.add(detail);
            //输出
            List<DeliverablesInfo> deliverables = deliverablesInfoService.getDeliverablesInfoByPlanTemplateExcel(vo, switchStr,
                    bizCurrentdbi, bizVersiondbi, policydbi, deliverMap, nameDeliverablesMap.get(detail.getPlanName()),userDto,orgId);
            if (!CommonUtil.isEmpty(deliverables)) {
//                deliverablesInsert.addAll(deliverables);
                detail.setDeliInfoList(deliverables);
            }
            //前置计划
            if(!CommonUtil.isEmpty(vo.getPreposeNos())) {
                List<PreposePlanTemplate> preposePlans = preposePlanTemplateService.getPreposePlanInfoByPlanTemplateExcel(vo, excelMap,
                        preposeInput, detail,userDto,orgId);
                /*if (!CommonUtil.isEmpty(preposePlans)) {
                    preposePlanInsert.addAll(preposePlans);

                }   */
            }
            templateDetailList.add(detail);
        }
        //输入
        Map<String, List<Inputs>> inputsMap = new HashMap<String, List<Inputs>>();
        inputsMap = inputsService.getInputsInfoByPlanTemplateExcel(preposeInput, excelMap, deliverMap,userDto,orgId);
        for(Plan d1 : templateDetailList) {
            if(!CommonUtil.isEmpty(inputsMap.get(d1.getId()))) {
                d1.setInputList(inputsMap.get(d1.getId()));
                redisService.setToRedis("INPUTSLIST", d1.getId(), inputsMap.get(d1.getId()));
            }
        }
        //savePlanTemplateAllByList(detailInsert, deliverablesInsert, preposePlanInsert, inputsInsert);
        String detailStr = JSON.toJSONString(templateDetailList);
        redisService.setToRedis("PROJTMPPLANLIST", projTemplate.getId(), detailStr);
        return projTemplate.getId();
    }
}