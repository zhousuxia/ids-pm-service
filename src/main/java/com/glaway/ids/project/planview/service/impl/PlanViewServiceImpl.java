package com.glaway.ids.project.planview.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.dto.TSDepartDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.fdk.dev.service.threemember.FeignDepartService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.config.constant.PlanviewConstant;
import com.glaway.ids.project.plan.dto.PlanViewColumnInfoDto;
import com.glaway.ids.project.plan.dto.PlanViewInfoDto;
import com.glaway.ids.project.plan.dto.PlanViewSearchConditionDto;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.planview.entity.PlanViewApplyProject;
import com.glaway.ids.project.planview.entity.PlanViewApplyUser;
import com.glaway.ids.project.planview.service.PlanViewServiceI;
import com.glaway.ids.util.CommonInitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;

@Service("planViewService")
@Transactional
public class PlanViewServiceImpl extends CommonServiceImpl implements PlanViewServiceI {
    @Autowired
    private SessionFacade sessionFacade;


    @Autowired
    private FeignDepartService departService;


    @Autowired
    private FeignUserService userService;

    @Value(value="${spring.application.name}")
    private String appKey;

    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;

    @SuppressWarnings("unchecked")
    @Override
    public List<PlanViewInfoDto> constructionPlanViewTree(String projectId, String type, String currentUserId,String orgId) {
        List<PlanViewInfo> resultList = new ArrayList<PlanViewInfo>();
        if(!PlanviewConstant.MANAGEMENT.equals(type)) {
            //查询公共默认视图
            String hql = "from PlanViewInfo where status = ? and isDefault = 'true'  order by publishTime desc";
            List<PlanViewInfo> list = sessionFacade.findHql(hql, PlanviewConstant.PUBLIC_STATUS);
            resultList.addAll(list);
            //公共视图
            String hql2 = "from PlanViewInfo where id in(select planViewInfoId from PlanViewApplyProject where projectId = ? or projectId is null)\n" +
                    "and id in(select planViewInfoId from PlanViewApplyUser where userId = ? or userId is null) and status = ? and createOrgId=? and isDefault = ? \n" +
                    "order by publishTime desc";
            List<PlanViewInfo> list2 = null;
            try {
                list2 = sessionFacade.findHql(hql2, projectId, currentUserId, PlanviewConstant.PUBLIC_STATUS, orgId,"false");
            } catch (Exception e) {
                e.printStackTrace();
            }
            resultList.addAll(list2);
            //查询私有视图
            String hql3 = "from PlanViewInfo where status = ? and createBy = ? and createOrgId=? order by createTime desc";
            List<PlanViewInfo> privatelist = sessionFacade.findHql(hql3, PlanviewConstant.PRIVATE_STATUS, currentUserId,orgId);
            resultList.addAll(privatelist);
        } else {
            //管理列表公共视图
            String sql = "select * from (select * from planview_info t where t.createOrgId='"+orgId+"' and t.createby = '"+ currentUserId +"'" +
                    " union" +
                    " select * from Planview_Info t where t.id in(select PLANVIEWINFOID from PLANVIEW_APPLY_USER where USERID = '"+ currentUserId +"')) order by CREATETIME desc";
            List<Map<String,Object>> listForManage  = sessionFacade.findForJdbc(sql);
            if(!CommonUtil.isEmpty(listForManage)){
                for(Map<String,Object> map : listForManage){
                    PlanViewInfo info = new PlanViewInfo();
                    info.setId(!CommonUtil.isEmpty(String.valueOf(map.get("ID"))) ? String.valueOf(map.get("ID")):"");
                    info.setCreateBy(!CommonUtil.isEmpty(String.valueOf(map.get("CREATEBY"))) ? String.valueOf(map.get("CREATEBY")):"");
                    info.setName(!CommonUtil.isEmpty(String.valueOf(map.get("NAME"))) ? String.valueOf(map.get("NAME")):"");
                    info.setStatus(!CommonUtil.isEmpty(String.valueOf(map.get("STATUS"))) ? String.valueOf(map.get("STATUS")):"");
                    info.setPublishPerson(!CommonUtil.isEmpty(String.valueOf(map.get("PUBLISHPERSON"))) ? String.valueOf(map.get("PUBLISHPERSON")):"");
                    info.setPublishDept(!CommonUtil.isEmpty(String.valueOf(map.get("PUBLISHDEPT"))) ? String.valueOf(map.get("PUBLISHDEPT")):"");
                    resultList.add(info);
                }
            }
//            resultList.addAll(listForManage);

        }
        List<PlanViewInfoDto> dtoList = JSON.parseArray(JSON.toJSONString(resultList),PlanViewInfoDto.class);
        return dtoList;
    }

    @Override
    public List<PlanViewColumnInfoDto> getColumnInfoListByPlanViewInfoId(String planViewInfoId) {
        List<PlanViewColumnInfo> columnInfoList = sessionFacade.findHql("from PlanViewColumnInfo where planViewInfoId = ?", planViewInfoId);
        List<PlanViewColumnInfoDto> dtoList = JSON.parseArray(JSON.toJSONString(columnInfoList),PlanViewColumnInfoDto.class);
        return dtoList;
    }

    @Override
    public PlanViewInfoDto getPlanViewEntity(String id) {
        PlanViewInfo planViewInfo = (PlanViewInfo)sessionFacade.getEntity(PlanViewInfo.class,id);
        PlanViewInfoDto dto = new PlanViewInfoDto();
        try {
            dto = (PlanViewInfoDto)VDataUtil.getVDataByEntity(planViewInfo,PlanViewInfoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public List<PlanViewSearchConditionDto> getSearchConditionListByViewId(String planViewId) {
        List<PlanViewSearchCondition> searchConditionList = sessionFacade.findHql("from PlanViewSearchCondition where planViewInfoId=?", planViewId);
        List<PlanViewSearchConditionDto> dtoList = JSON.parseArray(JSON.toJSONString(searchConditionList),PlanViewSearchConditionDto.class);
        return dtoList;
    }

    @Override
    public int getPlanViewTypeById(String planViewId) {
        int count = 0;
        String sql = "select count(t.planid) as count from planview_set_condition t where t.planviewinfoid='"+planViewId+"'";
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(sql);
        if(!CommonUtil.isEmpty(objArrayList)){
            for(Map<String, Object> map : objArrayList){
                count = Integer.valueOf(String.valueOf(map.get("COUNT")));
            }
        }
        return count;
    }

    @Override
    public boolean isUpdateCondition(String planViewInfoId,String userId) {
        boolean flag = false;
        String hql = "from PlanViewInfo where id = ?";
        List<PlanViewInfo> list = sessionFacade.findHql(hql, planViewInfoId);
        if(!CommonUtil.isEmpty(list)) {
            PlanViewInfo view= list.get(0);
            String status = view.getStatus();
            if(!"4028f00763ba3b200163ba6446bf000f".equals(view.getId()) && !"4028f00763ba3b200163ba63fd26000c".equals(view.getId())){    //排除“全部计划”及“我的计划”
                if(PlanviewConstant.PRIVATE_STATUS.equals(status)) {
                    flag = true;
                }
                else if(PlanviewConstant.PUBLIC_STATUS.equals(status) &&
                        userId.equals(view.getPublishPerson())) {
                    flag = true;
                }
            }

        }
        return flag;
    }

    @Override
    public List<PlanViewSearchCondition> getSearchConditionByPlanView(String planViewInfoId) {
        List<PlanViewSearchCondition> list = new ArrayList<PlanViewSearchCondition>();

        String hql = "from PlanViewSearchCondition where planViewInfoId = ?";
        list = sessionFacade.findHql(hql, planViewInfoId);
        return list;
    }

    @Override
    public void saveSearchCondition(List<ConditionVO> conditionList, String planViewInfoId, List<String> existConditionList, TSUserDto currentUser,String orgId) {
        for(ConditionVO c : conditionList) {
            PlanViewSearchCondition search = new PlanViewSearchCondition();
            search.setPlanViewInfoId(planViewInfoId);
            search.setAttributeName(c.getKey());
            search.setAttributeCondition(c.getCondition());
            search.setAttributeValue(c.getValue());
            if(!existConditionList.contains(c.getKey())) {
                CommonInitUtil.initGLObjectForCreate(search,currentUser,orgId);
                sessionFacade.save(search);
            }
            else {
                existConditionList.remove(c.getKey());
                //更新视图条件
                String hql = "from PlanViewSearchCondition where planViewInfoId = ? and attributeName = ?";
                List<PlanViewSearchCondition> result = sessionFacade.findHql(hql, planViewInfoId, c.getKey());

                if(!CommonUtil.isEmpty(result)) {
                    PlanViewSearchCondition r = result.get(0);
                    r.setAttributeValue(search.getAttributeValue());
                    r.setAttributeCondition(search.getAttributeCondition());
                    CommonInitUtil.initGLObjectForUpdate(r,currentUser,orgId);
                    sessionFacade.saveOrUpdate(r);
                }
            }

        }
        //删除以前的条件
        for(String condition : existConditionList) {
            sessionFacade.executeHql("delete from PlanViewSearchCondition where planViewInfoId = ? and attributeName = ?", planViewInfoId, condition);
        }
    }

    @Override
    public void updatePlanViewColumn(String planViewInfoId, String showColumnIds, TSUserDto curUser, String orgId) {
        if(!CommonUtil.isEmpty(planViewInfoId)){
            List<PlanViewColumnInfo> columnInfoList = getColumnInfoListByPlanViewInfoId_(planViewInfoId);

            if(!CommonUtil.isEmpty(columnInfoList)){
                PlanViewColumnInfo columnInfo = columnInfoList.get(0);
                columnInfo.setColumnId(showColumnIds);
                CommonInitUtil.initGLObjectForUpdate(columnInfo,curUser,orgId);
                saveOrUpdate(columnInfo);
            }else{
                PlanViewColumnInfo columnInfo = new PlanViewColumnInfo();
                columnInfo.setColumnId(showColumnIds);
                columnInfo.setPlanViewInfoId(planViewInfoId);
                CommonInitUtil.initGLObjectForCreate(columnInfo,curUser,orgId);
                save(columnInfo);
            }
        }
    }


    @Override
    public List<PlanViewColumnInfo> getColumnInfoListByPlanViewInfoId_(String planViewInfoId) {
        List<PlanViewColumnInfo> columnInfoList = sessionFacade.findHql("from PlanViewColumnInfo where planViewInfoId = ?", planViewInfoId);
        return columnInfoList;
    }

    @Override
    public List<PlanViewInfo> getPlanViewInfoByViewNameAndStatusAndCreateName(String planViewInfoName, String status, String createName) {
        StringBuffer sb = new StringBuffer();
        sb.append("from PlanViewInfo where 1=1 ");
        if(!CommonUtil.isEmpty(planViewInfoName)){
            sb.append(" and name = '"+planViewInfoName+"' ");
        }
        if(!CommonUtil.isEmpty(status)){
            sb.append(" and status = '"+status+"' ");
        }
        if(!CommonUtil.isEmpty(createName)){
            sb.append(" and createName = '"+createName+"' ");
        }
        List<PlanViewInfo> list = sessionFacade.findByQueryString(sb.toString());
        return list;
    }

    @Override
    public String saveAsNewView(String oldViewId, String newViewName, TSUserDto curUser, String orgId) {
        String pattern="[a-f0-9]{26,}";
        String viewId = "";
        //判断传入的参数是视图id
        if(!CommonUtil.isBlank(oldViewId)&&oldViewId.matches(pattern))
        {
            PlanViewInfo v = new PlanViewInfo();
            // 保存视图信息
            v.setIsDefault("false");
            v.setStatus(PlanviewConstant.PRIVATE_STATUS);
            v.setName(newViewName);
            CommonInitUtil.initGLObjectForCreate(v,curUser,orgId);
            sessionFacade.save(v);
            viewId = v.getId();

            // 保存视图设置条件
            List<PlanViewSetCondition> planViewSets= sessionFacade.executeQuery("from PlanViewSetCondition c where c.planViewInfoId=?",new Object[]{ oldViewId});
            if(!CommonUtil.isEmpty(planViewSets))
            {
                PlanViewSetCondition planViewSet1=planViewSets.get(0);
                PlanViewSetCondition planViewSet= new PlanViewSetCondition();

                planViewSet.setDepartmentId(planViewSet1.getDepartmentId());
                planViewSet.setTimeRange(planViewSet1.getTimeRange());
                planViewSet.setShowRange(planViewSet1.getShowRange());
                planViewSet.setPlanViewInfoId(v.getId());
                CommonInitUtil.initGLObjectForCreate(planViewSet,curUser,orgId);
                sessionFacade.save(planViewSet);
            }

            // 保存视图查询条件
            List<PlanViewSearchCondition> planViewSearchs= sessionFacade.executeQuery("from PlanViewSearchCondition c where c.planViewInfoId=?",new Object[]{ oldViewId});
            if(!CommonUtil.isEmpty(planViewSearchs))
            {
                for(PlanViewSearchCondition planViewSearch:planViewSearchs)
                {
                    PlanViewSearchCondition searchCondition=new PlanViewSearchCondition();
                    searchCondition.setAttributeCondition(planViewSearch.getAttributeCondition());
                    searchCondition.setAttributeName(planViewSearch.getAttributeName());
                    searchCondition.setAttributeValue(planViewSearch.getAttributeValue());
                    searchCondition.setPlanViewInfoId(v.getId());
                    CommonInitUtil.initGLObjectForCreate(searchCondition,curUser,orgId);
                    sessionFacade.save(searchCondition);
                }
            }

            // 保存视图项目范围
            List<PlanViewApplyProject> planViewApplyProjects= sessionFacade.executeQuery("from PlanViewApplyProject c where c.planViewInfoId=?",new Object[]{ oldViewId});
            if(!CommonUtil.isEmpty(planViewApplyProjects))
            {
                PlanViewApplyProject planViewApplyProject1 =planViewApplyProjects.get(0);
                PlanViewApplyProject planViewApplyProject=new PlanViewApplyProject();
                planViewApplyProject.setPlanViewInfoId(v.getId());
                planViewApplyProject.setProjectId(planViewApplyProject1.getProjectId());
                CommonInitUtil.initGLObjectForCreate(planViewApplyProject,curUser,orgId);
                sessionFacade.save(planViewApplyProject);
            }

            // 保存视图用户范围
            List<PlanViewApplyUser> planViewApplyUsers= sessionFacade.executeQuery("from PlanViewApplyUser c where c.planViewInfoId=?",new Object[]{ oldViewId});
            if(!CommonUtil.isEmpty(planViewApplyUsers))
            {
                PlanViewApplyUser planViewApplyUser1 = planViewApplyUsers.get(0);
                PlanViewApplyUser planViewApplyUser=new PlanViewApplyUser();
                planViewApplyUser.setPlanViewInfoId(v.getId());
                planViewApplyUser.setUserId(planViewApplyUser1.getUserId());
                CommonInitUtil.initGLObjectForCreate(planViewApplyUser,curUser,orgId);
                sessionFacade.save(planViewApplyUser);
            }
        }
        return viewId;
    }


    @Override
    public boolean getViewCountByStatusAndName(String name, String status, String id) {
        boolean result = false;
        String hql = "select count(*) from PlanViewInfo t where t.name = ? and t.status = ?";
        if(!CommonUtil.isEmpty(id)) {
            hql = hql + "and t.id not in('" + id + "')";
        }
        int count = (int) sessionFacade.getCountByHql(hql, name, status);
        if(count > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public String saveSetConditionByDeaprtment(String departmentId, String name, TSUserDto curUser, String orgId) {
        //保存视图信息
        PlanViewInfo planViewInfo = new PlanViewInfo();
        planViewInfo.setName(name);
        planViewInfo.setIsDefault("false");
        planViewInfo.setStatus("PRIVATE");
        CommonInitUtil.initGLObjectForCreate(planViewInfo,curUser,orgId);
        sessionFacade.save(planViewInfo);

        //保存视图设置部门
        PlanViewSetCondition planViewSet = new PlanViewSetCondition();
        planViewSet.setPlanViewInfoId(planViewInfo.getId());
        planViewSet.setDepartmentId(departmentId);
        CommonInitUtil.initGLObjectForCreate(planViewSet,curUser,orgId);
        sessionFacade.save(planViewSet);

        //保存视图查询条件
       /* PlanViewSearchCondition planViewSearch = new PlanViewSearchCondition();
        planViewSearch.setPlanViewInfoId(planViewInfo.getId());
        planViewSearch.setAttributeName("departId");
        planViewSearch.setAttributeCondition("in");
        planViewSearch.setAttributeValue(departmentId);
        CommonUtil.glObjectSet(planViewSearch);
        sessionFacade.save(planViewSearch);*/
        return planViewInfo.getId();
    }

    @Override
    public void saveColumnInfo(String planViewInfoId, TSUserDto curUser, String orgId) {
        PlanViewColumnInfo info = new PlanViewColumnInfo();
        String columnId = PlanviewConstant.LINK_COLUMN;
        info.setPlanViewInfoId(planViewInfoId);
        info.setColumnId(columnId);
        CommonInitUtil.initGLObjectForCreate(info,curUser,orgId);
        save(info);
    }

    @Override
    public PlanViewSetCondition getBeanByPlanViewInfoId(String viewId) {
        Object[] params = new Object[] {viewId};

        List<PlanViewSetCondition> executeQuery = sessionFacade.executeQuery(
                "from PlanViewSetCondition c where 1=1 and c.planViewInfoId=? ", params);
        if (CommonUtil.isEmpty(executeQuery)) {
            return null;
        }
        else {
            return executeQuery.get(0);
        }
    }


    @Override
    public void saveCustomView(PlanViewInfo info, List<PlanViewSetCondition> conditionList, String projectId, TSUserDto curUser, String orgId) {
        String tempViewId = info.getId();
        info.setId(null);
        sessionFacade.save(info);
        if(!CommonUtil.isEmpty(conditionList)){
            List<PlanViewSetCondition>  resConditionList = new ArrayList<PlanViewSetCondition>();
            for(PlanViewSetCondition con : conditionList){
                con.setPlanViewInfoId(info.getId());
                CommonInitUtil.initGLObjectForCreate(con,curUser,orgId);
                resConditionList.add(con);
            }
            batchSave(resConditionList);
        }

        UserPlanViewProject userPlan = new UserPlanViewProject();
        userPlan.setPlanViewInfoId(info.getId());
        userPlan.setProjectId(projectId);
        userPlan.setUserId(curUser.getId());
        CommonInitUtil.initGLObjectForCreate(userPlan,curUser,orgId);
        save(userPlan);
    }


    @Override
    public List<PlanViewInfo> getViewList(String projectId,TSUserDto userDto) {
        List<PlanViewInfo> result = new ArrayList<PlanViewInfo>();
        Map<String, TSDepartDto> departMap = new HashMap<String, TSDepartDto>();
        List<TSDepartDto> departList = departService.getAllDepart();
        for(TSDepartDto d : departList) {
            departMap.put(d.getId(), d);
        }
        Map<String, TSUserDto> userMap = userService.getAllUserIdsMap();

        result = constructionPlanViewTree(projectId, PlanviewConstant.MANAGEMENT,userDto);
        for(PlanViewInfo v : result) {
            String userId = v.getPublishPerson();
            String deptId = v.getPublishDept();
            TSUserDto user = userMap.get(userId);
            TSDepartDto depart = departMap.get(deptId);
            if(!CommonUtil.isEmpty(depart)) {
                v.setPublishDeptName(depart.getDepartname());
            }
            if(!CommonUtil.isEmpty(user)) {
                v.setPublishPersonName(user.getRealName()+"-"+user.getUserName());
            }
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<PlanViewInfo> constructionPlanViewTree(String projectId, String type,TSUserDto userDto) {
        String currentUserId = userDto.getId();
        List<PlanViewInfo> resultList = new ArrayList<PlanViewInfo>();
        if(!PlanviewConstant.MANAGEMENT.equals(type)) {
            //查询公共默认视图
            String hql = "from PlanViewInfo where status = ? and isDefault = 'true'  order by publishTime desc";
            List<PlanViewInfo> list = sessionFacade.findHql(hql, PlanviewConstant.PUBLIC_STATUS);
            resultList.addAll(list);
            //公共视图
            String hql2 = "from PlanViewInfo where id in(select planViewInfoId from PlanViewApplyProject where projectId = ? or projectId is null)\n" +
                    "and id in(select planViewInfoId from PlanViewApplyUser where userId = ? or userId is null) and status = ? and isDefault = ? \n" +
                    "order by publishTime desc";
            List<PlanViewInfo> list2 = sessionFacade.findHql(hql2, projectId, currentUserId, PlanviewConstant.PUBLIC_STATUS, "false");
            resultList.addAll(list2);
            //查询私有视图
            String hql3 = "from PlanViewInfo where status = ? and createBy = ? order by createTime desc";
            List<PlanViewInfo> privatelist = sessionFacade.findHql(hql3, PlanviewConstant.PRIVATE_STATUS, currentUserId);
            resultList.addAll(privatelist);
        } else {
            //管理列表公共视图
            String sql = "select * from (select id,createBy,name,status,publishPerson,publishDept,createTime from planview_info t where t.createby = '"+ currentUserId +"'" +
                    " union" +
                    " select id,createBy,name,status,publishPerson,publishDept,createTime from planview_info t where t.id in(select PLANVIEWINFOID from PLANVIEW_APPLY_USER where USERID = '"+ currentUserId +"')) " +
                    " order by CREATETIME desc";
            List<Map<String,Object>> listForManage  = sessionFacade.findForJdbc(sql);
            if(!CommonUtil.isEmpty(listForManage)){
                for(Map<String,Object> map : listForManage){
                    PlanViewInfo info = new PlanViewInfo();
                    info.setId(!CommonUtil.isEmpty(String.valueOf(map.get("ID"))) ? String.valueOf(map.get("ID")):"");
                    info.setCreateBy(!CommonUtil.isEmpty(String.valueOf(map.get("CREATEBY"))) ? String.valueOf(map.get("CREATEBY")):"");
                    info.setName(!CommonUtil.isEmpty(String.valueOf(map.get("NAME"))) ? String.valueOf(map.get("NAME")):"");
                    info.setStatus(!CommonUtil.isEmpty(String.valueOf(map.get("STATUS"))) ? String.valueOf(map.get("STATUS")):"");
                    info.setPublishPerson(!CommonUtil.isEmpty(String.valueOf(map.get("PUBLISHPERSON"))) ? String.valueOf(map.get("PUBLISHPERSON")):"");
                    info.setPublishDept(!CommonUtil.isEmpty(String.valueOf(map.get("PUBLISHDEPT"))) ? String.valueOf(map.get("PUBLISHDEPT")):"");
                    resultList.add(info);
                }
            }
//            resultList.addAll(listForManage);

        }
        return resultList;
    }

    @Override
    public List<PlanViewSetCondition> getSetConditionByPlanView(String planViewInfoId) {
        List<PlanViewSetCondition> list = new ArrayList<PlanViewSetCondition>();

        String hql = "from PlanViewSetCondition where planViewInfoId = ?";
        list = sessionFacade.findHql(hql, planViewInfoId);
        return list;
    }

    @Override
    public void publishView(String planViewInfoId, String projectIds, String userIds, String name, TSUserDto curUser, String orgId) {
        //保存指定项目
        if(!CommonUtil.isEmpty(projectIds)) {
            String[] projectIdArr = projectIds.split(",");
            for(String projectId : projectIdArr) {
                PlanViewApplyProject applyProject = new PlanViewApplyProject();
                applyProject.setPlanViewInfoId(planViewInfoId);
                applyProject.setProjectId(projectId);
                CommonInitUtil.initGLObjectForCreate(applyProject,curUser,orgId);
                sessionFacade.save(applyProject);
            }
        }
        else {
            PlanViewApplyProject applyProject = new PlanViewApplyProject();
            applyProject.setPlanViewInfoId(planViewInfoId);
            CommonInitUtil.initGLObjectForCreate(applyProject,curUser,orgId);
            sessionFacade.save(applyProject);
        }

        //保存指定用户
        if(!CommonUtil.isEmpty(userIds)) {
            String[] userIdArr = userIds.split(",");
            for(String userId : userIdArr) {
                PlanViewApplyUser applyUser = new PlanViewApplyUser();
                applyUser.setPlanViewInfoId(planViewInfoId);
                applyUser.setUserId(userId);
                CommonInitUtil.initGLObjectForCreate(applyUser,curUser,orgId);
                sessionFacade.save(applyUser);
            }
        }
        else {
            PlanViewApplyUser applyUser = new PlanViewApplyUser();
            applyUser.setPlanViewInfoId(planViewInfoId);
            CommonInitUtil.initGLObjectForCreate(applyUser,curUser,orgId);
            sessionFacade.save(applyUser);
        }
        //更新视图状态,名称
        PlanViewInfo v = getEntity(PlanViewInfo.class, planViewInfoId);
        CommonInitUtil.initGLObjectForUpdate(v,curUser,orgId);
        v.setName(name);
        v.setStatus(PlanviewConstant.PUBLIC_STATUS);
        v.setPublishPerson(curUser.getId());
        v.setPublishTime(new Date());
     //   List<TSDepartDto> departDtos = (List<TSDepartDto>)departService.getTSDepartById(appKey,curUser.getId());
        Map<String, List<TSDepartDto>> depMap = departService.getAllTSDepartByCache(appKey,"");
        List<TSDepartDto> departDtos = depMap.get(curUser.getId());
        String departmentIds = "";
        if(!CommonUtil.isEmpty(departDtos)){
            for(TSDepartDto dto : departDtos){
                if(CommonUtil.isEmpty(departmentIds)){
                    departmentIds = dto.getId();
                }
                else{
                    departmentIds =departmentIds + ","+ dto.getId();
                }
            }
        }
        v.setPublishDept(departmentIds);
        sessionFacade.saveOrUpdate(v);
    }

    @Override
    public void cancelPublishView(String id, String status) {
        //更新视图状态,名称
        String hql = "update PlanViewInfo t set t.status = ?, t.publishPerson = '', t.publishDept = '', t.publishTime = '' where t.id = ?";
        sessionFacade.executeHql(hql, status, id);
        //删除关联的用户和项目信息
        sessionFacade.executeHql("delete from PlanViewApplyProject where planViewInfoId = ?", id);
        sessionFacade.executeHql("delete from PlanViewApplyUser where planViewInfoId = ?", id);
    }
}
