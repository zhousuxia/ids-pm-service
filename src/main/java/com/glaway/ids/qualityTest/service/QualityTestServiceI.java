package com.glaway.ids.qualityTest.service;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.qualityTest.entity.CofigDataGridTest;
import com.glaway.ids.qualityTest.entity.CofigFormTest;

import java.util.List;

/**
 * @Description: 〈一句话功能简述〉
 * @author:
 * @ClassName: QualityTestServiceI
 * @Date: 2019/10/30-18:30
 * @since
 */
public interface QualityTestServiceI extends CommonService {

    /**
     * 通过计划id获取质量检查项
     * @param planId
     * @return
     */
    CofigFormTest getEntityByPlanId(String planId);

    /**
     * 获取质量检查项列表
     * @param planId
     * @return
     */
    List<CofigDataGridTest> getCofigDataGridTestList(String planId);

    /**
     * 新增质量检查项
     * @param useObjectId
     * @return
     */
    FeignJson addQualityDataGrid(String useObjectId);

    /**
     * 质量检查项列表查询
     * @param useObjectId
     * @return
     */
    FeignJson searchDataGrid(String useObjectId);

    /**
     * 保存质量检查项
     * @param planId
     * @param useObjectId
     * @return
     */
    FeignJson saveQualityDataGrid(String planId, String useObjectId);

    /**
     * 获取质量检查单信息
     * @param planId
     * @return
     */
    FeignJson getFormTestByPlanId(String planId);

    /**
     * 计划执行列表加载
     * @param planId
     * @return
     */
    FeignJson searchList(String planId);

    /**
     * 更新质量检查项
     * @param formTest
     * @return
     */
    FeignJson updateFormTest(CofigFormTest formTest);
}
