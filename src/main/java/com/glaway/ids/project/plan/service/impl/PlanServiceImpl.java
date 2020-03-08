package com.glaway.ids.project.plan.service.impl;


import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.activiti.core.dto.MyStartedTaskDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSDepartDto;
import com.glaway.foundation.common.dto.TSTypeDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.util.param.GWConstants;
import com.glaway.foundation.common.util.param.JudgeRangeParam;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.TaskDto;
import com.glaway.foundation.fdk.dev.dto.outwardextension.OutwardExtensionDto;
import com.glaway.foundation.fdk.dev.dto.outwardextension.OutwardExtensionUrlDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.service.FeignOutwardExtensionService;
import com.glaway.foundation.fdk.dev.service.FeignSystemService;
import com.glaway.foundation.fdk.dev.service.calendar.FeignCalendarService;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignDepartService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignGroupService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.common.constant.PluginConstants;
import com.glaway.ids.common.service.PluginValidateServiceI;
import com.glaway.ids.config.auth.ProjLibAuthManager;
import com.glaway.ids.config.constant.ConfigStateConstants;
import com.glaway.ids.config.constant.ConfigTypeConstants;
import com.glaway.ids.config.constant.NameStandardSwitchConstants;
import com.glaway.ids.config.constant.SwitchConstants;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.ids.constant.*;
import com.glaway.ids.message.notice.service.PlanAssignBackMsgNoticeI;
import com.glaway.ids.message.notice.service.PlanFinishMsgNoticeI;
import com.glaway.ids.message.notice.service.PlanInvalidMsgNoticeI;
import com.glaway.ids.models.HttpClientUtil;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.ActivityTypeManageEntityServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.TabCbTemplateEntityServiceI;
import com.glaway.ids.project.menu.dto.ProjectDto;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.*;
import com.glaway.ids.project.plan.vo.PlanAuthorityVo;
import com.glaway.ids.project.plan.vo.PlanExcelSaveVo;
import com.glaway.ids.project.plan.vo.PlanExcelVo;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.service.PlanTemplateDetailServiceI;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateDetailReq;
import com.glaway.ids.project.planview.service.PlanViewServiceI;
import com.glaway.ids.project.planview.util.TimeConditionUtil;
import com.glaway.ids.project.projectmanager.entity.ProjDocRelation;
import com.glaway.ids.project.projectmanager.service.ProjLibAuthServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjectServiceI;
import com.glaway.ids.review.service.ReviewFeignServiceI;
import com.glaway.ids.review.vo.ReviewBaseInfoVo;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.CommonInitUtil;
import com.glaway.ids.util.ProcessUtil;
import com.glaway.ids.util.mpputil.MppInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


/**
 * 项目计划
 *
 * @author blcao
 * @version 2015年3月24日
 * @see PlanServiceImpl
 * @since
 */
@Service("planService")
@Transactional(propagation = Propagation.REQUIRED)
public class PlanServiceImpl extends BusinessObjectServiceImpl<Plan> implements PlanServiceI {

    private static final SystemLog log = BaseLogFactory.getSystemLog(PlanServiceImpl.class);

    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;

    @Autowired
    private PlanTemplateDetailServiceI planTemplateDetailService;

    /**
     * 配置业务接口
     */
    @Autowired
    private PlanBusinessConfigServiceI businessConfigService;

    /**
     * 项目角色人员服务实现接口<br>
     */
    @Autowired
    private ProjRoleServiceI projRoleService;

    /**
     * 项目管理
     */
    @Autowired
    private ProjectServiceI projectService;

    @Autowired
    private InputsServiceI inputsService;

    @Autowired
    private NameStandardRemoteFeignServiceI nameStandardService;

    @Autowired
    private ProjLibServiceI projLibService;

    /**
     * 注入WorkFlowFacade
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;


    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private PlanChangeServiceI planChangeService;



    @Autowired
    private PlanViewServiceI planViewService;

    @Autowired
    private ProjLibAuthServiceI projLibAuthService;


    @Autowired
    private FeignUserService userService;

    @Autowired
    private FeignRepService repService;

    @Autowired
    private ResourceLinkInfoRemoteFeignServiceI resourceLinkInfoService;

    @Autowired
    private PreposePlanServiceI preposePlanService;

    @Autowired
    private NameStandardFeignService nameStandardFeignService;

    /**
     * 交付物信息
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;


    @Autowired
    private ReviewFeignServiceI reviewFeignService;

    /**
     * 资源每日使用信息
     */
    @Autowired
    private ResourceEverydayuseFeignServiceI resourceEverydayuseService;

    @Autowired
    private FeignGroupService groupService;

    @Autowired
    private FeignCalendarService calendarService;

    @Autowired
    private ParamSwitchServiceI paramSwitchService;

    @Autowired
    private FeignSystemService feignSystemService;

    @Autowired
    private FeignOutwardExtensionService outwardExtensionService;

    /**
     * 计划反馈
     */
    @Autowired
    private PlanFeedBackServiceI planFeedBackService;

    /**
     * 计划日志
     */
    @Autowired
    private PlanLogServiceI planLogService;


    @Autowired
    private FeignDepartService departService;

    /**
     * 配置业务接口
     */
    @Autowired
    private PlanFlowForworkServiceI planFlowForworkService;


    @Autowired
    private DeliverablesInfoServiceI deliveryStandardService;

    @Autowired
    private ActivityTypeManageEntityServiceI activityTypeManageEntityService;

    @Autowired
    private PluginValidateServiceI pluginValidateService;

    @Autowired
    private RdFlowTaskFlowResolveRemoteFeignServiceI rdFlowTaskFlowResolveFeignService;

    @Autowired
    private TabCbTemplateEntityServiceI tabCbTemplateEntityService;

    @Autowired
    private PlanExcuteServiceI planExcuteServiceI;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;



    @Value(value="${spring.application.name}")
    private String appKey;

    @Autowired
    private Environment env;

    /**
     * 获取废弃消息的接口
     */
    @Autowired
    private PlanInvalidMsgNoticeI planInvalidMsgNotice;

    /**
     * 注入项目planService
     */
    @Autowired
    private PlanAssignBackMsgNoticeI planAssignBackMsgNotice;

    @Autowired
    private PlanFinishMsgNoticeI planFinishMsgNotice;

    @Override
    public Plan getEntity(String id) {
        Plan plan = (Plan) sessionFacade.getEntity(Plan.class,id);
        return plan;
    }


    @Override
    public List<Plan> queryPlanList(Plan plan, int page, int rows, boolean isPage) {
        List<Plan> list = new ArrayList<>();
        String hql = createHql(plan);
        if (isPage) {
            list =  sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        list =  sessionFacade.findByQueryString(hql);

        return list;
    }

    @Override
    public PlanownerApplychangeInfo getPlanownerApplychangeInfo(String id) {
        return (PlanownerApplychangeInfo) sessionFacade.getEntity(PlanownerApplychangeInfo.class,id);
    }

    @Override
    public long getCount(PlanDto plan) {
        Plan pla = JSON.parseObject(JSON.toJSONString(plan),Plan.class);
        String hql = "select count(*) " + createHql(pla);
        return sessionFacade.getCount(hql, null);
    }

    @Override
    public UserPlanViewProject getUserViewProjectLinkByProjectId(String projectId,String userId) {
        UserPlanViewProject link = new UserPlanViewProject();
        List<UserPlanViewProject> linkList = sessionFacade.findHql("from UserPlanViewProject where userId=?",userId);
        if(!CommonUtil.isEmpty(linkList)){
            link = linkList.get(0);
        }
        return link;
    }

    @Override
    public void saveUserViewProjectLink(String projectId, String planViewId,TSUserDto curUser,String orgId) {
        int count = planViewService.getPlanViewTypeById(planViewId);

        List<UserPlanViewProject> linkList = new ArrayList<UserPlanViewProject>();
//        if(count == 0){
        linkList = sessionFacade.findHql("from UserPlanViewProject where  userId = ? ",
                curUser.getId());
//        }else{
//            linkList = sessionFacade.findHql("from UserPlanViewProject where  userId = ? and projectId = ?",
//                UserUtil.getInstance().getUser().getId(),projectId);
//        }

        if(!CommonUtil.isEmpty(linkList)){
            sessionFacade.deleteAllEntitie(linkList);
        }
        UserPlanViewProject link = new UserPlanViewProject();
        link.setPlanViewInfoId(planViewId);
        if(count != 0){
            link.setProjectId(projectId);
        }

        link.setUserId(curUser.getId());
        CommonInitUtil.initGLObjectForCreate(link,curUser,orgId);
        sessionFacade.save(link);
    }

    @Override
    public List<PlanDto> queryPlanListForTreegridView(PlanDto plan, String planViewId, List<ConditionVO> conditionList, String userName, String progressRate, String workTime,String userId) {
        StringBuffer hqlBuffer = createStringBuffer(plan.getProjectId());
        StringBuffer planIds = new StringBuffer();
        if(!CommonUtil.isEmpty(planViewId)){
            PlanViewInfo view = (PlanViewInfo)sessionFacade.getEntity(PlanViewInfo.class, planViewId);
            if(!CommonUtil.isEmpty(view)){
                List<PlanViewSearchCondition> searchConditionList = sessionFacade.findHql("from PlanViewSearchCondition where planViewInfoId=?", view.getId());
                for (PlanViewSearchCondition searchCondition : searchConditionList) {

                    if (searchCondition.getAttributeName().equals("Plan.progressRate")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            if ("-1".equals(searchCondition.getAttributeValue()) && searchCondition.getAttributeCondition().equals("le")) {
                                hqlBuffer.append("  and PL.progressRate = " + searchCondition.getAttributeValue());
                            }
                            else if ("-1".equals(searchCondition.getAttributeValue()) && searchCondition.getAttributeCondition().equals("ge")) {

                            }
                            else {
                                // int progressRateNumber = (Integer.parseInt(progressRate));
                                if (searchCondition.getAttributeCondition().equals("le")) {
                                    hqlBuffer.append(" and (PL.progressRate is null");
                                    hqlBuffer.append(" or PL.progressRate <= " + searchCondition.getAttributeValue() + ")");
                                }
                                else {
                                    hqlBuffer.append(" and PL.progressRate >= " + searchCondition.getAttributeValue());
                                }
                            }
                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.workTime")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            if ("-1".equals(searchCondition.getAttributeValue()) && searchCondition.getAttributeCondition().equals("le")) {
                                hqlBuffer.append(" and PL.workTime = " + searchCondition.getAttributeValue());
                            }
                            else if ("-1".equals(searchCondition.getAttributeValue()) && searchCondition.getAttributeCondition().equals("ge")) {

                            }
                            else {
                                // int progressRateNumber = (Integer.parseInt(progressRate));
                                if (searchCondition.getAttributeCondition().equals("le")) {
                                    hqlBuffer.append(" and (PL.workTime is null");
                                    hqlBuffer.append(" or PL.workTime <= " + searchCondition.getAttributeValue() + ")");
                                }
                                else {
                                    hqlBuffer.append(" and PL.workTime >= " + searchCondition.getAttributeValue());
                                }
                            }
                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.planStartTime")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            String[] attr = searchCondition.getAttributeValue().split(",");
                            hqlBuffer.append(" and to_date(to_char(PL.planStartTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                                    + attr[0]
                                    + "','yyyy-MM-dd') and to_date('"
                                    + attr[1]
                                    + "','yyyy-MM-dd')");
                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.planEndTime")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            String[] attr = searchCondition.getAttributeValue().split(",");
                            hqlBuffer.append(" and to_date(to_char(PL.planEndTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                                    + attr[0]
                                    + "','yyyy-MM-dd') and to_date('"
                                    + attr[1]
                                    + "','yyyy-MM-dd')");
                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.planLevelInfo.id")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            String[] attr = searchCondition.getAttributeValue().split(",");
                            String str = "";
                            for (String s : attr) {
                                if (StringUtils.isNotEmpty(s)) {
                                    if (StringUtils.isNotEmpty(str.trim())) {
                                        str = str + ",'" + s.trim() + "'";
                                    }
                                    else {
                                        str = "'" + s.trim() + "'";
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(str.trim())) {
                                hqlBuffer.append(" and PL.PLANLEVEL in (" + str + ")");
                            }
                        }

                    }
                    else if (searchCondition.getAttributeName().equals("Plan.planNumber")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            hqlBuffer.append(" and PL.planNumber like '%" + searchCondition.getAttributeValue() + "%'");
                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.planName")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            hqlBuffer.append(" and PL.planName like '%" + searchCondition.getAttributeValue() + "%'");
                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.bizCurrent")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            String[] attr = searchCondition.getAttributeValue().split(",");
                            String str = "";
                            for (String s : attr) {
                                if (StringUtils.isNotEmpty(s)) {
                                    if (StringUtils.isNotEmpty(str.trim())) {
                                        str = str + ",'" + s.trim() + "'";
                                    }
                                    else {
                                        str = "'" + s.trim() + "'";
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(str.trim())) {
                                hqlBuffer.append(" and PL.bizCurrent in (" + str + ")");
                            }
                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.isDelay")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            String[] attr = searchCondition.getAttributeValue().split(",");
                            String currentDateStr = DateUtil.getCurrDate("yyyy-MM-dd");
                            if(attr.length < 2){
                                for (String s : attr) {
                                    if (StringUtils.isNotEmpty(s.trim())) {
                                        if("DELAY".equals(s)){
                                            hqlBuffer.append(" and PL.planEndTime <= to_date('" + currentDateStr + "','yyyy-mm-dd') and PL.bizCurrent <> 'FINISH' and PL.bizCurrent <> 'INVALID'");
                                        }
                                        if("NORMAL".equals(s)){
                                            hqlBuffer.append(" and PL.id not in (select t.id from pm_plan t where t.planEndTime <= to_date('" + currentDateStr + "','yyyy-mm-dd') and t.bizCurrent <> 'FINISH' and t.bizCurrent <> 'INVALID')");
                                        }
                                    }
                                }
                            }

                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.taskNameType")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            String[] attr = searchCondition.getAttributeValue().split(",");
                            String str = "";
                            for (String s : attr) {
                                if (StringUtils.isNotEmpty(s)) {
                                    if (StringUtils.isNotEmpty(str.trim())) {
                                        str = str + ",'" + s.trim() + "'";
                                    }
                                    else {
                                        str = "'" + s.trim() + "'";
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(str.trim())) {
                                hqlBuffer.append(" and PL.taskNameType in (" + str + ")");
                            }
                        }
                    }
                    else if (searchCondition.getAttributeName().equals("Plan.taskType")) {
                        if (StringUtils.isNotEmpty(searchCondition.getAttributeValue())) {
                            String[] attr = searchCondition.getAttributeValue().split(",");
                            String str = "";
                            for (String s : attr) {
                                if (StringUtils.isNotEmpty(s)) {
                                    if (StringUtils.isNotEmpty(str.trim())) {
                                        str = str + ",'" + s.trim() + "'";
                                    }
                                    else {
                                        str = "'" + s.trim() + "'";
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(str.trim())) {
                                hqlBuffer.append(" and PL.taskType in (" + str + ")");
                            }
                        }
                    }
                    else if(searchCondition.getAttributeName().equals("myPlan")){
                        hqlBuffer.append(" and (PL.owner = '"+userId+"' or PL.assigner = '"+userId+"' or PL.createBy = '"+userId+"')");
                    }
                }
            }

            List<PlanViewSetCondition> setConditionList = sessionFacade.findHql("from PlanViewSetCondition where planViewInfoId=?", planViewId);
            if(!CommonUtil.isEmpty(setConditionList)){
                for(PlanViewSetCondition condition : setConditionList){
                    if(!CommonUtil.isEmpty(condition.getShowRange()) && !CommonUtil.isEmpty(condition.getTimeRange())){
                        String hql = TimeConditionUtil.toHql(condition, "PL");
                        hqlBuffer.append(hql);
                    }else if(!CommonUtil.isEmpty(condition.getPlanId())){
                        if(CommonUtil.isEmpty(planIds)) {
                            planIds.append(condition.getPlanId() + "'");
                        } else {
                            planIds.append(", '" + condition.getPlanId() + "'");
                        }
                    }
                }
            }
        }
        if(!CommonUtil.isEmpty(planIds)){
            hqlBuffer.append(" and PL.ID in ('" + planIds + ")");
        }

        if (StringUtils.isNotEmpty(userName) && userName != "null") {
            hqlBuffer.append("  and (OU.realName like '%" + userName + "%'");
            hqlBuffer.append("  or OU.userName like '%" + userName + "%')");
        }
        for (ConditionVO c : conditionList) {
            if (c.getKey().equals("Plan.progressRate")) {
                if (StringUtils.isNotEmpty(progressRate)) {
                    if ("-1".equals(progressRate) && c.getCondition().equals("le")) {
                        hqlBuffer.append("  and PL.progressRate = " + progressRate);
                    }
                    else if ("-1".equals(progressRate) && c.getCondition().equals("ge")) {

                    }
                    else {
                        // int progressRateNumber = (Integer.parseInt(progressRate));
                        if (c.getCondition().equals("le")) {
                            hqlBuffer.append(" and (PL.progressRate is null");
                            hqlBuffer.append(" or PL.progressRate <= " + progressRate + ")");
                        }
                        else {
                            hqlBuffer.append(" and PL.progressRate >= " + progressRate);
                        }
                    }
                }
            }
            else if (c.getKey().equals("Plan.workTime")) {
                if (StringUtils.isNotEmpty(workTime)) {
                    if ("-1".equals(workTime) && c.getCondition().equals("le")) {
                        hqlBuffer.append(" and PL.workTime = " + workTime);
                    }
                    else if ("-1".equals(workTime) && c.getCondition().equals("ge")) {

                    }
                    else {
                        // int progressRateNumber = (Integer.parseInt(progressRate));
                        if (c.getCondition().equals("le")) {
                            hqlBuffer.append(" and (PL.workTime is null");
                            hqlBuffer.append(" or PL.workTime <= " + workTime + ")");
                        }
                        else {
                            hqlBuffer.append(" and PL.workTime >= " + workTime);
                        }
                    }
                }
            }
            else if (c.getKey().equals("Plan.planStartTime")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValue().split(",");
                    hqlBuffer.append(" and to_date(to_char(PL.planStartTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }
            else if (c.getKey().equals("Plan.planEndTime")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValue().split(",");
                    hqlBuffer.append(" and to_date(to_char(PL.planEndTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }
            else if (c.getKey().equals("Plan.planLevelInfo.id")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.PLANLEVEL in (" + str + ")");
                    }
                }

            }
            else if (c.getKey().equals("Plan.planNumber")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    hqlBuffer.append(" and PL.planNumber like '%" + c.getValue() + "%'");
                }
            }
            else if (c.getKey().equals("Plan.planName")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    hqlBuffer.append(" and PL.planName like '%" + c.getValue() + "%'");
                }
            }
            else if (c.getKey().equals("Plan.bizCurrent")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.bizCurrent in (" + str + ")");
                    }
                }
            }
            else if (c.getKey().equals("Plan.isDelay")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String currentDateStr = DateUtil.getCurrDate("yyyy-MM-dd");
                    if(attr.length < 2){
                        for (String s : attr) {
                            if (StringUtils.isNotEmpty(s.trim())) {
                                if("DELAY".equals(s)){
                                    hqlBuffer.append(" and PL.planEndTime <= to_date('" + currentDateStr + "','yyyy-mm-dd') and PL.bizCurrent <> 'FINISH' and PL.bizCurrent <> 'INVALID'");
                                }
                                if("NORMAL".equals(s)){
                                    hqlBuffer.append(" and PL.id not in (select t.id from pm_plan t where t.planEndTime <= to_date('" + currentDateStr + "','yyyy-mm-dd') and t.bizCurrent <> 'FINISH' and t.bizCurrent <> 'INVALID')");
                                }
                            }
                        }
                    }

                }
            }
            else if (c.getKey().equals("Plan.taskNameType")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.taskNameType in (" + str + ")");
                    }
                }
            }
            else if (c.getKey().equals("Plan.taskType")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.taskType in (" + str + ")");
                    }
                }
            }
        }

        hqlBuffer.append(" ORDER BY PL.PARENTPLANID, PL.STOREYNO, PL.CREATETIME");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        List<Plan> planList = changeQueryResToPlanList(objArrayList);
        List<PlanDto> dtoList = CodeUtils.JsonListToList(planList,PlanDto.class);
        return dtoList;
    }

    @Override
    public List<PlanDto> queryPlanListForTreegridWithView(List<PlanDto> planList, String planViewId, String userId) {
        List<PlanDto> planList1 = new ArrayList<PlanDto>();

        Map<String, List<TSDepartDto>> departList = departService.getAllTSDepartByCache(appKey,"");

        if(!CommonUtil.isEmpty(planViewId)){
            PlanViewInfo view = (PlanViewInfo)sessionFacade.getEntity(PlanViewInfo.class, planViewId);
            if(!CommonUtil.isEmpty(view)){
                List<PlanViewSetCondition> conditionList = sessionFacade.findHql("from PlanViewSetCondition where planViewInfoId = ?", view.getId());
                PlanViewSetCondition condition = new PlanViewSetCondition();
                if(!CommonUtil.isEmpty(conditionList)){
                    condition = conditionList.get(0);
                    if(!CommonUtil.isEmpty(condition.getDepartmentId())){
                        if(!CommonUtil.isEmpty(planList)){
                            for(PlanDto plan : planList){
                                if(!CommonUtil.isEmpty(plan.getOwner())&&!CommonUtil.isEmpty(departList.get(plan.getOwner()))){
                                    for(String departmentId : condition.getDepartmentId().split(",")){
                                        for(TSDepartDto dto : departList.get(plan.getOwner())){
                                            if(departmentId.equals(dto.getId())){
                                                planList1.add(plan);
                                                break;
                                            }
                                        }
                                    }

                                }else if(!CommonUtil.isEmpty(plan.getCreateBy())&&!CommonUtil.isEmpty(departList.get(plan.getCreateBy()))){
                                    for(String departmentId : condition.getDepartmentId().split(",")){
                                        for(TSDepartDto dto : departList.get(plan.getCreateBy())){
                                            if(departmentId.equals(dto.getId())){
                                                planList1.add(plan);
                                                break;
                                            }
                                        }
                                    }

                                }else if(!CommonUtil.isEmpty(plan.getAssigner())&&!CommonUtil.isEmpty(departList.get(plan.getAssigner())) ){
                                    for(String departmentId : condition.getDepartmentId().split(",")){
                                        for(TSDepartDto dto : departList.get(plan.getAssigner())){
                                            if(departmentId.equals(dto.getId())){
                                                planList1.add(plan);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        planList1 = planList;
                    }
                }
                //关注计划
                List<PlanDto> concernList = new ArrayList<PlanDto>();
                for (PlanDto plan : planList1){
                    plan.setConcernCode(getInfoByPlanIdAndUserId(plan.getId(), userId));
                    if (plan.getConcernCode().equals("1")&&!plan.getBizCurrent().equals("EDITING")){
                        concernList.add(plan);
                    }
                }
                if (view.getName().equals("关注计划")){
                    planList1 = concernList;
                }
            }

        }
        return planList1;
    }

    private String getInfoByPlanIdAndUserId(String planId, String userId){
        String exist = "1";
        String hql = "from ConcernPlanInfo c where c.planId ='"+planId+"' and c.userId ='"+userId+"'";
        List<ConcernPlanInfo> list = sessionFacade.findByQueryString(hql);
        if (CommonUtil.isEmpty(list)){
            exist = "0";
        }
        return exist;
    }

    @Override
    public List<PlanDto> queryPlanListForTreegrid(PlanDto plan) {
        StringBuffer hqlBuffer = createStringBuffer(plan.getProjectId());
        hqlBuffer.append(" ORDER BY PL.PARENTPLANID, PL.STOREYNO, PL.CREATETIME");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        List<Plan> planList =  changeQueryResToPlanList(objArrayList);
        List<PlanDto> dtoList = JSON.parseArray(JSON.toJSONString(planList),PlanDto.class);
        return dtoList;
    }

    @Override
    public String initPlan(PlanDto planDto) {
        Plan plan = JSON.parseObject(JSON.toJSONString(planDto),Plan.class);
        Plan p = initBusinessObject(plan);
        updateProgressRate(p);
        String json = JSON.toJSONString(p);
        return json;
    }

    @Override
    public String getLifeCycleStatusList() {
        Plan p = new Plan();
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
    public PlanDto getPlanEntity(String id) {
        Plan p = (Plan)sessionFacade.getEntity(Plan.class,id);
        PlanDto plan = new PlanDto();
        if (!CommonUtil.isEmpty(p)) {
            try {
                plan = (PlanDto)VDataUtil.getVDataByEntity(p,PlanDto.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            plan = null;
        }
        return plan;
    }

    @Override
    public List<Plan> queryPlansExceptInvalid(Plan plan) {
        String hql = "from Plan l where l.bizCurrent <> '" + PlanConstants.PLAN_INVALID + "'";
        // 计划ID
        if (StringUtils.isNotEmpty(plan.getId())) {
            hql = hql + " and l.id = '" + plan.getId() + "'";
        }
        // 所属项目
        if (StringUtils.isNotEmpty(plan.getProjectId())) {
            hql = hql + " and l.projectId = '" + plan.getProjectId() + "'";
        }
        // 父计划
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            hql = hql + " and l.parentPlanId = '" + plan.getParentPlanId() + "'";
        }
        // 计划名称
        if (StringUtils.isNotEmpty(plan.getPlanName())) {
            hql = hql + " and l.planName like '%" + plan.getPlanName() + "%'";
        }
        // 责任人
        if (StringUtils.isNotEmpty(plan.getOwner())) {
            hql = hql + " and l.owner = '" + plan.getOwner() + "'";
        }
        // 生命周期状态
        if (StringUtils.isNotEmpty(plan.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + plan.getBizCurrent() + "'";
        }
        // 下达人
        if (StringUtils.isNotEmpty(plan.getAssigner())) {
            hql = hql + " and l.assigner = '" + plan.getAssigner() + "'";
        }
        // 下达时间
        if (plan.getAssignTimeStart() != null) {
            hql = hql + " and l.assignTime >= " + plan.getAssignTimeStart();
        }
        if (plan.getAssignTimeEnd() != null) {
            hql = hql + " and l.assignTime <= " + plan.getAssignTimeEnd();
        }
        // 计划等级
        if (StringUtils.isNotEmpty(plan.getPlanLevel())) {
            hql = hql + " and l.planLevel = '" + plan.getPlanLevel() + "'";
        }

        // cellId
        if (StringUtils.isNotEmpty(plan.getCellId())) {
            hql = hql + " and l.cellId = '" + plan.getCellId() + "'";
        }

        // 开始时间
        if (plan.getPlanStartTimeStart() != null) {
            hql = hql + " and l.planStartTime >= " + plan.getPlanStartTimeStart();
        }
        if (plan.getPlanStartTimeEnd() != null) {
            hql = hql + " and l.planStartTime <= " + plan.getPlanStartTimeEnd();
        }
        // 结束时间
        if (plan.getPlanEndTimeStart() != null) {
            hql = hql + " and l.planEndTime >= " + plan.getPlanEndTimeStart();
        }
        if (plan.getPlanEndTimeEnd() != null) {
            hql = hql + " and l.planEndTime <= " + plan.getPlanEndTimeEnd();
        }
        // 是否可用
        if (StringUtils.isNotEmpty(plan.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + plan.getAvaliable() + "'";
        }
        hql = hql + " order by parentPlanId, createTime, storeyno";
        return sessionFacade.findByQueryString(hql);
    }

    @Override
    public FeignJson queryPlanIdAndNameMap(String parentPlanId) {
        FeignJson j = new FeignJson();
        // 查出子计划：
        Map<String, String> planIdAndplanNameMap = new HashMap<String, String>();
        Plan condition = new Plan();
        condition.setParentPlanId(parentPlanId);
        List<Plan> childrenlist = queryPlansExceptInvalid(condition);
        if (!CommonUtil.isEmpty(childrenlist)) {
            for (Plan curPlan : childrenlist) {
                planIdAndplanNameMap.put(curPlan.getId(), curPlan.getPlanName());
            }
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("planIdAndplanNameMap",planIdAndplanNameMap);
        j.setAttributes(map);
        return j;
    }

    @Override
    public String getOutPowerForPlanList(String folderId, String planId, String userId) {

        Plan plan = (Plan)sessionFacade.getEntity(Plan.class, planId);
        List<String> codeList = ProjLibAuthManager.getAllAuthActionCode();
        RepFileDto r = repService.getRepFileByRepFileId(appKey, folderId);
        Boolean detail = false;
        Boolean download = false;
        if (!CommonUtil.isEmpty(r)) {
            String createRep = r.getCreateBy();
            Boolean havePower = false;
            Boolean isCreate = false;
            String categoryFileAuths = projLibAuthService.getDocumentFileAuths(folderId, userId);
            if (!CommonUtil.isEmpty(plan.getOwner()) && plan.getOwner().equals(userId)) {
                return "downloadDetail";
            }
            if (StringUtil.isNotEmpty(categoryFileAuths)) {
                if (categoryFileAuths.contains("detail")) {
                    detail = true;
                }
                if (categoryFileAuths.contains("download")) {
                    download = true;
                }
            }
            else {
                detail = false;
                download = false;
            }
        }
        if (download == true) {
            return "downloadDetail";
        }
        if (detail == true && download == false) {
            return "detail";
        }
        return "";
    }


    /**
     * 将原生SQL查询的结果转化为Plan
     *
     * @param objArrayList
     * @return
     * @see
     */
    private List<Plan> changeQueryResToPlanList(List<Map<String, Object>> objArrayList) {
        List<Plan> list = new ArrayList<Plan>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            Map<String, TSUserDto> usersMap = userService.getCommonUserAll();
            Map<String, TSUserDto> userIdsMap = new HashMap<String, TSUserDto>();
            for (TSUserDto user : usersMap.values()) {
                userIdsMap.put(user.getId(), user);
            }
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("ID");
                if (StringUtils.isNotEmpty(id)) {
                    Plan p = new Plan();
                    p.setId(id);
                    String createBy = (String)map.get("CREATEBY");
                    if (StringUtils.isNotEmpty(createBy)) {
                        p.setCreateBy(createBy);
                        p.setCreateName(StringUtils.isNotEmpty((String)map.get("CREATENAME")) ? (String)map.get("CREATENAME") : "");
                        p.setCreateFullName(StringUtils.isNotEmpty((String)map.get("CREATEFULLNAME")) ? (String)map.get("CREATEFULLNAME") : "");
                        p.setCreateTime((Date)map.get("CREATETIME"));
                        if (StringUtils.isNotEmpty(createBy)) {
                            TSUserDto creator = userIdsMap.get(createBy);
                            if (creator != null) {
                                p.setCreator(creator);
                            }
                        }
                    }

                    p.setUpdateBy(StringUtils.isNotEmpty((String)map.get("UPDATEBY")) ? (String)map.get("UPDATEBY") : "");
                    p.setIsAssignBack(StringUtils.isNotEmpty((String)map.get("ISASSIGNBACK")) ? (String)map.get("ISASSIGNBACK") : "");
                    p.setIsAssignSingleBack(StringUtils.isNotEmpty((String)map.get("ISASSIGNSINGLEBACK")) ? (String)map.get("ISASSIGNSINGLEBACK") : "");
                    p.setIsChangeSingleBack(StringUtils.isNotEmpty((String)map.get("ISCHANGESINGLEBACK")) ? (String)map.get("ISCHANGESINGLEBACK") : "");
                    p.setUpdateName(StringUtils.isNotEmpty((String)map.get("UPDATENAME")) ? (String)map.get("UPDATENAME") : "");
                    p.setUpdateFullName(StringUtils.isNotEmpty((String)map.get("UPDATEFULLNAME")) ? (String)map.get("UPDATEFULLNAME") : "");
                    p.setUpdateTime((Date)map.get("UPDATETIME"));
                    p.setAvaliable(StringUtils.isNotEmpty((String)map.get("AVALIABLE")) ? (String)map.get("AVALIABLE") : "");
                    p.setBizCurrent(StringUtils.isNotEmpty((String)map.get("BIZCURRENT")) ? (String)map.get("BIZCURRENT") : "");
                    if(!CommonUtil.isEmpty(map.get("SECURITYLEVEL"))){
                        p.setSecurityLevel(Short.valueOf(map.get("SECURITYLEVEL").toString()));
                    }

                    if (StringUtils.isNotEmpty((String)map.get("POLICY_ID"))) {
                        LifeCyclePolicy policy = new LifeCyclePolicy();
                        policy.setId((String)map.get("POLICY_ID"));
                        p.setPolicy(policy);
                    }
                    String assigner = (String)map.get("ASSIGNER");
                    if (StringUtils.isNotEmpty(assigner)) {
                        p.setAssigner(assigner);
                        p.setAssignTime((Date)map.get("ASSIGNTIME"));
                        if (StringUtils.isNotEmpty(assigner)) {
                            TSUserDto assignerInfo = userIdsMap.get(assigner);
                            if (assignerInfo != null) {
                                p.setAssignerInfo(assignerInfo);
                            }
                        }
                    }

                    p.setFlowStatus(StringUtils.isNotEmpty((String)map.get("FLOWSTATUS")) ? (String)map.get("FLOWSTATUS") : "NORMAL");
                    p.setMilestone(StringUtils.isNotEmpty((String)map.get("MILESTONE")) ? (String)map.get("MILESTONE") : "false");

                    String owner = (String)map.get("OWNER");
                    if (StringUtils.isNotEmpty(owner)) {
                        p.setOwner(owner);
                        if (StringUtils.isNotEmpty(owner)) {
                            TSUserDto ownerInfo = userIdsMap.get(owner);
                            if (ownerInfo != null) {
                                p.setOwnerInfo(ownerInfo);
                            }
                        }
                    }

                    if (StringUtils.isNotEmpty((String)map.get("PARENTPLANID"))) {
                        p.setParentPlanId((String)map.get("PARENTPLANID"));
                        if (StringUtils.isNotEmpty((String)map.get("PARENT_ID"))) {
                            Plan parentPlan = new Plan();
                            parentPlan.setId((String)map.get("PARENT_ID"));
                            parentPlan.setCreateBy(StringUtils.isNotEmpty((String)map.get("PARENT_CREATEBY")) ? (String)map.get("PARENT_CREATEBY") : "");
                            parentPlan.setOwner(StringUtils.isNotEmpty((String)map.get("PARENT_OWNER")) ? (String)map.get("PARENT_OWNER") : "");
                            parentPlan.setAvaliable(StringUtils.isNotEmpty((String)map.get("PARENT_AVALIABLE")) ? (String)map.get("PARENT_AVALIABLE") : "");
                            parentPlan.setPlanName(StringUtils.isNotEmpty((String)map.get("PARENT_PLANNAME")) ? (String)map.get("PARENT_PLANNAME") : "");
                            parentPlan.setBizCurrent(StringUtils.isNotEmpty((String)map.get("PARENT_BIZCURRENT")) ? (String)map.get("PARENT_BIZCURRENT") : "");
                            parentPlan.setFlowStatus(StringUtils.isNotEmpty((String)map.get("PARENT_FLOWSTATUS")) ? (String)map.get("PARENT_FLOWSTATUS") : "NORMAL");
                            parentPlan.setPlanStartTime((Date)map.get("PARENT_PLANSTARTTIME"));
                            parentPlan.setPlanEndTime((Date)map.get("PARENT_PLANENDTIME"));
                            parentPlan.setPlanNumber(Integer.parseInt(map.get("PARENT_PLANNUMBER").toString()));
                            parentPlan.setPlanType(StringUtils.isNotEmpty((String)map.get("PARENT_PLANTYPE")) ? (String)map.get("PARENT_PLANTYPE") : "");
                            parentPlan.setCellId(StringUtils.isNotEmpty((String)map.get("PARENT_CELLID")) ? (String)map.get("PARENT_CELLID") : "");
                            parentPlan.setFromTemplate(StringUtils.isNotEmpty((String)map.get("PARENT_FROMTEMPLATE")) ? (String)map.get("PARENT_FROMTEMPLATE") : "false");
                            parentPlan.setFormId(StringUtils.isNotEmpty((String)map.get("PARENT_FORMID")) ? (String)map.get("PARENT_FORMID") : "");
                            p.setParentPlan(parentPlan);
                        }
                    }

                    p.setPlanStartTime((Date)map.get("PLANSTARTTIME"));
                    p.setPlanEndTime((Date)map.get("PLANENDTIME"));
                    p.setPlanName(StringUtils.isNotEmpty((String)map.get("PLANNAME")) ? (String)map.get("PLANNAME") : "");

                    if (StringUtils.isNotEmpty((String)map.get("PLANLEVEL"))) {
                        p.setPlanLevel((String)map.get("PLANLEVEL"));
                        if (StringUtils.isNotEmpty((String)map.get("LV_ID"))) {
                            BusinessConfig planLevelInfo = new BusinessConfig();
                            planLevelInfo.setId((String)map.get("LV_ID"));
                            planLevelInfo.setName(StringUtils.isNotEmpty((String)map.get("LV_NAME")) ? (String)map.get("LV_NAME") : "");
                            p.setPlanLevelInfo(planLevelInfo);
                        }
                    }

                    p.setPlanNumber(Integer.parseInt(map.get("PLANNUMBER").toString()));
                    p.setPlanOrder(StringUtils.isNotEmpty((String)map.get("PLANORDER")) ? (String)map.get("PLANORDER") : "");
                    p.setProgressRate(StringUtils.isNotEmpty((String)map.get("PROGRESSRATE")) ? (String)map.get("PROGRESSRATE") : "");

                    if (StringUtils.isNotEmpty((String)map.get("PROJECTID"))) {
                        p.setProjectId((String)map.get("PROJECTID"));
                        if (StringUtils.isNotEmpty((String)map.get("PROJ_ID"))) {
                            Project project = new Project();
                            project.setId((String)map.get("PROJ_ID"));
                            project.setAvaliable(StringUtils.isNotEmpty((String)map.get("PROJ_AVALIABLE")) ? (String)map.get("PROJ_AVALIABLE") : "");
                            project.setBizCurrent(StringUtils.isNotEmpty((String)map.get("PROJ_BIZCURRENT")) ? (String)map.get("PROJ_BIZCURRENT") : "");
                            project.setStartProjectTime((Date)map.get("PROJ_STARTPROJECTTIME"));
                            project.setEndProjectTime((Date)map.get("PROJ_ENDPROJECTTIME"));
                            project.setIsRefuse(StringUtils.isNotEmpty((String)map.get("PROJ_ISREFUSE")) ? (String)map.get("PROJ_ISREFUSE") : "");
                            project.setName(StringUtils.isNotEmpty((String)map.get("PROJ_NAME")) ? (String)map.get("PROJ_NAME") : "");
                            project.setProjectNumber(StringUtils.isNotEmpty((String)map.get("PROJ_PROJECTNUMBER")) ? (String)map.get("PROJ_PROJECTNUMBER") : "");
                            project.setProjectTimeType(StringUtils.isNotEmpty((String)map.get("PROJ_PROJECTTIMETYPE")) ? (String)map.get("PROJ_PROJECTTIMETYPE") : "");
                            project.setStatus(StringUtils.isNotEmpty((String)map.get("PROJ_STATUS")) ? (String)map.get("PROJ_STATUS") : "");
                            p.setProject(project);
                        }
                    }

                    p.setProjectStatus(StringUtils.isNotEmpty((String)map.get("PROJECTSTATUS")) ? (String)map.get("PROJECTSTATUS") : "");
                    p.setRemark(StringUtils.isNotEmpty((String)map.get("REMARK")) ? (String)map.get("REMARK") : "");
                    p.setStoreyNo(Integer.parseInt(map.get("STOREYNO").toString()));
                    p.setWorkTime(StringUtils.isNotEmpty((String)map.get("WORKTIME")) ? (String)map.get("WORKTIME") : "");
                    p.setFeedbackProcInstId(StringUtils.isNotEmpty((String)map.get("FEEDBACKPROCINSTID")) ? (String)map.get("FEEDBACKPROCINSTID") : "");
                    p.setFeedbackRateBefore(StringUtils.isNotEmpty((String)map.get("FEEDBACKRATEBEFORE")) ? (String)map.get("FEEDBACKRATEBEFORE") : "");
                    p.setPlanSource(StringUtils.isNotEmpty((String)map.get("PLANSOURCE")) ? (String)map.get("PLANSOURCE") : "");
                    p.setOpContent(StringUtils.isNotEmpty((String)map.get("OPCONTENT")) ? (String)map.get("OPCONTENT") : "");
                    p.setPlanType(StringUtils.isNotEmpty((String)map.get("PLANTYPE")) ? (String)map.get("PLANTYPE") : "");
                    p.setCellId(StringUtils.isNotEmpty((String)map.get("CELLID")) ? (String)map.get("CELLID") : "");
                    p.setRequired(StringUtils.isNotEmpty((String)map.get("REQUIRED")) ? (String)map.get("REQUIRED") : "");
                    p.setFromTemplate(StringUtils.isNotEmpty((String)map.get("FROMTEMPLATE")) ? (String)map.get("FROMTEMPLATE") : "false");
                    p.setFormId(StringUtils.isNotEmpty((String)map.get("FORMID")) ? (String)map.get("FORMID") : "");
                    p.setWorkTimeReference(StringUtils.isNotEmpty((String)map.get("WORKTIMEREFERENCE")) ? (String)map.get("WORKTIMEREFERENCE") : "0");

                    String launcher = (String)map.get("LAUNCHER");
                    if (StringUtils.isNotEmpty(launcher)) {
                        p.setLauncher(launcher);
                        p.setLaunchTime((Date)map.get("LAUNCHTIME"));
                        if (StringUtils.isNotEmpty(launcher)) {
                            TSUserDto launcherInfo = userIdsMap.get(launcher);
                            if (launcherInfo != null) {
                                p.setLauncherInfo(launcherInfo);
                            }
                        }
                    }

                    p.setPreposePlans(StringUtils.isNotEmpty((String)map.get("PREPOSEPLANS")) ? (String)map.get("PREPOSEPLANS") : "");
                    p.setFlowFlag(StringUtils.isNotEmpty((String)map.get("FLOWFLAG")) ? (String)map.get("FLOWFLAG") : "");
                    p.setIsCreateByPmo(Boolean.valueOf(map.get("ISCREATEBYPMO").toString()));

                    // 增加计划实际完成时间、废弃时间、计划类型、计划类别
                    p.setInvalidTime((Date)map.get("INVALIDTIME"));
                    p.setActualStartTime((Date)map.get("ACTUALSTARTTIME"));
                    p.setActualEndTime((Date)map.get("ACTUALENDTIME"));
                    p.setTaskNameType(StringUtils.isNotEmpty((String)map.get("TASKNAMETYPE")) ? (String)map.get("TASKNAMETYPE") : "");
                    p.setTaskType(StringUtils.isNotEmpty((String)map.get("TASKTYPE")) ? (String)map.get("TASKTYPE") : "");
                    p.setPlanType(StringUtils.isNotEmpty((String)map.get("PLANTYPE")) ? (String)map.get("PLANTYPE") : "");
                    p.setTabCbTemplateId(StringUtils.isNotEmpty((String)map.get("TABCBTEMPLATEID")) ? (String)map.get("TABCBTEMPLATEID") : "");

                    p.set_parentId(p.getParentPlanId());
                    p.setOrder(String.valueOf(p.getStoreyNo()));
                    list.add(p);
                }
            }
        }
        return list;
    }

    /**
     * 将计划数列表初始化SQL和计划树列表查询SQL 相同的前半部分抽出
     *
     * @param projectId
     * @return
     * @see
     */
    private StringBuffer createStringBuffer(String projectId) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" SELECT nvl2(yy.FLOWFLAG_, '1', '0') FLOWFLAG, ");
        hqlBuffer.append(" nvl2(USERROLEDATA.ROLECODE, 'true', 'false') ISCREATEBYPMO, ");
        hqlBuffer.append(" PL.ID, ");
        hqlBuffer.append(" PL.CREATEBY, ");
        hqlBuffer.append(" PL.CREATENAME, ");
        hqlBuffer.append(" PL.CREATETIME, ");
        hqlBuffer.append(" PL.UPDATEBY, ");
        hqlBuffer.append(" PL.UPDATENAME, ");
        hqlBuffer.append(" PL.UPDATETIME, ");
        hqlBuffer.append(" PL.AVALIABLE, ");
        hqlBuffer.append(" PL.BIZCURRENT, ");
        hqlBuffer.append(" PL.SECURITYLEVEL, ");
        hqlBuffer.append(" PL.POLICY_ID, ");
        hqlBuffer.append(" PL.ACTUALSTARTTIME, ");
        hqlBuffer.append(" PL.ACTUALENDTIME, ");
        hqlBuffer.append(" PL.ASSIGNTIME, ");
        hqlBuffer.append(" PL.ASSIGNER, ");
        hqlBuffer.append(" PL.FLOWSTATUS, ");
        hqlBuffer.append(" PL.MILESTONE, ");
        hqlBuffer.append(" PL.OWNER, ");
        hqlBuffer.append(" PL.PARENTPLANID, ");
        hqlBuffer.append(" PL.PLANENDTIME, ");
        hqlBuffer.append(" PL.PLANLEVEL, ");
        hqlBuffer.append(" PL.PLANNAME, ");
        hqlBuffer.append(" PL.PLANNUMBER, ");
        hqlBuffer.append(" PL.PLANORDER, ");
        hqlBuffer.append(" PL.PLANSTARTTIME, ");
        hqlBuffer.append(" PL.PROGRESSRATE, ");
        hqlBuffer.append(" PL.PROJECTID, ");
        hqlBuffer.append(" PL.PROJECTSTATUS, ");
        hqlBuffer.append(" PL.REMARK, ");
        hqlBuffer.append(" PL.STOREYNO, ");
        hqlBuffer.append(" PL.WORKTIME, ");
        hqlBuffer.append(" PL.FEEDBACKPROCINSTID, ");
        hqlBuffer.append(" PL.FEEDBACKRATEBEFORE, ");
        hqlBuffer.append(" PL.PLANSOURCE, ");
        hqlBuffer.append(" PL.OPCONTENT, ");
        hqlBuffer.append(" PL.PLANTYPE, ");
        hqlBuffer.append(" PL.CELLID, ");
        hqlBuffer.append(" PL.FLOWRESOLVEXML, ");
        hqlBuffer.append(" PL.REQUIRED, ");
        hqlBuffer.append(" PL.LAUNCHTIME, ");
        hqlBuffer.append(" PL.LAUNCHER, ");
        hqlBuffer.append(" PL.FROMTEMPLATE, ");
        hqlBuffer.append(" PL.CREATEFULLNAME, ");
        hqlBuffer.append(" PL.UPDATEFULLNAME, ");
        hqlBuffer.append(" PL.WORKTIMEREFERENCE, ");
        hqlBuffer.append(" PL.FORMID, ");
        hqlBuffer.append(" PL.ISASSIGNBACK, ");
        hqlBuffer.append(" PL.ISASSIGNSINGLEBACK, ");
        hqlBuffer.append(" PL.ISCHANGESINGLEBACK, ");
        hqlBuffer.append(" PL.TASKNAMETYPE, ");
        hqlBuffer.append(" PL.INVALIDTIME, ");
        hqlBuffer.append(" PL.TASKTYPE, ");
        hqlBuffer.append(" PL.PLANTYPE, ");
        hqlBuffer.append(" PL.TABCBTEMPLATEID, ");
        hqlBuffer.append(" PROJ.ID PROJ_ID, ");
        hqlBuffer.append(" PROJ.AVALIABLE PROJ_AVALIABLE, ");
        hqlBuffer.append(" PROJ.BIZCURRENT PROJ_BIZCURRENT, ");
        hqlBuffer.append(" PROJ.ENDPROJECTTIME PROJ_ENDPROJECTTIME, ");
        hqlBuffer.append(" PROJ.ISREFUSE PROJ_ISREFUSE, ");
        hqlBuffer.append(" PROJ.NAME PROJ_NAME, ");
        hqlBuffer.append(" PROJ.PROJECTNUMBER PROJ_PROJECTNUMBER, ");
        hqlBuffer.append(" PROJ.PROJECTTIMETYPE PROJ_PROJECTTIMETYPE, ");
        hqlBuffer.append(" PROJ.STARTPROJECTTIME PROJ_STARTPROJECTTIME, ");
        hqlBuffer.append(" PROJ.STATUS PROJ_STATUS, ");
        hqlBuffer.append(" PARENT.ID PARENT_ID, ");
        hqlBuffer.append(" PARENT.CREATEBY PARENT_CREATEBY, ");
        hqlBuffer.append(" PARENT.OWNER PARENT_OWNER, ");
        hqlBuffer.append(" PARENT.AVALIABLE PARENT_AVALIABLE, ");
        hqlBuffer.append(" PARENT.PLANNAME PARENT_PLANNAME, ");
        hqlBuffer.append(" PARENT.BIZCURRENT PARENT_BIZCURRENT, ");
        hqlBuffer.append(" PARENT.FLOWSTATUS PARENT_FLOWSTATUS, ");
        hqlBuffer.append(" PARENT.PLANSTARTTIME PARENT_PLANSTARTTIME, ");
        hqlBuffer.append(" PARENT.PLANENDTIME PARENT_PLANENDTIME, ");
        hqlBuffer.append(" PARENT.PLANNUMBER PARENT_PLANNUMBER, ");
        hqlBuffer.append(" PARENT.PLANTYPE PARENT_PLANTYPE, ");
        hqlBuffer.append(" PARENT.CELLID PARENT_CELLID, ");
        hqlBuffer.append(" PARENT.FLOWRESOLVEXML PARENT_FLOWRESOLVEXML, ");
        hqlBuffer.append(" PARENT.FROMTEMPLATE PARENT_FROMTEMPLATE, ");
        hqlBuffer.append(" PARENT.FORMID PARENT_FORMID, ");
        hqlBuffer.append(" PARENT.WORKTIMEREFERENCE PARENT_WORKTIMEREFERENCE, ");
        hqlBuffer.append(" LV.ID LV_ID, ");
        hqlBuffer.append(" LV.NAME LV_NAME, ");
        hqlBuffer.append(" PRE.PREPOSEPLANS ");
        hqlBuffer.append(" FROM PM_PROJECT PROJ ");
        hqlBuffer.append(" JOIN PM_PLAN PL ON (PROJ.ID = PL.PROJECTID AND PL.AVALIABLE = '1') ");
        hqlBuffer.append(" LEFT JOIN PM_PLAN PARENT ON PARENT.ID = PL.PARENTPLANID ");
        hqlBuffer.append(" LEFT JOIN CM_BUSINESS_CONFIG LV ON LV.ID = PL.PLANLEVEL ");
        hqlBuffer.append(" LEFT JOIN (SELECT RU.USERID, ROLE.ROLECODE ");
        hqlBuffer.append(" FROM T_S_ROLE_USER RU, T_S_ROLE ROLE ");
        hqlBuffer.append(" WHERE RU.ROLEID = ROLE.ID ");
        hqlBuffer.append(" AND ROLE.ROLECODE = 'PMO' ");
        hqlBuffer.append(" UNION ");
        hqlBuffer.append(" SELECT GU.USERID, ROLE.ROLECODE ");
        hqlBuffer.append(" FROM T_S_ROLE_GROUP RG, T_S_GROUP_USER GU, T_S_ROLE ROLE ");
        hqlBuffer.append(" WHERE RG.ROLEID = ROLE.ID ");
        hqlBuffer.append(" AND RG.GROUPID = GU.GROUPID ");
        hqlBuffer.append(" AND ROLE.ROLECODE = 'PMO') USERROLEDATA  ");
        hqlBuffer.append(" ON USERROLEDATA.USERID = PL.CREATEBY ");
        hqlBuffer.append(" LEFT JOIN (SELECT PREC.PLANID, ");
        hqlBuffer.append(" WM_CONCAT(PLAN.PLANNAME) AS PREPOSEPLANS ");
        hqlBuffer.append(" FROM PM_PREPOSE_PLAN PREC ");
        hqlBuffer.append(" JOIN PM_PLAN PLAN ON PLAN.ID = PREC.PREPOSEPLANID ");
        hqlBuffer.append(" GROUP BY PREC.PLANID) PRE ON PRE.PLANID = PL.ID ");
        hqlBuffer.append(" LEFT JOIN (SELECT FLOWPLAN.PLANID,  ");
        hqlBuffer.append(" DECODE(COUNT(1), 0, '0', '1') FLOWFLAG_ ");
        hqlBuffer.append(" FROM (SELECT F.PLANID, L.Projectid ");
        hqlBuffer.append(" FROM PM_APPROVE_PLAN_INFO F, PM_PLAN L ");
        hqlBuffer.append(" WHERE F.PLANID = L.ID ");
        hqlBuffer.append(" and l.projectid = '" + projectId + "' ");
        hqlBuffer.append(" UNION ");
        hqlBuffer.append(" SELECT L.PLANID AS PLANID, L.PROJECTID ");
        hqlBuffer.append(" FROM PM_APPROVE_PLAN_INFO F, PM_TEMPORARY_PLAN L ");
        hqlBuffer.append(" WHERE F.PLANID = L.ID ");
        hqlBuffer.append(" and l.projectid = '" + projectId + "' ");
        hqlBuffer.append(" UNION ");
        hqlBuffer.append(" SELECT L.ID AS PLANID, l.projectid ");
        hqlBuffer.append(" FROM PM_PLAN L, PM_FLOWTASK_PARENT FP ");
        hqlBuffer.append(" WHERE FP.PARENTID = L.ID ");
        hqlBuffer.append(" and l.projectid = '" + projectId + "') FLOWPLAN ");
        hqlBuffer.append(" group by planid) yy ON YY.PLANID = PL.ID ");
        hqlBuffer.append(" LEFT JOIN T_S_BASE_USER OU ON OU.ID = PL.OWNER ");
        hqlBuffer.append(" WHERE PROJ.ID = '" + projectId + "' ");
        return hqlBuffer;
    }




    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param plan
     * @return
     * @see
     */
    private String createHql(Plan plan) {
        String hql = "from Plan l where 1 = 1";
        // 计划ID
        if (StringUtils.isNotEmpty(plan.getId())) {
            hql = hql + " and l.id = '" + plan.getId() + "'";
        }
        // 所属项目
        if (StringUtils.isNotEmpty(plan.getProjectId())) {
            hql = hql + " and l.projectId = '" + plan.getProjectId() + "'";
        }
        // 父计划
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            hql = hql + " and l.parentPlanId = '" + plan.getParentPlanId() + "'";
        }
        // 计划名称
        if (StringUtils.isNotEmpty(plan.getPlanName())) {
            hql = hql + " and l.planName like '%" + plan.getPlanName() + "%'";
        }
        // 责任人
        if (StringUtils.isNotEmpty(plan.getOwner())) {
            hql = hql + " and l.owner = '" + plan.getOwner() + "'";
        }
        // 生命周期状态
        if (StringUtils.isNotEmpty(plan.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + plan.getBizCurrent() + "'";
        }
        // // 是否流程中
        // if (StringUtils.isNotEmpty(plan.getFlowStatus())) {
        // hql = hql + " and l.flowStatus = '" + plan.getFlowStatus() + "'";
        // }
        // 下达人
        if (StringUtils.isNotEmpty(plan.getAssigner())) {
            hql = hql + " and l.assigner = '" + plan.getAssigner() + "'";
        }
        // 下达时间
        if (plan.getAssignTimeStart() != null) {
            hql = hql + " and l.assignTime >= " + plan.getAssignTimeStart();
        }
        if (plan.getAssignTimeEnd() != null) {
            hql = hql + " and l.assignTime <= " + plan.getAssignTimeEnd();
        }
        // 计划等级
        if (StringUtils.isNotEmpty(plan.getPlanLevel())) {
            hql = hql + " and l.planLevel = '" + plan.getPlanLevel() + "'";
        }

        // cellId
        if (StringUtils.isNotEmpty(plan.getCellId())) {
            hql = hql + " and l.cellId = '" + plan.getCellId() + "'";
        }

        // 开始时间
        if (plan.getPlanStartTimeStart() != null) {
            hql = hql + " and l.planStartTime >= " + plan.getPlanStartTimeStart();
        }
        if (plan.getPlanStartTimeEnd() != null) {
            hql = hql + " and l.planStartTime <= " + plan.getPlanStartTimeEnd();
        }
        // 结束时间
        if (plan.getPlanEndTimeStart() != null) {
            hql = hql + " and l.planEndTime >= " + plan.getPlanEndTimeStart();
        }
        if (plan.getPlanEndTimeEnd() != null) {
            hql = hql + " and l.planEndTime <= " + plan.getPlanEndTimeEnd();
        }
        // 是否可用
        if (StringUtils.isNotEmpty(plan.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + plan.getAvaliable() + "'";
        }
        hql = hql + " order by parentPlanId, createTime, storeyno";
        return hql;
    }


    @Override
    public FeignJson saveAsCreate(Plan plan,TSUserDto user,List<Inputs> inputsList,List<ResourceLinkInfo> resourceLinkList,String orgId) {
        FeignJson j = new FeignJson();
        String oldPlanId = plan.getId();
        // 如果是下方插入计划
        if (StringUtils.isNotEmpty(plan.getBeforePlanId())) {
            Plan beforePlan = (Plan)sessionFacade.getEntity(Plan.class, plan.getBeforePlanId());
            plan.setParentPlanId(beforePlan.getParentPlanId());
            List<Plan> sameLevelPlans = getSameLevelPlans(plan);
            for (Plan sameLevel : sameLevelPlans) {
                if (sameLevel.getStoreyNo() > beforePlan.getStoreyNo()) {
                    Plan p = (Plan)sessionFacade.getEntity(Plan.class, sameLevel.getId());
                    p.setStoreyNo(p.getStoreyNo() + 1);
                    sessionFacade.updateEntitie(p);
                }
            }
            plan.setPlanNumber(getMaxPlanNumber(plan) + 1);
            plan.setStoreyNo(beforePlan.getStoreyNo() + 1);

            /*
             * List<Inputs> inputsList = inputsService.getInputsByUseObeject(
             * PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());
             */
            /*List<Inputs> inputsList = (List<Inputs>)redisService.getFromRedis("INPUTSLIST",
                    plan.getId());*/

            redisService.setToRedis("INPUTSLIST", plan.getId(), null);

            List<DeliverablesInfo> deliverablesList = deliverablesInfoService.getDeliverablesByUseObeject(
                    PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());

            plan.setId(null);
            plan.setAvaliable("1");
            CommonInitUtil.initGLObjectForCreate(plan,user,orgId);
            initBusinessObject(plan);
            sessionFacade.save(plan);

            // 存放其前置计划
            if (StringUtils.isNotEmpty(plan.getPreposeIds())) {
                for (String preposeId : plan.getPreposeIds().split(",")) {
                    Plan preposePlan = (Plan)sessionFacade.getEntity(Plan.class, preposeId);
                    if (preposePlan != null) {
                        PreposePlan prepose = new PreposePlan();
                        prepose.setPlanId(plan.getId());
                        prepose.setPreposePlanId(preposeId);
                        CommonInitUtil.initGLObjectForCreate(prepose,user,orgId);
                        sessionFacade.save(prepose);
                    }
                }
            }

            if (!CommonUtil.isEmpty(inputsList)) {
                for (Inputs inputs : inputsList) {
                    // Inputs in = getEntity(Inputs.class, inputs.getId());
                    inputs.setUseObjectId(plan.getId());
//                    CommonUtil.glObjectSet(inputs);
                    inputs.setCreateBy(user.getId());
                    inputs.setCreateName(user.getUserName());
                    inputs.setCreateFullName(user.getRealName());
                    inputs.setCreateTime(new Date());
                    CommonInitUtil.initGLObjectForCreate(inputs,user,orgId);
                    sessionFacade.save(inputs);
                }
            }

            for (DeliverablesInfo deliverablesInfo : deliverablesList) {
                DeliverablesInfo d = (DeliverablesInfo)sessionFacade.getEntity(DeliverablesInfo.class, deliverablesInfo.getId());
                d.setUseObjectId(plan.getId());
                CommonInitUtil.initGLObjectForCreate(d,user,orgId);
                sessionFacade.save(d);
            }

            String resourceIds = "";
            if(!CommonUtil.isEmpty(resourceLinkList)){
                for (ResourceLinkInfo resourceLink : resourceLinkList) {
                    if(CommonUtil.isEmpty(resourceIds)){
                        resourceIds = resourceLink.getResourceId();
                    }else{
                        resourceIds = resourceIds + "," +resourceLink.getResourceId();
                    }
                    //    resourceLinkInfoService.saveResourceLinkInfoById(resourceLink.getResourceId(),plan.getId());
                }
                resourceLinkInfoService.doAddResourceForPlan(resourceIds,plan.getId(),DateUtil.dateToString(plan.getPlanStartTime(),"yyyy-MM-dd"),DateUtil.dateToString(plan.getPlanEndTime(),"yyyy-MM-dd"), resourceLinkList.get(0).getUseObjectType());
            }

        }
        else {
            CommonInitUtil.initGLObjectForCreate(plan,user,orgId);
            initBusinessObject(plan);
            plan.setPlanNumber(getMaxPlanNumber(plan) + 1);
            plan.setStoreyNo(getMaxStoreyNo(plan) + 1);

            /*
             * List<Inputs> inputsList = inputsService.getInputsByUseObeject(
             * PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());
             */

           /* List<Inputs> inputsListFromRedis = null;
            try {
                inputsListFromRedis = (List<Inputs>)redisService.getFromRedis("INPUTSLIST",
                        plan.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<Inputs> inputsList = JSON.parseArray(JSON.toJSONString(inputsListFromRedis),Inputs.class);
*/
            redisService.setToRedis("INPUTSLIST", plan.getId(), null);

            List<DeliverablesInfo> deliverablesList = deliverablesInfoService.getDeliverablesByUseObeject(
                    PlanConstants.USEOBJECT_TYPE_PLAN, plan.getId());

            plan.setAvaliable("1");
            sessionFacade.save(plan);

            // 存放其前置计划
            if (!CommonUtil.isEmpty(plan.getPreposeIds())) {
                for (String preposeId : plan.getPreposeIds().split(",")) {
                    Plan preposePlan = (Plan)sessionFacade.getEntity(Plan.class, preposeId);
                    if (preposePlan != null) {
                        PreposePlan prepose = new PreposePlan();
                        prepose.setPlanId(plan.getId());
                        prepose.setPreposePlanId(preposeId);
                        CommonInitUtil.initGLObjectForCreate(prepose,user,orgId);
                        sessionFacade.saveOrUpdate(prepose);
                    }

                }
            }

            if (!CommonUtil.isEmpty(inputsList)) {
                for (Inputs inputs : inputsList) {
                    /*
                     * Inputs in = getEntity(Inputs.class, inputs.getId());
                     * in.setUseObjectId(plan.getId());
                     * saveOrUpdate(in);
                     */
                    inputs.setUseObjectId(plan.getId());
//                    CommonUtil.glObjectSet(inputs);
                    inputs.setCreateBy(user.getId());
                    inputs.setCreateName(user.getUserName());
                    inputs.setCreateFullName(user.getRealName());
                    inputs.setCreateTime(new Date());
                    CommonInitUtil.initGLObjectForCreate(inputs,user,orgId);
                    sessionFacade.save(inputs);
                }
            }

            for (DeliverablesInfo deliverablesInfo : deliverablesList) {
                DeliverablesInfo d = (DeliverablesInfo)sessionFacade.getEntity(DeliverablesInfo.class, deliverablesInfo.getId());
                d.setUseObjectId(plan.getId());
                CommonInitUtil.initGLObjectForCreate(d,user,orgId);
                sessionFacade.saveOrUpdate(d);
            }

            String resourceIds = "";
            if(!CommonUtil.isEmpty(resourceLinkList)){
                for (ResourceLinkInfo resourceLink : resourceLinkList) {
                    if(CommonUtil.isEmpty(resourceIds)){
                        resourceIds = resourceLink.getResourceId();
                    }else{
                        resourceIds = resourceIds + "," +resourceLink.getResourceId();
                    }
                }
                resourceLinkInfoService.doAddResourceForPlan(resourceIds,plan.getId(),DateUtil.dateToString(plan.getPlanStartTime(),"yyyy-MM-dd"),DateUtil.dateToString(plan.getPlanEndTime(),"yyyy-MM-dd"), resourceLinkList.get(0).getUseObjectType());
            }
        }

        resourceLinkInfoService.updateResourceEverydayuseInfos(oldPlanId, plan.getId(),
                plan.getProjectId());

        // 计划日志记录
        PlanLog planLog = new PlanLog();
        planLog.setPlanId(plan.getId());
        planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);
        /*
         * if (StringUtils.isNotEmpty(plan.getLauncher())) { TSUser user =
         * userService.getEntity(TSUser.class, plan.getLauncher());
         * planLog.setCreateBy(user.getId()); planLog.setCreateName(user.getUserName());
         * planLog.setCreateFullName(user.getRealName()); planLog.setCreateTime(new Date()); }
         */
        planLog.setCreateBy(user.getId());
        planLog.setCreateName(user.getUserName());
        planLog.setCreateFullName(user.getRealName());
        planLog.setCreateTime(new Date());
        // planLog.setRemark(newRemark);
        sessionFacade.saveOrUpdate(planLog);
        j.setObj(plan.getId());
        return j;
    }


    /**
     * 取得所有同级计划
     *
     * @param plan
     * @return
     * @see
     */
    private List<Plan> getSameLevelPlans(Plan plan) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from Plan t");
        hql.append(" where 1 = 1");

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
        return sessionFacade.findByQueryString(hql.toString());
    }


    /**
     * 获取最大的planNumber
     *
     * @param plan
     * @return
     * @see
     */
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
    public void saveAsUpdate(Plan plan, TSUserDto user,List<ResourceLinkInfo> resourceLinkInfo,String orgId) {
        Plan p = (Plan) sessionFacade.getEntity(Plan.class,plan.getId());
        BeanUtils.copyProperties(plan,p,"id","policy");
        CommonInitUtil.initGLObjectForUpdate(p,user,orgId);
        sessionFacade.saveOrUpdate(p);
        // 获取已有的前置计划
        List<PreposePlan> list = preposePlanService.getPreposePlansByPlanId(plan);
        List<String> preposeList = new ArrayList<String>();
        for (PreposePlan prepose : list) {
            preposeList.add(prepose.getPreposePlanId());
        }
        // 删除其已有的前置
        preposePlanService.removePreposePlansByPlanId(plan);
        // 存放其新的前置计划
        Map<String, String> newPreposeMap = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(plan.getPreposeIds())) {
            for (String preposeId : plan.getPreposeIds().split(",")) {
                Plan preposePlan = (Plan)sessionFacade.getEntity(Plan.class, preposeId);
                if (preposePlan != null) {
                    PreposePlan prepose = new PreposePlan();
                    prepose.setPlanId(plan.getId());
                    prepose.setPreposePlanId(preposeId);
                    sessionFacade.save(prepose);
                }
                newPreposeMap.put(preposeId, preposeId);
            }
        }

        // 将移除的前置计划对应的输入删除
        for (String str : preposeList) {
            if (StringUtils.isEmpty(newPreposeMap.get(str))) {
                inputsService.deleteInputsByPreposePlan(str, PlanConstants.USEOBJECT_TYPE_PLAN,
                        plan.getId());
            }
        }

        List<Inputs> inputsList = new ArrayList<>();

        String inpStr = (String)redisService.getFromRedis("INPUTSLIST",
                plan.getId());
        if(!CommonUtil.isEmpty(inpStr)){
            inputsList = JSON.parseArray(inpStr,Inputs.class);
        }

        redisService.setToRedis("INPUTSLIST", plan.getId(), null);

        if(!CommonUtil.isEmpty(inputsList)){
            List<Inputs> iList = sessionFacade.findHql("from Inputs where useObjectId=?",plan.getId());
            sessionFacade.deleteAllEntitie(iList);
            for (Inputs inputs : inputsList) {
                Inputs in = (Inputs)sessionFacade.getEntity(Inputs.class, inputs.getId());
                if (!CommonUtil.isEmpty(in)) {
                    in.setUseObjectId(plan.getId());
                    in.setDocId(inputs.getDocId());
                    in.setDocName(inputs.getDocName());
                    in.setExt1(inputs.getExt1());
                    in.setExt2(inputs.getExt2());
                    in.setExt3(inputs.getExt3());
                    in.setName(inputs.getName());
                    in.setOrigin(inputs.getOrigin());
                    in.setOriginDeliverablesInfoId(inputs.getOriginDeliverablesInfoId());
                    in.setOriginObjectId(inputs.getOriginObjectId());
                    in.setOriginType(inputs.getOriginType());
                    sessionFacade.saveOrUpdate(in);
                }
                else {
                    inputs.setUseObjectId(plan.getId());
//                    CommonUtil.glObjectSet(inputs);
                    inputs.setCreateBy(user.getId());
                    inputs.setCreateName(user.getUserName());
                    inputs.setCreateFullName(user.getRealName());
                    inputs.setCreateTime(new Date());
                    sessionFacade.save(inputs);
                }

            }
        }

        ResourceLinkInfoDto resourceLinkInfoDto = new ResourceLinkInfoDto();
        resourceLinkInfoDto.setUseObjectId(plan.getId());
        resourceLinkInfoDto.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);

        //删除现有的资源
        resourceLinkInfoService.doBatchDelResourceForWork(resourceLinkInfoDto,plan.getId(),plan.getProjectId());

        //保存资源
        String resourceIds = "";
        if(!CommonUtil.isEmpty(resourceLinkInfo)){
            for (ResourceLinkInfo resourceLink : resourceLinkInfo) {
                if(CommonUtil.isEmpty(resourceIds)){
                    resourceIds = resourceLink.getResourceId();
                }else{
                    resourceIds = resourceIds + "," +resourceLink.getResourceId();
                }
                //    resourceLinkInfoService.saveResourceLinkInfoById(resourceLink.getResourceId(),plan.getId());
            }
            resourceLinkInfoService.doAddResourceForPlan(resourceIds,plan.getId(),DateUtil.dateToString(plan.getPlanStartTime(),"yyyy-MM-dd"),DateUtil.dateToString(plan.getPlanEndTime(),"yyyy-MM-dd"), resourceLinkInfo.get(0).getUseObjectType());
        }
    }

    /**
     * 获取同级最大的storeyNo
     *
     * @param plan
     * @return
     * @see
     */
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

    /**
     * 返回权限
     *
     * @return
     * @see
     */
    @SuppressWarnings("unused")
    @Override
    public String getOutPowerForPlanListForReview(String folderId, String userId) {
        List<String> codeList = ProjLibAuthManager.getAllAuthActionCode();

        RepFileDto r = repService.getRepFileByRepFileId(appKey,folderId);
        Boolean detail = false;
        Boolean download = false;
        Boolean list = false;
        if (!CommonUtil.isEmpty(r)) {
            String createRep = r.getCreateBy();
            Boolean havePower = false;
            Boolean isCreate = false;
            String categoryFileAuths = projLibAuthService.getDocumentFileAuths(folderId, userId);
            if (StringUtil.isNotEmpty(categoryFileAuths)) {
                if (categoryFileAuths.contains("detail")) {
                    detail = true;
                }
                if (categoryFileAuths.contains("download")) {
                    download = true;
                }
                if (categoryFileAuths.contains("list")) {
                    list = true;
                }
            }
            else {
                detail = false;
                download = false;
                list = false;
            }
        }
        if (download == true) {
            return "listDownloadDetail";
        }
        if (detail == true && download == false) {
            return "listDetail";
        }
        if (list == true && download == false && detail == false) {
            return "list";
        }
        return "";
    }

    /**
     * 根据计划ID查找其所有子计划（包括计划本身和所有子孙计划）
     *
     * @param plan
     * @return
     * @see
     */
    @Override
    public List<Plan> getPlanAllChildren(Plan plan) {
        List<Plan> list = new ArrayList<Plan>();
        if (plan != null) {
            Plan currentPlan = (Plan) sessionFacade.getEntity(Plan.class, plan.getId());
            if (currentPlan != null) {
                getPlanChildren(list, currentPlan);
            }
        }

        return list;
    }

    /**
     * 根据当前计划，查找其所有子孙计划
     *
     * @param list
     * @param currentPlan
     * @return
     * @see
     */
    private List<Plan> getPlanChildren(List<Plan> list, Plan currentPlan) {
        if (currentPlan != null) {
            list.add(currentPlan);
            Plan conditionPlan = new Plan();
            conditionPlan.setParentPlanId(currentPlan.getId());
            List<Plan> childrenPlan = queryPlanList(conditionPlan, 1, 10, false);
            if (!CommonUtil.isEmpty(childrenPlan)) {
                for (Plan condition : childrenPlan) {
                    getPlanChildren(list, condition);
                }
            }
        }
        return list;
    }

    @Override
    public PageList queryProjectList(List<ConditionVO> conditionList) {
        try {
            String hql = "from Project where bizCurrent = 'STARTING'";

            Map<String, String> map = new HashMap<String, String>();
            // 排序
            map.put("Project.createTime", "desc");
            List list = sessionFacade.searchByCommonFormHql(hql, conditionList, true, map);
            List<ProjectDto> resultList = new ArrayList<>();
            try {
                resultList = (List<ProjectDto>) VDataUtil.getVDataByEntity(list,ProjectDto.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int count = sessionFacade.searchByCommonFormHqlCount(hql, conditionList);
            PageList pageList = new PageList(count, resultList);
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

    /**
     * 根据项目的暂停/恢复设置该项目下计划的暂停/恢复
     *
     * @param project
     * @param projectStatus
     * @see
     */
    @Override
    public void setPlanProjectStatus(Project project, String projectStatus) {
        Plan plan = new Plan();
        plan.setProjectId(project.getId());
        String hql = createHql(plan);
        List<Plan> list = sessionFacade.findByQueryString(hql);
        Map<String, String> procInstMap = new HashMap<String, String>();
        if (ProjectStatusConstants.CLOSE.equals(projectStatus)) {
            for (Plan p : list) {
                if (!"FINISH".equals(p.getBizCurrent())) {// 已完工计划不受项目暂停和关闭影响
                    p.setProjectStatus(projectStatus);
                    sessionFacade.saveOrUpdate(p);
                    if (StringUtils.isNotEmpty(p.getFeedbackProcInstId())) {
                        FeignJson json = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                                p.getFeedbackProcInstId());
                        String tasks = json.getObj().toString();
                        if (!CommonUtil.isEmpty(tasks)) {
                            // 计划关闭时，计划相关的流程终止
                            workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                                    p.getId(), p.getFeedbackProcInstId(),
                                    PlanConstants.PROJECTATTENTION_CLOSE);
                        }
                    }
                }
            }
            // 查找Plan、TemporaryPlan、FlowTask对应的ApprovePlanForm的数据
            List<ApprovePlanForm> forms = getApproveFormListByPlan(project.getId());
            for (ApprovePlanForm form : forms) {
                if (StringUtil.isNotEmpty(procInstMap.get(form.getProcInstId()))) {
                    continue;
                }
                else {
                    FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                            form.getProcInstId());
                    String tasks = feignJson.getObj().toString();
                    if (!CommonUtil.isEmpty(tasks) && StringUtils.isNotEmpty(form.getProcInstId())) {
                        // 计划关闭时，计划相关的流程终止
                        workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                                form.getId(), form.getProcInstId(),
                                PlanConstants.PROJECTATTENTION_CLOSE);
                    }
                    procInstMap.put(form.getProcInstId(), form.getProcInstId());
                }
            }
        }
        else if (ProjectStatusConstants.PAUSE.equals(projectStatus)) {
            for (Plan p : list) {
                if (!"FINISH".equals(p.getBizCurrent())) {// 已完工计划不受项目暂停和关闭影响
                    p.setProjectStatus(projectStatus);
                    sessionFacade.saveOrUpdate(p);
                    if (StringUtils.isNotEmpty(p.getFeedbackProcInstId())) {
                        FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                                p.getFeedbackProcInstId());
                        String tasks = feignJson.getObj().toString();
                        if (!CommonUtil.isEmpty(tasks)) {
                            // 计划暂停时，计划相关的流程挂起
                            workFlowFacade.getWorkFlowMonitorService().suspendProcessInstance(
                                    p.getId(), p.getFeedbackProcInstId(),
                                    PlanConstants.PROJECTATTENTION_STOP);
                        }
                    }
                }
            }
            // 查找Plan、TemporaryPlan、FlowTask对应的ApprovePlanForm的数据
            List<ApprovePlanForm> forms = getApproveFormListByPlan(project.getId());
            for (ApprovePlanForm form : forms) {
                if (StringUtil.isNotEmpty(procInstMap.get(form.getProcInstId()))) {
                    continue;
                }
                else {
                    FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                            form.getProcInstId());
                    String tasks = feignJson.getObj().toString();
                    if (!CommonUtil.isEmpty(tasks) && StringUtils.isNotEmpty(form.getProcInstId())) {
                        // 计划暂停时，计划相关的流程挂起
                        workFlowFacade.getWorkFlowMonitorService().suspendProcessInstance(
                                form.getId(), form.getProcInstId(),
                                PlanConstants.PROJECTATTENTION_STOP);
                    }
                    procInstMap.put(form.getProcInstId(), form.getProcInstId());
                }
            }
        }
        else if (ProjectStatusConstants.RECOVER.equals(projectStatus)) {
            for (Plan p : list) {
                if (!"FINISH".equals(p.getBizCurrent())) {// 已完工计划不受项目暂停和关闭影响
                    p.setProjectStatus(projectStatus);
                    sessionFacade.saveOrUpdate(p);
                    if (StringUtils.isNotEmpty(p.getFeedbackProcInstId())) {
                        FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                                p.getFeedbackProcInstId());
                        String tasks = feignJson.getObj().toString();
                        if (!CommonUtil.isEmpty(tasks)) {
                            // 计划恢复时，计划相关的流程重新执行
                            workFlowFacade.getWorkFlowMonitorService().activateSelectProcesses(
                                    p.getId(), p.getFeedbackProcInstId());
                        }
                    }
                }

            }
            // 查找Plan、TemporaryPlan、FlowTask对应的ApprovePlanForm的数据
            List<ApprovePlanForm> forms = getApproveFormListByPlan(project.getId());
            for (ApprovePlanForm form : forms) {
                if (StringUtil.isNotEmpty(procInstMap.get(form.getProcInstId()))) {
                    continue;
                }
                else {
                    FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                            form.getProcInstId());
                    String tasks = feignJson.getObj().toString();
                    if (!CommonUtil.isEmpty(tasks) && StringUtils.isNotEmpty(form.getProcInstId())) {
                        // 计划恢复时，计划相关的流程重新执行
                        workFlowFacade.getWorkFlowMonitorService().activateSelectProcesses(
                                form.getId(), form.getProcInstId());
                    }
                    procInstMap.put(form.getProcInstId(), form.getProcInstId());
                }
            }
        }
        else {
            for (Plan p : list) {
                p.setProjectStatus(projectStatus);
                sessionFacade.saveOrUpdate(p);
            }
        }
    }

    /**
     * 获取项目相关的审批表单ApprovePlanForm的数据
     *
     * @param projectId
     * @return
     */
    private List<ApprovePlanForm> getApproveFormListByPlan(String projectId) {
        List<ApprovePlanForm> list = new ArrayList<ApprovePlanForm>();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select am.id id, am.approveType approveType, am.procInstId procInstId ");
        hqlBuffer.append(" from pm_approve_plan_form am ");
        hqlBuffer.append(" join PM_APPROVE_PLAN_INFO ap on ap.formId = am.id ");
        hqlBuffer.append(" where ap.planId in ");
        hqlBuffer.append(" (select pl.id from PM_plan pl ");
        hqlBuffer.append(" where pl.projectid = '" + projectId + "') ");
        hqlBuffer.append(" or ap.planId in ");
        hqlBuffer.append("   (select tm.id from PM_TEMPORARY_PLAN tm ");
        hqlBuffer.append(" where tm.projectid = '" + projectId + "') ");
        hqlBuffer.append(" or ap.planId in ");
        hqlBuffer.append("  (select tk.id from PM_FLOWTASK tk ");
        hqlBuffer.append(" where tk.projectid = '" + projectId + "') ");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    ApprovePlanForm form = new ApprovePlanForm();
                    form.setId(id);
                    form.setApproveType((String)map.get("approveType"));
                    form.setProcInstId((String)map.get("procInstId"));
                    list.add(form);
                }
            }
        }
        return list;
    }

    @Override
    public FeignJson checkOriginIsNullBeforeSub(String ids, String useObjectType) {

        FeignJson j = new FeignJson();

        String msg = "";
        try {
            if (!CommonUtil.isEmpty(ids)) {
                for (String id : ids.split(",")) {
                    Plan plan = (Plan) sessionFacade.getEntity(Plan.class, id);
                    List<Inputs> inputsList = inputsService.getInputsByUseObeject(useObjectType,
                            id);
                    if (!CommonUtil.isEmpty(inputsList)) {
                        for (Inputs input : inputsList) {
                            if(CommonUtil.isEmpty(input.getOriginType())){
                                if(!"LOCAL".equals(input.getOriginType())){
                                    msg += "【" + plan.getPlanName() + "】";
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!CommonUtil.isEmpty(msg)) {
                    j.setSuccess(false);
                    j.setMsg("计划" + msg + "存在没有挂载的输入，不可发布");
                }
                else {
                    j.setSuccess(true);
                }
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            j.setSuccess(false);
        }
        finally {
            return j;
        }
    }

    @Override
    public FeignJson pdAssign(Plan plan, String id) {
        FeignJson j = new FeignJson();

        Plan p = getEntity(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        j.setSuccess(true);
        List<PreposePlan> preposePlanList = preposePlanService.getPreposePlansByPlanId(plan);
        for (PreposePlan preposePlan : preposePlanList) {
            if (preposePlan.getPreposePlanInfo().getPlanEndTime().getTime() > p.getPlanStartTime().getTime()) {
                j.setSuccess(false);
                j.setMsg("【" + p.getPlanName() + "】开始时间不能早于其前置计划的结束时间");
                return j;
            }
        }
        if (p.getParentPlanId() != null) {
            Plan planp = getEntity(p.getParentPlanId());
            if (PlanConstants.PLAN_EDITING.equals(planp.getBizCurrent())) {
                j.setSuccess(false);
                Object[] arguments = new String[] {planp.getPlanName()};
                j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignOne", arguments));
                return j;
            }
            else if (!PlanConstants.PLAN_EDITING.equals(planp.getBizCurrent())) {
                String time1 = sdf.format(p.getPlanStartTime());
                String time2 = sdf.format(planp.getPlanStartTime());
                String time3 = sdf.format(p.getPlanEndTime());
                String time4 = sdf.format(planp.getPlanEndTime());
                if ((Integer.parseInt(time2) > Integer.parseInt(time1))
                        || (Integer.parseInt(time3) > Integer.parseInt(time4))) {
                    j.setSuccess(false);
                    Object[] arguments = new String[] {p.getPlanName(), planp.getPlanName(),
                            sdf1.format(planp.getPlanStartTime()), sdf1.format(planp.getPlanEndTime())};
                    j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignTwo",
                            arguments));
                    return j;
                }
            }
            List<Plan> paramList = new ArrayList<Plan>();
            paramList.add(p);
            if (!childrenPlanCoverParent(paramList,j)) {
                j.setSuccess(false);
                Object[] arguments = new String[] {planp.getPlanName()};
                j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignThree",
                        arguments));
                return j;
            }
        }
        List<Plan> childrenPlanList = sessionFacade.findHql("from Plan where parentPlanId = ? and bizCurrent <>'" + PlanConstants.PLAN_EDITING +"' and bizCurrent <> '" + PlanConstants.PLAN_INVALID + "' ",p.getId());
        if(!CommonUtil.isEmpty(childrenPlanList)){
            if (!childrenPlanCoverParent(childrenPlanList,j)) {
                j.setSuccess(false);
                Object[] arguments = new String[] {p.getPlanName()};
                j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignThree",
                        arguments));
                return j;
            }

            String parentStartTime = sdf.format(p.getPlanStartTime());
            String parentEndTime = sdf.format(p.getPlanEndTime());
            for(Plan chiPlan : childrenPlanList){
                String childStartTime = sdf.format(chiPlan.getPlanStartTime());
                String childEndTime = sdf.format(chiPlan.getPlanEndTime());
                if ((Integer.parseInt(childStartTime) > Integer.parseInt(parentStartTime))
                        || (Integer.parseInt(childEndTime) > Integer.parseInt(parentEndTime))) {
                    j.setSuccess(false);
                    Object[] arguments = new String[] {chiPlan.getPlanName(), p.getPlanName(),
                            sdf1.format(p.getPlanStartTime()), sdf1.format(p.getPlanEndTime())};
                    j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignTwo",
                            arguments));
                    return j;
                }
            }

        }
        ResourceLinkInfoDto resourceLinkInfo = new ResourceLinkInfoDto();
        resourceLinkInfo.setUseObjectId(p.getId());
        List<ResourceLinkInfoDto> resourceList = resourceLinkInfoService.queryResourceList(
                resourceLinkInfo, 1, 10, false);
        if (p.getOwner() == null || (p.getOwner() != null && "".equals(p.getOwner()))) {
            j.setSuccess(false);
            Object[] arguments = new String[] {p.getPlanName()};
            j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignFour", arguments));
            return j;
        }
        else {
            //TSUser t = planService.getEntity(TSUser.class, p.getOwner());
            TSUserDto t = userService.getUserByUserId(p.getOwner());
            if (StringUtils.isEmpty(t.getId())) {
                j.setSuccess(false);
                Object[] arguments = new String[] {p.getPlanName()};
                j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignFour",
                        arguments));
                return j;
            }
        }
        if (resourceList != null && resourceList.size() > 0) {
            for (int i = 0; i < resourceList.size(); i++ ) {
                if (StringUtils.isEmpty(resourceList.get(i).getUseRate())) {
                    if (resourceList.get(i).getResourceInfo() != null) {
                        j.setSuccess(false);
                        Object[] arguments = new String[] {p.getPlanName(),
                                resourceList.get(i).getResourceInfo().getName()};
                        j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignFive",
                                arguments));
                    }
                    else {
                        j.setSuccess(false);
                        j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignSix"));
                    }
                    break;
                }
                if (resourceList.get(i).getStartTime() == null) {
                    if (resourceList.get(i).getResourceInfo() != null) {
                        j.setSuccess(false);
                        Object[] arguments = new String[] {p.getPlanName(),
                                resourceList.get(i).getResourceInfo().getName()};
                        j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignSeven",
                                arguments));
                    }
                    else {
                        j.setSuccess(false);
                        j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignEight"));
                    }
                    break;
                }
                if (resourceList.get(i).getEndTime() == null) {
                    if (resourceList.get(i).getResourceInfo() != null) {
                        j.setSuccess(false);
                        Object[] arguments = new String[] {p.getPlanName(),
                                resourceList.get(i).getResourceInfo().getName()};
                        j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignNine",
                                arguments));
                    }
                    else {
                        j.setSuccess(false);
                        j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignTen"));
                    }
                    break;
                }
                if (resourceList.get(i).getStartTime() != null
                        && resourceList.get(i).getEndTime() != null) {
                    String time1 = sdf.format(resourceList.get(i).getStartTime());
                    String time2 = sdf.format(resourceList.get(i).getEndTime());
                    String time3 = sdf.format(p.getPlanStartTime());
                    String time4 = sdf.format(p.getPlanEndTime());
                    if ((Integer.parseInt(time3) > Integer.parseInt(time1))
                            || (Integer.parseInt(time2) > Integer.parseInt(time4))) {
                        if (resourceList.get(i).getResourceInfo() != null) {
                            j.setSuccess(false);
                            Object[] arguments = new String[] {p.getPlanName(),
                                    resourceList.get(i).getResourceInfo().getName()};
                            j.setMsg(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plan.checkAssignEleven", arguments));
                        }
                        else {
                            j.setSuccess(false);
                            j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.project.plan.checkAssignTweleve"));
                        }
                        break;
                    }
                }

            }
        }
        return j;
    }

    /**
     * 判断子计划的输出是否覆盖父计划
     *
     * @param children
     * @return
     * @see
     */
    private boolean childrenPlanCoverParent(List<Plan> children, FeignJson json) {
        for (Plan p : children) {
            p = getEntity(p.getId());
            if (StringUtils.isNotEmpty(p.getParentPlanId())) {
                DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
                deliverablesInfo.setUseObjectId(p.getParentPlanId());
                deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                List<DeliverablesInfo> deliverablesList = deliverablesInfoService.queryDeliverableList(
                        deliverablesInfo, 1, 10, false);
                if (!CommonUtil.isEmpty(deliverablesList)) {
                    Map<String, String> deliverablesMap = new HashMap<String, String>();

                    // 将p的同级且和p一起下达的计划的输出添加到deliverablesMap
                    List<Plan> sameParentPlanList = getSameParentPlanList(p, children);
                    for (Plan sameParent : sameParentPlanList) {
                        deliverablesInfo = new DeliverablesInfo();
                        deliverablesInfo.setUseObjectId(sameParent.getId());
                        deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                        List<DeliverablesInfo> currentDeliverables = deliverablesInfoService.queryDeliverableList(
                                deliverablesInfo, 1, 10, false);
                        for (DeliverablesInfo deli : currentDeliverables) {
                            deliverablesMap.put(deli.getName(), deli.getName());
                        }
                    }

                    // 将p的父计划的所有非拟制中的子计划的输出添加到deliverablesMap
                    Plan parent = new Plan();
                    parent.setParentPlanId(p.getParentPlanId());
                    List<Plan> childList = queryPlanList(parent, 1, 10, false);
                    for (Plan child : childList) {
                        if (!PlanConstants.PLAN_EDITING.equals(child.getBizCurrent())) {
                            deliverablesInfo = new DeliverablesInfo();
                            deliverablesInfo.setUseObjectId(child.getId());
                            deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                            List<DeliverablesInfo> childDeliverables = deliverablesInfoService.queryDeliverableList(
                                    deliverablesInfo, 1, 10, false);
                            for (DeliverablesInfo childDeli : childDeliverables) {
                                deliverablesMap.put(childDeli.getName(), childDeli.getName());
                            }
                        }
                    }
                    for (DeliverablesInfo parentDeli : deliverablesList) {
                        // 判断父级交付物书否被全部覆盖
                        if (StringUtils.isEmpty(deliverablesMap.get(parentDeli.getName()))) {
                            Plan pa = getEntity(p.getParentPlanId());
                            String planSonName = pa.getPlanName();
                            //req.getSession().setAttribute("planSonName", planSonName);
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("planSonName",planSonName);
                            json.setAttributes(map);
                            return false;
                        }
                    }
                }

            }
        }
        return true;
    }

    /**
     * 返回和plan同属一个父级的list（包括plan）
     *
     * @param plan
     * @param plans
     * @return
     * @see
     */
    private List<Plan> getSameParentPlanList(Plan plan, List<Plan> plans) {
        List<Plan> list = new ArrayList<Plan>();
        plan = getEntity(plan.getId());
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            for (Plan p : plans) {
                p = getEntity(p.getId());
                if (!list.contains(p) && plan.getParentPlanId().equals(p.getParentPlanId())) {
                    list.add(p);
                }
            }
        }
        return list;
    }

    @Override
    public Date getMaxPlanEndTimeByProject(Project project) {
        StringBuilder hql = new StringBuilder("");
        hql.append("select max(planEndTime) from Plan t");
        hql.append(" where t.avaliable = '1' ");

        List<Object> paramList = new ArrayList<Object>();
        if (project != null) {
            if (StringUtils.isNotEmpty(project.getId())) {
                hql.append(" and t.projectId = '" + project.getId()
                        + "' and t.planEndTime is not null");
            }
        }
        List<Object> list = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        if (!CommonUtil.isEmpty(list)) {
            return (Date)list.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public Date getMinPlanStartTimeByProject(Project project) {
        StringBuilder hql = new StringBuilder("");
        hql.append("select min(planStartTime) from Plan t");
        hql.append(" where t.avaliable = '1' ");

        List<Object> paramList = new ArrayList<Object>();
        if (project != null) {
            if (StringUtils.isNotEmpty(project.getId())) {
                hql.append(" and t.projectId = '" + project.getId()
                        + "' and t.planStartTime is not null");
            }
        }
        List<Object> list = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
        if (!CommonUtil.isEmpty(list)) {
            return (Date)list.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public void updatePlan(Plan plan) {
        sessionFacade.updateEntitie(plan);
    }

    @Override
    public void updateEntity(Plan plan) {
        sessionFacade.updateEntitie(plan);
    }

    @Override
    public void saveOrUpdate(Plan plan) {
        sessionFacade.saveOrUpdate(plan);
    }

    /**
     * 改变计划下达中记录字段
     *
     * @param formId
     * @see
     */
    @Override
    public void planAssignBack(String formId) {
        ApprovePlanForm approvePlanForm = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class,formId);
        List<Plan> list = getPlanListByApprovePlanForm(approvePlanForm);
        for (Plan p : list) {
            Plan plan = getEntity(p.getId());
            plan.setIsAssignSingleBack("true");
            saveOrUpdate(plan);
        }
    }

    @Override
    public void discardPlans(String id, String userId) {

        List<Inputs> inputsList = new ArrayList<>();

        String inpStr = (String)redisService.getFromRedis("INPUTSLIST", id);
        if(!CommonUtil.isEmpty(inpStr)){
            inputsList = JSON.parseArray(inpStr,Inputs.class);
        }
        // FIXME delete
        redisService.delete(id);
        List<Inputs> inputsLists = new ArrayList<>();
        String inputStr = (String)redisService.getFromRedis("INPUTSLIST", id);
        if(!CommonUtil.isEmpty(inputStr)){
            inputsLists = JSON.parseArray(inputStr,Inputs.class);
        }
        if (!CommonUtil.isEmpty(inputsLists)) {
            redisService.deleteFromRedis("INPUTSLIST", id);
        }
        Plan plan = (Plan) sessionFacade.getEntity(Plan.class, id);
        try {
            TSUserDto user =userService.getUserByUserId(userId);
            planInvalidMsgNotice.getAllMessage(id, user);
        }
        catch (Exception e) {}
        // 删除后置计划的 redis缓存
        List<PreposePlan> preposePlans = sessionFacade.executeQuery(
                "from PreposePlan l where l.preposePlanId=?", new Object[] {id});
        {
            if (!CommonUtil.isEmpty(preposePlans)) {
                for (PreposePlan p : preposePlans) {
                    redisService.deleteFromRedis("INPUTSLIST", p.getPlanId());
                }
            }
        }

        // 删除其相关的后置计划的输入

        inputsService.deleteInputsByOriginObject(plan.getId(), PlanConstants.USEOBJECT_TYPE_PLAN);
        cancelPlan(plan, userId);
        plan.setBizCurrent(PlanConstants.PLAN_INVALID);
        plan.setInvalidTime(new Date());
        plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
        saveOrUpdate(plan);
        resourceEverydayuseService.delResourceUseByPlanAndOperationTime(id, plan.getInvalidTime());
        List<Plan> plans = getPlanAllChildren(plan);
        if (plans != null && plans.size() > 1) {
            for (Plan p : plans) {
                if (!id.equals(p.getId())) {
                    inputsService.deleteInputsByOriginObject(p.getId(), PlanConstants.USEOBJECT_TYPE_PLAN);
                    if (PlanConstants.PLAN_EDITING.equals(p.getBizCurrent())) {
                        p.setAvaliable("0");
                        cancelPlan(p, userId);
                        p.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                        p.setInvalidTime(new Date());
                        saveOrUpdate(p);
                        resourceEverydayuseService.delResourceUseByPlan(p.getId());
                    }
                    else {
                        TSUserDto user =userService.getUserByUserId(userId);
                        planInvalidMsgNotice.getAllMessage(p.getId(),user);
                        cancelPlan(p, userId);
                        p.setBizCurrent(PlanConstants.PLAN_INVALID);
                        p.setInvalidTime(new Date());
                        p.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                        saveOrUpdate(p);
                        resourceEverydayuseService.delResourceUseByPlanAndOperationTime(p.getId(),
                                p.getInvalidTime());
                    }
                }
            }
        }
    }

    /**
     * 废弃id
     *
     * @param
     * @return
     * @see
     */
    @Override
    public void cancelPlan(Plan plan ,String userId) {
        PlanLog planLog = new PlanLog();
        planLog.setPlanId(plan.getId());
        planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_DISCARD);
        TSUserDto user =userService.getUserByUserId(userId);
        planLog.setCreateBy(user.getId());
        planLog.setCreateName(user.getUserName());
        planLog.setCreateFullName(user.getRealName());
        planLog.setCreateTime(new Date());
        planLogService.save(planLog);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", user.getUserName());

        // 流程终止
        if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
            String taskId = null;
            if (StringUtils.isNotEmpty(plan.getPlanReceivedProcInstId())) {
                FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                        plan.getPlanReceivedProcInstId());
                if (feignJson.isSuccess()) {
                    taskId = feignJson.getObj() == null ? "" : feignJson.getObj().toString();
                }
                if (!CommonUtil.isEmpty(taskId)) {
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                            plan.getId(), plan.getPlanReceivedProcInstId(), "");

                    MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                            plan.getId());
                    workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask));
                }
            }
        }

        if (!PlanConstants.PLAN_FLOWSTATUS_NORMAL.equals(plan.getFlowStatus())) {
            TemporaryPlan temporaryPlan = new TemporaryPlan();
            temporaryPlan.setPlanId(plan.getId());
            List<TemporaryPlan> listTemporaryPlan = planChangeService.queryTemporaryPlanList(
                    temporaryPlan, 1, 10, false);

            ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
            /*
             * if (listTemporaryPlan != null && listTemporaryPlan.size() > 0) {
             * approvePlanInfo.setPlanId(listTemporaryPlan.get(0).getId()); }else{
             * approvePlanInfo.setPlanId(plan.getId()); }
             */
            if (!PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING.equals(plan.getFlowStatus())) {
                if (PlanConstants.PLAN_FLOWSTATUS_ORDERED.equals(plan.getBizCurrent())
                        && listTemporaryPlan != null && listTemporaryPlan.size() > 0) {
                    approvePlanInfo.setPlanId(listTemporaryPlan.get(0).getId());
                }
                else {
                    approvePlanInfo.setPlanId(plan.getId());
                }
                List<ApprovePlanInfo> approvePlanInfoList = queryAssignList(approvePlanInfo, 1,
                        10, false);

                if (!CommonUtil.isEmpty(approvePlanInfoList)) {
                    String formId = approvePlanInfoList.get(0).getFormId();
                    ApprovePlanInfo a = new ApprovePlanInfo();
                    a.setFormId(formId);
                    List<ApprovePlanInfo> approveInfoList = queryAssignList(a, 1, 10, false);
                    if (approveInfoList != null && approveInfoList.size() == 1) {
                        // 执行流程
                        ApprovePlanForm approvePlanForm = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, formId);
                        String taskId = null;
                        if (approvePlanForm.getProcInstId() != null
                                && !"".equals(approvePlanForm.getProcInstId())) {
                            FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                                    approvePlanForm.getProcInstId());
                            if (feignJson.isSuccess()) {
                                taskId = feignJson.getObj() == null ? "" : feignJson.getObj().toString();
                            }
                            if (!CommonUtil.isEmpty(taskId)) {
                                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                                        formId, approvePlanForm.getProcInstId(), "");

                                MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                                        formId);
                                workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,
                                        String.valueOf(myStartedTask.getTaskId()));
                            }
                        }
                    }
                    else if (approveInfoList != null && approveInfoList.size() > 1) {
                        sessionFacade.delete(approvePlanInfoList.get(0));
                    }
                }
            }
            else if (PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING.equals(plan.getFlowStatus())) {
                String taskId = null;
                if (StringUtils.isNotEmpty(plan.getFeedbackProcInstId())) {
                    FeignJson feignJson = workFlowFacade.getWorkFlowCommonService().getTaskIdByProcessInstance(
                            plan.getFeedbackProcInstId());
                    if (feignJson.isSuccess()) {
                        taskId = feignJson.getObj() == null ? "" : feignJson.getObj().toString();
                    }
                    if (!CommonUtil.isEmpty(taskId)) {
                        workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                                plan.getId(), plan.getFeedbackProcInstId(), "");

                        MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                                plan.getId());
                        workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask));
                    }
                }
            }

            plan.setBizCurrent(PlanConstants.PLAN_INVALID);
            saveOrUpdate(plan);

        }

        // 删除后置关系
        Plan p = new Plan();
        p.setPreposeIds(plan.getId());
        List<PreposePlan> preposePlanList = preposePlanService.getPostposesByPreposeId(p);
        if (preposePlanList != null && preposePlanList.size() > 0) {
            for (PreposePlan pre : preposePlanList) {
                sessionFacade.delete(pre);
                if (!plan.getId().equals(pre.getPlanId())) {
                    inputsService.deleteInputsByPlan(pre.getPlanInfo());
                }
            }
        }
        // 删除前置关系需求变更暂时不删
        /*
         * List<PreposePlan> preposePlanList2 = preposePlanService.getPreposePlansByPlanId(plan);
         * if(preposePlanList2 != null&& preposePlanList2.size()>0){ for(PreposePlan pre
         * :preposePlanList2){ delete(pre); } }
         */

        // 删除输入关系需求变更暂时不删
        /*
         * inputsService.deleteInputsByPlan(plan);
         * inputsService.deleteInputsByOriginObject(plan.getId(), "PLAN");
         */

        // 释放资源
        ResourceEverydayuseInfoDto resourceLinkInfo = new ResourceEverydayuseInfoDto();
        resourceLinkInfo.setUseObjectId(plan.getId());
        List<ResourceEverydayuseInfoDto> resourceEveryList = resourceLinkInfoService.queryResourceEverydayuseInfoList(
                resourceLinkInfo, 1, 10, false);
        List<ResourceEverydayuseInfo> resourceList = JSON.parseArray(JSON.toJSONString(resourceEveryList),ResourceEverydayuseInfo.class);
        Date currentDate = new Date();
        if (resourceList != null && resourceList.size() > 0) {
            for (ResourceEverydayuseInfo r : resourceList) {
                if (r.getUseDate() != null) {
                    if (currentDate.getTime() > r.getUseDate().getTime()) {
                        sessionFacade.delete(r);
                    }
                }

            }
        }
    }

    /**
     * 获取项目所有非“已完工”状态的一级计划
     *
     * @param project
     * @return
     * @see
     */
    @Override
    public List<Plan> getOneLevelPlanByProject(Project project) {
        Plan plan = new Plan();
        plan.setProjectId(project.getId());
        // this.initBusinessObject(plan);
        // List<LifeCycleStatus> list = plan.getPolicy().getLifeCycleStatusList();
        // if(CommonUtil.isNotEmpty(list)){
        // plan.setBizCurrent(list.get(list.size()-1).getName());
        // }
        plan.setBizCurrent("FINISH");
        String hql = "from Plan l where l.projectId = '" + plan.getProjectId()
                + "' and l.bizCurrent != '" + plan.getBizCurrent() + "' and l.avaliable = '"
                + plan.getAvaliable() + "'";
        return sessionFacade.findByQueryString(hql);
    }

    @Override
    public long getEditingPlanCount(Plan plan) {
        long count = 0;
        String sql = "select count(t.id) as count from pm_plan t where t.parentplanid='"+plan.getId()+"' and t.bizcurrent='EDITING' and t.avaliable='1'";
        List<Map<String,Object>> objArr = sessionFacade.findForJdbc(sql);
        if(!CommonUtil.isEmpty(objArr)){
            for(Map<String,Object> map : objArr){
                count = Long.valueOf(String.valueOf(map.get("COUNT")));
            }
        }
        return count;
    }

    @Override
    public long judgePlanStatusByPlan(Plan plan) {
        StringBuilder hql = new StringBuilder("");
        hql.append("select count(*) from Plan t");
        hql.append(" where t.avaliable!=0");

        List<Object> paramList = new ArrayList<Object>();
        if (plan != null) {
            if (StringUtils.isNotEmpty(plan.getId())) {
                hql.append(" and t.parentPlanId = ?");
                paramList.add(plan.getId());
            }
            hql.append(" and (t.bizCurrent != ?"); // 获得不为拟制中的数量
            paramList.add(PlanConstants.PLAN_EDITING);

            hql.append(" or t.flowStatus != ?)"); // 获得在流程中的数量
            paramList.add(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
        }

        return sessionFacade.getCount(hql.toString(), paramList.toArray());
    }

    /**
     * Description: <br> 删除项目下所有的计划<br> Implement: <br> 通过项目编号删除所有的计划<br>
     *
     * @see
     */
    @Override
    public void deletePhysicsPlansByProjectId(String projectId) {
        sessionFacade.executeSql2("delete from PM_PLAN where projectId = '" + projectId + "'");
        sessionFacade.executeSql2("delete from PM_DELIVERABLES_INFO where useobjectid in (select id from PM_PLAN where projectId = '"
                + projectId + "')");
        sessionFacade.executeSql2("delete from CM_RESOURCE_LINK_INFO where useobjectid in (select id from PM_PLAN where projectId = '"
                + projectId + "')");
        sessionFacade.executeSql2("delete from CM_RESOURCE_EVERYDAYUSE_INFO where useobjectid in (select id from PM_PLAN where projectId = '"
                + projectId + "')");
        sessionFacade.executeSql2("delete from PM_INPUTS where useobjectid in (select id from PM_PLAN where projectId = '"
                + projectId + "')");
        sessionFacade.executeSql2("delete from PM_PREPOSE_PLAN where planid in (select id from PM_PLAN where projectId = '"
                + projectId
                + "') or preposeplanid in (select id from PM_PLAN where projectId = '"
                + projectId + "')");

        /*
         * Plan plan = new Plan(); plan.setProjectId(projectId); List<Plan> planList =
         * queryPlanList(plan, 1, 10, false); deletePlansPhysics(planList);
         */
    }

    /**
     * 删除计划及其相关的交付物、资源
     *
     * @param planList
     * @return
     * @see
     */
    @Override
    public void deletePlans(List<Plan> planList) {
        for (Plan plan : planList) {
            // 将计划逻辑删除标识设为"0"
            plan.setAvaliable("0");
            // 删除其相关的交付物
            deliverablesInfoService.deleteDeliverablesPhysicsByPlan(plan);
            // 删除其相关的资源
            ResourceLinkInfoDto resourceLinkInfo = new ResourceLinkInfoDto();
            resourceLinkInfo.setUseObjectId(plan.getId());
            resourceLinkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            resourceLinkInfoService.deleteResourceByCondition(resourceLinkInfo);
            resourceEverydayuseService.delResourceUseByPlan(plan.getId());
            // 删除其相关输入
            // inputsService.deleteInputsByPlan(plan);
            // 删除其相关的后置计划的输入
            inputsService.deleteInputsByOriginObject(plan.getId(),
                    PlanConstants.USEOBJECT_TYPE_PLAN);
            // 删除其相关前置
            List<PreposePlan> list1 = preposePlanService.getPreposePlansByPlanId(plan);
            for (PreposePlan prepose : list1) {
                sessionFacade.deleteEntityById(PreposePlan.class, prepose.getId());
            }
            // 删除其后置
            Plan p = new Plan();
            p.setPreposeIds(plan.getId());
            List<PreposePlan> list2 = preposePlanService.getPostposesByPreposeId(plan);
            for (PreposePlan pre : list2) {
                sessionFacade.deleteEntityById(PreposePlan.class, pre.getId());
                if (!plan.getId().equals(pre.getPlanId())) {
                    inputsService.deleteInputsByPlan(pre.getPlanInfo());
                }
            }

            // 删除其子孙节点
            List<Plan> childList = getPlanAllChildren(plan);
            for (Plan child : childList) {
                child.setAvaliable("0");
                // 删除其相关的交付物
                deliverablesInfoService.deleteDeliverablesPhysicsByPlan(child);
                // 删除其相关的资源
                ResourceLinkInfoDto linkInfo = new ResourceLinkInfoDto();
                linkInfo.setUseObjectId(child.getId());
                linkInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
                resourceLinkInfoService.deleteResourceByCondition(linkInfo);
                resourceEverydayuseService.delResourceUseByPlan(child.getId());
                // 删除其相关输入
                inputsService.deleteInputsByPlan(child);
                // 删除其相关的后置计划的输入
                inputsService.deleteInputsByOriginObject(child.getId(),
                        PlanConstants.USEOBJECT_TYPE_PLAN);
                // 删除其相关前置
                List<PreposePlan> preposeList1 = preposePlanService.getPreposePlansByPlanId(child);
                for (PreposePlan pre : preposeList1) {
                    sessionFacade.deleteEntityById(PreposePlan.class, pre.getId());
                }
                // 删除后置
                Plan childp = new Plan();
                childp.setPreposeIds(child.getId());
                List<PreposePlan> preposeList2 = preposePlanService.getPostposesByPreposeId(childp);
                for (PreposePlan pre : preposeList2) {
                    sessionFacade.deleteEntityById(PreposePlan.class, pre.getId());
                    if (!plan.getId().equals(pre.getPlanId())) {
                        inputsService.deleteInputsByPlan(pre.getPlanInfo());
                    }
                }

                if ("true".equals(child.getIsAssignSingleBack()) || (!CommonUtil.isEmpty(child.getFormId()) && "false".equals(child.getIsAssignSingleBack()) )) {
                    ApprovePlanForm approvePlanForm = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class,
                            child.getFormId());
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                            child.getFormId(), approvePlanForm.getProcInstId(), "");
                    MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                            child.getFormId());
                    workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask.getTaskId()));
                }

                if(!CommonUtil.isEmpty(child.getPlanDelegateProcInstId()) && "false".equals(child.getIsDelegateComplete())){
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                            child.getId(), child.getPlanDelegateProcInstId(), "");

                    MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                            child.getId());
                    workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask));
                }

                if(!CommonUtil.isEmpty(child.getPlanReceivedProcInstId()) && CommonUtil.isEmpty(child.getPlanReceivedCompleteTime())){
                    workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                            child.getId(), child.getPlanReceivedProcInstId(), "");

                    MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                            child.getId());
                    workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask));
                }

                sessionFacade.saveOrUpdate(child);

            }
            // 删除计划流程
            sessionFacade.saveOrUpdate(plan);
            if ("true".equals(plan.getIsAssignSingleBack())) {
                ApprovePlanForm approvePlanForm = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class,
                        plan.getFormId());
                workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(
                        plan.getFormId(), approvePlanForm.getProcInstId(), "");
                MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                        plan.getFormId());
                workFlowFacade.getWorkFlowStartedTaskService().deleteMyStartedTaskById(appKey,String.valueOf(myStartedTask.getTaskId()));
            }
        }
    }

    @Override
    public void inputForWork(Set<String> mapKeys, String projectIdForPlan, Plan plan, String type, String userId,List<List<Map<String, Object>>> taskMapList,Map<String, List<String>> preposePlanIdMap)
            throws IOException {

        String taskType = PlanConstants.PLAN_TYPE_TASK;
        // 获取当前用户的角色
        // String userId = UserUtil.getInstance().getUser().getId();
        String teamId = projRoleService.getTeamIdByProjectId(projectIdForPlan);
        boolean isPmo = groupService.judgeHasLimit(appKey,ProjectRoleConstants.PMO, userId, null);
        boolean isProjectManger = groupService.judgeHasLimit(appKey,ProjectRoleConstants.PROJ_MANAGER,
                userId, teamId);
        TSUserDto user = userService.getUserByUserId(userId);
        // 如果上方计划不为空，需要获得上级的计划编号
        if (StringUtils.isNotEmpty(plan.getId())) {
            if ("insert".equals(type)) {
                Plan upPlan = new Plan();
                upPlan = getEntity(plan.getId());
                // 获取上方计划的父级计划的计划类别
                if (StringUtils.isNotEmpty(upPlan.getParentPlanId())) {
                    Plan selectedPlan = getEntity(upPlan.getParentPlanId());
                    if (selectedPlan != null
                            && StringUtils.isNotEmpty(selectedPlan.getParentPlanId())) {
                        Plan parent = getEntity(selectedPlan.getParentPlanId());
                        if (PlanConstants.PLAN_TYPE_WBS.equals(parent.getTaskType())
                                && (isPmo || isProjectManger)) {
                            taskType = PlanConstants.PLAN_TYPE_WBS;
                        }
                    }
                    else {
                        if (isPmo || isProjectManger) {
                            taskType = PlanConstants.PLAN_TYPE_WBS;
                        }
                    }
                }
                else {
                    if (isPmo || isProjectManger) {
                        taskType = PlanConstants.PLAN_TYPE_WBS;
                    }
                }
            }
            else {
                Plan selectedPlan = getEntity(plan.getId());
                if (selectedPlan != null) {
                    if (PlanConstants.PLAN_TYPE_WBS.equals(selectedPlan.getTaskType())
                            && (isPmo || isProjectManger)) {
                        taskType = PlanConstants.PLAN_TYPE_WBS;
                    }
                }
                else {
                    if (isPmo || isProjectManger) {
                        taskType = PlanConstants.PLAN_TYPE_WBS;
                    }
                }
            }
        }
        else {
            if (isPmo || isProjectManger) {
                taskType = PlanConstants.PLAN_TYPE_WBS;
            }
        }

        String defaultNameType = "";
        String dictCode = "activeCategory";
        Map<String,List<TSTypeDto>> tsMap = feignSystemService.getAllTypesOfTypeGroup(ResourceUtil.getApplicationInformation().getAppKey());
        List<TSTypeDto> types = tsMap.get(dictCode);
        if (!CommonUtil.isEmpty(types)) {
            defaultNameType = types.get(0).getTypecode();
        }

        // 获取当前系统是否启用标准名称库
        Map<String, String> standardNameMap = new HashMap<String, String>();
        boolean isStandard = false;
        String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
        if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                || NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
            isStandard = true;
        }

        if (isStandard) {
            NameStandardDto ns = new NameStandardDto();
            ns.setStopFlag(ConfigStateConstants.START);
            //调用common 获取活动名称库
            List<NameStandardDto> list = nameStandardFeignService.searchNameStandardsAccurate(ns);
            for (NameStandardDto n : list) {
                standardNameMap.put(n.getName(), n.getActiveCategory());
            }
        }

        int count = 0;
        for (String mapKey : mapKeys) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(taskMapList.get(count))){
                mapList = taskMapList.get(count);
            }

//            List<String> idList = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(preposePlanIdList.get(count))){
//                idList = preposePlanIdList.get(count);
//            }

            boolean insertFlag = false;
            // 如果下方导入时，把insertFlag变成true
            if (StringUtils.isNotEmpty(type)) {
                insertFlag = true;
            }

            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("projectId", projectIdForPlan);
            paraMap.put("switchStr", switchStr); // 项目参数名称库配置
            paraMap.put("taskType", taskType);
            paraMap.put("isStandard", isStandard); // 是否启用名称库
            paraMap.put("defaultNameType", defaultNameType);
            paraMap.put("type", type);
            paraMap.put("user", user);
            paraMap.put("insertFlag", insertFlag);

            savePlanImport(plan, mapList, standardNameMap, paraMap, preposePlanIdMap);
            count++;
        }
    }

    @Override
    public void savePlanImport(Plan plan, List<Map<String, Object>> mapList,
                               Map<String, String> standardNameMap, Map<String, Object> paraMap, Map<String, List<String>> preposePlanIdMap) {

        boolean insertFlag = (boolean)paraMap.get("insertFlag");
        String taskType = (String)paraMap.get("taskType");
        boolean isStandard = (boolean)paraMap.get("isStandard");
        String defaultNameType = (String)paraMap.get("defaultNameType");
        TSUserDto user = (TSUserDto) paraMap.get("user");
        String switchStr = (String)paraMap.get("switchStr"); // 项目参数名称库配置
        String type = (String)paraMap.get("type");
        String projectId = (String)paraMap.get("projectId");
        Plan p1 = new Plan();
        Plan p2 = new Plan();
        // 如果没有上级计划，则需要把计划全部删除
        if (StringUtils.isEmpty(plan.getProjectId())) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.savePlanImportInSerOne"));
        }
        Project project = projectService.findBusinessObjectById(Project.class, plan.getProjectId());
        if (project == null) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.savePlanImportInSerTwo"));
        }
        boolean needDelete = false;
        if (StringUtils.isEmpty(plan.getId())) { // 如果导入计划，则需要把计划全部删除
            // 如果是插入,则不需要删除数据,不是则需要删除数据
            if (!insertFlag) {
                long count = judgePlanStatus(project);
                if (count != 0L) {
                    throw new GWException(
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.savePlanImportInSerThree"));
                }
                needDelete = true;
            }
        }
        else {
            p1 = getEntity(plan.getId());
            p2 = (Plan)p1.clone();
            p2.setId(p1.getId());
        }

        Map<String, String> detailIdPlanIdMap = new HashMap<String, String>();
        Map<String, Plan> detailIdPlanMap = new HashMap<String, Plan>();
        Integer storeyNo = 0;
        Map<String, String> nameDeliverablesMap = nameStandardFeignService.getNameDeliverysMap();
        Map<String, TSUserDto> projectTeamUsers = new HashMap<String, TSUserDto>();
        List<TSUserDto> users = projRoleService.getUserInProject(plan.getProjectId());
        for (TSUserDto teamUser : users) {
            projectTeamUsers.put(teamUser.getUserName(), teamUser);
        }

        Map<String,ActivityTypeManage> activityTypeNameMap = activityTypeManageEntityService.getActivityTypeAndNameMap();
        Map<String,String> activityIdAndTabCbTempMap = tabCbTemplateEntityService.getTabCbTempIdAndActivityIdMap();
        Map<String, String> planLevelMap = new HashMap<String, String>();
        BusinessConfig businessConfig = new BusinessConfig();
        businessConfig.setConfigType(ConfigTypeConstants.PLANLEVEL);
        businessConfig.setStopFlag(ConfigStateConstants.START);
        businessConfig.setAvaliable("1");
        List<BusinessConfig> planLevelConfigs = businessConfigService.searchUseableBusinessConfigs(businessConfig);
        for (BusinessConfig confog : planLevelConfigs) {
            planLevelMap.put(confog.getName(), confog.getId());
        }
        int maxPlanNumber = 0;
        int maxStoreyNo = 0;
        if (!needDelete) {
            maxPlanNumber = getMaxPlanNumber(plan);
            maxStoreyNo = getMaxStoreyNo(plan);
        }

        List<Plan> insertPlans = new ArrayList<Plan>();
        List<PlanLog> insertPlanLogs = new ArrayList<PlanLog>();
        List<DeliverablesInfo> insertDeliverablesInfos = new ArrayList<DeliverablesInfo>();
        List<PreposePlan> insertPreposePlans = new ArrayList<PreposePlan>();

        Plan p = new Plan();
        initBusinessObject(p);
        p.setSecurityLevel(Short.valueOf("1"));
        String bizCurrent = p.getBizCurrent();
        String bizVersion = p.getBizVersion();
        LifeCyclePolicy policy = p.getPolicy();

        DeliverablesInfo d = new DeliverablesInfo();
        deliverablesInfoService.initBusinessObject(d);
        String dBizCurrent = d.getBizCurrent();
        String dBizVersion = d.getBizVersion();
        LifeCyclePolicy dPolicy = p.getPolicy();

        for (Map<String, Object> mapTask : mapList) {
            String ownerName = (String)mapTask.get("ownerName");
            Integer id = (Integer)mapTask.get("id");
            String name = (String)mapTask.get("name");
            Long startLong = (Long)mapTask.get("start");
            Long finishLong = (Long)mapTask.get("finish");
            Date start = new Date(startLong);
            Date finish = new Date(finishLong);
            Object[] arguments = new String[] {id.toString()};
            if (id == 0 || StringUtils.isEmpty(name)) {
                continue;
            }
            else if (name.length() > 30) {
                throw new GWException(I18nUtil.getValue(
                        "com.glaway.ids.pm.project.plan.judgeTaskDateInSerOne", arguments));
            }
            else if (StringUtils.isEmpty(ownerName)) {
                throw new GWException(I18nUtil.getValue(
                        "com.glaway.ids.pm.project.plan.savePlanImportInSerFive", arguments));
            }
            else if (start == null) {
                throw new GWException(I18nUtil.getValue(
                        "com.glaway.ids.pm.project.plan.savePlanImportInSerSix", arguments));
            }
            else if (finish == null) {
                throw new GWException(I18nUtil.getValue(
                        "com.glaway.ids.pm.project.plan.savePlanImportInSerSeven", arguments));
            }
            else {
                Plan detail = new Plan();
                String currentPlanId = UUIDGenerator.generate().toString();
                detail.setId(currentPlanId);
                detailIdPlanIdMap.put(id.toString(), detail.getId());
                if (!ownerName.contains("-")) {
                    throw new GWException(I18nUtil.getValue(
                            "com.glaway.ids.pm.project.plan.savePlanImportInSerEight", arguments));
                }
                String[] ownerNames = ownerName.split("-");
                if (ownerNames.length != 2) {
                    throw new GWException(I18nUtil.getValue(
                            "com.glaway.ids.pm.project.plan.savePlanImportInSerEight", arguments));
                }
                else {
                    TSUserDto u = projectTeamUsers.get(ownerNames[1]);
                    if (u != null) {
                        detail.setOwnerRealName(u.getRealName());
                        detail.setOwner(u.getId());
                    }
                    else {
                        throw new GWException(I18nUtil.getValue(
                                "com.glaway.ids.pm.project.plan.savePlanImportInSerTen", arguments));
                    }
                }

                // 如果导入MPP时，是插入或在下方导入时，计划节点需要重新排序(未做)
                detail.setPlanNumber(id);

                if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr) || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                    if (nameDeliverablesMap.get(name) == null) {
                        throw new GWException(I18nUtil.getValue(
                                "com.glaway.ids.pm.project.plan.savePlanImportInSerFour", arguments));
                    }
                }

                detail.setPlanName(name);
                detail.setProjectId(plan.getProjectId());
                String planlevelpd = (String)mapTask.get("planlevelpd");
                if (planlevelpd != null) {
                    if (StringUtils.isNotEmpty(planLevelMap.get(planlevelpd))) {
                        detail.setPlanLevel(planLevelMap.get(planlevelpd));
                    }else{
                        throw new GWException(I18nUtil.getValue(
                                "com.glaway.ids.pm.project.plan.planLevelIsNullOrNotExist", arguments));
                    }
                }
                // 如果有上级的ID，需要获得上级对应的记录的ID
                Integer parentId1 = (Integer)mapTask.get("parentId");
                if (parentId1 != null
                        && !StringUtils.isEmpty(detailIdPlanIdMap.get(parentId1.toString()))) {
                    String parentId = detailIdPlanIdMap.get(parentId1.toString());
                    detail.setParentPlanId(parentId);
                }
                else {
                    if (StringUtils.isNotEmpty(plan.getId())) {
                        if (insertFlag) {
                            if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
                                detail.setParentPlanId(plan.getParentPlanId());
                            }
                        }
                        else {
                            detail.setParentPlanId(plan.getId());
                        }
                    }
                }

                if (StringUtil.isNotEmpty(plan.getId()) && !insertFlag) {
                    if (p2 != null) {
                        long dayDiff = DateUtil.dayDiff(start, p2.getPlanStartTime());
                        if (dayDiff > 0) {
                            throw new GWException(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plan.judgeTaskDateInSerTwo", arguments));
                        }
                        dayDiff = DateUtil.dayDiff(finish, p2.getPlanEndTime());
                        if (dayDiff < 0) {
                            throw new GWException(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plan.judgeTaskDateInSerThree", arguments));
                        }
                    }
                }

                if (StringUtils.isNotEmpty(detail.getParentPlanId())) {
                    Plan planDb = detailIdPlanMap.get(detail.getParentPlanId());
                    if (planDb != null) {
                        long dayDiff = DateUtil.dayDiff(start, planDb.getPlanStartTime());
                        if (dayDiff > 0) {
                            throw new GWException(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plan.judgeTaskDateInSerTwo", arguments));
                        }
                        dayDiff = DateUtil.dayDiff(finish, planDb.getPlanEndTime());
                        if (dayDiff < 0) {
                            throw new GWException(I18nUtil.getValue(
                                    "com.glaway.ids.pm.project.plan.judgeTaskDateInSerThree", arguments));
                        }
                    }
                }
                else { // 如果没有上级，则计划结束时间少于项结束时间，则不导入

                    long dayDiff = DateUtil.dayDiff(start, project.getStartProjectTime());
                    if (dayDiff > 0) {
                        throw new GWException(I18nUtil.getValue(
                                "com.glaway.ids.pm.project.plan.judgeTaskDateInSerFour", arguments));
                    }

                    dayDiff = DateUtil.dayDiff(finish, project.getEndProjectTime());
                    if (dayDiff < 0) {
                        throw new GWException(I18nUtil.getValue(
                                "com.glaway.ids.pm.project.plan.judgeTaskDateInSerFive", arguments));
                    }
                }
                detail.setPlanStartTime(start);
                detail.setPlanEndTime(finish);

                long workTime = 0;
                if (ProjectConstants.WORKDAY.equals(project.getProjectTimeType())) {
                    workTime = TimeUtil.getWorkDayNumber(detail.getPlanStartTime(),
                            detail.getPlanEndTime());
                }
                else if (ProjectConstants.COMPANYDAY.equals(project.getProjectTimeType())) {
                    Map<String,Object> params = new HashMap<String, Object>();
                    params.put("startDate",detail.getPlanStartTime());
                    params.put("endDate",detail.getPlanEndTime());
                    workTime = calendarService.getAllWorkingDay(appKey,params).size();
                }
                else {
                    workTime = DateUtil.dayDiff(detail.getPlanStartTime(), detail.getPlanEndTime()) + 1;
                }

                String[] workTimeArr = String.valueOf(workTime).split("[.]");
                detail.setWorkTime(workTimeArr[0]);

                // 如果导入模板时计划节点需要重新排序
                // 每次都是最大的planNumber+1
                detail.setPlanNumber(maxPlanNumber + 1);
                maxPlanNumber++ ;

                detail.setStoreyNo(maxStoreyNo + 1);
                maxStoreyNo++ ;

                // 如果是下方计划导入并且计划的上级与上方的上级相同，需要记录下最终的序号
                if (insertFlag
                        && (StringUtils.isEmpty(plan.getParentPlanId()) || plan.getParentPlanId().equals(
                        detail.getParentPlanId()))) {
                    storeyNo = detail.getStoreyNo();
                }

                boolean milestone = (boolean)mapTask.get("milestone");
                detail.setMilestone(String.valueOf(milestone));

                // 保存计划详细
                if (StringUtils.isNotEmpty(plan.getPlanSource())) {
                    detail.setPlanSource(plan.getPlanSource());
                }

                // 给导入计划的计划类型、计划类别设值
                detail.setTaskType(taskType);
                detail.setTaskNameType(defaultNameType);
                if (isStandard) {
                    if (StringUtils.isNotEmpty(standardNameMap.get(detail.getPlanName()))) {
                        detail.setTaskNameType(standardNameMap.get(detail.getPlanName()));
                    }
                }

                detail.setCreateBy(user.getId());
                detail.setBizCurrent(bizCurrent);
                detail.setBizVersion(bizVersion);
                detail.setPolicy(policy);
                detail.setBizId(UUID.randomUUID().toString());
                detailIdPlanMap.put(detail.getId(), detail);
                String taskNameType = (String)mapTask.get("taskNameType");
                ActivityTypeManage activityTypeManageList =activityTypeNameMap.get(taskNameType);
                if (!CommonUtil.isEmpty(activityTypeManageList)){
                    detail.setTaskNameType(activityTypeManageList.getId());
                    //   List<TabCombinationTemplate> list = tabCbTemplateEntityService.findTabCbTemplatesByActivityId(activityTypeManageList.get(0).getId());
                    if(!CommonUtil.isEmpty(activityIdAndTabCbTempMap.get(activityTypeManageList.getId()))){
                        detail.setTabCbTemplateId(activityIdAndTabCbTempMap.get(activityTypeManageList.getId()));
                    }
                }else{
                    throw new GWException(I18nUtil.getValue(
                            "com.glaway.ids.pm.project.plan.judgeTaskDateInSerSix", arguments));
                }
                insertPlans.add(detail);
                // savePlan(detail, user);

                PlanLog planLog = new PlanLog();
                planLog.setId(UUIDGenerator.generate().toString());
                planLog.setPlanId(plan.getId());
                planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);
                planLog.setCreateBy(user.getId());
                planLog.setCreateName(user.getUserName());
                planLog.setCreateFullName(user.getRealName());
                insertPlanLogs.add(planLog);

                // 判断交付项名称是否存在,如果存在保存交付项名称编号
                String documents = (String)mapTask.get("documents");;

                if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                        || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                    if (!nameDeliverablesMap.containsKey(detail.getPlanName())) {
                        log.warn("计划名称【" + detail.getPlanName() + "】不在名称库中，请先联系名称库管理员后再重新上传导入!");
                        Object[] arguments1 = new String[] {detail.getPlanName()};
                        throw new GWException(I18nUtil.getValue(
                                "com.glaway.ids.pm.project.plan.deliverables.saveSystemDeliverable",
                                arguments1));
                    }
                }
                List<DeliverablesInfo> deliverables = deliverablesInfoService.getPlanDeliverablesByDetailAndName(
                        detail, switchStr, documents, nameDeliverablesMap.get(detail.getPlanName()),
                        dBizCurrent, dBizVersion, dPolicy);
                if (!CommonUtil.isEmpty(deliverables)) {
                    insertDeliverablesInfos.addAll(deliverables);
                }

            }
        }
        insertPreposePlans = savePreposePlanTemplateByMpp(mapList,
                detailIdPlanIdMap, preposePlanIdMap);

        for (PreposePlan preposePlan : insertPreposePlans) {
            Plan plan1 = detailIdPlanMap.get(preposePlan.getPlanId());
            Plan plan2 = detailIdPlanMap.get(preposePlan.getPreposePlanId());
            // long dayDiff = DateUtil.dayDiff(plan1.getPlanStartTime(), plan2.getPlanEndTime());
            if (plan1.getPlanStartTime().getTime() > plan2.getPlanEndTime().getTime()) {}
            else {
                throw new GWException("第" + plan1.getPlanNumber() + "行的开始时间必须晚于前置计划的结束时间");
            }
        }

        // 若模板中的业务数据为空，则提示且不进行导入操作
        if (CommonUtil.isEmpty(insertPlans)) {
            throw new GWException(I18nUtil.getValue("com.glaway.ids.common.importDataIsNull"));
        }
        Map<String, Object> flgMap = new HashMap<>();
        flgMap.put("user", user);
        flgMap.put("needDelete", needDelete);
        if (needDelete) {
            flgMap.put("projectId", projectId);
        }
        if (insertFlag && StringUtils.isNotEmpty(plan.getId())) {
            Plan upPlan = getEntity(plan.getId());
            Integer upStoreyNo = upPlan.getStoreyNo();
            flgMap.put("upPlanId", plan.getId());
            flgMap.put("upPlan", upPlan);
            flgMap.put("upStoreyNo", upStoreyNo);
            flgMap.put("storeyNo", storeyNo);
        }

        batchSaveDatas(flgMap, insertPlans, insertPlanLogs, new ArrayList<Inputs>(),
                insertDeliverablesInfos, insertPreposePlans,new ArrayList<Inputs>());
    }

    public List<PreposePlan> savePreposePlanTemplateByMpp(List<Map<String, Object>> mapList, Map<String, String> paraMap, Map<String, List<String>> preposePlanIdMap) {
        List<PreposePlan> list = new ArrayList<PreposePlan>();
        for (Map<String, Object> task : mapList) {
            Integer id = (Integer)task.get("id");
            String name = (String)task.get("name");
            if (id != 0 && StringUtils.isNotEmpty(name)
                    && !CommonUtil.isEmpty(preposePlanIdMap)) {
                 /*for (String preposeId : idList) {
                    String planId = (String)paraMap.get(id.toString());
                    String preposePlanId = (String)paraMap.get(preposeId);
                    if (StringUtils.isNotEmpty(planId) && StringUtils.isNotEmpty(preposePlanId)) {
                        PreposePlan preposePlan = new PreposePlan();
                        preposePlan.setId(UUIDGenerator.generate().toString());
                        preposePlan.setPlanId(planId);
                        preposePlan.setPreposePlanId(preposePlanId);
                        list.add(preposePlan);
                    }
                }*/
                if (preposePlanIdMap.containsKey(id.toString())) {
                    List<String> prePlanIds = preposePlanIdMap.get(id.toString());
                    if (!CommonUtil.isEmpty(prePlanIds)) {
                        for (String preposeId : prePlanIds) {
                            String planId = (String)paraMap.get(id.toString());
                            String preposePlanId = (String)paraMap.get(preposeId);
                            if (StringUtils.isNotEmpty(planId) && StringUtils.isNotEmpty(preposePlanId)) {
                                PreposePlan preposePlan = new PreposePlan();
                                preposePlan.setId(UUIDGenerator.generate().toString());
                                preposePlan.setPlanId(planId);
                                preposePlan.setPreposePlanId(preposePlanId);
                                list.add(preposePlan);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }


    @Override
    public long judgePlanStatus(Project project) {
        StringBuilder hql = new StringBuilder("");
        hql.append("select count(*) from Plan t");
        hql.append(" where t.avaliable!=0");

        List<Object> paramList = new ArrayList<Object>();
        if (project != null) {
            if (StringUtils.isNotEmpty(project.getId())) {
                hql.append(" and t.projectId = ?");
                paramList.add(project.getId());
            }
            hql.append(" and (t.bizCurrent != ?"); // 获得不为拟制中的数量
            paramList.add(PlanConstants.PLAN_EDITING);

            hql.append(" or t.flowStatus != ?)"); // 获得在流程中的数量
            paramList.add(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
        }

        return sessionFacade.getCount(hql.toString(), paramList.toArray());
    }

    /**
     * sql addBatch
     *
     * @param insertPlans
     * @param insertPlanLogs
     * @param insertDeliverablesInfos
     * @param insertPreposePlans
     */
    @SuppressWarnings({"finally"})
    @Override
    public String batchSaveDatas(Map<String, Object> flgMap, List<Plan> insertPlans,
                                 List<PlanLog> insertPlanLogs, List<Inputs> insertInputs,
                                 List<DeliverablesInfo> insertDeliverablesInfos,
                                 List<PreposePlan> insertPreposePlans,List<Inputs> inputsList) {
        String msg = "";
        Connection conn = null;

        // 删除计划及相关信息
        PreparedStatement psFordelPlan = null;
        PreparedStatement psFordelDeliverables = null;
        PreparedStatement psFordelResourceLink = null;
        PreparedStatement psFordelResEverydayUse = null;
        PreparedStatement psFordelInputs = null;
        PreparedStatement psFordelPreposeplan = null;

        // 插入计划及相关信息
        PreparedStatement psForPlan = null;
        PreparedStatement psForPlanLog = null;
        PreparedStatement psForInputs = null;
        PreparedStatement psForDeliverablesInfo = null;
        PreparedStatement psForPreposePlan = null;

        // 更新计划storeyNo
        PreparedStatement psForUpdatePlan = null;
        TSUserDto currentUser = (TSUserDto) flgMap.get("user");
        String createBy = currentUser.getId();
        String createFullName = currentUser.getRealName();
        String createName = currentUser.getUserName();
        String updateBy = currentUser.getId();
        String updateFullName = currentUser.getRealName();
        String updateName = currentUser.getUserName();
        String firstBy = currentUser.getId();
        String firstFullName = currentUser.getRealName();
        String firstName = currentUser.getUserName();
        Timestamp createTime = new Timestamp(new Date().getTime());
        Timestamp updateTime = new Timestamp(new Date().getTime());
        Timestamp firstTime = new Timestamp(new Date().getTime());

        boolean needDelete = (boolean)flgMap.get("needDelete");
        String upPlanId = (String)flgMap.get("upPlanId");
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            // conn.setAutoCommit(false);
            // 当项目下的所有计划为拟制中计划、且选择导入覆盖时
            if (needDelete) {
                String projectId = (String)flgMap.get("projectId");

                // 删除原有计划
                String delSqlForPlan = "delete from PM_PLAN where projectId = ?";
                psFordelPlan = conn.prepareStatement(delSqlForPlan);
                psFordelPlan.setObject(1, projectId);
                psFordelPlan.execute();

                // 删除原有计划相关的交付项
                String delSqlForDeliverables = "delete from PM_DELIVERABLES_INFO"
                        + " where useobjectid in (select id from PM_PLAN where projectId = ?)";
                psFordelDeliverables = conn.prepareStatement(delSqlForDeliverables);
                psFordelDeliverables.setObject(1, projectId);
                psFordelDeliverables.execute();

                // 删除原有计划相关的资源
                String delSqlForResourceLink = "delete from CM_RESOURCE_LINK_INFO"
                        + " where useobjectid in (select id from PM_PLAN where projectId = ?)";
                psFordelResourceLink = conn.prepareStatement(delSqlForResourceLink);
                psFordelResourceLink.setObject(1, projectId);
                psFordelResourceLink.execute();

                // 删除原有计划相关的资源每日使用情况
                String delSqlForResEverydayUse = "delete from CM_RESOURCE_EVERYDAYUSE_INFO"
                        + " where useobjectid in (select id from PM_PLAN where projectId = ?)";
                psFordelResEverydayUse = conn.prepareStatement(delSqlForResEverydayUse);
                psFordelResEverydayUse.setObject(1, projectId);
                psFordelResEverydayUse.execute();

                // 删除原有计划相关的输入
                String delSqlForInputs = "delete from PM_INPUTS"
                        + " where useobjectid in (select id from PM_PLAN where projectId = ?)";
                psFordelInputs = conn.prepareStatement(delSqlForInputs);
                psFordelInputs.setObject(1, projectId);
                psFordelInputs.execute();

                // 删除原有计划相关的前后置关系
                String delSqlForPreposeplan = "delete from PM_PREPOSE_PLAN"
                        + " where planid in (select id from PM_PLAN where projectId = ? )"
                        + " or preposeplanid in (select id from PM_PLAN where projectId = ? )";
                psFordelPreposeplan = conn.prepareStatement(delSqlForPreposeplan);
                psFordelPreposeplan.setObject(1, projectId);
                psFordelPreposeplan.setObject(2, projectId);
                psFordelPreposeplan.execute();
            }

            String progressRate = planFeedBackService.getWeightByStatus("EDITING");
            // 批量插入计划
            if (!CommonUtil.isEmpty(insertPlans)) {
                String sqlForPlan = " insert into PM_PLAN ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                        + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
                        + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
                        + " SECURITYLEVEL, AVALIABLE,"
                        + " PLANNAME, PLANNUMBER, PROJECTID, PARENTPLANID,"
                        + " PLANLEVEL, PLANSTARTTIME, PLANENDTIME, "
                        + " PROGRESSRATE, STOREYNO, MILESTONE, PLANSOURCE,"
                        + " WORKTIME, WORKTIMEREFERENCE, TASKNAMETYPE, TASKTYPE, "
                        + " FLOWSTATUS, CELLID, FROMTEMPLATE, REQUIRED, OWNER,CREATEORGID,TABCBTEMPLATEID) "
                        + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'),"
                        + " to_date(?, 'yyyy-mm-dd'), ?, ?, ?, ?,"
                        + " ?, ?, ?, ?,'NORMAL'," + " ?, 'true', ?, ?,?,?)";
                psForPlan = conn.prepareStatement(sqlForPlan);
                for (Plan plan : insertPlans) {
                    psForPlan.setObject(1, plan.getId());
                    psForPlan.setObject(2, createTime);
                    psForPlan.setObject(3, createBy);
                    psForPlan.setObject(4, createFullName);
                    psForPlan.setObject(5, createName);
                    psForPlan.setObject(6, updateTime);
                    psForPlan.setObject(7, updateBy);
                    psForPlan.setObject(8, updateFullName);
                    psForPlan.setObject(9, updateName);
                    psForPlan.setObject(10, firstTime);
                    psForPlan.setObject(11, firstBy);
                    psForPlan.setObject(12, firstFullName);
                    psForPlan.setObject(13, firstName);
                    psForPlan.setObject(14, plan.getPolicy().getId());
                    psForPlan.setObject(15, plan.getBizCurrent());
                    psForPlan.setObject(16, plan.getBizId());
                    psForPlan.setObject(17, plan.getBizVersion());
                    psForPlan.setObject(18, plan.getSecurityLevel());
                    psForPlan.setObject(19, plan.getAvaliable());
                    psForPlan.setObject(20, plan.getPlanName());
                    psForPlan.setObject(21, plan.getPlanNumber());
                    psForPlan.setObject(22, plan.getProjectId());
                    psForPlan.setObject(23, plan.getParentPlanId());
                    psForPlan.setObject(24, plan.getPlanLevel());
                    psForPlan.setObject(25,
                            DateUtil.dateToString(plan.getPlanStartTime(), DateUtil.YYYY_MM_DD));
                    psForPlan.setObject(26,
                            DateUtil.dateToString(plan.getPlanEndTime(), DateUtil.YYYY_MM_DD));
                    psForPlan.setObject(27, progressRate);
                    psForPlan.setObject(28, plan.getStoreyNo());
                    psForPlan.setObject(29, plan.getMilestone());
                    psForPlan.setObject(30, plan.getPlanSource());
                    psForPlan.setObject(31, plan.getWorkTime());
                    psForPlan.setObject(32, plan.getWorkTimeReference());
                    psForPlan.setObject(33, plan.getTaskNameType());
                    psForPlan.setObject(34, plan.getTaskType());
                    psForPlan.setObject(35, plan.getCellId());
                    psForPlan.setObject(36,
                            StringUtils.isEmpty(plan.getRequired()) ? "" : plan.getRequired());
                    psForPlan.setObject(37,
                            StringUtils.isEmpty(plan.getOwner()) ? "" : plan.getOwner());
                    psForPlan.setObject(38,
                            StringUtils.isEmpty(plan.getCreateOrgId()) ? "" : plan.getCreateOrgId());
                    psForPlan.setObject(39, plan.getTabCbTemplateId());
                    psForPlan.addBatch();
                }
                psForPlan.executeBatch();
            }

            // 批量插入计划日志
            if (!CommonUtil.isEmpty(insertPlanLogs)) {
                String sqlForPlanLog = " insert into PM_PLAN_LOG ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                        + " PLANID, LOGINFO,CREATEORGID)" + " values (" + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, ?,?," + " ?)";
                psForPlanLog = conn.prepareStatement(sqlForPlanLog);
                for (PlanLog planLog : insertPlanLogs) {
                    psForPlanLog.setObject(1, planLog.getId());
                    psForPlanLog.setObject(2, createTime);
                    psForPlanLog.setObject(3, createBy);
                    psForPlanLog.setObject(4, createFullName);
                    psForPlanLog.setObject(5, createName);
                    psForPlanLog.setObject(6, updateTime);
                    psForPlanLog.setObject(7, updateBy);
                    psForPlanLog.setObject(8, updateFullName);
                    psForPlanLog.setObject(9, updateName);
                    psForPlanLog.setObject(10, planLog.getPlanId());
                    psForPlanLog.setObject(11, planLog.getLogInfo());
                    psForPlanLog.setObject(12, CommonUtil.isEmpty(planLog.getCreateOrgId())?"":planLog.getCreateOrgId());
                    psForPlanLog.addBatch();
                }
                psForPlanLog.executeBatch();
            }

            // 批量插入输入
            if (!CommonUtil.isEmpty(insertInputs)) {
                String sqlForInputs = " insert into PM_INPUTS ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, USEOBJECTID,"
                        + " USEOBJECTTYPE, NAME, ORIGINOBJECTID, ORIGINDELIVERABLESINFOID,DOCID,DOCNAME,OriginType,OriginTypeExt,CREATEORGID) "
                        + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?,?,?,?,?,?)";
                psForInputs = conn.prepareStatement(sqlForInputs);
                for (Inputs inputs : insertInputs) {
                    psForInputs.setObject(1, inputs.getId());
                    psForInputs.setObject(2, createTime);
                    psForInputs.setObject(3, createBy);
                    psForInputs.setObject(4, createFullName);
                    psForInputs.setObject(5, createName);
                    psForInputs.setObject(6, updateTime);
                    psForInputs.setObject(7, updateBy);
                    psForInputs.setObject(8, updateFullName);
                    psForInputs.setObject(9, updateName);
                    psForInputs.setObject(10, inputs.getUseObjectId());
                    psForInputs.setObject(11, inputs.getUseObjectType());
                    psForInputs.setObject(12, inputs.getName());
                    psForInputs.setObject(13, inputs.getOriginObjectId());
                    psForInputs.setObject(14, inputs.getOriginDeliverablesInfoId());
                    psForInputs.setObject(15, inputs.getDocId());
                    psForInputs.setObject(16, inputs.getDocName());
                    psForInputs.setObject(17, inputs.getOriginType());
                    psForInputs.setObject(18, inputs.getOriginTypeExt());
                    psForInputs.setObject(19, CommonUtil.isEmpty(inputs.getCreateOrgId())?"":inputs.getCreateOrgId());
                    psForInputs.addBatch();
                }
                psForInputs.executeBatch();
            }

            // 批量插入输入
            if (!CommonUtil.isEmpty(inputsList)) {
                String sqlForInputs = " insert into PM_INPUTS ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, USEOBJECTID,"
                        + " USEOBJECTTYPE, NAME, ORIGINOBJECTID, ORIGINDELIVERABLESINFOID,DOCID,DOCNAME,OriginType,OriginTypeExt,CREATEORGID) "
                        + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?,?,?,?,?,?)";
                psForInputs = conn.prepareStatement(sqlForInputs);
                for (Inputs inputs : inputsList) {
                    psForInputs.setObject(1, UUID.randomUUID().toString().replaceAll("-", ""));
                    psForInputs.setObject(2, createTime);
                    psForInputs.setObject(3, createBy);
                    psForInputs.setObject(4, createFullName);
                    psForInputs.setObject(5, createName);
                    psForInputs.setObject(6, updateTime);
                    psForInputs.setObject(7, updateBy);
                    psForInputs.setObject(8, updateFullName);
                    psForInputs.setObject(9, updateName);
                    psForInputs.setObject(10, inputs.getUseObjectId());
                    psForInputs.setObject(11, "PLAN");
                    psForInputs.setObject(12, inputs.getName());
                    psForInputs.setObject(13, inputs.getOriginObjectId());
                    psForInputs.setObject(14, inputs.getOriginDeliverablesInfoId());
                    psForInputs.setObject(15, inputs.getDocId());
                    psForInputs.setObject(16, inputs.getDocName());
                    if(inputs.getOriginType().equals("PLANTEMPLATE")){
                        psForInputs.setObject(17, "PLAN");
                    }else{
                        psForInputs.setObject(17, inputs.getOriginType());
                    }

                    psForInputs.setObject(18, inputs.getOriginTypeExt());
                    psForInputs.setObject(19, CommonUtil.isEmpty(inputs.getCreateOrgId())?"":inputs.getCreateOrgId());
                    psForInputs.addBatch();
                }
                psForInputs.executeBatch();
            }



            // 批量插入计划交付项
            if (!CommonUtil.isEmpty(insertDeliverablesInfos)) {
                String sqlForDeliverablesInfo = " insert into PM_DELIVERABLES_INFO ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                        + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
                        + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
                        + " SECURITYLEVEL, AVALIABLE,"
                        + " USEOBJECTID, USEOBJECTTYPE, NAME, ORIGIN, REQUIRED,CREATEORGID) "
                        + " values (" + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, ?," + " ?, ?, ?,?,?)";
                psForDeliverablesInfo = conn.prepareStatement(sqlForDeliverablesInfo);
                for (DeliverablesInfo info : insertDeliverablesInfos) {
                    psForDeliverablesInfo.setObject(1, info.getId());
                    psForDeliverablesInfo.setObject(2, createTime);
                    psForDeliverablesInfo.setObject(3, createBy);
                    psForDeliverablesInfo.setObject(4, createFullName);
                    psForDeliverablesInfo.setObject(5, createName);
                    psForDeliverablesInfo.setObject(6, updateTime);
                    psForDeliverablesInfo.setObject(7, updateBy);
                    psForDeliverablesInfo.setObject(8, updateFullName);
                    psForDeliverablesInfo.setObject(9, updateName);
                    psForDeliverablesInfo.setObject(10, firstTime);
                    psForDeliverablesInfo.setObject(11, firstBy);
                    psForDeliverablesInfo.setObject(12, firstFullName);
                    psForDeliverablesInfo.setObject(13, firstName);
                    psForDeliverablesInfo.setObject(14, info.getPolicy().getId());
                    psForDeliverablesInfo.setObject(15, info.getBizCurrent());
                    psForDeliverablesInfo.setObject(16, info.getBizId());
                    psForDeliverablesInfo.setObject(17, info.getBizVersion());
                    psForDeliverablesInfo.setObject(18, info.getSecurityLevel());
                    psForDeliverablesInfo.setObject(19, info.getAvaliable());
                    psForDeliverablesInfo.setObject(20, info.getUseObjectId());
                    psForDeliverablesInfo.setObject(21, info.getUseObjectType());
                    psForDeliverablesInfo.setObject(22, info.getName());
                    psForDeliverablesInfo.setObject(23, info.getOrigin());
                    psForDeliverablesInfo.setObject(24, info.getRequired());
                    psForDeliverablesInfo.setObject(25, CommonUtil.isEmpty(info.getCreateOrgId())?"":info.getCreateOrgId());
                    psForDeliverablesInfo.addBatch();
                }
                psForDeliverablesInfo.executeBatch();
            }

            // 批量插入计划前后置关系
            if (!CommonUtil.isEmpty(insertPreposePlans)) {
                String sqlForPreposePlan = " insert into PM_PREPOSE_PLAN ("
                        + "ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                        + " PLANID, PREPOSEPLANID,CREATEORGID)" + " values ("
                        + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,?," + " ?)";
                psForPreposePlan = conn.prepareStatement(sqlForPreposePlan);
                for (PreposePlan prepose : insertPreposePlans) {
                    psForPreposePlan.setObject(1, prepose.getId());
                    psForPreposePlan.setObject(2, createTime);
                    psForPreposePlan.setObject(3, createBy);
                    psForPreposePlan.setObject(4, createFullName);
                    psForPreposePlan.setObject(5, createName);
                    psForPreposePlan.setObject(6, updateTime);
                    psForPreposePlan.setObject(7, updateBy);
                    psForPreposePlan.setObject(8, updateFullName);
                    psForPreposePlan.setObject(9, updateName);
                    psForPreposePlan.setObject(10, prepose.getPlanId());
                    psForPreposePlan.setObject(11, prepose.getPreposePlanId());
                    psForPreposePlan.setObject(12, CommonUtil.isEmpty(prepose.getCreateOrgId())?"":prepose.getCreateOrgId());
                    psForPreposePlan.addBatch();
                }
                psForPreposePlan.executeBatch();
            }

            // 下方导入模板计划时、该计划下方的原有计划storeyNo需更新
            if (StringUtils.isNotEmpty(upPlanId)) {
                Plan upPlan = (Plan)flgMap.get("upPlan");
                Integer upStoreyNo = (Integer)flgMap.get("upStoreyNo");
                Integer storeyNo = (Integer)flgMap.get("storeyNo");
                // 如果是上方导入时，需要更新下方的序号
                List<Plan> upPlanList = getSameLevelPlans(upPlan);
                if (!CommonUtil.isEmpty(upPlanList)) {
                    String sqlForUpdatePlanStoreyno = "update PM_PLAN set storeyno = ? where id = ?";
                    psForUpdatePlan = conn.prepareStatement(sqlForUpdatePlanStoreyno);
                    for (Plan plan : upPlanList) {
                        // 如果计划的序号小于或等于上方计划的充号不做处理
                        if (plan.getStoreyNo() > upStoreyNo) {
                            // 每个storeyNo+基数
                            psForUpdatePlan.setObject(1, plan.getStoreyNo() + storeyNo);
                            psForUpdatePlan.setObject(2, plan.getId());
                            psForUpdatePlan.addBatch();
                        }
                    }
                    psForUpdatePlan.executeBatch();
                }
            }

            //conn.commit();
            msg = insertPlans.size() + "";
        }
        catch (Exception ex) {
            msg = "false";
            try {
                conn.rollback();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        finally {
            try {
                //conn.setAutoCommit(true);
                DBUtil.closeConnection(null, psFordelPlan, conn);
                DBUtil.closeConnection(null, psFordelDeliverables, conn);
                DBUtil.closeConnection(null, psFordelResourceLink, conn);
                DBUtil.closeConnection(null, psFordelResEverydayUse, conn);
                DBUtil.closeConnection(null, psFordelInputs, conn);
                DBUtil.closeConnection(null, psFordelPreposeplan, conn);
                DBUtil.closeConnection(null, psForPlan, conn);
                DBUtil.closeConnection(null, psForPlanLog, conn);
                DBUtil.closeConnection(null, psForInputs, conn);
                DBUtil.closeConnection(null, psForDeliverablesInfo, conn);
                DBUtil.closeConnection(null, psForPreposePlan, conn);
                DBUtil.closeConnection(null, psForUpdatePlan, conn);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return msg;
        }
    }

    @Override
    public FeignJson goAssignPlanOne(String assignId, String userId) {
        FeignJson j = new FeignJson();
        TSUserDto curUser = userService.getUserByUserId(userId);
        Map<String,Object> map = new HashMap<String, Object>();
        Plan p = (Plan) sessionFacade.getEntity(Plan.class,assignId);
        if (!CommonUtil.isEmpty(p) && !CommonUtil.isEmpty(p.getFormId())) {
            ApprovePlanForm a = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, p.getFormId());
            List<TaskDto> tasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstanceAndAssign(
                    a.getProcInstId(), curUser.getUserName());
            String taskId = "";
            String leader = "";
            String leaderId = "";
            String deptLeader = "";
            String deptLeaderId = "";
            if (!CommonUtil.isEmpty(tasks)) {
                taskId = tasks.get(0).getId();
                Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(taskId);
                leader = (String)variables.get("leader");
                deptLeader = (String)variables.get("deptLeader");

                TSUserDto t = userService.getUserByUserName(leader);
                TSUserDto t2 = userService.getUserByUserName(deptLeader);

                leaderId = t.getId();
                deptLeaderId = t2.getId();
            }
            map.put("leaderId", leaderId);
            map.put("deptLeaderId", deptLeaderId);
            map.put("leader", leader);
            map.put("deptLeader", deptLeader);
        }
        else {
            map.put("leaderId", "");
            map.put("deptLeaderId", "");
            map.put("leader", "");
            map.put("deptLeader", "");

        }
        j.setAttributes(map);
        return j;
    }




    @Override
    public String getPlanIdByLinkPlanId2(String planId) {
        String curPlanId = "";
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select rd.linkplanid from rdf_rdtask rd where rd.linkplanid ='" + planId + "'");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("linkplanid");
                if (StringUtils.isNotEmpty(id)) {
                    curPlanId = id;

                }
            }
        }
        return curPlanId;
    }


    /**
     * 返回权限
     *
     * @param folderId
     *
     * @return
     * @see
     */
    @SuppressWarnings("unused")
    @Override
    public String getOutPower(String folderId, String planId,String userId) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class, planId);
        List<String> codeList = ProjLibAuthManager.getAllAuthActionCode();
//        String userId = UserUtil.getInstance().getUser().getId();

        RepFileDto r = repService.getRepFileByRepFileId(appKey, folderId);
        Boolean detail = false;
        Boolean download = false;
        if (!CommonUtil.isEmpty(r)) {
            String createRep = r.getCreateBy();
            Boolean havePower = false;
            Boolean isCreate = false;
            String categoryFileAuths = projLibAuthService.getDocumentFileAuths(folderId, userId);
            if ((!CommonUtil.isEmpty(plan) && !CommonUtil.isEmpty(plan.getOwner()) && plan.getOwner().equals(userId))
                    || (!CommonUtil.isEmpty(plan) && PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING.equals(plan.getFlowStatus()))) {
                return "downloadDetail";
            }
            if (StringUtil.isNotEmpty(categoryFileAuths)) {

                if (categoryFileAuths.contains("detail")) {
                    detail = true;
                }
                if (categoryFileAuths.contains("download")) {
                    download = true;
                }
            }
            else {
                detail = false;
                download = false;
            }
        }
        if (download == true) {
            return "downloadDetail";
        }
        if (detail == true && download == false) {
            return "detail";
        }
        return "";
    }


    @Override
    public List<FlowTaskCellConnectVo> queryFlowTaskCellConnectList(FlowTaskCellConnectVo flowTaskCellConnect) {
        List<FlowTaskCellConnectVo> list = new ArrayList<FlowTaskCellConnectVo>();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select t.id,t.cellId,t.infoid,t.parentplanid,t.targetid,t.targetinfoid from PM_FLOW_TASK_CELL_CONNECT t where 1=1");
        // parentPlanId
        if (StringUtils.isNotEmpty(flowTaskCellConnect.getParentPlanId())) {
            hqlBuffer.append(" and t.parentPlanId = '" + flowTaskCellConnect.getParentPlanId()
                    + "' ");
        }
        // cellId
        if (StringUtils.isNotEmpty(flowTaskCellConnect.getCellId())) {
            hqlBuffer.append(" and t.cellId = '" + flowTaskCellConnect.getCellId() + "'");
        }
        // targetId
        if (StringUtils.isNotEmpty(flowTaskCellConnect.getTargetId())) {
            hqlBuffer.append(" and t.targetId = '" + flowTaskCellConnect.getTargetId() + "'");
        }
        // infoId
        if (StringUtils.isNotEmpty(flowTaskCellConnect.getInfoId())) {
            hqlBuffer.append(" and t.infoId = '" + flowTaskCellConnect.getInfoId() + "'");
        }
        // targetInfoId
        if (StringUtils.isNotEmpty(flowTaskCellConnect.getTargetInfoId())) {
            hqlBuffer.append(" and t.targetInfoId = '" + flowTaskCellConnect.getTargetInfoId()
                    + "'");
        }

        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("ID");
                if (StringUtils.isNotEmpty(id)) {
                    FlowTaskCellConnectVo iitem = new FlowTaskCellConnectVo();
                    iitem.setId(id);
                    iitem.setCellId((String)map.get("CELLID"));
                    iitem.setInfoId((String)map.get("INFOID"));
                    iitem.setParentPlanId((String)map.get("PARENTPLANID"));
                    iitem.setTargetId((String)map.get("TARGETID"));
                    iitem.setTargetInfoId((String)map.get("TARGETINFOID"));
                    list.add(iitem);
                }
            }
        }
        return list;
    }


    @Override
    public List<FlowTaskVo> taskSort(List<FlowTaskVo> changeFlowTaskList) {
        // TODO Auto-generated method stub
        PlanDto parentPlan = null;
        ProjectDto project = null;
        for (int j = 0; j < changeFlowTaskList.size() - 1; j++ ) {
            if (changeFlowTaskList.get(j).getParentPlan() != null && parentPlan == null) {
                parentPlan = changeFlowTaskList.get(j).getParentPlan();
                project = changeFlowTaskList.get(j).getProject();
            }
            FlowTaskVo floTask = new FlowTaskVo();
            for (int k = j; k < changeFlowTaskList.size() - 1; k++ ) {
                if (changeFlowTaskList.get(k).getPlanStartTime() != null) {
                    long s = changeFlowTaskList.get(k).getPlanStartTime().getTime();
                    long s1 = 0;
                    if (changeFlowTaskList.get(k + 1).getPlanStartTime() == null) {
                        changeFlowTaskList.get(k + 1).setPlanStartTime(
                                parentPlan.getPlanStartTime());
                        Date date = (Date)changeFlowTaskList.get(k + 1).getPlanStartTime().clone();
                        if (project != null
                                && StringUtils.isNotEmpty(project.getProjectTimeType())) {
                            if (ProjectConstants.WORKDAY.equals(project.getProjectTimeType())) {
                                changeFlowTaskList.get(k + 1).setPlanEndTime(
                                        com.glaway.foundation.common.util.DateUtil.nextWorkDay(
                                                date,
                                                Integer.valueOf(changeFlowTaskList.get(k + 1).getWorkTime()) - 1));
                            }
                            else if (ProjectConstants.COMPANYDAY.equals(project.getProjectTimeType())) {
                                Map<String,Object> params = new HashMap<>();
                                params.put("startDate", date);
                                params.put("days", Integer.valueOf(changeFlowTaskList.get(k + 1).getWorkTime()) - 1);
                                changeFlowTaskList.get(k + 1).setPlanEndTime(
                                        calendarService.getNextWorkingDay(appKey,params));
                            }
                            else {
                                changeFlowTaskList.get(k + 1).setPlanEndTime(
                                        TimeUtil.getExtraDate(
                                                date,
                                                Integer.valueOf(changeFlowTaskList.get(k + 1).getWorkTime()) - 1));
                            }
                        }
                        else {
                            changeFlowTaskList.get(k + 1).setPlanEndTime(
                                    TimeUtil.getExtraDate(
                                            date,
                                            Integer.valueOf(changeFlowTaskList.get(k + 1).getWorkTime()) - 1));
                        }

                    }
                    s1 = changeFlowTaskList.get(k + 1).getPlanStartTime().getTime();
                    if (s > s1) {
                        floTask = changeFlowTaskList.get(k);
                        changeFlowTaskList.set(k, changeFlowTaskList.get(k + 1));
                        changeFlowTaskList.set(k + 1, floTask);
                    }
                    else if (s == s1) {
                        FlowTaskVo floTask1 = new FlowTaskVo();
                        long endtime1 = changeFlowTaskList.get(k).getPlanEndTime().getTime();
                        long endtime2 = changeFlowTaskList.get(k + 1).getPlanEndTime().getTime();
                        if (endtime1 > endtime2) {
                            floTask1 = changeFlowTaskList.get(k);
                            changeFlowTaskList.set(k, changeFlowTaskList.get(k + 1));
                            changeFlowTaskList.set(k + 1, floTask1);
                        }
                    }
                }
            }

        }

        return changeFlowTaskList;
    }



    /**
     * 保存流程分解产生的计划
     *
     *
     * @param plan
     * @see
     */
    @Override
    public Plan saveFlowResolvePlan(Plan plan,String userId) {
        String hql = "from Plan p where p.avaliable = '1' and p.parentPlanId= '"
                + plan.getParentPlanId() + "' and p.planName = '" + plan.getPlanName() + "'";
        List<Plan> list = sessionFacade.findByQueryString(hql);
        TSUserDto user = userService.getUserByUserId(userId);
        if (!CommonUtil.isEmpty(list)) {
            Plan oldPlan = list.get(0);
            if (oldPlan.getPlanStartTime() != null
                    && (oldPlan.getPlanStartTime().getTime() >= plan.getPlanStartTime().getTime())) {
                return oldPlan;
            }
            else {
                Plan oldPlan1 =  (Plan)sessionFacade.getEntity(Plan.class, oldPlan.getId());
                oldPlan1.setPlanStartTime(plan.getPlanStartTime());
                oldPlan1.setWorkTime(plan.getWorkTime());
                oldPlan1.setPlanEndTime(plan.getPlanEndTime());
                oldPlan1.setUpdateBy(user.getId());
                oldPlan1.setUpdateName(user.getUserName());
                oldPlan1.setUpdateFullName(user.getRealName());
                oldPlan1.setUpdateTime(new Date());
                sessionFacade.saveOrUpdate(oldPlan1);
                return oldPlan1;
            }
        }
        else {
            plan.setPlanNumber(getMaxPlanNumber(plan) + 1);
            plan.setStoreyNo(getMaxStoreyNo(plan) + 1);
            initBusinessObject(plan);
            plan.setCreateBy(user.getId());
            plan.setCreateName(user.getUserName());
            plan.setCreateFullName(user.getRealName());
            plan.setCreateTime(new Date());
            sessionFacade.save(plan);
            // 计划日志记录
            PlanLog planLog = new PlanLog();
            planLog.setPlanId(plan.getId());
            planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);

            /*
             * if (StringUtils.isNotEmpty(plan.getLauncher())) { TSUser user =
             * userService.getEntity(TSUser.class, plan.getLauncher());
             * planLog.setCreateBy(user.getId()); planLog.setCreateName(user.getUserName());
             * planLog.setCreateFullName(user.getRealName()); planLog.setCreateTime(new Date()); }
             */

            planLog.setCreateBy(user.getId());
            planLog.setCreateName(user.getUserName());
            planLog.setCreateFullName(user.getRealName());
            planLog.setCreateTime(new Date());
            sessionFacade.saveOrUpdate(planLog);
            return plan;
        }
    }


    @Override
    public List<ApprovePlanInfo> queryAssignList(ApprovePlanInfo approvePlanInfo, int page,
                                                 int rows, boolean isPage) {
        String hql = createAssignHql(approvePlanInfo);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }

    @Override
    public void queryAssignListAndUpdate(ApprovePlanInfo approvePlanInfo, int page, int rows, boolean isPage) {
        List<ApprovePlanInfo> approve = queryAssignList(approvePlanInfo, 1, 10, false);
        for (int i = 0; i < approve.size(); i++ ) {
            Plan p = getEntity(approve.get(i).getPlanId());
            p.setIsAssignBack("false");
            p.setIsAssignSingleBack("false");
            p.setFlowStatus("ORDERED");
            saveOrUpdate(p);
        }
    }

    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param approvePlanInfo
     * @return
     * @see
     */
    private String createAssignHql(ApprovePlanInfo approvePlanInfo) {
        String hql = "from ApprovePlanInfo l where 1 = 1";
        // 计划ID
        if (StringUtils.isNotEmpty(approvePlanInfo.getId())) {
            hql = hql + " and l.id = '" + approvePlanInfo.getId() + "'";
        }

        if (StringUtils.isNotEmpty(approvePlanInfo.getPlanId())) {
            hql = hql + " and l.planId = '" + approvePlanInfo.getPlanId() + "'";
        }

        if (StringUtils.isNotEmpty(approvePlanInfo.getFormId())) {
            hql = hql + " and l.formId = '" + approvePlanInfo.getFormId() + "'";
        }
        return hql;
    }


    @Override
    public List<Plan> queryPlanInputsList(Plan plan) {
        String hql = "from Plan l where l.bizCurrent <> '" + PlanConstants.PLAN_INVALID + "'";
        // 计划ID
        if (StringUtils.isNotEmpty(plan.getId())) {
            hql = hql + " and l.id = '" + plan.getId() + "'";
        }
        // 所属项目
        if (StringUtils.isNotEmpty(plan.getProjectId())) {
            hql = hql + " and l.projectId = '" + plan.getProjectId() + "'";
        }
        // 父计划
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            hql = hql + " and l.parentPlanId = '" + plan.getParentPlanId() + "'";
        }
        // 计划名称
        if (StringUtils.isNotEmpty(plan.getPlanName())) {
            hql = hql + " and l.planName like '%" + plan.getPlanName() + "%'";
        }
        // 责任人
        if (StringUtils.isNotEmpty(plan.getOwner())) {
            hql = hql + " and l.owner = '" + plan.getOwner() + "'";
        }
        // 生命周期状态
        if (StringUtils.isNotEmpty(plan.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + plan.getBizCurrent() + "'";
        }
        // 下达人
        if (StringUtils.isNotEmpty(plan.getAssigner())) {
            hql = hql + " and l.assigner = '" + plan.getAssigner() + "'";
        }
        // 下达时间
        if (plan.getAssignTimeStart() != null) {
            hql = hql + " and l.assignTime >= " + plan.getAssignTimeStart();
        }
        if (plan.getAssignTimeEnd() != null) {
            hql = hql + " and l.assignTime <= " + plan.getAssignTimeEnd();
        }
        // 计划等级
        if (StringUtils.isNotEmpty(plan.getPlanLevel())) {
            hql = hql + " and l.planLevel = '" + plan.getPlanLevel() + "'";
        }

        // cellId
        if (StringUtils.isNotEmpty(plan.getCellId())) {
            hql = hql + " and l.cellId = '" + plan.getCellId() + "'";
        }

        // 开始时间
        if (plan.getPlanStartTimeStart() != null) {
            hql = hql + " and l.planStartTime >= " + plan.getPlanStartTimeStart();
        }
        if (plan.getPlanStartTimeEnd() != null) {
            hql = hql + " and l.planStartTime <= " + plan.getPlanStartTimeEnd();
        }
        // 结束时间
        if (plan.getPlanEndTimeStart() != null) {
            hql = hql + " and l.planEndTime >= " + plan.getPlanEndTimeStart();
        }
        if (plan.getPlanEndTimeEnd() != null) {
            hql = hql + " and l.planEndTime <= " + plan.getPlanEndTimeEnd();
        }
        // 是否可用
        if (StringUtils.isNotEmpty(plan.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + plan.getAvaliable() + "'";
        }
        hql = hql + " order by parentPlanId, createTime, storeyno";
        return sessionFacade.findByQueryString(hql);
    }


    @Override
    public List<Plan> queryPlanOrderByStarttime(Plan plan, int page, int rows, boolean isPage) {
        String hql = createHqlOrderByStarttime(plan);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }


    /**
     * 根据Plan中的传值拼接HQL
     *
     *
     * @param plan
     * @return
     * @see
     */
    private String createHqlOrderByStarttime(Plan plan) {
        String hql = "from Plan l where 1 = 1";
        // 计划ID
        if (StringUtils.isNotEmpty(plan.getId())) {
            hql = hql + " and l.id = '" + plan.getId() + "'";
        }
        // 所属项目
        if (StringUtils.isNotEmpty(plan.getProjectId())) {
            hql = hql + " and l.projectId = '" + plan.getProjectId() + "'";
        }
        // 父计划
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            hql = hql + " and l.parentPlanId = '" + plan.getParentPlanId() + "'";
        }
        // 计划名称
        if (StringUtils.isNotEmpty(plan.getPlanName())) {
            hql = hql + " and l.planName like '%" + plan.getPlanName() + "%'";
        }
        // 责任人
        if (StringUtils.isNotEmpty(plan.getOwner())) {
            hql = hql + " and l.owner = '" + plan.getOwner() + "'";
        }
        // 生命周期状态
        if (StringUtils.isNotEmpty(plan.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + plan.getBizCurrent() + "'";
        }
        // 下达人
        if (StringUtils.isNotEmpty(plan.getAssigner())) {
            hql = hql + " and l.assigner = '" + plan.getAssigner() + "'";
        }
        // 下达时间
        if (plan.getAssignTimeStart() != null) {
            hql = hql + " and l.assignTime >= " + plan.getAssignTimeStart();
        }
        if (plan.getAssignTimeEnd() != null) {
            hql = hql + " and l.assignTime <= " + plan.getAssignTimeEnd();
        }
        // 计划等级
        if (StringUtils.isNotEmpty(plan.getPlanLevel())) {
            hql = hql + " and l.planLevel = '" + plan.getPlanLevel() + "'";
        }

        // cellId
        if (StringUtils.isNotEmpty(plan.getCellId())) {
            hql = hql + " and l.cellId = '" + plan.getCellId() + "'";
        }

        // 开始时间
        if (plan.getPlanStartTimeStart() != null) {
            hql = hql + " and l.planStartTime >= " + plan.getPlanStartTimeStart();
        }
        if (plan.getPlanStartTimeEnd() != null) {
            hql = hql + " and l.planStartTime <= " + plan.getPlanStartTimeEnd();
        }
        // 结束时间
        if (plan.getPlanEndTimeStart() != null) {
            hql = hql + " and l.planEndTime >= " + plan.getPlanEndTimeStart();
        }
        if (plan.getPlanEndTimeEnd() != null) {
            hql = hql + " and l.planEndTime <= " + plan.getPlanEndTimeEnd();
        }
        // 是否可用
        if (StringUtils.isNotEmpty(plan.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + plan.getAvaliable() + "'";
        }
        hql = hql + " order by l.planEndTime,l.createTime desc";
        return hql;
    }


    @Override
    public void savePlanByPlanDto(Plan plan) {
        initBusinessObject(plan);
        sessionFacade.save(plan);
    }


    @Override
    public ApprovePlanFormDto getApprovePlanFormEntity(String id) {
        ApprovePlanForm p = (ApprovePlanForm)sessionFacade.getEntity(ApprovePlanForm.class,id);
        ApprovePlanFormDto dto = new ApprovePlanFormDto();
        try {
            dto = (ApprovePlanFormDto)VDataUtil.getVDataByEntity(p,ApprovePlanFormDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public PageList queryEntity(List<ConditionVO> conditionList, String projectId, String userName, String progressRate, String workTime) {
        StringBuffer hqlBuffer = createStringBuffer(projectId);

        if (StringUtils.isNotEmpty(userName)) {
            hqlBuffer.append("  and (OU.realName like '%" + userName + "%'");
            hqlBuffer.append("  or OU.userName like '%" + userName + "%')");
        }
        //   hqlBuffer.append("and PL.bizCurrent <> '" + PlanConstants.PLAN_INVALID + "'");
        for (ConditionVO c : conditionList) {
            if (c.getKey().equals("Plan.progressRate")) {
                if (StringUtils.isNotEmpty(progressRate)) {
                    if ("-1".equals(progressRate) && c.getCondition().equals("le")) {
                        hqlBuffer.append("  and PL.progressRate = " + progressRate);
                    }
                    else if ("-1".equals(progressRate) && c.getCondition().equals("ge")) {

                    }
                    else {
                        // int progressRateNumber = (Integer.parseInt(progressRate));
                        if (c.getCondition().equals("le")) {
                            hqlBuffer.append(" and (PL.progressRate is null");
                            hqlBuffer.append(" or PL.progressRate <= " + progressRate + ")");
                        }
                        else {
                            hqlBuffer.append(" and PL.progressRate >= " + progressRate);
                        }
                    }
                }
            }
            else if (c.getKey().equals("Plan.workTime")) {
                if (StringUtils.isNotEmpty(workTime)) {
                    if ("-1".equals(workTime) && c.getCondition().equals("le")) {
                        hqlBuffer.append(" and PL.workTime = " + workTime);
                    }
                    else if ("-1".equals(workTime) && c.getCondition().equals("ge")) {

                    }
                    else {
                        // int progressRateNumber = (Integer.parseInt(progressRate));
                        if (c.getCondition().equals("le")) {
                            hqlBuffer.append(" and (PL.workTime is null");
                            hqlBuffer.append(" or PL.workTime <= " + workTime + ")");
                        }
                        else {
                            hqlBuffer.append(" and PL.workTime >= " + workTime);
                        }
                    }
                }
            }
            else if (c.getKey().equals("Plan.planStartTime")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValue().split(",");
                    hqlBuffer.append(" and to_date(to_char(PL.planStartTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }
            else if (c.getKey().equals("Plan.planEndTime")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValue().split(",");
                    hqlBuffer.append(" and to_date(to_char(PL.planEndTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }
            else if (c.getKey().equals("Plan.planLevelInfo.id")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.PLANLEVEL in (" + str + ")");
                    }
                }

            }
            else if (c.getKey().equals("Plan.planNumber")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    hqlBuffer.append(" and PL.planNumber like '%" + c.getValue() + "%'");
                }
            }
            else if (c.getKey().equals("Plan.planName")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    hqlBuffer.append(" and PL.planName like '%" + c.getValue() + "%'");
                }
            }
            else if (c.getKey().equals("Plan.bizCurrent")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.bizCurrent in (" + str + ")");
                    }
                }
            }
            else if (c.getKey().equals("Plan.isDelay")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String currentDateStr = DateUtil.getCurrDate("yyyy-MM-dd");
                    if(attr.length < 2){
                        for (String s : attr) {
                            if (StringUtils.isNotEmpty(s.trim())) {
                                if("DELAY".equals(s)){
                                    hqlBuffer.append(" and PL.planEndTime <= to_date('" + currentDateStr + "','yyyy-mm-dd') and PL.bizCurrent <> 'FINISH' and PL.bizCurrent <> 'INVALID'");
                                }
                                if("NORMAL".equals(s)){
                                    hqlBuffer.append(" and PL.id not in (select t.id from pm_plan t where t.planEndTime <= to_date('" + currentDateStr + "','yyyy-mm-dd') and t.bizCurrent <> 'FINISH' and t.bizCurrent <> 'INVALID')");
                                }
                            }
                        }
                    }

                }
            }
            else if (c.getKey().equals("Plan.taskNameType")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.taskNameType in (" + str + ")");
                    }
                }
            }
            else if (c.getKey().equals("Plan.taskType")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.taskType in (" + str + ")");
                    }
                }
            }
        }
        hqlBuffer.append(" ORDER BY PL.PARENTPLANID, PL.STOREYNO, PL.CREATETIME");

        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());

        List<Plan> list = changeQueryResToPlanList(objArrayList);

        List<PlanDto> list1 = CodeUtils.JsonListToList(list, PlanDto.class);
        for (PlanDto planDto:list1) {
            String conCode = getInfoByPlanIdAndUserId(planDto.getId(),planDto.getCreateBy());
            planDto.setConcernCode(conCode);
        }

        PageList pageList = new PageList(list.size(), list1);
        return pageList;
    }


    @Override
    public PageList queryEntityForSelectedPlan(List<ConditionVO> conditionList, String projectId, String userName, String progressRate, String workTime, String planIds) {
        StringBuffer hqlBuffer = createStringBuffer(projectId);

        hqlBuffer.append(" and PL.ID in ("+planIds+")");
        if (StringUtils.isNotEmpty(userName)) {
            hqlBuffer.append("  and (OU.realName like '%" + userName + "%'");
            hqlBuffer.append("  or OU.userName like '%" + userName + "%')");
        }
        for (ConditionVO c : conditionList) {
            if (c.getKey().equals("Plan.progressRate")) {
                if (StringUtils.isNotEmpty(progressRate)) {
                    if ("-1".equals(progressRate) && c.getCondition().equals("le")) {
                        hqlBuffer.append("  and PL.progressRate = " + progressRate);
                    }
                    else if ("-1".equals(progressRate) && c.getCondition().equals("ge")) {

                    }
                    else {
                        // int progressRateNumber = (Integer.parseInt(progressRate));
                        if (c.getCondition().equals("le")) {
                            hqlBuffer.append(" and (PL.progressRate is null");
                            hqlBuffer.append(" or PL.progressRate <= " + progressRate + ")");
                        }
                        else {
                            hqlBuffer.append(" and PL.progressRate >= " + progressRate);
                        }
                    }
                }
            }
            else if (c.getKey().equals("Plan.workTime")) {
                if (StringUtils.isNotEmpty(workTime)) {
                    if ("-1".equals(workTime) && c.getCondition().equals("le")) {
                        hqlBuffer.append(" and PL.workTime = " + workTime);
                    }
                    else if ("-1".equals(workTime) && c.getCondition().equals("ge")) {

                    }
                    else {
                        // int progressRateNumber = (Integer.parseInt(progressRate));
                        if (c.getCondition().equals("le")) {
                            hqlBuffer.append(" and (PL.workTime is null");
                            hqlBuffer.append(" or PL.workTime <= " + workTime + ")");
                        }
                        else {
                            hqlBuffer.append(" and PL.workTime >= " + workTime);
                        }
                    }
                }
            }
            else if (c.getKey().equals("Plan.planStartTime")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValue().split(",");
                    hqlBuffer.append(" and to_date(to_char(PL.planStartTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }
            else if (c.getKey().equals("Plan.planEndTime")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValue().split(",");
                    hqlBuffer.append(" and to_date(to_char(PL.planEndTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }
            else if (c.getKey().equals("Plan.planLevelInfo.id")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.PLANLEVEL in (" + str + ")");
                    }
                }

            }
            else if (c.getKey().equals("Plan.planNumber")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    hqlBuffer.append(" and PL.planNumber like '%" + c.getValue() + "%'");
                }
            }
            else if (c.getKey().equals("Plan.planName")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    hqlBuffer.append(" and PL.planName like '%" + c.getValue() + "%'");
                }
            }
            else if (c.getKey().equals("Plan.bizCurrent")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.bizCurrent in (" + str + ")");
                    }
                }
            }
            else if (c.getKey().equals("Plan.isDelay")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String currentDateStr = DateUtil.getCurrDate("yyyy-MM-dd");
                    if(attr.length < 2){
                        for (String s : attr) {
                            if (StringUtils.isNotEmpty(s.trim())) {
                                if("DELAY".equals(s)){
                                    hqlBuffer.append(" and PL.planEndTime <= to_date('" + currentDateStr + "','yyyy-mm-dd') and PL.bizCurrent <> 'FINISH' and PL.bizCurrent <> 'INVALID'");
                                }
                                if("NORMAL".equals(s)){
                                    hqlBuffer.append(" and PL.id not in (select t.id from pm_plan t where t.planEndTime <= to_date('" + currentDateStr + "','yyyy-mm-dd') and t.bizCurrent <> 'FINISH' and t.bizCurrent <> 'INVALID')");
                                }
                            }
                        }
                    }

                }
            }
            else if (c.getKey().equals("Plan.taskNameType")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.taskNameType in (" + str + ")");
                    }
                }
            }
            else if (c.getKey().equals("Plan.taskType")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.taskType in (" + str + ")");
                    }
                }
            }
        }
        hqlBuffer.append(" ORDER BY PL.PARENTPLANID, PL.STOREYNO, PL.CREATETIME");

        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());

        List<Plan> list = changeQueryResToPlanList(objArrayList);

        List<PlanDto> list1 = CodeUtils.JsonListToList(list, PlanDto.class);

        PageList pageList = new PageList(list.size(), list1);
        return pageList;
    }

    @Override
    public FeignJson startAssignProcess(Map<String, String> params) {
        FeignJson j = new FeignJson();
        String ids = params.get("ids");
        String kddProductType = params.get("kddProductType");
        String assignId = params.get("assignId");
        String userId = params.get("userId");
        String leader = params.get("leader");
        String deptLeader = params.get("deptLeader");
        String assignType = params.get("assignType");

        TSUserDto curUser = userService.getUserByUserId(userId);

        String message  = "项目计划发布流程启动成功";
        // 判断是否是KDD到IDS得计划中
        if (!CommonUtil.isEmpty(kddProductType) && "kddProduct".equals(kddProductType)) {
            message = "计划发布流程启动成功";
        }

        try {
            TSUserDto actor = new TSUserDto();
            actor.setId(curUser.getId());
            actor.setUserName(curUser.getUserName());
            actor.setRealName(curUser.getRealName());

            if (assignType != null && "single".equals(assignType)) {
                Plan plan = getEntity(assignId);
                if ("true".equals(plan.getIsAssignSingleBack())) {
                    String taskId = "";
                    FeignJson taskIdFeignJson = workFlowFacade.getWorkFlowCommonService().getAssignTaskIdByObjectIdForIDS(plan.getFormId(), BpmnConstants.BPMN_START_PLAN);
                    if (taskIdFeignJson.isSuccess()) {
                        taskId = taskIdFeignJson.getObj() == null ? "" : taskIdFeignJson.getObj().toString();
                    }
                    planFlowForworkService.startAssignForWorkFlow(plan.getFormId(), taskId,
                            leader, deptLeader,userId);
                }
                else {
                    planFlowForworkService.startAssignForWork(ids, leader, deptLeader, assignType,
                            actor, assignId);
                }
            }
            else {
                planFlowForworkService.startAssignForWork(ids, leader, deptLeader, assignType,
                        actor, assignId);
            }
            log.info(message, ids, "");
        }
        catch (Exception e) {
            message = "项目计划发布流程启动失败";
            log.error(message, e, ids, "");
            Object[] objs = new Object[] {message, Plan.class.getClass() + " oids:" + ids};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2001, objs, e);
        }
        finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson startPlanFlow(Map<String, String> map) {
        String ids = map.get("ids");
        String formId = map.get("formId");
        String taskId = map.get("taskId");
        String leader = map.get("leader");
        String deptLeader =  map.get("deptLeader");
        String userId = map.get("userId");
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.startAssignSuccess");
        try {
            planFlowForworkService.startAssignForWorkFlow(formId, taskId, leader, deptLeader,userId);
            log.info(message, formId, "");
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.startAssignFail");
            log.error(message, e, "", "");
            Object[] params = new Object[] {message, ApprovePlanForm.class.getClass() + " oids:"};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2001, params, e);
        }
        finally {
            // systemService.addLog(message, Globals.Log_Type_DEL,
            // Globals.Log_Leavel_INFO);
            j.setMsg(message);
            return j;
        }
    }

    /**
     * 通过ApprovePlanForm的ID获取其相关的所有计划
     *
     * @param approvePlanForm
     * @return
     * @see
     */
    @Override
    public List<Plan> getPlanListByApprovePlanForm(ApprovePlanForm approvePlanForm) {
        List<Plan> list = new ArrayList<Plan>();
        List<ApprovePlanInfo> approvePlanInfos = sessionFacade.findByQueryString("from ApprovePlanInfo i where i.formId = '"
                + approvePlanForm.getId() + "'");
        for (ApprovePlanInfo info : approvePlanInfos) {
            if (info != null && info.getPlan() != null) {
                list.add(info.getPlan());
            }
        }
        return list;
    }

    @Override
    public void planLifeCycleForward(String formId, TSUserDto assigner) {
        ApprovePlanForm approvePlanForm = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, formId);
        List<Plan> list = getPlanListByApprovePlanForm(approvePlanForm);
        for (Plan p : list) {
            Plan plan = getEntity(p.getId());
            plan.setAssigner(assigner.getId());
            plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
            plan.setAssignTime(new Date());
            plan.setBizCurrent(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED);

            updateBizCurrent(plan);
            //调用计划反馈引擎计算进度
            updateProgressRate(plan);

            sessionFacade.saveOrUpdate(plan);
            // forwardBusinessObject(plan);
            // 计划日志记录
            PlanLog planLog = new PlanLog();
            TSUserDto user = null;
            if (StringUtils.isNotEmpty(plan.getLauncher())) {
                user = userService.getUserByUserId(plan.getLauncher());
            }
            if (user != null) {
                planLog.setCreateBy(user.getId());
                planLog.setCreateName(user.getUserName());
                planLog.setCreateFullName(user.getRealName());
                planLog.setCreateTime(new Date());
            }

            planLog.setPlanId(plan.getId());
            planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_ORDERED);
            sessionFacade.saveOrUpdate(planLog);

            if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                if(CommonUtil.isEmpty(plan.getPlanReceivedProcInstId())){
                    startPlanReceivedProcess(plan,assigner.getId());
                }else if(!CommonUtil.isEmpty(plan.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(plan.getPlanReceivedCompleteTime())){
                    startPlanReceivedProcess(plan,assigner.getId());
                }
            }

            // 如果存在父计划，则判断父计划的“子计划下达”日志是否记录，若未记录则需记录
            if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
                if (!planLogService.checkSonAssignInfo(plan.getParentPlanId())) {
                    PlanLog sonAssignLog = new PlanLog();
                    if (user != null) {
                        sonAssignLog.setCreateBy(user.getId());
                        sonAssignLog.setCreateName(user.getUserName());
                        sonAssignLog.setCreateFullName(user.getRealName());
                        sonAssignLog.setCreateTime(new Date());
                    }
                    sonAssignLog.setPlanId(plan.getParentPlanId());
                    sonAssignLog.setLogInfo(PlanConstants.PLAN_LOGINFO_SON_ORDERED);
                    sessionFacade.saveOrUpdate(sonAssignLog);
                }
            }
            boolean needHttp = false;
            if (!CommonUtil.isEmpty(p.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(p.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(p.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(p.getTaskNameType())) {
                needHttp = true;
            }
            if (needHttp) {
                List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"taskCategoryHttpServer");
                if (!CommonUtil.isEmpty(outwardExtensionList)) {
                    for (OutwardExtensionDto ext : outwardExtensionList) {
                        if (!CommonUtil.isEmpty(ext.getUrlList())) {
                            List<String> urls = new ArrayList<String>();
                            for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                                if ("step".equals(out.getOperateCode())) {
                                    // 根据名称查询活动名称库：
                                    String id = "";
                                    NameStandardDto nameStandard = new NameStandardDto();
                                    nameStandard.setStopFlag("启用");
                                    nameStandard.setName(p.getPlanName());
                                    List<NameStandardDto> nameStandardlist = nameStandardFeignService.searchNameStandards(nameStandard);
                                    // nameStandardlist不为空说明使用了活动名称库
                                    if (!CommonUtil.isEmpty(nameStandardlist)) {
                                        id = nameStandardlist.get(0).getId();
                                    }
                                    urls.add(out.getOperateUrl() + "&planId=" + p.getId()
                                            + "&productId=" + p.getProjectId()
                                            + "&nameStandardId=" + id);
                                }
                            }
                            if (!CommonUtil.isEmpty(urls)) {
                                for (String operateUrl : urls) {
                                    try {
                                        HttpClientUtil.httpClientPostByTest(operateUrl, null);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }

                }

            }
        }
    }

    @Override
    public void updateBizCurrent(Plan plan) {
        String bizCurrent = PlanConstants.PLAN_FLOWSTATUS_LAUNCHED;
        if(!CommonUtil.isEmpty(plan.getId())){
            if(!CommonUtil.isEmpty(plan.getParentPlanId())){
                Plan parentPlan = (Plan)sessionFacade.getEntity(Plan.class,plan.getParentPlanId());
                if(PlanConstants.PLAN_EDITING.equals(parentPlan.getBizCurrent()) || PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(parentPlan.getBizCurrent())  //任务4837
                        || PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(parentPlan.getBizCurrent())){
                    bizCurrent = PlanConstants.PLAN_FLOWSTATUS_LAUNCHED;
                }else{
                    bizCurrent = getBizCurrent(plan);
                }
            }else{
                bizCurrent = getBizCurrent(plan);
            }
        }
        plan.setBizCurrent(bizCurrent);
    }

    private String getBizCurrent(Plan plan){
        String bizCurrent;
        List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where planId=?",plan.getId());
        List<Inputs> inputsList = sessionFacade.findHql("from Inputs where useObjectId = ?",plan.getId());

        //无前置计划
        if(CommonUtil.isEmpty(preposePlanList)){
            //无输入项
            if(CommonUtil.isEmpty(inputsList)){
                bizCurrent = PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED;   //无前置计划且无输入项状态更新为待接收
            }else{
                bizCurrent = PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED;
                //有输入项
                for(Inputs inp : inputsList){
                    if("LOCAL".equals(inp.getOriginType())){   //本地文档，不做特殊处理，默认审批通过
                        continue;
                    }else if("PROJECTLIBDOC".equals(inp.getOriginType())){   //项目库挂接
                        RepFileDto repFileDto = repService.findBusinessObjectByBizId(appKey,inp.getDocId());   //最新版本的项目库文档状态为已归档则视为审批通过，否则不满足条件
                        if(!CommonUtil.isEmpty(repFileDto)){
                            if("guidang".equals(repFileDto.getBizCurrent())){
                                continue;
                            }else{
                                bizCurrent = PlanConstants.PLAN_FLOWSTATUS_LAUNCHED;
                                break;
                            }
                        }
                    }else if("PLAN".equals(inp.getOriginType())){   //计划关联,关联的计划对应的输出挂接的项目库文档状态为已归档则视为审批通过，否则不满足条件
                        List<ProjDocRelation> list = projLibService.getDocRelationList(inp.getOriginDeliverablesInfoId());
                        if(!CommonUtil.isEmpty(list)){
                            for(ProjDocRelation doc : list){
                                RepFileDto repFileDto = repService.getRepFileByRepFileId(appKey,doc.getDocId());
                                if("guidang".equals(repFileDto.getBizCurrent())){
                                    continue;
                                }else{
                                    bizCurrent = PlanConstants.PLAN_FLOWSTATUS_LAUNCHED;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }else{ //有前置计划
            bizCurrent = PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED;

            for(PreposePlan prepose : preposePlanList){
                Plan pl = getEntity(prepose.getPreposePlanId());
                if("FINISH".equals(pl.getBizCurrent())){
                    continue;
                }else{
                    bizCurrent = PlanConstants.PLAN_FLOWSTATUS_LAUNCHED;
                    break;
                }
            }

            if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(bizCurrent)){   //状态为待接收即前置计划状态全部为已完工时再去校验输入项
                //无输入项
                if(CommonUtil.isEmpty(inputsList)){
                    bizCurrent = PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED;  //有前置计划无输入项且前置计划为已完工状态更新为待接收
                }else{
                    //有输入项
                    for(Inputs inp : inputsList){
                        if("LOCAL".equals(inp.getOriginType())){   //本地文档，不做特殊处理，默认审批通过
                            continue;
                        }else if("PROJECTLIBDOC".equals(inp.getOriginType())){   //项目库挂接
                            RepFileDto repFileDto = repService.findBusinessObjectByBizId(appKey,inp.getDocId());   //最新版本的项目库文档状态为已归档则视为审批通过，否则不满足条件
                            if(!CommonUtil.isEmpty(repFileDto)){
                                if("guidang".equals(repFileDto.getBizCurrent())){
                                    continue;
                                }else{
                                    bizCurrent = PlanConstants.PLAN_FLOWSTATUS_LAUNCHED;
                                    break;
                                }
                            }
                        }else if("PLAN".equals(inp.getOriginType())){   //计划关联,关联的计划对应的输出挂接的项目库文档状态为已归档则视为审批通过，否则不满足条件
                            List<ProjDocRelation> list = projLibService.getDocRelationList(inp.getOriginDeliverablesInfoId());
                            if(!CommonUtil.isEmpty(list)){
                                for(ProjDocRelation doc : list){
                                    RepFileDto repFileDto = repService.getRepFileByRepFileId(appKey,doc.getDocId());
                                    if("guidang".equals(repFileDto.getBizCurrent())){
                                        continue;
                                    }else{
                                        bizCurrent = PlanConstants.PLAN_FLOWSTATUS_LAUNCHED;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return bizCurrent;
    }

    @Override
    public FeignJson assignListViewTree(Plan plan, String formId, String userId) {
        FeignJson j = new FeignJson();
        ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
        approvePlanInfo.setFormId(formId);

        List<ApprovePlanInfo> approve = queryAssignList(approvePlanInfo, 1, 10, false);
        List<Plan> planList = new ArrayList<Plan>();
        for (int i = 0; i < approve.size(); i++ ) {
            Plan p = getEntity(approve.get(i).getPlanId());
            Plan plan2 = (Plan)p.clone();
            if (p != null) {
                plan2.setId(p.getId());
                planList.add(plan2);
            }
        }
        boolean isModifyBoo = false;
        String projectId = "";
        if (!CommonUtil.isEmpty(planList)) {
            isModifyBoo = projectService.isModifyForPlan(planList.get(0).getProjectId());
            projectId = planList.get(0).getProjectId();
        }
        String isModify = "false";
        if (isModifyBoo) {
            isModify = "true";
        }

        for (Plan node : planList) {
            if (node.getParentPlanId() != null
                    && !PlanConstants.PLAN_EDITING.equals(node.getParentPlan().getBizCurrent())) {
                node.setParentPlanId(null);
            }
            node.set_parentId(node.getParentPlanId());
            node.setOrder(String.valueOf(node.getStoreyNo()));
        }
        boolean isPmo = groupService.judgeHasLimit(appKey,ProjectRoleConstants.PMO, userId, null);
        String teamId = projRoleService.getTeamIdByProjectId(projectId);
        boolean isProjectManger = groupService.judgeHasLimit(appKey,ProjectRoleConstants.PROJ_MANAGER,
                userId, teamId);
        PlanAuthorityVo parameVo = new PlanAuthorityVo();
        parameVo.setIsModify(isModify);
        parameVo.setPlanModifyOperationCode("false");
        parameVo.setPlanAssignOperationCode("false");
        parameVo.setPlanDeleteOperationCode("false");
        parameVo.setPlanChangeOperationCode("false");
        parameVo.setPlanDiscardOperationCode("false");
        parameVo.setPmo(isPmo);
        parameVo.setProjectManger(isProjectManger);

        Project project = projectService.getProjectEntity(projectId);

        Plan pl = new Plan();
        initBusinessObject(pl);
        List<LifeCycleStatus> statusList = pl.getPolicy().getLifeCycleStatusList();

        List<JSONObject> rootList = changePlansToJSONObjects(planList, parameVo, project,
                statusList);

        String resultJSON = JSON.toJSONString(rootList);
        j.setObj(resultJSON);
        return j;
    }

    @Override
    public void updateOpContentByPlanId(TSUserDto usetDto,String planId,String opContent) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class, planId);
        plan.setOpContent(opContent);
        plan.setUpdateBy(usetDto.getId());
        plan.setUpdateFullName(usetDto.getRealName());
        plan.setUpdateName(usetDto.getUserName());
        plan.setUpdateTime(new Date());
        sessionFacade.saveOrUpdate(plan);
    }

    @Override
    public void deleteResourceByPlanId(String planId) {
        // TODO Auto-generated method stub

        List<ResourceLinkInfo> tempPlanResourceLinkInfoList = sessionFacade.findByQueryString(creatDeleteResourceInfoHql(planId));
        for (ResourceLinkInfo tempPlanResourceLinkInfo : tempPlanResourceLinkInfoList) {
            sessionFacade.delete(tempPlanResourceLinkInfo);
        }
    }

    @Override
    public void deleteDeliverablesByPlanId(String planId) {

        List<DeliverablesInfo> tempPlanDeliverablesList = sessionFacade.findByQueryString(creatDeleteDeliverablesInfoHql(planId));
        for (DeliverablesInfo deliverables : tempPlanDeliverablesList) {
            sessionFacade.delete(deliverables);
        }
        List<Inputs> tempPlanInputsList2 = sessionFacade.findByQueryString(creatInputInfoHql(planId));
        for (Inputs inputs : tempPlanInputsList2) {
            sessionFacade.delete(inputs);
        }
    }

    @Override
    public FlowTaskVo getFlowTaskVo(String planId) {
        FlowTaskVo flowTaskVo = new FlowTaskVo();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select t.planId, p.parentid,p.id from PM_FLOWTASK t,PM_FLOWTASK_PARENT p where t.parentplanid=p.id(+) and t.id ='"
                + planId + "'");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    flowTaskVo.setId(id);
                    flowTaskVo.setParentPlanId((String)map.get("parentid"));
                    flowTaskVo.setPlanId((String)map.get("planId"));
                }
            }
        }
        return flowTaskVo;
    }

    @Override
    public List<FlowTaskVo> getFlowTaskVoList(String formId) {
        List<FlowTaskVo> flowTaskVoList = new ArrayList<FlowTaskVo>();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select t.id, t.parentPlanId from PM_FLOWTASK t where t.id in (select l.planId from PM_APPROVE_PLAN_INFO l where l.formId = '"+ formId +"')");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                FlowTaskVo flowTaskVo = new FlowTaskVo();
                String curId = (String)map.get("id");
                if (StringUtils.isNotEmpty(curId)) {
                    flowTaskVo.setId(curId);
                    flowTaskVo.setParentPlanId((String)map.get("parentPlanId"));
                }
                flowTaskVoList.add(flowTaskVo);
            }
        }
        return flowTaskVoList;
    }

    @Override
    public FlowTaskParentVo getFlowTaskParentVoInfo(String id) {
        FlowTaskParentVo flowTaskParentVo = new FlowTaskParentVo();
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select t.id, t.parentid, t.flowresolvexml from PM_FLOWTASK_PARENT t where t.id ='"
                + id + "'");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String curId = (String)map.get("id");
                if (StringUtils.isNotEmpty(curId)) {
                    flowTaskParentVo.setId(curId);
                    flowTaskParentVo.setParentId((String)map.get("parentid"));
                    flowTaskParentVo.setFlowResolveXml((String)map.get("flowresolvexml"));
                }
            }
        }
        return flowTaskParentVo;
    }

    @Override
    public String getPlanIdByFlowTaskParentId(String flowTaskParentId) {
        String planId = "";
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select p.parentid,p.id from pm_flowtask_parent p where p.id ='"+flowTaskParentId+"'");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String parentid = (String)map.get("parentid");
                if (StringUtils.isNotEmpty(parentid)) {
                    planId = parentid;
                }
            }
        }
        return planId;
    }

    @Override
    public String getPlanIdByUseObjectId(String flowTaskId) {
        String planOldId = "";
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select p.planId from pm_flowtask p where p.id ='"+flowTaskId+"'");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String planId = (String)map.get("planId");
                if (StringUtils.isNotEmpty(planId)) {
                    planOldId = planId;
                }
            }
        }
        return planOldId;
    }

    @Override
    public void discardPlansForFlow(String id, TSUserDto user) {
        Plan plan = (Plan) sessionFacade.getEntity(Plan.class, id);
        try {
            planInvalidMsgNotice.getAllMessage(id, user);
            if("1".equals(plan.getAvaliable())) {
                cancelPlanForFlow(plan, user);
                plan.setBizCurrent(PlanConstants.PLAN_INVALID);
                plan.setInvalidTime(new Date());
                plan.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                saveOrUpdate(plan);
                resourceEverydayuseService.delResourceUseByPlanAndOperationTime(id, plan.getInvalidTime());
                List<Plan> plans = getPlanAllChildren(plan);
                if (plans != null && plans.size() > 1) {
                    for (Plan p : plans) {
                        if (!id.equals(p.getId())) {
                            if (PlanConstants.PLAN_EDITING.equals(p.getBizCurrent())) {
                                p.setAvaliable("0");
                                cancelPlanForFlow(p, user);
                                p.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                                p.setInvalidTime(new Date());
                                saveOrUpdate(p);
                                resourceEverydayuseService.delResourceUseByPlan(p.getId());
                            }
                            else {
                                planInvalidMsgNotice.getAllMessage(p.getId(), user);
                                cancelPlanForFlow(p, user);
                                p.setBizCurrent(PlanConstants.PLAN_INVALID);
                                p.setInvalidTime(new Date());
                                p.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                                saveOrUpdate(p);
                                resourceEverydayuseService.delResourceUseByPlanAndOperationTime(p.getId(),
                                        p.getInvalidTime());
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }

    }

    @Override
    public void saveFlowTaskAsCreate(Plan plan, TSUserDto user) {
        if (StringUtils.isNotEmpty(plan.getLauncher())) {
            user = userService.getUserByUserId(plan.getLauncher());
//            user = userService.getEntity(TSUser.class, plan.getLauncher());
        }

        // 计划日志记录
        PlanLog createLog = new PlanLog();
        createLog.setPlanId(plan.getId());
        createLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);
        createLog.setCreateBy(user.getId());
        createLog.setCreateName(user.getUserName());
        createLog.setCreateFullName(user.getRealName());
        createLog.setCreateTime(new Date());
        sessionFacade.saveOrUpdate(createLog);

        // 计划日志记录
        PlanLog assignLog = new PlanLog();
        assignLog.setPlanId(plan.getId());
        assignLog.setLogInfo(PlanConstants.PLAN_LOGINFO_ORDERED);
        assignLog.setCreateBy(user.getId());
        assignLog.setCreateName(user.getUserName());
        assignLog.setCreateFullName(user.getRealName());
        assignLog.setCreateTime(new Date());
        sessionFacade.saveOrUpdate(assignLog);

        // 如果存在父计划，则判断父计划的“子计划下达”日志是否记录，若未记录则需记录
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            if (!planLogService.checkSonAssignInfo(plan.getParentPlanId())) {
                PlanLog sonAssignLog = new PlanLog();
                sonAssignLog.setCreateBy(user.getId());
                sonAssignLog.setCreateName(user.getUserName());
                sonAssignLog.setCreateFullName(user.getRealName());
                sonAssignLog.setCreateTime(new Date());
                sonAssignLog.setPlanId(plan.getParentPlanId());
                sonAssignLog.setLogInfo(PlanConstants.PLAN_LOGINFO_SON_ORDERED);
                sessionFacade.saveOrUpdate(sonAssignLog);
            }
        }
    }

    @Override
    public boolean isReviewTask(String name) {

        String hql = " from NameStandard t where t.name=\'" + name + "\'";
        List<NameStandard> list = sessionFacade.executeQuery(hql, null);
        if (list.size() > 0 && list.get(0) != null
                && CommonConstants.NAMESTANDARD_TYPE_REVIEW.equals(list.get(0).getActiveCategory())) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void saveFlowTaskCellConnect(List<FlowTaskCellConnectVo> flowTaskCellConnectVoList) {
        try {
            for (FlowTaskCellConnectVo curFlowTaskCellConnectVo : flowTaskCellConnectVoList) {
                Connection conn = null;
                // 插入任务的formId到计划中
                conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement psForApprovePlanForm = null;
                String sqlForApprovePlanForm = " insert into PM_FLOW_TASK_CELL_CONNECT (id,cellid,infoid,parentplanid,targetid,targetinfoid) values (?, ?, ?,?,?,?)";
                psForApprovePlanForm = conn.prepareStatement(sqlForApprovePlanForm);
                psForApprovePlanForm.setObject(1, curFlowTaskCellConnectVo.getId());
                psForApprovePlanForm.setObject(2, curFlowTaskCellConnectVo.getCellId());
                psForApprovePlanForm.setObject(3, curFlowTaskCellConnectVo.getInfoId());
                psForApprovePlanForm.setObject(4, curFlowTaskCellConnectVo.getParentPlanId());
                psForApprovePlanForm.setObject(5, curFlowTaskCellConnectVo.getTargetId());
                psForApprovePlanForm.setObject(6, curFlowTaskCellConnectVo.getTargetInfoId());
                psForApprovePlanForm.execute();
                DBUtil.closeConnection(null, psForApprovePlanForm, conn);
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<com.alibaba.fastjson.JSONObject> changePlansToJSONObjects(List<Plan> planList) {

        List<com.alibaba.fastjson.JSONObject> rootList = new ArrayList<com.alibaba.fastjson.JSONObject>();

        Map<String, String> parentPlanIds = new HashMap<String, String>();
        Map<String, String> planIdMaps = new HashMap<String, String>();

        if (!CommonUtil.isEmpty(planList)) {
            for (Plan p : planList) {
                planIdMaps.put(p.getId(), p.getId());
                if (!StringUtils.isEmpty(p.getParentPlanId())) {
                    parentPlanIds.put(p.getParentPlanId(), p.getParentPlanId());
                }
            }
        }

        List<ActivityTypeManage> types = activityTypeManageEntityService.getAllActivityTypeManage(false);
//        List<TSTypeDto> types = TSTypegroup.allTypes.get(dictCode.toLowerCase());

        Plan pl = new Plan();
        initBusinessObject(pl);
        List<LifeCycleStatus> statusList = pl.getPolicy().getLifeCycleStatusList();

        //获取计划等级的id和名称集合：
        // 计划等级
        BusinessConfig planLevel = new BusinessConfig();
        planLevel.setConfigType(ConfigTypeConstants.PLANLEVEL);
        List<BusinessConfig> planLevelList = businessConfigService.searchUseableBusinessConfigs(planLevel);
        Map<String, String> planLevelIdAndNameMap = new HashMap<String, String>();
        for(BusinessConfig cur : planLevelList) {
            planLevelIdAndNameMap.put(cur.getId(), cur.getName());
        }

        for (Plan p : planList) {
            // 若其无父计划或者其父计划不在结果集中，则将其设为root节点
            if (StringUtils.isEmpty(p.getParentPlanId())
                    || StringUtils.isEmpty(planIdMaps.get(p.getParentPlanId()))) {
                com.alibaba.fastjson.JSONObject root = new com.alibaba.fastjson.JSONObject();
                root.put("id", p.getId());
                root.put("parentPlanId", p.getParentPlanId());
                root.put("_parentId", p.get_parentId());
                root.put("order", p.getOrder());
                root.put("planNumber", p.getPlanNumber());
                root.put("displayName", p.getPlanName());
                root.put("milestone", "true".equals(p.getMilestone()) ? "是" : "否");
                if (StringUtils.isNotEmpty(parentPlanIds.get(p.getId()))) {
                    com.alibaba.fastjson.JSONObject treeNode = new com.alibaba.fastjson.JSONObject();
                    treeNode.put("value", this.generatePlanNameUrl(p));
                    treeNode.put("image", "folder.gif");
                    root.put("planName", treeNode);
                }
                else {
                    root.put("planName", this.generatePlanNameUrl(p));
                }
                root.put("planLevelInfo",
                        planLevelIdAndNameMap.get(p.getPlanLevel()) == null ? "" : planLevelIdAndNameMap.get(p.getPlanLevel()));
                String ownerInfo = "";
                if (StringUtils.isNotEmpty(p.getOwner())) {
                    TSUserDto ownerDto = userService.getUserByUserId(p.getOwner());
                    ownerInfo = ownerDto.getRealName() + "-" +ownerDto.getUserName();
                }
                root.put("ownerInfo",ownerInfo);
                root.put("bizCurrentInfo", this.generatePlanBizCurrent(p, statusList));
                root.put("planStartTime",
                        DateUtil.dateToString(p.getPlanStartTime(), DateUtil.LONG_DATE_FORMAT));
                root.put("planEndTime",
                        DateUtil.dateToString(p.getPlanEndTime(), DateUtil.LONG_DATE_FORMAT));

                root.put("workTime", p.getWorkTime());

                root.put("bizCurrent", p.getBizCurrent());

                root.put("createTime",
                        DateUtil.dateToString(p.getCreateTime(), DateUtil.LONG_DATE_FORMAT));

                root.put("flowStatus", p.getFlowStatus());
                root.put("result", p.getResult());

                // 增加计划时间完成时间、废弃时间、计划类别和计划类型的展示
                root.put("actualEndTime",
                        DateUtil.dateToString(p.getActualEndTime(), DateUtil.LONG_DATE_FORMAT));
                root.put("invalidTime",
                        DateUtil.dateToString(p.getInvalidTime(), DateUtil.LONG_DATE_FORMAT));

                root.put("taskNameType", p.getTaskNameType());
                root.put("taskNameTypeDisplay", this.generateTaskNameTypeUrl(p, types));
                root.put("taskType", p.getTaskType());

                List<com.alibaba.fastjson.JSONObject> rows = new ArrayList<com.alibaba.fastjson.JSONObject>();
                root.put("rows", rows);
                rootList.add(root);
            }
        }

        for (int i = 0; i < rootList.size(); i++ ) {
            this.findSubNodeByPid(parentPlanIds, planList, rootList.get(i), types, statusList);
        }
        return rootList;
    }



    @Override
    public List<Plan> queryPlanListExceptEditing(Plan plan, int page, int rows, boolean isPage) {
        String hql = createHqlExceptEditing(plan);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }


    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param plan
     * @return
     * @see
     */
    private String createHqlExceptEditing(Plan plan) {
        String hql = "from Plan l where l.bizCurrent <> '" + PlanConstants.PLAN_EDITING
                + "' and l.bizCurrent <> '" + PlanConstants.PLAN_INVALID + "'";
        // 计划ID
        if (StringUtils.isNotEmpty(plan.getId())) {
            hql = hql + " and l.id = '" + plan.getId() + "'";
        }
        // 所属项目
        if (StringUtils.isNotEmpty(plan.getProjectId())) {
            hql = hql + " and l.projectId = '" + plan.getProjectId() + "'";
        }
        // 父计划
        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            hql = hql + " and l.parentPlanId = '" + plan.getParentPlanId() + "'";
        }
        // 计划名称
        if (StringUtils.isNotEmpty(plan.getPlanName())) {
            hql = hql + " and l.planName like '%" + plan.getPlanName() + "%'";
        }
        // 责任人
        if (StringUtils.isNotEmpty(plan.getOwner())) {
            hql = hql + " and l.owner = '" + plan.getOwner() + "'";
        }
        // 生命周期状态
        if (StringUtils.isNotEmpty(plan.getBizCurrent())) {
            hql = hql + " and l.bizCurrent = '" + plan.getBizCurrent() + "'";
        }
        // // 是否流程中
        // if (StringUtils.isNotEmpty(plan.getFlowStatus())) {
        // hql = hql + " and l.flowStatus = '" + plan.getFlowStatus() + "'";
        // }
        // 下达人
        if (StringUtils.isNotEmpty(plan.getAssigner())) {
            hql = hql + " and l.assigner = '" + plan.getAssigner() + "'";
        }
        // 下达时间
        if (plan.getAssignTimeStart() != null) {
            hql = hql + " and l.assignTime >= " + plan.getAssignTimeStart();
        }
        if (plan.getAssignTimeEnd() != null) {
            hql = hql + " and l.assignTime <= " + plan.getAssignTimeEnd();
        }
        // 计划等级
        if (StringUtils.isNotEmpty(plan.getPlanLevel())) {
            hql = hql + " and l.planLevel = '" + plan.getPlanLevel() + "'";
        }

        // cellId
        if (StringUtils.isNotEmpty(plan.getCellId())) {
            hql = hql + " and l.cellId = '" + plan.getCellId() + "'";
        }

        // 开始时间
        if (plan.getPlanStartTimeStart() != null) {
            hql = hql + " and l.planStartTime >= " + plan.getPlanStartTimeStart();
        }
        if (plan.getPlanStartTimeEnd() != null) {
            hql = hql + " and l.planStartTime <= " + plan.getPlanStartTimeEnd();
        }
        // 结束时间
        if (plan.getPlanEndTimeStart() != null) {
            hql = hql + " and l.planEndTime >= " + plan.getPlanEndTimeStart();
        }
        if (plan.getPlanEndTimeEnd() != null) {
            hql = hql + " and l.planEndTime <= " + plan.getPlanEndTimeEnd();
        }
        // 是否可用
        if (StringUtils.isNotEmpty(plan.getAvaliable())) {
            hql = hql + " and l.avaliable = '" + plan.getAvaliable() + "'";
        }
        hql = hql + " order by parentPlanId, createTime, storeyno";
        return hql;
    }

    /**
     * 废弃id
     *
     * @param
     * @return
     * @see
     */
    public void cancelPlanForFlow(Plan plan, TSUserDto user) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", user.getUserName());
        // 流程终止
        if (!PlanConstants.PLAN_FLOWSTATUS_NORMAL.equals(plan.getFlowStatus())) {
            TemporaryPlan temporaryPlan = new TemporaryPlan();
            temporaryPlan.setPlanId(plan.getId());
            List<TemporaryPlan> listTemporaryPlan = planChangeService.queryTemporaryPlanList(
                    temporaryPlan, 1, 10, false);

            ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
            /*
             * if (listTemporaryPlan != null && listTemporaryPlan.size() > 0) {
             * approvePlanInfo.setPlanId(listTemporaryPlan.get(0).getId()); }else{
             * approvePlanInfo.setPlanId(plan.getId()); }
             */
            if (!PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING.equals(plan.getFlowStatus())) {
                if (PlanConstants.PLAN_FLOWSTATUS_ORDERED.equals(plan.getBizCurrent())
                        && listTemporaryPlan != null && listTemporaryPlan.size() > 0) {
                    approvePlanInfo.setPlanId(listTemporaryPlan.get(0).getId());
                }
                else {
                    approvePlanInfo.setPlanId(plan.getId());
                }
                List<ApprovePlanInfo> approvePlanInfoList = queryAssignList(approvePlanInfo, 1,
                        10, false);

                /*
                 * String formId = approvePlanInfoList.get(0).getFormId(); ApprovePlanInfo a = new
                 * ApprovePlanInfo(); a.setFormId(formId); List<ApprovePlanInfo> approveInfoList =
                 * queryAssignList(a, 1, 10, false); if (approveInfoList != null &&
                 * approveInfoList.size() == 1) { // 执行流程 ApprovePlanForm approvePlanForm =
                 * getEntity(ApprovePlanForm.class, formId); String taskId; if
                 * (approvePlanForm.getProcInstId() != null &&
                 * !"".equals(approvePlanForm.getProcInstId())) {
                 * List<org.activiti.engine.task.Task> taskList =
                 * workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                 * approvePlanForm.getProcInstId()); if (taskList != null && taskList.size() > 0) {
                 * taskId = taskList.get(0).getId();
                 * workFlowFacade.getWorkFlowMonitorService().terminateProcessInstance(formId,
                 * approvePlanForm.getProcInstId(), ""); MyStartedTask myStartedTask =
                 * workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                 * formId); workFlowFacade.getWorkFlowStartedTaskService().delete(myStartedTask); }
                 * } } else if (approveInfoList != null && approveInfoList.size() > 1) {
                 * delete(approvePlanInfoList.get(0)); }
                 */
            }
            else if (PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING.equals(plan.getFlowStatus())) {
                String taskId;
                if (StringUtils.isNotEmpty(plan.getFeedbackProcInstId())) {
                    /*
                     * List<org.activiti.engine.task.Task> taskList =
                     * workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                     * plan.getFeedbackProcInstId()); if (taskList != null && taskList.size() > 0)
                     * { taskId = taskList.get(0).getId();
                     * workFlowFacade.getWorkFlowMonitorService(
                     * ).terminateProcessInstance(plan.getId (), plan.getFeedbackProcInstId(), "");
                     * MyStartedTask myStartedTask =
                     * workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumber(
                     * plan.getId());
                     * workFlowFacade.getWorkFlowStartedTaskService().delete(myStartedTask); }
                     */
                }
            }

            plan.setBizCurrent(PlanConstants.PLAN_INVALID);
            saveOrUpdate(plan);

        }

        // 删除后置关系
        Plan p = new Plan();
        p.setPreposeIds(plan.getId());
        List<PreposePlan> preposePlanList = preposePlanService.getPostposesByPreposeId(p);
        if (preposePlanList != null && preposePlanList.size() > 0) {
            for (PreposePlan pre : preposePlanList) {
                sessionFacade.delete(pre);
                if (!plan.getId().equals(pre.getPlanId())) {
                    // inputsService.deleteInputsByPlan(pre.getPlanInfo());
                    Plan pp = pre.getPlanInfo();
                    pp.setPreposeIds(plan.getId());
                    inputsService.deleteInputsByPlan(pp);
                }
            }
        }
        // 删除前置关系
        /*
         * List<PreposePlan> preposePlanList2 = preposePlanService.getPreposePlansByPlanId(plan);
         * if(preposePlanList2 != null&& preposePlanList2.size()>0){ for(PreposePlan pre
         * :preposePlanList2){ delete(pre); } }
         */

        // 删除输入关系
        // inputsService.deleteInputsByPlan(plan);
        // inputsService.deleteInputsByOriginObject(plan.getId(), "PLAN");

        // 释放资源
        ResourceEverydayuseInfoDto resourceLinkInfo = new ResourceEverydayuseInfoDto();
        resourceLinkInfo.setUseObjectId(plan.getId());
        List<ResourceEverydayuseInfoDto> resourceList = resourceLinkInfoService.queryResourceEverydayuseInfoList(
                resourceLinkInfo, 1, 10, false);
        Date currentDate = new Date();
        if (resourceList != null && resourceList.size() > 0) {
            for (ResourceEverydayuseInfoDto r : resourceList) {
                if (r.getUseDate() != null) {
                    if (currentDate.getTime() > r.getUseDate().getTime()) {
                        sessionFacade.delete(r);
                    }
                }

            }
        }
    }

    private String creatDeleteDeliverablesInfoHql(String planId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from DeliverablesInfo t");
        hql.append(" where 1 = 1");

        if (planId != null) {
            if (StringUtils.isNotEmpty(planId)) {
                hql.append(" and t.useObjectId = '" + planId + "'");
            }
        }
        return hql.toString();
    }

    private String creatInputInfoHql(String planId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from Inputs t");
        hql.append(" where 1 = 1");

        if (planId != null) {
            if (StringUtils.isNotEmpty(planId)) {
                hql.append(" and t.useObjectId = '" + planId + "'");
            }
        }
        return hql.toString();
    }

    private String creatDeleteResourceInfoHql(String planId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ResourceLinkInfo t");
        hql.append(" where 1 = 1");

        if (planId != null) {
            if (StringUtils.isNotEmpty(planId)) {
                hql.append(" and t.useObjectId = '" + planId + "'");
            }
        }
        return hql.toString();
    }
    /**
     * 将计划list组装为树节点json
     *
     * @param planList
     * @return
     * @see
     */

    private List<JSONObject> changePlansToJSONObjects(List<Plan> planList,
                                                      PlanAuthorityVo parameVo, Project project,
                                                      List<LifeCycleStatus> statusList) {
        Map<String,TSUserDto> userDtoMap = userService.getCommonPrepUserAllByUUid();
        List<JSONObject> rootList = new ArrayList<JSONObject>();
        Map<String, String> parentPlanIds = new HashMap<String, String>();
        Map<String, String> planIdMaps = new HashMap<String, String>();
        if (!CommonUtil.isEmpty(planList)) {
            for (Plan p : planList) {
                planIdMaps.put(p.getId(), p.getId());
                String parentPlanId = p.getParentPlanId();
                if (!StringUtils.isEmpty(parentPlanId)
                        && StringUtils.isEmpty(parentPlanIds.get(parentPlanId))) {
                    parentPlanIds.put(parentPlanId, parentPlanId);
                }
            }
        }
        List<ActivityTypeManage> types = activityTypeManageEntityService.getAllActivityTypeManage(false);
        String warningDay = paramSwitchService.getSwitch(SwitchConstants.PLANWARNINGDAYS);
        for (Plan p : planList) {
            // 若其无父计划或者其父计划不在结果集中，则将其设为root节点
            if (StringUtils.isEmpty(p.getParentPlanId())
                    || StringUtils.isEmpty(planIdMaps.get(p.getParentPlanId()))) {
                JSONObject root = new JSONObject();
                root.put("id", p.getId());
                root.put("parentPlanId", p.getParentPlanId());
                root.put("planNumber", p.getPlanNumber());
                root.put("optBtn", this.generateOptBtn(p, parameVo, project));
                root.put("progressRate", this.generateProgressRate(p, warningDay));
                root.put("displayName", p.getPlanName());
                if (StringUtils.isNotEmpty(parentPlanIds.get(p.getId()))) {
                    JSONObject treeNode = new JSONObject();
                    treeNode.put("value", p.getPlanName());
                    treeNode.put("image", "folder.gif");
                    root.put("displayNameNode", treeNode);

                    JSONObject treeNode1 = new JSONObject();
                    treeNode1.put("value", this.generatePlanNameUrl(p));
                    treeNode1.put("image", "folder.gif");
                    root.put("planName", treeNode1);
                }
                else {
                    root.put("displayNameNode", p.getPlanName());
                    root.put("planName", this.generatePlanNameUrl(p));
                }

                root.put("planLevelInfo",
                        p.getPlanLevelInfo() == null ? "" : p.getPlanLevelInfo().getName());
                if(!CommonUtil.isEmpty(p.getOwner())){
                    TSUserDto userDto = userDtoMap.get(p.getOwner());
                    root.put("ownerInfo",userDto.getRealName()+"-"+userDto.getUserName());
                }

                root.put("bizCurrentInfo", this.generatePlanBizCurrent(p, statusList));
                root.put("planStartTime",
                        DateUtil.dateToString(p.getPlanStartTime(), DateUtil.LONG_DATE_FORMAT));
                root.put("planEndTime",
                        DateUtil.dateToString(p.getPlanEndTime(), DateUtil.LONG_DATE_FORMAT));

                root.put("assignerInfo",
                        p.getAssignerInfo() == null ? "" : p.getAssignerInfo().getRealName() + "-"
                                + p.getAssignerInfo().getUserName());

                root.put("assignTime",
                        DateUtil.dateToString(p.getAssignTime(), DateUtil.LONG_DATE_FORMAT));
                root.put("workTime", p.getWorkTime());

                root.put("preposePlans", p.getPreposePlans());

                root.put("milestone", "true".equals(p.getMilestone()) ? "是" : "否");

                root.put("createBy", p.getCreateBy());
                root.put("owner", p.getOwner());
                root.put("parent_Id", p.getParentPlan() == null ? "" : p.getParentPlan().getId());
                root.put("isCreateByPmo", p.getIsCreateByPmo());
                root.put("flowStatus", p.getFlowStatus());
                root.put("isAssignSingleBack", p.getIsAssignSingleBack());
                root.put("bizCurrent", p.getBizCurrent());
                root.put("parent_owner",
                        p.getParentPlan() == null ? "" : p.getParentPlan().getOwner());
                root.put("parent_createBy",
                        p.getParentPlan() == null ? "" : p.getParentPlan().getCreateBy());
                root.put("parent_flowStatus",
                        p.getParentPlan() == null ? "" : p.getParentPlan().getFlowStatus());
                root.put("parent_isAssignSingleBack",
                        p.getParentPlan() == null ? "" : p.getParentPlan().getIsAssignSingleBack());
                root.put("parent_bizCurrent",
                        p.getParentPlan() == null ? "" : p.getParentPlan().getBizCurrent());
                root.put("project_bizCurrent",
                        p.getProject() == null ? "" : p.getProject().getBizCurrent());
                root.put("opContent", p.getOpContent());

                root.put("creator", p.getCreator() == null ? "" : p.getCreator().getRealName()
                        + "-"
                        + p.getCreator().getUserName());
                root.put("createTime",
                        DateUtil.dateToString(p.getCreateTime(), DateUtil.LONG_DATE_FORMAT));

                root.put("flowStatus", p.getFlowStatus());
                root.put("result", p.getResult());

                // 增加计划时间完成时间、废弃时间、计划类别和计划类型的展示
                root.put("actualEndTime",
                        DateUtil.dateToString(p.getActualEndTime(), DateUtil.LONG_DATE_FORMAT));
                root.put("invalidTime",
                        DateUtil.dateToString(p.getInvalidTime(), DateUtil.LONG_DATE_FORMAT));
                root.put("taskNameType", p.getTaskNameType());
                root.put("taskNameTypeDisplay", this.generateTaskNameTypeUrl(p, types));
                root.put("taskType", p.getTaskType());
                root.put("planType", p.getPlanType());

                List<JSONObject> rows = new ArrayList<JSONObject>();
                root.put("rows", rows);
                rootList.add(root);
            }
        }
        for (int i = 0; i < rootList.size(); i++ ) {
            this.findSubNodeByPid(parentPlanIds, planList, rootList.get(i), parameVo, types,
                    project, statusList, warningDay,userDtoMap);
        }
        return rootList;
    }

    /**
     * Description:递归查询获取所有子节点
     *
     * @param
     * @param parentObject
     * @see
     */
    @SuppressWarnings("unchecked")
    public void findSubNodeByPid(Map<String, String> parentPlanIds, List<Plan> planList,
                                 JSONObject parentObject, PlanAuthorityVo parameVo,
                                 List<ActivityTypeManage> types, Project currentProject,
                                 List<LifeCycleStatus> statusList, String warningDay,Map<String,TSUserDto> userDtoMap) {
        String pid = parentObject.getString("id");
        List<JSONObject> subNodeList = new ArrayList<JSONObject>();
        for (Plan plan : planList) {
            if (pid.equals(plan.getParentPlanId())) {
                JSONObject newNode = new JSONObject();
                newNode.put("id", plan.getId());
                newNode.put("parentPlanId", plan.getParentPlanId());
                newNode.put("planNumber", plan.getPlanNumber());
                newNode.put("optBtn", this.generateOptBtn(plan, parameVo, currentProject));
                newNode.put("progressRate", this.generateProgressRate(plan, warningDay));
                newNode.put("displayName", plan.getPlanName());
                if (StringUtils.isNotEmpty(parentPlanIds.get(plan.getId()))) {
                    JSONObject treeNode = new JSONObject();
                    treeNode.put("value", plan.getPlanName());
                    treeNode.put("image", "folder.gif");
                    newNode.put("displayNameNode", treeNode);

                    JSONObject treeNode1 = new JSONObject();
                    treeNode1.put("value", this.generatePlanNameUrl(plan));
                    treeNode1.put("image", "folder.gif");
                    newNode.put("planName", treeNode1);
                }
                else {
                    newNode.put("displayNameNode", plan.getPlanName());
                    newNode.put("planName", this.generatePlanNameUrl(plan));
                }

                newNode.put("planLevelInfo",
                        plan.getPlanLevelInfo() == null ? "" : plan.getPlanLevelInfo().getName());
                if(!CommonUtil.isEmpty(plan.getOwner())){
                    TSUserDto userDto = userDtoMap.get(plan.getOwner());
                    newNode.put("ownerInfo",userDto.getRealName()+"-"+userDto.getUserName());
                }

                newNode.put("bizCurrentInfo", this.generatePlanBizCurrent(plan, statusList));
                newNode.put("planStartTime",
                        DateUtil.dateToString(plan.getPlanStartTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("planEndTime",
                        DateUtil.dateToString(plan.getPlanEndTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("assignerInfo",
                        plan.getAssignerInfo() == null ? "" : plan.getAssignerInfo().getRealName()
                                + "-"
                                + plan.getAssignerInfo().getUserName());
                newNode.put("assignTime",
                        DateUtil.dateToString(plan.getAssignTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("workTime", plan.getWorkTime());

                newNode.put("preposePlans", plan.getPreposePlans());

                newNode.put("milestone", "true".equals(plan.getMilestone()) ? "是" : "否");

                newNode.put("createBy", plan.getCreateBy());
                newNode.put("owner", plan.getOwner());
                newNode.put("parent_Id",
                        plan.getParentPlan() == null ? "" : plan.getParentPlan().getId());
                newNode.put("isCreateByPmo", plan.getIsCreateByPmo());
                newNode.put("flowStatus", plan.getFlowStatus());
                newNode.put("bizCurrent", plan.getBizCurrent());
                newNode.put("parent_owner",
                        plan.getParentPlan() == null ? "" : plan.getParentPlan().getOwner());
                newNode.put("parent_createBy",
                        plan.getParentPlan() == null ? "" : plan.getParentPlan().getCreateBy());
                newNode.put("parent_flowStatus",
                        plan.getParentPlan() == null ? "" : plan.getParentPlan().getFlowStatus());
                newNode.put("parent_bizCurrent",
                        plan.getParentPlan() == null ? "" : plan.getParentPlan().getBizCurrent());
                newNode.put("project_bizCurrent",
                        plan.getProject() == null ? "" : plan.getProject().getBizCurrent());
                newNode.put("opContent", plan.getOpContent());

                newNode.put("creator",
                        plan.getCreator() == null ? "" : plan.getCreator().getRealName() + "-"
                                + plan.getCreator().getUserName());
                newNode.put("createTime",
                        DateUtil.dateToString(plan.getCreateTime(), DateUtil.LONG_DATE_FORMAT));

                newNode.put("flowStatus", plan.getFlowStatus());
                newNode.put("result", plan.getResult());

                // 增加计划时间完成时间、废弃时间、计划类别和计划类型的展示
                newNode.put("actualEndTime",
                        DateUtil.dateToString(plan.getActualEndTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("invalidTime",
                        DateUtil.dateToString(plan.getInvalidTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("taskNameType", plan.getTaskNameType());
                newNode.put("taskNameTypeDisplay", this.generateTaskNameTypeUrl(plan, types));
                newNode.put("taskType", plan.getTaskType());
                newNode.put("planType", plan.getPlanType());

                List<JSONObject> rows = new ArrayList<JSONObject>();
                newNode.put("rows", rows);
                subNodeList.add(newNode);
            }
        }
        if (subNodeList.size() > 0) {
            for (int i = 0; i < subNodeList.size(); i++ ) {
                List<JSONObject> rows = (List<JSONObject>)parentObject.get("rows");
                this.findSubNodeByPid(parentPlanIds, planList, subNodeList.get(i), parameVo,
                        types, currentProject, statusList, warningDay,userDtoMap);
                JSONObject currentNode = subNodeList.get(i);
                rows.add(currentNode);
                parentObject.put("rows", rows);
            }
        }
        else {
            return;
        }
    }

    /**
     * Description:递归查询获取所有子节点
     *
     * @param
     * @param parentObject
     * @see
     */
    private void findSubNodeByPid(Map<String, String> parentPlanIds, List<Plan> planList,
                                  com.alibaba.fastjson.JSONObject parentObject, List<ActivityTypeManage> types,
                                  List<LifeCycleStatus> statusList) {
        String pid = parentObject.getString("id");
        List<com.alibaba.fastjson.JSONObject> subNodeList = new ArrayList<com.alibaba.fastjson.JSONObject>();
        for (Plan plan : planList) {
            if (pid.equals(plan.getParentPlanId())) {
                com.alibaba.fastjson.JSONObject newNode = new com.alibaba.fastjson.JSONObject();
                newNode.put("id", plan.getId());
                newNode.put("parentPlanId", plan.getParentPlanId());
                newNode.put("_parentId", plan.get_parentId());
                newNode.put("order", plan.getOrder());
                newNode.put("planNumber", plan.getPlanNumber());
                newNode.put("displayName", plan.getPlanName());
                newNode.put("milestone", "true".equals(plan.getMilestone()) ? "是" : "否");
                if (StringUtils.isNotEmpty(parentPlanIds.get(plan.getId()))) {
                    com.alibaba.fastjson.JSONObject treeNode = new com.alibaba.fastjson.JSONObject();
                    treeNode.put("value", this.generatePlanNameUrl(plan));
                    treeNode.put("image", "folder.gif");
                    newNode.put("planName", treeNode);
                }
                else {
                    newNode.put("planName", this.generatePlanNameUrl(plan));
                }
                newNode.put("planLevelInfo",
                        plan.getPlanLevelInfo() == null ? "" : plan.getPlanLevelInfo().getName());
                String ownerInfo = "";
                if (StringUtils.isNotEmpty(plan.getOwner())) {
                    TSUserDto ownerDto = userService.getUserByUserId(plan.getOwner());
                    ownerInfo = ownerDto.getRealName() + "-" +ownerDto.getUserName();
                }
                newNode.put("ownerInfo",ownerInfo);
                /*newNode.put("ownerInfo",
                        plan.getOwnerInfo() == null ? "" : plan.getOwnerInfo().getRealName() + "-"
                                + plan.getOwnerInfo().getUserName());*/
                newNode.put("bizCurrentInfo", this.generatePlanBizCurrent(plan, statusList));
                newNode.put("planStartTime",
                        DateUtil.dateToString(plan.getPlanStartTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("planEndTime",
                        DateUtil.dateToString(plan.getPlanEndTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("workTime", plan.getWorkTime());

                newNode.put("bizCurrent", plan.getBizCurrent());

                newNode.put("createTime",
                        DateUtil.dateToString(plan.getCreateTime(), DateUtil.LONG_DATE_FORMAT));

                newNode.put("flowStatus", plan.getFlowStatus());
                newNode.put("result", plan.getResult());

                // 增加计划时间完成时间、废弃时间、计划类别和计划类型的展示
                newNode.put("actualEndTime",
                        DateUtil.dateToString(plan.getActualEndTime(), DateUtil.LONG_DATE_FORMAT));
                newNode.put("invalidTime",
                        DateUtil.dateToString(plan.getInvalidTime(), DateUtil.LONG_DATE_FORMAT));

                newNode.put("taskNameType", plan.getTaskNameType());
                newNode.put("taskNameTypeDisplay", this.generateTaskNameTypeUrl(plan, types));
                newNode.put("taskType", plan.getTaskType());

                List<com.alibaba.fastjson.JSONObject> rows = new ArrayList<com.alibaba.fastjson.JSONObject>();
                newNode.put("rows", rows);
                subNodeList.add(newNode);
            }
        }
        if (subNodeList.size() > 0) {
            for (int i = 0; i < subNodeList.size(); i++ ) {
                List<com.alibaba.fastjson.JSONObject> rows = (List)parentObject.get("rows");
                this.findSubNodeByPid(parentPlanIds, planList, subNodeList.get(i), types,
                        statusList);
                com.alibaba.fastjson.JSONObject currentNode = subNodeList.get(i);
                rows.add(currentNode);
                parentObject.put("rows", rows);
            }
        }
        else {
            return;
        }
    }

    /**
     * 转化计划类型
     *
     * @param plan
     * @return
     * @see
     */
    private String generateTaskNameTypeUrl(Plan plan, List<ActivityTypeManage> types ) {
        String taskNameTypeDisplay = "";
        for (ActivityTypeManage type : types) {
            if (type.getId().equals(plan.getTaskNameType())) {
                taskNameTypeDisplay = type.getName();
                break;
            }
        }
        return taskNameTypeDisplay;
    }

    /**
     * 构造计划进度页面展示
     *
     * @param plan
     * @return
     * @see
     */
    private String generateProgressRate(Plan plan, String warningDay) {
        String progressRate = plan.getProgressRate();
        if (StringUtils.isEmpty(plan.getProgressRate())) {
            progressRate = "0";
        }
        String warningDayFlag = "before";
        if (StringUtils.isNotEmpty(warningDay)) {
            if (warningDay.trim().startsWith("-")) {
                warningDayFlag = "after";
                warningDay = warningDay.trim().replace("-", "");
            }
        }
        else {
            warningDay = "0";
        }
        if (StringUtils.isEmpty(warningDay)) {
            warningDay = "0";
        }
        Date planEndTime = plan.getPlanEndTime();
        if (planEndTime != null) {
            Date warningDate = dateChange(planEndTime, Integer.parseInt(warningDay),
                    warningDayFlag);
            String currentDate = DateUtil.getStringFromDate(new Date(), DateUtil.YYYY_MM_DD);
            Date nowDate = com.glaway.foundation.common.util.DateUtil.convertStringDate2DateFormat(
                    currentDate, DateUtil.YYYY_MM_DD);
            if ((warningDate.getTime() <= nowDate.getTime())
                    && !"FINISH".equals(plan.getBizCurrent())) {
                return "<div style='width:100%;border:1px solid #dee5e7'><div style='width:"
                        + progressRate + "%;background:#cc0000;color:#000000;padding-left:10px;'>"
                        + progressRate + "%</div></div>";
            }
            else {
                return "<div style='width:100%;border:1px solid #dee5e7'><div style='width:"
                        + progressRate + "%;background:#92cd18;color:#000000;padding-left:10px;'>"
                        + progressRate + "%</div></div>";
            }
        }
        else {
            return "<div style='width:100%;border:1px solid #dee5e7'><div style='width:"
                    + progressRate + "%;background:#92cd18;color:#000000;padding-left:10px;'>"
                    + progressRate + "%</div></div>";
        }
    }

    /**
     * 构造计划名称页面链接
     *
     * @param plan
     * @return
     * @see
     */
    private String generatePlanNameUrl(Plan plan) {
        if ("true".equals(plan.getResult())) {
            return "<a href='javascript:void(0)' onclick=\"viewPlan_(\'" + plan.getId()
                    + "\')\" style='color:gray'>" + plan.getPlanName() + "</a>";
        }
        else {
            return "<a href='javascript:void(0)' onclick=\"viewPlan_(\'" + plan.getId()
                    + "\')\" style='color:blue'>" + plan.getPlanName() + "</a>";
        }
    }

    /**
     * 构造计划状态显示及链接
     *
     * @param plan
     * @return
     * @see
     */
    private String generatePlanBizCurrent(Plan plan, List<LifeCycleStatus> statusList) {
        String status = "";
        if ("PAUSE".equals(plan.getProjectStatus())) {
            status = "已暂停";
        }
        else if ("CLOSE".equals(plan.getProjectStatus())) {
            status = "已关闭";
        }
        else {
            for (LifeCycleStatus lifeCycleStatus : statusList) {
                if (lifeCycleStatus.getName().equals(plan.getBizCurrent())) {
                    status = lifeCycleStatus.getTitle();
                    break;
                }
            }
        }
        if ("1".equals(plan.getFlowFlag()) && !"true".equals(plan.getIsAssignBack())) {
            status = "<a href='#' onclick='openFlowPlans_(\"" + plan.getId()
                    + "\")'><font color='blue'>" + status + "</font></a>";
        }
        return status;
    }

    /**
     * 判断操作栏修改按钮是否隐藏
     *
     * @param plan
     * @return
     * @see
     */
    private boolean hideModify(Plan plan, PlanAuthorityVo parameVo, Project currentProject) {
        String isModify = parameVo.getIsModify();
        String planModifyOperationCode = parameVo.getPlanModifyOperationCode();
        boolean isPmo = parameVo.isPmo();
        boolean isProjectManger = parameVo.isProjectManger();
        // 需求变跟 权限变跟 如果父计划如果有负责人则给负责人权限 如果没有负责人则给 创建者权限 现在如果不是一级计划计划状态为拟制中时 计划操作权限跟着创建者走
        // 其它情况跟着父计划的负责人走
        if (StringUtils.isNotEmpty(plan.getParentPlanId())
                && plan.getParentPlan() != null
                &&
                // 需求变动 非拟制中的计划和 拟制中计划且父计划是非拟制中计划走新逻辑 其它的走原本逻辑
                (!("EDITING".equals(plan.getBizCurrent())) || (!("EDITING".equals(plan.getParentPlan().getBizCurrent())) && "EDITING".equals(plan.getBizCurrent()))

                )) {
            // 父计划如果有负责人 则权限跟着负责人走 如果 没有负责人权限跟着创建者走
            if (UserUtil.getInstance().getUser().getId().equals(plan.getParentPlan().getOwner())
                    || (UserUtil.getInstance().getUser().getId().equals(
                    plan.getParentPlan().getCreateBy()) && StringUtils.isEmpty(plan.getParentPlan().getOwner()))) {
                if ("true".equals(planModifyOperationCode)) {
                    if ("false".equals(plan.getResult())) {
                        if ("true".equals(isModify)
                                && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                            if (plan.getProject() == null) {
                                plan.setProject(currentProject);
                            }
                            if ("STARTING".equals(plan.getProject().getBizCurrent())) {

                                if (("EDITING".equals(plan.getBizCurrent()) && (StringUtils.isEmpty(plan.getFlowStatus())
                                        || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack())))) {
                                    return false;
                                }
                                else {
                                    return true;
                                }

                            }
                            else if ("EDITING".equals(plan.getProject().getBizCurrent())) {
                                if (!isPmo) {
                                    return true;
                                }
                                else {

                                    if (("EDITING".equals(plan.getBizCurrent()) && (StringUtils.isEmpty(plan.getFlowStatus())
                                            || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack())))) {
                                        return false;
                                    }
                                    else {
                                        return true;
                                    }

                                }
                            }
                            else {
                                return true;
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }

            }
            else {

                return true;
            }

        }
        // 如果是一级计划用原先逻辑
        else {
            if ("true".equals(planModifyOperationCode)) {
                if ("false".equals(plan.getResult())) {
                    if ("true".equals(isModify)
                            && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                        if (plan.getProject() == null) {
                            plan.setProject(currentProject);
                        }
                        if ("STARTING".equals(plan.getProject().getBizCurrent())) {
                            if (UserUtil.getInstance().getUser().getId().equals(plan.getCreateBy())
                                    || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                if (("EDITING".equals(plan.getBizCurrent()) && (StringUtils.isEmpty(plan.getFlowStatus())
                                        || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack())))) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else {
                                return true;
                            }
                        }
                        else if ("EDITING".equals(plan.getProject().getBizCurrent())) {
                            if (!isPmo) {
                                return true;
                            }
                            else {
                                if (UserUtil.getInstance().getUser().getId().equals(
                                        plan.getCreateBy())
                                        || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                    if (("EDITING".equals(plan.getBizCurrent()) && (StringUtils.isEmpty(plan.getFlowStatus())
                                            || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack())))) {
                                        return false;
                                    }
                                    else {
                                        return true;
                                    }
                                }
                                else {
                                    return true;
                                }
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
            else {
                return true;
            }
        }

    }

    /**
     * 构造计划树操作栏操作按钮
     *
     * @param plan
     * @return
     * @see
     */
    private String generateOptBtn(Plan plan, PlanAuthorityVo parameVo, Project project) {

        String returnStr = "";
        String modifyBtnStr = "<a class='basis ui-icon-pencil' style='display:inline-block;cursor:pointer;' onClick='modifyPlanOnTree_(\""
                + plan.getId() + "\")' title='修改'></a>";
        String assignBtnStr = "<a class='basis ui-icon-issue' style='display:inline-block;cursor:pointer;' onClick='assignPlanOnTree_(\""
                + plan.getId() + "\")' title='发布'></a>";
        String deleteBtnStr = "<a class='basis ui-icon-minus' style='display:inline-block;cursor:pointer;' onClick='deleteOnTree_(\""
                + plan.getId() + "\")' title='删除'></a>";
        String changeBtnStr = "";
        if ("true".equals(plan.getIsChangeSingleBack())) {
            changeBtnStr = "<a class='basis ui-icon-planchange' style='display:inline-block;cursor:pointer;' onClick='changePlanOnTreeFlow_(\""
                    + plan.getId() + "\")' title='变更'></a>";
        }
        else {
            changeBtnStr = "<a class='basis ui-icon-planchange' style='display:inline-block;cursor:pointer;' onClick='changePlanOnTree_(\""
                    + plan.getId() + "\")' title='变更'></a>";
        }
        String discardBtnStr = "<a class='basis ui-icon-trash' style='display:inline-block;cursor:pointer;' onClick='discardPlan_(\""
                + plan.getId() + "\")' title='废弃'></a>";
        String revocationBtnStr = "<a class='basis ui-icon-return' style='display:inline-block;cursor:pointer;' onClick='revocationPlanOnTree_(\""
                + plan.getId() + "\")' title='撤消'></a>";
        if (!hideModify(plan, parameVo, project)) {
            returnStr = returnStr + modifyBtnStr;
        }
        if (!hideAssign(plan, parameVo, project)) {
            returnStr = returnStr + assignBtnStr;
        }
        if (!hideDelete(plan, parameVo, project)) {
            returnStr = returnStr + deleteBtnStr;
        }
        if (!hideChange(plan, parameVo, project)) {
            returnStr = returnStr + changeBtnStr;
        }
        if(!hideRevocation(plan, parameVo, project)){
            returnStr = returnStr + revocationBtnStr;
        }
        if (!hideDiscard(plan, parameVo, project)) {
            returnStr = returnStr + discardBtnStr;
        }
        return returnStr;
    }

    /**
     * 判断操作栏下达按钮是否隐藏
     *
     * @param plan
     * @return
     * @see
     */
    private boolean hideAssign(Plan plan, PlanAuthorityVo parameVo, Project currentProject) {
        String isModify = parameVo.getIsModify();
        String planAssignOperationCode = parameVo.getPlanAssignOperationCode();
        boolean isPmo = parameVo.isPmo();
        boolean isProjectManger = parameVo.isProjectManger();
        if (StringUtils.isNotEmpty(plan.getParentPlanId())
                && plan.getParentPlan() != null
                &&
                // 需求变动 非拟制中的计划和 拟制中计划且父计划是非拟制中计划走新逻辑 其它的走原本逻辑
                (!("EDITING".equals(plan.getBizCurrent())) || (!("EDITING".equals(plan.getParentPlan().getBizCurrent())) && "EDITING".equals(plan.getBizCurrent()))

                )) {
            // 父计划如果有负责人 则权限跟着负责人走 如果 没有负责人权限跟着创建者走
            if (UserUtil.getInstance().getUser().getId().equals(plan.getParentPlan().getOwner())
                    || (UserUtil.getInstance().getUser().getId().equals(
                    plan.getParentPlan().getCreateBy()) && StringUtils.isEmpty(plan.getParentPlan().getOwner()))) {
                if ("true".equals(planAssignOperationCode)) {
                    if ("false".equals(plan.getResult())) {
                        if ("true".equals(isModify)
                                && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                            if (plan.getProject() == null) {
                                plan.setProject(currentProject);
                            }
                            if ("STARTING".equals(plan.getProject().getBizCurrent())) {
                                if ("EDITING".equals(plan.getBizCurrent())
                                        && (StringUtils.isEmpty(plan.getFlowStatus())
                                        || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack()))) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else {

                                return true;
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }

            }
            else {
                return true;

            }

        }
        else {
            if ("true".equals(planAssignOperationCode)) {
                if ("false".equals(plan.getResult())) {
                    if ("true".equals(isModify)
                            && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                        if (plan.getProject() == null) {
                            plan.setProject(currentProject);
                        }
                        if ("STARTING".equals(plan.getProject().getBizCurrent())) {
                            if (UserUtil.getInstance().getUser().getId().equals(plan.getCreateBy())
                                    || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                if ("EDITING".equals(plan.getBizCurrent())
                                        && (StringUtils.isEmpty(plan.getFlowStatus())
                                        || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack()))) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else {
                                return true;
                            }
                        }
                        else {

                            return true;
                        }
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
            else {
                return true;
            }

        }

    }

    /**
     * 判断操作栏删除按钮是否隐藏
     *
     * @param plan
     * @return
     * @see
     */
    private boolean hideDelete(Plan plan, PlanAuthorityVo parameVo, Project currentProject) {
        String isModify = parameVo.getIsModify();
        String planDeleteOperationCode = parameVo.getPlanDeleteOperationCode();
        boolean isPmo = parameVo.isPmo();
        boolean isProjectManger = parameVo.isProjectManger();
        if (StringUtils.isNotEmpty(plan.getParentPlanId())
                && plan.getParentPlan() != null
                &&
                // 需求变动 非拟制中的计划和 拟制中计划且父计划是非拟制中计划走新逻辑 其它的走原本逻辑
                (!("EDITING".equals(plan.getBizCurrent())) || (!("EDITING".equals(plan.getParentPlan().getBizCurrent())) && "EDITING".equals(plan.getBizCurrent()))

                )) {
            // 父计划如果有负责人 则权限跟着负责人走 如果 没有负责人权限跟着创建者走
            if (UserUtil.getInstance().getUser().getId().equals(plan.getParentPlan().getOwner())
                    || (UserUtil.getInstance().getUser().getId().equals(
                    plan.getParentPlan().getCreateBy()) && StringUtils.isEmpty(plan.getParentPlan().getOwner()))) {
                if ("true".equals(planDeleteOperationCode)) {
                    if ("false".equals(plan.getResult())) {
                        if ("true".equals(isModify)
                                && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                            if (plan.getProject() == null) {
                                plan.setProject(currentProject);
                            }
                            if ("STARTING".equals(plan.getProject().getBizCurrent())) {
                                if ("EDITING".equals(plan.getBizCurrent())
                                        && (StringUtils.isEmpty(plan.getFlowStatus())
                                        || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack()))) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else if ("EDITING".equals(plan.getProject().getBizCurrent())) {
                                if (!isPmo) {
                                    return true;
                                }
                                else {
                                    if ("EDITING".equals(plan.getBizCurrent())
                                            && (StringUtils.isEmpty(plan.getFlowStatus())
                                            || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack()))) {
                                        return false;
                                    }
                                    else {
                                        return true;
                                    }
                                }
                            }
                            else {
                                return true;
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
            else {
                return true;
            }
        }
        else {
            if ("true".equals(planDeleteOperationCode)) {
                if ("false".equals(plan.getResult())) {
                    if ("true".equals(isModify)
                            && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                        if (plan.getProject() == null) {
                            plan.setProject(currentProject);
                        }
                        if ("STARTING".equals(plan.getProject().getBizCurrent())) {
                            if (UserUtil.getInstance().getUser().getId().equals(plan.getCreateBy())
                                    || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                if ("EDITING".equals(plan.getBizCurrent())
                                        && (StringUtils.isEmpty(plan.getFlowStatus())
                                        || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack()))) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else {
                                return true;
                            }
                        }
                        else if ("EDITING".equals(plan.getProject().getBizCurrent())) {
                            if (!isPmo) {
                                return true;
                            }
                            else {
                                if (UserUtil.getInstance().getUser().getId().equals(
                                        plan.getCreateBy())
                                        || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                    if ("EDITING".equals(plan.getBizCurrent())
                                            && (StringUtils.isEmpty(plan.getFlowStatus())
                                            || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsAssignSingleBack()))) {
                                        return false;
                                    }
                                    else {
                                        return true;
                                    }
                                }
                                else {
                                    return true;
                                }
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
            else {
                return true;
            }
        }

    }

    /**
     * 判断操作栏变更按钮是否隐藏
     *
     * @param plan
     * @return
     * @see
     */
    private boolean hideChange(Plan plan, PlanAuthorityVo parameVo, Project currentProject) {
        String isModify = parameVo.getIsModify();
        String planChangeOperationCode = parameVo.getPlanChangeOperationCode();
        boolean isPmo = parameVo.isPmo();
        boolean isProjectManger = parameVo.isProjectManger();
        if ("true".equals(planChangeOperationCode)) {
            if ("false".equals(plan.getResult())) {
                if ("true".equals(isModify)
                        && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                    if (plan.getProject() == null) {
                        plan.setProject(currentProject);
                    }
                    if ("STARTING".equals(plan.getProject().getBizCurrent())) {
                        if ("ORDERED".equals(plan.getBizCurrent())
                                && (StringUtils.isEmpty(plan.getFlowStatus())
                                || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsChangeSingleBack()))) {
                            if (StringUtils.isNotEmpty(plan.getParentPlanId())
                                    && plan.getParentPlan() != null) {
                                boolean isPmoPd1 = groupService.judgeHasLimit(
                                        appKey,ProjectRoleConstants.PMO, plan.getParentPlan().getOwner(),
                                        null);
                                if (UserUtil.getInstance().getUser().getId().equals(
                                        plan.getParentPlan().getOwner())
                                        || isPmoPd1) {
                                    return false;
                                }
                                else {
                                    return true;
                                }

                            }
                            else if (UserUtil.getInstance().getUser().getId().equals(
                                    plan.getCreateBy())
                                    || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                return false;
                            }
                            else {
                                return true;
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    else if ("EDITING".equals(plan.getProject().getBizCurrent())) {
                        if (!isPmo) {
                            return true;
                        }
                        else {
                            if ("ORDERED".equals(plan.getBizCurrent())
                                    && (StringUtils.isEmpty(plan.getFlowStatus())
                                    || "NORMAL".equals(plan.getFlowStatus()) || "true".equals(plan.getIsChangeSingleBack()))) {
                                if (StringUtils.isNotEmpty(plan.getParentPlanId())
                                        && plan.getParentPlan() != null) {
                                    boolean isPmoPd1 = groupService.judgeHasLimit(
                                            appKey,ProjectRoleConstants.PMO, plan.getParentPlan().getOwner(),
                                            null);
                                    if (UserUtil.getInstance().getUser().getId().equals(
                                            plan.getParentPlan().getOwner())
                                            || isPmoPd1) {
                                        return false;
                                    }
                                    else {
                                        return true;
                                    }

                                }
                                else if (UserUtil.getInstance().getUser().getId().equals(
                                        plan.getCreateBy())
                                        || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else {
                                return true;
                            }
                        }
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }

    }


    /**
     * 判断操作栏撤消按钮是否隐藏
     *
     * @param plan
     * @return
     * @see
     */
    private boolean hideRevocation(Plan plan, PlanAuthorityVo parameVo, Project currentProject) {
        String isModify = parameVo.getIsModify();
        String planRevocationOperationCode = parameVo.getPlanRevocationOperationCode();
        boolean isPmo = parameVo.isPmo();
        boolean isProjectManger = parameVo.isProjectManger();
        if ("true".equals(planRevocationOperationCode)) {
            if ("false".equals(plan.getResult())) {
                if ("true".equals(isModify)
                        && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                    if (plan.getProject() == null) {
                        plan.setProject(currentProject);
                    }
                    if ("STARTING".equals(plan.getProject().getBizCurrent())) {
                        if ("ORDERED".equals(plan.getBizCurrent())
                                && (!CommonUtil.isEmpty(plan.getFlowStatus()) && "CHANGE".equals(plan.getFlowStatus())
                                && "true".equals(plan.getIsChangeSingleBack()))) {
                            List<TemporaryPlan> planList = getTemporaryPlanList(plan.getId());
                            if(!CommonUtil.isEmpty(planList)){
                                if(!CommonUtil.isEmpty(planList.get(0).getLauncher())
                                        && planList.get(0).getLauncher().equals(UserUtil.getInstance().getUser().getId())){
                                    return false;
                                }else{
                                    return true;
                                }
                            }else{
                                return true;
                            }


                        }else{
                            return true;
                        }
                    }else if("EDITING".equals(plan.getProject().getBizCurrent())){
                        if ("ORDERED".equals(plan.getBizCurrent())
                                && (!CommonUtil.isEmpty(plan.getFlowStatus()) && "CHANGE".equals(plan.getFlowStatus())
                                && "true".equals(plan.getIsChangeSingleBack()))) {
                            List<TemporaryPlan> planList = getTemporaryPlanList(plan.getId());
                            if(!CommonUtil.isEmpty(planList)){
                                if(!CommonUtil.isEmpty(planList.get(0).getLauncher())
                                        && planList.get(0).getLauncher().equals(UserUtil.getInstance().getUser().getId())){
                                    return false;
                                }else{
                                    return true;
                                }
                            }else{
                                return true;
                            }
                        }else{
                            return true;
                        }
                    }else{
                        return true;
                    }
                }
                else
                {
                    return true;
                }
            }else{
                return true;
            }
        }else{
            return true;
        }
    }

    /**
     * 判断操作栏废弃按钮是否隐藏
     *
     * @param plan
     * @return
     * @see
     */
    private boolean hideDiscard(Plan plan, PlanAuthorityVo parameVo, Project currentProject) {
        String isModify = parameVo.getIsModify();
        String planDiscardOperationCode = parameVo.getPlanDiscardOperationCode();
        boolean isPmo = parameVo.isPmo();
        boolean isProjectManger = parameVo.isProjectManger();
        if ("true".equals(planDiscardOperationCode)) {
            if ("false".equals(plan.getResult())) {
                if ("true".equals(isModify)
                        && !PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())) {
                    if (plan.getProject() == null) {
                        plan.setProject(currentProject);
                    }
                    if ("STARTING".equals(plan.getProject().getBizCurrent())) {
                        if (!PlanConstants.PLAN_EDITING.equals(plan.getBizCurrent())
                                && !PlanConstants.PLAN_INVALID.equals(plan.getBizCurrent())) {
                            if (StringUtils.isNotEmpty(plan.getParentPlanId())
                                    && plan.getParentPlan() != null) {
                                if (UserUtil.getInstance().getUser().getId().equals(
                                        plan.getParentPlan().getOwner())) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else if (UserUtil.getInstance().getUser().getId().equals(
                                    plan.getCreateBy())
                                    || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                return false;
                            }
                            else {
                                return true;
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    else if ("EDITING".equals(plan.getProject().getBizCurrent())) {
                        if (!isPmo) {
                            return true;
                        }
                        else {
                            if (!PlanConstants.PLAN_EDITING.equals(plan.getBizCurrent())
                                    && !PlanConstants.PLAN_INVALID.equals(plan.getBizCurrent())) {
                                if (StringUtils.isNotEmpty(plan.getParentPlanId())
                                        && plan.getParentPlan() != null) {
                                    if (UserUtil.getInstance().getUser().getId().equals(
                                            plan.getParentPlan().getOwner())) {
                                        return false;
                                    }
                                    else {
                                        return true;
                                    }
                                }
                                else if (UserUtil.getInstance().getUser().getId().equals(
                                        plan.getCreateBy())
                                        || (isProjectManger && (plan.getIsCreateByPmo() != null && plan.getIsCreateByPmo()))) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                            else {
                                return true;
                            }
                        }
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }

    }

    @Override
    public List<TemporaryPlan> getTemporaryPlanList(String planId) {
        List<TemporaryPlan> list = sessionFacade.findHql("from TemporaryPlan where planId=?", planId);
        return list;
    }

    /**
     * Description: <br>
     *
     * @param date
     * @param day
     * @param beforOrAfter
     * @return
     * @see
     */
    private Date dateChange(Date date, int day, String beforOrAfter) {
        Date changeDate = new Date();
        if ("after".equals(beforOrAfter)) {
            changeDate = DateUtil.nextDay(date, day);
        }
        else {
            changeDate = DateUtil.nextDay(date, 0 - day);
        }
        return changeDate;
    }

    @Override
    public Map<String,Object> goBasicCheck(Plan plan) {
        Map<String,Object> map = new HashMap<String, Object>();
        plan = getEntity(plan.getId());

        //手动添加负责人
        if (StringUtils.isNotEmpty(plan.getOwner())) {
            TSUserDto ownerInfo = userService.getUserByUserId(plan.getOwner());
            plan.setOwnerInfo(ownerInfo);
        }
        //手动添加下达人
        if (StringUtils.isNotEmpty(plan.getAssigner())) {
            TSUserDto assignerInfo = userService.getUserByUserId(plan.getAssigner());
            plan.setAssignerInfo(assignerInfo);
        }

        if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
            Plan planTemp = getEntity(plan.getParentPlanId());
            if (planTemp != null) {
                plan.setParentPlanName(planTemp.getPlanName() + "-" + planTemp.getPlanNumber());
            }
        }

        // 通过计划获得前置，并把前置组装
        List<PreposePlan> preposePlanList = preposePlanService.getPreposePlansByPlanId(plan);
        // 组装前置数据
        StringBuffer preposeSb = new StringBuffer();
        int k = 0;
        for (PreposePlan preposePlan : preposePlanList) {
            if (k > 0) {
                preposeSb.append(",");
            }
            if (preposePlan.getPreposePlanInfo() == null) {
                continue;
            }
            preposeSb.append(preposePlan.getPreposePlanInfo().getPlanName());
            k++ ;
        }
        plan.setPreposePlans(preposeSb.toString());

        // 获取计划的生命周期
        Plan statusP = new Plan();
        initBusinessObject(statusP);
        List<LifeCycleStatus> statusList = statusP.getPolicy().getLifeCycleStatusList();
        //map.put("statusList", statusList);

        if (plan.getOwnerInfo() != null) {
            if (plan.getOwnerInfo().getTSDepart() != null) {
                plan.setOwnerDept(plan.getOwnerInfo().getTSDepart().getDepartname());
            }
        }

        for (LifeCycleStatus status : statusList) {
            if (status.getName().equals(plan.getBizCurrent())) {
                plan.setStatus(status.getTitle());
                break;
            }
        }

        if (plan.getOwnerInfo() != null) {
            plan.setOwnerRealName(plan.getOwnerInfo().getRealName());
        }
        if (plan.getPlanLevelInfo() != null) {
            plan.setPlanLevelName(plan.getPlanLevelInfo().getName());
        }

        if ("true".equals(plan.getMilestone())) {
            plan.setMilestoneName("是");
        }
        else {
            plan.setMilestoneName("否");
        }

        if (plan.getOwnerInfo() != null) {
            map.put("ownerShow", plan.getOwnerInfo().getRealName() + "-"
                    + plan.getOwnerInfo().getUserName());
        }
        else {
            map.put("ownerShow", "");
        }
        if (!PlanConstants.PLAN_FINISH.equals(plan.getBizCurrent())
                && !PlanConstants.PLAN_INVALID.equals(plan.getBizCurrent())) {
            if (ProjectStatusConstants.PAUSE.equals(plan.getProjectStatus())) {
                plan.setStatus(ProjectStatusConstants.PAUSE_CHI);
            }
            else if (ProjectStatusConstants.CLOSE.equals(plan.getProjectStatus())) {
                plan.setStatus(ProjectStatusConstants.CLOSE_CHI);
            }
        }
        PlanDto dto  = new PlanDto();
        try {
            dto = (PlanDto) VDataUtil.getVDataByEntity(plan,PlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("plan",dto);
        return map;
    }

    @Override
    public FeignJson getPlanLifeStatus() {
        FeignJson j = new FeignJson();
        // 获取计划的生命周期
        Plan statusP = new Plan();
        initBusinessObject(statusP);
        List<LifeCycleStatus> statusList = statusP.getPolicy().getLifeCycleStatusList();
        String json = JSON.toJSONString(statusList);
        j.setObj(json);
        return j;
    }

    @Override
    public void deletePlansPhysics(List<Plan> planList) {
        deletePlans(planList);
    }

    @Override
    public FeignJson getTaskNameType(String planName) {
        FeignJson j = new FeignJson();
        String message = "";
        try {
            /*String dictCode = "activeCategory";
            Map<String,List<TSTypeDto>> tsMap = feignSystemService.getAllTypesOfTypeGroup(appKey);
            List<TSTypeDto> types = tsMap.get(dictCode);*/
            List<ActivityTypeManage> activityTypeManageList = activityTypeManageEntityService.getAllActivityTypeManage(true);
            if (StringUtils.isNotEmpty(planName.trim())) {
                message = "获取计划类型成功";
                NameStandardDto ns = new NameStandardDto();
                ns.setName(planName.trim());
                List<NameStandardDto> nameStandards = nameStandardFeignService.searchNameStandardsAccurate(ns);
                if (!CommonUtil.isEmpty(nameStandards)) {
                    j.setObj(nameStandards.get(0).getActiveCategory());
                }
                else {
                  /*  if (!CommonUtil.isEmpty(activityTypeManageList)) {
                        j.setObj(activityTypeManageList.get(0).getId());
                    }*/
                    /*else {
                        dictCode = "activeCategory";
                        types = tsMap.get(dictCode);
                        if (!CommonUtil.isEmpty(types)) {
                            j.setObj(types.get(0).getTypecode());
                        }
                    }*/
                }
                j.setSuccess(true);
            }
            else {
                message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.getTaskNameTypeSuccess");
                if (!CommonUtil.isEmpty(activityTypeManageList)) {
                    //      j.setObj(activityTypeManageList.get(0).getId());
                }
                else {
                    /*dictCode = "activeCategory";
                    types = tsMap.get(dictCode);
                    if (CommonUtil.isEmpty(types)) {
                        j.setObj(types.get(0).getTypecode());
                    }*/
                }
                j.setSuccess(true);
            }
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.getTaskNameTypeFail");
            j.setMsg(message);
            j.setSuccess(false);
            Object[] params = new Object[] {message, Plan.class.getClass() + " oids: "};// 异常原因：{0}；异常描述：{1}
            throw new GWException(GWConstants.ERROR_2001, params, e);
        }
        return j;
    }


    @Override
    public List<Plan> queryPlanListForCustomViewTreegrid(Plan plan, String planIds) {
        StringBuffer hqlBuffer = createStringBuffer(plan.getProjectId());
        hqlBuffer.append(" and PL.ID in ("+planIds+")");
        hqlBuffer.append(" ORDER BY PL.PARENTPLANID, PL.STOREYNO, PL.CREATETIME");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        return changeQueryResToPlanList(objArrayList);
    }


    @Override
    public void orderReviewTask(Plan plan, ReviewBaseInfoVo vo, String userId) {

        // 保存plan
        saveOrUpdate(plan);

        PlanLog planLog = new PlanLog();
        planLog.setPlanId(plan.getId());
        planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_ORDER_REVIEW_TASK);

        TSUserDto user = userService.getUserByUserId(userId);
        planLog.setCreateBy(user.getId());
        planLog.setCreateName(user.getUserName());
        planLog.setCreateFullName(user.getRealName());
        planLog.setCreateTime(new Date());
        sessionFacade.save(planLog);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        /*
         * JsonRequery jsonReq = new JsonRequery(); jsonReq.setReqObj(gson.toJson(vo));
         */
        String reviewBaceInfoStr = gson.toJson(vo);
        reviewFeignService.saveReviewBaceInfo(reviewBaceInfoStr);

    }

    @Override
    public List<PlanDto> getPlanInfoList(PlanDto plan) {
        List<PlanDto> results = new ArrayList<>();
        List<PlanDto> list = queryPlanListForTreegridView(plan, plan.getPlanViewInfoId(), new ArrayList<ConditionVO>(), "","","","");
        List<Plan> list1 = CodeUtils.JsonListToList(list, Plan.class);
        // -------------------------------------------性能优化
        // 将此部分新增------------------------------------------↑
        Plan lifeCyclePlan = new Plan();
        initBusinessObject(lifeCyclePlan);
        List<LifeCycleStatus> statusList = lifeCyclePlan.getPolicy().getLifeCycleStatusList();
        for (Plan p : list1) {
            if (ProjectStatusConstants.PAUSE.equals(p.getProjectStatus())) {
                p.setStatus(ProjectStatusConstants.PAUSE_CHI);
            }
            else if (ProjectStatusConstants.CLOSE.equals(p.getProjectStatus())) {
                p.setStatus(ProjectStatusConstants.CLOSE_CHI);
            }
            else {
                for (LifeCycleStatus lifeCycleStatus : statusList) {
                    if (lifeCycleStatus.getName().equals(p.getBizCurrent())) {
                        p.setStatus(lifeCycleStatus.getTitle());
                        break;
                    }
                }
            }
        }
        try {
            results = (List<PlanDto>) VDataUtil.getVDataByEntity(list1,PlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public TemporaryPlanDto getTemporaryPlanEntity(String id) {
        TemporaryPlan p = (TemporaryPlan)sessionFacade.getEntity(TemporaryPlan.class,id);
        TemporaryPlanDto plan = new TemporaryPlanDto();
        if (!CommonUtil.isEmpty(p)) {
            try {
                plan = (TemporaryPlanDto)VDataUtil.getVDataByEntity(p,TemporaryPlanDto.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return plan;
    }

    @Override
    public void StopPlanAssignExcution(String formId,String userId) {
        ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
        approvePlanInfo.setFormId(formId);
        List<ApprovePlanInfo> approve = this.queryAssignList(approvePlanInfo, 1, 10, false);
        if(!CommonUtil.isEmpty(approve)){
            for (int i = 0; i < approve.size(); i++ ) {
                Plan p = (Plan) sessionFacade.getEntity(Plan.class, approve.get(i).getPlanId());
                if(p != null){
                    p.setBizCurrent(PlanConstants.PLAN_EDITING);
                    p.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
                    p.setIsAssignBack("");
                    sessionFacade.saveOrUpdate(p);
                    try {
                        TSUserDto user = userService.getUserByUserId(userId);
                        planAssignBackMsgNotice.getAllMessage(p.getId(), user);
                    }
                    catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }
        ApprovePlanForm q = (ApprovePlanForm) sessionFacade.getEntity(ApprovePlanForm.class, formId);
        //planService.delete(q);
        for(ApprovePlanInfo q2: approve){
            //planService.delete(q2);
        }
    }

    @Override
    public List<Plan> exportXls(Plan plan, String projectId) {
        List<Plan> planLst = new ArrayList<Plan>();
        plan.setProjectId(projectId);
        Map<String, String> preposeMap = new HashMap<String, String>();
        Map<String, String> deliverMap = new HashMap<String, String>();
        List<Plan> plans = this.queryPlanList(plan, 1, 10, false);
        List<PreposePlan> preposePlans = preposePlanService.queryPreposePlanByProjectId(projectId);
        for(PreposePlan prepose : preposePlans) {
            String planId = prepose.getPlanId();
            if(CommonUtil.isEmpty(preposeMap.get(planId))) {
                if(!CommonUtil.isEmpty(prepose.getPreposePlanInfo())) {
                    if(!CommonUtil.isEmpty(prepose.getPreposePlanInfo())) {
                        preposeMap.put(planId, String.valueOf(prepose.getPreposePlanInfo().getPlanNumber()));
                    }
                }
            } else {
                String preposeNumber = preposeMap.get(planId);
                if(!CommonUtil.isEmpty(prepose.getPreposePlanInfo())) {
                    String currentNumber = String.valueOf(prepose.getPreposePlanInfo().getPlanNumber());
                    preposeNumber = preposeNumber + "," + currentNumber;
                    preposeMap.put(planId, preposeNumber);
                }
            }
        }

        List<DeliverablesInfo> deliverablesInfo = deliverablesInfoService.getDeliverablesByProject(projectId);
        for(DeliverablesInfo deliver : deliverablesInfo) {
            String userObjectId = deliver.getUseObjectId();
            if(CommonUtil.isEmpty(deliverMap.get(userObjectId))) {
                deliverMap.put(userObjectId, deliver.getName());
            } else {
                String deliverName = deliverMap.get(userObjectId);
                String currentName = deliver.getName();
                deliverName = deliverName + "," + currentName;
                deliverMap.put(userObjectId, deliverName);
            }
        }

        try {

            for (Plan p : plans) {
                Plan planTemp = new Plan();
                planTemp.setId(p.getId());
                planTemp.setPlanNumber(p.getPlanNumber());
                planTemp.setPlanName(p.getPlanName());
                planTemp.setWorkTime(p.getWorkTime());
                planTemp.setPlanStartTime(p.getPlanStartTime());
                planTemp.setPlanEndTime(p.getPlanEndTime());
                planTemp.setRemark(p.getRemark());

                if(!CommonUtil.isEmpty(p.getParentPlanId())) {
                    planTemp.setParentPlanNo(p.getParentPlan().getPlanNumber());
                }

                if (!CommonUtil.isEmpty(p.getOwner())) {
                    TSUserDto owner = userService.getUserByUserId(p.getOwner());
                    if (!CommonUtil.isEmpty(owner)) {
                        planTemp.setOwnerRealName(owner.getRealName()+"-"+owner.getUserName());
                    }
                }

                if (!CommonUtil.isEmpty(p.getPlanLevel())) {
                    BusinessConfig pLevel = (BusinessConfig) sessionFacade.getEntity(BusinessConfig.class,
                            p.getPlanLevel());
                    if (pLevel != null) {
                        planTemp.setPlanLevelName(pLevel.getName());
                    }
                }

                if(!CommonUtil.isEmpty(p.getMilestone())) {
                    if("true".equals(p.getMilestone())) {
                        planTemp.setMilestoneName("是");
                    } else {
                        planTemp.setMilestoneName("否");
                    }
                }
                planTemp.setPreposeNos(preposeMap.get(planTemp.getId()));
                planTemp.setDeliverablesName(deliverMap.get(planTemp.getId()));
                String taskNameType = p.getTaskNameType();
                if (StringUtils.isNotEmpty(taskNameType)){
                    ActivityTypeManage activityTypeManage =activityTypeManageEntityService.queryActivityTypeManageById(taskNameType);
                    if (activityTypeManage!=null&&StringUtils.isNotEmpty(activityTypeManage.getName())){
                        planTemp.setTaskNameType(activityTypeManage.getName());
                    }else{
                        planTemp.setTaskNameType("");
                    }
                }else{
                    planTemp.setTaskNameType("");
                }
                planLst.add(planTemp);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return planLst;
    }

    @Override
    public List<Plan> exportXlsSingle(Plan plan) {
        List<Plan> planLst = new ArrayList<Plan>();
        Map<String, String> preposeMap = new HashMap<String, String>();
        Map<String, String> deliverMap = new HashMap<String, String>();
        List<Plan> plans = this.getPlanAllChildren(plan);
        //拼接sql查询所需要的计划id
        StringBuffer planIds = new StringBuffer();
        for(Plan planTemp : plans) {
            if(CommonUtil.isEmpty(planIds)) {
                planIds.append(planTemp.getId() + "'");
            } else {
                planIds.append(", '" + planTemp.getId() + "'");
            }
        }

        List<PreposePlan> preposePlans = preposePlanService.queryPreposePlanByPlanIds(planIds.toString());
        for(PreposePlan prepose : preposePlans) {
            String planTempId = prepose.getPlanId();
            if(CommonUtil.isEmpty(preposeMap.get(planTempId))) {
                if(!CommonUtil.isEmpty(prepose.getPreposePlanInfo())) {
                    preposeMap.put(planTempId, String.valueOf(prepose.getPreposePlanInfo().getPlanNumber()));
                }
            } else {
                String preposeNumber = preposeMap.get(planTempId);
                String currentNumber = String.valueOf(prepose.getPreposePlanInfo().getPlanNumber());
                preposeNumber = preposeNumber + "," + currentNumber;
                preposeMap.put(planTempId, preposeNumber);
            }
        }

        List<DeliverablesInfo> deliverablesInfo = deliverablesInfoService.queryDeliverablesInfoByPlanIds(planIds.toString());
        for(DeliverablesInfo deliver : deliverablesInfo) {
            String userObjectId = deliver.getUseObjectId();
            if(CommonUtil.isEmpty(deliverMap.get(userObjectId))) {
                deliverMap.put(userObjectId, deliver.getName());
            } else {
                String deliverName = deliverMap.get(userObjectId);
                String currentName = deliver.getName();
                deliverName = deliverName + "," + currentName;
                deliverMap.put(userObjectId, deliverName);
            }
        }

        try {

            for (Plan p : plans) {
                Plan planTemp = new Plan();
                planTemp.setId(p.getId());
                planTemp.setPlanNumber(p.getPlanNumber());
                planTemp.setPlanName(p.getPlanName());
                planTemp.setWorkTime(p.getWorkTime());
                planTemp.setPlanStartTime(p.getPlanStartTime());
                planTemp.setPlanEndTime(p.getPlanEndTime());
                planTemp.setRemark(p.getRemark());

                if(!CommonUtil.isEmpty(p.getParentPlanId())) {
                    planTemp.setParentPlanNo(p.getParentPlan().getPlanNumber());
                }

                if (!CommonUtil.isEmpty(p.getOwner())) {
                    TSUserDto owner = userService.getUserByUserId(p.getOwner());
                    if (!CommonUtil.isEmpty(owner)) {
                        planTemp.setOwnerRealName(owner.getRealName()+"-"+owner.getUserName());
                    }
                }

                if (!CommonUtil.isEmpty(p.getPlanLevel())) {
                    BusinessConfig pLevel = (BusinessConfig) sessionFacade.getEntity(BusinessConfig.class,
                            p.getPlanLevel());
                    if (pLevel != null) {
                        planTemp.setPlanLevelName(pLevel.getName());
                    }
                }

                if(!CommonUtil.isEmpty(p.getMilestone())) {
                    if("true".equals(p.getMilestone())) {
                        planTemp.setMilestoneName("是");
                    } else {
                        planTemp.setMilestoneName("否");
                    }
                }
                planTemp.setPreposeNos(preposeMap.get(planTemp.getId()));
                planTemp.setDeliverablesName(deliverMap.get(planTemp.getId()));
                String taskNameType = p.getTaskNameType();
                if (StringUtils.isNotEmpty(taskNameType)){
                    ActivityTypeManage activityTypeManage =activityTypeManageEntityService.queryActivityTypeManageById(taskNameType);
                    if (activityTypeManage!=null&&StringUtils.isNotEmpty(activityTypeManage.getName())){
                        planTemp.setTaskNameType(activityTypeManage.getName());
                    }else{
                        planTemp.setTaskNameType("");
                    }
                }else{
                    planTemp.setTaskNameType("");
                }
                planLst.add(planTemp);
            }
            log.info(I18nUtil.getValue("com.glaway.ids.pm.project.plan.exportExcelSuccess"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return planLst;
    }

    @Override
    public List<MppInfo> saveMppInfo(List<Plan> plans) {
        List<MppInfo> mppList = new ArrayList<MppInfo>();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        for (int k = 0; k < plans.size(); k++ ) {
            // 通过计划模板ID获得WBS计划
            PlanTemplateDetailReq planTemplateDetailReq = new PlanTemplateDetailReq();
            planTemplateDetailReq.setPlanTemplateId(plans.get(k).getId());
            List<T> planTemplateDetailList = getList(planTemplateDetailReq, null, null);
            // 通过WBS计划获得交付项ID
            int i = 1;
            for (Object t : planTemplateDetailList) {

                // 通过交付项获ID得交付项
                Plan plan = (Plan)t;
                DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
                deliverablesInfo.setUseObjectId(plan.getId());
                List<DeliverablesInfo> deliverablesList = deliverablesInfoService.queryDeliverableList(
                        deliverablesInfo, 0, 0, false);

                // 把数据组成MPP的数据
                StringBuffer sb = new StringBuffer();
                int j = 0;
                for (DeliverablesInfo deliverables : deliverablesList) {

                    if (j > 0) {
                        sb.append(",");
                    }
                    sb.append(deliverables.getName());
                    j++ ;
                }

                mppList.add(getMppInfo(plan, sb.toString(), k + 1));
                // 计划上级的ID保存MPP的ID
                paraMap.put(plan.getId(), k + 1);
            }
        }
        for (MppInfo mppInfo : mppList) {
            if (mppInfo.getParentId() != null
                    && !CommonUtil.mapIsEmpty(paraMap, mppInfo.getParentId())) {
                mppInfo.setParentId(paraMap.get(mppInfo.getParentId()).toString());
            }
        }
        // 通过计划编号获得前置Id
        for (MppInfo mppInfo : mppList) {
            String[] preposeSplit = mppInfo.getPreposeName().split("[,]");
            // 把数据组成MPP的数据
            StringBuffer preposeSb = new StringBuffer();
            int k = 0;
            for (String preposeStr : preposeSplit) {
                if (preposeStr != null && !CommonUtil.mapIsEmpty(paraMap, preposeStr)) {
                    if (k > 0) {
                        preposeSb.append(",");
                    }
                    preposeSb.append(paraMap.get(preposeStr));
                    k++ ;
                }
            }
            mppInfo.setPreposeName(preposeSb.toString());

        }
        return mppList;
    }

    @Override
    public BusinessConfigDto getBusinessConfigEntity(String id) {
        BusinessConfig p = (BusinessConfig)sessionFacade.getEntity(BusinessConfig.class,id);
        BusinessConfigDto businessConfigDto = new BusinessConfigDto();
        if (!CommonUtil.isEmpty(p)) {
            try {
                businessConfigDto = (BusinessConfigDto)VDataUtil.getVDataByEntity(p,BusinessConfigDto.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            businessConfigDto = null;
        }
        return businessConfigDto;
    }

    @Override
    public String doImportExcel(Sheet sheet, String projectId, String planId, String type, List<PlanExcelSaveVo> dataTempList, Map<String, String> errorMsgMap) {
        String res = "";
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.success");
        String typeName = "项目计划";
        Plan plan = new Plan();
        if (!CommonUtil.isEmpty(projectId)) {
            plan.setProjectId(projectId);
        }
        if(!CommonUtil.isEmpty(type)) {
            if(PlanImAndExConstants.NEXT_PLAN.equals(type)) {
                Plan temp = new Plan();
                temp = this.findBusinessObjectById(Plan.class, planId);
                plan.setId(temp.getId());
                plan.setParentPlanId(temp.getParentPlanId());
            } else {
                plan.setId(planId);
            }
        }
        try{
            Map<String, Object> returnMap= getImportDataList(sheet, plan, type, dataTempList, errorMsgMap);
            res = (String)returnMap.get("res");
        }
        catch(Exception e){
            j.setMsg(e.getMessage());
            j.setSuccess(false);
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Date getEndTimeByStartAndWorkTime(String projectTimeType,
                                             Date startTime, int worktime) {
        Date planEndTime = null;
        // 工作日获取结束时间
        if (ProjectConstants.WORKDAY.equals(projectTimeType)) {
            Date date = (Date)startTime.clone();
            Boolean a = false;
            try {
                a = CalendarParser.judgeDefaultWorkingDay(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (a) {
                planEndTime = DateUtil.nextWorkDay(date, worktime - 1);
            }
            else {
                Date date2 = DateUtil.nextWorkDay(date, 1);
                planEndTime = DateUtil.nextWorkDay(date2, worktime - 1);
            }

        }
        // 公司日获取结束时间
        else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("startDate",startTime);
            params.put("days",Integer.valueOf(worktime) - 1);
            planEndTime = calendarService.getNextWorkingDay(appKey,params);
        }
        // 自然日获取结束时间
        else {
            Date date = (Date)startTime.clone();
            planEndTime = DateUtil.nextDay(date, worktime - 1);
        }
        return planEndTime;
    }

    @Override
    public void deleteDeliverablesAndResourceByTempForm(String formId) {
        // TODO Auto-generated method stub

        List<TempPlanResourceLinkInfo> resourceList = sessionFacade.findByQueryString(createDeliverablesOrResourceHql(
                "TempPlanResourceLinkInfo", formId));
        if(!CommonUtil.isEmpty(resourceList)){
            sessionFacade.deleteAllEntitie(resourceList);
        }

        List<TempPlanDeliverablesInfo> deliverablesList = sessionFacade.findByQueryString(createDeliverablesOrResourceHql(
                "TempPlanDeliverablesInfo", formId));
        if(!CommonUtil.isEmpty(deliverablesList)){
            sessionFacade.deleteAllEntitie(deliverablesList);
        }

        List<TempPlanInputs> inputList = sessionFacade.findByQueryString(createDeliverablesOrResourceHql(
                "TempPlanInputs", formId));
        if(!CommonUtil.isEmpty(inputList)){
            sessionFacade.deleteAllEntitie(inputList);
        }
    }

    @Override
    public FeignJson concernPlan(String planId, String concernCode, String userId) {
        FeignJson feignJson = new FeignJson();
        //关注代码为0，则为取消操作，需要删除数据，若为1则为关注计划，需要新增数据
        try{
            if (concernCode.equals("0")){
                String sql = "delete from PM_CONCERN_PLAN_INFO where planId='"+planId+"' and userId ='"+userId+"'";
                sessionFacade.executeSql2(sql);
            }else if (concernCode.equals("1")){
                ConcernPlanInfo info = new ConcernPlanInfo();
                info.setPlanId(planId);
                info.setUserId(userId);
                sessionFacade.save(info);
            }
        }catch (Exception e){
            feignJson.setSuccess(false);
            log.error("PlanServiceImpl#concernPlan失败", e);
        }
        return feignJson;
    }

    /**
     * 组装HQL
     *
     * @param entityName
     * @return
     * @see
     */
    private String createDeliverablesOrResourceHql(String entityName, String formId) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from " + entityName + " t");
        hql.append(" where 1 = 1");

        if (formId != null) {
            if (StringUtils.isNotEmpty(formId)) {
                hql.append(" and t.useObjectId = '" + formId + "'");
            }
        }
        return hql.toString();
    }

    @Override
    public Map<String,Object> getImportDataList(Sheet sheet, Plan plan, String type,
                                                List<PlanExcelSaveVo> dataTempList, Map<String, String> errorMsgMap) {
        Map<String,Object> returnMap = new HashMap<>();
        String appKey = ResourceUtil.getApplicationInformation().getAppKey();
        Iterator<Row> it = sheet.iterator();
        // 获取当前系统是否启用标准名称库
        String switchStr = paramSwitchService
                .getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
        boolean isStandard = false;
        if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                || NameStandardSwitchConstants.FORCEUSENAMESTANDARD
                .equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE
                .equals(switchStr)) {
            isStandard = true;
        }
        Map<String, String> standardNameMap = new HashMap<String, String>();
        Map<String, String> deliveryNameMap = new HashMap<String, String>();
        Map<String, String> planLevelMap = new HashMap<String, String>();
        if (isStandard) {
            NameStandardDto ns = new NameStandardDto();
            ns.setStopFlag(ConfigStateConstants.START);
            List<NameStandardDto> list = nameStandardService
                    .searchNameStandardsAccurate(ns);
            for (NameStandardDto n : list) {
                standardNameMap.put(n.getName(), n.getActiveCategory());
            }

            DeliveryStandard ds = new DeliveryStandard();
            ds.setStopFlag(ConfigStateConstants.START);
            List<DeliveryStandard> list2 = deliveryStandardService.searchDeliveryStandardAccurate(ds);
            for (DeliveryStandard d : list2) {
                deliveryNameMap.put(d.getName(), d.getName());
            }
        }
        BusinessConfig businessConfig = new BusinessConfig();
        businessConfig.setConfigType(ConfigTypeConstants.PLANLEVEL);
        businessConfig.setStopFlag(ConfigStateConstants.START);
        businessConfig.setAvaliable("1");
//        List<BusinessConfig> planLevelConfigs = businessConfigService.searchUseableBusinessConfigs(businessConfig);
        List<BusinessConfig> planLevelConfigs = businessConfigService.searchUseableBusinessConfigs(businessConfig);
        for (BusinessConfig confog : planLevelConfigs) {
            planLevelMap.put(confog.getName(), confog.getId());
        }
        Map<String, String> teamUsersMap = new HashMap<String, String>();
        List<TSUserDto> users = projRoleService.getUserInProject(plan.getProjectId());
        for (TSUserDto teamUser : users) {
            teamUsersMap.put(teamUser.getUserName(), teamUser.getId());
        }
        // 用于校验和预设数据的保存
        Map<String, PlanExcelVo> excelMap = new HashMap<String, PlanExcelVo>();
        // 计划序号集合
        List<String> numList = new ArrayList<String>();
        Project project = projectService.findBusinessObjectById(Project.class, plan.getProjectId());
        while (it.hasNext()) {
            Row r = it.next();
            int rowNum = r.getRowNum();
            Row row = sheet.getRow(rowNum);

            if (rowNum >= 3) {
                Cell numberCell = row.getCell(0);
                Cell parentNumberCell = row.getCell(1);
                Cell nameCell = row.getCell(2);
                Cell taskNameTypeCell = row.getCell(3);
                Cell ownerCell = row.getCell(4);
                Cell levelCell = row.getCell(5);
                //Cell worktimeCell = row.getCell(5);工期字段导入去除
                Cell milestoneCell = row.getCell(6);
                Cell startTimeCell = row.getCell(7);
                Cell endTimeCell = row.getCell(8);
                Cell preposeNumberCell = row.getCell(9);
                Cell deliverNameCell = row.getCell(10);
                Cell remarkCell = row.getCell(11);
                String number = POIExcelUtil.getCellValue(numberCell).trim();
                String parentNumber = POIExcelUtil.getCellValue(parentNumberCell).trim();
                List<ActivityTypeManage> activityTypeManages = activityTypeManageEntityService.queryActivityTypeManageByName(POIExcelUtil.getCellValue(taskNameTypeCell).trim());
                String taskNameType = activityTypeManages.get(0).getId();
                String name = POIExcelUtil.getCellValue(nameCell).trim();
                String owner = POIExcelUtil.getCellValue(ownerCell).trim();
                String level = POIExcelUtil.getCellValue(levelCell).trim();
//                String worktime = POIExcelUtil.getCellValue(worktimeCell).trim();
                String milestone = POIExcelUtil.getCellValue(milestoneCell).trim();
                String startTime = getCellValue(startTimeCell).trim();
                String endTime = getCellValue(endTimeCell).trim();
                String preposeNumbers = POIExcelUtil.getCellValue(preposeNumberCell).trim();
                preposeNumbers = preposeNumbers.replace("，", ",");
                String deliverName = POIExcelUtil.getCellValue(deliverNameCell).trim();
                deliverName = deliverName.replace("，", ",");
                String remark = POIExcelUtil.getCellValue(remarkCell).trim();
                // 时间
                Date planStartTime = null;
                Date planEndTime = null;
                if(!CommonUtil.isEmpty(startTime)) {
                    planStartTime = stringtoDate(startTime, "yyyy/MM/dd", rowNum, errorMsgMap,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeFormat"));
                }
                if(!CommonUtil.isEmpty(endTime)) {
                    planEndTime = stringtoDate(endTime, "yyyy/MM/dd", rowNum, errorMsgMap,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkEndtimeFormat"));
                }

                String checkStr = number + ";" + name + ";" + owner + ";"
                        + milestone + ";" + startTime + ";"
                        + deliverName + ";" + remark + ";" + level + ";" + rowNum;
                errorMsgMap = checkDataNew(rowNum, checkStr, switchStr,
                        standardNameMap, teamUsersMap, errorMsgMap,
                        deliveryNameMap, numList, planStartTime, planEndTime, planLevelMap);
                returnMap.put("errorMsgMap", errorMsgMap);
                PlanExcelSaveVo planTemp = new PlanExcelSaveVo();
                planTemp.setPlanNumber(number);
                planTemp.setParentPlanNo(parentNumber);
                planTemp.setPlanName(name);
                planTemp.setOwnerRealName(owner);
                planTemp.setPlanLevelName(level);
//                planTemp.setWorktime(worktime);
                planTemp.setMilestoneName(milestone);
                planTemp.setPreposeNos(preposeNumbers);
                planTemp.setDeliverablesName(deliverName);
                planTemp.setRemark(remark);
                planTemp.setPlanStartTime(startTime);
                planTemp.setPlanEndTime(endTime);
                planTemp.setTaskNameType(taskNameType);

                PlanExcelVo vo = new PlanExcelVo();
                vo.setId(UUIDGenerator.generate());
                vo.setRowNum(rowNum);
                vo.setDeliverablesName(deliverName);
                vo.setParentNo(parentNumber);
                vo.setPlanStartTime(planStartTime);
                vo.setPreposeNos(preposeNumbers);
                dataTempList.add(planTemp);
                //时间赋值
                if(!CommonUtil.isEmpty(planStartTime) ) {
                    vo.setPlanEndTime(planEndTime);
                    if(CommonUtil.isEmpty(planEndTime) ) {
                        Date endtime = this.getEndTimeByStartAndWorkTime(project.getProjectTimeType(), planStartTime, 1);
                        vo.setPlanEndTime(endtime);
                    } else {
                        vo.setPlanEndTime(planEndTime);
                    }
                }
                excelMap.put(number, vo);
            }
        }
        String res = "";
        // 若模板中的业务数据为空，则提示且不进行导入操作
        if (CommonUtil.isEmpty(dataTempList)) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.common.importDataIsNull"));
        } else if (dataTempList.size() < 1) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkLeastOnePlan"));
        }
        //父计划,前置计划，时间
        this.checkData2(project, excelMap, errorMsgMap, numList, type, plan);
        if (0 == errorMsgMap.size()) {
            String taskClass = PlanConstants.PLAN_TYPE_TASK;
            // 获取当前用户的角色
            String userId = UserUtil.getInstance().getUser().getId();
            String teamId = projRoleService.getTeamIdByProjectId(plan.getProjectId());
            boolean isPmo = groupService.judgeHasLimit(appKey,ProjectRoleConstants.PMO, userId, null);
            boolean isProjectManger = groupService.judgeHasLimit(appKey,
                    ProjectRoleConstants.PROJ_MANAGER, userId, teamId);
            if (isPmo || isProjectManger) {
                taskClass = PlanConstants.PLAN_TYPE_WBS;
            }
            boolean insertFlag = false;
            // 如果下方导入时，把insertFlag变成true
            if (PlanImAndExConstants.NEXT_PLAN.equals(type)) {
                insertFlag = true;
            }
            String defaultNameType = "";
            String dictCode = "activeCategory";
            Map<String,List<TSTypeDto>> tsMap = feignSystemService.getAllTypesOfTypeGroup(ResourceUtil.getApplicationInformation().getAppKey());
            List<TSTypeDto> types = tsMap.get(dictCode);

//            List<TSTypeDto> types = TSTypegroup.allTypes.get(dictCode
//                    .toLowerCase());
            if (!CommonUtil.isEmpty(types)) {
                defaultNameType = types.get(0).getTypecode();
            }
            Map<String, Object> paraMap = new HashMap<String, Object>();
            // 项目参数名称库配置
            paraMap.put("switchStr", switchStr);
            // 计划类别
            paraMap.put("taskClass", taskClass);
            // 计划类型
            paraMap.put("defaultNameType", defaultNameType);
            // 是否启用名称库
            paraMap.put("isStandard", isStandard);
            //项目日历类型
            paraMap.put("projectTimeType", project.getProjectTimeType());
            paraMap.put("type", type);
            paraMap.put("insertFlag", insertFlag);
            Map<String,String> activityIdAndTabCbTempMap = tabCbTemplateEntityService.getTabCbTempIdAndActivityIdMap();
            res = this.savePlanExcelData(plan, paraMap, dataTempList,
                    excelMap, teamUsersMap, standardNameMap, planLevelMap,userId,activityIdAndTabCbTempMap);
        }
        returnMap.put("res", res);
        return returnMap;
    }

    @Override
    public Map<String, Object> doImportExcel(String userId,Plan plan, String type, List<Map<String, String>> map) {
        Map<String,Object> returnMap = new HashMap<>();
        Map<String, String> errorMsgMap = new HashMap<>();
        List<PlanExcelSaveVo> dataTempList = new ArrayList<>();
        List<PlanExcelSaveVo> errorDataTempList = new ArrayList<>();
        String appKey = ResourceUtil.getApplicationInformation().getAppKey();
        // 获取当前系统是否启用标准名称库
        String switchStr = paramSwitchService
                .getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
        boolean isStandard = false;
        if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                || NameStandardSwitchConstants.FORCEUSENAMESTANDARD
                .equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE
                .equals(switchStr)) {
            isStandard = true;
        }
        Map<String, String> standardNameMap = new HashMap<String, String>();
        Map<String, String> deliveryNameMap = new HashMap<String, String>();
        Map<String, String> planLevelMap = new HashMap<String, String>();
        if (isStandard) {
            NameStandardDto ns = new NameStandardDto();
            ns.setStopFlag(ConfigStateConstants.START);
            List<NameStandardDto> list = nameStandardService
                    .searchNameStandardsAccurate(ns);
            for (NameStandardDto n : list) {
                standardNameMap.put(n.getName(), n.getActiveCategory());
            }

            DeliveryStandard ds = new DeliveryStandard();
            ds.setStopFlag(ConfigStateConstants.START);
            List<DeliveryStandard> list2 = deliveryStandardService.searchDeliveryStandardAccurate(ds);
            for (DeliveryStandard d : list2) {
                deliveryNameMap.put(d.getName(), d.getName());
            }
        }
        BusinessConfig businessConfig = new BusinessConfig();
        businessConfig.setConfigType(ConfigTypeConstants.PLANLEVEL);
        businessConfig.setStopFlag(ConfigStateConstants.START);
        businessConfig.setAvaliable("1");
        List<BusinessConfig> planLevelConfigs = businessConfigService.searchUseableBusinessConfigs(businessConfig);
        for (BusinessConfig confog : planLevelConfigs) {
            planLevelMap.put(confog.getName(), confog.getId());
        }
        Map<String, String> teamUsersMap = new HashMap<String, String>();
        List<TSUserDto> users = projRoleService.getUserInProject(plan.getProjectId());
        for (TSUserDto teamUser : users) {
            teamUsersMap.put(teamUser.getUserName(), teamUser.getId());
        }
        // 用于校验和预设数据的保存
        Map<String, PlanExcelVo> excelMap = new HashMap<String, PlanExcelVo>();
        // 计划序号集合
        List<String> numList = new ArrayList<String>();
        Project project = projectService.findBusinessObjectById(Project.class, plan.getProjectId());
        int rowNum = 3;
        Map<String,ActivityTypeManage> activityTypeNameMap = activityTypeManageEntityService.getActivityTypeAndNameMap();
        for (Map<String, String> dataMap : map){
            String number = dataMap.get("number");
            String parentNumber = dataMap.get("parentNumber");
            ActivityTypeManage activityTypeManages = activityTypeNameMap.get(dataMap.get("taskNameType"));
            String taskNameType = "";
            if(!CommonUtil.isEmpty(activityTypeManages)){
                taskNameType = activityTypeManages.getId();
            }
            String name = dataMap.get("name");
            String owner = dataMap.get("owner");
            String level = dataMap.get("level");
            String milestone = dataMap.get("milestone");
            String startTime = dataMap.get("startTime");
            String endTime = dataMap.get("endTime");
            String preposeNumbers = dataMap.get("preposeNumbers");
            String deliverName =dataMap.get("deliverName");
            String remark =dataMap.get("remark");
            // 时间
            Date planStartTime = null;
            Date planEndTime = null;
            if(!CommonUtil.isEmpty(startTime)) {
                planStartTime = stringtoDate(startTime, "yyyy/MM/dd", rowNum, errorMsgMap,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeFormat"));
            }
            if(!CommonUtil.isEmpty(endTime)) {
                planEndTime = stringtoDate(endTime, "yyyy/MM/dd", rowNum, errorMsgMap,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkEndtimeFormat"));
            }

            String checkStr = number + ";" + name + ";" + owner + ";"
                    + milestone + ";" + startTime + ";"
                    + deliverName + ";" + remark + ";" + level + ";" +taskNameType+";" +rowNum;
            errorMsgMap = checkDataNew(rowNum, checkStr, switchStr,
                    standardNameMap, teamUsersMap, errorMsgMap,
                    deliveryNameMap, numList, planStartTime, planEndTime, planLevelMap);
            returnMap.put("errorMsgMap", errorMsgMap);
            PlanExcelSaveVo planTemp = new PlanExcelSaveVo();
            planTemp.setPlanNumber(number);
            planTemp.setParentPlanNo(parentNumber);
            planTemp.setPlanName(name);
            planTemp.setOwnerRealName(owner);
            planTemp.setPlanLevelName(level);
            planTemp.setMilestoneName(milestone);
            planTemp.setPreposeNos(preposeNumbers);
            planTemp.setDeliverablesName(deliverName);
            planTemp.setRemark(remark);
            planTemp.setPlanStartTime(startTime);
            planTemp.setPlanEndTime(endTime);
            planTemp.setTaskNameType(taskNameType);

            PlanExcelSaveVo errorTemp = new PlanExcelSaveVo();
            errorTemp.setPlanNumber(number);
            errorTemp.setParentPlanNo(parentNumber);
            errorTemp.setPlanName(name);
            errorTemp.setOwnerRealName(owner);
            errorTemp.setPlanLevelName(level);
            errorTemp.setMilestoneName(milestone);
            errorTemp.setPreposeNos(preposeNumbers);
            errorTemp.setDeliverablesName(deliverName);
            errorTemp.setRemark(remark);
            errorTemp.setPlanStartTime(startTime);
            errorTemp.setPlanEndTime(endTime);
            errorTemp.setTaskNameType(dataMap.get("taskNameType"));

            PlanExcelVo vo = new PlanExcelVo();
            vo.setId(UUIDGenerator.generate());
            vo.setRowNum(rowNum);
            vo.setDeliverablesName(deliverName);
            vo.setParentNo(parentNumber);
            vo.setPlanStartTime(planStartTime);
            vo.setPreposeNos(preposeNumbers);
            dataTempList.add(planTemp);
            errorDataTempList.add(errorTemp);
            //时间赋值
            if(!CommonUtil.isEmpty(planStartTime) ) {
                vo.setPlanEndTime(planEndTime);
                if(CommonUtil.isEmpty(planEndTime) ) {
                    Date endtime = this.getEndTimeByStartAndWorkTime(project.getProjectTimeType(), planStartTime, 1);
                    vo.setPlanEndTime(endtime);
                } else {
                    vo.setPlanEndTime(planEndTime);
                }
            }
            excelMap.put(number, vo);
            rowNum++;
        }
        String res = "";
        // 若模板中的业务数据为空，则提示且不进行导入操作
        if (CommonUtil.isEmpty(dataTempList)) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.common.importDataIsNull"));
        } else if (dataTempList.size() < 1) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkLeastOnePlan"));
        }
        //父计划,前置计划，时间
        this.checkData2(project, excelMap, errorMsgMap, numList, type, plan);
        if (0 == errorMsgMap.size()) {
            String taskClass = PlanConstants.PLAN_TYPE_TASK;
            String teamId = projRoleService.getTeamIdByProjectId(plan.getProjectId());
            boolean isPmo = groupService.judgeHasLimit(appKey,ProjectRoleConstants.PMO, userId, null);
            boolean isProjectManger = groupService.judgeHasLimit(appKey,
                    ProjectRoleConstants.PROJ_MANAGER, userId, teamId);
            if (isPmo || isProjectManger) {
                taskClass = PlanConstants.PLAN_TYPE_WBS;
            }
            boolean insertFlag = false;
            // 如果下方导入时，把insertFlag变成true
            if (PlanImAndExConstants.NEXT_PLAN.equals(type)) {
                insertFlag = true;
            }
            String defaultNameType = "";
            String dictCode = "activeCategory";
            Map<String,List<TSTypeDto>> tsMap = feignSystemService.getAllTypesOfTypeGroup(ResourceUtil.getApplicationInformation().getAppKey());
            List<TSTypeDto> types = tsMap.get(dictCode);

            if (!CommonUtil.isEmpty(types)) {
                defaultNameType = types.get(0).getTypecode();
            }
            Map<String, Object> paraMap = new HashMap<String, Object>();
            // 项目参数名称库配置
            paraMap.put("switchStr", switchStr);
            // 计划类别
            paraMap.put("taskClass", taskClass);
            // 计划类型
            paraMap.put("defaultNameType", defaultNameType);
            // 是否启用名称库
            paraMap.put("isStandard", isStandard);
            //项目日历类型
            paraMap.put("projectTimeType", project.getProjectTimeType());
            paraMap.put("type", type);
            paraMap.put("insertFlag", insertFlag);
            Map<String,String> activityIdAndTabCbTempMap = tabCbTemplateEntityService.getTabCbTempIdAndActivityIdMap();
            res = this.savePlanExcelData(plan, paraMap, dataTempList,
                    excelMap, teamUsersMap, standardNameMap, planLevelMap,userId,activityIdAndTabCbTempMap);
        }
        returnMap.put("res", res);
        returnMap.put("dataTempList",errorDataTempList);
        return returnMap;
    }

    @Override
    public void checkData2(Project project, Map<String, PlanExcelVo> excelMap,
                           Map<String, String> errorMsgMap, List<String> numList, String type, Plan plan) {
        //long projectWorktime = projectService.getProjectWorkTime(project.getId());
        Plan plan1 = null;
        Plan plan2 = null;
        //当前计划
        if(!CommonUtil.isEmpty(type)) {
            if(PlanImAndExConstants.CHILDPLAN.equals(type)) {
                plan1 = findBusinessObjectById(Plan.class, plan.getId());
            } else {
                if(!CommonUtil.isEmpty(plan.getParentPlanId())) {
                    plan2 = findBusinessObjectById(Plan.class, plan.getParentPlanId());
                }
            }
        }
        //互为前置
        List<String> preposeList = new ArrayList<String>();
        //互为父子
        List<String> parentChildList = new ArrayList<String>();
        for(String number : excelMap.keySet()) {
            String parentNo = excelMap.get(number).getParentNo();
            String preposeNos = excelMap.get(number).getPreposeNos();
            //int worktime = excelMap.get(number).getWorktime();
            Date starttime = excelMap.get(number).getPlanStartTime();
            Date endtime = excelMap.get(number).getPlanEndTime();
            //校验依赖关系
            if(!CommonUtil.isEmpty(parentNo) && (number.equals(parentNo) || !numList.contains(parentNo))) {
                POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkParentPlanNumber")+";", errorMsgMap);
            } else {
                if(!CommonUtil.isEmpty(number) && !CommonUtil.isEmpty(parentNo)) {
                    if(!parentChildList.contains(number) && !CommonUtil.isEmpty(excelMap.get(parentNo))) {
                        if(number.equals(excelMap.get(parentNo).getParentNo())) {
                            parentChildList.add(parentNo);
                            parentChildList.add(number);
                            POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                                    "不能与序号为"+ parentNo + "的计划互为父子计划;", errorMsgMap);
                        }
                    }
                }
            }
            //前置计划可能有多个,取最大的计划完成时间比较
            Date maxPreposeEndtime = null;
            if(!preposeList.contains(number) && !CommonUtil.isEmpty(preposeNos)) {
                String[] preposeNoArr = preposeNos.split(",");
                for(String preposeNo : preposeNoArr) {
                    if(!CommonUtil.isEmpty(preposeNo) && number.equals(preposeNo) || !numList.contains(preposeNo)) {
                        POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPreposePlanNumber")+";", errorMsgMap);
                        break;
                    } else {
                        if(!CommonUtil.isEmpty(excelMap.get(preposeNo).getPreposeNos())
                                && excelMap.get(preposeNo).getPreposeNos().indexOf(number) != -1) {
                            preposeList.add(preposeNo);
                            preposeList.add(number);
                            POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                                    "不能与序号为"+ preposeNo + "的计划互为前后置;", errorMsgMap);
                            break;
                        }
                        if(!CommonUtil.isEmpty(maxPreposeEndtime)) {
                            if(!CommonUtil.isEmpty(excelMap.get(preposeNo).getPlanEndTime()) &&
                                    maxPreposeEndtime.compareTo(excelMap.get(preposeNo).getPlanEndTime()) < 0) {
                                maxPreposeEndtime = excelMap.get(preposeNo).getPlanEndTime();
                            }
                        } else {
                            maxPreposeEndtime = excelMap.get(preposeNo).getPlanEndTime();
                        }
                    }
                }
            }
            if(!CommonUtil.isEmpty(maxPreposeEndtime) && starttime.compareTo(maxPreposeEndtime) <= 0) {
                POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeThree")+ ";", errorMsgMap);
            }

            if(CommonUtil.isEmpty(parentNo)) {
                if(CommonUtil.isEmpty(type)) {
                    //开始
                    errorMsgMap = compareDateRange(starttime, project.getStartProjectTime(), project.getEndProjectTime(),
                            errorMsgMap, excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeTwo")+ ";");
                    //结束
                    errorMsgMap = compareDateRange(endtime, project.getStartProjectTime(), project.getEndProjectTime(),
                            errorMsgMap, excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkEndtimeTwo")+ ";");
                    /*if(worktime > (int)projectWorktime) {
                        POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.project.plan.importExcel.checkWorktimeTwo")+ ";", errorMsgMap);
                    } */
                } else if(PlanImAndExConstants.NEXT_PLAN.equals(type)) {
                    if(!CommonUtil.isEmpty(plan2)) {
                        //开始
                        errorMsgMap = compareDateRange(starttime, plan2.getPlanStartTime(), plan2.getPlanEndTime(),
                                errorMsgMap, excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeTwo")+ ";");
                        //结束
                        errorMsgMap = compareDateRange(endtime, plan2.getPlanStartTime(), plan2.getPlanEndTime(),
                                errorMsgMap, excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkEndtimeTwo")+ ";");
                        /*if(worktime > Integer.valueOf(plan2.getWorkTime())) {
                            POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.project.plan.importExcel.checkWorktimeTwo")+ ";", errorMsgMap);
                        }  */
                    } else {
                        //开始
                        errorMsgMap = compareDateRange(starttime, project.getStartProjectTime(), project.getEndProjectTime(),
                                errorMsgMap, excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeTwo")+ ";");
                        //结束
                        errorMsgMap = compareDateRange(endtime, project.getStartProjectTime(), project.getEndProjectTime(),
                                errorMsgMap, excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkEndtimeTwo")+ ";");
                        /*if(worktime > (int)projectWorktime) {
                            POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                                I18nUtil.getValue("com.glaway.ids.project.plan.importExcel.checkWorktimeTwo")+ ";", errorMsgMap);
                        }*/
                    }

                } else if(PlanImAndExConstants.CHILDPLAN.equals(type)) {
                    //开始
                    errorMsgMap = compareDateRange(starttime, plan1.getPlanStartTime(), plan1.getPlanEndTime(),
                            errorMsgMap, excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeTwo")+ ";");
                    //结束
                    errorMsgMap = compareDateRange(endtime, plan1.getPlanStartTime(), plan1.getPlanEndTime(),
                            errorMsgMap, excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkEndtimeTwo")+ ";");
                    /*if(worktime > Integer.valueOf(plan1.getWorkTime())) {
                        POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.project.plan.importExcel.checkWorktimeTwo")+ ";", errorMsgMap);
                    }  */
                }

            } else {
                if(!CommonUtil.isEmpty(excelMap.get(parentNo))) {
                    errorMsgMap = compareDateRange(starttime, excelMap.get(parentNo).getPlanStartTime(),
                            excelMap.get(parentNo).getPlanEndTime(), errorMsgMap, excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeTwo")+ ";");
                    errorMsgMap = compareDate(endtime, excelMap.get(parentNo).getPlanEndTime(), errorMsgMap,
                            excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkEndtimeTwo")+ ";");
                    /*if(worktime > excelMap.get(parentNo).getWorktime()) {
                        POIExcelUtil.addErrorMsg(excelMap.get(number).getRowNum(),
                            I18nUtil.getValue("com.glaway.ids.project.plan.importExcel.checkWorktimeTwo")+ ";", errorMsgMap);
                    }*/
                }
            }
        }
    }

    /**
     * 比较时间大小，并返回errorMsgMap
     * @param
     * @param
     * @param rowNum
     * @param msg
     * @param errorMsgMap
     */
    private Map<String, String> compareDate(Date date1, Date date2, Map<String, String> errorMsgMap,
                                            int rowNum, String msg) {
        if(!CommonUtil.isEmpty(date1) && !CommonUtil.isEmpty(date2)) {
            if(date1.compareTo(date2) > 0) {
                //如果d1>d2,返回错误信息
                POIExcelUtil.addErrorMsg(rowNum, msg, errorMsgMap);
            }
        }
        return errorMsgMap;
    }

    private Map<String, String> compareDateRange(Date date, Date dateRange1, Date dateRange2,
                                                 Map<String, String> errorMsgMap, int rowNum, String msg) {
        if(!CommonUtil.isEmpty(date)) {
            if(date.compareTo(dateRange1) < 0 || date.compareTo(dateRange2) > 0) {
                //如果date不在range1~range2范围，返回错误信息
                POIExcelUtil.addErrorMsg(rowNum, msg, errorMsgMap);
            }
        }

        return errorMsgMap;

    }

    @Override
    public String savePlanExcelData(Plan plan, Map<String, Object> paraMap, List<PlanExcelSaveVo> list,
                                    Map<String, PlanExcelVo> excelMap, Map<String, String> teamUsersMap,
                                    Map<String, String> standardNameMap, Map<String, String> planLevelMap,String userId,Map<String,String> activityIdAndTabCbTempMap) {
        boolean needDelete = false;
        String type = (String) paraMap.get("type");
        String projectTimeType = (String) paraMap.get("projectTimeType");
        String switchStr = (String) paraMap.get("switchStr");
        boolean insertFlag = (boolean) paraMap.get("insertFlag");
        if(CommonUtil.isEmpty(type)) {
            Plan planTemp = new Plan();
            planTemp.setProjectId(plan.getProjectId());
            List<Plan> tempList = queryPlanList(planTemp, 1, 10, false);
            //如果全量导入且表中有数据,则需要删除
            if (!CommonUtil.isEmpty(tempList)) {
                needDelete = true;
            }
        }
        Integer storeyNo = 0;
        int maxPlanNumber = 0;
        int maxStoreyNo = 0;
        if (!needDelete) {
            maxPlanNumber = getMaxPlanNumber(plan);
            maxStoreyNo = getMaxStoreyNo(plan);
        }

        List<Plan> insertPlans = new ArrayList<Plan>();
        List<PlanLog> insertPlanLogs = new ArrayList<PlanLog>();
        List<Inputs> insertInputs = new ArrayList<Inputs>();
        List<DeliverablesInfo> insertDeliverablesInfos = new ArrayList<DeliverablesInfo>();
        List<PreposePlan> insertPreposePlans = new ArrayList<PreposePlan>();

        Plan p = new Plan();
        initBusinessObject(p);
        String bizCurrent = p.getBizCurrent();
        String bizVersion = p.getBizVersion();
        LifeCyclePolicy policy = p.getPolicy();

        DeliverablesInfo d = new DeliverablesInfo();
        deliverablesInfoService.initBusinessObject(d);
        String dBizCurrent = d.getBizCurrent();
        String dBizVersion = d.getBizVersion();
        LifeCyclePolicy dPolicy = p.getPolicy();

        //记录 计划id+交付项名称:输出ID
        Map<String, String> deliverMap = new HashMap<String, String>();
        Map<String, String> preposeInput = new HashMap<String, String>();
        long time1 = System.currentTimeMillis();
        Map<String, String> nameDeliverablesMap = nameStandardService.getNameDeliverysMap();
        for(PlanExcelSaveVo e : list) {
            String number = e.getPlanNumber();
            PlanExcelVo vo = excelMap.get(number);
            Plan detail = new Plan();
            CommonUtil.glObjectSet(detail);
            detail.setId(vo.getId());
            detail.setProjectId(plan.getProjectId());
            //detail.setPlanNumber(number);
            detail.setPlanName(e.getPlanName());
            detail.setPlanStartTime(DateUtil.stringtoDate(e.getPlanStartTime(), "yyyy/MM/dd"));
            detail.setRemark(e.getRemark());
            // 给导入计划的计划类别,计划类型设值
            detail.setTaskType((String)paraMap.get("taskClass"));
            detail.setTaskNameType(e.getTaskNameType());
            //    List<TabCombinationTemplate> tabList = tabCbTemplateEntityService.findTabCbTemplatesByActivityId(e.getTaskNameType());
            if(!CommonUtil.isEmpty(activityIdAndTabCbTempMap.get(e.getTaskNameType()))){
            }
            detail.setTabCbTemplateId(activityIdAndTabCbTempMap.get(e.getTaskNameType()));
            boolean isStandard = (boolean) paraMap.get("isStandard");
            if (isStandard) {
                if (!CommonUtil.isEmpty(standardNameMap.get(detail.getPlanName()))) {
                    detail.setTaskNameType(standardNameMap.get(detail.getPlanName()));
                }
            }
            detail.setBizCurrent(bizCurrent);
            detail.setBizVersion(bizVersion);
            detail.setPolicy(policy);
            detail.setBizId(UUID.randomUUID().toString());
            String[] ownerNames = e.getOwnerRealName().split("-");
            detail.setOwner(teamUsersMap.get(ownerNames[1]));
            if(!CommonUtil.isEmpty(e.getParentPlanNo())) {
                detail.setParentPlanId(excelMap.get(e.getParentPlanNo().toString()).getId());
            } else {
                if(PlanImAndExConstants.NEXT_PLAN.equals(type)) {
                    detail.setParentPlanId(plan.getParentPlanId());
                } else if(PlanImAndExConstants.CHILDPLAN.equals(type)) {
                    detail.setParentPlanId(plan.getId());
                }
            }
            if(!CommonUtil.isEmpty(e.getPlanLevelName())) {
                detail.setPlanLevel(planLevelMap.get(e.getPlanLevelName()));
            }
            if(PlanConstants.PLAN_MILESTONE_TRUE.equals(e.getMilestoneName())) {
                detail.setMilestone("true");
            } else {
                detail.setMilestone("false");
            }
            if(!CommonUtil.isEmpty(e.getPlanEndTime())) {
                detail.setPlanEndTime(DateUtil.stringtoDate(e.getPlanEndTime(), "yyyy/MM/dd"));
                long worktime = getWorkTimeByStartAndEnd(projectTimeType, detail.getPlanStartTime(), detail.getPlanEndTime());
                detail.setWorkTime(String.valueOf(worktime));
                if(worktime == 0 && "false".equals(detail.getMilestone())) {
                    detail.setWorkTime("1");
                }
            } else {
                if(CommonUtil.isEmpty(e.getWorktime())) {
                    Date endtime = getEndTimeByStartAndWorkTime(projectTimeType, detail.getPlanStartTime(), 1);
                    detail.setPlanEndTime(endtime);
                    detail.setWorkTime("1");
                } else {
                    Date endTime2 = getEndTimeByStartAndWorkTime(projectTimeType, detail.getPlanStartTime(),
                            Integer.valueOf(e.getWorktime()));
                    detail.setPlanEndTime(endTime2);
                    detail.setWorkTime(e.getWorktime());
                }
            }

            // 如果导入模板时计划节点需要重新排序
            // 每次都是最大的planNumber+1
            detail.setPlanNumber(maxPlanNumber + 1);
            maxPlanNumber++ ;

            detail.setStoreyNo(maxStoreyNo + 1);
            maxStoreyNo++ ;

            // 如果是下方计划导入并且计划的上级与上方的上级相同，需要记录下最终的序号
            if(insertFlag){
                if(CommonUtil.isEmpty(plan.getParentPlanId())){
                    if(CommonUtil.isEmpty(detail.getParentPlanId())){
                        storeyNo = detail.getStoreyNo();
                    }
                }
                else {
                    if(plan.getParentPlanId().equals(detail.getParentPlanId())) {
                        storeyNo = detail.getStoreyNo();
                    }
                }
            }

            insertPlans.add(detail);

            PlanLog planLog = new PlanLog();
            CommonUtil.glObjectSet(planLog);
            planLog.setId(UUIDGenerator.generate());
            planLog.setPlanId(detail.getId());
            planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);
            insertPlanLogs.add(planLog);
            //输出
            List<DeliverablesInfo> deliverables = deliverablesInfoService.getDeliverablesInfoByExcel(vo,
                    switchStr, dBizCurrent, dBizVersion, dPolicy, deliverMap, nameDeliverablesMap.get(detail.getPlanName()));
            if (!CommonUtil.isEmpty(deliverables)) {
                insertDeliverablesInfos.addAll(deliverables);
            }
            //前置计划
            if(!CommonUtil.isEmpty(vo.getPreposeNos())) {
                List<PreposePlan> preposePlans = preposePlanService.getPreposePlanInfoByExcel(vo, excelMap,
                        preposeInput);
                if (!CommonUtil.isEmpty(preposePlans)) {
                    insertPreposePlans.addAll(preposePlans);
                }
            }
        }
        //输入
        if(!CommonUtil.isEmpty(preposeInput)) {
            List<Inputs> inputs = inputsService.getInputsInfoByExcel(preposeInput, excelMap, deliverMap);
            if (!CommonUtil.isEmpty(inputs)) {
                insertInputs.addAll(inputs);
            }
        }
        Map<String, Object> flgMap = new HashMap<>();
        TSUserDto user = userService.getUserByUserId(userId);
        flgMap.put("user", user);
        flgMap.put("needDelete", needDelete);
        if (needDelete) {
            flgMap.put("projectId", plan.getProjectId());
        }
        if (insertFlag && StringUtils.isNotEmpty(plan.getId())) {
            Plan upPlan = (Plan) sessionFacade.getEntity(Plan.class, plan.getId());
            Integer upStoreyNo = upPlan.getStoreyNo();
            flgMap.put("upPlanId", plan.getId());
            flgMap.put("upPlan", upPlan);
            flgMap.put("upStoreyNo", upStoreyNo);
            flgMap.put("storeyNo", storeyNo);
        }
        batchSaveDatas(flgMap, insertPlans, insertPlanLogs, insertInputs,
                insertDeliverablesInfos, insertPreposePlans,new ArrayList<Inputs>());
        return list.size() + "";
    }

    @Override
    public long getWorkTimeByStartAndEnd(String projectTimeType,
                                         Date startTime, Date endTime) {
        long workTime = 1;
        if(!CommonUtil.isEmpty(endTime)) {
            if (ProjectConstants.WORKDAY.equals(projectTimeType)) {
                workTime = TimeUtil.getWorkDayNumber(startTime, endTime);
            }
            else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("startDate",startTime);
                params.put("endDate",endTime);
                workTime = calendarService.getAllWorkingDay(appKey,params).size();
            }
            else {
                workTime = DateUtil.dayDiff(startTime, endTime) + 1;
            }
        }
        return workTime;
    }

    /**
     * 获取单元格值
     *
     * @param cell
     * @return
     * @see
     */
    private String getCellValue(Cell cell) {
        String value = "";
        if (cell != null) {// 判断Cell格式
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue().trim();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        if (date != null) {
                            value = new SimpleDateFormat("yyyy/MM/dd").format(date);
                        }
                        else {
                            value = "";
                        }
                    }
                    else {
                        value = new DecimalFormat("0").format(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    value = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = (cell.getBooleanCellValue() ? "Y" : "N");
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    value = cell.getStringCellValue().trim();
                    break;
                default:
                    value = "";
            }
        }
        return value;
    }

    //计划导入数据校验：
    private Map<String, String> checkDataNew(int rowNum, String strForBc, String switchStr,
                                             Map<String, String> standardNameMap, Map<String, String> teamUsersMap,
                                             Map<String, String> errorMsgMap, Map<String, String> deliveryNameMap,
                                             List<String> numList,Date planStartTime, Date planEndTime, Map<String, String> planLevelMap) {
        String[] data = strForBc.split(";");
        if(!CommonUtil.isEmpty(data))
        {
            String number = data[0];
            String name = data[1];
            String owner = data[2];
            String milestone = data[3];
            String starttime = data[4];
//                      String worktime = data[5];
            String deliverName = data[5];
            String remark = data[6];
            String level = data[7];
            String taskNameType = data[8];
            if(CommonUtil.isEmpty(number)) {
                POIExcelUtil.addErrorMsg(rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberRequired")+ ";", errorMsgMap);
            } else {
                Pattern pattern = Pattern.compile("[0-9]*");
                if(pattern.matcher(number).matches()) {
                    if(numList.contains(number)) {
                        POIExcelUtil.addErrorMsg(rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberTwo")+ ";", errorMsgMap);
                    } else {
                        numList.add(number);
                    }

                } else {
                    POIExcelUtil.addErrorMsg(rowNum,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkNumberOne")+ ";", errorMsgMap);
                }
            }

            if(CommonUtil.isEmpty(owner)) {
                POIExcelUtil.addErrorMsg(rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkOwnerOne")+ ";", errorMsgMap);
            } else {
                if(owner.indexOf("-") < 0) {
                    POIExcelUtil.addErrorMsg(rowNum, I18nUtil.getValue(
                            "com.glaway.ids.pm.project.plan.importExcel.checkOwnerTwo")+ ";", errorMsgMap);
                }
                else {
                    String[] userArr = owner.split("-");
                    if(CommonUtil.isEmpty(teamUsersMap.get(userArr[1]))) {
                        // addErrorMsg(row, "负责人不存在;", errorMsgMap);
                        POIExcelUtil.addErrorMsg(rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkOwnerThree")+ ";", errorMsgMap);
                    }
                }
            }
            if(!CommonUtil.isEmpty(level) && CommonUtil.isEmpty(planLevelMap.get(level))) {
                POIExcelUtil.addErrorMsg(rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanLevel")+ ";", errorMsgMap);
            }
            if(!CommonUtil.isEmpty(name)) {
                if(name.length() > 30) {
                    POIExcelUtil.addErrorMsg(rowNum,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanNameLength")+ ";", errorMsgMap);
                } else {
                    if(NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr) || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                        if(CommonUtil.isEmpty(standardNameMap.get(name))) {
                            POIExcelUtil.addErrorMsg(rowNum,
                                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanName")+ ";", errorMsgMap);
                        }
                    }
                }
            } else {
                POIExcelUtil.addErrorMsg(rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkPlanNameRequired")+ ";", errorMsgMap);
            }

            if(CommonUtil.isEmpty(milestone)) {
                POIExcelUtil.addErrorMsg(rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkMilestoneRequired")+ ";", errorMsgMap);
            } else {
                if(!PlanConstants.PLAN_MILESTONE_TRUE.equals(milestone) && !PlanConstants.PLAN_MILESTONE_FALSE.equals(milestone)) {
                    POIExcelUtil.addErrorMsg(rowNum,
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkMilestone")+ ";", errorMsgMap);
                }
            }
            if(CommonUtil.isEmpty(starttime)) {
                POIExcelUtil.addErrorMsg(rowNum,
                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkStarttimeOne")+ ";", errorMsgMap);
            } else {
                if(!CommonUtil.isEmpty(planEndTime) && !CommonUtil.isEmpty(planStartTime)) {
                    if(planStartTime.compareTo(planEndTime) > 0) {
                        POIExcelUtil.addErrorMsg(rowNum,
                                I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkEndtimeOne")+ ";", errorMsgMap);
                    }
                }
            }
            if(CommonUtil.isEmpty(taskNameType)){
                POIExcelUtil.addErrorMsg(rowNum,
                        "计划类型不能为空;", errorMsgMap);
            }
            if(!CommonUtil.isEmpty(deliverName)) {
                if(NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                    String[] deliverNames = deliverName.split(",");
                    for (String dn : deliverNames)
                    {
                        if (!CommonUtil.isEmpty(deliveryNameMap.get(dn)))
                        {
                            if (deliveryNameMap.get(dn).length() > 30)
                            {
                                POIExcelUtil.addErrorMsg(rowNum,I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkDeliverTwo")+ ";", errorMsgMap);
                            }
                        }
                    }
                } else {
                    if(!CommonUtil.isEmpty(switchStr)) {
                        String[] deliverNames = deliverName.split(",");
                        for(String dn : deliverNames) {
                            if(CommonUtil.isEmpty(deliveryNameMap.get(dn))) {
                                POIExcelUtil.addErrorMsg(rowNum,
                                        "【"+dn+"】"+I18nUtil.getValue("com.glaway.ids.pm.project.plan.importExcel.checkDeliver")+ ";", errorMsgMap);
                            }
                        }
                    }
                }
            }

            JudgeRangeParam remarkParam = new JudgeRangeParam();
            remarkParam.setKey(remark);
            remarkParam.setMinLength(0);
            remarkParam.setMaxLength(200);
            remarkParam.setRow(rowNum);
            remarkParam.setValue("备注说明");
            POIExcelUtil.doJudgeRange(remarkParam, errorMsgMap);
        }
        return errorMsgMap;
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     *
     * @param dateStr
     * @return
     */
    private Date stringtoDate(String dateStr, String format, int rowNum, Map<String, String> errorMsgMap,
                              String message) {
        Date date = null;
        if (dateStr != null && !"".equals(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                date = sdf.parse(dateStr);
            }
            catch (ParseException e) {
                POIExcelUtil.addErrorMsg(rowNum, message, errorMsgMap);
            }
        }
        return date;
    }

    /**
     * Description: <br> 组装MPP数据<br>
     *
     * @param
     * @param deliverablesName
     * @param i
     * @return
     * @see
     */
    private MppInfo getMppInfo(Plan plan, String deliverablesName, int i) {
        MppInfo mppInfo = new MppInfo();
        mppInfo.setDocumentName(deliverablesName);
        mppInfo.setId(i);
        mppInfo.setDuration(plan.getWorkTime());
        mppInfo.setName(plan.getPlanName());
        mppInfo.setParentId(plan.getParentPlanId());
        mppInfo.setStartTime(plan.getPlanStartTime());
        mppInfo.setEndTime(plan.getPlanEndTime());
        String taskNameType = plan.getTaskNameType();
        ActivityTypeManage activityTypeManage = activityTypeManageEntityService.queryActivityTypeManageById(taskNameType);
        if (activityTypeManage!=null){
            mppInfo.setTaskNameType(activityTypeManage.getName());
        }else{
            mppInfo.setTaskNameType("");
        }
        List<PreposePlan> preposePlanList = preposePlanService.getPreposePlansByPlanId(plan);
        // 把数据组成MPP的数据
        StringBuffer preposeSb = new StringBuffer();
        int k = 0;
        for (PreposePlan preposePlans : preposePlanList) {
            if (k > 0) {
                preposeSb.append(",");
            }
            preposeSb.append(preposePlans.getPreposePlanId());
            k++ ;
        }
        mppInfo.setPreposeName(preposeSb.toString());

        // 如果等级不为空，需要获得等级的名称
        if (StringUtils.isNotEmpty(plan.getPlanLevel())) {
            BusinessConfig businessConfig = (BusinessConfig) sessionFacade.getEntity(
                    BusinessConfig.class, plan.getPlanLevel());
            if (businessConfig != null && StringUtils.isNotEmpty(businessConfig.getName())) {
                mppInfo.setPlanLevel(businessConfig.getName());
            }
        }
        if ("true".equals(plan.getMilestone())) {
            mppInfo.setMilestone(true);
        }
        else {
            mppInfo.setMilestone(false);
        }
        return mppInfo;
    }

    @Override
    public FeignJson orderReviewTaskFj(Plan plan, String orderReviewTaskType, String userId) {
        log.info(I18nUtil.getValue("com.glaway.ids.pm.project.task.reviewtask")
                + I18nUtil.getValue("com.glaway.ids.pm.project.task.order"));
        Plan planOld = getEntity(plan.getId());
        // planOld.setPlanName(plan.getPlanName());
        // planOld.setPlanEndTime(plan.getPlanEndTime());
        // planOld.setOwner(plan.getOwner());
        planOld.setPlanType("评审任务");

        ReviewBaseInfoVo vo = new ReviewBaseInfoVo();
        vo.setName(planOld.getPlanName());
        vo.setTaskId(planOld.getId());
        vo.setReviewType(orderReviewTaskType);
        vo.setApplyOverTime(plan.getPlanEndTime());
        vo.setOrganizerId(planOld.getOwner());
        vo.setApplicantId(plan.getOwner());
        vo.setProjectId(planOld.getProjectId());
        vo.setProjectName(planOld.getProject().getName());
        vo.setPlanEndTime(planOld.getPlanEndTime());
        FeignJson j = new FeignJson();
        String message = "";
        Object[] arguments = new String[] {
                I18nUtil.getValue("com.glaway.ids.pm.project.task.reviewtask"),
                I18nUtil.getValue("com.glaway.ids.pm.project.task.order")};
        try {
            orderReviewTask(planOld, vo, userId);
            message = I18nUtil.getValue("com.glaway.ids.common.search.success", arguments);
        }
        catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.common.search.failure", arguments);
            log.error(message, e);
        }
        finally {
            j.setMsg(message);
            j.setObj(planOld.getPlanName() + ":" + planOld.getId());
            return j;
        }
    }


    @Override
    public List<T> getList(PlanTemplateDetailReq planTemplateDetailRep, Integer pageSize,
                           Integer pageNum) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from Plan t");
        Map<String, Object> resultMap = this.getQueryParam(hql, planTemplateDetailRep);
        String hqlStr = (String)resultMap.get("hql");
        List<T> paramList = (List<T>)resultMap.get("queryList");
        List<T> list = new ArrayList<T>();
        if (pageSize != null) {
            list = sessionFacade.pageList(hqlStr, paramList.toArray(), (pageNum - 1) * pageSize, pageSize);
        }
        else {
            list = sessionFacade.executeQuery(hqlStr, paramList.toArray());
        }
        return list;
    }

    /**
     * Description: <br> 组装查询方法<br>
     *
     * @param hql
     * @param planTemplateDetailRep
     * @return
     * @see
     */
    private Map<String, Object> getQueryParam(StringBuilder hql,
                                              PlanTemplateDetailReq planTemplateDetailRep) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        hql.append(" where t.avaliable!=0");

        List<Object> paramList = new ArrayList<Object>();
        if (planTemplateDetailRep != null) {
            if (StringUtils.isNotEmpty(planTemplateDetailRep.getPlanTemplateId())) {
                hql.append(" and t.id=?");
                paramList.add(planTemplateDetailRep.getPlanTemplateId());
            }
        }
        hql.append(" order by t.createTime desc");
        resultMap.put("hql", hql.toString());
        resultMap.put("queryList", paramList);
        return resultMap;
    }


    @Override
    public List<Plan> queryPlanListForTemplateTreegrid(Plan plan) {
        StringBuffer hqlBuffer = createStringBuffer(plan.getProjectId());
        hqlBuffer.append(" and PL.bizCurrent <> '" + PlanConstants.PLAN_INVALID + "'");
        hqlBuffer.append(" ORDER BY PL.PARENTPLANID, PL.STOREYNO, PL.CREATETIME");
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());
        return changeQueryResToPlanList(objArrayList);
    }

    @Override
    public Map<String, List<InputsDto>> getDetailInputsList(Plan plan) {
        Map<String, List<InputsDto>> map = new HashMap<String, List<InputsDto>>();
        if(!CommonUtil.isEmpty(plan.getProjectId())) {
            List<Inputs> tempInputsList = sessionFacade.findHql("from Inputs where useObjectId in(select id from Plan where projectId = ?)", plan.getProjectId());
            List<InputsDto>  inputsList = JSON.parseArray(JSON.toJSONString(tempInputsList),InputsDto.class);
            if(!CommonUtil.isEmpty(inputsList)){
                for(InputsDto in : inputsList){
                    List<InputsDto> curInputsList = new ArrayList<InputsDto>();
                    if(!CommonUtil.isEmpty(in.getUseObjectId())){
                        curInputsList.add(in);
                        if(CommonUtil.isEmpty(map.get(in.getUseObjectId()))){
                            map.put(in.getUseObjectId(), curInputsList);
                        }else{
                            List<InputsDto> curMap = map.get(in.getUseObjectId());
                            curMap.add(in);
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, List<Inputs>> getDetailInputs(Plan plan) {
        Map<String, List<Inputs>> map = new HashMap<String, List<Inputs>>();
        if(!CommonUtil.isEmpty(plan.getProjectId())) {
            List<Inputs> tempInputsList = sessionFacade.findHql("from Inputs where useObjectId in(select id from Plan where projectId = ?)", plan.getProjectId());
            if(!CommonUtil.isEmpty(tempInputsList)){
                for(Inputs in : tempInputsList){
                    List<Inputs> curInputsList = new ArrayList<Inputs>();
                    if(!CommonUtil.isEmpty(in.getUseObjectId())){
                        curInputsList.add(in);
                        if(CommonUtil.isEmpty(map.get(in.getUseObjectId()))){
                            map.put(in.getUseObjectId(), curInputsList);
                        }else{
                            List<Inputs> curMap = map.get(in.getUseObjectId());
                            curMap.add(in);
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, List<DeliverablesInfoDto>> getDeliverableListMap() {
        Map<String, List<DeliverablesInfoDto>> map = new HashMap<String, List<DeliverablesInfoDto>>();
        //List<DeliverablesInfo> tempDeliList = sessionFacade.findHql("from DeliverablesInfo where useObjectType = ?", "PLAN");
        List<DeliverablesInfo> tempDeliList = deliverablesInfoService.getDeliverablesByObjTypeAndId("PLAN","");
        List<DeliverablesInfoDto>  deliList = CodeUtils.JsonListToList(tempDeliList,DeliverablesInfoDto.class);
        if(!CommonUtil.isEmpty(deliList)){
            for(DeliverablesInfoDto deli : deliList){
                List<DeliverablesInfoDto> curDeliList = new ArrayList<DeliverablesInfoDto>();
                if(!CommonUtil.isEmpty(deli) && !CommonUtil.isEmpty(deli.getUseObjectId())){
                    curDeliList.add(deli);
                    if(CommonUtil.isEmpty(map.get(deli.getUseObjectId()))){
                        map.put(deli.getUseObjectId(), curDeliList);
                    }else{
                        List<DeliverablesInfoDto> curMap = map.get(deli.getUseObjectId());
                        curMap.add(deli);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public List<Plan> getPlanAllChildrenByProjectId(String projectId) {
        List<Plan> list = new ArrayList<Plan>();
        StringBuilder hql = new StringBuilder("");
        hql.append("from Plan t");
        hql.append(" where t.avaliable!=0 and t.parentPlanId is null and t.projectId = '"
                + projectId + "'");

        List<Plan> planList = sessionFacade.findByQueryString(hql.toString());
        for (Plan plan : planList) {
            list.addAll(getPlanAllChildren(plan));
        }
        return list;
    }

    @Override
    public void savePlanByPlanTemplateId(PlanTemplateDetailReq planTemplateDetailReq, String planId, String type, String userId, String orgId) throws GWException {
        if (StringUtils.isEmpty(planTemplateDetailReq.getProjectNumber())) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.savePlanImportInSerOne"));
        }
        Project project = projectService.findBusinessObjectById(Project.class,
                planTemplateDetailReq.getProjectNumber());
        if (project == null) {
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.savePlanImportInSerTwo"));
        }
        TSUserDto user = userService.getUserByUserId(userId);
        String taskType = PlanConstants.PLAN_TYPE_TASK;
        // 获取当前用户的角色
        String teamId = projRoleService.getTeamIdByProjectId(planTemplateDetailReq.getProjectNumber());
        boolean isProjectManger = groupService.judgeHasLimit(appKey,ProjectRoleConstants.PROJ_MANAGER,
                userId, teamId);

        Plan upPlan = new Plan();
        // 如果上方计划不为空，需要获得上级的计划编号
        if (StringUtils.isNotEmpty(planTemplateDetailReq.getUpPlanId())) {
            upPlan = (Plan)sessionFacade.getEntity(Plan.class, planTemplateDetailReq.getUpPlanId());
            planTemplateDetailReq.setPlanId(upPlan.getParentPlanId());
            // 获取上方计划的父级计划的计划类别
            if (StringUtils.isNotEmpty(upPlan.getParentPlanId())) {
                Plan selectedPlan = (Plan)sessionFacade.getEntity(Plan.class, upPlan.getParentPlanId());
                if (selectedPlan != null && StringUtils.isNotEmpty(selectedPlan.getParentPlanId())) {
                    Plan parent =(Plan)sessionFacade.getEntity(Plan.class, selectedPlan.getParentPlanId());
                    if (PlanConstants.PLAN_TYPE_WBS.equals(parent.getTaskType())
                            && (isProjectManger)) {
                        taskType = PlanConstants.PLAN_TYPE_WBS;
                    }
                }
                else {
                    if (isProjectManger) {
                        taskType = PlanConstants.PLAN_TYPE_WBS;
                    }
                }
            }
            else {
                if (isProjectManger) {
                    taskType = PlanConstants.PLAN_TYPE_WBS;
                }
            }
        }
        else if (StringUtils.isNotEmpty(planTemplateDetailReq.getPlanId())) {
            Plan selectedPlan = (Plan)sessionFacade.getEntity(Plan.class, planTemplateDetailReq.getPlanId());
            if (selectedPlan != null) {
                if (PlanConstants.PLAN_TYPE_WBS.equals(selectedPlan.getTaskType())
                        && (isProjectManger)) {
                    taskType = PlanConstants.PLAN_TYPE_WBS;
                }
            }
            else {
                if (isProjectManger) {
                    taskType = PlanConstants.PLAN_TYPE_WBS;
                }
            }
        }
        else {
            if (isProjectManger) {
                taskType = PlanConstants.PLAN_TYPE_WBS;
            }
        }

        // 获取当前系统是否启用标准名称库
        Map<String, String> standardNameMap = new HashMap<String, String>();
        boolean isStandard = false;
        String switchStr = paramSwitchService.getSwitch(SwitchConstants.NAMESTANDARDSWITCH);
        if (NameStandardSwitchConstants.USENAMESTANDARDLIB.equals(switchStr)
                || NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
            isStandard = true;
        }

        if (isStandard) {
            NameStandardDto ns = new NameStandardDto();
            ns.setStopFlag(ConfigStateConstants.START);
            List<NameStandardDto> list = nameStandardService.searchNameStandardsAccurate(ns);
            for (NameStandardDto n : list) {
                standardNameMap.put(n.getName(), n.getActiveCategory());
            }
        }

        String projectTimeType = project.getProjectTimeType();
        Date planStartTime = project.getStartProjectTime();

        // 如果有上级时，需要用上级的时间获得开始时间
        boolean needDelete = false;
        if (StringUtils.isNotEmpty(planTemplateDetailReq.getPlanId())) {
            if (StringUtils.isNotEmpty(planTemplateDetailReq.getUpPlanId())) {
                planStartTime = upPlan.getPlanStartTime();
            }
            else {
                Plan planDb = (Plan)sessionFacade.getEntity(Plan.class, planTemplateDetailReq.getPlanId());
                planStartTime = planDb.getPlanStartTime();
            }
        }
        else { // 没有上级，上方计划ID为空时,不是插入时，删除计划数据
            if (StringUtils.isEmpty(planTemplateDetailReq.getUpPlanId())) {
                // 拟制中的计划，并且不在流程中的计划都清空,否则不做动作
                long count = judgePlanStatus(project);
                if (count != 0L) {
                    throw new GWException(
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.savePlanImportInSerThree"));
                }
                needDelete = true;
                // 将删除处理移到最后的保存之中使用sql手动控制事务 20160902
                // deletePhysicsPlansByProjectId(planTemplateDetailReq.getProjectNumber());
            }
        }

        // 如果上方计划不为空，需要记录同级的计划列表
        Integer upStoreyNo = 0;
        if (StringUtils.isNotEmpty(planTemplateDetailReq.getUpPlanId())) {
            upStoreyNo = upPlan.getStoreyNo();
        }

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("planTemplateDetailReq", planTemplateDetailReq);
        paraMap.put("switchStr", switchStr); // 项目参数名称库配置
        paraMap.put("projectTimeType", projectTimeType); // 项目工期类型
        paraMap.put("planStartTime", planStartTime);
        paraMap.put("taskType", taskType);
        paraMap.put("isStandard", isStandard); // 是否启用名称库
        paraMap.put("planId", planId);
        paraMap.put("type", type);
        paraMap.put("project", project);
        paraMap.put("user", user);
        paraMap.put("needDelete", needDelete); // 是否需要删除原有计划
        paraMap.put("upStoreyNo", upStoreyNo);
        paraMap.put("upPlan", upPlan);

        List<PlanTemplateDetail> planTemplateDetailList = planTemplateDetailService.getDetailList(planTemplateDetailReq.getPlanTemplateId());

        Map<String, List<PlanTemplateDetail>> preposeMap = planTemplateDetailService.getDetailPreposes(planTemplateDetailReq.getPlanTemplateId());
        Map<String, String> detailIdDeliverablesMap = planTemplateDetailService.getPlanTemplateOrProjTemplateDetailDeliverables(planTemplateDetailReq.getPlanTemplateId(), null);
        Map<String, String> nameDeliverablesMap = nameStandardService.getNameDeliverysMap();
        saveImportTemplatePlans(planTemplateDetailList, paraMap, standardNameMap, preposeMap,
                detailIdDeliverablesMap, nameDeliverablesMap,orgId);
    }


    /**
     * 通过计划模板列表保存计划及子计划
     *
     * @param planTemplateDetailList
     * @param paraMap
     * @param standardNameMap
     * @param preposeMap
     * @return
     */
    private void saveImportTemplatePlans(List<PlanTemplateDetail> planTemplateDetailList,
                                         Map<String, Object> paraMap,
                                         Map<String, String> standardNameMap,
                                         Map<String, List<PlanTemplateDetail>> preposeMap,
                                         Map<String, String> detailIdDeliverablesMap,
                                         Map<String, String> nameDeliverablesMap,String orgId) {
        PlanTemplateDetailReq planTemplateDetailReq = (PlanTemplateDetailReq)paraMap.get("planTemplateDetailReq");
        String switchStr = (String)paraMap.get("switchStr");
        String projectTimeType = (String)paraMap.get("projectTimeType");
        Date planStartTime = (Date)paraMap.get("planStartTime");
        String taskType = (String)paraMap.get("taskType");
        boolean isStandard = (boolean)paraMap.get("isStandard");
        String planId = (String)paraMap.get("planId");
        String type = (String)paraMap.get("type");
        Project project = (Project)paraMap.get("project");
        TSUserDto user = (TSUserDto) paraMap.get("user");
        boolean needDelete = (boolean)paraMap.get("needDelete");
        Integer upStoreyNo = (Integer)paraMap.get("upStoreyNo");
        Plan upPlan = (Plan)paraMap.get("upPlan");
        // detailId、planId对照map
        Map<String, String> detailIdPlanIdMap = new HashMap<String, String>();
        Integer storeyNo = 0;
        Plan parentPlan = new Plan();
        Plan p = new Plan();
        // 修改BUG计划模板导入导入不了BUG
        if ("insert".equals(type)) {
            if (StringUtils.isNotEmpty(planTemplateDetailReq.getPlanId())) {
                p.setParentPlanId(planTemplateDetailReq.getPlanId());
                parentPlan = (Plan)sessionFacade.getEntity(Plan.class, planTemplateDetailReq.getPlanId());
            }
        }
        else {
            if (StringUtils.isNotEmpty(planId)) {
                p.setParentPlanId(planId);
                parentPlan = (Plan)sessionFacade.getEntity(Plan.class, planId);
            }
        }
        p.setProjectId(planTemplateDetailReq.getProjectNumber());
        int maxPlanNumber = 0;
        int maxStoreyNo = 0;
        if (!needDelete) {
            maxPlanNumber = getMaxPlanNumber(p);
            maxStoreyNo = getMaxStoreyNo(p);
        }

        initBusinessObject(p);
        CommonInitUtil.initGLObjectForCreate(p,user,orgId);
        String bizCurrent = p.getBizCurrent();
        String bizVersion = p.getBizVersion();
        LifeCyclePolicy policy = p.getPolicy();

        DeliverablesInfo d = new DeliverablesInfo();
        deliverablesInfoService.initBusinessObject(d);
        CommonInitUtil.initGLObjectForCreate(d,user,orgId);
        String dBizCurrent = d.getBizCurrent();
        String dBizVersion = d.getBizVersion();
        LifeCyclePolicy dPolicy = p.getPolicy();

        List<Plan> insertPlans = new ArrayList<Plan>();
        List<PlanLog> insertPlanLogs = new ArrayList<PlanLog>();
        List<DeliverablesInfo> insertDeliverablesInfos = new ArrayList<DeliverablesInfo>();
        List<Inputs> inputsListAll = new ArrayList<Inputs>();
        List<PreposePlan> insertPreposePlans = new ArrayList<PreposePlan>();

        Map<String, Plan> parentPlanMap = new HashMap<String, Plan>();

        for (PlanTemplateDetail detail : planTemplateDetailList) {
            Plan plan = new Plan();
            String currentPlanId = UUIDGenerator.generate().toString();
            plan.setId(currentPlanId);
            detailIdPlanIdMap.put(detail.getId(), plan.getId());
            String[] workTime = detail.getWorkTime().split("[.]");
            if (StringUtils.isNotEmpty(detail.getParentPlanId())) {
                parentPlan = parentPlanMap.get(detail.getParentPlanId());
                planStartTime = parentPlan.getPlanStartTime();
                // 如果有父计划,计划的开始时间应为父计划的开始时间
                if (StringUtils.isNotEmpty(projectTimeType)) {
                    if (ProjectConstants.NATURALDAY.equals(projectTimeType)) {
                        planStartTime = DateUtil.nextDay(planStartTime, 0);
                    }
                    else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("startDate",planStartTime);
                        params.put("days",0);
                        planStartTime = calendarService.getNextWorkingDay(appKey, params);
                    }
                    else {
                        planStartTime = DateUtil.nextWorkDay(planStartTime, 0);
                    }
                }
            }
            if (parentPlan != null) {
                plan.setParentPlanId(parentPlan.getId());
            }
            Date newPlanStartTime = new Date();
            /*
             * if (detail.getParentPlanId() != null) {
             * newPlanStartTime = getStartTime(projectTimeType, planStartTime, detail,
             * workTime[0], preposeMap);
             * }
             * else {
             * newPlanStartTime = planStartTime;
             * }
             */

            newPlanStartTime = planStartTime;

            Date planEndTime = setPlanTime(projectTimeType, newPlanStartTime, workTime[0]);

            plan.setProjectId(planTemplateDetailReq.getProjectNumber());

            // 如果有上级时，计划结束时间少于上级的结束时间,则不导入
            if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
                long dayDiff = DateUtil.dayDiff(planEndTime, parentPlan.getPlanEndTime());
                if (dayDiff < 0) {
                    throw new GWException(
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.getStoryNoSavePlanInSerTwo"));
                }
            }
            else { // 如果没有上级，则计划结束时间少于项结束时间，则不导入
                long dayDiff = DateUtil.dayDiff(planEndTime, project.getEndProjectTime());
                if (dayDiff < 0) {
                    throw new GWException(
                            I18nUtil.getValue("com.glaway.ids.pm.project.plan.getStoryNoSavePlanInSerThree"));
                }
            }

            // 如果导入模板时计划节点需要重新排序
            // 每次都是最大的planNumber+1
            plan.setPlanNumber(maxPlanNumber + 1);
            maxPlanNumber++ ;

            plan.setStoreyNo(maxStoreyNo + 1);
            maxStoreyNo++ ;

            if (StringUtils.isEmpty(plan.getParentPlanId())) {
                storeyNo = plan.getStoreyNo();
            }

            // 如果是下方计划导入并且计划的上级与上方的上级相同，需要记录下最终的序号
            if (StringUtils.isNotEmpty(plan.getParentPlanId())) {
                if (StringUtils.isNotEmpty(planTemplateDetailReq.getUpPlanId())
                        && plan.getParentPlanId().equals(planTemplateDetailReq.getPlanId())) {
                    storeyNo = plan.getStoreyNo();
                }
            }

            plan.setPlanStartTime(newPlanStartTime);
            plan.setPlanEndTime(planEndTime);
            plan.setPlanName(detail.getName());

            // 给导入计划的计划类型、计划类别设值
            if (StringUtils.isNotBlank(detail.getTaskType())) {
                plan.setTaskType(detail.getTaskType());
            } else {
                plan.setTaskType(taskType);
            }
            if (isStandard) {
                if (StringUtils.isNotEmpty(standardNameMap.get(detail.getName()))) {
                    plan.setTaskNameType(standardNameMap.get(detail.getName()));
                }
            }

            plan.setMilestone(detail.getMilestone());
            plan.setPlanLevel(detail.getPlanLevel());
            plan.setWorkTime(workTime[0]);
            if (StringUtils.isNotEmpty(planTemplateDetailReq.getPlanSource())) {
                plan.setPlanSource(planTemplateDetailReq.getPlanSource());
            }
            plan.setCreateBy(planTemplateDetailReq.getCreateBy());
            plan.setBizCurrent(bizCurrent);
            plan.setBizVersion(bizVersion);
            plan.setPolicy(policy);
            plan.setBizId(UUID.randomUUID().toString());
            plan.setSecurityLevel(Short.valueOf("1"));
            plan.setTaskNameType(detail.getTaskNameType());
            plan.setTabCbTemplateId(detail.getTabCbTemplateId());
            CommonInitUtil.initGLObjectForCreate(plan,user,orgId);
            insertPlans.add(plan);
            parentPlanMap.put(detail.getId(), plan);

            PlanLog planLog = new PlanLog();
            planLog.setId(UUIDGenerator.generate().toString());
            planLog.setPlanId(plan.getId());
            planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);
            planLog.setCreateBy(user.getId());
            planLog.setCreateName(user.getUserName());
            planLog.setCreateFullName(user.getRealName());
            CommonInitUtil.initGLObjectForCreate(planLog,user,orgId);
            insertPlanLogs.add(planLog);

            if (NameStandardSwitchConstants.FORCEUSENAMESTANDARD.equals(switchStr)
                    || NameStandardSwitchConstants.DELIVERABLEUPATEABLE.equals(switchStr)) {
                if (!standardNameMap.containsKey(plan.getPlanName())) {
                    log.warn("计划名称【" + plan.getPlanName() + "】不在名称库中或已被禁用，请先联系名称库管理员后再重新上传导入!");
                    Object[] arguments = new String[] {plan.getPlanName()};
                    throw new GWException(I18nUtil.getValue(
                            "com.glaway.ids.pm.project.plan.deliverables.saveSystemDeliverable",
                            arguments));
                }
            }
            List<DeliverablesInfo> deliverables = deliverablesInfoService.getPlanDeliverablesByDetailAndName(
                    plan, switchStr, detailIdDeliverablesMap.get(detail.getId()),
                    nameDeliverablesMap.get(plan.getPlanName()), dBizCurrent, dBizVersion, dPolicy);
            if (!CommonUtil.isEmpty(deliverables)) {
                insertDeliverablesInfos.addAll(deliverables);
            }

            Inputs input = new Inputs();
            input.setUseObjectId(detail.getId());
            input.setUseObjectType("PLANTEMPLATE");
            CommonInitUtil.initGLObjectForCreate(input,user,orgId);
            List<Inputs> inputsList = inputsService.queryNewInputsList(input);
            if(!CommonUtil.isEmpty(inputsList)){
                for(Inputs in : inputsList){
                    in.setUseObjectId(detailIdPlanIdMap.get(in.getUseObjectId()));
                }
                inputsListAll.addAll(inputsList);
            }

        }

        if(!CommonUtil.isEmpty(inputsListAll)){
            for(Inputs inp : inputsListAll){
                if(!CommonUtil.isEmpty(inp.getOriginObjectId()) && !CommonUtil.isEmpty(detailIdPlanIdMap.get(inp.getOriginObjectId()))){
                    inp.setOriginObjectId(detailIdPlanIdMap.get(inp.getOriginObjectId()));
                }
            }
        }

        // 修改BUG计划模板导入时候前置计划和后置计划时间不对应
        for (PlanTemplateDetail detail : planTemplateDetailList) {
            if (null != preposeMap.get(detail.getId())) {

                PlanTemplateDetail preposeDetail = preposeMap.get(detail.getId()).get(0);

                for (Plan plan : insertPlans) {

                    if (plan.getId().equals(parentPlanMap.get(detail.getId()).getId())) {

                        Date preposePlanEndTime = parentPlanMap.get(preposeDetail.getId()).getPlanEndTime();

                        plan.setPlanStartTime(DateUtil.nextDay(preposePlanEndTime, 1));

                        plan.setPlanEndTime(setPlanTime(projectTimeType,
                                DateUtil.nextDay(preposePlanEndTime, 1), plan.getWorkTime()));
                        // 如果有上级时，计划结束时间少于上级的结束时间,则不导入
                        if (StringUtils.isNotEmpty(plan.getParentPlanId())) { // 计划结束时间要结束时间小于父计划的结束时间
                            long dayDiff = 0;
                            // 计划的开始时间要大于等于父计划时间
                            long dayDiffStart = 0;
                            // 判断是从计划下导入模板 还是全部替换导入模板
                            if (planId != null && planId.equals(plan.getParentPlanId())) {
                                // 如果是从计划下导入模板 而且计划是模板中的一级计划 需要与父计划的结束时间做判断
                                Plan oldPlan = new Plan();
                                oldPlan = (Plan)sessionFacade.getEntity(Plan.class, planId);
                                dayDiff = DateUtil.dayDiff(plan.getPlanEndTime(),
                                        oldPlan.getPlanEndTime());
                                dayDiffStart = DateUtil.dayDiff(oldPlan.getPlanStartTime(),
                                        plan.getPlanStartTime());
                            }
                            else {
                                dayDiff = DateUtil.dayDiff(plan.getPlanEndTime(),
                                        parentPlanMap.get(detail.getParentPlanId()).getPlanEndTime());
                                dayDiffStart = DateUtil.dayDiff(
                                        parentPlanMap.get(detail.getParentPlanId()).getPlanStartTime(),
                                        plan.getPlanStartTime());
                            }

                            if (dayDiffStart < 0) {
                                throw new GWException(I18nUtil.getValue("子计划开始时间小于父计划开始时间"));
                            }
                            if (dayDiff < 0) {
                                throw new GWException(
                                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.getStoryNoSavePlanInSerTwo"));
                            }

                        }
                        else { // 如果没有上级，则计划结束时间少于项结束时间，则不导入
                            long dayDiff = DateUtil.dayDiff(plan.getPlanEndTime(),
                                    project.getEndProjectTime());
                            // 计划的开始时间要大于等于父计划时间
                            long dayDiffStart = DateUtil.dayDiff(project.getStartProjectTime(),
                                    plan.getPlanStartTime());
                            if (dayDiffStart < 0) {
                                throw new GWException(I18nUtil.getValue("子计划开始时间小于父计划开始时间"));
                            }

                            if (dayDiff < 0) {
                                throw new GWException(
                                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.getStoryNoSavePlanInSerThree"));
                            }
                        }

                        break;
                    }

                }

            }else if(null != preposeMap.get(detail.getParentPlanId())){
                PlanTemplateDetail preposeDetail = preposeMap.get(detail.getParentPlanId()).get(0);

                for (Plan plan : insertPlans) {

                    if (plan.getId().equals(parentPlanMap.get(detail.getId()).getId())) {

                        Date preposePlanEndTime = parentPlanMap.get(preposeDetail.getId()).getPlanEndTime();

                        plan.setPlanStartTime(DateUtil.nextDay(preposePlanEndTime, 1));

                        plan.setPlanEndTime(setPlanTime(projectTimeType,
                                DateUtil.nextDay(preposePlanEndTime, 1), plan.getWorkTime()));
                        // 如果有上级时，计划结束时间少于上级的结束时间,则不导入
                        if (StringUtils.isNotEmpty(plan.getParentPlanId())) { // 计划结束时间要结束时间小于父计划的结束时间
                            long dayDiff = 0;
                            // 计划的开始时间要大于等于父计划时间
                            long dayDiffStart = 0;
                            // 判断是从计划下导入模板 还是全部替换导入模板
                            if (planId != null && planId.equals(plan.getParentPlanId())) {
                                // 如果是从计划下导入模板 而且计划是模板中的一级计划 需要与父计划的结束时间做判断
                                Plan oldPlan = new Plan();
                                oldPlan = (Plan)sessionFacade.getEntity(Plan.class, planId);
                                dayDiff = DateUtil.dayDiff(plan.getPlanEndTime(),
                                        oldPlan.getPlanEndTime());
                                dayDiffStart = DateUtil.dayDiff(oldPlan.getPlanStartTime(),
                                        plan.getPlanStartTime());
                            }
                            else {
                                dayDiff = DateUtil.dayDiff(plan.getPlanEndTime(),
                                        parentPlanMap.get(detail.getParentPlanId()).getPlanEndTime());
                                dayDiffStart = DateUtil.dayDiff(
                                        parentPlanMap.get(detail.getParentPlanId()).getPlanStartTime(),
                                        plan.getPlanStartTime());
                            }

                            if (dayDiffStart < 0) {
                                throw new GWException(I18nUtil.getValue("子计划开始时间小于父计划开始时间"));
                            }
                            if (dayDiff < 0) {
                                throw new GWException(
                                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.getStoryNoSavePlanInSerTwo"));
                            }

                        }
                        else { // 如果没有上级，则计划结束时间少于项结束时间，则不导入
                            long dayDiff = DateUtil.dayDiff(plan.getPlanEndTime(),
                                    project.getEndProjectTime());
                            // 计划的开始时间要大于等于父计划时间
                            long dayDiffStart = DateUtil.dayDiff(project.getStartProjectTime(),
                                    plan.getPlanStartTime());
                            if (dayDiffStart < 0) {
                                throw new GWException(I18nUtil.getValue("子计划开始时间小于父计划开始时间"));
                            }

                            if (dayDiff < 0) {
                                throw new GWException(
                                        I18nUtil.getValue("com.glaway.ids.pm.project.plan.getStoryNoSavePlanInSerThree"));
                            }
                        }

                        break;
                    }

                }
            }

        }

        insertPreposePlans = preposePlanService.getPreposePlans(planTemplateDetailList,
                preposeMap, detailIdPlanIdMap);

        Map<String, Object> flgMap = new HashMap<>();
        flgMap.put("user", user);
        flgMap.put("needDelete", needDelete);
        if (needDelete) {
            flgMap.put("projectId", planTemplateDetailReq.getProjectNumber());
        }
        if (StringUtils.isNotEmpty(planTemplateDetailReq.getUpPlanId())) {
            flgMap.put("upPlanId", planTemplateDetailReq.getUpPlanId());
            flgMap.put("upPlan", upPlan);
            flgMap.put("upStoreyNo", upStoreyNo);
            flgMap.put("storeyNo", storeyNo);
        }
        batchSaveDatas(flgMap, insertPlans, insertPlanLogs, new ArrayList<Inputs>(),
                insertDeliverablesInfos, insertPreposePlans,inputsListAll);
    }


    /**
     * Description: <br> 设置计划时间结束<br> Implement: <br> 1、…<br> 2、…<br>
     *
     * @param projectTimeType
     * @param planStartTime
     * @param workTime
     * @return
     * @throws GWException
     * @see
     */
    private Date setPlanTime(String projectTimeType, Date planStartTime, String workTime)
            throws GWException {
        if (planStartTime == null) {
            log.warn("上级开始时间为空，不可以导入");
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.setPlanTimeInSerOne"));
        }
        Date date = (Date)planStartTime.clone();
        Date planEndTime = null;
        try {
            if (StringUtils.isNotEmpty(projectTimeType)) {
                if (ProjectConstants.NATURALDAY.equals(projectTimeType)) {
                    planEndTime = DateUtil.nextDay(date, Integer.valueOf(workTime) - 1);
                }
                else if (ProjectConstants.COMPANYDAY.equals(projectTimeType)) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("startDate",date);
                    params.put("days",Integer.valueOf(workTime) - 1);
                    planEndTime = calendarService.getNextWorkingDay(appKey,params);
                }
                else {
                    planEndTime = DateUtil.nextWorkDay(date, Integer.valueOf(workTime) - 1);
                }

            }
        }
        catch (GWException e) {
            log.warn("通过上级开始时间,获得工期之后的时间出错");
            throw new GWException(
                    I18nUtil.getValue("com.glaway.ids.pm.project.plan.setPlanTimeInSerTwo"));
        }

        return planEndTime;
    }

    @Override
    public List<PlanHistory> getPlanByHistory(String planId) {
        String hql = "from PlanHistory l where l.planId = '" + planId + "'";
        return sessionFacade.findByQueryString(hql);
    }

    /**
     * 通过计划的ID获取其相关的所有ApprovePlanForm
     *
     * @param plan
     * @return
     * @see
     */
    @Override
    public List<ApprovePlanForm> getApprovePlanFormListByPlan(Plan plan) {
        List<ApprovePlanForm> list = new ArrayList<ApprovePlanForm>();
        List<ApprovePlanInfo> approvePlanInfos = sessionFacade.findByQueryString("from ApprovePlanInfo i where i.planId = '"
                + plan.getId() + "'");
        TemporaryPlan temporaryPlan = new TemporaryPlan();
        temporaryPlan.setPlanId(plan.getId());
        Plan p = getEntity(plan.getId());
        if (PlanConstants.PLAN_FLOWSTATUS_CHANGE.equals(p.getFlowStatus())) {
            List<TemporaryPlan> temporaryPlanListTemp = planChangeService.queryTemporaryPlanList(
                    temporaryPlan, 1, 10, false);
            if (!CommonUtil.isEmpty(temporaryPlanListTemp)) {
                List<ApprovePlanInfo> approvePlanInfos2 = sessionFacade.findByQueryString("from ApprovePlanInfo i where i.planId = '"
                        + temporaryPlanListTemp.get(
                        0).getId()
                        + "' order by i.createTime desc");
                if (!CommonUtil.isEmpty(approvePlanInfos2)) {
                    list.add(approvePlanInfos2.get(0).getApprovePlanForm());
                }
            }
        }
        for (ApprovePlanInfo info : approvePlanInfos) {
            if (info != null && info.getPlan() != null) {
                list.add(info.getApprovePlanForm());
            }
        }
        return list;
    }

    @Override
    public String getTaskNumberByPlan(String taskNumber, String type) {
        // 计划
        if (CommonConstants.FLOW_TYPE_PLAN.equals(type)) {
            Plan p = new Plan();
            p.setId(taskNumber);
            // 计划下达FormID
            List<ApprovePlanForm> forms = getApprovePlanFormListByPlan(p);

            try {
                // 计划变更FormID
                List<PlanHistory> planHistoryList = getPlanByHistory(taskNumber);
                if (!CommonUtil.isEmpty(planHistoryList)) {
                    for (PlanHistory history : planHistoryList) {
                        taskNumber += ("," + history.getFormId());
                    }
                }

                // 计划任务变更的申请单信息
                TemporaryPlan temporaryPlan = new TemporaryPlan();
                temporaryPlan.setPlanId(taskNumber);
                List<TemporaryPlan> temporaryPlanList = planChangeService.queryTemporaryPlanList(
                        temporaryPlan, 0, 0, false);
                if (!CommonUtil.isEmpty(temporaryPlanList)) {
                    for (TemporaryPlan temporaryPlanDb : temporaryPlanList) {
                        ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
                        approvePlanInfo.setPlanId(temporaryPlanDb.getId());
                        List<ApprovePlanInfo> approvePlanInfoList = planChangeService.queryApprovePlanInfoList(
                                approvePlanInfo, 1, 10, false);
                        // 获得approvePlanInfo的formid
                        if (!CommonUtil.isEmpty(approvePlanInfoList)) {
                            for (ApprovePlanInfo approvePlanInfoDb : approvePlanInfoList) {
                                taskNumber += ("," + approvePlanInfoDb.getFormId());
                            }
                        }
                    }
                }

            }
            catch (Exception e) {
                log.warn(I18nUtil.getValue("com.glaway.ids.pm.rdtask.taskProcOptLog.planChangeFormIdSearchFailure"));
            }
            if (!CommonUtil.isEmpty(forms)) {
                for (ApprovePlanForm form : forms) {
                    taskNumber += ("," + form.getId());
                }
            }
        }

        return taskNumber;
    }


    @Override
    public void startPlanReceivedProcess(Plan plan,String userId) {
        TSUserDto curUser = userService.getUserByUserId(userId);
        TSUserDto ownerInfo = userService.getUserByUserId(plan.getOwner());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", curUser.getUserName());
        variables.put("userId", userId);
        variables.put("owner", ownerInfo.getUserName());
        variables.put("cancelEventListener",
                "com.glaway.foundation.activiti.task.common.CommonCancelListener");
        variables.put("desc", "");
        variables.put("approve", "true");
        variables.put("flag", "");
        variables.put("oid", "_"+BpmnConstants.OID_PLAN + plan.getId());
        variables.put("appKey",appKey);
        variables.put("viewUrl","/ids-pm-web/planController.do?goPlanReceivedPage&action=view&dataHeight=600&dataWidth=800");

        String planReceiveEndListener = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/planReceivedEndListenerRestController/notify.do";
        variables.put("planReceiveEndListener", planReceiveEndListener);
        String planRefuseEndListener = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/planReceivedRefuseListenerRestController/notify.do";
        variables.put("planRefuseEndListener", planRefuseEndListener);
        String planDelegateEndListener = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/planDelegateEndListenerRestController/notify.do";
        variables.put("planDelegateEndListener", planDelegateEndListener);

        FeignJson j = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                PlanConstants.PLAN_TOBERECEIVED_PROCESS, plan.getId(), variables);
        String procInstId = j.getObj().toString(); // 流程实例ID
        String taskId = "";
        try {
            FeignJson fj = workFlowFacade.getWorkFlowTodoTaskService().getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                    procInstId, curUser.getUserName());
            taskId = String.valueOf(fj.getObj());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String,Object> params = new HashMap<>();
        params.put("variables",variables);
        params.put("userId",userId);
        workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, params);

        FeignJson nextTask = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = nextTask.getObj().toString();

        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
        myStartedTask.setTaskNumber(plan.getId());
        myStartedTask.setType(PlanConstants.PLAN_TOBERECEIVED_PROCESS);
        myStartedTask.setCreater(curUser.getUserName());
        myStartedTask.setCreaterFullname(curUser.getRealName());
        myStartedTask.setStartTime(new Date());
        myStartedTask.setProcType(PlanConstants.PLAN_TOBERECEIVED_PROCESS);
        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        myStartedTask.setAppKey(appKey);
        // 流程已发起列表中：增加对象名称的记录，流程任务名称规范化
        myStartedTask.setObjectName(plan.getPlanName());
        myStartedTask.setTitle(ProcessUtil.getProcessTitle(plan.getPlanName(),
                PlanConstants.PLAN_TOBERECEIVED_PROCESS_NAME, procInstId));
        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);


        plan.setPlanReceivedProcInstId(procInstId);
        plan.setPlanReceivedCompleteTime(null);
        sessionFacade.updateEntitie(plan);

    }

    @Override
    public void forwardPlanStatus(String planId,String userId) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);
        plan.setBizCurrent(PlanConstants.PLAN_ORDERED);
        plan.setPlanReceivedCompleteTime(new Date());
        updateProgressRate(plan);
        sessionFacade.updateEntitie(plan);

        List<Plan> planList = sessionFacade.findHql("from Plan where parentPlanId = ?",planId);
        if(!CommonUtil.isEmpty(planList)){
            for(Plan pp : planList){
                if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pp.getBizCurrent())){
                    updateBizCurrent(pp);
                }

                if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pp.getBizCurrent())){
                    if(CommonUtil.isEmpty(pp.getPlanReceivedProcInstId())){
                        startPlanReceivedProcess(pp,userId);
                    }else if(!CommonUtil.isEmpty(pp.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(pp.getPlanReceivedCompleteTime())){
                        startPlanReceivedProcess(pp,userId);
                    }
                }
            }
            sessionFacade.batchUpdate(planList);
        }
    }

    @Override
    public void backwardPlanStatus(String planId) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);
        plan.setBizCurrent(PlanConstants.PLAN_EDITING);
        updateProgressRate(plan);
        plan.setPlanReceivedCompleteTime(new Date());
        sessionFacade.updateEntitie(plan);
    }

    @Override
    public void continuePlanReceiveProcess(String planId, String flag, TSUserDto curUser) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);
        //待接收状态，走计划执行情况记录---接收
        if (PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
            PlanExcuteInfo info = planExcuteServiceI.queryInfoByPlanIdAndProjId(planId,plan.getProjectId());
            //不存在，则记录--接收
            if (info==null){
                planExcuteServiceI.saveInfo(planId, plan.getProjectId(),plan.getTaskType(),PlanConstants.PLAN_EXCUTE_REPORT_RECEIVE);
            }
        }
        String creator = curUser.getUserName();
        String taskId = "";
        FeignJson taskIdFj = workFlowFacade.getWorkFlowTodoTaskService() .getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                plan.getPlanReceivedProcInstId(), creator);

        if (taskIdFj.isSuccess()) {
            taskId = taskIdFj.getObj() == null ? "" : taskIdFj.getObj().toString();
        }

        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);
            variables.put("flag", flag);
            variables.put("approve", "true");

            Map<String,Object> params = new HashMap<>();
            params.put("variables",variables);
            params.put("userId",curUser.getId());
            List<String> comments = new ArrayList<String>();
            comments.add("接收");
            params.put("comments",comments);

            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    plan.getPlanReceivedProcInstId());
            String status = newNextTasks.getObj().toString();
            // 更新我的已发起流程状态
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                    plan.getId(), plan.getPlanReceivedProcInstId());
            myStartedTask.setStatus(status);
            myStartedTask.setEndTime(new Date());
            workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);

           /* plan.setBizCurrent(PlanConstants.PLAN_ORDERED);
            sessionFacade.updateEntitie(plan);*/

        }
    }

    @Override
    public void refusePlanReceiveProcess(String planId, TSUserDto curUser,String refuseReason,String refuseRemark,String orgId) {

        PlanRefuseInfo planRefuseInfo = new PlanRefuseInfo();
        planRefuseInfo.setPlanId(planId);
        planRefuseInfo.setRefuseReason(refuseReason);
        planRefuseInfo.setRemark(refuseRemark);
        CommonInitUtil.initGLObjectForCreate(planRefuseInfo,curUser,orgId);
        sessionFacade.save(planRefuseInfo);

        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);

        //待接收状态，走计划执行情况记录---驳回
        if (PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
            PlanExcuteInfo info = planExcuteServiceI.queryInfoByPlanIdAndProjId(planId,plan.getProjectId());
            //不存在，则记录--驳回
            if (info==null){
                planExcuteServiceI.saveInfo(planId, plan.getProjectId(),plan.getTaskType(),PlanConstants.PLAN_EXCUTE_REPORT_TURN_DOWN);
            }
        }

        String creator = curUser.getUserName();
        String assigner = userService.getUserByUserId(plan.getAssigner()).getUserName();
        String taskId = "";
        FeignJson taskIdFj = workFlowFacade.getWorkFlowTodoTaskService() .getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                plan.getPlanReceivedProcInstId(), creator);

        if (taskIdFj.isSuccess()) {
            taskId = taskIdFj.getObj() == null ? "" : taskIdFj.getObj().toString();
        }

        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);
            variables.put("approve", "false");
            variables.put("desc", "驳回");
            variables.put("assigner",assigner);
            variables.put("viewUrl",
                    "/ids-pm-web/planController.do?goRefusePlanPage&dataHeight=300&dataWidth=800&refuseId="
                            + planRefuseInfo.getId()+"&planId = "+plan.getId());

            Map<String,Object> params = new HashMap<>();
            params.put("variables",variables);
            params.put("userId",curUser.getId());

            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    plan.getPlanReceivedProcInstId());
            String status = newNextTasks.getObj().toString();
            // 更新我的已发起流程状态
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                    plan.getId(), plan.getPlanReceivedProcInstId());
            myStartedTask.setStatus(status);
            myStartedTask.setEndTime(new Date());
            workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);

        }
    }

    @Override
    public PlanRefuseInfo getPlanRefuseInfoEntity(String id) {
        PlanRefuseInfo planRefuseInfo = (PlanRefuseInfo)sessionFacade.getEntity(PlanRefuseInfo.class,id);
        return planRefuseInfo;
    }

    @Override
    public void delegatePlanReceiveProcess(String planId, TSUserDto curUser, String delegateUserId, String changeType, String leaderId, String departLeaderId, String changeRemark, String flag, String orgId) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);
        TemporaryPlan temporaryPlan = new TemporaryPlan();
        temporaryPlan.setPlanId(planId);
        temporaryPlan.setChangeRemark(changeRemark);
        temporaryPlan.setOwner(delegateUserId);
        temporaryPlan.setChangeType(changeType);
        temporaryPlan.setPlanEndTime(plan.getPlanEndTime());
        temporaryPlan.setPlanLevel(plan.getPlanLevel());
        temporaryPlan.setPlanName(plan.getPlanName());
        temporaryPlan.setPlanNumber(plan.getPlanNumber());
        temporaryPlan.setPlanOrder(plan.getPlanOrder());
        temporaryPlan.setPlanStartTime(plan.getPlanStartTime());
        temporaryPlan.setParentPlanId(plan.getParentPlanId());
        temporaryPlan.setProgressRate(plan.getProgressRate());
        temporaryPlan.setProjectId(plan.getProjectId());
        temporaryPlan.setRemark(plan.getRemark());
        temporaryPlan.setWorkTime(plan.getWorkTime());
        temporaryPlan.setStoreyNo(plan.getStoreyNo());
        temporaryPlan.setTaskType(plan.getTaskType());
        temporaryPlan.setTaskNameType(plan.getTaskNameType());
        temporaryPlan.setMilestone(plan.getMilestone());
        initBusinessObject(temporaryPlan);
        CommonInitUtil.initGLObjectForCreate(temporaryPlan,curUser,orgId);
        sessionFacade.save(temporaryPlan);

        String creator = curUser.getUserName();
        String leader = userService.getUserByUserId(leaderId).getUserName();
        String departLeader = userService.getUserByUserId(departLeaderId).getUserName();
        String taskId = "";
        FeignJson taskIdFj = workFlowFacade.getWorkFlowTodoTaskService() .getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                plan.getPlanReceivedProcInstId(), creator);

        if (taskIdFj.isSuccess()) {
            taskId = taskIdFj.getObj() == null ? "" : taskIdFj.getObj().toString();
        }

        if (StringUtil.isNotEmpty(taskId)) {
            Map<String, Object> variables = workFlowFacade.getWorkFlowCommonService().getVariablesByTaskId(
                    taskId);
            variables.put("approve", "true");
            variables.put("delegateUserId", delegateUserId);
            variables.put("flag", flag);
            variables.put("desc", "委派");
            variables.put("leader", leader);
            variables.put("deptLeader", departLeader);
            variables.put("viewUrl",
                    "/ids-pm-web/planController.do?goDelegatePlanPage&dataHeight=300&dataWidth=800&temporyPlanId="
                            + temporaryPlan.getId()+"&planId = "+plan.getId());

            Map<String,Object> params = new HashMap<>();
            params.put("variables",variables);
            params.put("userId",curUser.getId());

            // 执行流程
            workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);

            FeignJson newNextTasks = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                    plan.getPlanReceivedProcInstId());
            String status = newNextTasks.getObj().toString();
            // 更新我的已发起流程状态
            MyStartedTaskDto myStartedTask = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByNumberAndProcInstId(
                    plan.getId(), plan.getPlanReceivedProcInstId());
            myStartedTask.setStatus(status);
            myStartedTask.setEndTime(new Date());
            workFlowFacade.getWorkFlowStartedTaskService().updateMyStartedTaskByMyStartedTask(myStartedTask);
            //待接收状态，走计划执行情况记录---委派
            if (PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                PlanExcuteInfo info = planExcuteServiceI.queryInfoByPlanIdAndProjId(planId,plan.getProjectId());
                //不存在，则记录--委派
                if (info==null){
                    planExcuteServiceI.saveInfo(planId, plan.getProjectId(),plan.getTaskType(),PlanConstants.PLAN_EXCUTE_REPORT_DELEGATE);
                }
            }
        }
    }

    @Override
    public void startPlanDelegateProcess(String planId, TSUserDto curUser, String delegateUserId, String changeType, String leaderId, String departLeaderId, String changeRemark, String orgId) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);
        TemporaryPlan temporaryPlan = new TemporaryPlan();
        temporaryPlan.setPlanId(planId);
        temporaryPlan.setChangeRemark(changeRemark);
        temporaryPlan.setOwner(delegateUserId);
        temporaryPlan.setChangeType(changeType);
        temporaryPlan.setPlanEndTime(plan.getPlanEndTime());
        temporaryPlan.setPlanLevel(plan.getPlanLevel());
        temporaryPlan.setPlanName(plan.getPlanName());
        temporaryPlan.setPlanNumber(plan.getPlanNumber());
        temporaryPlan.setPlanOrder(plan.getPlanOrder());
        temporaryPlan.setPlanStartTime(plan.getPlanStartTime());
        temporaryPlan.setParentPlanId(plan.getParentPlanId());
        temporaryPlan.setProgressRate(plan.getProgressRate());
        temporaryPlan.setProjectId(plan.getProjectId());
        temporaryPlan.setRemark(plan.getRemark());
        temporaryPlan.setWorkTime(plan.getWorkTime());
        temporaryPlan.setStoreyNo(plan.getStoreyNo());
        temporaryPlan.setTaskType(plan.getTaskType());
        temporaryPlan.setTaskNameType(plan.getTaskNameType());
        temporaryPlan.setMilestone(plan.getMilestone());
        initBusinessObject(temporaryPlan);
        CommonInitUtil.initGLObjectForCreate(temporaryPlan,curUser,orgId);
        sessionFacade.save(temporaryPlan);

        String leader = userService.getUserByUserId(leaderId).getUserName();
        String departLeader = userService.getUserByUserId(departLeaderId).getUserName();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", curUser.getUserName());
        variables.put("userId", curUser.getId());
        variables.put("leader", leader);
        variables.put("deptLeader", departLeader);
        variables.put("delegateUserId", delegateUserId);
        variables.put("cancelEventListener",
                "com.glaway.foundation.activiti.task.common.CommonCancelListener");
        variables.put("desc", "");
        variables.put("approve", "true");
        variables.put("oid", "_"+BpmnConstants.OID_PLAN + plan.getId());
        variables.put("appKey",appKey);
        variables.put("viewUrl",
                "/ids-pm-web/planController.do?goDelegatePlanPage&dataHeight=300&dataWidth=800&temporyPlanId="
                        + temporaryPlan.getId()+"&planId = "+plan.getId());

        String planReceiveEndListener = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/planDelegateReceivedListenerRestController/notify.do";
        variables.put("planDelegateEndListener", planReceiveEndListener);
        String planRefuseEndListener = "http://"+env.getProperty("server.address")+":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path")+"/feign/planDelegateRefuseListenerRestController/notify.do";
        variables.put("planDelegateRefuseListener", planRefuseEndListener);

        FeignJson j = workFlowFacade.getWorkFlowCommonService().getProcessInstanceIdByStartProcessByBusinessKey(
                PlanConstants.PLAN_DELEGATE_PROCESS, plan.getId(), variables);
        String procInstId = j.getObj().toString(); // 流程实例ID

        String taskId = "";
        try {
            FeignJson fj = workFlowFacade.getWorkFlowTodoTaskService().getTaskIdByProcInstIdAndAssigneeToFeignJson(appKey,
                    procInstId, curUser.getUserName());
            taskId = String.valueOf(fj.getObj());

        } catch (Exception e) {
            e.printStackTrace();
        }

        workFlowFacade.getWorkFlowCommonService().completeProcessByApp(taskId, variables);
        /*Map<String,Object> params = new HashMap<>();
        params.put("variables",variables);
        params.put("userId",userId);
        workFlowFacade.getWorkFlowCommonService().completeProcess(taskId, params);*/

        FeignJson nextTask = workFlowFacade.getWorkFlowCommonService().getTaskByProcessInstance(
                procInstId);
        String status = nextTask.getObj().toString();

        MyStartedTaskDto myStartedTask = new MyStartedTaskDto();
        myStartedTask.setTaskNumber(plan.getId());
        myStartedTask.setType(PlanConstants.PLAN_DELEGATE_PROCESS);
        myStartedTask.setCreater(curUser.getUserName());
        myStartedTask.setCreaterFullname(curUser.getRealName());
        myStartedTask.setStartTime(new Date());
        myStartedTask.setProcType(PlanConstants.PLAN_DELEGATE_PROCESS);
        myStartedTask.setProcInstId(procInstId);
        myStartedTask.setStatus(status);
        myStartedTask.setAppKey(appKey);
        // 流程已发起列表中：增加对象名称的记录，流程任务名称规范化
        myStartedTask.setObjectName(plan.getPlanName());
        myStartedTask.setTitle(ProcessUtil.getProcessTitle(plan.getPlanName(),
                PlanConstants.PLAN_DELEGATE_PROCESS_NAME, procInstId));
        workFlowFacade.getWorkFlowStartedTaskService().saveMyStartedTask(myStartedTask);

        plan.setPlanDelegateProcInstId(procInstId);
        plan.setIsDelegateComplete("false");

        sessionFacade.updateEntitie(plan);

    }

    @Override
    public void updatePlanOwnerAndRestartPlanReceivedProcess(String planId, String delegateUserId,String userId) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);
        plan.setOwner(delegateUserId);
        plan.setPlanReceivedCompleteTime(new Date());
        sessionFacade.updateEntitie(plan);

        startPlanReceivedProcess(plan,userId);
    }

    @Override
    public void updatePlanOwner(String planId, String delegateUserId) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);
        plan.setOwner(delegateUserId);
        plan.setIsDelegateComplete("recevied");
        sessionFacade.updateEntitie(plan);
    }

    @Override
    public void updatePlanDelegateFlag(String planId) {
        Plan plan = (Plan)sessionFacade.getEntity(Plan.class,planId);
        plan.setIsDelegateComplete("refuse");
        sessionFacade.updateEntitie(plan);
    }

    @Override
    public FeignJson getAllPlanLifeCycleArrayByProjId(String projId) {
        FeignJson feignJson = new FeignJson();
        try{
            StringBuilder hql = new StringBuilder("");
            hql.append("from Plan t where 1=1 and t.avaliable='1' and t.projectId=?");
            List<Object> paramList = new ArrayList<Object>();
            paramList.add(projId);
            List<Plan> planList = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
            //拟制中
            int enitingCount = 0;
            //已发布
            int lauchedCount= 0;
            //待接收
            int tobereceivedCount= 0;
            //执行中
            int orderedCount = 0;
            //完工确认
            int feedbackingCount= 0;
            //已完工
            int finishCount= 0;
            for (Plan plan: planList){
                if (PlanConstants.PLAN_EDITING.equals(plan.getBizCurrent())){
                    enitingCount++;
                }else if (PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(plan.getBizCurrent())){
                    lauchedCount++;
                }else if (PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(plan.getBizCurrent())){
                    tobereceivedCount++;
                }else if (PlanConstants.PLAN_ORDERED.equals(plan.getBizCurrent())){
                    orderedCount++;
                }else if (PlanConstants.PLAN_FEEDBACKING.equals(plan.getBizCurrent())){
                    feedbackingCount++;
                }else if (PlanConstants.PLAN_FINISH.equals(plan.getBizCurrent())){
                    finishCount++;
                }
            }
            StringBuffer sb = new StringBuffer();
            sb.append(enitingCount+",").append(lauchedCount+",").append(tobereceivedCount+",").append(orderedCount+",")
                    .append(feedbackingCount+",").append(finishCount);
            feignJson.setObj(sb.toString());
            feignJson.setMsg("操作成功");
        }catch (Exception e){
            feignJson.setSuccess(false);
            feignJson.setMsg("数据查询失败");
        }
        return feignJson;
    }

    @Override
    public FeignJson getAllPlanCompletionByProjId(String projId) {
        FeignJson feignJson = new FeignJson();
        try{
            StringBuilder hql = new StringBuilder("");
            hql.append("from Plan t where 1=1 and t.avaliable='1' and t.projectId=?");
            List<Object> paramList = new ArrayList<Object>();
            paramList.add(projId);
            List<Plan> planList = sessionFacade.executeQuery(hql.toString(), paramList.toArray());
            //WBS计划完成数量
            int wbsFinishCount = 0;
            //流程计划完成数量
            int flowFinishCount = 0;
            //任务计划完成数量
            int taskFinishCount = 0;

            //WBS计划正常未完成数量
            int wbsNoOvertimeCount = 0;
            //流程正常未完成数量
            int flowNoOvertimeCount = 0;
            //任务正常未完成数量
            int taskNoOvertimeCount = 0;

            //WBS计划延期未完成数量
            int wbsOvertimeCount = 0;
            //流程延期未完成数量
            int flowOvertimeCount = 0;
            //任务延期未完成数量
            int taskOvertimeCount = 0;

            for (Plan plan: planList){
                //WBS计划
                if (PlanConstants.PLAN_TYPE_WBS.equals(plan.getTaskType())){
                    //已完工数量
                    if (PlanConstants.PLAN_FINISH.equals(plan.getBizCurrent())){
                        wbsFinishCount++;
                    }else{
                        if (!PlanConstants.PLAN_INVALID.equals(plan.getBizCurrent())&&!PlanConstants.PLAN_EDITING.equals(plan.getBizCurrent())){
                            //计划结束时间大于当前时间，则正常未完成
                            if (plan.getPlanEndTime().getTime() >= (new Date()).getTime()){
                                wbsNoOvertimeCount++;
                            }else{
                                wbsOvertimeCount++;
                            }
                        }
                    }
                    //流程计划
                }else if (PlanConstants.PLAN_TYPE_FLOW.equals(plan.getTaskType())){
                    //已完工数量
                    if (PlanConstants.PLAN_FINISH.equals(plan.getBizCurrent())){
                        flowFinishCount++;
                    }else{
                        if (!PlanConstants.PLAN_INVALID.equals(plan.getBizCurrent())&&!PlanConstants.PLAN_EDITING.equals(plan.getBizCurrent())){
                            //计划结束时间大于当前时间，则正常未完成
                            if (plan.getPlanEndTime().getTime() >= (new Date()).getTime()){
                                flowNoOvertimeCount++;
                            }else{
                                flowOvertimeCount++;
                            }
                        }
                    }
                    //任务计划
                }else if (PlanConstants.PLAN_TYPE_TASK.equals(plan.getTaskType())){
                    //已完工数量
                    if (PlanConstants.PLAN_FINISH.equals(plan.getBizCurrent())){
                        taskFinishCount++;
                    }else{
                        if (!PlanConstants.PLAN_INVALID.equals(plan.getBizCurrent())&&!PlanConstants.PLAN_EDITING.equals(plan.getBizCurrent())){
                            //计划结束时间大于当前时间，则正常未完成
                            if (plan.getPlanEndTime().getTime() >= (new Date()).getTime()){
                                taskNoOvertimeCount++;
                            }else{
                                taskOvertimeCount++;
                            }
                        }
                    }
                }
            }
            String wbs = new StringBuffer().append(wbsFinishCount+",").append(flowFinishCount+",").append(taskFinishCount).toString();
            String flow= new StringBuffer().append(wbsNoOvertimeCount+",").append(flowNoOvertimeCount+",").append(taskNoOvertimeCount).toString();
            String task = new StringBuffer().append(wbsOvertimeCount+",").append(flowOvertimeCount+",").append(taskOvertimeCount).toString();
            Map<String, String> map = new HashMap<>(8);
            map.put("wbs", wbs);
            map.put("flow", flow);
            map.put("task", task);
            feignJson.setObj(map);
            feignJson.setMsg("操作成功");
        }catch (Exception e){
            feignJson.setSuccess(false);
            feignJson.setMsg("数据查询失败");
        }
        return feignJson;
    }

    @Override
    public void updateProgressRate(Plan plan) {
        String percent = getProgress(plan);
        plan.setProgressRate(String.valueOf(percent));
    }

    @Override
    public void updateProgress(Plan plan) {
        updateProgressRate(plan);
        sessionFacade.saveOrUpdate(plan);
    }

    @Override
    public String getProgress(Plan plan) {
        int deliveryCount = 0;

        //获取计划生命周期状态
        String status = plan.getBizCurrent();

        //获取交付物各个生命周期下的数量
        FeignJson fJson = repService.getLifeCycleListStr(ResourceUtil.getApplicationInformation().getAppKey(),new RepFileDto());
        String  lifeCycleListStr = fJson.getObj().toString();
        List<LifeCycleStatus> deliveryLifeCycleStatus = JSON.parseArray(lifeCycleListStr,LifeCycleStatus.class);
        Map<String,Integer> deliveryNumber = new HashMap<>();
        deliveryLifeCycleStatus.forEach(obj -> {
            deliveryNumber.put(obj.getName(),0);
        });
        if (StringUtils.isNotEmpty(plan.getId())) {
            //获取交付物数量
            DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
            deliverablesInfo.setUseObjectId(plan.getId());
            deliverablesInfo.setUseObjectType(PlanConstants.USEOBJECT_TYPE_PLAN);
            deliveryCount = (int) deliverablesInfoService.getCount(deliverablesInfo);
            List<DeliverablesInfo> deliverablesList = deliverablesInfoService.queryDeliverableList(deliverablesInfo,0,10,false);
            List<RepFileDto> repFileList = new ArrayList<>();
            if (!CommonUtil.isEmpty(deliverablesList)) {
                for (DeliverablesInfo info : deliverablesList) {
                    List<ProjDocRelation> projList = projLibService.getDocRelationList(info.getId());
                    if (!CommonUtil.isEmpty(projList)) {
                        for (ProjDocRelation projDocRelation : projList) {
                            //接口获取RepFile塞入对象
                            if (StringUtils.isNotBlank(projDocRelation.getDocId())) {
                                RepFileDto repFileDto = repService.getRepFileByRepFileId(ResourceUtil.getApplicationInformation().getAppKey(),projDocRelation.getDocId());
                                repFileList.add(repFileDto);
                            }
                        }
                    }
                }
            }
            if (!CommonUtil.isEmpty(repFileList)) {
                repFileList.forEach(obj -> {
                    int number = deliveryNumber.get(obj.getBizCurrent());
                    deliveryNumber.put(obj.getBizCurrent(),number+1);
                });
            }
        }

        double percent = planFeedBackService.calculateProgressRate(status,deliveryCount,deliveryNumber);
        return String.valueOf(percent);
    }

    @Override
    public List<LifeCycleStatus> getPlanLifeCycleStatus() {
        Plan p = new Plan();
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
        return orderList;
    }

    @Override
    public FeignJson doSaveBom(String planId, TSUserDto curUser, String bomId, String projectId, String code, String name, String orgId) {
        Plan plan = new Plan();
        plan.setProjectId(projectId);
        plan.setParentPlanId(planId);
        Plan initPlan = initBusinessObject(plan);
        String bizCurrent = initPlan.getBizCurrent();
        String bizVersion = initPlan.getBizVersion();
        LifeCyclePolicy policy = initPlan.getPolicy();
        int maxPlanNumber = getMaxPlanNumber(plan) + 1;
        int maxStoreyNo =getMaxStoreyNo(plan) + 1;
        String [] bomIds = bomId.split(",");
        String [] codes = code.split(",");
        String [] names = name.split(",");
        Plan addPlan;
        List<Plan> planList = new ArrayList<>();
        PlanLog planLog;
        List<PlanLog> planLogList = new ArrayList<>();
        String newPlanId = "";
        for (int i=0;i<names.length;i++){
            addPlan = new Plan();
            CommonInitUtil.initGLObjectForCreate(addPlan,curUser,orgId);
            newPlanId = UUIDGenerator.generate().toString();
            addPlan.setTabCbTemplateId("4028f00d6dd22ee4016dd25de68d0001");
            addPlan.setId(newPlanId);
            addPlan.setPlanEndTime(new Date());
            addPlan.setPlanStartTime(new Date());
            addPlan.setWorkTime("1");
            addPlan.setProjectId(projectId);
            addPlan.setBizCurrent(bizCurrent);
            addPlan.setTaskNameType("4028f00d6db34426016db365b27c0000");
            addPlan.setBizVersion(bizVersion);
            addPlan.setPolicy(policy);
            addPlan.setBizId(UUIDGenerator.generate().toString());
            addPlan.setSecurityLevel(Short.valueOf("1"));
            addPlan.setRelationId(bomIds[i]);
            addPlan.setRelationCode(codes[i]);
            addPlan.setPlanName("完成"+names[i]+"的设计");
            addPlan.setPlanNumber(maxPlanNumber+i);
            if (StringUtils.isNotEmpty(planId)){
                addPlan.setStoreyNo(maxStoreyNo+i);
                addPlan.setParentPlanId(planId);
                addPlan.setTaskType("任务计划");
            }else{
                addPlan.setStoreyNo(maxStoreyNo);
                addPlan.setTaskType("WBS计划");
            }
            planList.add(addPlan);

            planLog = new PlanLog();
            planLog.setId(UUIDGenerator.generate().toString());
            planLog.setPlanId(newPlanId);
            planLog.setLogInfo(PlanConstants.PLAN_LOGINFO_CREATE);
            planLog.setCreateBy(curUser.getId());
            planLog.setCreateName(curUser.getUserName());
            planLog.setCreateFullName(curUser.getRealName());
            CommonInitUtil.initGLObjectForCreate(planLog,curUser,orgId);
            planLogList.add(planLog);
        }
        FeignJson feignJson = new FeignJson();
        String msg = batchSaveDatas(planList, planLogList, curUser);
        if (!msg.equals("false")){
            feignJson.setSuccess(true);
            feignJson.setMsg(msg);
        }else{
            feignJson.setSuccess(false);
        }
        return feignJson;
    }

    private String batchSaveDatas(List<Plan> insertPlans, List<PlanLog> insertPlanLogs, TSUserDto currentUser) {
        String msg = "";
        Connection conn = null;

        // 插入计划及相关信息
        PreparedStatement psForPlan = null;
        PreparedStatement psForPlanLog = null;

        // 更新计划storeyNo
        String createBy = currentUser.getId();
        String createFullName = currentUser.getRealName();
        String createName = currentUser.getUserName();
        String updateBy = currentUser.getId();
        String updateFullName = currentUser.getRealName();
        String updateName = currentUser.getUserName();
        String firstBy = currentUser.getId();
        String firstFullName = currentUser.getRealName();
        String firstName = currentUser.getUserName();
        Timestamp createTime = new Timestamp(new Date().getTime());
        Timestamp updateTime = new Timestamp(new Date().getTime());
        Timestamp firstTime = new Timestamp(new Date().getTime());

        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            conn.setAutoCommit(false);

            // 批量插入计划
            if (!CommonUtil.isEmpty(insertPlans)) {
                String sqlForPlan = " insert into PM_PLAN ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                        + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
                        + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
                        + " SECURITYLEVEL, AVALIABLE,"
                        + " PLANNAME, PLANNUMBER, PROJECTID, PARENTPLANID,"
                        + " PLANLEVEL, PLANSTARTTIME, PLANENDTIME, "
                        + " PROGRESSRATE, STOREYNO, MILESTONE, PLANSOURCE,"
                        + " WORKTIME, WORKTIMEREFERENCE, TASKNAMETYPE, TASKTYPE, "
                        + " FLOWSTATUS, CELLID, FROMTEMPLATE, REQUIRED, OWNER,CREATEORGID,TABCBTEMPLATEID) "
                        + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'),"
                        + " to_date(?, 'yyyy-mm-dd'), ?, ?, ?, ?,"
                        + " ?, ?, ?, ?,'NORMAL'," + " ?, 'true', ?, ?,?,?)";
                psForPlan = conn.prepareStatement(sqlForPlan);
                for (Plan plan : insertPlans) {
                    psForPlan.setObject(1, plan.getId());
                    psForPlan.setObject(2, createTime);
                    psForPlan.setObject(3, createBy);
                    psForPlan.setObject(4, createFullName);
                    psForPlan.setObject(5, createName);
                    psForPlan.setObject(6, updateTime);
                    psForPlan.setObject(7, updateBy);
                    psForPlan.setObject(8, updateFullName);
                    psForPlan.setObject(9, updateName);
                    psForPlan.setObject(10, firstTime);
                    psForPlan.setObject(11, firstBy);
                    psForPlan.setObject(12, firstFullName);
                    psForPlan.setObject(13, firstName);
                    psForPlan.setObject(14, plan.getPolicy().getId());
                    psForPlan.setObject(15, plan.getBizCurrent());
                    psForPlan.setObject(16, plan.getBizId());
                    psForPlan.setObject(17, plan.getBizVersion());
                    psForPlan.setObject(18, plan.getSecurityLevel());
                    psForPlan.setObject(19, plan.getAvaliable());
                    psForPlan.setObject(20, plan.getPlanName());
                    psForPlan.setObject(21, plan.getPlanNumber());
                    psForPlan.setObject(22, plan.getProjectId());
                    psForPlan.setObject(23, plan.getParentPlanId());
                    psForPlan.setObject(24, plan.getPlanLevel());
                    psForPlan.setObject(25,
                            DateUtil.dateToString(plan.getPlanStartTime(), DateUtil.YYYY_MM_DD));
                    psForPlan.setObject(26,
                            DateUtil.dateToString(plan.getPlanEndTime(), DateUtil.YYYY_MM_DD));
                    psForPlan.setObject(27, plan.getProgressRate());
                    psForPlan.setObject(28, plan.getStoreyNo());
                    psForPlan.setObject(29, plan.getMilestone());
                    psForPlan.setObject(30, plan.getPlanSource());
                    psForPlan.setObject(31, plan.getWorkTime());
                    psForPlan.setObject(32, plan.getWorkTimeReference());
                    psForPlan.setObject(33, plan.getTaskNameType());
                    psForPlan.setObject(34, plan.getTaskType());
                    psForPlan.setObject(35, plan.getCellId());
                    psForPlan.setObject(36,
                            StringUtils.isEmpty(plan.getRequired()) ? "" : plan.getRequired());
                    psForPlan.setObject(37,
                            StringUtils.isEmpty(plan.getOwner()) ? "" : plan.getOwner());
                    psForPlan.setObject(38,
                            StringUtils.isEmpty(plan.getCreateOrgId()) ? "" : plan.getCreateOrgId());
                    psForPlan.setObject(39, plan.getTabCbTemplateId());
                    psForPlan.addBatch();
                }
                psForPlan.executeBatch();
            }

            // 批量插入计划日志
            if (!CommonUtil.isEmpty(insertPlanLogs)) {
                String sqlForPlanLog = " insert into PM_PLAN_LOG ("
                        + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                        + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                        + " PLANID, LOGINFO,CREATEORGID)" + " values (" + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, ?,?," + " ?)";
                psForPlanLog = conn.prepareStatement(sqlForPlanLog);
                for (PlanLog planLog : insertPlanLogs) {
                    psForPlanLog.setObject(1, planLog.getId());
                    psForPlanLog.setObject(2, createTime);
                    psForPlanLog.setObject(3, createBy);
                    psForPlanLog.setObject(4, createFullName);
                    psForPlanLog.setObject(5, createName);
                    psForPlanLog.setObject(6, updateTime);
                    psForPlanLog.setObject(7, updateBy);
                    psForPlanLog.setObject(8, updateFullName);
                    psForPlanLog.setObject(9, updateName);
                    psForPlanLog.setObject(10, planLog.getPlanId());
                    psForPlanLog.setObject(11, planLog.getLogInfo());
                    psForPlanLog.setObject(12, CommonUtil.isEmpty(planLog.getCreateOrgId())?"":planLog.getCreateOrgId());
                    psForPlanLog.addBatch();
                }
                psForPlanLog.executeBatch();
            }
            conn.commit();
            msg = insertPlans.size() + "";
        }
        catch (Exception ex) {
            msg = "false";
            try {
                conn.rollback();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        finally {
            try {
                conn.setAutoCommit(true);
                DBUtil.closeConnection(null, psForPlan, conn);
                DBUtil.closeConnection(null, psForPlanLog, conn);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return msg;
        }
    }

    @Override
    public void updateForFeedBack(String id, Map<String, Object> map) {
        String userId = (String)map.get("userId");
        TSUserDto user = userService.getUserByUserId(userId);
        Plan plan = getEntity(id);
        String logInfo = "";
        String flowStaus = "";
        String allFinishMsg = "";
        String leader = (String)map.get("leader");
        String procInstId = (String)map.get("procInstId");
        //提交完工反馈
        if (PlanConstants.PLAN_ORDERED.equals(plan.getBizCurrent())) {
            logInfo = PlanConstants.PLAN_LOGINFO_FEEDBACK;
            flowStaus = PlanConstants.PLAN_FLOWSTATUS_FEEDBACKING;
            plan.setBizCurrent(PlanConstants.PLAN_FEEDBACKING);
            plan.setFeedbackProcInstId(procInstId);
            plan.setFlowStatus(flowStaus);
            plan.setProgressRate(getProgress(plan));
            sessionFacade.saveOrUpdate(plan);

            if(user.getUserName().equals(leader)){
                allFinishMsg = id;
                logInfo = PlanConstants.PLAN_LOGINFO_FINISH;
                flowStaus = PlanConstants.PLAN_FLOWSTATUS_NORMAL;
                plan.setBizCurrent(PlanConstants.PLAN_FINISH);
                procInstId = "";
                plan.setFeedbackProcInstId(procInstId);
                plan.setFlowStatus(flowStaus);
                plan.setActualEndTime(new Date());
                plan.setProgressRate(getProgress(plan));

                boolean needHttp = false;
                if(!CommonUtil.isEmpty(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(plan.getTaskNameType())
                        && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(plan.getTaskNameType())){
                    needHttp = true;
                }
                if(needHttp){
                    List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"flowActiveCategoryHttpServer");
                    if (!CommonUtil.isEmpty(outwardExtensionList)) {
                        for (OutwardExtensionDto ext : outwardExtensionList) {
                            if (!CommonUtil.isEmpty(ext.getUrlList())) {
                                List<String> urls = new ArrayList<String>();
                                for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                                    if ("finishAllTaskStep".equals(out.getOperateCode())) {
                                        urls.add(out.getOperateUrl() + "&planId=" + plan.getId());
                                    }
                                }
                                if(!CommonUtil.isEmpty(urls)){
                                    for(String operateUrl : urls){
                                        try {
                                            HttpClientUtil.httpClientPostByTest(operateUrl, null);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                sessionFacade.saveOrUpdate(plan);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time1 = sdf.format(plan.getPlanEndTime());
                String time2 = sdf.format(plan.getActualEndTime());
                long endTime = DateUtil.stringtoDate(time1, DateUtil.LONG_DATE_FORMAT).getTime();
                long actualTime = DateUtil.stringtoDate(time2, DateUtil.LONG_DATE_FORMAT).getTime();
                if ((actualTime < endTime)) {
                    resourceEverydayuseService.delResourceUseByPlanAndOperationTime(plan.getId(),
                            plan.getActualEndTime());
                }
            }

            List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where preposePlanId = ?",plan.getId());  //查询所有前置计划为当前计划的计划列表
            List<Plan> planList = new ArrayList<>();
            if(!CommonUtil.isEmpty(preposePlanList)){
                for(PreposePlan prepose : preposePlanList){
                    Plan pl = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPlanId());
                    planList.add(pl);
                }
            }

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pl : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pl.getBizCurrent())){
                        updateBizCurrent(pl);
                        //更新进度
                        updateProgressRate(pl);
                    }
                }
            }
            sessionFacade.batchUpdate(planList);

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pla : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pla.getBizCurrent())){
                        if(CommonUtil.isEmpty(pla.getPlanReceivedProcInstId())){
                            startPlanReceivedProcess(pla,userId);
                        }else if(!CommonUtil.isEmpty(pla.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(pla.getPlanReceivedCompleteTime())){
                            startPlanReceivedProcess(pla,userId);
                        }

                    }
                }
            }

        }//完工反馈审批通过
        else if (PlanConstants.PLAN_FEEDBACKING.equals(plan.getBizCurrent())) {
            allFinishMsg = id;
            logInfo = PlanConstants.PLAN_LOGINFO_FINISH;
            flowStaus = PlanConstants.PLAN_FLOWSTATUS_NORMAL;
            plan.setBizCurrent(PlanConstants.PLAN_FINISH);
            plan.setFeedbackProcInstId(procInstId);
            plan.setBizCurrent(PlanConstants.PLAN_FINISH);
            plan.setFlowStatus(flowStaus);
            plan.setActualEndTime(new Date());
            plan.setProgressRate(getProgress(plan));

            boolean needHttp = false;
            if(!CommonUtil.isEmpty(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RD.equals(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_REVIEW.equals(plan.getTaskNameType())
                    && !NameStandardConstants.NAMESTANDARD_ACTIVECATEGORY_RISK.equals(plan.getTaskNameType())){
                needHttp = true;
            }
            if(needHttp){
                List<OutwardExtensionDto> outwardExtensionList = outwardExtensionService.getOutwardExtensionList(appKey,"flowActiveCategoryHttpServer");
                if (!CommonUtil.isEmpty(outwardExtensionList)) {
                    for (OutwardExtensionDto ext : outwardExtensionList) {
                        if (!CommonUtil.isEmpty(ext.getUrlList())) {
                            List<String> urls = new ArrayList<String>();
                            for (OutwardExtensionUrlDto out : ext.getUrlList()) {
                                if ("finishAllTaskStep".equals(out.getOperateCode())) {
                                    urls.add(out.getOperateUrl() + "&planId=" + plan.getId());
                                }
                            }
                            if(!CommonUtil.isEmpty(urls)){
                                for(String operateUrl : urls){
                                    try {
                                        HttpClientUtil.httpClientPostByTest(operateUrl, null);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                }
            }

            sessionFacade.saveOrUpdate(plan);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time1 = sdf.format(plan.getPlanEndTime());
            String time2 = sdf.format(plan.getActualEndTime());
            long endTime = DateUtil.stringtoDate(time1, DateUtil.LONG_DATE_FORMAT).getTime();
            long actualTime = DateUtil.stringtoDate(time2, DateUtil.LONG_DATE_FORMAT).getTime();
            if ((actualTime < endTime)) {
                resourceEverydayuseService.delResourceUseByPlanAndOperationTime(plan.getId(),
                        plan.getActualEndTime());
            }

            List<PreposePlan> preposePlanList = sessionFacade.findHql("from PreposePlan where preposePlanId = ?",plan.getId());  //查询所有前置计划为当前计划的计划列表
            List<Plan> planList = new ArrayList<>();
            if(!CommonUtil.isEmpty(preposePlanList)){
                for(PreposePlan prepose : preposePlanList){
                    Plan pl = (Plan)sessionFacade.getEntity(Plan.class,prepose.getPlanId());
                    planList.add(pl);
                }
            }

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pl : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_LAUNCHED.equals(pl.getBizCurrent())){
                        updateBizCurrent(pl);
                        //更新进度
                        updateProgressRate(pl);
                    }
                }
            }
            sessionFacade.batchUpdate(planList);

            if(!CommonUtil.isEmpty(planList)){
                for(Plan pla : planList){
                    if(PlanConstants.PLAN_FLOWSTATUS_TOBERECEIVED.equals(pla.getBizCurrent())){
                        if(CommonUtil.isEmpty(pla.getPlanReceivedProcInstId())){
                            startPlanReceivedProcess(pla,userId);
                        }else if(!CommonUtil.isEmpty(pla.getPlanReceivedProcInstId()) && !CommonUtil.isEmpty(pla.getPlanReceivedCompleteTime())){
                            startPlanReceivedProcess(pla,userId);
                        }

                    }
                }
            }
        }
        try{
            boolean rdflowPluginValid = pluginValidateService.isValidatePlugin(PluginConstants.RDFLOW_PLUGIN_NAME);
            if(rdflowPluginValid){
                String linkFlag = "false";
                FeignJson linkFlagFj = rdFlowTaskFlowResolveFeignService.isHaveLinkPlanId(id);
                if (linkFlagFj.isSuccess()) {
                    linkFlag = linkFlagFj.getObj() == null ? "false" : linkFlagFj.getObj().toString();
                }
                if (linkFlag.equals("true")) {
                    rdFlowTaskFlowResolveFeignService.saveRdTaskFeedBackInfoByPlanTaskFeedBackInfo(id,procInstId,plan.getBizCurrent(),user,leader);
                }
            }

        }catch(Exception e){

        }


        if(StringUtils.isNotEmpty(allFinishMsg)){
            try {
                planFinishMsgNotice.getAllMessage(allFinishMsg,user);
            }
            catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    @Override
    public PageList queryEntityForPrepose(List<ConditionVO> conditionList, String projectId, String userName, String progressRate, String workTime) {
        StringBuffer hqlBuffer = createStringBuffer(projectId);

        if (StringUtils.isNotEmpty(userName)) {
            hqlBuffer.append("  and (OU.realName like '%" + userName + "%'");
            hqlBuffer.append("  or OU.userName like '%" + userName + "%')");
        }
        hqlBuffer.append("and PL.bizCurrent <> '" + PlanConstants.PLAN_INVALID + "'");
        for (ConditionVO c : conditionList) {
            if (c.getKey().equals("Plan.progressRate")) {
                if (StringUtils.isNotEmpty(progressRate)) {
                    if ("-1".equals(progressRate) && c.getCondition().equals("le")) {
                        hqlBuffer.append("  and PL.progressRate = " + progressRate);
                    }
                    else if ("-1".equals(progressRate) && c.getCondition().equals("ge")) {

                    }
                    else {
                        // int progressRateNumber = (Integer.parseInt(progressRate));
                        if (c.getCondition().equals("le")) {
                            hqlBuffer.append(" and (PL.progressRate is null");
                            hqlBuffer.append(" or PL.progressRate <= " + progressRate + ")");
                        }
                        else {
                            hqlBuffer.append(" and PL.progressRate >= " + progressRate);
                        }
                    }
                }
            }
            else if (c.getKey().equals("Plan.workTime")) {
                if (StringUtils.isNotEmpty(workTime)) {
                    if ("-1".equals(workTime) && c.getCondition().equals("le")) {
                        hqlBuffer.append(" and PL.workTime = " + workTime);
                    }
                    else if ("-1".equals(workTime) && c.getCondition().equals("ge")) {

                    }
                    else {
                        // int progressRateNumber = (Integer.parseInt(progressRate));
                        if (c.getCondition().equals("le")) {
                            hqlBuffer.append(" and (PL.workTime is null");
                            hqlBuffer.append(" or PL.workTime <= " + workTime + ")");
                        }
                        else {
                            hqlBuffer.append(" and PL.workTime >= " + workTime);
                        }
                    }
                }
            }
            else if (c.getKey().equals("Plan.planStartTime")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValue().split(",");
                    hqlBuffer.append(" and to_date(to_char(PL.planStartTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }
            else if (c.getKey().equals("Plan.planEndTime")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValue().split(",");
                    hqlBuffer.append(" and to_date(to_char(PL.planEndTime,'yyyy-MM-dd'),'yyyy-MM-dd')  between to_date('"
                            + attr[0]
                            + "','yyyy-MM-dd') and to_date('"
                            + attr[1]
                            + "','yyyy-MM-dd')");
                }
            }
            else if (c.getKey().equals("Plan.planLevelInfo.id")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.PLANLEVEL in (" + str + ")");
                    }
                }

            }
            else if (c.getKey().equals("Plan.planNumber")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    hqlBuffer.append(" and PL.planNumber like '%" + c.getValue() + "%'");
                }
            }
            else if (c.getKey().equals("Plan.planName")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    hqlBuffer.append(" and PL.planName like '%" + c.getValue() + "%'");
                }
            }
            else if (c.getKey().equals("Plan.bizCurrent")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.bizCurrent in (" + str + ")");
                    }
                }
            }
            else if (c.getKey().equals("Plan.taskNameType")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.taskNameType in (" + str + ")");
                    }
                }
            }
            else if (c.getKey().equals("Plan.taskType")) {
                if (StringUtils.isNotEmpty(c.getValue())) {
                    String[] attr = c.getValueArr();
                    String str = "";
                    for (String s : attr) {
                        if (StringUtils.isNotEmpty(s)) {
                            if (StringUtils.isNotEmpty(str.trim())) {
                                str = str + ",'" + s.trim() + "'";
                            }
                            else {
                                str = "'" + s.trim() + "'";
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(str.trim())) {
                        hqlBuffer.append(" and PL.taskType in (" + str + ")");
                    }
                }
            }
        }
        hqlBuffer.append(" ORDER BY PL.PARENTPLANID, PL.STOREYNO, PL.CREATETIME");

        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(hqlBuffer.toString());

        List<Plan> list = changeQueryResToPlanList(objArrayList);

        List<PlanDto> resultList = CodeUtils.JsonListToList(list, PlanDto.class);
        for (PlanDto planDto:resultList) {
            String conCode = getInfoByPlanIdAndUserId(planDto.getId(),planDto.getCreateBy());
            planDto.setConcernCode(conCode);
        }

        PageList pageList = new PageList(list.size(), resultList);
        return pageList;
    }

    @Override
    public List<TreeNode> getProcPlanList(String projectId, String userId) {
        //判断项目状态是否是暂停或关闭
        Project project = projectService.getProjectEntity(projectId);
        boolean flag = false;
        if (project.getBizCurrent().equals(ProjectConstants.PAUSED) || project.getBizCurrent().equals(ProjectConstants.CLOSED)) {
            flag = true;
        }
        //查询该项目下流程计划的没有废弃父计划
        List<TreeNode> treeNodes = new ArrayList<>();
        List<Plan> results = new ArrayList<>();
        List<Plan> procPlanList = new ArrayList<>();
        String sql = "from Plan t where t.opContent='" + PlanConstants.PLAN_LOGINFO_FLOW_SPLIT + "' and t.projectId='" + projectId + "'"
                +" and t.bizCurrent <> '"+PlanConstants.PLAN_INVALID+"' and t.avaliable='1' order by t.planNumber asc";
        procPlanList = sessionFacade.findByQueryString(sql);
        //判断当前用户是否是项目经理,项目经理可以看到该项目下的所有流程计划
        boolean isTeamManager = projRoleService.isProjRoleByUserIdAndRoleCode(projectId,ProjectRoleConstants.PROJ_MANAGER,userId);
        if (!isTeamManager) {
            //如果子计划负责人或父计划负责人是当前用户，则添加该父计划
            for (int i = 0; i < procPlanList.size(); i++) {
                Plan plan = procPlanList.get(i);
                /*if (StringUtils.isNotBlank(plan.getOwner())) {
                    if (!plan.getOwner().equals(userId)) {
                        if (!childPlanOwnerIsCurUser(plan,userId)) {
                            procPlanList.remove(plan);
                        }
                    }
                } else {
                    procPlanList.remove(plan);
                }*/
                if (StringUtils.isNotBlank(plan.getOwner())) {
                    if (plan.getOwner().equals(userId)) {
                        results.add(plan);
                    } else {
                        if (childPlanOwnerIsCurUser(plan,userId)) {
                            results.add(plan);
                        }
                    }
                }
            }
        } else {
            results = procPlanList;
        }
        //list转换成TreeNode
        if (!CommonUtil.isEmpty(results)) {
            for (Plan plan : results) {
                TreeNode treeNode = transfer(plan);
                //控制流程分解按钮
                if (flag) {
                    treeNode.setTitle("");
                }
                treeNodes.add(treeNode);
            }
        }
        return treeNodes;
    }

    /**
     * 通过父计划判断其子计划负责人是否是当前用户
     * @param parent 父计划
     * @param userId 当前用户
     * @return
     */
    public Boolean childPlanOwnerIsCurUser(Plan parent, String userId) {
        boolean flag = false;
        List<Plan> childrens = sessionFacade.findByQueryString("from Plan t where t.parentPlanId = '" + parent.getId() + "' and t.avaliable='1'");
        if (!CommonUtil.isEmpty(childrens)) {
            for (Plan plan : childrens) {
                if (StringUtils.isNotBlank(plan.getOwner())) {
                    if (plan.getOwner().equals(userId)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;

    }

    /**
     * 转换树节点
     *
     * @param plan
     * @return
     * @see
     */
    private TreeNode transfer(Plan plan) {
        TreeNode node = new TreeNode();
        node.setId(plan.getId());
        node.setTitle(plan.getBizCurrent());
        node.setName(plan.getPlanName());
        node.setDataObject(plan.getOwner());
        node.setOpen(true);
        return node;
    }

    @Override
    public FeignJson checkisParentChildAllPublish(String id) {
        FeignJson j = new FeignJson();
        String msg = "";
        try {
            if (!CommonUtil.isEmpty(id)) {
                Plan parentPlan = (Plan)sessionFacade.get(Plan.class, id);
                Boolean flag = false;
                if(!CommonUtil.isEmpty(parentPlan.getFormId())) {
                    ApprovePlanForm approvePlanForm = new ApprovePlanForm();
                    approvePlanForm.setId(parentPlan.getFormId());
                    List<Plan> planList = getPlanListByApprovePlanForm(approvePlanForm);
                    Map<String, String> idAndParentMap = new  HashMap<String, String>();
                    for(Plan curPlan : planList) {
                        if(!CommonUtil.isEmpty(curPlan.getParentPlanId()) && curPlan.getParentPlanId().equals(id)) {
                            flag = true;
                            break;
                        }
                    }
                }
                Boolean endFlag = false;
                String formId = parentPlan.getFormId();
                ApprovePlanFormDto approvePlanFormDto = getApprovePlanFormEntity(formId);
                MyStartedTaskDto myStartedTaskDto = workFlowFacade.getWorkFlowStartedTaskService().getMyStartedTaskByProcInstId(approvePlanFormDto.getProcInstId());
                if(!CommonUtil.isEmpty(myStartedTaskDto) && !CommonUtil.isEmpty(myStartedTaskDto.getProcType())){
                    if("rdTaskSubmitProcess".equals(myStartedTaskDto.getProcType())){
                        endFlag = true;
                    };
                }
                if (flag && endFlag) {
                    j.setMsg("此计划经行过父子一起发布，再提交父计划不能单独发布");
                    j.setSuccess(true);
                }else{
                    j.setSuccess(false);
                }
            }

        }
        catch (Exception e) {
            j.setSuccess(false);
        }
        finally {
            return j;
        }
    }

    @Override
    public Map<String, PlanDto> queryPlanMap(String projectId, String avaliable, List<String> planIdList) {
        Map<String, PlanDto> resultMap = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from PM_PLAN P where  1 = 1");
        if (StringUtils.isNotBlank(projectId)) {
            sb.append(" and P.PROJECTID = '" + projectId + "'");
        }
        if (!CommonUtil.isEmpty(planIdList)) {
            List<String> idsLists = new ArrayList<>();
            planIdList.forEach(str -> {
                idsLists.add("'"+str+"'");
            });
            String id = String.join(",",idsLists);
            sb.append(" and P.ID in ("+id+")");
        }
        if (StringUtils.isNotBlank(avaliable)) {
            sb.append(" and P.AVALIABLE = " + avaliable + "");
        }
        List<Map<String, Object>> objArrayList = this.sessionFacade.findForJdbc(sb.toString());
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("id");
                if (StringUtils.isNotEmpty(id)) {
                    PlanDto dto = new PlanDto();
                    dto.setId(id);
                    dto.setPlanName(map.get("PLANNAME") == null ? "" : map.get("PLANNAME").toString());
                    String owner = map.get("OWNER") == null ? "" : map.get("OWNER").toString();
                    if (StringUtils.isNotBlank(owner)) {
                        TSUserDto ownerInfo = userService.getUserByUserId(owner);
                        dto.setOwnerInfo(ownerInfo);
                    }
                    dto.setBizCurrent(map.get("BIZCURRENT") == null ? "" : map.get("BIZCURRENT").toString());
                    resultMap.put(id, dto);
                }
            }
        }
        return resultMap;
    }

    @Override
    public void updatePlanInfo(String formId) {
        ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
        approvePlanInfo.setFormId(formId);
        List<ApprovePlanInfo> approve = queryAssignList(approvePlanInfo, 1, 10, false);
        for (ApprovePlanInfo q2 : approve) {
            TemporaryPlan q3 = (TemporaryPlan) sessionFacade.getEntity(TemporaryPlan.class, q2.getPlanId());
            Plan q4 = (Plan) sessionFacade.getEntity(Plan.class, q3.getPlanId());
            q4.setFlowStatus(PlanConstants.PLAN_FLOWSTATUS_NORMAL);
            sessionFacade.saveOrUpdate(q4);
            //planService.delete(q2);
            try {
//                planChangeBackMsgNotice.getAllMessage(q4.getId(), UserUtil.getInstance().getUser());
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    @Override
    public List<Plan> queryPlanListForTime(Plan plan, int page, int rows, boolean isPage) {
        String hql = createHqlForTime(plan);
        if (isPage) {
            return sessionFacade.pageList(hql, (page - 1) * rows, rows);
        }
        return sessionFacade.findByQueryString(hql);
    }

    @Override
    public List<PreposePlanDto> allPreposePlanList(String projId) {
        List<PreposePlan> allPreposePlanList = this.sessionFacade.findHql("from PreposePlan t where t.planInfo.projectId = ?", projId);
        List<PreposePlanDto> results = new ArrayList<>();
        try{
            results = (List<PreposePlanDto>) VDataUtil.getVDataByEntity(allPreposePlanList,PreposePlanDto.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    /**
     * 根据Plan中的传值拼接HQL
     *
     * @param plan
     * @return
     * @see
     */
    private String createHqlForTime(Plan plan) {
        String hql = "from Plan l where l.bizCurrent != 'EDITING' and l.bizCurrent != 'INVALID' and l.bizCurrent != 'FINISH'";
        return hql;
    }
}

