package com.glaway.ids.dynamicform.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.OneToMany;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.dynamicform.DynamicFormDto;
import com.glaway.foundation.fdk.dev.dto.dynamicform.DynamicFormFieldDto;
import com.glaway.foundation.fdk.dev.service.FeignDynamicFormService;
import com.glaway.foundation.fdk.dev.service.activiti.FeignActivitiCommonService;
import com.glaway.ids.dynamicform.constants.DynamicFormConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaway.foundation.businessobject.constant.BusinessObjectConstant;
import com.glaway.foundation.businessobject.dao.BusinessObjectDao;
import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.foundation.businessobject.service.BusinessObjectServiceI;
import com.glaway.foundation.businessobject.service.impl.BusinessObjectServiceImpl;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.exception.NullParameterException;
import com.glaway.foundation.common.exception.system.BusinessObjectNotUniqueException;
import com.glaway.foundation.common.exception.system.NotFoundLifeCyclePolicyException;
import com.glaway.foundation.common.exception.system.PersistBusinessObjectFailException;
import com.glaway.foundation.common.fdexception.FdException;
import com.glaway.foundation.common.fdexception.FdExceptionPolicy;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.DBUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.common.util.UUIDGenerator;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.foundation.system.lifecycle.constant.LifeCycleConstant;
import com.glaway.foundation.system.lifecycle.constant.LifeCycleConstant.ReviseSequence;
import com.glaway.foundation.system.lifecycle.entity.LifeCyclePolicy;
import com.glaway.foundation.system.lifecycle.service.LifeCycleStatusServiceI;
import com.glaway.ids.dynamicform.service.DynamicFormCommonServiceI;
import com.glaway.ids.dynamicform.vo.DynamicFormCommonVo;
import com.glaway.ids.dynamicform.vo.DynamicFormFieldCommonVo;

/**
 * 动态表单接口实现
 * @author likaiyong
 * @version 2018年8月8日
 * @see DynamicFormCommonServiceImpl
 */
@Service("dynamicFormCommonService")
@Transactional
public class DynamicFormCommonServiceImpl extends BusinessObjectServiceImpl<DynamicFormDto> implements DynamicFormCommonServiceI{


    @Autowired
    private FeignDynamicFormService dynamicFormService;

    /**
     * sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private FeignActivitiCommonService activitiCommonService;

    /**
     * Jdbc服务接口
     */
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value(value="${spring.application.name}")
    private String appKey;

    /**
     * 生命周期状态service
     */
    @Autowired
    private LifeCycleStatusServiceI statusService;

    @Override
    public DynamicFormCommonVo generateDynamicForm(DynamicFormCommonVo vo, TSUserDto currentUser) {
        DynamicFormDto form = new DynamicFormDto();
        generateDynamicFormObject(vo.getFormCode(), vo.getFormName(), vo.getFieldList(), form);
        if(!CommonUtil.isEmpty(form)) {
            List<DynamicFormDto> dtoList = dynamicFormService.getDynamicFormByFormCode(form.getFormCode());
            if(!CommonUtil.isEmpty(dtoList)){
                for(DynamicFormDto df : dtoList){
                    if(!CommonUtil.isEmpty(df.getNewVersion()) && "Y".equals(df.getNewVersion())){
                        form.setId(df.getId());
                        break;
                    }
                }
            }
            FeignJson fj = dynamicFormService.saveOrUpdate(form);
            List<DynamicFormDto> dynamicList = dynamicFormService.getDynamicFormByFormCode(vo.getFormCode());
            if(!CommonUtil.isEmpty(dynamicList)){
                for(DynamicFormDto df : dynamicList){
                    if(!CommonUtil.isEmpty(df.getNewVersion()) &&"Y".equals(df.getNewVersion())){
                        form = df;
                        break;
                    }
                }
            }
            vo.setBizVersion(form.getBizVersion());
        }
        return vo;
    }


    /*  @Override
    public DynamicFormDto saveOrUpdateForm(DynamicFormDto form, TSUserDto currentUser) {
      String newFormContent = "";
        String newHtmlParse = "";
        String newFdFormParse = "";
        String formId = "";
        //动态表单json字符串
        Map<String, Object> jsonStr = new HashMap<String, Object>();
        //判断是否为修改操作，默认为否
        boolean updataAction = false;
        if (form.getFormContent() != null) {
//            formId = form.getId();
            newFdFormParse = form.getFdFormParse();
            jsonStr = JSONObject.parseObject(form.getFormContent(), Map.class);
            newFormContent = form.getFormContent();
            newHtmlParse = (String)jsonStr.get("template");
            //根据动态表单编码是否存在
            List<DynamicFormDto> list = dynamicFormService.getDynamicFormByFormCode(form.getFormCode());

            if (!CommonUtil.isEmpty(list)) {
                // 修改
                formId = list.get(0).getId();
                List<DynamicFormDto> formList = findBusinessObjectHistoryByBizId(DynamicFormDto.class, list.get(0).getBizId());
                DynamicFormDto formLatest = new DynamicFormDto();
                if(!CommonUtil.isEmpty(formList)) {
                    formLatest = formList.get(0);
                }
                form = reviseBusinessObjectForForm(formLatest, LifeCycleConstant.ReviseModel.MAJOR);
                form.setFormName(form.getFormName());
                updataAction = true;
            }
            else {
                // 获取动态表单html源码
                form.setFdFormParse(newFdFormParse);
                //动态表单html源码
                form.setFormParse(newHtmlParse);
                //动态表单content内容，即UEdit操作的文本内容
                form.setFormContent(newFormContent);
            }
            if (updataAction) {
                form.setFdFormParse(newFdFormParse);
                form.setFormParse(newHtmlParse);
                form.setFormContent(newFormContent);
                form.setNewVersion(DynamicFormConstants.FORM_NEWVERSION);//设置为新版本
                form.setIfLock(DynamicFormConstants.FORM_UNLOCK);//设置为未锁定
                form.setFromVersion(formId);//设置本次版本升级来源于那条数据，存放的是来源表单主键id
                dynamicFormService.saveOrUpdate(form);
   //             saveFormBySql(form, true,currentUser);
//                sessionFacade.saveOrUpdate(form);
            }
            else {
          //      form.setId(UUIDGenerator.generate());
                // 新增
              //  initBusinessObject(form);
                form.setNewVersion(DynamicFormConstants.FORM_NEWVERSION);//设置为新版本
                form.setIfLock(DynamicFormConstants.FORM_UNLOCK);//设置为未锁定
//                sessionFacade.save(form);
                dynamicFormService.saveOrUpdate(form);
               *//* saveFormBySql(form, false,currentUser);*//*
            }

            // 保存动态表单fields字段属性数据
            Map<String, Object> formFields = (Map<String, Object>)jsonStr.get("add_fields");


            DynamicFormFieldDto field = null;
            // 如果是修改逻辑，先删除历史表中的字段信息数据
            if(updataAction) {
                dynamicFormService.delDynamicFormFieldByDynamicFormId(form.getId());

            }
            // 重新赋值
            Map<String, String> nameMap = new HashMap<String, String>();
            // 遍历formFields，获取具体字段属性内容
            for (Map.Entry<String, Object> entry : formFields.entrySet()) {
                field = new DynamicFormFieldDto();
       //         initBusinessObject(field);
                Map<String, String> fieldInfo = (Map<String, String>)entry.getValue();
                if (fieldInfo.get("fieldname") != null) {
                    field.setFieldName(fieldInfo.get("fieldname").toString());
                }
                if (fieldInfo.get("fieldtitle") != null) {
                    field.setFieldTitle(fieldInfo.get("fieldtitle").toString());
                }
                if (fieldInfo.get("plugins") != null) {
                    field.setFieldPlugins(fieldInfo.get("plugins").toString());
                }
                if (fieldInfo.get("fieldtype") != null) {
                    field.setFieldTyle(fieldInfo.get("fieldtype"));
                }
                if (fieldInfo.get("fieldflow") != null) {
                    field.setFieldFlow(fieldInfo.get("fieldflow"));
                }
                nameMap.put(entry.getKey(), fieldInfo.get("fieldname"));
                field.setDynamicFormId(form.getId());
                field.setFieldCode(DynamicFormConstants.TABLE_PREFIX + form.getFormCode());
                // 逐个保存field信息
                field.setId(UUIDGenerator.generate());
                dynamicFormService.saveFormFieldBySql(currentUser.getId(),field);
            }
        }

        return form;
    }*/



    /**
     * 表单Sql保存
     * @param form
     */
    private void saveFormBySql(DynamicFormDto form, Boolean isUpdate, TSUserDto currentUser) {
        Connection conn = null;
        // 表单相关信息
        PreparedStatement insertForForm = null;
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
            if(!CommonUtil.isEmpty(form)) {
                String sqlForForm = "";
                if(!isUpdate) {
                    sqlForForm = " insert into DYNAMIC_FORM ("
                            + " ID, CREATETIME, CREATEBY, CREATEFULLNAME, CREATENAME,"
                            + " UPDATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME,"
                            + " FIRSTTIME, FIRSTBY, FIRSTFULLNAME, FIRSTNAME,"
                            + " POLICY_ID, BIZCURRENT, BIZID, BIZVERSION,"
                            + " SECURITYLEVEL, AVALIABLE,"
                            + " FORMCODE, FORMCONTENT, FORMNAME, FORMPARSE,"
                            + " FDFORMPARSE, NEWVERSION, IFLOCK, FROMVERSION) "
                            + " values (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                            + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
                            + " ?, ?, ?, ?, ?," + " ?, ?)";
                    insertForForm = conn.prepareStatement(sqlForForm);
                    insertForForm.setObject(1, form.getId());
                    insertForForm.setObject(2, createTime);
                    insertForForm.setObject(3, createBy);
                    insertForForm.setObject(4, createFullName);
                    insertForForm.setObject(5, createName);
                    insertForForm.setObject(6, updateTime);
                    insertForForm.setObject(7, updateBy);
                    insertForForm.setObject(8, updateFullName);
                    insertForForm.setObject(9, updateName);
                    insertForForm.setObject(10, firstTime);
                    insertForForm.setObject(11, firstBy);
                    insertForForm.setObject(12, firstFullName);
                    insertForForm.setObject(13, firstName);
                    insertForForm.setObject(14, form.getPolicy().getId());
                    insertForForm.setObject(15, form.getBizCurrent());
                    insertForForm.setObject(16, form.getBizId());
                    insertForForm.setObject(17, form.getBizVersion());
                    insertForForm.setObject(18, form.getSecurityLevel());
                    insertForForm.setObject(19, form.getAvaliable());
                    insertForForm.setObject(20, form.getFormCode());
                    insertForForm.setObject(21, form.getFormContent());
                    insertForForm.setObject(22, form.getFormName());
                    insertForForm.setObject(23, form.getFormParse());
                    insertForForm.setObject(24, form.getFdFormParse());
                    insertForForm.setObject(25, form.getNewVersion());
                    insertForForm.setObject(26, form.getIfLock());
                    insertForForm.setObject(27, form.getFromVersion());
                }
                else {
                    sqlForForm = "update DYNAMIC_FORM set FORMNAME = ?, FORMCONTENT = ?, FORMPARSE = ?, FDFORMPARSE = ?,"
                            + " NEWVERSION = ?, IFLOCK = ?, FROMVERSION = ? where ID = ?";
                    insertForForm = conn.prepareStatement(sqlForForm);
                    insertForForm.setObject(1, form.getFormName());
                    insertForForm.setObject(2, form.getFormContent());
                    insertForForm.setObject(3, form.getFormParse());
                    insertForForm.setObject(4, form.getFdFormParse());
                    insertForForm.setObject(5, form.getNewVersion());
                    insertForForm.setObject(6, form.getIfLock());
                    insertForForm.setObject(7, form.getFromVersion());
                    insertForForm.setObject(8, form.getId());

                }
                insertForForm.addBatch();
                insertForForm.executeBatch();
                conn.commit();
            }
        }
        catch (Exception ex) {
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
                DBUtil.closeConnection(null, insertForForm, conn);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 动态表单升版
     * @param formLatest
     * @param model
     * @return
     */
    private DynamicFormDto reviseBusinessObjectForForm(DynamicFormDto formLatest,
                                                    String model) {


        formLatest = reviseBusinessObject(
                formLatest, LifeCycleConstant.ReviseModel.MAJOR);
        movedBusinessObjectByOrder(formLatest, 0);
        modifyBusinessObject(formLatest);

        return formLatest;
    }


    /**
     * 修改businessObject的状态，但不保存<br>
     * @param businessObject
     * @param orderNum
     * @throws FdException
     * @see
     */
    private void movedBusinessObjectByOrder(BusinessObject businessObject, int orderNum)
            throws FdException {
        // 验证参数
        if (businessObject == null) {
            throw new NullParameterException();
        }

        // 是否有生命周期策略
        LifeCyclePolicy policy = businessObject.getPolicy();
        if (policy == null) {
            throw new NotFoundLifeCyclePolicyException();
        }
        // 找到序号和Name的键值对
        Map<Integer, String> statusMap = statusService.mappedLifeCycleStatusOrder(policy.getId());
        // 验证step的合理性
        if (!statusMap.containsKey(orderNum)) {
            throw new FdException("生命周期移动的步数超出范围。");
        }
        // 修改状态
        businessObject.setBizCurrent(statusMap.get(orderNum));
    }

    @Override
    public DynamicFormDto generateDynamicFormObject(String formCode, String formName,
                                                 List<DynamicFormFieldCommonVo> formVoList, DynamicFormDto form) {
        form.setFormCode(formCode);
        form.setFormName(formName);
        StringBuffer formParseBuffer = new StringBuffer();
        StringBuffer fdFormParseBuffer = new StringBuffer();
        JSONObject obj = new JSONObject();
        if(!CommonUtil.isEmpty(formVoList)) {
            formParseBuffer.append("<p>");
            JSONObject addFieldsObj = new JSONObject();
            JSONArray elementsArr = new JSONArray();
            int index = 0;
            for(DynamicFormFieldCommonVo vo : formVoList) {
                JSONObject tempObj = new JSONObject();
                //防止Json出现引用tempObj
                JSONObject tempObj2 = new JSONObject();
                JSONObject tempElementObj = new JSONObject();
                if(DynamicFormConstants.SELECT_USER.equals(vo.getFieldType())) {
                    tempObj.put("fieldname", vo.getFieldName());
                    tempObj.put("fieldtitle", vo.getFieldTitle());
                    tempObj.put("fieldtype", vo.getFieldType());
                    tempObj.put("fieldflow", vo.getFieldFlow());
                    tempObj.put("orgrequired", vo.getOrgrequired());
                    tempObj.put("plugins", vo.getFieldPlugins());
                    //是否支持多选
                    tempObj.put("fieldmulti", vo.getMultiple());
                    addFieldsObj.put(vo.getFieldName(), tempObj);

                    tempObj2 = (JSONObject)tempObj.clone();
                    tempElementObj.put("key", vo.getFieldName());
                    tempElementObj.put("value", tempObj2);
                    elementsArr.add(index, tempElementObj);
                    index++;

                    formParseBuffer.append("<input class=\"dynamic-selectuser easyui-searchbox\" title=\"" + vo.getFieldTitle() + "\" name=\"" + vo.getFieldName() + "\" ");
                    formParseBuffer.append("data-options=\"editable:false,multiple:"+ vo.getMultiple() +",searcher:function(){try {$(this).searchuser(); } catch (e) {}}\" leipiplugins=\"dynccommonuser\" ");
                    formParseBuffer.append("fieldname= \""+ vo.getFieldName() +"\" plugins=\"dynccommonuser\" fieldtype=\"selectuser\" ext1=\"undefined\"/>");
                    fdFormParseBuffer.append("<span id=\"glaway_input_readonly_"+ vo.getFieldName() +"\" class=\"glaway_search_box_width_330\">");
                    fdFormParseBuffer.append("<span class=\"search_title\" title=\""+ vo.getFieldTitle() +"\">"+ vo.getFieldTitle() +"&nbsp;&nbsp;</span>");
                    fdFormParseBuffer.append("<input name=\""+ vo.getFieldName() +"_condition\" type=\"hidden\" value=\"in\">");
                    fdFormParseBuffer.append("<input id=\""+ vo.getFieldName() +"\" name=\""+ vo.getFieldName() +"\" class=\" glaway-input \" type=\"text\" leipiplugins=\"dynccommonuser\" "
                            + "data-options=\"isTrim:true,required:true,hiddenInputId:'selectUserIdHidden_"+ vo.getFieldName() +"',multiple:" + vo.getMultiple()
                            + ",searcher:function(){ var result ; try{   result=$(this).searchuser().apply(this,arguments); }catch(e){} return result; },editable:false,prompt:'',maxLength:30\">");
                    fdFormParseBuffer.append("</span>");
                    fdFormParseBuffer.append("<input type=\"hidden\" id=\"selectUserIdHidden_"+ vo.getFieldName() +"\" name=\"selectUserIdHidden_"+ vo.getFieldName() +"\">"
                            + "<input type=\"hidden\" id=\"selectUserNameHidden_"+ vo.getFieldName() +"\" name=\"selectUserNameHidden_"+ vo.getFieldName() +"\">");
                    fdFormParseBuffer.append("<script>$(function() {});</script>");
                    fdFormParseBuffer.append("<script>$(function(){ debugger; var leaders=$('#"+ vo.getFieldName() +"').searchbox(); var options = $('#"+ vo.getFieldName() +"').searchbox('options');"
                            + "$('#"+ vo.getFieldName() +"').searchbox('textbox').one('focus',function(){ var temp =options.maxLength;if(temp)$(this).attr('maxlength', Number(temp));}"
                            + ").focusout(function (){ var isTrim= options.isTrim; if(isTrim){ $(this).val($.trim($(this).val()));}});});</script>");
                }
            }
            formParseBuffer.append("</p>");
            obj.put("fields", formVoList.size());
            obj.put("template", formParseBuffer.toString());
            obj.put("add_fields", addFieldsObj);
            JSONObject formMapObj = new JSONObject();
            formMapObj.put("elements", elementsArr);
            obj.put("form_map", formMapObj);
        }
        form.setFormContent(obj.toString());
        form.setFormParse(formParseBuffer.toString());
        form.setFdFormParse(fdFormParseBuffer.toString());
        return form;
    }

}
