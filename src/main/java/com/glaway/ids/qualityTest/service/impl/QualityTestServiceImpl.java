package com.glaway.ids.qualityTest.service.impl;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.qualityTest.dto.CofigDataGridTestDto;
import com.glaway.ids.qualityTest.entity.CofigDataGridTest;
import com.glaway.ids.qualityTest.entity.CofigFormTest;
import com.glaway.ids.qualityTest.service.QualityTestServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: QualityTestServiceImpl
 * @Date: 2019/10/30-18:31
 * @since
 */
@Service
@Transactional
public class QualityTestServiceImpl extends CommonServiceImpl implements QualityTestServiceI {
    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private FeignUserService userService;

    @Override
    public CofigFormTest getEntityByPlanId(String planId) {
        CofigFormTest cofigFormTest = new CofigFormTest();
        List<CofigFormTest> list = sessionFacade.findHql("from CofigFormTest t where t.planId=?",planId);
        if (!CommonUtil.isEmpty(list)) {
            cofigFormTest = list.get(0);
        }
        return cofigFormTest;
    }

    @Override
    public List<CofigDataGridTest> getCofigDataGridTestList(String planId) {
        List<CofigDataGridTest> list = sessionFacade.findHql("from CofigDataGridTest t where t.planId=?",planId);
        return list;
    }

    @Override
    public FeignJson addQualityDataGrid(String useObjectId) {
        FeignJson j = new FeignJson();
        try {
            CofigDataGridTest test = new CofigDataGridTest();
            test.setName("质量检查"+new Date());
            test.setRule(String.valueOf(System.currentTimeMillis()));
            List<CofigDataGridTest> lists = new ArrayList<>();
            String inStr = (String) redisService.getFromRedis("QualityDataGrid", useObjectId);
            if (!CommonUtil.isEmpty(inStr)) {
                lists = JSON.parseArray(inStr, CofigDataGridTest.class);
            }
            lists.add(test);
            String trs = JSON.toJSONString(lists);
            redisService.setToRedis("QualityDataGrid", useObjectId, trs);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @Override
    public FeignJson searchDataGrid(String useObjectId) {
        FeignJson j = new FeignJson();
        List<CofigDataGridTest> lists = new ArrayList<>();
        String inStr = (String) redisService.getFromRedis("QualityDataGrid", useObjectId);
        if (!CommonUtil.isEmpty(inStr)) {
            lists = JSON.parseArray(inStr, CofigDataGridTest.class);
        }
        List<CofigDataGridTestDto> dtos = new ArrayList<>();
        try {
            dtos = (List<CofigDataGridTestDto>) VDataUtil.getVDataByEntity(lists,CofigDataGridTestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",JSON.toJSONString(dtos));
        map.put("total",dtos.size());
        j.setAttributes(map);
        return j;
    }

    @Override
    public FeignJson saveQualityDataGrid(String planId, String useObjectId) {
        FeignJson j = new FeignJson();
        try {
            List<CofigDataGridTest> lists = new ArrayList<>();
            String inStr = (String) redisService.getFromRedis("QualityDataGrid", useObjectId);
            if (!CommonUtil.isEmpty(inStr)) {
                lists = JSON.parseArray(inStr, CofigDataGridTest.class);
            }
            if (lists.size() > 0) {
                for (CofigDataGridTest test : lists) {
                    test.setPlanId(planId);
                }
            }
            sessionFacade.batchSave(lists);
        } catch (Exception e) {
            j.setMsg(e.getMessage());
        } finally {
            //数据入库，清除redis数据
            redisService.deleteFromRedis("QualityDataGrid", useObjectId);
            return j;
        }
    }

    @Override
    public FeignJson getFormTestByPlanId(String planId) {
        FeignJson j = new FeignJson();
        CofigFormTest cofigFormTest = getEntityByPlanId(planId);
        String checkInfo = "";
        if (!CommonUtil.isEmpty(cofigFormTest)) {
            if (StringUtils.isNotEmpty(cofigFormTest.getCheckPerson())) {
                TSUserDto userDto = userService.getUserByUserId(cofigFormTest.getCheckPerson());
                checkInfo = userDto.getRealName() + "-" + userDto.getUserName();
                cofigFormTest.setCheckInfo(checkInfo);
            }
        }
        j.setObj(cofigFormTest);
        return j;
    }

    @Override
    public FeignJson searchList(String planId) {
        FeignJson j = new FeignJson();
        List<CofigDataGridTest> list = getCofigDataGridTestList(planId);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",JSON.toJSONString(list));
        map.put("total",list.size());
        j.setAttributes(map);
        return j;
    }

    @Override
    public FeignJson updateFormTest(CofigFormTest formTest) {
        FeignJson j = null;
        try {
            j = new FeignJson();
            if (StringUtils.isNotEmpty(formTest.getPlanId())) {
                CofigFormTest test = getEntityByPlanId(formTest.getPlanId());
                test.setApprove(formTest.getApprove());
                sessionFacade.updateEntitie(test);
            }
        } catch (Exception e) {
            j.setMsg(e.getMessage());
            j.setSuccess(false);
        } finally {
            return j;
        }
    }
}
