package com.glaway.ids.project.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.ResourceUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.config.service.RepFileLifeCycleAuthConfigServiceI;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.project.plan.entity.PlanFeedBack;
import com.glaway.ids.project.plan.service.PlanFeedBackServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: PlanFeedBackServiceImpl
 * @Date: 2019/10/16-14:43
 * @since
 */
@Service("planFeedBackService")
@Transactional
public class PlanFeedBackServiceImpl extends CommonServiceImpl implements PlanFeedBackServiceI {

    private static final SystemLog LOG = BaseLogFactory.getSystemLog(PlanFeedBackServiceImpl.class);

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private FeignUserService userService;

    @Autowired
    private PlanServiceI planService;

    @Autowired
    private RepFileLifeCycleAuthConfigServiceI repFileLifeCycleAuthConfigService;

    @Autowired
    private FeignRepService repService;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public List<PlanFeedBack> getPlanFeedBack() {
        List<PlanFeedBack> list = sessionFacade.findByQueryString("from PlanFeedBack");
        return list;
    }

    @Override
    public Double calculateProgressRate(String status, int deliveryCount, Map<String, Integer> deliveryNumber) {
        List<PlanFeedBack> planFeedBacks = getPlanFeedBack();
        Map<String, String> params = planFeedBacks.stream().collect(Collectors.toMap(PlanFeedBack::getLifeCycleStatus,PlanFeedBack::getWeightPercent));
        return calculateWeight(status,deliveryCount,deliveryNumber,params);
    }

    @Override
    public int calculateWeightByStatus(String status) {
        int weigth = 0;
        List<PlanFeedBack> planFeedBacks = getPlanFeedBack();
        for (PlanFeedBack planFeedBack : planFeedBacks) {
            weigth += Integer.parseInt(planFeedBack.getWeightPercent());
            if (planFeedBack.getLifeCycleStatus().equals(status.toUpperCase())) {
                break;
            }
        }
        if (weigth > 100) {
            return 0;
        }else {
            return weigth;
        }
    }

    @Override
    public FeignJson saveFeedBack(Map<String,String> params, String userId) {
        FeignJson j = new FeignJson();
        TSUserDto userDto  =userService.getUserByUserId(userId);
        message = I18nUtil.getValue("com.glaway.ids.pm.project.task.saveSuccess");
        //获取数据库中计划反馈数据进行更新
        try {
            List<PlanFeedBack> list = getPlanFeedBack();
            for (PlanFeedBack planFeedBack : list) {
                planFeedBack.setWeightPercent(params.get(planFeedBack.getLifeCycleStatus()));
                planFeedBack.setUpdateBy(userId);
                planFeedBack.setUpdateName(userDto.getUserName());
                planFeedBack.setUpdateFullName(userDto.getRealName());
            }
            sessionFacade.batchUpdate(list);
            LOG.info(message);
        } catch (Exception e) {
            message = I18nUtil.getValue("com.glaway.ids.pm.rdtask.task.savefailure");
            j.setSuccess(false);
            LOG.error(message, e, "", "");
        } finally {
            j.setMsg(message);
        }
        return j;
    }

    @Override
    public FeignJson calculateWeightForFeign(Map<String, Object> map) {
        FeignJson j = new FeignJson();
        String status = map.get("status") == null ? "" : String.valueOf(map.get("status"));//计划生命周期状态
        String deliveryNum = map.get("deliveryNum") == null ? "" : String.valueOf(map.get("deliveryNum"));//交付物数量
        String nizhiNum = map.get("nizhiNum") == null ? "" : String.valueOf(map.get("nizhiNum"));//已上传数量
        String shenpiNum = map.get("shenpiNum") == null ? "" : String.valueOf(map.get("shenpiNum"));//提交审批数量
        String guidangNum = map.get("guidangNum") == null ? "" : String.valueOf(map.get("guidangNum"));//审批通过数量
        Map<String, String> params = new ObjectMapper().convertValue(map.get("infos"), new TypeReference<Map<String, String>>(){});

        int delivery = Integer.parseInt(deliveryNum);
        Map<String, Integer> number = new HashMap<>();
        number.put("nizhi",Integer.parseInt(nizhiNum));
        number.put("shenpi",Integer.parseInt(shenpiNum));
        number.put("guidang",Integer.parseInt(guidangNum));

        double percent = calculateWeight(status, delivery, number, params);
        j.setObj(percent%1 == 0 ? (int)percent : (double)percent);
        return j;
    }

    @Override
    public Double calculateWeight(String status, int delivery,  Map<String, Integer> number, Map<String, String> params) {
        double percent = (double) 0.0;
        //获取计划生命周期
        List<LifeCycleStatus> planLifeCycleStatus = planService.getPlanLifeCycleStatus();
        for (int i = 0; i < planLifeCycleStatus.size(); i++) {
            if (status.equals(planLifeCycleStatus.get(i).getName())) {
                if (status.equals("ORDERED") && Integer.parseInt(params.get(status)) > 0 && delivery != 0) { //执行中状态单独计算
                    //获取交付项生命周期
                    //TODO FD接口优化，获取排序后的数据
                   /* FeignJson fJson = repService.getLifeCycleListStr(ResourceUtil.getApplicationInformation().getAppKey(),new RepFileDto());
                    String  lifeCycleListStr = fJson.getObj().toString();
                    List<LifeCycleStatus> deliveryLifeCycleStatus = JSON.parseArray(lifeCycleListStr,LifeCycleStatus.class);
                    Collections.sort(deliveryLifeCycleStatus, new Comparator<LifeCycleStatus>() {
                        @Override
                        public int compare(LifeCycleStatus o1, LifeCycleStatus o2) {
                            return o1.getOrderNum().compareTo(o2.getOrderNum());
                        }
                    });*/
                    List<LifeCycleStatus> deliveryLifeCycleStatus = repFileLifeCycleAuthConfigService.queryLifeCycleStatusByPolicyName(CommonConstants.REPFILE_LIFECYCLE_POLICY_NAME);
                    int total = 0;
                    int deliveryNum = 0;//交付物占比
                    for (int k = 0; k < deliveryLifeCycleStatus.size(); k++) {
                        deliveryNum += (Integer.parseInt(params.get(deliveryLifeCycleStatus.get(k).getName())) + total) * number.get(deliveryLifeCycleStatus.get(k).getName());
                        total += Integer.parseInt(params.get(deliveryLifeCycleStatus.get(k).getName()));
                    }
                    int temp = (int) Math.round(((double)Integer.parseInt(params.get(planLifeCycleStatus.get(i).getName()))/delivery)* deliveryNum);
                    percent += temp%100 == 0 ? (int)temp/100 : (double)temp/100;
                } else {
                    percent += Integer.parseInt(params.get(planLifeCycleStatus.get(i).getName()));
                }
                break;
            } else {
                percent += Integer.parseInt(params.get(planLifeCycleStatus.get(i).getName()));
                continue;
            }
        }
        return percent;
    }

    @Override
    public String getWeightByStatus(String status) {
        String weight = "0";
        List<PlanFeedBack> list = sessionFacade.findHql("from PlanFeedBack where lifeCycleStatus = ?",status);
        if(!CommonUtil.isEmpty(list)){
            weight = list.get(0).getWeightPercent();
        }
        return weight;
    }
}
