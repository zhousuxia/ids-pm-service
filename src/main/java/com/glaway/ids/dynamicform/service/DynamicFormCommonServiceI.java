package com.glaway.ids.dynamicform.service;

import java.util.List;

import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.dto.dynamicform.DynamicFormDto;
import com.glaway.ids.dynamicform.vo.DynamicFormCommonVo;
import com.glaway.ids.dynamicform.vo.DynamicFormFieldCommonVo;

/**
 * 动态表单接口
 * @author likaiyong
 * @version 2018年8月8日
 * @see DynamicFormCommonServiceI
 */
public interface DynamicFormCommonServiceI extends BusinessObjectServiceI<DynamicFormDto>{

    
    /**
     * 生成动态表单
     * @param vo  动态表单vo
     * @return
     */
    DynamicFormCommonVo generateDynamicForm(DynamicFormCommonVo vo, TSUserDto currentUser);


    /**
     * 生成动态表单对象
     * @param formCode 动态表单编号
     * @param formName 动态表单名称
     * @param formVoList 动态表单列表
     * @return
     */
    DynamicFormDto generateDynamicFormObject(String formCode, String formName,
                                          List<DynamicFormFieldCommonVo> formVoList, DynamicFormDto form);


    /**
     * 保存/修改表单
     * @param form 动态表单
     */
   /* DynamicFormDto saveOrUpdateForm(DynamicFormDto form, TSUserDto currentUser);*/

    
}
