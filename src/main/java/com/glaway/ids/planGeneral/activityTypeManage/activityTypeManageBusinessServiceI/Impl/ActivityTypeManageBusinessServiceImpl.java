package com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageBusinessServiceI.Impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.glaway.ids.planGeneral.tabCombinationTemplate.businessServiceI.TabCbTemplateBusinessServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.system.serial.SerialNumberManager;
import com.glaway.ids.config.constant.SerialNumberConstants;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageBusinessServiceI.ActivityTypeManageBusinessServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.ActivityTypeManageEntityServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;

/**
 * Created by LHR on 2019/8/26.
 */
@Service("activityTypeManageBusinessService")
@Transactional
public class ActivityTypeManageBusinessServiceImpl extends CommonServiceImpl implements ActivityTypeManageBusinessServiceI {

    @Autowired
    private ActivityTypeManageEntityServiceI activityTypeManageEntityServiceI;

    @Autowired
    private TabCbTemplateBusinessServiceI tabCbTemplateBusinessService;


    @Override
    public FeignJson doAddActivityTypeManage(String userId, String name, String remark, String id) {
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.addSuccess");
        if (StringUtils.isNotEmpty(id)){
            message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.updateSuccess");
        }

        try {
//            if (StringUtil.isNotEmpty(remark)) {
//                remark = URLDecoder.decode(remark, "UTF-8");
//            }
//            if (StringUtil.isNotEmpty(name)) {
//                name = URLDecoder.decode(name, "UTF-8");
//            }

            Boolean flag = false;

            //判断是否重名
            List<ActivityTypeManage> list = activityTypeManageEntityServiceI.queryActivityTypeManageByName(name);
            if (StringUtils.isNotEmpty(id)) {
                //修改
                if (list.size() == 1) {
                    if (list.get(0).getId().equals(id)) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                }
                if (list.size() > 1) {
                    flag = true;
                }
            } else {
                if (!list.isEmpty()) {
                    flag = true;
                }
            }
            if (flag) {
                message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.nameTwo");
                j.setSuccess(false);
            } else {
                if (StringUtils.isNotEmpty(id)) {
                    //修改
                    ActivityTypeManage ac = activityTypeManageEntityServiceI.queryActivityTypeManageById(id);
                    ac.setRemark(remark);
                    ac.setUpdateTime(new Date());
                    ac.setName(name);
                    activityTypeManageEntityServiceI.updateActivityTypeManage(ac);
                    //同步页签组合模板
                    tabCbTemplateBusinessService.updateTabCbTemplateByActivity(ac);
                } else {
                    //新增
                    ActivityTypeManage ac = new ActivityTypeManage();
                    ac.setName(name);
                    ac.setCreateBy(userId);
                    ac.setRemark(remark);
                    ac.setCreateTime(new Date());
                    ac.setUpdateTime(new Date());
                    String no = SerialNumberManager.getSerialNumber(SerialNumberConstants.ACTIVITY_TYPE_CODE);
                    ac.setCode(no);
                    activityTypeManageEntityServiceI.saveActivityTypeManage(ac);
                }
                if (!I18nUtil.getValue("com.glaway.ids.activityTypeManage.addSuccess").equals(message) && !I18nUtil.getValue("com.glaway.ids.activityTypeManage.updateSuccess").equals(message)) {
                    j.setSuccess(false);
                }
            }

        } catch (Exception e) {
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.addError");
        } finally {
            j.setMsg(message);
            return j;
        }
    }
    
    @Override
    public FeignJson deleteBatchBeforeCheckDate(String ids) {
        FeignJson j = new FeignJson();
        String message = "";
        try {
            String[] idsArr = ids.split(",");
            List<String> idsList = Arrays.asList(idsArr);
            AtomicReference<Boolean> flag = new AtomicReference<>(true);
            idsList.forEach(it -> {
                ActivityTypeManage manage = activityTypeManageEntityServiceI.queryActivityTypeManageById(it);
                if (manage.getSource().equals("0")){
                    flag.set(false);
                }
            });
            if (!flag.get()){
                message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.deleteSourse");
            }
            j.setSuccess(flag.get());
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.deleteSourse");
            j.setMsg(message);
        } finally {
            j.setMsg(message);
            return j;
        }
    }
    
    @Override
    public FeignJson doDeleteBatch(String userId, String ids) {
        FeignJson j = new FeignJson();
        String message = I18nUtil.getValue("com.glaway.ids.common.config.deleteSuccess");
        try {
            String[] idsArr = ids.split(",");
            List<String> idsList = Arrays.asList(idsArr);
            AtomicReference<Boolean> flag = new AtomicReference<>(true);
            idsList.forEach(it -> {
                ActivityTypeManage manage = activityTypeManageEntityServiceI.queryActivityTypeManageById(it);
                if (manage.getSource().equals("0")){
                    flag.set(false);
                }
            });
//            if (!flag.get()){
//                message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.deleteSourse");
//            }
            if (flag.get()){
                idsList.forEach(it -> {
                    activityTypeManageEntityServiceI.deleteActivityTypeManage(it);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.common.config.deleteError");
            j.setMsg(message);
        } finally {
            j.setMsg(message);
            return j;
        }
    }

    @Override
    public FeignJson doStartOrStop(String userId, String ids, String status) {
        FeignJson j = new FeignJson();
        String message = "";
        if (status.equals("enable")) {
            message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.startSuccess");
        } else {
            message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.stopSuccess");
        }

        try {
            String[] idsArr = ids.split(",");
            List<String> idsList = Arrays.asList(idsArr);
            AtomicReference<Boolean> flag = new AtomicReference<>(true);
            idsList.forEach(it -> {
                ActivityTypeManage manage = activityTypeManageEntityServiceI.queryActivityTypeManageById(it);
                if (manage.getStatus().equals(status)){
                    flag.set(false);
                }
            });
            if (flag.get()){
                idsList.forEach(it -> {
                    activityTypeManageEntityServiceI.doStartOrStop(it, status);
                });
            }else {
                if (status.equals("enable")){
                    message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.enable");
                }else {
                    message = I18nUtil.getValue("com.glaway.ids.activityTypeManage.disable");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            message = I18nUtil.getValue("com.glaway.ids.common.config.deleteError");
            j.setMsg(message);
        } finally {
            j.setMsg(message);
            return j;
        }
    }
}
