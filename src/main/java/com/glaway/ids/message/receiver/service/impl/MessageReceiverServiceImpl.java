package com.glaway.ids.message.receiver.service.impl;


import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.message.constant.MessageConstants;
import com.glaway.ids.message.messageReceiverConfig.service.MessageReceiverConfigRemoteFeignServiceI;
import com.glaway.ids.message.receiver.service.MessageReceiverServiceI;
import com.glaway.ids.message.vo.MessageReceiverConfigDto;
import com.glaway.ids.project.plan.entity.Plan;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 获取消息接收人
 * 
 * @author blcao
 * @version 2016年6月15日
 * @see MessageReceiverServiceImpl
 * @since
 */
@Service("messageReceiverService")
@Transactional
public class MessageReceiverServiceImpl extends CommonServiceImpl implements MessageReceiverServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private MessageReceiverConfigRemoteFeignServiceI messageReceiverConfigRemoteFeignService;

    @Autowired
    private FeignUserService userService;

    @Override
    public List<TSUserDto> getProjectMembers(String projectId) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select bu1.id ID, bu1.username USERNAME, bu1.realname REALNAME");
        hqlBuffer.append(" from t_s_base_user bu1 ");
        hqlBuffer.append(" join fd_team_role_user u on bu1.id = u.userid ");
        hqlBuffer.append(" join pm_proj_team_link tl1 on tl1.teamid = u.teamid ");
        hqlBuffer.append(" where tl1.projectid = '" + projectId + "'");
        hqlBuffer.append(" union ");
        hqlBuffer.append(" select bu2.id ID, bu2.username USERNAME, bu2.realname REALNAME");
        hqlBuffer.append(" from t_s_base_user bu2 ");
        hqlBuffer.append(" join t_s_group_user gu on gu.userid = bu2.id ");
        hqlBuffer.append(" join fd_team_role_group g on g.groupid = gu.groupid ");
        hqlBuffer.append(" join pm_proj_team_link tl2 on tl2.teamid = g.teamid ");
        hqlBuffer.append(" where tl2.projectid = '" + projectId + "'");
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hqlBuffer.toString());
        return changeQueryResToTSUserDtos(objArrayList);
    }

    @Override
    public List<TSUserDto> getParentPlanOwner(String planId) {
        List<TSUserDto> list = new ArrayList<TSUserDto>();
        Plan pl = getEntity(Plan.class, planId);
        if (pl != null) {
            if (pl.getParentPlan() != null) {
                if (pl.getParentPlan().getOwnerInfo() != null) {
                    list.add(pl.getParentPlan().getOwnerInfo());
                }
            }
            else if (StringUtils.isNotEmpty(pl.getParentPlanId())) {
                Plan parent = getEntity(Plan.class, pl.getParentPlanId());
                if (parent != null) {
                    if (parent.getOwnerInfo() != null) {
                        list.add(parent.getOwnerInfo());
                    }
                    else if (StringUtils.isNotEmpty(parent.getOwner())) {
                        TSUserDto user = getEntity(TSUserDto.class, parent.getOwner());
                        if (user != null) {
                            list.add(user);
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<TSUserDto> getSonPlanOwner(String planId) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select distinct bu.id ID, bu.username USERNAME, bu.realname REALNAME");
        hqlBuffer.append(" from t_s_base_user bu");
        hqlBuffer.append(" join pm_plan pl on pl.owner = bu.id");
        hqlBuffer.append(" where pl.parentplanid =  '" + planId + "'");
        hqlBuffer.append(" and pl.avaliable = '1'");
        hqlBuffer.append(" and pl.bizcurrent not in ('EDITING', 'INVALID')");
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hqlBuffer.toString());
        return changeQueryResToTSUserDtos(objArrayList);
    }

    @Override
    public List<TSUserDto> getBackPlanOwner(String planId) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append(" select distinct bu.id ID, bu.username USERNAME, bu.realname REALNAME");
        hqlBuffer.append(" from t_s_base_user bu");
        hqlBuffer.append(" join pm_plan pl on pl.owner = bu.id");
        hqlBuffer.append(" join pm_prepose_plan pr on pr.planid = pl.id");
        hqlBuffer.append(" where pr.preposeplanid =  '" + planId + "'");
        hqlBuffer.append(" and pl.avaliable = '1'");
        hqlBuffer.append(" and pl.bizcurrent not in ('EDITING', 'INVALID')");
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hqlBuffer.toString());
        return changeQueryResToTSUserDtos(objArrayList);
    }

    @Override
    public List<TSUserDto> getMessageReceivers(String businessType, String businessId) {
        List<TSUserDto> messageReceivers = new ArrayList<TSUserDto>();
        if (StringUtils.isNotEmpty(businessType) && StringUtils.isNotEmpty(businessId)) {
            // 获取消息通知设置的信息
            List<MessageReceiverConfigDto> receiverConfigs = messageReceiverConfigRemoteFeignService.getMessageList();
            if (!CommonUtil.isEmpty(receiverConfigs)) {
            	MessageReceiverConfigDto receiverConfig = new MessageReceiverConfigDto();
				for(MessageReceiverConfigDto config : receiverConfigs){
					if(config.getName().contains(businessType)){
						receiverConfig = config;
						break;
					}
				}
        	 	if (StringUtils.isNotEmpty(receiverConfig.getReceiver())) {
                    String[] receiverArr = receiverConfig.getReceiver().split("\\+");
                    Map<String, String> receiverTypeMap = new HashMap<String, String>();
                    Map<String, TSUserDto> receiverMap = new HashMap<String, TSUserDto>();
                    for (String receiverType : receiverArr) {
                        receiverTypeMap.put(receiverType, receiverType);
                    }
                    // 若配置中设置了项目成员
                    if (StringUtils.isNotEmpty(receiverTypeMap.get(MessageConstants.MESSAGE_RECEIVER_PROJECT_MEMBER))
                    		|| StringUtils.isNotEmpty(receiverTypeMap.get(MessageConstants.MESSAGE_RECEIVER_PRODUCT_MEMBER))) {
                        List<TSUserDto> list = getProjectMembers(businessId);
                        for (TSUserDto user : list) {
                            receiverMap.put(user.getId(), user);
                        }
                    }
                    // 若配置中设置了计划负责人
                    if (StringUtils.isNotEmpty(receiverTypeMap.get(MessageConstants.MESSAGE_RECEIVER_PLAN_OWNER))) {
                        Plan plan = getEntity(Plan.class, businessId);
                        if (plan != null) {
                            if (plan.getOwnerInfo() != null) {
                                receiverMap.put(plan.getOwnerInfo().getId(), plan.getOwnerInfo());
                            }
                            else if (StringUtils.isNotEmpty(plan.getOwner())) {
                                TSUserDto user =  userService.getUserByUserId(plan.getOwner());
                                if (user != null) {
                                    receiverMap.put(user.getId(), user);
                                }
                            }
                        }
                    }
                    // 若配置中设置了后置计划负责人
                    if (StringUtils.isNotEmpty(receiverTypeMap.get(MessageConstants.MESSAGE_RECEIVER_BACK_OWNER))) {
                        List<TSUserDto> list = getBackPlanOwner(businessId);
                        for (TSUserDto user : list) {
                            receiverMap.put(user.getId(), user);
                        }
                    }
                    // 若配置中设置了子计划负责人
                    if (StringUtils.isNotEmpty(receiverTypeMap.get(MessageConstants.MESSAGE_RECEIVER_SON_OWNER))) {
                        List<TSUserDto> list = getSonPlanOwner(businessId);
                        for (TSUserDto user : list) {
                            receiverMap.put(user.getId(), user);
                        }
                    }
                    // 若配置中设置了父计划负责人
                    if (StringUtils.isNotEmpty(receiverTypeMap.get(MessageConstants.MESSAGE_RECEIVER_PARENT_OWNER))) {
                        List<TSUserDto> list = getParentPlanOwner(businessId);
                        for (TSUserDto user : list) {
                            receiverMap.put(user.getId(), user);
                        }
                    }
                    messageReceivers = new ArrayList<TSUserDto>(receiverMap.values());
                }
            }
        }
        return messageReceivers;
    }

    /**
     * 将原生SQL查询的结果转化为TSUserDto
     * 
     * @param objArrayList
     * @return
     * @see
     */
    private List<TSUserDto> changeQueryResToTSUserDtos(List<Map<String, Object>> objArrayList) {
        List<TSUserDto> list = new ArrayList<TSUserDto>();
        if (!CommonUtil.isEmpty(objArrayList)) {
            for (Map<String, Object> map : objArrayList) {
                String id = (String)map.get("ID");
                if (StringUtils.isNotEmpty(id)) {
                    TSUserDto user = new TSUserDto();
                    user.setId(id);
                    user.setUserName(StringUtils.isNotEmpty((String)map.get("USERNAME")) ? (String)map.get("USERNAME") : "");
                    user.setRealName(StringUtils.isNotEmpty((String)map.get("REALNAME")) ? (String)map.get("REALNAME") : "");
                    list.add(user);
                }
            }
        }
        return list;
    }

}
