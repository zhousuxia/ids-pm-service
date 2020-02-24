package com.glaway.ids.statisticalAnalysis.service.impl;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.DateUtil;
import com.glaway.foundation.common.util.JsonUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.common.constant.PluginConstants;
import com.glaway.ids.common.service.PluginValidateServiceI;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.config.entity.ParamSwitch;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.statisticalAnalysis.service.StatisticalAnalysisServiceI;
import com.glaway.ids.statisticalAnalysis.vo.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 统计分析接口
 * @author: sunmeng
 * @ClassName: StatisticalAnalysisServiceImpl
 * @Date: 2019/8/16-17:14
 * @since
 */
@Service("statisticalAnalysisService")
@Transactional
public class StatisticalAnalysisServiceImpl extends CommonServiceImpl implements StatisticalAnalysisServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private PluginValidateServiceI pluginValidateService;

    @Autowired
    private ParamSwitchServiceI paramSwitchService;

    @Autowired
    private PlanServiceI planService;

    @Autowired
    private FeignUserService userService;

    @Override
    public List<ProjectBoardVo> getProjectBoardVoList(String uid, int page, int rows,
                                                      boolean isPage) {
        List<ProjectBoardVo> voList = new ArrayList<ProjectBoardVo>();
        String planLevel = getKeyPlanLevel();
        String commonSql = " SELECT PROJ.ID, "
                + " PROJ.NAME, "
                + " PROJ.PROJECTNUMBER, "
                + " u.realname || '-' || u.username uname, "
                + " u.id  uuid, "
                + " to_char(proj.startprojecttime, 'yyyy.mm.dd')||'-'||to_char(proj.endprojecttime, 'yyyy.mm.dd') time"
                + " FROM FD_TEAM_ROLE_USER RU, "
                + " FD_TEAM_ROLE      ROLE, "
                + " PM_PROJ_TEAM_LINK LINK, "
                + " PM_PROJECT        PROJ, "
                + " t_s_base_user     u "
                + " WHERE RU.ROLEID = ROLE.ROLEID "
                + " AND LINK.TEAMID = RU.TEAMID "
                + " AND LINK.PROJECTID = PROJ.ID "
                + " and u.id = ru.userid "
                + " and proj.bizcurrent = 'STARTING' "
                + " UNION "
                + " SELECT PROJ.ID, "
                + " PROJ.NAME, "
                + " PROJ.PROJECTNUMBER, "
                + " u.realname || '-' || u.username uname, "
                + " u.id  uuid, "
                + " to_char(proj.startprojecttime, 'yyyy.mm.dd')||'-'||to_char(proj.endprojecttime, 'yyyy.mm.dd') time"
                + " FROM FD_TEAM_ROLE_GROUP RG, " + " T_S_GROUP_USER     GU, "
                + " FD_TEAM_ROLE       ROLE, " + " PM_PROJ_TEAM_LINK  LINK, "
                + " PM_PROJECT         PROJ, " + " t_s_base_user      u "
                + " WHERE RG.ROLEID = ROLE.ROLEID " + " AND RG.GROUPID = GU.GROUPID "
                + " AND LINK.TEAMID = RG.TEAMID " + " AND LINK.PROJECTID = PROJ.ID "
                + " and u.id = gu.userid " + " and proj.bizcurrent = 'STARTING' ";

        String commonSqlForManager = " SELECT PROJ.ID, "
                + " PROJ.NAME, "
                + " PROJ.PROJECTNUMBER, "
                + " u.realname || '-' || u.username uname, "
                + " u.id  uuid, "
                + " to_char(proj.startprojecttime, 'yyyy.mm.dd')||'-'||to_char(proj.endprojecttime, 'yyyy.mm.dd') time"
                + " FROM FD_TEAM_ROLE_USER RU, "
                + " FD_TEAM_ROLE      ROLE, "
                + " PM_PROJ_TEAM_LINK LINK, "
                + " PM_PROJECT        PROJ, "
                + " t_s_base_user     u "
                + " WHERE RU.ROLEID = ROLE.ROLEID "
                + " AND ROLE.ROLECODE = 'manager' "
                + " AND LINK.TEAMID = RU.TEAMID "
                + " AND LINK.PROJECTID = PROJ.ID "
                + " and u.id = ru.userid "
                + " and proj.bizcurrent = 'STARTING' "
                + " UNION "
                + " SELECT PROJ.ID, "
                + " PROJ.NAME, "
                + " PROJ.PROJECTNUMBER, "
                + " u.realname || '-' || u.username uname, "
                + " u.id  uuid, "
                + " to_char(proj.startprojecttime, 'yyyy.mm.dd')||'-'||to_char(proj.endprojecttime, 'yyyy.mm.dd') time"
                + " FROM FD_TEAM_ROLE_GROUP RG, "
                + " T_S_GROUP_USER     GU, " + " FD_TEAM_ROLE       ROLE, "
                + " PM_PROJ_TEAM_LINK  LINK, " + " PM_PROJECT         PROJ, "
                + " t_s_base_user      u "
                + " WHERE RG.ROLEID = ROLE.ROLEID "
                + " AND ROLE.ROLECODE = 'manager' "
                + " AND RG.GROUPID = GU.GROUPID "
                + " AND LINK.TEAMID = RG.TEAMID "
                + " AND LINK.PROJECTID = PROJ.ID " + " and u.id = gu.userid "
                + " and proj.bizcurrent = 'STARTING' ";
        String condition = "";
        if (StringUtils.isNotEmpty(uid)) {
            condition = " where a3.uuid = '" + uid + "'";
        }
        String planLevelCondition = "";
        if (StringUtils.isNotEmpty(planLevel)) {
            planLevelCondition = " where A1.PLANLEVEL in (" + planLevel + ")";
        }
        String sqlForProj = " SELECT distinct p.projectnumber, p.id projectid, case when a1.O1 >0 then a1.O1 else 0 end O1 , case when a2.O2 >0 then a2.O2 else 0 end O2 "
                + " FROM pm_project p "
                + " left join (SELECT V1.PROJECTID, V1.NUM O1, v1.PROJECTNUMBER "
                + " FROM (SELECT A1.PROJECTID, "
                + " A1.PROJECTNUMBER, A1.dep, '计划' CF, COUNT(ID) NUM "
                + " FROM view_report_project_plan_info A1 "
                + planLevelCondition
                + " GROUP BY A1.PROJECTID, A1.PROJECTNUMBER, A1.dep) V1 "
                + " WHERE V1.DEP = '已超期计划数') a1 on p.id = a1.projectid "
                + " left join (SELECT v2.PROJECTID, "
                + " (CASE WHEN V2.NUM IS NULL THEN 0 ELSE V2.NUM END) O2, "
                + " V2.PROJECTNUMBER "
                + " FROM view_project_resource V2 "
                + " WHERE V2.dep = '超期') a2 on a2.PROJECTID = p.id "
                +" where  p.id in "
                +" (select link.projectId "
                +" from PM_PROJ_TEAM_LINK link "
                +" where link.teamId in "
                +" ((select u.teamId from fd_team_role_user u  where u.userId = '"+uid+"') union all "
                +" (select rg.teamId from fd_team_role_group rg, t_s_group_user gu where 1 = 1 "
                +" and gu.groupId = rg.groupId and gu.userId = '"+uid+"')) union all (select t.id "
                +" from PM_PROJECT t where t.createBy = '"+uid+"')) "
                + " ORDER BY O1 DESC, O2 DESC, p.PROJECTNUMBER ";
        if (isPage) {
            sqlForProj = " select a5.* from (select a4.projectid, a4.O1, a4.O2, rownum rn "
                    + " from ( " + sqlForProj + ")a4 where rownum <= " + page * rows
                    + ") a5 where a5.rn > " + (page - 1) * rows;
        }
        List<String> projectIdList = new ArrayList<String>();
        List<Map<String, Object>> objArrayListP = sessionFacade.findForJdbc(sqlForProj);
        if (!CommonUtil.isEmpty(objArrayListP)) {
            for (Map<String, Object> map : objArrayListP) {
                int o1 = Integer.parseInt(map.get("O1").toString());
                int o2 = Integer.parseInt(map.get("O2").toString());
                if (o1 > 0) {
                    projectIdList.add(map.get("projectId").toString() + "," + "red");
                }
                else if (o1 == 0 && o2 > 0) {
                    projectIdList.add(map.get("projectId").toString() + "," + "orange");
                }
                else {
                    projectIdList.add(map.get("projectId").toString() + "," + "green");
                }
            }
        }
        //组装项目看板头部信息
        if (projectIdList.size() > 0) {
            int i = 0;
            for (String s : projectIdList) {
                i++ ;
                String projectId = s.split(",")[0];
                String color = s.split(",")[1];
                String conditionP = " where a1.id = '" + projectId + "'";
                String riskSql = " SELECT 0 num, '' mname, '' pname, '' time, COUNT(R.ID) rednum "
                        + " FROM PM_RISKINVENTORY_TARGET R "
                        + " JOIN CM_RISK_SCORE_MODEL MP ON MP.TYPECODE = R.PROBABILITYTYPECODE "
                        + " JOIN CM_RISK_SCORE_MODEL MI ON MI.TYPECODE = R.influencetypecode "
                        + " JOIN CM_RISK_SCORE_MODEL ML ON ML.TYPECODE = 'risklevel_high' "
                        + " JOIN PM_RISK_PROBLEM_CATEGORY CATEGORY ON CATEGORY.INVENTORYTARGETID = R.ID "
                        + " JOIN PM_RISK_PROBLEM_INFO INFO ON (CATEGORY.ID = INFO.PROBLEMCATEGORYID) "
                        + " WHERE R.PROJECTID = '" + projectId + "' "
                        + " AND R.STATUS = '1' "
                        + " AND (ML.SCOREINTERVAL_START <= MP.SCORE * MI.SCORE AND "
                        + " MP.SCORE * MI.SCORE <= ML.SCOREINTERVAL_END) "
                        + " AND INFO.BIZCURRENT <> 'CLOSED'";
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append(" select wm_concat(t.num) unum, ");
                sqlBuffer.append(" wm_concat(t.mname) mname, ");
                sqlBuffer.append(" wm_concat(t.pname) pname,  ");
                sqlBuffer.append(" wm_concat(t.time) time,  ");
                sqlBuffer.append(" wm_concat(t.rednum) color ");
                sqlBuffer.append(" from (select count(a1.uname) num, '' mname, '' pname, '' time, 0 rednum ");
                sqlBuffer.append(" from (" + commonSql + " ) a1 ");
                sqlBuffer.append(" join pm_project proj on proj.id = a1.id " + conditionP );
                sqlBuffer.append(" union ");
                sqlBuffer.append(" select 0 num, wm_concat(a1.uname) mname, '' pname, '' time, 0 rednum ");
                sqlBuffer.append(" from ( " + commonSqlForManager + " ) a1 ");
                sqlBuffer.append(" join pm_project proj on proj.id = a1.id " + conditionP );
                sqlBuffer.append(" union  ");
                sqlBuffer.append(" select distinct 0 num, '' mname, a1.name pname, '' time, 0 rednum ");
                sqlBuffer.append(" from ( " + commonSql + " ) a1 ");
                sqlBuffer.append(" join pm_project proj on proj.id = a1.id " + conditionP );
                sqlBuffer.append(" union ");
                sqlBuffer.append(" select 0 num, '' mname, '' pname, a1.time time, 0 rednum ");
                sqlBuffer.append(" from ( " + commonSql + " ) a1 ");
                sqlBuffer.append(" join pm_project proj on proj.id = a1.id " + conditionP );
                if(pluginValidateService.isValidatePlugin(PluginConstants.RISK_PLUGIN_NAME)){
                    sqlBuffer.append(" union " + riskSql );
                }
                sqlBuffer.append(" ) t  ");
                String querySql = sqlBuffer.toString();
                List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(querySql);
                if (!CommonUtil.isEmpty(objArrayList)) {
                    for (Map<String, Object> map : objArrayList) {
                        ProjectBoardVo vo = new ProjectBoardVo();
                        vo.setPname(StringUtils.isEmpty((String)map.get("pname")) ? "" : map.get(
                                "pname").toString());
                        String mname = StringUtils.isEmpty((String)map.get("mname")) ? "" : map.get(
                                "mname").toString();
                        vo.setMname(mname);
                        if (mname.length() > 20) {
                            vo.setShowName(mname.substring(0, 20) + "...");
                        }
                        String[] numArray = map.get("unum").toString().split(",");
                        int num = Integer.parseInt(numArray[0]);
                        for (String na : numArray) {
                            if (Integer.parseInt(na) > num) {
                                num = Integer.parseInt(na);
                            }
                        }
                        vo.setUnum(String.valueOf(num) + "人");
                        vo.setTime(StringUtils.isEmpty((String)map.get("time")) ? "" : map.get(
                                "time").toString());
                        vo.setPid(projectId);
                        vo.setRownum(String.valueOf(i));
                        //头部颜色信息
                        String[] colorNumArray = map.get("color").toString().split(",");
                        int rednum = Integer.parseInt(numArray[0]);
                        for (String na : colorNumArray) {
                            if (Integer.parseInt(na) > rednum) {
                                num = Integer.parseInt(na);
                            }
                        }
                        if(rednum > 0){
                            vo.setColor("red");
                        }else{
                            vo.setColor(color);
                        }
                        vo.setPlanlevel(planLevel);
                        voList.add(vo);
                    }
                }
            }
        }

        return voList;
    }


    @Override
    public int getProjectBoardVoListSize(String uid, int page, int rows, boolean isPage) {
        int dataSize = 0;
        String planLevel = getKeyPlanLevel();

        String planLevelCondition = "";
        if (StringUtils.isNotEmpty(planLevel)) {
            planLevelCondition = " where A1.PLANLEVEL in (" + planLevel + ")";
        }
        String sqlForProj = " SELECT distinct p.projectnumber, p.id projectid, case when a1.O1 >0 then a1.O1 else 0 end O1 , case when a2.O2 >0 then a2.O2 else 0 end O2 "
                + " FROM pm_project p "
                + " left join (SELECT V1.PROJECTID, V1.NUM O1, v1.PROJECTNUMBER "
                + " FROM (SELECT A1.PROJECTID, "
                + " A1.PROJECTNUMBER, A1.dep, '计划' CF, COUNT(ID) NUM "
                + " FROM view_report_project_plan_info A1 "
                + planLevelCondition
                + " GROUP BY A1.PROJECTID, A1.PROJECTNUMBER, A1.dep) V1 "
                + " WHERE V1.DEP = '已超期计划数') a1 on p.id = a1.projectid "
                + " left join (SELECT v2.PROJECTID, "
                + " (CASE WHEN V2.NUM IS NULL THEN 0 ELSE V2.NUM END) O2, "
                + " V2.PROJECTNUMBER "
                + " FROM view_project_resource V2 "
                + " WHERE V2.dep = '超期') a2 on a2.PROJECTID = p.id "
                +" where  p.id in "
                +" (select link.projectId "
                +" from PM_PROJ_TEAM_LINK link "
                +" where link.teamId in "
                +" ((select u.teamId from fd_team_role_user u  where u.userId = '"+uid+"') union all "
                +" (select rg.teamId from fd_team_role_group rg, t_s_group_user gu where 1 = 1 "
                +" and gu.groupId = rg.groupId and gu.userId = '"+uid+"')) union all (select t.id "
                +" from PM_PROJECT t where t.createBy = '"+uid+"')) "
                + " ORDER BY O1 DESC, O2 DESC, p.PROJECTNUMBER ";
        if (isPage) {
            sqlForProj = " select a5.* from (select a4.projectid, a4.O1, a4.O2, rownum rn "
                    + " from ( " + sqlForProj + ")a4 where rownum <= " + page * rows
                    + ") a5 where a5.rn > " + (page - 1) * rows;
        }
        List<Map<String, Object>> objArrayListP = sessionFacade.findForJdbc(sqlForProj);
        dataSize = objArrayListP.size();
        return dataSize;
    }

    @Override
    public List<ProjectAnalysisVo> getMilestoneVoList(String pid) {
        String planLevel = getKeyPlanLevel();
        String plCondition = "";
        if (StringUtils.isNotEmpty(planLevel)) {
            plCondition = "or t.planLevel in (" + planLevel + ")";
        }
        String hql = "from Plan t where t.avaliable='1' and (t.milestone = 'true' "
                + plCondition
                + ") and t.projectId = '"
                + pid
                + "' and t.bizCurrent NOT IN ('EDITING', 'INVALID') order by t.planEndTime asc";
        List<Plan> planList = findByQueryString(hql);
        List<ProjectAnalysisVo> voList = new ArrayList<ProjectAnalysisVo>();
        int mcount = 1;// 用于显示左侧标记
        int planSizeFlag = planList.size() - 1;
        List<TSUserDto> userList = userService.getAllUsers();
        Map<String, String> userMap = new HashMap<String, String>();
        for (TSUserDto u : userList) {
            userMap.put(u.getId(), u.getRealName() + "-" + u.getUserName());
        }
        Plan pl = new Plan();
        planService.initBusinessObject(pl);
        List<LifeCycleStatus> lifeCycleStatus = pl.getPolicy().getLifeCycleStatusList();
        Map<String, String> statusMap = new HashMap<String, String>();
        for (LifeCycleStatus s : lifeCycleStatus) {
            statusMap.put(s.getName(), s.getTitle());
        }
        for (Plan p : planList) {
            ProjectAnalysisVo vo = new ProjectAnalysisVo();
            if (p.getId().equals(planList.get(planSizeFlag).getId())) {
                if ("true".equals(p.getMilestone())) { // 是里程碑时标记为1
                    vo.setMilestone("1");
                    vo.setMcount("" + mcount);
                    mcount++ ;
                    vo.setDivflag("0");// div标记 1为开始结束 0为结束
                }
            }
            else {
                if ("true".equals(p.getMilestone())) { // 是里程碑时标记为1
                    vo.setMilestone("1");
                    vo.setMcount("" + mcount);
                    mcount++ ;
                    vo.setDivflag("1");
                }
            }
            vo.setStarttime(DateUtil.dateToString(p.getPlanStartTime(), DateUtil.LONG_DATE_FORMAT));
            vo.setEndtime(DateUtil.dateToString(p.getPlanEndTime(), DateUtil.LONG_DATE_FORMAT));
            vo.setWorktime(p.getWorkTime());
            if (p.getOwnerInfo() != null) {
                vo.setAname(userMap.get(p.getOwnerInfo().getId()));
            }
            vo.setStatus(statusMap.get(p.getBizCurrent()));
            if (p.getBizCurrent().equals("FINISH")) {
                vo.setFinish("1");
            }
            else {
                vo.setFinish("0");
            }
            vo.setPname(p.getPlanName());
            if (p.getProgressRate() == null) {
                vo.setRate("0%");
            }
            else {
                vo.setRate(p.getProgressRate() + "%");
            }

            voList.add(vo);
        }

        return voList;
    }

    @Override
    public List<MonthRateVo> getMonthRateVoList(String pid, String year) {
        List<MonthRateVo> voList = new ArrayList<MonthRateVo>();
        for (int i = 1; i <= 12; i++ ) {
            CompleteRateVo wbsVo = getWBSCompleteRateVo(pid, "'WBS计划'", year, i + "");
            CompleteRateVo taskVo = getCompleteRateVo(pid, "'任务计划','流程计划'", year, i + "");
            MonthRateVo vo = new MonthRateVo();
            vo.setMonth(i + "月");
            if (wbsVo.getTotal().equals("0") || StringUtils.isEmpty(wbsVo.getTotal())) {
                vo.setWbstotal("0");
                vo.setWbscomplete("0");
                vo.setWbsrate("-");
            }
            else {
                vo.setWbstotal(wbsVo.getTotal());
                vo.setWbscomplete(wbsVo.getComplete());
                vo.setWbsrate(wbsVo.getRate());
            }
            if (taskVo.getTotal().equals("0") || StringUtils.isEmpty(taskVo.getTotal())) {
                vo.setTasktotal("0");
                vo.setTaskcomplete("0");
                vo.setTaskrate("-");
            }
            else {
                vo.setTasktotal(taskVo.getTotal());
                vo.setTaskcomplete(taskVo.getComplete());
                vo.setTaskrate(taskVo.getRate());
            }
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public String getKeyPlanLevel() {
        ParamSwitch ps = new ParamSwitch();
        ps.setName("关键计划对应计划等级");
        List<ParamSwitch> psList = paramSwitchService.search(ps);
        if (psList.get(0).getStatus() != null && psList.get(0).getStatus() != "") {
            String levelStr = psList.get(0).getStatus().replace("+", "','");
            levelStr = "'" + levelStr + "'";
            String hql = "from BusinessConfig t where t.configType ='PLANLEVEL' and t.name in ("
                    + levelStr + ")";
            List<BusinessConfig> bcList = findByQueryString(hql);
            String res = "";
            for (BusinessConfig bc : bcList) {
                res = res + ",'" + bc.getId() + "'";
            }
            if(!CommonUtil.isEmpty(res)){
                return res.substring(1);
            }
        }
        return null;
    }

    @Override
    public CompleteRateVo getCompleteRateVo(String pid, String type, String year, String month) {
        CompleteRateVo wbsVo = new CompleteRateVo();
        double total = 0; // 总计划数
        double complete = 0; // 完成
        double uncomplete = 0; // 正常未完成
        double delay = 0; // 延期未完成
        String condition = "";

        if (year != null && year != "" && month != null && month != "") {
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
            condition = " and to_char(p.planendtime, 'yyyy-mm-dd') like '" + year + "-" + month
                    + "%'";
        }
        String totalSql = " SELECT COUNT(P.ID) c FROM PM_PLAN P WHERE P.PROJECTID = '" + pid
                + "' AND P.TASKTYPE in (" + type + ") AND P.AVALIABLE = '1'"
                + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID') "
                + " AND P.ID NOT IN (SELECT PL.PARENTPLANID "
                + " FROM PM_PLAN PL WHERE PL.PROJECTID = '" + pid + "'"
                + " AND PL.AVALIABLE = '1' AND PL.PARENTPLANID IS NOT NULL "
                + " AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID'))" + condition;
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(totalSql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            String c = objArrayList.get(0).get("c").toString();
            total = Double.parseDouble(c);
            wbsVo.setTotal(c);
        }

        if (total != 0) {
            String completeSql = " SELECT COUNT(P.ID) c FROM PM_PLAN P "
                    + " WHERE P.PROJECTID = '" + pid + "' AND P.TASKTYPE in (" + type
                    + ") AND P.AVALIABLE = '1' "
                    + " AND P.BIZCURRENT = 'FINISH' AND P.ID NOT IN "
                    + " (SELECT PL.PARENTPLANID FROM PM_PLAN PL "
                    + " WHERE PL.PROJECTID = '" + pid + "'"
                    + " AND PL.AVALIABLE = '1' "
                    + " AND PL.PARENTPLANID IS NOT NULL "
                    + " AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID'))" + condition;
            objArrayList = sessionFacade.findForJdbc(completeSql);
            if (!CommonUtil.isEmpty(objArrayList)) {
                String c = objArrayList.get(0).get("c").toString();
                complete = Double.parseDouble(c);
                wbsVo.setComplete(c);
            }

            String unCompleteSql = " SELECT COUNT(P.ID) c FROM PM_PLAN P "
                    + " WHERE P.PROJECTID = '"
                    + pid
                    + "' AND P.TASKTYPE in ("
                    + type
                    + ") AND P.AVALIABLE = '1' "
                    + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID', 'FINISH') "
                    + " AND to_date(to_char(P.PLANENDTIME, 'yyyy-mm-dd'), 'yyyy-mm-dd') >= "
                    + " to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') "
                    + " AND P.ID NOT IN (SELECT PL.PARENTPLANID "
                    + " FROM PM_PLAN PL WHERE PL.PROJECTID = '" + pid + "'"
                    + " AND PL.AVALIABLE = '1' "
                    + " AND PL.PARENTPLANID IS NOT NULL "
                    + " AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID'))"
                    + condition;
            objArrayList = sessionFacade.findForJdbc(unCompleteSql);
            if (!CommonUtil.isEmpty(objArrayList)) {
                String c = objArrayList.get(0).get("c").toString();
                uncomplete = Double.parseDouble(c);
                wbsVo.setUncomplete(c);
            }

            String delaySql = " SELECT COUNT(P.ID) c FROM PM_PLAN P "
                    + " WHERE P.PROJECTID = '"
                    + pid
                    + "' AND P.TASKTYPE in ("
                    + type
                    + ") AND P.AVALIABLE = '1' "
                    + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID', 'FINISH') "
                    + " AND to_date(to_char(P.PLANENDTIME, 'yyyy-mm-dd'), 'yyyy-mm-dd') < "
                    + " to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') "
                    + " AND P.ID NOT IN (SELECT PL.PARENTPLANID "
                    + " FROM PM_PLAN PL WHERE PL.PROJECTID = '" + pid + "'"
                    + " AND PL.AVALIABLE = '1' " + " AND PL.PARENTPLANID IS NOT NULL "
                    + " AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID'))" + condition;
            objArrayList = sessionFacade.findForJdbc(delaySql);
            if (!CommonUtil.isEmpty(objArrayList)) {
                String c = objArrayList.get(0).get("c").toString();
                delay = Double.parseDouble(c);
                wbsVo.setDelay(c);
            }
        }
        if ("'WBS计划'".equals(type)) {
            wbsVo.setType("底层" + PlanConstants.PLAN_TYPE_WBS);
        }
        else {
            wbsVo.setType("底层" + PlanConstants.PLAN_TYPE_TASK);
        }
        if (total == 0) {
            wbsVo.setRate("-");
            wbsVo.setComplete("" + (int)complete);
            wbsVo.setUncomplete("" + (int)uncomplete);
        }
        else {
            double res = (complete / total) * 100;
            wbsVo.setRate(formatDouble(res) + "%");
        }
        return wbsVo;
    }

    @Override
    public CompleteRateVo getWBSCompleteRateVo(String pid, String type, String year, String month)
    {
        //修改BUG 计划统计分析 WBS计划达成率计算错误问题
        //底层wbs计划定义 :计划非拟制和废弃的状态   分类:
        //无子计划: 父计划底层WBS
        //有子计划 ：   子计划是WBS且为废弃和拟制中   父计划为底层WBS   子计划是任务计划   父计划wie底层WBS计划
        CompleteRateVo wbsVo = new CompleteRateVo();
        double total = 0; // 总计划数
        double complete = 0; // 完成
        double uncomplete = 0; // 正常未完成
        double delay = 0; // 延期未完成
        String condition = "";

        if (year != null && year != "" && month != null && month != "") {
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
            condition = " and to_char(p.planendtime, 'yyyy-mm-dd') like '" + year + "-" + month
                    + "%'";
        }
        String totalSql = " SELECT COUNT(P.ID) c FROM PM_PLAN P WHERE P.PROJECTID = '" + pid
                + "' AND P.TASKTYPE in (" + type + ") AND P.AVALIABLE = '1'"
                + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID') "
                + " AND P.ID NOT IN (SELECT PL.PARENTPLANID "
                + " FROM PM_PLAN PL WHERE PL.PROJECTID = '" + pid + "'"
                + " AND PL.AVALIABLE = '1' AND PL.PARENTPLANID IS NOT NULL "
                + " AND PL.TASKTYPE in (" + type + ")"
                + " AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID'))" + condition;
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(totalSql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            String c = objArrayList.get(0).get("c").toString();
            total = Double.parseDouble(c);
            wbsVo.setTotal(c);
        }

        if (total != 0) {
            String completeSql = " SELECT COUNT(P.ID) c FROM PM_PLAN P "
                    + " WHERE P.PROJECTID = '" + pid + "' AND P.TASKTYPE in (" + type
                    + ") AND P.AVALIABLE = '1' "
                    + " AND P.BIZCURRENT = 'FINISH' AND P.ID NOT IN "
                    + " (SELECT PL.PARENTPLANID FROM PM_PLAN PL "
                    + " WHERE PL.PROJECTID = '" + pid + "'"
                    + " AND PL.AVALIABLE = '1' "
                    + " AND PL.PARENTPLANID IS NOT NULL "
                    + " AND PL.TASKTYPE in (" + type + ")"
                    + " AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID'))" + condition;
            objArrayList = sessionFacade.findForJdbc(completeSql);
            if (!CommonUtil.isEmpty(objArrayList)) {
                String c = objArrayList.get(0).get("c").toString();
                complete = Double.parseDouble(c);
                wbsVo.setComplete(c);
            }

            String unCompleteSql = " SELECT COUNT(P.ID) c FROM PM_PLAN P "
                    + " WHERE P.PROJECTID = '"
                    + pid
                    + "' AND P.TASKTYPE in ("
                    + type
                    + ") AND P.AVALIABLE = '1' "
                    + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID', 'FINISH') "
                    + " AND to_date(to_char(P.PLANENDTIME, 'yyyy-mm-dd'), 'yyyy-mm-dd') >= "
                    + " to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') "
                    + " AND P.ID NOT IN (SELECT PL.PARENTPLANID "
                    + " FROM PM_PLAN PL WHERE PL.PROJECTID = '" + pid + "'"
                    + " AND PL.TASKTYPE in (" + type + ")"
                    + " AND PL.AVALIABLE = '1' "
                    + " AND PL.PARENTPLANID IS NOT NULL "
                    + " AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID'))"
                    + condition;
            objArrayList = sessionFacade.findForJdbc(unCompleteSql);
            if (!CommonUtil.isEmpty(objArrayList)) {
                String c = objArrayList.get(0).get("c").toString();
                uncomplete = Double.parseDouble(c);
                wbsVo.setUncomplete(c);
            }

            String delaySql = " SELECT COUNT(P.ID) c FROM PM_PLAN P "
                    + " WHERE P.PROJECTID = '"
                    + pid
                    + "' AND P.TASKTYPE in ("
                    + type
                    + ") AND P.AVALIABLE = '1' "
                    + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID', 'FINISH') "
                    + " AND to_date(to_char(P.PLANENDTIME, 'yyyy-mm-dd'), 'yyyy-mm-dd') < "
                    + " to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') "
                    + " AND P.ID NOT IN (SELECT PL.PARENTPLANID "
                    + " FROM PM_PLAN PL WHERE PL.PROJECTID = '" + pid + "'"
                    + " AND PL.AVALIABLE = '1' " + " AND PL.PARENTPLANID IS NOT NULL "
                    + " AND PL.TASKTYPE in (" + type + ")"
                    + " AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID'))" + condition;
            objArrayList = sessionFacade.findForJdbc(delaySql);
            if (!CommonUtil.isEmpty(objArrayList)) {
                String c = objArrayList.get(0).get("c").toString();
                delay = Double.parseDouble(c);
                wbsVo.setDelay(c);
            }
        }
        if ("'WBS计划'".equals(type)) {
            wbsVo.setType("底层" + PlanConstants.PLAN_TYPE_WBS);
        }
        else {
            wbsVo.setType("底层" + PlanConstants.PLAN_TYPE_TASK);
        }
        if (total == 0) {
            wbsVo.setRate("-");
            wbsVo.setComplete("" + (int)complete);
            wbsVo.setUncomplete("" + (int)uncomplete);
        }
        else {
            double res = (complete / total) * 100;
            wbsVo.setRate(formatDouble(res) + "%");
        }

        return wbsVo;
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    @Override
    public List<DelayTaskVo> getDelayTaskVoList(String pid, int page, int rows, boolean isPage) {
        List<DelayTaskVo> voList = new ArrayList<DelayTaskVo>();
        String sql = " SELECT P.ID id, "
                + " case when p.progressrate is null then '0%' else p.progressrate || '%' end rate, "
                + " P.Planname pname, "
                + " case when p.bizcurrent = 'ORDERED' then '执行中' else '完工确认' end status, "
                + " LV.NAME plevel, "
                + " OU.REALNAME || '-' || OU.USERNAME oname, "
                + " to_char(p.planstarttime, 'yyyy-mm-dd') stime, "
                + " to_char(p.planendtime, 'yyyy-mm-dd') etime, "
                + " AU.REALNAME || '-' || OU.USERNAME aname, "
                + " p.tasktype type, "
                + " to_char(p.assigntime, 'yyyy-mm-dd') atime "
                + " FROM PM_PLAN P "
                + " LEFT JOIN CM_BUSINESS_CONFIG LV ON (P.PLANLEVEL = LV.ID AND LV.CONFIGTYPE = 'PLANLEVEL') "
                + " LEFT JOIN T_S_BASE_USER OU ON (P.OWNER = OU.ID) "
                + " LEFT JOIN T_S_BASE_USER AU ON (P.ASSIGNER = AU.ID) "
                + " WHERE P.PROJECTID = '"
                + pid
                + "' AND P.AVALIABLE = '1' "
                + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID', 'FINISH') AND P.PLANENDTIME < to_date(to_char(sysdate, 'yyyy-MM-dd'), 'yyyy-MM-dd') "
                + " AND P.ID NOT IN " + " (SELECT PL.PARENTPLANID " + "  FROM PM_PLAN PL "
                + "  WHERE PL.PROJECTID = '" + pid + "'" + "  AND PL.AVALIABLE = '1' "
                + "  AND PL.PARENTPLANID IS NOT NULL "
                + "  AND PL.BIZCURRENT NOT IN ('EDITING', 'INVALID')) " + " ORDER BY P.ID ";
        if (isPage) {
            sql = "SELECT IDSRV.* FROM (SELECT IRV.*, ROWNUM RN FROM (" + sql
                    + ")IRV WHERE ROWNUM <= " + page * rows + ") IDSRV WHERE RN > " + (page - 1)
                    * rows;
        }
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                DelayTaskVo vo = new DelayTaskVo();
                vo.setId(map.get("id").toString());
                vo.setRate(map.get("rate").toString());
                vo.setPname(map.get("pname").toString());
                vo.setAname(map.get("aname").toString());
                vo.setAtime(map.get("atime").toString());
                vo.setEtime(map.get("etime").toString());
                vo.setLevel(StringUtils.isEmpty((String)map.get("plevel")) ? "" : map.get("plevel").toString());
                vo.setOname(map.get("oname").toString());
                vo.setStime(map.get("stime").toString());
                vo.setType(StringUtils.isEmpty((String)map.get("type")) ? "" : map.get("type").toString());
                vo.setStatus(map.get("status").toString());
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public String getYearCombobox() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int maxYear = Integer.parseInt(sdf.format(new Date()));
        String minYearStr = "0";
        String sql = " select to_char(min(p.startprojecttime), 'yyyy') minYear from pm_project p ";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        if (!CommonUtil.isEmpty(objArrayList)) {
            minYearStr = objArrayList.get(0).get("minYear").toString();
        }
        int minYear = Integer.parseInt(minYearStr);
        List<String> years = new ArrayList<String>();
        for (int i = minYear; i <= maxYear; i++ ) {
            years.add("" + i);
        }
        JSONArray jList = new JSONArray();
        for (String s : years) {
            JSONObject obj = new JSONObject();
            obj.put("id", s);
            obj.put("name", s);
            jList.add(obj);
        }
        String yearsStr = jList.toString();
        return yearsStr;
    }

    /**
     * Description: <br>格式化double
     *
     * @param d
     * @return
     * @see
     */
    private String formatDouble(double d) {
        DecimalFormat df = new DecimalFormat("#.00");// 小数点保留两位小数
        if (d != 0) {
            return df.format(d) + "";
        }
        return "0";
    }

    @Override
    public String getProjectCombobox(String orgId) {
        String hql = "from Project t where avaliable='1'";
        if (StringUtils.isNotBlank(orgId)) {
            hql += " and (createorgid = '" + orgId + "' or createorgid is null)";
        }
        List<Project> projectList = findByQueryString(hql);
        JSONArray jList = new JSONArray();
        for (Project proj : projectList) {
            JSONObject obj = new JSONObject();
            obj.put("id", proj.getId());
            obj.put("name", proj.getName());
            jList.add(obj);
        }
        String projectListStr = jList.toString();
        return projectListStr;
    }


    @Override
    public JSONArray getPlanChangeHighchartsInfo(String condition, String conditionForManager,String type) {
        String commonQuery = " select b.name changereason, "
                + " count(t.id) changenumber "
                + " from ( select pph.* , "
                + " case cbc.parentId "
                + " when null  then pph.changetype "
                + " when 'ROOT' then pph.changetype "
                + " else substr(cbc.path,2,(instr(cbc.path,'/',1,2))-2) end "
                + " as newChangeType "
                + " from pm_plan_history pph "
                + " join cm_business_config cbc  on  (pph.changetype = cbc.id  ) ) t "
                + " join cm_business_config b on t.newChangeType = b.id "
                + " join pm_project p on t.projectid = p.id "
                + " join t_s_base_user u on t.assigner = u.id "
                + " join pm_plan pp on t.planid = pp.id "
                + " join t_s_base_user uu on uu.id = pp.assigner "
                + " join (SELECT distinct PROJ.ID, PROJ.NAME, PROJ.PROJECTNUMBER "
                + " FROM FD_TEAM_ROLE_USER RU "
                + " join FD_TEAM_ROLE ROLE on role.roleid = ru.roleid AND ROLE.ROLECODE = 'manager' "
                + " join PM_PROJ_TEAM_LINK LINK on link.teamid = ru.teamid "
                + " join PM_PROJECT PROJ on proj.id = link.projectid "
                + " join t_s_base_user u on u.id = ru.userid "
                + " WHERE 1 = 1 "
                + conditionForManager
                + " UNION "
                + " SELECT distinct PROJ.ID, PROJ.NAME, PROJ.PROJECTNUMBER "
                + " FROM FD_TEAM_ROLE_GROUP RG "
                + " join T_S_GROUP_USER GU on gu.groupid = rg.groupid "
                + " join FD_TEAM_ROLE ROLE on role.roleid = RG.roleid AND ROLE.ROLECODE = 'manager' "
                + " join PM_PROJ_TEAM_LINK LINK on link.teamid = rg.teamid "
                + " join PM_PROJECT PROJ on proj.id = link.projectid "
                + " join t_s_base_user u on u.id = gu.userid "
                + " WHERE 1 = 1 "
                + conditionForManager + ") m on m.id = p.id"
                + " where b.configtype = 'PLANCHANGECATEGORY' " + condition
                + " and ( b.parentId = 'ROOT' or b.parentId is null ) "
                + " group by b.name  order by "
                + " count(t.id) desc, b.name desc";

        String sql = " select rownum rn, A5.changereason, A5.changenumber from ( "
                + " select  A4.changereason, A4.changenumber, a4.seq "
                + " from ( "
                + " 	select sqlForR.changereason changereason, sqlForR.changenumber changenumber, 1 seq "
                + " 		from ( select rownum, changereason, changenumber from (select na.changereason  changereason, na.changenumber "
                + "		from ( " + commonQuery + ") na )  where rownum <= 5) sqlForR "
                + " union "
                + " select na.changereason  changereason, na.changenumber, 0 seq from("
                + " 	select case when A3.c = 6 then (select rr.changereason from (select rownum r, changereason, changenumber "
                + " 	              											from ( " + commonQuery + " )) rr where rr.r = 6) "
                + "         else '其他' end changereason, A1.changenumber - A2.Top5num changenumber, 0 seq "
                + " 		from (select '总数' changereason, sqlForT.changenumber "
                + "      	from (select count(t.id) changenumber "
                + "           from pm_plan_history t "
                + "           join cm_business_config b on t.changetype = b.id "
                + "           join pm_project p on t.projectid = p.id "
                + "           join t_s_base_user u on t.assigner = u.id "
                + "           join pm_plan pp on t.planid = pp.id "
                + "           join t_s_base_user uu on uu.id = pp.assigner "
                + " join (SELECT distinct PROJ.ID, PROJ.NAME, PROJ.PROJECTNUMBER "
                + " FROM FD_TEAM_ROLE_USER RU "
                + " join FD_TEAM_ROLE ROLE on role.roleid = ru.roleid AND ROLE.ROLECODE = 'manager' "
                + " join PM_PROJ_TEAM_LINK LINK on link.teamid = ru.teamid "
                + " join PM_PROJECT PROJ on proj.id = link.projectid "
                + " join t_s_base_user u on u.id = ru.userid "
                + " WHERE 1 = 1 "
                + conditionForManager
                + " UNION "
                + " SELECT distinct PROJ.ID, PROJ.NAME, PROJ.PROJECTNUMBER "
                + " FROM FD_TEAM_ROLE_GROUP RG "
                + " join T_S_GROUP_USER GU on gu.groupid = rg.groupid "
                + " join FD_TEAM_ROLE ROLE on role.roleid = RG.roleid AND ROLE.ROLECODE = 'manager' "
                + " join PM_PROJ_TEAM_LINK LINK on link.teamid = rg.teamid "
                + " join PM_PROJECT PROJ on proj.id = link.projectid "
                + " join t_s_base_user u on u.id = gu.userid "
                + " WHERE 1 = 1 "
                + conditionForManager + " ) m on m.id = p.id"
                + "          where b.parentId is null and b.configtype = 'PLANCHANGECATEGORY'" + condition + ") sqlForT) A1, "
                + "(select SUM(sqlForTOP5.changenumber) Top5num "
                + "   from (select rownum r, changereason, changenumber "
                + "           from (" + commonQuery + " ) "
                + "          where rownum <= 5) sqlForTOP5) A2, "
                + "(select count(1) c "
                + " from ( " + commonQuery + " )) A3) na ) A4 "
                + " where A4.changenumber > 0 "
                + " order by seq desc,changenumber desc) A5 ";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        JSONArray jList = new JSONArray();
        if (!CommonUtil.isEmpty(objArrayList)) {
            int allChangenumber = 0;
            for (Map<String, Object> map : objArrayList) {
                if(!CommonUtil.isEmpty(map.get("changenumber"))) {
                    int changenumber = Integer.valueOf(map.get("changenumber").toString());
                    allChangenumber = allChangenumber + changenumber;
                }
            }
            int curChangenumber = 0;
            for (Map<String, Object> map : objArrayList) {
                JSONObject obj = new JSONObject();
                obj.put("changereason",  StringUtils.isEmpty((String)map.get("changereason")) ? "" : map.get("changereason").toString());
                int changenumber = Integer.valueOf(map.get("changenumber").toString());
                obj.put("changenumber", changenumber);
                curChangenumber = curChangenumber + changenumber;
                String num = txfloat(curChangenumber, allChangenumber);
                obj.put("pointNumber", Integer.valueOf(num));
                jList.add(obj);
            }
        }
        return jList;
    }

    private static String txfloat(int a ,int b){
        DecimalFormat df = new DecimalFormat("0");
        return df.format((float)a/b*100);
    }

    @Override
    public List<planChangeAnalysisVo> conditionSearch(String condition, String conditionForManager,int page, int rows) {
        int start = (page - 1) * rows;
        int end =  page * rows;

        String sql = " select t2.* from (select t1.*,rowNum rm from ( ";
        String sql2 = "  select count(t1.planid) allNumber from ( ";
        String allSql = getCommonSql(conditionForManager,condition);
        sql = sql + allSql;
        sql2 = sql2 + allSql;
        sql = sql + " ) t1 )t2 where t2.rm>"+start+" and t2.rm<="+end+"";
        sql2 = sql2 + " ) t1 ";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        List<Map<String, Object>> objArrayList1 = sessionFacade.findForJdbc(sql2);
        List<planChangeAnalysisVo> list = new ArrayList<planChangeAnalysisVo>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            int allNumber = 0;
            for (Map<String, Object> map1 : objArrayList1) {
                allNumber = Integer.valueOf(map1.get("allNumber").toString());
            }
            for (Map<String, Object> map : objArrayList) {
                planChangeAnalysisVo vo = new planChangeAnalysisVo();
                vo.setPlanId(StringUtils.isEmpty((String)map.get("planid")) ? "" : map.get("planid").toString());
                vo.setChangeReason(StringUtils.isEmpty((String)map.get("changereason")) ? "" : map.get("changereason").toString());
                vo.setOwnerName(StringUtils.isEmpty((String)map.get("ownername")) ? "" : map.get("ownername").toString());
                if(!CommonUtil.isEmpty(map.get("changenumber"))) {
                    int changenumber = Integer.valueOf(map.get("changenumber").toString());
                    vo.setChangeNumber(String.valueOf(changenumber));
                }
                vo.setProjectName(StringUtils.isEmpty((String)map.get("projectname")) ? "" : map.get("projectname").toString());
                vo.setPlanName(StringUtils.isEmpty((String)map.get("planname")) ? "" : map.get("planname").toString());
                vo.setAssignerName(StringUtils.isEmpty((String)map.get("assignername")) ? "" : map.get("assignername").toString());
                vo.setAllNumber(allNumber);
                list.add(vo);
            }
        }
        return list;
    }

    private String getCommonSql(String conditionForManager,String condition){
        String sql = " select t.planid planid, b.name changereason, u.ownername ownername,"
                + " count(t.id) changenumber"
                + ", p.name projectname, t.planname planname, uu.assignername assignername"
                + " from ( select pph.* , "
                + " case cbc.parentId "
                + " when null  then pph.changetype "
                + " when 'ROOT' then pph.changetype "
                + " else substr(cbc.path,2,(instr(cbc.path,'/',1,2))-2) end "
                + " as newChangeType "
                + " from pm_plan_history pph "
                + " join cm_business_config cbc  on  (pph.changetype = cbc.id  ) ) t "
                + " join cm_business_config b on t.newChangeType = b.id "
                + " join pm_project p on p.id = t.projectid "
                + " join (select id, realname || '-' || username ownername from t_s_base_user ) u on u.id = t.owner "
                + " join pm_plan pp on pp.id = t.planid "
                + " join (select id, realname || '-' || username assignername from t_s_base_user ) uu on uu.id = pp.assigner "
                + " join (SELECT PROJ.ID, PROJ.NAME, PROJ.PROJECTNUMBER "
                + " FROM FD_TEAM_ROLE_USER RU, "
                + " FD_TEAM_ROLE      ROLE, "
                + " PM_PROJ_TEAM_LINK LINK, "
                + " PM_PROJECT        PROJ, "
                + " t_s_base_user     u "
                + " WHERE RU.ROLEID = ROLE.ROLEID "
                + " AND ROLE.ROLECODE = 'manager' "
                + " AND LINK.TEAMID = RU.TEAMID "
                + " AND LINK.PROJECTID = PROJ.ID "
                + " and u.id = ru.userid "
                + conditionForManager
                + " UNION "
                + " SELECT PROJ.ID, PROJ.NAME, PROJ.PROJECTNUMBER "
                + " FROM FD_TEAM_ROLE_GROUP RG, "
                + " T_S_GROUP_USER     GU, "
                + " FD_TEAM_ROLE       ROLE, "
                + " PM_PROJ_TEAM_LINK  LINK, "
                + " PM_PROJECT         PROJ, "
                + " t_s_base_user      u "
                + " WHERE RG.ROLEID = ROLE.ROLEID "
                + " AND RG.GROUPID = GU.GROUPID "
                + " AND ROLE.ROLECODE = 'manager' "
                + " AND LINK.TEAMID = RG.TEAMID "
                + " AND LINK.PROJECTID = PROJ.ID "
                + " and u.id = gu.userid "
                + conditionForManager + ") m on m.id = p.id "
                + " where b.configtype = 'PLANCHANGECATEGORY' " + condition
                + " and ( b.parentId = 'ROOT' or b.parentId is null ) "
                + " group by b.name, u.ownername, p.name, t.planname, t.planid, uu.assignername";
        return sql;
    }


    @Override
    public List<ProjectBoardReportDataVo> getProjectBoardReportData(String projectId,String planLevel) {
        List<ProjectBoardReportDataVo> list = new ArrayList<>();
        String condition1 = " where 1 = 1 ";
        String condition2 = " where 1 = 1 ";
        String condition3 = " ";

        if(!CommonUtil.isEmpty(projectId)){
            condition1 = condition1 + " and p.projectId = '" + projectId + "'";
            condition2 = condition2 + " and A1.RESOURCEID IN "
                    + " (select INFO.RESOURCEID from CM_RESOURCE_EVERYDAYUSE_INFO INFO where INFO.projectid = '" + projectId + "') "
                    + " AND ((select startprojecttime from PM_project where id = '" + projectId + "') <= A1.USEDATE) "
                    + " AND ((select endprojecttime from PM_project where id = '" + projectId + "') >= A1.USEDATE) ";
            condition3 = " AND R.PROJECTID = '" + projectId + "'";
        }

        if(!CommonUtil.isEmpty(planLevel)){
            condition1 = condition1 + " and p.planlevel in (" + planLevel + ")";
        }

        String query1 =" select a1.c num, a1.dep dep, '计划' cf, 1 seq, depp "
                + " from (select 0 c, '填充字段1' dep, '4' depp from dual union "
                + " SELECT COUNT(P.ID) c, '已完成计划数' dep, '1' depp "
                + " FROM PM_PLAN P "
                + condition1
                + " AND P.AVALIABLE = '1' "
                + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID') "
                + " AND P.BIZCURRENT = 'FINISH' "
                + " union "
                + " SELECT COUNT(P.ID) c, '未完成计划数' dep, '3' depp "
                + " FROM PM_PLAN P "
                + condition1
                + " AND P.AVALIABLE = '1' "
                + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID', 'FINISH') "
                + " AND to_date(to_char(P.PLANENDTIME, 'yyyy-mm-dd'), 'yyyy-mm-dd') >= "
                + " to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') "
                + " union "
                + " SELECT COUNT(P.ID) c, '已超期计划数' dep, '2' depp "
                + " FROM PM_PLAN P "
                + condition1
                + " AND P.AVALIABLE = '1' "
                + " AND P.BIZCURRENT NOT IN ('EDITING', 'INVALID', 'FINISH') "
                + " AND to_date(to_char(P.PLANENDTIME, 'yyyy-mm-dd'), 'yyyy-mm-dd') < "
                + " to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd')) a1 ";

        String query2 = " select 0 num, '填充字段1' dep, '资源' cf, 3 seq, '2' depp from dual "
                + " union "
                + " select 0 num, '填充字段2' dep, '资源' cf, 3 seq, '3' depp from dual"
                + " union"
                + " SELECT count(A3.RESOURCEID) num, '正常' dep, '资源' cf, 3 seq, '1' depp "
                + " FROM (SELECT A2.RESOURCEID, A2.NAME, SUM(ISWARN) ISWARN "
                + " FROM (SELECT A1.* "
                + " FROM (SELECT BASE.RESOURCEID, BASE.NAME, BASE.USEDATE, "
                + " (CASE WHEN RB.OCCUPATIONWARN IS NULL  OR RB.OCCUPATIONWARN = 0 THEN 0 ELSE "
                + " (CASE WHEN BASE.SUMRATE / RB.OCCUPATIONWARN > 1 THEN 1 ELSE 0 END) END) ISWARN "
                + " FROM (SELECT R.RESOURCEID, RS.NAME, R.USEDATE, SUM(to_number(R.USERATE)) SUMRATE "
                + " FROM CM_RESOURCE_EVERYDAYUSE_INFO R "
                + " JOIN CM_RESOURCE RS ON RS.ID = R.RESOURCEID "
                + " GROUP BY R.RESOURCEID, RS.NAME, R.USEDATE ORDER BY R.RESOURCEID, R.USEDATE ASC) BASE "
                + " JOIN CM_RESOURCE RB ON RB.ID = BASE.RESOURCEID ) A1 "
                + condition2 + ") A2 "
                + " GROUP by A2.RESOURCEID, A2.NAME) A3 where A3.ISWARN = 0";

        String query3 = " SELECT count(A3.RESOURCEID) num, '超期' dep, '资源' cf, 3 seq, '4' depp "
                + " FROM (SELECT A2.RESOURCEID, A2.NAME, SUM(ISWARN) ISWARN "
                + " FROM (SELECT A1.* "
                + " FROM (SELECT BASE.RESOURCEID, BASE.NAME, BASE.USEDATE, "
                + " (CASE WHEN RB.OCCUPATIONWARN IS NULL OR RB.OCCUPATIONWARN = 0 THEN 0 ELSE "
                + " (CASE WHEN BASE.SUMRATE / RB.OCCUPATIONWARN > 1 THEN 1 ELSE 0 END) END) ISWARN "
                + " FROM (SELECT R.RESOURCEID, RS.NAME, R.USEDATE, SUM(to_number(R.USERATE)) SUMRATE "
                + " FROM CM_RESOURCE_EVERYDAYUSE_INFO R "
                + " JOIN CM_RESOURCE RS ON RS.ID = R.RESOURCEID "
                + " GROUP BY R.RESOURCEID, RS.NAME, R.USEDATE ORDER BY R.RESOURCEID, R.USEDATE ASC) BASE "
                + " JOIN CM_RESOURCE RB ON RB.ID = BASE.RESOURCEID ) A1 "
                + condition2 + ") A2 "
                + " GROUP by A2.RESOURCEID, A2.NAME) A3 where A3.ISWARN > 0 ";

        String query4 = " Select 0 num, '填充字段1' DEP, '风险' cf, 2 seq, '3' depp FROM dual  "
                + " union "
                + " Select 0 num, '填充字段2' DEP, '风险' cf, 2 seq, '4' depp FROM dual  "
                + " union "
                + " SELECT COUNT(ID) NUM, 'RED' dep, '风险' cf, 2 seq, '2' depp from("
                + " select distinct r.id id "
                + " FROM PM_RISKINVENTORY_TARGET R "
                + " JOIN CM_RISK_SCORE_MODEL MP ON MP.TYPECODE = R.PROBABILITYTYPECODE "
                + " JOIN CM_RISK_SCORE_MODEL MI ON MI.TYPECODE = R.influencetypecode "
                + " JOIN CM_RISK_SCORE_MODEL ML ON ML.TYPECODE = 'risklevel_high' "
                + " JOIN PM_RISK_PROBLEM_CATEGORY CATEGORY ON CATEGORY.INVENTORYTARGETID = R.ID "
                + " JOIN PM_RISK_PROBLEM_INFO INFO ON (CATEGORY.ID = INFO.PROBLEMCATEGORYID) "
                + " WHERE R.STATUS = '1' "
                + " AND (ML.SCOREINTERVAL_START <= MP.SCORE * MI.SCORE AND "
                + " MP.SCORE * MI.SCORE <= ML.SCOREINTERVAL_END) "
                + " AND INFO.BIZCURRENT <> 'CLOSED' " + condition3 + ")"
                + " UNION "
                + " SELECT RESULT.NUM - A1.NUM NUM, 'GREEN' dep, '风险' cf, 2 seq, '1' depp "
                + " FROM (SELECT COUNT(ID) NUM from ("
                + " select distinct r.id "
                + " FROM PM_RISKINVENTORY_TARGET R "
                + " JOIN CM_RISK_SCORE_MODEL MP ON MP.TYPECODE = R.PROBABILITYTYPECODE "
                + " JOIN CM_RISK_SCORE_MODEL MI ON MI.TYPECODE = R.influencetypecode "
                + " JOIN CM_RISK_SCORE_MODEL ML ON ML.TYPECODE = 'risklevel_high' "
                + " JOIN PM_RISK_PROBLEM_CATEGORY CATEGORY ON CATEGORY.INVENTORYTARGETID = R.ID "
                + " JOIN PM_RISK_PROBLEM_INFO INFO ON (CATEGORY.ID = INFO.PROBLEMCATEGORYID) "
                + " WHERE R.STATUS = '1' "
                + " AND (ML.SCOREINTERVAL_START <= MP.SCORE * MI.SCORE AND "
                + " MP.SCORE * MI.SCORE <= ML.SCOREINTERVAL_END) "
                + " AND INFO.BIZCURRENT <> 'CLOSED' " + condition3 + ")) A1 "
                + " LEFT JOIN (SELECT 'total' COLOR, COUNT(R.ID) NUM "
                + " FROM PM_RISKINVENTORY_TARGET R "
                + " WHERE R.STATUS = '1' " + condition3 + ") RESULT ON 1 = 1 ";

        System.out.println("select * from (" +  query1 + " union " + query2 + " union " + query3 + " union " + query4 + ") order by seq");

        String sqlStr = "select * from (" +  query1 + " union " + query2 + " union " + query3 + " union " + query4 + ") order by seq,depp";
        List<Map<String,Object>> mapList = sessionFacade.findForJdbc(sqlStr);
        if(!CommonUtil.isEmpty(mapList)){
            for(Map<String,Object> map : mapList){
                ProjectBoardReportDataVo vo = new ProjectBoardReportDataVo();
                vo.setName(String.valueOf(map.get("CF")));
                vo.setAction(String.valueOf(map.get("DEP")));
                vo.setNumber(String.valueOf(map.get("NUM")));
                vo.setOrder(String.valueOf(map.get("DEPP")));
                list.add(vo);
            }
        }
        return list;
    }

    @Override
    public FeignJson searchlaborLoadList(Map<String, Object> params) {
        FeignJson j = new FeignJson();
        int rows = params.get("rows") == null ? 0 : Integer.parseInt(params.get("rows").toString());
        int page = params.get("page") == null ? 0 : Integer.parseInt(params.get("page").toString());
        String beginDate = params.get("beginDate") == null ? "" : params.get("beginDate").toString();
        String endDate = params.get("endDate") == null ? "" : params.get("endDate").toString();
        String departId = params.get("departId") == null ? "" : params.get("departId").toString();
        String projectId = params.get("projectId") == null ? "" : params.get("projectId").toString();
        String projectType = params.get("projectType") == null ? "" : params.get("projectType").toString();
        String owner = params.get("owner") == null ? "" : params.get("owner").toString();
        String seq = params.get("seq") == null ? "" : params.get("seq").toString();
        try {
            int num = page * rows;
            int size = (page - 1) * rows;
            String sql = "select p.projectid id," +
                    "uo.ownername ownername," +
                    "proj.projectnumber projectnumber," +
                    "proj.name projectname," +
                    "p.planname planname," +
                    " case" +
                    " when p.progressRate is null then" +
                    " '0%' else" +
                    " p.progressRate || '%'" +
                    " end progressRate," +
                    "lcs.title status," +
                    "bc.name planlevel," +
                    "to_char(p.planstarttime, 'yyyy-mm-dd') planstarttime," +
                    "to_char(p.planendtime, 'yyyy-mm-dd') planendtime," +
                    "ua.ownername assignername," +
                    "to_char(p.assigntime, 'yyyy-mm-dd') assigntime," +
                    "p.plantype plantype" +
                    " from pm_plan p" +
                    " join (select id, realname || '-' || username ownername, departid" +
                    " from t_s_base_user) uo on uo.id = p.owner" +
                    " left join t_s_depart d on d.id = uo.departid" +
                    " left join (select id, realname || '-' || username ownername" +
                    " from t_s_base_user) ua on ua.id = p.assigner" +
                    " join PM_PROJECT proj on proj.id = p.projectid" +
                    " join LIFE_CYCLE_STATUS lcs on lcs.name = p.bizcurrent" +
                    " join LIFE_CYCLE_POLICY lcp on lcp.id = lcs.policy_id" +
                    " and lcp.name = 'PLAN'" +
                    " left join cm_business_config bc on bc.id = p.planlevel"
                    + " where p.bizcurrent not in ('CLOSED','EDITING','PAUSED','INVALID') and "
                    + " ((p.planstarttime >= to_date('" + beginDate + "', 'yyyy-mm-dd') and "
                    + " p.planstarttime <= to_date('" + endDate + "', 'yyyy-mm-dd')) or "
                    + " (p.planendtime >= to_date('" + beginDate + "', 'yyyy-mm-dd') and "
                    + " p.planendtime <= to_date('" + endDate + "', 'yyyy-mm-dd')) or "
                    + " (p.planstarttime <= to_date('" + beginDate + "', 'yyyy-mm-dd') and "
                    + " p.planendtime >= to_date('" + endDate + "', 'yyyy-mm-dd'))) and lcp.avaliable = '1' ";

            if (StringUtils.isNotBlank(departId)) {
                sql = sql + " and d.id in (" + departId + ")";
            }
            if (StringUtils.isNotBlank(projectId)) {
                sql = sql + " and p.projectId = '" + projectId + "'";
            }
            if (StringUtils.isNotBlank(projectType)) {
                sql = sql + " and proj.eps in (" + projectType + ")";
            }
            if (StringUtils.isNotBlank(owner)) {
                sql = sql + " and uu.ownername like '%" + owner + "%'";
            }
            /*sql = sql + " order by uo.ownername";*/
            //查询总数据条数
            String countSql = "select count(ct.id) AllNumber from (" + sql + ") ct";
            int count = 0;
            List<Map<String, Object>> coutArrayList = sessionFacade.findForJdbc(countSql);
            if (!CommonUtil.isEmpty(coutArrayList)) {
                for (Map<String, Object> map : coutArrayList) {
                    count = Integer.parseInt(map.get("AllNumber").toString());
                }
            }
            String searchSql = "select * from (select ROWNUM as num, tt.* from (" + sql + ") tt where ROWNUM <= "+num+") where num > "+size+"";
            List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(searchSql);
            List<LaborLoadVo> voList = new ArrayList<>();
            if (!CommonUtil.isEmpty(objArrayList)) {
                for (Map<String, Object> map : objArrayList) {
                    LaborLoadVo vo = new LaborLoadVo();
                    vo.setOwnerName(StringUtils.isEmpty((String)map.get("ownername")) ? "" : map.get(
                            "ownername").toString());
                    vo.setProjectNumber(StringUtils.isEmpty((String)map.get("projectnumber")) ? "" : map.get(
                            "projectnumber").toString());
                    vo.setProjectName(StringUtils.isEmpty((String)map.get("projectname")) ? "" : map.get(
                            "projectname").toString());
                    vo.setPlanName(StringUtils.isEmpty((String)map.get("planname")) ? "" : map.get(
                            "planname").toString());
                    vo.setProgressRate(StringUtils.isEmpty((String)map.get("progressRate")) ? "" : map.get(
                            "progressRate").toString());
                    vo.setStatus(StringUtils.isEmpty((String)map.get("status")) ? "" : map.get(
                            "status").toString());
                    vo.setPlanlevel(StringUtils.isEmpty((String)map.get("planlevel")) ? "" : map.get(
                            "planlevel").toString());
                    vo.setStartTime(StringUtils.isEmpty((String)map.get("planstarttime")) ? "" : map.get(
                            "planstarttime").toString());
                    vo.setEndTime(StringUtils.isEmpty((String)map.get("planendtime")) ? "" : map.get(
                            "planendtime").toString());
                    vo.setAssignerName(StringUtils.isEmpty((String)map.get("assignername")) ? "" : map.get(
                            "assignername").toString());
                    vo.setAssignTime(StringUtils.isEmpty((String)map.get("assigntime")) ? "" : map.get(
                            "assigntime").toString());
                    vo.setPlanType(StringUtils.isEmpty((String)map.get("plantype")) ? "" : map.get(
                            "plantype").toString());
                    voList.add(vo);
                }
            }
            String json = JsonUtil.getListJsonWithoutQuote(voList);
            String datagridStr = "{\"rows\":" + json + ",\"total\":" + count + "}";
            j.setObj(datagridStr);
        } catch (NumberFormatException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @Override
    public FeignJson getLaborLoadListCharts(Map<String, Object> params) {
        FeignJson j = new FeignJson();
        String beginDate = params.get("beginDate") == null ? "" : params.get("beginDate").toString();
        String endDate = params.get("endDate") == null ? "" : params.get("endDate").toString();
        String departId = params.get("departId") == null ? "" : params.get("departId").toString();
        String projectId = params.get("projectId") == null ? "" : params.get("projectId").toString();
        String projectType = params.get("projectType") == null ? "" : params.get("projectType").toString();
        String owner = params.get("owner") == null ? "" : params.get("owner").toString();
        String seq = params.get("seq") == null ? "" : params.get("seq").toString();

        String condition = " where p.bizcurrent not in ('CLOSED','EDITING','PAUSED','INVALID') and "
                + " ((to_date(to_char(p.planstarttime,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date('" + beginDate + "', 'yyyy-mm-dd') and "
                + " to_date(to_char(p.planstarttime,'yyyy-mm-dd'),'yyyy-mm-dd') <= to_date('" + endDate + "', 'yyyy-mm-dd')) or "
                + " (to_date(to_char(p.planendtime,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date('" + beginDate + "', 'yyyy-mm-dd') and "
                + " to_date(to_char(p.planendtime,'yyyy-mm-dd'),'yyyy-mm-dd') <= to_date('" + endDate + "', 'yyyy-mm-dd')) or "
                + " (to_date(to_char(p.planstarttime,'yyyy-mm-dd'),'yyyy-mm-dd') <= to_date('" + beginDate + "', 'yyyy-mm-dd') and "
                + " to_date(to_char(p.planendtime,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date('" + endDate + "', 'yyyy-mm-dd'))) ";

        if (StringUtils.isNotBlank(departId)) {
            condition = condition + " and d.id in (" + departId + ")";
        }
        if (StringUtils.isNotBlank(projectId)) {
            condition = condition + " and p.projectId = '" + projectId + "'";
        }
        if (StringUtils.isNotBlank(projectType)) {
            condition = condition + " and pj.eps in (" + projectType + ")";
        }
        if (StringUtils.isNotBlank(owner)) {
            condition = condition + " and uu.ownername like '%" + owner + "%'";
        }

        String commonQuery = " select a1.name name, count(a1.id) num, a1.status status "
                + " from (select uu.ownername name, "
                + " p.id id, "
                + " case "
                + " when p.bizcurrent = 'FINISH' then "
                + "  '已完成' "
                + " when p.bizcurrent != 'FINISH' and p.planendtime >= to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd') then "
                + "  '正常未完成' "
                + " when p.bizcurrent != 'FINISH' and p.planendtime < to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd') then "
                + "  '延期未完成' "
                + " end as status "
                + " from pm_plan p "
                + " join (select id, realname || '-' || username ownername, departid from t_s_base_user ) uu on p.owner = uu.id "
                + " left join t_s_depart d on uu.departid = d.id "
                + " join pm_project pj on p.projectid = pj.id "
                + condition
                + " )a1 group by a1.name, a1.status "
                + " union "
                + " select distinct uu.ownername name, 0 num, '已完成' status "
                + " from pm_plan p "
                + " join (select id, "
                + " realname || '-' || username ownername, "
                + " departid "
                + " from t_s_base_user) uu on p.owner = uu.id "
                + " left join t_s_depart d on uu.departid = d.id "
                + " join pm_project pj on p.projectid = pj.id "
                + condition
                + " union "
                + " select distinct uu.ownername name, 0 num, '正常未完成' status "
                + " from pm_plan p "
                + " join (select id, "
                + " realname || '-' || username ownername, "
                + " departid "
                + " from t_s_base_user) uu on p.owner = uu.id "
                + " left join t_s_depart d on uu.departid = d.id "
                + " join pm_project pj on p.projectid = pj.id "
                + condition
                + " union "
                + " select distinct uu.ownername name, 0 num, '延期未完成' status "
                + " from pm_plan p "
                + " join (select id, "
                + " realname || '-' || username ownername, "
                + " departid "
                + " from t_s_base_user) uu on p.owner = uu.id "
                + " left join t_s_depart d on uu.departid = d.id "
                + " join pm_project pj on p.projectid = pj.id "
                + condition;

        String commonQuery2 = " select uu.ownername name, "
                + " p.id id, "
                + " case "
                + " when p.bizcurrent = 'FINISH' then "
                + "  '已完成' "
                + " when p.bizcurrent != 'FINISH' and p.planendtime >= to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd') then "
                + "  '正常未完成' "
                + " when p.bizcurrent != 'FINISH' and p.planendtime < to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd') then "
                + "  '延期未完成' "
                + " end as status "
                + " from pm_plan p "
                + " join (select id, realname || '-' || username ownername, departid from t_s_base_user ) uu on p.owner = uu.id "
                + " left join t_s_depart d on uu.departid = d.id "
                + " join pm_project pj on p.projectid = pj.id "
                + condition;

        String seqCondition = "";
        String seqQuery = "";

        if(seq.equals("1")){//按计划总数排列
            seqCondition = " left join (select a4.name, sum(a4.num) seq "
                    + " from (select a3.name name, count(a3.id) num, a3.status status "
                    + " from ( " + commonQuery2 + " )a3 group by a3.name, a3.status) a4 group by a4.name "
                    + " ) a5 on (a5.name = a2.name) ";
        }else if(seq.equals("2")){//按延期未完成排列
            seqCondition = " left join (select a4.name, a4.num seq, a4.status "
                    + " from (select a3.name name, count(a3.id) num, a3.status status "
                    + " from ( " + commonQuery2 + " )a3 group by a3.name, a3.status) a4 where a4.status = '延期未完成' "
                    + " ) a5 on (a5.name = a2.name) ";
        }else if(seq.equals("3")){//按正常未完成排列
            seqCondition = " left join (select a4.name, a4.num seq, a4.status "
                    + " from (select a3.name name, count(a3.id) num, a3.status status "
                    + " from ( " + commonQuery2 + " )a3 group by a3.name, a3.status) a4 where a4.status = '正常未完成' "
                    + " ) a5 on (a5.name = a2.name) ";
        }
        seqQuery = " select distinct a2.name, a2.status, a2.num, case when a5.seq is null then 0 else a5.seq end sqq, a6.rn rn"
                + " from (select aa.name name, sum(aa.num) num, aa.status status "
                + " from ( " + commonQuery + " )aa group by aa.name, aa.status ) a2 "
                + seqCondition
                + " join (select rownum rn, uc.name "
                + " from (select distinct uu.ownername name "
                + " from pm_plan p "
                + " join (select id, realname || '-' || username ownername, departid "
                + " from t_s_base_user) uu on p.owner = uu.id "
                + " left join t_s_depart d on uu.departid = d.id "
                + " join pm_project pj on p.projectid = pj.id "
                + condition
                + ") uc) a6 on a6.name = a2.name "
                + " order by sqq desc ";

        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(seqQuery);
        Map<String, Map<String,Integer>> value = new HashMap<>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                Map<String,Integer> temp = new HashMap<String, Integer>();
                String name = StringUtils.isEmpty((String)map.get("name")) ? "" : map.get(
                        "name").toString();
                String status = StringUtils.isEmpty((String)map.get("status")) ? "" : map.get(
                        "status").toString();
                int num = map.get("num") == null ? 0 : Integer.parseInt(map.get("num").toString());
                if (StringUtils.isNotBlank(status)) {
                    temp.put(status,num);
                }
                if (StringUtils.isNotBlank(name)) {
                    if (!CommonUtil.isEmpty(value) && value.containsKey(name)) {
                        value.get(name).put(status, num);
                    } else {
                        value.put(name,temp);
                    }
                }
            }
        }
        List<String> laborLoadList = CommonConstants.laborLoadList;
        List<LaborLoadCharts>  charts = new ArrayList<>();
        for (int i = 0; i<laborLoadList.size(); i++) {
            LaborLoadCharts chart = new LaborLoadCharts();
            chart.setName(laborLoadList.get(i));
            List<Integer> data = chart.getData();
            for (Map.Entry<String, Map<String,Integer>> entry : value.entrySet()) {
                Map<String,Integer> mapValue = entry.getValue();
                data.add(mapValue.get(laborLoadList.get(i)));
            }
            chart.setData(data);
            if (laborLoadList.get(i).equals("已完成")) {
                chart.setColor("#00FF00");
            } else if (laborLoadList.get(i).equals("延期未完成")) {
                chart.setColor("#FF0000");
            } else {
                chart.setColor("#C0C0C0");
            }
            charts.add(chart);
        }
        List<String> chartsName = value.keySet().stream().collect(Collectors.toList());
        JSONObject object = new JSONObject();
        object.put("xvalue",JsonUtil.toJSONString(chartsName));
        object.put("series",JsonUtil.toJSONString(charts));
        j.setObj(object);
        return j ;
    }
}
